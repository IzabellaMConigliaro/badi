/*
 * File: GoogleMapsApiService.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import com.badi.BuildConfig;
import com.badi.common.utils.MyGsonTypeAdapterFactory;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Api Service for retrieving data from Google Maps
 */
public interface GoogleMapsAPIService {

    @GET("/maps/api/geocode/json")
    Single<GoogleMapsGeocoderResponse> reverseGeocodeByCoordinates(@Query("key") String key, @Query("latlng") String latlng);

    @GET("/maps/api/geocode/json")
    Single<GoogleMapsGeocoderResponse> reverseGeocodeByPlaceId(@Query("key") String key, @Query("place_id") String placeId);

    /******** Helper class that sets up a new services *******/

    class Creator {

        public static Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .create();

        public static GoogleMapsAPIService newGoogleMapsAPIService() {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl("https://maps.googleapis.com/")
                    .client(okHttpClientBuilder.build())
                    .build();

            return retrofit.create(GoogleMapsAPIService.class);
        }
    }
}
