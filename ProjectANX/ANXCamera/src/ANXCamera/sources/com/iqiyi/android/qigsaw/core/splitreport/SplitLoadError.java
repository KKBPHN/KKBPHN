package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

public class SplitLoadError extends SplitBriefInfo {
    public static final int ACTIVATE_APPLICATION_FAILED = -24;
    public static final int ACTIVATE_PROVIDERS_FAILED = -25;
    public static final int CREATE_CLASSLOADER_FAILED = -27;
    public static final int INTERRUPTED_ERROR = -26;
    public static final int LOAD_DEX_FAILED = -23;
    public static final int LOAD_LIB_FAILED = -22;
    public static final int LOAD_RES_FAILED = -21;
    public final Throwable cause;
    public final int errorCode;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public SplitLoadError(SplitBriefInfo splitBriefInfo, int i, Throwable th) {
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
