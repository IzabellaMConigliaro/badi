/*
 * File: LocationUtil.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;

import timber.log.Timber;

/**
 * Location Util for knowing if Location services are enabled & other utilities related
 */
public class LocationHelper extends ContextWrapper {

    public interface Listener {
        void onLocationFound(double latitude, double longitude);
        void onLocationError(ResolvableApiException exception);
    }

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest locationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    protected LocationSettingsRequest locationSettingsRequest;

    /**
     * Provides access to the Settings API.
     */
    private SettingsClient settingsClient;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient fusedLocationProviderClient;

    /**
     * Provides interface to interact with the resolution of the request.
     */
    private Listener callback;

    /**
     * LocationHelper Builder.
     *
     * @param context The current context
     */
    public LocationHelper(Context context) {
        super(context);
        // Kick off the process of building the LocationRequest, and LocationSettingsRequest objects.
        buildSettingsLocationClients();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    /**
     * Sets the LocationHelper callback interface.
     */
    public void setLocationCallback(Listener callback) {
        this.callback = callback;
    }

    /**
     * Builds a SettingsClient and FusedLocationClient.
     */
    private void buildSettingsLocationClients() {
        settingsClient = LocationServices.getSettingsClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    protected void createLocationRequest() {
        locationRequest = new LocationRequest();

        locationRequest.setNumUpdates(1);

        locationRequest.setInterval(0);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsClient#checkLocationSettings(LocationSettingsRequest)}
     * method, with the results provided through a {@code Task}.
     * Also examines the {@link com.google.android.gms.location.LocationSettingsResult} object and determines if
     * location settings are adequate. If they are not, begins the process of presenting a location
     * settings dialog to the user.
     */
    public void checkLocationSettings() {
        settingsClient
                .checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(task -> {
                    // All location settings are satisfied. The client can initialize
                    // location requests here.
                    Timber.i("All location settings are satisfied.");
                    getLastLocation();
                })
                .addOnFailureListener(e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            Timber.i("Location settings are not satisfied. "
                                    .concat("Show the user a dialog to upgrade location settings "));
                            callback.onLocationError((ResolvableApiException) e);
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way
                            // to fix the settings so we won't show the dialog.
                            Timber.i("Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                });
    }

    /**
     * Requests last known location from the FusedLocationApi.
     */
    public void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient
                    .getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null)
                            callback.onLocationFound(location.getLatitude(), location.getLongitude());
                        else
                            startLocationUpdates();
                    });
        }
    }

    /**
     * Requests location updates from the FusedLocationApi.
     */
    protected void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient
                    .requestLocationUpdates(locationRequest, new LocationCallback() {
                        @Override
                        public void onLocationResult(LocationResult locationResult) {
                            if (locationResult.getLastLocation() != null)
                                callback.onLocationFound(
                                        locationResult.getLastLocation().getLatitude(),
                                        locationResult.getLastLocation().getLongitude());
                        }
                    }, null);
        }
    }

}
