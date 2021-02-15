package net.majorkernelpanic.streaming.gl;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.View.MeasureSpec;
import java.util.concurrent.Semaphore;

public class SurfaceView extends android.view.SurfaceView implements Runnable, OnFrameAvailableListener, Callback {
    public static final int ASPECT_RATIO_PREVIEW = 1;
    public static final int ASPECT_RATIO_STRETCH = 0;
    public static final String TAG = "SurfaceView";
    /* access modifiers changed from: private */
    public int mAspectRatioMode;
    private SurfaceManager mCodecSurfaceManager;
    private boolean mFrameAvailable;
    private Handler mHandler;
    private final Semaphore mLock;
    private boolean mRunning;
    private final Object mSyncObject;
    private TextureManager mTextureManager;
    private Thread mThread;
    private ViewAspectRatioMeasurer mVARM;
    private SurfaceManager mViewSurfaceManager;

    public class ViewAspectRatioMeasurer {
        private double aspectRatio;
        private Integer measuredHeight = null;
        private Integer measuredWidth = null;

        public ViewAspectRatioMeasurer() {
        }

        public double getAspectRatio() {
            return this.aspectRatio;
        }

        public int getMeasuredHeight() {
            Integer num = this.measuredHeight;
            if (num != null) {
                return num.intValue();
            }
            throw new IllegalStateException("You need to run measure() before trying to get measured dimensions");
        }

        public int getMeasuredWidth() {
            Integer num = this.measuredWidth;
            if (num != null) {
                return num.intValue();
            }
            throw new IllegalStateException("You need to run measure() before trying to get measured dimensions");
        }

        public void measure(int i, int i2) {
            measure(i, i2, this.aspectRatio);
        }

        public void measure(int i, int i2, double d) {
            Integer num;
            Integer valueOf;
            int mode = MeasureSpec.getMode(i);
            int i3 = Integer.MAX_VALUE;
            int size = mode == 0 ? Integer.MAX_VALUE : MeasureSpec.getSize(i);
            int mode2 = MeasureSpec.getMode(i2);
            if (mode2 != 0) {
                i3 = MeasureSpec.getSize(i2);
            }
            if (mode2 == 1073741824 && mode == 1073741824) {
                this.measuredWidth = Integer.valueOf(size);
                num = Integer.valueOf(i3);
            } else {
                if (mode2 == 1073741824) {
                    size = (int) Math.min((double) size, ((double) i3) * d);
                } else {
                    if (mode == 1073741824) {
                        valueOf = Integer.valueOf((int) Math.min((double) i3, ((double) size) / d));
                    } else if (((double) size) > ((double) i3) * d) {
                        valueOf = Integer.valueOf(i3);
                    }
                    this.measuredHeight = valueOf;
                    this.measuredWidth = Integer.valueOf((int) (((double) this.measuredHeight.intValue()) * d));
                    return;
                }
                this.measuredWidth = Integer.valueOf(size);
                num = Integer.valueOf((int) (((double) this.measuredWidth.intValue()) / d));
            }
            this.measuredHeight = num;
        }

        public void setAspectRatio(double d) {
            this.aspectRatio = d;
        }
    }

    public SurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mThread = null;
        this.mHandler = null;
        this.mFrameAvailable = false;
        this.mRunning = true;
        this.mAspectRatioMode = 0;
        this.mViewSurfaceManager = null;
        this.mCodecSurfaceManager = null;
        this.mTextureManager = null;
        this.mLock = new Semaphore(0);
        this.mSyncObject = new Object();
        this.mVARM = new ViewAspectRatioMeasurer();
        this.mHandler = new Handler();
        getHolder().addCallback(this);
    }

    public void addMediaCodecSurface(Surface surface) {
        synchronized (this.mSyncObject) {
            this.mCodecSurfaceManager = new SurfaceManager(surface, this.mViewSurfaceManager);
        }
    }

    public SurfaceTexture getSurfaceTexture() {
        return this.mTextureManager.getSurfaceTexture();
    }

    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        synchronized (this.mSyncObject) {
            this.mFrameAvailable = true;
            this.mSyncObject.notifyAll();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mVARM.getAspectRatio() <= 0.0d || this.mAspectRatioMode != 1) {
            super.onMeasure(i, i2);
            return;
        }
        this.mVARM.measure(i, i2);
        setMeasuredDimension(this.mVARM.getMeasuredWidth(), this.mVARM.getMeasuredHeight());
    }

    public void removeMediaCodecSurface() {
        synchronized (this.mSyncObject) {
            if (this.mCodecSurfaceManager != null) {
                this.mCodecSurfaceManager.release();
                this.mCodecSurfaceManager = null;
            }
        }
    }

    public void requestAspectRatio(double d) {
        if (this.mVARM.getAspectRatio() != d) {
            this.mVARM.setAspectRatio(d);
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (SurfaceView.this.mAspectRatioMode == 1) {
                        SurfaceView.this.requestLayout();
                    }
                }
            });
        }
    }

    public void run() {
        this.mViewSurfaceManager = new SurfaceManager(getHolder().getSurface());
        this.mViewSurfaceManager.makeCurrent();
        this.mTextureManager.createTexture().setOnFrameAvailableListener(this);
        this.mLock.release();
        while (this.mRunning) {
            try {
                synchronized (this.mSyncObject) {
                    this.mSyncObject.wait(2500);
                    if (this.mFrameAvailable) {
                        this.mFrameAvailable = false;
                        this.mViewSurfaceManager.makeCurrent();
                        this.mTextureManager.updateFrame();
                        this.mTextureManager.drawFrame();
                        this.mViewSurfaceManager.swapBuffer();
                        if (this.mCodecSurfaceManager != null) {
                            this.mCodecSurfaceManager.makeCurrent();
                            this.mTextureManager.drawFrame();
                            this.mCodecSurfaceManager.setPresentationTime(this.mTextureManager.getSurfaceTexture().getTimestamp());
                            this.mCodecSurfaceManager.swapBuffer();
                        }
                    } else {
                        Log.e(TAG, "No frame received !");
                    }
                }
            } catch (InterruptedException unused) {
            } catch (Throwable th) {
                this.mViewSurfaceManager.release();
                this.mTextureManager.release();
                throw th;
            }
        }
        this.mViewSurfaceManager.release();
        this.mTextureManager.release();
    }

    public void setAspectRatioMode(int i) {
        this.mAspectRatioMode = i;
    }

    public void startGLThread() {
        Log.d(TAG, "Thread started.");
        if (this.mTextureManager == null) {
            this.mTextureManager = new TextureManager();
        }
        if (this.mTextureManager.getSurfaceTexture() == null) {
            this.mThread = new Thread(this);
            this.mRunning = true;
            this.mThread.start();
            this.mLock.acquireUninterruptibly();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        Thread thread = this.mThread;
        if (thread != null) {
            thread.interrupt();
        }
        this.mRunning = false;
    }
}
