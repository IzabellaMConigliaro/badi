/*
 * File: Social.java
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

import java.util.List;

/**
 * {@link Social} model used for storing the mutual friends & the mutual likes between users
 */
@AutoValue
public abstract class Social implements Parcelable {

    @SerializedName("mutual_friends")
    public abstract List<Mutual> mutualFriends();

    @SerializedName("mutual_likes")
    public abstract List<Mutual> mutualLikes();

    public static Social create(List<Mutual> mutualFriends, List<Mutual> mutualLikes) {
        return new AutoValue_Social(mutualFriends, mutualLikes);
    }

    public static TypeAdapter<Social> typeAdapter(Gson gson) {
        return new AutoValue_Social.GsonTypeAdapter(gson);
    }
}