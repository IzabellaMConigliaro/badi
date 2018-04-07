/*
 * File: MapFragment.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.badi.R;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.MapUtils;
import com.badi.common.utils.PriceUtils;
import com.badi.common.utils.map.MapStateListener;
import com.badi.common.utils.map.TouchableMapFragment;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomBase;
import com.badi.data.entity.room.RoomMarker;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.presentation.base.BaseFragment;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.navigation.Navigator;
import com.badi.presentation.roomdetail.RoomDetailActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A {@link MapFragment} that shows a map of the searched rooms
 */
public class MapFragment extends BaseFragment implements MapContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener {

    private static final String ARG_COORDINATES = "ARG_COORDINATES";
    private static final String ARG_LOCATION = "ARG_LOCATION";

    @Inject MapPresenter mapPresenter;

    @BindView(R.id.viewpager_rooms_map) ViewPager roomsViewPager;
    @BindView(R.id.view_search_loading) RelativeLayout searchLoadingView;
    @BindView(R.id.view_search_blocker_loading) FrameLayout searchBlockerLoadingView;
    @BindView(R.id.view_map_rooms_empty) RelativeLayout roomsEmptyView;

    private MapRoomsAdapter mapRoomsAdapter;
    private GoogleMap googleMap;
    private Unbinder unbinder;
    private List<Room> roomList = new ArrayList<>();
    private List<Integer> roomSeenList = new ArrayList<>();
    private List<RoomMarker> roomMarkerList = new ArrayList<>();
    private List<Marker> dotMarkerList = new ArrayList<>();
    private Marker selectedMarker;
    private HashMap<Room, Marker> roomMarkerHashMap = new HashMap<>();
    private TouchableMapFragment mapFragment;
    private Filters filters;
    private Handler handler = new Handler();
    private boolean isRunningSearchDelay = false;
    private boolean isUserInteractingWithMap = false;
    private boolean isMyLocationButtonClicked = false;
    private boolean shouldMoveMap = true;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance(Coordinates coordinates, Location location) {
        MapFragment propertiesFragment = new MapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COORDINATES, coordinates);
        args.putParcelable(ARG_LOCATION, location);
        propertiesFragment.setArguments(args);
        return propertiesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((MainActivity) getActivity()).getRoomComponent().inject(this);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        mapPresenter.attachView(this);

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        mapFragment = (TouchableMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupRoomsMapViewPager();

        setupViewPagerListener();

        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        //Binding reset to avoid memory leaks
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Navigator.REQUEST_CODE_ROOM_DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                int editedRoomID = data.getIntExtra(RoomDetailActivity.EXTRA_ROOM_ID, 0);
                for (Room room : roomList) {
                    if (room.id().equals(editedRoomID)) {
                        Marker marker = roomMarkerHashMap.get(room);
                        if (marker != null) {
                            int indexRoom = roomList.indexOf(room);
                            roomList.remove(indexRoom);
                            roomMarkerHashMap.get(room).remove();
                            roomMarkerHashMap.remove(room);
                            mapPresenter.incrementOffsetRequestedRoom();
                        }
                        break;
                    }
                }
                mapRoomsAdapter.setRooms(this.roomList);
                if (roomList.isEmpty()) showEmptyView();
            }
        }
        if (requestCode == Navigator.REQUEST_CODE_FILTERS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                filters = data.getParcelableExtra(FiltersActivity.EXTRA_FILTERS);
            } else {
                filters = null;
            }
            if (googleMap != null) {
                mapPresenter.loadRoomsWithBounds(googleMap.getProjection().getVisibleRegion().latLngBounds, filters);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isResumed()) {
            handleRemoveRequestedRoomsFromList();
        }
    }

    private void loadRooms() {
        if (filters != null) {
            mapPresenter.loadRoomsWithBounds(googleMap.getProjection().getVisibleRegion().latLngBounds, filters);
            return;
        }

        Coordinates coordinates = getArguments().getParcelable(ARG_COORDINATES);
        if (coordinates != null)
            mapPresenter.loadRoomsWithCoordinates(coordinates, filters);
        else {
            Location location = getArguments().getParcelable(ARG_LOCATION);
            if (location != null) {
                mapPresenter.loadRoomsWithLocation(location, filters);
            }
        }
    }

    private Runnable searchMoreRoomsRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isAdded() || isUserInteractingWithMap) {
                return;
            }

            isRunningSearchDelay = false;
            ((SearchResultFragment) getParentFragment()).setCustomMapSearchToolbarTitle();
            mapPresenter.loadRoomsWithBounds(googleMap.getProjection().getVisibleRegion().latLngBounds, filters);
        }
    };

    private void searchMoreRoomsWithDelay() {
        if (isRunningSearchDelay || isUserInteractingWithMap) {
            handler.removeCallbacks(searchMoreRoomsRunnable);
        }

        isRunningSearchDelay = true;
        handler.postDelayed(searchMoreRoomsRunnable, 500);
    }

    private void setupRoomsMapViewPager() {
        mapRoomsAdapter = new MapRoomsAdapter(context());
        mapRoomsAdapter.setRooms(roomList);
        mapRoomsAdapter.setOnRoomListener(onRoomListener);
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        roomsViewPager.setPageMargin((int)
                (getResources().getDimension(R.dimen.viewpager_page_margin) / getResources().getDisplayMetrics().density));
        roomsViewPager.setAdapter(mapRoomsAdapter);
    }

    private MapRoomsAdapter.OnRoomListener onRoomListener = (roomImage, room) -> {
        RoomBase roomBase = RoomBase.create(room.id(), room.status(), room.title(), room.owned(), room.allowedToBook(),
                new ArrayList<>(room.pictures()), new ArrayList<>(room.pricesAttributes()));
        navigator.navigateToRoomDetailFromMap(getActivity(), roomBase, roomImage);
    };

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMarkerClickListener(this);
        this.googleMap.setOnCameraMoveStartedListener(this);
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnMyLocationButtonClickListener(() -> {
            isMyLocationButtonClicked = true;
            return false;
        });

        if (ContextCompat.checkSelfPermission(context(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // Access to the location has been granted to the app.
            this.googleMap.setMyLocationEnabled(true);
        }
        loadRooms();

        new MapStateListener(googleMap, mapFragment, getActivity()) {
            @Override
            public void onMapTouched() {

            }

            @Override
            public void onMapReleased() {

            }

            @Override
            public void onMapUnsettled() {

            }

            @Override
            public void onMapSettled() {

            }
        };
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!isAdded()) {
            return false;
        }

        if (roomMarkerHashMap.containsValue(marker) && (context() != null || googleMap != null)) {
            Room room = MapUtils.getKeyByValue(roomMarkerHashMap, marker);
            if (room != null) {
                if (selectedMarker != null) {
                    Room roomSelectedMarker = MapUtils.getKeyByValue(roomMarkerHashMap, selectedMarker);
                    if (roomSelectedMarker != null) {
                        roomSeenList.add(roomSelectedMarker.id());
                        selectedMarker.setIcon(MapUtils.createDrawableFromView(
                                context(),
                                PriceUtils.getPriceMap(context(), roomSelectedMarker.pricesAttributes()),
                                false, roomSeenList.contains(roomSelectedMarker.id())));
                    }
                }
                if (roomsViewPager != null && MapUtils.getKeyByValue(roomMarkerHashMap, marker) != null)
                    roomsViewPager.setCurrentItem(roomList.indexOf(MapUtils.getKeyByValue(roomMarkerHashMap, marker)));
                selectedMarker = marker;
                selectedMarker.setIcon(MapUtils.createDrawableFromView(
                        context(),
                        PriceUtils.getPriceMap(context(), room.pricesAttributes()),
                        true, roomSeenList.contains(room.id())));
                rearrangeMarkers();
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private void rearrangeMarkers() {
        // Set all markers Z Index to 0
        for (Marker marker : roomMarkerHashMap.values()) marker.setZIndex(0.0f);
        // Set selected marker Z Index to 1
        selectedMarker.setZIndex(1.0f);
    }

    private void showMarkersInMap(List<Room> roomList) {
        if (context() != null && googleMap != null) {
            for (Room room : roomList) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(room.latitude(), room.longitude()))
                        .anchor(0.5f, 1.0f)
                        .icon(MapUtils.createDrawableFromView(
                                context(),
                                PriceUtils.getPriceMap(context(), room.pricesAttributes()),
                                selectedMarker == null, roomSeenList.contains(room.id()))));
                if (selectedMarker == null) selectedMarker = marker;
                roomMarkerHashMap.put(room, marker);
            }
        }
    }

    private void setupViewPagerListener() {
        if (roomsViewPager != null)
            roomsViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                public void onPageSelected(int position) {
                    if (isAdded()) {
                        Marker marker = roomMarkerHashMap.get(roomList.get(position));
                        if (marker != null) {
                            onMarkerClick(marker);
                        }
                        if (position == roomList.size() - 1) {
                            mapPresenter.loadRoomsPaginated();
                        }
                    }
                }
            });
    }

    private void animateCameraOnMarkersPosition() {
        if (!isAdded()) {
            return;
        }

        if (!shouldMoveMap) {
            return;
        }

        shouldMoveMap = false;
        if (roomMarkerHashMap.isEmpty()) {
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : roomMarkerHashMap.values()) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30); // offset from edges of the map 30% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

        googleMap.moveCamera(cu);
        googleMap.moveCamera(CameraUpdateFactory.zoomTo(12));
    }

    private void reSelectPreviousSelection(Room previouslySelectedRoom) {
        if (previouslySelectedRoom == null) {
            return;
        }

        for (Room room : roomList) {
            if (room != null && previouslySelectedRoom.id().equals(room.id())) {
                Marker roomMarker = roomMarkerHashMap.get(room);
                onMarkerClick(roomMarker);
                return;
            }
        }
    }

    public void updateMarkersDot(List<RoomMarker> roomMarkers) {
        this.roomMarkerList = roomMarkers;
        if (!roomMarkerList.isEmpty()) {
            List<RoomMarker> roomMarkerListToDelete = new ArrayList<>();
            for (RoomMarker roomMarker : roomMarkerList) {
                if (!roomList.isEmpty()) {
                    for (Room room : roomList) {
                        if (roomMarker.id().equals(room.id())) {
                            roomMarkerListToDelete.add(roomMarker);
                        }
                    }
                }
            }
            roomMarkerList.removeAll(roomMarkerListToDelete);
            //Clear all current dot markers
            if (!dotMarkerList.isEmpty()) {
                for (Marker marker : dotMarkerList) {
                    marker.remove();
                }
                dotMarkerList.clear();
            }
        }
    }

    private void showDotMarkersInMap() {
        if (googleMap != null && !roomMarkerList.isEmpty()) {
            for (RoomMarker roomMarker : roomMarkerList) {
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(roomMarker.latitude(), roomMarker.longitude()))
                        .anchor(0.5f, 1.0f)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_marker_dot)));
                dotMarkerList.add(marker);
            }
        }
    }

    private void handleRemoveRequestedRoomsFromList() {
        if (!RoomsListFragment.roomsRequestedIDs.isEmpty() && !roomList.isEmpty()) {
            List<Room> roomsToDelete = new ArrayList<>();
            for (Room room : roomList) {
                if (RoomsListFragment.roomsRequestedIDs.contains(room.id())) {
                    roomsToDelete.add(room);
                    mapPresenter.incrementOffsetRequestedRoom();
                }
            }
            for (Room room : roomsToDelete) {
                if (roomMarkerHashMap.containsKey(room)) {
                    roomMarkerHashMap.get(room).remove();
                    roomMarkerHashMap.remove(room);
                }
            }
            roomList.removeAll(roomsToDelete);
            RoomsListFragment.roomsRequestedIDs.clear();
            mapRoomsAdapter.setRooms(this.roomList);
            if (roomList.isEmpty()) showEmptyView();
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {
        isUserInteractingWithMap = reason == 1;
    }

    @Override
    public void onCameraIdle() {
        if (isMyLocationButtonClicked || isUserInteractingWithMap) {
            isMyLocationButtonClicked = false;
            isUserInteractingWithMap = false;
            searchMoreRoomsWithDelay();
        }
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyView() {
        shouldMoveMap = false;
        if (roomsEmptyView != null) roomsEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        if (roomsEmptyView != null) roomsEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void clearMap() {
        if (googleMap != null) googleMap.clear();
    }

    @Override
    public void showRoomList(List<Room> roomList) {
        this.roomList = roomList;
        mapRoomsAdapter.setRooms(this.roomList);

        Room previouslySelectedRoom = MapUtils.getKeyByValue(roomMarkerHashMap, selectedMarker);
        selectedMarker = null;
        roomMarkerHashMap.clear();

        showMarkersInMap(this.roomList);

        updateMarkersDot(roomMarkerList);

        showDotMarkersInMap();

        animateCameraOnMarkersPosition();

        reSelectPreviousSelection(previouslySelectedRoom);
    }

    @Override
    public void showRoomMarkerList(List<RoomMarker> markersList) {
        this.roomMarkerList = markersList;

        updateMarkersDot(roomMarkerList);

        showDotMarkersInMap();
    }

    @Override
    public void addRoomsToRoomList(List<Room> roomList) {
        this.roomList.addAll(roomList);
        mapRoomsAdapter.setRooms(this.roomList);

        showMarkersInMap(roomList);

        updateMarkersDot(roomMarkerList);

        showDotMarkersInMap();

        animateCameraOnMarkersPosition();

    }

    @Override
    public void showLoading() {
        if (searchLoadingView != null) searchLoadingView.setVisibility(View.VISIBLE);
        if (searchBlockerLoadingView != null) searchBlockerLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (searchLoadingView != null) searchLoadingView.setVisibility(View.GONE);
        if (searchBlockerLoadingView != null) searchBlockerLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        if (context() != null)
            DialogFactory.createSimpleOkErrorDialog(context(), getString(R.string.error_warning), message).show();
    }

    @Override
    public Context context() {
        return getContext();
    }
}
