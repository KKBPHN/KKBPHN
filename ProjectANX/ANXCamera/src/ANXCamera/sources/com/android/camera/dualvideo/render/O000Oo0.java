package com.android.camera.dualvideo.render;

import android.media.ImageReader;
import android.media.ImageReader.OnImageAvailableListener;

/* compiled from: lambda */
public final /* synthetic */ class O000Oo0 implements OnImageAvailableListener {
    private final /* synthetic */ RenderManager O0OOoO0;

    public /* synthetic */ O000Oo0(RenderManager renderManager) {
        this.O0OOoO0 = renderManager;
    }

    public final void onImageAvailable(ImageReader imageReader) {
        this.O0OOoO0.O000000o(imageReader);
    }
}
