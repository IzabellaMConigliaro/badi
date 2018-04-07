/*
 * File: Presenter.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.presentation.base;

/**
 * Every presenter in the app must either implement this interface or extend BasePresenter
 * indicating the BaseView type that wants to be attached with.
 */
public interface Presenter<V extends BaseView> {

    void attachView(V baseView);

    void detachView();
}
