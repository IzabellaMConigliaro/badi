/*
 * File: PricesAttribute.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.entity.room;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

/**
 * {@link PricesAttribute} model with the information regarding the pricing policy, price and currency of the price.
 * It's used inside the {@link RoomDetail} model.
 * Array of prices with the following attributes: price_type: {"hourly"=>1, "daily"=>2, "monthly"=>3}, price: price
 */
@AutoValue
public abstract class PricesAttribute implements Parcelable {

    public static final int TYPE_HOURLY = 1;
    public static final int TYPE_DAILY = 2;
    public static final int TYPE_MONTHLY = 3;

    @SerializedName("price_type")
    public abstract Integer priceType();

    @SerializedName("price")
    @Nullable public abstract Integer price();

    @SerializedName("currency")
    @Nullable public abstract String currency();

    @SerializedName("bills_included")
    @Nullable public abstract Boolean billsIncluded();

    public static PricesAttribute create(Integer priceType, Integer price, String currency, Boolean billsIncluded) {
        return new AutoValue_PricesAttribute(priceType, price, currency, billsIncluded);
    }

    public static TypeAdapter<PricesAttribute> typeAdapter(Gson gson) {
        return new AutoValue_PricesAttribute.GsonTypeAdapter(gson);
    }

    public static Builder builder() {
        return new AutoValue_PricesAttribute.Builder()
                .setPriceType(TYPE_MONTHLY);
    }

    public abstract Builder toBuilder();

    public PricesAttribute withPrice(Integer price) {
        return toBuilder().setPrice(price).build();
    }

    public PricesAttribute withCurrency(String currency) {
        return toBuilder().setCurrency(currency).build();
    }

    public PricesAttribute withBillsIncluded(Boolean billsIncluded) {
        return toBuilder().setBillsIncluded(billsIncluded).build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setPriceType(Integer priceType);
        public abstract Builder setPrice(Integer price);
        public abstract Builder setCurrency(String currency);
        public abstract Builder setBillsIncluded(Boolean billsIncluded);
        public abstract PricesAttribute build();
    }
}
