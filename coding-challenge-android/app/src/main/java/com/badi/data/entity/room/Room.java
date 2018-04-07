/*
 * File: Room.java
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
 * {@link Room} model to return the room details
 */
@AutoValue
public abstract class Room implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("status")
    public abstract Integer status();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("description")
    @Nullable public abstract String description();

    @SerializedName("city")
    @Nullable public abstract String city();

    @SerializedName("latitude")
    public abstract Double latitude();

    @SerializedName("longitude")
    public abstract Double longitude();

    @SerializedName("distance")
    @Nullable public abstract Double distance();

    @SerializedName("favourite")
    public abstract boolean favourite();

    @SerializedName("requested")
    public abstract boolean requested();

    @SerializedName("owned")
    public abstract boolean owned();

    @SerializedName("allowed_to_book")
    public abstract boolean allowedToBook();

    @SerializedName("available_from")
    public abstract Date availableFrom();

    @SerializedName("published_at")
    public abstract Date publishedAt();

    @SerializedName("tenants")
    public abstract List<Tenant> tenants();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    @SerializedName("prices_attributes")
    public abstract List<PricesAttribute> pricesAttributes();

    public static Room create(Integer id, Integer status, String title, String description, String city, Double latitude,
                              Double longitude, Double distance, boolean favourite, boolean requested, boolean owned,
                              boolean allowedToBook, Date availableFrom, Date publishedAt, List<Tenant> tenants,
                              List<Picture> pictures, List<PricesAttribute> pricesAttributes) {
        return new AutoValue_Room(id, status, title, description, city, latitude, longitude, distance, favourite, requested,
                owned, allowedToBook, availableFrom, publishedAt, tenants, pictures, pricesAttributes);
    }

    public abstract Builder toBuilder();

    public Room withRequested(boolean requested) {
        return toBuilder().setRequested(requested).build();
    }

    public static TypeAdapter<Room> typeAdapter(Gson gson) {
        return new AutoValue_Room.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setStatus(Integer status);
        public abstract Builder setTitle(String title);
        public abstract Builder setDescription(String description);
        public abstract Builder setCity(String city);
        public abstract Builder setLatitude(Double latitude);
        public abstract Builder setLongitude(Double longitude);
        public abstract Builder setDistance(Double distance);
        public abstract Builder setFavourite(boolean favourite);
        public abstract Builder setRequested(boolean requested);
        public abstract Builder setOwned(boolean owned);
        public abstract Builder setAllowedToBook(boolean allowedToBook);
        public abstract Builder setAvailableFrom(Date availableFrom);
        public abstract Builder setPublishedAt(Date publishedAt);
        public abstract Builder setTenants(List<Tenant> tenants);
        public abstract Builder setPictures(List<Picture> pictures);
        public abstract Builder setPricesAttributes(List<PricesAttribute> pricesAttributes);
        public abstract Room build();
    }
}
