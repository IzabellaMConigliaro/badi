/*
 * File: SearchDataRepository.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository;

import com.badi.data.entity.PlaceAddress;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.data.repository.remote.APIService;
import com.badi.domain.repository.SearchRepository;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * {@link SearchRepository} implementation for retrieving search data.
 */
@Singleton
public class SearchDataRepository implements SearchRepository {

    private final APIService apiService;
    private final PreferencesHelper preferencesHelper;
    private final Type typeListPlaceAddress = new TypeToken<ArrayList<PlaceAddress>>() { }.getType();

    /**
     * Constructs a {@link SearchRepository}
     *
     * @param apiService {@link APIService}
     * @param preferencesHelper {@link PreferencesHelper}
     */
    @Inject
    public SearchDataRepository(APIService apiService, PreferencesHelper preferencesHelper) {
        this.apiService = apiService;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public Completable setSearchInPrefs(PlaceAddress search) {
        List<PlaceAddress> searches = new ArrayList<>();
        if (preferencesHelper.getUserSearches() == null) {
            searches.add(search);
        } else {
            searches = APIService.Creator.gson.fromJson(preferencesHelper.getUserSearches(), typeListPlaceAddress);
            if (!searches.contains(search))
                searches.add(search);
        }
        preferencesHelper.saveUserSearches(APIService.Creator.gson.toJson(searches));
        return Completable.complete();
    }

    @Override
    public Observable<List<PlaceAddress>> getSearchesInPrefs() {
        List<PlaceAddress> placeAddresses = new ArrayList<>();
        if (preferencesHelper.getUserSearches() != null)
            placeAddresses = APIService.Creator.gson.fromJson(preferencesHelper.getUserSearches(), typeListPlaceAddress);
        return Observable.just(placeAddresses);
    }

}
