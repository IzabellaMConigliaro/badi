/*
 * File: SearchPlaceRecentAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.PlaceTypeMapper;
import com.badi.data.entity.PlaceAddress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link PlaceAddress}.
 */
public class SearchPlaceRecentAdapter extends RecyclerView.Adapter<SearchPlaceRecentAdapter.SearchPlaceRecentHolder> {
    private List<PlaceAddress> recentSearches;
    private OnSearchListener onSearchListener;
    private boolean tagsEnabled = false;
    private PlaceTypeMapper placeTypeMapper;

    interface OnSearchListener {
        void onUserItemClicked(PlaceAddress search);
    }

    @Inject
    SearchPlaceRecentAdapter() {
        this.recentSearches = new ArrayList<>();
    }

    @Override
    public SearchPlaceRecentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place_suggestion, parent, false);
        return new SearchPlaceRecentHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchPlaceRecentHolder holder, int position) {
        holder.show(recentSearches.get(position));
    }

    @Override
    public int getItemCount() {
        return recentSearches == null ? 0 : recentSearches.size();
    }

    void setRecentSearches(List<PlaceAddress> recentSearches) {
        Collections.reverse(recentSearches);
        this.recentSearches = recentSearches;
        notifyDataSetChanged();
    }

    class SearchPlaceRecentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_place_title) TextView placeAddressTitleText;
        @BindView(R.id.text_place_subtitle) TextView placeAddressSubtitleText;
        @BindView(R.id.text_place_tag) TextView placeAddressTagText;

        SearchPlaceRecentHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void show(PlaceAddress search) {
            placeAddressTitleText.setText(search.name());
            placeAddressSubtitleText.setText(search.address());

            if (!tagsEnabled) {
                placeAddressTagText.setVisibility(View.GONE);
            } else if (placeTypeMapper != null) {
                Integer placeTypeNameResource = placeTypeMapper.getPlaceTypeNameResource(search.placeTypes());
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
            onSearchListener.onUserItemClicked(recentSearches.get(getAdapterPosition()));
        }
    }

    void setOnSearchListener(OnSearchListener onSearchListener) {
        this.onSearchListener = onSearchListener;
    }

    void enableTags() {
        tagsEnabled = true;
    }

    void setPlaceTypeMapper(PlaceTypeMapper placeTypeMapper) {
        this.placeTypeMapper = placeTypeMapper;
    }
}
