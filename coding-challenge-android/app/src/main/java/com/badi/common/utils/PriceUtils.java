/*
 * File: PriceUtils.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.content.Context;

import com.badi.R;
import com.badi.data.entity.room.PricesAttribute;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

/**
 * Class with static methods to change price values
 */

public class PriceUtils {

    public static String getPriceRoom(Context context, List<PricesAttribute> pricesAttributes) {
        if (context != null && !pricesAttributes.isEmpty())
            return context.getString(R.string.price_room_monthly,
                    pricesAttributes.get(0).price(),
                    getPriceSymbol(context, pricesAttributes.get(0).currency()));
        else
            return "";
    }

    public static String getPriceMap(Context context, List<PricesAttribute> pricesAttributes) {
        if (context != null && !pricesAttributes.isEmpty())
            return context.getString(R.string.price_room,
                    pricesAttributes.get(0).price(),
                    getPriceSymbol(context, pricesAttributes.get(0).currency()));
        else
            return "";
    }

    public static String getPriceSymbol(Context context, String currencyCode) {
        if (currencyCode != null)
            return Currency.getInstance(currencyCode).getSymbol(Locale.getDefault());
        else
            return context.getString(R.string.currency_sign_euro);
    }

}
