package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.Image;
import android.media.Image.Plane;
import android.media.ImageReader;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraSettings;
import com.android.camera.JpegUtil;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.storage.Storage;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.core.ParallelTaskDataParameter;
import com.xiaomi.stat.d;
import java.util.Arrays;

public class MiCamera2ShotStill extends MiCamera2Shot {
    /* access modifiers changed from: private */
    public static final String TAG = "MiCamera2ShotStill";
    /* access modifiers changed from: private */
    public TotalCaptureResult mCaptureResult;
    /* access modifiers changed from: private */
    public ParallelTaskData mCurrentParallelTaskData;
    private boolean mHasDepth;
    /* access modifiers changed from: private */
    public boolean mIsIntent;
    /* access modifiers changed from: private */
    public boolean mNeedCaptureResult;

    public MiCamera2ShotStill(MiCamera2 miCamera2) {
        super(miCamera2);
    }

    /* access modifiers changed from: private */
    public void notifyResultData(ParallelTaskData parallelTaskData, @Nullable CaptureResult captureResult, @Nullable CameraCharacteristics cameraCharacteristics) {
        ParallelCallback parallelCallback = getParallelCallback();
        if (parallelCallback == null) {
            Log.w(TAG, "notifyResultData: null parallel callback");
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        this.mCurrentParallelTaskData.setPreviewThumbnailHash(this.mPreviewThumbnailHash);
        parallelCallback.onParallelProcessFinish(parallelTaskData, captureResult, cameraCharacteristics);
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("mJpegCallbackFinishTime = ");
        sb.append(currentTimeMillis2);
        sb.append(d.H);
        Log.d(str, sb.toString());
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                String access$000 = MiCamera2ShotStill.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: ");
                sb.append(totalCaptureResult.getFrameNumber());
                Log.d(access$000, sb.toString());
                boolean z = false;
                if (MiCamera2ShotStill.this.mMiCamera.getSuperNight()) {
                    MiCamera2ShotStill.this.mMiCamera.setAWBLock(false);
                }
                if (!(DataRepository.dataItemGlobal().getCurrentMode() == 173 && CameraSettings.isFrontCamera() && MiCamera2ShotStill.this.mMiCamera.getCameraConfigs().getShotType() == 0)) {
                    MiCamera2ShotStill miCamera2ShotStill = MiCamera2ShotStill.this;
                    miCamera2ShotStill.mMiCamera.onCapturePictureFinished(true, miCamera2ShotStill);
                }
                MiCamera2ShotStill.this.mCaptureResult = totalCaptureResult;
                if (DataRepository.dataItemGlobal().getCurrentMode() == 187) {
                    PictureCallback pictureCallback = MiCamera2ShotStill.this.getPictureCallback();
                    if (pictureCallback != null) {
                        pictureCallback.onPictureTaken(null, MiCamera2ShotStill.this.mCaptureResult);
                    }
                }
                if (MiCamera2ShotStill.this.mNeedCaptureResult) {
                    PictureCallback pictureCallback2 = MiCamera2ShotStill.this.getPictureCallback();
                    if (pictureCallback2 == null || MiCamera2ShotStill.this.mCurrentParallelTaskData == null) {
                        String access$0002 = MiCamera2ShotStill.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("onCaptureCompleted: something wrong: callback = ");
                        sb2.append(pictureCallback2);
                        sb2.append(" mCurrentParallelTaskData = ");
                        sb2.append(MiCamera2ShotStill.this.mCurrentParallelTaskData);
                        Log.w(access$0002, sb2.toString());
                        return;
                    }
                    if (MiCamera2ShotStill.this.mCurrentParallelTaskData.isJpegDataReady() && MiCamera2ShotStill.this.mCaptureResult != null) {
                        z = true;
                    }
                    if (!z) {
                        return;
                    }
                    if (MiCamera2ShotStill.this.mIsIntent) {
                        MiCamera2ShotStill miCamera2ShotStill2 = MiCamera2ShotStill.this;
                        miCamera2ShotStill2.notifyResultData(miCamera2ShotStill2.mCurrentParallelTaskData);
                        pictureCallback2.onPictureTakenFinished(true);
                        return;
                    }
                    pictureCallback2.onPictureTakenFinished(true);
                    MiCamera2ShotStill miCamera2ShotStill3 = MiCamera2ShotStill.this;
                    miCamera2ShotStill3.notifyResultData(miCamera2ShotStill3.mCurrentParallelTaskData, MiCamera2ShotStill.this.mCaptureResult, MiCamera2ShotStill.this.mMiCamera.getCapabilities().getCameraCharacteristics());
                }
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                String access$000 = MiCamera2ShotStill.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: reason=");
                sb.append(captureFailure.getReason());
                sb.append(" frameNumber=");
                sb.append(captureFailure.getFrameNumber());
                Log.e(access$000, sb.toString());
                if (MiCamera2ShotStill.this.mMiCamera.getSuperNight()) {
                    MiCamera2ShotStill.this.mMiCamera.setAWBLock(false);
                }
                MiCamera2ShotStill miCamera2ShotStill = MiCamera2ShotStill.this;
                miCamera2ShotStill.mMiCamera.onCapturePictureFinished(false, miCamera2ShotStill);
            }

            public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
                super.onCaptureProgressed(cameraCaptureSession, captureRequest, captureResult);
                if (DataRepository.dataItemGlobal().getCurrentMode() == 187) {
                    PictureCallback pictureCallback = MiCamera2ShotStill.this.getPictureCallback();
                    if (pictureCallback != null) {
                        pictureCallback.onCaptureProgress(false, false, false, false, captureResult);
                    }
                }
            }

            public void onCaptureStarted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, long j, long j2) {
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
                if ((!CameraSettings.isSupportedZslShutter() || CameraSettings.isUltraPixelOn()) && !CameraSettings.getPlayToneOnCaptureStart()) {
                    PictureCallback pictureCallback = MiCamera2ShotStill.this.getPictureCallback();
                    if (pictureCallback != null) {
                        pictureCallback.onCaptureShutter(false, false, false, false);
                    } else {
                        Log.w(MiCamera2ShotStill.TAG, "onCaptureStarted: null picture callback");
                    }
                }
                if (0 == MiCamera2ShotStill.this.mCurrentParallelTaskData.getTimestamp()) {
                    MiCamera2ShotStill.this.mCurrentParallelTaskData.setTimestamp(j);
                }
                String access$000 = MiCamera2ShotStill.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStarted: mCurrentParallelTaskData: ");
                sb.append(MiCamera2ShotStill.this.mCurrentParallelTaskData.getTimestamp());
                Log.d(access$000, sb.toString());
            }
        };
    }

    /* access modifiers changed from: protected */
    public Builder generateRequestBuilder() {
        Builder createCaptureRequest = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
        ImageReader photoImageReader = this.mMiCamera.getPhotoImageReader();
        createCaptureRequest.addTarget(photoImageReader.getSurface());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("size=");
        sb.append(photoImageReader.getWidth());
        sb.append("x");
        sb.append(photoImageReader.getHeight());
        Log.d(str, sb.toString());
        if (C0122O00000o.instance().OO0O0o0() && (!isInQcfaMode() || Camera2DataContainer.getInstance().getBokehFrontCameraId() == this.mMiCamera.getId())) {
            createCaptureRequest.addTarget(this.mMiCamera.getPreviewSurface());
        }
        int rawCallbackType = this.mMiCamera.getRawCallbackType();
        if (!((rawCallbackType & 1) == 0 && (rawCallbackType & 2) == 0 && (rawCallbackType & 4) == 0)) {
            createCaptureRequest.addTarget(this.mMiCamera.getRawSurface());
        }
        if (this.mHasDepth) {
            createCaptureRequest.addTarget(this.mMiCamera.getDepthImageReader().getSurface());
            createCaptureRequest.addTarget(this.mMiCamera.getPortraitRawImageReader().getSurface());
        }
        createCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE, (Integer) this.mMiCamera.getPreviewRequestBuilder().get(CaptureRequest.CONTROL_AF_MODE));
        this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
        if (CameraSettings.isUltraPixelRawOn()) {
            if (C0124O00000oO.isMTKPlatform()) {
                Log.d(TAG, "enable remosaic capture hint");
                MiCameraCompat.applyRemosaicHint(createCaptureRequest, true);
            }
            Log.d(TAG, "apply remosaic capture request: true");
            MiCameraCompat.applyRemosaicEnabled(createCaptureRequest, true);
        }
        if (this.mMiCamera.useLegacyFlashStrategy() && this.mMiCamera.isNeedFlashOn()) {
            this.mMiCamera.pausePreview();
        }
        return createCaptureRequest;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public long getTimeStamp() {
        ParallelTaskData parallelTaskData = this.mCurrentParallelTaskData;
        if (parallelTaskData == null) {
            return 0;
        }
        return parallelTaskData.getTimestamp();
    }

    /* access modifiers changed from: protected */
    public void notifyResultData(ParallelTaskData parallelTaskData) {
        notifyResultData(parallelTaskData, null, null);
    }

    /* access modifiers changed from: protected */
    public void onImageReceived(Image image, int i) {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback == null || this.mCurrentParallelTaskData == null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onImageReceived: something wrong happened when image received: ");
            sb.append(image.getTimestamp());
            sb.append(" callback = ");
            sb.append(pictureCallback);
            sb.append(" mCurrentParallelTaskData = ");
            sb.append(this.mCurrentParallelTaskData);
            Log.w(str, sb.toString());
            image.close();
            return;
        }
        if (DataRepository.dataItemGlobal().getCurrentMode() == 173 && CameraSettings.isFrontCamera() && this.mMiCamera.getCameraConfigs().getShotType() == 0) {
            this.mMiCamera.onCapturePictureFinished(true, this);
        }
        if (pictureCallback.onPictureTakenImageConsumed(image, this.mCaptureResult)) {
            image.close();
            pictureCallback.onPictureTakenFinished(true);
            return;
        }
        if (0 == this.mCurrentParallelTaskData.getTimestamp()) {
            Log.w(TAG, "onImageReceived: image arrived first");
            this.mCurrentParallelTaskData.setTimestamp(image.getTimestamp());
        }
        if (this.mCurrentParallelTaskData.getTimestamp() == image.getTimestamp() || !this.mCurrentParallelTaskData.isDataFilled(i)) {
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onImageReceived mCurrentParallelTaskData timestamp:");
            sb2.append(this.mCurrentParallelTaskData.getTimestamp());
            sb2.append(" image timestamp:");
            sb2.append(image.getTimestamp());
            Log.d(str2, sb2.toString());
            Plane[] planesExtra = JpegUtil.getPlanesExtra(image);
            boolean z = false;
            byte[] jpegData = JpegUtil.getJpegData(planesExtra, 0);
            byte[] jpegData2 = JpegUtil.getJpegData(planesExtra, 1);
            if (jpegData == null) {
                jpegData = Util.getFirstPlane(image);
            }
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onImageReceived: dataLen=");
            sb3.append(jpegData == null ? "null" : Integer.valueOf(jpegData.length));
            sb3.append(" resultType = ");
            sb3.append(i);
            sb3.append(" timeStamp=");
            sb3.append(image.getTimestamp());
            sb3.append(" holder=");
            sb3.append(hashCode());
            Log.d(str3, sb3.toString());
            image.close();
            this.mCurrentParallelTaskData.fillJpegData(jpegData, i);
            if (jpegData2 != null) {
                this.mCurrentParallelTaskData.setDataOfTheRegionUnderWatermarks(jpegData2);
                ParallelTaskDataParameter dataParameter = this.mCurrentParallelTaskData.getDataParameter();
                int[] vendorWatermarkRange = Util.getVendorWatermarkRange(dataParameter.getPictureSize().getWidth(), dataParameter.getPictureSize().getHeight(), dataParameter.getJpegRotation());
                String str4 = TAG;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onImageReceived: rotation = ");
                sb4.append(dataParameter.getJpegRotation());
                sb4.append(", watermarkRange = ");
                sb4.append(Arrays.toString(vendorWatermarkRange));
                Log.d(str4, sb4.toString());
                this.mCurrentParallelTaskData.setCoordinatesOfTheRegionUnderWatermarks(vendorWatermarkRange);
            }
            if (!this.mNeedCaptureResult) {
                z = this.mCurrentParallelTaskData.isJpegDataReady();
            } else if (this.mCurrentParallelTaskData.isJpegDataReady() && this.mCaptureResult != null) {
                z = true;
            }
            if (z) {
                if (this.mIsIntent) {
                    notifyResultData(this.mCurrentParallelTaskData);
                    pictureCallback.onPictureTakenFinished(true);
                } else {
                    pictureCallback.onPictureTakenFinished(true);
                    notifyResultData(this.mCurrentParallelTaskData, this.mCaptureResult, this.mMiCamera.getCapabilities().getCameraCharacteristics());
                }
            }
            return;
        }
        String str5 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("image has been filled ");
        sb5.append(i);
        Log.e(str5, sb5.toString());
        image.close();
    }

    /* access modifiers changed from: protected */
    public void prepare() {
        if (this.mMiCamera.getSuperNight()) {
            this.mMiCamera.setAWBLock(true);
        }
        this.mShotBoostParams = this.mMiCamera.getShotBoostParams();
        int shotType = this.mMiCamera.getCameraConfigs().getShotType();
        if (shotType == -3) {
            this.mHasDepth = true;
        } else if (shotType != -2) {
            if (shotType == 1) {
                this.mNeedCaptureResult = true;
                return;
            } else if (shotType == 2) {
                this.mHasDepth = true;
                return;
            } else {
                return;
            }
        }
        this.mIsIntent = true;
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        String str;
        String str2;
        try {
            this.mCurrentParallelTaskData = generateParallelTaskData(0);
            if (this.mCurrentParallelTaskData == null) {
                Log.w(TAG, "startSessionCapture: null task data");
                return;
            }
            this.mCurrentParallelTaskData.setShot2Gallery(this.mMiCamera.getCameraConfigs().isShot2Gallery());
            this.mCurrentParallelTaskData.setInTimerBurstShotting(this.mMiCamera.isInTimerBurstShotting());
            String savePath = this.mCurrentParallelTaskData.getSavePath();
            if (savePath != null) {
                this.mCurrentParallelTaskData.setSaveToHiddenFolder(Storage.isSaveToHidenFolder(Util.getFileTitleFromPath(savePath)));
            }
            CaptureCallback generateCaptureCallback = generateCaptureCallback();
            Builder generateRequestBuilder = generateRequestBuilder();
            PerformanceTracker.trackPictureCapture(0);
            StringBuilder sb = new StringBuilder();
            sb.append("shotstill for camera ");
            sb.append(this.mMiCamera.getId());
            Log.dumpRequest(sb.toString(), generateRequestBuilder.build());
            this.mMiCamera.getCaptureSession().capture(generateRequestBuilder.build(), generateCaptureCallback, this.mCameraHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(TAG, "Cannot capture a still picture");
            this.mMiCamera.notifyOnError(e.getReason());
        } catch (IllegalStateException e2) {
            e = e2;
            str2 = TAG;
            str = "Failed to capture a still picture, IllegalState";
            Log.e(str2, str, e);
            this.mMiCamera.notifyOnError(256);
        } catch (IllegalArgumentException e3) {
            e = e3;
            str2 = TAG;
            str = "Failed to capture a still picture, IllegalArgument";
            Log.e(str2, str, e);
            this.mMiCamera.notifyOnError(256);
        }
    }
}
