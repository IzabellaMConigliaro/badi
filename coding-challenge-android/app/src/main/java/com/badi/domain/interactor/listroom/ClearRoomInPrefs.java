/*
 * File: ClearRoomInPrefs.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.listroom;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.domain.interactor.CompletableUseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Completable;

/**
 * This class is an implementation of {@link CompletableUseCase} that represents a use case for
 * clearing the list room in shared preferences
 */

public class ClearRoomInPrefs extends CompletableUseCase {

    private final RoomRepository roomRepository;

    @Inject
    public ClearRoomInPrefs(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                            PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    /**
     * Get a {@link Completable} which will clear the listing of the room saved in prefs.
     */
    @Override
    protected Completable buildCompletableUseCaseObservable() {
        return roomRepository.clearRoomInPrefs();
    }
}
