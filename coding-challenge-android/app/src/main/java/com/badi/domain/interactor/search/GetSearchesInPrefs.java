/*
 * File: GetSearchesInPrefs.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.SearchRepository;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * saving a place searched in shared preferences
 */

public class GetSearchesInPrefs extends UseCase {

    private final SearchRepository searchRepository;

    @Inject
    public GetSearchesInPrefs(SearchRepository searchRepository, ThreadExecutor threadExecutor,
                              PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.searchRepository = searchRepository;
    }

    /**
     * Get an {@link Observable} which will emit a list of rooms based on coordinates.
     */
    @Override
    public Observable buildUseCaseObservable() {
        return searchRepository.getSearchesInPrefs();
    }
}
