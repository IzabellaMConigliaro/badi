/*
 * File: ListRoomActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.di.HasComponent;
import com.badi.common.di.components.DaggerRoomComponent;
import com.badi.common.di.components.RoomComponent;
import com.badi.common.di.modules.RoomModule;
import com.badi.common.utils.DateUtil;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.PriceUtils;
import com.badi.common.utils.ViewUtil;
import com.badi.common.utils.recyclerview.SimpleItemTouchHelperCallback;
import com.badi.data.entity.PlaceAddress;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.data.entity.room.NonBadiTenants;
import com.badi.data.entity.room.PricesAttribute;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.user.Picture;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.presentation.base.BaseActivity;
import com.badi.presentation.navigation.Navigator;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.badi.presentation.listroom.ListRoomPicturesAdapter.URL_DEFAULT_IMAGE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ListRoomActivity extends BaseActivity implements HasComponent<RoomComponent>, ListRoomContract.View {

    public static final String EXTRA_EDIT = "ListRoomActivity.EXTRA_EDIT";
    public static final String EXTRA_ROOM = "ListRoomActivity.EXTRA_ROOM";
    public static final String LIST_ROOM_EXTRA_ROOM = "ListRoomActivity.LIST_ROOM_EXTRA_ROOM";
    private static final String EXTRA_RESTORE_STATE = ListRoomActivity.class.getSimpleName() + ".RESTORE_STATE";
    private static final String EXTRA_RESTORE_ROOM = ListRoomActivity.class.getSimpleName() + ".RESTORE_STATE_ROOM";

    private static final int MAX_CHARACTERS_ROOM_TITLE = 35;
    private static final int MAX_CHARACTERS_DESCRIPTION = 3000;

    @Inject ListRoomPresenter listRoomPresenter;
    @Inject PreferencesHelper preferencesHelper;

    @BindView(R.id.activity_list_room) CoordinatorLayout listRoomActivityLayout;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.layout_pictures) View picturesLayout;
    @BindView(R.id.layout_about_the_room) View aboutTheRoomLayout;
    @BindView(R.id.layout_availability) View availabilityLayout;
    @BindView(R.id.layout_who_lives_there) View whoLivesThereLayout;
    @BindView(R.id.layout_tenants) View tenantsLayout;
    @BindView(R.id.layout_type_of_bed) View typeOfBedLayout;
    @BindView(R.id.layout_amenities) View amenitiesLayout;
    @BindView(R.id.text_list_room_cover_placeholder) TextView coverPlaceholderText;
    @BindView(R.id.image_list_room_cover) ImageView coverImageView;
    @BindView(R.id.recycler_view_pictures) RecyclerView picturesRecyclerView;
    @BindView(R.id.progress_loading_image) ProgressBar loadingImageProgress;
    @BindView(R.id.text_title_room_characters) TextView charactersTitleRoomText;
    @BindView(R.id.til_list_room_room_title) TextInputLayout roomTitleLayout;
    @BindView(R.id.tied_list_room_room_title) TextInputEditText roomTitleEditText;
    @BindView(R.id.til_list_room_location) TextInputLayout roomLocationLayout;
    @BindView(R.id.tied_list_room_location) TextInputEditText roomLocationEditText;
    @BindView(R.id.layout_price_attributes) LinearLayout priceAttributesLayout;
    @BindView(R.id.tied_list_room_price) TextInputEditText roomPriceEditText;
    @BindView(R.id.text_currency) TextView currencyText;
    @BindView(R.id.checkbox_bills_included) CheckBox billsIncludedCheckbox;
    @BindView(R.id.checkbox_bills_not_included) CheckBox billsNotIncludedCheckbox;
    @BindView(R.id.checkbox_from_right_now) CheckBox fromNowCheckBox;
    @BindView(R.id.checkbox_from_date) CheckBox fromDateCheckBox;
    @BindView(R.id.checkbox_to_no_date) CheckBox toNoDateCheckBox;
    @BindView(R.id.checkbox_to_date) CheckBox toDateCheckBox;
    @BindView(R.id.checkbox_minimum_stay_one_month) CheckBox oneMonthMinimumStayCheckbox;
    @BindView(R.id.checkbox_minimum_stay_two_months) CheckBox twoMonthsMinimumStayCheckbox;
    @BindView(R.id.checkbox_minimum_stay_three_months) CheckBox threeMonthsMinimumStayCheckbox;
    @BindView(R.id.checkbox_minimum_stay_six_months) CheckBox sixMonthsMinimumStayCheckbox;
    @BindView(R.id.checkbox_minimum_stay_twelve_months) CheckBox twelveMonthsMinimumStayCheckbox;
    @BindView(R.id.text_number_female_tenants) TextView numberFemaleTenantsText;
    @BindView(R.id.text_number_male_tenants) TextView numberMaleTenantsText;
    @BindView(R.id.recycler_view_tenants) RecyclerView tenantsRecyclerView;
    @BindView(R.id.check_box_single_bed) CheckBox singleBedCheckBox;
    @BindView(R.id.check_box_double_bed) CheckBox doubleBedCheckBox;
    @BindView(R.id.check_box_sofa_bed) CheckBox sofaBedCheckBox;
    @BindView(R.id.check_box_unfurnished) CheckBox unfurnishedCheckBox;
    @BindView(R.id.layout_fake_amenities) LinearLayout fakeAmenitiesLayout;
    @BindView(R.id.recycler_view_amenities) RecyclerView amenitiesRecycleView;
    @BindView(R.id.text_list_room_description_characters) TextView charactersDescriptionText;
    @BindView(R.id.tied_list_room_description) TextInputEditText roomDescriptionEditText;
    @BindView(R.id.button_list_room) Button listRoomButton;

    private RoomComponent roomComponent;

    private ListRoomPicturesAdapter listRoomPicturesAdapter;
    private ListRoomAmenitiesAdapter listRoomAmenitiesAdapter;
    private ListRoomTenantsAdapter listRoomTenantsAdapter;

    private RoomDetail room;
    private Calendar calendar = Calendar.getInstance();
    private int year, month, day, imagePosition = 0;
    private boolean isLoadingImage = false;
    private boolean isEditMode = false;
    private boolean shouldNavigateToMyRoomsSwitch = false;

    /**
     * Return an Intent to start this Activity in list room mode.
     */
    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ListRoomActivity.class);
    }

    /**
     * Return an Intent to start this Activity in edit room mode.
     */
    public static Intent getCallingIntentEdit(Context context, RoomDetail room) {
        Intent intent = new Intent(context, ListRoomActivity.class);
        intent.putExtra(EXTRA_EDIT, true);
        intent.putExtra(EXTRA_ROOM, room);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room);
        this.initializeInjector();
        this.getComponent().inject(this);
        ButterKnife.bind(this);
        listRoomPresenter.attachView(this);

        boolean isRestoringState = false;
        if (savedInstanceState != null) {
            isRestoringState = savedInstanceState.getBoolean(EXTRA_RESTORE_STATE, false);
            this.room = savedInstanceState.getParcelable(EXTRA_RESTORE_ROOM);
        }

        setupToolbar();
        setupChecks();
        setupCalendar();

        if (!isRestoringState) {
            setupRoom();
        } else {
            populateListRoom();
        }
    }

    private void setupRoom() {
        isEditMode = getIntent().getBooleanExtra(EXTRA_EDIT, false);
        if (isEditMode) {
            collapsingToolbar.setTitle(getString(R.string.title_edit_room));
            listRoomButton.setText(R.string.list_room_save_changes);
            RoomDetail roomDetail = getIntent().getParcelableExtra(EXTRA_ROOM);
            if (roomDetail != null) {
                this.room = roomDetail;
                populateListRoom();
            } else {
                populateRoomInitialState();
            }
        } else {
            listRoomPresenter.getRoomSavedInPrefs();
        }
    }

    @Override
    public void onDestroy() {
        listRoomPresenter.detachView();
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!isEditMode) {
            removePlaceHolderImagesBeforeSave();
            listRoomPresenter.saveRoomInPrefs(room);
        } else
            super.onBackPressed();
    }

    private void setupToolbar() {
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        collapsingToolbar.setCollapsedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
        collapsingToolbar.setExpandedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
    }

    @Override
    public RoomComponent getComponent() {
        return roomComponent;
    }

    private void initializeInjector() {
        roomComponent = DaggerRoomComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .roomModule(new RoomModule())
                .build();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Snackbar.make(listRoomActivityLayout, R.string.message_uploading_image, Snackbar.LENGTH_LONG).show();
                listRoomPresenter.uploadImage(imageFiles.get(0).getPath());
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(ListRoomActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

        switch (requestCode) {
            case (Navigator.REQUEST_CODE_LIST_ROOM_AMENITIES_ACTIVITY) :
                if (resultCode == Activity.RESULT_OK) {
                    List<AmenitiesAttribute> amenities = data.getParcelableArrayListExtra(
                            ListRoomAmenitiesActivity.LIST_ROOM_EXTRA_AMENITIES);
                    room = room.withAmenitiesAttributes(amenities);
                    if (room.amenitiesAttributes().isEmpty()) {
                        fakeAmenitiesLayout.setVisibility(View.VISIBLE);
                        amenitiesRecycleView.setVisibility(View.GONE);
                    } else {
                        fakeAmenitiesLayout.setVisibility(View.GONE);
                        amenitiesRecycleView.setVisibility(View.VISIBLE);
                        setupAmenitiesAdapter();
                    }
                }
                break;
            case Navigator.REQUEST_CODE_LIST_ROOM_PLACE_ACTIVITY :
                if (resultCode == RESULT_OK) {
                    PlaceAddress placeAddress = data.getParcelableExtra(ListRoomPlaceActivity.LIST_ROOM_EXTRA_PLACE);
                    room = room.withAddress(placeAddress.address());
                    room = room.withStreet(placeAddress.street());
                    room = room.withStreetNumber(placeAddress.streetNumber());
                    room = room.withCity(placeAddress.city());
                    room = room.withCountry(placeAddress.country());
                    room = room.withPostalCode(placeAddress.postalCode());
                    room = room.withLatitude(placeAddress.latitude());
                    room = room.withLongitude(placeAddress.longitude());
                    roomLocationLayout.setError(null);
                    roomLocationLayout.setErrorEnabled(false);
                    roomLocationEditText.setText(placeAddress.address());
                    priceAttributesLayout.setVisibility(View.VISIBLE);
                    roomPriceEditText.setText(null);
                    currencyText.setText(PriceUtils.getPriceSymbol(context(), placeAddress.currencyCode()));
                    manageCurrency(placeAddress.currencyCode());
                }
                break;
            case Navigator.REQUEST_CODE_LIST_ROOM_ROOMMATES_ACTIVITY :
                if (resultCode == RESULT_OK) {
                    List<Tenant> tenants = data.getParcelableArrayListExtra(ListRoomRoommatesActivity.LIST_ROOM_EXTRA_ROOMMATES);
                    room = room.withTenants(tenants);
                    if (!isEditMode) {
                        listRoomPresenter.editRoom(room);
                    } else {
                        listRoomTenantsAdapter.setTenants(room.tenants());
                    }
                } else
                    if (!isEditMode) showShareListingDialog();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_RESTORE_STATE, true);
        outState.putParcelable(EXTRA_RESTORE_ROOM, this.room);

        super.onSaveInstanceState(outState);
    }

    private void manageCurrency(String currencyCode) {
        if (room.pricesAttributes().isEmpty()) {
            room.pricesAttributes().add(
                    PricesAttribute.builder()
                            .setCurrency(currencyCode)
                            .build());
        } else {
            PricesAttribute pricesAttributeEdited
                    = room.pricesAttributes().get(0).withCurrency(currencyCode);
            room.pricesAttributes().clear();
            room.pricesAttributes().add(pricesAttributeEdited);
        }
        room = room.withPricesAttributes(room.pricesAttributes());
    }

    private void populateListRoom() {
        switch (room.pictures().size()) {
            case 0:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_room).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_kitchen).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_living_room).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_bath).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            case 1:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_kitchen).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_living_room).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_bath).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            case 2:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_living_room).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_bath).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            case 3:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_bath).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            case 4:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build());
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            case 5:
                room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build());
                break;
            default:
                boolean placeholderPictureVisible = false;
                for (Picture picture : room.pictures()) {
                    if (picture.url().equals(URL_DEFAULT_IMAGE))
                        placeholderPictureVisible = true;
                }
                if (!placeholderPictureVisible)
                    room.pictures().add(Picture.builder().setId(R.drawable.ic_picture_camera).setUrl(URL_DEFAULT_IMAGE).build());
                break;
        }
        if (!room.pictures().isEmpty() && !room.pictures().get(0).url().equals(URL_DEFAULT_IMAGE))
            showImageCover(room.pictures().get(0));

        setupPicturesAdapter();
        roomTitleEditText.setText(room.title());
        roomLocationEditText.setText(room.address());

        if (!room.address().isEmpty())
            priceAttributesLayout.setVisibility(View.VISIBLE);

        if (!room.pricesAttributes().isEmpty()) {
            if (room.pricesAttributes().get(0).price() != null)
                roomPriceEditText.setText(String.valueOf(room.pricesAttributes().get(0).price()));

            currencyText.setText(PriceUtils.getPriceSymbol(context(), room.pricesAttributes().get(0).currency()));

            Boolean billsIncluded = room.pricesAttributes().get(0).billsIncluded();
            if (billsIncluded != null) {
                billsIncludedCheckbox.setChecked(billsIncluded);
                billsNotIncludedCheckbox.setChecked(!billsIncluded);
            }
        }
        fromDateCheckBox.setText(DateUtil.getDefaultDateFormatWithLocale().format(room.availableFrom()));
        fromDateCheckBox.setChecked(true);
        if (room.availableTo() != null) {
            toDateCheckBox.setText(DateUtil.getDefaultDateFormatWithLocale().format(room.availableTo()));
            toDateCheckBox.setChecked(true);
        } else
            toNoDateCheckBox.setChecked(true);

        Integer minDays = room.minDays();
        if (minDays != null) {
            if (minDays > 0 && minDays < 31) {
                oneMonthMinimumStayCheckbox.setChecked(true);
            } else if (minDays > 30 && minDays < 61) {
                twoMonthsMinimumStayCheckbox.setChecked(true);
            } else if (minDays > 60 && minDays < 91) {
                threeMonthsMinimumStayCheckbox.setChecked(true);
            } else if (minDays > 90 && minDays < 181) {
                sixMonthsMinimumStayCheckbox.setChecked(true);
            } else if (minDays > 180 && minDays < 361) {
                twelveMonthsMinimumStayCheckbox.setChecked(true);
            }
        }
        numberFemaleTenantsText.setText(String.valueOf(room.nonBadiTenants().femaleTenants()));
        numberMaleTenantsText.setText(String.valueOf(room.nonBadiTenants().maleTenants()));
        tenantsLayout.setVisibility(room.tenants().isEmpty() ? View.GONE : View.VISIBLE);
        if (!room.tenants().isEmpty())
            setupTenantsAdapter();
        switch (room.bedType()) {
            case 1:
                sofaBedCheckBox.setChecked(true);
                break;
            case 2:
                singleBedCheckBox.setChecked(true);
                break;
            case 3:
                doubleBedCheckBox.setChecked(true);
                break;
            case 4:
                unfurnishedCheckBox.setChecked(true);
                break;
            default:
                break;
        }
        if (!room.amenitiesAttributes().isEmpty()) {
            fakeAmenitiesLayout.setVisibility(View.GONE);
            amenitiesRecycleView.setVisibility(View.VISIBLE);
            setupAmenitiesAdapter();
        }
        roomDescriptionEditText.setText(room.description());
        int roomTitleCharactersLeft = MAX_CHARACTERS_ROOM_TITLE - (room.title() == null ? 0 : room.title().length());
        int roomDescriptionCharactersLeft = MAX_CHARACTERS_DESCRIPTION -
                (room.description() == null ? 0 : room.description().length());
        charactersTitleRoomText.setText(getString(R.string.edit_profile_characters_left, roomTitleCharactersLeft));
        charactersDescriptionText.setText(getString(R.string.edit_profile_characters_left, roomDescriptionCharactersLeft));
    }

    private void setupCalendar() {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private void setupTenantsAdapter() {
        listRoomTenantsAdapter = new ListRoomTenantsAdapter(context());
        listRoomTenantsAdapter.setTenants(room.tenants());
        tenantsRecyclerView.setAdapter(listRoomTenantsAdapter);
        tenantsRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupAmenitiesAdapter() {
        listRoomAmenitiesAdapter = new ListRoomAmenitiesAdapter();
        listRoomAmenitiesAdapter.setAmenities(room.amenitiesAttributes());
        amenitiesRecycleView.setAdapter(listRoomAmenitiesAdapter);
        amenitiesRecycleView.setNestedScrollingEnabled(false);
    }

    private void setupPicturesAdapter() {
        listRoomPicturesAdapter = new ListRoomPicturesAdapter(context());
        listRoomPicturesAdapter.setPictures(room.pictures());
        listRoomPicturesAdapter.setOnPictureListener(onPictureListener);
        picturesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        picturesRecyclerView.setAdapter(listRoomPicturesAdapter);
        picturesRecyclerView.setNestedScrollingEnabled(false);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(listRoomPicturesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(picturesRecyclerView);
    }

    private ListRoomPicturesAdapter.OnPictureListener onPictureListener = new ListRoomPicturesAdapter.OnPictureListener() {
        @Override
        public void onUserItemClicked(Integer position) {
            if (!isLoadingImage) {
                imagePosition = position;
                askForWriteStoragePermission();
            } else {
                Snackbar.make(listRoomActivityLayout, R.string.list_room_message_wait_for_upload, Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUserItemDeleteClicked(Integer position) {
            if (isLoadingImage) {
                Snackbar.make(listRoomActivityLayout, R.string.list_room_message_wait_for_upload, Snackbar.LENGTH_LONG).show();
                return;
            }
            
            switch (position) {
                case 0:
                    hideImageCover();
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_room).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);
                    break;
                case 1:
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_kitchen).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);
                    break;
                case 2:
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_living_room).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);
                    break;
                case 3:
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_bath).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);
                    break;
                case 4:
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_window).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);
                    break;
                case 5:
                    replacePicture(Picture.builder().setId(R.drawable.ic_picture_exterior).setUrl(URL_DEFAULT_IMAGE).build(),
                            position);

                    if (room.pictures().size() > position + 1) {
                        Picture pictureToRemove = room.pictures().get(position + 1);
                        // Do not remove a valid picture!
                        if (pictureToRemove.id() == R.drawable.ic_picture_camera && pictureToRemove.url().equals
                                (URL_DEFAULT_IMAGE)) {
                            removePicture(position + 1);
                        }
                    }
                    break;
                default:
                    removePicture(position);
                    if (room.pictures().size() > position + 1) {
                        Picture pictureToRemove = room.pictures().get(position + 1);
                        // Do not remove a valid picture!
                        if (pictureToRemove.id() == R.drawable.ic_picture_camera && pictureToRemove.url().equals
                                (URL_DEFAULT_IMAGE)) {
                            removePicture(position + 1);
                        }
                    }
                    break;
            }

        }

        @Override
        public void onUserItemMovedRefresh() {
            showImageCover(room.pictures().get(0));
        }
    };

    private void addPicture(Picture picture) {
        room.pictures().add(picture);
        listRoomPicturesAdapter.notifyDataSetChanged();
    }

    private void removePicture(int position) {
        room.pictures().remove(position);
        listRoomPicturesAdapter.notifyDataSetChanged();
    }

    private void replacePicture(Picture picture, int position) {
        room.pictures().remove(position);
        room.pictures().add(position, picture);
        listRoomPicturesAdapter.notifyDataSetChanged();
    }

    private void showImageCover(Picture picture) {
        if (!picture.url().equals(URL_DEFAULT_IMAGE)) {
            coverPlaceholderText.setVisibility(View.GONE);
            coverImageView.setVisibility(View.VISIBLE);
            GlideApp.with(this)
                    .load(picture.width500Url())
                    .transition(withCrossFade())
                    .centerCrop()
                    .into(coverImageView);
        } else {
            coverPlaceholderText.setVisibility(View.VISIBLE);
            coverImageView.setVisibility(View.GONE);
        }
    }

    private void hideImageCover() {
        coverPlaceholderText.setVisibility(View.VISIBLE);
        coverImageView.setVisibility(View.GONE);
    }

    private void removePlaceHolderImagesBeforeSave() {
        List<Picture> picturesToRemove = new ArrayList<>();
        for (Picture picture : room.pictures()) {
            if (picture.url().equals(URL_DEFAULT_IMAGE))
                picturesToRemove.add(picture);
        }
        room.pictures().removeAll(picturesToRemove);
    }

    private void setupChecks() {
        roomTitleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (room != null)
                    room = room.withTitle(text.toString());
                charactersTitleRoomText.setText(
                        getString(R.string.edit_profile_characters_left, MAX_CHARACTERS_ROOM_TITLE - text.length()));
                if (!text.toString().isEmpty()) {
                    roomTitleLayout.setError(null);
                    roomTitleLayout.setErrorEnabled(false);
                }
            }
        });
        roomDescriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (room != null)
                    room = room.withDescription(text.toString());
                charactersDescriptionText.setText(
                        getString(R.string.edit_profile_characters_left, MAX_CHARACTERS_DESCRIPTION - text.length()));
            }
        });

        //Add text watcher to control the input of the user.
        roomPriceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable text) {
                if (!text.toString().isEmpty()) {
                    currencyText.setTextColor(ContextCompat.getColor(context(), R.color.green_500));
                    managePrice(Integer.parseInt(text.toString()));
                } else {
                    roomPriceEditText.setError(null);
                    currencyText.setTextColor(ContextCompat.getColor(context(), R.color.grey_300));
                    managePrice(null);
                }
            }
        });
    }

    private void managePrice(Integer price) {
        if (room.pricesAttributes().isEmpty()) {
            room.pricesAttributes().add(
                    PricesAttribute.builder()
                            .setPrice(price)
                            .build());
        } else {
            PricesAttribute pricesAttributeEdited
                    = room.pricesAttributes().get(0).withPrice(price);
            room.pricesAttributes().clear();
            room.pricesAttributes().add(pricesAttributeEdited);
        }
        room = room.withPricesAttributes(room.pricesAttributes());
    }

    public void askForWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteExternalStoragePermission();
        } else {
            Timber.i("Write storage permission has already been granted.");
            EasyImage.configuration(this)
                    .setImagesFolderName("Badi Images")
                    .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                    .setCopyPickedImagesToPublicGalleryAppFolder(true)
                    .setAllowMultiplePickInGallery(false);

            EasyImage.openChooserWithGallery(this, getString(R.string.image_choose_input_method), 0);
        }
    }

    /**
     * Requests the write external storage permission.
     * If the permission has been denied previously, a SnackBar will prompt the user to grant the
     * permission, otherwise it is requested directly.
     */
    private void requestWriteExternalStoragePermission() {
        Timber.i("WRITE EXTERNAL STORAGE permission has NOT been granted. Requesting permission.");

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            Timber.i("Displaying storage permission rationale to provide additional context.");
            Snackbar.make(listRoomActivityLayout, R.string.permission_write_external_storage_rationale,
                    Snackbar.LENGTH_LONG)
                    .setAction(R.string.permission_ok, view -> ActivityCompat.requestPermissions(
                            ListRoomActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            10))
                    .show();
        } else {
            // Storage permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    10);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @OnClick(R.id.button_navigate_to_add_location)
    void onClickLocationText() {
        navigator.navigateToListRoomPlace(this, roomLocationEditText.getText().toString());
    }

    @OnClick(R.id.checkbox_bills_included)
    void onClickBillsIncludedCheckbox(CheckBox checkBox) {
        billsNotIncludedCheckbox.setChecked(!checkBox.isChecked());
        manageBillsIncluded();
    }

    @OnClick(R.id.checkbox_bills_not_included)
    void onClickBillsNotIncludedCheckbox(CheckBox checkBox) {
        billsIncludedCheckbox.setChecked(!checkBox.isChecked());
        manageBillsIncluded();
    }

    private void manageBillsIncluded() {
        if (room.pricesAttributes().isEmpty()) {
            room.pricesAttributes().add(
                    PricesAttribute.builder()
                            .setBillsIncluded(billsIncludedCheckbox.isChecked())
                            .build());
        } else {
            PricesAttribute pricesAttributeEdited
                    = room.pricesAttributes().get(0).withBillsIncluded(billsIncludedCheckbox.isChecked());
            room.pricesAttributes().clear();
            room.pricesAttributes().add(pricesAttributeEdited);
        }
        room = room.withPricesAttributes(room.pricesAttributes());
    }

    @OnClick(R.id.checkbox_from_right_now)
    void onClickAvailableFromNow() {
        fromNowCheckBox.setChecked(true);
        fromDateCheckBox.setChecked(false);
        fromDateCheckBox.setText(getString(R.string.list_room_choose_date));
        room = room.withAvailableFrom(new Date());
    }

    @OnClick(R.id.checkbox_from_date)
    void onClickAvailableFromDate() {
        fromDateCheckBox.setChecked(false);
        fromDateCheckBox.setText(getString(R.string.list_room_choose_date));
        DatePickerDialog dateDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            room = room.withAvailableFrom(calendar.getTime());
            fromNowCheckBox.setChecked(false);
            fromDateCheckBox.setChecked(true);
            fromDateCheckBox.setText(DateUtil.getDefaultDateFormatWithLocale().format(room.availableFrom()));
            if (room.availableTo() != null && room.availableFrom().after(room.availableTo())) {
                onClickAvailableToNoDate();
            }
        }, year, month, day);
        // Subtract one second to allow set current day.
        dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dateDialog.setTitle(null);
        dateDialog.show();
    }

    @OnClick(R.id.checkbox_to_no_date)
    void onClickAvailableToNoDate() {
        toNoDateCheckBox.setChecked(true);
        toDateCheckBox.setChecked(false);
        toDateCheckBox.setText(getString(R.string.list_room_choose_date));
        room = room.withAvailableTo(null);
    }

    @OnClick(R.id.checkbox_to_date)
    void onClickAvailableToDate() {
        toDateCheckBox.setChecked(false);
        toDateCheckBox.setText(getString(R.string.list_room_choose_date));
        DatePickerDialog dateDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            room = room.withAvailableTo(calendar.getTime());
            toNoDateCheckBox.setChecked(false);
            toDateCheckBox.setChecked(true);
            toDateCheckBox.setText(DateUtil.getDefaultDateFormatWithLocale().format(room.availableTo()));
        }, year, month, day);
        // Add at least one day to the current available from date if available
        if (room.availableFrom() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(room.availableFrom());
            c.add(Calendar.DATE, 1);
            Date datePlusDay = c.getTime();
            dateDialog.getDatePicker().setMinDate(datePlusDay.getTime());
            dateDialog.setTitle(null);
        } else {
            dateDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dateDialog.setTitle(null);
        }
        dateDialog.show();
    }

    @OnClick(R.id.checkbox_minimum_stay_one_month)
    void onClickMinimumStayOneMonth(CheckBox checkBox) {
        if (checkBox.isChecked())
            room = room.withMinDays(30);
        else
            room = room.withMinDays(0);
        twoMonthsMinimumStayCheckbox.setChecked(false);
        threeMonthsMinimumStayCheckbox.setChecked(false);
        sixMonthsMinimumStayCheckbox.setChecked(false);
        twelveMonthsMinimumStayCheckbox.setChecked(false);
    }

    @OnClick(R.id.checkbox_minimum_stay_two_months)
    void onClickMinimumStayTwoMonths(CheckBox checkBox) {
        if (checkBox.isChecked())
            room = room.withMinDays(30 * 2);
        else
            room = room.withMinDays(0);
        oneMonthMinimumStayCheckbox.setChecked(false);
        threeMonthsMinimumStayCheckbox.setChecked(false);
        sixMonthsMinimumStayCheckbox.setChecked(false);
        twelveMonthsMinimumStayCheckbox.setChecked(false);
    }

    @OnClick(R.id.checkbox_minimum_stay_three_months)
    void onClickMinimumStayThreeMonths(CheckBox checkBox) {
        if (checkBox.isChecked())
            room = room.withMinDays(30 * 3);
        else
            room = room.withMinDays(0);
        oneMonthMinimumStayCheckbox.setChecked(false);
        twoMonthsMinimumStayCheckbox.setChecked(false);
        sixMonthsMinimumStayCheckbox.setChecked(false);
        twelveMonthsMinimumStayCheckbox.setChecked(false);
    }

    @OnClick(R.id.checkbox_minimum_stay_six_months)
    void onClickMinimumStaySixMonths(CheckBox checkBox) {
        if (checkBox.isChecked())
            room = room.withMinDays(30 * 6);
        else
            room = room.withMinDays(0);
        oneMonthMinimumStayCheckbox.setChecked(false);
        twoMonthsMinimumStayCheckbox.setChecked(false);
        threeMonthsMinimumStayCheckbox.setChecked(false);
        twelveMonthsMinimumStayCheckbox.setChecked(false);
    }

    @OnClick(R.id.checkbox_minimum_stay_twelve_months)
    void onClickMinimumStayTwelveMonths(CheckBox checkBox) {
        if (checkBox.isChecked())
            room = room.withMinDays(30 * 12);
        else
            room = room.withMinDays(0);
        oneMonthMinimumStayCheckbox.setChecked(false);
        twoMonthsMinimumStayCheckbox.setChecked(false);
        threeMonthsMinimumStayCheckbox.setChecked(false);
        sixMonthsMinimumStayCheckbox.setChecked(false);
    }

    @OnClick(R.id.button_remove_female_tenant)
    void onClickRemoveFemaleTenantButton() {
        if (room.nonBadiTenants().femaleTenants() > 0) {
            room = room.withNonBadiTenants(
                    NonBadiTenants.create(0, room.nonBadiTenants().maleTenants(), room.nonBadiTenants().femaleTenants() - 1));
        }
        numberFemaleTenantsText.setText(String.valueOf(room.nonBadiTenants().femaleTenants()));
    }

    @OnClick(R.id.button_add_female_tenant)
    void onClickAddFemaleTenantButton() {
        if (room.nonBadiTenants().femaleTenants() < 9) {
            room = room.withNonBadiTenants(
                    NonBadiTenants.create(0, room.nonBadiTenants().maleTenants(), room.nonBadiTenants().femaleTenants() + 1));
        }
        numberFemaleTenantsText.setText(String.valueOf(room.nonBadiTenants().femaleTenants()));
    }

    @OnClick(R.id.button_remove_male_tenant)
    void onClickRemoveMaleTenantButton() {
        if (room.nonBadiTenants().maleTenants() > 0) {
            room = room.withNonBadiTenants(
                    NonBadiTenants.create(0, room.nonBadiTenants().maleTenants() - 1, room.nonBadiTenants().femaleTenants()));
        }
        numberMaleTenantsText.setText(String.valueOf(room.nonBadiTenants().maleTenants()));
    }

    @OnClick(R.id.button_add_male_tenant)
    void onClickAddMaleTenantButton() {
        if (room.nonBadiTenants().maleTenants() < 9) {
            room = room.withNonBadiTenants(
                    NonBadiTenants.create(0, room.nonBadiTenants().maleTenants() + 1, room.nonBadiTenants().femaleTenants()));
        }
        numberMaleTenantsText.setText(String.valueOf(room.nonBadiTenants().maleTenants()));
    }

    @OnClick(R.id.text_edit_tenants)
    void onClickEditTenantsText() {
        navigator.navigateToListRoomRoommates(ListRoomActivity.this, new ArrayList<>(room.tenants()));
    }

    @OnClick(R.id.check_box_single_bed)
    void onClickSingleBedCheckbox() {
        room = room.withBedType(2);
        singleBedCheckBox.setChecked(true);
        doubleBedCheckBox.setChecked(false);
        sofaBedCheckBox.setChecked(false);
        unfurnishedCheckBox.setChecked(false);
    }

    @OnClick(R.id.check_box_double_bed)
    void onClickDoubleBedCheckbox() {
        room = room.withBedType(3);
        singleBedCheckBox.setChecked(false);
        doubleBedCheckBox.setChecked(true);
        sofaBedCheckBox.setChecked(false);
        unfurnishedCheckBox.setChecked(false);
    }

    @OnClick(R.id.check_box_sofa_bed)
    void onClickSofaBedCheckbox() {
        room = room.withBedType(1);
        singleBedCheckBox.setChecked(false);
        doubleBedCheckBox.setChecked(false);
        sofaBedCheckBox.setChecked(true);
        unfurnishedCheckBox.setChecked(false);
    }

    @OnClick(R.id.check_box_unfurnished)
    void onClickUnfurnishedCheckbox() {
        room = room.withBedType(4);
        singleBedCheckBox.setChecked(false);
        doubleBedCheckBox.setChecked(false);
        sofaBedCheckBox.setChecked(false);
        unfurnishedCheckBox.setChecked(true);
    }

    @OnClick(R.id.layout_fake_amenities)
    void onClickFakeAmenitiesLayout() {
        navigator.navigateToListRoomAmenities(this, new ArrayList<>(room.amenitiesAttributes()));
    }

    @OnClick(R.id.text_see_all_amenities)
    void onClickSeeAllAmenitiesText() {
        navigator.navigateToListRoomAmenities(this, new ArrayList<>(room.amenitiesAttributes()));
    }

    @OnClick(R.id.button_list_room)
    void onClickListRoomButton() {
        attemptCreateRoom();
    }

    /**
     * Attempts to create room by the provided data in the form.
     * If there are form errors (invalid values, missing fields, etc.), the
     * errors are presented and no actual create room attempt is made.
     */
    private void attemptCreateRoom() {

        //Clear focus
        getWindow().getDecorView().clearFocus();

        // Store pictures
        List<Integer> picturesIDs = new ArrayList<>();
        for (Picture picture : room.pictures()) {
            if (!picture.url().equals(URL_DEFAULT_IMAGE))
                picturesIDs.add(picture.id());
        }

        // Reset errors & return spacing to original size (reset error spacing).
        roomTitleLayout.setError(null);
        roomTitleLayout.setErrorEnabled(false);
        roomLocationLayout.setError(null);
        roomLocationLayout.setErrorEnabled(false);
        roomPriceEditText.setError(null);
        roomDescriptionEditText.setError(null);

        // Store values at the time of the create room attempt.
        String roomTitle = roomTitleEditText.getText().toString();
        String roomLocation = roomLocationEditText.getText().toString();
        String roomPrice = roomPriceEditText.getText().toString();
        String roomDescription = roomDescriptionEditText.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for at least a valid picture uploaded
        if (picturesIDs.isEmpty()) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_no_pictures, Snackbar.LENGTH_LONG).show();
            focusView = picturesLayout;
            cancel = true;
        }

        // Check if a picture is being uploaded
        if (isLoadingImage) {
            Snackbar.make(listRoomActivityLayout, R.string.list_room_message_wait_for_upload, Snackbar.LENGTH_LONG).show();
            focusView = picturesLayout;
            cancel = true;
        }

        // Check for a valid room title.
        if (TextUtils.isEmpty(roomTitle) && !cancel) {
            roomTitleLayout.setError(getString(R.string.error_field_required));
            focusView = aboutTheRoomLayout;
            cancel = true;
        }

        // Check for a valid room location, if the user entered one.
        if (TextUtils.isEmpty(roomLocation) && !cancel) {
            roomLocationLayout.setError(getString(R.string.error_field_required));
            focusView = aboutTheRoomLayout;
            cancel = true;
        }

        // Check for a valid room location, if the user entered one.
        if (TextUtils.isEmpty(roomPrice) && !cancel) {
            roomPriceEditText.setError(getString(R.string.error_field_required));
            focusView = aboutTheRoomLayout;
            cancel = true;
        }

        if (TextUtils.isEmpty(roomDescription) && !cancel) {
            roomDescriptionEditText.setError(getString(R.string.error_field_required));
            focusView = aboutTheRoomLayout;
            cancel = true;
        }

        // Check for a valid bills included state.
        if (!billsIncludedCheckbox.isChecked() && !billsNotIncludedCheckbox.isChecked() && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_bills_included, Snackbar.LENGTH_LONG).show();
            focusView = aboutTheRoomLayout;
            cancel = true;
        }

        // Check for a valid from date
        if (!fromNowCheckBox.isChecked() && !fromDateCheckBox.isChecked() && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_availability_from, Snackbar.LENGTH_LONG).show();
            focusView = availabilityLayout;
            cancel = true;
        }

        // Check for a valid to date
        if (!toNoDateCheckBox.isChecked() && !toDateCheckBox.isChecked() && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_availability_to, Snackbar.LENGTH_LONG).show();
            focusView = availabilityLayout;
            cancel = true;
        }

        // Check for a valid number of tenants
        if (room.nonBadiTenants().maleTenants() == 0 && room.nonBadiTenants().femaleTenants() == 0 && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_no_tenants, Snackbar.LENGTH_LONG).show();
            focusView = whoLivesThereLayout;
            cancel = true;
        }

        // Check for a valid bed type
        if (!singleBedCheckBox.isChecked() && !doubleBedCheckBox.isChecked() && !sofaBedCheckBox.isChecked() &&
                !unfurnishedCheckBox.isChecked() && room.bedType() == 0 && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_no_type_bed_selected, Snackbar.LENGTH_LONG).show();
            focusView = typeOfBedLayout;
            cancel = true;
        }

        if (room.amenitiesAttributes().isEmpty() && !cancel) {
            Snackbar.make(listRoomActivityLayout, R.string.error_list_room_no_amenities, Snackbar.LENGTH_LONG).show();
            focusView = amenitiesLayout;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt create room and focus the first
            // form field with an error.
            ViewUtil.focusOnView(focusView);
            sendListRoomFailureEvent();
        } else {
            // Disable click list room button to avoid multiple uploads
            listRoomButton.setClickable(false);
            // Show a progress spinner, and kick off a background task to
            // perform the create room attempt and hide the keyboard.
            ViewUtil.hideKeyboard(this);
            if (isEditMode) {
                listRoomPresenter.editRoom(room);
            } else {
                listRoomPresenter.uploadRoom(room);
            }

        }
    }

    private void showAddRoommatesDialog() {

    }

    private void showShareListingDialog() {

    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void populateRoomInitialState() {
        this.room = RoomDetail.initRoomDetail();
        populateListRoom();
    }

    @Override
    public void populateRoomSavedInPrefs(RoomDetail room) {
        this.room = room;
        populateListRoom();
    }

    @Override
    public void roomClearSuccessful() {
        Timber.i("Room successfully cleared from preferences and changed user as lister with user mode lister");
    }

    @Override
    public void roomSaveSuccessful() {
        supportFinishAfterTransition();
    }

    @Override
    public void showLoadingImage() {
        isLoadingImage = true;
        loadingImageProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingImage() {
        isLoadingImage = false;
        loadingImageProgress.setVisibility(View.GONE);
    }

    @Override
    public void showImageLoaded(Picture picture) {
        replacePicture(picture, imagePosition);
        if (imagePosition == 0) {
            showImageCover(picture);
        }
        if (imagePosition > 4) {
            addPicture(Picture.builder().setId(R.drawable.ic_picture_camera).setUrl(URL_DEFAULT_IMAGE).build());
        }
    }

    @Override
    public void roomUploadSuccessful(RoomDetail room) {
        this.room = room;
        if (!isEditMode) listRoomPresenter.clearRoomSavedInPrefs();
        showAddRoommatesDialog();
    }

    @Override
    public void roomEditSuccessful(RoomDetail room) {
        this.room = room;
        if (isEditMode) {
            // Re enable list room button
            listRoomButton.setClickable(true);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(LIST_ROOM_EXTRA_ROOM, room);
            setResult(Activity.RESULT_OK, resultIntent);
            supportFinishAfterTransition();
        } else {
            showShareListingDialog();
        }
    }

    @Override
    public void sendListRoomSuccessEvent() {

    }

    @Override
    public void sendListRoomFailureEvent() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        // Re enable list room button
        listRoomButton.setClickable(true);
        DialogFactory.createSimpleOkErrorDialog(context(), getString(R.string.error_warning), message).show();
    }

    @Override
    public Context context() {
        return this;
    }
}
