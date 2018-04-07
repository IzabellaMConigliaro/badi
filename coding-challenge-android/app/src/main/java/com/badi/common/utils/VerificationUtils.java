/*
 * File: VerificationUtils.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.util.Patterns;

import com.badi.BuildConfig;

import java.util.regex.Pattern;

/**
 * Class with static methods to check components
 */
public final class VerificationUtils {

    //Password SignUp Pattern: Min 6 characters and at least one alphabet letter and one number.
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d^a-zA-Z0-9].{5,}$");

    //Password IBAN Pattern: Max 16 characters.
    private static final Pattern IBAN_PATTERN =
            Pattern.compile("[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[a-zA-Z0-9]{7}([a-zA-Z0-9]?){0,16}");

    public static boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneNumberValid(String number) {
        return Patterns.PHONE.matcher(number).matches();
    }

    public static boolean isIBANBankAccountValid(String bankAccount) {
        return IBAN_PATTERN.matcher(bankAccount).matches();
    }

    public static boolean isOldVersion(String lastVersion) {
        String currentVersion = BuildConfig.VERSION_NAME.replaceAll("\\.", "");
        String[] currentVersionSplit = currentVersion.split("\\s+");
        lastVersion = lastVersion.replaceAll("\\.", "");
        return Integer.parseInt(currentVersionSplit[0]) < Integer.parseInt(lastVersion);
    }

}
