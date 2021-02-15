package com.google.android.play.core.remote;

import android.os.IBinder;
import android.os.IInterface;

final class ServiceConnectedTask extends RemoteTask {
    private final IBinder mService;
    private final ServiceConnectionImpl mServiceConnection;

    ServiceConnectedTask(ServiceConnectionImpl serviceConnectionImpl, IBinder iBinder) {
        this.mServiceConnection = serviceConnectionImpl;
        this.mService = iBinder;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        RemoteManager remoteManager = this.mServiceConnection.mRemoteManager;
        remoteManager.mIInterface = (IInterface) remoteManager.mRemote.asInterface(this.mService);
        this.mServiceConnection.mRemoteManager.linkToDeath();
        RemoteManager remoteManager2 = this.mServiceConnection.mRemoteManager;
        remoteManager2.mBindingService = false;
        for (Runnable run : remoteManager2.mPendingTasks) {
            run.run();
        }
        this.mServiceConnection.mRemoteManager.mPendingTasks.clear();
    }
}
