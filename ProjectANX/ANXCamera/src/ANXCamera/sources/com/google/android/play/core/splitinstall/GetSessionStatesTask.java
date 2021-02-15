package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.remote.RemoteTask;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceProxy;
import com.google.android.play.core.tasks.TaskWrapper;

final class GetSessionStatesTask extends RemoteTask {
    private final SplitInstallService mSplitInstallService;
    private final TaskWrapper mTask;

    GetSessionStatesTask(SplitInstallService splitInstallService, TaskWrapper taskWrapper, TaskWrapper taskWrapper2) {
        super(taskWrapper);
        this.mSplitInstallService = splitInstallService;
        this.mTask = taskWrapper2;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        try {
            ((ISplitInstallServiceProxy) this.mSplitInstallService.mSplitRemoteManager.getIInterface()).getSessionStates(this.mSplitInstallService.mPackageName, new GetSessionStatesCallback(this.mSplitInstallService, this.mTask));
        } catch (RemoteException e) {
            SplitInstallService.playCore.error(e, "getSessionStates", new Object[0]);
            this.mTask.setException(new RuntimeException(e));
        }
    }
}
