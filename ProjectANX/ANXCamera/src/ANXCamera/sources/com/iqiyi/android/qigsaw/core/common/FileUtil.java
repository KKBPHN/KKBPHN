package com.iqiyi.android.qigsaw.core.common;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.zip.ZipFile;

@RestrictTo({Scope.LIBRARY_GROUP})
public class FileUtil {
    private static final String TAG = "Split.FileUtil";

    private FileUtil() {
    }

    @SuppressLint({"NewApi"})
    public static void closeQuietly(Object obj) {
        if (obj != null) {
            if (obj instanceof Closeable) {
                try {
                    ((Closeable) obj).close();
                } catch (Throwable unused) {
                }
            } else if (VERSION.SDK_INT >= 19 && (obj instanceof AutoCloseable)) {
                ((AutoCloseable) obj).close();
            } else if (obj instanceof ZipFile) {
                ((ZipFile) obj).close();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("obj: ");
                sb.append(obj);
                sb.append(" cannot be closed.");
                throw new IllegalArgumentException(sb.toString());
            }
        }
    }

    public static void copyFile(File file, File file2) {
        copyFile((InputStream) new FileInputStream(file), (OutputStream) new FileOutputStream(file2));
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        try {
            byte[] bArr = new byte[16384];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read != -1) {
                    bufferedOutputStream.write(bArr, 0, read);
                } else {
                    bufferedOutputStream.flush();
                    return;
                }
            }
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x004b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void createFileSafely(@NonNull File file) {
        if (!file.exists()) {
            Throwable th = null;
            int i = 0;
            boolean z = false;
            while (true) {
                String str = TAG;
                if (i < 3 && !z) {
                    i++;
                    try {
                        if (!file.createNewFile()) {
                            SplitLog.w(str, "File %s already exists", file.getAbsolutePath());
                        }
                        z = true;
                    } catch (Exception e) {
                        th = e;
                        z = false;
                    }
                } else if (!z) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Succeed to create file ");
                    sb.append(file.getAbsolutePath());
                    SplitLog.v(str, sb.toString(), new Object[0]);
                    return;
                } else {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to create file ");
                    sb2.append(file.getAbsolutePath());
                    throw new IOException(sb2.toString(), th);
                }
            }
            if (!z) {
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0019, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r3 = new java.lang.StringBuilder();
        r3.append("Failed to create file ");
        r3.append(r5.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0034, code lost:
        throw new java.io.IOException(r3.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0035, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r2 = new java.lang.StringBuilder();
        r2.append("Failed to lock file ");
        r2.append(r6.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0051, code lost:
        throw new java.io.IOException(r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0052, code lost:
        if (r6 != null) goto L_0x0054;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        closeQuietly(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0057, code lost:
        throw r5;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x001a */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0037 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void createFileSafelyLock(@NonNull File file, File file2) {
        synchronized (FileUtil.class) {
            if (!file.exists()) {
                FileLockHelper fileLockHelper = null;
                fileLockHelper = FileLockHelper.getFileLock(file2);
                createFileSafely(file);
                if (file2 != null) {
                    closeQuietly(fileLockHelper);
                }
            }
        }
    }

    public static boolean deleteDir(File file) {
        return deleteDir(file, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002b, code lost:
        if (r5 != false) goto L_0x0010;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean deleteDir(File file, boolean z) {
        if (file == null || !file.exists()) {
            return false;
        }
        if (!file.isFile()) {
            if (file.isDirectory()) {
                File[] listFiles = file.listFiles();
                if (listFiles != null) {
                    for (File deleteDir : listFiles) {
                        deleteDir(deleteDir);
                    }
                }
            }
            return true;
        }
        deleteFileSafely(file);
        return true;
    }

    public static boolean deleteFileSafely(@NonNull File file) {
        if (!file.exists()) {
            return true;
        }
        int i = 0;
        boolean z = false;
        while (i < 3 && !z) {
            i++;
            if (file.delete()) {
                z = true;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("%s to delete file: ");
        sb.append(file.getAbsolutePath());
        String sb2 = sb.toString();
        Object[] objArr = new Object[1];
        objArr[0] = z ? "Succeed" : "Failed";
        SplitLog.d(TAG, sb2, objArr);
        return z;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:17|18|19) */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001b, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x001c, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2 = new java.lang.StringBuilder();
        r2.append("Failed to lock file ");
        r2.append(r5.getAbsolutePath());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0038, code lost:
        throw new java.io.IOException(r2.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0039, code lost:
        if (r5 != null) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        closeQuietly(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x003e, code lost:
        throw r4;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x001e */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized boolean deleteFileSafelyLock(@NonNull File file, File file2) {
        synchronized (FileUtil.class) {
            if (!file.exists()) {
                return true;
            }
            FileLockHelper fileLockHelper = null;
            fileLockHelper = FileLockHelper.getFileLock(file2);
            boolean deleteFileSafely = deleteFileSafely(file);
            if (file2 != null) {
                closeQuietly(fileLockHelper);
            }
        }
    }

    public static String getMD5(File file) {
        FileInputStream fileInputStream;
        FileInputStream fileInputStream2 = null;
        if (file != null && file.exists()) {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    String md5 = getMD5((InputStream) fileInputStream);
                    closeQuietly(fileInputStream);
                    return md5;
                } catch (Exception unused) {
                    closeQuietly(fileInputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    fileInputStream2 = fileInputStream;
                    closeQuietly(fileInputStream2);
                    throw th;
                }
            } catch (Exception unused2) {
                fileInputStream = null;
                closeQuietly(fileInputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                closeQuietly(fileInputStream2);
                throw th;
            }
        }
        return null;
    }

    public static String getMD5(InputStream inputStream) {
        int i;
        if (inputStream == null) {
            return null;
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            MessageDigest instance = MessageDigest.getInstance("MD5");
            StringBuilder sb = new StringBuilder(32);
            byte[] bArr = new byte[102400];
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                instance.update(bArr, 0, read);
            }
            byte[] digest = instance.digest();
            for (byte b : digest) {
                sb.append(Integer.toString((b & -1) + 0, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception unused) {
            return null;
        }
    }

    public static boolean isLegalFile(File file) {
        return file != null && file.exists() && file.canRead() && file.isFile() && file.length() > 0;
    }
}
