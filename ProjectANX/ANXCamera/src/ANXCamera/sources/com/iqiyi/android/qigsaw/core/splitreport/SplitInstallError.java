package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

public final class SplitInstallError extends SplitBriefInfo {
    public static final int APK_FILE_ILLEGAL = -11;
    public static final int CLASSLOADER_CREATE_FAILED = -17;
    public static final int DEX_EXTRACT_FAILED = -14;
    public static final int DEX_OAT_FAILED = -18;
    public static final int LIB_EXTRACT_FAILED = -15;
    public static final int MARK_CREATE_FAILED = -16;
    public static final int MD5_ERROR = -13;
    public static final int SIGNATURE_MISMATCH = -12;
    public final Throwable cause;
    public final int errorCode;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public SplitInstallError(SplitBriefInfo splitBriefInfo, int i, Throwable th) {
        super(splitBriefInfo.splitName, splitBriefInfo.version, splitBriefInfo.builtIn);
        this.errorCode = i;
        this.cause = th;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"splitName\":\"");
        sb.append(this.splitName);
        sb.append("\",\"version\":\"");
        sb.append(this.version);
        sb.append("\",\"builtIn\":");
        sb.append(this.builtIn);
        sb.append("\",errorCode\":");
        sb.append(this.errorCode);
        sb.append("\",errorMsg\":\"");
        sb.append(this.cause.getMessage());
        sb.append("\"}");
        return sb.toString();
    }
}
