package com.ss.android.medialib.common;

import android.annotation.SuppressLint;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.os.Build.VERSION;
import com.ss.android.vesdk.VELogUtil;

@SuppressLint({"InlinedApi"})
public class SharedContext {
    public static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final String LOG_TAG = "SharedContext";
    private static int mBitsA = 8;
    private static int mBitsB = 8;
    private static int mBitsG = 8;
    private static int mBitsR = 8;
    private static int mGLVersion = 3;
    private EGLConfig mConfig;
    private EGLContext mContext;
    private EGLDisplay mDisplay;
    private volatile boolean mInitialized = false;
    private EGLSurface mSurface;

    SharedContext() {
    }

    private void checkEglError(String str) {
        int eglGetError = EGL14.eglGetError();
        if (eglGetError != 12288) {
            String str2 = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(": EGL error: 0x");
            sb.append(Integer.toHexString(eglGetError));
            VELogUtil.e(str2, sb.toString());
        }
    }

    public static SharedContext create() {
        return create(EGL14.EGL_NO_CONTEXT, 64, 64, 1, null);
    }

    public static SharedContext create(int i, int i2) {
        return create(EGL14.EGL_NO_CONTEXT, i, i2, 1, null);
    }

    public static SharedContext create(EGLContext eGLContext, int i, int i2) {
        return create(eGLContext, i, i2, 1, null);
    }

    public static SharedContext create(EGLContext eGLContext, int i, int i2, int i3, Object obj) {
        SharedContext sharedContext = new SharedContext();
        if (sharedContext.initEGL(eGLContext, i, i2, i3, obj)) {
            return sharedContext;
        }
        sharedContext.release();
        return null;
    }

    public static SharedContext create(EGLContext eGLContext, int i, Object obj) {
        return create(eGLContext, 64, 64, i, obj);
    }

    private boolean initEGL(EGLContext eGLContext, int i, int i2, int i3, Object obj) {
        EGLSurface eglCreatePixmapSurface;
        String str;
        String sb;
        int[] iArr;
        Object[] objArr;
        String str2;
        int i4 = i3;
        EGLContext eGLContext2 = eGLContext == null ? EGL14.EGL_NO_CONTEXT : eGLContext;
        int[] iArr2 = {12440, mGLVersion, 12344};
        EGLConfig[] eGLConfigArr = new EGLConfig[1];
        int[] iArr3 = new int[2];
        int[] iArr4 = {12375, i, 12374, i2, 12344};
        EGLDisplay eglGetDisplay = EGL14.eglGetDisplay(0);
        this.mDisplay = eglGetDisplay;
        if (eglGetDisplay == EGL14.EGL_NO_DISPLAY) {
            str = LOG_TAG;
            objArr = new Object[]{Integer.valueOf(EGL14.eglGetError())};
            str2 = "eglGetDisplay() returned error 0x%x";
        } else if (!EGL14.eglInitialize(this.mDisplay, iArr3, 0, iArr3, 1)) {
            str = LOG_TAG;
            objArr = new Object[]{Integer.valueOf(EGL14.eglGetError())};
            str2 = "eglInitialize() returned error 0x%x";
        } else {
            int i5 = (mGLVersion < 3 || iArr3[1] < 4) ? 4 : 64;
            int[] iArr5 = {12352, i5, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12339, i4, 12344};
            if (i4 == 12610) {
                iArr5[iArr5.length - 3] = 12610;
                iArr5[iArr5.length - 2] = 1;
            }
            VELogUtil.i(LOG_TAG, String.format("eglInitialize - major: %d, minor: %d", new Object[]{Integer.valueOf(iArr3[0]), Integer.valueOf(iArr3[1])}));
            int[] iArr6 = new int[1];
            int[] iArr7 = iArr4;
            EGLConfig[] eGLConfigArr2 = eGLConfigArr;
            if (!EGL14.eglChooseConfig(this.mDisplay, iArr5, 0, eGLConfigArr, 0, eGLConfigArr.length, iArr6, 0)) {
                str = LOG_TAG;
                objArr = new Object[]{Integer.valueOf(EGL14.eglGetError())};
                str2 = "eglChooseConfig() returned error 0x%x";
            } else {
                String str3 = LOG_TAG;
                Object[] objArr2 = new Object[2];
                objArr2[0] = Integer.valueOf(iArr6[0]);
                objArr2[1] = eGLContext2 == EGL14.EGL_NO_CONTEXT ? "NO" : "YES";
                VELogUtil.i(str3, String.format("Config num: %d, has sharedContext: %s", objArr2));
                this.mConfig = eGLConfigArr2[0];
                this.mContext = EGL14.eglCreateContext(this.mDisplay, this.mConfig, eGLContext2, iArr2, 0);
                if (this.mContext == EGL14.EGL_NO_CONTEXT) {
                    str = LOG_TAG;
                    sb = "eglCreateContext Failed!";
                } else {
                    if (i4 != 2) {
                        if (i4 == 4) {
                            iArr = iArr7;
                        } else if (i4 != 12610) {
                            eglCreatePixmapSurface = EGL14.eglCreatePbufferSurface(this.mDisplay, this.mConfig, iArr7, 0);
                        } else {
                            iArr = iArr7;
                            iArr[0] = 12344;
                            iArr[2] = 12344;
                        }
                        eglCreatePixmapSurface = EGL14.eglCreateWindowSurface(this.mDisplay, this.mConfig, obj, iArr, 0);
                    } else {
                        eglCreatePixmapSurface = EGL14.eglCreatePixmapSurface(this.mDisplay, this.mConfig, 0, iArr7, 0);
                    }
                    this.mSurface = eglCreatePixmapSurface;
                    EGLSurface eGLSurface = this.mSurface;
                    if (eGLSurface == EGL14.EGL_NO_SURFACE) {
                        checkEglError("eglCreateSurface");
                        str = LOG_TAG;
                        sb = "eglCreateSurface Failed!";
                    } else if (!EGL14.eglMakeCurrent(this.mDisplay, eGLSurface, eGLSurface, this.mContext)) {
                        str = LOG_TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("eglMakeCurrent failed:");
                        sb2.append(EGL14.eglGetError());
                        sb = sb2.toString();
                    } else {
                        int[] iArr8 = new int[1];
                        EGL14.eglQueryContext(this.mDisplay, this.mContext, 12440, iArr8, 0);
                        String str4 = LOG_TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("EGLContext created, client version ");
                        sb3.append(iArr8[0]);
                        VELogUtil.i(str4, sb3.toString());
                        this.mInitialized = true;
                        return true;
                    }
                }
                VELogUtil.e(str, sb);
                return false;
            }
        }
        sb = String.format(str2, objArr);
        VELogUtil.e(str, sb);
        return false;
    }

    public static void setContextColorBits(int i, int i2, int i3, int i4) {
        mBitsR = i;
        mBitsG = i2;
        mBitsB = i3;
        mBitsA = i4;
    }

    public static void setGLVersion(int i) {
        mGLVersion = i;
    }

    public String getClientVersion() {
        if (!isInitialized()) {
            return "Not initialized";
        }
        int[] iArr = new int[1];
        EGL14.eglQueryContext(this.mDisplay, this.mContext, 12440, iArr, 0);
        String str = LOG_TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("EGLContext created, client version ");
        sb.append(iArr[0]);
        VELogUtil.i(str, sb.toString());
        return "";
    }

    public String getConfigAttrib(int i) {
        if (isInitialized()) {
            EGLConfig eGLConfig = this.mConfig;
            if (eGLConfig != null) {
                int[] iArr = new int[1];
                EGL14.eglGetConfigAttrib(this.mDisplay, eGLConfig, i, iArr, 0);
                return String.valueOf(iArr[0]);
            }
        }
        return "Not initialized";
    }

    public EGLContext getContext() {
        return this.mContext;
    }

    public EGLDisplay getDisplay() {
        return this.mDisplay;
    }

    public EGLSurface getSurface() {
        return this.mSurface;
    }

    public boolean isInitialized() {
        return (!this.mInitialized || this.mContext == EGL14.EGL_NO_CONTEXT || this.mDisplay == EGL14.EGL_NO_DISPLAY) ? false : true;
    }

    public void makeCurrent() {
        EGLDisplay eGLDisplay = this.mDisplay;
        EGLSurface eGLSurface = this.mSurface;
        if (!EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, this.mContext)) {
            String str = LOG_TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("eglMakeCurrent failed:");
            sb.append(EGL14.eglGetError());
            VELogUtil.e(str, sb.toString());
        }
    }

    public String queryString(int i) {
        return !isInitialized() ? "Not initialized" : VERSION.SDK_INT >= 17 ? EGL14.eglQueryString(this.mDisplay, i) : "";
    }

    public void release() {
        VELogUtil.i(LOG_TAG, "#### CGESharedGLContext Destroying context... ####");
        EGLDisplay eGLDisplay = this.mDisplay;
        if (eGLDisplay != EGL14.EGL_NO_DISPLAY) {
            EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
            EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroyContext(this.mDisplay, this.mContext);
            EGL14.eglDestroySurface(this.mDisplay, this.mSurface);
            EGL14.eglTerminate(this.mDisplay);
        }
        this.mDisplay = EGL14.EGL_NO_DISPLAY;
        this.mSurface = EGL14.EGL_NO_SURFACE;
        this.mContext = EGL14.EGL_NO_CONTEXT;
        this.mInitialized = false;
    }

    public boolean swapBuffers() {
        return EGL14.eglSwapBuffers(this.mDisplay, this.mSurface);
    }
}
