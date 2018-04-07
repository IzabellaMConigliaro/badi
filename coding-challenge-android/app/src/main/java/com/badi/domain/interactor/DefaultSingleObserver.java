/*
 * File: DefaultSingleObserver.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * Default {@link DisposableSingleObserver} base class to be used whenever you want default error handling.
 */
public class DefaultSingleObserver<T> extends DisposableSingleObserver<T> {

    @Override
    public void onSuccess(@NonNull T t) {
        // no-op by default.
    }

    @Override public void onError(Throwable exception) {
        // no-op by default.
    }
}
