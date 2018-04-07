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
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import com.badi.common.utils.PlaceAdapter;
import com.badi.common.utils.PlaceAutoCompleteAdapter;
import com.badi.common.utils.PlaceTypeMapper;
import com.badi.common.utils.SimplePlaceAutoCompleteAdapter;
import com.badi.data.entity.PlaceAddress;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.navigation.Navigator;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import timber.log.Timber;

public class SearchPlaceActivity extends BaseActivity implements HasComponent<SearchComponent>,
        SearchPlaceContract.View, GoogleApiClient.OnConnectionFailedListener {

    public static final String SEARCH_EXTRA_PLACE = "SearchPlaceActivity.SEARCH_EXTRA_PLACE";
    private static final int TIME_IDLE = 1000;

    @Inject SearchPlacePresenter searchPlacePresenter;
    @Inject SearchPlaceRecentAdapter searchPlaceRecentAdapter;
    @Inject PlaceTypeMapper placeTypeMapper;

    private SearchComponent searchComponent;
    private LocationHelper locationHelper;
    private PlaceAutoCompleteAdapter adapter;
    private PlaceAdapter placeAdapter;
    private GeoDataClient geoDataClient;

    @BindView(R.id.layout_search_place) CoordinatorLayout searchPlaceLayout;
    @BindView(R.id.edit_text_autocomplete) EditText autoCompleteEditText;
    @BindView(R.id.button_clear_text) ImageView clearTextButton;
    @BindView(R.id.recycler_view_searches) RecyclerView searchesRecyclerView;
    @BindView(R.id.recycler_view_autocomplete_places) RecyclerView placesRecyclerView;
    @BindView(R.id.button_current_location) Button currentLocationButton;
    @BindView(R.id.view_invalid_search) ConstraintLayout viewInvalidSearch;

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
        searchPlacePresenter.getUserSearches();

        // The entry points to the Places API.
        geoDataClient = Places.getGeoDataClient(this, null);
        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        adapter = new SimplePlaceAutoCompleteAdapter(geoDataClient, null, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build());
        adapter.setOnPlaceListener(onPlaceListener);
        adapter.setOnListPopulationListener(onListPopulationListener);

        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setAdapter(adapter);

        placeAdapter = new PlaceAdapter(new ArrayList<>());
        searchesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchesRecyclerView.setAdapter(placeAdapter);

        autoCompleteEditText.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == R.id.search || actionId == EditorInfo.IME_ACTION_GO) {
                if (textView.getText().length() > 0) {
                    if (adapter.getItemCount() > 0)
                        onPlaceListener.onUserItemClicked(0);
                }
                return true;
            }
            return false;
        });

        RxTextView.textChanges(autoCompleteEditText)
                .filter(charSequence -> charSequence.length() > 0)
                .debounce(TIME_IDLE, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(string ->
                adapter.getFilter().filter(string));
    }

    private PlaceAutoCompleteAdapter.OnPlaceListener onPlaceListener = new PlaceAutoCompleteAdapter.OnPlaceListener() {
        @Override
        public void onUserItemClicked(Integer position) {
            if (position != RecyclerView.NO_POSITION) {
                /*
                Retrieve the place ID of the selected item from the Adapter.
                The adapter stores each Place suggestion in a AutocompletePrediction from which we
                read the place ID and title.
                */
                final AutocompletePrediction item = adapter.getItem(position);
                final String placeId = item.getPlaceId();

                /*
                Issue a request to the Places Geo Data API to retrieve a Place object with additional
                details about the place.
                */
                Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(placeId);
                placeResult.addOnCompleteListener(updatePlaceDetailsCallback);
            }
        }
    };

    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */
    private OnCompleteListener<PlaceBufferResponse> updatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(@NonNull Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                if (places.getCount() > 0) {
                    // Get the Place object from the buffer.
                    final Place place = places.get(0);

                    Timber.i("Place details received: ".concat(place.getName().toString()));

                    searchPlacePresenter.onPlaceDetails(place);
                } else {
                    Timber.e("Place query did not found any result.");
                }
                places.release();
            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                Timber.e( "Place query did not complete.".concat(e.toString()));
            }
        }
    };

    /**
     * Listener that notifies when the suggestion list is populated or clear.
     */
    private PlaceAutoCompleteAdapter.OnListPopulationListener onListPopulationListener = () -> {
        if (adapter.getItemCount() == 0) {
            if (!TextUtils.isEmpty(autoCompleteEditText.getText())) {
                viewInvalidSearch.setVisibility(View.VISIBLE);
            } else {
                viewInvalidSearch.setVisibility(View.GONE);
            }
            searchesRecyclerView.setVisibility(View.VISIBLE);
            placesRecyclerView.setVisibility(View.GONE);
        } else {
            searchesRecyclerView.setVisibility(View.GONE);
            viewInvalidSearch.setVisibility(View.GONE);
            placesRecyclerView.setVisibility(View.VISIBLE);
        }
    };

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
        placeAdapter.setPlacesList(searchList);
    }

    @Override
    public void showEmptySearchList() {

    }

    @Override
    public void searchSavedInPrefs() {

    }

    @Override
    public void setResultOKActivity(PlaceAddress address) {
        searchPlacePresenter.saveUserSearch(address);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
