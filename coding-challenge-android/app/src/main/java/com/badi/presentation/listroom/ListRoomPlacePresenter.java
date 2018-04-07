package com.badi.presentation.listroom;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.PlaceAddress;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.listroom.ResolveLocation;
import com.badi.presentation.base.BasePresenter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link ListRoomPlacePresenter} that controls communication between views and models of the presentation
 * layer of the list room place view
 */
@PerActivity
public class ListRoomPlacePresenter extends BasePresenter<ListRoomPlaceContract.View> implements ListRoomPlaceContract.Presenter {

    private final ResolveLocation resolveLocationUseCase;
    private PlaceAddress placeAddress;

    @Inject
    ListRoomPlacePresenter(ResolveLocation resolveLocationUseCase) {
        this.resolveLocationUseCase = resolveLocationUseCase;
    }

    @Override
    public void attachView(ListRoomPlaceContract.View baseView) {
        super.attachView(baseView);
    }

    @Override
    public void detachView() {
        super.detachView();
        resolveLocationUseCase.clear();
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

    @Override
    public void onPlaceSelected(PlaceAddress placeAddress) {
        this.placeAddress = placeAddress;

        checkViewAttached();
        getView().showMap();
        getView().centerMapInPlace(placeAddress);
    }

    @Override
    public void onCameraIdleWithCoordinates(double latitude, double longitude) {
        resolveLocationUseCase.execute(latitude, longitude, new ResolveNewLocationObserver());
    }

    @Override
    public void onConfirm() {
        checkViewAttached();
        getView().closeWithResult(placeAddress);
    }

    @Override
    public void onCancel() {
        checkViewAttached();
        getView().close();
    }

    private final class ResolveNewLocationObserver extends DefaultObserver<List<PlaceAddress>> {

        @Override
        public void onError(Throwable exception) {
            Timber.e("There was an error resolving the address.");
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) exception).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(List<PlaceAddress> placeAddresses) {
            if (placeAddresses == null || placeAddresses.isEmpty()) {
                String breadCrumb = String.format("ListRoomPlacePresenter#ResolveNewLocationObserver | PlaceAddresses is null? " +
                        "%s", placeAddresses == null);
                if (placeAddresses != null) {
                    breadCrumb += String.format("| PlaceAddresses.isEmpty()? %s", placeAddresses.isEmpty());
                }
                return;
            }
            placeAddress = placeAddresses.get(0);
            getView().displayResolvedLocation(placeAddress);
        }
    }

    private final class ResolveLocationObserver extends DefaultObserver<List<PlaceAddress>> {

        @Override
        public void onError(Throwable exception) {
            Timber.e("There was an error resolving the address.");
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) exception).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(List<PlaceAddress> placeAddresses) {
            if (placeAddresses == null || placeAddresses.isEmpty()) {
                String breadCrumb = String.format("ListRoomPlacePresenter#ResolveLocationObserver | PlaceAddresses is null? " +
                        "%s", placeAddresses == null);
                if (placeAddresses != null) {
                    breadCrumb += String.format("| PlaceAddresses.isEmpty()? %s", placeAddresses.isEmpty());
                }
                getView().showErrorNotResolved();
                return;
            }

            PlaceAddress placeAddress = placeAddresses.get(0);
            if (placeAddress == null) {
                getView().showErrorNotResolved();
            } else {
                getView().setResultOKActivity(placeAddress);
                onPlaceSelected(placeAddress);
            }
        }
    }
}
