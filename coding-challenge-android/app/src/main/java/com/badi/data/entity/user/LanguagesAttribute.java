/*
 * File: LanguagesAttribute.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link LanguagesAttribute} model used for storing which language the user speaks & the order of preference of if he/she knows
 * more than one.
 */
@AutoValue
public abstract class LanguagesAttribute implements Parcelable {

    @SerializedName("language_id")
    public abstract Integer languageId();

    @SerializedName("language_order")
    @Nullable public abstract Integer languageOrder();

    public static LanguagesAttribute create(Integer languageId, Integer languageOrder) {
        return new AutoValue_LanguagesAttribute(languageId, languageOrder);
    }

    public static TypeAdapter<LanguagesAttribute> typeAdapter(Gson gson) {
        return new AutoValue_LanguagesAttribute.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_LanguagesAttribute.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLanguageId(Integer languageId);
        public abstract Builder setLanguageOrder(Integer languageOrder);
        public abstract LanguagesAttribute build();
    }
}
