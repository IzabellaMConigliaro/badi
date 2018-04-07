/*
 * File: Bounds.java
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
 * {@link Bounds} model gives a custom northEast and southWest coordinates of a map.
 * It's used in {@link SearchRooms} model.
 */
@AutoValue
public abstract class Bounds {

    @SerializedName("ne")
    public abstract CustomLatLng northEast();

    @SerializedName("sw")
    public abstract CustomLatLng southWest();

    public static Bounds create(CustomLatLng northEast, CustomLatLng southWest) {
        return new AutoValue_Bounds(northEast, southWest);
    }

    public static TypeAdapter<Bounds> typeAdapter(Gson gson) {
        return new AutoValue_Bounds.GsonTypeAdapter(gson);
    }
}
