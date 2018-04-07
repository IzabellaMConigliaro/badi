/*
 * File: SearchResultFragment.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badi.R;
import com.badi.common.utils.LockableViewPager;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.presentation.base.BaseFragment;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.navigation.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A {@link BaseFragment} that displays the result of the search
 */
public class SearchResultFragment extends BaseFragment {

    private static final String ARG_COORDINATES = "ARG_COORDINATES";
    private static final String ARG_ADDRESS = "ARG_ADDRESS";
    private static final String ARG_LOCATION = "ARG_LOCATION";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tab_layout_search) TabLayout tabLayout;
    @BindView(R.id.viewpager_search) LockableViewPager viewPager;

    private Unbinder unbinder;
    private Filters filters;
    private String mapSearchToolbarTitle;

    public SearchResultFragment() {
        setHasOptionsMenu(true);
    }

    public static SearchResultFragment newInstance(String address, Coordinates coordinates, Location location) {
        SearchResultFragment propertiesFragment = new SearchResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ADDRESS, address);
        args.putParcelable(ARG_COORDINATES, coordinates);
        args.putParcelable(ARG_LOCATION, location);
        propertiesFragment.setArguments(args);
        return propertiesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((MainActivity) getActivity()).getRoomComponent().inject(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewUtil.changeStatusBarColor(getActivity(), getResources().getColor(R.color.green_700));
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_search_result, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);

        mapSearchToolbarTitle = getArguments().getString(ARG_ADDRESS);
        setupToolbar(getArguments().getString(ARG_ADDRESS));

        SearchResultFragmentPagerAdapter adapter = new SearchResultFragmentPagerAdapter(getChildFragmentManager(), getActivity());
        adapter.setSearchDetails(getArguments().getParcelable(ARG_COORDINATES), getArguments().getParcelable(ARG_LOCATION));
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == SearchResultFragmentPagerAdapter.ROOMS_FRAGMENT_POSITION)
                    toolbar.setTitle(getArguments().getString(ARG_ADDRESS));
                else if (position == SearchResultFragmentPagerAdapter.MAP_FRAGMENT_POSITION)
                    toolbar.setTitle(mapSearchToolbarTitle);
            }
        });

        // Give the TabLayout the ViewPager
        tabLayout.setupWithViewPager(viewPager);
        // Change tabs font
        ViewUtil.changeTabsFont(tabLayout);
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Navigator.REQUEST_CODE_FILTERS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                filters = data.getParcelableExtra(FiltersActivity.EXTRA_FILTERS);
                toolbar.getMenu().findItem(R.id.action_filter).setIcon(R.drawable.ic_search_filter_active);
            } else {
                filters = null;
                toolbar.getMenu().findItem(R.id.action_filter).setIcon(R.drawable.ic_search_filter_inactive);
            }
        }
        if (getChildFragmentManager().getFragments() != null) {
            for (Fragment fragment : getChildFragmentManager().getFragments()) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onDestroyView() {
        //Binding reset to avoid memory leaks
        unbinder.unbind();
        super.onDestroyView();
    }

    private void setupToolbar(String title) {
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.search);
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_filter) {
                navigator.navigateToFilters(SearchResultFragment.this, filters);
                return true;
            }
            return false;
        });
        toolbar.setNavigationOnClickListener(v -> {
            getActivity().onBackPressed();
            ViewUtil.changeStatusBarColor(getActivity(), getResources().getColor(R.color.white));
        });
    }

    @Override
    protected boolean onBackPressed() {
        ViewUtil.changeStatusBarColor(getActivity(), getResources().getColor(R.color.white));
        return super.onBackPressed();
    }

    public void setCustomMapSearchToolbarTitle() {
        mapSearchToolbarTitle = getString(R.string.title_map_search);
        if (toolbar != null) {
            toolbar.setTitle(mapSearchToolbarTitle);
        }
    }
}
