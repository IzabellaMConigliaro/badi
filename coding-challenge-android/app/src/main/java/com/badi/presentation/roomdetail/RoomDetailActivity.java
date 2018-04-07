/*
 * File: RoomDetailActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.di.HasComponent;
import com.badi.common.di.components.DaggerRoomComponent;
import com.badi.common.di.components.RoomComponent;
import com.badi.common.di.modules.RoomModule;
import com.badi.common.utils.DateUtil;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.MapUtils;
import com.badi.common.utils.PriceUtils;
import com.badi.common.utils.RoomUtils;
import com.badi.common.utils.UserUtils;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.data.entity.room.RoomBase;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.user.Picture;
import com.badi.data.entity.user.UserBase;
import com.badi.presentation.base.BaseActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.TypefaceUtils;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class RoomDetailActivity extends BaseActivity implements HasComponent<RoomComponent>, RoomDetailContract.View,
        OnMapReadyCallback {

    public static final String EXTRA_BASE_ROOM = "RoomDetailActivity.EXTRA_BASE_ROOM";
    public static final String EXTRA_ROOM_ID = "RoomDetailActivity.EXTRA_ROOM_ID";
    public static final String EXTRA_INVITATION_ID = "RoomDetailActivity.EXTRA_INVITATION_ID";

    @Inject RoomDetailPresenter roomDetailPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.images_room_viewpager) ViewPager imagesRoomViewPager;
    @BindView(R.id.images_room_indicator) CircleIndicator imagesRoomIndicator;
    @BindView(R.id.text_name_tenant) TextView nameTenantText;
    @BindView(R.id.text_age_tenant) TextView ageTenantText;
    @BindView(R.id.text_city_tenant) TextView cityTenantText;
    @BindView(R.id.image_thumbnail_tenant) ImageView tenantImage;
    @BindView(R.id.border_thumbnail_tenant) RelativeLayout borderTenantImage;
    @BindView(R.id.image_verified_profile) ImageView verifiedProfileImage;
    @BindView(R.id.text_title_room) TextView titleRoomText;
    @BindView(R.id.text_published_room) TextView publishedRoomText;
    @BindView(R.id.view_dot) TextView dotText;
    @BindView(R.id.text_roommates_number) TextView roommatesNumberText;
    @BindView(R.id.image_gender_tenant) ImageView genderTenantImage;
    @BindView(R.id.image_occupation_tenant) ImageView occupationTenantImage;
    @BindView(R.id.text_price_room) TextView priceRoomText;
    @BindView(R.id.text_description_room) TextView descriptionRoomText;
    @BindView(R.id.recycler_view_amenities) RecyclerView amenitiesRecycleView;
    @BindView(R.id.text_num_female_tenants) TextView numFemaleTenantsText;
    @BindView(R.id.text_num_male_tenants) TextView numMaleTenantsText;
    @BindView(R.id.text_num_undefined_tenants) TextView numUndefinedTenantsText;
    @BindView(R.id.text_availability_room_from) TextView availableFromText;
    @BindView(R.id.text_availability_room_to_title) TextView availableToTitleText;
    @BindView(R.id.text_availability_room_to) TextView availableToText;
    @BindView(R.id.layout_min_stay) LinearLayout minStayLayout;
    @BindView(R.id.text_min_stay_room) TextView minStayRoomText;
    @BindView(R.id.text_location_room) TextView locationRoomText;
    @BindView(R.id.recycler_view_tenants) RecyclerView tenantsRecycleView;
    @BindView(R.id.button_send_request_room_detail) Button sendRequestRoomDetailButton;
    @BindView(R.id.layout_buttons_from_invitations) LinearLayout layoutButtonsFromInvitations;
    @BindView(R.id.status_container) RelativeLayout statusContainerRelativeLayout;

    private AmenitiesAdapter amenitiesAdapter;
    private PicturesAdapter picturesAdapter;

    private RoomDetail roomDetail;
    private LatLng roomCoordinates;
    private RoomComponent roomComponent;

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, RoomBase room) {
        Intent intent = new Intent(context, RoomDetailActivity.class);
        intent.putExtra(EXTRA_BASE_ROOM, room);
        return intent;
    }

    public static Intent getCallingIntentFromInvitations(Context context, RoomBase room, Integer invitationID) {
        Intent intent = new Intent(context, RoomDetailActivity.class);
        intent.putExtra(EXTRA_BASE_ROOM, room);
        intent.putExtra(EXTRA_INVITATION_ID, invitationID);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_detail);
        this.initializeInjector();
        this.getComponent().inject(this);
        ButterKnife.bind(this);
        roomDetailPresenter.attachView(this);
        initComponents();
        handleIntent();
    }

    private void handleIntent() {
        // ATTENTION: This was auto-generated to handle app links.
        Intent currentIntent = getIntent();
        String appLinkAction = currentIntent.getAction();
        Uri appLinkData = currentIntent.getData();
        if (appLinkData != null) {
            String roomID = appLinkData.getLastPathSegment();
            roomDetailPresenter.getRoomDetailFromID(Integer.parseInt(roomID));
        } else {
            RoomBase roomBase = getIntent().getParcelableExtra(EXTRA_BASE_ROOM);
            roomDetailPresenter.getRoomDetailFromID(roomBase.id());
        }
    }

    @Override
    protected void onDestroy() {
        roomDetailPresenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        roomDetailPresenter.getValidationStatus();
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

    private void initComponents() {
        picturesAdapter = new PicturesAdapter(context());
        picturesAdapter.registerDataSetObserver(imagesRoomIndicator.getDataSetObserver());
        toolbar.setNavigationOnClickListener(view -> supportFinishAfterTransition());
        toolbar.inflateMenu(R.menu.room_detail);
    }

    private void setupToolbar() {
        toolbar.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.action_share) {
                if (roomDetail.owned())
                    RoomUtils.shareMyRoom(RoomDetailActivity.this, roomDetail.id());
                else
                    RoomUtils.shareRoom(RoomDetailActivity.this, roomDetail.id(), roomDetail.city());
                return true;
            } else if (id == R.id.action_report_room) {
                UserUtils.reportRoomEmailIntent(this, roomDetail.id());
            }
            return false;
        });
    }

    private void setupCollapsingToolbar() {
        collapsingToolbar.setTitle(PriceUtils.getPriceRoom(this, roomDetail.pricesAttributes()));
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbar.setCollapsedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
        collapsingToolbar.setExpandedTitleTypeface(
                TypefaceUtils.load(getResources().getAssets(), ViewUtil.FONT_PATH_NUNITO_REGULAR));
    }

    private void setupMapFragment() {
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_room_location);
        mapFragment.getMapAsync(this);
    }

    private void setupRoomImagesPager(List<Picture> pictures) {
        picturesAdapter.setPictures(pictures);
        picturesAdapter.setOnPictureListener(onPictureListener);
        imagesRoomViewPager.setAdapter(picturesAdapter);
        imagesRoomIndicator.setViewPager(imagesRoomViewPager);
    }

    private PicturesAdapter.OnPictureListener onPictureListener = new PicturesAdapter.OnPictureListener() {
        @Override
        public void onUserItemClicked(View roomImage, Integer roomID) {
            ArrayList<String> images = new ArrayList<>();
            int currentImageIndex = 0;
            for (Picture picture : roomDetail.pictures()) {
                images.add(picture.width1080Url());
                if (picture.id().equals(roomID)) {
                    currentImageIndex = roomDetail.pictures().indexOf(picture);
                }
            }
            navigator.navigateToGallery(RoomDetailActivity.this, images, currentImageIndex, roomImage);
        }
    };

    private void setupAmenitiesAdapter() {
        //Add fake amenity bed type to amenities attributes list.
        roomDetail.amenitiesAttributes().add(0, AmenitiesAttribute.create(0));
        amenitiesAdapter = new AmenitiesAdapter();
        amenitiesAdapter.setAmenities(roomDetail.amenitiesAttributes(), roomDetail.bedType());
        amenitiesRecycleView.setAdapter(amenitiesAdapter);
        amenitiesRecycleView.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.border_thumbnail_tenant)
    public void onImageTenantClick(View transitionView) {
        if (roomDetail != null) {
            UserBase user = UserBase.create(roomDetail.tenants().get(0).id(),
                    roomDetail.tenants().get(0).firstName(),
                    roomDetail.tenants().get(0).lastName(),
                    roomDetail.tenants().get(0).birthDate(),
                    roomDetail.tenants().get(0).pictures());
        }
    }

    @OnClick(R.id.text_see_all_amenities)
    public void onSeeAllAmenitiesClick() {
        if (roomDetail != null)
            navigator.navigateToAmenities(this, new ArrayList<>(roomDetail.amenitiesAttributes()), roomDetail.bedType());
    }

    @OnClick(R.id.button_send_request_room_detail)
    public void onRequestRoomDetailClick() {
        sendRequestRoomDetailButton.setClickable(false);
        roomDetailPresenter.requestRoom(roomDetail.id());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        roomCoordinates = new LatLng(roomDetail.latitude(), roomDetail.longitude());

        googleMap.setOnMapClickListener(latLng ->
                navigator.navigateToMap(RoomDetailActivity.this, roomDetail.displayAddress(), roomCoordinates, true));
        googleMap.addCircle(MapUtils.createCirclePositionRoom(this, roomCoordinates));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(roomCoordinates, 16));
    }

    private void setResultActivity() {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_ROOM_ID, roomDetail.id().intValue());
        setResult(Activity.RESULT_OK, resultIntent);
    }

    private void changeRequestButtonState() {
        sendRequestRoomDetailButton.setBackgroundResource(R.drawable.button_background_round_grey_dark);
        sendRequestRoomDetailButton.setText(getString(R.string.button_room_requested));
        sendRequestRoomDetailButton.setClickable(false);
    }

    //Buttons Listeners
    @OnClick(R.id.button_accept)
    public void onAcceptRequestClick() {
        roomDetailPresenter.acceptInvitation(getIntent().getIntExtra(EXTRA_INVITATION_ID, 0));
    }

    @OnClick(R.id.button_reject)
    public void onRejectRequestClick() {
        roomDetailPresenter.rejectInvitation(getIntent().getIntExtra(EXTRA_INVITATION_ID, 0));
    }

    private void displayRoomStatus(Integer status) {
        switch (status) {
            case RoomDetail.STATUS_RENTED:
                displayUnavailableRoomLayout();
                break;
            case RoomDetail.STATUS_UNPUBLISHED:
                displayUnavailableRoomLayout();
                break;
            case RoomDetail.STATUS_BOOKED:
                displayBookedRoomLayout();
                break;
        }
    }

    private void displayUnavailableRoomLayout() {
        sendRequestRoomDetailButton.setVisibility(View.GONE);
        publishedRoomText.setText(getString(R.string.text_label_room_not_available));
        dotText.setVisibility(View.GONE);
        roommatesNumberText.setVisibility(View.GONE);
        statusContainerRelativeLayout.setBackgroundColor(ContextCompat.getColor(context(), R.color.red_300));
    }

    private void displayBookedRoomLayout() {
        sendRequestRoomDetailButton.setVisibility(View.GONE);
        publishedRoomText.setText(getString(R.string.my_room_detail_booked));
        dotText.setVisibility(View.GONE);
        roommatesNumberText.setVisibility(View.GONE);
        statusContainerRelativeLayout.setBackgroundColor(ContextCompat.getColor(context(), R.color.blue_500));
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showRoomDetails(RoomDetail roomDetails) {
        roomDetail = roomDetails;
        setupToolbar();
        setupCollapsingToolbar();
        setupMapFragment();
        setupRoomImagesPager(roomDetail.pictures());

        Tenant mainTenant = roomDetail.tenants().get(0);
        String fullNameTenant = mainTenant.firstName() + " " + mainTenant.lastName();
        nameTenantText.setText(fullNameTenant);
        titleRoomText.setText(roomDetail.title());
        String published =
                getString(R.string.published) + " " + DateUtil.getRoomDateRepresentation(this, roomDetail.publishedAt());
        publishedRoomText.setText(published);
        priceRoomText.setText(PriceUtils.getPriceRoom(this, roomDetail.pricesAttributes()));

        genderTenantImage.setVisibility(mainTenant.biologicalSex() != null ? View.VISIBLE : View.GONE);
        occupationTenantImage.setVisibility(mainTenant.occupation() != null ? View.VISIBLE : View.GONE);

        if (mainTenant.biologicalSex() != null)
            //noinspection ConstantConditions
            genderTenantImage.setImageDrawable(mainTenant.biologicalSex() == 1 ?
                    ContextCompat.getDrawable(this, R.drawable.ic_profile_female) :
                    ContextCompat.getDrawable(this, R.drawable.ic_profile_male));

        if (mainTenant.occupation() != null) {
            //noinspection ConstantConditions
            switch (mainTenant.occupation()) {
                case 1:
                    occupationTenantImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile_study));
                    break;
                case 2:
                    occupationTenantImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile_work));
                    break;
                default:
                    occupationTenantImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_profile_work_study));
                    break;
            }
        }

        if (mainTenant.birthDate() != null)
            ageTenantText.setText(
                    String.format(getString(R.string.profile_years_old),
                            DateUtil.getDiffYears(mainTenant.birthDate(), new Date())));
        cityTenantText.setText(mainTenant.cityOfResidence());

        //noinspection ConstantConditions
        descriptionRoomText.setVisibility(roomDetail.description() != null &&
                !roomDetail.description().isEmpty() ? View.VISIBLE : View.GONE);
        descriptionRoomText.setText(roomDetail.description());

        availableFromText.setText(DateUtil.roomIsAvailable(roomDetail.availableFrom())
                ? getString(R.string.room_detail_now)
                : DateUtil.getDateFormatWithLocale().format(roomDetail.availableFrom()));

        if (roomDetail.availableTo() != null) {
            availableToTitleText.setVisibility(View.VISIBLE);
            availableToText.setVisibility(View.VISIBLE);
            availableToText.setText(DateUtil.getDateFormatWithLocale().format(roomDetail.availableTo()));
        } else {
            availableToTitleText.setVisibility(View.GONE);
            availableToText.setVisibility(View.GONE);
        }

        //noinspection ConstantConditions
        if (roomDetail.minDays() != null && roomDetail.minDays() != 0) {
            minStayLayout.setVisibility(View.VISIBLE);
            minStayRoomText.setText(DateUtil.getRoomDaysRepresentation(this, roomDetail.minDays()));
        } else
            minStayLayout.setVisibility(View.GONE);

        verifiedProfileImage.setVisibility(mainTenant.verifiedAccount() ? View.VISIBLE : View.GONE);

        borderTenantImage.setBackground(mainTenant.verifiedAccount() ?
                ContextCompat.getDrawable(this, R.drawable.img_circle_border_green) :
                ContextCompat.getDrawable(this, R.drawable.img_circle_border_white));

        if (!mainTenant.pictures().isEmpty())
            GlideApp.with(this)
                    .load(mainTenant.pictures().get(0).width500Url())
                    .transition(withCrossFade())
                    .circleCrop()
                    .placeholder(R.drawable.ic_placeholder_profile_round)
                    .into(tenantImage);
        else
            tenantImage.setImageResource(R.drawable.ic_placeholder_profile_round);

        sendRequestRoomDetailButton.setVisibility(roomDetail.owned() || getIntent().getIntExtra(EXTRA_INVITATION_ID, 0) != 0
                ? View.GONE : View.VISIBLE);
        if (roomDetail.requested())
            changeRequestButtonState();
        int numberRoommates = roomDetail.nonBadiTenants().femaleTenants()
                + roomDetail.nonBadiTenants().maleTenants()
                + roomDetail.nonBadiTenants().undefinedTenants();
        roommatesNumberText.setText(getResources().getQuantityString(R.plurals.amount_roommates, numberRoommates,
                numberRoommates));
        setupAmenitiesAdapter();
        locationRoomText.setText(roomDetail.displayAddress());

        numFemaleTenantsText.setVisibility(roomDetail.nonBadiTenants().femaleTenants() > 0 ? View.VISIBLE : View.GONE);
        numMaleTenantsText.setVisibility(roomDetail.nonBadiTenants().maleTenants() > 0 ? View.VISIBLE : View.GONE);
        numUndefinedTenantsText.setVisibility(roomDetail.nonBadiTenants().undefinedTenants() > 0 ? View.VISIBLE : View.GONE);

        numFemaleTenantsText.setText(String.valueOf(roomDetail.nonBadiTenants().femaleTenants()));
        numMaleTenantsText.setText(String.valueOf(roomDetail.nonBadiTenants().maleTenants()));
        numUndefinedTenantsText.setText(String.valueOf(roomDetail.nonBadiTenants().undefinedTenants()));

        layoutButtonsFromInvitations.setVisibility(getIntent().getIntExtra(EXTRA_INVITATION_ID, 0) != 0
                ? View.VISIBLE : View.GONE);

        displayRoomStatus(roomDetail.status());
    }

    @Override
    public void changeRequestButton() {
        changeRequestButtonState();
        roomDetail = roomDetail.withRequested(true);
        setResultActivity();
    }

    @Override
    public void setRequestButtonClickable() {
        sendRequestRoomDetailButton.setClickable(true);
    }

    @Override
    public void showBookingTutorial(boolean isLister) {

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
        if (!isFinishing()) {
            DialogFactory.createSimpleOkErrorDialog(context(),
                    getString(R.string.error_warning), message)
                    .show();
        }
    }

    @Override
    public Context context() {
        return this;
    }

}
