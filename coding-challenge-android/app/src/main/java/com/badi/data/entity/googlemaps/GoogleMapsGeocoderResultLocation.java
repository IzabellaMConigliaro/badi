/*
 * File: GoogleMapsGeocoderResultLocation.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.googlemaps;

import com.google.gson.annotations.SerializedName;

public class GoogleMapsGeocoderResultLocation {

    @SerializedName("lat") private Double latitude;
    @SerializedName("lng") private Double longitude;

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
