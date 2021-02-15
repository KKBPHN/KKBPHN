package com.android.camera.dualvideo.util;

import com.android.camera.dualvideo.render.LayoutType;
import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.util.O0000o0O reason: case insensitive filesystem */
public final /* synthetic */ class C0241O0000o0O implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0241O0000o0O(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return DualVideoConfigManager.O00000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
