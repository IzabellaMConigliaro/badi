/*
 * File: ThreadExecutor.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.executor;

import java.util.concurrent.Executor;

/**
 * Executor implementation can be based on different frameworks or techniques of asynchronous
 * execution, but every implementation will execute UseCases out of the UI thread.
 */
public interface ThreadExecutor extends Executor { }