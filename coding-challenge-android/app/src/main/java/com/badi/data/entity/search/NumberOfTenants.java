package com.badi.data.entity.search;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link NumberOfTenants} model gives a custom amount of minimum and maximum number of tenants living in a room.
 * It's used in {@link SearchRooms} model.
 */
@AutoValue
public abstract class NumberOfTenants implements Parcelable {

    @SerializedName("min")
    @Nullable public abstract Integer minimum();

    @SerializedName("max")
    @Nullable public abstract Integer maximum();

    public static NumberOfTenants create(Integer minimum, Integer maximum) {
        return new AutoValue_NumberOfTenants(minimum, maximum);
    }

    public static Builder builder() {
        return new AutoValue_NumberOfTenants.Builder();
    }

    public static TypeAdapter<NumberOfTenants> typeAdapter(Gson gson) {
        return new AutoValue_NumberOfTenants.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMinimum(Integer minimum);
        public abstract Builder setMaximum(Integer maximum);
        public abstract NumberOfTenants build();
    }
}
