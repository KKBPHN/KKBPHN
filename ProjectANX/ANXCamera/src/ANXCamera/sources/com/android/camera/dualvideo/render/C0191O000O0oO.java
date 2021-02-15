package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O000O0oO reason: case insensitive filesystem */
public final /* synthetic */ class C0191O000O0oO implements Predicate {
    private final /* synthetic */ ConfigItem O0OOoO0;

    public /* synthetic */ C0191O000O0oO(ConfigItem configItem) {
        this.O0OOoO0 = configItem;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (CameraItemInterface) obj);
    }
}
