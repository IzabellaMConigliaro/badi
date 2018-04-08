/*
 * File: SearchFragment.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.arch.lifecycle.Lifecycle;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.LocationHelper;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.PlaceAddress;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Location;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.listroom.ResolveLocation;
import com.badi.presentation.base.BaseFragment;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.navigation.Navigator;
import com.google.android.gms.common.api.ResolvableApiException;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static android.app.Activity.RESULT_OK;
import static com.badi.presentation.search.DialogNeedABadi.DIALOG_NEED_A_BADI;

/**
 * A {@link BaseFragment} that displays the search
 */
public class SearchFragment extends BaseFragment {
    public static final String PARAM_PLACE_ADDRESS = "param_place_address";

    @Inject PreferencesHelper preferencesHelper;
    @Inject ResolveLocation resolveLocationUseCase;

    @BindView(R.id.appbar) AppBarLayout appBar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.view_search) View searchView;
    @BindView(R.id.countries_recycler_view) RecyclerView countriesRecyclerView;
    @BindView(R.id.suggestion_name) TextView suggestionName;
    @BindView(R.id.cities_recycler_view) RecyclerView citiesRecyclerView;

    private LocationHelper locationHelper;
    private Unbinder unbinder;

    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        locationHelper = new LocationHelper(getContext());
        ((MainActivity) getActivity()).getSearchComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_search, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        ViewUtil.shouldHideStatusBarSearch(appBar);
        setupToolbar();
        setupComponents();
        setupLocationCallback();
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Navigator.REQUEST_CHECK_SETTINGS:{
                switch (resultCode) {
                    case RESULT_OK:
                        Timber.i("User agreed to make required location settings changes.");
                        locationHelper.getLastLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        Timber.i("User chose not to make required location settings changes.");
                        break;
                }
            }
            break;
            case Navigator.REQUEST_CODE_SEARCH_PLACE_ACTIVITY:{
                switch (resultCode) {
                    case RESULT_OK:
                        PlaceAddress placeAddress = data.getParcelableExtra(PARAM_PLACE_ADDRESS);
                        if (placeAddress != null) {
                            if (placeAddress.id() != null) {
                                Location location = Location.builder().setAddress(placeAddress.address()).setPlaceID(placeAddress.id()).build();
                                navigateToSearchResultWithLocation(placeAddress.name(), location, true);
                            } else {
                                Coordinates coordinates = Coordinates.builder()
                                        .setLatitude(placeAddress.latitude())
                                        .setLongitude(placeAddress.longitude())
                                        .build();
                                navigateToSearchResultWithCoordinates(placeAddress.name(), coordinates, true);
                            }
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
        }

        if (getChildFragmentManager().getFragments() != null) {
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    private void setupToolbar() {
        collapsingToolbar.setCollapsedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
        collapsingToolbar.setExpandedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //Binding reset to avoid memory leaks
        unbinder.unbind();
    }

    @OnClick(R.id.button_search_place)
    void onClickButtonSearchPlace() {
        navigator.navigateToSearchPlace(getActivity());
    }

    @OnClick(R.id.button_search_list_room)
    void onClickButtonListARoom() {
        new DialogNeedABadi().showAllowingStateLoss(getFragmentManager(),
                DIALOG_NEED_A_BADI, getOnDialogNeedABadiListener());
    }

    @NonNull
    private DialogNeedABadi.OnDialogNeedABadiListener getOnDialogNeedABadiListener() {
        return new DialogNeedABadi.OnDialogNeedABadiListener() {
            @Override
            public void onListRoomClicked() {
                navigator.navigateToListRoom(getContext());
            }

            @Override
            public void onDismissDialog(Dialog dialog) {
                dialog.dismiss();
            }
        };
    }

    private void handleSearchTypeNavigation(PlaceAddress placeAddress) {
        if (placeAddress.id() == null)
            navigateToSearchResultWithCoordinates(placeAddress.name(),
                    buildCoordinates(placeAddress.latitude(), placeAddress.longitude()));
        else
            navigateToSearchResultWithLocation(placeAddress.name(),
                    buildLocation(placeAddress.address(), placeAddress.id()));
    }

    private Coordinates buildCoordinates(Double latitude, Double longitude) {
        return Coordinates.builder()
                .setLatitude(latitude)
                .setLongitude(longitude)
                .build();
    }

    private Location buildLocation(String address, String placeID) {
        return Location.builder()
                .setAddress(address)
                .setPlaceID(placeID)
                .build();
    }

    private void setupComponents() {
        countriesRecyclerView.setAdapter(new CountriesAdapter(getContext(), getCityClickListener()));

        suggestionName.setText(R.string.search_suggested);
        citiesRecyclerView.setAdapter(new SuggestedCitiesAdapter(getContext(), getCityClickListener()));
    }

    @NonNull
    private CitiesAdapter.OnCityListener getCityClickListener() {
        return (cityImage, city) ->
                navigateToSearchResultWithLocation(city.name(), buildLocation(city.address(), city.id()));
    }

    private void navigateToSearchResultWithLocation(String toolbarTitle, Location location) {
        navigateToSearchResultFragment(SearchResultFragment.newInstance(toolbarTitle, null, location), false);
    }

    private void navigateToSearchResultWithLocation(String toolbarTitle, Location location, boolean forceOpen) {
        navigateToSearchResultFragment(SearchResultFragment.newInstance(toolbarTitle, null, location), forceOpen);
    }

    private void navigateToSearchResultWithCoordinates(String toolbarTitle, Coordinates coordinates) {
        navigateToSearchResultFragment(SearchResultFragment.newInstance(toolbarTitle, coordinates, null), false);
    }

    private void navigateToSearchResultWithCoordinates(String toolbarTitle, Coordinates coordinates, boolean forceOpen) {
        navigateToSearchResultFragment(SearchResultFragment.newInstance(toolbarTitle, coordinates, null), forceOpen);
    }

    private void navigateToSearchResultFragment(Fragment fragment, boolean forceOpen) {
        if (forceOpen || getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
            // Avoid double transaction if user clicks two times the "Search nearby" button
            if (getChildFragmentManager().getBackStackEntryCount() == 0) {
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.fragment_search, fragment, fragment.getClass().getSimpleName());
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
                fragmentTransaction.commit();
            }
        }
    }

    @OnClick(R.id.button_current_location)
    public void onCurrentLocationButtonClick() {
        askForLocationPermission();
    }

    public void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
        } else {
            Timber.i("Location permission has already been granted. Asking for location updates");
            locationHelper.checkLocationSettings();
        }
    }

    /**
     * Requests the Location permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestLocationPermission() {
        Timber.i("LOCATION permission has NOT been granted. Requesting permission.");

        if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Timber.i("Displaying location permission rationale to provide additional context.");
            Snackbar.make(searchView, R.string.permission_location_rationale, Snackbar.LENGTH_LONG)
                    .setAction(R.string.permission_ok,
                            view -> requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            Navigator.REQUEST_LOCATION))
                    .show();
        } else {
            // Location permission has not been granted yet. Request it directly.
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Navigator.REQUEST_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Navigator.REQUEST_LOCATION: {
                // Received permission result for location permission.
                Timber.i("Received response for Location permission request.");

                // Check if the only required permission has been granted
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Location permission has been granted, preview can be displayed
                    Timber.i("LOCATION permission has now been granted.");
                    Snackbar.make(searchView, R.string.permission_available_location, Snackbar.LENGTH_SHORT).show();
                    askForLocationPermission();
                } else {
                    Timber.i("LOCATION permission was NOT granted.");
                    Snackbar.make(searchView, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    private void setupLocationCallback() {
        locationHelper.setLocationCallback(new LocationHelper.Listener() {
            @Override
            public void onLocationFound(double latitude, double longitude) {
                resolveLocationUseCase.execute(latitude, longitude, new ResolveLocationObserver());
            }

            @Override
            public void onLocationError(ResolvableApiException exception) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(getActivity(), Navigator.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    Timber.i("PendingIntent unable to execute request.");
                }
            }
        });
    }

    private final class ResolveLocationObserver extends DefaultObserver<List<PlaceAddress>> {

        @Override
        public void onError(Throwable exception) {
            DialogFactory.createSimpleOkErrorDialog(getActivity(),
                    getString(R.string.error_warning), getString(R.string.error_location_service))
                    .show();
        }

        @Override
        public void onNext(List<PlaceAddress> placeAddresses) {
            if (placeAddresses == null || placeAddresses.isEmpty()) {
                DialogFactory.createSimpleOkErrorDialog(getActivity(),
                        getString(R.string.error_warning), getString(R.string.error_location_service))
                        .show();
            } else {
                PlaceAddress placeAddress = placeAddresses.get(0);
                Coordinates coordinates = buildCoordinates(placeAddress.latitude(), placeAddress.longitude());
                navigateToSearchResultWithCoordinates(placeAddress.name(), coordinates);
            }
        }
    }
}
