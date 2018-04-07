/*
 * File: MapsActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.badi.R;
import com.badi.common.utils.MapUtils;
import com.badi.presentation.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapsActivity extends BaseActivity implements OnMapReadyCallback {

    public static final String EXTRA_TITLE_TOOLBAR = "MapsActivity.EXTRA_TITLE_TOOLBAR";
    public static final String EXTRA_COORDINATES = "MapsActivity.EXTRA_COORDINATES";
    public static final String EXTRA_OBFUSCATED = "MapsActivity.EXTRA_OBFUSCATED";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private LatLng coordinates;
    private boolean obfuscated = true;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, String address, LatLng coordinates, boolean obfuscated) {
        Intent intent = new Intent(context, MapsActivity.class);
        intent.putExtra(EXTRA_TITLE_TOOLBAR, address);
        intent.putExtra(EXTRA_COORDINATES, coordinates);
        intent.putExtra(EXTRA_OBFUSCATED, obfuscated);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        ButterKnife.bind(this);
        setupToolbar(getIntent().getStringExtra(EXTRA_TITLE_TOOLBAR));
        coordinates = getIntent().getParcelableExtra(EXTRA_COORDINATES);
        obfuscated = getIntent().getBooleanExtra(EXTRA_OBFUSCATED, true);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setupToolbar(String title) {
        toolbar.setTitle(title);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_close_white));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (obfuscated) {
            googleMap.addCircle(MapUtils.createCirclePositionRoom(this, coordinates));
        } else {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_pin_precise));
            googleMap.addMarker(markerOptions);
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16));
    }
}
