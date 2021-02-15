package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import android.hardware.camera2.CaptureFailure;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.Face;
import android.hardware.camera2.utils.SurfaceUtils;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.SystemProperties;
import android.util.Size;
import android.view.Surface;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.LocalParallelService.LocalBinder;
import com.android.camera.MemoryHelper;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.module.loader.camera2.ParallelSnapshotManager;
import com.android.camera.parallel.AlgoConnector;
import com.android.camera2.Camera2Proxy.PictureCallback;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.HdrEvValue;
import com.android.camera2.vendortag.struct.SuperNightEvValue;
import com.xiaomi.camera.base.CameraDeviceUtil;
import com.xiaomi.camera.core.ParallelTaskData;
import com.xiaomi.camera.imagecodec.ImagePool;
import com.xiaomi.camera.imagecodec.OutputConfiguration;
import com.xiaomi.camera.isp.IspInterfaceIO;
import com.xiaomi.protocol.ICustomCaptureResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;

public class MiCamera2ShotParallelBurst extends MiCamera2ShotParallel {
    private static final String TAG = "ShotParallelBurst";
    /* access modifiers changed from: private */
    public int mAlgoType = 0;
    /* access modifiers changed from: private */
    public boolean mAnchorFrame;
    /* access modifiers changed from: private */
    public long mCaptureTimestamp = -1;
    /* access modifiers changed from: private */
    public int mCompletedNum;
    /* access modifiers changed from: private */
    public boolean mFirstNum;
    private int mHdrCheckerAdrc;
    private int[] mHdrCheckerEvValue;
    private int mHdrCheckerSceneType;
    private int mHdrType;
    private boolean mIsHdrBokeh;
    /* access modifiers changed from: private */
    public boolean mIsHdrSR;
    /* access modifiers changed from: private */
    public boolean mIsSatFusionShotEnabled;
    /* access modifiers changed from: private */
    public int mMainPhysicalCameraId = -1;
    private int mMultiFrameNum;
    /* access modifiers changed from: private */
    public volatile boolean mNeedDoAnchorFrame = true;
    private final int mOperationMode;
    /* access modifiers changed from: private */
    public CameraSize mPreviewSize;
    /* access modifiers changed from: private */
    public int mSequenceNum;
    private boolean mShouldDoMFNR;
    private boolean mShouldDoSR;
    private boolean mSingleCaptureForHDRplusMFNR;
    /* access modifiers changed from: private */
    public int mSubPhysicalCameraId = -1;
    private SuperNightEvValue mSuperNightValue;
    /* access modifiers changed from: private */
    public final boolean mUseParallelVtCam;

    public MiCamera2ShotParallelBurst(MiCamera2 miCamera2, CaptureResult captureResult, boolean z) {
        super(miCamera2);
        this.mUseParallelVtCam = z;
        this.mPreviewCaptureResult = captureResult;
        this.mOperationMode = this.mMiCamera.getCapabilities().getOperatingMode();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0017, code lost:
        if (r6 != 10) goto L_0x0044;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyAlgoParameter(Builder builder, int i, int i2) {
        if (i2 == 1) {
            applyHdrParameter(builder, i);
        } else if (i2 == 2) {
            applyClearShotParameter(builder);
        } else if (i2 != 3) {
            if (i2 != 7) {
                if (i2 != 12) {
                    if (i2 != 9) {
                    }
                }
                applySuperNightParameter(builder, i);
            } else {
                MiCameraCompat.applySwMfnrEnable(builder, this.mShouldDoMFNR);
                MiCameraCompat.applyMfnrEnable(builder, false);
                MiCameraCompat.applyMultiFrameInputNum(builder, this.mSequenceNum);
                MiCameraCompat.applyHHT(builder, true);
                Log.i(TAG, "HHT algo in applyAlgoParameter");
            }
            applyLowLightBokehParameter(builder);
        } else {
            applySuperResolutionParameter(builder, i);
        }
        if (C0124O00000oO.isMTKPlatform()) {
            MiCameraCompat.copyAiSceneFromCaptureResultToRequest(this.mPreviewCaptureResult, builder);
        } else if (isIn3OrMoreSatMode()) {
            CaptureRequestBuilder.applySmoothTransition(builder, this.mMiCamera.getCapabilities(), false);
            CaptureRequestBuilder.applySatFallback(builder, this.mMiCamera.getCapabilities(), false);
        }
    }

    private void applyClearShotParameter(Builder builder) {
        MiCameraCompat.applySwMfnrEnable(builder, this.mShouldDoMFNR);
        MiCameraCompat.applyMfnrEnable(builder, false);
        if (C0124O00000oO.OOooOoO() || C0124O00000oO.O0o00o) {
            CompatibilityUtils.setZsl(builder, true);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:105:? A[ORIG_RETURN, RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01bc  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void applyHdrParameter(Builder builder, int i) {
        int i2;
        String str;
        int i3;
        StringBuilder sb;
        if (i <= this.mSequenceNum) {
            MiCameraCompat.applyMultiFrameIndex(builder, i + 1);
            MiCameraCompat.applyMultiFrameCount(builder, this.mSequenceNum);
            if (!C0122O00000o.instance().OOo000o()) {
                if (this.mIsHdrBokeh) {
                    MiCameraCompat.applyHdrBracketMode(builder, (byte) (this.mHdrCheckerEvValue[i] < 0 ? 1 : 0));
                } else {
                    MiCameraCompat.applyHdrBracketMode(builder, 1);
                }
            }
            boolean OOo000o = C0122O00000o.instance().OOo000o();
            String str2 = TAG;
            if (!OOo000o || !this.mMiCamera.getCameraConfigs().isHDREnabled()) {
                MiCameraCompat.applyMultiFrameInputNum(builder, this.mSequenceNum);
            } else {
                Log.e(str2, "[ALGOUP|MMCAMERA] Algo Up HDR!!!!");
                MiCameraCompat.applyMultiFrameInputNum(builder, this.mSequenceNum * 2);
                CaptureRequestBuilder.applyAELock(builder, true);
            }
            if (C0124O00000oO.isMTKPlatform() || C0124O00000oO.O0o0O00 || C0124O00000oO.O0o0O0 || C0124O00000oO.O0o0OOO || C0124O00000oO.O0o0O0O || C0124O00000oO.O0o0O0o || C0124O00000oO.O0o0O) {
                CaptureRequestBuilder.applyAELock(builder, true);
            }
            builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(this.mHdrCheckerEvValue[i]));
            MiCameraCompat.applyHdrParameter(builder, Integer.valueOf(this.mHdrCheckerSceneType), Integer.valueOf(this.mHdrCheckerAdrc));
            MiCameraCompat.applyMiHDRSR(builder, false);
            boolean z = !C0124O00000oO.O0o0O00 ? !(!C0122O00000o.instance().OOOoo0O() ? !C0122O00000o.instance().OOOO0() || this.mHdrCheckerEvValue[i] != 0 : this.mHdrCheckerEvValue[i] != 0) : this.mHdrCheckerEvValue[i] >= 0;
            boolean z2 = (this.mMiCamera.getSatMasterCameraId() == 2 || this.mMiCamera.getSatMasterCameraId() == 1) ? true : this.mMiCamera.getSatMasterCameraId() == 3 ? C0124O00000oO.O0o0OOO : false;
            boolean Oo0Oo0 = this.mMiCamera.getCapabilities().getCameraId() == Camera2DataContainer.getInstance().getFrontCameraId() ? C0122O00000o.instance().getConfig().Oo0Oo0() : false;
            String str3 = "applyHdrParameter enable mfnr EV = ";
            if (z && z2 && isIn3OrMoreSatMode() && this.mSequenceNum < 4) {
                sb = new StringBuilder();
                sb.append(str3);
                i3 = this.mHdrCheckerEvValue[i];
            } else if (z && Oo0Oo0 && this.mSequenceNum < 4) {
                sb = new StringBuilder();
                sb.append(str3);
                i3 = this.mHdrCheckerEvValue[i];
            } else if (this.mSingleCaptureForHDRplusMFNR) {
                str = "applyHdrParameter enable mfnr";
                Log.d(str2, str);
                MiCameraCompat.applyMfnrEnable(builder, true);
                if (this.mIsHdrBokeh) {
                    CameraCapabilities capabilities = this.mMiCamera.getCapabilities();
                    if (capabilities != null && capabilities.isSupportHdrBokeh()) {
                        MiCameraCompat.applyHdrBokeh(builder, true);
                    }
                }
                if (C0122O00000o.instance().OOo0oOO()) {
                    int i4 = this.mHdrType;
                    if (i4 == 0) {
                        Log.d(str2, "enable isp tuning capture hint for HDR");
                        i2 = CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_HDR;
                    } else if (i4 == 1) {
                        Log.d(str2, "enable isp tuning capture hint for LLHDR");
                        i2 = CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_LLHDR;
                    } else {
                        return;
                    }
                    MiCameraCompat.applyIspTuningHint(builder, i2);
                    return;
                }
                return;
            } else if (z && z2 && C0122O00000o.instance().OOOO0()) {
                sb = new StringBuilder();
                sb.append(str3);
                i3 = this.mHdrCheckerEvValue[i];
            } else if (!z || !this.mIsHdrBokeh) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applyHdrParameter disable mfnr EV = ");
                sb2.append(this.mHdrCheckerEvValue[i]);
                Log.d(str2, sb2.toString());
                MiCameraCompat.applyMfnrEnable(builder, false);
                if (this.mIsHdrBokeh) {
                }
                if (C0122O00000o.instance().OOo0oOO()) {
                }
            } else {
                sb = new StringBuilder();
                sb.append(str3);
                i3 = this.mHdrCheckerEvValue[i];
            }
            sb.append(i3);
            str = sb.toString();
            Log.d(str2, str);
            MiCameraCompat.applyMfnrEnable(builder, true);
            if (this.mIsHdrBokeh) {
            }
            if (C0122O00000o.instance().OOo0oOO()) {
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("wrong request index ");
            sb3.append(i);
            throw new RuntimeException(sb3.toString());
        }
    }

    private void applyLowLightBokehParameter(Builder builder) {
        MiCameraCompat.applyMultiFrameInputNum(builder, this.mSequenceNum);
        MiCameraCompat.applySwMfnrEnable(builder, false);
        MiCameraCompat.applyMfnrEnable(builder, false);
    }

    private void applySuperNightParameter(Builder builder, int i) {
        if (i <= this.mSequenceNum) {
            StringBuilder sb = new StringBuilder();
            sb.append("applySuperNightParameter: requestIndex > ");
            sb.append(i);
            sb.append(" | ev = ");
            sb.append(this.mSuperNightValue.getValue()[i]);
            String sb2 = sb.toString();
            String str = TAG;
            Log.d(str, sb2);
            if (C0124O00000oO.isMTKPlatform()) {
                CaptureRequestBuilder.applyAELock(builder, true);
            } else if (CameraSettings.isFrontCamera() && !C0124O00000oO.O0o0o00) {
                MiCameraCompat.applyHdrBracketMode(builder, 1);
            }
            if (this.mAlgoType == 12) {
                Log.d(str, "apply raw super night params");
                builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(this.mSuperNightValue.getValue()[i]));
                MiCameraCompat.applyIspTuningHint(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_SUPER_RAW);
                MiCameraCompat.applySuperNightRawEnabled(builder, true);
                MiCameraCompat.applyMtkProcessRaw(builder, 1);
            } else {
                builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(this.mSuperNightValue.getValue()[i]));
            }
            MiCameraCompat.applyMultiFrameInputNum(builder, this.mMultiFrameNum);
            MiCameraCompat.applySwMfnrEnable(builder, false);
            MiCameraCompat.applyMfnrEnable(builder, false);
            return;
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("wrong request index ");
        sb3.append(i);
        throw new RuntimeException(sb3.toString());
    }

    private void applySuperResolutionParameter(Builder builder, int i) {
        MiCameraCompat.applyMultiFrameIndex(builder, i + 1);
        MiCameraCompat.applyMultiFrameCount(builder, this.mSequenceNum);
        MiCameraCompat.applyMultiFrameInputNum(builder, this.mMultiFrameNum);
        MiCameraCompat.applyMfnrEnable(builder, false);
        MiCameraCompat.applyHDR(builder, false);
        MiCameraCompat.applySuperResolution(builder, true);
        CaptureRequestBuilder.applyAELock(builder, true);
        CaptureRequestBuilder.applyAWBLock(builder, true);
        boolean isMTKPlatform = C0124O00000oO.isMTKPlatform();
        String str = TAG;
        if (isMTKPlatform && C0122O00000o.instance().OOo0oOO()) {
            Log.d(str, "enable isp tuning capture hint for MFSR");
            MiCameraCompat.applyIspTuningHint(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_MFSR);
            MiCameraCompat.applyNoiseReduction(builder, false);
            MiCameraCompat.applyHighQualityReprocess(builder, false);
        }
        if (this.mHdrCheckerEvValue == null || !this.mIsHdrSR) {
            MiCameraCompat.applyMiHDRSR(builder, false);
            return;
        }
        MiCameraCompat.applyMiHDRSR(builder, true);
        MiCameraCompat.applyHDR(builder, false);
        if (this.mHdrCheckerEvValue[i] == 0) {
            MiCameraCompat.applySuperResolution(builder, true);
        } else {
            MiCameraCompat.applySuperResolution(builder, false);
        }
        builder.set(CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(true));
        Log.d(str, String.format(Locale.ENGLISH, "HdrSrEv[%d]=%d", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mHdrCheckerEvValue[i])}));
        builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(this.mHdrCheckerEvValue[i]));
        MiCameraCompat.applyHdrBracketMode(builder, 1);
    }

    private boolean doAnchorFrameAsThumbnail() {
        boolean isAnchorFrameType;
        StringBuilder sb;
        String str;
        boolean isModuleAnchorFrame = this.mMiCamera.isModuleAnchorFrame();
        String str2 = TAG;
        if (!isModuleAnchorFrame) {
            Log.d(str2, "anchor frame do not enable");
            return false;
        }
        CameraCapabilities capabilities = this.mMiCamera.getCapabilities();
        if (capabilities == null) {
            return false;
        }
        boolean z = !CameraSettings.isBackCamera();
        int i = this.mAlgoType;
        if (i == 3) {
            isAnchorFrameType = capabilities.isAnchorFrameType(z, 2);
            sb = new StringBuilder();
            str = "SR anchor frame ";
        } else if (i == 1) {
            isAnchorFrameType = capabilities.isAnchorFrameType(z, 5);
            sb = new StringBuilder();
            str = "HDR anchor frame ";
        } else if (i == 10 || i == 12) {
            isAnchorFrameType = capabilities.isAnchorFrameType(z ? 1 : 0, 6);
            sb = new StringBuilder();
            str = "super night anchor frame ";
        } else {
            Log.d(str2, "default anchor frame true");
            return true;
        }
        sb.append(str);
        sb.append(isAnchorFrameType);
        Log.d(str2, sb.toString());
        return isAnchorFrameType;
    }

    private int getGroupShotMaxImage() {
        CaptureResult captureResult = this.mPreviewCaptureResult;
        Face[] faceArr = captureResult == null ? null : (Face[]) captureResult.get(CaptureResult.STATISTICS_FACES);
        return Util.clamp((faceArr != null ? faceArr.length : 0) + 1, 2, 4);
    }

    private int getGroupShotNum() {
        if (Util.isMemoryRich(CameraAppImpl.getAndroidContext())) {
            return getGroupShotMaxImage();
        }
        Log.w(TAG, "getGroupShotNum: low memory");
        return 2;
    }

    private void initFeatureSetting() {
        String str = TAG;
        Log.d(str, "initFeatureSetting: E");
        CameraSize sensorRawImageSize = this.mMiCamera.getSensorRawImageSize();
        CameraSize outputSize = this.mMiCamera.getCameraConfigs().getOutputSize();
        StringBuilder sb = new StringBuilder();
        sb.append("initFeatureSetting: rawInputSize = ");
        sb.append(sensorRawImageSize);
        sb.append(", yuvInputSize = ");
        sb.append(outputSize);
        Log.d(str, sb.toString());
        CameraSize outputSize2 = this.mMiCamera.getCameraConfigs().getOutputSize();
        int width = outputSize2 == null ? outputSize.getWidth() : outputSize2.getWidth();
        int height = outputSize2 == null ? outputSize.getHeight() : outputSize2.getHeight();
        if (!(width == outputSize.getWidth() && height == outputSize.getHeight())) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("initFeatureSetting: outputSize = ");
            sb2.append(outputSize2);
            Log.d(str, sb2.toString());
        }
        OutputConfiguration outputConfiguration = new OutputConfiguration(width, height, this.mMiCamera.getCameraConfigs().getPhotoFormat());
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        CaptureResult captureResult = this.mPreviewCaptureResult;
        if (!(localBinder == null || captureResult == null)) {
            localBinder.queryFeatureSetting(new IspInterfaceIO(new Size(outputSize.getWidth(), outputSize.getHeight()), new Size(sensorRawImageSize.getWidth(), sensorRawImageSize.getHeight()), outputConfiguration), CameraDeviceUtil.getNativeMetadata(captureResult), null, false);
            Log.d(str, "initFeatureSetting: ");
        }
        Log.d(str, "initFeatureSetting: X");
    }

    private boolean isUpdateHDRCheckerValues() {
        return getMagneticDetectedCallback() == null || !getMagneticDetectedCallback().isLockHDRChecker(TAG);
    }

    private void prepareClearShot(int i) {
        int i2 = C0124O00000oO.OOooOoO() ? 10 : 5;
        this.mSequenceNum = i2;
        this.mMultiFrameNum = i2;
    }

    private void prepareGroupShot() {
        int groupShotNum = getGroupShotNum();
        this.mSequenceNum = groupShotNum;
        this.mMultiFrameNum = groupShotNum;
    }

    private void prepareHHT() {
        String str;
        LocalBinder localBinder = AlgoConnector.getInstance().getLocalBinder();
        String str2 = TAG;
        if (localBinder == null || localBinder.isIdle() || this.mMiCamera.getCameraConfigs().isAiASDEnabled() || this.mMiCamera.getCameraConfigs().getBeautyValues() == null || this.mMiCamera.getCameraConfigs().getBeautyValues().isSmoothLevelOn()) {
            int hHTFrameNumber = CaptureResultParser.getHHTFrameNumber(this.mMiCamera.getCapabilities(), this.mPreviewCaptureResult);
            if (hHTFrameNumber > 0) {
                this.mSequenceNum = hHTFrameNumber;
                this.mMultiFrameNum = hHTFrameNumber;
                StringBuilder sb = new StringBuilder();
                sb.append("getHHTFrameNumber hht(");
                sb.append(hHTFrameNumber);
                sb.append(" -> 1)");
                str = sb.toString();
            } else {
                this.mSequenceNum = 5;
                this.mMultiFrameNum = 5;
                str = "default hht(5 -> 1)";
            }
        } else {
            this.mSequenceNum = 3;
            this.mMultiFrameNum = 3;
            str = "switch to quick shot hht(3 -> 1)";
        }
        Log.i(str2, str);
    }

    private void prepareHdr() {
        int[] iArr;
        boolean isFlashHDR = this.mMiCamera.getCameraConfigs().getHDRStatus().isFlashHDR();
        String str = TAG;
        if (isFlashHDR) {
            StringBuilder sb = new StringBuilder();
            sb.append("prepareHDR night hdr ev_value:");
            sb.append(Arrays.toString(this.mHdrCheckerEvValue));
            Log.d(str, sb.toString());
        }
        if (this.mSingleCaptureForHDRplusMFNR) {
            this.mSequenceNum = 1;
            this.mMultiFrameNum = 1;
            iArr = new int[]{0};
        } else {
            HdrEvValue hdrEvValue = new HdrEvValue(CaptureResultParser.getHdrCheckerValues(this.mPreviewCaptureResult));
            this.mHdrType = hdrEvValue.getHdrType();
            int sequenceNum = hdrEvValue.getSequenceNum();
            this.mSequenceNum = sequenceNum;
            this.mMultiFrameNum = sequenceNum;
            iArr = hdrEvValue.getHdrCheckerEvValue();
        }
        this.mHdrCheckerEvValue = iArr;
        String str2 = "prepareHdr: scene = ";
        if (isUpdateHDRCheckerValues() || this.mMiCamera.getCameraConfigs().getHdrCheckerEvValue() == null) {
            this.mHdrCheckerSceneType = CaptureResultParser.getHdrCheckerSceneType(this.mPreviewCaptureResult);
            this.mHdrCheckerAdrc = CaptureResultParser.getHdrCheckerAdrc(this.mPreviewCaptureResult);
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(this.mHdrCheckerSceneType);
            sb2.append(" adrc = ");
            sb2.append(this.mHdrCheckerAdrc);
            Log.d(str, sb2.toString());
            this.mMiCamera.getCameraConfigs().setHdrCheckerEvValue(this.mHdrCheckerEvValue);
            this.mMiCamera.getCameraConfigs().setHdrCheckerSceneType(this.mHdrCheckerSceneType);
            this.mMiCamera.getCameraConfigs().setHdrCheckerAdrc(this.mHdrCheckerAdrc);
            return;
        }
        Log.d(str, "hdr checker values not update：");
        this.mHdrCheckerEvValue = this.mMiCamera.getCameraConfigs().getHdrCheckerEvValue();
        this.mHdrCheckerSceneType = this.mMiCamera.getCameraConfigs().getHdrCheckerSceneType();
        this.mHdrCheckerAdrc = this.mMiCamera.getCameraConfigs().getHdrCheckerAdrc();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(str2);
        sb3.append(this.mHdrCheckerSceneType);
        sb3.append(",adrc = ");
        sb3.append(this.mHdrCheckerAdrc);
        sb3.append(",EvValue = ");
        int[] iArr2 = this.mHdrCheckerEvValue;
        sb3.append(iArr2 != null ? Arrays.toString(iArr2) : null);
        Log.d(str, sb3.toString());
    }

    private void prepareLowLightBokeh() {
        this.mSequenceNum = 6;
        this.mMultiFrameNum = 6;
    }

    private void prepareSR(boolean z) {
        String str = TAG;
        if (z) {
            HdrEvValue hdrEvValue = new HdrEvValue(CaptureResultParser.getHdrCheckerValues(this.mPreviewCaptureResult), z);
            StringBuilder sb = new StringBuilder();
            sb.append("hdr ev value is ");
            sb.append(hdrEvValue);
            Log.d(str, sb.toString());
            this.mSequenceNum = hdrEvValue.getSequenceNum();
            this.mHdrCheckerEvValue = hdrEvValue.getHdrCheckerEvValue();
            int[] iArr = this.mHdrCheckerEvValue;
            if (iArr != null) {
                this.mSequenceNum = iArr.length;
            }
            this.mMultiFrameNum = (this.mSequenceNum - 10) + 1;
            return;
        }
        int i = SystemProperties.getInt("camera.sr.framecount", C0122O00000o.instance().OO00OO());
        this.mSequenceNum = i;
        this.mMultiFrameNum = i;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("prepareSR: captureNum=");
        sb2.append(this.mSequenceNum);
        Log.d(str, sb2.toString());
    }

    private void prepareSuperNight() {
        this.mSuperNightValue = SuperNightEvValue.parseSuperNightEvValue(CaptureResultParser.getSuperNightCheckerEv(this.mPreviewCaptureResult), SystemProperties.get("camera.debug.superlowlight"), CameraSettings.isFrontCamera());
        StringBuilder sb = new StringBuilder();
        sb.append("prepareSuperNight: mSuperNightValue >> ");
        sb.append(this.mSuperNightValue.toString());
        Log.d(TAG, sb.toString());
        int sequenceNum = this.mSuperNightValue.getSequenceNum();
        this.mSequenceNum = sequenceNum;
        this.mMultiFrameNum = sequenceNum;
        if (this.mAlgoType == 12) {
            initFeatureSetting();
        }
    }

    /* access modifiers changed from: protected */
    public CaptureCallback generateCaptureCallback() {
        return new CaptureCallback() {
            public void onCaptureCompleted(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull TotalCaptureResult totalCaptureResult) {
                StringBuilder sb;
                int i;
                MiCamera2ShotParallelBurst.this.mCompletedNum = MiCamera2ShotParallelBurst.this.mCompletedNum + 1;
                boolean OOo000o = C0122O00000o.instance().OOo000o();
                String str = "/";
                String str2 = "onCaptureCompleted: ";
                String str3 = MiCamera2ShotParallelBurst.TAG;
                if (!OOo000o || !MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().isHDREnabled()) {
                    sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(MiCamera2ShotParallelBurst.this.mCompletedNum);
                    sb.append(str);
                    i = MiCamera2ShotParallelBurst.this.mSequenceNum;
                } else {
                    sb = new StringBuilder();
                    sb.append(str2);
                    sb.append(MiCamera2ShotParallelBurst.this.mCompletedNum);
                    sb.append(str);
                    i = MiCamera2ShotParallelBurst.this.mSequenceNum * 2;
                }
                sb.append(i);
                Log.d(str3, sb.toString());
                ICustomCaptureResult customCaptureResult = CameraDeviceUtil.getCustomCaptureResult(totalCaptureResult);
                Map physicalCameraResults = totalCaptureResult.getPhysicalCameraResults();
                if (physicalCameraResults != null) {
                    if (MiCamera2ShotParallelBurst.this.mMainPhysicalCameraId != -1) {
                        CaptureResult captureResult = (CaptureResult) physicalCameraResults.get(String.valueOf(MiCamera2ShotParallelBurst.this.mMainPhysicalCameraId));
                        if (captureResult != null) {
                            customCaptureResult.setMainPhysicalResult(CameraDeviceUtil.getNativeMetadata(captureResult));
                        }
                    }
                    if (MiCamera2ShotParallelBurst.this.mSubPhysicalCameraId != -1) {
                        CaptureResult captureResult2 = (CaptureResult) physicalCameraResults.get(String.valueOf(MiCamera2ShotParallelBurst.this.mSubPhysicalCameraId));
                        if (captureResult2 != null) {
                            customCaptureResult.setSubPhysicalResult(CameraDeviceUtil.getNativeMetadata(captureResult2));
                        }
                    }
                }
                AlgoConnector.getInstance().getLocalBinder().onCaptureCompleted(customCaptureResult, MiCamera2ShotParallelBurst.this.mCompletedNum == 1);
                String str4 = "onCaptureCompleted: finished all frame";
                if (!C0122O00000o.instance().OOo000o() || !MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().isHDREnabled() ? MiCamera2ShotParallelBurst.this.mSequenceNum == MiCamera2ShotParallelBurst.this.mCompletedNum : MiCamera2ShotParallelBurst.this.mSequenceNum * 2 == MiCamera2ShotParallelBurst.this.mCompletedNum) {
                    onCaptureShutter();
                    MiCamera2ShotParallelBurst miCamera2ShotParallelBurst = MiCamera2ShotParallelBurst.this;
                    miCamera2ShotParallelBurst.mMiCamera.onCapturePictureFinished(true, miCamera2ShotParallelBurst);
                    Log.d(str3, str4);
                }
                boolean isSREnable = CaptureResultParser.isSREnable(totalCaptureResult);
                if (isSREnable && VERSION.SDK_INT >= 29) {
                    Boolean bool = (Boolean) captureRequest.get(new Key("xiaomi.superResolution.enabled", Boolean.class));
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onCaptureCompleted: isSRRequest = ");
                    sb2.append(bool);
                    Log.d(str3, sb2.toString());
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("onCaptureCompleted: isSREnabled = ");
                sb3.append(isSREnable);
                Log.d(str3, sb3.toString());
                Boolean bool2 = (Boolean) VendorTagHelper.getValue((CaptureResult) totalCaptureResult, CaptureResultVendorTags.IS_HDR_ENABLE);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("onCaptureCompleted: hdrEnabled = ");
                sb4.append(bool2);
                Log.d(str3, sb4.toString());
                StringBuilder sb5 = new StringBuilder();
                sb5.append("onCaptureCompleted: fusion = ");
                sb5.append(MiCamera2ShotParallelBurst.this.mIsSatFusionShotEnabled);
                Log.d(str3, sb5.toString());
                ImagePool.getInstance().trimPoolBuffer();
            }

            public void onCaptureFailed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureFailure captureFailure) {
                super.onCaptureFailed(cameraCaptureSession, captureRequest, captureFailure);
                StringBuilder sb = new StringBuilder();
                sb.append("onCaptureFailed: reason=");
                sb.append(captureFailure.getReason());
                sb.append(" timestamp=");
                sb.append(MiCamera2ShotParallelBurst.this.mCaptureTimestamp);
                sb.append(" frameNumber=");
                sb.append(captureFailure.getFrameNumber());
                Log.k(6, MiCamera2ShotParallelBurst.TAG, sb.toString());
                onCaptureShutter();
                MiCamera2ShotParallelBurst miCamera2ShotParallelBurst = MiCamera2ShotParallelBurst.this;
                miCamera2ShotParallelBurst.mMiCamera.onCapturePictureFinished(false, miCamera2ShotParallelBurst);
                if (MiCamera2ShotParallelBurst.this.mCaptureTimestamp != -1) {
                    AlgoConnector.getInstance().getLocalBinder().onCaptureFailed(MiCamera2ShotParallelBurst.this.mCaptureTimestamp, captureFailure.getReason());
                }
            }

            public void onCaptureProgressed(@NonNull CameraCaptureSession cameraCaptureSession, @NonNull CaptureRequest captureRequest, @NonNull CaptureResult captureResult) {
                super.onCaptureProgressed(cameraCaptureSession, captureRequest, captureResult);
                if (MiCamera2ShotParallelBurst.this.mMiCamera.getPreviewCallbackEnabled() > 0 && (MiCamera2ShotParallelBurst.this.mMiCamera.getPreviewCallbackEnabled() & 16) != 0) {
                    Long l = (Long) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.ANCHOR_FRAME_TIMESTAMP);
                    long longValue = l == null ? -1 : l.longValue();
                    StringBuilder sb = new StringBuilder();
                    sb.append("begin to choose anchor frame ");
                    sb.append(longValue);
                    Log.d(MiCamera2ShotParallelBurst.TAG, sb.toString());
                    int i = (longValue > 0 ? 1 : (longValue == 0 ? 0 : -1));
                    if (i > 0 && MiCamera2ShotParallelBurst.this.mAnchorFrame) {
                        PictureCallback pictureCallback = MiCamera2ShotParallelBurst.this.getPictureCallback();
                        if (pictureCallback != null && MiCamera2ShotParallelBurst.this.mNeedDoAnchorFrame) {
                            pictureCallback.onCaptureProgress(MiCamera2ShotParallelBurst.this.isQuickShotAnimation(), MiCamera2ShotParallelBurst.this.mAnchorFrame, true, false, captureResult);
                            MiCamera2ShotParallelBurst.this.mNeedDoAnchorFrame = false;
                            MiCamera2ShotParallelBurst.this.mMiCamera.getCacheImageDecoder().saveAnchorFrameThumbnail(longValue, MiCamera2ShotParallelBurst.this.mPreviewSize.width, MiCamera2ShotParallelBurst.this.mPreviewSize.height, null, String.valueOf(System.currentTimeMillis()));
                        }
                    } else if (i == 0 && MiCamera2ShotParallelBurst.this.mAnchorFrame) {
                        PictureCallback pictureCallback2 = MiCamera2ShotParallelBurst.this.getPictureCallback();
                        if (pictureCallback2 != null && MiCamera2ShotParallelBurst.this.mNeedDoAnchorFrame) {
                            pictureCallback2.onCaptureProgress(MiCamera2ShotParallelBurst.this.isQuickShotAnimation(), MiCamera2ShotParallelBurst.this.mAnchorFrame, false, true, captureResult);
                            MiCamera2ShotParallelBurst.this.mNeedDoAnchorFrame = false;
                        }
                    }
                }
            }

            public void onCaptureShutter() {
                if (MiCamera2ShotParallelBurst.this.mSequenceNum > 1) {
                    if (C0122O00000o.instance().OO0OoOO() || DataRepository.dataItemRunning().isSuperNightCaptureWithKnownDuration()) {
                        PictureCallback pictureCallback = MiCamera2ShotParallelBurst.this.getPictureCallback();
                        if (pictureCallback != null) {
                            pictureCallback.onCaptureShutter(MiCamera2ShotParallelBurst.this.isQuickShotAnimation(), MiCamera2ShotParallelBurst.this.mAnchorFrame, false, false);
                        }
                    }
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
                sb.append(" isFirst=");
                sb.append(MiCamera2ShotParallelBurst.this.mFirstNum);
                String sb2 = sb.toString();
                String str2 = MiCamera2ShotParallelBurst.TAG;
                Log.k(3, str2, sb2);
                if (MiCamera2ShotParallelBurst.this.mUseParallelVtCam) {
                    MiCamera2ShotParallelBurst.this.mMiCamera.enableSat();
                }
                super.onCaptureStarted(cameraCaptureSession, captureRequest, j, j2);
                if (MiCamera2ShotParallelBurst.this.mFirstNum) {
                    PictureCallback pictureCallback = MiCamera2ShotParallelBurst.this.getPictureCallback();
                    if (pictureCallback != null) {
                        ParallelTaskData parallelTaskData = new ParallelTaskData(MiCamera2ShotParallelBurst.this.mMiCamera.getId(), j, MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().getShotType(), MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().getShotPath(), MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().getCaptureTime());
                        parallelTaskData.setBurstNum(MiCamera2ShotParallelBurst.this.mSequenceNum);
                        MiCamera2ShotParallelBurst miCamera2ShotParallelBurst = MiCamera2ShotParallelBurst.this;
                        ParallelTaskData onCaptureStart = pictureCallback.onCaptureStart(parallelTaskData, miCamera2ShotParallelBurst.mAlgoSize, miCamera2ShotParallelBurst.isQuickShotAnimation(), MiCamera2ShotParallelBurst.this.mAnchorFrame, false, false);
                        if (onCaptureStart != null) {
                            onCaptureStart.setIsSatFusionShot(MiCamera2ShotParallelBurst.this.mIsSatFusionShotEnabled);
                            onCaptureStart.setAlgoType(MiCamera2ShotParallelBurst.this.mAlgoType);
                            onCaptureStart.setHdrSR(MiCamera2ShotParallelBurst.this.mIsHdrSR);
                            if (MiCamera2ShotParallelBurst.this.mAlgoType == 12) {
                                onCaptureStart.setRawInputSize(MiCamera2ShotParallelBurst.this.mMiCamera.getSensorRawImageSize().width, MiCamera2ShotParallelBurst.this.mMiCamera.getSensorRawImageSize().height);
                                onCaptureStart.setActiveRegion(MiCamera2ShotParallelBurst.this.mMiCamera.getCapabilities().getActiveArraySize());
                                onCaptureStart.setZoomRatio(MiCamera2ShotParallelBurst.this.mMiCamera.getZoomRatio());
                            }
                            int access$500 = (!C0122O00000o.instance().OOo000o() || !MiCamera2ShotParallelBurst.this.mMiCamera.getCameraConfigs().isHDREnabled()) ? MiCamera2ShotParallelBurst.this.mSequenceNum : MiCamera2ShotParallelBurst.this.mSequenceNum * 2;
                            onCaptureStart.setBurstNum(access$500);
                            MiCamera2ShotParallelBurst.this.mCaptureTimestamp = j3;
                            AlgoConnector.getInstance().getLocalBinder().onCaptureStarted(onCaptureStart);
                            MiCamera2ShotParallelBurst.this.mFirstNum = false;
                        }
                        str = "onCaptureStarted: null task data";
                    } else {
                        str = "onCaptureStarted: null picture callback";
                    }
                    Log.w(str2, str);
                    MiCamera2ShotParallelBurst.this.mFirstNum = false;
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:100:0x0282  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Builder generateRequestBuilder() {
        String sb;
        String sb2;
        Builder createCaptureRequest = (!this.mUseParallelVtCam ? this.mMiCamera.getCameraDevice() : ParallelSnapshotManager.getInstance().getCameraDevice()).createCaptureRequest(2);
        boolean isQcfaEnable = this.mMiCamera.isQcfaEnable();
        String str = TAG;
        if (isQcfaEnable) {
            Surface qcfaRemoteSurface = this.mMiCamera.getQcfaRemoteSurface();
            Size surfaceSize = SurfaceUtils.getSurfaceSize(qcfaRemoteSurface);
            Log.d(str, String.format(Locale.ENGLISH, "[QCFA]add surface %s to capture request, size is: %s", new Object[]{qcfaRemoteSurface, surfaceSize}));
            createCaptureRequest.addTarget(qcfaRemoteSurface);
            if (C0124O00000oO.OOooOoO() || C0124O00000oO.O0o00o) {
                createCaptureRequest.addTarget(this.mMiCamera.getPreviewSurface());
            }
            Size size = this.mLockedAlgoSize;
            if (size != null) {
                configParallelSession(size);
            } else {
                configParallelSession(surfaceSize);
            }
        } else {
            if (isIn3OrMoreSatMode() || isInMultiSurfaceSatMode()) {
                Surface mainCaptureSurface = !this.mUseParallelVtCam ? getMainCaptureSurface() : ParallelSnapshotManager.getInstance().getCaptureSurface(this.mMiCamera.getSatMasterCameraId());
                Size surfaceSize2 = SurfaceUtils.getSurfaceSize(mainCaptureSurface);
                Log.d(str, String.format(Locale.ENGLISH, "[SAT]add main surface %s to capture request, size is: %s", new Object[]{mainCaptureSurface, surfaceSize2}));
                this.mMainPhysicalCameraId = this.mMiCamera.getSatPhysicalCameraId();
                createCaptureRequest.addTarget(mainCaptureSurface);
                int i = 513;
                if (mainCaptureSurface == this.mMiCamera.getUltraWideRemoteSurface() || (this.mUseParallelVtCam && mainCaptureSurface == ParallelSnapshotManager.getInstance().getCaptureSurface(1))) {
                    i = 3;
                }
                if (this.mIsSatFusionShotEnabled) {
                    Surface ultraTeleRemoteSurface = this.mMiCamera.getUltraTeleRemoteSurface();
                    Log.d(str, String.format(Locale.ENGLISH, "[SAT]add ultra tele surface %s to capture request, size is: %s", new Object[]{ultraTeleRemoteSurface, SurfaceUtils.getSurfaceSize(ultraTeleRemoteSurface)}));
                    this.mSubPhysicalCameraId = Camera2DataContainer.getInstance().getUltraTeleCameraId();
                    createCaptureRequest.addTarget(ultraTeleRemoteSurface);
                    VendorTagHelper.setValueQuietly(createCaptureRequest, CaptureRequestVendorTags.CONTROL_SAT_FUSION_SHOT, Byte.valueOf(1));
                    i = 516;
                } else {
                    VendorTagHelper.setValueQuietly(createCaptureRequest, CaptureRequestVendorTags.CONTROL_SAT_FUSION_SHOT, Byte.valueOf(0));
                }
                StringBuilder sb3 = new StringBuilder();
                sb3.append("[SAT]combinationMode: ");
                sb3.append(i);
                Log.d(str, sb3.toString());
                configParallelSession(surfaceSize2, i);
            } else {
                boolean z = this.mAlgoType == 12;
                for (Surface surface : this.mMiCamera.getRemoteSurfaceList()) {
                    if (!z) {
                        if (!(surface == this.mMiCamera.getActiveRawSurface() || surface == this.mMiCamera.getRawSurfaceForTuningBuffer())) {
                            if (surface == this.mMiCamera.getTuningRemoteSurface()) {
                            }
                        }
                    }
                    Log.d(str, String.format(Locale.ENGLISH, "add surface %s to capture request, size is: %s", new Object[]{surface, SurfaceUtils.getSurfaceSize(surface)}));
                    createCaptureRequest.addTarget(surface);
                }
                this.mAlgoSize = this.mMiCamera.getPictureSize();
            }
            if (!C0124O00000oO.isMTKPlatform() && this.mOperationMode != 36865 && (C0124O00000oO.OOooOoO() || C0124O00000oO.O0o00o || this.mOperationMode != 36867)) {
                Surface previewSurface = this.mMiCamera.getPreviewSurface();
                Log.d(str, String.format(Locale.ENGLISH, "add preview surface %s to capture request, size is: %s", new Object[]{previewSurface, SurfaceUtils.getSurfaceSize(previewSurface)}));
                if (!this.mUseParallelVtCam) {
                    createCaptureRequest.addTarget(previewSurface);
                }
            }
        }
        if (C0122O00000o.instance().OOo0oOO()) {
            Surface tuningRemoteSurface = this.mMiCamera.getTuningRemoteSurface();
            if (tuningRemoteSurface != null) {
                Log.d(str, "add tuning surface to capture request, size is: %s", SurfaceUtils.getSurfaceSize(tuningRemoteSurface));
                createCaptureRequest.addTarget(tuningRemoteSurface);
            }
        }
        createCaptureRequest.set(CaptureRequest.CONTROL_AE_MODE, Integer.valueOf(1));
        this.mMiCamera.applySettingsForCapture(createCaptureRequest, 3);
        if (this.mAlgoType != 1) {
            if (ModuleManager.isSuperMoonMode()) {
                boolean z2 = this.mAlgoType != 3;
                StringBuilder sb4 = new StringBuilder();
                sb4.append(z2 ? "enable" : "disable");
                sb4.append(" ZSL for SuperMoonMode");
                Log.d(str, sb4.toString());
                CompatibilityUtils.setZsl(createCaptureRequest, z2);
            } else if (!C0124O00000oO.isMTKPlatform()) {
                if (this.mAlgoType != 7 || !C0122O00000o.instance().OOOo00O()) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("disable ZSL for algo ");
                    sb5.append(this.mAlgoType);
                    sb = sb5.toString();
                    Log.d(str, sb);
                    CompatibilityUtils.setZsl(createCaptureRequest, false);
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("enable ZSL for algo ");
                    sb6.append(this.mAlgoType);
                    sb2 = sb6.toString();
                }
            }
            CaptureRequestBuilder.applyFlawDetectEnable(this.mMiCamera.getCapabilities(), createCaptureRequest, this.mMiCamera.getCameraConfigs().isFlawDetectEnable());
            if (this.mUseParallelVtCam) {
            }
            return createCaptureRequest;
        } else if (this.mIsHdrBokeh) {
            sb2 = "enable ZSL for HDR";
        } else {
            sb = "disable ZSL for HDR";
            Log.d(str, sb);
            CompatibilityUtils.setZsl(createCaptureRequest, false);
            CaptureRequestBuilder.applyFlawDetectEnable(this.mMiCamera.getCapabilities(), createCaptureRequest, this.mMiCamera.getCameraConfigs().isFlawDetectEnable());
            if (this.mUseParallelVtCam) {
                if (this.mMiCamera.getCameraConfigs().isLLSEnabled()) {
                    MiCameraCompat.applyLLS(createCaptureRequest, 1);
                } else {
                    MiCameraCompat.applyLLS(createCaptureRequest, 0);
                }
            }
            return createCaptureRequest;
        }
        Log.d(str, sb2);
        CompatibilityUtils.setZsl(createCaptureRequest, true);
        CaptureRequestBuilder.applyFlawDetectEnable(this.mMiCamera.getCapabilities(), createCaptureRequest, this.mMiCamera.getCameraConfigs().isFlawDetectEnable());
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
        this.mFirstNum = true;
        CameraConfigs cameraConfigs = this.mMiCamera.getCameraConfigs();
        this.mShouldDoSR = cameraConfigs.isSuperResolutionEnabled();
        this.mPreviewSize = cameraConfigs.getAlgorithmPreviewSize();
        boolean isHDREnabled = cameraConfigs.isHDREnabled();
        String str = TAG;
        if (isHDREnabled) {
            this.mIsHdrBokeh = cameraConfigs.isDualBokehEnabled();
            MiCamera2 miCamera2 = this.mMiCamera;
            this.mSingleCaptureForHDRplusMFNR = miCamera2.useSingleCaptureForHdrPlusMfnr(miCamera2.getCapabilities());
            StringBuilder sb = new StringBuilder();
            sb.append("singleFrameHDR = ");
            sb.append(this.mSingleCaptureForHDRplusMFNR);
            Log.d(str, sb.toString());
            if (cameraConfigs.getHDRStatus().isSuperResolutionHDR()) {
                Log.d(str, "prepare: HdrSR");
                this.mAlgoType = 3;
                this.mIsHdrSR = true;
                prepareSR(true);
            } else {
                this.mAlgoType = 1;
                prepareHdr();
            }
        } else if (C0122O00000o.instance().OOo0o00() && cameraConfigs.isDualBokehEnabled()) {
            this.mAlgoType = 9;
            prepareLowLightBokeh();
        } else if (CameraSettings.isGroupShotOn()) {
            this.mAlgoType = 5;
            prepareGroupShot();
        } else if (this.mShouldDoSR) {
            this.mAlgoType = 3;
            prepareSR(false);
        } else if (this.mOperationMode == 32778) {
            this.mAlgoType = this.mMiCamera.getRawCallbackType() == 8 ? 12 : 10;
            prepareSuperNight();
        } else {
            CaptureResult captureResult = this.mPreviewCaptureResult;
            Integer num = captureResult == null ? null : (Integer) captureResult.get(CaptureResult.SENSOR_SENSITIVITY);
            boolean isMfnrEnabled = this.mMiCamera.getCameraConfigs().isMfnrEnabled();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("prepare: iso = ");
            sb2.append(num);
            sb2.append(" isHwMFNREnabled = ");
            sb2.append(isMfnrEnabled);
            Log.d(str, sb2.toString());
            if (C0124O00000oO.OOooO() || C0124O00000oO.OOoo0o() || C0122O00000o.instance().OOoOOoO()) {
                this.mShouldDoMFNR = true;
            } else {
                boolean z = num != null && num.intValue() >= 800;
                this.mShouldDoMFNR = z;
            }
            if (!this.mShouldDoMFNR || (ModuleManager.isSuperMoonMode() && isMfnrEnabled)) {
                this.mAlgoType = 0;
                this.mSequenceNum = 1;
                this.mMultiFrameNum = 1;
            } else if ((!C0124O00000oO.OOooO() || !CameraSettings.isFrontCamera()) && ((C0124O00000oO.OOoo0o() && CameraSettings.isBackCamera()) || C0122O00000o.instance().OOoOOoO())) {
                this.mAlgoType = 7;
                prepareHHT();
            } else {
                this.mAlgoType = 2;
                prepareClearShot(num.intValue());
            }
        }
        boolean z2 = isSatFusionShotEnabled() && this.mAlgoType == 3;
        this.mIsSatFusionShotEnabled = z2;
        this.mAnchorFrame = doAnchorFrameAsThumbnail();
        this.mNeedDoAnchorFrame = this.mAnchorFrame;
        Log.d(str, String.format(Locale.ENGLISH, "prepare: algo=%d captureNum=%d doMFNR=%b doSR=%b anchor=%b mUseParallelVtCam=%b", new Object[]{Integer.valueOf(this.mAlgoType), Integer.valueOf(this.mSequenceNum), Boolean.valueOf(this.mShouldDoMFNR), Boolean.valueOf(this.mShouldDoSR), Boolean.valueOf(this.mAnchorFrame), Boolean.valueOf(this.mUseParallelVtCam)}));
    }

    /* access modifiers changed from: protected */
    public void startSessionCapture() {
        String str;
        CameraCaptureSession captureSession;
        Handler handler;
        Rect rect;
        String str2 = TAG;
        try {
            CaptureCallback generateCaptureCallback = generateCaptureCallback();
            Builder generateRequestBuilder = generateRequestBuilder();
            ArrayList arrayList = new ArrayList();
            StringBuilder sb = new StringBuilder();
            sb.append("startSessionCapture mSequenceNum:");
            sb.append(this.mSequenceNum);
            Log.d(str2, sb.toString());
            for (int i = 0; i < this.mSequenceNum; i++) {
                if (C0124O00000oO.isMTKPlatform()) {
                    if (this.mMiCamera.getCapabilities().getCameraId() == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                        MiCameraCompat.copyFpcDataFromCaptureResultToRequest(this.mPreviewCaptureResult, generateRequestBuilder);
                    }
                    if (!isIn3OrMoreSatMode()) {
                        if (!isInMultiSurfaceSatMode()) {
                            if (this.mMiCamera.getCapabilities().getCameraId() == Camera2DataContainer.getInstance().getUltraWideCameraId() || this.mAlgoType == 3) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("uw/sr set crop = ");
                                sb2.append(this.mActiveArraySize);
                                Log.d(str2, sb2.toString());
                                generateRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, this.mActiveArraySize);
                                rect = HybridZoomingSystem.toCropRegion(this.mMiCamera.getZoomRatio(), this.mActiveArraySize);
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("uw/sr set mtkCrop = ");
                                sb3.append(rect);
                                Log.d(str2, sb3.toString());
                                MiCameraCompat.applyPostProcessCropRegion(generateRequestBuilder, rect);
                            }
                        }
                    }
                    Rect[] rectArr = (Rect[]) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.MI_STATISTICS_FACE_RECTANGLES);
                    if (rectArr != null) {
                        Log.d(str2, "set mtk face");
                        MiCameraCompat.applyFaceRectangles(generateRequestBuilder, rectArr);
                    } else {
                        Log.d(str2, "get mtk face = null");
                    }
                    MiCameraCompat.applyNotificationTrigger(generateRequestBuilder, true);
                    rect = (Rect) VendorTagHelper.getValueSafely(this.mPreviewCaptureResult, CaptureResultVendorTags.POST_PROCESS_CROP_REGION);
                    if (rect != null) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("sat set mtkCrop = ");
                        sb4.append(rect);
                        Log.d(str2, sb4.toString());
                        MiCameraCompat.applyPostProcessCropRegion(generateRequestBuilder, rect);
                    } else {
                        Log.d(str2, "sat get mtkCrop = null");
                    }
                }
                if (C0122O00000o.instance().OOo000o()) {
                    MiCameraCompat.applyAlgoUpEnabled(generateRequestBuilder, true);
                }
                applyAlgoParameter(generateRequestBuilder, i, this.mAlgoType);
                arrayList.add(generateRequestBuilder.build());
                if (C0122O00000o.instance().OOo000o() && this.mMiCamera.getCameraConfigs().isHDREnabled()) {
                    Builder generateRequestBuilder2 = generateRequestBuilder();
                    MiCameraCompat.applyAlgoUpEnabled(generateRequestBuilder2, true);
                    applyAlgoParameter(generateRequestBuilder2, i, this.mAlgoType);
                    arrayList.add(generateRequestBuilder2.build());
                }
            }
            StringBuilder sb5 = new StringBuilder();
            sb5.append("startSessionCapture request number:");
            sb5.append(arrayList.size());
            Log.d(str2, sb5.toString());
            if (this.mUseParallelVtCam) {
                captureSession = ParallelSnapshotManager.getInstance().getCaptureSession();
                handler = this.mCameraHandler;
            } else {
                captureSession = this.mMiCamera.getCaptureSession();
                handler = this.mCameraHandler;
            }
            captureSession.captureBurst(arrayList, generateCaptureCallback, handler);
            MemoryHelper.addCapturedNumber(this.mMiCamera.hashCode(), this.mSequenceNum);
        } catch (CameraAccessException e) {
            Log.e(str2, "Failed to captureBurst, CameraAccessException", (Throwable) e);
            this.mMiCamera.notifyOnError(e.getReason());
        } catch (IllegalStateException e2) {
            e = e2;
            str = "Failed to captureBurst, IllegalState";
            Log.e(str2, str, e);
            this.mMiCamera.notifyOnError(256);
        } catch (IllegalArgumentException e3) {
            e = e3;
            str = "Failed to captureBurst, IllegalArgument";
            Log.e(str2, str, e);
            this.mMiCamera.notifyOnError(256);
        }
    }
}
