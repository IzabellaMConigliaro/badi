/*
 * File: Language.java
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
 * {@link Language} model used for storing which language the user speaks & the order of preference of if he/she knows
 * more than one.
 */
@AutoValue
public abstract class Language implements Parcelable {

    @SerializedName("language_id")
    public abstract Integer languageId();

    @SerializedName("language_order")
    @Nullable public abstract Integer languageOrder();

    @Nullable public abstract String language();

    public abstract boolean userSpeak();

    public static Language create(Integer languageId, Integer languageOrder, String language, Boolean userSpeak) {
        return new AutoValue_Language(languageId, languageOrder, language, userSpeak);
    }

    public static TypeAdapter<Language> typeAdapter(Gson gson) {
        return new AutoValue_Language.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Language.Builder()
                .setUserSpeak(false);
    }

    public abstract Builder toBuilder();

    public Language withLanguageOrder(Integer languageOrder) {
        return toBuilder().setLanguageOrder(languageOrder).build();
    }

    public Language withUserSpeak(Boolean userSpeak) {
        return toBuilder().setUserSpeak(userSpeak).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLanguageId(Integer languageId);
        public abstract Builder setLanguageOrder(Integer languageOrder);
        public abstract Builder setLanguage(String language);
        public abstract Builder setUserSpeak(boolean userSpeak);
        public abstract Language build();
    }
}
