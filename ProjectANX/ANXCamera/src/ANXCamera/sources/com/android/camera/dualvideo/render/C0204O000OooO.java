package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OooO reason: case insensitive filesystem */
public final /* synthetic */ class C0204O000OooO implements Predicate {
    private final /* synthetic */ float O0OOoO0;
    private final /* synthetic */ float O0OOoOO;

    public /* synthetic */ C0204O000OooO(float f, float f2) {
        this.O0OOoO0 = f;
        this.O0OOoOO = f2;
    }

    public final boolean test(Object obj) {
        return RenderManager.O00000o0(this.O0OOoO0, this.O0OOoOO, (CameraItemInterface) obj);
    }
}
