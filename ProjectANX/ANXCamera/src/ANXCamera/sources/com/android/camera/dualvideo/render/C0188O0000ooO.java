package com.android.camera.dualvideo.render;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000ooO reason: case insensitive filesystem */
public final /* synthetic */ class C0188O0000ooO implements Consumer {
    private final /* synthetic */ CameraItemManager O0OOoO0;
    private final /* synthetic */ float O0OOoOO;
    private final /* synthetic */ List O0OOoOo;

    public /* synthetic */ C0188O0000ooO(CameraItemManager cameraItemManager, float f, List list) {
        this.O0OOoO0 = cameraItemManager;
        this.O0OOoOO = f;
        this.O0OOoOo = list;
    }

    public final void accept(Object obj) {
        this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (CameraItemInterface) obj);
    }
}
