package com.xiaomi.asr.engine.jni;

public class VoicePrintInterface {
    public static void loadLibrary(String str) {
        System.loadLibrary(str);
    }

    public native int voiceprintDestroy();

    public native boolean voiceprintGenerateModel(String str);

    public native String voiceprintGetAllRegister();

    public native int voiceprintInit(String str, String str2);

    public native int voiceprintReEnrollRegister(String str, byte[] bArr, int i, int i2);

    public native String voiceprintRecognition(byte[] bArr, int i, int i2);

    public native int voiceprintRegister(String str, byte[] bArr);

    public native void voiceprintRemoveAll();

    public native String voiceprintVersion();
}
