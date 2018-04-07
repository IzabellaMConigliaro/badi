/*
 * File: GoogleMapsGeocoderResultGeometry.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.googlemaps;

import com.google.gson.annotations.SerializedName;

public class GoogleMapsGeocoderResultGeometry {

    @SerializedName("location") private GoogleMapsGeocoderResultLocation location;

    public GoogleMapsGeocoderResultLocation getLocation() {
        return location;
    }
}
