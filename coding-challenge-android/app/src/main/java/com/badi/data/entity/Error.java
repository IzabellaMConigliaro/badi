/*
 * File: Error.java
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
 * Error Basic Response used in the data layer used to parse the code and message error from the API.
 * It's used in {@link APIError} model.
 */

@AutoValue
public abstract class Error {

    @SerializedName("code")
    public abstract String code();

    @SerializedName("message")
    public abstract String message();

    public static Error create(String code, String message) {
        return new AutoValue_Error(code, message);
    }

    public static TypeAdapter<Error> typeAdapter(Gson gson) {
        return new AutoValue_Error.GsonTypeAdapter(gson);
    }

}
