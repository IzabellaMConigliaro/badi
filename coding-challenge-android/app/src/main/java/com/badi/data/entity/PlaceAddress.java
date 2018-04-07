/*
 * File: PlaceAddress.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link PlaceAddress} used for mapping the Place id from google apis
 */
@AutoValue
public abstract class PlaceAddress implements Parcelable {

    @SerializedName("id")
    @Nullable public abstract String id();

    @SerializedName("name")
    @Nullable public abstract String name();

    @SerializedName("address")
    @Nullable public abstract String address();

    @SerializedName("street")
    @Nullable public abstract String street();

    @SerializedName("street_number")
    @Nullable public abstract String streetNumber();

    @SerializedName("city")
    @Nullable public abstract String city();

    @SerializedName("country")
    @Nullable public abstract String country();

    @SerializedName("postal_code")
    @Nullable public abstract String postalCode();

    @SerializedName("currency_code")
    @Nullable public abstract String currencyCode();

    @SerializedName("latitude")
    public abstract double latitude();

    @SerializedName("longitude")
    public abstract double longitude();

    @SerializedName("place_types")
    @Nullable public abstract List<Integer> placeTypes();

    public static PlaceAddress create(String id, String name, String address, String street, String streetNumber, String city,
                                      String country, String postalCode, String currencyCode, double latitude, double longitude,
                                      List<Integer> placeTypes) {
        return new AutoValue_PlaceAddress(id, name, address, street, streetNumber, city, country, postalCode, currencyCode,
                latitude, longitude, placeTypes);
    }

    public static Builder builder() {
        return new AutoValue_PlaceAddress.Builder();
    }

    public abstract Builder toBuilder();

    public PlaceAddress withCity(String city) {
        return toBuilder().setCity(city).build();
    }

    public PlaceAddress withPlaceTypes(List<Integer> placeTypes) {
        return toBuilder().setPlaceTypes(placeTypes).build();
    }

    public static TypeAdapter<PlaceAddress> typeAdapter(Gson gson) {
        return new AutoValue_PlaceAddress.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);
        public abstract Builder setName(String name);
        public abstract Builder setAddress(String address);
        public abstract Builder setStreet(String street);
        public abstract Builder setStreetNumber(String streetNumber);
        public abstract Builder setCity(String city);
        public abstract Builder setCountry(String country);
        public abstract Builder setPostalCode(String postalCode);
        public abstract Builder setCurrencyCode(String currencyCode);
        public abstract Builder setLatitude(double latitude);
        public abstract Builder setLongitude(double longitude);
        public abstract Builder setPlaceTypes(List<Integer> placeTypes);
        public abstract PlaceAddress build();
    }
}
