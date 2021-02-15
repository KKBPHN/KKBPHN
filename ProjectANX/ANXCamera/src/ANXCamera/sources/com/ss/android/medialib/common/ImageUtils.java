package com.ss.android.medialib.common;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.android.camera.storage.Storage;
import com.ss.android.medialib.FileUtils;
import com.ss.android.vesdk.VELogUtil;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    private static final String TAG = "ImageUtils";

    public static void saveBitmap(Bitmap bitmap) {
        StringBuilder sb = new StringBuilder();
        sb.append(System.currentTimeMillis());
        sb.append(Storage.JPEG_SUFFIX);
        saveBitmap(bitmap, sb.toString());
    }

    public static void saveBitmap(Bitmap bitmap, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(FileUtils.getPath());
        sb.append("/");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("saving Bitmap : ");
        sb3.append(str);
        VELogUtil.i(str2, sb3.toString());
        saveBitmapWithPath(bitmap, sb2, CompressFormat.JPEG);
        String str3 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Bitmap ");
        sb4.append(str);
        sb4.append(" saved!");
        VELogUtil.i(str3, sb4.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0071 A[SYNTHETIC, Splitter:B:30:0x0071] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x007b A[SYNTHETIC, Splitter:B:35:0x007b] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x0087 A[SYNTHETIC, Splitter:B:42:0x0087] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0091 A[SYNTHETIC, Splitter:B:47:0x0091] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void saveBitmapWithPath(Bitmap bitmap, String str, CompressFormat compressFormat) {
        FileOutputStream fileOutputStream;
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        String str3 = "Bitmap ";
        sb.append(str3);
        sb.append(str);
        sb.append("saving");
        VELogUtil.i(str2, sb.toString());
        BufferedOutputStream bufferedOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(str);
            try {
                BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(fileOutputStream);
                try {
                    bitmap.compress(compressFormat, 100, bufferedOutputStream2);
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
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str3);
                    sb2.append(str);
                    sb2.append(" saved!");
                    VELogUtil.i(str4, sb2.toString());
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
                }
                if (fileOutputStream != null) {
                }
            }
        } catch (IOException e7) {
            e = e7;
            fileOutputStream = null;
            VELogUtil.e(TAG, "Err when saving bitmap...");
            e.printStackTrace();
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e8) {
                    e8.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e9) {
                    e9.printStackTrace();
                }
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
