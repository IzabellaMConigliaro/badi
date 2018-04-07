/*
 * File: RoomUtils.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.app.Activity;
import android.support.v4.app.ShareCompat;

import com.badi.BuildConfig;
import com.badi.R;

import java.util.Locale;

/**
 * Class with static methods to change room values
 */

public final class RoomUtils {

    public static void shareRoom(Activity activity, Integer id, String city) {
        String textToShare;
        if (city != null) {
            textToShare = String.format(Locale.getDefault(),
                    activity.getString(R.string.text_share_room_with_city),
                    city,
                    BuildConfig.WEB_URL + activity.getString(R.string.url_room_prefix),
                    id);
        } else {
            textToShare = String.format(Locale.getDefault(),
                    activity.getString(R.string.text_share_room),
                    BuildConfig.WEB_URL + activity.getString(R.string.url_room_prefix),
                    id);
        }

        share(activity, textToShare);
    }

    public static void shareMyRoom(Activity activity, Integer id) {
        String textToShare = String.format(Locale.getDefault(),
                    activity.getString(R.string.text_share_my_room),
                    BuildConfig.WEB_URL + activity.getString(R.string.url_room_prefix),
                    id);

        share(activity, textToShare);
    }

    private static void share(Activity activity, String textToShare) {
        ShareCompat.IntentBuilder
                .from(activity)
                .setType("text/plain")
                .setText(textToShare)
                .setChooserTitle(R.string.text_share_room_chooser)
                .startChooser();
    }

    public static String getDistance(Double distance) {
        if (distance < 1)
            return Long.toString(Math.round(distance * 1000)) + " m";
        else
            return String.format(Locale.getDefault(), "%.2f", distance) + " km";
    }
}
