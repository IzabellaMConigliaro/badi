/*
 * File: Mutual.java
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
 * {@link Mutual} model used for storing the id and name of mutual friends or mutual likes.
 */
@AutoValue
public abstract class Mutual implements Parcelable {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("name")
    public abstract String name();

    public static Mutual create(String id, String name) {
        return new AutoValue_Mutual(id, name);
    }

    public static TypeAdapter<Mutual> typeAdapter(Gson gson) {
        return new AutoValue_Mutual.GsonTypeAdapter(gson);
    }

    /**
     *
     * @return
     * The imageUrl
     */
    public String getImageUrl() {
        return "https://graph.facebook.com/" + id() + "/picture?type=large";
    }
}
