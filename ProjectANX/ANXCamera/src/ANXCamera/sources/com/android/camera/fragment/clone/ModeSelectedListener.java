package com.android.camera.fragment.clone;

import com.xiaomi.fenshen.FenShenCam.Mode;

public interface ModeSelectedListener {
    void onModeReady();

    void onModeSelected(Mode mode);
}
