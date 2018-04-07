package com.badi.common.utils;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 */
public class SimplePlaceAutoCompleteAdapter extends PlaceAutoCompleteAdapter {
    public SimplePlaceAutoCompleteAdapter(GeoDataClient geoDataClient, LatLngBounds bounds, AutocompleteFilter filter) {
        super(geoDataClient, bounds, filter);
    }

    @Override
    public SimplePredictionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_place_suggestion, parent, false);
        return new SimplePredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((SimplePredictionHolder) holder).show(resultList.get(position));
    }

    @Override
    protected void invalidListResult() {
        resultList = new ArrayList<>();
        notifyDataSetChanged();

        if (onListPopulationListener != null) {
            onListPopulationListener.onListPopulated();
        }
    }

    public class SimplePredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_title) TextView placeAddressTitleText;

        SimplePredictionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void show(AutocompletePrediction prediction) {
            placeAddressTitleText.setText(prediction.getFullText(STYLE_BOLD));
        }

        @Override
        public void onClick(View v) {
            onPlaceListener.onUserItemClicked(getAdapterPosition());
        }
    }
}
