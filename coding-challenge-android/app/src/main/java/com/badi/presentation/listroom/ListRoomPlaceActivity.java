/*
 * File: ListRoomPlaceActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.badi.R;
import com.badi.common.di.HasComponent;
import com.badi.common.di.components.DaggerRoomComponent;
import com.badi.common.di.components.RoomComponent;
import com.badi.common.di.modules.RoomModule;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.LocationHelper;
import com.badi.common.utils.PlaceAutoCompleteAdapter;
import com.badi.common.utils.ViewUtil;
import com.badi.common.utils.map.MapStateListener;
import com.badi.common.utils.map.TouchableMapFragment;
import com.badi.data.entity.PlaceAddress;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.navigation.Navigator;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ListRoomPlaceActivity extends BaseActivity implements HasComponent<RoomComponent>, ListRoomPlaceContract.View,
        OnMapReadyCallback, GoogleMap.OnCameraIdleListener {

    public static final String LIST_ROOM_EXTRA_PLACE = "ListRoomPlaceActivity.LIST_ROOM_EXTRA_PLACE";

    // The entry points to the Places API.
    private GeoDataClient geoDataClient;
    private LocationHelper locationHelper;
    private RoomComponent roomComponent;
    private PlaceAutoCompleteAdapter adapter;
    private TouchableMapFragment mapFragment;
    private GoogleMap googleMap;
    @Inject ListRoomPlacePresenter listRoomPlacePresenter;

    @BindView(R.id.layout_map) LinearLayout mapLayout;
    @BindView(R.id.layout_list_room_place) CoordinatorLayout listRoomPlaceLayout;
    @BindView(R.id.edit_text_autocomplete) EditText autoCompleteEditText;
    @BindView(R.id.button_clear_text) ImageView clearTextButton;
    @BindView(R.id.recycler_view_autocomplete_places) RecyclerView placesRecyclerView;
    @BindView(R.id.button_current_location) Button currentLocationButton;
    @BindView(R.id.button_confirmation) Button confirmationButton;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, String address) {
        Intent intent = new Intent(context, ListRoomPlaceActivity.class);
        intent.putExtra(LIST_ROOM_EXTRA_PLACE, address);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room_place);
        this.initializeInjector();
        this.getComponent().inject(this);
        ButterKnife.bind(this);
        listRoomPlacePresenter.attachView(this);
        locationHelper = new LocationHelper(this);
        setupLocationCallback();
        setResultCancelActivity();
        setupComponents();
    }

    @Override
    public RoomComponent getComponent() {
        return roomComponent;
    }

    private void initializeInjector() {
        roomComponent = DaggerRoomComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .roomModule(new RoomModule())
                .build();
    }

    private void setupLocationCallback() {
        locationHelper.setLocationCallback(new LocationHelper.Listener() {
            @Override
            public void onLocationFound(double latitude, double longitude) {
                listRoomPlacePresenter.getLocationDetails(latitude, longitude);
            }

            @Override
            public void onLocationError(ResolvableApiException exception) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(ListRoomPlaceActivity.this, Navigator.REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                    Timber.i("PendingIntent unable to execute request.");
                }
            }
        });
    }

    private void setupComponents() {
        // The entry points to the Places API.
        geoDataClient = Places.getGeoDataClient(this, null);
        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        adapter = new PlaceAutoCompleteAdapter(geoDataClient, null, new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build());
        adapter.setOnPlaceListener(onPlaceListener);
        adapter.setOnListPopulationListener(onListPopulationListener);

        placesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        placesRecyclerView.setAdapter(adapter);

        autoCompleteEditText.addTextChangedListener(autocompleteTextWatcher);

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

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        mapFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @OnClick(R.id.button_close)
    public void onCloseButtonClick() {
        listRoomPlacePresenter.onCancel();
    }

    @OnClick(R.id.button_clear_text)
    void onClickClearTextButton() {
        autoCompleteEditText.setText("");
        adapter.clear();
        hideMap();
    }

    @OnClick(R.id.button_confirmation)
    void onClickConfirmation() {
        listRoomPlacePresenter.onConfirm();
    }

    /**
     * Listener that handles the autocomplete edit text input
     */
    private TextWatcher autocompleteTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                clearTextButton.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(s.toString());
            } else
                clearTextButton.setVisibility(View.GONE);
        }
    };

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
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
                final CharSequence primaryText = item.getPrimaryText(null);

                Timber.i("Autocomplete item selected: ".concat(primaryText.toString()));

                /*
                Issue a request to the Places Geo Data API to retrieve a Place object with additional
                details about the place.
                */
                Task<PlaceBufferResponse> placeResult = geoDataClient.getPlaceById(placeId);
                placeResult.addOnCompleteListener(updatePlaceDetailsCallback);
                Timber.i("Called getPlaceById to get Place details for ".concat(placeId != null ? placeId : ""));
            }
        }
    };

    /**
     * Listener that notifies when the suggestion list is populated or clear.
     */
    private PlaceAutoCompleteAdapter.OnListPopulationListener onListPopulationListener = this::hideMap;

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

                            listRoomPlacePresenter.onPlaceDetails(place);
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(ListRoomPlaceActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Timber.i("Displaying location permission rationale to provide additional context.");
            Snackbar.make(listRoomPlaceLayout, R.string.permission_location_rationale, Snackbar.LENGTH_LONG)
                    .setAction(R.string.permission_ok, view -> ActivityCompat.requestPermissions(ListRoomPlaceActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Navigator.REQUEST_LOCATION))
                    .show();
        } else {
            // Location permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(ListRoomPlaceActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
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
                    Snackbar.make(listRoomPlaceLayout, R.string.permission_available_location, Snackbar.LENGTH_SHORT).show();
                    askForLocationPermission();
                } else {
                    Timber.i("LOCATION permission was NOT granted.");
                    Snackbar.make(listRoomPlaceLayout, R.string.permissions_not_granted, Snackbar.LENGTH_SHORT).show();
                }
            }
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Access to the location has been granted to the app.
            this.googleMap.setMyLocationEnabled(false);
            this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        this.googleMap.setOnCameraIdleListener(this);

        new MapStateListener(googleMap, mapFragment, this) {
            @Override
            public void onMapTouched() {

            }

            @Override
            public void onMapReleased() {

            }

            @Override
            public void onMapUnsettled() {

            }

            @Override
            public void onMapSettled() {

            }
        };
    }

    @Override
    public void onCameraIdle() {
        CameraPosition cameraPosition = googleMap.getCameraPosition();
        listRoomPlacePresenter.onCameraIdleWithCoordinates(cameraPosition.target.latitude, cameraPosition.target.longitude);
    }

    private void hideMap() {
        mapLayout.setVisibility(View.GONE);
        currentLocationButton.setVisibility(View.VISIBLE);
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void setResultOKActivity(PlaceAddress address) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(LIST_ROOM_EXTRA_PLACE, address);
        setResult(Activity.RESULT_OK, resultIntent);
        ViewUtil.hideKeyboard(this);
    }

    @Override
    public void showErrorNotResolved() {
        DialogFactory.createSimpleOkErrorDialog(this,
                getString(R.string.error_warning), getString(R.string.error_location_service))
                .show();
    }

    @Override
    public void centerMapInPlace(PlaceAddress placeAddress) {
        LatLng latLng = new LatLng(placeAddress.latitude(), placeAddress.longitude());
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, 16);

        googleMap.moveCamera(center);
    }

    @Override
    public void showMap() {
        mapLayout.setVisibility(View.VISIBLE);
        currentLocationButton.setVisibility(View.GONE);
    }

    @Override
    public void displayResolvedLocation(PlaceAddress placeAddress) {
        autoCompleteEditText.removeTextChangedListener(autocompleteTextWatcher);
        autoCompleteEditText.setText(placeAddress.address());
        autoCompleteEditText.addTextChangedListener(autocompleteTextWatcher);
        clearTextButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void close() {
        setResultCancelActivity();
        ViewUtil.hideKeyboard(this);
        supportFinishAfterTransition();
    }

    @Override
    public void closeWithResult(PlaceAddress placeAddress) {
        setResultOKActivity(placeAddress);
        supportFinishAfterTransition();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        DialogFactory.createSimpleOkErrorDialog(context(), getString(R.string.error_warning), message).show();
    }

    @Override
    public Context context() {
        return this;
    }
}
