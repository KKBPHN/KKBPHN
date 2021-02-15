package net.majorkernelpanic.streaming.gl;

import android.annotation.SuppressLint;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.view.Surface;

@SuppressLint({"NewApi"})
public class SurfaceManager {
    private static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final String TAG = "TextureManager";
    private EGLContext mEGLContext = null;
    private EGLDisplay mEGLDisplay = null;
    private EGLContext mEGLSharedContext = null;
    private EGLSurface mEGLSurface = null;
    private Surface mSurface;

    public SurfaceManager(Surface surface) {
        this.mSurface = surface;
        eglSetup();
    }

    public SurfaceManager(Surface surface, SurfaceManager surfaceManager) {
        this.mSurface = surface;
        this.mEGLSharedContext = surfaceManager.mEGLContext;
        eglSetup();
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError != 12288) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": EGL error: 0x");
            sb.append(Integer.toHexString(eglGetError));
            throw new RuntimeException(sb.toString());
        }
    }

    private void eglSetup() {
        this.mEGLDisplay = EGL14.eglGetDisplay(0);
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
            int[] iArr = new int[2];
            if (EGL14.eglInitialize(eGLDisplay, iArr, 0, iArr, 1)) {
                EGLConfig[] eGLConfigArr = new EGLConfig[1];
                EGL14.eglChooseConfig(this.mEGLDisplay, this.mEGLSharedContext == null ? new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12344} : new int[]{12324, 8, 12323, 8, 12322, 8, 12352, 4, 12610, 1, 12344}, 0, eGLConfigArr, 0, eGLConfigArr.length, new int[1], 0);
                checkEglError("eglCreateContext RGB888+recordable ES2");
                int[] iArr2 = {12440, 2, 12344};
                EGLContext eGLContext = this.mEGLSharedContext;
                this.mEGLContext = eGLContext == null ? EGL14.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], EGL14.EGL_NO_CONTEXT, iArr2, 0) : EGL14.eglCreateContext(this.mEGLDisplay, eGLConfigArr[0], eGLContext, iArr2, 0);
                checkEglError("eglCreateContext");
                this.mEGLSurface = EGL14.eglCreateWindowSurface(this.mEGLDisplay, eGLConfigArr[0], this.mSurface, new int[]{12344}, 0);
                checkEglError("eglCreateWindowSurface");
                GLES20.glDisable(2929);
                GLES20.glDisable(2884);
                return;
            }
            throw new RuntimeException("unable to initialize EGL14");
        }
        throw new RuntimeException("unable to get EGL14 display");
    }

    public void makeCurrent() {
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        EGLSurface eGLSurface = this.mEGLSurface;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mEGLContext)) {
            throw new RuntimeException("eglMakeCurrent failed");
        }
    }

    public void release() {
        EGLDisplay eGLDisplay = this.mEGLDisplay;
        if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroySurface(this.mEGLDisplay, this.mEGLSurface);
            EGL14.eglDestroyContext(this.mEGLDisplay, this.mEGLContext);
            EGL14.eglReleaseThread();
            EGL14.eglTerminate(this.mEGLDisplay);
        }
        this.mEGLDisplay = EGL14.EGL_NO_DISPLAY;
        this.mEGLContext = EGL14.EGL_NO_CONTEXT;
        this.mEGLSurface = EGL14.EGL_NO_SURFACE;
        this.mSurface.release();
    }

    public void setPresentationTime(long j) {
        EGLExt.eglPresentationTimeANDROID(this.mEGLDisplay, this.mEGLSurface, j);
        checkEglError("eglPresentationTimeANDROID");
    }

    public void swapBuffer() {
        EGL14.eglSwapBuffers(this.mEGLDisplay, this.mEGLSurface);
    }
}
