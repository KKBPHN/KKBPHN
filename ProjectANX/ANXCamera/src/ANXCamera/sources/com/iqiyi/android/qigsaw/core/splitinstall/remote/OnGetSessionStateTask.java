package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;

class OnGetSessionStateTask extends DefaultTask {
    private final int mSessionId;

    OnGetSessionStateTask(ISplitInstallServiceCallback iSplitInstallServiceCallback, int i) {
        super(iSplitInstallServiceCallback);
        this.mSessionId = i;
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.getSessionState(this.mSessionId, this);
    }

    public void onGetSession(int i, Bundle bundle) {
        super.onGetSession(i, bundle);
        try {
            this.mCallback.onGetSession(i, bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
