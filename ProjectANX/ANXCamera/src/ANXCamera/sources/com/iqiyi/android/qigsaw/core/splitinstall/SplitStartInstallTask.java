package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Intent;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.splitreport.SplitInstallError;
import java.util.ArrayList;
import java.util.List;

final class SplitStartInstallTask extends SplitInstallTask {
    private final SplitInstallSessionManager mSessionManager;
    private final SplitInstallInternalSessionState mSessionState;

    SplitStartInstallTask(int i, SplitInstaller splitInstaller, SplitInstallSessionManager splitInstallSessionManager, List list) {
        super(splitInstaller, list);
        this.mSessionState = splitInstallSessionManager.getSessionState(i);
        this.mSessionManager = splitInstallSessionManager;
    }

    private void emitSessionStatus() {
        this.mSessionManager.emitSessionState(this.mSessionState);
    }

    /* access modifiers changed from: 0000 */
    public boolean isStartInstallOperation() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<com.iqiyi.android.qigsaw.core.splitinstall.SplitInstaller$InstallResult>, for r6v0, types: [java.util.List, java.util.List<com.iqiyi.android.qigsaw.core.splitinstall.SplitInstaller$InstallResult>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onInstallCompleted(List<InstallResult> list) {
        super.onInstallCompleted(list);
        ArrayList arrayList = new ArrayList(list.size());
        for (InstallResult installResult : list) {
            Intent intent = new Intent();
            List list2 = installResult.addedDexPaths;
            if (list2 != null) {
                intent.putStringArrayListExtra(SplitConstants.KEY_ADDED_DEX, (ArrayList) list2);
            }
            intent.putExtra(SplitConstants.KEY_APK, installResult.apkFile.getAbsolutePath());
            intent.putExtra(SplitConstants.KET_NAME, installResult.splitName);
            arrayList.add(intent);
        }
        this.mSessionState.setSplitFileIntents(arrayList);
        this.mSessionManager.changeSessionState(this.mSessionState.sessionId(), 10);
        emitSessionStatus();
    }

    /* access modifiers changed from: 0000 */
    public void onInstallFailed(List list) {
        super.onInstallFailed(list);
        this.mSessionState.setErrorCode(((SplitInstallError) list.get(0)).errorCode);
        this.mSessionManager.changeSessionState(this.mSessionState.sessionId(), 6);
        emitSessionStatus();
    }

    /* access modifiers changed from: protected */
    public void onPreInstall() {
        super.onPreInstall();
        this.mSessionManager.changeSessionState(this.mSessionState.sessionId(), 4);
        emitSessionStatus();
    }
}
