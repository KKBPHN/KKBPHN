package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oo0O reason: case insensitive filesystem */
public final /* synthetic */ class C0230O000oo0O implements Predicate {
    public static final /* synthetic */ C0230O000oo0O INSTANCE = new C0230O000oo0O();

    private /* synthetic */ C0230O000oo0O() {
    }

    public final boolean test(Object obj) {
        return ((CameraItemInterface) obj).isPressedInSelectWindow();
    }
}
