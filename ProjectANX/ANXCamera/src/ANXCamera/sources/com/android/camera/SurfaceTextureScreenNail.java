package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.opengl.Matrix;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import com.android.camera.effect.FrameBuffer;
import com.android.camera.effect.draw_mode.DrawExtTexAttribute;
import com.android.camera.effect.framework.gles.OpenGlUtils;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.ui.Rotatable;
import com.android.gallery3d.ui.BitmapTexture;
import com.android.gallery3d.ui.ExtTexture;
import com.android.gallery3d.ui.GLCanvas;
import com.android.gallery3d.ui.GLCanvasImpl;
import com.android.gallery3d.ui.RawTexture;
import com.android.gallery3d.ui.ScreenNail;
import java.util.Locale;

public abstract class SurfaceTextureScreenNail implements ScreenNail, OnFrameAvailableListener, Rotatable {
    private static final float MOVIE_SOLID_CROPPED_X = 0.8f;
    private static final float MOVIE_SOLID_CROPPED_Y = 0.8f;
    private static final String TAG = "STScreenNail";
    private static HandlerThread sFrameListener = new HandlerThread("FrameListener");
    private static int sMaxHighPriorityFrameCount = 8;
    private int currentFrameCount = 0;
    protected RawTexture mAnimTexture;
    protected BitmapTexture mBitmapTexture;
    protected int mCameraHeight;
    protected int mCameraWidth;
    protected FrameBuffer mCaptureAnimFrameBuffer;
    protected RawTexture mCaptureAnimTexture;
    private int mDisplayOrientation;
    protected Rect mDisplayRect;
    private DrawExtTexAttribute mDrawAttribute = new DrawExtTexAttribute();
    protected ExtTexture mExtTexture;
    protected ExternalFrameProcessor mExternalFrameProcessor;
    protected FrameBuffer mFrameBuffer;
    protected FrameBuffer mFullCaptureAnimFrameBuffer;
    protected RawTexture mFullCaptureAnimTexture;
    private boolean mHasTexture = false;
    private int mHeight;
    private boolean mIsFullScreen;
    private boolean mIsRatio16_9 = true;
    protected final Object mLock = new Object();
    protected boolean mModuleSwitching;
    private boolean mNeedCropped;
    protected PreviewSaveListener mPreviewSaveListener;
    private int mRenderHeight;
    protected Rect mRenderLayoutRect = new Rect();
    private int mRenderOffsetX;
    private int mRenderOffsetY;
    private int mRenderWidth;
    private float mScaleX = 1.0f;
    private float mScaleY = 1.0f;
    private SurfaceTextureScreenNailCallback mScreenNailCallback;
    protected boolean mSkipFirstFrame;
    private long mSurfaceCreatedTimestamp;
    protected int mSurfaceHeight;
    private SurfaceTexture mSurfaceTexture;
    protected int mSurfaceWidth;
    private int mTargetRatio = -1;
    protected int mTheight;
    private float[] mTransform = new float[16];
    protected int mTwidth;
    protected int mTx;
    protected int mTy;
    private int mUncroppedRenderHeight;
    private int mUncroppedRenderWidth;
    private boolean mVideoStabilizationCropped;
    private int mWidth;

    public interface ExternalFrameProcessor {
        int getProcessorType() {
            return 0;
        }

        boolean isProcessorReady();

        void onDrawFrame(Rect rect, int i, int i2, boolean z) {
        }

        void onDrawFrame(GLCanvasImpl gLCanvasImpl, CameraScreenNail cameraScreenNail) {
        }

        void releaseRender() {
        }
    }

    public class ExternalProcessorType {
        public static final int DEFAULT = 0;
        public static final int DUAL_VIDEO = 1;

        public ExternalProcessorType() {
        }
    }

    public interface PreviewSaveListener {
        void save(byte[] bArr, int i, int i2, int i3);
    }

    public interface SurfaceTextureScreenNailCallback {
        void onSurfaceTextureCreated(SurfaceTexture surfaceTexture);

        boolean onSurfaceTexturePending(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute) {
            return false;
        }

        void onSurfaceTextureReleased();

        void onSurfaceTextureUpdated(GLCanvas gLCanvas, DrawExtTexAttribute drawExtTexAttribute);
    }

    public SurfaceTextureScreenNail(SurfaceTextureScreenNailCallback surfaceTextureScreenNailCallback) {
        this.mScreenNailCallback = surfaceTextureScreenNailCallback;
    }

    private void checkThreadPriority() {
        int i = this.currentFrameCount;
        int i2 = sMaxHighPriorityFrameCount;
        if (i == i2) {
            Log.i(TAG, "normalHandlerCapacity:set normal");
            Process.setThreadPriority(sFrameListener.getThreadId(), 0);
            i = this.currentFrameCount;
        } else if (i >= i2) {
            return;
        }
        this.currentFrameCount = i + 1;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0084  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void computeRatio() {
        int i;
        boolean z = false;
        if (CameraSettings.getStrictAspectRatio(this.mRenderWidth, this.mRenderHeight) > -1 || !CameraSettings.isNearAspectRatio(this.mCameraWidth, this.mCameraHeight, this.mRenderWidth, this.mRenderHeight)) {
            int i2 = this.mCameraWidth;
            int i3 = this.mCameraHeight;
            int i4 = this.mTargetRatio;
            if (i4 != 0) {
                if (i4 == 1) {
                    this.mIsRatio16_9 = true;
                    this.mIsFullScreen = true;
                    if (!CameraSettings.isAspectRatio16_9(i2, i3)) {
                        this.mNeedCropped = true;
                        if (i2 * 16 > i3 * 9) {
                            int i5 = (int) ((((float) i3) * 9.0f) / 16.0f);
                            this.mScaleX = ((float) i5) / ((float) i2);
                            i2 = i5;
                        } else {
                            i = (int) ((((float) i2) * 16.0f) / 9.0f);
                            this.mScaleY = ((float) i) / ((float) i3);
                        }
                    } else {
                        this.mNeedCropped = false;
                        this.mScaleX = 1.0f;
                        this.mScaleY = 1.0f;
                    }
                } else if (i4 == 2) {
                    this.mIsFullScreen = false;
                    this.mIsRatio16_9 = false;
                    this.mNeedCropped = true;
                    if (i2 != i3) {
                        this.mScaleX = 1.0f;
                        this.mScaleY = ((float) i2) / ((float) i3);
                        i = i2;
                    }
                }
                i = i3;
            } else {
                this.mIsFullScreen = false;
                this.mIsRatio16_9 = false;
                if (!CameraSettings.isAspectRatio123(i2, i3)) {
                    this.mNeedCropped = !CameraSettings.isGifOn();
                    if (i2 * 4 > i3 * 3) {
                        int i6 = (int) (((float) i3) * 0.75f);
                        this.mScaleX = ((float) i6) / ((float) i2);
                        int i7 = i6;
                    } else {
                        int i8 = (int) ((((float) i2) * 4.0f) / 3.0f);
                        this.mScaleY = ((float) i8) / ((float) i3);
                        i3 = i8;
                    }
                } else {
                    this.mNeedCropped = false;
                    this.mScaleX = 1.0f;
                    this.mScaleY = 1.0f;
                }
                if (CameraSettings.sCroppedIfNeeded) {
                    this.mIsFullScreen = true;
                    this.mNeedCropped = true;
                    this.mIsRatio16_9 = true;
                    i = (int) ((((float) i2) * 16.0f) / 9.0f);
                    this.mScaleX *= 0.75f;
                } else {
                    i = i3;
                }
                if (C0124O00000oO.isPad()) {
                    this.mIsFullScreen = true;
                }
            }
            this.mWidth = i2;
            this.mHeight = i;
        } else {
            int i9 = this.mCameraWidth;
            if (i9 != 0) {
                int i10 = this.mCameraHeight;
                if (i10 != 0) {
                    int i11 = this.mRenderWidth;
                    if (i11 != 0) {
                        int i12 = this.mRenderHeight;
                        if (!(i12 == 0 || i11 * i10 == i12 * i9)) {
                            this.mNeedCropped = true;
                            if (i9 * i12 > i10 * i11) {
                                this.mHeight = i10;
                                this.mWidth = (i10 * i11) / i12;
                                this.mScaleX = ((float) this.mWidth) / ((float) i9);
                                this.mScaleY = 1.0f;
                            } else {
                                this.mWidth = i9;
                                this.mHeight = (i9 * i12) / i11;
                                this.mScaleX = 1.0f;
                                this.mScaleY = ((float) this.mHeight) / ((float) i10);
                            }
                            if ((((float) this.mRenderHeight) / ((float) this.mRenderWidth)) - (((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth())) < 0.1f) {
                                z = true;
                            }
                            this.mIsFullScreen = z;
                        }
                    }
                    this.mNeedCropped = false;
                    this.mScaleX = 1.0f;
                    this.mScaleY = 1.0f;
                    this.mWidth = this.mCameraWidth;
                    this.mHeight = this.mCameraHeight;
                    if ((((float) this.mRenderHeight) / ((float) this.mRenderWidth)) - (((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth())) < 0.1f) {
                    }
                    this.mIsFullScreen = z;
                }
            }
        }
        updateRenderSize();
        updateRenderRect();
    }

    private void initializePreviewTexture() {
        if (this.mExtTexture == null) {
            this.mExtTexture = new ExtTexture();
        }
        this.mExtTexture.setSize(this.mWidth, this.mHeight);
        if (C0124O00000oO.Oo000oO() && !sFrameListener.isAlive()) {
            sFrameListener.start();
        }
        if (this.mSurfaceTexture == null) {
            this.mSurfaceTexture = new SurfaceTexture(this.mExtTexture.getId());
        }
        this.mSurfaceCreatedTimestamp = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append("acquireSurfaceTexture: setDefaultBufferSize ");
        sb.append(this.mWidth);
        sb.append("x");
        sb.append(this.mHeight);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        int i = this.mWidth;
        int i2 = this.mHeight;
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (i > i2) {
            surfaceTexture.setDefaultBufferSize(i, i2);
        } else {
            surfaceTexture.setDefaultBufferSize(i2, i);
        }
        if (VERSION.SDK_INT < 21 || !C0124O00000oO.Oo000oO()) {
            this.mSurfaceTexture.setOnFrameAvailableListener(this);
        } else {
            CompatibilityUtils.setSurfaceTextureOnFrameAvailableListener(this.mSurfaceTexture, this, new Handler(sFrameListener.getLooper()));
            Log.i(str, "fullHandlerCapacity:set urgent display");
            Process.setThreadPriority(sFrameListener.getThreadId(), -8);
            this.currentFrameCount = 0;
        }
        SurfaceTextureScreenNailCallback surfaceTextureScreenNailCallback = this.mScreenNailCallback;
        if (surfaceTextureScreenNailCallback != null) {
            surfaceTextureScreenNailCallback.onSurfaceTextureCreated(this.mSurfaceTexture);
        }
    }

    private void updateRenderSize() {
        int i;
        if (2 != this.mTargetRatio) {
            this.mUncroppedRenderWidth = (int) (((float) this.mRenderWidth) / this.mScaleX);
            i = this.mRenderHeight;
        } else {
            i = this.mRenderWidth;
            this.mUncroppedRenderWidth = (int) (((float) i) / this.mScaleX);
        }
        this.mUncroppedRenderHeight = (int) (((float) i) / this.mScaleY);
    }

    public void acquireSurfaceTexture() {
        StringBuilder sb = new StringBuilder();
        sb.append("acquireSurfaceTexture: mHasTexture = ");
        sb.append(this.mHasTexture);
        Log.d(TAG, sb.toString());
        synchronized (this) {
            if (!this.mHasTexture) {
                initializePreviewTexture();
                this.mAnimTexture = new RawTexture(this.mWidth / 4, this.mHeight / 4, true);
                this.mCaptureAnimTexture = new RawTexture(Util.LIMIT_SURFACE_WIDTH, (this.mHeight * Util.LIMIT_SURFACE_WIDTH) / this.mWidth, true);
                this.mFrameBuffer = null;
                this.mCaptureAnimFrameBuffer = null;
                synchronized (this) {
                    this.mHasTexture = true;
                    this.mModuleSwitching = false;
                    this.mSkipFirstFrame = false;
                }
            }
        }
    }

    public void draw(GLCanvas gLCanvas) {
        if (this.mSkipFirstFrame) {
            this.mSkipFirstFrame = false;
            this.mSurfaceTexture.updateTexImage();
            return;
        }
        ExternalFrameProcessor externalFrameProcessor = this.mExternalFrameProcessor;
        if (externalFrameProcessor != null) {
            SurfaceTexture surfaceTexture = this.mSurfaceTexture;
            if (surfaceTexture != null) {
                surfaceTexture.updateTexImage();
                if ((!isAnimationRunning() || isAnimationGaussian()) && externalFrameProcessor.isProcessorReady()) {
                    return;
                }
            }
        }
        gLCanvas.clearBuffer();
        draw(gLCanvas, this.mTx, this.mTy, this.mTwidth, this.mTheight);
    }

    public void draw(GLCanvas gLCanvas, int i, int i2, int i3, int i4) {
        synchronized (this) {
            if (this.mHasTexture) {
                if (C0124O00000oO.Oo000oO()) {
                    checkThreadPriority();
                }
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("SurfaceTextureScreenNail draw start: ");
                sb.append(String.format(Locale.ENGLISH, "[%d %d %d %d] [%d %d %d %d]", new Object[]{Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(this.mSurfaceWidth), Integer.valueOf(this.mSurfaceHeight), Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight)}));
                OpenGlUtils.checkGlErrorAndWarning(str, sb.toString());
                gLCanvas.setPreviewSize(this.mWidth, this.mHeight);
                this.mSurfaceTexture.updateTexImage();
                this.mSurfaceTexture.getTransformMatrix(this.mTransform);
                gLCanvas.getState().pushState();
                updateTransformMatrix(this.mTransform);
                updateExtraTransformMatrix(this.mTransform);
                this.mDrawAttribute.init(this.mExtTexture, this.mTransform, i, i2, i3, i4);
                if (this.mScreenNailCallback == null) {
                    gLCanvas.draw(this.mDrawAttribute);
                } else {
                    if (!this.mScreenNailCallback.onSurfaceTexturePending(gLCanvas, this.mDrawAttribute)) {
                        gLCanvas.draw(this.mDrawAttribute);
                    }
                    this.mScreenNailCallback.onSurfaceTextureUpdated(gLCanvas, this.mDrawAttribute);
                }
                gLCanvas.getState().popState();
                OpenGlUtils.checkGlErrorAndWarning(TAG, "SurfaceTextureScreenNail draw end");
            }
        }
    }

    public void draw(GLCanvas gLCanvas, RectF rectF, RectF rectF2) {
        throw new UnsupportedOperationException();
    }

    public float[] getCurrentTransform() {
        return this.mTransform;
    }

    public ExtTexture getExtTexture() {
        ExtTexture extTexture;
        synchronized (this.mLock) {
            extTexture = this.mExtTexture;
        }
        return extTexture;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getRenderHeight() {
        return this.mUncroppedRenderHeight;
    }

    public int getRenderTargetRatio() {
        return this.mTargetRatio;
    }

    public int getRenderWidth() {
        return this.mUncroppedRenderWidth;
    }

    public long getSurfaceCreatedTimestamp() {
        return this.mSurfaceCreatedTimestamp;
    }

    public SurfaceTexture getSurfaceTexture() {
        SurfaceTexture surfaceTexture;
        synchronized (this.mLock) {
            surfaceTexture = this.mSurfaceTexture;
        }
        return surfaceTexture;
    }

    public int getTranslateY() {
        return this.mTy;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public boolean isAnimationGaussian() {
        return false;
    }

    public boolean isAnimationRunning() {
        return false;
    }

    public abstract void noDraw();

    public abstract void onFrameAvailable(SurfaceTexture surfaceTexture);

    public abstract void recycle();

    public abstract void releaseBitmapIfNeeded();

    public void releaseSurfaceTexture() {
        StringBuilder sb = new StringBuilder();
        sb.append("releaseSurfaceTexture: mHasTexture = ");
        sb.append(this.mHasTexture);
        Log.d(TAG, sb.toString());
        synchronized (this) {
            this.mHasTexture = false;
        }
        ExtTexture extTexture = this.mExtTexture;
        if (extTexture != null) {
            extTexture.recycle();
            this.mExtTexture = null;
        }
        SurfaceTexture surfaceTexture = this.mSurfaceTexture;
        if (surfaceTexture != null) {
            surfaceTexture.release();
            this.mSurfaceTexture.setOnFrameAvailableListener(null);
            this.mSurfaceTexture = null;
            this.mSurfaceCreatedTimestamp = 0;
        }
        SurfaceTextureScreenNailCallback surfaceTextureScreenNailCallback = this.mScreenNailCallback;
        if (surfaceTextureScreenNailCallback != null) {
            surfaceTextureScreenNailCallback.onSurfaceTextureReleased();
        }
        RawTexture rawTexture = this.mAnimTexture;
        if (rawTexture != null) {
            rawTexture.recycle();
            this.mAnimTexture = null;
        }
        RawTexture rawTexture2 = this.mCaptureAnimTexture;
        if (rawTexture2 != null) {
            rawTexture2.recycle();
            this.mCaptureAnimTexture = null;
        }
        this.mFrameBuffer = null;
        this.mCaptureAnimFrameBuffer = null;
        releaseBitmapIfNeeded();
    }

    public void setDisplayArea(Rect rect) {
        this.mDisplayRect = rect;
        this.mRenderOffsetX = rect.left;
        this.mRenderOffsetY = rect.top;
        this.mRenderWidth = rect.width();
        this.mRenderHeight = rect.height();
        computeRatio();
    }

    public void setDisplayOrientation(int i) {
        this.mDisplayOrientation = i;
    }

    public void setOrientation(int i, boolean z) {
    }

    public void setPreviewSize(int i, int i2) {
        if (i > i2) {
            this.mCameraWidth = i2;
            this.mCameraHeight = i;
        } else {
            this.mCameraWidth = i;
            this.mCameraHeight = i2;
        }
        this.mTargetRatio = CameraSettings.getRenderAspectRatio(i, i2);
        computeRatio();
        if (this.mSurfaceTexture != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("setDefaultBufferSize: ");
            sb.append(i);
            sb.append(" | ");
            sb.append(i2);
            Log.d(TAG, sb.toString());
            this.mSurfaceTexture.setDefaultBufferSize(i, i2);
            this.mExtTexture.setSize(i, i2);
        }
    }

    public void setVideoStabilizationCropped(boolean z) {
        if (!C0124O00000oO.Oo00o0()) {
            z = false;
        }
        this.mVideoStabilizationCropped = z;
    }

    /* access modifiers changed from: protected */
    public void updateExtraTransformMatrix(float[] fArr) {
    }

    /* access modifiers changed from: protected */
    public void updateRenderRect() {
        int i = 0;
        if (this.mTargetRatio == 2) {
            int i2 = this.mRenderWidth;
            this.mTx = i2 == 0 ? 0 : (this.mRenderOffsetX * this.mSurfaceWidth) / i2;
            int i3 = this.mSurfaceHeight;
            int i4 = (i3 - this.mSurfaceWidth) / 2;
            int i5 = this.mRenderHeight;
            if (i5 != 0) {
                i = (this.mRenderOffsetY * i3) / i5;
            }
            this.mTy = i4 + i;
            int i6 = this.mSurfaceWidth;
            this.mTwidth = i6;
            this.mTheight = i6;
            Rect rect = this.mRenderLayoutRect;
            int i7 = this.mRenderOffsetX;
            int i8 = this.mRenderHeight;
            int i9 = this.mRenderWidth;
            int i10 = (i8 - i9) / 2;
            int i11 = this.mRenderOffsetY;
            rect.set(i7, i10 + i11, i9 + i7, ((i8 - i9) / 2) + i11 + i9);
            return;
        }
        int i12 = this.mRenderWidth;
        this.mTx = i12 == 0 ? 0 : (this.mRenderOffsetX * this.mSurfaceWidth) / i12;
        int i13 = this.mRenderHeight;
        this.mTy = i13 == 0 ? 0 : (this.mRenderOffsetY * this.mSurfaceHeight) / i13;
        this.mTwidth = this.mSurfaceWidth;
        this.mTheight = this.mSurfaceHeight;
        this.mRenderLayoutRect.set(0, 0, this.mRenderWidth, this.mRenderHeight);
    }

    /* access modifiers changed from: protected */
    public void updateTransformMatrix(float[] fArr) {
        boolean z;
        float f;
        float f2;
        boolean z2 = true;
        if (!this.mVideoStabilizationCropped || !ModuleManager.isInVideoCategory()) {
            f2 = 1.0f;
            f = 1.0f;
            z = false;
        } else {
            f2 = 0.8f;
            f = 0.8f;
            z = true;
        }
        if (this.mNeedCropped) {
            f2 *= this.mScaleX;
            f *= this.mScaleY;
            z = true;
        }
        if (this.mDisplayOrientation == 0) {
            z2 = z;
        }
        if (z2) {
            Matrix.translateM(fArr, 0, 0.5f, 0.5f, 0.0f);
            Matrix.rotateM(fArr, 0, (float) this.mDisplayOrientation, 0.0f, 0.0f, 1.0f);
            Matrix.scaleM(fArr, 0, f2, f, 1.0f);
            Matrix.translateM(fArr, 0, -0.5f, -0.5f, 0.0f);
        }
    }
}
