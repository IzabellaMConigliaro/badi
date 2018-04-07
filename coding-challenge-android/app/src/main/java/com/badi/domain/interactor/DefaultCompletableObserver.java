/*
 * File: DefaultCompletableObserver.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor;

import io.reactivex.observers.DisposableCompletableObserver;

/**
 * Default {@link DisposableCompletableObserver} base class to be used whenever you want default error handling.
 */
public class DefaultCompletableObserver extends DisposableCompletableObserver {

    @Override public void onComplete() {
        // no-op by default.
    }

    @Override public void onError(Throwable exception) {
        // no-op by default.
    }
}
