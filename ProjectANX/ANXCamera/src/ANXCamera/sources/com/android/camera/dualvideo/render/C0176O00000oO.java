package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O00000oO reason: case insensitive filesystem */
public final /* synthetic */ class C0176O00000oO implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0176O00000oO(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
