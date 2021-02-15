package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.utils.SurfaceUtils;
import android.media.Image;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.imagecodec.ImagePool;
import java.util.Locale;

public class MiCamera2ShotParallelRepeating extends MiCamera2ShotParallel {
    /* access modifiers changed from: private */
    public static final int DROP_IMAGE_COUNT = C0122O00000o.instance().OO000oo();
    private static final int INVALID_SEQUENCE_ID = -1;
    private static final String TAG = "ParallelRepeating";
    private boolean isFirstRequest = true;
    private int mCaptureRequestNum;
    /* access modifiers changed from: private */
    public int mCapturedNum;
    /* access modifiers changed from: private */
    public int mCurrentSequenceId;
    /* access modifiers changed from: private */
    public final boolean mIsSatFusionShotEnabled;
    /* access modifiers changed from: private */
    public boolean mIsSupportP2done = false;
    /* access modifiers changed from: private */
    public int mLatestSequenceId;
    private int mMaxCapturedNum;
    /* access modifiers changed from: private */
    public CaptureResult mRepeatingCaptureResult;
    private final Surface mZoomMapSurface;

    public MiCamera2ShotParallelRepeating(MiCamera2 miCamera2, int i, Surface surface) {
        super(miCamera2);
        boolean z = true;
        this.mMaxCapturedNum = i;
        this.mIsSatFusionShotEnabled = false;
        this.mZoomMapSurface = surface;
        if (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOOO00O() || !this.mMiCamera.getCapabilities().isSupportP2done() || !C0122O00000o.instance().OOOO0O()) {
            z = false;
        }
        this.mIsSupportP2done = z;
    }

    /* access modifiers changed from: private */
    public void onRepeatingEnd(boolean z, int i) {
        this.mMiCamera.setAWBLock(false);
        this.mMiCamera.resumePreview();
        if (-1 != i) {
            PictureCallback pictureCallback = getPictureCallback();
            if (pictureCallback != null) {
                pictureCallback.onPictureTakenFinished(z);
            } else {
                Log.w(TAG, "onRepeatingEnd: null picture callback");
            }
            this.mMiCamera.onMultiSnapEnd(z, this);
        }
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            long mCaptureTimestamp = -1;

            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                super.onCaptureCompleted(cameraCaptureSession, captureRequest, totalCaptureResult);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: frameNumber=");
                sb.append(totalCaptureResult.getFrameNumber());
                String sb2 = sb.toString();
                String str = MiCamera2ShotParallelRepeating.TAG;
                Log.d(str, sb2);
                MiCamera2ShotParallelRepeating.this.mRepeatingCaptureResult = totalCaptureResult;
                AlgoConnector.getInstance().getLocalBinder().onCaptureCompleted(CameraDeviceUtil.getCustomCaptureResult(MiCamera2ShotParallelRepeating.this.mRepeatingCaptureResult), true);
                if (C0124O00000oO.isMTKPlatform() && C0122O00000o.instance().OOOO00O() && !MiCamera2ShotParallelRepeating.this.mIsSupportP2done) {
                    if (MiCamera2ShotParallelRepeating.this.mMiCamera.isMultiSnapStopRequest()) {
                        MiCamera2ShotParallelRepeating miCamera2ShotParallelRepeating = MiCamera2ShotParallelRepeating.this;
                        miCamera2ShotParallelRepeating.mLatestSequenceId = miCamera2ShotParallelRepeating.mCurrentSequenceId;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("onCaptureCompleted: latestSequenceId: ");
                        sb3.append(MiCamera2ShotParallelRepeating.this.mLatestSequenceId);
                        Log.d(str, sb3.toString());
                        return;
                    }
                    MiCamera2ShotParallelRepeating.this.startSessionCapture();
                }
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: reason=");
                sb.append(captureFailure.getReason());
                sb.append(" timestamp=");
                sb.append(this.mCaptureTimestamp);
                sb.append(" frameNumber=");
                sb.append(captureFailure.getFrameNumber());
                Log.e(MiCamera2ShotParallelRepeating.TAG, sb.toString());
                MiCamera2ShotParallelRepeating.this.onRepeatingEnd(false, -1);
                if (this.mCaptureTimestamp != -1) {
                    AlgoConnector.getInstance().getLocalBinder().onCaptureFailed(this.mCaptureTimestamp, captureFailure.getReason());
                }
            }

            public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
                super.onCaptureProgressed(cameraCaptureSession, captureRequest, captureResult);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureProgressed: frameNumber=");
                sb.append(captureResult.getFrameNumber());
                String sb2 = sb.toString();
                String str = MiCamera2ShotParallelRepeating.TAG;
                Log.d(str, sb2);
                if (MiCamera2ShotParallelRepeating.this.mIsSupportP2done && CaptureResultParser.isP2doneReady(captureResult)) {
                    if (MiCamera2ShotParallelRepeating.this.mMiCamera.isMultiSnapStopRequest()) {
                        MiCamera2ShotParallelRepeating miCamera2ShotParallelRepeating = MiCamera2ShotParallelRepeating.this;
                        miCamera2ShotParallelRepeating.mLatestSequenceId = miCamera2ShotParallelRepeating.mCurrentSequenceId;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("onCaptureProgressed:  latestSequenceId: ");
                        sb3.append(MiCamera2ShotParallelRepeating.this.mLatestSequenceId);
                        Log.d(str, sb3.toString());
                        return;
                    }
                    MiCamera2ShotParallelRepeating.this.startSessionCapture();
                }
            }

            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession cameraCaptureSession, int i) {
                super.onCaptureSequenceAborted(cameraCaptureSession, i);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureSequenceAborted: sequenceId=");
                sb.append(i);
                Log.w(MiCamera2ShotParallelRepeating.TAG, sb.toString());
                MiCamera2ShotParallelRepeating.this.onRepeatingEnd(false, i);
                ImagePool.getInstance().trimPoolBuffer();
            }

            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession cameraCaptureSession, int i, long j) {
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureSequenceCompleted: sequenceId=");
                sb.append(i);
                sb.append(" latestSequenceId= ");
                sb.append(MiCamera2ShotParallelRepeating.this.mLatestSequenceId);
                sb.append(" frameNumber=");
                sb.append(j);
                Log.d(MiCamera2ShotParallelRepeating.TAG, sb.toString());
                if (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOOO00O() || MiCamera2ShotParallelRepeating.this.mLatestSequenceId == i) {
                    MiCamera2ShotParallelRepeating.this.onRepeatingEnd(true, i);
                    ImagePool.getInstance().trimPoolBuffer();
                }
            }

            public void onCaptureStarted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, long j, long j2) {
                String str;
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStarted: timestamp=");
                sb.append(j);
                sb.append(" frameNumber=");
                sb.append(j2);
                String sb2 = sb.toString();
                String str2 = MiCamera2ShotParallelRepeating.TAG;
                Log.d(str2, sb2);
                boolean z = false;
                if (MiCamera2ShotParallelRepeating.DROP_IMAGE_COUNT > 0) {
                    MiCamera2ShotParallelRepeating.this.mCapturedNum = MiCamera2ShotParallelRepeating.this.mCapturedNum + 1;
                    if (MiCamera2ShotParallelRepeating.this.mCapturedNum % MiCamera2ShotParallelRepeating.DROP_IMAGE_COUNT == 0) {
                        z = true;
                    }
                }
                if (z) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("onCaptureStarted: drop task ");
                    sb3.append(j);
                    Log.d(str2, sb3.toString());
                    ParallelTaskData parallelTaskData = new ParallelTaskData(MiCamera2ShotParallelRepeating.this.mMiCamera.getId(), j, MiCamera2ShotParallelRepeating.this.mMiCamera.getCameraConfigs().getShotType(), "");
                    parallelTaskData.setIsSatFusionShot(MiCamera2ShotParallelRepeating.this.mIsSatFusionShotEnabled);
                    parallelTaskData.setAlgoType(4);
                    parallelTaskData.setBurstNum(1);
                    if (C0122O00000o.instance().OOo0oOO()) {
                        parallelTaskData.setRequireTuningData(true);
                    }
                    parallelTaskData.setAbandoned(true);
                    this.mCaptureTimestamp = j;
                    AlgoConnector.getInstance().getLocalBinder().onCaptureStarted(parallelTaskData);
                    return;
                }
                PictureCallback pictureCallback = MiCamera2ShotParallelRepeating.this.getPictureCallback();
                if (pictureCallback != null) {
                    ParallelTaskData parallelTaskData2 = new ParallelTaskData(MiCamera2ShotParallelRepeating.this.mMiCamera.getId(), j, MiCamera2ShotParallelRepeating.this.mMiCamera.getCameraConfigs().getShotType(), null);
                    MiCamera2ShotParallelRepeating miCamera2ShotParallelRepeating = MiCamera2ShotParallelRepeating.this;
                    ParallelTaskData onCaptureStart = pictureCallback.onCaptureStart(parallelTaskData2, miCamera2ShotParallelRepeating.mAlgoSize, miCamera2ShotParallelRepeating.isQuickShotAnimation(), false, false, false);
                    if (onCaptureStart != null) {
                        onCaptureStart.setIsSatFusionShot(MiCamera2ShotParallelRepeating.this.mIsSatFusionShotEnabled);
                        onCaptureStart.setAlgoType(4);
                        onCaptureStart.setBurstNum(1);
                        this.mCaptureTimestamp = j;
                        AlgoConnector.getInstance().getLocalBinder().onCaptureStarted(onCaptureStart);
                        return;
                    }
                    str = "onCaptureStarted: null task data";
                } else {
                    str = "onCaptureStarted: null picture callback";
                }
                Log.w(str2, str);
            }
        };
    }

    /* access modifiers changed from: protected */
    public Builder generateRequestBuilder() {
        Builder builder;
        boolean isMTKPlatform = C0124O00000oO.isMTKPlatform();
        String str = TAG;
        if (!isMTKPlatform) {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
        } else if (C0122O00000o.instance().OOOO00O()) {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
            MiCameraCompat.applyCShotFeatureCapture(builder, true);
            MiCameraCompat.applyNotificationTrigger(builder, true);
        } else {
            builder = this.mMiCamera.getCameraDevice().createCaptureRequest(1);
            Log.d(str, "applyPanoramaP2SEnabled true");
            MiCameraCompat.applyPanoramaP2SEnabled(builder, true);
        }
        String str2 = "add surface %s to capture request, size is: %s";
        if (isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) {
            Surface mainCaptureSurface = getMainCaptureSurface();
            Size surfaceSize = SurfaceUtils.getSurfaceSize(mainCaptureSurface);
            Log.d(str, String.format(Locale.ENGLISH, str2, new Object[]{mainCaptureSurface, surfaceSize}));
            builder.addTarget(mainCaptureSurface);
            int i = 513;
            if (mainCaptureSurface == this.mMiCamera.getUltraWideRemoteSurface()) {
                i = 3;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("combinationMode: ");
            sb.append(i);
            Log.d(str, sb.toString());
            configParallelSession(surfaceSize, i);
        } else {
            for (Surface surface : this.mMiCamera.getRemoteSurfaceList()) {
                if (!(surface == this.mMiCamera.getActiveRawSurface() || surface == this.mMiCamera.getRawSurfaceForTuningBuffer())) {
                    if (surface != this.mMiCamera.getTuningRemoteSurface()) {
                        Log.d(str, String.format(Locale.ENGLISH, str2, new Object[]{surface, SurfaceUtils.getSurfaceSize(surface)}));
                        builder.addTarget(surface);
                    }
                }
            }
            this.mAlgoSize = this.mMiCamera.getPictureSize();
        }
        if (!this.mIsSupportP2done) {
            builder.addTarget(this.mMiCamera.getPreviewSurface());
        }
        if (C0122O00000o.instance().OOo0oOO()) {
            Surface tuningRemoteSurface = this.mMiCamera.getTuningRemoteSurface();
            if (tuningRemoteSurface != null) {
                Log.d(str, "add tuning surface to capture request, size is: %s", SurfaceUtils.getSurfaceSize(tuningRemoteSurface));
                builder.addTarget(tuningRemoteSurface);
            }
        }
        builder.set(CaptureRequest.CONTROL_AF_MODE, (Integer) this.mMiCamera.getPreviewRequestBuilder().get(CaptureRequest.CONTROL_AF_MODE));
        CameraCapabilities capabilities = this.mMiCamera.getCapabilities();
        this.mMiCamera.applySettingsForCapture(builder, 3);
        MiCameraCompat.applyMfnrEnable(builder, false);
        MiCameraCompat.applyHDR(builder, false);
        MiCameraCompat.applySuperResolution(builder, false);
        MiCameraCompat.applyDepurpleEnable(builder, false);
        if (capabilities.isSupportBeauty()) {
            BeautyValues beautyValues = new BeautyValues();
            beautyValues.mBeautyLevel = BeautyConstant.LEVEL_CLOSE;
            MiCameraCompat.applyBeautyParameter(builder, capabilities.getCaptureRequestVendorKeys(), beautyValues);
        }
        if (C0124O00000oO.isMTKPlatform() && C0122O00000o.instance().OOOO00O() && !this.mIsSupportP2done) {
            MiCameraCompat.applyZsl(builder, false);
        }
        if (isIn3OrMoreSatMode() && !C0124O00000oO.isMTKPlatform()) {
            CaptureRequestBuilder.applySmoothTransition(builder, capabilities, false);
            CaptureRequestBuilder.applySatFallback(builder, capabilities, false);
        }
        MiCameraCompat.applyMultiFrameInputNum(builder, 1);
        if (capabilities.isSupportBurstHint()) {
            MiCameraCompat.applyBurstHint(builder, 1);
        }
        return builder;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(byte[] bArr) {
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
    }

    /* access modifiers changed from: protected */
    public void prepare() {
        this.mMiCamera.setAWBLock(true);
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        String str;
        if (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOOO00O() || this.mCaptureRequestNum < this.mMaxCapturedNum) {
            StringBuilder sb = new StringBuilder();
            sb.append("startSessionCapture: ");
            sb.append(this.isFirstRequest);
            sb.append("  mIsSupportP2done: ");
            sb.append(this.mIsSupportP2done);
            String sb2 = sb.toString();
            String str2 = TAG;
            Log.d(str2, sb2);
            PerformanceTracker.trackPictureCapture(0);
            if (!this.mIsSupportP2done) {
                this.mMiCamera.pausePreview();
            }
            try {
                CaptureCallback generateCaptureCallback = generateCaptureCallback();
                Builder generateRequestBuilder = generateRequestBuilder();
                if (this.mZoomMapSurface != null) {
                    generateRequestBuilder.addTarget(this.mZoomMapSurface);
                }
                if (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOOO00O()) {
                    int repeatingRequest = this.mMiCamera.getCaptureSession().setRepeatingRequest(generateRequestBuilder.build(), generateCaptureCallback, this.mCameraHandler);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("repeating sequenceId: ");
                    sb3.append(repeatingRequest);
                    Log.d(str2, sb3.toString());
                }
                int i = this.isFirstRequest ? 3 : 1;
                this.isFirstRequest = false;
                for (int i2 = 0; i2 < i; i2++) {
                    int capture = this.mMiCamera.getCaptureSession().capture(generateRequestBuilder.build(), generateCaptureCallback, this.mCameraHandler);
                    this.mCaptureRequestNum++;
                    this.mCurrentSequenceId = capture;
                    if (this.mCaptureRequestNum == this.mMaxCapturedNum) {
                        this.mLatestSequenceId = capture;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("mtk cshot repeating latestSequenceId: ");
                        sb4.append(this.mLatestSequenceId);
                        Log.d(str2, sb4.toString());
                    }
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("mtk cshot repeating sequenceId: ");
                    sb5.append(capture);
                    sb5.append(" captureRequestNum=");
                    sb5.append(this.mCaptureRequestNum);
                    Log.d(str2, sb5.toString());
                }
            } catch (CameraAccessException e) {
                e.printStackTrace();
                this.mMiCamera.notifyOnError(e.getReason());
            } catch (IllegalStateException e2) {
                e = e2;
                str = "Failed to capture burst, IllegalState";
                Log.e(str2, str, e);
                this.mMiCamera.notifyOnError(256);
            } catch (IllegalArgumentException e3) {
                e = e3;
                str = "Failed to capture a still picture, IllegalArgument";
                Log.e(str2, str, e);
                this.mMiCamera.notifyOnError(256);
            }
        }
    }
}
