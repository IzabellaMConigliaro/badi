/*
 * File: ServiceInterceptor.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;

import com.badi.BadiApplication;
import com.badi.BuildConfig;
import com.badi.common.di.ApplicationContext;
import com.badi.common.utils.NetworkUtil;
import com.badi.data.repository.local.PreferencesHelper;

import java.io.IOException;
import java.util.Locale;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * The {@link ServiceInterceptor}  class adds the authorization header in the request if necessary.
 * If the request have the header no authentication at true it doesn't provide any authorization header for cases like:
 * (/create_with_email or /token requests)
 *
 * This class is also responsible to add the Data headers about the device in each request for the Data department.
 */

public class ServiceInterceptor implements Interceptor {

    @Inject PreferencesHelper preferencesHelper;

    private Context context;

    public ServiceInterceptor(@ApplicationContext Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Inject Retrofit service
        ((BadiApplication) context.getApplicationContext()).getComponent().inject(this);

        Request request = chain.request();

        if (request.header(APIService.HEADER_NO_AUTHENTICATION) == null) {
            String accessTokenHeader = NetworkUtil.buildHeaderAuthorization(preferencesHelper.getAccessToken());

            request = request.newBuilder()
                    .header(APIService.HEADER_AUTHORIZATION, accessTokenHeader)
                    .build();
        }

        request = manageDataHeaders(request);

        return chain.proceed(request);
    }

    private Request manageDataHeaders(Request request) {
        request = request.newBuilder()
                .header(APIService.HEADER_APP_VERSION, BuildConfig.VERSION_NAME)
                .header(APIService.HEADER_OS_VERSION, Build.VERSION.RELEASE)
                .header(APIService.HEADER_PLATFORM, "android")
                .header(APIService.HEADER_DEVICE_MODEL, Build.BRAND + " " + Build.MODEL)
                .header(APIService.HEADER_LANGUAGE, Locale.getDefault().getLanguage())
                .header(APIService.HEADER_CONNECTION, checkConnectionStatus())
                .header(APIService.HEADER_PUSH_ENABLED, checkNotificationStatus())
                .header(APIService.HEADER_LOCATION_ENABLED, checkLocationPermission())
                .build();
        return request;
    }

    private String checkConnectionStatus() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        String connection = "null";
        //If connected
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                connection = "wifi";
            else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                connection = "data";
        }

        return connection;
    }

    private String checkNotificationStatus() {
        // 1 = enabled, 2 = disabled, 3 = undefined
        return NotificationManagerCompat.from(context).areNotificationsEnabled() ? "1" : "2";
    }

    private String checkLocationPermission() {
        // 1 = enabled, 2 = disabled, 3 = undefined
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return "1";
        } else
            return "2";
    }
}
