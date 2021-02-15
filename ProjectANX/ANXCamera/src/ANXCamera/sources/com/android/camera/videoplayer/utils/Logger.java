package com.android.camera.videoplayer.utils;

import android.util.Log;

public class Logger {
    private Logger() {
    }

    private static String attachThreadId(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(Thread.currentThread().getId());
        sb.append(" ");
        sb.append(str);
        return sb.toString();
    }

    public static int d(String str, String str2) {
        return Log.d(str, attachThreadId(str2));
    }

    public static int err(String str, String str2) {
        return Log.e(str, attachThreadId(str2));
    }

    public static int err(String str, String str2, Throwable th) {
        return Log.e(str, attachThreadId(str2), th);
    }

    public static int inf(String str, String str2) {
        return Log.i(str, attachThreadId(str2));
    }

    public static int v(String str, String str2) {
        return Log.v(str, attachThreadId(str2));
    }

    public static int w(String str, String str2) {
        return Log.w(str, attachThreadId(str2));
    }
}
