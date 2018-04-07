/*
 * File: GoogleMapsGeocoderResultAddressComponent.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.googlemaps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapsGeocoderResultAddressComponent {

    @SerializedName("long_name") String longName;
    @SerializedName("short_name") String shortName;
    @SerializedName("types") List<String> types;

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }

    public List<String> getTypes() {
        return types;
    }
}
