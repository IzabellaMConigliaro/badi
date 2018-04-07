/*
 * File: SearchComponent.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di.components;

import com.badi.common.di.PerActivity;
import com.badi.common.di.modules.ActivityModule;
import com.badi.common.di.modules.SearchModule;
import com.badi.presentation.search.SearchFragment;
import com.badi.presentation.search.SearchPlaceActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Used for the search.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = {ActivityModule.class, SearchModule.class})
public interface SearchComponent extends ActivityComponent {
    void inject(SearchFragment searchFragment);
    void inject(SearchPlaceActivity searchPlaceActivity);
}
