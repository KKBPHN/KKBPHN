package com.android.camera.dualvideo.render;

import com.android.camera.module.encoder.RenderHandler;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oo0 reason: case insensitive filesystem */
public final /* synthetic */ class C0229O000oo0 implements Consumer {
    public static final /* synthetic */ C0229O000oo0 INSTANCE = new C0229O000oo0();

    private /* synthetic */ C0229O000oo0() {
    }

    public final void accept(Object obj) {
        ((RenderHandler) obj).release();
    }
}
