package com.xiaomi.mediacodec;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Build.VERSION;
import android.view.Surface;
import miui.text.ExtraTextUtils;

@TargetApi(18)
public class EglBase14 extends EglBase {
    private EGLConfig eglConfig;
    private EGLContext eglContext;
    private EGLDisplay eglDisplay;
    private EGLSurface eglSurface;

    public class Context extends Context {
        /* access modifiers changed from: private */
        public final EGLContext eglContext;

        public Context(EGLContext eGLContext) {
            this.eglContext = eGLContext;
        }
    }

    public EglBase14(EGLContext eGLContext) {
        this.eglSurface = EGL14.EGL_NO_SURFACE;
        this.eglDisplay = getEglDisplay();
        this.eglConfig = getEglConfig(this.eglDisplay, EglBase.CONFIG_PLAIN);
        synchronized (EglBase.lock) {
            this.eglContext = EGL14.eglCreateContext(this.eglDisplay, this.eglConfig, eGLContext, new int[]{12440, 3, 12344}, 0);
            StringBuilder sb = new StringBuilder();
            sb.append("  ddd create content egl content ");
            sb.append(this.eglContext);
            sb.append(" share ");
            sb.append(eGLContext);
            Logg.LogI(sb.toString());
            if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                throw new RuntimeException("Failed to create EGL context");
            }
        }
    }

    public EglBase14(Context context, int[] iArr) {
        this.eglSurface = EGL14.EGL_NO_SURFACE;
        this.eglDisplay = getEglDisplay();
        this.eglConfig = getEglConfig(this.eglDisplay, iArr);
        this.eglContext = createEglContext(context, this.eglDisplay, this.eglConfig);
    }

    private void checkIsNotReleased() {
        if (this.eglDisplay == EGL14.EGL_NO_DISPLAY || this.eglContext == EGL14.EGL_NO_CONTEXT || this.eglConfig == null) {
            if (this.eglDisplay == EGL14.EGL_NO_DISPLAY) {
                Logg.LogI("DDDDDDD");
            }
            if (this.eglContext == EGL14.EGL_NO_CONTEXT) {
                Logg.LogI("cccccccccc");
            }
            if (this.eglConfig == null) {
                Logg.LogI(" nnnn cccccccccc");
            }
            throw new RuntimeException("This object has been released");
        }
    }

    private static EGLContext createEglContext(Context context, EGLDisplay eGLDisplay, EGLConfig eGLConfig) {
        EGLContext eglCreateContext;
        if (context == null || context.eglContext != EGL14.EGL_NO_CONTEXT) {
            int[] iArr = {12440, 3, 12344};
            EGLContext access$000 = context == null ? EGL14.EGL_NO_CONTEXT : context.eglContext;
            synchronized (EglBase.lock) {
                eglCreateContext = EGL14.eglCreateContext(eGLDisplay, eGLConfig, access$000, iArr, 0);
                StringBuilder sb = new StringBuilder();
                sb.append(" shared content ");
                sb.append(context);
                Logg.LogI(sb.toString());
                if (context != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(" shared content egl content ");
                    sb2.append(context.eglContext);
                    Logg.LogI(sb2.toString());
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" create content egl content ");
                sb3.append(eglCreateContext);
                Logg.LogI(sb3.toString());
            }
            if (eglCreateContext != EGL14.EGL_NO_CONTEXT) {
                return eglCreateContext;
            }
            throw new RuntimeException("Failed to create EGL context");
        }
        throw new RuntimeException("Invalid sharedContext");
    }

    private void createSufaceImpl(Object obj) {
        if ((obj instanceof Surface) || (obj instanceof SurfaceTexture)) {
            checkIsNotReleased();
            if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
                this.eglSurface = EGL14.eglCreateWindowSurface(this.eglDisplay, this.eglConfig, obj, new int[]{12344}, 0);
                if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
                    throw new RuntimeException("Failed to create window surface");
                }
                return;
            }
            throw new RuntimeException("Already has an EGLSurface");
        }
        throw new RuntimeException("Input must be either a Surface or SurfaceTexture");
    }

    public static Context getCurrentContext14() {
        return new Context(EGL14.eglGetCurrentContext());
    }

    private static EGLConfig getEglConfig(EGLDisplay eGLDisplay, int[] iArr) {
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        int[] iArr2 = new int[1];
        if (!EGL14.eglChooseConfig(eGLDisplay, iArr, 0, eGLConfigArr, 0, eGLConfigArr.length, iArr2, 0)) {
            throw new RuntimeException("eglChooseConfig failed");
        } else if (iArr2[0] >= 0) {
            EGLConfig eGLConfig = eGLConfigArr[0];
            if (eGLConfig != null) {
                return eGLConfig;
            }
            throw new RuntimeException("eglChooseConfig returned null");
        } else {
            throw new RuntimeException("Unable to find any matching EGL config");
        }
    }

    private static EGLDisplay getEglDisplay() {
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        if (eglGetDisplay != EGL14.EGL_NO_DISPLAY) {
            int[] iArr = new int[2];
            if (EGL14.eglInitialize(eglGetDisplay, iArr, 0, iArr, 1)) {
                return eglGetDisplay;
            }
            throw new RuntimeException("Unable to initialize EGL14");
        }
        throw new RuntimeException("Unable to get EGL14 display");
    }

    public static boolean isEGL14Supported() {
        return VERSION.SDK_INT >= 18;
    }

    private int querySurfaceType(int i) {
        int[] iArr = new int[1];
        EGL14.eglQuerySurface(this.eglDisplay, this.eglSurface, i, iArr, 0);
        return iArr[0];
    }

    public void createPbufferSurface(int i, int i2) {
        checkIsNotReleased();
        if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
            this.eglSurface = EGL14.eglCreatePbufferSurface(this.eglDisplay, this.eglConfig, new int[]{12375, i, 12374, i2, 12344}, 0);
            if (this.eglSurface == EGL14.EGL_NO_SURFACE) {
                throw new RuntimeException("Failed to create pixel buffer surface");
            }
            return;
        }
        throw new RuntimeException("Already has an EGLSurface");
    }

    public void createSurface(SurfaceTexture surfaceTexture) {
        createSufaceImpl(surfaceTexture);
    }

    public void createSurface(Surface surface) {
        createSufaceImpl(surface);
    }

    public void detachCurrent() {
        synchronized (EglBase.lock) {
            if (!EGL14.eglMakeCurrent(this.eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)) {
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
        return this.eglSurface != EGL14.EGL_NO_SURFACE;
    }

    public void makeCurrent() {
        checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            synchronized (EglBase.lock) {
                if (!EGL14.eglMakeCurrent(this.eglDisplay, this.eglSurface, this.eglSurface, this.eglContext)) {
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
        EGL14.eglDestroyContext(this.eglDisplay, this.eglContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(this.eglDisplay);
        this.eglContext = EGL14.EGL_NO_CONTEXT;
        this.eglDisplay = EGL14.EGL_NO_DISPLAY;
        this.eglConfig = null;
    }

    public void releaseSuface() {
        EGLSurface eGLSurface = this.eglSurface;
        if (eGLSurface != EGL14.EGL_NO_SURFACE) {
            EGL14.eglDestroySurface(this.eglDisplay, eGLSurface);
            this.eglSurface = EGL14.EGL_NO_SURFACE;
        }
    }

    public void setPresentTime(long j) {
        StringBuilder sb = new StringBuilder();
        sb.append(" nnnn setPresentTime:");
        long j2 = j * ExtraTextUtils.MB;
        sb.append(j2);
        Logg.LogI(sb.toString());
        EGLExt.eglPresentationTimeANDROID(this.eglDisplay, this.eglSurface, j2);
    }

    public void swapBuffers() {
        checkIsNotReleased();
        if (this.eglSurface != EGL14.EGL_NO_SURFACE) {
            synchronized (EglBase.lock) {
                EGL14.eglSwapBuffers(this.eglDisplay, this.eglSurface);
            }
            return;
        }
        throw new RuntimeException("No EGLSurface can't make current");
    }
}
