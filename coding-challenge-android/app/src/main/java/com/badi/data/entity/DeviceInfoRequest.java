/*
 * File: DeviceInfoRequest.java
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
 * {@link DeviceInfoRequest} model used for mapping the response of register device api call
 */
@AutoValue
public abstract class DeviceInfoRequest {

    @SerializedName("token")
    @Nullable public abstract String token();

    @SerializedName("os")
    public abstract Integer os();

    @SerializedName("os_version")
    public abstract String osVersion();

    @SerializedName("brand")
    public abstract String brand();

    @SerializedName("model")
    public abstract String model();

    @SerializedName("locale")
    public abstract String locale();

    @SerializedName("app_version")
    public abstract String appVersion();

    public static Builder builder() {
        return new $AutoValue_DeviceInfoRequest.Builder();
    }

    public static TypeAdapter<DeviceInfoRequest> typeAdapter(Gson gson) {
        return new AutoValue_DeviceInfoRequest.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setToken(String token);
        public abstract Builder setOs(Integer os);
        public abstract Builder setOsVersion(String osVersion);
        public abstract Builder setBrand(String brand);
        public abstract Builder setModel(String model);
        public abstract Builder setLocale(String locale);
        public abstract Builder setAppVersion(String appVersion);
        public abstract DeviceInfoRequest build();
    }
}
