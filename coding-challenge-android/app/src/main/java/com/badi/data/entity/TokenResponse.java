/*
 * File: TokenResponse.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * Token Response used in the data layer.
 */
@AutoValue
public abstract class TokenResponse {

    @SerializedName("access_token")
    public abstract String accessToken();

    @SerializedName("token_type")
    public abstract String tokenType();

    @SerializedName("expires_in")
    public abstract Integer expiresIn();

    @SerializedName("refresh_token")
    public abstract String refreshToken();

    @SerializedName("created_at")
    public abstract Integer createdAt();

    @SerializedName("user_id")
    public abstract Integer userId();

    @SerializedName("new_user")
    public abstract Boolean newUser();

    @SerializedName("lister")
    public abstract Boolean lister();

    public static TokenResponse create(String accessToken, String tokenType, Integer expiresIn,  String refreshToken,
                                       Integer createdAt, Integer userId, Boolean newUser, Boolean lister) {
        return new AutoValue_TokenResponse(accessToken, tokenType, expiresIn, refreshToken, createdAt, userId, newUser, lister);
    }

    public static TypeAdapter<TokenResponse> typeAdapter(Gson gson) {
        return new AutoValue_TokenResponse.GsonTypeAdapter(gson);
    }
}
