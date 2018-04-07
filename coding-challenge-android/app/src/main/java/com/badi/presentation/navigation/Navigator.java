/*
 * File: Navigator.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.view.View;

import com.badi.R;
import com.badi.data.entity.room.AmenitiesAttribute;
import com.badi.data.entity.room.RoomBase;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.search.Filters;
import com.badi.presentation.listroom.ListRoomActivity;
import com.badi.presentation.listroom.ListRoomAmenitiesActivity;
import com.badi.presentation.listroom.ListRoomPlaceActivity;
import com.badi.presentation.listroom.ListRoomRoommatesActivity;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.roomdetail.AmenitiesActivity;
import com.badi.presentation.roomdetail.GalleryActivity;
import com.badi.presentation.roomdetail.MapsActivity;
import com.badi.presentation.roomdetail.RoomDetailActivity;
import com.badi.presentation.search.FiltersActivity;
import com.badi.presentation.search.SearchPlaceActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Class used to navigate through the application.
 */
@Singleton
public class Navigator {

    /**
     * Constant used in the location settings dialog.
     */
    public static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * Constants used for requesting user permissions
     */
    public static final int REQUEST_LOCATION = 1;

    /**
     * Constants used for request for result navigation
     */
    public static final int REQUEST_CODE_EDIT_PROFILE_ACTIVITY = 33;
    public static final int REQUEST_CODE_VERIFY_PHONE_ACTIVITY = 52;
    public static final int REQUEST_CODE_ROOM_DETAIL = 11;
    public static final int REQUEST_CODE_FILTERS_ACTIVITY = 15;
    public static final int REQUEST_CODE_LIST_ROOM = 27;
    public static final int REQUEST_CODE_LIST_ROOM_PLACE_ACTIVITY = 25;
    public static final int REQUEST_CODE_LIST_ROOM_ROOMMATES_ACTIVITY = 45;
    public static final int REQUEST_CODE_LIST_ROOM_AMENITIES_ACTIVITY = 20;
    public static final int REQUEST_CODE_SEARCH_PLACE_ACTIVITY = 26;

    @Inject
    public Navigator() {
        //empty
    }

    /**
     * Goes to the Home screen (MainActivity).
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToHome(Context context) {
        if (context != null) {
            //Set getCallingIntent to true to start background service
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to the Home screen (MainActivity).
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToHomeSwitch(Context context) {
        if (context != null) {
            Intent intentToLaunch = MainActivity.getCallingIntent(context);
            intentToLaunch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to the Search place screen.
     *
     * @param activity      A Context needed to open the destiny activity.
     */
    public void navigateToSearchPlace(Activity activity) {
        if (activity != null) {
            Intent intentToLaunch = SearchPlaceActivity.getCallingIntent(activity);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_SEARCH_PLACE_ACTIVITY);
        }
    }

    /**
     * Goes to the List room screen in listing mode.
     *
     * @param context A Context needed to open the destiny activity.
     */
    public void navigateToListRoom(Context context) {
        if (context != null) {
            Intent intentToLaunch = ListRoomActivity.getCallingIntent(context);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to the List room screen in edit mode.
     *
     * @param activity An activity needed to open the destiny activity.
     * @param room The room to edit
     */
    public void navigateToEditListedRoom(Activity activity, RoomDetail room) {
        if (activity != null) {
            Intent intentToLaunch = ListRoomActivity.getCallingIntentEdit(activity, room);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_LIST_ROOM);
        }
    }

    /**
     * Goes to the List room place screen.
     *
     * @param activity      A Context needed to open the destiny activity.
     * @param address       The already selected address if any
     */
    public void navigateToListRoomPlace(Activity activity, String address) {
        if (activity != null) {
            Intent intentToLaunch = ListRoomPlaceActivity.getCallingIntent(activity, address);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_LIST_ROOM_PLACE_ACTIVITY);
        }
    }

    /**
     * Goes to the List room add roommates screen.
     *
     * @param activity      A Context needed to open the destiny activity.
     * @param tenants       The tenants already in the flat/house
     */
    public void navigateToListRoomRoommates(Activity activity, ArrayList<Tenant> tenants) {
        if (activity != null) {
            Intent intentToLaunch = ListRoomRoommatesActivity.getCallingIntent(activity, tenants);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_LIST_ROOM_ROOMMATES_ACTIVITY);
        }
    }

    /**
     * Goes to the List room amenities screen.
     *
     * @param activity      A Context needed to open the destiny activity.
     * @param amenities     The already applied amenities
     */
    public void navigateToListRoomAmenities(Activity activity, ArrayList<AmenitiesAttribute> amenities) {
        if (activity != null) {
            Intent intentToLaunch = ListRoomAmenitiesActivity.getCallingIntent(activity, amenities);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_LIST_ROOM_AMENITIES_ACTIVITY);
        }
    }

    /**
     * Goes to the Filters screen.
     *
     * @param fragment A Fragment needed to open the destiny activity.
     * @param filters  The already applied filters
     */
    public void navigateToFilters(Fragment fragment, Filters filters) {
        if (fragment != null) {
            Intent intentToLaunch = FiltersActivity.getCallingIntent(fragment.getContext(), filters);
            fragment.startActivityForResult(intentToLaunch, REQUEST_CODE_FILTERS_ACTIVITY);
        }
    }

    /**
     * Goes to the Room detail screen.
     *
     * @param activity An Activity needed to open the destiny activity.
     * @param room     The base information about the room
     * @param roomView The imageView of the room to make the transition from.
     * @param userView The imageView of the main tenant of the room to make the transition from.
     */
    public void navigateToRoomDetail(Activity activity, RoomBase room, View roomView, View userView) {
        if (activity != null) {
            Intent intentToLaunch = RoomDetailActivity.getCallingIntent(activity, room);

            Pair<View, String> roomBackground = Pair.create(roomView, activity.getString(R.string.transition_room_background));
            Pair<View, String> tenantImage = Pair.create(userView, activity.getString(R.string.transition_tenant_image));

            /*@SuppressWarnings("unchecked")
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, roomBackground, tenantImage);
            activity.startActivityForResult(intentToLaunch, RoomDetailActivity.REQUEST_CODE_ROOM_DETAIL, options.toBundle());*/
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_ROOM_DETAIL);
        }
    }

    /**
     * Goes to the Room detail screen from map.
     *
     * @param activity An Activity needed to open the destiny activity.
     * @param room     The base information about the room
     * @param roomView The imageView of the room to make the transition from.
     */
    public void navigateToRoomDetailFromMap(Activity activity, RoomBase room, View roomView) {
        if (activity != null) {
            Intent intentToLaunch = RoomDetailActivity.getCallingIntent(activity, room);

            /*ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, roomView, activity.getString(R.string.transition_room_background));
            activity.startActivityForResult(intentToLaunch, RoomDetailActivity.REQUEST_CODE_ROOM_DETAIL, options.toBundle());*/
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_ROOM_DETAIL);
        }
    }

    /**
     * Goes to the Room detail screen from invitations.
     *
     * @param activity     An Activity needed to open the destiny activity.
     * @param room         The base information about the room
     * @param invitationID The invitationID necessary for accepting and rejecting the invitation.
     */
    public void navigateToRoomDetailFromInvitations(Activity activity, RoomBase room, Integer invitationID) {
        if (activity != null) {
            Intent intentToLaunch = RoomDetailActivity.getCallingIntentFromInvitations(activity, room, invitationID);
            activity.startActivityForResult(intentToLaunch, REQUEST_CODE_ROOM_DETAIL);
        }
    }

    /**
     * Goes to the Gallery room pictures screen.
     *
     * @param activity          An Activity needed to open the destiny activity.
     * @param images            A list of url strings to load images from
     * @param currentImageIndex The current index of the images list visible
     * @param transitionView    The view to make the transition from
     */
    public void navigateToGallery(Activity activity, ArrayList<String> images, int currentImageIndex, View transitionView) {
        if (activity != null) {
            Intent intentToLaunch = GalleryActivity.getCallingIntent(activity, images, currentImageIndex);
            /*ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, transitionView,
                            activity.getString(R.string.transition_room_background));
            activity.startActivity(intentToLaunch, options.toBundle());*/
            activity.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to the Amenities screen.
     *
     * @param context             A Context needed to open the destiny activity.
     * @param amenitiesAttributes An array list of active amenities attributes
     * @param bedType             Current bed type of that particular room
     */
    public void navigateToAmenities(Context context, ArrayList<AmenitiesAttribute> amenitiesAttributes, Integer bedType) {
        if (context != null) {
            Intent intentToLaunch = AmenitiesActivity.getCallingIntent(context, amenitiesAttributes, bedType);
            context.startActivity(intentToLaunch);
        }
    }

    /**
     * Goes to the Room Map screen.
     *
     * @param context     A Context needed to open the destiny activity.
     * @param address     An string with the address of the coordinates to display in the toolbar title.
     * @param coordinates The coordinates to focus the map on.
     */
    public void navigateToMap(Context context, String address, LatLng coordinates, boolean obfuscated) {
        if (context != null) {
            Intent intentToLaunch = MapsActivity.getCallingIntent(context, address, coordinates, obfuscated);
            context.startActivity(intentToLaunch);
        }
    }

}
