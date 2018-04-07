/*
 * File: SingleUseCase.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.domain.interactor;

import com.badi.common.executor.PostExecutionThread;
import com.badi.common.executor.ThreadExecutor;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


/**
 * Abstract class for a Use Case (Interactor in terms of Clean Architecture).
 * This interface represents a execution unit for different use cases (this means any single use case
 * in the application should implement this contract).
 *
 * By convention each Single UseCase implementation will return the result using a {@link DisposableSingleObserver}
 * that will execute its job in a background thread and will post the result in the UI thread.
 */
public abstract class SingleUseCase {

    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;
    private final CompositeDisposable disposables;

    protected SingleUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.disposables = new CompositeDisposable();
    }

    /**
     * Builds an {@link Single} which will be used when executing the current {@link SingleUseCase}.
     */
    protected abstract Single buildSingleUseCaseObservable();

    /**
     * Executes the current use case.
     *
     * @param observer {@link DisposableSingleObserver} which will be listening to the observable build
     * by {@link #buildSingleUseCaseObservable()} ()} method.
     */
    @SuppressWarnings("unchecked")
    public void execute(DisposableSingleObserver observer) {
        final Single<?> observable = this.buildSingleUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(postExecutionThread.getScheduler());
        addDisposable(observable.subscribeWith(observer));
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }

    /**
     * Clear and dispose from current {@link CompositeDisposable}.
     */
    public void clear() {
        disposables.clear();
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        if (disposable != null) disposables.add(disposable);
    }
}
