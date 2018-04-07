/*
 * File: ListRoomPicturesAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.recyclerview.ItemTouchHelperAdapter;
import com.badi.common.utils.recyclerview.ItemTouchHelperViewHolder;
import com.badi.data.entity.user.Picture;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Adapter that manages a collection of {@link Picture}.
 */
public class ListRoomPicturesAdapter extends RecyclerView.Adapter<ListRoomPicturesAdapter.PictureHolder>
        implements ItemTouchHelperAdapter {

    public static final String URL_DEFAULT_IMAGE = "URL_DEFAULT_IMAGE";

    private Context context;
    private List<Picture> pictureList;
    private OnPictureListener onPictureListener;

    interface OnPictureListener {
        void onUserItemClicked(Integer position);
        void onUserItemDeleteClicked(Integer position);
        void onUserItemMovedRefresh();
    }

    ListRoomPicturesAdapter(Context context) {
        this.context = context;
        this.pictureList = new ArrayList<>();
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_room_picture, parent, false);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(pictureList.get(position));
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(pictureList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(pictureList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        onPictureListener.onUserItemMovedRefresh();
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        pictureList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return pictureList == null ? 0 : pictureList.size();
    }

    public void setPictures(List<Picture> pictures) {
        pictureList = pictures;
        notifyDataSetChanged();
    }

    class PictureHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

        @BindView(R.id.layout_room_picture) RelativeLayout roomImageLayout;

        @BindView(R.id.image_room_picture) ImageView roomImage;

        @BindView(R.id.image_delete_picture) ImageView deleteRoomImage;

        PictureHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(Picture picture) {
            if (picture.url().equals(URL_DEFAULT_IMAGE)) {
                roomImage.setImageResource(picture.id());
                deleteRoomImage.setVisibility(View.GONE);
            } else {
                GlideApp.with(context)
                        .asBitmap()
                        .load(picture.width500Url())
                        .centerCrop()
                        .into(new BitmapImageViewTarget(roomImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable roundedBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(roomImage.getResources(), resource);
                                roundedBitmapDrawable.setCornerRadius(10.0f);
                                roomImage.setImageDrawable(roundedBitmapDrawable);
                            }
                        });
                deleteRoomImage.setVisibility(View.VISIBLE);
            }
        }

        @OnClick(R.id.image_room_picture)
        void onClickPictureRoom() {
            onPictureListener.onUserItemClicked(getAdapterPosition());
        }

        @OnClick(R.id.image_delete_picture)
        void onClickDeletePicture() {
            onPictureListener.onUserItemDeleteClicked(getAdapterPosition());
        }

        @Override
        public void onItemSelected() {
            roomImageLayout.setAlpha(0.5f);
        }

        @Override
        public void onItemClear() {
            roomImageLayout.setAlpha(1.0f);
        }
    }

    void setOnPictureListener(OnPictureListener onPictureListener) {
        this.onPictureListener = onPictureListener;
    }
}
