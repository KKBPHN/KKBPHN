package com.android.camera.resource;

import com.android.camera.log.Log;

public class RequestHelper {
    private static String TAG = "RequestHelper";

    static {
        try {
            System.loadLibrary("com.xiaomi.camera.requestutil");
        } catch (Throwable th) {
            Log.e(TAG, "load requestutil.so failed.", th);
        }
    }

    private static native byte[] genSubtitleAccessAppID();

    private static native byte[] genSubtitleAccessAppKey();

    private static native byte[] genSubtitleAccessAppSecret();

    private static native byte[] genTMAccessKey();

    private static native byte[] generate(long j, long j2);

    public static String getIdentityID() {
        return "MIUICamera";
    }

    public static String getSubtitleAccessAppID() {
        return new String(genSubtitleAccessAppID());
    }

    public static String getSubtitleAccessAppKey() {
        return new String(genSubtitleAccessAppKey());
    }

    public static String getSubtitleAccessAppSecret() {
        return new String(genSubtitleAccessAppSecret());
    }

    public static String getTMMusicAccessKey() {
        return new String(genTMAccessKey());
    }

    public static String md5(long j, long j2) {
        byte[] generate = generate(j, j2);
        String str = "";
        for (byte b : generate) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(Integer.toHexString((b & -1) | 0).substring(6));
            str = sb.toString();
        }
        return str;
    }
}
