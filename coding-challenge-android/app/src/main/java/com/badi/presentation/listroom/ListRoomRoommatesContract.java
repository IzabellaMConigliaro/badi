/*
 * File: ListRoomRoommatesContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;


import com.badi.data.entity.room.Tenant;
import com.badi.presentation.base.BaseView;

import java.util.List;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface ListRoomRoommatesContract {

    interface View extends BaseView {

        /**
         * Show a the user list filtered in the UI.
         *
         * @param tenantList The List of {@link Tenant} that will be shown.
         */
        void showTenantListFiltered(List<Tenant> tenantList);

        /**
         * Show the empty user view in the UI.
         */
        void showEmptyTenantList();

    }

    interface Presenter {

        void searchUsers(String query);

    }

}
