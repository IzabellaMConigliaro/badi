/*
 * File: MapStateListener.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.common.utils.map;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;

import java.util.Timer;
import java.util.TimerTask;

public abstract class MapStateListener {

    private boolean mapTouched = false;
    private boolean mapSettled = false;
    private Timer timer;
    private static final int SETTLE_TIME = 500;

    private GoogleMap map;
    private CameraPosition lastPosition;
    private Activity activity;

    public MapStateListener(GoogleMap map, TouchableMapFragment touchableMapFragment, Activity activity) {
        this.map = map;
        this.activity = activity;

        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                unsettleMap();
                if (!mapTouched) {
                    runSettleTimer();
                }
            }
        });

        touchableMapFragment.setTouchListener(new TouchableWrapper.OnTouchListener() {
            @Override
            public void onTouch() {
                touchMap();
                unsettleMap();
            }

            @Override
            public void onRelease() {
                releaseMap();
                runSettleTimer();
            }
        });
    }

    private void updateLastPosition() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lastPosition = MapStateListener.this.map.getCameraPosition();
            }
        });
    }

    private void runSettleTimer() {
        updateLastPosition();

        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CameraPosition currentPosition = MapStateListener.this.map.getCameraPosition();
                        if (currentPosition.equals(lastPosition)) {
                            settleMap();
                        }
                    }
                });
            }
        }, SETTLE_TIME);
    }

    private synchronized void releaseMap() {
        if (mapTouched) {
            mapTouched = false;
            onMapReleased();
        }
    }

    private void touchMap() {
        if (!mapTouched) {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            mapTouched = true;
            onMapTouched();
        }
    }

    public void unsettleMap() {
        if (mapSettled) {
            if (timer != null) {
                timer.cancel();
                timer.purge();
            }
            mapSettled = false;
            lastPosition = null;
            onMapUnsettled();
        }
    }

    public void settleMap() {
        if (!mapSettled) {
            mapSettled = true;
            onMapSettled();
        }
    }

    public abstract void onMapTouched();
    public abstract void onMapReleased();
    public abstract void onMapUnsettled();
    public abstract void onMapSettled();
}
