/*
 * File: PlaceAutoCompleteAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.badi.data.entity.PlaceAddress;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.SimplePredictionHolder> {
    private List<PlaceAddress> placesList;
    private OnPlaceListener onPlaceListener;

    public interface OnPlaceListener {
        void onUserItemClicked(Integer position);
    }

    public PlaceAdapter(List<PlaceAddress> places) {
        this.placesList = places;
    }

    public PlaceAddress getItem(int position) {
        return placesList.get(position);
    }

    @Override
    public SimplePredictionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_place_suggestion, parent, false);
        return new SimplePredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(SimplePredictionHolder holder, int position) {
        holder.show(placesList.get(position));
    }

    @Override
    public int getItemCount() {
        if (placesList != null)
            return placesList.size();
        else
            return 0;
    }

    public void setPlacesList(List<PlaceAddress> placesList) {
        this.placesList = placesList;
        notifyDataSetChanged();
    }

    public class SimplePredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_title) TextView placeAddressTitleText;

        SimplePredictionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void show(PlaceAddress placeAddress) {
            placeAddressTitleText.setText(placeAddress.address());
        }

        @Override
        public void onClick(View v) {
            onPlaceListener.onUserItemClicked(getAdapterPosition());
        }
    }

    public void setOnPlaceListener(OnPlaceListener onPlaceListener) {
        this.onPlaceListener = onPlaceListener;
    }
}
