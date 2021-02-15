package com.android.camera.module;

/* compiled from: lambda */
public final /* synthetic */ class O000OOo0 implements Runnable {
    private final /* synthetic */ DollyZoomModule O0OOoO0;
    private final /* synthetic */ int O0OOoOO;
    private final /* synthetic */ float O0OOoOo;
    private final /* synthetic */ int O0OOoo0;

    public /* synthetic */ O000OOo0(DollyZoomModule dollyZoomModule, int i, float f, int i2) {
        this.O0OOoO0 = dollyZoomModule;
        this.O0OOoOO = i;
        this.O0OOoOo = f;
        this.O0OOoo0 = i2;
    }

    public final void run() {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, this.O0OOoo0);
    }
}
