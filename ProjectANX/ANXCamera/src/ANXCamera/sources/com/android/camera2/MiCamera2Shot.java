package com.android.camera2;

import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureRequest.Builder;
import android.media.Image;
import android.os.Handler;
import com.android.camera.Thumbnail;
import com.android.camera.log.Log;
import com.android.camera2.Camera2Proxy.MagneticDetectedCallback;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.core.ParallelTaskData;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public abstract class MiCamera2Shot {
    protected static final long DEFAULT_TASK_DATA_TIMESTAMP = 0;
    private static final int SHUTTER_FRAMENUM_CAPTUREING = 1;
    private static final int SHUTTER_FRAMENUM_NONE = 0;
    private static final int SHUTTER_FRAMENUM_SHUTTERED = 2;
    protected Handler mCameraHandler;
    protected boolean mDeparted;
    private WeakReference mMagneticDetectedCallback;
    protected MiCamera2 mMiCamera;
    private ParallelCallback mParallelCallback;
    private PictureCallback mPictureCallback;
    protected int mPreviewThumbnailHash = -1;
    private boolean mQuickShotAnimation;
    protected Consumer mShotBoostParams;
    private int mShutterFrameNum;

    public MiCamera2Shot(MiCamera2 miCamera2) {
        this.mMiCamera = miCamera2;
        this.mCameraHandler = miCamera2.getCameraHandler();
        this.mShutterFrameNum = 0;
    }

    public abstract CaptureCallback generateCaptureCallback();

    /* access modifiers changed from: protected */
    public final ParallelTaskData generateParallelTaskData(long j) {
        return generateParallelTaskData(j, false);
    }

    /* access modifiers changed from: protected */
    public final ParallelTaskData generateParallelTaskData(long j, boolean z) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback == null) {
            Log.e(getClass().getSimpleName(), "null callback is not allowed!");
            return null;
        }
        ParallelTaskData parallelTaskData = new ParallelTaskData(this.mMiCamera.getId(), j, this.mMiCamera.getCameraConfigs().getShotType(), this.mMiCamera.getCameraConfigs().getShotPath(), this.mMiCamera.getCameraConfigs().getCaptureTime());
        return pictureCallback.onCaptureStart(parallelTaskData, this.mMiCamera.getPictureSize(), isQuickShotAnimation(), z, false, false);
    }

    public abstract Builder generateRequestBuilder();

    public MagneticDetectedCallback getMagneticDetectedCallback() {
        WeakReference weakReference = this.mMagneticDetectedCallback;
        if (weakReference != null) {
            return (MagneticDetectedCallback) weakReference.get();
        }
        return null;
    }

    public ParallelCallback getParallelCallback() {
        return this.mParallelCallback;
    }

    public PictureCallback getPictureCallback() {
        return this.mPictureCallback;
    }

    public Consumer getShotBoostParams() {
        return this.mShotBoostParams;
    }

    public abstract String getTag();

    /* access modifiers changed from: protected */
    public boolean isInQcfaMode() {
        return this.mMiCamera.getCapabilities().isSupportedQcfa() && (this.mMiCamera.getCapabilities().getOperatingMode() == 32775 || this.mMiCamera.getCapabilities().getOperatingMode() == 33013 || this.mMiCamera.getCapabilities().getOperatingMode() == 33011);
    }

    public boolean isQuickShotAnimation() {
        return this.mQuickShotAnimation;
    }

    public boolean isShutterReturned() {
        return true;
    }

    /* access modifiers changed from: protected */
    public void makeClobber() {
        this.mDeparted = true;
    }

    public abstract void notifyResultData(Object obj);

    public void onCaptureShutter() {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onCaptureShutter(true, false, false, false);
        }
    }

    public abstract void onImageReceived(Image image, int i);

    public boolean onPreviewComing() {
        if (!isQuickShotAnimation()) {
            return false;
        }
        int i = this.mShutterFrameNum;
        if (i == 0) {
            return false;
        }
        if (i >= 2) {
            return true;
        }
        this.mShutterFrameNum = i + 1;
        if (this.mShutterFrameNum != 2) {
            return false;
        }
        this.mShutterFrameNum = 2;
        onCaptureShutter();
        return true;
    }

    /* access modifiers changed from: protected */
    public final void onPreviewThumbnailReceived(Thumbnail thumbnail) {
        this.mPreviewThumbnailHash = thumbnail.hashCode();
    }

    public abstract void prepare();

    public void setMagneticDetectedCallback(MagneticDetectedCallback magneticDetectedCallback) {
        this.mMagneticDetectedCallback = new WeakReference(magneticDetectedCallback);
    }

    public void setParallelCallback(ParallelCallback parallelCallback) {
        this.mParallelCallback = parallelCallback;
    }

    public void setPictureCallback(PictureCallback pictureCallback) {
        this.mPictureCallback = pictureCallback;
    }

    public void setQuickShotAnimation(boolean z) {
        this.mQuickShotAnimation = z;
    }

    public abstract void startSessionCapture();

    /* access modifiers changed from: protected */
    public final void startShot() {
        Log.k(3, getTag(), "startShot");
        prepare();
        startSessionCapture();
        this.mShutterFrameNum = 1;
    }
}
