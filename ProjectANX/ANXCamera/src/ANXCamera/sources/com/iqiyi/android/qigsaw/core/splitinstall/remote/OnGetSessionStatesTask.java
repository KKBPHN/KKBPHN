package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import java.util.List;

final class OnGetSessionStatesTask extends DefaultTask {
    OnGetSessionStatesTask(ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        super(iSplitInstallServiceCallback);
    }

    /* access modifiers changed from: 0000 */
    public void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor) {
        splitInstallSupervisor.getSessionStates(this);
    }

    public void onGetSessionStates(List list) {
        super.onGetSessionStates(list);
        try {
            this.mCallback.onGetSessionStates(list);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
