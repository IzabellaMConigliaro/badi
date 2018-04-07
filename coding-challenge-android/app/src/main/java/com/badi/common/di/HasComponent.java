/*
 * File: HasComponent.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di;

/**
 * Interface representing a contract for clients that contains a component for dependency injection.
 */
public interface HasComponent<C> {
    C getComponent();
}
