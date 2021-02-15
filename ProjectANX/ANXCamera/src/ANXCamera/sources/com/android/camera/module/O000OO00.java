package com.android.camera.module;

import com.xiaomi.fenshen.FenShenCam.Message;

/* compiled from: lambda */
public final /* synthetic */ class O000OO00 implements Runnable {
    private final /* synthetic */ CloneModule O0OOoO0;
    private final /* synthetic */ Message O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ O000OO00(CloneModule cloneModule, Message message, int i) {
        this.O0OOoO0 = cloneModule;
        this.O0OOoOO = message;
        this.O0OOoOo = i;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo);
    }
}
