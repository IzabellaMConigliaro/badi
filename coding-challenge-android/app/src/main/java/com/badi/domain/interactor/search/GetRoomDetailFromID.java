/*
 * File: GetRoomDetailFromID.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.room.RoomDetail;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.RoomRepository;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * getting de room details of a particular room based on id.
 */

public class GetRoomDetailFromID extends UseCase {

    private final RoomRepository roomRepository;
    private Integer roomID;

    @Inject
    public GetRoomDetailFromID(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                               PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(Integer roomID, DisposableObserver useCaseObserver) {
        this.roomID = roomID;
        super.execute(useCaseObserver);
    }

    /**
     * Get an {@link Observable} which will emit an object {@link RoomDetail} based on an id provided.
     */
    @Override
    public Observable buildUseCaseObservable() {
        return roomRepository.getRoomDetailFromID(roomID);
    }
}
