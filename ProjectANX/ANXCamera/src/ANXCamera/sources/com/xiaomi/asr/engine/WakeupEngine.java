package com.xiaomi.asr.engine;

import com.xiaomi.asr.engine.impl.WakeupEngineImpl;

public class WakeupEngine {
    private WakeupEngine() {
    }

    public static void init(String str) {
        WakeupEngineImpl.getInstance().init(str);
    }

    public static void loadLibraryWrapper(String str) {
        WakeupEngineImpl.getInstance().loadLibraryWrapper(str);
    }

    public static void openLog(boolean z) {
        WakeupEngineImpl.getInstance().openLog(z);
    }

    public static void release() {
        WakeupEngineImpl.getInstance().release();
    }

    public static void restart() {
        WakeupEngineImpl.getInstance().restart();
    }

    public static void saveRecord(boolean z) {
        WakeupEngineImpl.getInstance().saveRecord(z);
    }

    public static void setListener(WVPListener wVPListener) {
        WakeupEngineImpl.getInstance().setListener(wVPListener);
    }

    public static void start() {
        WakeupEngineImpl.getInstance().start(0);
    }

    public static void stop() {
        WakeupEngineImpl.getInstance().stop(5000);
    }

    public static void stop(int i) {
        WakeupEngineImpl.getInstance().stop((long) i);
    }

    public static String wakeupVersion() {
        return WakeupEngineImpl.getInstance().wakeupVersion();
    }
}
