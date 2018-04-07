package com.badi.domain.interactor.listroom;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;
import com.badi.data.entity.PlaceAddress;
import com.badi.domain.interactor.UseCase;
import com.badi.domain.repository.LocationRepository;
import com.google.android.gms.location.places.Place;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * This class is an implementation of {@link UseCase} that represents a use case for
 * resolving a location
 */

public class ResolveLocation extends UseCase {

    private final LocationRepository locationRepository;
    private Double latitude;
    private Double longitude;
    private Place place;

    @Inject
    public ResolveLocation(LocationRepository locationRepository, ThreadExecutor threadExecutor, PostExecutionThread
            postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.locationRepository = locationRepository;
    }

    public void execute(Double latitude, Double longitude, DisposableObserver useCaseObserver) {
        this.latitude = latitude;
        this.longitude = longitude;
        super.execute(useCaseObserver);
    }

    public void execute(Place place, Double latitude, Double longitude, DisposableObserver useCaseObserver) {
        this.place = place;
        this.latitude = latitude;
        this.longitude = longitude;
        super.execute(useCaseObserver);
    }

    @Override
    protected Observable buildUseCaseObservable() {
        if (place != null) {
            Observable<List<PlaceAddress>> useCaseObservable = locationRepository.getFromLocation(place, latitude, longitude);
            place = null;
            return useCaseObservable;
        }

        return locationRepository.getFromLocation(latitude, longitude);
    }
}
