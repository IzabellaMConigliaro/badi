/*
 * File: RoomRental.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * {@link RoomRental} model used for mapping the request of a room rental with the server.
 */
@AutoValue
public abstract class RoomRental {

    @SerializedName("rented_to_user")
    @Nullable
    public abstract Integer rentedToUser();

    @SerializedName("rented_to_date")
    @Nullable
    public abstract Date rentedToDate();

    public static RoomRental create(Integer rentedToUser, Date rentedToDate) {
        return new AutoValue_RoomRental(rentedToUser, rentedToDate);
    }

    public static TypeAdapter<RoomRental> typeAdapter(Gson gson) {
        return new AutoValue_RoomRental.GsonTypeAdapter(gson);
    }
}
