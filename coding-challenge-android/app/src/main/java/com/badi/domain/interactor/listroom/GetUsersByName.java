/*
 * File: GetUsersByName.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.listroom;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * requesting a list of users based on a query parameter
 */

public class GetUsersByName extends UseCase {

    private final RoomRepository roomRepository;
    private String query;

    @Inject
    public GetUsersByName(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(String query, DisposableObserver useCaseObserver) {
        this.query = query;
        super.execute(useCaseObserver);
    }

    /**
     * Get an {@link Observable} which will emit a list of users based on a query parameter given.
     */
    @Override
    public Observable buildUseCaseObservable() {
        return roomRepository.getUsersByName(query);
    }
}
