/*
 * File: RoomsPresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.domain.interactor.DefaultCompletableObserver;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.search.RequestRoom;
import com.badi.domain.interactor.search.SearchRoomsByCoordinates;
import com.badi.domain.interactor.search.SearchRoomsByLocation;
import com.badi.presentation.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link RoomsPresenter} that controls communication between views and models of the presentation
 * layer of the Room.
 */
@PerActivity
public class RoomsPresenter extends BasePresenter<RoomsContract.View> implements RoomsContract.Presenter {

    private final SearchRoomsByCoordinates searchRoomsByCoordinatesUseCase;
    private final SearchRoomsByLocation searchRoomsByLocationUseCase;
    private final RequestRoom requestRoomUseCase;

    private boolean isSearchByLocation = true;

    //Initialize offset requested rooms
    private int offsetRequestedRooms = 0;
    private int roomRequestedID = 0;

    @Inject
    RoomsPresenter(SearchRoomsByCoordinates searchRoomsByCoordinates,
                   SearchRoomsByLocation searchRoomsByLocationUseCase,
                   RequestRoom requestRoom) {
        this.searchRoomsByCoordinatesUseCase = searchRoomsByCoordinates;
        this.searchRoomsByLocationUseCase = searchRoomsByLocationUseCase;
        this.requestRoomUseCase = requestRoom;
    }

    @Override
    public void attachView(RoomsContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        searchRoomsByCoordinatesUseCase.clear();
        searchRoomsByLocationUseCase.clear();
        requestRoomUseCase.clear();
    }

    @Override
    public void loadRoomsWithLocation(Location location, Filters filters) {
        isSearchByLocation = true;
        checkViewAttached();
        getView().hideEmptyView();
        getView().showLoading();
        searchRoomsByLocationUseCase.execute(location, filters, new SearchRoomsObserver());
    }

    @Override
    public void loadRoomsWithCoordinates(Coordinates coordinates, Filters filters) {
        isSearchByLocation = false;
        checkViewAttached();
        getView().hideEmptyView();
        getView().showLoading();
        searchRoomsByCoordinatesUseCase.execute(coordinates, filters, new SearchRoomsObserver());
    }

    @Override
    public void loadRoomsPaginated(Integer page) {
        checkViewAttached();
        if (!isSearchByLocation) {
            searchRoomsByCoordinatesUseCase.executePaginated(page, offsetRequestedRooms, new SearchPaginatedRoomsObserver());
        } else {
            searchRoomsByLocationUseCase.executePaginated(page, offsetRequestedRooms, new SearchRoomsObserver());
        }
    }

    @Override
    public void requestRoom(Integer roomID) {
        checkViewAttached();
        roomRequestedID = roomID;
        requestRoomUseCase.execute(roomRequestedID, new RequestSearchRoomObserver());
    }

    @Override
    public void onRefresh() {
        checkViewAttached();
        getView().hideEmptyView();
        offsetRequestedRooms = 0;
        if (!isSearchByLocation) {
            searchRoomsByCoordinatesUseCase.executePaginated(1, offsetRequestedRooms, new SearchRoomsObserver());
        } else {
            searchRoomsByLocationUseCase.executePaginated(1, offsetRequestedRooms, new SearchRoomsObserver());
        }
    }

    @Override
    public void incrementOffsetRequestedRoom() {
        offsetRequestedRooms++;
    }

    private final class SearchRoomsObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onError(Throwable exception) {
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) exception).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            offsetRequestedRooms = 0;
            getView().hideLoading();
            if (rooms.isEmpty()) {
                getView().showEmptyView();
            } else {
                getView().showRoomList(rooms);
            }
        }
    }

    private final class SearchPaginatedRoomsObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onError(Throwable exception) {
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) exception).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            getView().hideLoading();
            getView().addRoomsToRoomList(rooms);
        }
    }

    private final class RequestSearchRoomObserver extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            offsetRequestedRooms++;
            getView().hideLoading();
            getView().deleteRequestedRoomFromRoomList(roomRequestedID);
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in setting the room as requested data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }
    }

}
