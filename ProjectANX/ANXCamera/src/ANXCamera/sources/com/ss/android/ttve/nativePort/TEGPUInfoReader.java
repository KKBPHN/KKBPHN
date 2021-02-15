package com.ss.android.ttve.nativePort;

import androidx.annotation.Keep;

@Keep
public class TEGPUInfoReader {
    static {
        TENativeLibsLoader.loadLibrary();
    }

    public static native String nativeGetGLVersion();

    public static native String nativeGetGPURenderer();
}
