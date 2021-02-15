package com.iqiyi.android.qigsaw.core.splitinstall.remote;

import android.os.Bundle;
import android.os.RemoteException;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitinstall.SplitApkInstaller;
import com.iqiyi.android.qigsaw.core.splitinstall.protocol.ISplitInstallServiceCallback;
import com.iqiyi.android.qigsaw.core.splitinstall.remote.SplitInstallSupervisor.Callback;
import com.ss.android.vesdk.VEResult;
import java.util.List;

abstract class DefaultTask implements Runnable, Callback {
    private static final String TAG = "Split:DefaultTask";
    private final SplitInstallSupervisor installSupervisor = SplitApkInstaller.getSplitInstallSupervisor();
    final ISplitInstallServiceCallback mCallback;

    DefaultTask(ISplitInstallServiceCallback iSplitInstallServiceCallback) {
        this.mCallback = iSplitInstallServiceCallback;
    }

    public abstract void execute(@NonNull SplitInstallSupervisor splitInstallSupervisor);

    public void onCancelInstall(int i, Bundle bundle) {
    }

    public void onDeferredInstall(Bundle bundle) {
    }

    public void onDeferredUninstall(Bundle bundle) {
    }

    public void onError(Bundle bundle) {
        try {
            this.mCallback.onError(bundle);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void onGetSession(int i, Bundle bundle) {
    }

    public void onGetSessionStates(List list) {
    }

    public void onStartInstall(int i, Bundle bundle) {
    }

    public void run() {
        SplitInstallSupervisor splitInstallSupervisor = this.installSupervisor;
        if (splitInstallSupervisor != null) {
            try {
                execute(splitInstallSupervisor);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            this.mCallback.onError(SplitInstallSupervisor.bundleErrorCode(VEResult.TER_INVALID_CONTEXT));
            SplitLog.w(TAG, "Have you call Qigsaw#onApplicationCreated method?", new Object[0]);
        }
    }
}
