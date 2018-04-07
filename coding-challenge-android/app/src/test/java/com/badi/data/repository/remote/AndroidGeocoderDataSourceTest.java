/*
 * File: AndroidGeocoderDataSourceTest.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.location.Geocoder;

import com.badi.data.entity.PlaceAddress;
import com.badi.data.mapper.AndroidGeocoderMapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by andres on 04/08/2017.
 */
public class AndroidGeocoderDataSourceTest {

    private AndroidGeocoderDataSource androidGeocoderDataSource;

    @Mock private Geocoder geocoderMock;
    @Mock private AndroidGeocoderMapper androidGeocoderMapper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        androidGeocoderDataSource = new AndroidGeocoderDataSource(geocoderMock, androidGeocoderMapper);
    }

    @Test
    public void shouldProperlyHandleGeocoderException() throws Exception {
        when(geocoderMock.getFromLocation(100, 200, 1)).thenThrow(new IOException());

        Observable<List<PlaceAddress>> fromLocationObservable = androidGeocoderDataSource.getFromLocation(100, 200);

        TestObserver<List<PlaceAddress>> testObserver = fromLocationObservable.test();
        assertEquals(1, testObserver.errorCount());
        testObserver.assertError(IOException.class);
    }

    @Test
    public void shouldThrowExceptionWhenGeocoderReturnsEmptyResults() throws Exception {
        when(geocoderMock.getFromLocation(500, 500, 1)).thenReturn(null);

        Observable<List<PlaceAddress>> fromLocationObservable = androidGeocoderDataSource.getFromLocation(500, 500);

        TestObserver<List<PlaceAddress>> testObserver = fromLocationObservable.test();
        assertEquals(1, testObserver.errorCount());
        testObserver.assertError(IOException.class);
    }
}