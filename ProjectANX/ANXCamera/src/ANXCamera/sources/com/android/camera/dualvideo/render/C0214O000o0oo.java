package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000o0oo reason: case insensitive filesystem */
public final /* synthetic */ class C0214O000o0oo implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ C0214O000o0oo(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return RenderManager.O000000o(this.O0OOoO0, (RenderSource) obj);
    }
}
