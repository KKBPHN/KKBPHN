package com.bef.effectsdk;

import android.content.Context;
import android.text.TextUtils;
import com.ss.android.ttve.monitor.MonitorUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EffectSDKUtils {
    private static List assetFiles = ModelsList.list;
    private static Set localFiles = new HashSet();
    private static Set needRemoveFiles = new HashSet();

    /* access modifiers changed from: private */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    private static void copyAssets(Context context, String str, String[] strArr, boolean z) {
        boolean z2;
        if (!needRemoveFiles.isEmpty()) {
            needRemoveFiles.clear();
        }
        needRemoveFiles.addAll(localFiles);
        String str2 = "/";
        if (!str.endsWith(str2)) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(str2);
            str = sb.toString();
        }
        for (String str3 : assetFiles) {
            final String fileName = getFileName(str3);
            File takeFirstMatchingOrNull = takeFirstMatchingOrNull(needRemoveFiles, new FileFilter() {
                public boolean accept(File file) {
                    return file.getName().contains(fileName);
                }
            });
            boolean z3 = false;
            if (takeFirstMatchingOrNull == null || !new File(str, getAssetRelativePath(str3)).exists()) {
                z2 = true;
            } else {
                needRemoveFiles.remove(takeFirstMatchingOrNull);
                z2 = false;
            }
            if (z2) {
                if (strArr != null && !TextUtils.isEmpty(fileName)) {
                    int length = strArr.length;
                    int i = 0;
                    while (true) {
                        if (i >= length) {
                            break;
                        } else if (fileName.equals(strArr[i])) {
                            z3 = true;
                            break;
                        } else {
                            i++;
                        }
                    }
                }
                if (z3 && z) {
                    copyFile(context, str3, str);
                }
                if (!z3 && !z) {
                    copyFile(context, str3, str);
                }
            }
        }
    }

    /* JADX WARNING: type inference failed for: r1v0 */
    /* JADX WARNING: type inference failed for: r4v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v1, types: [java.io.Closeable] */
    /* JADX WARNING: type inference failed for: r1v2 */
    /* JADX WARNING: type inference failed for: r1v3 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void copyFile(Context context, String str, String str2) {
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        String str3 = "/";
        ? r1 = 0;
        try {
            inputStream = context.getAssets().open(str);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str.substring(str.indexOf(MonitorUtils.KEY_MODEL) + 6, str.lastIndexOf(str3)));
                String sb2 = sb.toString();
                File file = new File(sb2);
                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Can not mkdirs ");
                        sb3.append(file.getPath());
                        throw new IOException(sb3.toString());
                    }
                }
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb2);
                sb4.append(str3);
                sb4.append(getFileName(str));
                fileOutputStream = new FileOutputStream(new File(sb4.toString()));
            } catch (Throwable th) {
                th = th;
                inputStream = inputStream;
                try {
                    closeQuietly(inputStream);
                    throw th;
                } finally {
                    closeQuietly(r1);
                }
            }
            try {
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        try {
                            closeQuietly(inputStream);
                            return;
                        } finally {
                            closeQuietly(fileOutputStream);
                        }
                    }
                }
            } catch (Throwable th2) {
                r1 = fileOutputStream;
                th = th2;
                closeQuietly(inputStream);
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStream = r1;
            r1 = r1;
            closeQuietly(inputStream);
            throw th;
        }
    }

    private static void deleteNoUseModel() {
        for (File file : localFiles) {
            if (needRemoveFiles.contains(file) && file.exists()) {
                file.delete();
            }
        }
    }

    public static void flushAlgorithmModelFiles(Context context, String str) {
        if (!localFiles.isEmpty()) {
            localFiles.clear();
        }
        scanRecursive(str, localFiles);
        copyAssets(context, str, null, false);
        deleteNoUseModel();
        localFiles.clear();
    }

    public static void flushAlgorithmModelFiles(Context context, String str, String[] strArr, boolean z) {
        if (!localFiles.isEmpty()) {
            localFiles.clear();
        }
        scanRecursive(str, localFiles);
        copyAssets(context, str, strArr, z);
        deleteNoUseModel();
        localFiles.clear();
    }

    private static String getAssetRelativePath(String str) {
        int indexOf = str.indexOf("model/");
        return indexOf >= 0 ? str.substring(indexOf + 6, str.length()) : str;
    }

    private static String getFileName(String str) {
        int lastIndexOf = str.lastIndexOf("/");
        return lastIndexOf != -1 ? str.substring(lastIndexOf + 1, str.length()) : "";
    }

    public static String getSdkVersion() {
        return nativeGetSdkVersion();
    }

    private static native String nativeGetSdkVersion();

    public static boolean needUpdate(final Context context, String str) {
        if (!localFiles.isEmpty()) {
            localFiles.clear();
        }
        scanRecursive(str, localFiles);
        try {
            if (assetFiles.size() > localFiles.size()) {
                return true;
            }
            for (final String str2 : assetFiles) {
                if (takeFirstMatchingOrNull(localFiles, new FileFilter() {
                    public boolean accept(File file) {
                        boolean z = false;
                        if (str2.contains(file.getName())) {
                            InputStream inputStream = null;
                            try {
                                inputStream = context.getAssets().open(str2);
                                if (file.length() == ((long) inputStream.available())) {
                                    z = true;
                                }
                                return z;
                            } catch (IOException unused) {
                            } finally {
                                EffectSDKUtils.closeQuietly(inputStream);
                            }
                        }
                        return false;
                    }
                }) == null) {
                    return true;
                }
            }
            return false;
        } catch (Throwable th) {
            th.printStackTrace();
            return true;
        }
    }

    private static void scanRecursive(String str, Set set) {
        File file = new File(str);
        if (file.exists()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    if (file2.isDirectory()) {
                        scanRecursive(file2.getAbsolutePath(), set);
                    } else {
                        set.add(file2);
                    }
                }
            }
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.Set, code=java.util.Set<java.io.File>, for r2v0, types: [java.util.Set, java.util.Set<java.io.File>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static File takeFirstMatchingOrNull(Set<File> set, FileFilter fileFilter) {
        for (File file : set) {
            if (fileFilter.accept(file)) {
                return file;
            }
        }
        return null;
    }
}
