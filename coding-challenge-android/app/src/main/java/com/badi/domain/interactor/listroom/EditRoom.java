/*
 * File: EditRoom.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.listroom;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.data.entity.room.RoomDetail;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.RoomRepository;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;


/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * editing a room in the app.
 */
public class EditRoom extends UseCase {

    private final RoomRepository roomRepository;
    private RoomDetail room;

    @Inject
    public EditRoom(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                    PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void execute(RoomDetail room, DisposableObserver useCaseObserver) {
        this.room = removeAmenityTypeZero(room);
        super.execute(useCaseObserver);
    }

    @Override
    public Observable buildUseCaseObservable() {
        return roomRepository.editRoom(room);
    }

    private RoomDetail removeAmenityTypeZero(RoomDetail room) {
        //Remove "amenity_type": 0
        ArrayList<AmenitiesAttribute> amenitiesAttributes = new ArrayList<>(room.amenitiesAttributes());
        if (!amenitiesAttributes.isEmpty()) {
            for (AmenitiesAttribute attribute : room.amenitiesAttributes()) {
                if (attribute.amenityType() == 0) {
                    amenitiesAttributes.remove(attribute);
                }
            }
        }
        return room.withAmenitiesAttributes(amenitiesAttributes);
    }
}
