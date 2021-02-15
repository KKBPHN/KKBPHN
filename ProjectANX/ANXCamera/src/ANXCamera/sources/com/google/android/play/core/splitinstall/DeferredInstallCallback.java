package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;

final class DeferredInstallCallback extends SplitInstallServiceCallbackImpl {
    DeferredInstallCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    public void onDeferredInstall(Bundle bundle) {
        super.onDeferredInstall(bundle);
        this.mTask.setResult(null);
    }
}
