/*
 * File: SetRoomInPrefs.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.listroom;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.room.RoomDetail;
import com.badi.domain.interactor.CompletableUseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.observers.DisposableCompletableObserver;

/**
 * This class is an implementation of {@link CompletableUseCase} that represents a use case for
 * saving the list room in process in shared preferences
 */

public class SetRoomInPrefs extends CompletableUseCase {

    private final RoomRepository roomRepository;
    private RoomDetail room;

    @Inject
    public SetRoomInPrefs(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(RoomDetail room, DisposableCompletableObserver useCaseObserver) {
        this.room = room;
        super.execute(useCaseObserver);
    }

    /**
     * Get a {@link Completable} which will save the listing of the room in progress.
     */
    @Override
    protected Completable buildCompletableUseCaseObservable() {
        return roomRepository.setRoomInPrefs(room);
    }
}
