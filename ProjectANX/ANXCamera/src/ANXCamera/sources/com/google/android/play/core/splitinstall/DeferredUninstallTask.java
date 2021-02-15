package com.google.android.play.core.splitinstall;

import android.os.RemoteException;
import com.google.android.play.core.remote.RemoteTask;
import com.google.android.play.core.splitinstall.protocol.ISplitInstallServiceProxy;
import com.google.android.play.core.tasks.TaskWrapper;
import java.util.List;

final class DeferredUninstallTask extends RemoteTask {
    private final SplitInstallService mSplitInstallService;
    private final TaskWrapper mTask;
    private final List moduleNames;

    DeferredUninstallTask(SplitInstallService splitInstallService, TaskWrapper taskWrapper, List list, TaskWrapper taskWrapper2) {
        super(taskWrapper);
        this.mSplitInstallService = splitInstallService;
        this.moduleNames = list;
        this.mTask = taskWrapper2;
    }

    /* access modifiers changed from: protected */
    public void execute() {
        try {
            ((ISplitInstallServiceProxy) this.mSplitInstallService.mSplitRemoteManager.getIInterface()).deferredUninstall(this.mSplitInstallService.mPackageName, SplitInstallService.wrapModuleNames(this.moduleNames), SplitInstallService.wrapVersionCode(), new DeferredUninstallCallback(this.mSplitInstallService, this.mTask));
        } catch (RemoteException e) {
            SplitInstallService.playCore.error(e, "deferredUninstall(%s)", this.moduleNames);
            this.mTask.setException(new RuntimeException(e));
        }
    }
}
