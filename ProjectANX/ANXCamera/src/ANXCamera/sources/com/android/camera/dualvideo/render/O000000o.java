package com.android.camera.dualvideo.render;

import java.util.function.Predicate;

/* compiled from: lambda */
public final /* synthetic */ class O000000o implements Predicate {
    public static final /* synthetic */ O000000o INSTANCE = new O000000o();

    private /* synthetic */ O000000o() {
    }

    public final boolean test(Object obj) {
        return ((CameraItemInterface) obj).isAnimating();
    }
}
