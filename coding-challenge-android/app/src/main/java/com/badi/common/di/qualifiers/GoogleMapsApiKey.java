package com.badi.common.di.qualifiers;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A qualifier annotation to identify the Google Maps Api key
 */
@Qualifier
@Retention(RUNTIME)
public @interface GoogleMapsApiKey {
}