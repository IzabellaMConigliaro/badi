/*
 * File: SearchPlacePresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.PlaceAddress;
import com.badi.domain.interactor.DefaultCompletableObserver;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.listroom.ResolveLocation;
import com.badi.domain.interactor.search.GetSearchesInPrefs;
import com.badi.domain.interactor.search.SetSearchInPrefs;
import com.badi.presentation.base.BasePresenter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link SearchPlacePresenter} that controls communication between views and models of the presentation
 * layer of the Search.
 */
@PerActivity
public class SearchPlacePresenter extends BasePresenter<SearchPlaceContract.View> implements SearchPlaceContract.Presenter {

    private final GetSearchesInPrefs getSearchesInPrefs;
    private final SetSearchInPrefs setSearchInPrefs;
    private final ResolveLocation resolveLocationUseCase;

    @Inject
    SearchPlacePresenter(GetSearchesInPrefs getSearchesInPrefs, SetSearchInPrefs setSearchInPrefs, ResolveLocation
            resolveLocation) {
        this.getSearchesInPrefs = getSearchesInPrefs;
        this.setSearchInPrefs = setSearchInPrefs;
        this.resolveLocationUseCase = resolveLocation;
    }

    @Override
    public void attachView(SearchPlaceContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        getSearchesInPrefs.clear();
        setSearchInPrefs.clear();
    }

    @Override
    public void getUserSearches() {
        checkViewAttached();
        getSearchesInPrefs.execute(new GetSearchesInPrefsObserver());
    }

    @Override
    public void saveUserSearch(PlaceAddress search) {
        checkViewAttached();
        setSearchInPrefs.execute(search, new SetSearchInPrefsObserver());
    }

    @Override
    public void onPlaceDetails(Place place) {
        if (place == null) {
            return;
        }

        LatLng placeLatLng = place.getLatLng();
        resolveLocationUseCase.execute(place, placeLatLng.latitude, placeLatLng.longitude, new ResolveLocationObserver());
    }

    @Override
    public void getLocationDetails(double latitude, double longitude) {
        checkViewAttached();
        resolveLocationUseCase.execute(latitude, longitude, new ResolveLocationObserver());
    }

    private final class GetSearchesInPrefsObserver extends DefaultObserver<List<PlaceAddress>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the user saved places data process");
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(List<PlaceAddress> searches) {
            getView().hideLoading();
            if (searches != null)
                getView().showSearchList(searches);
            else
                getView().showEmptySearchList();
        }
    }

    private final class SetSearchInPrefsObserver extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            getView().searchSavedInPrefs();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in saving the user place data process");
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }
    }

    private final class ResolveLocationObserver extends DefaultObserver<List<PlaceAddress>> {

        @Override
        public void onError(Throwable exception) {
            Timber.e("There was an error resolving the address.");
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) exception).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(List<PlaceAddress> placeAddresses) {
            if (placeAddresses == null || placeAddresses.isEmpty()) {
                getView().showErrorNotResolved();
                return;
            }

            PlaceAddress placeAddress = placeAddresses.get(0);
            if (placeAddress == null) {
                getView().showErrorNotResolved();
            } else {
                getView().setResultOKActivity(placeAddress);
            }
        }
    }

}
