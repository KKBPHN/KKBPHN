package com.iqiyi.android.qigsaw.core.splitinstall;

import android.content.Context;
import android.text.TextUtils;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitInfo;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

final class SplitDownloadPreprocessor implements Closeable {
    private static final String LOCK_FILENAME = "SplitCopier.lock";
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final String TAG = "SplitDownloadPreprocessor";
    private final FileLock cacheLock;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File splitApk;
    private final File splitDir;

    SplitDownloadPreprocessor(File file, File file2) {
        String str = TAG;
        this.splitApk = file2;
        this.splitDir = file;
        File file3 = new File(file, LOCK_FILENAME);
        this.lockRaf = new RandomAccessFile(file3, "rw");
        try {
            this.lockChannel = this.lockRaf.getChannel();
            StringBuilder sb = new StringBuilder();
            sb.append("Blocking on lock ");
            sb.append(file3.getPath());
            SplitLog.i(str, sb.toString(), new Object[0]);
            this.cacheLock = this.lockChannel.lock();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file3.getPath());
            sb2.append(" locked");
            SplitLog.i(str, sb2.toString(), new Object[0]);
        } catch (IOException | Error | RuntimeException e) {
            FileUtil.closeQuietly(this.lockChannel);
            throw e;
        } catch (IOException | Error | RuntimeException e2) {
            FileUtil.closeQuietly(this.lockRaf);
            throw e2;
        }
    }

    private boolean checkSplitMD5(SplitInfo splitInfo) {
        String md5 = FileUtil.getMD5(this.splitApk);
        if (!TextUtils.isEmpty(md5)) {
            return splitInfo.getMd5().equals(md5);
        }
        return splitInfo.getSize() == this.splitApk.length();
    }

    private void copyBuiltInSplit(Context context, SplitInfo splitInfo) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(splitInfo.getSplitName());
        sb.append(".zip");
        String sb2 = sb.toString();
        File splitTmpDir = SplitPathManager.require().getSplitTmpDir();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("tmp-");
        sb3.append(splitInfo.getSplitName());
        File createTempFile = File.createTempFile(sb3.toString(), ".apk", splitTmpDir);
        boolean z = false;
        int i = 0;
        while (!z && i < 3) {
            i++;
            try {
                FileUtil.copyFile(context.getAssets().open(sb2), (OutputStream) new FileOutputStream(createTempFile));
                if (!createTempFile.renameTo(this.splitApk)) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failed to rename \"");
                    sb4.append(createTempFile.getAbsolutePath());
                    sb4.append("\" to \"");
                    sb4.append(this.splitApk.getAbsolutePath());
                    sb4.append("\"");
                    SplitLog.w(str, sb4.toString(), new Object[0]);
                } else {
                    z = true;
                }
            } catch (IOException unused) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Failed to copy built-in split apk, attempts times : ");
                sb5.append(i);
                SplitLog.w(str, sb5.toString(), new Object[0]);
            }
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Copy built-in split ");
            sb6.append(z ? "succeeded" : BaseEvent.VALUE_FAILED);
            sb6.append(" '");
            sb6.append(this.splitApk.getAbsolutePath());
            sb6.append("': length ");
            sb6.append(this.splitApk.length());
            SplitLog.i(str, sb6.toString(), new Object[0]);
            if (!z) {
                FileUtil.deleteFileSafely(this.splitApk);
                if (this.splitApk.exists()) {
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Failed to delete copied split apk which has been corrupted'");
                    sb7.append(this.splitApk.getPath());
                    sb7.append("'");
                    SplitLog.w(str, sb7.toString(), new Object[0]);
                }
            }
        }
        FileUtil.deleteFileSafely(createTempFile);
        if (!z) {
            throw new IOException(String.format("Failed to copy built-in file %s to path %s", new Object[]{sb2, this.splitApk.getPath()}));
        }
    }

    private void deleteCorruptedOrObsoletedSplitApk() {
        FileUtil.deleteDir(this.splitDir);
        if (this.splitDir.exists()) {
            SplitLog.w(TAG, "Failed to delete corrupted split files", new Object[0]);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0011, code lost:
        if (r3 != false) goto L_0x0013;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean verifySplitApk(Context context, SplitInfo splitInfo, boolean z) {
        boolean z2;
        if (!FileUtil.isLegalFile(this.splitApk)) {
            return false;
        }
        if (z) {
            z2 = SignatureValidator.validateSplit(context, this.splitApk);
        }
        z2 = checkSplitMD5(splitInfo);
        if (!z2) {
            SplitLog.w(TAG, "Oops! Failed to check split %s signature and md5", splitInfo.getSplitName());
            deleteCorruptedOrObsoletedSplitApk();
        }
        return z2;
    }

    public void close() {
        this.lockChannel.close();
        this.lockRaf.close();
        this.cacheLock.release();
    }

    /* access modifiers changed from: 0000 */
    public void load(Context context, SplitInfo splitInfo, boolean z) {
        if (this.cacheLock.isValid()) {
            String splitName = splitInfo.getSplitName();
            boolean isBuiltIn = splitInfo.isBuiltIn();
            String str = TAG;
            if (isBuiltIn) {
                boolean startsWith = splitInfo.getUrl().startsWith(SplitConstants.URL_ASSETS);
                String str2 = "Failed to check built-in split %s, it may be corrupted";
                if (!this.splitApk.exists()) {
                    SplitLog.v(str, "Built-in split %s is not existing, copy it from asset to [%s]", splitName, this.splitApk.getAbsolutePath());
                    if (startsWith) {
                        copyBuiltInSplit(context, splitInfo);
                    }
                    if (!verifySplitApk(context, splitInfo, z)) {
                        throw new IOException(String.format(str2, new Object[]{splitName}));
                    }
                    return;
                }
                SplitLog.v(str, "Built-in split %s is existing", this.splitApk.getAbsolutePath());
                if (!verifySplitApk(context, splitInfo, z)) {
                    if (startsWith) {
                        copyBuiltInSplit(context, splitInfo);
                    }
                    if (!verifySplitApk(context, splitInfo, z)) {
                        throw new IOException(String.format(str2, new Object[]{this.splitApk.getAbsolutePath()}));
                    }
                }
            } else if (this.splitApk.exists()) {
                SplitLog.v(str, "split %s is downloaded", splitName);
                verifySplitApk(context, splitInfo, z);
            } else {
                SplitLog.v(str, " split %s is not downloaded", splitName);
            }
        } else {
            throw new IllegalStateException("FileCheckerAndCopier was closed");
        }
    }
}
