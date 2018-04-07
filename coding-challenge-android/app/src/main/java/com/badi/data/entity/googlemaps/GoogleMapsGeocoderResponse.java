/*
 * File: GoogleMapsGeocoderResponse.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.googlemaps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapsGeocoderResponse {

    @SerializedName("results")
    private List<GoogleMapsGeocoderResult> results;

    public List<GoogleMapsGeocoderResult> getResults() {
        return results;
    }
}
