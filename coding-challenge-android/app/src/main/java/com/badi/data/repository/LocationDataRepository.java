package com.badi.data.repository;

import com.badi.data.entity.PlaceAddress;
import com.badi.data.repository.remote.AndroidGeocoderDataSource;
import com.badi.data.repository.remote.GeocoderDataSource;
import com.badi.data.repository.remote.GoogleMapsGeocoderDataSource;
import com.badi.domain.repository.LocationRepository;
import com.google.android.gms.location.places.Place;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * {@link LocationRepository} implementation for retrieving location data.
 */
@Singleton
public class LocationDataRepository implements LocationRepository {

    private static final int RETRY_COUNT = 3;

    private final GeocoderDataSource androidGeocoderDataSource;
    private final GeocoderDataSource googleMapsGeocoderDataSource;

    /**
     * Constructs a {@link LocationRepository}
     *
     * @param androidGeocoderDataSource {@link AndroidGeocoderDataSource}
     * @param googleMapsGeocoderDataSource {@link GoogleMapsGeocoderDataSource}
     */
    @Inject
    public LocationDataRepository(AndroidGeocoderDataSource androidGeocoderDataSource,
                                  GoogleMapsGeocoderDataSource googleMapsGeocoderDataSource) {
        this.androidGeocoderDataSource = androidGeocoderDataSource;
        this.googleMapsGeocoderDataSource = googleMapsGeocoderDataSource;
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(double latitude, double longitude) {
        return androidGeocoderDataSource.getFromLocation(latitude, longitude)
                .retry(RETRY_COUNT)
                .onErrorResumeNext(googleMapsGeocoderDataSource.getFromLocation(latitude, longitude));
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(Place place, double latitude, double longitude) {
        return androidGeocoderDataSource.getFromLocation(place, latitude, longitude)
                .retry(RETRY_COUNT)
                .onErrorResumeNext(googleMapsGeocoderDataSource.getFromLocation(place, latitude, longitude));
    }
}
