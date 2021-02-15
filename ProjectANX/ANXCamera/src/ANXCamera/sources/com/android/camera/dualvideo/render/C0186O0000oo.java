package com.android.camera.dualvideo.render;

import com.android.camera.dualvideo.render.RegionHelper.UpdatedListener;

/* compiled from: lambda */
/* renamed from: com.android.camera.dualvideo.render.O0000oo reason: case insensitive filesystem */
public final /* synthetic */ class C0186O0000oo implements UpdatedListener {
    private final /* synthetic */ CameraItemManager O0OOoO0;

    public /* synthetic */ C0186O0000oo(CameraItemManager cameraItemManager) {
        this.O0OOoO0 = cameraItemManager;
    }

    public final void onUpdated() {
        this.O0OOoO0.updateRenderableList();
    }
}
