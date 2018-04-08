package com.badi;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressBack;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.IsNot.not;

public class TestUtils {
    public static void checkText(int id, String text) {
        onView(withId(id)).check(matches(withText(text)));
    }

    public static void performClick(int id) {
        onView(withId(id)).perform(click());
    }

    public static String getResourceString(int id) {
        Context targetContext = InstrumentationRegistry.getTargetContext();
        return targetContext.getResources().getString(id);
    }

    public static void performBackPressed() {
        onView(isRoot()).perform(pressBack());
    }

    public static void checkIsDisplayed(int id) {
        onView(withId(id)).check(matches(isDisplayed()));
    }

    public static void checkIsGone(int id) {
        onView(withId(id)).check(doesNotExist());
    }

    public static void performTypeTextPressGoAndCloseSoftKeyboard(int id, String text) {
        onView(withId(id)).perform(typeText(text), pressImeActionButton(), closeSoftKeyboard());
    }

    public static void performClickRecyclerView(int id, int child) {
        onView(allOf(withId(id), isDisplayed()))
                .perform(actionOnItemAtPosition(child, click()));
    }

    public static void performClearTextCloseSoftKeyboard(int id) {
        onView(withId(id)).perform(clearText(), typeText(" "), pressImeActionButton(), closeSoftKeyboard());
    }

    public static void checkRecyclerViewChildText(int id, String text) {
        onView(allOf(withId(id), isDisplayed()))
                .check(matches(hasDescendant(withText(containsString(text)))));
    }
}
