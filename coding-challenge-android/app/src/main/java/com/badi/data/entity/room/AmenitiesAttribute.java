/*
 * File: AmenitiesAttribute.java
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
 * {@link AmenitiesAttribute} model with an amenities id type.
 * It's used inside the {@link RoomDetail} model.
 * Array of amenities with the following attributes: amenity_type: We add "bed"=>0
 * {"tv"=>1, "wifi"=>2, "air_conditioner"=>3, "parking"=>4, "smoker_friendly"=>5, "pet_friendly"=>6, "elevator"=>7, "heating"=>8,
 * "washing_machine"=>9, "dryer"=>10, "transportation_nearby"=>11, "doorman"=>12, "couples_accepted"=>13, "furnished"=>14,
 * "pool"=>15, "own_bathroom"=>16, "terrace"=>17, "window"=>18, "dishwasher"=>19, "wheelchair"=> 20}
 */
@AutoValue
public abstract class AmenitiesAttribute implements Parcelable {

    @SerializedName("amenity_type")
    public abstract Integer amenityType();

    public static AmenitiesAttribute create(Integer amenityType) {
        return new AutoValue_AmenitiesAttribute(amenityType);
    }

    public static TypeAdapter<AmenitiesAttribute> typeAdapter(Gson gson) {
        return new AutoValue_AmenitiesAttribute.GsonTypeAdapter(gson);
    }
}
