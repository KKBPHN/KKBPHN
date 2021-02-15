package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import android.content.Context;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitBaseInfoProvider;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitPathManager {
    private static final String TAG = "SplitPathManager";
    private static final AtomicReference sSplitPathManagerRef = new AtomicReference();
    private final String qigsawId;
    private final File rootDir;

    private SplitPathManager(File file, String str) {
        this.rootDir = new File(file, str);
        this.qigsawId = str;
    }

    private static SplitPathManager create(Context context) {
        return new SplitPathManager(context.getDir(SplitConstants.QIGSAW, 0), SplitBaseInfoProvider.getQigsawId());
    }

    public static void install(Context context) {
        sSplitPathManagerRef.compareAndSet(null, create(context));
    }

    public static SplitPathManager require() {
        if (sSplitPathManagerRef.get() != null) {
            return (SplitPathManager) sSplitPathManagerRef.get();
        }
        throw new RuntimeException("SplitPathManager must be initialized firstly!");
    }

    public void clearCache() {
        File[] listFiles = this.rootDir.getParentFile().listFiles();
        if (listFiles != null && listFiles.length > 0) {
            int length = listFiles.length;
            for (int i = 0; i < length; i++) {
                File file = listFiles[i];
                if (file.isDirectory() && !file.getName().equals(this.qigsawId)) {
                    FileUtil.deleteDir(file);
                    SplitLog.i(TAG, "Success to delete all obsolete splits for current app version!", new Object[0]);
                }
            }
        }
    }

    public File getSplitCodeCacheDir(SplitInfo splitInfo) {
        File file = new File(getSplitDir(splitInfo), "code_cache");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getSplitDir(SplitInfo splitInfo) {
        File file = new File(getSplitRootDir(splitInfo), splitInfo.getSplitVersion());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getSplitLibDir(SplitInfo splitInfo) {
        File splitDir = getSplitDir(splitInfo);
        StringBuilder sb = new StringBuilder();
        sb.append("nativeLib");
        sb.append(File.separator);
        sb.append(splitInfo.getLibInfo().getAbi());
        File file = new File(splitDir, sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getSplitMarkFile(SplitInfo splitInfo) {
        return new File(getSplitDir(splitInfo), splitInfo.getMd5());
    }

    public File getSplitOptDir(SplitInfo splitInfo) {
        File file = new File(getSplitDir(splitInfo), "oat");
        if (!file.exists() && file.mkdirs()) {
            file.setWritable(true);
            file.setReadable(true);
        }
        return file;
    }

    public File getSplitRootDir(SplitInfo splitInfo) {
        File file = new File(this.rootDir, splitInfo.getSplitName());
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getSplitSpecialLockFile(SplitInfo splitInfo) {
        return new File(getSplitDir(splitInfo), "ov.lock");
    }

    public File getSplitSpecialMarkFile(SplitInfo splitInfo) {
        File splitDir = getSplitDir(splitInfo);
        StringBuilder sb = new StringBuilder();
        sb.append(splitInfo.getMd5());
        sb.append(".ov");
        return new File(splitDir, sb.toString());
    }

    public File getSplitTmpDir() {
        File file = new File(this.rootDir, "tmp");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public File getUninstallSplitsDir() {
        File file = new File(this.rootDir, "uninstall");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }
}
