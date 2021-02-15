package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.text.TextUtils;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import java.util.List;

@RestrictTo({Scope.LIBRARY_GROUP})
public class SplitInfo {
    private final String appVersion;
    private final boolean builtIn;
    private final List dependencies;
    private final int dexNumber;
    private final boolean hasLibs;
    private final boolean isMultiDex;
    private final String md5;
    private final int minSdkVersion;
    private final LibInfo primaryLibInfo;
    private final long size;
    private final String splitName;
    private final String splitVersion;
    private final String url;
    private final List workProcesses;

    public class LibInfo {
        /* access modifiers changed from: private */
        public final String abi;
        /* access modifiers changed from: private */
        public final List libs;

        public class Lib {
            /* access modifiers changed from: private */
            public final String md5;
            /* access modifiers changed from: private */
            public final String name;
            private final long size;

            Lib(String str, String str2, long j) {
                this.name = str;
                this.md5 = str2;
                this.size = j;
            }

            public String getMd5() {
                return this.md5;
            }

            public String getName() {
                return this.name;
            }

            public long getSize() {
                return this.size;
            }
        }

        LibInfo(String str, List list) {
            this.abi = str;
            this.libs = list;
        }

        public String getAbi() {
            return this.abi;
        }

        public List getLibs() {
            return this.libs;
        }
    }

    SplitInfo(String str, String str2, String str3, String str4, String str5, long j, boolean z, int i, int i2, List list, List list2, boolean z2, LibInfo libInfo) {
        this.splitName = str;
        this.appVersion = str2;
        this.splitVersion = str3;
        this.url = str4;
        this.md5 = str5;
        this.size = j;
        this.builtIn = z;
        this.minSdkVersion = i;
        this.dexNumber = i2;
        this.workProcesses = list;
        this.dependencies = list2;
        this.hasLibs = z2;
        this.primaryLibInfo = libInfo;
        boolean z3 = true;
        if (i2 <= 1) {
            z3 = false;
        }
        this.isMultiDex = z3;
    }

    private boolean checkLibInfo() {
        LibInfo libInfo = this.primaryLibInfo;
        if (!(libInfo == null || libInfo.libs == null || this.primaryLibInfo.libs.isEmpty())) {
            if (TextUtils.isEmpty(this.primaryLibInfo.abi)) {
                return false;
            }
            for (Lib lib : this.primaryLibInfo.libs) {
                if (TextUtils.isEmpty(lib.name) || TextUtils.isEmpty(lib.md5) || (!lib.name.startsWith("lib") && !lib.name.endsWith(SplitConstants.DOT_SO))) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getAppVersion() {
        return this.appVersion;
    }

    public List getDependencies() {
        return this.dependencies;
    }

    public LibInfo getLibInfo() {
        if (!this.hasLibs || this.primaryLibInfo != null) {
            return this.primaryLibInfo;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("No supported abi for split ");
        sb.append(this.splitName);
        throw new RuntimeException(sb.toString());
    }

    public String getMd5() {
        return this.md5;
    }

    public int getMinSdkVersion() {
        return this.minSdkVersion;
    }

    public long getSize() {
        return this.size;
    }

    public String getSplitName() {
        return this.splitName;
    }

    public String getSplitVersion() {
        return this.splitVersion;
    }

    public String getUrl() {
        return this.url;
    }

    public List getWorkProcesses() {
        return this.workProcesses;
    }

    public boolean hasDex() {
        return this.dexNumber > 0;
    }

    public boolean hasLibs() {
        return this.hasLibs;
    }

    public boolean isBuiltIn() {
        return this.builtIn;
    }

    public boolean isMultiDex() {
        return this.isMultiDex;
    }

    /* access modifiers changed from: 0000 */
    public boolean isValid() {
        return !TextUtils.isEmpty(this.url) && checkLibInfo() && !TextUtils.isEmpty(this.splitName) && !TextUtils.isEmpty(this.md5) && this.size > 0;
    }
}
