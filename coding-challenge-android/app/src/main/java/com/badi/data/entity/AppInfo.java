/*
 * File: AppInfo.java
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
 * {@link AppInfo} model used for mapping the app info response from the server.
 * It gives information about the current last version of the app i.e. 4.0.1, about the minimum app compatible version i.e. 4.0.0
 * and if we should force update to the latest version if the user has an old one.
 */
@AutoValue
public abstract class AppInfo {

    @SerializedName("app_last_version")
    public abstract String appLastVersion();

    @SerializedName("app_minimum_version")
    public abstract String appMinimumVersion();

    public static AppInfo create( String appLastVersion, String appMinimumVersion) {
        return new AutoValue_AppInfo(appLastVersion, appMinimumVersion);
    }

    public static TypeAdapter<AppInfo> typeAdapter(Gson gson) {
        return new AutoValue_AppInfo.GsonTypeAdapter(gson);
    }

}
