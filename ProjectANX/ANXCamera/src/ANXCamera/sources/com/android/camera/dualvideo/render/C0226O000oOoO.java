package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000oOoO reason: case insensitive filesystem */
public final /* synthetic */ class C0226O000oOoO implements Predicate {
    private final /* synthetic */ LayoutType O0OOoO0;

    public /* synthetic */ C0226O000oOoO(LayoutType layoutType) {
        this.O0OOoO0 = layoutType;
    }

    public final boolean test(Object obj) {
        return RenderUtil.O000000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
