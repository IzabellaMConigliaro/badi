/*
 * File: GoogleMapsGeocoderResult.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.googlemaps;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoogleMapsGeocoderResult {

    @SerializedName("address_components")
    private List<GoogleMapsGeocoderResultAddressComponent> addressComponents;

    @SerializedName("formatted_address")
    private String formattedAddress;

    @SerializedName("geometry")
    private GoogleMapsGeocoderResultGeometry geometry;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("types")
    private List<String> types;

    public List<GoogleMapsGeocoderResultAddressComponent> getAddressComponents() {
        return addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public GoogleMapsGeocoderResultGeometry getGeometry() {
        return geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public List<String> getTypes() {
        return types;
    }
}
