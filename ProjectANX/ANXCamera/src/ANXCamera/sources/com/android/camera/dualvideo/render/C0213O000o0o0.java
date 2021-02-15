package com.android.camera.dualvideo.render;

import com.android.camera.module.encoder.RenderHandler;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000o0o0 reason: case insensitive filesystem */
public final /* synthetic */ class C0213O000o0o0 implements Consumer {
    private final /* synthetic */ List O0OOoO0;

    public /* synthetic */ C0213O000o0o0(List list) {
        this.O0OOoO0 = list;
    }

    public final void accept(Object obj) {
        ((RenderHandler) obj).draw(this.O0OOoO0);
    }
}
