package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.util.O0000o0o reason: case insensitive filesystem */
public final /* synthetic */ class C0242O0000o0o implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0242O0000o0o(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O00000oO(this.O0OOoO0, (ConfigItem) obj);
    }
}
