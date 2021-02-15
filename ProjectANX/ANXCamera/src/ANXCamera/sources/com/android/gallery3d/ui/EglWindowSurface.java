package com.android.gallery3d.ui;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import com.xiaomi.camera.liveshot.gles.EglCore;
import com.xiaomi.camera.liveshot.gles.EglSurfaceBase;

@WorkerThread
public class EglWindowSurface extends EglSurfaceBase {
    private boolean mAvailability = false;
    private Object mLock = new Object();

    public EglWindowSurface(@NonNull EglCore eglCore, SurfaceTexture surfaceTexture) {
        super(eglCore);
        init(surfaceTexture);
    }

    public EglWindowSurface(@NonNull EglCore eglCore, Surface surface) {
        super(eglCore);
        init(surface);
    }

    private void init(Object obj) {
        synchronized (this.mLock) {
            this.mEGLSurface = obj instanceof Surface ? this.mEglCore.createWindowSurface((Surface) obj) : this.mEglCore.createWindowSurface((SurfaceTexture) obj);
            this.mAvailability = true;
        }
    }

    public boolean makeCurrent() {
        synchronized (this.mLock) {
            if (!this.mAvailability) {
                return false;
            }
            boolean makeCurrent = super.makeCurrent();
            return makeCurrent;
        }
    }

    public void releaseEglSurface() {
        synchronized (this.mLock) {
            super.releaseEglSurface();
            this.mAvailability = false;
        }
    }

    public boolean swapBuffers() {
        synchronized (this.mLock) {
            if (!this.mAvailability) {
                return false;
            }
            boolean swapBuffers = super.swapBuffers();
            return swapBuffers;
        }
    }
}
