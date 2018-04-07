/*
 * File: RoomDetail.java
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * {@link RoomDetail} model to return the room details
 *
 * Possible types status:{"published"=>1, "unpublished"=>2, "rented"=>4, "booked"=>5}
 * Possible types bedType: {"sofa_bed"=>1, "single_bed"=>2, "double_bed"=>3}
 */
@AutoValue
public abstract class RoomDetail implements Parcelable {

    public static final int STATUS_PUBLISHED = 1;
    public static final int STATUS_UNPUBLISHED = 2;
    public static final int STATUS_RENTED = 4;
    public static final int STATUS_BOOKED = 5;

    public static final int BED_TYPE_SOFA_BED = 1;
    public static final int BED_TYPE_SINGLE_BED = 2;
    public static final int BED_TYPE_DOUBLE_BED = 3;

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("status")
    public abstract Integer status();

    @SerializedName("bed_type")
    public abstract Integer bedType();

    @SerializedName("description")
    @Nullable public abstract String description();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("address")
    public abstract String address();

    @SerializedName("display_address")
    public abstract String displayAddress();

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
    public abstract Double latitude();

    @SerializedName("longitude")
    public abstract Double longitude();

    @SerializedName("available_from")
    public abstract Date availableFrom();

    @SerializedName("available_to")
    @Nullable public abstract Date availableTo();

    @SerializedName("published_at")
    public abstract Date publishedAt();

    @SerializedName("min_days")
    @Nullable public abstract Integer minDays();

    @SerializedName("max_days")
    @Nullable public abstract Integer maxDays();

    @SerializedName("tenants_number")
    public abstract Integer tenantsNumber();

    @SerializedName("non_badi_tenants")
    public abstract NonBadiTenants nonBadiTenants();

    @SerializedName("tenants")
    public abstract List<Tenant> tenants();

    @SerializedName("amenities_attributes")
    public abstract List<AmenitiesAttribute> amenitiesAttributes();

    @SerializedName("prices_attributes")
    public abstract List<PricesAttribute> pricesAttributes();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    @SerializedName("favourite")
    public abstract Boolean favourite();

    @SerializedName("requested")
    public abstract Boolean requested();

    @SerializedName("owned")
    public abstract Boolean owned();

    @SerializedName("allowed_to_book")
    public abstract Boolean allowedToBook();

    @SerializedName("allowed_to_edit")
    public abstract Boolean allowedToEdit();

    @SerializedName("has_booking_activity")
    public abstract Boolean hasBookingActivity();

    public static RoomDetail create(Integer id, Integer status, Integer bedType, String description, String title, String address,
                                    String displayAddress, String street, String streetNumber, String city, String country,
                                    String postalCode, Double latitude, Double longitude, Date availableFrom, Date availableTo,
                                    Date publishedAt, Integer minDays, Integer maxDays, Integer tenantsNumber,
                                    NonBadiTenants nonBadiTenants, List<Tenant> tenants,
                                    List<AmenitiesAttribute> amenitiesAttributes, List<PricesAttribute> pricesAttributes,
                                    List<Picture> pictures, Boolean requested, Boolean favourite, Boolean owned,
                                    Boolean allowedToBook, Boolean allowedToEdit, Boolean hasBookingActivity) {
        return new AutoValue_RoomDetail(id, status, bedType, description, title, address, displayAddress, street, streetNumber,
                city, country, postalCode, latitude, longitude, availableFrom, availableTo, publishedAt, minDays, maxDays,
                tenantsNumber, nonBadiTenants, tenants, amenitiesAttributes, pricesAttributes, pictures, requested, favourite,
                owned, allowedToBook, allowedToEdit, hasBookingActivity);
    }

    public static TypeAdapter<RoomDetail> typeAdapter(Gson gson) {
        return new AutoValue_RoomDetail.GsonTypeAdapter(gson);
    }

    public abstract Builder toBuilder();

    public RoomDetail withId(Integer id) {
        return toBuilder().setId(id).build();
    }

    public RoomDetail withStatus(Integer status) {
        return toBuilder().setStatus(status).build();
    }

    public RoomDetail withBedType(Integer bedType) {
        return toBuilder().setBedType(bedType).build();
    }

    public RoomDetail withTitle(String title) {
        return toBuilder().setTitle(title).build();
    }

    public RoomDetail withDescription(String description) {
        return toBuilder().setDescription(description).build();
    }

    public RoomDetail withAddress(String address) {
        return toBuilder().setAddress(address).build();
    }

    public RoomDetail withDisplayAddress(String displayAddress) {
        return toBuilder().setDisplayAddress(displayAddress).build();
    }

    public RoomDetail withStreet(String street) {
        return toBuilder().setStreet(street).build();
    }

    public RoomDetail withStreetNumber(String streetNumber) {
        return toBuilder().setStreetNumber(streetNumber).build();
    }

    public RoomDetail withCity(String city) {
        return toBuilder().setCity(city).build();
    }

    public RoomDetail withCountry(String country) {
        return toBuilder().setCountry(country).build();
    }

    public RoomDetail withPostalCode(String postalCode) {
        return toBuilder().setPostalCode(postalCode).build();
    }

    public RoomDetail withLatitude(Double latitude) {
        return toBuilder().setLatitude(latitude).build();
    }

    public RoomDetail withLongitude(Double longitude) {
        return toBuilder().setLongitude(longitude).build();
    }

    public RoomDetail withAvailableFrom(Date availableFrom) {
        return toBuilder().setAvailableFrom(availableFrom).build();
    }

    public RoomDetail withAvailableTo(Date availableTo) {
        return toBuilder().setAvailableTo(availableTo).build();
    }

    public RoomDetail withMinDays(Integer minDays) {
        return toBuilder().setMinDays(minDays).build();
    }

    public RoomDetail withNonBadiTenants(NonBadiTenants nonBadiTenants) {
        return toBuilder().setNonBadiTenants(nonBadiTenants).build();
    }

    public RoomDetail withTenants(List<Tenant> tenants) {
        return toBuilder().setTenants(tenants).build();
    }

    public RoomDetail withAmenitiesAttributes(List<AmenitiesAttribute> amenitiesAttributes) {
        return toBuilder().setAmenitiesAttributes(amenitiesAttributes).build();
    }

    public RoomDetail withPictures(List<Picture> pictures) {
        return toBuilder().setPictures(pictures).build();
    }

    public RoomDetail withPricesAttributes(List<PricesAttribute> pricesAttributes) {
        return toBuilder().setPricesAttributes(pricesAttributes).build();
    }

    public RoomDetail withFavourite(Boolean favourite) {
        return toBuilder().setFavourite(favourite).build();
    }

    public RoomDetail withRequested(Boolean requested) {
        return toBuilder().setRequested(requested).build();
    }

    public RoomDetail withOwned(Boolean owned) {
        return toBuilder().setOwned(owned).build();
    }

    public static RoomDetail initRoomDetail() {
        return RoomDetail.create(0, 0, 0, null, "", "", "", "", "", "", "", "", 0.0, 0.0, new Date(), null,
                new Date(), 0, 0, 0, NonBadiTenants.create(0, 0, 0), new ArrayList<Tenant>(), new ArrayList<AmenitiesAttribute>(),
                new ArrayList<PricesAttribute>(), new ArrayList<Picture>(), false, false, false, false, false, false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setStatus(Integer status);
        public abstract Builder setBedType(Integer bedType);
        public abstract Builder setTitle(String title);
        public abstract Builder setDescription(String description);
        public abstract Builder setAddress(String address);
        public abstract Builder setDisplayAddress(String displayAddress);
        public abstract Builder setStreet(String street);
        public abstract Builder setStreetNumber(String streetNumber);
        public abstract Builder setCity(String city);
        public abstract Builder setCountry(String country);
        public abstract Builder setPostalCode(String postalCode);
        public abstract Builder setLatitude(Double latitude);
        public abstract Builder setLongitude(Double longitude);
        public abstract Builder setAvailableFrom(Date availableFrom);
        public abstract Builder setAvailableTo(Date availableTo);
        public abstract Builder setPublishedAt(Date publishedAt);
        public abstract Builder setMinDays(Integer minDays);
        public abstract Builder setMaxDays(Integer maxDays);
        public abstract Builder setTenantsNumber(Integer tenantsNumber);
        public abstract Builder setNonBadiTenants(NonBadiTenants nonBadiTenants);
        public abstract Builder setTenants(List<Tenant> tenants);
        public abstract Builder setAmenitiesAttributes(List<AmenitiesAttribute> amenitiesAttributes);
        public abstract Builder setPricesAttributes(List<PricesAttribute> pricesAttributes);
        public abstract Builder setPictures(List<Picture> pictures);
        public abstract Builder setFavourite(Boolean favourite);
        public abstract Builder setRequested(Boolean requested);
        public abstract Builder setOwned(Boolean owned);
        public abstract Builder setAllowedToBook(Boolean allowedToBook);
        public abstract Builder setAllowedToEdit(Boolean allowedToEdit);
        public abstract Builder setHasBookingActivity(Boolean hasBookingActivity);
        public abstract RoomDetail build();
    }

}
