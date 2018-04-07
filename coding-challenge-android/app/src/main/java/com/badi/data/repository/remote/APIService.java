/*
 * File: APIService.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.content.Context;

import com.badi.BuildConfig;
import com.badi.common.di.ApplicationContext;
import com.badi.common.utils.MyGsonTypeAdapterFactory;
import com.badi.data.entity.AppInfo;
import com.badi.data.entity.DeviceInfo;
import com.badi.data.entity.DeviceInfoRequest;
import com.badi.data.entity.PictureUpload;
import com.badi.data.entity.TokenRequest;
import com.badi.data.entity.TokenResponse;
import com.badi.data.entity.room.Match;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.room.RoomRental;
import com.badi.data.entity.room.RoomUpload;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.search.SearchRooms;
import com.badi.data.entity.user.Picture;
import com.badi.data.entity.user.User;
import com.badi.data.entity.user.UserUpdate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * APIService for retrieving data from the network using Retrofit 2
 */
public interface APIService {

    String VERSION = "/v1";

    String HEADER_AUTHORIZATION = "Authorization";
    String HEADER_NO_AUTHENTICATION = "No-Authentication";
    String HEADER_NO_AUTHENTICATION_ACTIVE = "No-Authentication: true";

    //Data headers
    String HEADER_APP_VERSION = "Badi-App-Version";
    String HEADER_OS_VERSION = "Badi-Os-Version";
    String HEADER_PLATFORM = "Badi-Platform";
    String HEADER_DEVICE_MODEL = "Badi-Device-Model";
    String HEADER_LANGUAGE = "Badi-Language";
    String HEADER_CONNECTION = "Badi-Connection";
    String HEADER_PUSH_ENABLED = "Badi-Push-Enabled";
    String HEADER_LOCATION_ENABLED = "Badi-Location-Enabled";

    String GRANT_TYPE_PASSWORD = "password";
    String GRANT_TYPE_ASSERTION = "assertion";
    String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

    /**
     * ACCOUNT Endpoints
     */

    @POST(VERSION + "/account/register_device")
    Observable<DeviceInfo> registerDevice(@Body DeviceInfoRequest request);

    @DELETE(VERSION + "/account/device_token")
    Completable unRegisterDevice(@Query("token") String token);

    /**
     * OAUTH Endpoints
     */

    @POST("/oauth/token/")
    @Headers(HEADER_NO_AUTHENTICATION_ACTIVE)
    Observable<TokenResponse> refreshToken(@Body TokenRequest request);

    /**
     * APP Endpoints
     */

    @GET(VERSION + "/app/version/android")
    @Headers(HEADER_NO_AUTHENTICATION_ACTIVE)
    Observable<AppInfo> getAppVersion();

    /**
     * ME Endpoints
     */

    @GET(VERSION + "/me")
    Observable<User> getUser();

    @PUT(VERSION + "/me")
    Observable<User> updateUser(@Body UserUpdate request);

    @GET(VERSION + "/me/rooms")
    Observable<List<RoomDetail>> getUserRooms();

    @GET(VERSION + "/me/rooms?status[]=1")
    Observable<List<RoomDetail>> getUserPublishedRooms();

    /**
     * PICTURE Endpoints
     */

    @POST(VERSION + "/pictures/upload")
    Observable<Picture> pictureUpload(@Body PictureUpload request);

    /**
     * ROOMS Endpoints
     */

    @DELETE(VERSION + "/rooms/{id}")
    Completable deleteRoomById(@Path("id") int roomId);

    @GET(VERSION + "/rooms/{id}")
    Observable<RoomDetail> getRoomById(@Path("id") int roomId);

    @PUT(VERSION + "/rooms/{id}")
    Observable<RoomDetail> updateRoomById(@Path("id") int roomId, @Body RoomUpload request);

    @POST(VERSION + "/rooms")
    Observable<RoomDetail> createRoom(@Body RoomUpload request);

    @POST(VERSION + "/rooms/{id}/requests/create")
    Completable requestRoom(@Path("id") int roomId);

    @POST(VERSION + "/rooms/{id}/publish")
    Single<RoomDetail> publishRoomByID(@Path("id") int roomId);

    @POST(VERSION + "/rooms/{id}/unpublish")
    Single<RoomDetail> unPublishRoomByID(@Path("id") int roomId);

    @GET(VERSION + "/rooms/{id}/accepted_request_users")
    Observable<List<Tenant>> getAcceptedRequestUsers(@Path("id") int roomId);

    @DELETE(VERSION + "/rooms/{id}/favourite")
    Completable deleteFavouriteRoomById(@Path("id") int roomId);

    @POST(VERSION + "/rooms/{id}/favourite")
    Completable addFavouriteRoomById(@Path("id") int roomId);

    @POST(VERSION + "/rooms/{id}/rentals/create")
    Completable createRentalRoomById(@Path("id") int roomId, @Body RoomRental request);

    @GET(VERSION + "/rooms/{id}/matches")
    Observable<List<Match>> getMatchesForRoom(@Path("id") int roomId);

    /**
     * USERS Endpoints
     */

    @GET(VERSION + "/users/{id}")
    Observable<User> getUserById(@Path("id") int userId);

    /**
     * SEARCH Endpoints
     */

    @POST(VERSION + "/search/rooms")
    Observable<List<Room>> searchRooms(@Body SearchRooms request);

    @POST(VERSION + "/search/room_markers")
    Observable<List<RoomMarker>> searchRoomMarkers(@Body SearchRooms request);

    @GET(VERSION + "/search/users")
    Observable<List<Tenant>> searchUsers(@Query("q") String filter);



    /******** Helper class that sets up a new services *******/

    class Creator {

        public static Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        public static APIService newAPIService(@ApplicationContext Context applicationContext) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                    .addInterceptor(new ServiceInterceptor(applicationContext))
                    .authenticator(new AuthenticationHelper(applicationContext));

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }

            OkHttpClient okHttpClient = okHttpClientBuilder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(APIService.class);
        }
    }

}
