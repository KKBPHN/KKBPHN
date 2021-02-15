package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Handler;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.MemoryHelper;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.base.PerformanceTracker;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.Locale;
import java.util.Map;

@TargetApi(21)
public class MiCamera2ShotParallelStill extends MiCamera2ShotParallel {
    private static final float SAT_REMOSIC_RATIO = 1.5f;
    private static final String TAG = "ShotParallelStill";
    /* access modifiers changed from: private */
    public int mAlgoType;
    /* access modifiers changed from: private */
    public boolean mAnchorFrame;
    /* access modifiers changed from: private */
    public long mCaptureTimestamp = -1;
    /* access modifiers changed from: private */
    public final boolean mIsSatFusionShotEnabled;
    /* access modifiers changed from: private */
    public int mMainPhysicalId = -1;
    /* access modifiers changed from: private */
    public volatile boolean mNeedDoAnchorFrame = true;
    private final int mOperationMode;
    /* access modifiers changed from: private */
    public CameraSize mPreviewSize;
    private boolean mShouldDoQcfaCapture;
    /* access modifiers changed from: private */
    public CaptureResult mStillCaptureResult;
    /* access modifiers changed from: private */
    public int mSubPhysicalId = -1;
    /* access modifiers changed from: private */
    public boolean mUseParallelVtCam;

    public MiCamera2ShotParallelStill(@NonNull MiCamera2 miCamera2, @NonNull CaptureResult captureResult, boolean z, boolean z2) {
        super(miCamera2);
        this.mPreviewCaptureResult = captureResult;
        this.mOperationMode = miCamera2.getCapabilities().getOperatingMode();
        this.mUseParallelVtCam = z;
        this.mIsSatFusionShotEnabled = isSatFusionShotEnabled();
        this.mShouldDoQcfaCapture = z2;
    }

    private void applyAlgoParameter(Builder builder) {
        MiCameraCompat.applySwMfnrEnable(builder, false);
        MiCameraCompat.applyHDR(builder, false);
        MiCameraCompat.applySuperResolution(builder, false);
        MiCameraCompat.applyMultiFrameInputNum(builder, 1);
        if (C0124O00000oO.isMTKPlatform()) {
            boolean isIn3OrMoreSatMode = isIn3OrMoreSatMode();
            String str = TAG;
            if (isIn3OrMoreSatMode || isInMultiSurfaceSatMode()) {
                Rect[] rectArr = (Rect[]) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.MI_STATISTICS_FACE_RECTANGLES);
                if (rectArr != null) {
                    Log.d(str, "set mtk face");
                    MiCameraCompat.applyFaceRectangles(builder, rectArr);
                } else {
                    Log.d(str, "get mtk face = null");
                }
                Rect rect = (Rect) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.POST_PROCESS_CROP_REGION);
                if (rect != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("sat set mtkCrop = ");
                    sb.append(rect);
                    Log.d(str, sb.toString());
                    MiCameraCompat.applyPostProcessCropRegion(builder, rect);
                } else {
                    Log.d(str, "sat get mtkCrop = null");
                }
                MiCameraCompat.applyNotificationTrigger(builder, true);
                Log.d(str, "sat applyNotificationTrigger true");
            } else if (this.mMiCamera.getCapabilities().getCameraId() == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                MiCameraCompat.copyFpcDataFromCaptureResultToRequest(this.mPreviewCaptureResult, builder);
                Rect rect2 = (Rect) builder.get(CaptureRequest.SCALER_CROP_REGION);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("uw set crop = ");
                sb2.append(this.mActiveArraySize);
                Log.d(str, sb2.toString());
                builder.set(CaptureRequest.SCALER_CROP_REGION, this.mActiveArraySize);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("uw set mtkCrop = ");
                sb3.append(rect2);
                Log.d(str, sb3.toString());
                MiCameraCompat.applyPostProcessCropRegion(builder, rect2);
            }
            MiCameraCompat.copyAiSceneFromCaptureResultToRequest(this.mPreviewCaptureResult, builder);
        } else if (isIn3OrMoreSatMode()) {
            CaptureRequestBuilder.applySmoothTransition(builder, this.mMiCamera.getCapabilities(), false);
        }
    }

    private boolean doAnchorFrameAsThumbnail() {
        boolean isAnchorFrameType;
        StringBuilder sb;
        String str;
        String str2;
        boolean isModuleAnchorFrame = this.mMiCamera.isModuleAnchorFrame();
        String str3 = TAG;
        if (!isModuleAnchorFrame) {
            str2 = "anchor frame not enabled";
        } else {
            CameraCapabilities capabilities = this.mMiCamera.getCapabilities();
            if (capabilities == null) {
                return false;
            }
            boolean z = !CameraSettings.isBackCamera();
            if (this.mAlgoType == 8) {
                str2 = "LLS disable anchor frame";
            } else if (this.mMiCamera.getSuperNight()) {
                return false;
            } else {
                if (this.mMiCamera.isQcfaEnable() && this.mShouldDoQcfaCapture) {
                    isAnchorFrameType = capabilities.isAnchorFrameType(z, 4);
                    sb = new StringBuilder();
                    str = "qcfa anchor frame ";
                } else if (!this.mMiCamera.isQcfaEnable() || this.mShouldDoQcfaCapture) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("default anchor frame ");
                    sb2.append(true);
                    Log.d(str3, sb2.toString());
                    return true;
                } else {
                    isAnchorFrameType = capabilities.isAnchorFrameType(z ? 1 : 0, 3);
                    sb = new StringBuilder();
                    str = "upscale anchor frame ";
                }
                sb.append(str);
                sb.append(isAnchorFrameType);
                Log.d(str3, sb.toString());
                return isAnchorFrameType;
            }
        }
        Log.d(str3, str2);
        return false;
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: frameNumber=");
                sb.append(totalCaptureResult.getFrameNumber());
                String sb2 = sb.toString();
                String str = MiCamera2ShotParallelStill.TAG;
                Log.k(3, str, sb2);
                MiCamera2ShotParallelStill miCamera2ShotParallelStill = MiCamera2ShotParallelStill.this;
                miCamera2ShotParallelStill.mMiCamera.onCapturePictureFinished(true, miCamera2ShotParallelStill);
                Boolean bool = (Boolean) VendorTagHelper.getValue((CaptureResult) totalCaptureResult, CaptureResultVendorTags.IS_HDR_ENABLE);
                if (bool != null && bool.booleanValue()) {
                    Log.e(str, "onCaptureCompleted: HDR error");
                }
                Boolean bool2 = (Boolean) VendorTagHelper.getValue((CaptureResult) totalCaptureResult, CaptureResultVendorTags.IS_SR_ENABLE);
                if (bool2 != null && bool2.booleanValue()) {
                    Log.e(str, "onCaptureCompleted: SR error");
                }
                MiCamera2ShotParallelStill.this.mStillCaptureResult = totalCaptureResult;
                ICustomCaptureResult customCaptureResult = CameraDeviceUtil.getCustomCaptureResult(totalCaptureResult);
                Map physicalCameraResults = totalCaptureResult.getPhysicalCameraResults();
                if (physicalCameraResults != null) {
                    if (MiCamera2ShotParallelStill.this.mMainPhysicalId != -1) {
                        CaptureResult captureResult = (CaptureResult) physicalCameraResults.get(String.valueOf(MiCamera2ShotParallelStill.this.mMainPhysicalId));
                        if (captureResult != null) {
                            customCaptureResult.setMainPhysicalResult(CameraDeviceUtil.getNativeMetadata(captureResult));
                        }
                    }
                    if (MiCamera2ShotParallelStill.this.mSubPhysicalId != -1) {
                        CaptureResult captureResult2 = (CaptureResult) physicalCameraResults.get(String.valueOf(MiCamera2ShotParallelStill.this.mSubPhysicalId));
                        if (captureResult2 != null) {
                            customCaptureResult.setSubPhysicalResult(CameraDeviceUtil.getNativeMetadata(captureResult2));
                        }
                    }
                }
                try {
                    customCaptureResult.getResults().set(CaptureRequestVendorTags.DXO_ASD_SCENE, MiCamera2ShotParallelStill.this.mMiCamera.getCameraConfigs().getDxoAsdScene());
                } catch (Exception unused) {
                    Log.w(str, "metadata set dxo_asd_scene fail!");
                }
                AlgoConnector.getInstance().getLocalBinder().onCaptureCompleted(customCaptureResult, true);
                ImagePool.getInstance().trimPoolBuffer();
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: reason=");
                sb.append(captureFailure.getReason());
                sb.append(" timestamp=");
                sb.append(MiCamera2ShotParallelStill.this.mCaptureTimestamp);
                sb.append(" frameNumber=");
                sb.append(captureFailure.getFrameNumber());
                Log.k(6, MiCamera2ShotParallelStill.TAG, sb.toString());
                MiCamera2ShotParallelStill miCamera2ShotParallelStill = MiCamera2ShotParallelStill.this;
                miCamera2ShotParallelStill.mMiCamera.onCapturePictureFinished(false, miCamera2ShotParallelStill);
                if (MiCamera2ShotParallelStill.this.mCaptureTimestamp != -1) {
                    AlgoConnector.getInstance().getLocalBinder().onCaptureFailed(MiCamera2ShotParallelStill.this.mCaptureTimestamp, captureFailure.getReason());
                }
            }

            public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
                super.onCaptureProgressed(cameraCaptureSession, captureRequest, captureResult);
                String str = MiCamera2ShotParallelStill.TAG;
                Log.d(str, "onCaptureProgressed");
                if (MiCamera2ShotParallelStill.this.mMiCamera.getPreviewCallbackEnabled() > 0 && (MiCamera2ShotParallelStill.this.mMiCamera.getPreviewCallbackEnabled() & 16) != 0) {
                    Long l = (Long) VendorTagHelper.getValueQuietly(captureResult, CaptureResultVendorTags.ANCHOR_FRAME_TIMESTAMP);
                    long longValue = l == null ? -1 : l.longValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append("begin to choose anchor frame ");
                    sb.append(longValue);
                    Log.d(str, sb.toString());
                    int i = (longValue > 0 ? 1 : (longValue == 0 ? 0 : -1));
                    if (i > 0 && MiCamera2ShotParallelStill.this.mAnchorFrame) {
                        PictureCallback pictureCallback = MiCamera2ShotParallelStill.this.getPictureCallback();
                        if (pictureCallback != null && MiCamera2ShotParallelStill.this.mNeedDoAnchorFrame) {
                            pictureCallback.onCaptureProgress(MiCamera2ShotParallelStill.this.isQuickShotAnimation(), MiCamera2ShotParallelStill.this.mAnchorFrame, true, false, captureResult);
                            MiCamera2ShotParallelStill.this.mMiCamera.getCacheImageDecoder().saveAnchorFrameThumbnail(longValue, MiCamera2ShotParallelStill.this.mPreviewSize.width, MiCamera2ShotParallelStill.this.mPreviewSize.height, null, String.valueOf(System.currentTimeMillis()));
                        } else {
                            return;
                        }
                    } else if (i == 0 && MiCamera2ShotParallelStill.this.mAnchorFrame) {
                        PictureCallback pictureCallback2 = MiCamera2ShotParallelStill.this.getPictureCallback();
                        if (pictureCallback2 != null && MiCamera2ShotParallelStill.this.mNeedDoAnchorFrame) {
                            pictureCallback2.onCaptureProgress(MiCamera2ShotParallelStill.this.isQuickShotAnimation(), MiCamera2ShotParallelStill.this.mAnchorFrame, false, true, captureResult);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                    MiCamera2ShotParallelStill.this.mNeedDoAnchorFrame = false;
                }
            }

            public void onCaptureStarted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, long j, long j2) {
                String str;
                long j3 = j;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStarted: timestamp=");
                sb.append(j3);
                sb.append(" frameNumber=");
                sb.append(j2);
                String sb2 = sb.toString();
                String str2 = MiCamera2ShotParallelStill.TAG;
                Log.k(3, str2, sb2);
                if (MiCamera2ShotParallelStill.this.mUseParallelVtCam) {
                    CaptureRequestBuilder.applySmoothTransition(MiCamera2ShotParallelStill.this.mMiCamera.getPreviewRequestBuilder(), MiCamera2ShotParallelStill.this.mMiCamera.getCapabilities(), true);
                }
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
                PictureCallback pictureCallback = MiCamera2ShotParallelStill.this.getPictureCallback();
                String shotPath = MiCamera2ShotParallelStill.this.mMiCamera.getCameraConfigs().getShotPath();
                if (pictureCallback != null) {
                    ParallelTaskData parallelTaskData = new ParallelTaskData(MiCamera2ShotParallelStill.this.mMiCamera.getId(), j, MiCamera2ShotParallelStill.this.mMiCamera.getCameraConfigs().getShotType(), shotPath, MiCamera2ShotParallelStill.this.mMiCamera.getCameraConfigs().getCaptureTime());
                    MiCamera2ShotParallelStill miCamera2ShotParallelStill = MiCamera2ShotParallelStill.this;
                    ParallelTaskData onCaptureStart = pictureCallback.onCaptureStart(parallelTaskData, miCamera2ShotParallelStill.mAlgoSize, miCamera2ShotParallelStill.isQuickShotAnimation(), MiCamera2ShotParallelStill.this.mAnchorFrame, false, false);
                    Boolean bool = (Boolean) VendorTagHelper.getValue(captureRequest, CaptureRequestVendorTags.MFNR_ENABLED);
                    if (onCaptureStart != null) {
                        onCaptureStart.setIsSatFusionShot(MiCamera2ShotParallelStill.this.mIsSatFusionShotEnabled);
                        onCaptureStart.setAlgoType(MiCamera2ShotParallelStill.this.mAlgoType);
                        onCaptureStart.setBurstNum(1);
                        if (bool == null || !bool.booleanValue()) {
                            onCaptureStart.setHWMFNRProcessing(false);
                        } else {
                            Log.d(str2, "onCaptureStarted, set HWMFNRProcessing is true");
                            onCaptureStart.setHWMFNRProcessing(true);
                        }
                        MiCamera2ShotParallelStill.this.mCaptureTimestamp = j3;
                        onCaptureStart.setParallelVTCameraSnapshot(MiCamera2ShotParallelStill.this.mUseParallelVtCam);
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
    /* JADX WARNING: Code restructure failed: missing block: B:71:0x01e7, code lost:
        if (r11.mUseParallelVtCam == false) goto L_0x005f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x02fa  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x01f5  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x023a  */
    /* JADX WARNING: Removed duplicated region for block: B:94:0x0291  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Builder generateRequestBuilder() {
        Integer isSpecshotDetected;
        Surface previewSurface;
        Builder createCaptureRequest = (this.mUseParallelVtCam ? ParallelSnapshotManager.getInstance().getCameraDevice() : this.mMiCamera.getCameraDevice()).createCaptureRequest(2);
        boolean isQcfaEnable = this.mMiCamera.isQcfaEnable();
        String str = TAG;
        if (isQcfaEnable) {
            previewSurface = (this.mMiCamera.alwaysUseRemosaicSize() || this.mShouldDoQcfaCapture) ? this.mMiCamera.getWideRemoteSurface() : this.mMiCamera.getQcfaRemoteSurface();
            Size surfaceSize = SurfaceUtils.getSurfaceSize(previewSurface);
            Size size = this.mLockedAlgoSize;
            if (size != null) {
                configParallelSession(size);
            } else {
                configParallelSession(surfaceSize);
            }
            Log.d(str, String.format(Locale.ENGLISH, "[QCFA]add surface %s to capture request, size is: %s", new Object[]{previewSurface, surfaceSize}));
        } else {
            if (isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) {
                Surface surface = this.mUseParallelVtCam ? ParallelSnapshotManager.getInstance().getCaptureSurface(this.mMiCamera.getSatMasterCameraId()) : (!C0122O00000o.instance().OOoO0oO() || this.mMiCamera.getZoomRatio() < 1.5f) ? getMainCaptureSurface() : this.mMiCamera.getSATRemosicRemoteSurface();
                this.mMainPhysicalId = this.mMiCamera.getSatPhysicalCameraId();
                Size surfaceSize2 = SurfaceUtils.getSurfaceSize(surface);
                Log.d(str, String.format(Locale.ENGLISH, "[SAT]add master surface %s to capture request, size is: %s", new Object[]{surface, surfaceSize2}));
                createCaptureRequest.addTarget(surface);
                int i = 513;
                if (surface == this.mMiCamera.getUltraWideRemoteSurface() || (this.mUseParallelVtCam && surface == ParallelSnapshotManager.getInstance().getCaptureSurface(1))) {
                    i = 3;
                }
                if (this.mIsSatFusionShotEnabled) {
                    Surface ultraTeleRemoteSurface = this.mMiCamera.getUltraTeleRemoteSurface();
                    surfaceSize2 = SurfaceUtils.getSurfaceSize(ultraTeleRemoteSurface);
                    Log.d(str, String.format(Locale.ENGLISH, "[SAT]add ultra tele surface %s to capture request, size is: %s", new Object[]{ultraTeleRemoteSurface, surfaceSize2}));
                    this.mSubPhysicalId = Camera2DataContainer.getInstance().getUltraTeleCameraId();
                    createCaptureRequest.addTarget(ultraTeleRemoteSurface);
                    VendorTagHelper.setValueQuietly(createCaptureRequest, CaptureRequestVendorTags.CONTROL_SAT_FUSION_SHOT, Byte.valueOf(1));
                    i = 516;
                } else {
                    VendorTagHelper.setValueQuietly(createCaptureRequest, CaptureRequestVendorTags.CONTROL_SAT_FUSION_SHOT, Byte.valueOf(0));
                }
                StringBuilder sb = new StringBuilder();
                sb.append("[SAT]combinationMode: ");
                sb.append(i);
                Log.d(str, sb.toString());
                configParallelSession(surfaceSize2, i);
            } else {
                for (Surface surface2 : this.mMiCamera.getRemoteSurfaceList()) {
                    if (!(surface2 == this.mMiCamera.getActiveRawSurface() || surface2 == this.mMiCamera.getRawSurfaceForTuningBuffer())) {
                        if (surface2 != this.mMiCamera.getTuningRemoteSurface()) {
                            Log.d(str, String.format(Locale.ENGLISH, "add surface %s to capture request, size is: %s", new Object[]{surface2, SurfaceUtils.getSurfaceSize(surface2)}));
                            createCaptureRequest.addTarget(surface2);
                        }
                    }
                }
                this.mAlgoSize = this.mMiCamera.getPictureSize();
            }
            if (this.mOperationMode == 36864) {
                this.mMainPhysicalId = this.mMiCamera.getPhysicalBokehMainId();
                this.mSubPhysicalId = this.mMiCamera.getPhysicalBokehSubId();
            }
            if (!C0124O00000oO.isMTKPlatform()) {
                int i2 = this.mOperationMode;
                if (!(i2 == 36865 || i2 == 36867 || ((this.mMiCamera.isFacingFront() && this.mOperationMode == 36869) || DataRepository.dataItemGlobal().isOnSuperNightHalfAlgoUp()))) {
                    previewSurface = this.mMiCamera.getPreviewSurface();
                    Log.d(str, String.format(Locale.ENGLISH, "add preview surface %s to capture request, size is: %s", new Object[]{previewSurface, SurfaceUtils.getSurfaceSize(previewSurface)}));
                }
            }
            if (C0122O00000o.instance().OOo0oOO()) {
                Surface tuningRemoteSurface = this.mMiCamera.getTuningRemoteSurface();
                if (tuningRemoteSurface != null) {
                    Log.d(str, "add tuning surface to capture request, size is: %s", SurfaceUtils.getSurfaceSize(tuningRemoteSurface));
                    createCaptureRequest.addTarget(tuningRemoteSurface);
                }
            }
            createCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE, (Integer) this.mMiCamera.getPreviewRequestBuilder().get(CaptureRequest.CONTROL_AF_MODE));
            this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
            if (this.mShouldDoQcfaCapture && CameraSettings.isFrontCamera()) {
                MiCameraCompat.applyMfnrEnable(createCaptureRequest, false);
            }
            if (this.mMiCamera.isQcfaEnable()) {
                if (C0124O00000oO.isMTKPlatform()) {
                    Log.d(str, "enable remosaic capture hint");
                    MiCameraCompat.applyRemosaicHint(createCaptureRequest, true);
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("apply remosaic capture request: ");
                sb2.append(this.mShouldDoQcfaCapture);
                Log.d(str, sb2.toString());
                MiCameraCompat.applyRemosaicEnabled(createCaptureRequest, this.mShouldDoQcfaCapture);
            }
            if (C0124O00000oO.isMTKPlatform() && this.mMiCamera.getCameraConfigs().isSpecshotModeEnable()) {
                isSpecshotDetected = CaptureResultParser.isSpecshotDetected(this.mPreviewCaptureResult);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("apply specshot mode capture request: ");
                sb3.append(isSpecshotDetected);
                Log.d(str, sb3.toString());
                if (isSpecshotDetected != null) {
                    MiCameraCompat.applySpecshotMode(createCaptureRequest, isSpecshotDetected.intValue());
                }
            }
            CaptureRequestBuilder.applyFlawDetectEnable(this.mMiCamera.getCapabilities(), createCaptureRequest, this.mMiCamera.getCameraConfigs().isFlawDetectEnable());
            boolean isAiShutterExistMotion = this.mMiCamera.getCameraConfigs().isAiShutterExistMotion();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("generateRequestBuilder.isAiShutterExistMotion: ");
            sb4.append(isAiShutterExistMotion);
            Log.d(str, sb4.toString());
            if (!isAiShutterExistMotion && this.mMiCamera.isFixShotTime() && C0122O00000o.instance().OO0OoO0() && AlgoConnector.getInstance().getLocalBinder().isAnyRequestIsHWMFNRProcessing()) {
                Log.d(str, "Do not apply hwmfnr.");
                MiCameraCompat.applyMfnrEnable(createCaptureRequest, false);
                MiCameraCompat.applyMultiFrameInputNum(createCaptureRequest, 1);
            }
            if (this.mUseParallelVtCam) {
                if (this.mMiCamera.getCameraConfigs().isLLSEnabled()) {
                    MiCameraCompat.applyLLS(createCaptureRequest, 1);
                } else {
                    MiCameraCompat.applyLLS(createCaptureRequest, 0);
                }
            }
            return createCaptureRequest;
        }
        createCaptureRequest.addTarget(previewSurface);
        if (C0122O00000o.instance().OOo0oOO()) {
        }
        createCaptureRequest.set(CaptureRequest.CONTROL_AF_MODE, (Integer) this.mMiCamera.getPreviewRequestBuilder().get(CaptureRequest.CONTROL_AF_MODE));
        this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
        MiCameraCompat.applyMfnrEnable(createCaptureRequest, false);
        if (this.mMiCamera.isQcfaEnable()) {
        }
        isSpecshotDetected = CaptureResultParser.isSpecshotDetected(this.mPreviewCaptureResult);
        StringBuilder sb32 = new StringBuilder();
        sb32.append("apply specshot mode capture request: ");
        sb32.append(isSpecshotDetected);
        Log.d(str, sb32.toString());
        if (isSpecshotDetected != null) {
        }
        CaptureRequestBuilder.applyFlawDetectEnable(this.mMiCamera.getCapabilities(), createCaptureRequest, this.mMiCamera.getCameraConfigs().isFlawDetectEnable());
        boolean isAiShutterExistMotion2 = this.mMiCamera.getCameraConfigs().isAiShutterExistMotion();
        StringBuilder sb42 = new StringBuilder();
        sb42.append("generateRequestBuilder.isAiShutterExistMotion: ");
        sb42.append(isAiShutterExistMotion2);
        Log.d(str, sb42.toString());
        Log.d(str, "Do not apply hwmfnr.");
        MiCameraCompat.applyMfnrEnable(createCaptureRequest, false);
        MiCameraCompat.applyMultiFrameInputNum(createCaptureRequest, 1);
        if (this.mUseParallelVtCam) {
        }
        return createCaptureRequest;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    public boolean isShutterReturned() {
        return this.mCaptureTimestamp != -1;
    }

    public void onCaptureShutter() {
        PictureCallback pictureCallback = getPictureCallback();
        if (pictureCallback != null) {
            pictureCallback.onCaptureShutter(true, this.mAnchorFrame, false, false);
        }
    }

    /* access modifiers changed from: protected */
    public void prepare() {
        this.mAlgoType = 0;
        this.mPreviewSize = this.mMiCamera.getCameraConfigs().getAlgorithmPreviewSize();
        if (this.mShouldDoQcfaCapture && (this.mMiCamera.getCameraConfigs().isHDREnabled() || this.mMiCamera.isBeautyOn())) {
            this.mShouldDoQcfaCapture = false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("prepare: qcfa = ");
        sb.append(this.mShouldDoQcfaCapture);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mShouldDoQcfaCapture) {
            this.mAlgoType = 6;
        }
        if (this.mMiCamera.getCameraConfigs().isLLSEnabled()) {
            this.mAlgoType = 8;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("prepare: mUseParallelVtCam:");
        sb3.append(this.mUseParallelVtCam);
        Log.d(str, sb3.toString());
        this.mAnchorFrame = doAnchorFrameAsThumbnail();
        this.mNeedDoAnchorFrame = this.mAnchorFrame;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("use anchor frame ");
        sb4.append(this.mAnchorFrame);
        Log.d(str, sb4.toString());
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        String str;
        CameraCaptureSession captureSession;
        CaptureRequest build;
        Handler handler;
        String str2 = TAG;
        PerformanceTracker.trackPictureCapture(0);
        try {
            CaptureCallback generateCaptureCallback = generateCaptureCallback();
            Builder generateRequestBuilder = generateRequestBuilder();
            applyAlgoParameter(generateRequestBuilder);
            StringBuilder sb = new StringBuilder();
            sb.append("parallel shotstill for camera ");
            sb.append(this.mMiCamera.getId());
            Log.dumpRequest(sb.toString(), generateRequestBuilder.build());
            if (this.mUseParallelVtCam) {
                captureSession = ParallelSnapshotManager.getInstance().getCaptureSession();
                build = generateRequestBuilder.build();
                handler = this.mCameraHandler;
            } else {
                captureSession = this.mMiCamera.getCaptureSession();
                build = generateRequestBuilder.build();
                handler = this.mCameraHandler;
            }
            captureSession.capture(build, generateCaptureCallback, handler);
            MemoryHelper.addCapturedNumber(this.mMiCamera.hashCode(), 1);
        } catch (CameraAccessException e) {
            e.printStackTrace();
            Log.e(str2, "Cannot capture a still picture");
            this.mMiCamera.notifyOnError(e.getReason());
        } catch (IllegalStateException e2) {
            e = e2;
            str = "Failed to capture a still picture, IllegalState";
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
