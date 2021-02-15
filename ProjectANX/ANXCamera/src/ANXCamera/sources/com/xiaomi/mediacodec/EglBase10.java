package com.xiaomi.mediacodec;

import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.SurfaceHolder;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

public class EglBase10 extends EglBase {
    private static final int EGL_CONTEXT_CLIENT_VERSION = 12440;
    private final EGL10 egl = ((EGL10) EGLContext.getEGL());
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay = getEglDisplay();
    private EGLSurface eglSurface = EGL10.EGL_NO_SURFACE;

    public class Context extends Context {
        /* access modifiers changed from: private */
        public final EGLContext eglContext;

        public Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }
    }

    public EglBase10(Context context, int[] iArr) {
        this.eglConfig = getEglConfig(this.eglDisplay, iArr);
        this.eglContext = createEglContext(context, this.eglDisplay, this.eglConfig);
    }

    private void checkIsNotReleased() {
        if (this.eglDisplay == EGL10.EGL_NO_DISPLAY || this.eglContext == EGL10.EGL_NO_CONTEXT || this.eglConfig == null) {
            throw new RuntimeException("This object has been released");
        }
    }

    private EGLContext createEglContext(Context context, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        EGLContext eglCreateContext;
        if (context == null || context.eglContext != EGL10.EGL_NO_CONTEXT) {
            int[] iArr = {EGL_CONTEXT_CLIENT_VERSION, 2, 12344};
            EGLContext access$000 = context == null ? EGL10.EGL_NO_CONTEXT : context.eglContext;
            synchronized (EglBase.lock) {
                eglCreateContext = this.egl.eglCreateContext(eGLDisplay, eGLConfig, access$000, iArr);
            }
            if (eglCreateContext != null) {
                return eglCreateContext;
            }
            throw new RuntimeException("Failed to create EGL context");
        }
        throw new RuntimeException("Invalid sharedContext");
    }

    private void createSurfaceImpl(Object obj) {
        if ((obj instanceof SurfaceHolder) || (obj instanceof SurfaceTexture)) {
            checkIsNotReleased();
            if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
                this.eglSurface = this.egl.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, obj, new int[]{12344});
                if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
                    throw new RuntimeException("Failed to create window surface");
                }
                return;
            }
            throw new RuntimeException("Already has an EGLSurface");
        }
        throw new RuntimeException("Input must be either a SurfaceHodler or SurfaceTexture");
    }

    public static Context getCurrentContext10() {
        return new Context(((EGL10) EGLContext.getEGL()).eglGetCurrentContext());
    }

    private EGLConfig getEglConfig(EGLDisplay eGLDisplay, int[] iArr) {
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        int[] iArr2 = new int[1];
        if (!this.egl.eglChooseConfig(eGLDisplay, iArr, eGLConfigArr, eGLConfigArr.length, iArr2)) {
            throw new RuntimeException("eglChooseConfig failed");
        } else if (iArr2[0] > 0) {
            EGLConfig eGLConfig = eGLConfigArr[0];
            if (eGLConfig != null) {
                return eGLConfig;
            }
            throw new RuntimeException("eglChooseConfig returned null");
        } else {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
    }

    private EGLDisplay getEglDisplay() {
        EGLDisplay eglGetDisplay = this.egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        if (eglGetDisplay != EGL10.EGL_NO_DISPLAY) {
            if (this.egl.eglInitialize(eglGetDisplay, new int[2])) {
                return eglGetDisplay;
            }
            throw new RuntimeException("Unable to initalize EGL10");
        }
        throw new RuntimeException("Unable to get EGL10 display");
    }

    private int querySurfaceType(int i) {
        int[] iArr = new int[1];
        this.egl.eglQuerySurface(this.eglDisplay, this.eglSurface, i, iArr);
        return iArr[0];
    }

    public void createPbufferSurface(int i, int i2) {
        checkIsNotReleased();
        if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
            this.eglSurface = this.egl.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, new int[]{12375, i, 12374, i2, 12344});
            if (this.eglSurface == EGL10.EGL_NO_SURFACE) {
                throw new RuntimeException("Failed to carete pixel buffer surface");
            }
            return;
        }
        throw new RuntimeException("Already has an EGLSurface");
    }

    public void createSurface(SurfaceTexture surfaceTexture) {
        createSurfaceImpl(surfaceTexture);
    }

    public void createSurface(Surface surface) {
    }

    public void detachCurrent() {
        synchronized (EglBase.lock) {
            if (!this.egl.eglMakeCurrent(this.eglDisplay, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
                throw new RuntimeException("detachCurrent failed");
            }
        }
    }

    public Context getEglBaseContext() {
        return new Context(this.eglContext);
    }

    public int getSurfaceHeight() {
        return querySurfaceType(12374);
    }

    public int getSurfaceWidth() {
        return querySurfaceType(12375);
    }

    public boolean hasSurface() {
        return this.eglSurface != EGL10.EGL_NO_SURFACE;
    }

    public void makeCurrent() {
        checkIsNotReleased();
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            synchronized (EglBase.lock) {
                if (!this.egl.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
                    throw new RuntimeException("eglMakeCurrent failed");
                }
            }
            return;
        }
        throw new RuntimeException("No EGLSurface can't make current");
    }

    public void release() {
        checkIsNotReleased();
        releaseSuface();
        detachCurrent();
        this.egl.eglDestroyContext(this.eglDisplay, this.eglContext);
        this.egl.eglTerminate(this.eglDisplay);
        this.eglContext = EGL10.EGL_NO_CONTEXT;
        this.eglDisplay = EGL10.EGL_NO_DISPLAY;
        this.eglConfig = null;
    }

    public void releaseSuface() {
        EGLSurface eGLSurface = this.eglSurface;
        if (eGLSurface != EGL10.EGL_NO_SURFACE) {
            this.egl.eglDestroySurface(this.eglDisplay, eGLSurface);
            this.eglSurface = EGL10.EGL_NO_SURFACE;
        }
    }

    public void setPresentTime(long j) {
    }

    public void swapBuffers() {
        checkIsNotReleased();
        if (this.eglSurface != EGL10.EGL_NO_SURFACE) {
            synchronized (EglBase.lock) {
                this.egl.eglSwapBuffers(this.eglDisplay, this.eglSurface);
            }
            return;
        }
        throw new RuntimeException("No EGLSurface can't make current");
    }
}
