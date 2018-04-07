/*
 * File: ListRoomContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;


import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.user.Picture;
import com.badi.presentation.base.BaseView;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface ListRoomContract {

    interface View extends BaseView {

        /**
         * Show the info of the room at the initial state
         */
        void populateRoomInitialState();

        /**
         * Show the info of the room saved in shared preferences
         */
        void populateRoomSavedInPrefs(RoomDetail room);

        /**
         * Show room clear successful
         */
        void roomClearSuccessful();

        /**
         * Show room save successful
         */
        void roomSaveSuccessful();

        /**
         * Show progress bar loading image
         */
        void showLoadingImage();

        /**
         * Hide progress bar loading image
         */
        void hideLoadingImage();

        /**
         * Show image loaded in server
         */
        void showImageLoaded(Picture picture);

        /**
         * Show room upload successful
         */
        void roomUploadSuccessful(RoomDetail room);

        /**
         * Show room edit successful
         */
        void roomEditSuccessful(RoomDetail room);

        void sendListRoomSuccessEvent();

        void sendListRoomFailureEvent();
    }

    interface Presenter {

        void getRoomSavedInPrefs();

        void saveRoomInPrefs(RoomDetail room);

        void clearRoomSavedInPrefs();

        void uploadImage(String imageURI);

        void uploadRoom(RoomDetail room);

        void editRoom(RoomDetail room);

    }

}
