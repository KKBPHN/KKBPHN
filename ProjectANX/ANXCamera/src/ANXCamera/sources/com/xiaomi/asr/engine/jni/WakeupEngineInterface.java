package com.xiaomi.asr.engine.jni;

public class WakeupEngineInterface {
    public static void loadLibrary(String str) {
        System.loadLibrary(str);
    }

    public native String wakeupDebugFeedData(byte[] bArr, int i);

    public native int wakeupDestroy();

    public native int wakeupFeedData(byte[] bArr, int i, int i2);

    public native int wakeupGetFrameSize();

    public native int wakeupInit(String str);

    public native int wakeupReset();

    public native String wakeupVersion();
}
