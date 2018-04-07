/*
 * File: MapUtils.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.badi.R;
import com.badi.data.entity.search.Coordinates;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

/**
 * Class with static methods with map ui helper methods
 */

public class MapUtils {

    private static final int RADIUS_CIRCLE_IN_METERS = 100;

    public static BitmapDescriptor createDrawableFromView(Context context, String text, boolean isSelected, boolean isSeen) {
        if (context == null) {
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        }

        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(R.layout.item_icon_map, null);

        FrameLayout background = view.findViewById(R.id.marker_background);
        TextView price = view.findViewById(R.id.marker_price_text);

        //Set price text
        price.setText(text);

        if (isSelected) {
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.img_marker_active));
            price.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            background.setBackground(ContextCompat.getDrawable(context, R.drawable.img_marker_inactive));
            if (isSeen)
                price.setTextColor(ContextCompat.getColor(context, R.color.grey_400));
            else
                price.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, background.getMeasuredWidth(), background.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static CircleOptions createCirclePositionRoom(Context context, LatLng coordinates) {
        return new CircleOptions()
                .center(coordinates)
                .fillColor(ContextCompat.getColor(context, R.color.green_500_alpha_30))
                .strokeWidth(0)
                .radius(RADIUS_CIRCLE_IN_METERS);
    }

    public static String generateStaticMapPreview(Context context, Coordinates coordinates) {
        return "https://maps.googleapis.com/maps/api/staticmap?center="
                + coordinates.latitude() + "," + coordinates.longitude() + "&zoom=13&size=200x200" +
                "&markers=color:green%7C" + coordinates.latitude() + "," + coordinates.longitude() +
                "&key=" + context.getString(R.string.google_api_key);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
