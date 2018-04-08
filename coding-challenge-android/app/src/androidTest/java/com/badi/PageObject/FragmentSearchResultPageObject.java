package com.badi.PageObject;

import com.badi.R;

import static com.badi.TestUtils.checkIsDisplayed;

public class FragmentSearchResultPageObject {

    public static void checkFragmentSearchResultIsVisible() {
        checkIsDisplayed(R.id.fragment_search_result_container);
    }
}
