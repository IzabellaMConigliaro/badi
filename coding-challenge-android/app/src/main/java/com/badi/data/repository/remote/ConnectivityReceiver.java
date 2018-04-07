/*
 * File: ConnectivityReceiver.java
 *
 * Created by Guillem Roca Castany.
 * Copyright (c) 2017. Badi. All rights reserved.
 */

package com.badi.data.repository.remote;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ConnectivityReceiver extends BroadcastReceiver {

    ConnectivityInterface connectivityInterface;

    public ConnectivityReceiver(ConnectivityInterface connectivityInterface) {
        this.connectivityInterface = connectivityInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getExtras() != null) {
            final ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo ni = connectivityManager.getActiveNetworkInfo();

            if (ni != null && ni.isConnectedOrConnecting()) {
                Log.i("NetworkStatus", "Network " + ni.getTypeName() + " connected");
                if (connectivityInterface != null) {
                    connectivityInterface.onChangedConnectivity(true);
                }
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                Log.d("NetworkStatus", "There's no network connectivity");
                if (connectivityInterface != null) {
                    connectivityInterface.onChangedConnectivity(false);
                }
            }
        }
    }

    public interface ConnectivityInterface {
        void onChangedConnectivity(Boolean internetON);
    }
}
