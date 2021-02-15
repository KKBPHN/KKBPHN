package com.android.camera;

import android.graphics.Rect;
import android.graphics.YuvImage;
import com.android.camera.log.Log;
import com.android.camera.storage.Storage;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageHelper {
    public static final String TAG = "ImageHelper";

    private static void compressYuv(byte[] bArr, int i, int i2, int[] iArr, int i3, OutputStream outputStream) {
        YuvImage yuvImage = new YuvImage(bArr, 17, i, i2, iArr);
        yuvImage.compressToJpeg(new Rect(0, 0, i, i2), i3, outputStream);
    }

    public static byte[] encodeNv21ToJpeg(byte[] bArr, int i, int i2, int i3) {
        ByteArrayOutputStream byteArrayOutputStream;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                compressYuv(bArr, i, i2, null, i3, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                Util.closeSafely(byteArrayOutputStream);
                return byteArray;
            } catch (Exception e) {
                e = e;
                String str = TAG;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append("encodeNv21 error, ");
                    sb.append(e.getMessage());
                    Log.w(str, sb.toString());
                    Util.closeSafely(byteArrayOutputStream);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    Util.closeSafely(byteArrayOutputStream);
                    throw th;
                }
            }
        } catch (Exception e2) {
            e = e2;
            byteArrayOutputStream = null;
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("encodeNv21 error, ");
            sb2.append(e.getMessage());
            Log.w(str2, sb2.toString());
            Util.closeSafely(byteArrayOutputStream);
            return null;
        } catch (Throwable th2) {
            th = th2;
            byteArrayOutputStream = null;
            Util.closeSafely(byteArrayOutputStream);
            throw th;
        }
    }

    public static void saveYuvToJpg(byte[] bArr, int i, int i2, int[] iArr, String str) {
        String str2 = TAG;
        if (bArr == null) {
            Log.w(str2, "saveYuvToJpg: null data");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("sdcard/DCIM/Camera/");
        sb.append(str);
        sb.append(Storage.JPEG_SUFFIX);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("saveYuvToJpg: ");
        sb3.append(sb2);
        Log.v(str2, sb3.toString());
        Closeable closeable = null;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(sb2);
            try {
                compressYuv(bArr, i, i2, iArr, 100, new FileOutputStream(sb2));
                Util.closeSafely(fileOutputStream);
            } catch (FileNotFoundException e) {
                e = e;
                closeable = fileOutputStream;
                try {
                    Log.e(str2, e.getMessage(), (Throwable) e);
                    Util.closeSafely(closeable);
                } catch (Throwable th) {
                    th = th;
                    Util.closeSafely(closeable);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                closeable = fileOutputStream;
                Util.closeSafely(closeable);
                throw th;
            }
        } catch (FileNotFoundException e2) {
            e = e2;
            Log.e(str2, e.getMessage(), (Throwable) e);
            Util.closeSafely(closeable);
        }
    }
}
