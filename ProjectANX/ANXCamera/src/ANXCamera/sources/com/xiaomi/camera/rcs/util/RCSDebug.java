package com.xiaomi.camera.rcs.util;

import com.android.camera.log.Log;

public class RCSDebug {
    private static final boolean LOGGING_ENABLED = true;
    private static final String LOG_TAG_PREFIX = "CAM_RCS_";

    public static String createTag(Class cls) {
        StringBuilder sb = new StringBuilder();
        sb.append(LOG_TAG_PREFIX);
        sb.append(cls.getSimpleName());
        return sb.toString();
    }

    public static void d(String str, String str2) {
        println(3, str, str2);
    }

    public static void d(String str, String str2, Throwable th) {
        println(3, str, str2, th);
    }

    public static void e(String str, String str2) {
        println(6, str, str2);
    }

    public static void e(String str, String str2, Throwable th) {
        println(6, str, str2, th);
    }

    public static void i(String str, String str2) {
        println(4, str, str2);
    }

    public static void i(String str, String str2, Throwable th) {
        println(4, str, str2, th);
    }

    private static void println(int i, String str, String str2) {
        Log.println(i, str, str2);
    }

    private static void println(int i, String str, String str2, Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(10);
        sb.append(Log.getStackTraceString(th));
        Log.println(i, str, sb.toString());
    }

    public static void w(String str, String str2) {
        println(5, str, str2);
    }

    public static void w(String str, String str2, Throwable th) {
        println(5, str, str2, th);
    }
}
