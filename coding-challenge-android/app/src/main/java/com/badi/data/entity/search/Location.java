/*
 * File: Location.java
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
 * {@link Location} model gives a custom query (address) and placeID to add in a search request
 * It's used in {@link SearchRooms} model.
 */
@AutoValue
public abstract class Location implements Parcelable {

    @SerializedName("q")
    public abstract String address();

    @SerializedName("place_id")
    public abstract String placeID();

    public static Location create(String address, String placeID) {
        return new AutoValue_Location(address, placeID);
    }

    public static Builder builder() {
        return new AutoValue_Location.Builder();
    }

    public static TypeAdapter<Location> typeAdapter(Gson gson) {
        return new AutoValue_Location.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setAddress(String address);
        public abstract Builder setPlaceID(String placeID);
        public abstract Location build();
    }
}
