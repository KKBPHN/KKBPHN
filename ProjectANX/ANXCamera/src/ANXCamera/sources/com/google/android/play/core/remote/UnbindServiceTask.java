package com.google.android.play.core.remote;

final class UnbindServiceTask extends RemoteTask {
    private final RemoteManager mRemoteManager;

    UnbindServiceTask(RemoteManager remoteManager) {
        this.mRemoteManager = remoteManager;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        RemoteManager remoteManager = this.mRemoteManager;
        if (remoteManager.mIInterface != null) {
            remoteManager.mContext.unbindService(remoteManager.mServiceConnection);
            RemoteManager remoteManager2 = this.mRemoteManager;
            remoteManager2.mBindingService = false;
            remoteManager2.mIInterface = null;
            remoteManager2.mServiceConnection = null;
        }
    }
}
