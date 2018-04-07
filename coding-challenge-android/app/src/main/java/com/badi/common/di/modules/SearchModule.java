/*
 * File: SearchModule.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.modules;

import com.badi.common.di.PerActivity;
import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.repository.LocationDataRepository;
import com.badi.data.repository.SearchDataRepository;
import com.badi.domain.interactor.listroom.ResolveLocation;
import com.badi.domain.interactor.search.GetSearchesInPrefs;
import com.badi.domain.interactor.search.SetSearchInPrefs;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module that provides search related collaborators.
 */
@Module
public class SearchModule {

    @Provides
    @PerActivity
    GetSearchesInPrefs provideGetSearchesInPrefs(SearchDataRepository searchDataRepository, ThreadExecutor threadExecutor,
                                                    PostExecutionThread postExecutionThread) {
        return new GetSearchesInPrefs(searchDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SetSearchInPrefs provideSetSearchInPrefs(SearchDataRepository searchDataRepository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new SetSearchInPrefs(searchDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    ResolveLocation provideResolveLocationUseCase(LocationDataRepository locationDataRepository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new ResolveLocation(locationDataRepository, threadExecutor, postExecutionThread);
    }
}
