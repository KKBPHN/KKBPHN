package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import java.util.List;

final class OnDeferredUninstallTask extends DefaultTask {
    private final List mModuleNames;

    OnDeferredUninstallTask(ISplitInstallServiceCallback iSplitInstallServiceCallback, List list) {
        super(iSplitInstallServiceCallback);
        this.mModuleNames = list;
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.deferredUninstall(this.mModuleNames, this);
    }

    public void onDeferredUninstall(Bundle bundle) {
        super.onDeferredUninstall(bundle);
        try {
            this.mCallback.onDeferredUninstall(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
