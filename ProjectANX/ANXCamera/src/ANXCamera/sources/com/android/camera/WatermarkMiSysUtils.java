package com.android.camera;

import com.android.camera.log.Log;

public class WatermarkMiSysUtils {
    private static final String TAG = "WatermarkMiSysUtils";

    public static int eraseFile(String str) {
        Log.e(TAG, "eraseFile NO Permission!!!!");
        return -1;
    }

    public static int isFileExist(String str) {
        Log.e(TAG, "isFileExist NO Permission!!!!");
        return -1;
    }

    public static boolean writeFileToPersist(byte[] bArr, String str) {
        Log.e(TAG, "writeFileToPersist NO Permission!!!!");
        return false;
    }
}
