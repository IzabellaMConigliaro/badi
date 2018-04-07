/*
 * File: PreferencesHelperTest.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.badi.data.repository.local.PreferencesHelper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;


/**
 * Test for PreferencesHelper
 */
public class PreferencesHelperTest {

    private static final String TOKEN = "TEST_TOKEN";
    private static final String USER_NAME = "TEST_USER_NAME";
    private static final String USER_EMAIL = "TEST_USER_EMAIL";
    private static final boolean VALIDATION_STATUS = true;
    private static final boolean DEFAULT_VALUE = false;
    private static final String EMPTY = "";
    private static final String PREF_FILE_NAME = "badi_pref_file";
    private static final String PREF_ACCESS_TOKEN = "badi_pref_access_token";
    private static final String PREF_REFRESH_TOKEN = "badi_pref_refresh_token";
    private static final String PREF_USER_NAME =  "badi_pref_user_name";
    private static final String PREF_USER_EMAIL =  "badi_pref_user_email";
    private static final String PREF_VALIDATION_STATUS = "badi_pref_validation_status";

    @Mock private Context mockContext;
    @Mock private PreferencesHelper mockPreferencesHelper;
    @Mock private SharedPreferences mockSharedPreferences;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSaveAccessToken() throws Exception {
        mockPreferencesHelper.saveAccessToken(TOKEN);
        Mockito.when(mockSharedPreferences.getString(PREF_ACCESS_TOKEN, EMPTY)).thenReturn(TOKEN);
        String expectedToken = mockSharedPreferences.getString(PREF_ACCESS_TOKEN, EMPTY);
        assertEquals(expectedToken, TOKEN);
    }

    @Test
    public void testSaveRefreshToken() throws Exception {
        mockPreferencesHelper.saveRefreshToken(TOKEN);
        Mockito.when(mockSharedPreferences.getString(PREF_REFRESH_TOKEN, EMPTY)).thenReturn(TOKEN);
        String expectedToken = mockSharedPreferences.getString(PREF_REFRESH_TOKEN, EMPTY);
        assertEquals(expectedToken, TOKEN);
    }

    @Test
    public void testGetAccessToken() throws Exception {
        String token =  mockPreferencesHelper.getAccessToken();
        assertEquals(token, null);
    }

    @Test
    public void testSaveUserName() throws Exception {
        mockPreferencesHelper.saveUserName(USER_NAME);
        Mockito.when(mockSharedPreferences.getString(PREF_USER_NAME, EMPTY)).thenReturn(USER_NAME);
        String expectedName = mockSharedPreferences.getString(PREF_USER_NAME, EMPTY);
        assertEquals(expectedName, USER_NAME);
    }

    @Test
    public void testSaveUserEmail() throws Exception {
        mockPreferencesHelper.saveUserEmail(USER_EMAIL);
        Mockito.when(mockSharedPreferences.getString(PREF_USER_EMAIL, EMPTY)).thenReturn(USER_EMAIL);
        String expectedEmail = mockSharedPreferences.getString(PREF_USER_EMAIL, EMPTY);
        assertEquals(expectedEmail, USER_EMAIL);
    }

    @Test
    public void testSaveValidationStatus() throws Exception {
        mockPreferencesHelper.saveValidationStatus(VALIDATION_STATUS);
        Mockito.when(mockSharedPreferences.getBoolean(PREF_VALIDATION_STATUS, DEFAULT_VALUE)).thenReturn(VALIDATION_STATUS);
        boolean expectedStatus = mockSharedPreferences.getBoolean(PREF_VALIDATION_STATUS, DEFAULT_VALUE);
        assertEquals(expectedStatus, VALIDATION_STATUS);
    }
}
