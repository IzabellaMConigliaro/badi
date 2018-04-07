/*
 * File: AndroidLocationResolver.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.location.Address;
import android.location.Geocoder;

import com.badi.data.entity.PlaceAddress;
import com.badi.data.mapper.AndroidGeocoderMapper;
import com.google.android.gms.location.places.Place;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class AndroidGeocoderDataSource implements GeocoderDataSource {

    private static final int MAX_RESULTS = 1;

    private final Geocoder geocoder;
    private final AndroidGeocoderMapper androidGeocoderMapper;

    @Inject public AndroidGeocoderDataSource(Geocoder geocoder, AndroidGeocoderMapper androidGeocoderMapper) {
        this.geocoder = geocoder;
        this.androidGeocoderMapper = androidGeocoderMapper;
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, MAX_RESULTS);
            if (addresses == null || addresses.size() <= 0) {
                return Observable.error(new IOException());
            } else {
                List<PlaceAddress> placeAddresses = androidGeocoderMapper.transformIntoPlaceAddress(addresses);
                return Observable.just(placeAddresses);
            }
        } catch (IOException e) {
            return Observable.error(new IOException());
        }
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(Place place, double latitude, double longitude) {
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, MAX_RESULTS);
            if (addresses == null || addresses.size() <= 0) {
                return Observable.error(new IOException());
            } else {
                List<PlaceAddress> placeAddresses = androidGeocoderMapper.transformIntoPlaceAddress(addresses, place);
                return Observable.just(placeAddresses);
            }
        } catch (IOException e) {
            return Observable.error(new IOException());
        }
    }
}
