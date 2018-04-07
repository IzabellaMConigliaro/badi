/*
 * File: SearchPlaceActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.badi.R;
import com.badi.common.di.HasComponent;
import com.badi.common.di.components.DaggerSearchComponent;
import com.badi.common.di.components.SearchComponent;
import com.badi.common.di.modules.SearchModule;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.LocationHelper;
import com.badi.common.utils.PlaceTypeMapper;
import com.badi.data.entity.PlaceAddress;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.navigation.Navigator;
import com.google.android.gms.common.api.ResolvableApiException;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class SearchPlaceActivity extends BaseActivity implements HasComponent<SearchComponent>, SearchPlaceContract.View {

    public static final String SEARCH_EXTRA_PLACE = "SearchPlaceActivity.SEARCH_EXTRA_PLACE";

    @Inject SearchPlacePresenter searchPlacePresenter;
    @Inject SearchPlaceRecentAdapter searchPlaceRecentAdapter;
    @Inject PlaceTypeMapper placeTypeMapper;

    private SearchComponent searchComponent;
    private LocationHelper locationHelper;

    @BindView(R.id.layout_search_place) CoordinatorLayout searchPlaceLayout;
    @BindView(R.id.edit_text_autocomplete) EditText autoCompleteEditText;
    @BindView(R.id.button_clear_text) ImageView clearTextButton;
    @BindView(R.id.recycler_view_searches) RecyclerView searchesRecyclerView;
    @BindView(R.id.recycler_view_autocomplete_places) RecyclerView placesRecyclerView;
    @BindView(R.id.button_current_location) Button currentLocationButton;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, SearchPlaceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        this.initializeInjector();
        this.getComponent().inject(this);
        ButterKnife.bind(this);
        searchPlacePresenter.attachView(this);
        locationHelper = new LocationHelper(this);
        setupLocationCallback();
        setResultCancelActivity();
        setupComponents();
    }

    @Override
    public SearchComponent getComponent() {
        return searchComponent;
    }

    private void initializeInjector() {
        searchComponent = DaggerSearchComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .searchModule(new SearchModule())
                .build();
    }

    private void setupComponents() {

    }

    private void setupLocationCallback() {
        locationHelper.setLocationCallback(new LocationHelper.Listener() {
            @Override
            public void onLocationFound(double latitude, double longitude) {
                searchPlacePresenter.getLocationDetails(latitude, longitude);
            }

            @Override
            public void onLocationError(ResolvableApiException exception) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(SearchPlaceActivity.this, Navigator.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    Timber.i("PendingIntent unable to execute request.");
                }
            }
        });
    }

    @OnClick(R.id.button_close)
    public void onCloseButtonClick() {
        supportFinishAfterTransition();
    }

    @OnClick(R.id.button_clear_text)
    void onClickClearTextButton () {

    }

    private void setResultCancelActivity() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
    }

    @OnClick(R.id.button_current_location)
    public void onCurrentLocationButtonClick() {
        askForLocationPermission();
    }

    public void askForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(SearchPlaceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Timber.i("Displaying location permission rationale to provide additional context.");
            Snackbar.make(searchPlaceLayout, R.string.permission_location_rationale, Snackbar.LENGTH_LONG)
                    .setAction(R.string.permission_ok, view -> ActivityCompat.requestPermissions(SearchPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Navigator.REQUEST_LOCATION))
                    .show();
        } else {
            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(SearchPlaceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Navigator.REQUEST_LOCATION);
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
                    Snackbar.make(searchPlaceLayout, R.string.permission_available_location, Snackbar.LENGTH_SHORT).show();
                    askForLocationPermission();
                } else {
                    Timber.i("LOCATION permission was NOT granted.");
                    Snackbar.make(searchPlaceLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showSearchList(List<PlaceAddress> searchList) {

    }

    @Override
    public void showEmptySearchList() {

    }

    @Override
    public void searchSavedInPrefs() {

    }

    @Override
    public void setResultOKActivity(PlaceAddress address) {

    }

    @Override
    public void showErrorNotResolved() {
        DialogFactory.createSimpleOkErrorDialog(this,
                getString(R.string.error_warning), getString(R.string.exception_message_no_connection))
                .show();
    }

    @Override
    public void showLoading() {
        //Not called
    }

    @Override
    public void hideLoading() {
        //Not called
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        DialogFactory.createSimpleOkErrorDialog(this,
                getString(R.string.error_warning), message)
                .show();
    }

    @Override
    public Context context() {
        return this;
    }
}
