package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;

final class OnCancelInstallTask extends DefaultTask {
    private final int mSessionId;

    OnCancelInstallTask(ISplitInstallServiceCallback iSplitInstallServiceCallback, int i) {
        super(iSplitInstallServiceCallback);
        this.mSessionId = i;
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.cancelInstall(this.mSessionId, this);
    }

    public void onCancelInstall(int i, Bundle bundle) {
        super.onCancelInstall(i, bundle);
        try {
            this.mCallback.onCancelInstall(i, bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
