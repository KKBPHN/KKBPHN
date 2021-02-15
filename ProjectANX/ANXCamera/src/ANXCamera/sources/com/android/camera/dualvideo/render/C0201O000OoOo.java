package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OoOo reason: case insensitive filesystem */
public final /* synthetic */ class C0201O000OoOo implements Predicate {
    private final /* synthetic */ RenderManager O0OOoO0;
    private final /* synthetic */ int O0OOoOO;
    private final /* synthetic */ int O0OOoOo;

    public /* synthetic */ C0201O000OoOo(RenderManager renderManager, int i, int i2) {
        this.O0OOoO0 = renderManager;
        this.O0OOoOO = i;
        this.O0OOoOo = i2;
    }

    public final boolean test(Object obj) {
        return this.O0OOoO0.O000000o(this.O0OOoOO, this.O0OOoOo, (CameraItemInterface) obj);
    }
}
