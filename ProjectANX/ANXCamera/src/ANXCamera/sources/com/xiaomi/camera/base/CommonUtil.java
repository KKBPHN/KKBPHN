package com.xiaomi.camera.base;

import android.content.Context;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public final class CommonUtil {
    private static final String CAPTURE_RESULT_EXTRA_CLASS = "android.hardware.camera2.impl.CaptureResultExtras";
    private static final String PHYSICAL_CAPTURE_RESULT_CLASS = "android.hardware.camera2.impl.PhysicalCaptureResultInfo";
    private static final String TAG = "CommonUtil";

    private CommonUtil() {
    }

    private static boolean saveCameraCalibrationToFile(Context context, byte[] bArr, String str) {
        String str2 = "saveCameraCalibrationToFile: failed!";
        String str3 = TAG;
        boolean z = false;
        if (!(bArr == null || context == null)) {
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = context.openFileOutput(str, 0);
                fileOutputStream.write(bArr);
                z = true;
                try {
                } catch (Exception e) {
                    Log.w(str3, str2, e);
                }
            } catch (FileNotFoundException e2) {
                Log.w(str3, "saveCameraCalibrationToFile: FileNotFoundException", e2);
            } catch (IOException e3) {
                Log.w(str3, "saveCameraCalibrationToFile: IOException", e3);
            } finally {
                try {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e4) {
                    Log.w(str3, str2, e4);
                }
            }
        }
        return z;
    }

    public static boolean saveCameraCalibrationToFile(Context context, byte[] bArr, boolean z) {
        return saveCameraCalibrationToFile(context, bArr, z ? "front_dual_camera_caldata.bin" : "back_dual_camera_caldata.bin");
    }
}
