/*
 * File: Filters.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.search;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link Filters} model for the request search rooms.
 */
@AutoValue
public abstract class Filters implements Parcelable {

    @SerializedName("available_from")
    @Nullable public abstract Date availableFrom();

    @SerializedName("max_price")
    @Nullable public abstract Integer maxPrice();

    @SerializedName("gender")
    @Nullable public abstract String gender();

    @SerializedName("number_of_tenants")
    @Nullable public abstract NumberOfTenants numberOfTenants();

    @SerializedName("bed_types")
    @Nullable public abstract List<Integer> bedTypes();

    @SerializedName("amenities")
    @Nullable public abstract List<Integer> amenities();

    public static Filters create(Date availableFrom, Integer maxPrice, String gender, NumberOfTenants numberOfTenants,
                                 List<Integer> bedTypes, List<Integer> amenities) {
        return new AutoValue_Filters(availableFrom, maxPrice, gender, numberOfTenants, bedTypes, amenities);
    }

    public static Builder builder() {
        return new AutoValue_Filters.Builder();
    }

    public static TypeAdapter<Filters> typeAdapter(Gson gson) {
        return new AutoValue_Filters.GsonTypeAdapter(gson);
    }

    public abstract Builder toBuilder();

    public Filters withAvailableFrom(Date availableFrom) {
        return toBuilder().setAvailableFrom(availableFrom).build();
    }

    public Filters withMaxPrice(Integer maxPrice) {
        return toBuilder().setMaxPrice(maxPrice).build();
    }

    public Filters withBedTypes(List<Integer> bedTypes) {
        return toBuilder().setBedTypes(bedTypes).build();
    }

    public Filters withAmenities(List<Integer> amenities) {
        return toBuilder().setAmenities(amenities).build();
    }

    public Filters withGender(String gender) {
        return toBuilder().setGender(gender).build();
    }

    public Filters withNumberOfTenants(NumberOfTenants numberOfTenants) {
        return toBuilder().setNumberOfTenants(numberOfTenants).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setAvailableFrom(Date date);
        public abstract Builder setMaxPrice(Integer maxPrice);
        public abstract Builder setBedTypes(List<Integer> bedTypes);
        public abstract Builder setAmenities(List<Integer> amenities);
        public abstract Builder setGender(String gender);
        public abstract Builder setNumberOfTenants(NumberOfTenants numberOfTenants);
        public abstract Filters build();
    }
}
