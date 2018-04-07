/*
 * File: RoomComponent.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.components;

import com.badi.common.di.PerActivity;
import com.badi.common.di.modules.ActivityModule;
import com.badi.common.di.modules.RoomModule;
import com.badi.presentation.listroom.ListRoomActivity;
import com.badi.presentation.listroom.ListRoomPlaceActivity;
import com.badi.presentation.listroom.ListRoomRoommatesActivity;
import com.badi.presentation.roomdetail.RoomDetailActivity;
import com.badi.presentation.search.MapFragment;
import com.badi.presentation.search.RoomsListFragment;
import com.badi.presentation.search.SearchResultFragment;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Used for the room.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, RoomModule.class})
public interface RoomComponent extends ActivityComponent {
    void inject(SearchResultFragment searchResultFragment);
    void inject(RoomsListFragment roomsListFragment);
    void inject(MapFragment mapFragment);
    void inject(RoomDetailActivity roomDetailActivity);
    void inject(ListRoomActivity listRoomActivity);
    void inject(ListRoomPlaceActivity listRoomPlaceActivity);
    void inject(ListRoomRoommatesActivity listRoomRoommatesActivity);
}
