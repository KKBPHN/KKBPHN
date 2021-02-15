package com.xiaomi.camera.liveshot.gles;

import android.opengl.EGLContext;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Surface;
import com.android.camera.effect.VideoRecorderCanvas;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.log.Log;
import java.lang.ref.WeakReference;

public final class RenderThread extends Thread {
    public static final int MSG_CINEMATIC_CHANGED = 64;
    public static final int MSG_DRAW_REQUESTED = 16;
    public static final int MSG_FILTER_CHANGED = 32;
    public static final int MSG_QUIT_REQUESTED = 48;
    private static final String TAG = "RenderThread";
    private VideoRecorderCanvas mCanvas;
    private int mCanvasHeight = 0;
    private int mCanvasWidth = 0;
    private DrawExtTexAttribute mDrawExtTexAttribute = new DrawExtTexAttribute();
    private volatile boolean mEglContextPrepared = false;
    private EglCore mEglCore;
    private EglSurfaceBase mEglSurfaceBase;
    /* access modifiers changed from: private */
    public ConditionVariable mEglThreadBlockVar = new ConditionVariable();
    private RenderHandler mHandler;
    private boolean mIsOrientationLocked = true;
    private boolean mIsRecordable;
    private final Object mLock = new Object();
    private volatile boolean mReady = false;
    private volatile int mRequestDraw;
    private volatile boolean mRequestRelease = false;
    private final EGLContext mShardContext;
    private int mStreamHeight;
    private int mStreamWidth;
    private Surface mSurface;
    private int mTextureHeight = 0;
    private int mTextureOffsetX = 0;
    private int mTextureOffsetY = 0;
    private int mTextureWidth = 0;

    public class RenderHandler extends Handler {
        private final WeakReference mRenderThread;

        private RenderHandler(RenderThread renderThread) {
            this.mRenderThread = new WeakReference(renderThread);
        }

        public void handleMessage(Message message) {
            RenderThread renderThread = (RenderThread) this.mRenderThread.get();
            if (renderThread != null) {
                int i = message.what;
                if (i == 16) {
                    renderThread.doDraw();
                    renderThread.mEglThreadBlockVar.open();
                } else if (i == 32) {
                    renderThread.applyFilterId(message.arg1);
                } else if (i == 48) {
                    renderThread.doQuit();
                } else if (i == 64) {
                    renderThread.updateCinematic();
                }
            }
        }
    }

    public RenderThread(String str, int i, int i2, EGLContext eGLContext, Surface surface, boolean z) {
        super(str);
        this.mStreamWidth = i;
        this.mStreamHeight = i2;
        int i3 = this.mStreamWidth;
        this.mCanvasWidth = i3;
        int i4 = this.mStreamHeight;
        this.mCanvasHeight = i4;
        this.mTextureOffsetX = 0;
        this.mTextureOffsetY = 0;
        this.mTextureWidth = i3;
        this.mTextureHeight = i4;
        this.mShardContext = eGLContext;
        this.mSurface = surface;
        this.mIsRecordable = z;
    }

    /* access modifiers changed from: private */
    public void applyFilterId(int i) {
        if (this.mCanvas != null && this.mEglContextPrepared) {
            this.mCanvas.applyFilterId(i);
        }
    }

    /* access modifiers changed from: private */
    public void doDraw() {
        float f;
        float min;
        int i;
        int i2;
        if (!this.mRequestRelease && this.mEglContextPrepared) {
            synchronized (this.mLock) {
                boolean z = this.mRequestDraw > 0;
                if (z) {
                    this.mRequestDraw--;
                }
                if (z && this.mEglCore != null) {
                    this.mEglSurfaceBase.makeCurrent();
                    this.mCanvas.setSize(this.mCanvasWidth, this.mCanvasHeight);
                    this.mCanvas.getState().pushState();
                    int i3 = this.mDrawExtTexAttribute.mRotation;
                    if (!this.mIsOrientationLocked && i3 != 0) {
                        if (i3 == 270) {
                            i3 = -90;
                            min = ((float) Math.min(this.mCanvasWidth, this.mCanvasHeight)) * 1.0f;
                            i = this.mCanvasWidth;
                            i2 = this.mCanvasHeight;
                        } else if (i3 == 90) {
                            min = ((float) Math.min(this.mCanvasWidth, this.mCanvasHeight)) * 1.0f;
                            i = this.mCanvasWidth;
                            i2 = this.mCanvasHeight;
                        } else {
                            f = 1.0f;
                            this.mCanvas.getState().translate(((float) this.mCanvasWidth) / 2.0f, ((float) this.mCanvasHeight) / 2.0f);
                            this.mCanvas.getState().rotate((float) i3, 0.0f, 0.0f, 1.0f);
                            this.mCanvas.getState().scale(f, f, 1.0f);
                            this.mCanvas.getState().translate(((float) (-this.mCanvasWidth)) / 2.0f, ((float) (-this.mCanvasHeight)) / 2.0f);
                        }
                        f = min / ((float) Math.max(i, i2));
                        this.mCanvas.getState().translate(((float) this.mCanvasWidth) / 2.0f, ((float) this.mCanvasHeight) / 2.0f);
                        this.mCanvas.getState().rotate((float) i3, 0.0f, 0.0f, 1.0f);
                        this.mCanvas.getState().scale(f, f, 1.0f);
                        this.mCanvas.getState().translate(((float) (-this.mCanvasWidth)) / 2.0f, ((float) (-this.mCanvasHeight)) / 2.0f);
                    }
                    this.mCanvas.clearBuffer();
                    this.mCanvas.draw(this.mDrawExtTexAttribute);
                    this.mCanvas.getState().popState();
                    this.mEglSurfaceBase.swapBuffers();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void doQuit() {
        if (!this.mRequestRelease) {
            this.mRequestRelease = true;
            release();
            Looper.myLooper().quit();
        }
    }

    private void prepare() {
        this.mEglCore = new EglCore(this.mShardContext, this.mIsRecordable ? 3 : 2);
        this.mEglSurfaceBase = new EglSurfaceBase(this.mEglCore);
        this.mEglSurfaceBase.createWindowSurface(this.mSurface);
        this.mEglSurfaceBase.makeCurrent();
        this.mCanvas = new VideoRecorderCanvas();
        this.mCanvas.setSize(this.mCanvasWidth, this.mCanvasHeight);
    }

    private void release() {
        EglSurfaceBase eglSurfaceBase = this.mEglSurfaceBase;
        if (eglSurfaceBase != null) {
            eglSurfaceBase.releaseEglSurface();
            this.mEglSurfaceBase = null;
        }
        Surface surface = this.mSurface;
        if (surface != null) {
            surface.release();
            this.mSurface = null;
        }
        VideoRecorderCanvas videoRecorderCanvas = this.mCanvas;
        if (videoRecorderCanvas != null) {
            videoRecorderCanvas.deleteProgram();
            this.mCanvas.recycledResources();
            this.mCanvas = null;
        }
        EglCore eglCore = this.mEglCore;
        if (eglCore != null) {
            eglCore.release();
            this.mEglCore = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateCinematic() {
        VideoRecorderCanvas videoRecorderCanvas = this.mCanvas;
        if (videoRecorderCanvas != null) {
            videoRecorderCanvas.updateCinematic();
        }
    }

    private void updateCropRect() {
        int i = this.mStreamHeight;
        int i2 = this.mCanvasWidth;
        int i3 = i * i2;
        int i4 = this.mStreamWidth;
        int i5 = this.mCanvasHeight;
        if (i3 > i4 * i5) {
            this.mTextureWidth = i2;
            this.mTextureHeight = (int) ((((float) (i2 * i)) * 1.0f) / ((float) i4));
            this.mTextureOffsetX = 0;
            this.mTextureOffsetY = -((int) (((float) (this.mTextureHeight - i5)) / 2.0f));
            return;
        }
        if (i * i2 == i4 * i5) {
            this.mTextureWidth = i2;
            this.mTextureHeight = i5;
            this.mTextureOffsetX = 0;
        } else {
            this.mTextureWidth = (int) ((((float) (i4 * i5)) * 1.0f) / ((float) i));
            this.mTextureHeight = i5;
            this.mTextureOffsetX = -((int) (((float) (this.mTextureWidth - i2)) / 2.0f));
        }
        this.mTextureOffsetY = 0;
    }

    public void draw(DrawExtTexAttribute drawExtTexAttribute) {
        draw(drawExtTexAttribute, 0);
    }

    public void draw(DrawExtTexAttribute drawExtTexAttribute, int i) {
        synchronized (this.mLock) {
            if (!this.mRequestRelease) {
                if (this.mEglContextPrepared) {
                    this.mDrawExtTexAttribute.init(drawExtTexAttribute.mExtTexture, drawExtTexAttribute.mTextureTransform, this.mTextureOffsetX, this.mTextureOffsetY, this.mTextureWidth, this.mTextureHeight);
                    this.mDrawExtTexAttribute.mRotation = i;
                    this.mRequestDraw++;
                    this.mHandler.obtainMessage(16).sendToTarget();
                }
            }
        }
    }

    public RenderHandler getHandler() {
        synchronized (this.mLock) {
            if (!this.mReady) {
                throw new IllegalStateException("render thread is not ready yet");
            }
        }
        return this.mHandler;
    }

    public void quit() {
        this.mHandler.obtainMessage(48).sendToTarget();
    }

    public void run() {
        Looper.prepare();
        this.mHandler = new RenderHandler();
        Log.d(TAG, "prepare render thread: E");
        try {
            this.mEglContextPrepared = false;
            prepare();
            this.mEglContextPrepared = true;
        } catch (Exception e) {
            Log.d(TAG, "FATAL: failed to prepare render thread", (Throwable) e);
            release();
        }
        synchronized (this.mLock) {
            this.mReady = true;
            this.mLock.notify();
        }
        Looper.loop();
        synchronized (this.mLock) {
            this.mReady = false;
            this.mHandler = null;
        }
        Log.d(TAG, "prepare render thread: X");
    }

    public void setCanvasSize(int i, int i2) {
        this.mCanvasWidth = i;
        this.mCanvasHeight = i2;
        updateCropRect();
    }

    public void setCinematicEnable(boolean z) {
        this.mHandler.obtainMessage(64).sendToTarget();
    }

    public void setFilterId(int i) {
        Message obtainMessage = this.mHandler.obtainMessage(32);
        obtainMessage.arg1 = i;
        obtainMessage.sendToTarget();
    }

    public void setOrientationLocked(boolean z) {
        this.mIsOrientationLocked = z;
    }

    public void setStreamSize(int i, int i2) {
        this.mStreamWidth = i;
        this.mStreamHeight = i2;
        updateCropRect();
    }

    public void syncDraw(DrawExtTexAttribute drawExtTexAttribute) {
        this.mEglThreadBlockVar.close();
        draw(drawExtTexAttribute);
        this.mEglThreadBlockVar.block();
    }

    public void waitUntilReady() {
        synchronized (this.mLock) {
            while (!this.mReady) {
                try {
                    this.mLock.wait();
                } catch (InterruptedException e) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("waitUntilReady() interrupted: ");
                    sb.append(e);
                    Log.e(str, sb.toString());
                }
            }
        }
    }
}
