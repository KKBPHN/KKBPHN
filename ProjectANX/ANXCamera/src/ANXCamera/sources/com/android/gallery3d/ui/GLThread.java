package com.android.gallery3d.ui;

import android.opengl.EGLContext;
import android.os.Handler;
import android.os.HandlerThread;
import androidx.annotation.CallSuper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import com.android.camera.log.Log;
import com.xiaomi.camera.liveshot.gles.EglCore;

public class GLThread {
    private static final String TAG = "GLThread";
    /* access modifiers changed from: private */
    public EglCore mEglCore;
    /* access modifiers changed from: private */
    public EglOffscreenSurface mEglOffscreenSurface;
    private Handler mGlHandler;
    private HandlerThread mGlHandlerThread;

    public GLThread(@NonNull String str, @IntRange(from = 2, to = 3) int i) {
        this(str, i, null);
    }

    public GLThread(@NonNull String str, @IntRange(from = 2, to = 3) int i, @Nullable final EGLContext eGLContext) {
        this.mGlHandlerThread = new HandlerThread(str);
        this.mGlHandlerThread.start();
        this.mGlHandler = new Handler(this.mGlHandlerThread.getLooper());
        StringBuilder sb = new StringBuilder();
        sb.append("new Instance with thread id:");
        sb.append(this.mGlHandlerThread.getThreadId());
        sb.append(" name:");
        sb.append(this.mGlHandlerThread.getName());
        Log.d(TAG, sb.toString());
        this.mGlHandler.post(new Runnable() {
            public void run() {
                synchronized (GLThread.this) {
                    Log.d(GLThread.TAG, "new egl Instance");
                    GLThread.this.mEglCore = new EglCore(eGLContext, 2);
                    GLThread.this.notifyAll();
                    GLThread.this.onGLThreadPrepared();
                }
            }
        });
    }

    public GLThread(@NonNull String str, @NonNull EGLContext eGLContext) {
        this(str, 3, eGLContext);
    }

    private boolean waitDone() {
        final Object obj = new Object();
        AnonymousClass3 r1 = new Runnable() {
            public void run() {
                synchronized (obj) {
                    obj.notifyAll();
                }
            }
        };
        synchronized (obj) {
            this.mGlHandler.post(r1);
            try {
                obj.wait();
            } catch (InterruptedException unused) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    public EglCore getEglCore() {
        synchronized (this) {
            while (this.mEglCore == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return this.mEglCore;
    }

    @Nullable
    public Handler getHandler() {
        StringBuilder sb = new StringBuilder();
        sb.append("getHandler to do some thing on egl thread ");
        sb.append(this.mGlHandler);
        Log.d(TAG, sb.toString());
        return this.mGlHandler;
    }

    /* access modifiers changed from: protected */
    @CallSuper
    @WorkerThread
    public void onGLThreadPrepared() {
        this.mEglOffscreenSurface = new EglOffscreenSurface(this.mEglCore, 1, 1);
        this.mEglOffscreenSurface.makeCurrent();
    }

    public void release() {
        StringBuilder sb = new StringBuilder();
        sb.append("release with thread id:");
        sb.append(this.mGlHandlerThread.getThreadId());
        sb.append(" name:");
        sb.append(this.mGlHandlerThread.getName());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.mGlHandler.post(new Runnable() {
            public void run() {
                GLThread.this.mEglOffscreenSurface.releaseEglSurface();
                GLThread.this.mEglOffscreenSurface = null;
                GLThread.this.mEglCore.release();
                GLThread.this.mEglCore = null;
                Log.d(GLThread.TAG, "mEglOffscreenSurface and mEglCore release done");
            }
        });
        this.mGlHandlerThread.quitSafely();
        this.mGlHandlerThread = null;
        this.mGlHandler = null;
        Log.d(str, "release done");
    }
}
