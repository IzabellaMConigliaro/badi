/*
 * File: MapRoomsAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.StyleableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.badi.data.entity.search.City;
import com.badi.data.entity.search.Country;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link Country} in the search.
 */
public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountryHolder> {

    private Context context;
    private List<Country> countryList;
    private CitiesAdapter.OnCityListener onCityListener;

    CountriesAdapter(Context context, CitiesAdapter.OnCityListener onCityListener) {
        this.context = context;
        this.onCityListener = onCityListener;
        setupCountryList();
    }

    @Override
    public CountryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suggestion, parent, false);
        return new CountryHolder(view);
    }

    @Override
    public void onBindViewHolder(CountryHolder holder, int position) {
        // Show country details inside viewHolder
        holder.show(countryList.get(position));
    }

    @Override
    public int getItemCount() {
        return countryList == null ? 0 : countryList.size();
    }

    public void setCountries(List<Country> countries) {
        countryList = countries;
        notifyDataSetChanged();
    }

    private void setupCountryList() {
        countryList = new ArrayList<>();

        TypedArray countriesTypedArray = context.getResources().obtainTypedArray(R.array.countries);

        for (int countryCount = 0; countryCount < countriesTypedArray.length(); countryCount++) {
            TypedArray countryTypedArray = getTypedArray(countriesTypedArray, countryCount);
            TypedArray citiesTypedArray = getTypedArray(countryTypedArray, Country.CITY_LIST_INDEX);

            List<City> citiesList = new ArrayList<>();

            for (int cityCount = 0; cityCount < citiesTypedArray.length(); cityCount++) {
                TypedArray city = getTypedArray(citiesTypedArray, cityCount);

                citiesList.add(getCityBuild(city));

                city.recycle();
            }

            countryList.add(getCountryBuild(countryTypedArray, citiesList));

            citiesTypedArray.recycle();
            countryTypedArray.recycle();
        }

        countriesTypedArray.recycle();
    }

    @NonNull
    private TypedArray getTypedArray(TypedArray typedArray, int index) {
        return context.getResources().obtainTypedArray(typedArray.getResourceId(index, 0));
    }

    private Country getCountryBuild(TypedArray countryTypedArray, List<City> citiesList) {
        return Country.builder()
                .setName(countryTypedArray.getString(Country.NAME_INDEX))
                .setCitiesList(citiesList)
                .build();
    }

    private City getCityBuild(TypedArray city) {
        return City.builder()
                .setId(city.getString(City.ID_INDEX))
                .setName(city.getString(City.NAME_INDEX))
                .setAddress(city.getString(City.ADDRESS_INDEX))
                .setImgSmall(city.getResourceId(City.CITY_IMG_SMALL_INDEX, 0))
                .setImgLarge(city.getResourceId(City.CITY_IMG_LARGE_INDEX, 0))
                .build();
    }

    class CountryHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cities_recycler_view) RecyclerView citiesRecyclerView;

        @BindView(R.id.suggestion_name) TextView countryName;

        CountryHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(final Country country) {
            countryName.setText(country.name());

            citiesRecyclerView.setAdapter(new CitiesAdapter(context, country.citiesList(), onCityListener));
        }

    }
}
