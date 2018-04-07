/*
 * File: RequestRoom.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.domain.interactor.CompletableUseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * This class is an implementation of {@link CompletableUseCase} that represents a use case for
 * requesting a room.
 */

public class RequestRoom extends CompletableUseCase {

    private final RoomRepository roomRepository;
    private Integer roomID;

    @Inject
    public RequestRoom(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                       PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(Integer roomID, DisposableCompletableObserver useCaseObserver) {
        this.roomID = roomID;
        super.execute(useCaseObserver);
    }

    /**
     * Get a {@link Completable} which will add the room as requested and return an empty observable when finishes.
     */
    @Override
    protected Completable buildCompletableUseCaseObservable() {
        return roomRepository.requestRoom(roomID);
    }
}
