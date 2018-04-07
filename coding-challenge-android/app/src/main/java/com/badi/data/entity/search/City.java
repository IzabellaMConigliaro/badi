package com.badi.data.entity.search;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

@AutoValue
public abstract class City implements Parcelable {

    public static final int ID_INDEX = 0;
    public static final int NAME_INDEX = 1;
    public static final int ADDRESS_INDEX = 2;
    public static final int CITY_IMG_SMALL_INDEX = 3;
    public static final int CITY_IMG_LARGE_INDEX = 4;

    @SerializedName("id")
    public abstract String id();

    @SerializedName("name")
    public abstract String name();

    @SerializedName("address")
    public abstract String address();

    @SerializedName("img_small")
    public abstract Integer imgSmall();

    @SerializedName("img_large")
    public abstract Integer imgLarge();

    public static City create(String id, String name, String address, Integer imgSmall, Integer imgLarge) {
        return new AutoValue_City(id, name, address, imgSmall, imgLarge);
    }

    public static Builder builder() {
        return new AutoValue_City.Builder();
    }

    public static TypeAdapter<City> typeAdapter(Gson gson) {
        return new AutoValue_City.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String id);
        public abstract Builder setName(String name);
        public abstract Builder setAddress(String address);
        public abstract Builder setImgSmall(Integer imgSmall);
        public abstract Builder setImgLarge(Integer imgLarge);
        public abstract City build();
    }
}
