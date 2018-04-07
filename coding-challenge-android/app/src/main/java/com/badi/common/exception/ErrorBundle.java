/*
 * File: ErrorBundle.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.exception;

/**
 * Interface to represent a wrapper around an {@link java.lang.Exception} to manage errors.
 */
public interface ErrorBundle {
    Exception getException();

    String getErrorMessage();
}
