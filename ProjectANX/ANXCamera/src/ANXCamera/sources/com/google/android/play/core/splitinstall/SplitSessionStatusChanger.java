package com.google.android.play.core.splitinstall;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitSessionStatusChanger {
    final SplitInstallListenerRegistry mRegistry;
    final SplitInstallSessionState sessionState;

    SplitSessionStatusChanger(SplitInstallListenerRegistry splitInstallListenerRegistry, SplitInstallSessionState splitInstallSessionState) {
        this.mRegistry = splitInstallListenerRegistry;
        this.sessionState = splitInstallSessionState;
    }

    public void changeStatus(int i) {
        this.mRegistry.mMainHandler.post(new ChangeSessionStatusWorker(this, i));
    }

    public void changeStatus(int i, int i2) {
        this.mRegistry.mMainHandler.post(new ChangeSessionStatusWorker(this, i, i2));
    }
}
