package com.xiaomi.asr.engine;

import com.xiaomi.asr.engine.impl.WVPEngineImpl;

public class WVPEngine {
    private WVPEngine() {
    }

    public static void abortEnrollment() {
        WVPEngineImpl.getInstance().abortEnrollment();
    }

    public static void cancelEnrollment() {
        WVPEngineImpl.getInstance().cancelEnrollment();
    }

    public static void commitEnrollment() {
        WVPEngineImpl.getInstance().commitEnrollment();
    }

    public static void generateModel() {
        WVPEngineImpl.getInstance().generateModel();
    }

    public static String getAllEnrollRegister() {
        return WVPEngineImpl.getInstance().getAllRegister();
    }

    public static void init(String str, String str2) {
        WVPEngineImpl.getInstance().init(str, str2);
    }

    public static void openLog(boolean z) {
        WVPEngineImpl.getInstance().openLog(z);
    }

    public static void openVoicePrint(boolean z) {
        WVPEngineImpl.getInstance().openVoicePrint(z);
    }

    public static void release() {
        WVPEngineImpl.getInstance().release();
    }

    public static void removeAllEnrollRegister() {
        WVPEngineImpl.getInstance().removeAllRegister();
    }

    public static void restart() {
        WVPEngineImpl.getInstance().restart();
    }

    public static void saveRecord(boolean z) {
        WVPEngineImpl.getInstance().saveRecord(z);
    }

    public static void setDebugPath(String str) {
        WVPEngineImpl.getInstance().setDebugPath(str);
    }

    public static void setListener(WVPListener wVPListener) {
        WVPEngineImpl.getInstance().setListener(wVPListener);
    }

    public static void start() {
        WVPEngineImpl.getInstance().start(0);
    }

    public static void startEnrollment(int i) {
        WVPEngineImpl.getInstance().startEnrollment(i);
    }

    public static void stop() {
        WVPEngineImpl.getInstance().stop(5000);
    }

    public static String version() {
        return WVPEngineImpl.getInstance().version();
    }
}
