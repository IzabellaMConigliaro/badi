package com.badi.data.entity.room;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class Match implements Parcelable {

    @SerializedName("id")
    public abstract Integer id();

    @SerializedName("request_date")
    public abstract String requestDate();

    @SerializedName("response_date")
    public abstract String responseDate();

    @SerializedName("status")
    public abstract Integer status();

    @SerializedName("user")
    public abstract Tenant user();

    public static TypeAdapter<Match> typeAdapter(Gson gson) {
        return new AutoValue_Match.GsonTypeAdapter(gson);
    }
}
