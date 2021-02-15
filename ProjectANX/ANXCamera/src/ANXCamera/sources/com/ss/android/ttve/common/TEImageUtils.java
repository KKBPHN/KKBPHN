package com.ss.android.ttve.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.android.camera.storage.Storage;
import com.ss.android.vesdk.VELogUtil;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class TEImageUtils {
    private static final String TAG = "TEImageUtils";

    public static void saveBitmap(Bitmap bitmap) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(Storage.JPEG_SUFFIX);
        saveBitmap(bitmap, sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x0088 A[SYNTHETIC, Splitter:B:29:0x0088] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0092 A[SYNTHETIC, Splitter:B:34:0x0092] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x009e A[SYNTHETIC, Splitter:B:41:0x009e] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00a8 A[SYNTHETIC, Splitter:B:46:0x00a8] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBitmap(Bitmap bitmap, String str) {
        FileOutputStream fileOutputStream;
        StringBuilder sb = new StringBuilder();
        sb.append(TEFileUtils.getPath());
        sb.append("/");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("saving Bitmap : ");
        sb3.append(str);
        VELogUtil.i(str2, sb3.toString());
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(sb2);
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream);
                try {
                    bitmap.compress(CompressFormat.JPEG, 100, bufferedOutputStream2);
                    bufferedOutputStream2.flush();
                    try {
                        bufferedOutputStream2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                    String str3 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Bitmap ");
                    sb4.append(str);
                    sb4.append(" saved!");
                    VELogUtil.i(str3, sb4.toString());
                } catch (IOException e3) {
                    e = e3;
                    bufferedOutputStream = bufferedOutputStream2;
                    try {
                        VELogUtil.e(TAG, "Err when saving bitmap...");
                        e.printStackTrace();
                        if (bufferedOutputStream != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (bufferedOutputStream != null) {
                            try {
                                bufferedOutputStream.close();
                            } catch (IOException e4) {
                                e4.printStackTrace();
                            }
                        }
                        if (fileOutputStream != null) {
                            try {
                                fileOutputStream.close();
                            } catch (IOException e5) {
                                e5.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    bufferedOutputStream = bufferedOutputStream2;
                    if (bufferedOutputStream != null) {
                    }
                    if (fileOutputStream != null) {
                    }
                    throw th;
                }
            } catch (IOException e6) {
                e = e6;
                VELogUtil.e(TAG, "Err when saving bitmap...");
                e.printStackTrace();
                if (bufferedOutputStream != null) {
                    try {
                        bufferedOutputStream.close();
                    } catch (IOException e7) {
                        e7.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e8) {
                        e8.printStackTrace();
                    }
                }
            }
        } catch (IOException e9) {
            e = e9;
            fileOutputStream = null;
            VELogUtil.e(TAG, "Err when saving bitmap...");
            e.printStackTrace();
            if (bufferedOutputStream != null) {
            }
            if (fileOutputStream != null) {
            }
        } catch (Throwable th3) {
            th = th3;
            fileOutputStream = null;
            if (bufferedOutputStream != null) {
            }
            if (fileOutputStream != null) {
            }
            throw th;
        }
    }
}
