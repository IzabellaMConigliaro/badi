/*
 * File: RoomDetailPresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.roomdetail;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.user.User;
import com.badi.data.repository.local.PreferencesHelper;
import com.badi.domain.interactor.DefaultCompletableObserver;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.search.GetRoomDetailFromID;
import com.badi.domain.interactor.search.RequestRoom;
import com.badi.presentation.base.BasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link RoomDetailPresenter} that controls communication between views and models of the presentation
 * layer of the Room.
 */
@PerActivity
public class RoomDetailPresenter extends BasePresenter<RoomDetailContract.View> implements RoomDetailContract.Presenter {

    private final GetRoomDetailFromID getRoomDetailFromIDUseCase;
    private final RequestRoom requestRoomUseCase;
    private final PreferencesHelper preferencesHelper;

    @Inject
    RoomDetailPresenter(GetRoomDetailFromID getRoomDetailFromIDUseCase, RequestRoom requestRoomUseCase,
                        PreferencesHelper preferencesHelper) {
        this.getRoomDetailFromIDUseCase = getRoomDetailFromIDUseCase;
        this.requestRoomUseCase = requestRoomUseCase;
        this.preferencesHelper = preferencesHelper;
    }

    @Override
    public void attachView(RoomDetailContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        getRoomDetailFromIDUseCase.clear();
        requestRoomUseCase.clear();
    }

    @Override
    public void getRoomDetailFromID(Integer roomID) {
        checkViewAttached();
        getRoomDetailFromIDUseCase.execute(roomID, new GetRoomDetailFromIDObserver());
    }

    @Override
    public void requestRoom(Integer roomID) {
        checkViewAttached();
        requestRoomUseCase.execute(roomID, new RequestDetailRoomObserver());
    }

    @Override
    public void getValidationStatus() {

    }

    @Override
    public void acceptInvitation(Integer invitationID) {

    }

    @Override
    public void rejectInvitation(Integer invitationID) {

    }

    private final class GetRoomDetailFromIDObserver extends DefaultObserver<RoomDetail> {

        @Override
        public void onComplete() {
            getView().hideLoading();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting room detail from id data process");
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(RoomDetail roomDetail) {
            getView().showRoomDetails(roomDetail);

            preferencesHelper.increaseRoomDetailCounter();
            if (!preferencesHelper.isBookingTutorialShown() && preferencesHelper.getRoomDetailCounter() >= 5) {
                getView().showBookingTutorial(preferencesHelper.getUserRole() == User.USER_LISTER);
                preferencesHelper.setBookingTutorialShown();
            }
        }
    }

    private final class RequestDetailRoomObserver extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            getView().changeRequestButton();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in setting the room as requested data process");
            getView().hideLoading();
            getView().setRequestButtonClickable();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }
    }

}
