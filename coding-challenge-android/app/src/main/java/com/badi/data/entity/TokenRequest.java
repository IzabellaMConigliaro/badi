/*
 * File: TokenRequest.java
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
 * {@link TokenRequest} used for mapping the JSON Request to the server.
 */
@AutoValue
public abstract class TokenRequest {

    @SerializedName("refresh_token")
    public abstract String refreshToken();

    @SerializedName("grant_type")
    public abstract String grantType();

    public static TokenRequest create(String refreshToken, String grantType) {
        return new AutoValue_TokenRequest(refreshToken, grantType);
    }

    public static TypeAdapter<TokenRequest> typeAdapter(Gson gson) {
        return new AutoValue_TokenRequest.GsonTypeAdapter(gson);
    }
}
