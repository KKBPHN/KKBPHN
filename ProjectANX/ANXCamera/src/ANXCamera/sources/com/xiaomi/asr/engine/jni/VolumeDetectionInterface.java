package com.xiaomi.asr.engine.jni;

public class VolumeDetectionInterface {
    static {
        System.loadLibrary("volume-detection");
    }

    public native void vdDestroy();

    public native int vdDetection(byte[] bArr, int i, float[] fArr);

    public native int vdInit();

    public native void vdReset();

    public native int vdStopDetection(float[] fArr);
}
