/*
 * File: MapContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.presentation.base.BaseView;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface MapContract {

    interface View extends BaseView {

        /**
         * Show an empty room list in the UI.
         */
        void showEmptyView();

        /**
         * Hide an empty room list in the UI.
         */
        void hideEmptyView();

        /**
         * Clear all markers in the map UI.
         */
        void clearMap();

        /**
         * Show a room list in the UI.
         *
         * @param roomList The List of {@link Room} that will be shown.
         */
        void showRoomList(List<Room> roomList);

        /**
         * Show a room marker list in the UI.
         *
         * @param markersList The List of {@link RoomMarker} that will be shown.
         */
        void showRoomMarkerList(List<RoomMarker> markersList);

        /**
         * Add rooms from pagination to current room list in the UI.
         *
         * @param roomList The List of {@link Room} that will be added to the current displayed rooms.
         */
        void addRoomsToRoomList(List<Room> roomList);

    }

    interface Presenter {

        void loadRoomsWithLocation(Location location, Filters filters);

        void loadRoomsWithCoordinates(Coordinates coordinates, Filters filters);

        void loadRoomsWithBounds(LatLngBounds latLngBounds, Filters filters);

        void loadRoomsPaginated();

        void incrementOffsetRequestedRoom();

    }
}
