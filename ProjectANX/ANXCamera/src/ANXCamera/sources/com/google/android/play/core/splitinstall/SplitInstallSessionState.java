package com.google.android.play.core.splitinstall;

import android.app.PendingIntent;
import android.os.Bundle;
import java.util.List;

public class SplitInstallSessionState {
    private final long bytesDownloaded;
    private final int errorCode;
    private final List moduleNames;
    private final int sessionId;
    List splitFileIntents;
    private final int status;
    private final long totalBytesToDownload;
    private final PendingIntent userConfirmationIntent;

    private SplitInstallSessionState(int i, int i2, int i3, long j, long j2, List list, PendingIntent pendingIntent, List list2) {
        this.sessionId = i;
        this.status = i2;
        this.errorCode = i3;
        this.bytesDownloaded = j;
        this.totalBytesToDownload = j2;
        this.moduleNames = list;
        this.userConfirmationIntent = pendingIntent;
        this.splitFileIntents = list2;
    }

    static SplitInstallSessionState createFrom(Bundle bundle) {
        SplitInstallSessionState splitInstallSessionState = new SplitInstallSessionState(bundle.getInt("session_id"), bundle.getInt("status"), bundle.getInt("error_code"), bundle.getLong("bytes_downloaded"), bundle.getLong("total_bytes_to_download"), bundle.getStringArrayList("module_names"), (PendingIntent) bundle.getParcelable("user_confirmation_intent"), bundle.getParcelableArrayList("split_file_intents"));
        return splitInstallSessionState;
    }

    /* access modifiers changed from: 0000 */
    public final SplitInstallSessionState a(int i) {
        SplitInstallSessionState splitInstallSessionState = new SplitInstallSessionState(sessionId(), i, errorCode(), bytesDownloaded(), totalBytesToDownload(), moduleNames(), resolutionIntent(), this.splitFileIntents);
        return splitInstallSessionState;
    }

    /* access modifiers changed from: 0000 */
    public final SplitInstallSessionState a(int i, int i2) {
        SplitInstallSessionState splitInstallSessionState = new SplitInstallSessionState(sessionId(), i, i2, bytesDownloaded(), totalBytesToDownload(), moduleNames(), resolutionIntent(), this.splitFileIntents);
        return splitInstallSessionState;
    }

    public long bytesDownloaded() {
        return this.bytesDownloaded;
    }

    public int errorCode() {
        return this.errorCode;
    }

    public List moduleNames() {
        return this.moduleNames;
    }

    public final PendingIntent resolutionIntent() {
        return this.userConfirmationIntent;
    }

    public int sessionId() {
        return this.sessionId;
    }

    public int status() {
        return this.status;
    }

    public final String toString() {
        int i = this.sessionId;
        int i2 = this.status;
        int i3 = this.errorCode;
        long j = this.bytesDownloaded;
        long j2 = this.totalBytesToDownload;
        String valueOf = String.valueOf(this.moduleNames);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 183);
        sb.append("SplitInstallSessionState{sessionId=");
        sb.append(i);
        sb.append(", status=");
        sb.append(i2);
        sb.append(", errorCode=");
        sb.append(i3);
        sb.append(", bytesDownloaded=");
        sb.append(j);
        sb.append(",totalBytesToDownload=");
        sb.append(j2);
        sb.append(",moduleNames=");
        sb.append(valueOf);
        sb.append("}");
        return sb.toString();
    }

    public long totalBytesToDownload() {
        return this.totalBytesToDownload;
    }
}
