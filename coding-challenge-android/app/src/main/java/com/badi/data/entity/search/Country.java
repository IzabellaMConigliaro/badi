package com.badi.data.entity.search;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Country implements Parcelable {

    @SerializedName("name")
    public abstract String name();

    @SerializedName("cities_list")
    public abstract List<City> citiesList();

    public static Country create(String name, List<City> citiesList) {
        return new AutoValue_Country(name, citiesList);
    }

    public static Builder builder() {
        return new AutoValue_Country.Builder();
    }

    public static TypeAdapter<Country> typeAdapter(Gson gson) {
        return new AutoValue_Country.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setName(String name);
        public abstract Builder setCitiesList(List<City> citiesList);
        public abstract Country build();
    }
}
