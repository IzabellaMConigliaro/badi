/*
 * File: AmenitiesActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.badi.R;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.presentation.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AmenitiesActivity extends BaseActivity {

    public static final String EXTRA_AMENITIES = "AmenitiesActivity.EXTRA_AMENITIES";
    public static final String EXTRA_BED_TYPE = "AmenitiesActivity.EXTRA_BED_TYPE";

    @BindView(R.id.text_amenities_bed_type) TextView bedTypeText;
    @BindView(R.id.text_amenities_tv) TextView tvText;
    @BindView(R.id.text_amenities_wifi) TextView wifiText;
    @BindView(R.id.text_amenities_air_conditioner) TextView airConditionerText;
    @BindView(R.id.text_amenities_parking) TextView parkingText;
    @BindView(R.id.text_amenities_smoker_friendly) TextView smokerFriendlyText;
    @BindView(R.id.text_amenities_pet_friendly) TextView petFriendlyText;
    @BindView(R.id.text_amenities_elevator) TextView elevatorText;
    @BindView(R.id.text_amenities_heating) TextView heatingText;
    @BindView(R.id.text_amenities_washing_machine) TextView washingMachineText;
    @BindView(R.id.text_amenities_dryer) TextView dryerText;
    @BindView(R.id.text_amenities_doorman) TextView doormanText;
    @BindView(R.id.text_amenities_couples_accepted) TextView couplesAcceptedText;
    @BindView(R.id.text_amenities_furnished) TextView furnishedText;
    @BindView(R.id.text_amenities_pool) TextView poolText;
    @BindView(R.id.text_amenities_private_bathroom) TextView privateBathroomText;
    @BindView(R.id.text_amenities_terrace) TextView terraceText;
    @BindView(R.id.text_amenities_window) TextView windowText;
    @BindView(R.id.text_amenities_dishwasher) TextView dishwasher;
    @BindView(R.id.text_amenities_wheelchair_friendly) TextView wheelchairFriendlyText;

    private List<AmenitiesAttribute> amenitiesAttributes;
    private int bedType;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, ArrayList<AmenitiesAttribute> amenitiesAttributes, Integer bedType) {
        Intent intent = new Intent(context, AmenitiesActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_AMENITIES, amenitiesAttributes);
        intent.putExtra(EXTRA_BED_TYPE, bedType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amenities);
        ButterKnife.bind(this);
        amenitiesAttributes = getIntent().getParcelableArrayListExtra(EXTRA_AMENITIES);
        bedType = getIntent().getIntExtra(EXTRA_BED_TYPE, 0);
        setupAmenities();
    }

    @OnClick(R.id.button_close)
    public void onCloseButtonClick() {
        supportFinishAfterTransition();
    }

    private void setupAmenities() {
        for (AmenitiesAttribute amenity : amenitiesAttributes) {
            switch (amenity.amenityType()) {
                case 0:
                    if (bedType == 1) {
                        bedTypeText.setText(getString(R.string.amenities_sofa_bed));
                        bedTypeText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_bed_type_sofa_bed, 0, 0, 0);
                    } else if (bedType == 2) {
                        bedTypeText.setText(getString(R.string.amenities_single_bed));
                        bedTypeText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_bed_type_single_bed, 0, 0, 0);
                    } else if (bedType == 3) {
                        bedTypeText.setText(getString(R.string.amenities_double_bed));
                        bedTypeText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_bed_type_double_bed, 0, 0, 0);
                    } else {
                        bedTypeText.setText(getString(R.string.amenities_unfurnished));
                        bedTypeText
                                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_bed_type_unfurnished, 0, 0, 0);
                    }
                    bedTypeText.setSelected(true);
                    break;
                case 1:
                    tvText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_tv_active, 0, 0, 0);
                    tvText.setSelected(true);
                    break;
                case 2:
                    wifiText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_wifi_active, 0, 0, 0);
                    wifiText.setSelected(true);
                    break;
                case 3:
                    airConditionerText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_amenities_air_conditioner_active, 0, 0, 0);
                    airConditionerText.setSelected(true);
                    break;
                case 4:
                    parkingText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_parking_active, 0, 0, 0);
                    parkingText.setSelected(true);
                    break;
                case 5:
                    smokerFriendlyText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_amenities_smoker_friendly_active, 0, 0, 0);
                    smokerFriendlyText.setSelected(true);
                    break;
                case 6:
                    petFriendlyText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_pet_friendly_active, 0, 0, 0);
                    petFriendlyText.setSelected(true);
                    break;
                case 7:
                    elevatorText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_elevator_active, 0, 0, 0);
                    elevatorText.setSelected(true);
                    break;
                case 8:
                    heatingText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_heating_active, 0, 0, 0);
                    heatingText.setSelected(true);
                    break;
                case 9:
                    washingMachineText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_amenities_washing_machine_active, 0, 0, 0);
                    washingMachineText.setSelected(true);
                    break;
                case 10:
                    dryerText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_dryer_active, 0, 0, 0);
                    dryerText.setSelected(true);
                    break;
                /*case 11:
                    dryerText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_dryer_active, 0, 0, 0);
                    break;*/
                case 12:
                    doormanText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_doorman_active, 0, 0, 0);
                    doormanText.setSelected(true);
                    break;
                case 13:
                    couplesAcceptedText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_amenities_couples_accepted_active, 0, 0, 0);
                    couplesAcceptedText.setSelected(true);
                    break;
                case 14:
                    furnishedText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_furnished_active, 0, 0, 0);
                    furnishedText.setSelected(true);
                    break;
                case 15:
                    poolText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_pool_active, 0, 0, 0);
                    poolText.setSelected(true);
                    break;
                case 16:
                    privateBathroomText.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.ic_amenities_private_bathroom_active, 0, 0, 0);
                    privateBathroomText.setSelected(true);
                    break;
                case 17:
                    terraceText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_terrace_active, 0, 0, 0);
                    terraceText.setSelected(true);
                    break;
                case 18:
                    windowText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_window_active, 0, 0, 0);
                    windowText.setSelected(true);
                    break;
                case 19:
                    dishwasher.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_dishwasher_active, 0, 0, 0);
                    dishwasher.setSelected(true);
                    break;
                case 20:
                    wheelchairFriendlyText
                            .setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_amenities_wheelchair_active, 0, 0, 0);
                    wheelchairFriendlyText.setSelected(true);
                default:
                    break;
            }
        }
    }
}
