package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oO00 reason: case insensitive filesystem */
public final /* synthetic */ class C0217O000oO00 implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0217O000oO00(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return RenderManager.O000000o(this.O0OOoO0, (CameraItemInterface) obj);
    }
}
