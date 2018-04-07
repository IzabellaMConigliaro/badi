package com.badi.data.repository.remote;

import com.badi.data.entity.PlaceAddress;
import com.google.android.gms.location.places.Place;

import java.util.List;

import io.reactivex.Observable;

public interface GeocoderDataSource {
    Observable<List<PlaceAddress>> getFromLocation(double latitude, double longitude);
    Observable<List<PlaceAddress>> getFromLocation(Place place, double latitude, double longitude);
}
