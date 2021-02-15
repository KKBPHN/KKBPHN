package com.iqiyi.android.qigsaw.core.splitinstall;

import android.app.PendingIntent;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

final class SplitInstallInternalSessionState {
    private long bytesDownloaded;
    final List downloadRequests;
    private int errorCode;
    private final List moduleNames;
    final List needInstalledSplits;
    private int sessionId;
    private List splitFileIntents;
    private int status;
    private long totalBytesToDownload;
    private PendingIntent userConfirmationIntent;

    SplitInstallInternalSessionState(int i, List list, List list2, List list3) {
        this.sessionId = i;
        this.moduleNames = list;
        this.needInstalledSplits = list2;
        this.downloadRequests = list3;
    }

    static Bundle transform2Bundle(SplitInstallInternalSessionState splitInstallInternalSessionState) {
        Bundle bundle = new Bundle();
        bundle.putInt("session_id", splitInstallInternalSessionState.sessionId());
        bundle.putInt("status", splitInstallInternalSessionState.status());
        bundle.putInt("error_code", splitInstallInternalSessionState.errorCode);
        bundle.putLong("total_bytes_to_download", splitInstallInternalSessionState.totalBytesToDownload);
        bundle.putLong("bytes_downloaded", splitInstallInternalSessionState.bytesDownloaded);
        bundle.putStringArrayList("module_names", (ArrayList) splitInstallInternalSessionState.moduleNames());
        bundle.putParcelable("user_confirmation_intent", splitInstallInternalSessionState.userConfirmationIntent);
        bundle.putParcelableArrayList("split_file_intents", (ArrayList) splitInstallInternalSessionState.splitFileIntents);
        return bundle;
    }

    /* access modifiers changed from: 0000 */
    public List moduleNames() {
        return this.moduleNames;
    }

    /* access modifiers changed from: 0000 */
    public int sessionId() {
        return this.sessionId;
    }

    /* access modifiers changed from: 0000 */
    public void setBytesDownloaded(long j) {
        if (this.bytesDownloaded != j) {
            this.bytesDownloaded = j;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setErrorCode(int i) {
        this.errorCode = i;
    }

    /* access modifiers changed from: 0000 */
    public void setSessionId(int i) {
        this.sessionId = i;
    }

    /* access modifiers changed from: 0000 */
    public void setSplitFileIntents(List list) {
        this.splitFileIntents = list;
    }

    /* access modifiers changed from: 0000 */
    public void setStatus(int i) {
        if (this.status != i) {
            this.status = i;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setTotalBytesToDownload(long j) {
        this.totalBytesToDownload = j;
    }

    /* access modifiers changed from: 0000 */
    public void setUserConfirmationIntent(PendingIntent pendingIntent) {
        this.userConfirmationIntent = pendingIntent;
    }

    /* access modifiers changed from: 0000 */
    public int status() {
        return this.status;
    }
}
