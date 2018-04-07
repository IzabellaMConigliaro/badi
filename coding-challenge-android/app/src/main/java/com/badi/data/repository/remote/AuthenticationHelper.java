/*
 * File: AuthenticationHelper.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.content.Context;

import com.badi.BadiApplication;
import com.badi.common.di.ApplicationContext;
import com.badi.common.utils.NetworkUtil;
import com.badi.data.entity.APIError;
import com.badi.data.entity.TokenRequest;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.presentation.navigation.Navigator;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import okio.Buffer;
import okio.BufferedSource;

/**
 * This class responds to an Unauthorized error (401).
 * This class returns a request that includes an authorization header, or refuses to retry by returning null.
 */
public class AuthenticationHelper implements Authenticator {

    @Inject APIService apiService;
    @Inject PreferencesHelper preferencesHelper;
    @Inject Navigator navigator;

    private static final String ERROR_INVALID_TOKEN = "invalid_token";

    private Context context;
    private String newAccessToken;

    public AuthenticationHelper(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        // Inject Retrofit service
        ((BadiApplication) context.getApplicationContext()).getComponent().inject(this);

        String responseBodyString = cloneResponseBody(response);
        APIError apiError = APIService.Creator.gson.fromJson(responseBodyString, APIError.class);

        if (apiError.error().code().equals(ERROR_INVALID_TOKEN)) {

            newAccessToken = null;
            getNewAccessToken().subscribe(new DisposableObserver<String>() {
                @Override
                public void onComplete() {

                }

                @Override
                public void onError(Throwable e) {
                    newAccessToken = null;
                }

                @Override
                public void onNext(String accessToken) {
                    newAccessToken = NetworkUtil.buildHeaderAuthorization(accessToken);
                }
            });

            return response.request().newBuilder()
                    .header(APIService.HEADER_AUTHORIZATION, newAccessToken)
                    .build();
        }
        return null;
    }

    private Observable<String> getNewAccessToken() {
        TokenRequest request = TokenRequest.create(preferencesHelper.getRefreshToken(), APIService.GRANT_TYPE_REFRESH_TOKEN);
        return apiService.refreshToken(request)
                .map(tokenResponse -> {
                    preferencesHelper.saveAccessToken(tokenResponse.accessToken());
                    preferencesHelper.saveRefreshToken(tokenResponse.refreshToken());
                    preferencesHelper.saveTokenType(tokenResponse.tokenType());
                    preferencesHelper.saveUserId(tokenResponse.userId());
                    preferencesHelper.saveUserLister(tokenResponse.lister());
                    return tokenResponse.accessToken();
                });
    }

    private String cloneResponseBody(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();
        return buffer.clone().readString(Charset.forName("UTF-8"));
    }
}
