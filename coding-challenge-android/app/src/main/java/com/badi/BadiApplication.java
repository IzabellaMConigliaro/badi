/*
 * File: BadiApplication.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi;

import android.app.Application;
import android.content.Context;

import com.badi.common.di.HasComponent;
import com.badi.common.di.components.ApplicationComponent;
import com.badi.common.di.components.DaggerApplicationComponent;
import com.badi.common.di.modules.ApplicationModule;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Guillem Roca on 30/9/16.
 *
 * Android Main Application
 */
public class BadiApplication extends Application implements HasComponent<ApplicationComponent> {

    private ApplicationComponent applicationComponent;

    @Override public void onCreate() {
        super.onCreate();
        initializeInjector();
        initializeCalligraphy();
        initializeTimberLogger();
    }

    @Override
    public ApplicationComponent getComponent() {
        return applicationComponent;
    }

    public static BadiApplication get(Context context) {
        return (BadiApplication) context.getApplicationContext();
    }

    private void initializeInjector() {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    private void initializeTimberLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void initializeCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Nunito-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
