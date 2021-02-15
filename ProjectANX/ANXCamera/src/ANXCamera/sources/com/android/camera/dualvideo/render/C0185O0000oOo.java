package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.util.UserSelectData;
import java.util.function.Predicate;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000oOo reason: case insensitive filesystem */
public final /* synthetic */ class C0185O0000oOo implements Predicate {
    private final /* synthetic */ CameraItem O0OOoO0;

    public /* synthetic */ C0185O0000oOo(CameraItem cameraItem) {
        this.O0OOoO0 = cameraItem;
    }

    public final boolean test(Object obj) {
        return CameraItemManager.O000000o(this.O0OOoO0, (UserSelectData) obj);
    }
}
