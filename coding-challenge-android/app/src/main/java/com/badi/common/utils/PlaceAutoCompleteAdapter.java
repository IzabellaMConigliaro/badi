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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.badi.R;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBufferResponse;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.RuntimeExecutionException;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Adapter that handles Autocomplete requests from the Places Geo Data API.
 * {@link AutocompletePrediction} results from the API are frozen and stored directly in this
 * adapter. (See {@link AutocompletePrediction#freeze()}.)
 */
public class PlaceAutoCompleteAdapter
        extends RecyclerView.Adapter<PlaceAutoCompleteAdapter.PredictionHolder> implements Filterable {

    private static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);

    /**
     * Current results returned by this adapter.
     */
    private ArrayList<AutocompletePrediction> resultList;

    /**
     * Handles autocomplete requests.
     */
    private GeoDataClient geoDataClient;

    /**
     * The bounds used for Places Geo Data autocomplete API requests.
     */
    private LatLngBounds bounds;

    /**
     * The autocomplete filter used to restrict queries to a specific set of place types.
     */
    private AutocompleteFilter placeFilter;

    private OnPlaceListener onPlaceListener;
    private OnListPopulationListener onListPopulationListener;
    private boolean tagsEnabled = false;
    private PlaceTypeMapper placeTypeMapper;

    public interface OnPlaceListener {
        void onUserItemClicked(Integer position);
    }

    public interface OnListPopulationListener {
        void onListPopulated();
    }

    public PlaceAutoCompleteAdapter(GeoDataClient geoDataClient, LatLngBounds bounds, AutocompleteFilter filter) {
        this.geoDataClient = geoDataClient;
        this.bounds = bounds;
        this.placeFilter = filter;
    }

    /**
     * Sets the bounds for all subsequent queries.
     */
    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public void clear() {
        if (resultList != null) {
            this.resultList.clear();
            notifyDataSetChanged();
        }
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                // We need a separate list to store the results, since
                // this is run asynchronously.
                ArrayList<AutocompletePrediction> filterData = new ArrayList<>();

                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    filterData = getAutocomplete(constraint);
                }

                results.values = filterData;
                if (filterData != null) {
                    results.count = filterData.size();
                } else {
                    results.count = 0;
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    resultList = (ArrayList<AutocompletePrediction>) results.values;
                    notifyDataSetChanged();
                    if (onListPopulationListener != null) {
                        onListPopulationListener.onListPopulated();
                    }
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetChanged();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getFullText(null);
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    /**
     * Submits an autocomplete query to the Places Geo Data Autocomplete API.
     * Results are returned as frozen AutocompletePrediction objects, ready to be cached.
     * Returns an empty list if no results were found.
     * Returns null if the API client is not available or the query did not complete
     * successfully.
     * This method MUST be called off the main UI thread, as it will block until data is returned
     * from the API, which may include a network request.
     *
     * @param constraint Autocomplete query string
     * @return Results from the autocomplete API or null if the query was not successful.
     * @see GeoDataClient#getAutocompletePredictions(String, LatLngBounds, AutocompleteFilter)
     * @see AutocompletePrediction#freeze()
     */
    private ArrayList<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        Timber.i("Starting autocomplete query for: ".concat(constraint.toString()));

        // Submit the query to the autocomplete API and retrieve a PendingResult that will
        // contain the results when the query completes.
        Task<AutocompletePredictionBufferResponse> results =
                geoDataClient.getAutocompletePredictions(constraint.toString(), bounds, placeFilter);

        // This method should have been called off the main UI thread. Block and wait for at most
        // 60s for a result from the API.
        try {
            Tasks.await(results, 60, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            e.printStackTrace();
        }

        try {
            AutocompletePredictionBufferResponse autocompletePredictions = results.getResult();

            Timber.i("Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        } catch (RuntimeExecutionException e) {
            // If the query did not complete successfully return null
            Timber.e("Error getting autocomplete prediction API call".concat(e.toString()));
            return null;
        }
    }

    @Override
    public PredictionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_suggestion, parent, false);
        return new PredictionHolder(view);
    }

    @Override
    public void onBindViewHolder(PredictionHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(resultList.get(position));
    }

    @Override
    public int getItemCount() {
        if (resultList != null)
            return resultList.size();
        else
            return 0;
    }

    public AutocompletePrediction getItem(int position) {
        return resultList.get(position);
    }

    class PredictionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_title) TextView placeAddressTitleText;
        @BindView(R.id.text_place_subtitle) TextView placeAddressSubtitleText;
        @BindView(R.id.text_place_tag) TextView placeAddressTagText;

        PredictionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void show(AutocompletePrediction prediction) {
            placeAddressTitleText.setText(prediction.getPrimaryText(STYLE_BOLD));
            placeAddressSubtitleText.setText(prediction.getSecondaryText(STYLE_BOLD));

            if (!tagsEnabled) {
                placeAddressTagText.setVisibility(View.GONE);
            } else if (placeTypeMapper != null) {
                Integer placeTypeNameResource = placeTypeMapper.getPlaceTypeNameResource(prediction.getPlaceTypes());
                if (placeTypeNameResource != null) {
                    placeAddressTagText.setText(placeTypeNameResource);
                    placeAddressTagText.setVisibility(View.VISIBLE);
                } else {
                    placeAddressTagText.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            onPlaceListener.onUserItemClicked(getAdapterPosition());
        }
    }

    public void setOnPlaceListener(PlaceAutoCompleteAdapter.OnPlaceListener onPlaceListener) {
        this.onPlaceListener = onPlaceListener;
    }

    public void setOnListPopulationListener(OnListPopulationListener onListPopulationListener) {
        this.onListPopulationListener = onListPopulationListener;
    }

    public void enableTags() {
        tagsEnabled = true;
    }

    public void setPlaceTypeMapper(PlaceTypeMapper placeTypeMapper) {
        this.placeTypeMapper = placeTypeMapper;
    }
}
