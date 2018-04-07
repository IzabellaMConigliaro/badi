/*
 * File: ListRoomRoommatesAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.data.entity.room.Tenant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter that manages a collection of {@link Tenant}.
 */
public class ListRoomRoommatesAdapter extends RecyclerView.Adapter<ListRoomRoommatesAdapter.RoommateHolder> {

    private Context context;
    private List<Tenant> savedTenantsList = new ArrayList<>();
    private List<Tenant> tenantList;
    private OnRoommatesListener onRoommatesListener;

    interface OnRoommatesListener {
        void onUserAddRoommate(Tenant tenant);
        void onUserDeleteRoommate(Tenant tenant);
    }

    ListRoomRoommatesAdapter(Context context) {
        this.context = context;
        this.tenantList = new ArrayList<>();
    }

    @Override
    public RoommateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_roommate, parent, false);
        return new RoommateHolder(view);
    }

    @Override
    public void onBindViewHolder(RoommateHolder holder, int position) {
        // Show room details inside viewHolder
        holder.show(tenantList.get(position));
    }

    @Override
    public int getItemCount() {
        return tenantList == null ? 0 : tenantList.size();
    }

    public void setSavedTenants(List<Tenant> savedTenants) {
        savedTenantsList = savedTenants;
        notifyDataSetChanged();
    }

    public void setTenants(List<Tenant> tenants) {
        tenantList = tenants;
        notifyDataSetChanged();
    }

    public void clear() {
        this.tenantList.clear();
        notifyDataSetChanged();
    }

    class RoommateHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_roommate) CircleImageView roommateImage;

        @BindView(R.id.checked_text_view_roommate) CheckedTextView roommateCheckedTextView;

        RoommateHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(Tenant tenant) {
            if (!savedTenantsList.isEmpty()) {
                if (savedTenantsList.contains(tenant))
                    roommateCheckedTextView.setChecked(true);
                else
                    roommateCheckedTextView.setChecked(false);
            }
            if (!tenant.pictures().isEmpty())
                GlideApp.with(context)
                        .load(tenant.pictures().get(0).width500Url())
                        .placeholder(R.drawable.ic_placeholder_profile_round)
                        .centerCrop()
                        .dontAnimate()
                        .into(roommateImage);
            else
                roommateImage.setImageResource(R.drawable.ic_placeholder_profile_round);

            String fullNameRoommate = tenant.firstName() + " " + tenant.lastName();
            roommateCheckedTextView.setText(fullNameRoommate);
        }

        @OnClick(R.id.checked_text_view_roommate)
        void onClickRoommate(CheckedTextView view) {
            if (view.isChecked())
                onRoommatesListener.onUserDeleteRoommate(tenantList.get(getAdapterPosition()));
            else
                onRoommatesListener.onUserAddRoommate(tenantList.get(getAdapterPosition()));
        }

    }

    void setOnRoommatesListener(OnRoommatesListener onRoommatesListener) {
        this.onRoommatesListener = onRoommatesListener;
    }
}
