package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.remote.RemoteTask;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceProxy;
import com.google.android.play.core.tasks.TaskWrapper;

final class GetSessionStateTask extends RemoteTask {
    private final SplitInstallService mSplitInstallService;
    private final TaskWrapper mTask;
    private final int sessionId;

    GetSessionStateTask(SplitInstallService splitInstallService, TaskWrapper taskWrapper, int i, TaskWrapper taskWrapper2) {
        super(taskWrapper);
        this.mSplitInstallService = splitInstallService;
        this.sessionId = i;
        this.mTask = taskWrapper2;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        try {
            ((ISplitInstallServiceProxy) this.mSplitInstallService.mSplitRemoteManager.getIInterface()).getSessionState(this.mSplitInstallService.mPackageName, this.sessionId, new GetSessionStateCallback(this.mSplitInstallService, this.mTask));
        } catch (RemoteException e) {
            SplitInstallService.playCore.error(e, "getSessionState(%d)", Integer.valueOf(this.sessionId));
            this.mTask.setException(new RuntimeException(e));
        }
    }
}
