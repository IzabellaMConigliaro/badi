/*
 * File: PersonalityTraitsAttribute.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link PersonalityTraitsAttribute} model used for storing the personality traits of the user.
 */
@AutoValue
public abstract class PersonalityTraitsAttribute implements Parcelable {

    @SerializedName("personality_trait_id")
    public abstract Integer personalityTraitId();

    @SerializedName("value")
    public abstract Integer value();

    public static PersonalityTraitsAttribute create(Integer personalityTraitId, Integer value) {
        return new AutoValue_PersonalityTraitsAttribute(personalityTraitId, value);
    }

    public static TypeAdapter<PersonalityTraitsAttribute> typeAdapter(Gson gson) {
        return new AutoValue_PersonalityTraitsAttribute.GsonTypeAdapter(gson);
    }

}
