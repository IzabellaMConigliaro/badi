/*
 * File: MainActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.badi.BadiApplication;
import com.badi.R;
import com.badi.common.di.components.DaggerRoomComponent;
import com.badi.common.di.components.DaggerSearchComponent;
import com.badi.common.di.components.RoomComponent;
import com.badi.common.di.components.SearchComponent;
import com.badi.common.di.modules.RoomModule;
import com.badi.common.di.modules.SearchModule;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.search.SearchFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;

/**
 * The {@link MainActivity} of the app that controls the navigation between tabs/fragments
 */

public class MainActivity extends BaseActivity {

    @Inject PreferencesHelper preferencesHelper;

    private SearchComponent searchComponent;
    private RoomComponent roomComponent;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BadiApplication.get(this).getComponent().inject(this);
        ButterKnife.bind(this);
        //TODO Change access and refresh tokens for a custom user.
        if (preferencesHelper.getAccessToken() == null) {
            preferencesHelper.saveAccessToken("f171a6429b12dcb9484ac1416a041fbc328c01d5a6fe139dbdb4a3442b38435a");
            preferencesHelper.saveRefreshToken("19cadb3faf8d7252074566b00741e7e796a15e551fd69b321c3293b526f38727");
        }
        // Display the fragment as the main content.
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_main, SearchFragment.newInstance())
                .commit();
        if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
            //Update requests calling onResume in fragments of the getSupportFragmentManager
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    fragment.onResume();
            }
        }
    }

    public RoomComponent getRoomComponent() {
        if (roomComponent == null) {
            roomComponent = DaggerRoomComponent.builder()
                    .applicationComponent(getApplicationComponent())
                    .activityModule(getActivityModule())
                    .roomModule(new RoomModule())
                    .build();
        }
        return roomComponent;
    }

    public SearchComponent getSearchComponent() {
        if (searchComponent == null) {
            searchComponent = DaggerSearchComponent.builder()
                    .applicationComponent(getApplicationComponent())
                    .activityModule(getActivityModule())
                    .searchModule(new SearchModule())
                    .build();
        }
        return searchComponent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getSupportFragmentManager() != null && getSupportFragmentManager().getFragments() != null) {
            for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                if (fragment != null)
                    fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
}
