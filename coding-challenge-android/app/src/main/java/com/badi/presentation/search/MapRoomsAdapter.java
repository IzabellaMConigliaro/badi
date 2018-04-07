/*
 * File: MapRoomsAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.search;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.PriceUtils;
import com.badi.common.utils.RecyclerPagerAdapter;
import com.badi.data.entity.room.Room;
import com.badi.data.entity.room.Tenant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link Room} in the search.
 */
public class MapRoomsAdapter extends RecyclerPagerAdapter<MapRoomsAdapter.RoomHolder> {

    private Context context;
    private List<Room> roomsList;
    private OnRoomListener onRoomListener;

    interface OnRoomListener {
        void onUserItemClicked(View roomImage, Room room);
    }

    MapRoomsAdapter(Context context) {
        this.context = context;
        this.roomsList = new ArrayList<>();
    }

    @Override
    public RoomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room_map, parent, false);
        return new RoomHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(roomsList.get(position));
    }

    @Override
    public int getItemCount() {
        return roomsList == null ? 0 : roomsList.size();
    }

    public void setRooms(List<Room> rooms) {
        roomsList = rooms;
        notifyDataSetChanged();
    }

    class RoomHolder extends RecyclerPagerAdapter.ViewHolder {

        @BindView(R.id.container_room) View roomContainer;

        @BindView(R.id.image_thumbnail_room) ImageView roomImage;

        @BindView(R.id.image_thumbnail_user_room) ImageView userImage;

        @BindView(R.id.border_thumbnail_user_room) RelativeLayout borderUserImage;

        @BindView(R.id.text_title_room) TextView roomTitleText;

        @BindView(R.id.text_value_room) TextView roomValueText;

        RoomHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(final Room room) {
            if (!room.pictures().isEmpty())
                GlideApp.with(context)
                        .load(room.pictures().get(0).width500Url())
                        .transition(withCrossFade())
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder_room_map)
                        .into(roomImage);
            else
                roomImage.setImageResource(R.drawable.ic_placeholder_room_map);

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
            }

            roomTitleText.setText(room.title());
            roomValueText.setText(PriceUtils.getPriceRoom(roomValueText.getContext(), room.pricesAttributes()));
            roomContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRoomListener.onUserItemClicked(roomImage, room);
                }
            });
        }

    }

    void setOnRoomListener(OnRoomListener onRoomListener) {
        this.onRoomListener = onRoomListener;
    }
}
