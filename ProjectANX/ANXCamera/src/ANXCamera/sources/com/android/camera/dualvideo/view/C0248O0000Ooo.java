package com.android.camera.dualvideo.view;

import com.android.camera.dualvideo.render.CameraItemInterface;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.view.O0000Ooo reason: case insensitive filesystem */
public final /* synthetic */ class C0248O0000Ooo implements Predicate {
    private final /* synthetic */ int O0OOoO0;

    public /* synthetic */ C0248O0000Ooo(int i) {
        this.O0OOoO0 = i;
    }

    public final boolean test(Object obj) {
        return TouchHelper.O000000o(this.O0OOoO0, (CameraItemInterface) obj);
    }
}
