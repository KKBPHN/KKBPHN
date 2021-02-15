package com.xiaomi.camera.parallelservice.util;

import com.android.camera.log.Log;
import com.android.gallery3d.exif.ExifInterface;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ParallelExifUtil {
    private static final String TAG = "ParallelExifUtil";

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0099 A[SYNTHETIC, Splitter:B:27:0x0099] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x00a1 A[Catch:{ IOException -> 0x009d }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00ae A[SYNTHETIC, Splitter:B:37:0x00ae] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00b6 A[Catch:{ IOException -> 0x00b2 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void updateExif(String str) {
        FileOutputStream fileOutputStream;
        FileInputStream fileInputStream;
        Exception e;
        FileOutputStream fileOutputStream2;
        String str2 = "close image file failed!";
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("updateExif path:");
        sb.append(str);
        Log.v(str3, sb.toString());
        File file = new File(str);
        if (!file.exists()) {
            String str4 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateExif path not exist. ");
            sb2.append(str);
            Log.e(str4, sb2.toString());
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str);
        sb3.append(".tmp");
        File file2 = new File(sb3.toString());
        try {
            file2.createNewFile();
            fileInputStream = new FileInputStream(file);
            try {
                fileOutputStream2 = new FileOutputStream(file2);
                try {
                    byte[] bArr = new byte[((int) file.length())];
                    fileInputStream.read(bArr);
                    fileOutputStream2.write(ExifInterface.removeParallelProcessComment(bArr));
                    fileOutputStream2.close();
                    file.delete();
                    file2.renameTo(file);
                } catch (Exception e2) {
                    e = e2;
                    try {
                        Log.e(TAG, "process exif failed!", (Throwable) e);
                        if (fileInputStream != null) {
                            fileInputStream.close();
                        }
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (fileInputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        throw th;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                fileOutputStream2 = null;
                e = e;
                Log.e(TAG, "process exif failed!", (Throwable) e);
                if (fileInputStream != null) {
                }
                if (fileOutputStream != null) {
                }
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                th = th;
                if (fileInputStream != null) {
                }
                if (fileOutputStream != null) {
                }
                throw th;
            }
            try {
                fileInputStream.close();
                fileOutputStream2.close();
            } catch (IOException e4) {
                Log.e(TAG, str2, (Throwable) e4);
            }
        } catch (Exception e5) {
            e = e5;
            FileInputStream fileInputStream2 = null;
            fileOutputStream2 = null;
            e = e;
            Log.e(TAG, "process exif failed!", (Throwable) e);
            if (fileInputStream != null) {
            }
            if (fileOutputStream != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
            fileOutputStream = null;
            th = th;
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e6) {
                    Log.e(TAG, str2, (Throwable) e6);
                    throw th;
                }
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }
}
