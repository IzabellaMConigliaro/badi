/*
 * File: APIError.java
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
 * API Error Basic Response used in the data layer used to parse the errorBody from the API
 */

@AutoValue
public abstract class APIError {

    @SerializedName("error")
    public abstract Error error();

    public static APIError create(Error error) {
        return new AutoValue_APIError(error);
    }

    public static TypeAdapter<APIError> typeAdapter(Gson gson) {
        return new AutoValue_APIError.GsonTypeAdapter(gson);
    }

}
