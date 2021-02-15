package com.android.camera.module;

import com.android.camera.protocol.ModeProtocol.RecordState;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000o0OO reason: case insensitive filesystem */
public final /* synthetic */ class C0376O000o0OO implements Runnable {
    private final /* synthetic */ RecordState O0OOoO0;

    public /* synthetic */ C0376O000o0OO(RecordState recordState) {
        this.O0OOoO0 = recordState;
    }

    public final void run() {
        this.O0OOoO0.onFinish();
    }
}
