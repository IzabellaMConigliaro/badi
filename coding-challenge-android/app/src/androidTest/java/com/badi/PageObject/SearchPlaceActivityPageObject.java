package com.badi.PageObject;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.badi.R;
import com.badi.ViewIsVisibleInstruction;

import static com.badi.TestUtils.checkIsDisplayed;
import static com.badi.TestUtils.checkRecyclerViewChildText;
import static com.badi.TestUtils.performBackPressed;
import static com.badi.TestUtils.performClearTextCloseSoftKeyboard;
import static com.badi.TestUtils.performClick;
import static com.badi.TestUtils.performClickRecyclerView;
import static com.badi.TestUtils.performTypeTextPressGoAndCloseSoftKeyboard;

public class SearchPlaceActivityPageObject {

    private static final String INVALID_LOCATION = "ksdflkajsdflkjfgl ";
    private static final String VALID_LOCATION = "Barcelona";

    public static void searchForLocation(String text) {
        performTypeTextPressGoAndCloseSoftKeyboard(R.id.edit_text_autocomplete, text);
    }

    public static void searchForValidLocation() {
        searchForLocation(VALID_LOCATION);
    }

    public static void searchByCurrentLocation() {
        performClick(R.id.button_current_location);
    }

    public static void searchForInvalidLocation() {
        searchForLocation(INVALID_LOCATION);
    }

    public static void checkInvalidSearchMessageIsDisplayed() throws Exception {
        ConditionWatcher.waitForCondition(new ViewIsVisibleInstruction(R.id.view_invalid_search));
        checkIsDisplayed(R.id.view_invalid_search);
    }

    public static void checkSearchResultListIsDisplayed() throws Exception {
        ConditionWatcher.waitForCondition(new ViewIsVisibleInstruction(R.id.recycler_view_autocomplete_places));
        checkIsDisplayed(R.id.recycler_view_autocomplete_places);
    }

    public static void openFirstResult() {
        performClickRecyclerView(R.id.recycler_view_autocomplete_places, 0);
    }

    public static void openSearchPlaceActivity() {
        performClick(R.id.button_search_place);
    }

    public static void clearSeach() {
        performClearTextCloseSoftKeyboard(R.id.edit_text_autocomplete);
    }

    public static void checkSavedResultListIsDisplayed() throws Exception {
        ConditionWatcher.waitForCondition(new ViewIsVisibleInstruction(R.id.recycler_view_searches));
        checkIsDisplayed(R.id.recycler_view_searches);
    }

    public static void openSearchPlaceActivityFromSearchFragment() {
        performBackPressed();
        openSearchPlaceActivity();
    }

    public static void checkFirstSavedResult() {
        checkRecyclerViewChildText(R.id.recycler_view_searches, VALID_LOCATION);
    }
}
