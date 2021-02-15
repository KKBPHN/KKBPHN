package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oO0 reason: case insensitive filesystem */
public final /* synthetic */ class C0216O000oO0 implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ C0216O000oO0(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return RenderManager.O00000Oo(this.O0OOoO0, (RenderSource) obj);
    }
}
