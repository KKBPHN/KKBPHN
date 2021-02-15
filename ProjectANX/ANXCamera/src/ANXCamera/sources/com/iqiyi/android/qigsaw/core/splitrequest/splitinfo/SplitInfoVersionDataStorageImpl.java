package com.iqiyi.android.qigsaw.core.splitrequest.splitinfo;

import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.Properties;

final class SplitInfoVersionDataStorageImpl implements SplitInfoVersionDataStorage {
    private static final String NEW_VERSION = "newVersion";
    private static final String OLD_VERSION = "oldVersion";
    private static final String TAG = "SplitInfoVersionStorageImpl";
    private static final String VERSION_DATA_LOCK_NAME = "version.lock";
    private static final String VERSION_DATA_NAME = "version.info";
    private final FileLock cacheLock;
    private final FileChannel lockChannel;
    private final RandomAccessFile lockRaf;
    private final File versionDataFile;

    SplitInfoVersionDataStorageImpl(File file) {
        String str = TAG;
        this.versionDataFile = new File(file, VERSION_DATA_NAME);
        File file2 = new File(file, VERSION_DATA_LOCK_NAME);
        this.lockRaf = new RandomAccessFile(file2, "rw");
        try {
            this.lockChannel = this.lockRaf.getChannel();
            StringBuilder sb = new StringBuilder();
            sb.append("Blocking on lock ");
            sb.append(file2.getPath());
            SplitLog.i(str, sb.toString(), new Object[0]);
            this.cacheLock = this.lockChannel.lock();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(file2.getPath());
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

    /* JADX WARNING: Removed duplicated region for block: B:19:0x004c  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0006 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static SplitInfoVersionData readVersionDataProperties(File file) {
        FileInputStream fileInputStream;
        int i = 0;
        boolean z = false;
        String str = null;
        String str2 = null;
        while (i < 3 && !z) {
            i++;
            Properties properties = new Properties();
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    properties.load(fileInputStream);
                    str = properties.getProperty(OLD_VERSION);
                    str2 = properties.getProperty(NEW_VERSION);
                } catch (IOException e) {
                    e = e;
                    String str3 = TAG;
                    try {
                        StringBuilder sb = new StringBuilder();
                        sb.append("read property failed, e:");
                        sb.append(e);
                        SplitLog.w(str3, sb.toString(), new Object[0]);
                        FileUtil.closeQuietly(fileInputStream);
                        if (str != null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                    }
                }
            } catch (IOException e2) {
                e = e2;
                FileInputStream fileInputStream2 = null;
                String str32 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("read property failed, e:");
                sb2.append(e);
                SplitLog.w(str32, sb2.toString(), new Object[0]);
                FileUtil.closeQuietly(fileInputStream);
                if (str != null) {
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = null;
                FileUtil.closeQuietly(fileInputStream);
                throw th;
            }
            FileUtil.closeQuietly(fileInputStream);
            if (str != null) {
                if (str2 != null) {
                    z = true;
                }
            }
        }
        if (z) {
            return new SplitInfoVersionData(str, str2);
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b3  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00cc  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0046 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean updateVersionDataProperties(File file, SplitInfoVersionData splitInfoVersionData) {
        if (file == null || splitInfoVersionData == null) {
            return false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("updateVersionDataProperties file path:");
        sb.append(file.getAbsolutePath());
        sb.append(" , oldVer:");
        sb.append(splitInfoVersionData.oldVersion);
        sb.append(", newVer:");
        sb.append(splitInfoVersionData.newVersion);
        String sb2 = sb.toString();
        Object[] objArr = new Object[0];
        String str = TAG;
        SplitLog.i(str, sb2, objArr);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        int i = 0;
        boolean z = false;
        while (i < 3 && !z) {
            i++;
            Properties properties = new Properties();
            properties.put(OLD_VERSION, splitInfoVersionData.oldVersion);
            properties.put(NEW_VERSION, splitInfoVersionData.newVersion);
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                try {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("from old version:");
                    sb3.append(splitInfoVersionData.oldVersion);
                    sb3.append(" to new version:");
                    sb3.append(splitInfoVersionData.newVersion);
                    properties.store(fileOutputStream2, sb3.toString());
                    FileUtil.closeQuietly(fileOutputStream2);
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    try {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("write property failed, e:");
                        sb4.append(e);
                        SplitLog.w(str, sb4.toString(), new Object[0]);
                        FileUtil.closeQuietly(fileOutputStream);
                        SplitInfoVersionData readVersionDataProperties = readVersionDataProperties(file);
                        if (readVersionDataProperties == null) {
                        }
                        if (!z) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream2 = fileOutputStream;
                        FileUtil.closeQuietly(fileOutputStream2);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    FileUtil.closeQuietly(fileOutputStream2);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                StringBuilder sb42 = new StringBuilder();
                sb42.append("write property failed, e:");
                sb42.append(e);
                SplitLog.w(str, sb42.toString(), new Object[0]);
                FileUtil.closeQuietly(fileOutputStream);
                SplitInfoVersionData readVersionDataProperties2 = readVersionDataProperties(file);
                if (readVersionDataProperties2 == null) {
                }
                if (!z) {
                }
            }
            SplitInfoVersionData readVersionDataProperties22 = readVersionDataProperties(file);
            z = readVersionDataProperties22 == null && readVersionDataProperties22.oldVersion.equals(splitInfoVersionData.oldVersion) && readVersionDataProperties22.newVersion.equals(splitInfoVersionData.newVersion);
            if (!z) {
                file.delete();
            }
        }
        return z;
    }

    public void close() {
        this.lockChannel.close();
        this.lockRaf.close();
        this.cacheLock.release();
    }

    public SplitInfoVersionData readVersionData() {
        if (!this.cacheLock.isValid()) {
            throw new IllegalStateException("SplitInfoVersionDataStorage was closed");
        } else if (this.versionDataFile.exists()) {
            return readVersionDataProperties(this.versionDataFile);
        } else {
            return null;
        }
    }

    public boolean updateVersionData(SplitInfoVersionData splitInfoVersionData) {
        if (this.cacheLock.isValid()) {
            return updateVersionDataProperties(this.versionDataFile, splitInfoVersionData);
        }
        throw new IllegalStateException("SplitInfoVersionDataStorage was closed");
    }
}
