/*
 * File: RoomModule.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.modules;

import com.badi.common.di.PerActivity;
import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.repository.LocationDataRepository;
import com.badi.data.repository.RoomDataRepository;
import com.badi.domain.interactor.listroom.ClearRoomInPrefs;
import com.badi.domain.interactor.listroom.EditRoom;
import com.badi.domain.interactor.listroom.GetRoomInPrefs;
import com.badi.domain.interactor.listroom.GetUsersByName;
import com.badi.domain.interactor.listroom.ResolveLocation;
import com.badi.domain.interactor.listroom.SetRoomInPrefs;
import com.badi.domain.interactor.listroom.UploadRoom;
import com.badi.domain.interactor.search.GetRoomDetailFromID;
import com.badi.domain.interactor.search.GetRoomMarkersBySearch;
import com.badi.domain.interactor.search.GetRoomsBySearch;
import com.badi.domain.interactor.search.RequestRoom;
import com.badi.domain.interactor.search.SearchRoomsByBounds;
import com.badi.domain.interactor.search.SearchRoomsByCoordinates;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger Module that provides search related collaborators.
 */
@Module
public class RoomModule {

    @Provides
    @PerActivity
    GetRoomsBySearch provideGetRoomsBySearch(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new GetRoomsBySearch(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SearchRoomsByCoordinates provideSearchRoomsByCoordinates(RoomDataRepository roomDataRepository, ThreadExecutor
            threadExecutor, PostExecutionThread postExecutionThread) {
        return new SearchRoomsByCoordinates(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SearchRoomsByBounds provideSearchRoomsByBounds(RoomDataRepository roomDataRepository, ThreadExecutor
            threadExecutor, PostExecutionThread postExecutionThread) {
        return new SearchRoomsByBounds(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetRoomMarkersBySearch provideGetRoomMarkersBySearch(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                         PostExecutionThread postExecutionThread) {
        return new GetRoomMarkersBySearch(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    RequestRoom provideRequestRoom(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread) {
        return new RequestRoom(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetRoomDetailFromID provideGetRoomDetailFromID(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                        PostExecutionThread postExecutionThread) {
        return new GetRoomDetailFromID(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    UploadRoom provideUploadRoomUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                        PostExecutionThread postExecutionThread) {
        return new UploadRoom(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    EditRoom provideEditRoomUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread) {
        return new EditRoom(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    ClearRoomInPrefs provideClearRoomInPrefsUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new ClearRoomInPrefs(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    SetRoomInPrefs provideSetRoomInPrefsUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                            PostExecutionThread postExecutionThread) {
        return new SetRoomInPrefs(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetRoomInPrefs provideGetRoomInPrefsUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread) {
        return new GetRoomInPrefs(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    GetUsersByName provideGetUsersByNameUseCase(RoomDataRepository roomDataRepository, ThreadExecutor threadExecutor,
                                                PostExecutionThread postExecutionThread) {
        return new GetUsersByName(roomDataRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    @PerActivity
    ResolveLocation provideResolveLocationUseCase(LocationDataRepository locationDataRepository, ThreadExecutor threadExecutor,
                                                  PostExecutionThread postExecutionThread) {
        return new ResolveLocation(locationDataRepository, threadExecutor, postExecutionThread);
    }
}
