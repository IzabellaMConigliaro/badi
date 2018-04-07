package com.badi.domain.repository;

import com.badi.data.entity.PlaceAddress;
import com.google.android.gms.location.places.Place;

import java.util.List;

import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting location related data.
 */
public interface LocationRepository {
    Observable<List<PlaceAddress>> getFromLocation(double latitude, double longitude);
    Observable<List<PlaceAddress>> getFromLocation(Place place, double latitude, double longitude);
}
