package com.android.camera.module.impl.component;

import com.android.camera.module.Camera2Module;

/* compiled from: lambda */
public final /* synthetic */ class O000O00o implements Runnable {
    private final /* synthetic */ Camera2Module O0OOoO0;
    private final /* synthetic */ boolean O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ O000O00o(Camera2Module camera2Module, boolean z, int i) {
        this.O0OOoO0 = camera2Module;
        this.O0OOoOO = z;
        this.O0OOoOo = i;
    }

    public final void run() {
        MiAsdDetectImpl.O000000o(this.O0OOoO0, this.O0OOoOO, this.O0OOoOo);
    }
}
