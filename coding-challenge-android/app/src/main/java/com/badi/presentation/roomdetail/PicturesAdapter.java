/*
 * File: PicturesAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.common.utils.RecyclerPagerAdapter;
import com.badi.data.entity.user.Picture;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link Picture}.
 */
public class PicturesAdapter extends RecyclerPagerAdapter<PicturesAdapter.PictureHolder> {

    private Context context;
    private List<Picture> pictureList;
    private OnPictureListener onPictureListener;

    public interface OnPictureListener {
        void onUserItemClicked(View roomImage, Integer roomID);
    }

    public PicturesAdapter(Context context) {
        this.context = context;
        this.pictureList = new ArrayList<>();
    }

    @Override
    public PictureHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_picture, parent, false);
        return new PictureHolder(view);
    }

    @Override
    public void onBindViewHolder(PictureHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(pictureList.get(position));
    }

    @Override
    public int getItemCount() {
        return pictureList == null ? 0 : pictureList.size();
    }

    public void setPictures(List<Picture> pictures) {
        pictureList = pictures;
        notifyDataSetChanged();
    }

    class PictureHolder extends RecyclerPagerAdapter.ViewHolder {

        @BindView(R.id.picture_room) ImageView roomImage;

        PictureHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(final Picture picture) {
            GlideApp.with(context)
                    .load(picture.width1080Url())
                    .transition(withCrossFade())
                    .placeholder(R.drawable.ic_placeholder_room)
                    .centerCrop()
                    .into(roomImage);

            roomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onPictureListener.onUserItemClicked(v, picture.id());
                }
            });
        }
    }

    public void setOnPictureListener(OnPictureListener onPictureListener) {
        this.onPictureListener = onPictureListener;
    }
}
