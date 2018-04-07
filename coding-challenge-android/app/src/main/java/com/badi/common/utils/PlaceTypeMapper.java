package com.badi.common.utils;

import android.util.SparseIntArray;

import com.badi.R;
import com.google.android.gms.location.places.Place;

import java.util.List;

import javax.inject.Inject;

public class PlaceTypeMapper {

    private final SparseIntArray placeTypeIdsAndNames;

    @Inject
    public PlaceTypeMapper() {
        placeTypeIdsAndNames = new SparseIntArray();
        initPlaceTypeIdsAndNames(placeTypeIdsAndNames);
    }

    public Integer getPlaceTypeNameResource(List<Integer> placeTypeIds) {
        if (placeTypeIds == null || placeTypeIds.isEmpty()) {
            return null;
        }

        for (Integer typeId : placeTypeIds) {
            if (hasTypeId(placeTypeIdsAndNames, typeId)) {
                return placeTypeIdsAndNames.get(typeId);
            }
        }

        return null;
    }

    private void initPlaceTypeIdsAndNames(SparseIntArray placeTypeIdsAndNames) {
        placeTypeIdsAndNames.put(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1, R.string.search_suggestion_tag_region);
        placeTypeIdsAndNames.put(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_2, R.string.search_suggestion_tag_region);
        placeTypeIdsAndNames.put(Place.TYPE_COUNTRY, R.string.search_suggestion_tag_country);
        placeTypeIdsAndNames.put(Place.TYPE_ESTABLISHMENT, R.string.search_suggestion_tag_establishment);
        placeTypeIdsAndNames.put(Place.TYPE_LOCALITY, R.string.search_suggestion_tag_city);
        placeTypeIdsAndNames.put(Place.TYPE_NEIGHBORHOOD, R.string.search_suggestion_tag_neighborhood);
        placeTypeIdsAndNames.put(Place.TYPE_ROUTE, R.string.search_suggestion_tag_street);
        placeTypeIdsAndNames.put(Place.TYPE_SUBLOCALITY_LEVEL_1, R.string.search_suggestion_tag_district);
        placeTypeIdsAndNames.put(Place.TYPE_TRANSIT_STATION, R.string.search_suggestion_tag_transit_station);
    }

    private boolean hasTypeId(SparseIntArray placeTypeIdsAndNames, Integer typeId) {
        return placeTypeIdsAndNames.get(typeId) != 0;
    }
}
