package com.android.camera.dualvideo.util;

import com.android.camera.Util;

public final class Assert {
    public static void check(boolean z) {
        if (Util.DEBUG && !z) {
            throw new AssertionError("Assertion failed");
        }
    }

    public static void check(boolean z, Runnable runnable) {
        if (Util.DEBUG && !z) {
            runnable.run();
            throw new AssertionError("Assertion failed");
        }
    }

    public static void check(boolean z, String str) {
        if (Util.DEBUG && !z) {
            throw new AssertionError(str);
        }
    }
}
