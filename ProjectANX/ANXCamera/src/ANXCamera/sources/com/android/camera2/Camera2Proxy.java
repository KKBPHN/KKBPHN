package com.android.camera2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.location.Location;
import android.media.Image;
import android.media.ImageReader;
import android.media.ImageWriter;
import android.os.Handler;
import android.util.Range;
import android.view.Surface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraScreenNail;
import com.android.camera.CameraSize;
import com.android.camera.IPreviewMetadata;
import com.android.camera.Thumbnail;
import com.android.camera.effect.FaceAnalyzeInfo;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.module.loader.camera2.FocusTask;
import com.android.camera.storage.ImageSaver;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableSuperNightExif.SuperNightExif;
import com.android.zxing.CacheImageDecoder;
import com.xiaomi.camera.core.ParallelCallback;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.liveshot.LivePhotoResult;
import java.lang.ref.WeakReference;
import java.util.function.Consumer;

public abstract class Camera2Proxy implements IPreviewMetadata {
    public static final int INVALID_REQUEST_ID = -1;
    public static final int PREVIEW_CALLBACK_CACHE_IMAGE = 16;
    public static final int PREVIEW_CALLBACK_DISABLE = 0;
    public static final int PREVIEW_CALLBACK_DOCUMENT = 32;
    public static final int PREVIEW_CALLBACK_ENABLE_UNKNOWN = 1;
    public static final int PREVIEW_CALLBACK_GOOGLE_LENS = 8;
    public static final int PREVIEW_CALLBACK_HAND_GESTURE = 4;
    public static final int PREVIEW_CALLBACK_SCAN_QR = 2;
    public static final int RAW_CALLBACK_RAW_ALGO_HIDL = 8;
    public static final int RAW_CALLBACK_RAW_HIDL = 4;
    public static final int RAW_CALLBACK_RAW_NONE = 0;
    public static final int RAW_CALLBACK_RAW_NORMAL = 1;
    public static final int RAW_CALLBACK_RAW_REPROCESS = 2;
    final int mActualCameraId;
    private PreviewCallback mAnchorCallback;
    protected CacheImageDecoder mCacheImageDecoder;
    private final Object mCallbackLock = new Object();
    protected CameraErrorCallback mErrorCallback;
    private WeakReference mFocusCallback;
    IFirstCaptureFocus mFocusFrameAvailable;
    private volatile boolean mIsFixedShotTimeEnabled = false;
    private WeakReference mMagneticDetectedCallback;
    private WeakReference mMetadataCallback;
    private ParallelCallback mParallelCallback;
    private PictureCallback mPictureCallBack;
    private PreviewCallback mPreviewCallback;
    private ScreenLightCallback mScreenLightCallback;

    public interface ASDSceneCallback {
        boolean isAutoFlashOff();
    }

    public interface AnchorPreviewCallback {
        void onFrameThumbnailFail();

        void onFrameThumbnailSuccess(long j);

        void saveBitmapAsThumbnail(Bitmap bitmap, int i, int i2, boolean z, boolean z2);
    }

    public interface BeautyBodySlimCountCallback {
        boolean isBeautyBodySlimCountDetectStarted();

        void onBeautyBodySlimCountChange(boolean z);
    }

    public interface CameraErrorCallback {
        void onCameraError(Camera2Proxy camera2Proxy, int i);
    }

    public interface CameraMetaDataCallback {
        void onPreviewMetaDataUpdate(CaptureResult captureResult);
    }

    public interface CameraPreviewCallback {
        void onPreviewRelease();

        void onPreviewSessionClosed(CameraCaptureSession cameraCaptureSession);

        void onPreviewSessionFailed(CameraCaptureSession cameraCaptureSession);

        void onPreviewSessionSuccess(CameraCaptureSession cameraCaptureSession);
    }

    public interface CaptureBusyCallback {
        void onCaptureCompleted(boolean z);
    }

    public interface FaceDetectionCallback {
        boolean isFaceDetectStarted();

        boolean isUseFaceInfo();

        void onFaceDetected(CameraHardwareFace[] cameraHardwareFaceArr, FaceAnalyzeInfo faceAnalyzeInfo, Rect rect);
    }

    public interface FocusCallback {
        void onFocusStateChanged(FocusTask focusTask);
    }

    public class HDRStatus {
        private boolean enable;
        private boolean isFlashHDR;
        private boolean isSuperResolutionHDR;

        private HDRStatus() {
        }

        public HDRStatus(boolean z, boolean z2, boolean z3) {
            this.enable = z;
            this.isFlashHDR = z2;
            this.isSuperResolutionHDR = z3;
        }

        public boolean isEnable() {
            return this.enable;
        }

        public boolean isFlashHDR() {
            return this.isFlashHDR;
        }

        public boolean isSuperResolutionHDR() {
            return this.isSuperResolutionHDR;
        }

        @NonNull
        public String toString() {
            return String.format("hdrEnable: %b, isNightHDR: %b, isSuperResolutionHDR: %b", new Object[]{Boolean.valueOf(this.enable), Boolean.valueOf(this.isFlashHDR), Boolean.valueOf(this.isSuperResolutionHDR)});
        }
    }

    public interface HdrCheckerCallback {
        boolean isHdrSceneDetectionStarted();

        boolean isMatchCurrentHdrMode(int i);

        void onHdrMotionDetectionResult(boolean z);

        void onHdrSceneChanged(boolean z);
    }

    public interface IFirstCaptureFocus {
        boolean isFirstCreateCapture();

        boolean isFocusFrameAvailable();
    }

    public interface LivePhotoResultCallback {
        int getFilterId();

        boolean isGyroStable();

        boolean isLivePhotoStarted();

        void onLivePhotoResultCallback(LivePhotoResult livePhotoResult);
    }

    public interface MagneticDetectedCallback {
        boolean isLockHDRChecker(String str);

        void resetMagneticInfo();
    }

    public interface NearRangeModeCallback {
        void setNearRangeMode(boolean z);

        void setNearRangeModeUIStatus(boolean z);
    }

    public interface PictureCallback {
        void onCaptureCompleted(boolean z) {
        }

        void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult);

        void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4);

        ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4);

        void onPictureTaken(byte[] bArr, CaptureResult captureResult);

        void onPictureTakenFinished(boolean z);

        boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult);
    }

    public class PictureCallbackWrapper implements PictureCallback {
        public void onCaptureProgress(boolean z, boolean z2, boolean z3, boolean z4, CaptureResult captureResult) {
        }

        public void onCaptureShutter(boolean z, boolean z2, boolean z3, boolean z4) {
        }

        public ParallelTaskData onCaptureStart(ParallelTaskData parallelTaskData, CameraSize cameraSize, boolean z, boolean z2, boolean z3, boolean z4) {
            return null;
        }

        public void onPictureTaken(byte[] bArr, CaptureResult captureResult) {
        }

        public void onPictureTakenFinished(boolean z) {
        }

        public boolean onPictureTakenImageConsumed(Image image, TotalCaptureResult totalCaptureResult) {
            return false;
        }
    }

    public interface PreviewCallback {
        boolean onPreviewFrame(Image image, Camera2Proxy camera2Proxy, int i);
    }

    public interface ScreenLightCallback {
        void startScreenLight(int i, int i2);

        void stopScreenLight();
    }

    public interface SuperNightCallback {
        boolean isSupportSuperNight();

        void onSuperNightChanged(boolean z);

        void onSuperNightExif(SuperNightExif superNightExif);
    }

    public interface VideoRecordStateCallback {
        void onVideoRecordStopped();
    }

    public Camera2Proxy(int i) {
        this.mActualCameraId = i;
    }

    public abstract void applyAiShutterEnable(boolean z);

    public abstract void applyHighQualityPreferred(boolean z);

    public abstract void applyVideoHdrMode(boolean z);

    public abstract void cancelContinuousShot();

    public abstract void cancelFocus(int i);

    public abstract void cancelSession();

    public abstract void captureAbortBurst();

    public abstract void captureBurstPictures(int i, @NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback);

    public abstract void captureGroupShotPictures(@NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback, int i, Context context);

    public abstract void captureVideoSnapshot(PictureCallback pictureCallback);

    public abstract boolean close();

    public abstract void forceTurnFlashONAndPausePreview();

    public abstract void forceTurnFlashOffAndPausePreview();

    public abstract int getActivityHashCode();

    public abstract int getAlgorithmPreviewFormat();

    public abstract CameraSize getAlgorithmPreviewSize();

    /* access modifiers changed from: 0000 */
    public PreviewCallback getAnchorCallback() {
        PreviewCallback previewCallback;
        synchronized (this.mCallbackLock) {
            previewCallback = this.mAnchorCallback;
        }
        return previewCallback;
    }

    public abstract CameraSize getBinningPictureSize();

    public abstract int getBokehAuxCameraId();

    public abstract CameraSize getBokehDepthSize();

    public abstract CacheImageDecoder getCacheImageDecoder();

    public abstract CameraConfigs getCameraConfigs();

    public abstract CameraDevice getCameraDevice();

    public abstract Handler getCameraHandler();

    public abstract CameraCapabilities getCapabilities();

    public abstract CameraCaptureSession getCaptureSession();

    public abstract Integer getCurrentAEState();

    public abstract ImageReader getDepthImageReader();

    public abstract int getExposureCompensation();

    public abstract int getExtendSceneMode();

    public abstract int getFlashMode();

    /* access modifiers changed from: 0000 */
    public FocusCallback getFocusCallback() {
        FocusCallback focusCallback;
        synchronized (this.mCallbackLock) {
            focusCallback = this.mFocusCallback != null ? (FocusCallback) this.mFocusCallback.get() : null;
        }
        return focusCallback;
    }

    public abstract int getFocusMode();

    public abstract int getHDRMode();

    public int getId() {
        return this.mActualCameraId;
    }

    public MagneticDetectedCallback getMagneticDetectedCallback() {
        WeakReference weakReference = this.mMagneticDetectedCallback;
        if (weakReference != null) {
            return (MagneticDetectedCallback) weakReference.get();
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public CameraMetaDataCallback getMetadataCallback() {
        synchronized (this.mCallbackLock) {
            if (this.mMetadataCallback == null) {
                return null;
            }
            CameraMetaDataCallback cameraMetaDataCallback = (CameraMetaDataCallback) this.mMetadataCallback.get();
            return cameraMetaDataCallback;
        }
    }

    /* access modifiers changed from: 0000 */
    public ParallelCallback getParallelCallback() {
        ParallelCallback parallelCallback;
        synchronized (this.mCallbackLock) {
            parallelCallback = this.mParallelCallback;
        }
        return parallelCallback;
    }

    public abstract String getParallelShotSavePath();

    public abstract ImageReader getPhotoImageReader();

    /* access modifiers changed from: 0000 */
    public PictureCallback getPictureCallback() {
        PictureCallback pictureCallback;
        synchronized (this.mCallbackLock) {
            pictureCallback = this.mPictureCallBack;
        }
        return pictureCallback;
    }

    public abstract int getPictureFormat();

    public abstract int getPictureMaxImages();

    public abstract CameraSize getPictureSize();

    public abstract ImageReader getPortraitRawImageReader();

    /* access modifiers changed from: 0000 */
    public PreviewCallback getPreviewCallback() {
        PreviewCallback previewCallback;
        synchronized (this.mCallbackLock) {
            previewCallback = this.mPreviewCallback;
        }
        return previewCallback;
    }

    public abstract CaptureResult getPreviewCaptureResult();

    public abstract int getPreviewMaxImages();

    public abstract Builder getPreviewRequestBuilder();

    public abstract CameraSize getPreviewSize();

    public abstract Surface getPreviewSurface();

    public abstract int getRawCallbackType();

    public abstract ImageReader getRawImageReader();

    public abstract ImageWriter getRawImageWriter();

    public abstract Surface getRawSurface();

    public abstract Surface getRecordSurface();

    public abstract int[] getSATSubCameraIds();

    public abstract CameraCapabilities getSatCapabilities();

    public abstract int getSatMasterCameraId();

    public abstract int getSceneMode();

    /* access modifiers changed from: 0000 */
    public ScreenLightCallback getScreenLightCallback() {
        ScreenLightCallback screenLightCallback;
        synchronized (this.mCallbackLock) {
            screenLightCallback = this.mScreenLightCallback;
        }
        return screenLightCallback;
    }

    public abstract CameraSize getSensorRawImageSize();

    public abstract CaptureSessionConfigurations getSessionConfigs();

    public abstract Consumer getShotBoostParams();

    public abstract boolean getSuperNight();

    public abstract ImageReader getVideoSnapshotImageReader();

    public abstract float getZoomRatio();

    public abstract boolean isCaptureBusy(boolean z);

    public abstract boolean isDisconnected();

    public abstract boolean isFacingFront();

    public abstract boolean isFakeSatEnable();

    public boolean isFixShotTime() {
        return this.mIsFixedShotTimeEnabled;
    }

    public abstract boolean isInMultiSurfaceSatMode();

    public abstract boolean isInTimerBurstShotting();

    public abstract boolean isMacroMode();

    public abstract boolean isModuleAnchorFrame();

    public abstract boolean isNeedFlashForAuto(Integer num, int i);

    public abstract boolean isNeedFlashOn();

    public abstract boolean isNeedPreviewThumbnail();

    public abstract boolean isParallelBusy(boolean z);

    public abstract boolean isPreviewReady();

    public abstract boolean isQcfaEnable();

    public abstract boolean isSessionReady();

    public abstract boolean isShotQueueMultitasking();

    public abstract boolean isSingleBokehEnabled();

    public abstract void lockExposure(boolean z);

    public abstract void lockExposure(boolean z, boolean z2);

    /* access modifiers changed from: protected */
    public void notifyOnError(int i) {
        CameraErrorCallback cameraErrorCallback = this.mErrorCallback;
        if (cameraErrorCallback != null) {
            cameraErrorCallback.onCameraError(this, i);
        }
    }

    public abstract void notifyVideoStreamEnd();

    public abstract void onCameraDisconnected();

    public abstract void onCapabilityChanged(CameraCapabilities cameraCapabilities);

    public abstract void onMultiSnapEnd(boolean z, MiCamera2Shot miCamera2Shot);

    public abstract void onParallelImagePostProcStart();

    public abstract void onPreviewComing();

    public abstract void onPreviewThumbnailReceived(Thumbnail thumbnail);

    public abstract void pausePreview();

    public abstract void releaseCameraPreviewCallback(@Nullable CameraPreviewCallback cameraPreviewCallback);

    public abstract void releaseFakeSurfaceIfNeed();

    public abstract void releasePreview(int i);

    public abstract void resetConfigs();

    public abstract void resetFlashStateTimeLock();

    public abstract int resumePreview();

    public abstract void sendSatFallbackDisableRequest(boolean z, boolean z2);

    public abstract int sendSatFallbackRequest(int i);

    public abstract void setAELock(boolean z);

    public abstract void setAERegions(MeteringRectangle[] meteringRectangleArr);

    public abstract void setAFRegions(MeteringRectangle[] meteringRectangleArr);

    public abstract void setAIIEPreviewEnable(boolean z);

    public abstract void setASDEnable(boolean z);

    public abstract void setASDPeriod(int i);

    public abstract void setASDScene(int i);

    public abstract void setAWBLock(boolean z);

    public abstract void setAWBMode(int i);

    public abstract void setActivityHashCode(int i);

    public abstract void setAiASDEnable(boolean z);

    public abstract void setAiMoonEffectEnable(boolean z);

    public abstract void setAiShutterExistMotion(boolean z);

    public abstract void setAlgorithmPreviewFormat(int i);

    public abstract void setAlgorithmPreviewSize(CameraSize cameraSize);

    public abstract void setAmbilightAeTarget(int i);

    public abstract void setAmbilightMode(int i);

    /* access modifiers changed from: 0000 */
    public void setAnchorCallback(PreviewCallback previewCallback) {
        synchronized (this.mCallbackLock) {
            this.mAnchorCallback = previewCallback;
        }
    }

    public abstract void setAntiBanding(int i);

    public abstract void setAsdDirtyEnable(boolean z);

    public abstract void setAutoZoomMode(int i);

    public abstract void setAutoZoomScaleOffset(float f);

    public abstract void setAutoZoomStartCapture(float[] fArr, boolean z);

    public abstract void setAutoZoomStopCapture(int i, boolean z);

    public abstract void setBeautyValues(BeautyValues beautyValues);

    public abstract void setBinningPictureSize(CameraSize cameraSize);

    public abstract void setBokehDepthSize(CameraSize cameraSize);

    public abstract void setBurstShotSpeed(int i);

    public abstract void setCacheImageDecoder(CacheImageDecoder cacheImageDecoder);

    public abstract void setCameraAI30(boolean z);

    public abstract void setCaptureBusyCallback(CaptureBusyCallback captureBusyCallback);

    public abstract void setCaptureTime(long j);

    public abstract void setCaptureTriggerFlow(int[] iArr);

    public abstract void setCinematicPhotoEnabled(boolean z);

    public abstract void setCinematicVideoEnabled(boolean z);

    public abstract void setColorEffect(int i);

    public abstract void setColorEnhanceEnable(boolean z);

    public abstract void setContrast(int i);

    public abstract void setCustomAWB(int i);

    public abstract void setDeviceOrientation(int i);

    public abstract void setDisplayOrientation(int i);

    public abstract void setDodepurpleEnabled(boolean z);

    public abstract void setDualBokehEnable(boolean z);

    public abstract void setDualCamWaterMarkEnable(boolean z);

    public abstract void setEnableEIS(boolean z);

    public abstract void setEnableOIS(boolean z);

    public abstract void setEnableZsl(boolean z);

    public void setErrorCallback(@Nullable CameraErrorCallback cameraErrorCallback) {
        this.mErrorCallback = cameraErrorCallback;
    }

    public abstract void setExposureCompensation(int i);

    public abstract void setExposureMeteringMode(int i);

    public abstract void setExposureTime(long j);

    public abstract void setExtendSceneMode(int i);

    public abstract void setEyeLight(int i);

    public abstract void setFNumber(String str);

    public abstract void setFaceAgeAnalyze(boolean z);

    public abstract void setFaceScore(boolean z);

    public abstract void setFaceWaterMarkEnable(boolean z);

    public abstract void setFaceWaterMarkFormat(String str);

    public abstract void setFakeTeleOutputSize(CameraSize cameraSize);

    public abstract void setFakeTelePictureSize(CameraSize cameraSize);

    public void setFixShotTimeEnabled(boolean z) {
        this.mIsFixedShotTimeEnabled = z;
    }

    public abstract void setFlashMode(int i);

    public abstract void setFlawDetectEnable(boolean z);

    public void setFocusCallback(FocusCallback focusCallback) {
        synchronized (this.mCallbackLock) {
            this.mFocusCallback = new WeakReference(focusCallback);
        }
    }

    public abstract void setFocusDistance(float f);

    public void setFocusFrameAvailable(IFirstCaptureFocus iFirstCaptureFocus) {
        this.mFocusFrameAvailable = iFirstCaptureFocus;
    }

    public abstract void setFocusMode(int i);

    public abstract void setFpsRange(Range range);

    public abstract void setFrontMirror(boolean z);

    public abstract void setGlobalWatermark();

    public abstract void setGpsLocation(Location location);

    public abstract void setHDR(HDRStatus hDRStatus);

    public abstract void setHDRCheckerEnable(boolean z);

    public abstract void setHDRCheckerStatus(int i);

    public abstract void setHDRMode(int i);

    public abstract void setHFRDeflickerEnable(boolean z);

    public abstract void setHHT(boolean z);

    public abstract void setHdr10VideoMode(boolean z);

    public abstract void setHistogramStatsEnabled(boolean z);

    public abstract void setISO(int i);

    public abstract void setInTimerBurstShotting(boolean z);

    public abstract void setIsFaceExist(boolean z);

    public abstract void setJpegQuality(int i);

    public abstract void setJpegRotation(int i);

    public abstract void setJpegThumbnailSize(CameraSize cameraSize);

    public abstract void setLLS(boolean z);

    public abstract void setLensDirtyDetect(boolean z);

    public abstract void setMFLockAfAe(boolean z);

    public abstract void setMacroMode(boolean z);

    public void setMagneticDetectedCallback(MagneticDetectedCallback magneticDetectedCallback) {
        this.mMagneticDetectedCallback = new WeakReference(magneticDetectedCallback);
    }

    public abstract void setMarcroPictureSize(CameraSize cameraSize);

    public void setMetaDataCallback(CameraMetaDataCallback cameraMetaDataCallback) {
        synchronized (this.mCallbackLock) {
            this.mMetadataCallback = new WeakReference(cameraMetaDataCallback);
        }
    }

    public abstract void setMfnr(boolean z);

    public abstract void setModuleAnchorFrame(boolean z);

    public abstract void setModuleParameter(int i, int i2);

    public abstract void setMtkPipDevices(int[] iArr);

    public abstract void setMultiSnapStopRequest(boolean z);

    public abstract void setNeedPausePreview(boolean z);

    public abstract void setNeedSequence(boolean z);

    public abstract void setNewWatermark(boolean z);

    public abstract void setNormalWideLDC(boolean z);

    public abstract void setOnTripodModeStatus(ASDScene[] aSDSceneArr);

    public abstract void setOpticalZoomToTele(boolean z);

    /* access modifiers changed from: 0000 */
    public void setParallelCallback(ParallelCallback parallelCallback) {
        synchronized (this.mCallbackLock) {
            this.mParallelCallback = parallelCallback;
        }
    }

    public abstract void setParallelSettings(int i, int i2);

    /* access modifiers changed from: 0000 */
    public void setPictureCallback(PictureCallback pictureCallback) {
        synchronized (this.mCallbackLock) {
            this.mPictureCallBack = pictureCallback;
        }
    }

    public abstract void setPictureFormat(int i);

    public abstract void setPictureMaxImages(int i);

    public abstract void setPictureSize(CameraSize cameraSize);

    public abstract void setPortraitLighting(int i);

    /* access modifiers changed from: 0000 */
    public void setPreviewCallback(PreviewCallback previewCallback) {
        synchronized (this.mCallbackLock) {
            this.mPreviewCallback = previewCallback;
        }
    }

    public abstract void setPreviewMaxImages(int i);

    public abstract void setPreviewSize(CameraSize cameraSize);

    public abstract void setQcfaEnable(boolean z);

    public abstract void setQuickShotAnimation(boolean z);

    public abstract void setRawSizeOfMacro(CameraSize cameraSize);

    public abstract void setRawSizeOfTele(CameraSize cameraSize);

    public abstract void setRawSizeOfTuningBuffer(CameraSize cameraSize);

    public abstract void setRawSizeOfUltraTele(CameraSize cameraSize);

    public abstract void setRawSizeOfUltraWide(CameraSize cameraSize);

    public abstract void setRawSizeOfWide(CameraSize cameraSize);

    public abstract void setSATRemosicPictureSize(CameraSize cameraSize);

    public abstract void setSATUltraWideLDC(boolean z);

    public abstract void setSatIsZooming(boolean z);

    public abstract void setSaturation(int i);

    public abstract void setSceneMode(int i);

    public void setScreenLightCallback(ScreenLightCallback screenLightCallback) {
        synchronized (this.mCallbackLock) {
            this.mScreenLightCallback = screenLightCallback;
        }
    }

    public abstract void setSensorRawImageSize(CameraSize cameraSize, int i);

    public abstract void setSharpness(int i);

    public abstract void setShot2Gallery(boolean z);

    public abstract void setShotBoostParams(Consumer consumer);

    public abstract void setShotSavePath(String str, boolean z);

    public abstract void setShotType(int i);

    public abstract void setSingleBokeh(boolean z);

    public abstract void setSpecshotModeEnable(boolean z);

    public abstract void setSubPictureSize(CameraSize cameraSize);

    public abstract void setSuperNight(boolean z);

    public abstract void setSuperResolution(boolean z);

    public abstract void setSwMfnr(boolean z);

    public abstract void setTargetZoom(float f);

    public abstract void setTelePictureSize(CameraSize cameraSize);

    public abstract void setThermalLevel(int i);

    public abstract void setTimeWaterMarkEnable(boolean z);

    public abstract void setTimeWatermarkValue(String str);

    public abstract void setTuningBufferSize(CameraSize cameraSize);

    public abstract void setTuningMode(byte b);

    public abstract void setUltraPixelPortrait(boolean z);

    public abstract void setUltraTelePictureSize(CameraSize cameraSize);

    public abstract void setUltraWideLDC(boolean z);

    public abstract void setUltraWidePictureSize(CameraSize cameraSize);

    public abstract void setUseLegacyFlashMode(boolean z);

    public abstract void setVendorSetting(Key key, Object obj);

    public abstract void setVideoBokehLevelBack(int i);

    public abstract void setVideoBokehLevelFront(float f);

    public abstract void setVideoFilterColorRetentionBack(boolean z);

    public abstract void setVideoFilterColorRetentionFront(boolean z);

    public abstract void setVideoFilterColorRetentionMode(int i, boolean z);

    public abstract void setVideoFilterId(int i);

    public abstract void setVideoFpsRange(Range range);

    public abstract void setVideoLogEnabled(boolean z);

    public abstract void setVideoSnapshotSize(CameraSize cameraSize);

    public abstract void setWidePictureSize(CameraSize cameraSize);

    public abstract void setZoomRatio(float f);

    public abstract void setZoomRatioForCapture(float f);

    public abstract void startFaceDetection();

    public abstract void startFocus(FocusTask focusTask, int i);

    public abstract void startHighSpeedRecordPreview();

    public abstract void startHighSpeedRecordSession(@NonNull Surface surface, @NonNull Surface surface2, Range range, CameraPreviewCallback cameraPreviewCallback);

    public abstract void startHighSpeedRecording();

    public abstract void startObjectTrack(RectF rectF);

    public abstract void startPreviewCallback(PreviewCallback previewCallback, PreviewCallback previewCallback2);

    public abstract void startPreviewSession(Surface surface, int i, int i2, Surface surface2, int i3, boolean z, CameraPreviewCallback cameraPreviewCallback);

    public abstract void startRecordPreview();

    public abstract void startRecordSession(@NonNull Surface surface, @NonNull Surface surface2, boolean z, int i, CameraPreviewCallback cameraPreviewCallback);

    public abstract void startRecording(boolean z);

    public abstract void startVideoPreviewSession(Surface surface, int i, int i2, Surface surface2, int i3, boolean z, CameraPreviewCallback cameraPreviewCallback);

    public abstract void stopFaceDetection();

    public abstract void stopObjectTrack();

    public abstract void stopPreviewCallback(boolean z);

    public abstract void stopRecording();

    public abstract void takePicture(@NonNull PictureCallback pictureCallback, @NonNull ParallelCallback parallelCallback);

    public abstract void takeSimplePicture(@NonNull PictureCallback pictureCallback, @NonNull ImageSaver imageSaver, @NonNull CameraScreenNail cameraScreenNail);

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append(" - cid: ");
        sb.append(getId());
        return sb.toString();
    }

    public abstract void unlockExposure();

    public abstract boolean updateDeferPreviewSession(Surface surface);

    public abstract void updateFlashAutoDetectionEnabled(boolean z);

    public abstract void updateFlashStateTimeLock();

    public abstract boolean useLegacyFlashStrategy();

    public abstract boolean useSingleCaptureForHdrPlusMfnr(CameraCapabilities cameraCapabilities);
}
