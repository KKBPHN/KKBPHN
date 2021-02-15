package com.google.android.play.core.remote;

final class ServiceDisconnectedTask extends RemoteTask {
    private final ServiceConnectionImpl mServiceConnection;

    ServiceDisconnectedTask(ServiceConnectionImpl serviceConnectionImpl) {
        this.mServiceConnection = serviceConnectionImpl;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        this.mServiceConnection.mRemoteManager.unlinkToDeath();
        RemoteManager remoteManager = this.mServiceConnection.mRemoteManager;
        remoteManager.mIInterface = null;
        remoteManager.mBindingService = false;
    }
}
