/*
 * File: Picture.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.user;

import android.os.Parcelable;

import com.badi.data.entity.room.RoomDetail;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link Picture} model used for storing the id and url of a picture. This model is used in the following classes:
 * {@link RoomDetail} and {@link User}
 */
@AutoValue
public abstract class Picture implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("url")
    public abstract String url();

    @SerializedName("original_url")
    public abstract String originalUrl();

    @SerializedName("width_27_url")
    public abstract String width27Url();

    @SerializedName("width_100_url")
    public abstract String width100Url();

    @SerializedName("width_500_url")
    public abstract String width500Url();

    @SerializedName("width_1080_url")
    public abstract String width1080Url();

    public static Picture create(Integer id, String url, String originalUrl, String width27Url, String width100Url,
                                 String width500Url, String width1080Url) {
        return new AutoValue_Picture(id, url, originalUrl, width27Url, width100Url, width500Url, width1080Url);
    }

    public static TypeAdapter<Picture> typeAdapter(Gson gson) {
        return new AutoValue_Picture.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_Picture.Builder()
                .setOriginalUrl("")
                .setWidth27Url("")
                .setWidth100Url("")
                .setWidth500Url("")
                .setWidth1080Url("");
    }

    public abstract Builder toBuilder();

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setUrl(String url);
        public abstract Builder setOriginalUrl(String originalUrl);
        public abstract Builder setWidth27Url(String width27Url);
        public abstract Builder setWidth100Url(String width100Url);
        public abstract Builder setWidth500Url(String width500Url);
        public abstract Builder setWidth1080Url(String width1080Url);
        public abstract Picture build();
    }
}
