/*
 * File: RoomUpload.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * {@link RoomUpload} model to return the room upload model to create room
 *
 * Possible types status:{"published"=>1, "unpublished"=>2, "rented"=>4, "booked"=>5}
 * Possible types bedType: {"sofa_bed"=>1, "single_bed"=>2, "double_bed"=>3}
 */
@AutoValue
public abstract class RoomUpload implements Parcelable {

    @SerializedName("id")
    @Nullable public abstract Integer id();

    @SerializedName("status")
    @Nullable public abstract Integer status();

    @SerializedName("bed_type")
    @Nullable public abstract Integer bedType();

    @SerializedName("description")
    @Nullable public abstract String description();

    @SerializedName("title")
    @Nullable public abstract String title();

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

    @SerializedName("latitude")
    @Nullable public abstract Double latitude();

    @SerializedName("longitude")
    @Nullable public abstract Double longitude();

    @SerializedName("available_from")
    @Nullable public abstract Date availableFrom();

    @SerializedName("available_to")
    @Nullable public abstract Date availableTo();

    @SerializedName("min_days")
    @Nullable public abstract Integer minDays();

    @SerializedName("undefined_tenants")
    @Nullable public abstract Integer undefinedTenants();

    @SerializedName("male_tenants")
    @Nullable public abstract Integer maleTenants();

    @SerializedName("female_tenants")
    @Nullable public abstract Integer femaleTenants();

    @SerializedName("tenants")
    @Nullable public abstract List<Integer> tenants();

    @SerializedName("amenities_attributes")
    @Nullable public abstract List<AmenitiesAttribute> amenitiesAttributes();

    @SerializedName("pictures")
    @Nullable public abstract List<Integer> pictures();

    @SerializedName("prices_attributes")
    @Nullable public abstract List<PricesAttribute> pricesAttributes();


    public static RoomUpload create(Integer id, Integer status, Integer bedType, String description, String title, String address,
                                    String street, String streetNumber, String city, String country, String postalCode,
                                    Double latitude, Double longitude, Date availableFrom, Date availableTo, Integer minDays,
                                    Integer undefinedTenants, Integer maleTenants, Integer femaleTenants, List<Integer> tenants,
                                    List<AmenitiesAttribute> amenitiesAttributes, List<Integer> pictures,
                                    List<PricesAttribute> pricesAttributes) {
        return new AutoValue_RoomUpload(id, status, bedType, description, title, address, street, streetNumber, city, country,
                postalCode, latitude, longitude, availableFrom, availableTo, minDays, undefinedTenants, maleTenants,
                femaleTenants, tenants, amenitiesAttributes, pictures, pricesAttributes);
    }

    public static TypeAdapter<RoomUpload> typeAdapter(Gson gson) {
        return new AutoValue_RoomUpload.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_RoomUpload.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setStatus(Integer status);
        public abstract Builder setBedType(Integer bedType);
        public abstract Builder setTitle(String title);
        public abstract Builder setDescription(String description);
        public abstract Builder setAddress(String address);
        public abstract Builder setStreet(String street);
        public abstract Builder setStreetNumber(String streetNumber);
        public abstract Builder setCity(String city);
        public abstract Builder setCountry(String country);
        public abstract Builder setPostalCode(String postalCode);
        public abstract Builder setLatitude(Double latitude);
        public abstract Builder setLongitude(Double longitude);
        public abstract Builder setAvailableFrom(Date availableFrom);
        public abstract Builder setAvailableTo(Date availableTo);
        public abstract Builder setMinDays(Integer minDays);
        public abstract Builder setUndefinedTenants(Integer undefinedTenants);
        public abstract Builder setMaleTenants(Integer maleTenants);
        public abstract Builder setFemaleTenants(Integer femaleTenants);
        public abstract Builder setTenants(List<Integer> tenants);
        public abstract Builder setAmenitiesAttributes(List<AmenitiesAttribute> amenitiesAttributes);
        public abstract Builder setPictures(List<Integer> pictures);
        public abstract Builder setPricesAttributes(List<PricesAttribute> pricesAttributes);
        public abstract RoomUpload build();
    }
}
