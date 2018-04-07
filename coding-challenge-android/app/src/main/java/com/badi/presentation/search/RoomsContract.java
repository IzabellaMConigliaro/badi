/*
 * File: RoomsContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.data.entity.room.Room;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.presentation.base.BaseView;

import java.util.List;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface RoomsContract {

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
         * Show a room list in the UI.
         *
         * @param roomList The List of {@link Room} that will be shown.
         */
        void showRoomList(List<Room> roomList);

        /**
         * Add rooms from pagination to current room list in the UI.
         *
         * @param roomList The List of {@link Room} that will be added to the current displayed rooms.
         */
        void addRoomsToRoomList(List<Room> roomList);
         
         /**
         * Deletes one requested room from the current room list in the UI.
         *
         * @param roomID The ID of {@link Room} that will be deleted of the current displayed rooms.
         */
        void deleteRequestedRoomFromRoomList(Integer roomID);

    }

    interface Presenter {

        void loadRoomsWithLocation(Location location, Filters filters);

        void loadRoomsWithCoordinates(Coordinates coordinates, Filters filters);

        void loadRoomsPaginated(Integer page);

        void requestRoom(Integer roomID);

        void onRefresh();

        void incrementOffsetRequestedRoom();

    }
}
