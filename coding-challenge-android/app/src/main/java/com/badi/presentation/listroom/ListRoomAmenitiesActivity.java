/*
 * File: ListRoomAmenitiesActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.badi.R;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.presentation.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListRoomAmenitiesActivity extends BaseActivity {

    public static final String LIST_ROOM_EXTRA_AMENITIES = "ListRoomAmenitiesActivity.LIST_ROOM_EXTRA_AMENITIES";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindViews({R.id.button_tv, R.id.button_wifi, R.id.button_air_conditioner, R.id.button_parking, R.id.button_smoker_friendly,
            R.id.button_pets_friendly, R.id.button_elevator, R.id.button_heating, R.id.button_washing_machine, R.id.button_dryer,
            R.id.button_doorman, R.id.button_couples_accepted, R.id.button_furnished, R.id.button_pool,
            R.id.button_private_bathroom, R.id.button_terrace, R.id.button_window, R.id.button_dishwasher,
            R.id.button_wheelchair_friendly})
    CheckBox[] amenitiesCheckBoxArray;

    private List<AmenitiesAttribute> amenities;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, ArrayList<AmenitiesAttribute> amenities) {
        Intent intent = new Intent(context, ListRoomAmenitiesActivity.class);
        intent.putParcelableArrayListExtra(LIST_ROOM_EXTRA_AMENITIES, amenities);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room_amenities);
        ButterKnife.bind(this);
        setupToolbar();
        amenities = getIntent().getParcelableArrayListExtra(LIST_ROOM_EXTRA_AMENITIES);
        setResultActivity();
        initAmenities();
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supportFinishAfterTransition();
            }
        });
    }

    private void initAmenities() {
        if (amenities != null) {
            for (AmenitiesAttribute attribute : amenities) {
                for (CheckBox checkBox : amenitiesCheckBoxArray) {
                    if (Integer.parseInt(checkBox.getTag().toString()) == attribute.amenityType())
                        checkBox.setChecked(true);
                }
            }
        }
    }

    @OnClick(R.id.button_list_room_amenities_apply)
    void onClickApplyListRoomAmenitiesButton() {
        amenities = new ArrayList<>();

        for (CheckBox checkBox : amenitiesCheckBoxArray) {
            if (checkBox.isChecked()) {
                amenities.add(AmenitiesAttribute.create(Integer.parseInt(checkBox.getTag().toString())));
            }
        }

        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra(LIST_ROOM_EXTRA_AMENITIES, new ArrayList<>(amenities));
        setResult(Activity.RESULT_OK, resultIntent);
        supportFinishAfterTransition();
    }

    private void setResultActivity() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
    }


}
