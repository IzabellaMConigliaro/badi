/*
 * File: FiltersActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.DateUtil;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.NumberOfTenants;
import com.badi.presentation.base.BaseActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

public class FiltersActivity extends BaseActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_FILTERS = "FiltersActivity.EXTRA_FILTERS";

    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tied_filters_price) TextInputEditText priceEditText;
    @BindView(R.id.checkbox_filters_available_now) CheckBox availableNowCheckBox;
    @BindView(R.id.checkbox_filters_available_date) CheckBox availableDateCheckBox;

    @BindViews({R.id.button_sofa_bed, R.id.button_single_bed, R.id.button_double_bed, R.id.button_unfurnished})
    CheckBox[] bedTypeCheckBoxArray;
    @BindViews({R.id.button_tv, R.id.button_wifi, R.id.button_air_conditioner, R.id.button_parking, R.id.button_smoker_friendly,
            R.id.button_pets_friendly, R.id.button_elevator, R.id.button_heating, R.id.button_washing_machine, R.id.button_dryer,
            R.id.button_doorman, R.id.button_couples_accepted, R.id.button_furnished, R.id.button_pool,
            R.id.button_private_bathroom, R.id.button_terrace, R.id.button_window, R.id.button_dishwasher,
            R.id.button_wheelchair_friendly})
    CheckBox[] amenitiesCheckBoxArray;
    @BindViews({R.id.button_gender_all_female, R.id.button_gender_all_male, R.id.button_gender_both})
    CheckBox[] genderCheckboxArray;
    @BindView(R.id.text_number_minimum_tenants) TextView minimumTenantsText;
    @BindView(R.id.text_number_minimum_tenants_description) TextView minimumTenantsDescriptionText;
    @BindView(R.id.text_number_maximum_tenants) TextView maximumTenantsText;
    @BindView(R.id.text_number_maximum_tenants_description) TextView maximumTenantsDescriptionText;

    private Calendar calendar = Calendar.getInstance();
    private Filters filters;
    private int year, month, day;
    private Integer maxPrice;
    private String gender;
    private NumberOfTenants numberOfTenants;
    private List<Integer> bedTypeFilterList, amenitiesFilterList;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, Filters filters) {
        Intent intent = new Intent(context, FiltersActivity.class);
        intent.putExtra(EXTRA_FILTERS, filters);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        ButterKnife.bind(this);
        setupToolbar();
        setupListeners();
        setupCalendar();
        filters = getIntent().getParcelableExtra(EXTRA_FILTERS);
        setResultActivity();
        initFilters();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filters, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        setResultFilters();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.getItemId() == R.id.action_clear_all) {
            clearFilters();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        collapsingToolbar.setCollapsedTitleTypeface(
                TypefaceUtils.load(getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
        collapsingToolbar.setExpandedTitleTypeface(
                TypefaceUtils.load(getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
    }

    private void setupListeners() {
        //Add text watcher to control the input of the user.
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    maxPrice = Integer.parseInt(editable.toString());
                } else {
                    maxPrice = null;
                }
            }
        });
    }

    private void initFilters() {

        if (filters == null) {
            filters = Filters.create(null, null, null, null, null, null);
        }

        if (filters.maxPrice() != null)
            priceEditText.setText(String.valueOf(filters.maxPrice()));

        if (filters.availableFrom() != null) {
            availableDateCheckBox.setText(DateUtil.getDateFormatWithLocale().format(filters.availableFrom()));
            availableDateCheckBox.setChecked(true);
        } else {
            availableDateCheckBox.setText(getString(R.string.filters_choose_date));
            availableDateCheckBox.setChecked(false);
        }

        bedTypeFilterList = filters.bedTypes();
        amenitiesFilterList = filters.amenities();
        gender = filters.gender();
        numberOfTenants = filters.numberOfTenants();

        if (bedTypeFilterList != null) {
            for (Integer bedID : bedTypeFilterList) {
                for (CheckBox checkBox : bedTypeCheckBoxArray) {
                    if (Integer.parseInt(checkBox.getTag().toString()) == bedID)
                        checkBox.setChecked(true);
                }
            }
        }

        if (amenitiesFilterList != null) {
            for (Integer amenitiesID : amenitiesFilterList) {
                for (CheckBox checkBox : amenitiesCheckBoxArray) {
                    if (Integer.parseInt(checkBox.getTag().toString()) == amenitiesID)
                        checkBox.setChecked(true);
                }
            }
        }

        if (gender != null) {
            for (CheckBox checkBox : genderCheckboxArray) {
                boolean isChecked = gender.equalsIgnoreCase(checkBox.getTag().toString());
                checkBox.setChecked(isChecked);
            }
        }

        if (numberOfTenants != null) {
            Integer minimum = numberOfTenants.minimum();
            if (minimum == null) {
                minimumTenantsText.setText(getString(R.string.filters_badis_total_amount_undefined));
            } else {
                minimumTenantsText.setText(String.valueOf(minimum));
                updateStyle(minimumTenantsText, minimumTenantsDescriptionText);
            }

            Integer maximum = numberOfTenants.maximum();
            if (maximum == null) {
                maximumTenantsText.setText(getString(R.string.filters_badis_total_amount_undefined));
            } else {
                maximumTenantsText.setText(String.valueOf(maximum));
                updateStyle(maximumTenantsText, maximumTenantsDescriptionText);
            }
        }
    }

    private void clearFilters() {
        filters = Filters.create(null, null, null, null, null, null);

        priceEditText.setText("");

        availableDateCheckBox.setText(getString(R.string.filters_choose_date));
        availableDateCheckBox.setChecked(false);
        availableNowCheckBox.setChecked(false);

        for (CheckBox bedCheckBox : bedTypeCheckBoxArray) {
            bedCheckBox.setChecked(false);
        }


        for (CheckBox amenitiesCheckBox : amenitiesCheckBoxArray) {
            amenitiesCheckBox.setChecked(false);
        }

        for (CheckBox checkBox : genderCheckboxArray) {
            checkBox.setChecked(false);
        }

        String undefinedAmountOfBadis = getString(R.string.filters_badis_total_amount_undefined);
        minimumTenantsText.setText(undefinedAmountOfBadis);
        maximumTenantsText.setText(undefinedAmountOfBadis);
        updateStyle(minimumTenantsText, minimumTenantsDescriptionText);
        updateStyle(maximumTenantsText, maximumTenantsDescriptionText);

        onClickApplyFiltersButton();
    }

    private void setupCalendar() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    @OnClick(R.id.checkbox_filters_available_now)
    void onClickAvailableNow() {
        availableDateCheckBox.setChecked(false);
        availableNowCheckBox.setChecked(true);
        availableDateCheckBox.setText(getString(R.string.filters_choose_date));
        filters = filters.withAvailableFrom(new Date());
    }

    @OnClick(R.id.checkbox_filters_available_date)
    void onClickAvailableDate() {
        availableDateCheckBox.setChecked(false);

        //Reset date
        filters = filters.withAvailableFrom(null);
        availableDateCheckBox.setText(getString(R.string.filters_choose_date));
        DatePickerDialog dialog = new DatePickerDialog(this, this, year, month, day);
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date dateRepresentation = calendar.getTime();
        //Set date
        filters = filters.withAvailableFrom(dateRepresentation);
        availableNowCheckBox.setChecked(false);
        availableDateCheckBox.setChecked(true);
        availableDateCheckBox.setText(DateUtil.getDateFormatWithLocale().format(dateRepresentation));
    }

    @OnCheckedChanged({R.id.button_gender_all_female, R.id.button_gender_all_male, R.id.button_gender_both})
    void onGenderCheckedChanged(CompoundButton changedCheckBox, boolean checked) {
        if (!checked) {
            return;
        }

        for (CheckBox checkBox : genderCheckboxArray) {
            if (changedCheckBox.getId() != checkBox.getId()) {
                checkBox.setChecked(false);
            }
        }
    }

    @OnClick(R.id.button_filters_apply)
    void onClickApplyFiltersButton() {

        bedTypeFilterList = new ArrayList<>();
        amenitiesFilterList = new ArrayList<>();
        gender = null;
        numberOfTenants = null;

        for (CheckBox checkBox : bedTypeCheckBoxArray) {
            if (checkBox.isChecked()) {
                bedTypeFilterList.add(Integer.parseInt(checkBox.getTag().toString()));
            }
        }

        for (CheckBox checkBox : amenitiesCheckBoxArray) {
            if (checkBox.isChecked()) {
                amenitiesFilterList.add(Integer.parseInt(checkBox.getTag().toString()));
            }
        }

        for (CheckBox checkBox : genderCheckboxArray) {
            if (checkBox.isChecked() && checkBox.getTag() != null) {
                gender = checkBox.getTag().toString();
            }
        }

        numberOfTenants = determineNumberOfTenants();

        filters = filters.withMaxPrice(maxPrice);

        filters = filters.withBedTypes(!bedTypeFilterList.isEmpty() ? bedTypeFilterList : null);

        filters = filters.withAmenities(!amenitiesFilterList.isEmpty() ? amenitiesFilterList : null);

        filters = filters.withGender(gender);

        filters = filters.withNumberOfTenants(numberOfTenants);

        setResultFilters();
    }

    private void setResultFilters() {
        if (filters.maxPrice() == null && filters.gender() == null && filters.numberOfTenants() == null && filters.bedTypes()
                == null && filters.amenities() == null && filters.availableFrom() == null) {
            setResultActivity();
            supportFinishAfterTransition();
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(EXTRA_FILTERS, filters);
            setResult(Activity.RESULT_OK, resultIntent);
            supportFinishAfterTransition();
        }
    }

    private void setResultActivity() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
    }

    @OnClick(R.id.button_add_minimum_tenant)
    public void onClickAddMinimumTenant() {
        increaseAmountOfTenants(minimumTenantsText);
        updateStyle(minimumTenantsText, minimumTenantsDescriptionText);
    }

    @OnClick(R.id.button_remove_minimum_tenant)
    public void onClickRemoveMinimumTenant() {
        decreaseAmountOfTenants(minimumTenantsText);
        updateStyle(minimumTenantsText, minimumTenantsDescriptionText);
    }

    @OnClick(R.id.button_add_maximum_tenant)
    public void onClickAddMaximumTenant() {
        increaseAmountOfTenants(maximumTenantsText);
        updateStyle(maximumTenantsText, maximumTenantsDescriptionText);
    }

    @OnClick(R.id.button_remove_maximum_tenant)
    public void onClickRemoveMaximumTenant() {
        decreaseAmountOfTenants(maximumTenantsText);
        updateStyle(maximumTenantsText, maximumTenantsDescriptionText);
    }

    private void increaseAmountOfTenants(TextView amountOfTenants) {
        String amountText = amountOfTenants.getText().toString();
        if (amountText.equalsIgnoreCase(getString(R.string.filters_badis_total_amount_undefined))) {
            amountText = "1";
        } else {
            int amountValue = Integer.parseInt(amountText);
            if (amountValue < 9) {
                amountValue++;
                amountText = String.valueOf(amountValue);
            }
        }
        amountOfTenants.setText(amountText);
    }

    private void decreaseAmountOfTenants(TextView amountOfTenants) {
        String amountText = amountOfTenants.getText().toString();
        if (!amountText.equalsIgnoreCase(getString(R.string.filters_badis_total_amount_undefined))) {
            int amountValue = Integer.parseInt(amountText);
            if (amountValue == 1) {
                amountText = getString(R.string.filters_badis_total_amount_undefined);
            } else {
                amountValue--;
                amountText = String.valueOf(amountValue);
            }
        }
        amountOfTenants.setText(amountText);
    }

    private void updateStyle(TextView amountOfTenants, TextView amountOfTenantsDescription) {
        if (amountOfTenants.getText().toString().equalsIgnoreCase(getString(R.string
                .filters_badis_total_amount_undefined))) {
            setDefaultState(amountOfTenants);
            setDefaultState(amountOfTenantsDescription);
        } else {
            setHighlightedState(amountOfTenants);
            setHighlightedState(amountOfTenantsDescription);
        }
    }

    private void setDefaultState(TextView amountOfTenants) {
        int greyColor = ContextCompat.getColor(this, R.color.grey_400);
        amountOfTenants.setTextColor(greyColor);
    }

    private void setHighlightedState(TextView amountOfTenants) {
        int blackColor = ContextCompat.getColor(this, R.color.black);
        amountOfTenants.setTextColor(blackColor);
    }

    private NumberOfTenants determineNumberOfTenants() {
        NumberOfTenants numberOfTenants = null;

        final String undefinedAmountOfTenants = getString(R.string.filters_badis_total_amount_undefined);
        Integer minimumTenants = null;
        Integer maximumTenants = null;
        if (!minimumTenantsText.getText().toString().equalsIgnoreCase(undefinedAmountOfTenants)) {
            minimumTenants = Integer.parseInt(minimumTenantsText.getText().toString());
        }
        if (!maximumTenantsText.getText().toString().equalsIgnoreCase(undefinedAmountOfTenants)) {
            maximumTenants = Integer.parseInt(maximumTenantsText.getText().toString());
        }

        if (minimumTenants != null || maximumTenants != null) {
            numberOfTenants = NumberOfTenants.create(minimumTenants, maximumTenants);
        }

        return numberOfTenants;
    }
}
