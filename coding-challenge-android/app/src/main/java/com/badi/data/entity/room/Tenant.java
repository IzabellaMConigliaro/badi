/*
 * File: Tenant.java
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
 * {@link Tenant} model provides information about the tenant of the room. It's used inside the {@link Room} model.
 */
@AutoValue
public abstract class Tenant implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("first_name")
    @Nullable public abstract String firstName();

    @SerializedName("last_name")
    @Nullable public abstract String lastName();

    @SerializedName("birth_date")
    @Nullable public abstract Date birthDate();

    @SerializedName("biological_sex")
    @Nullable public abstract Integer biologicalSex();

    @SerializedName("occupation")
    @Nullable public abstract Integer occupation();

    @SerializedName("city_of_residence")
    @Nullable public abstract String cityOfResidence();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    @SerializedName("verified_account")
    public abstract Boolean verifiedAccount();

    public static Tenant create(Integer id, String firstName, String lastName, Date birthDate, Integer biologicalSex,
                                Integer occupation, String cityOfResidence, List<Picture> pictures, Boolean verifiedAccount) {
        return new AutoValue_Tenant(id, firstName, lastName, birthDate, biologicalSex, occupation, cityOfResidence,
                pictures, verifiedAccount);
    }

    public static TypeAdapter<Tenant> typeAdapter(Gson gson) {
        return new AutoValue_Tenant.GsonTypeAdapter(gson);
    }
}
