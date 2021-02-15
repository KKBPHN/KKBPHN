package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;

final class GetSessionStateCallback extends SplitInstallServiceCallbackImpl {
    GetSessionStateCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    public void onGetSession(int i, Bundle bundle) {
        super.onGetSession(i, bundle);
        this.mTask.setResult(SplitInstallSessionState.createFrom(bundle));
    }
}
