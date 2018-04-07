/*
 * File: Coordinates.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.search;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link Coordinates} model gives a custom latitude and longitude & max distance.
 * It's used in {@link SearchRooms} model.
 */
@AutoValue
public abstract class Coordinates implements Parcelable {

    @SerializedName("latitude")
    public abstract Double latitude();

    @SerializedName("longitude")
    public abstract Double longitude();

    public static Coordinates create(Double latitude, Double longitude) {
        return new AutoValue_Coordinates(latitude, longitude);
    }

    public static Builder builder() {
        return new AutoValue_Coordinates.Builder();
    }

    public static TypeAdapter<Coordinates> typeAdapter(Gson gson) {
        return new AutoValue_Coordinates.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLatitude(Double latitude);
        public abstract Builder setLongitude(Double longitude);
        public abstract Coordinates build();
    }
}
