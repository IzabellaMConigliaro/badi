package com.badi.PageObject;

import com.badi.R;

import static com.badi.TestUtils.checkIsDisplayed;

public class ListRoomActivityPageObject {

    public static void checkListRoomActivityIsVisible() {
        checkIsDisplayed(R.id.activity_list_room);
    }
}
