package com.ss.android.ttve.nativePort;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;

@Keep
public class TELogcat {
    static {
        TENativeLibsLoader.loadLibrary();
    }

    public static void Log(byte b, @NonNull String str, @NonNull String str2) {
        nativeLog(b, str, str2);
    }

    private static native void nativeLog(byte b, String str, String str2);

    private static native void nativeSetLogLevel(byte b);

    public static void setLogLevel(byte b) {
        nativeSetLogLevel(b);
    }
}
