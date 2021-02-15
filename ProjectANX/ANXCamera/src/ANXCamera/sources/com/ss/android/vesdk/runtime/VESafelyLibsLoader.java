package com.ss.android.vesdk.runtime;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.ss.android.vesdk.VEFileUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class VESafelyLibsLoader {
    private static final String LIB_DIR = "libso";
    private static final String TAG = "VESafelyLibsLoader";
    private static List sLoadedLibs = new ArrayList();

    private static File getLibraryFile(Context context, String str) {
        String mapLibraryName = System.mapLibraryName(str);
        File libraryFolder = getLibraryFolder(context);
        if (libraryFolder != null) {
            return new File(libraryFolder, mapLibraryName);
        }
        return null;
    }

    private static File getLibraryFolder(Context context) {
        if (context == null || context.getFilesDir() == null) {
            return null;
        }
        File file = new File(context.getFilesDir(), LIB_DIR);
        if (!file.exists()) {
            VEFileUtils.mkdir(file.getAbsolutePath());
        }
        return file;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:36:0x009b, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized boolean loadLibrary(String str, Context context) {
        synchronized (VESafelyLibsLoader.class) {
            if (sLoadedLibs.contains(str)) {
                return true;
            }
            try {
                System.loadLibrary(str);
                sLoadedLibs.add(str);
            } catch (UnsatisfiedLinkError e) {
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("loadLibrary ");
                sb.append(str);
                sb.append(" error");
                Log.e(str2, sb.toString(), e);
                File libraryFile = getLibraryFile(context, str);
                if (libraryFile == null) {
                    return false;
                }
                if (libraryFile.exists()) {
                    libraryFile.delete();
                }
                String unpackLibrary = unpackLibrary(context, str, libraryFile);
                if (unpackLibrary != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(e.getMessage());
                    sb2.append("[");
                    sb2.append(unpackLibrary);
                    sb2.append("]");
                    Log.e("loadLibrary", sb2.toString());
                    return false;
                }
                System.load(libraryFile.getAbsolutePath());
                sLoadedLibs.add(str);
            } catch (Throwable unused) {
                return false;
            }
        }
    }

    private static String unpackLibrary(Context context, String str, File file) {
        InputStream inputStream;
        ZipFile zipFile;
        String message;
        InputStream inputStream2;
        FileOutputStream fileOutputStream;
        String str2 = "/";
        String str3 = "lib/";
        FileOutputStream fileOutputStream2 = null;
        try {
            zipFile = new ZipFile(new File(context.getApplicationInfo().sourceDir), 1);
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(Build.CPU_ABI);
                sb.append(str2);
                sb.append(System.mapLibraryName(str));
                ZipEntry entry = zipFile.getEntry(sb.toString());
                if (entry == null) {
                    int indexOf = Build.CPU_ABI.indexOf(45);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    String str4 = Build.CPU_ABI;
                    if (indexOf <= 0) {
                        indexOf = Build.CPU_ABI.length();
                    }
                    sb2.append(str4.substring(0, indexOf));
                    sb2.append(str2);
                    sb2.append(System.mapLibraryName(str));
                    String sb3 = sb2.toString();
                    ZipEntry entry2 = zipFile.getEntry(sb3);
                    if (entry2 == null) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Library entry not found:");
                        sb4.append(sb3);
                        message = sb4.toString();
                        VEFileUtils.close((Closeable) null);
                        VEFileUtils.close((Closeable) null);
                        VEFileUtils.close(zipFile);
                        return message;
                    }
                    entry = entry2;
                }
                file.createNewFile();
                inputStream2 = zipFile.getInputStream(entry);
            } catch (Throwable th) {
                th = th;
                inputStream = null;
                try {
                    message = th.getMessage();
                    VEFileUtils.close((Closeable) fileOutputStream2);
                    VEFileUtils.close((Closeable) inputStream);
                    VEFileUtils.close(zipFile);
                    return message;
                } catch (Throwable th2) {
                    VEFileUtils.close((Closeable) fileOutputStream2);
                    VEFileUtils.close((Closeable) inputStream);
                    VEFileUtils.close(zipFile);
                    throw th2;
                }
            }
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                inputStream = inputStream2;
                th = th4;
                message = th.getMessage();
                VEFileUtils.close((Closeable) fileOutputStream2);
                VEFileUtils.close((Closeable) inputStream);
                VEFileUtils.close(zipFile);
                return message;
            }
            try {
                byte[] bArr = new byte[16384];
                while (true) {
                    int read = inputStream2.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        VEFileUtils.setPermissions(file.getAbsolutePath(), 493);
                        VEFileUtils.close((Closeable) fileOutputStream);
                        VEFileUtils.close((Closeable) inputStream2);
                        VEFileUtils.close(zipFile);
                        return null;
                    }
                }
            } catch (Throwable th5) {
                fileOutputStream2 = fileOutputStream;
                inputStream = inputStream2;
                th = th5;
                message = th.getMessage();
                VEFileUtils.close((Closeable) fileOutputStream2);
                VEFileUtils.close((Closeable) inputStream);
                VEFileUtils.close(zipFile);
                return message;
            }
        } catch (Throwable th6) {
            th = th6;
            inputStream = null;
            zipFile = null;
            message = th.getMessage();
            VEFileUtils.close((Closeable) fileOutputStream2);
            VEFileUtils.close((Closeable) inputStream);
            VEFileUtils.close(zipFile);
            return message;
        }
    }
}
