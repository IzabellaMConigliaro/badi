package com.badi.data.entity.search;

import android.graphics.drawable.Drawable;
import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class City implements Parcelable {

    @SerializedName("name")
    public abstract String name();

    @SerializedName("img_small")
    public abstract Integer imgSmall();

    @SerializedName("img_large")
    public abstract Integer imgLarge();

    public static City create(String name, Integer imgSmall, Integer imgLarge) {
        return new AutoValue_City(name, imgSmall, imgLarge);
    }

    public static Builder builder() {
        return new AutoValue_City.Builder();
    }

    public static TypeAdapter<City> typeAdapter(Gson gson) {
        return new AutoValue_City.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);
        public abstract Builder setImgSmall(Integer imgSmall);
        public abstract Builder setImgLarge(Integer imgLarge);
        public abstract City build();
    }
}
