/*
 * File: RoomsAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.DateUtil;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.PriceUtils;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.Tenant;
import com.badi.presentation.roomdetail.PicturesAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.relex.circleindicator.CircleIndicator;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link Room}.
 */
public class RoomsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ROOM = 1;
    private static final int VIEW_TYPE_DESCRIPTION = 2;

    private Context context;
    private List<Room> roomsList;
    private OnRoomListener onRoomListener;

    interface OnRoomListener {
        void onUserItemClicked(View roomImage, View userImage, Room room);
        void onUserProfileClicked(View userImage, Tenant tenant);
        void onUserShareClicked(int position);
        void onUserRequestClicked(int position);
    }

    RoomsAdapter(Context context) {
        this.context = context;
        this.roomsList = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DESCRIPTION) {
            View descriptionView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_list_description,
                    parent, false);
            return new DescriptionHolder(descriptionView);
        } else {
            View roomView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
            return new RoomHolder(roomView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() != VIEW_TYPE_DESCRIPTION) {
            // Show room details inside viewHolder
            RoomHolder roomHolder = (RoomHolder) holder;
            roomHolder.show(roomsList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return roomsList == null || roomsList.size() <= 0 ? 0 : roomsList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position == (getItemCount() - 1) ? VIEW_TYPE_DESCRIPTION : VIEW_TYPE_ROOM;
    }

    public void setRooms(List<Room> rooms) {
        roomsList = rooms;
        notifyDataSetChanged();
    }

    private class DescriptionHolder extends RecyclerView.ViewHolder {
        DescriptionHolder(View itemView) {
            super(itemView);
        }
    }

    class RoomHolder extends RecyclerView.ViewHolder {

        @Inject PicturesAdapter picturesAdapter;

        @BindView(R.id.label_my_room) RelativeLayout myRoomLabel;

        @BindView(R.id.images_room_viewpager) ViewPager imagesRoomViewPager;

        @BindView(R.id.images_room_indicator) CircleIndicator imagesRoomIndicator;

        @BindView(R.id.image_thumbnail_user_room) ImageView userImage;

        @BindView(R.id.border_thumbnail_user_room) RelativeLayout borderUserImage;

        @BindView(R.id.image_verified_profile) ImageView verifiedProfileImage;

        @BindView(R.id.text_user_name_age) TextView userNameAgeText;

        @BindView(R.id.text_title_room) TextView roomTitleText;

        @BindView(R.id.text_value_room) TextView roomValueText;

        @BindView(R.id.button_send_request_room) ImageButton roomSendRequestButton;

        @BindView(R.id.button_delete_room) ImageButton roomDeleteButton;

        RoomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(Room room) {
            if (!room.pictures().isEmpty()) {
                picturesAdapter = new PicturesAdapter(context);
                picturesAdapter.setPictures(room.pictures());
                imagesRoomViewPager.setAdapter(picturesAdapter);
                imagesRoomIndicator.setViewPager(imagesRoomViewPager);
                picturesAdapter.setOnPictureListener(new PicturesAdapter.OnPictureListener() {
                    @Override
                    public void onUserItemClicked(View roomImage, Integer roomID) {
                        int adapterPosition = getAdapterPosition();
                        if (adapterPosition < 0 || adapterPosition >= roomsList.size()) {
                            return;
                        }
                        onRoomListener.onUserItemClicked(roomImage, borderUserImage, roomsList.get(adapterPosition));
                    }
                });
            }

            if (!room.tenants().isEmpty()) {
                Tenant mainTenant = room.tenants().get(0);
                if (!mainTenant.pictures().isEmpty())
                    GlideApp.with(context)
                            .load(mainTenant.pictures().get(0).width100Url())
                            .transition(withCrossFade())
                            .circleCrop()
                            .placeholder(R.drawable.ic_placeholder_profile_round)
                            .error(R.drawable.ic_placeholder_profile_round)
                            .into(userImage);
                else
                    userImage.setImageResource(R.drawable.ic_placeholder_profile_round);


                borderUserImage.setBackground(mainTenant.verifiedAccount() ?
                        ContextCompat.getDrawable(borderUserImage.getContext(), R.drawable.img_circle_border_green) :
                        ContextCompat.getDrawable(borderUserImage.getContext(), R.drawable.img_circle_border_white));

                verifiedProfileImage.setVisibility(mainTenant.verifiedAccount() ? View.VISIBLE : View.GONE);

                String nameAgeUser;
                if (mainTenant.birthDate() != null) {
                    nameAgeUser = mainTenant.firstName() + ", " + DateUtil.getDiffYears(mainTenant.birthDate(), new Date());
                } else {
                    nameAgeUser = mainTenant.firstName();
                }
                userNameAgeText.setText(nameAgeUser);
            }

            roomTitleText.setText(room.title());
            roomValueText.setText(PriceUtils.getPriceRoom(roomValueText.getContext(), room.pricesAttributes()));

            roomSendRequestButton.setVisibility(View.VISIBLE);
            //Set selected state based on this state
            roomSendRequestButton.setSelected(roomsList.get(getAdapterPosition()).requested());
            roomSendRequestButton.setClickable(!roomsList.get(getAdapterPosition()).requested());

            myRoomLabel.setVisibility(room.owned() ? View.VISIBLE : View.GONE);
            roomSendRequestButton.setVisibility(room.owned() ? View.INVISIBLE : View.VISIBLE);

            // Hide 'Delete Denied Request' button
            roomDeleteButton.setVisibility(View.GONE);
        }

        @OnClick(R.id.container_user)
        void onImageUserClick() {
            if (getAdapterPosition() != -1)
                onRoomListener.onUserProfileClicked(borderUserImage, roomsList.get(getAdapterPosition()).tenants().get(0));
        }

        @OnClick(R.id.container_room)
        void onImageRoomClick(View view) {
            if (getAdapterPosition() != -1)
                onRoomListener.onUserItemClicked(view, borderUserImage, roomsList.get(getAdapterPosition()));
        }

        @OnClick(R.id.button_send_request_room)
        void onRequestButtonClick(View view) {
            if (view.isClickable()) {
                view.setClickable(false);
                if (getAdapterPosition() != -1)
                    onRoomListener.onUserRequestClicked(getAdapterPosition());
            }
        }

        @OnClick(R.id.button_share_room)
        void onShareButtonClick() {
            if (getAdapterPosition() != -1)
                onRoomListener.onUserShareClicked(getAdapterPosition());
        }
    }

    void setOnRoomListener(OnRoomListener onRoomListener) {
        this.onRoomListener = onRoomListener;
    }
}
