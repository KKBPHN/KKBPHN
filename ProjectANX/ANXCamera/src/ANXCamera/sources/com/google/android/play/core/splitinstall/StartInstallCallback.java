package com.google.android.play.core.splitinstall;

import android.os.Bundle;
import com.google.android.play.core.tasks.TaskWrapper;

final class StartInstallCallback extends SplitInstallServiceCallbackImpl {
    StartInstallCallback(SplitInstallService splitInstallService, TaskWrapper taskWrapper) {
        super(splitInstallService, taskWrapper);
    }

    public void onStartInstall(int i, Bundle bundle) {
        super.onStartInstall(i, bundle);
        this.mTask.setResult(Integer.valueOf(i));
    }
}
