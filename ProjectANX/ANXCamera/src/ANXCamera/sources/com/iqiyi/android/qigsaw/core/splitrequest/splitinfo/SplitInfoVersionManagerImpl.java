package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.ProcessUtil;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitreport.SplitUpdateReporter;
import java.io.File;
import java.io.IOException;

final class SplitInfoVersionManagerImpl implements SplitInfoVersionManager {
    private static final String TAG = "SplitInfoVersionManager";
    private String currentVersion;
    private String defaultVersion;
    private boolean isMainProcess;
    private File rootDir;

    private SplitInfoVersionManagerImpl(Context context, boolean z, String str, String str2) {
        this.defaultVersion = str;
        this.isMainProcess = z;
        this.rootDir = new File(new File(context.getDir(SplitConstants.QIGSAW, 0), str2), SplitInfoVersionManager.SPLIT_ROOT_DIR_NAME);
        processVersionData(context);
        reportNewSplitInfoVersionLoaded();
    }

    static SplitInfoVersionManager createSplitInfoVersionManager(Context context, boolean z) {
        return new SplitInfoVersionManagerImpl(context, z, SplitBaseInfoProvider.getDefaultSplitInfoVersion(), SplitBaseInfoProvider.getQigsawId());
    }

    private void processVersionData(Context context) {
        SplitInfoVersionData readVersionData = readVersionData();
        String str = TAG;
        if (readVersionData == null) {
            SplitLog.i(str, "No new split info version, just use default version.", new Object[0]);
            this.currentVersion = this.defaultVersion;
            return;
        }
        String str2 = readVersionData.oldVersion;
        String str3 = readVersionData.newVersion;
        if (str2.equals(str3)) {
            SplitLog.i(str, "Splits have been updated, so we use new split info version %s.", str3);
            this.currentVersion = str3;
        } else if (!this.isMainProcess) {
            this.currentVersion = str2;
        } else if (updateVersionData(new SplitInfoVersionData(str3, str3))) {
            this.currentVersion = str3;
            ProcessUtil.killAllOtherProcess(context);
            SplitLog.i(str, "Splits have been updated, start to kill other processes!", new Object[0]);
        } else {
            this.currentVersion = str2;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to update new split info version: ");
            sb.append(str3);
            SplitLog.w(str, sb.toString(), new Object[0]);
        }
    }

    private SplitInfoVersionData readVersionData() {
        try {
            SplitInfoVersionDataStorageImpl splitInfoVersionDataStorageImpl = new SplitInfoVersionDataStorageImpl(this.rootDir);
            SplitInfoVersionData readVersionData = splitInfoVersionDataStorageImpl.readVersionData();
            FileUtil.closeQuietly(splitInfoVersionDataStorageImpl);
            return readVersionData;
        } catch (IOException unused) {
            return null;
        }
    }

    private void reportNewSplitInfoVersionLoaded() {
        if (this.isMainProcess && !TextUtils.equals(this.currentVersion, this.defaultVersion)) {
            SplitUpdateReporter updateReporter = SplitUpdateReporterManager.getUpdateReporter();
            if (updateReporter != null) {
                updateReporter.onNewSplitInfoVersionLoaded(this.currentVersion);
            }
        }
    }

    private boolean updateVersionData(SplitInfoVersionData splitInfoVersionData) {
        try {
            SplitInfoVersionDataStorageImpl splitInfoVersionDataStorageImpl = new SplitInfoVersionDataStorageImpl(this.rootDir);
            boolean updateVersionData = splitInfoVersionDataStorageImpl.updateVersionData(splitInfoVersionData);
            FileUtil.closeQuietly(splitInfoVersionDataStorageImpl);
            return updateVersionData;
        } catch (IOException unused) {
            return false;
        }
    }

    @NonNull
    public String getCurrentVersion() {
        return this.currentVersion;
    }

    @NonNull
    public String getDefaultVersion() {
        return this.defaultVersion;
    }

    public File getRootDir() {
        return this.rootDir;
    }

    public boolean updateVersion(Context context, String str, File file) {
        boolean z;
        boolean exists = this.rootDir.exists();
        String str2 = TAG;
        if (exists || this.rootDir.mkdirs()) {
            StringBuilder sb = new StringBuilder();
            sb.append(SplitConstants.QIGSAW_PREFIX);
            sb.append(str);
            sb.append(SplitConstants.DOT_JSON);
            try {
                FileUtil.copyFile(file, new File(this.rootDir, sb.toString()));
                z = true;
                if (updateVersionData(new SplitInfoVersionData(this.currentVersion, str))) {
                    SplitLog.i(str2, "Success to update split info version, current version %s, new version %s", this.currentVersion, str);
                } else {
                    z = false;
                }
                try {
                    if (file.exists() && !file.delete()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to delete temp split info file: ");
                        sb2.append(file.getAbsolutePath());
                        SplitLog.w(str2, sb2.toString(), new Object[0]);
                    }
                } catch (IOException e) {
                    e = e;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Failed to rename file : ");
                    sb3.append(file.getAbsolutePath());
                    SplitLog.printErrStackTrace(str2, e, sb3.toString(), new Object[0]);
                    return z;
                }
            } catch (IOException e2) {
                e = e2;
                boolean z2 = false;
                StringBuilder sb32 = new StringBuilder();
                sb32.append("Failed to rename file : ");
                sb32.append(file.getAbsolutePath());
                SplitLog.printErrStackTrace(str2, e, sb32.toString(), new Object[0]);
                return z;
            }
            return z;
        }
        SplitLog.w(str2, "Failed to make dir for split info file!", new Object[0]);
        return false;
    }
}
