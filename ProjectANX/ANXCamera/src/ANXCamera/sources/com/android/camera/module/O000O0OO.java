package com.android.camera.module;

import com.xiaomi.fenshen.FenShenCam;

/* compiled from: lambda */
public final /* synthetic */ class O000O0OO implements Runnable {
    public static final /* synthetic */ O000O0OO INSTANCE = new O000O0OO();

    private /* synthetic */ O000O0OO() {
    }

    public final void run() {
        FenShenCam.release();
    }
}
