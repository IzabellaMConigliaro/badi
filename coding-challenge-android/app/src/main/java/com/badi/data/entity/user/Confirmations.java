/*
 * File: Confirmations.java
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
 * {@link Confirmations} model used for checking if the account of the user has been confirmed with the following options
 */

@AutoValue
public abstract class Confirmations implements Parcelable {

    @SerializedName("email")
    public abstract Boolean email();

    @SerializedName("phone")
    public abstract Boolean phone();

    @SerializedName("facebook")
    public abstract Boolean facebook();

    @SerializedName("google")
    public abstract Boolean google();


    public static Confirmations create(Boolean email, Boolean phone, Boolean facebook, Boolean google) {
        return new AutoValue_Confirmations(email, phone, facebook, google);
    }

    public static TypeAdapter<Confirmations> typeAdapter(Gson gson) {
        return new AutoValue_Confirmations.GsonTypeAdapter(gson);
    }

    public abstract Builder toBuilder();

    public Confirmations withEmail(Boolean email) {
        return toBuilder().setEmail(email).build();
    }

    public Confirmations withPhone(Boolean phone) {
        return toBuilder().setPhone(phone).build();
    }

    public Confirmations withFacebook(Boolean facebook) {
        return toBuilder().setFacebook(facebook).build();
    }

    public Confirmations withGoogle(Boolean google) {
        return toBuilder().setGoogle(google).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setEmail(Boolean email);
        public abstract Builder setPhone(Boolean phone);
        public abstract Builder setFacebook(Boolean facebook);
        public abstract Builder setGoogle(Boolean google);
        public abstract Confirmations build();
    }
}
