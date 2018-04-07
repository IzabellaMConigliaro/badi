/*
 * File: PictureUpload.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link PictureUpload} used for mapping the JSON Request picture upload to the server.
 */
@AutoValue
public abstract class PictureUpload {

    @SerializedName("url")
    @Nullable public abstract String url();

    @SerializedName("file")
    public abstract String file();

    public static PictureUpload create(String url, String file) {
        return new AutoValue_PictureUpload(url, file);
    }

    public static TypeAdapter<PictureUpload> typeAdapter(Gson gson) {
        return new AutoValue_PictureUpload.GsonTypeAdapter(gson);
    }
}
