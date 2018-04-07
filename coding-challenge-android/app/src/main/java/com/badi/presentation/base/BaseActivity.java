/*
 * File: BaseActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.badi.BadiApplication;
import com.badi.common.di.components.ApplicationComponent;
import com.badi.common.di.modules.ActivityModule;
import com.badi.presentation.navigation.Navigator;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Base {@link AppCompatActivity} class for every Activity in this application.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Inject protected Navigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Adds a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where add the fragment.
     * @param fragment The fragment to be added.
     */
    protected void addFragmentToBackStack(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(containerViewId, fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.addToBackStack(fragment.getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    /**
     * Replace a {@link Fragment} to this activity's layout.
     *
     * @param containerViewId The container view to where replace the fragment.
     * @param fragment The fragment to be added.
     */
    protected void replaceFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerViewId, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * Get the Main Application component for dependency injection.
     *
     * @return {@link ApplicationComponent}
     */
    protected ApplicationComponent getApplicationComponent() {
        return ((BadiApplication) getApplication()).getComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     *
     * @return {@link ActivityModule}
     */
    public ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                } else {
                    supportFinishAfterTransition();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (!BaseFragment.handleBackPressed(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }
}
