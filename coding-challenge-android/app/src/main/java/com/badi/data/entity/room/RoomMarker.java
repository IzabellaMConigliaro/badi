/*
 * File: RoomMarker.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.badi.data.entity.user.Picture;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link RoomMarker} model to return the room id and position
 */
@AutoValue
public abstract class RoomMarker implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("latitude")
    public abstract Double latitude();

    @SerializedName("longitude")
    public abstract Double longitude();


    public static RoomMarker create(Integer id, Double latitude, Double longitude) {
        return new AutoValue_RoomMarker(id, latitude, longitude);
    }

    public static TypeAdapter<RoomMarker> typeAdapter(Gson gson) {
        return new AutoValue_RoomMarker.GsonTypeAdapter(gson);
    }

}
