package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import java.util.List;

final class OnStartInstallTask extends DefaultTask {
    private final List mModuleNames;

    OnStartInstallTask(ISplitInstallServiceCallback iSplitInstallServiceCallback, List list) {
        super(iSplitInstallServiceCallback);
        this.mModuleNames = list;
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.startInstall(this.mModuleNames, this);
    }

    public void onStartInstall(int i, Bundle bundle) {
        super.onStartInstall(i, bundle);
        try {
            this.mCallback.onStartInstall(i, bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
