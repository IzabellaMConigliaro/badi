/*
 * File: MapPresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.search.GetRoomMarkersBySearch;
import com.badi.domain.interactor.search.GetRoomsBySearch;
import com.badi.domain.interactor.search.RequestRoom;
import com.badi.presentation.base.BasePresenter;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link MapPresenter} that controls communication between views and models of the presentation
 * layer of the Room.
 */
@PerActivity
public class MapPresenter extends BasePresenter<MapContract.View> implements MapContract.Presenter {

    private final GetRoomsBySearch getRoomsBySearchUseCase;
    private final GetRoomMarkersBySearch getRoomMarkersBySearchUseCase;
    private final RequestRoom requestRoomUseCase;

    //Initialize offset requested rooms
    private int offsetRequestedRooms = 0, page = 1, pageDots = 1;

    @Inject
    MapPresenter(GetRoomsBySearch getRoomsBySearch, GetRoomMarkersBySearch getRoomMarkersBySearch, RequestRoom requestRoom) {
        this.getRoomsBySearchUseCase = getRoomsBySearch;
        this.getRoomMarkersBySearchUseCase = getRoomMarkersBySearch;
        this.requestRoomUseCase = requestRoom;
    }

    @Override
    public void attachView(MapContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRoomsBySearchUseCase.clear();
        getRoomMarkersBySearchUseCase.clear();
        requestRoomUseCase.clear();
    }

    @Override
    public void loadRoomsWithLocation(Location location, Filters filters) {
        checkViewAttached();
        getView().hideEmptyView();
        getView().showLoading();
        getView().clearMap();
        offsetRequestedRooms = 0;
        page = 1;
        pageDots = 1;
        getRoomsBySearchUseCase.executeLocationSearch(location, filters, new GetRoomsByLocationObserver());
        getRoomMarkersBySearchUseCase.executeLocationSearch(location, filters, new GetRoomMarkersObserver());
    }

    @Override
    public void loadRoomsWithCoordinates(Coordinates coordinates, Filters filters) {
        checkViewAttached();
        getView().hideEmptyView();
        getView().showLoading();
        getView().clearMap();
        offsetRequestedRooms = 0;
        page = 1;
        pageDots = 1;
        getRoomsBySearchUseCase.executeCoordinatesSearch(coordinates, filters, new GetRoomsByCoordinatesObserver());
        getRoomMarkersBySearchUseCase.executeCoordinatesSearch(coordinates, filters,  new GetRoomMarkersObserver());
    }

    @Override
    public void loadRoomsWithBounds(LatLngBounds latLngBounds, Filters filters) {
        checkViewAttached();
        getView().hideEmptyView();
        getView().showLoading();
        getView().clearMap();
        offsetRequestedRooms = 0;
        page = 1;
        pageDots = 1;
        getRoomsBySearchUseCase.executeBoundsSearch(latLngBounds, new GetRoomsByBoundsObserver(), filters);
        getRoomMarkersBySearchUseCase.executeBoundsSearch(latLngBounds, new GetRoomMarkersObserver(), filters);
    }

    @Override
    public void loadRoomsPaginated() {
        checkViewAttached();
        getView().showLoading();
        page++;
        getRoomsBySearchUseCase.executePaginationSearch(page, offsetRequestedRooms, new GetRoomsPaginatedObserver());
        if (page % 4 == 0) {
            pageDots++;
            getRoomMarkersBySearchUseCase.executePaginationSearch(pageDots, offsetRequestedRooms, new GetRoomMarkersObserver());
        }
    }

    @Override
    public void incrementOffsetRequestedRoom() {
        offsetRequestedRooms++;
    }

    private final class GetRoomsByLocationObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the rooms by city data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            getView().hideLoading();
            if (rooms.isEmpty()) {
                getView().showEmptyView();
            } else {
                getView().showRoomList(rooms);
            }
        }
    }

    private final class GetRoomsByCoordinatesObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the rooms by coordinates data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            getView().hideLoading();
            if (rooms.isEmpty())
                getView().showEmptyView();
            else
                getView().showRoomList(rooms);
        }
    }

    private final class GetRoomsByBoundsObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the rooms by bounds data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            checkViewAttached();
            getView().hideLoading();
            if (rooms.isEmpty())
                getView().showEmptyView();
            else
                getView().showRoomList(rooms);
        }
    }

    private final class GetRoomsPaginatedObserver extends DefaultObserver<List<Room>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the rooms paginated data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<Room> rooms) {
            getView().hideLoading();
            getView().addRoomsToRoomList(rooms);
        }
    }

    private final class GetRoomMarkersObserver extends DefaultObserver<List<RoomMarker>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the rooms markers data process");
            getView().hideLoading();
            if (getView().context() != null) {
                getView().showError(
                        ErrorMessageFactory.create(getView().context(),
                                new DefaultErrorBundle((Exception) e).getException()));
            }
            getView().showRetry();
        }

        @Override
        public void onNext(List<RoomMarker> roomMarkers) {
            checkViewAttached();
            if (!roomMarkers.isEmpty())
                getView().showRoomMarkerList(roomMarkers);
        }
    }

}
