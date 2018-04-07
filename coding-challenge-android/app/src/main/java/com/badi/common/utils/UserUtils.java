/*
 * File: UserUtils.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ShareCompat;

import com.badi.BuildConfig;
import com.badi.R;

/**
 * Class with static methods to handle user actions
 */

public final class UserUtils {

    public static void reportUserEmailIntent(Activity activity, Integer userID, String userName) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + activity.getString(R.string.email_info)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, String.format(activity.getString(R.string.report_user_subject), userID));
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format(activity.getString(R.string.report_user_body), userName, userID));

        if (emailIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.report_user_chooser)));
    }

    public static void inviteFriendIntent(Activity activity) {
        ShareCompat.IntentBuilder
                .from(activity) // getActivity() or activity field if within Fragment
                .setType("text/plain") // most general text sharing MIME type
                .setText(activity.getString(R.string.invite_friend_email_subject) + activity.getString(R.string.emoticon_ok_hand)
                        + "\n\n" + String.format(activity.getString(R.string.invite_friend_email_body),
                        activity.getString(R.string.emoticon_winking_face),
                        activity.getString(R.string.url_get_app)))
                .setChooserTitle(activity.getString(R.string.invite_friend_chooser))
                .startChooser();
    }

    public static void sendEmailIntentWithAddressAndSubject(Activity activity, String address, String subject) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + address));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);

        if (emailIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.send_email_chooser)));
    }

    public static void launchPlayStore(Activity activity) {
        launchPlayStore(((Context) activity));
    }

    public static void launchPlayStore(Context context) {
        //Uri uri = Uri.parse("market://details?id=" + activity.getPackageName());
        Uri uri = Uri.parse("market://details?id=es.inmovens.badi");
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        if (playStoreIntent.resolveActivity(context.getPackageManager()) != null)
            context.startActivity(playStoreIntent);
    }

    public static void deleteAccountEmailIntent(Activity activity, Integer userID) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + activity.getString(R.string.email_info)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.delete_account_email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format(activity.getString(R.string.delete_account_email_body), userID));

        if (emailIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.delete_account_chooser)));
    }

    public static void reportBugEmailIntent(Activity activity, String userID) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + activity.getString(R.string.email_feedback)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.report_bug_email_subject));
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format(activity.getString(R.string.report_bug_email_body),
                Build.MODEL, Build.VERSION.RELEASE, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE, userID));

        if (emailIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.report_bug_chooser)));
    }

    public static void reportRoomEmailIntent(Activity activity, int roomId) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + activity.getString(R.string.email_report)));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.room_report_email_title));
        emailIntent.putExtra(Intent.EXTRA_TEXT, activity.getString(R.string.room_report_email_body, roomId));

        if (emailIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(emailIntent, activity.getString(R.string.room_report_chooser)));
    }

    public static void dialNumberIntent(Activity activity, String phoneNumber) {
        Intent dialIntent = new Intent(Intent.ACTION_DIAL);
        dialIntent.setData(Uri.parse("tel:" + phoneNumber.trim()));

        if (dialIntent.resolveActivity(activity.getPackageManager()) != null)
            activity.startActivity(Intent.createChooser(dialIntent, activity.getString(R.string.phone_dial_chooser)));
    }

}
