package com.android.camera.ui;

import android.opengl.GLException;
import com.arcsoft.camera.wideselfie.WideSelfieEngine;
import java.io.IOException;
import java.io.Writer;
import javax.microedition.khronos.egl.EGL;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;

class EGLLogWrapper implements EGL11 {
    private int mArgCount;
    boolean mCheckError;
    private EGL10 mEgl10;
    Writer mLog;
    boolean mLogArgumentNames;

    public EGLLogWrapper(EGL egl, int i, Writer writer) {
        this.mEgl10 = (EGL10) egl;
        this.mLog = writer;
        boolean z = false;
        this.mLogArgumentNames = (i & 4) != 0;
        if ((i & 1) != 0) {
            z = true;
        }
        this.mCheckError = z;
    }

    private void arg(String str, int i) {
        arg(str, Integer.toString(i));
    }

    private void arg(String str, Object obj) {
        arg(str, toString(obj));
    }

    private void arg(String str, String str2) {
        int i = this.mArgCount;
        this.mArgCount = i + 1;
        if (i > 0) {
            log(", ");
        }
        if (this.mLogArgumentNames) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("=");
            log(sb.toString());
        }
        log(str2);
    }

    private void arg(String str, EGLContext eGLContext) {
        arg(str, eGLContext == EGL10.EGL_NO_CONTEXT ? "EGL10.EGL_NO_CONTEXT" : toString(eGLContext));
    }

    private void arg(String str, EGLDisplay eGLDisplay) {
        String eGLLogWrapper = eGLDisplay == EGL10.EGL_DEFAULT_DISPLAY ? "EGL10.EGL_DEFAULT_DISPLAY" : eGLDisplay == EGL11.EGL_NO_DISPLAY ? "EGL10.EGL_NO_DISPLAY" : toString(eGLDisplay);
        arg(str, eGLLogWrapper);
    }

    private void arg(String str, EGLSurface eGLSurface) {
        arg(str, eGLSurface == EGL10.EGL_NO_SURFACE ? "EGL10.EGL_NO_SURFACE" : toString(eGLSurface));
    }

    private void arg(String str, int[] iArr) {
        arg(str, iArr == null ? "null" : toString(iArr.length, iArr, 0));
    }

    private void arg(String str, Object[] objArr) {
        arg(str, objArr == null ? "null" : toString(objArr.length, objArr, 0));
    }

    private void begin(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append('(');
        log(sb.toString());
        this.mArgCount = 0;
    }

    private void checkError() {
        int eglGetError = this.mEgl10.eglGetError();
        if (eglGetError != 12288) {
            StringBuilder sb = new StringBuilder();
            sb.append("eglError: ");
            sb.append(getErrorString(eglGetError));
            String sb2 = sb.toString();
            logLine(sb2);
            if (this.mCheckError) {
                throw new GLException(eglGetError, sb2);
            }
        }
    }

    private void end() {
        log(");\n");
        flush();
    }

    private void flush() {
        try {
            this.mLog.flush();
        } catch (IOException unused) {
            this.mLog = null;
        }
    }

    public static String getErrorString(int i) {
        switch (i) {
            case 12288:
                return "EGL_SUCCESS";
            case WideSelfieEngine.HORIZ_FLIP /*12289*/:
                return "EGL_NOT_INITIALIZED";
            case WideSelfieEngine.VERT_FLIP /*12290*/:
                return "EGL_BAD_ACCESS";
            case 12291:
                return "EGL_BAD_ALLOC";
            case 12292:
                return "EGL_BAD_ATTRIBUTE";
            case 12293:
                return "EGL_BAD_CONFIG";
            case 12294:
                return "EGL_BAD_CONTEXT";
            case 12295:
                return "EGL_BAD_CURRENT_SURFACE";
            case 12296:
                return "EGL_BAD_DISPLAY";
            case 12297:
                return "EGL_BAD_MATCH";
            case 12298:
                return "EGL_BAD_NATIVE_PIXMAP";
            case 12299:
                return "EGL_BAD_NATIVE_WINDOW";
            case 12300:
                return "EGL_BAD_PARAMETER";
            case 12301:
                return "EGL_BAD_SURFACE";
            case 12302:
                return "EGL_CONTEXT_LOST";
            default:
                return getHex(i);
        }
    }

    private static String getHex(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }

    private void log(String str) {
        try {
            this.mLog.write(str);
        } catch (IOException unused) {
        }
    }

    private void logLine(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(10);
        log(sb.toString());
    }

    private void returns(int i) {
        returns(Integer.toString(i));
    }

    private void returns(Object obj) {
        returns(toString(obj));
    }

    private void returns(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(" returns ");
        sb.append(str);
        sb.append(";\n");
        log(sb.toString());
        flush();
    }

    private void returns(boolean z) {
        returns(Boolean.toString(z));
    }

    private String toString(int i, int[] iArr, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        int length = iArr.length;
        for (int i3 = 0; i3 < i; i3++) {
            int i4 = i2 + i3;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" [");
            sb2.append(i4);
            sb2.append("] = ");
            sb.append(sb2.toString());
            if (i4 < 0 || i4 >= length) {
                sb.append("out of bounds");
            } else {
                sb.append(iArr[i4]);
            }
            sb.append(10);
        }
        sb.append("}");
        return sb.toString();
    }

    private String toString(int i, Object[] objArr, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        int length = objArr.length;
        for (int i3 = 0; i3 < i; i3++) {
            int i4 = i2 + i3;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" [");
            sb2.append(i4);
            sb2.append("] = ");
            sb.append(sb2.toString());
            if (i4 < 0 || i4 >= length) {
                sb.append("out of bounds");
            } else {
                sb.append(objArr[i4]);
            }
            sb.append(10);
        }
        sb.append("}");
        return sb.toString();
    }

    private String toString(Object obj) {
        return obj == null ? "null" : obj.toString();
    }

    public boolean eglChooseConfig(EGLDisplay eGLDisplay, int[] iArr, EGLConfig[] eGLConfigArr, int i, int[] iArr2) {
        begin("eglChooseConfig");
        arg("display", eGLDisplay);
        arg("attrib_list", iArr);
        arg("config_size", i);
        end();
        boolean eglChooseConfig = this.mEgl10.eglChooseConfig(eGLDisplay, iArr, eGLConfigArr, i, iArr2);
        arg("configs", (Object[]) eGLConfigArr);
        arg("num_config", iArr2);
        returns(eglChooseConfig);
        checkError();
        return eglChooseConfig;
    }

    public boolean eglCopyBuffers(EGLDisplay eGLDisplay, EGLSurface eGLSurface, Object obj) {
        begin("eglCopyBuffers");
        arg("display", eGLDisplay);
        arg("surface", eGLSurface);
        arg("native_pixmap", obj);
        end();
        boolean eglCopyBuffers = this.mEgl10.eglCopyBuffers(eGLDisplay, eGLSurface, obj);
        returns(eglCopyBuffers);
        checkError();
        return eglCopyBuffers;
    }

    public EGLContext eglCreateContext(EGLDisplay eGLDisplay, EGLConfig eGLConfig, EGLContext eGLContext, int[] iArr) {
        begin("eglCreateContext");
        arg("display", eGLDisplay);
        arg("config", (Object) eGLConfig);
        arg("share_context", eGLContext);
        arg("attrib_list", iArr);
        end();
        EGLContext eglCreateContext = this.mEgl10.eglCreateContext(eGLDisplay, eGLConfig, eGLContext, iArr);
        returns((Object) eglCreateContext);
        checkError();
        return eglCreateContext;
    }

    public EGLSurface eglCreatePbufferSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int[] iArr) {
        begin("eglCreatePbufferSurface");
        arg("display", eGLDisplay);
        arg("config", (Object) eGLConfig);
        arg("attrib_list", iArr);
        end();
        EGLSurface eglCreatePbufferSurface = this.mEgl10.eglCreatePbufferSurface(eGLDisplay, eGLConfig, iArr);
        returns((Object) eglCreatePbufferSurface);
        checkError();
        return eglCreatePbufferSurface;
    }

    public EGLSurface eglCreatePixmapSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj, int[] iArr) {
        begin("eglCreatePixmapSurface");
        arg("display", eGLDisplay);
        arg("config", (Object) eGLConfig);
        arg("native_pixmap", obj);
        arg("attrib_list", iArr);
        end();
        EGLSurface eglCreatePixmapSurface = this.mEgl10.eglCreatePixmapSurface(eGLDisplay, eGLConfig, obj, iArr);
        returns((Object) eglCreatePixmapSurface);
        checkError();
        return eglCreatePixmapSurface;
    }

    public EGLSurface eglCreateWindowSurface(EGLDisplay eGLDisplay, EGLConfig eGLConfig, Object obj, int[] iArr) {
        begin("eglCreateWindowSurface");
        arg("display", eGLDisplay);
        arg("config", (Object) eGLConfig);
        arg("native_window", obj);
        arg("attrib_list", iArr);
        end();
        EGLSurface eglCreateWindowSurface = this.mEgl10.eglCreateWindowSurface(eGLDisplay, eGLConfig, obj, iArr);
        returns((Object) eglCreateWindowSurface);
        checkError();
        return eglCreateWindowSurface;
    }

    public boolean eglDestroyContext(EGLDisplay eGLDisplay, EGLContext eGLContext) {
        begin("eglDestroyContext");
        arg("display", eGLDisplay);
        arg("context", eGLContext);
        end();
        boolean eglDestroyContext = this.mEgl10.eglDestroyContext(eGLDisplay, eGLContext);
        returns(eglDestroyContext);
        checkError();
        return eglDestroyContext;
    }

    public boolean eglDestroySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        begin("eglDestroySurface");
        arg("display", eGLDisplay);
        arg("surface", eGLSurface);
        end();
        boolean eglDestroySurface = this.mEgl10.eglDestroySurface(eGLDisplay, eGLSurface);
        returns(eglDestroySurface);
        checkError();
        return eglDestroySurface;
    }

    public boolean eglGetConfigAttrib(EGLDisplay eGLDisplay, EGLConfig eGLConfig, int i, int[] iArr) {
        begin("eglGetConfigAttrib");
        arg("display", eGLDisplay);
        arg("config", (Object) eGLConfig);
        arg("attribute", i);
        end();
        boolean eglGetConfigAttrib = this.mEgl10.eglGetConfigAttrib(eGLDisplay, eGLConfig, i, iArr);
        arg("value", iArr);
        returns(eglGetConfigAttrib);
        checkError();
        return false;
    }

    public boolean eglGetConfigs(EGLDisplay eGLDisplay, EGLConfig[] eGLConfigArr, int i, int[] iArr) {
        begin("eglGetConfigs");
        arg("display", eGLDisplay);
        arg("config_size", i);
        end();
        boolean eglGetConfigs = this.mEgl10.eglGetConfigs(eGLDisplay, eGLConfigArr, i, iArr);
        arg("configs", (Object[]) eGLConfigArr);
        arg("num_config", iArr);
        returns(eglGetConfigs);
        checkError();
        return eglGetConfigs;
    }

    public EGLContext eglGetCurrentContext() {
        begin("eglGetCurrentContext");
        end();
        EGLContext eglGetCurrentContext = this.mEgl10.eglGetCurrentContext();
        returns((Object) eglGetCurrentContext);
        checkError();
        return eglGetCurrentContext;
    }

    public EGLDisplay eglGetCurrentDisplay() {
        begin("eglGetCurrentDisplay");
        end();
        EGLDisplay eglGetCurrentDisplay = this.mEgl10.eglGetCurrentDisplay();
        returns((Object) eglGetCurrentDisplay);
        checkError();
        return eglGetCurrentDisplay;
    }

    public EGLSurface eglGetCurrentSurface(int i) {
        begin("eglGetCurrentSurface");
        arg("readdraw", i);
        end();
        EGLSurface eglGetCurrentSurface = this.mEgl10.eglGetCurrentSurface(i);
        returns((Object) eglGetCurrentSurface);
        checkError();
        return eglGetCurrentSurface;
    }

    public EGLDisplay eglGetDisplay(Object obj) {
        begin("eglGetDisplay");
        arg("native_display", obj);
        end();
        EGLDisplay eglGetDisplay = this.mEgl10.eglGetDisplay(obj);
        returns((Object) eglGetDisplay);
        checkError();
        return eglGetDisplay;
    }

    public int eglGetError() {
        begin("eglGetError");
        end();
        int eglGetError = this.mEgl10.eglGetError();
        returns(getErrorString(eglGetError));
        return eglGetError;
    }

    public boolean eglInitialize(EGLDisplay eGLDisplay, int[] iArr) {
        begin("eglInitialize");
        arg("display", eGLDisplay);
        end();
        boolean eglInitialize = this.mEgl10.eglInitialize(eGLDisplay, iArr);
        returns(eglInitialize);
        arg("major_minor", iArr);
        checkError();
        return eglInitialize;
    }

    public boolean eglMakeCurrent(EGLDisplay eGLDisplay, EGLSurface eGLSurface, EGLSurface eGLSurface2, EGLContext eGLContext) {
        begin("eglMakeCurrent");
        arg("display", eGLDisplay);
        arg("draw", eGLSurface);
        arg("read", eGLSurface2);
        arg("context", eGLContext);
        end();
        boolean eglMakeCurrent = this.mEgl10.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface2, eGLContext);
        returns(eglMakeCurrent);
        checkError();
        return eglMakeCurrent;
    }

    public boolean eglQueryContext(EGLDisplay eGLDisplay, EGLContext eGLContext, int i, int[] iArr) {
        begin("eglQueryContext");
        arg("display", eGLDisplay);
        arg("context", eGLContext);
        arg("attribute", i);
        end();
        boolean eglQueryContext = this.mEgl10.eglQueryContext(eGLDisplay, eGLContext, i, iArr);
        returns(iArr[0]);
        returns(eglQueryContext);
        checkError();
        return eglQueryContext;
    }

    public String eglQueryString(EGLDisplay eGLDisplay, int i) {
        begin("eglQueryString");
        arg("display", eGLDisplay);
        arg("name", i);
        end();
        String eglQueryString = this.mEgl10.eglQueryString(eGLDisplay, i);
        returns(eglQueryString);
        checkError();
        return eglQueryString;
    }

    public boolean eglQuerySurface(EGLDisplay eGLDisplay, EGLSurface eGLSurface, int i, int[] iArr) {
        begin("eglQuerySurface");
        arg("display", eGLDisplay);
        arg("surface", eGLSurface);
        arg("attribute", i);
        end();
        boolean eglQuerySurface = this.mEgl10.eglQuerySurface(eGLDisplay, eGLSurface, i, iArr);
        returns(iArr[0]);
        returns(eglQuerySurface);
        checkError();
        return eglQuerySurface;
    }

    public boolean eglReleaseThread() {
        begin("eglReleaseThread");
        end();
        boolean eglReleaseThread = this.mEgl10.eglReleaseThread();
        returns(eglReleaseThread);
        checkError();
        return eglReleaseThread;
    }

    public boolean eglSwapBuffers(EGLDisplay eGLDisplay, EGLSurface eGLSurface) {
        begin("eglSwapBuffers");
        arg("display", eGLDisplay);
        arg("surface", eGLSurface);
        end();
        boolean eglSwapBuffers = this.mEgl10.eglSwapBuffers(eGLDisplay, eGLSurface);
        returns(eglSwapBuffers);
        checkError();
        return eglSwapBuffers;
    }

    public boolean eglTerminate(EGLDisplay eGLDisplay) {
        begin("eglTerminate");
        arg("display", eGLDisplay);
        end();
        boolean eglTerminate = this.mEgl10.eglTerminate(eGLDisplay);
        returns(eglTerminate);
        checkError();
        return eglTerminate;
    }

    public boolean eglWaitGL() {
        begin("eglWaitGL");
        end();
        boolean eglWaitGL = this.mEgl10.eglWaitGL();
        returns(eglWaitGL);
        checkError();
        return eglWaitGL;
    }

    public boolean eglWaitNative(int i, Object obj) {
        begin("eglWaitNative");
        arg("engine", i);
        arg("bindTarget", obj);
        end();
        boolean eglWaitNative = this.mEgl10.eglWaitNative(i, obj);
        returns(eglWaitNative);
        checkError();
        return eglWaitNative;
    }
}
