/*
 * File: CustomLatLng.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.search;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link CustomLatLng} model gives a custom latitude and longitude. It's used in {@link Bounds} model.
 */
@AutoValue
public abstract class CustomLatLng {

    @SerializedName("lat")
    public abstract Double lat();

    @SerializedName("lng")
    public abstract Double lng();

    public static CustomLatLng create(Double lat, Double lng) {
        return new AutoValue_CustomLatLng(lat, lng);
    }

    public static TypeAdapter<CustomLatLng> typeAdapter(Gson gson) {
        return new AutoValue_CustomLatLng.GsonTypeAdapter(gson);
    }
}
