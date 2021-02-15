package com.xiaomi.asr.engine;

import com.xiaomi.asr.engine.impl.MultiWakeupEngineImpl;

public class MultiWakeupEngine {
    private MultiWakeupEngine() {
    }

    public static void init() {
        MultiWakeupEngineImpl.getInstance().init();
    }

    public static void openLog(boolean z) {
        MultiWakeupEngineImpl.getInstance().openLog(z);
    }

    public static void release() {
        MultiWakeupEngineImpl.getInstance().release();
    }

    public static void restart() {
        MultiWakeupEngineImpl.getInstance().restart();
    }

    public static void saveRecord(boolean z) {
        MultiWakeupEngineImpl.getInstance().saveRecord(z);
    }

    public static void setListener(WVPListener wVPListener) {
        MultiWakeupEngineImpl.getInstance().setListener(wVPListener);
    }

    public static void start() {
        MultiWakeupEngineImpl.getInstance().start(0);
    }

    public static void stop() {
        MultiWakeupEngineImpl.getInstance().stop(5000);
    }

    public static void stop(int i) {
        MultiWakeupEngineImpl.getInstance().stop((long) i);
    }

    public static String wakeupVersion() {
        return MultiWakeupEngineImpl.getInstance().wakeupVersion();
    }
}
