/*
 * File: RoomDataRepository.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository;

import com.badi.data.entity.room.Match;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.room.RoomRental;
import com.badi.data.entity.room.RoomUpload;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.search.SearchRooms;
import com.badi.data.entity.user.Picture;
import com.badi.data.entity.user.User;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.data.repository.remote.APIService;
import com.badi.domain.repository.RoomRepository;
import com.badi.presentation.listroom.ListRoomPicturesAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * {@link RoomRepository} implementation for retrieving room data.
 */
@Singleton
public class RoomDataRepository implements RoomRepository {

    private final APIService apiService;
    private final PreferencesHelper preferencesHelper;

    /**
     * Constructs a {@link RoomRepository}
     *
     * @param apiService {@link APIService}
     * @param preferencesHelper {@link PreferencesHelper}
     */
    @Inject
    public RoomDataRepository(APIService apiService, PreferencesHelper preferencesHelper) {
        this.apiService = apiService;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public Observable<List<Room>> getRoomsBySearch(SearchRooms request) {
        return apiService.searchRooms(request);
    }

    @Override
    public Observable<List<RoomMarker>> getRoomMarkersBySearch(SearchRooms request) {
        return apiService.searchRoomMarkers(request);
    }

    @Override
    public Completable requestRoom(Integer id) {
        return apiService.requestRoom(id);
    }

    @Override
    public Observable<RoomDetail> getRoomDetailFromID(Integer id) {
        return apiService.getRoomById(id);
    }

    @Override
    public Observable<List<RoomDetail>> getUserPublishedRooms() {
        return apiService.getUserPublishedRooms();
    }

    @Override
    public Observable<List<RoomDetail>> getUserRooms() {
        return apiService.getUserRooms();
    }

    @Override
    public Observable<RoomDetail> uploadRoom(RoomDetail room) {
        List<Integer> picturesIDs = new ArrayList<>();
        for (Picture picture : room.pictures()) {
            if (!picture.url().equals(ListRoomPicturesAdapter.URL_DEFAULT_IMAGE))
                picturesIDs.add(picture.id());
        }

        List<Integer> tenantsIds = new ArrayList<>();
        for (Tenant tenant : room.tenants()) {
            tenantsIds.add(tenant.id());
        }

        RoomUpload request = RoomUpload.builder()
                .setBedType(room.bedType())
                .setTitle(room.title())
                .setDescription(room.description())
                .setAddress(room.address())
                .setCity(room.city())
                .setCountry(room.country())
                .setPostalCode(room.postalCode())
                .setStreet(room.street())
                .setStreetNumber(room.streetNumber())
                .setAvailableFrom(room.availableFrom())
                .setAvailableTo(room.availableTo())
                .setLatitude(room.latitude())
                .setLongitude(room.longitude())
                .setUndefinedTenants(room.nonBadiTenants().undefinedTenants())
                .setMaleTenants(room.nonBadiTenants().maleTenants())
                .setFemaleTenants(room.nonBadiTenants().femaleTenants())
                .setMinDays(room.minDays())
                .setTenants(tenantsIds)
                .setAmenitiesAttributes(room.amenitiesAttributes())
                .setPictures(picturesIDs)
                .setPricesAttributes(room.pricesAttributes())
                .build();

        return apiService.createRoom(request);
    }

    @Override
    public Observable<RoomDetail> editRoom(RoomDetail room) {
        List<Integer> picturesIDs = new ArrayList<>();
        for (Picture picture : room.pictures()) {
            if (!picture.url().equals(ListRoomPicturesAdapter.URL_DEFAULT_IMAGE))
                picturesIDs.add(picture.id());
        }

        List<Integer> tenantsIds = new ArrayList<>();
        //Get all the ids of all the tenants minus our own which is at position 0
        for (Tenant tenant : room.tenants()) {
            tenantsIds.add(tenant.id());
        }
        if (!tenantsIds.isEmpty())
            tenantsIds.remove(0);

        RoomUpload request = RoomUpload.builder()
                .setStatus(room.status())
                .setBedType(room.bedType())
                .setTitle(room.title())
                .setDescription(room.description())
                .setAddress(room.address())
                .setStreet(room.street())
                .setStreetNumber(room.streetNumber())
                .setCity(room.city())
                .setCountry(room.country())
                .setPostalCode(room.postalCode())
                .setAvailableFrom(room.availableFrom())
                .setAvailableTo(room.availableTo())
                .setLatitude(room.latitude())
                .setLongitude(room.longitude())
                .setUndefinedTenants(room.nonBadiTenants().undefinedTenants())
                .setMaleTenants(room.nonBadiTenants().maleTenants())
                .setFemaleTenants(room.nonBadiTenants().femaleTenants())
                .setMinDays(room.minDays())
                .setTenants(tenantsIds)
                .setAmenitiesAttributes(room.amenitiesAttributes())
                .setPictures(picturesIDs)
                .setPricesAttributes(room.pricesAttributes())
                .build();

        return apiService.updateRoomById(room.id(), request);
    }

    @Override
    public Completable setRoomInPrefs(RoomDetail roomDetail) {
        if (roomDetail == null)
            preferencesHelper.saveUserListRoom(null);
        else
            preferencesHelper.saveUserListRoom(APIService.Creator.gson.toJson(roomDetail));
        return Completable.complete();
    }

    @Override
    public Observable<RoomDetail> getRoomInPrefs() {
        RoomDetail roomDetail = RoomDetail.initRoomDetail();
        if (preferencesHelper.getUserListRoom() != null) {
            // Delete preferences PREF_USER_LIST_ROOM if the model throws NullPointerException because the room model changes.
            try {
                roomDetail = APIService.Creator.gson.fromJson(preferencesHelper.getUserListRoom(), RoomDetail.class);
            } catch (NullPointerException exception) {
                preferencesHelper.saveUserListRoom(null);
            }
            if (roomDetail == null)
                roomDetail = RoomDetail.initRoomDetail();
        }
        return Observable.just(roomDetail);
    }

    @Override
    public Completable clearRoomInPrefs() {
        preferencesHelper.saveUserLister(true);
        preferencesHelper.saveUserRole(User.USER_LISTER);
        preferencesHelper.saveUserListRoom(null);
        return Completable.complete();
    }

    @Override
    public Observable<List<Tenant>> getUsersByName(String query) {
        return apiService.searchUsers(query);
    }

    @Override
    public Completable deleteRoom(Integer id) {
        return apiService.deleteRoomById(id);
    }

    @Override
    public Single<RoomDetail> publishRoomFromID(Integer roomID) {
        return apiService.publishRoomByID(roomID);
    }

    @Override
    public Single<RoomDetail> unPublishRoomFromID(Integer roomID) {
        return apiService.unPublishRoomByID(roomID);
    }

    @Override
    public Observable<List<Tenant>> getAcceptedRequestUsers(Integer roomId) {
        return apiService.getAcceptedRequestUsers(roomId);
    }

    @Override
    public Completable createRentalRoomById(int roomId, RoomRental request) {
        return apiService.createRentalRoomById(roomId, request);
    }

    @Override
    public Observable<List<Match>> getMatchesForRoom(int roomId) {
        return apiService.getMatchesForRoom(roomId);
    }

}
