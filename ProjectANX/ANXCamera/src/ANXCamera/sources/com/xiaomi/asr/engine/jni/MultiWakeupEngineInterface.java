package com.xiaomi.asr.engine.jni;

public class MultiWakeupEngineInterface {
    public static void loadLibrary(String str) {
        System.loadLibrary(str);
    }

    public native String wakeupDebugFeedData(byte[] bArr, int i);

    public native int wakeupDestroy();

    public native WakeupInfo wakeupFeedData(byte[] bArr, int i, int i2, WakeupInfo wakeupInfo);

    public native int wakeupGetFrameSize();

    public native int wakeupInit(String str);

    public native int wakeupReset();

    public native String wakeupVersion();
}
