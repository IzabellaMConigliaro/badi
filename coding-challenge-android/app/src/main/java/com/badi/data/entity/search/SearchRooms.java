/*
 * File: SearchRooms.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.search;

import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * {@link SearchRooms} model for the request search rooms by cities (initial screen with the predefined cities).
 *
 * Bounds are the northEast (upper right corner) and the southWest (down left corner) af the current visible map.
 * City is the string variable with the name of the city, i.e: Barcelona
 * The sortBy vale by default is: "published_at desc"
 *
 * The Coordinates value are the default value (LatLng) from the Google Maps Api console.
 * The sortBy vale by default is: "distance asc"
 */
@AutoValue
public abstract class SearchRooms {

    //Different options one at a time
    @SerializedName("bounds")
    @Nullable public abstract Bounds bounds();

    @SerializedName("location")
    @Nullable public abstract Location location();

    @SerializedName("coordinates")
    @Nullable public abstract Coordinates coordinates();



    @SerializedName("sort_by")
    @Nullable public abstract String sortBy();

    @SerializedName("available_from")
    @Nullable public abstract Date availableFrom();

    @SerializedName("gender")
    @Nullable public abstract String gender();

    @SerializedName("number_of_tenants")
    @Nullable public abstract NumberOfTenants numberOfTenants();

    @SerializedName("bed_types")
    @Nullable public abstract List<Integer> bedTypes();

    @SerializedName("amenities")
    @Nullable public abstract List<Integer> amenities();

    @SerializedName("price_types")
    @Nullable public abstract List<Integer> priceTypes();

    @SerializedName("max_price")
    @Nullable public abstract Integer maxPrice();

    @SerializedName("new_search_mode")
    @Nullable public abstract Boolean newSearchMode();

    @SerializedName("page")
    public abstract Integer page();

    @SerializedName("per")
    @Nullable public abstract Integer per();

    @SerializedName("offset")
    public abstract Integer offset();

    public static SearchRooms create(Bounds bounds, Location location, Coordinates coordinates, String sortBy, Date availableFrom,
                                     String gender, NumberOfTenants numberOfTenants, List<Integer> bedTypes, List<Integer>
                                             amenities, List<Integer> priceTypes, Integer maxPrice, Boolean newSearchMode,
                                     Integer page, Integer per, Integer offset) {
        return new AutoValue_SearchRooms(bounds, location, coordinates, sortBy, availableFrom, gender, numberOfTenants, bedTypes,
                amenities, priceTypes, maxPrice, newSearchMode, per, page, offset);
    }

    public static Builder builder() {
        return new AutoValue_SearchRooms.Builder();
    }

    public static TypeAdapter<SearchRooms> typeAdapter(Gson gson) {
        return new AutoValue_SearchRooms.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setBounds(Bounds bounds);
        public abstract Builder setLocation(Location location);
        public abstract Builder setCoordinates(Coordinates coordinates);

        public abstract Builder setSortBy(String sortBy);
        public abstract Builder setAvailableFrom(Date availableFrom);
        public abstract Builder setGender(String gender);
        public abstract Builder setNumberOfTenants(NumberOfTenants numberOfTenants);
        public abstract Builder setBedTypes(List<Integer> bedTypes);
        public abstract Builder setAmenities(List<Integer> amenities);
        public abstract Builder setPriceTypes(List<Integer> priceTypes);
        public abstract Builder setMaxPrice(Integer maxPrice);
        public abstract Builder setNewSearchMode(Boolean newSearchMode);
        public abstract Builder setPage(Integer page);
        public abstract Builder setPer(Integer per);
        public abstract Builder setOffset(Integer offset);
        public abstract SearchRooms build();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object obj) {
        SearchRooms other = (SearchRooms) obj;
        return Objects.equals(other.bounds(), this.bounds())
                && Objects.equals(other.location(), this.location())
                && Objects.equals(other.coordinates(), this.coordinates())
                && Objects.equals(other.sortBy(), this.sortBy())
                && Objects.equals(other.availableFrom(), this.availableFrom())
                && Objects.equals(other.gender(), this.gender())
                && Objects.equals(other.bedTypes(), this.bedTypes())
                && Objects.equals(other.amenities(), this.amenities())
                && Objects.equals(other.priceTypes(), this.priceTypes())
                && Objects.equals(other.maxPrice(), this.maxPrice())
                && Objects.equals(other.newSearchMode(), this.newSearchMode())
                && Objects.equals(other.page(), this.page())
                && Objects.equals(other.per(), this.per())
                && Objects.equals(other.offset(), this.offset());
    }
}
