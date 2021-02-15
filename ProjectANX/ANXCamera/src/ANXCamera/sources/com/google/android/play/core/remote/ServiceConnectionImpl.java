package com.google.android.play.core.remote;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

final class ServiceConnectionImpl implements ServiceConnection {
    final RemoteManager mRemoteManager;

    ServiceConnectionImpl(RemoteManager remoteManager) {
        this.mRemoteManager = remoteManager;
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mRemoteManager.mPlayCore.info("ServiceConnectionImpl.onServiceConnected(%s)", componentName);
        this.mRemoteManager.post(new ServiceConnectedTask(this, iBinder));
    }

    public void onServiceDisconnected(ComponentName componentName) {
        this.mRemoteManager.mPlayCore.info("ServiceConnectionImpl.onServiceDisconnected(%s)", componentName);
        this.mRemoteManager.post(new ServiceDisconnectedTask(this));
    }
}
