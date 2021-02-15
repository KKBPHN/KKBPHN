package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oo0o reason: case insensitive filesystem */
public final /* synthetic */ class C0231O000oo0o implements Predicate {
    public static final /* synthetic */ C0231O000oo0o INSTANCE = new C0231O000oo0o();

    private /* synthetic */ C0231O000oo0o() {
    }

    public final boolean test(Object obj) {
        return ((RenderSource) obj).canDraw();
    }
}
