package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000OOoo reason: case insensitive filesystem */
public final /* synthetic */ class C0194O000OOoo implements Predicate {
    public static final /* synthetic */ C0194O000OOoo INSTANCE = new C0194O000OOoo();

    private /* synthetic */ C0194O000OOoo() {
    }

    public final boolean test(Object obj) {
        return ((CameraItemInterface) obj).isVisible();
    }
}
