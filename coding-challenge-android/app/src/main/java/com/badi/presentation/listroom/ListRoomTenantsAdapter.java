/*
 * File: ListRoomTenantsAdapter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.badi.R;
import com.badi.common.utils.GlideApp;
import com.badi.data.entity.room.Tenant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Adapter that manages a collection of {@link Tenant}.
 */
public class ListRoomTenantsAdapter extends RecyclerView.Adapter<ListRoomTenantsAdapter.TenantHolder> {

    private Context context;
    private List<Tenant> tenantList;

    ListRoomTenantsAdapter(Context context) {
        this.context = context;
        this.tenantList = new ArrayList<>();
    }

    @Override
    public TenantHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tenant, parent, false);
        return new TenantHolder(view);
    }

    @Override
    public void onBindViewHolder(TenantHolder holder, int position) {
        // Show tenant info inside viewHolder
        holder.show(tenantList.get(position));
    }

    @Override
    public int getItemCount() {
        return tenantList == null ? 0 : tenantList.size();
    }

    public void setTenants(List<Tenant> tenants) {
        tenantList = tenants;
        notifyDataSetChanged();
    }

    class TenantHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.image_thumbnail_user_room) ImageView userImage;

        @BindView(R.id.border_thumbnail_user_room) RelativeLayout borderUserImage;

        @BindView(R.id.text_name_tenant) TextView nameTenantText;

        TenantHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void show(Tenant tenant) {

            nameTenantText.setText(tenant.firstName());

            if (!tenant.pictures().isEmpty()) {
                GlideApp.with(context)
                        .load(tenant.pictures().get(0).width500Url())
                        .transition(withCrossFade())
                        .circleCrop()
                        .placeholder(R.drawable.ic_placeholder_profile_round)
                        .into(userImage);
            } else
                GlideApp.with(context)
                        .load(R.drawable.ic_placeholder_profile_round)
                        .transition(withCrossFade())
                        .circleCrop()
                        .placeholder(R.drawable.ic_placeholder_profile_round)
                        .into(userImage);


            borderUserImage.setBackground(tenant.verifiedAccount() ?
                    ContextCompat.getDrawable(borderUserImage.getContext(), R.drawable.img_circle_border_green) :
                    ContextCompat.getDrawable(borderUserImage.getContext(), R.drawable.img_circle_border_white));

        }

    }
}
