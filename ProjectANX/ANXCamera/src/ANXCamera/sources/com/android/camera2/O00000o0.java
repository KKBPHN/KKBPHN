package com.android.camera2;

import com.android.camera2.Camera2Proxy.ScreenLightCallback;

/* compiled from: lambda */
public final /* synthetic */ class O00000o0 implements Runnable {
    private final /* synthetic */ ScreenLightCallback O0OOoO0;

    public /* synthetic */ O00000o0(ScreenLightCallback screenLightCallback) {
        this.O0OOoO0 = screenLightCallback;
    }

    public final void run() {
        this.O0OOoO0.stopScreenLight();
    }
}
