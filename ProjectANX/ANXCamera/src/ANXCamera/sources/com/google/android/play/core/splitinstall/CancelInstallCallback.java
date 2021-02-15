package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;

final class CancelInstallCallback extends SplitInstallServiceCallbackImpl {
    CancelInstallCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    public void onCancelInstall(int i, Bundle bundle) {
        super.onCancelInstall(i, bundle);
        this.mTask.setResult(null);
    }
}
