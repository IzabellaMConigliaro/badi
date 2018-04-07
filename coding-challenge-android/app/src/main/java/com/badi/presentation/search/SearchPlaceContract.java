/*
 * File: SearchPlaceContract.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import com.badi.data.entity.PlaceAddress;
import com.badi.presentation.base.BaseView;
import com.google.android.gms.location.places.Place;

import java.util.List;

/**
 * Interface representing the contract between the view and the presenter in a model view presenter (MVP) pattern.
 */

public interface SearchPlaceContract {

    interface View extends BaseView {

        /**
         * Show a user searches list in the UI.
         *
         * @param searchList The List of {@link PlaceAddress} that will be shown.
         */
        void showSearchList(List<PlaceAddress> searchList);

        /**
         * Show an empty user searches list in the UI.
         */
        void showEmptySearchList();

        /**
         * Trigger when search is saved in prefs
         */
        void searchSavedInPrefs();

        /**
         * Finish the activity with success once the address is resolved
         */
        void setResultOKActivity(PlaceAddress placeAddress);

        /**
         * Show an error when location cannot be successfully resolved
         */
        void showErrorNotResolved();

    }

    interface Presenter {

        void saveUserSearch(PlaceAddress search);

        void getUserSearches();

        void onPlaceDetails(Place place);

        void getLocationDetails(double latitude, double longitude);

    }
}
