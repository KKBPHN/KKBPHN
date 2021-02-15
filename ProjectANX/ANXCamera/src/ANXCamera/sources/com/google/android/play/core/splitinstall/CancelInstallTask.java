package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.remote.RemoteTask;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceProxy;
import com.google.android.play.core.tasks.TaskWrapper;

final class CancelInstallTask extends RemoteTask {
    private final SplitInstallService mSplitInstallService;
    private final TaskWrapper mTask;
    private final int sessionId;

    CancelInstallTask(SplitInstallService splitInstallService, TaskWrapper taskWrapper, int i, TaskWrapper taskWrapper2) {
        super(taskWrapper2);
        this.mSplitInstallService = splitInstallService;
        this.mTask = taskWrapper;
        this.sessionId = i;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        try {
            ((ISplitInstallServiceProxy) this.mSplitInstallService.mSplitRemoteManager.getIInterface()).cancelInstall(this.mSplitInstallService.mPackageName, this.sessionId, SplitInstallService.wrapVersionCode(), new CancelInstallCallback(this.mSplitInstallService, this.mTask));
        } catch (RemoteException e) {
            SplitInstallService.playCore.error(e, "cancelInstall(%d)", Integer.valueOf(this.sessionId));
            this.mTask.setException(new RuntimeException(e));
        }
    }
}
