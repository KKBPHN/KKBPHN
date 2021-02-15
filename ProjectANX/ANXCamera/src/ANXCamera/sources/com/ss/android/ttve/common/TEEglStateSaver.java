package com.ss.android.ttve.common;

import android.annotation.TargetApi;
import android.opengl.EGL14;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.util.Log;

@TargetApi(18)
public class TEEglStateSaver {
    private static final boolean DEBUG = true;
    private static final String TAG = "TEEglStateSaver";
    private EGLContext mSavedContext = EGL14.EGL_NO_CONTEXT;
    private EGLDisplay mSavedDisplay;
    private EGLSurface mSavedDrawSurface;
    private EGLSurface mSavedReadSurface;

    public TEEglStateSaver() {
        EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
        this.mSavedReadSurface = eGLSurface;
        this.mSavedDrawSurface = eGLSurface;
        this.mSavedDisplay = EGL14.EGL_NO_DISPLAY;
    }

    public EGLContext getSavedEGLContext() {
        return this.mSavedContext;
    }

    public void logState() {
        boolean equals = this.mSavedContext.equals(EGL14.eglGetCurrentContext());
        String str = TAG;
        Log.i(str, !equals ? "Saved context DOES NOT equal current." : "Saved context DOES equal current.");
        String str2 = !this.mSavedReadSurface.equals(EGL14.eglGetCurrentSurface(12378)) ? this.mSavedReadSurface.equals(EGL14.EGL_NO_SURFACE) ? "Saved read surface is EGL_NO_SURFACE" : "Saved read surface DOES NOT equal current." : "Saved read surface DOES equal current.";
        Log.i(str, str2);
        String str3 = !this.mSavedDrawSurface.equals(EGL14.eglGetCurrentSurface(12377)) ? this.mSavedDrawSurface.equals(EGL14.EGL_NO_SURFACE) ? "Saved draw surface is EGL_NO_SURFACE" : "Saved draw surface DOES NOT equal current." : "Saved draw surface DOES equal current.";
        Log.i(str, str3);
        Log.i(str, !this.mSavedDisplay.equals(EGL14.eglGetCurrentDisplay()) ? "Saved display DOES NOT equal current." : "Saved display DOES equal current.");
    }

    public void makeNothingCurrent() {
        EGLDisplay eGLDisplay = this.mSavedDisplay;
        EGLSurface eGLSurface = EGL14.EGL_NO_SURFACE;
        EGL14.eglMakeCurrent(eGLDisplay, eGLSurface, eGLSurface, EGL14.EGL_NO_CONTEXT);
    }

    public void makeSavedStateCurrent() {
        EGL14.eglMakeCurrent(this.mSavedDisplay, this.mSavedReadSurface, this.mSavedDrawSurface, this.mSavedContext);
    }

    public void saveEGLState() {
        this.mSavedContext = EGL14.eglGetCurrentContext();
        boolean equals = this.mSavedContext.equals(EGL14.EGL_NO_CONTEXT);
        String str = TAG;
        if (equals) {
            Log.e(str, "Saved EGL_NO_CONTEXT");
        }
        this.mSavedReadSurface = EGL14.eglGetCurrentSurface(12378);
        String str2 = "Saved EGL_NO_SURFACE";
        if (this.mSavedReadSurface.equals(EGL14.EGL_NO_SURFACE)) {
            Log.e(str, str2);
        }
        this.mSavedDrawSurface = EGL14.eglGetCurrentSurface(12377);
        if (this.mSavedDrawSurface.equals(EGL14.EGL_NO_SURFACE)) {
            Log.e(str, str2);
        }
        this.mSavedDisplay = EGL14.eglGetCurrentDisplay();
        if (this.mSavedDisplay.equals(EGL14.EGL_NO_DISPLAY)) {
            Log.e(str, "Saved EGL_NO_DISPLAY");
        }
    }
}
