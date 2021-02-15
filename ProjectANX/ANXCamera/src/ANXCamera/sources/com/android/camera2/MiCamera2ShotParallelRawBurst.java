package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Parcelable;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.CameraSize;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.MemoryHelper;
import com.android.camera.log.Log;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.imagecodec.FeatureSetting;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.OutputConfiguration;
import com.xiaomi.camera.imagecodec.QueryFeatureSettingParameter.Builder;
import com.xiaomi.camera.isp.IspInterfaceIO;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.Map;

public class MiCamera2ShotParallelRawBurst extends MiCamera2ShotParallel {
    private static final String TAG = "ShotParallelRawBurst";
    /* access modifiers changed from: private */
    public int mAlgoType = 11;
    /* access modifiers changed from: private */
    public int mCompletedNum;
    private FeatureSetting mFeatureSetting;
    private boolean mFeatureSettingInitialized;
    /* access modifiers changed from: private */
    public boolean mFirstNum;
    private boolean mIsAinr;
    /* access modifiers changed from: private */
    public int mMainPhysicalCameraId = -1;
    /* access modifiers changed from: private */
    public Size mRawInputSize;
    private Surface mRawSurface;
    /* access modifiers changed from: private */
    public int mSequenceNum;
    private Size mYuvInputSize;
    private Surface mYuvSurface;

    public MiCamera2ShotParallelRawBurst(MiCamera2 miCamera2, CaptureResult captureResult) {
        super(miCamera2);
        this.mPreviewCaptureResult = captureResult;
    }

    private void initFeatureSetting() {
        long j;
        int i;
        String str = TAG;
        Log.d(str, "initFeatureSetting: E");
        try {
            this.mRawSurface = this.mMiCamera.getActiveRawSurface();
        } catch (RuntimeException unused) {
        }
        if (this.mRawSurface == null) {
            this.mFeatureSettingInitialized = true;
            Log.w(str, "initFeatureSetting: raw surface hasn't been initialized");
            return;
        }
        boolean z = this.mMiCamera.isIn3OrMoreSatMode() || this.mMiCamera.isInMultiSurfaceSatMode();
        int satMasterCameraId = z ? this.mMiCamera.getSatMasterCameraId() : this.mMiCamera.getId();
        StringBuilder sb = new StringBuilder();
        sb.append("initFeatureSetting: activeCameraId = ");
        sb.append(satMasterCameraId);
        Log.d(str, sb.toString());
        this.mMainPhysicalCameraId = satMasterCameraId;
        this.mRawInputSize = this.mMiCamera.getActiveRawSize(satMasterCameraId).toSizeObject();
        this.mYuvSurface = this.mMiCamera.getMainCaptureSurface(satMasterCameraId);
        this.mYuvInputSize = SurfaceUtils.getSurfaceSize(this.mYuvSurface);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("initFeatureSetting: rawInputSize = ");
        sb2.append(this.mRawInputSize);
        sb2.append(", yuvInputSize = ");
        sb2.append(this.mYuvInputSize);
        Log.d(str, sb2.toString());
        CameraSize outputSize = this.mMiCamera.getCameraConfigs().getOutputSize();
        int width = outputSize == null ? this.mYuvInputSize.getWidth() : outputSize.getWidth();
        int height = outputSize == null ? this.mYuvInputSize.getHeight() : outputSize.getHeight();
        if (!(width == this.mYuvInputSize.getWidth() && height == this.mYuvInputSize.getHeight())) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("initFeatureSetting: outputSize = ");
            sb3.append(outputSize);
            Log.d(str, sb3.toString());
        }
        OutputConfiguration outputConfiguration = new OutputConfiguration(width, height, this.mMiCamera.getCameraConfigs().getPhotoFormat());
        CaptureResult captureResult = this.mPreviewCaptureResult;
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        if (!(localBinder == null || captureResult == null)) {
            Parcelable nativeMetadata = CameraDeviceUtil.getNativeMetadata(captureResult);
            if (CaptureResultParser.isAishutExistMotion(captureResult)) {
                Integer num = (Integer) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.MTK_AISHUT_ISO);
                i = num != null ? num.intValue() : 0;
                Integer num2 = (Integer) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.MTK_AISHUT_EXPOSURE_TIME);
                j = num2 != null ? num2.longValue() : 0;
                StringBuilder sb4 = new StringBuilder();
                sb4.append("initFeatureSetting: aiShutIso=");
                sb4.append(num);
                sb4.append(" aiShutExposureTime=");
                sb4.append(num2);
                Log.v(str, sb4.toString());
            } else {
                j = 0;
                i = 0;
            }
            if (i == 0 || j == 0) {
                Integer num3 = (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
                if (num3 != null) {
                    int intValue = num3.intValue();
                }
                Long l = (Long) captureResult.get(CaptureResult.SENSOR_EXPOSURE_TIME);
                if (l != null) {
                    j = l.longValue();
                }
            }
            Integer isSpecshotDetected = CaptureResultParser.isSpecshotDetected(captureResult);
            int i2 = 2;
            boolean z2 = isSpecshotDetected != null && (isSpecshotDetected.intValue() == 1 || isSpecshotDetected.intValue() == 2);
            this.mIsAinr = z2;
            IspInterfaceIO ispInterfaceIO = new IspInterfaceIO(this.mYuvInputSize, this.mRawInputSize, outputConfiguration);
            Builder iso = new Builder().setActiveCameraId(satMasterCameraId).setExposureTime(j).setISO(i);
            if (!this.mIsAinr) {
                i2 = 1;
            }
            this.mFeatureSetting = localBinder.queryFeatureSetting(ispInterfaceIO, nativeMetadata, iso.setFeatureType(i2).build(), true);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("initFeatureSetting: ");
            sb5.append(this.mFeatureSetting);
            Log.d(str, sb5.toString());
            this.mFeatureSettingInitialized = true;
        }
        Log.d(str, "initFeatureSetting: X");
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            long mCaptureTimestamp = -1;

            public void onCaptureBufferLost(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull Surface surface, long j) {
                super.onCaptureBufferLost(cameraCaptureSession, captureRequest, surface, j);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureBufferLost: frameNumber=");
                sb.append(j);
                Log.w(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
            }

            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                MiCamera2ShotParallelRawBurst.this.mCompletedNum = MiCamera2ShotParallelRawBurst.this.mCompletedNum + 1;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureCompleted: ");
                sb.append(MiCamera2ShotParallelRawBurst.this.mCompletedNum);
                sb.append("/");
                sb.append(MiCamera2ShotParallelRawBurst.this.mSequenceNum);
                Log.d(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
                ICustomCaptureResult customCaptureResult = CameraDeviceUtil.getCustomCaptureResult(totalCaptureResult);
                Map physicalCameraResults = totalCaptureResult.getPhysicalCameraResults();
                if (!(physicalCameraResults == null || MiCamera2ShotParallelRawBurst.this.mMainPhysicalCameraId == -1)) {
                    CaptureResult captureResult = (CaptureResult) physicalCameraResults.get(String.valueOf(MiCamera2ShotParallelRawBurst.this.mMainPhysicalCameraId));
                    if (captureResult != null) {
                        customCaptureResult.setMainPhysicalResult(CameraDeviceUtil.getNativeMetadata(captureResult));
                    }
                }
                AlgoConnector.getInstance().getLocalBinder().onCaptureCompleted(customCaptureResult, MiCamera2ShotParallelRawBurst.this.mCompletedNum == 1);
                if (MiCamera2ShotParallelRawBurst.this.mSequenceNum == MiCamera2ShotParallelRawBurst.this.mCompletedNum) {
                    MiCamera2ShotParallelRawBurst miCamera2ShotParallelRawBurst = MiCamera2ShotParallelRawBurst.this;
                    miCamera2ShotParallelRawBurst.mMiCamera.onCapturePictureFinished(true, miCamera2ShotParallelRawBurst);
                }
                ImagePool.getInstance().trimPoolBuffer();
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
                Log.e(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
                MiCamera2ShotParallelRawBurst miCamera2ShotParallelRawBurst = MiCamera2ShotParallelRawBurst.this;
                miCamera2ShotParallelRawBurst.mMiCamera.onCapturePictureFinished(false, miCamera2ShotParallelRawBurst);
                if (this.mCaptureTimestamp != -1) {
                    AlgoConnector.getInstance().getLocalBinder().onCaptureFailed(this.mCaptureTimestamp, captureFailure.getReason());
                }
            }

            public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
                super.onCaptureProgressed(cameraCaptureSession, captureRequest, captureResult);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureProgressed: frameNumber=");
                sb.append(captureResult.getFrameNumber());
                Log.d(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
            }

            public void onCaptureSequenceAborted(@NonNull CameraCaptureSession cameraCaptureSession, int i) {
                super.onCaptureSequenceAborted(cameraCaptureSession, i);
                StringBuilder sb = new StringBuilder();
                sb.append("sequenceId=");
                sb.append(i);
                Log.w(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
            }

            public void onCaptureSequenceCompleted(@NonNull CameraCaptureSession cameraCaptureSession, int i, long j) {
                super.onCaptureSequenceCompleted(cameraCaptureSession, i, j);
                StringBuilder sb = new StringBuilder();
                sb.append("sequenceId=");
                sb.append(i);
                sb.append(" frameNumber=");
                sb.append(j);
                Log.d(MiCamera2ShotParallelRawBurst.TAG, sb.toString());
            }

            public void onCaptureStarted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, long j, long j2) {
                String str;
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureStarted: timestamp=");
                sb.append(j);
                sb.append(" frameNumber=");
                sb.append(j2);
                sb.append(" isFirst=");
                sb.append(MiCamera2ShotParallelRawBurst.this.mFirstNum);
                String sb2 = sb.toString();
                String str2 = MiCamera2ShotParallelRawBurst.TAG;
                Log.d(str2, sb2);
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
                if (MiCamera2ShotParallelRawBurst.this.mFirstNum) {
                    PictureCallback pictureCallback = MiCamera2ShotParallelRawBurst.this.getPictureCallback();
                    if (pictureCallback != null) {
                        ParallelTaskData parallelTaskData = new ParallelTaskData(MiCamera2ShotParallelRawBurst.this.mMiCamera.getId(), j, MiCamera2ShotParallelRawBurst.this.mMiCamera.getCameraConfigs().getShotType(), MiCamera2ShotParallelRawBurst.this.mMiCamera.getCameraConfigs().getShotPath(), MiCamera2ShotParallelRawBurst.this.mMiCamera.getCameraConfigs().getCaptureTime());
                        ParallelTaskData onCaptureStart = pictureCallback.onCaptureStart(parallelTaskData, MiCamera2ShotParallelRawBurst.this.mAlgoSize, false, false, false, false);
                        if (onCaptureStart != null) {
                            onCaptureStart.setBurstNum(MiCamera2ShotParallelRawBurst.this.mSequenceNum);
                            onCaptureStart.setAlgoType(MiCamera2ShotParallelRawBurst.this.mAlgoType);
                            onCaptureStart.setRawInputSize(MiCamera2ShotParallelRawBurst.this.mRawInputSize.getWidth(), MiCamera2ShotParallelRawBurst.this.mRawInputSize.getHeight());
                            AlgoConnector.getInstance().getLocalBinder().onCaptureStarted(onCaptureStart);
                            MiCamera2ShotParallelRawBurst.this.mFirstNum = false;
                        }
                        str = "onCaptureStarted: null task data";
                    } else {
                        str = "onCaptureStarted: null picture callback";
                    }
                    Log.w(str2, str);
                    MiCamera2ShotParallelRawBurst.this.mFirstNum = false;
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x009a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public CaptureRequest.Builder generateRequestBuilder() {
        int i;
        int i2 = 2;
        CaptureRequest.Builder createCaptureRequest = this.mMiCamera.getCameraDevice().createCaptureRequest(2);
        createCaptureRequest.addTarget(this.mMiCamera.getPreviewSurface());
        createCaptureRequest.addTarget(this.mRawSurface);
        if (isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) {
            MiCameraCompat.applyNotificationTrigger(createCaptureRequest, true);
            if (this.mYuvSurface == this.mMiCamera.getUltraWideRemoteSurface()) {
                i = 3;
                StringBuilder sb = new StringBuilder();
                sb.append("combinationMode: ");
                sb.append(i);
                String sb2 = sb.toString();
                String str = TAG;
                Log.d(str, sb2);
                configParallelSession(this.mYuvInputSize, i);
                if (C0122O00000o.instance().OOo0oOO()) {
                    Surface rawSurfaceForTuningBuffer = this.mMiCamera.getRawSurfaceForTuningBuffer();
                    if (rawSurfaceForTuningBuffer != null) {
                        Log.d(str, "add tuning surface to capture request, size is: %s", SurfaceUtils.getSurfaceSize(rawSurfaceForTuningBuffer));
                        createCaptureRequest.addTarget(rawSurfaceForTuningBuffer);
                    }
                }
                this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
                MiCameraCompat.applyIspMetaType(createCaptureRequest, 1);
                MiCameraCompat.applyIspFrameCount(createCaptureRequest, this.mSequenceNum);
                MiCameraCompat.applyIspPackedRawSupport(createCaptureRequest, 1);
                MiCameraCompat.applyIspPackedRawEnable(createCaptureRequest, 1);
                if (!this.mIsAinr) {
                    i2 = 1;
                }
                MiCameraCompat.applyIspTuningHint(createCaptureRequest, i2);
                return createCaptureRequest;
            }
        } else if (this.mMiCamera.isFacingFront()) {
            i = 17;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("combinationMode: ");
            sb3.append(i);
            String sb22 = sb3.toString();
            String str2 = TAG;
            Log.d(str2, sb22);
            configParallelSession(this.mYuvInputSize, i);
            if (C0122O00000o.instance().OOo0oOO()) {
            }
            this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
            MiCameraCompat.applyIspMetaType(createCaptureRequest, 1);
            MiCameraCompat.applyIspFrameCount(createCaptureRequest, this.mSequenceNum);
            MiCameraCompat.applyIspPackedRawSupport(createCaptureRequest, 1);
            MiCameraCompat.applyIspPackedRawEnable(createCaptureRequest, 1);
            if (!this.mIsAinr) {
            }
            MiCameraCompat.applyIspTuningHint(createCaptureRequest, i2);
            return createCaptureRequest;
        }
        i = 513;
        StringBuilder sb32 = new StringBuilder();
        sb32.append("combinationMode: ");
        sb32.append(i);
        String sb222 = sb32.toString();
        String str22 = TAG;
        Log.d(str22, sb222);
        configParallelSession(this.mYuvInputSize, i);
        if (C0122O00000o.instance().OOo0oOO()) {
        }
        this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
        MiCameraCompat.applyIspMetaType(createCaptureRequest, 1);
        MiCameraCompat.applyIspFrameCount(createCaptureRequest, this.mSequenceNum);
        MiCameraCompat.applyIspPackedRawSupport(createCaptureRequest, 1);
        MiCameraCompat.applyIspPackedRawEnable(createCaptureRequest, 1);
        if (!this.mIsAinr) {
        }
        MiCameraCompat.applyIspTuningHint(createCaptureRequest, i2);
        return createCaptureRequest;
    }

    /* access modifiers changed from: protected */
    public String getTag() {
        return TAG;
    }

    /* access modifiers changed from: protected */
    public void prepare() {
        if (!this.mFeatureSettingInitialized) {
            initFeatureSetting();
        }
        this.mFirstNum = true;
        this.mSequenceNum = this.mFeatureSetting.getFrameCount();
    }

    public boolean shouldApply() {
        if (!this.mFeatureSettingInitialized) {
            initFeatureSetting();
        }
        FeatureSetting featureSetting = this.mFeatureSetting;
        return featureSetting != null && featureSetting.getFrameCount() > 0;
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        MiCamera2 miCamera2;
        int i;
        long j;
        String str = TAG;
        try {
            CaptureCallback generateCaptureCallback = generateCaptureCallback();
            CaptureRequest.Builder generateRequestBuilder = generateRequestBuilder();
            long[] tuningIndexes = this.mFeatureSetting.getTuningIndexes();
            ArrayList arrayList = new ArrayList();
            for (int i2 = 0; i2 < this.mSequenceNum; i2++) {
                if (tuningIndexes != null) {
                    if (tuningIndexes.length > i2) {
                        j = tuningIndexes[i2];
                    } else if (tuningIndexes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("startSessionCapture: apply tuningIndexes[0] for frame ");
                        sb.append(i2);
                        Log.w(str, sb.toString());
                        j = tuningIndexes[0];
                    }
                    MiCameraCompat.applyIspTuningIndex(generateRequestBuilder, j);
                }
                MiCameraCompat.applyIspFrameIndex(generateRequestBuilder, i2);
                arrayList.add(generateRequestBuilder.build());
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("startSessionCapture request number: ");
            sb2.append(arrayList.size());
            Log.d(str, sb2.toString());
            this.mMiCamera.getCaptureSession().captureBurst(arrayList, generateCaptureCallback, this.mCameraHandler);
            MemoryHelper.addCapturedNumber(this.mMiCamera.hashCode(), this.mSequenceNum);
        } catch (CameraAccessException e) {
            Log.e(str, e.getMessage());
            miCamera2 = this.mMiCamera;
            i = e.getReason();
            miCamera2.notifyOnError(i);
        } catch (IllegalStateException e2) {
            Log.e(str, e2.getMessage());
            miCamera2 = this.mMiCamera;
            i = 256;
            miCamera2.notifyOnError(i);
        }
    }
}
