package com.android.camera.module;

import android.graphics.Bitmap;
import android.util.Size;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;

/* compiled from: lambda */
/* renamed from: com.android.camera.module.O0000oOO reason: case insensitive filesystem */
public final /* synthetic */ class C0350O0000oOO implements Runnable {
    private final /* synthetic */ ActionProcessing O0OOoO0;
    private final /* synthetic */ Bitmap O0OOoOO;
    private final /* synthetic */ float[] O0OOoOo;
    private final /* synthetic */ Size O0OOoo0;

    public /* synthetic */ C0350O0000oOO(ActionProcessing actionProcessing, Bitmap bitmap, float[] fArr, Size size) {
        this.O0OOoO0 = actionProcessing;
        this.O0OOoOO = bitmap;
        this.O0OOoOo = fArr;
        this.O0OOoo0 = size;
    }

    public final void run() {
        Camera2Module.O000000o(this.O0OOoO0, this.O0OOoOO, this.O0OOoOo, this.O0OOoo0);
    }
}
