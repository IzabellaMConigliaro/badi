/*
 * File: BaseUser.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.badi.data.entity.room.RoomDetail;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link UserBase} model used for mapping the base info of one user.
 * It's used in Chat and {@link RoomDetail}
 */
@AutoValue
public abstract class UserBase implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("first_name")
    @Nullable public abstract String firstName();

    @SerializedName("last_name")
    @Nullable public abstract String lastName();

    @SerializedName("birth_date")
    @Nullable public abstract Date birthDate();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    public static UserBase create(Integer id, String firstName, String lastName, Date birthDate, List<Picture> pictures) {
        return new AutoValue_UserBase(id, firstName, lastName, birthDate, pictures);
    }

    public static TypeAdapter<UserBase> typeAdapter(Gson gson) {
        return new AutoValue_UserBase.GsonTypeAdapter(gson);
    }
}
