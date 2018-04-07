/*
 * File: RoomsListFragment.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.EndlessRecyclerViewScrollListener;
import com.badi.common.utils.RoomUtils;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.RoomBase;
import com.badi.data.entity.room.Tenant;
import com.badi.data.entity.search.Coordinates;
import com.badi.data.entity.search.Filters;
import com.badi.data.entity.search.Location;
import com.badi.data.entity.user.UserBase;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.presentation.base.BaseFragment;
import com.badi.presentation.main.MainActivity;
import com.badi.presentation.navigation.Navigator;
import com.badi.presentation.roomdetail.RoomDetailActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A {@link BaseFragment} that displays a list of rooms
 */
public class RoomsListFragment extends BaseFragment implements RoomsContract.View {

    private static final String ARG_COORDINATES = "ARG_COORDINATES";
    private static final String ARG_LOCATION = "ARG_LOCATION";

    @Inject PreferencesHelper preferencesHelper;
    @Inject RoomsPresenter roomsPresenter;

    public static List<Integer> roomsRequestedIDs = new ArrayList<>();

    @BindView(R.id.layout_swipe_refresh) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler_view_rooms) RecyclerView recyclerView;
    @BindView(R.id.text_distance_from_room) TextView distanceFromRoomText;
    @BindView(R.id.view_room_list_empty) View roomListEmptyView;

    private RoomsAdapter roomsAdapter;
    private Unbinder unbinder;
    private List<Room> roomList = new ArrayList<>();
    private Filters filters;

    public RoomsListFragment() {
        // Required empty public constructor
    }

    public static RoomsListFragment newInstance(Coordinates coordinates, Location location) {
        RoomsListFragment propertiesFragment = new RoomsListFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_room_list, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        roomsPresenter.attachView(this);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(context(), R.color.green_500));
        swipeRefreshLayout.setOnRefreshListener(() -> roomsPresenter.onRefresh());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context());
        linearLayoutManager.setInitialPrefetchItemCount(2);
        // Retain an instance so that you can call `resetState()` for fresh searches
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                if (roomsPresenter.isViewAttached()) {
                    roomsPresenter.loadRoomsPaginated(page);
                }
            }

            @Override
            public void onScrollFirstVisibleItem(int firstItemVisible) {
                if (firstItemVisible != -1 && firstItemVisible < roomList.size()) {
                    if (roomList.get(firstItemVisible).distance() != null) {
                        distanceFromRoomText.setVisibility(View.VISIBLE);
                        distanceFromRoomText.setText(RoomUtils.getDistance(roomList.get(firstItemVisible).distance()));
                    } else {
                        distanceFromRoomText.setVisibility(View.GONE);
                    }
                }
            }

        };

        roomsAdapter = new RoomsAdapter(context());
        roomsAdapter.setRooms(roomList);
        roomsAdapter.setOnRoomListener(onRoomListener);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(roomsAdapter);
        recyclerView.addOnScrollListener(scrollListener);

        loadRooms();
        return fragmentView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Navigator.REQUEST_CODE_ROOM_DETAIL) {
            if (resultCode == Activity.RESULT_OK) {
                int editedRoomID = data.getIntExtra(RoomDetailActivity.EXTRA_ROOM_ID, 0);
                for (Room room : roomList) {
                    if (room.id().equals(editedRoomID)) {
                        removeRoomRequestFromList(room);
                        roomsPresenter.incrementOffsetRequestedRoom();
                        break;
                    }
                }
            }
        }
        if (requestCode == Navigator.REQUEST_CODE_FILTERS_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                filters = data.getParcelableExtra(FiltersActivity.EXTRA_FILTERS);
            } else {
                filters = null;
            }
            loadRooms();
        }
    }

    @Override
    public void onDestroyView() {
        //Binding reset to avoid memory leaks
        unbinder.unbind();
        super.onDestroyView();
    }

    private void loadRooms() {
        Coordinates coordinates = getArguments().getParcelable(ARG_COORDINATES);
        if (coordinates != null)
            roomsPresenter.loadRoomsWithCoordinates(coordinates, filters);
        else {
            Location location = getArguments().getParcelable(ARG_LOCATION);
            if (location != null) {
                roomsPresenter.loadRoomsWithLocation(location, filters);
            }
        }
    }

    private void showCompleteProfileDialog() {

    }

    private RoomsAdapter.OnRoomListener onRoomListener = new RoomsAdapter.OnRoomListener() {

        @Override
        public void onUserItemClicked(View roomImage, View userImage, Room room) {
            RoomBase roomBase = RoomBase.create(room.id(), room.status(), room.title(), room.owned(), room.allowedToBook(),
                    new ArrayList<>(room.pictures()), new ArrayList<>(room.pricesAttributes()));
            navigator.navigateToRoomDetail(getActivity(), roomBase, roomImage, userImage);
        }

        @Override
        public void onUserProfileClicked(View userImage, Tenant tenant) {
            UserBase user = UserBase.create(tenant.id(), tenant.firstName(), tenant.lastName(), tenant.birthDate(),
                    tenant.pictures());
        }

        @Override
        public void onUserShareClicked(int position) {
            if (roomList.get(position).owned())
                RoomUtils.shareMyRoom(getActivity(), roomList.get(position).id());
            else
                RoomUtils.shareRoom(getActivity(), roomList.get(position).id(), roomList.get(position).city());
        }

        @Override
        public void onUserRequestClicked(int position) {
            if (preferencesHelper.getValidationStatus()) {
                roomsPresenter.requestRoom(roomList.get(position).id());
            } else
                showCompleteProfileDialog();
        }

    };

    private void removeRoomRequestFromList(Room room) {
        int indexRoom = roomList.indexOf(room);
        roomList.remove(room);
        if (!roomList.isEmpty())
            roomsAdapter.notifyItemRemoved(indexRoom);
        else {
            roomsAdapter.notifyDataSetChanged();
            showEmptyView();
        }
    }

    /***** MVP View methods implementation *****/

    @Override
    public void showEmptyView() {
        if (roomListEmptyView != null) roomListEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        if (roomListEmptyView != null) roomListEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showRoomList(List<Room> roomList) {
        this.roomList = roomList;
        roomsAdapter.setRooms(this.roomList);
    }

    @Override
    public void addRoomsToRoomList(List<Room> roomList) {
        this.roomList.addAll(roomList);
        roomsAdapter.setRooms(this.roomList);
    }

    @Override
    public void deleteRequestedRoomFromRoomList(Integer roomID) {
        for (Room room : roomList) {
            if (room.id().equals(roomID)) {
                roomsRequestedIDs.add(room.id());
                removeRoomRequestFromList(room);
                break;
            }
        }
    }

    @Override
    public void showLoading() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {
        DialogFactory.createSimpleOkErrorDialog(context(),
                getString(R.string.error_warning), message)
                .show();
    }

    @Override
    public Context context() {
        return getContext();
    }
}
