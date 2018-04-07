/*
 * File: BaseRoom.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.os.Parcelable;

import com.badi.data.entity.user.Picture;
import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {@link RoomBase} model to return the base room details for booking
 */
@AutoValue
public abstract class RoomBase implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("status")
    public abstract Integer status();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("owned")
    public abstract boolean owned();

    @SerializedName("allowed_to_book")
    public abstract boolean allowedToBook();

    @SerializedName("pictures")
    public abstract List<Picture> pictures();

    @SerializedName("prices_attributes")
    public abstract List<PricesAttribute> pricesAttributes();

    public static RoomBase create(Integer id, Integer status, String title, boolean owned, boolean allowedToBook,
                                  List<Picture> pictures, List<PricesAttribute> pricesAttributes) {
        return new AutoValue_RoomBase(id, status, title,  owned, allowedToBook, pictures, pricesAttributes);
    }

    public abstract Builder toBuilder();

    public static TypeAdapter<RoomBase> typeAdapter(Gson gson) {
        return new AutoValue_RoomBase.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(Integer id);
        public abstract Builder setStatus(Integer status);
        public abstract Builder setTitle(String title);
        public abstract Builder setOwned(boolean owned);
        public abstract Builder setAllowedToBook(boolean allowedToBook);
        public abstract Builder setPictures(List<Picture> pictures);
        public abstract Builder setPricesAttributes(List<PricesAttribute> pricesAttributes);
        public abstract RoomBase build();
    }
}
