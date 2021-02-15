package com.iqiyi.android.qigsaw.core.splitreport;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@Keep
public class SplitBriefInfo {
    public static final int ALREADY_INSTALLED = 2;
    public static final int FIRST_INSTALLED = 1;
    public static final int UNKNOWN = 0;
    public final boolean builtIn;
    private int installFlag = 0;
    public final String splitName;
    public final String version;

    @RestrictTo({Scope.LIBRARY_GROUP})
    public SplitBriefInfo(@NonNull String str, @NonNull String str2, boolean z) {
        this.splitName = str;
        this.version = str2;
        this.builtIn = z;
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof SplitBriefInfo)) {
            return false;
        }
        SplitBriefInfo splitBriefInfo = (SplitBriefInfo) obj;
        if (!this.splitName.equals(splitBriefInfo.splitName) || !this.version.equals(splitBriefInfo.version) || this.builtIn != splitBriefInfo.builtIn) {
            return super.equals(obj);
        }
        return true;
    }

    public int getInstallFlag() {
        return this.installFlag;
    }

    @RestrictTo({Scope.LIBRARY_GROUP})
    public void setInstallFlag(int i) {
        this.installFlag = i;
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
        sb.append("}");
        return sb.toString();
    }
}
