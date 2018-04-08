package com.badi.PageObject;

import com.badi.R;

import static com.badi.TestUtils.checkIsDisplayed;
import static com.badi.TestUtils.checkIsGone;
import static com.badi.TestUtils.performClick;

public class DialogNeedABadiPageObject {

    public static void openDialog() {
        performClick(R.id.button_search_list_room);
    }

    public static void closeDialog() {
        performClick(R.id.button_close);
    }

    public static void openListRoom() {
        performClick(R.id.button_list_room);
    }

    public static void checkDialogIsVisible() {
        checkIsDisplayed(R.id.dialog_message);
    }

    public static void checkDialogIsGone() {
        checkIsGone(R.id.dialog_message);
    }
}
