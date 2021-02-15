package com.android.camera.module;

import com.android.camera.protocol.ModeProtocol.TopAlert;

/* compiled from: lambda */
public final /* synthetic */ class O000o0 implements Runnable {
    private final /* synthetic */ TopAlert O0OOoO0;

    public /* synthetic */ O000o0(TopAlert topAlert) {
        this.O0OOoO0 = topAlert;
    }

    public final void run() {
        this.O0OOoO0.hideAlert();
    }
}
