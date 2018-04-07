/*
 * File: SetSearchInPrefs.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.PlaceAddress;
import com.badi.domain.interactor.CompletableUseCase;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.SearchRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * saving a place searched in shared preferences
 */

public class SetSearchInPrefs extends CompletableUseCase {

    private final SearchRepository searchRepository;
    private PlaceAddress search;

    @Inject
    public SetSearchInPrefs(SearchRepository searchRepository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.searchRepository = searchRepository;
    }

    public void execute(PlaceAddress search, DisposableCompletableObserver useCaseObserver) {
        this.search = search;
        super.execute(useCaseObserver);
    }

    /**
     * Get a {@link Completable} which will save the listing of the room in progress.
     */
    @Override
    protected Completable buildCompletableUseCaseObservable() {
        return searchRepository.setSearchInPrefs(search);
    }
}
