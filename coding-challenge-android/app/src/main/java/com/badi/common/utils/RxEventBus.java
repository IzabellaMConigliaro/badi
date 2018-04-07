/*
 * File: RxEventBus.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * A simple event bus built with RxJava
 */
@Singleton
public class RxEventBus {

    private final PublishSubject<Object> busSubject;

    @Inject
    public RxEventBus() {
        busSubject = PublishSubject.create();
    }

    /**
     * Posts an object (usually an Event) to the bus
     */
    public void post(Object event) {
        busSubject.onNext(event);
    }

    /**
     * Observable that will emmit everything posted to the event bus.
     */
    public Observable<Object> observable() {
        return busSubject;
    }

    /**
     * Observable that only emits events of a specific class.
     * Use this if you only want to subscribe to one type of events.
     */
    public <T> Observable<T> filteredObservable(final Class<T> eventClass) {
        return busSubject.ofType(eventClass);
    }
}
