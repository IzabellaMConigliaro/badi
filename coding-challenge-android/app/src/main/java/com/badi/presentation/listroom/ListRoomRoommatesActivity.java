/*
 * File: ListRoomRoommatesActivity.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.badi.R;
import com.badi.common.di.HasComponent;
import com.badi.common.di.components.DaggerRoomComponent;
import com.badi.common.di.components.RoomComponent;
import com.badi.common.di.modules.RoomModule;
import com.badi.common.utils.DialogFactory;
import com.badi.common.utils.UserUtils;
import com.badi.common.utils.ViewUtil;
import com.badi.data.entity.room.Tenant;
import com.badi.presentation.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListRoomRoommatesActivity extends BaseActivity implements HasComponent<RoomComponent>,
        ListRoomRoommatesContract.View {

    public static final String LIST_ROOM_EXTRA_ROOMMATES = "ListRoomRoommatesActivity.LIST_ROOM_EXTRA_ROOMMATES";

    @Inject ListRoomRoommatesPresenter listRoomRoommatesPresenter;

    @BindView(R.id.edit_text_autocomplete) EditText autoCompleteEditText;
    @BindView(R.id.button_clear_text) ImageView clearTextButton;
    @BindView(R.id.recycler_view_autocomplete_roommates) RecyclerView roommatesRecyclerView;
    @BindView(R.id.layout_invite_roommates) View inviteRoommatesLayout;
    @BindView(R.id.layout_empty_roommates) View emptyRoommatesLayout;

    private RoomComponent roomComponent;
    private ListRoomRoommatesAdapter listRoomRoommatesAdapter;

    private List<Tenant> savedTenants = new ArrayList<>();

    /**
     * Return an Intent to start this Activity.
     */
    public static Intent getCallingIntent(Context context, ArrayList<Tenant> tenants) {
        Intent intent = new Intent(context, ListRoomRoommatesActivity.class);
        intent.putParcelableArrayListExtra(LIST_ROOM_EXTRA_ROOMMATES, tenants);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room_roommates);
        this.initializeInjector();
        this.getComponent().inject(this);
        ButterKnife.bind(this);
        listRoomRoommatesPresenter.attachView(this);
        savedTenants = getIntent().getParcelableArrayListExtra(LIST_ROOM_EXTRA_ROOMMATES);
        setResultActivity();
        initComponents();
    }

    @Override
    public void onDestroy() {
        listRoomRoommatesPresenter.detachView();
        super.onDestroy();
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
        listRoomRoommatesAdapter = new ListRoomRoommatesAdapter(context());
        listRoomRoommatesAdapter.setSavedTenants(savedTenants);
        listRoomRoommatesAdapter.setOnRoommatesListener(onRoommatesListener);
        roommatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        roommatesRecyclerView.setAdapter(listRoomRoommatesAdapter);
        autoCompleteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    clearTextButton.setVisibility(View.VISIBLE);
                    inviteRoommatesLayout.setVisibility(View.GONE);
                    listRoomRoommatesPresenter.searchUsers(s.toString());
                } else {
                    clearTextButton.setVisibility(View.GONE);
                    inviteRoommatesLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private ListRoomRoommatesAdapter.OnRoommatesListener onRoommatesListener =
            new ListRoomRoommatesAdapter.OnRoommatesListener() {
        @Override
        public void onUserAddRoommate(Tenant tenant) {
            savedTenants.add(tenant);
            listRoomRoommatesAdapter.setSavedTenants(savedTenants);
        }

        @Override
        public void onUserDeleteRoommate(Tenant tenant) {
            savedTenants.remove(tenant);
            listRoomRoommatesAdapter.setSavedTenants(savedTenants);
        }
    };

    @OnClick(R.id.button_close)
    public void onCloseButtonClick() {
        ViewUtil.hideKeyboard(this);
        supportFinishAfterTransition();
    }

    @OnClick(R.id.button_clear_text)
    void onClickClearTextButton() {
        autoCompleteEditText.setText("");
        listRoomRoommatesAdapter.clear();
    }

    @OnClick(R.id.text_invite_roommates)
    void onClickInviteRoommates() {
        UserUtils.inviteFriendIntent(this);
    }

    @OnClick(R.id.button_add_roommates)
    void onClickAddRoommates() {
        Intent resultIntent = new Intent();
        resultIntent.putParcelableArrayListExtra(LIST_ROOM_EXTRA_ROOMMATES, new ArrayList<>(savedTenants));
        setResult(Activity.RESULT_OK, resultIntent);
        ViewUtil.hideKeyboard(this);
        supportFinishAfterTransition();
    }

    private void setResultActivity() {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, resultIntent);
    }

    /*****
     * MVP View methods implementation
     *****/

    @Override
    public void showTenantListFiltered(List<Tenant> tenantList) {
        roommatesRecyclerView.setVisibility(View.VISIBLE);
        emptyRoommatesLayout.setVisibility(View.GONE);
        listRoomRoommatesAdapter.setTenants(tenantList);
    }

    @Override
    public void showEmptyTenantList() {
        roommatesRecyclerView.setVisibility(View.GONE);
        emptyRoommatesLayout.setVisibility(View.VISIBLE);
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
        DialogFactory.createSimpleOkErrorDialog(context(), getString(R.string.error_warning), message).show();
    }

    @Override
    public Context context() {
        return this;
    }
}
