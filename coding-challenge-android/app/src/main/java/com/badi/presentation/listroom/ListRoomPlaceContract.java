package com.badi.presentation.listroom;

import com.badi.data.entity.PlaceAddress;
import com.badi.presentation.base.BaseView;
import com.google.android.gms.location.places.Place;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */
public interface ListRoomPlaceContract {

    interface View extends BaseView {
        void setResultOKActivity(PlaceAddress placeAddress);
        void showErrorNotResolved();
        void centerMapInPlace(PlaceAddress placeAddress);
        void showMap();
        void displayResolvedLocation(PlaceAddress placeAddress);
        void close();
        void closeWithResult(PlaceAddress placeAddress);
    }

    interface Presenter {
        void onPlaceDetails(Place place);
        void getLocationDetails(double latitude, double longitude);
        void onPlaceSelected(PlaceAddress placeAddress);
        void onCameraIdleWithCoordinates(double latitude, double longitude);
        void onConfirm();
        void onCancel();
    }
}
