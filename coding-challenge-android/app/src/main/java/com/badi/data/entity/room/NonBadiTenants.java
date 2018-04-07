/*
 * File: NonBadiTenants.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link NonBadiTenants} model provides information about the non badi tenants of the room.
 * It's used inside the {@link RoomDetail} model.
 */
@AutoValue
public abstract class NonBadiTenants implements Parcelable {

    @SerializedName("undefined_tenants")
    public abstract Integer undefinedTenants();

    @SerializedName("male_tenants")
    public abstract Integer maleTenants();

    @SerializedName("female_tenants")
    public abstract Integer femaleTenants();

    public static NonBadiTenants create(Integer undefinedTenants, Integer maleTenants, Integer femaleTenants) {
        return new AutoValue_NonBadiTenants(undefinedTenants, maleTenants, femaleTenants);
    }

    public static TypeAdapter<NonBadiTenants> typeAdapter(Gson gson) {
        return new AutoValue_NonBadiTenants.GsonTypeAdapter(gson);
    }

    public abstract Builder toBuilder();

    public NonBadiTenants withUndefinedTenants(Integer undefinedTenants) {
        return toBuilder().setUndefinedTenants(undefinedTenants).build();
    }

    public NonBadiTenants withMaleTenants(Integer maleTenants) {
        return toBuilder().setMaleTenants(maleTenants).build();
    }

    public NonBadiTenants withFemaleTenants(Integer femaleTenants) {
        return toBuilder().setFemaleTenants(femaleTenants).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setUndefinedTenants(Integer undefinedTenants);
        public abstract Builder setMaleTenants(Integer maleTenants);
        public abstract Builder setFemaleTenants(Integer femaleTenants);
        public abstract NonBadiTenants build();
    }
}
