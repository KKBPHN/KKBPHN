package com.bumptech.glide.util;

import android.annotation.TargetApi;
import android.os.Build.VERSION;
import android.os.SystemClock;

public final class LogTime {
    private static final double MILLIS_MULTIPLIER;

    static {
        double d = 1.0d;
        if (VERSION.SDK_INT >= 17) {
            d = 1.0d / Math.pow(10.0d, 6.0d);
        }
        MILLIS_MULTIPLIER = d;
    }

    private LogTime() {
    }

    public static double getElapsedMillis(long j) {
        return ((double) (getLogTime() - j)) * MILLIS_MULTIPLIER;
    }

    @TargetApi(17)
    public static long getLogTime() {
        return VERSION.SDK_INT >= 17 ? SystemClock.elapsedRealtimeNanos() : SystemClock.uptimeMillis();
    }
}
