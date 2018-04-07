/*
 * File: SearchFragmentPagerAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.badi.R;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Location;

/**
 * The {@link SearchResultFragmentPagerAdapter} is an implementation of the FragmentPagerAdapter that holds all
 * search result fragments (room list and map).
 */

public class SearchResultFragmentPagerAdapter extends FragmentPagerAdapter {

    static final int ROOMS_FRAGMENT_POSITION = 0;
    static final int MAP_FRAGMENT_POSITION = 1;

    private String[] tabTitles;
    private Location location;
    private Coordinates coordinates;


    SearchResultFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        tabTitles = context.getResources().getStringArray(R.array.search_options);
    }

    public void setSearchDetails(Coordinates coordinates, Location location) {
        this.location = location;
        this.coordinates = coordinates;
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment currentFragment = new Fragment();
        switch (position) {
            case ROOMS_FRAGMENT_POSITION:
                currentFragment = RoomsListFragment.newInstance(coordinates, location);
                break;
            case MAP_FRAGMENT_POSITION:
                currentFragment = MapFragment.newInstance(coordinates, location);
                break;
        }
        return currentFragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

}
