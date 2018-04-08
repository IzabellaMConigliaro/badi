package com.badi.presentation.search;

import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.badi.PageObject.DialogNeedABadiPageObject;
import com.badi.PageObject.ListRoomActivityPageObject;
import com.badi.presentation.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.badi.TestUtils.performBackPressed;

public class DialogNeedABadiAndroidTest {

    @Rule
    public IntentsTestRule<MainActivity> activityRule =
            new IntentsTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        DialogNeedABadiPageObject.openDialog();
    }

    @Test
    public void testDialogIsVisible() {
        DialogNeedABadiPageObject.checkDialogIsVisible();
    }

    @Test
    public void testDialogIsDismissedOnBackPressed() {
        performBackPressed();
        DialogNeedABadiPageObject.checkDialogIsGone();
    }

    @Test
    public void testDialogIsDismissedOnClickClose() {
        DialogNeedABadiPageObject.closeDialog();
        DialogNeedABadiPageObject.checkDialogIsGone();
    }

    @Test
    public void testOpenListRoomOnClickButton() {
        DialogNeedABadiPageObject.openListRoom();
        ListRoomActivityPageObject.checkListRoomActivityIsVisible();
    }
}
