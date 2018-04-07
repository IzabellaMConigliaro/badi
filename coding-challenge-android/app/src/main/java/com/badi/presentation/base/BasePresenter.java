/*
 * File: BasePresenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.base;

import com.badi.common.exception.ErrorBundle;
import com.badi.common.exception.ErrorMessageFactory;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the baseView that
 * can be accessed from the children classes by calling getView().
 */
public class BasePresenter<T extends BaseView> implements Presenter<T> {

    private T baseView;

    @Override
    public void attachView(T baseView) {
        this.baseView = baseView;
    }

    @Override
    public void detachView() {
        baseView = null;
    }

    public boolean isViewAttached() {
        return baseView != null;
    }

    public T getView() {
        return baseView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new BaseViewNotAttachedException();
    }

    public void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(baseView.context(), errorBundle.getException());
        baseView.showError(errorMessage);
    }

    public static class BaseViewNotAttachedException extends RuntimeException {
        public BaseViewNotAttachedException() {
            super("Please call Presenter.attachView(BaseView) before requesting data to the Presenter");
        }
    }
}
