package com.iqiyi.android.qigsaw.core.splitinstall;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.iqiyi.android.qigsaw.core.common.FileUtil;
import com.iqiyi.android.qigsaw.core.common.SplitLog;
import com.iqiyi.android.qigsaw.core.splitrequest.splitinfo.SplitPathManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;

@RestrictTo({Scope.LIBRARY_GROUP})
public final class SplitPendingUninstallManager {
    private static final String PENDING_UNINSTALL_SPLITS = "pendingUninstallSplits";
    private static final String TAG = "PendingUninstallSplitsManager";
    private static final String VERSION_DATA_NAME = "uninstallsplits.info";
    private static final Object sLock = new Object();
    private final File pendingUninstallSplitsFile = new File(SplitPathManager.require().getUninstallSplitsDir(), VERSION_DATA_NAME);

    private List readPendingUninstallSplitsInternal(File file) {
        FileInputStream fileInputStream;
        List list = null;
        int i = 0;
        boolean z = false;
        while (i < 3 && !z) {
            i++;
            Properties properties = new Properties();
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    properties.load(fileInputStream);
                    String property = properties.getProperty(PENDING_UNINSTALL_SPLITS);
                    if (property != null) {
                        String[] split = property.split(",");
                        ArrayList arrayList = new ArrayList();
                        try {
                            Collections.addAll(arrayList, split);
                            list = arrayList;
                        } catch (IOException e) {
                            e = e;
                            list = arrayList;
                            String str = TAG;
                            try {
                                StringBuilder sb = new StringBuilder();
                                sb.append("read property failed, e:");
                                sb.append(e);
                                SplitLog.w(str, sb.toString(), new Object[0]);
                                FileUtil.closeQuietly(fileInputStream);
                            } catch (Throwable th) {
                                th = th;
                                FileUtil.closeQuietly(fileInputStream);
                                throw th;
                            }
                        }
                    }
                    FileUtil.closeQuietly(fileInputStream);
                    z = true;
                } catch (IOException e2) {
                    e = e2;
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("read property failed, e:");
                    sb2.append(e);
                    SplitLog.w(str2, sb2.toString(), new Object[0]);
                    FileUtil.closeQuietly(fileInputStream);
                }
            } catch (IOException e3) {
                e = e3;
                fileInputStream = null;
                String str22 = TAG;
                StringBuilder sb22 = new StringBuilder();
                sb22.append("read property failed, e:");
                sb22.append(e);
                SplitLog.w(str22, sb22.toString(), new Object[0]);
                FileUtil.closeQuietly(fileInputStream);
            } catch (Throwable th2) {
                fileInputStream = null;
                th = th2;
                FileUtil.closeQuietly(fileInputStream);
                throw th;
            }
        }
        return list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00e1  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00ec  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x007f A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean recordPendingUninstallSplitsInternal(File file, List list) {
        if (file == null || list == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList(list);
        StringBuilder sb = new StringBuilder();
        sb.append("recordSplitUninstallInfo file path:");
        sb.append(file.getAbsolutePath());
        sb.append(" , uninstalls splits: ");
        sb.append(arrayList.toString());
        String sb2 = sb.toString();
        Object[] objArr = new Object[0];
        String str = TAG;
        SplitLog.i(str, sb2, objArr);
        if (file.exists()) {
            List readPendingUninstallSplits = readPendingUninstallSplits();
            if (readPendingUninstallSplits != null) {
                if (readPendingUninstallSplits.containsAll(arrayList)) {
                    SplitLog.i(str, "Splits %s have been marked to uninstall!", arrayList.toString());
                    return true;
                }
                arrayList.addAll(readPendingUninstallSplits);
                HashSet hashSet = new HashSet(arrayList);
                arrayList.clear();
                arrayList.addAll(hashSet);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Splits which need to be uninstalled have been updated, new pending uninstall splits: ");
                sb3.append(arrayList.toString());
                SplitLog.i(str, sb3.toString(), new Object[0]);
            }
        }
        int i = 0;
        boolean z = false;
        while (i < 3 && !z) {
            i++;
            Properties properties = new Properties();
            properties.put(PENDING_UNINSTALL_SPLITS, TextUtils.join(",", arrayList));
            FileOutputStream fileOutputStream = null;
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file, false);
                try {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("splits need to be uninstalled: ");
                    sb4.append(arrayList.toString());
                    properties.store(fileOutputStream2, sb4.toString());
                    FileUtil.closeQuietly(fileOutputStream2);
                } catch (Exception e) {
                    e = e;
                    fileOutputStream = fileOutputStream2;
                    try {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("write property failed, e:");
                        sb5.append(e);
                        SplitLog.w(str, sb5.toString(), new Object[0]);
                        FileUtil.closeQuietly(fileOutputStream);
                        List readPendingUninstallSplits2 = readPendingUninstallSplits();
                        if (readPendingUninstallSplits2 == null) {
                        }
                        if (!z) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        FileUtil.closeQuietly(fileOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = fileOutputStream2;
                    FileUtil.closeQuietly(fileOutputStream);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                StringBuilder sb52 = new StringBuilder();
                sb52.append("write property failed, e:");
                sb52.append(e);
                SplitLog.w(str, sb52.toString(), new Object[0]);
                FileUtil.closeQuietly(fileOutputStream);
                List readPendingUninstallSplits22 = readPendingUninstallSplits();
                if (readPendingUninstallSplits22 == null) {
                }
                if (!z) {
                }
            }
            List readPendingUninstallSplits222 = readPendingUninstallSplits();
            z = readPendingUninstallSplits222 == null && readPendingUninstallSplits222.containsAll(arrayList);
            if (!z) {
                file.delete();
            }
        }
        return z;
    }

    public boolean deletePendingUninstallSplitsRecord() {
        synchronized (sLock) {
            if (!this.pendingUninstallSplitsFile.exists()) {
                return true;
            }
            boolean deleteFileSafely = FileUtil.deleteFileSafely(this.pendingUninstallSplitsFile);
            return deleteFileSafely;
        }
    }

    public List readPendingUninstallSplits() {
        synchronized (sLock) {
            if (!this.pendingUninstallSplitsFile.exists()) {
                return null;
            }
            List readPendingUninstallSplitsInternal = readPendingUninstallSplitsInternal(this.pendingUninstallSplitsFile);
            return readPendingUninstallSplitsInternal;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean recordPendingUninstallSplits(@NonNull List list) {
        boolean recordPendingUninstallSplitsInternal;
        synchronized (sLock) {
            recordPendingUninstallSplitsInternal = recordPendingUninstallSplitsInternal(this.pendingUninstallSplitsFile, list);
        }
        return recordPendingUninstallSplitsInternal;
    }
}
