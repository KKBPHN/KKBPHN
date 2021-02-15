package com.xiaomi.asr.engine;

import com.xiaomi.asr.engine.impl.W2VPEngineImpl;

public class W2VPEngine {
    private W2VPEngine() {
    }

    public static void abortEnrollment() {
        W2VPEngineImpl.getInstance().abortEnrollment();
    }

    public static void cancelEnrollment() {
        W2VPEngineImpl.getInstance().cancelEnrollment();
    }

    public static void commitEnrollment() {
        W2VPEngineImpl.getInstance().commitEnrollment();
    }

    public static void generateModel() {
        W2VPEngineImpl.getInstance().generateModel();
    }

    public static String getAllEnrollRegister() {
        return W2VPEngineImpl.getInstance().getAllRegister();
    }

    public static void init(String str, String str2) {
        W2VPEngineImpl.getInstance().init(str, str2);
    }

    public static void openLog(boolean z) {
        W2VPEngineImpl.getInstance().openLog(z);
    }

    public static void openVoicePrint(boolean z) {
        W2VPEngineImpl.getInstance().openVoicePrint(z);
    }

    public static void release() {
        W2VPEngineImpl.getInstance().release();
    }

    public static void removeAllEnrollRegister() {
        W2VPEngineImpl.getInstance().removeAllRegister();
    }

    public static void saveRecord(boolean z) {
        W2VPEngineImpl.getInstance().saveRecord(z);
    }

    public static void setDebugPath(String str) {
        W2VPEngineImpl.getInstance().setDebugPath(str);
    }

    public static void setListener(WVPListener wVPListener) {
        W2VPEngineImpl.getInstance().setListener(wVPListener);
    }

    public static void start(int i) {
        W2VPEngineImpl.getInstance().start(i);
    }

    public static void startEnrollment(int i) {
        W2VPEngineImpl.getInstance().startEnrollment(i);
    }

    public static String version() {
        return W2VPEngineImpl.getInstance().version();
    }
}
