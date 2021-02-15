package com.iqiyi.android.qigsaw.core.splitinstall;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import java.io.File;
import java.util.List;

abstract class SplitInstaller {

    final class InstallException extends Exception {
        private final int errorCode;

        InstallException(int i, Throwable th) {
            StringBuilder sb = new StringBuilder(32);
            sb.append("Split Install Error: ");
            sb.append(i);
            super(sb.toString(), th);
            this.errorCode = i;
        }

        /* access modifiers changed from: 0000 */
        public int getErrorCode() {
            return this.errorCode;
        }
    }

    class InstallResult {
        final List addedDexPaths;
        final File apkFile;
        final boolean firstInstalled;
        final String splitName;

        InstallResult(@NonNull String str, @NonNull File file, @Nullable List list, boolean z) {
            this.splitName = str;
            this.apkFile = file;
            this.addedDexPaths = list;
            this.firstInstalled = z;
        }
    }

    SplitInstaller() {
    }

    public abstract void checkSplitMD5(File file, String str);

    public abstract boolean createInstalledMark(File file);

    public abstract boolean createInstalledMarkLock(File file, File file2);

    public abstract void extractLib(SplitInfo splitInfo, File file);

    public abstract List extractMultiDex(SplitInfo splitInfo, File file);

    public abstract InstallResult install(boolean z, SplitInfo splitInfo);

    public abstract void verifySignature(File file);
}
