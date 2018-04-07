/*
 * File: GetRoomMarkersBySearch.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor.search;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.search.Bounds;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.CustomLatLng;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.data.entity.search.SearchRooms;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.RoomRepository;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import timber.log.Timber;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * retrieving the search room markers data
 */

public class GetRoomMarkersBySearch extends UseCase {

    private static final int ROOM_MARKERS_PER_PAGE = 120;

    private final RoomRepository roomRepository;
    private Location location;
    private Coordinates coordinates;
    private Bounds bounds;
    private Integer page, offset;
    private Filters filters;
    private String publishedAtDescendant = "published_at desc";
    private String sortByAscendant = "distance asc";

    @Inject
    public GetRoomMarkersBySearch(RoomRepository roomRepository, ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.roomRepository = roomRepository;
    }

    public void executeLocationSearch(Location location, Filters filters, DisposableObserver useCaseObserver) {
        this.location = location;
        this.bounds = null;
        this.coordinates = null;
        this.filters = filters;
        this.page = 1;
        this.offset = 0;
        super.execute(useCaseObserver);
    }

    public void executeCoordinatesSearch(Coordinates coordinates, Filters filters, DisposableObserver useCaseObserver) {
        this.location = null;
        this.bounds = null;
        this.coordinates = coordinates;
        this.filters = filters;
        this.page = 1;
        this.offset = 0;
        super.execute(useCaseObserver);
    }

    public void executeBoundsSearch(LatLngBounds latLngBounds, DisposableObserver useCaseObserver, Filters filters) {
        CustomLatLng ne = CustomLatLng.create(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude);
        CustomLatLng sw = CustomLatLng.create(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude);
        this.location = null;
        this.bounds = Bounds.create(ne, sw);
        this.coordinates = null;
        this.filters = filters;
        this.page = 1;
        this.offset = 0;
        super.execute(useCaseObserver);
    }

    public void executePaginationSearch(Integer page, Integer offset, DisposableObserver useCaseObserver) {
        this.page = page;
        this.offset = offset;
        super.execute(useCaseObserver);
    }

    /**
     * Get an {@link Observable} which will emit a list of rooms based on coordinates.
     */
    @Override
    public Observable buildUseCaseObservable() {
        SearchRooms request;
        if (coordinates != null) {
            if (filters != null) {
                request = SearchRooms.builder()
                        .setCoordinates(coordinates)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(sortByAscendant)
                        .setNewSearchMode(true)
                        .setMaxPrice(filters.maxPrice())
                        .setAvailableFrom(filters.availableFrom())
                        .setGender(filters.gender())
                        .setNumberOfTenants(filters.numberOfTenants())
                        .setBedTypes(filters.bedTypes())
                        .setAmenities(filters.amenities())
                        .build();
            } else {
                request = SearchRooms.builder()
                        .setCoordinates(coordinates)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(sortByAscendant)
                        .setNewSearchMode(true)
                        .build();
            }
        } else if (bounds != null) {
            if (filters != null) {
                request = SearchRooms.builder()
                        .setBounds(bounds)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(publishedAtDescendant)
                        .setNewSearchMode(true)
                        .setMaxPrice(filters.maxPrice())
                        .setAvailableFrom(filters.availableFrom())
                        .setGender(filters.gender())
                        .setNumberOfTenants(filters.numberOfTenants())
                        .setBedTypes(filters.bedTypes())
                        .setAmenities(filters.amenities())
                        .build();
            } else {
                request = SearchRooms.builder()
                        .setBounds(bounds)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(publishedAtDescendant)
                        .setNewSearchMode(true)
                        .build();
            }
        } else {
            if (filters != null) {
                request = SearchRooms.builder()
                        .setLocation(location)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(publishedAtDescendant)
                        .setNewSearchMode(true)
                        .setMaxPrice(filters.maxPrice())
                        .setAvailableFrom(filters.availableFrom())
                        .setGender(filters.gender())
                        .setNumberOfTenants(filters.numberOfTenants())
                        .setBedTypes(filters.bedTypes())
                        .setAmenities(filters.amenities())
                        .build();
            } else {
                request = SearchRooms.builder()
                        .setLocation(location)
                        .setPage(page)
                        .setPer(ROOM_MARKERS_PER_PAGE)
                        .setOffset(offset)
                        .setSortBy(publishedAtDescendant)
                        .setNewSearchMode(true)
                        .build();
            }
        }
        Timber.i("GetRoomMarkersBySearch: ".concat(request.toString()));
        return roomRepository.getRoomMarkersBySearch(request);
    }
}
