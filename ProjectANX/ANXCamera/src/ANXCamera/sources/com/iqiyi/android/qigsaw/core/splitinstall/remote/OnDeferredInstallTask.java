package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import java.util.List;

final class OnDeferredInstallTask extends DefaultTask {
    private final List mModuleNames;

    OnDeferredInstallTask(ISplitInstallServiceCallback iSplitInstallServiceCallback, List list) {
        super(iSplitInstallServiceCallback);
        this.mModuleNames = list;
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.deferredInstall(this.mModuleNames, this);
    }

    public void onDeferredInstall(Bundle bundle) {
        super.onDeferredInstall(bundle);
        try {
            this.mCallback.onDeferredInstall(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
