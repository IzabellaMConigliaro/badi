/*
 * File: ApplicationContext.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.di;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A qualifier annotation to identify application context dependency
 */
@Qualifier
@Retention(RUNTIME)
public @interface ApplicationContext {
}