/*
 * File: ListRoomRoommatesPresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.room.Tenant;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.listroom.GetUsersByName;
import com.badi.presentation.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link ListRoomRoommatesPresenter} that controls communication between views and models of the presentation
 * layer of the list room view.
 */
@PerActivity
public class ListRoomRoommatesPresenter extends BasePresenter<ListRoomRoommatesContract.View>
        implements ListRoomRoommatesContract.Presenter {

    private final GetUsersByName getUsersByNameUseCase;

    @Inject
    ListRoomRoommatesPresenter(GetUsersByName getUsersByNameUseCase) {
        this.getUsersByNameUseCase = getUsersByNameUseCase;
    }

    @Override
    public void attachView(ListRoomRoommatesContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        getUsersByNameUseCase.clear();
    }

    @Override
    public void searchUsers(String query) {
        checkViewAttached();
        getView().showLoading();
        getUsersByNameUseCase.execute(query, new GetUsersByNameObserver());
    }


    private final class GetUsersByNameObserver extends DefaultObserver<List<Tenant>> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in searching for users data process in list room roommates");
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(List<Tenant> tenantList) {
            if (tenantList.isEmpty())
                getView().showEmptyTenantList();
            else
                getView().showTenantListFiltered(tenantList);
        }
    }

}
