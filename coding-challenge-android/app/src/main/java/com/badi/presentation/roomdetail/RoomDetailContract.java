/*
 * File: RoomDetailContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;


import com.badi.data.entity.room.RoomDetail;
import com.badi.presentation.base.BaseView;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface RoomDetailContract {

    interface View extends BaseView {

        /**
         * Show room details in the UI.
         *
         * @param roomDetail The {@link RoomDetail} info that will be shown.
         */
        void showRoomDetails(RoomDetail roomDetail);

        /**
         * Change button room send request state in case of success
         */
        void changeRequestButton();

        /**
         * Set button room send request clickable again in case of error
         */
        void setRequestButtonClickable();

        /**
         * Show the booking tutorial
         */
        void showBookingTutorial(boolean isLister);
    }

    interface Presenter {

        void getRoomDetailFromID(Integer roomID);

        void requestRoom(Integer roomID);

        void getValidationStatus();

        void acceptInvitation(Integer invitationID);

        void rejectInvitation(Integer invitationID);

    }
}
