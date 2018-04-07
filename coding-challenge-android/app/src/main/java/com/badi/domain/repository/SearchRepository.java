/*
 * File: SearchRepository.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.repository;

import com.badi.data.entity.PlaceAddress;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Interface that represents a Repository for getting user related data.
 */
public interface SearchRepository {

    /**
     * Get a {@link Completable} which will save a place in {@link android.content.SharedPreferences}.
     */
    Completable setSearchInPrefs(PlaceAddress search);

    /**
     * Get an {@link Observable} which will emit a List of {@link PlaceAddress}.
     */
    Observable<List<PlaceAddress>> getSearchesInPrefs();

}
