package com.miui.internal.core;

import android.app.Application;
import java.util.Map;

public class SdkManager {
    private SdkManager() {
    }

    public static int initialize(Application application, Map map) {
        return miui.core.SdkManager.initialize(application, map);
    }

    public static int start(Map map) {
        return miui.core.SdkManager.start(map);
    }

    public static boolean supportUpdate(Map map) {
        return miui.core.SdkManager.supportUpdate(map);
    }

    public static boolean update(Map map) {
        return miui.core.SdkManager.update(map);
    }
}
