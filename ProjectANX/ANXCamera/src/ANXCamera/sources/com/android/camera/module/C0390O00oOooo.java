package com.android.camera.module;

import android.graphics.Bitmap;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O00oOooo reason: case insensitive filesystem */
public final /* synthetic */ class C0390O00oOooo implements Runnable {
    private final /* synthetic */ Camera2Module O0OOoO0;
    private final /* synthetic */ String O0OOoOO;
    private final /* synthetic */ Bitmap O0OOoOo;

    public /* synthetic */ C0390O00oOooo(Camera2Module camera2Module, String str, Bitmap bitmap) {
        this.O0OOoO0 = camera2Module;
        this.O0OOoOO = str;
        this.O0OOoOo = bitmap;
    }

    public final void run() {
        this.O0OOoO0.O00000Oo(this.O0OOoOO, this.O0OOoOo);
    }
}
