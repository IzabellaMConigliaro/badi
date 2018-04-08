package com.badi.presentation.search;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.GrantPermissionRule;

import com.badi.PageObject.FragmentSearchResultPageObject;
import com.badi.PageObject.SearchPlaceActivityPageObject;
import com.badi.presentation.main.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class SearchPlaceActivityAndroidTest {
    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class, true, true);

    @Rule public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setUp() throws Exception {
        activityRule.getActivity().getPreferencesHelper().clearUserSearches();
        SearchPlaceActivityPageObject.openSearchPlaceActivity();
    }

    @After
    public void tearDown() throws Exception {
        activityRule.getActivity().getPreferencesHelper().clearUserSearches();
    }

    @Test
    public void testInvalidLocationShowErrorMessage() throws Exception {
        SearchPlaceActivityPageObject.searchForInvalidLocation();

        SearchPlaceActivityPageObject.checkInvalidSearchMessageIsDisplayed();
    }

    @Test
    public void testValidLocationShowList() throws Exception {
        SearchPlaceActivityPageObject.searchForValidLocation();

        SearchPlaceActivityPageObject.checkSearchResultListIsDisplayed();
    }

    @Test
    public void testSearchValidLocationAndClickOnFirstResult() throws Exception {
        SearchPlaceActivityPageObject.searchForValidLocation();

        SearchPlaceActivityPageObject.checkSearchResultListIsDisplayed();
        SearchPlaceActivityPageObject.openFirstResult();

        FragmentSearchResultPageObject.checkFragmentSearchResultIsVisible();
    }

    @Test
    public void testSearchValidLocationAndThenClearSearchField() throws Exception {
        SearchPlaceActivityPageObject.searchForValidLocation();

        SearchPlaceActivityPageObject.checkSearchResultListIsDisplayed();
        SearchPlaceActivityPageObject.clearSeach();

        SearchPlaceActivityPageObject.checkSavedResultListIsDisplayed();
    }

    @Test
    public void testSearchInvalidLocationAndThenClearSearchField() throws Exception {
        SearchPlaceActivityPageObject.searchForInvalidLocation();

        SearchPlaceActivityPageObject.checkInvalidSearchMessageIsDisplayed();
        SearchPlaceActivityPageObject.clearSeach();

        SearchPlaceActivityPageObject.checkSavedResultListIsDisplayed();
    }

    @Test
    public void testSaveSearchInPrefs() throws Exception {
        SearchPlaceActivityPageObject.searchForValidLocation();

        SearchPlaceActivityPageObject.checkSearchResultListIsDisplayed();
        SearchPlaceActivityPageObject.openFirstResult();

        SearchPlaceActivityPageObject.openSearchPlaceActivityFromSearchFragment();
        SearchPlaceActivityPageObject.checkFirstSavedResult();
    }

    @Test
    public void testSearchByMyLocation() throws Exception {
        SearchPlaceActivityPageObject.searchByCurrentLocation();

        FragmentSearchResultPageObject.checkFragmentSearchResultIsVisible();
    }
}
