/*
 * File: AmenitiesAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badi.R;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.data.entity.room.RoomDetail;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Adapter that manages a collection of {@link AmenitiesAttribute} & bedType.
 * Possible types bedType: {"sofa_bed"=>1, "single_bed"=>2, "double_bed"=>3}
 * Array of amenities with the following attributes: amenity_type: We add "bed"=>0
 * {"tv"=>1, "wifi"=>2, "air_conditioner"=>3, "parking"=>4, "smoker_friendly"=>5, "pet_friendly"=>6, "elevator"=>7, "heating"=>8,
 * "washing_machine"=>9, "dryer"=>10, "transportation_nearby"=>11, "doorman"=>12, "couples_accepted"=>13, "furnished"=>14,
 * "pool"=>15, "own_bathroom"=>16, "terrace"=>17, "window"=>18, "dishwasher"=>19}
 */


public class AmenitiesAdapter extends RecyclerView.Adapter<AmenitiesAdapter.AmenitiesHolder> {
    private List<AmenitiesAttribute> amenitiesList;
    private Integer bedType;

    public AmenitiesAdapter() {
        this.amenitiesList = new ArrayList<>();
    }

    @Override
    public AmenitiesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amenity, parent, false);
        return new AmenitiesHolder(view);
    }

    @Override
    public void onBindViewHolder(AmenitiesHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(amenitiesList.get(position));
    }

    @Override
    public int getItemCount() {
        return amenitiesList == null ? 0 : amenitiesList.size();
    }

    public void setAmenities(List<AmenitiesAttribute> amenities, Integer bedType) {
        amenitiesList = amenities;
        this.bedType = bedType;
        notifyDataSetChanged();
    }

    class AmenitiesHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_amenity) ImageView amenityImage;

        AmenitiesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(AmenitiesAttribute amenity) {
            int resourceID;
            switch (amenity.amenityType()) {
                case 0:
                    if (bedType == RoomDetail.BED_TYPE_SOFA_BED) {
                        resourceID = R.drawable.ic_amenities_bed_type_sofa_bed;
                    } else if (bedType == RoomDetail.BED_TYPE_SINGLE_BED) {
                        resourceID = R.drawable.ic_amenities_bed_type_single_bed;
                    } else if (bedType == RoomDetail.BED_TYPE_DOUBLE_BED)  {
                        resourceID = R.drawable.ic_amenities_bed_type_double_bed;
                    } else {
                        resourceID = R.drawable.ic_amenities_bed_type_unfurnished;
                    }
                    break;
                case 1:
                    resourceID = R.drawable.ic_amenities_tv_active;
                    break;
                case 2:
                    resourceID = R.drawable.ic_amenities_wifi_active;
                    break;
                case 3:
                    resourceID = R.drawable.ic_amenities_air_conditioner_active;
                    break;
                case 4:
                    resourceID = R.drawable.ic_amenities_parking_active;
                    break;
                case 5:
                    resourceID = R.drawable.ic_amenities_smoker_friendly_active;
                    break;
                case 6:
                    resourceID = R.drawable.ic_amenities_pet_friendly_active;
                    break;
                case 7:
                    resourceID = R.drawable.ic_amenities_elevator_active;
                    break;
                case 8:
                    resourceID = R.drawable.ic_amenities_heating_active;
                    break;
                case 9:
                    resourceID = R.drawable.ic_amenities_washing_machine_active;
                    break;
                case 10:
                    resourceID = R.drawable.ic_amenities_dryer_active;
                    break;
                /*case 11:
                    resourceID = R.drawable.tran;
                    break;*/
                case 12:
                    resourceID = R.drawable.ic_amenities_doorman_active;
                    break;
                case 13:
                    resourceID = R.drawable.ic_amenities_couples_accepted_active;
                    break;
                case 14:
                    resourceID = R.drawable.ic_amenities_furnished_active;
                    break;
                case 15:
                    resourceID = R.drawable.ic_amenities_pool_active;
                    break;
                case 16:
                    resourceID = R.drawable.ic_amenities_private_bathroom_active;
                    break;
                case 17:
                    resourceID = R.drawable.ic_amenities_terrace_active;
                    break;
                case 18:
                    resourceID = R.drawable.ic_amenities_window_active;
                    break;
                case 19:
                    resourceID = R.drawable.ic_amenities_dishwasher_active;
                    break;
                case 20:
                    resourceID = R.drawable.ic_amenities_wheelchair_active;
                    break;
                default:
                    resourceID = R.drawable.ic_amenities_bed_type_double_bed;
                    break;
            }
            amenityImage.setImageResource(resourceID);
        }
    }
}
