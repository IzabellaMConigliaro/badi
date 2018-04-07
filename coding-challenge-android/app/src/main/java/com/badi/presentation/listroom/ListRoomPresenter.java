/*
 * File: ListRoomPresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.listroom;

import com.badi.common.di.PerActivity;
import com.badi.common.exception.DefaultErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;
import com.badi.data.entity.room.RoomDetail;
import com.badi.data.entity.user.Picture;
import com.badi.domain.interactor.DefaultCompletableObserver;
import com.badi.domain.interactor.DefaultObserver;
import com.badi.domain.interactor.listroom.ClearRoomInPrefs;
import com.badi.domain.interactor.listroom.EditRoom;
import com.badi.domain.interactor.listroom.GetRoomInPrefs;
import com.badi.domain.interactor.listroom.SetRoomInPrefs;
import com.badi.domain.interactor.listroom.UploadRoom;
import com.badi.presentation.base.BasePresenter;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * {@link ListRoomPresenter} that controls communication between views and models of the presentation
 * layer of the list room view.
 */
@PerActivity
public class ListRoomPresenter extends BasePresenter<ListRoomContract.View> implements ListRoomContract.Presenter {

    private final UploadRoom uploadRoomUseCase;
    private final EditRoom editRoomUseCase;
    private final SetRoomInPrefs setRoomInPrefsUseCase;
    private final GetRoomInPrefs getRoomInPrefsUseCase;
    private final ClearRoomInPrefs clearRoomInPrefsUseCase;

    @Inject
    ListRoomPresenter(UploadRoom uploadRoomUseCase, EditRoom editRoomUseCase,
                      SetRoomInPrefs setRoomInPrefsUseCase, GetRoomInPrefs getRoomInPrefsUseCase,
                      ClearRoomInPrefs clearRoomInPrefsUseCase) {
        this.uploadRoomUseCase = uploadRoomUseCase;
        this.editRoomUseCase = editRoomUseCase;
        this.setRoomInPrefsUseCase = setRoomInPrefsUseCase;
        this.getRoomInPrefsUseCase = getRoomInPrefsUseCase;
        this.clearRoomInPrefsUseCase = clearRoomInPrefsUseCase;
    }

    @Override
    public void attachView(ListRoomContract.View mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        uploadRoomUseCase.clear();
        editRoomUseCase.clear();
        setRoomInPrefsUseCase.clear();
        getRoomInPrefsUseCase.clear();
        clearRoomInPrefsUseCase.clear();
    }

    @Override
    public void getRoomSavedInPrefs() {
        checkViewAttached();
        getRoomInPrefsUseCase.execute(new GetRoomInPrefsObserver());
    }

    @Override
    public void saveRoomInPrefs(RoomDetail room) {
        checkViewAttached();
        setRoomInPrefsUseCase.execute(room, new SetRoomInPrefsObserver());
    }

    @Override
    public void clearRoomSavedInPrefs() {
        checkViewAttached();
        clearRoomInPrefsUseCase.execute(new ClearRoomInPrefsObserver());
    }

    @Override
    public void uploadImage(String imageURI) {

    }

    @Override
    public void uploadRoom(RoomDetail room) {
        checkViewAttached();
        uploadRoomUseCase.execute(room, new UploadRoomObserver());
    }

    @Override
    public void editRoom(RoomDetail room) {
        checkViewAttached();
        editRoomUseCase.execute(room, new EditRoomObserver());
    }

    private final class UploadPictureObserver extends DefaultObserver<Picture> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in uploading the picture data process in list room");
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(Picture picture) {
            getView().hideLoadingImage();
            getView().showImageLoaded(picture);
        }
    }

    private final class UploadRoomObserver extends DefaultObserver<RoomDetail> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in uploading the room data process in list room");
            getView().sendListRoomFailureEvent();
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(RoomDetail roomDetail) {
            getView().sendListRoomSuccessEvent();
            getView().roomUploadSuccessful(roomDetail);
        }
    }

    private final class EditRoomObserver extends DefaultObserver<RoomDetail> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in editing the room data process in list room");
            getView().sendListRoomFailureEvent();
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(RoomDetail roomDetail) {
            getView().sendListRoomSuccessEvent();
            getView().roomEditSuccessful(roomDetail);
        }
    }

    private final class SetRoomInPrefsObserver extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            getView().roomSaveSuccessful();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in saving the room data process in prefs in list room");
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }
    }

    private final class GetRoomInPrefsObserver extends DefaultObserver<RoomDetail> {

        @Override
        public void onComplete() {
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in getting the room data process in prefs in list room");
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }

        @Override
        public void onNext(RoomDetail roomDetail) {
            if (roomDetail != null)
                getView().populateRoomSavedInPrefs(roomDetail);
            else
                getView().populateRoomInitialState();
        }
    }

    private final class ClearRoomInPrefsObserver extends DefaultCompletableObserver {

        @Override
        public void onComplete() {
            getView().roomClearSuccessful();
        }

        @Override
        public void onError(Throwable e) {
            Timber.e(e, "There was an error in clearing the room data process in prefs in list room");
            getView().hideLoadingImage();
            getView().hideLoading();
            getView().showError(
                    ErrorMessageFactory.create(getView().context(),
                            new DefaultErrorBundle((Exception) e).getException()));
            getView().showRetry();
        }
    }

}
