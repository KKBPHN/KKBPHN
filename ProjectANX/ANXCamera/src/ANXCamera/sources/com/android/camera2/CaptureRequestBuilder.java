package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.params.MeteringRectangle;
import android.os.Build.VERSION;
import android.util.Range;
import android.util.Rational;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.Util;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.VendorTagHelper;

public class CaptureRequestBuilder {
    private static final long MAX_REALTIME_EXPOSURE_TIME = 125000000;
    private static final String TAG = "CaptureRequestBuilder";

    static void applyAELock(Builder builder, boolean z) {
        if (builder != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAELock: ");
            sb.append(z);
            Log.d(str, sb.toString());
            builder.set(CaptureRequest.CONTROL_AE_LOCK, Boolean.valueOf(z));
        }
    }

    static void applyAERegions(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MeteringRectangle[] aERegions = cameraConfigs.getAERegions();
            if (aERegions != null) {
                builder.set(CaptureRequest.CONTROL_AE_REGIONS, aERegions);
            } else {
                builder.set(CaptureRequest.CONTROL_AE_REGIONS, MiCamera2.ZERO_WEIGHT_3A_REGION);
            }
        }
    }

    static void applyAFRegions(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MeteringRectangle[] aFRegions = cameraConfigs.getAFRegions();
            if (aFRegions != null) {
                builder.set(CaptureRequest.CONTROL_AF_REGIONS, aFRegions);
            } else {
                builder.set(CaptureRequest.CONTROL_AF_REGIONS, MiCamera2.ZERO_WEIGHT_3A_REGION);
            }
        }
    }

    public static void applyASDEnable(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.ASD_ENABLE.getName())) {
            MiCameraCompat.applyASDEnable(builder, cameraConfigs.isASDEnabled());
        }
    }

    public static void applyASDScene(CameraCapabilities cameraCapabilities, Builder builder, CameraConfigs cameraConfigs) {
        if (!(builder == null || cameraConfigs == null)) {
            if (!cameraCapabilities.isASDSceneSupported()) {
                Log.d(TAG, "applyASDScene(): unsupported");
                return;
            }
            int aSDScene = cameraConfigs.getASDScene();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyASDScene: ");
            sb.append(aSDScene);
            Log.d(str, sb.toString());
            MiCameraCompat.applyASDScene(builder, aSDScene);
        }
    }

    static void applyAWBLock(Builder builder, boolean z) {
        if (builder != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAWBLock: ");
            sb.append(z);
            Log.d(str, sb.toString());
            builder.set(CaptureRequest.CONTROL_AWB_LOCK, Boolean.valueOf(z));
        }
    }

    static void applyAWBMode(Builder builder, int i) {
        if (builder != null) {
            builder.set(CaptureRequest.CONTROL_AWB_MODE, Integer.valueOf(i));
        }
    }

    static void applyAiAIIEPreviewEnable(CameraCapabilities cameraCapabilities, Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            if (cameraCapabilities == null || !cameraCapabilities.isTagDefined(CaptureRequestVendorTags.AI_AIIE_PREVIEWENABLED.getName())) {
                Log.e(TAG, "is tag defined:false");
            } else {
                MiCameraCompat.applyAiAIIEPreviewEnable(builder, cameraConfigs.isAIIEPreviewEnabled());
            }
        }
    }

    static void applyAiASDEnable(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            boolean isAiASDEnabled = cameraConfigs.isAiASDEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAiASDEnable:");
            sb.append(isAiASDEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyAiASDEnable(builder, isAiASDEnabled);
        }
    }

    static void applyAiMoonEffectEnable(CameraCapabilities cameraCapabilities, Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            if (!cameraCapabilities.isAiMoonEffectEnableSupported()) {
                Log.e(TAG, "applyAiMoonEffectEnable: is not Support");
                return;
            }
            boolean isAiMoonEffectEnabled = cameraConfigs.isAiMoonEffectEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAiMoonEffectEnable:");
            sb.append(isAiMoonEffectEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyAiMoonEffectEnable(builder, isAiMoonEffectEnabled);
        }
    }

    static void applyAiSceneDetectPeriod(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MiCameraCompat.applyAiScenePeriod(builder, cameraConfigs.getAiSceneDetectPeriod());
        }
    }

    public static void applyAiShutterEnable(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportAiShutter()) {
            boolean isAiShutterEnable = cameraConfigs.isAiShutterEnable();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAiShutterEnable: ");
            sb.append(isAiShutterEnable);
            Log.d(str, sb.toString());
            MiCameraCompat.applyAiShutterEnable(builder, isAiShutterEnable);
        }
    }

    public static void applyAiShutterExistMotion(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && i == 3 && cameraCapabilities.isSupportAiShutter()) {
            boolean isAiShutterEnable = cameraConfigs.isAiShutterEnable();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAiShutterExistMotion.isAiShutterEnable: ");
            sb.append(isAiShutterEnable);
            Log.d(str, sb.toString());
            if (isAiShutterEnable) {
                boolean isAiShutterExistMotion = cameraConfigs.isAiShutterExistMotion();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applyAiShutterExistMotion.isAiShutterExistMotion: ");
                sb2.append(isAiShutterExistMotion);
                Log.d(str2, sb2.toString());
                MiCameraCompat.applyAiShutterExistMotion(builder, isAiShutterExistMotion);
            }
        }
    }

    public static void applyAmbilightAeTarget(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.AMBILIGHT_AE_TARGET.getName()) && cameraConfigs.getAmbilightMode() != 0) {
            int i = cameraConfigs.getmAmbilightAeTarget();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAmbilightAeTarget: ");
            sb.append(i);
            Log.d(str, sb.toString());
            MiCameraCompat.applyAmbilightAeTarget(builder, i);
        }
    }

    public static void applyAmbilightMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.AMBILIGHT_MODE.getName())) {
            int ambilightMode = cameraConfigs.getAmbilightMode();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyAmbilightMode: ");
            sb.append(ambilightMode);
            Log.d(str, sb.toString());
            MiCameraCompat.applyAmbilightMode(builder, ambilightMode);
        }
    }

    static void applyAntiBanding(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int antiBanding = cameraConfigs.getAntiBanding();
            if (antiBanding != -1) {
                builder.set(CaptureRequest.CONTROL_AE_ANTIBANDING_MODE, Integer.valueOf(antiBanding));
            }
        }
    }

    static void applyAntiShake(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            boolean isEISEnabled = cameraConfigs.isEISEnabled();
            boolean isOISEnabled = cameraConfigs.isOISEnabled();
            if (!isEISEnabled || !isOISEnabled || !Util.isDebugOsBuild()) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("EIS: ");
                String str2 = "on";
                String str3 = "off";
                sb.append(isEISEnabled ? str2 : str3);
                Log.v(str, sb.toString());
                builder.set(CaptureRequest.CONTROL_VIDEO_STABILIZATION_MODE, Integer.valueOf(isEISEnabled ? 1 : 0));
                if (cameraCapabilities.isSupportOIS()) {
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("OIS: ");
                    if (isEISEnabled || !isOISEnabled) {
                        str2 = str3;
                    }
                    sb2.append(str2);
                    Log.v(str4, sb2.toString());
                    Key key = CaptureRequest.LENS_OPTICAL_STABILIZATION_MODE;
                    int i = (isEISEnabled || !isOISEnabled) ? 0 : 1;
                    builder.set(key, Integer.valueOf(i));
                }
                return;
            }
            throw new RuntimeException("EIS&OIS are both on");
        }
    }

    public static void applyAsdDirtyEnable(CameraCapabilities cameraCapabilities, Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.ASD_DIRTY_ENABLE.getName())) {
            MiCameraCompat.applyAsdDirtyEnable(builder, cameraConfigs.isAsdDirtyEnable());
        }
    }

    static void applyAutoZoomMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MiCameraCompat.applyAutoZoomMode(builder, cameraConfigs.getAutoZoomMode());
        }
    }

    static void applyAutoZoomScaleOffset(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MiCameraCompat.applyAutoZoomScaleOffset(builder, cameraConfigs.getAutoZoomScaleOffset());
        }
    }

    public static void applyBackSoftLight(CameraCapabilities cameraCapabilities, Builder builder, boolean z) {
        if (builder != null) {
            if (!cameraCapabilities.isBackSoftLightSupported()) {
                Log.d(TAG, "applyBackSoftLight(): unsupported");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyBackSoftLight(): ");
            sb.append(z);
            Log.d(str, sb.toString());
            MiCameraCompat.applyBackSoftLight(builder, z ? (byte) 1 : 0);
        }
    }

    public static void applyBackwardCaptureHint(CameraCapabilities cameraCapabilities, Builder builder, boolean z) {
        if (builder != null) {
            if (!cameraCapabilities.isBackwardCaptureSupported()) {
                Log.d(TAG, "applyBackwardCaptureHint(): unsupported");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyBackwardCaptureHint(): ");
            sb.append(z);
            Log.d(str, sb.toString());
            MiCameraCompat.applyBackwardCaptureHint(builder, z ? (byte) 1 : 0);
        }
    }

    static void applyBeautyValues(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportBeauty() && cameraConfigs.getBeautyValues() != null) {
            MiCameraCompat.applyBeautyParameter(builder, cameraCapabilities.getCaptureRequestVendorKeys(), cameraConfigs.getBeautyValues());
        }
    }

    static void applyCameraAi30Enable(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportCameraAi30()) {
            MiCameraCompat.applyCameraAi30Enable(builder, cameraConfigs.isCameraAi30Enabled());
        }
    }

    static void applyCinematicPhoto(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        applyCinematicPhoto(builder, i, cameraCapabilities, cameraConfigs.isCinematicPhotoEnabled());
    }

    static void applyCinematicPhoto(Builder builder, int i, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder != null && cameraCapabilities.isCinematicPhotoSupported() && i == 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyCinematicPhoto: ");
            sb.append(z);
            Log.d(str, sb.toString());
            MiCameraCompat.applyCinematicPhoto(builder, z ? (byte) 1 : 0);
        }
    }

    public static void applyCinematicVideo(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isCinematicVideoSupported()) {
            boolean isCinematicVideoEnabled = cameraConfigs.isCinematicVideoEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyCinematicVideo: ");
            sb.append(isCinematicVideoEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyCinematicVideo(builder, isCinematicVideoEnabled ? (byte) 1 : 0);
        }
    }

    static void applyColorEffect(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int colorEffect = cameraConfigs.getColorEffect();
            if (colorEffect != -1) {
                builder.set(CaptureRequest.CONTROL_EFFECT_MODE, Integer.valueOf(colorEffect));
            }
        }
    }

    public static void applyColorEnhance(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.COLOR_ENHANCE_ENABLED.getName())) {
            MiCameraCompat.applyColorEnhanceEnable(builder, cameraConfigs.getColorEnhanceEnabled());
        }
    }

    static void applyColorRetentionBack(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.getFacing() != 0 && cameraCapabilities.isSupportColorRetentionBackRequestTag()) {
            MiCameraCompat.applyVideoFilterColorRetentionBack(builder, cameraConfigs.getVideoFilterColorRetentionBack());
        }
    }

    static void applyColorRetentionFront(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.getFacing() == 0 && cameraCapabilities.isSupportColorRetentionFrontRequestTag()) {
            MiCameraCompat.applyVideoFilterColorRetentionFront(builder, cameraConfigs.getVideoFilterColorRetentionFront());
        }
    }

    static void applyContrast(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportContrast()) {
            int contrastLevel = cameraConfigs.getContrastLevel();
            if (contrastLevel != -1) {
                MiCameraCompat.applyContrast(builder, contrastLevel);
            }
        }
    }

    static void applyCustomAWB(Builder builder, int i) {
        MiCameraCompat.applyCustomAWB(builder, i);
    }

    static void applyDepurpleEnable(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            boolean isDodepurpleEnabled = cameraConfigs.isDodepurpleEnabled();
            if (i == 4) {
                isDodepurpleEnabled = false;
            }
            if (!cameraCapabilities.isSupportDepurple()) {
                Log.d(TAG, "applyDepurpleEnable: is not Support  ");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyDepurpleEnable: dodepurpleEnabled = ");
            sb.append(isDodepurpleEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyDepurpleEnable(builder, isDodepurpleEnabled);
        }
    }

    static void applyDeviceOrientation(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportDeviceOrientation()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyDeviceOrientation: ");
            sb.append(cameraConfigs.getDeviceOrientation());
            Log.d(str, sb.toString());
            MiCameraCompat.applyDeviceOrientation(builder, cameraConfigs.getDeviceOrientation());
        }
    }

    static void applyDualBokeh(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportDualBokeh()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyDualBokeh: ");
            sb.append(cameraConfigs.isDualBokehEnabled());
            Log.d(str, sb.toString());
            MiCameraCompat.applyDualBokehEnable(builder, cameraConfigs.isDualBokehEnabled());
        }
    }

    static void applyExposureCompensation(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int exposureCompensationIndex = cameraConfigs.getExposureCompensationIndex();
            if (C0124O00000oO.o00OO00() && ((ModuleManager.isProModule() || ModuleManager.isFastmotionModulePro()) && ((i == 1 || ModuleManager.isFastmotionModulePro()) && cameraConfigs.getISO() == 0 && cameraConfigs.getExposureTime() > MAX_REALTIME_EXPOSURE_TIME))) {
                double log = Math.log((double) ((float) (((double) cameraConfigs.getExposureTime()) / 1.25E8d))) / Math.log(2.0d);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("applyExposureCompensation: EV = ");
                sb.append(log);
                Log.d(str, sb.toString());
                Rational exposureCompensationRational = cameraCapabilities.getExposureCompensationRational();
                exposureCompensationIndex = Math.min((int) ((log * ((double) exposureCompensationRational.getDenominator())) / ((double) exposureCompensationRational.getNumerator())), cameraCapabilities.getMaxExposureCompensation());
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("applyExposureCompensation: ");
            sb2.append(exposureCompensationIndex);
            Log.d(str2, sb2.toString());
            builder.set(CaptureRequest.CONTROL_AE_EXPOSURE_COMPENSATION, Integer.valueOf(exposureCompensationIndex));
        }
    }

    static void applyExposureMeteringMode(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int exposureMeteringMode = cameraConfigs.getExposureMeteringMode();
            if (exposureMeteringMode != -1) {
                MiCameraCompat.applyExposureMeteringMode(builder, exposureMeteringMode);
            }
        }
    }

    static void applyExposureTime(Builder builder, int i, CameraConfigs cameraConfigs) {
        if (builder != null) {
            long exposureTime = cameraConfigs.getExposureTime();
            if (C0124O00000oO.o00OO00() && (i == 1 || ModuleManager.isFastmotionModulePro())) {
                exposureTime = Math.min(exposureTime, MAX_REALTIME_EXPOSURE_TIME);
            }
            if (C0124O00000oO.o00OO00() || i == 3) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("applyExposureTime: ");
                sb.append(exposureTime);
                Log.d(str, sb.toString());
                MiCameraCompat.applyExposureTime(builder, exposureTime);
            }
        }
    }

    public static void applyExtendSceneMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int extendSceneMode = cameraConfigs.getExtendSceneMode();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyExtendSceneMode: ");
            sb.append(extendSceneMode);
            Log.d(str, sb.toString());
            CompatibilityUtils.applyExtendSceneMode(builder, extendSceneMode);
            throw null;
        }
    }

    static void applyEyeLight(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportEyeLight()) {
            int eyeLightType = cameraConfigs.getEyeLightType();
            if (eyeLightType < 0) {
                MiCameraCompat.applyEyeLight(builder, 0, 0);
            } else {
                MiCameraCompat.applyEyeLight(builder, eyeLightType, 100);
            }
        }
    }

    static void applyFNumber(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            if (!cameraCapabilities.isSupportBokehAdjust()) {
                Log.d(TAG, "set f number on unsupported devices");
            } else if (cameraConfigs.getFNumber() != null) {
                MiCameraCompat.applyFNumber(builder, cameraConfigs.getFNumber());
            }
        }
    }

    static void applyFaceAgeAnalyze(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportFaceAgeAnalyze()) {
            MiCameraCompat.applyFaceAgeAnalyzeEnable(builder, cameraConfigs.isFaceAgeAnalyzeEnabled());
        }
    }

    static void applyFaceDetection(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            MiCameraCompat.applyFaceDetection(builder, cameraConfigs.isFaceDetectionEnabled());
        }
    }

    static void applyFaceScore(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportFaceScore()) {
            MiCameraCompat.applyFaceScoreEnable(builder, cameraConfigs.isFaceScoreEnabled());
        }
    }

    public static void applyFlashCurrent(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.FLASH_CURRENT.getName())) {
            MiCameraCompat.applyFlashCurrent(builder, cameraConfigs.getFlashCurrent());
        }
    }

    public static void applyFlashMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (C0122O00000o.instance().OOoO0o0() && builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.FLASH_MODE.getName())) {
            MiCameraCompat.applyFlashMode(builder, cameraConfigs.getFlashMode());
        }
    }

    public static void applyFlawDetectEnable(CameraCapabilities cameraCapabilities, Builder builder, boolean z) {
        if (builder != null && cameraCapabilities != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.FLAW_DETECT_ENABLE.getName())) {
            MiCameraCompat.applyFlawDetectEnable(builder, z);
        }
    }

    static void applyFocusDistance(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs.getFocusMode() == 0) {
            float focusDistance = cameraConfigs.getFocusDistance();
            if (focusDistance > -1.0f) {
                builder.set(CaptureRequest.LENS_FOCUS_DISTANCE, Float.valueOf(focusDistance));
            }
        }
    }

    static void applyFocusMode(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            builder.set(CaptureRequest.CONTROL_AF_MODE, Integer.valueOf(cameraConfigs.getFocusMode()));
            applyAFRegions(builder, cameraConfigs);
            applyAERegions(builder, cameraConfigs);
        }
    }

    static void applyFpsRange(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            Range previewFpsRange = cameraConfigs.getPreviewFpsRange();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyFpsRange: fpsRange = ");
            sb.append(previewFpsRange);
            Log.d(str, sb.toString());
            if (previewFpsRange != null) {
                builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, previewFpsRange);
            }
        }
    }

    static void applyFrontMirror(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportFrontMirror() && i == 3) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyFrontMirror: ");
            sb.append(cameraConfigs.isFrontMirror());
            Log.d(str, sb.toString());
            MiCameraCompat.applyFrontMirror(builder, cameraConfigs.isFrontMirror());
        }
    }

    static void applyHDR(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        boolean isHDREnabled;
        if (builder != null && cameraCapabilities.isSupportHdr()) {
            if (i != 3) {
                isHDREnabled = false;
            } else {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("applyHDR:");
                sb.append(cameraConfigs.isHDREnabled());
                Log.d(str, sb.toString());
                isHDREnabled = cameraConfigs.isHDREnabled();
            }
            MiCameraCompat.applyHDR(builder, isHDREnabled);
        }
    }

    public static void applyHDR10Video(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.HDR10_VIDEO.getName())) {
            MiCameraCompat.applyHDR10Video(builder, cameraConfigs.getHDR10Video());
        }
    }

    static void applyHDRCheckerEnable(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && i == 1 && cameraCapabilities.isSupportAutoHdr()) {
            MiCameraCompat.applyHDRCheckerEnable(builder, cameraConfigs.isHDRCheckerEnabled());
        }
    }

    static void applyHDRCheckerStatus(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && i == 1 && cameraCapabilities.isSupportHdrCheckerStatus()) {
            MiCameraCompat.applyHDRCheckerStatus(builder, cameraConfigs.getHDRCheckerStatus());
        }
    }

    static void applyHDRMode(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && i == 1 && cameraCapabilities.isSupportHdrMode()) {
            MiCameraCompat.applyHDRMode(builder, cameraConfigs.getHDRMode());
        }
    }

    public static void applyHFRDeflicker(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraCapabilities.isSupportHFRDeflicker()) {
            boolean isHFRDeflicker = cameraConfigs.isHFRDeflicker();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyHFRDeflicker: ");
            sb.append(isHFRDeflicker);
            Log.d(str, sb.toString());
            MiCameraCompat.applyHFRDeflicker(builder, isHFRDeflicker);
        }
    }

    static void applyHHT(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && i == 3 && cameraCapabilities.isSupportHHT()) {
            MiCameraCompat.applyHHT(builder, cameraConfigs.isHHTEnabled());
        }
    }

    public static void applyHighQualityPreferred(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportHighQualityPreferred()) {
            boolean isHighQualityPreferred = cameraConfigs.isHighQualityPreferred();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyHighQualityPreferred: ");
            sb.append(isHighQualityPreferred);
            Log.d(str, sb.toString());
            MiCameraCompat.applyHighQualityPreferred(builder, isHighQualityPreferred);
        }
    }

    public static void applyHistogramStats(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isHistogramStatsSupported()) {
            boolean isHistogramStatsEnabled = cameraConfigs.isHistogramStatsEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyHistogramStats: ");
            sb.append(isHistogramStatsEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyHistogramStats(builder, isHistogramStatsEnabled ? (byte) 1 : 0);
        }
    }

    static void applyHwMfnr(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder == null || !cameraCapabilities.isSupportMfnr()) {
            return;
        }
        if (i != 3) {
            MiCameraCompat.applyMfnrEnable(builder, false);
            return;
        }
        MiCameraCompat.applyMfnrEnable(builder, cameraConfigs.isMfnrEnabled());
        if (cameraConfigs.isMfnrEnabled() && !CameraSettings.isHighQualityPreferred()) {
            int O0oooOO = C0122O00000o.instance().O0oooOO();
            if (O0oooOO > 0) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("applyMfnrFrameNum: ");
                sb.append(O0oooOO);
                Log.d(str, sb.toString());
                MiCameraCompat.applyMfnrFrameNum(builder, O0oooOO);
            }
        }
    }

    static void applyIso(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int iso = cameraConfigs.getISO();
            if (C0124O00000oO.o00OO00() && ((i == 1 || ModuleManager.isFastmotionModulePro()) && iso > 0 && cameraConfigs.getExposureTime() > MAX_REALTIME_EXPOSURE_TIME)) {
                iso = Math.min((int) (((float) iso) * ((float) (((double) cameraConfigs.getExposureTime()) / 1.25E8d))), cameraCapabilities.getMaxIso());
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyIso: ");
            sb.append(iso);
            Log.d(str, sb.toString());
            MiCameraCompat.applyISO(builder, iso);
        }
    }

    static void applyLensDirtyDetect(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportLensDirtyDetect()) {
            MiCameraCompat.applyLensDirtyDetect(builder, cameraConfigs.isLensDirtyDetectEnabled());
        }
    }

    public static void applyMacroMode(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraCapabilities.isSupportMacroMode()) {
            boolean isMacroMode = cameraConfigs.isMacroMode();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyMacroMode: ");
            sb.append(isMacroMode);
            Log.d(str, sb.toString());
            MiCameraCompat.applyMacroMode(builder, isMacroMode);
        }
    }

    public static void applyMtkPipDevices(Builder builder, CameraConfigs cameraConfigs) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MTK_STREAMING_FEATURE_PIP_DEVICES, cameraConfigs.getmMtkPipDevices());
    }

    private void applyNoiseReduction(Builder builder) {
        if (builder != null) {
            builder.set(CaptureRequest.NOISE_REDUCTION_MODE, Integer.valueOf(2));
        }
    }

    static void applyNormalWideLDC(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportNormalWideLDC()) {
            boolean normalWideLDCEnabled = cameraConfigs.getNormalWideLDCEnabled();
            if (i == 4) {
                normalWideLDCEnabled = false;
            }
            MiCameraCompat.applyNormalWideLDC(builder, normalWideLDCEnabled);
        }
    }

    public static void applyOnTripodModeStatus(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraConfigs.getOnTripodScenes() != null) {
            MiCameraCompat.applyOnTripodModeStatus(builder, cameraConfigs.getOnTripodScenes());
        }
    }

    public static void applyParallelSnapshot(Builder builder, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.IS_PARALLEL_SNAPSHOT.getName())) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyParallelSnapshot: ");
            sb.append(z);
            Log.d(str, sb.toString());
            MiCameraCompat.applyParallelSnapshot(builder, z);
        }
    }

    static void applyPortraitLighting(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            if ((i == 3 || C0122O00000o.instance().O0oo0OO()) && cameraCapabilities.isSupportPortraitLighting()) {
                MiCameraCompat.applyPortraitLighting(builder, cameraConfigs.getPortraitLightingPattern());
            }
        }
    }

    static void applySATUltraWideLDC(Builder builder, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder != null && cameraCapabilities.supportSATUltraWideLDCEnable()) {
            MiCameraCompat.applySATUltraWideLDC(builder, z);
        }
    }

    public static void applySatFallback(Builder builder, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.SAT_FALLBACK_ENABLE.getName())) {
            MiCameraCompat.applySatFallback(builder, z);
        }
    }

    public static void applySatFallbackDisable(Builder builder, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder == null) {
            Log.e(TAG, "applySatFallbackDisable: request null");
        } else if (!cameraCapabilities.isTagDefined(CaptureRequestVendorTags.SAT_FALLBACK_DISABLE.getName())) {
            Log.e(TAG, "applySatFallbackDisable: %s not defined", CaptureRequestVendorTags.SAT_FALLBACK_DISABLE.getName());
        } else {
            MiCameraCompat.applySatFallbackDisable(builder, z);
        }
    }

    public static void applySatIsZooming(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null) {
            MiCameraCompat.applySatIsZooming(builder, cameraConfigs.isSatIsZooming());
        }
    }

    static void applySaturation(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int saturationLevel = cameraConfigs.getSaturationLevel();
            if (saturationLevel != -1) {
                MiCameraCompat.applySaturation(builder, saturationLevel);
            }
        }
    }

    static void applySceneMode(Builder builder, CameraConfigs cameraConfigs) {
        Key key;
        int i;
        if (builder != null) {
            int sceneMode = cameraConfigs.getSceneMode();
            if (!"-1".equals(String.valueOf(sceneMode))) {
                builder.set(CaptureRequest.CONTROL_SCENE_MODE, Integer.valueOf(sceneMode));
                key = CaptureRequest.CONTROL_MODE;
                i = 2;
            } else {
                key = CaptureRequest.CONTROL_MODE;
                i = 1;
            }
            builder.set(key, Integer.valueOf(i));
        }
    }

    public static void applyScreenLightHint(CameraCapabilities cameraCapabilities, Builder builder, boolean z) {
        if (builder != null) {
            if (!cameraCapabilities.isScreenLightHintSupported()) {
                Log.d(TAG, "applyScreenLightHint(): unsupported");
                return;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyScreenLightHint(): ");
            sb.append(z);
            Log.d(str, sb.toString());
            MiCameraCompat.applyScreenLightHint(builder, z ? (byte) 1 : 0);
        }
    }

    public static void applySessionParameters(Builder builder, CaptureSessionConfigurations captureSessionConfigurations) {
        if (builder != null && captureSessionConfigurations != null) {
            captureSessionConfigurations.apply(builder);
        }
    }

    static void applySharpness(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            int sharpnessLevel = cameraConfigs.getSharpnessLevel();
            if (sharpnessLevel != -1) {
                MiCameraCompat.applySharpness(builder, sharpnessLevel);
            }
        }
    }

    public static void applyShrinkMemoryMode(Builder builder, CameraCapabilities cameraCapabilities, int i) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.SHRINK_MEMORY_MODE.getName())) {
            MiCameraCompat.applyShrinkMemoryMode(builder, i);
        }
    }

    static void applySingleBokeh(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportMiBokeh()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applySingleBokeh: ");
            sb.append(cameraConfigs.isSingleBokehEnabled());
            Log.d(str, sb.toString());
            MiCameraCompat.applySingleBokehEnable(builder, cameraConfigs.isSingleBokehEnabled());
        }
    }

    public static void applySmoothTransition(Builder builder, CameraCapabilities cameraCapabilities, boolean z) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.ST_ENABLED.getName())) {
            MiCameraCompat.applySmoothTransition(builder, z);
        }
    }

    static void applySuperNightScene(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraCapabilities.isSupportSuperNight()) {
            boolean isSuperNightEnabled = cameraConfigs.isSuperNightEnabled();
            if (cameraConfigs.getShotType() == 9 || i == 1 || cameraConfigs.isNeedFlash()) {
                isSuperNightEnabled = false;
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applySuperNightScene: ");
            sb.append(isSuperNightEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applySuperNightScene(builder, isSuperNightEnabled);
            if (cameraCapabilities.isDebugInfoAsWatermarkSupported()) {
                boolean isDebugInfoAsWatermarkEnabled = CameraSettings.isDebugInfoAsWatermarkEnabled();
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("show debug info as watermark: ");
                sb2.append(isDebugInfoAsWatermarkEnabled);
                Log.d(str2, sb2.toString());
                VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.DEBUG_INFO_AS_WATERMARK, Boolean.valueOf(isDebugInfoAsWatermarkEnabled));
            }
        }
    }

    static void applySuperResolution(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        String str;
        StringBuilder sb;
        if (builder != null && cameraCapabilities.isSupportSuperResolution()) {
            boolean isSuperResolutionEnabled = cameraConfigs.isSuperResolutionEnabled();
            String str2 = ", applyType = ";
            String str3 = "applySuperResolution: ";
            if (!C0124O00000oO.isMTKPlatform()) {
                boolean OO00ooo = C0122O00000o.instance().OO00ooo();
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("applySuperResolution: applySr2PreviewStream = ");
                sb2.append(OO00ooo);
                Log.d(str4, sb2.toString());
                if (!OO00ooo || ModuleManager.isProPhotoModule()) {
                    isSuperResolutionEnabled &= i == 3;
                }
                str = TAG;
                sb = new StringBuilder();
            } else if (i != 3) {
                String str5 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("applySuperResolution: ignored for applyType(");
                sb3.append(i);
                sb3.append(")");
                Log.d(str5, sb3.toString());
                return;
            } else {
                str = TAG;
                sb = new StringBuilder();
            }
            sb.append(str3);
            sb.append(isSuperResolutionEnabled);
            sb.append(str2);
            sb.append(i);
            Log.d(str, sb.toString());
            MiCameraCompat.applySuperResolution(builder, isSuperResolutionEnabled);
        }
    }

    static void applySwMfnr(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportSwMfnr()) {
            MiCameraCompat.applySwMfnrEnable(builder, i != 3 ? false : cameraConfigs.isSwMfnrEnabled());
        }
    }

    static void applyTargetZoom(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportTargetZoom()) {
            float targetZoom = cameraConfigs.getTargetZoom();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyTargetZoom(): ");
            sb.append(targetZoom);
            Log.v(str, sb.toString());
            MiCameraCompat.applyTargetZoom(builder, targetZoom);
        }
    }

    public static void applyThermal(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.THERMAL_LEVEL.getName())) {
            MiCameraCompat.applyThermalLevel(builder, Integer.valueOf(cameraConfigs.getThermalLevel()));
        }
    }

    public static void applyTuningMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.MI_TUNING_MODE.getName())) {
            byte turingMode = cameraConfigs.getTuringMode();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyTuningMode: ");
            sb.append(turingMode);
            Log.d(str, sb.toString());
            MiCameraCompat.applyTuningMode(builder, turingMode);
        }
    }

    public static void applyUltraPixelPortrait(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraConfigs != null && cameraCapabilities.isUltraPixelPortraitTagDefined()) {
            boolean isUltraPixelPortraitEnabled = cameraConfigs.isUltraPixelPortraitEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyUltraPixelPortrait: ");
            sb.append(isUltraPixelPortraitEnabled);
            Log.d(str, sb.toString());
            MiCameraCompat.applyUltraPixelPortrait(builder, isUltraPixelPortraitEnabled);
        }
    }

    static void applyUltraWideLDC(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportUltraWideLDC()) {
            MiCameraCompat.applyUltraWideLDC(builder, cameraConfigs.isUltraWideLDCEnabled());
        }
    }

    public static void applyVideoBokehColorRetentionMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs, boolean z) {
        if (builder != null && cameraCapabilities.isSupportVideoBokehColorRetentionTag(z)) {
            MiCameraCompat.applyVideoBokehColorRetentionMode(builder, cameraConfigs.getVideoBokehColorRetentionMode(z), z);
        }
    }

    static void applyVideoBokehLevelBack(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.getFacing() != 0 && cameraCapabilities.isSupportVideoBokehRequestTag(false)) {
            MiCameraCompat.applyVideoBokehBackLevel(builder, cameraConfigs.getVideoBokehLevelBack());
        }
    }

    static void applyVideoBokehLevelFront(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.getFacing() == 0 && cameraCapabilities.isSupportVideoBokehRequestTag(true)) {
            MiCameraCompat.applyVideoBokehFrontLevel(builder, cameraConfigs.getVideoBokehLevelFront());
        }
    }

    static void applyVideoFilterId(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportVideoFilterRequestTag()) {
            int videoFilterId = cameraConfigs.getVideoFilterId();
            if (videoFilterId != -1) {
                MiCameraCompat.applyVideoFilterId(builder, videoFilterId);
            }
        }
    }

    static void applyVideoFlash(Builder builder, CameraConfigs cameraConfigs) {
        Key key;
        Integer num;
        if (builder != null) {
            boolean z = 5 == cameraConfigs.getFlashMode();
            boolean z2 = 2 == cameraConfigs.getFlashMode() || z;
            if (z2) {
                if (z) {
                    MiCameraCompat.applyBackSoftLight(builder, 1);
                }
                key = CaptureRequest.FLASH_MODE;
                num = Integer.valueOf(2);
            } else {
                key = CaptureRequest.FLASH_MODE;
                num = Integer.valueOf(0);
            }
            builder.set(key, num);
        }
    }

    static void applyVideoFpsRange(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            Range videoFpsRange = cameraConfigs.getVideoFpsRange();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyVideoFpsRange: fpsRange = ");
            sb.append(videoFpsRange);
            Log.d(str, sb.toString());
            if (videoFpsRange != null) {
                builder.set(CaptureRequest.CONTROL_AE_TARGET_FPS_RANGE, videoFpsRange);
            }
        }
    }

    public static void applyVideoHdrMode(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isSupportVideoHdr()) {
            boolean isVideoHdrEnable = cameraConfigs.isVideoHdrEnable();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyVideoHdrMode: ");
            sb.append(isVideoHdrEnable);
            Log.d(str, sb.toString());
            if (C0124O00000oO.isMTKPlatform()) {
                MiCameraCompat.applyVideoHdrMode(builder, isVideoHdrEnable ? CaptureRequestVendorTags.MTK_HDR_FEATURE_HDR_MODE_VIDEO_ON : CaptureRequestVendorTags.MTK_HDR_FEATURE_HDR_MODE_OFF);
            } else {
                MiCameraCompat.applyVideoHdrMode(builder, isVideoHdrEnable);
            }
        }
    }

    public static void applyVideoLog(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null && cameraCapabilities.isTagDefined(CaptureRequestVendorTags.PRO_VIDEO_LOG_ENABLED.getName())) {
            MiCameraCompat.applyVideoLogEnable(builder, cameraConfigs.isVideoLogEnabled() ? (byte) 1 : 0);
        }
    }

    static void applyWaterMark(Builder builder, int i, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder == null || !cameraCapabilities.isSupportWatermark()) {
            return;
        }
        if (i != 3) {
            if (i == 4) {
                MiCameraCompat.applyWaterMarkAppliedList(builder, "");
            }
        } else if (!C0122O00000o.instance().OOo0ooO()) {
            String join = Util.join(",", cameraConfigs.getWaterMarkAppliedList());
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyWaterMark appliedList:");
            sb.append(join);
            Log.d(str, sb.toString());
            MiCameraCompat.applyWaterMarkAppliedList(builder, join);
            if (C0122O00000o.instance().OOoOo0O() && cameraCapabilities.isSupportCustomWatermark() && join.contains("device")) {
                String watermarkFileName = Util.getWatermarkFileName(cameraConfigs.isCinematicPhotoEnabled());
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Util.WATERMARK_STORAGE_DIRECTORY);
                sb2.append(watermarkFileName);
                MiCameraCompat.applyCustomWaterMark(builder, sb2.toString());
            }
            if (join.contains(WatermarkConstant.ITEM_TAG)) {
                MiCameraCompat.applyTimeWaterMark(builder, cameraConfigs.getTimeWaterMarkValue());
            }
            if (join.contains("beautify")) {
                MiCameraCompat.applyFaceWaterMark(builder, cameraConfigs.getFaceWaterMarkFormat());
            }
            cameraConfigs.setNewWatermark(false);
        }
    }

    static void applyZoomRatio(Builder builder, CameraCapabilities cameraCapabilities, CameraConfigs cameraConfigs) {
        if (builder != null) {
            applyTargetZoom(builder, cameraCapabilities, cameraConfigs);
            float zoomRatio = cameraConfigs != null ? cameraConfigs.getZoomRatio() : 1.0f;
            if (VERSION.SDK_INT < 30 || !cameraCapabilities.isZoomRatioSupported()) {
                Rect activeArraySize = cameraCapabilities.getActiveArraySize();
                Rect cropRegion = HybridZoomingSystem.toCropRegion(zoomRatio, activeArraySize);
                builder.set(CaptureRequest.SCALER_CROP_REGION, cropRegion);
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("applyZoomRatio(): cameraId = ");
                sb.append(cameraCapabilities.getCameraId());
                sb.append(", zoomRatio = ");
                sb.append(zoomRatio);
                sb.append(", activeArraySize = ");
                sb.append(activeArraySize);
                sb.append(", cropRegion = ");
                sb.append(cropRegion);
                Log.v(str, sb.toString());
                return;
            }
            CompatibilityUtils.applyZoomRatio(builder, zoomRatio);
            throw null;
        }
    }

    static void applyZsl(Builder builder, CameraConfigs cameraConfigs) {
        if (builder != null) {
            boolean isZslEnabled = cameraConfigs.isZslEnabled();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("applyZsl(): ");
            sb.append(isZslEnabled);
            Log.v(str, sb.toString());
            MiCameraCompat.applyZsl(builder, isZslEnabled);
        }
    }
}
