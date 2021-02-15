package com.android.camera.zoommap;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;

/* compiled from: lambda */
public final /* synthetic */ class O00000o implements OnFrameAvailableListener {
    private final /* synthetic */ ZoomMapController O0OOoO0;

    public /* synthetic */ O00000o(ZoomMapController zoomMapController) {
        this.O0OOoO0 = zoomMapController;
    }

    public final void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.O0OOoO0.O000000o(surfaceTexture);
    }
}
