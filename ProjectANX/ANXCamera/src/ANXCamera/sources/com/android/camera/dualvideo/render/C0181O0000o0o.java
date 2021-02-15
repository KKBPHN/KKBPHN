package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.DualVideoConfigManager.ConfigItem;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000o0o reason: case insensitive filesystem */
public final /* synthetic */ class C0181O0000o0o implements Predicate {
    private final /* synthetic */ CameraItemInterface O0OOoO0;

    public /* synthetic */ C0181O0000o0o(CameraItemInterface cameraItemInterface) {
        this.O0OOoO0 = cameraItemInterface;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (ConfigItem) obj);
    }
}
