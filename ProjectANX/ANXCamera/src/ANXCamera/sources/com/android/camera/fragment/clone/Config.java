package com.android.camera.fragment.clone;

import com.xiaomi.fenshen.FenShenCam.Mode;

public class Config {
    private static Mode sMode;

    private Config() {
    }

    public static Mode getCloneMode() {
        return sMode;
    }

    public static void resetIfNeed(int i) {
        if (i != 185 && i != 210 && i != 213) {
            sMode = null;
        }
    }

    public static void setCloneMode(Mode mode) {
        sMode = mode;
    }
}
