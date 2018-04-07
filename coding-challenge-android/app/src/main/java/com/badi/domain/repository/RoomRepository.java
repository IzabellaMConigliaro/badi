/*
 * File: RoomRepository.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.repository;

import com.badi.data.entity.room.Match;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.room.RoomRental;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.search.SearchRooms;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Interface that represents a Repository for getting user related data.
 */
public interface RoomRepository {

    /**
     * Get an {@link Observable} which will emit a List of {@link Room} by a search {@link SearchRooms}.
     */
    Observable<List<Room>> getRoomsBySearch(SearchRooms request);

    /**
     * Get an {@link Observable} which will emit a List of {@link RoomMarker} by a search {@link SearchRooms}.
     */
    Observable<List<RoomMarker>> getRoomMarkersBySearch(SearchRooms request);

    /**
     * Get a {@link Completable} which will add the room as requested and return an empty observable when finishes.
     */
    Completable requestRoom(Integer roomID);

    /**
     * Get an {@link Observable} which will emit an object {@link RoomDetail} based on an id provided.
     */
    Observable<RoomDetail> getRoomDetailFromID(Integer roomID);

    /**
     * Get an {@link Observable} which will emit a List of {@link RoomDetail} of the user published rooms.
     */
    Observable<List<RoomDetail>> getUserPublishedRooms();

    /**
     * Get an {@link Observable} which will emit a List of {@link RoomDetail} of the user rooms.
     */
    Observable<List<RoomDetail>> getUserRooms();

    /**
     * Get an {@link Observable} which will create a room and emit an object {@link RoomDetail} when finishes.
     */
    Observable<RoomDetail> uploadRoom(RoomDetail room);

    /**
     * Get an {@link Observable} which will edit a room and emit an object {@link RoomDetail} when finishes.
     */
    Observable<RoomDetail> editRoom(RoomDetail room);

    /**
     * Get a {@link Completable} which will save the listing of the room in progress.
     */
    Completable setRoomInPrefs(RoomDetail room);

    /**
     * Get an {@link Observable} which will emit an object {@link RoomDetail} as a list room in process or null when finishes.
     */
    Observable<RoomDetail> getRoomInPrefs();

    /**
     * Get a {@link Completable} which will clear the listing of the room saved in prefs.
     */
    Completable clearRoomInPrefs();

    /**
     * Get an {@link Observable} which will emit a List of {@link Tenant} based on a query parameter
     */
    Observable<List<Tenant>> getUsersByName(String query);

    /**
     * Get a {@link Completable} which will delete the room.
     */
    Completable deleteRoom(Integer id);

    /**
     * Get a {@link Single} which will set the room with the given ID to publish and emit the edited object {@link RoomDetail}
     * when finishes.
     */
    Single<RoomDetail> publishRoomFromID(Integer roomID);

    /**
     * Get a {@link Single} which will set the room with the given an ID to unpublished and emit the edited object
     * {@link RoomDetail} when finishes.
     */
    Single<RoomDetail> unPublishRoomFromID(Integer roomID);

    /**
     * Get a {@link Observable} which will emit a List of {@link Tenant}.
     */
    Observable<List<Tenant>> getAcceptedRequestUsers(Integer roomID);

    /**
     * Get a {@link Completable} which will create a new room rental.
     */
    Completable createRentalRoomById(int roomId, RoomRental request);

    /**
     * Get a {@link Observable} which will emit a List of {@link Match}.
     */
    Observable<List<Match>> getMatchesForRoom(int roomID);

}
