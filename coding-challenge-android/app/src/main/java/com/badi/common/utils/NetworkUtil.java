/*
 * File: NetworkUtil.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.HttpException;
import timber.log.Timber;

/**
 * Class with static methods to check network connectivity
 */
public class NetworkUtil {

    private static final String DELIM_LINK_PARAM1 = ", "; //$NON-NLS-1$
    private static final String DELIM_LINK_PARAM = ";"; //$NON-NLS-1$
    private static final String META_REL = "rel"; //$NON-NLS-1$
    private static final String META_NEXT = "next"; //$NON-NLS-1$

    /**
     * Returns true if the Throwable is an instance of RetrofitError with an
     * http status code equals to the given one.
     */
    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException
                && ((HttpException) throwable).code() == statusCode;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static String buildHeaderAuthorization(String accessToken) {
        return "Bearer " + accessToken;
    }

    public static String getNextLinkPaginate(String link) {
        // No links
        if (link == null)
            return null;

        Timber.i(link);
        String[] fSegments = link.split(DELIM_LINK_PARAM1);
        String fLinkPart = fSegments[0].trim();

        String[] segments = fLinkPart.split(DELIM_LINK_PARAM);
        if (segments.length < 2)
            return null;

        String linkPart = segments[0].trim();
        if (!linkPart.startsWith("<") || !linkPart.endsWith(">")) //$NON-NLS-1$ //$NON-NLS-2$
            return null;
        linkPart = linkPart.substring(1, linkPart.length() - 1);

        for (int i = 1; i < segments.length; i++) {
            String[] rel = segments[i].trim().split("="); //$NON-NLS-1$
            if (rel.length < 2 || !META_REL.equals(rel[0]))
                continue;

            String relValue = rel[1];
            if (relValue.startsWith("\"") && relValue.endsWith("\"")) //$NON-NLS-1$ //$NON-NLS-2$
                relValue = relValue.substring(1, relValue.length() - 1);

            if (!META_NEXT.equals(relValue))
                linkPart = null;
        }
        Timber.i(linkPart);
        return linkPart;
    }

}
