package com.badi.data.repository.remote;

import com.badi.common.di.qualifiers.GoogleMapsApiKey;
import com.badi.data.entity.PlaceAddress;
import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResponse;
import com.badi.data.mapper.GoogleMapsGeocoderMapper;
import com.google.android.gms.location.places.Place;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

@Singleton
public class GoogleMapsGeocoderDataSource implements GeocoderDataSource {

    private final GoogleMapsAPIService googleMapsAPIService;
    private final String googleMapsApiKey;
    private final GoogleMapsGeocoderMapper googleMapsGeocoderMapper;

    @Inject
    public GoogleMapsGeocoderDataSource(GoogleMapsAPIService googleMapsAPIService, @GoogleMapsApiKey String googleMapsApiKey,
                                        GoogleMapsGeocoderMapper googleMapsGeocoderMapper) {
        this.googleMapsAPIService = googleMapsAPIService;
        this.googleMapsApiKey = googleMapsApiKey;
        this.googleMapsGeocoderMapper = googleMapsGeocoderMapper;
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(final double latitude, final double longitude) {
        String latLng = String.format("%1s,%2s", latitude, longitude);
        return googleMapsAPIService.reverseGeocodeByCoordinates(googleMapsApiKey, latLng)
                .map(new Function<GoogleMapsGeocoderResponse, List<PlaceAddress>>() {
                    @Override
                    public List<PlaceAddress> apply(@NonNull GoogleMapsGeocoderResponse googleMapsGeocoderResponse) throws
                            Exception {
                        return googleMapsGeocoderMapper.transformIntoPlaceAddresses(googleMapsGeocoderResponse);
                    }
                }).toObservable();
    }

    @Override
    public Observable<List<PlaceAddress>> getFromLocation(Place place, double latitude, double longitude) {
        Single<GoogleMapsGeocoderResponse> singleStream;
        if (place != null && place.getId() != null && !place.getId().isEmpty()) {
            singleStream = googleMapsAPIService.reverseGeocodeByPlaceId(googleMapsApiKey, place.getId());
        } else {
            String latLng = String.format("%1s,%2s", latitude, longitude);
            singleStream = googleMapsAPIService.reverseGeocodeByCoordinates(googleMapsApiKey, latLng);
        }

        return singleStream
                .map(new Function<GoogleMapsGeocoderResponse, List<PlaceAddress>>() {
                    @Override
                    public List<PlaceAddress> apply(@NonNull GoogleMapsGeocoderResponse googleMapsGeocoderResponse) throws
                            Exception {
                        return googleMapsGeocoderMapper.transformIntoPlaceAddresses(googleMapsGeocoderResponse);
                    }
                }).toObservable();
    }
}
