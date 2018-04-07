/*
 * File: PreferencesHelper.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.local;

import android.content.Context;
import android.content.SharedPreferences;

import com.badi.common.di.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Helper class to retrieve and clear the Shared Preferences file.
 * It is used to store User preferences like Login credentials or settings.
 */
@Singleton
public class PreferencesHelper {

    private static final String PREF_FILE_NAME = "badi_pref_file";
    private static final String PREF_USER_LISTER =  "badi_pref_user_lister";
    private static final String PREF_USER_ROLE =  "badi_pref_user_role";
    private static final String PREF_USER_ID =  "badi_pref_user_id";
    private static final String PREF_USER_NAME =  "badi_pref_user_name";
    private static final String PREF_USER_EMAIL =  "badi_pref_user_email";
    private static final String PREF_USER_PASSWORD =  "badi_pref_user_password";
    private static final String PREF_USER_PHOTO =  "badi_pref_user_photo";
    private static final String PREF_USER_LOGIN_TYPE =  "badi_pref_user_login_type";
    private static final String PREF_USER_PHONE_VALIDATION = "badi_pref_user_phone_validation";
    private static final String PREF_VALIDATION_STATUS = "badi_pref_validation_status";
    private static final String PREF_ACCESS_TOKEN = "badi_pref_access_token";
    private static final String PREF_DEVICE_TOKEN = "badi_pref_device_token";
    private static final String PREF_TOKEN_TYPE = "badi_pref_token_type";
    private static final String PREF_REFRESH_TOKEN = "badi_pref_refresh_token";
    private static final String PREF_USER_SEARCHES = "badi_pref_user_searches";
    private static final String PREF_USER_LIST_ROOM = "badi_pref_user_list_room";
    private static final String PREF_RATE_APP_DIALOG = "badi_pref_rate_app_dialog";
    private static final String PREF_ROOM_DETAIL_COUNTER = "badi_pref_room_detail_counter";
    private static final String PREF_BOOKING_TUTORIAL_SHOWN = "badi_pref_booking_tutorial_shown";

    private final SharedPreferences pref;

    @Inject
    public PreferencesHelper(@ApplicationContext Context context) {
        pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void clear() {
        pref.edit().clear().apply();
    }

    public void saveUserLister(Boolean userLister) {
        pref.edit().putBoolean(PREF_USER_LISTER, userLister).apply();
    }

    public Boolean getUserLister() {
        return pref.getBoolean(PREF_USER_LISTER, false);
    }

    public void saveUserRole(int userRole) {
        pref.edit().putInt(PREF_USER_ROLE, userRole).apply();
    }

    public int getUserRole() {
        return pref.getInt(PREF_USER_ROLE, 0);
    }

    public void saveUserId(int userId) {
        pref.edit().putInt(PREF_USER_ID, userId).apply();
    }

    public int getUserId() {
        return pref.getInt(PREF_USER_ID, 0);
    }

    public void saveUserName(String userName) {
        pref.edit().putString(PREF_USER_NAME, userName).apply();
    }

    public String getUserName() {
        return pref.getString(PREF_USER_NAME, null);
    }

    public void saveUserEmail(String userEmail) {
        pref.edit().putString(PREF_USER_EMAIL, userEmail).apply();
    }

    public String getUserEmail() {
        return pref.getString(PREF_USER_EMAIL, null);
    }

    public void saveUserPassword(String userPassword) {
        pref.edit().putString(PREF_USER_PASSWORD, userPassword).apply();
    }

    public String getUserPassword() {
        return pref.getString(PREF_USER_PASSWORD, null);
    }

    public void clearPassword() {
        pref.edit().putString(PREF_USER_PASSWORD, null).apply();
    }

    public void saveUserPhoto(String userPhoto) {
        pref.edit().putString(PREF_USER_PHOTO, userPhoto).apply();
    }

    public String getUserPhoto() {
        return pref.getString(PREF_USER_PHOTO, null);
    }

    public void saveUserLoginType(int loginType) {
        pref.edit().putInt(PREF_USER_LOGIN_TYPE, loginType).apply();
    }

    public int getUserLoginType() {
        return pref.getInt(PREF_USER_LOGIN_TYPE, 0);
    }

    public void saveUserPhoneValidation(Boolean status) {
        pref.edit().putBoolean(PREF_USER_PHONE_VALIDATION, status).apply();
    }

    public Boolean getUserPhoneValidation() {
        return pref.getBoolean(PREF_USER_PHONE_VALIDATION, false);
    }

    public void saveValidationStatus(Boolean status) {
        pref.edit().putBoolean(PREF_VALIDATION_STATUS, status).apply();
    }

    public Boolean getValidationStatus() {
        return pref.getBoolean(PREF_VALIDATION_STATUS, false);
    }

    public void saveAccessToken(String accessToken) {
        pref.edit().putString(PREF_ACCESS_TOKEN, accessToken).apply();
    }

    public String getAccessToken() {
        return pref.getString(PREF_ACCESS_TOKEN, null);
    }

    public void saveDeviceToken(String deviceToken) {
        pref.edit().putString(PREF_DEVICE_TOKEN, deviceToken).apply();
    }

    public String getDeviceToken() {
        return pref.getString(PREF_DEVICE_TOKEN, null);
    }

    public void saveTokenType(String accessToken) {
        pref.edit().putString(PREF_TOKEN_TYPE, accessToken).apply();
    }

    public String getTokenType() {
        return pref.getString(PREF_TOKEN_TYPE, null);
    }

    public void saveRefreshToken(String refreshToken) {
        pref.edit().putString(PREF_REFRESH_TOKEN, refreshToken).apply();
    }

    public String getRefreshToken() {
        return pref.getString(PREF_REFRESH_TOKEN, null);
    }

    public void saveUserSearches(String places) {
        pref.edit().putString(PREF_USER_SEARCHES, places).apply();
    }

    public String getUserSearches() {
        return pref.getString(PREF_USER_SEARCHES, null);
    }

    public void saveUserListRoom(String roomDetail) {
        pref.edit().putString(PREF_USER_LIST_ROOM, roomDetail).apply();
    }

    public String getUserListRoom() {
        return pref.getString(PREF_USER_LIST_ROOM, null);
    }

    public void saveShouldShowRateAppDialog(Boolean shouldShowRateAppDialog) {
        pref.edit().putBoolean(PREF_RATE_APP_DIALOG, shouldShowRateAppDialog).apply();
    }

    public Boolean shouldShowRateAppDialog() {
        return pref.getBoolean(PREF_RATE_APP_DIALOG, true);
    }

    public int getRoomDetailCounter() {
        return pref.getInt(PREF_ROOM_DETAIL_COUNTER, 0);
    }

    public void increaseRoomDetailCounter() {
        if (!isBookingTutorialShown()) {
            int roomDetailCounter = getRoomDetailCounter();
            pref.edit().putInt(PREF_ROOM_DETAIL_COUNTER, ++roomDetailCounter).apply();
        }
    }

    public Boolean isBookingTutorialShown() {
        return pref.getBoolean(PREF_BOOKING_TUTORIAL_SHOWN, false);
    }

    public void setBookingTutorialShown() {
        pref.edit().putBoolean(PREF_BOOKING_TUTORIAL_SHOWN, true).apply();
    }
}
