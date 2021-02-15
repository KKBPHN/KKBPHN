package com.iqiyi.android.qigsaw.core.splitinstall;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import java.util.List;

final class DeferredDownloadCallback implements DownloadCallback {
    private final List splitInfoList;
    private final SplitInstaller splitInstaller;

    DeferredDownloadCallback(SplitInstaller splitInstaller2, List list) {
        this.splitInfoList = list;
        this.splitInstaller = splitInstaller2;
    }

    public void onCanceled() {
    }

    public void onCanceling() {
    }

    public void onCompleted() {
        SplitBackgroundExecutor.getExecutor().execute(new SplitDeferredInstallTask(this.splitInstaller, this.splitInfoList));
    }

    public void onError(int i) {
    }

    public void onProgress(long j) {
    }

    public void onStart() {
    }
}
