/*
 * File: GetRoomInPrefs.java
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

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * getting the list room in process in shared preferences
 */

public class GetRoomInPrefs extends UseCase {

    private final RoomRepository roomRepository;

    @Inject
    public GetRoomInPrefs(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    /**
     * Get an {@link Observable} which will emit void once the room is saved completely
     */
    @Override
    public Observable buildUseCaseObservable() {
        return roomRepository.getRoomInPrefs();
    }
}
