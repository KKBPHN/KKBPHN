package com.android.camera.module;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O000oOO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0387O000oOO0 implements Runnable {
    private final /* synthetic */ Camera2Module O0OOoO0;

    public /* synthetic */ C0387O000oOO0(Camera2Module camera2Module) {
        this.O0OOoO0 = camera2Module;
    }

    public final void run() {
        this.O0OOoO0.handlePendingScreenSlide();
    }
}
