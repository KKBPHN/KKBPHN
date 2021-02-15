package com.google.android.play.core.remote;

import android.os.IBinder.DeathRecipient;

final class DeathRecipientImpl implements DeathRecipient {
    private final RemoteManager mRemoteManager;

    DeathRecipientImpl(RemoteManager remoteManager) {
        this.mRemoteManager = remoteManager;
    }

    public void binderDied() {
        this.mRemoteManager.reportBinderDeath();
    }
}
