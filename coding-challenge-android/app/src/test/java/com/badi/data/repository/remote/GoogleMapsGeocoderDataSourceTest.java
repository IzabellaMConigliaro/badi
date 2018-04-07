package com.badi.data.repository.remote;

import com.badi.data.entity.googlemaps.GoogleMapsGeocoderResponse;
import com.badi.data.entity.PlaceAddress;
import com.badi.data.mapper.GoogleMapsGeocoderMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * Created by andres on 04/08/2017.
 */
public class GoogleMapsGeocoderDataSourceTest {

    private GoogleMapsGeocoderDataSource googleMapsGeocoderDataSource;

    @Mock private GoogleMapsAPIService googleMapsAPIServiceMock;
    private String googleMapsApiKeyMock = "whatever";
    @Mock private GoogleMapsGeocoderMapper googleMapsGeocoderMapperMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        googleMapsGeocoderDataSource = new GoogleMapsGeocoderDataSource(googleMapsAPIServiceMock, googleMapsApiKeyMock,
                googleMapsGeocoderMapperMock);
    }

    @Test
    public void shouldReturnEmptyCollectionWhenNoResultsFound() throws Exception {
        String formattedCoordinates = String.format("%1s,%2s", 100.0, 400.0);
        when(googleMapsAPIServiceMock.reverseGeocodeByCoordinates(googleMapsApiKeyMock, formattedCoordinates)).thenReturn(
                Single.just(new GoogleMapsGeocoderResponse()));

        Observable<List<PlaceAddress>> fromLocationObservable = googleMapsGeocoderDataSource.getFromLocation(100, 400);

        TestObserver<List<PlaceAddress>> testObserver = fromLocationObservable.test();
        testObserver.assertComplete();
        assertNotNull(testObserver.values());
        List<PlaceAddress> placeAddressesResult = testObserver.values().get(0);
        assertNotNull(placeAddressesResult);
        assertEquals(0, placeAddressesResult.size());
    }
}