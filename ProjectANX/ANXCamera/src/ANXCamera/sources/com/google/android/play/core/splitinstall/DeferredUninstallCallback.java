package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;

final class DeferredUninstallCallback extends SplitInstallServiceCallbackImpl {
    DeferredUninstallCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    public void onDeferredUninstall(Bundle bundle) {
        super.onDeferredUninstall(bundle);
        this.mTask.setResult(null);
    }
}
