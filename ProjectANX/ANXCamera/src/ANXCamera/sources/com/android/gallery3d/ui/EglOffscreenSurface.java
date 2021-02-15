package com.android.gallery3d.ui;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import com.xiaomi.camera.liveshot.gles.EglCore;
import com.xiaomi.camera.liveshot.gles.EglSurfaceBase;

@WorkerThread
public class EglOffscreenSurface extends EglSurfaceBase {
    public EglOffscreenSurface(@NonNull EglCore eglCore, int i, int i2) {
        super(eglCore);
        this.mEGLSurface = this.mEglCore.createOffscreenSurface(i, i2);
        this.mWidth = i;
        this.mHeight = i2;
    }
}
