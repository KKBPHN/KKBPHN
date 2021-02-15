package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;
import com.bumptech.glide.util.Preconditions;

final class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private static final String TAG = "ConnectivityMonitor";
    private final BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {
        public void onReceive(@NonNull Context context, Intent intent) {
            DefaultConnectivityMonitor defaultConnectivityMonitor = DefaultConnectivityMonitor.this;
            boolean z = defaultConnectivityMonitor.isConnected;
            defaultConnectivityMonitor.isConnected = defaultConnectivityMonitor.isConnected(context);
            if (z != DefaultConnectivityMonitor.this.isConnected) {
                String str = DefaultConnectivityMonitor.TAG;
                if (Log.isLoggable(str, 3)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("connectivity changed, isConnected: ");
                    sb.append(DefaultConnectivityMonitor.this.isConnected);
                    Log.d(str, sb.toString());
                }
                DefaultConnectivityMonitor defaultConnectivityMonitor2 = DefaultConnectivityMonitor.this;
                defaultConnectivityMonitor2.listener.onConnectivityChanged(defaultConnectivityMonitor2.isConnected);
            }
        }
    };
    private final Context context;
    boolean isConnected;
    private boolean isRegistered;
    final ConnectivityListener listener;

    DefaultConnectivityMonitor(@NonNull Context context2, @NonNull ConnectivityListener connectivityListener) {
        this.context = context2.getApplicationContext();
        this.listener = connectivityListener;
    }

    private void register() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            try {
                this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
                this.isRegistered = true;
            } catch (SecurityException e) {
                String str = TAG;
                if (Log.isLoggable(str, 5)) {
                    Log.w(str, "Failed to register", e);
                }
            }
        }
    }

    private void unregister() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }

    /* access modifiers changed from: 0000 */
    @SuppressLint({"MissingPermission"})
    public boolean isConnected(@NonNull Context context2) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context2.getSystemService("connectivity");
        Preconditions.checkNotNull(connectivityManager);
        boolean z = true;
        try {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                z = false;
            }
            return z;
        } catch (RuntimeException e) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                Log.w(str, "Failed to determine connectivity status when connectivity changed", e);
            }
            return true;
        }
    }

    public void onDestroy() {
    }

    public void onStart() {
        register();
    }

    public void onStop() {
        unregister();
    }
}
