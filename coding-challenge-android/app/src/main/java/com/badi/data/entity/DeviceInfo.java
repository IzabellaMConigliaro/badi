/*
 * File: DeviceInfo.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * {@link DeviceInfo} model used for mapping the response of register device api call
 */
@AutoValue
public abstract class DeviceInfo {

    @SerializedName("user_id")
    public abstract Integer userId();

    @SerializedName("token")
    public abstract String token();

    @SerializedName("app_version")
    public abstract String appVersion();

    @SerializedName("brand")
    public abstract String brand();

    @SerializedName("last_login")
    public abstract Date lastLogin();

    @SerializedName("locale")
    public abstract String locale();

    @SerializedName("model")
    public abstract String model();

    @SerializedName("os")
    public abstract String os();

    @SerializedName("os_version")
    public abstract String osVersion();

    @SerializedName("created_at")
    public abstract Date createdAt();

    @SerializedName("updated_at")
    public abstract Date updatedAt();

    public static DeviceInfo create(Integer userId, String token, String appVersion, String brand, Date lastLogin, String locale,
                                    String model, String os, String osVersion, Date createdAt, Date updatedAt) {
        return new AutoValue_DeviceInfo(userId, token, appVersion, brand, lastLogin,
                locale, model, os, osVersion, createdAt, updatedAt);
    }

    public static TypeAdapter<DeviceInfo> typeAdapter(Gson gson) {
        return new AutoValue_DeviceInfo.GsonTypeAdapter(gson);
    }

}
