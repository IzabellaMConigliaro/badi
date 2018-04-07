/*
 * File: ApplicationModule.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.modules;

import android.app.Application;
import android.content.Context;
import android.location.Geocoder;

import com.badi.BuildConfig;
import com.badi.common.di.ApplicationContext;
import com.badi.common.di.qualifiers.GoogleMapsApiKey;
import com.badi.common.executor.JobExecutor;
import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.common.executor.UIThread;
import com.badi.data.repository.LocationDataRepository;
import com.badi.data.repository.RoomDataRepository;
import com.badi.data.repository.SearchDataRepository;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.data.repository.remote.APIService;
import com.badi.data.repository.remote.GoogleMapsAPIService;
import com.badi.domain.repository.LocationRepository;
import com.badi.domain.repository.RoomRepository;
import com.badi.domain.repository.SearchRepository;

import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {
    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides @ApplicationContext @Singleton
    Context provideApplicationContext() {
        return application;
    }

    @Provides @Singleton
    ThreadExecutor provideThreadExecutor(JobExecutor jobExecutor) {
        return jobExecutor;
    }

    @Provides @Singleton
    PostExecutionThread providePostExecutionThread(UIThread uiThread) {
        return uiThread;
    }

    @Provides @Singleton
    PreferencesHelper providePreferencesHelper() {
        return new PreferencesHelper(provideApplicationContext());
    }

    @Provides @Singleton
    APIService provideApiService() {
        return APIService.Creator.newAPIService(provideApplicationContext());
    }

    @Provides @Singleton
    SearchRepository provideSearchRepository(SearchDataRepository searchDataRepository) {
        return searchDataRepository;
    }

    @Provides @Singleton
    RoomRepository provideRoomRepository(RoomDataRepository roomDataRepository) {
        return roomDataRepository;
    }

    @Provides
    Geocoder provideGeocoder(@ApplicationContext Context context) {
        return new Geocoder(context, Locale.getDefault());
    }

    @Provides @Singleton
    LocationRepository provideLocationRepository(LocationDataRepository locationDataRepository) {
        return locationDataRepository;
    }

    @Provides @Singleton
    @GoogleMapsApiKey
    String provideGoogleMapsApiKey() {
        return BuildConfig.GEOCODING_API_KEY;
    }


    @Provides @Singleton
    GoogleMapsAPIService provideGoogleMapsApiService() {
        return GoogleMapsAPIService.Creator.newGoogleMapsAPIService();
    }
}
