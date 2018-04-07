/*
 * File: ApplicationComponent.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.components;

import android.content.Context;

import com.badi.common.di.ApplicationContext;
import com.badi.common.di.modules.ApplicationModule;
import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.common.utils.RxEventBus;
import com.badi.data.repository.LocationDataRepository;
import com.badi.data.repository.RoomDataRepository;
import com.badi.data.repository.SearchDataRepository;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.data.repository.remote.APIService;
import com.badi.data.repository.remote.AuthenticationHelper;
import com.badi.data.repository.remote.ServiceInterceptor;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.navigation.Navigator;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A component whose lifetime is the life of the application.
 */
@Singleton // Constraints this component to one-per-application or unscoped bindings.
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    void inject(BaseActivity baseActivity);
    void inject(ServiceInterceptor serviceInterceptor);
    void inject(AuthenticationHelper authenticationHelper);
    void inject(MainActivity mainActivity);

    //Exposed to sub-graphs.
    @ApplicationContext Context context();
    ThreadExecutor threadExecutor();
    PostExecutionThread postExecutionThread();
    APIService apiService();
    PreferencesHelper preferencesHelper();
    Navigator navigator();
    RxEventBus eventBus();
    SearchDataRepository searchDataRepository();
    RoomDataRepository roomDataRepository();
    LocationDataRepository locationDataRepository();
}
