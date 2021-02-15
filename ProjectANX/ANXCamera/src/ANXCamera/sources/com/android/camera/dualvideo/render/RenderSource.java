package com.android.camera.dualvideo.render;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import com.android.camera.dualvideo.util.Assert;
import com.android.camera.log.Log;
import com.android.gallery3d.ui.ExtTexture;
import com.android.gallery3d.ui.GLCanvas;

public class RenderSource {
    private static final int SKIP_FRAMES = 2;
    private static final String TAG = "RenderSource";
    /* access modifiers changed from: private */
    public boolean mCanDraw = false;
    private boolean mFrameReady;
    /* access modifiers changed from: private */
    public int mFramesNeedSkip = 2;
    private Handler mHandler;
    /* access modifiers changed from: private */
    public SourceListener mListener;
    /* access modifiers changed from: private */
    public final int mSourceDeviceId;
    private Surface mSurface;
    private SurfaceTexture mSurfaceTexture;
    private ExtTexture mTexture;
    private Size mTextureSize;

    interface SourceListener {
        void onImageUpdated(int i);

        void onNewStreamAvailable();
    }

    public RenderSource(int i, Handler handler) {
        this.mSourceDeviceId = i;
        this.mHandler = handler;
        resetStatus();
    }

    private void createSurfaceTexture() {
        if (this.mSurfaceTexture == null) {
            this.mSurfaceTexture = new SurfaceTexture(0);
        }
        this.mSurfaceTexture.setDefaultBufferSize(this.mTextureSize.getWidth(), this.mTextureSize.getHeight());
        this.mSurface = new Surface(this.mSurfaceTexture);
        this.mSurfaceTexture.setOnFrameAvailableListener(new OnFrameAvailableListener() {
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                if (!RenderSource.this.mCanDraw) {
                    int access$100 = RenderSource.this.mFramesNeedSkip;
                    String str = RenderSource.TAG;
                    if (access$100 > 0) {
                        Log.d(str, "frame skipped: ");
                        RenderSource.this.mFramesNeedSkip = RenderSource.this.mFramesNeedSkip - 1;
                    } else {
                        Log.d(str, "subFrameReady");
                        RenderSource.this.mCanDraw = true;
                        RenderSource.this.mListener.onNewStreamAvailable();
                    }
                }
                RenderSource.this.mListener.onImageUpdated(RenderSource.this.mSourceDeviceId);
            }
        }, this.mHandler);
    }

    private void resetStatus() {
        this.mFramesNeedSkip = 2;
        this.mFrameReady = false;
        this.mCanDraw = false;
    }

    public void attachToGL(GLCanvas gLCanvas) {
        Log.d(TAG, "attachToGL: ");
        Assert.check(this.mSurfaceTexture != null);
        if (this.mTexture == null) {
            this.mTexture = new ExtTexture();
            this.mTexture.onBind(gLCanvas);
            this.mTexture.setSize(this.mTextureSize.getWidth(), this.mTextureSize.getHeight());
            this.mSurfaceTexture.detachFromGLContext();
            this.mSurfaceTexture.attachToGLContext(this.mTexture.getId());
        }
    }

    public boolean canDraw() {
        return this.mCanDraw;
    }

    public int getSourceDeviceId() {
        return this.mSourceDeviceId;
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mSurfaceTexture;
    }

    public int getTexId() {
        return this.mTexture.getId();
    }

    public ExtTexture getTexture() {
        return this.mTexture;
    }

    public void prepare(Size size) {
        Size size2 = this.mTextureSize;
        if (size2 == null) {
            this.mTextureSize = size;
            createSurfaceTexture();
        } else if (!size2.equals(size)) {
            this.mTextureSize = size;
            this.mSurfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());
        }
        resetStatus();
    }

    public void release(GLCanvas gLCanvas) {
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            this.mCanDraw = false;
            this.mHandler = null;
            this.mListener = null;
            if (gLCanvas != null) {
                gLCanvas.deleteSurfaceTexture(surfaceTexture);
            } else {
                surfaceTexture.release();
            }
            this.mSurfaceTexture = null;
            Surface surface = this.mSurface;
            if (surface != null) {
                surface.release();
                this.mSurface = null;
            }
            ExtTexture extTexture = this.mTexture;
            if (extTexture != null) {
                extTexture.recycle();
                this.mTexture = null;
            }
        }
    }

    public void setListener(SourceListener sourceListener) {
        this.mListener = sourceListener;
    }
}
