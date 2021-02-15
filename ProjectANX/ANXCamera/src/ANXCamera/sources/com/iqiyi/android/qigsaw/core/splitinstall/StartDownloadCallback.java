package com.iqiyi.android.qigsaw.core.splitinstall;

import com.iqiyi.android.qigsaw.core.splitdownload.DownloadCallback;
import java.util.List;

final class StartDownloadCallback implements DownloadCallback {
    private final SplitSessionInstaller installer;
    private final int sessionId;
    private final SplitInstallSessionManager sessionManager;
    private final SplitInstallInternalSessionState sessionState;
    private final List splitInfoList;

    StartDownloadCallback(SplitInstaller splitInstaller, int i, SplitInstallSessionManager splitInstallSessionManager, List list) {
        this.sessionId = i;
        this.sessionManager = splitInstallSessionManager;
        this.installer = new SplitSessionInstallerImpl(splitInstaller, splitInstallSessionManager, SplitBackgroundExecutor.getExecutor());
        this.splitInfoList = list;
        this.sessionState = splitInstallSessionManager.getSessionState(i);
    }

    private void broadcastSessionStatusChange() {
        this.sessionManager.emitSessionState(this.sessionState);
    }

    private void onInstall() {
        this.installer.install(this.sessionId, this.splitInfoList);
    }

    public void onCanceled() {
        this.sessionManager.changeSessionState(this.sessionId, 7);
        broadcastSessionStatusChange();
    }

    public void onCanceling() {
        this.sessionManager.changeSessionState(this.sessionId, 9);
        broadcastSessionStatusChange();
    }

    public void onCompleted() {
        this.sessionManager.changeSessionState(this.sessionId, 3);
        broadcastSessionStatusChange();
        onInstall();
    }

    public void onError(int i) {
        this.sessionState.setErrorCode(-10);
        this.sessionManager.changeSessionState(this.sessionId, 6);
        broadcastSessionStatusChange();
    }

    public void onProgress(long j) {
        this.sessionState.setBytesDownloaded(j);
        this.sessionManager.changeSessionState(this.sessionId, 2);
        broadcastSessionStatusChange();
    }

    public void onStart() {
        this.sessionManager.changeSessionState(this.sessionId, 2);
        broadcastSessionStatusChange();
    }
}
