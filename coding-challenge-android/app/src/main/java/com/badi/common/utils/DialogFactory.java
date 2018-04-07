/*
 * File: DialogFactory.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

/**
 * Class with static methods for creating error dialogs
 */
public class DialogFactory {

    public interface OnOneOptionDialogClickListener {
        void onDialogPositiveButtonClick();
    }

    public interface OnDialogClickListener {
        void onDialogPositiveButtonClick();
        void onDialogNegativeButtonClick();
    }

    public static Dialog createSimpleOkErrorDialog(Context context, String title, String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNeutralButton(android.R.string.ok, null);
        return alertDialog.create();
    }

    public static Dialog createCustomDialog(Context context, View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        AlertDialog dialog = alertDialog.create();
        dialog.setView(view);
        return dialog;
    }

    public static Dialog createOneOptionDialog(Context context, String title, String message, boolean cancelable,
                                               final OnOneOptionDialogClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogPositiveButtonClick();
                });
        return alertDialog.create();
    }

    public static Dialog createTwoOptionDialog(Context context, String title, String message, String positiveButtonText,
                                               String negativeButtonText, final OnOneOptionDialogClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogPositiveButtonClick();
                })
                .setNegativeButton(negativeButtonText, (dialog, which) -> dialog.dismiss());
        return alertDialog.create();
    }

    public static Dialog createTwoOptionDialog(Context context, String title, String message, String positiveButtonText,
                                               String negativeButtonText, boolean cancelable,
                                               final OnDialogClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogPositiveButtonClick();
                })
                .setNegativeButton(negativeButtonText, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogNegativeButtonClick();
                });
        return alertDialog.create();
    }

    public static Dialog createThreeOptionDialog(Context context, String title,
                                                 String message, String positiveButtonText, String negativeButtonText,
                                                 String neutralButtonText, final OnDialogClickListener listener) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogPositiveButtonClick();
                })
                .setNegativeButton(negativeButtonText, (dialog, which) -> {
                    dialog.dismiss();
                    listener.onDialogNegativeButtonClick();
                })
                .setNeutralButton(neutralButtonText, (dialog, which) -> dialog.dismiss());
        return alertDialog.create();
    }
}
