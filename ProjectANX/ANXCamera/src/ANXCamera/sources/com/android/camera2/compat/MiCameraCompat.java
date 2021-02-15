package com.android.camera2.compat;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureResult;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import java.util.HashSet;

public class MiCameraCompat {
    private static final MiCameraCompatBaseImpl IMPL;

    static {
        MiCameraCompatBaseImpl miCameraCompatBaseImpl = C0124O00000oO.Oo000OO() ? new MiCameraCompatQcomImpl() : C0124O00000oO.isMTKPlatform() ? new MiCameraCompatMtkImpl() : new MiCameraCompatBaseImpl();
        IMPL = miCameraCompatBaseImpl;
    }

    public static void applyASDEnable(Builder builder, boolean z) {
        IMPL.applyASDEnable(builder, z);
    }

    public static void applyASDScene(Builder builder, int i) {
        IMPL.applyASDScene(builder, i);
    }

    public static void applyAiAIIEPreviewEnable(Builder builder, boolean z) {
        IMPL.applyAiAIIEPreviewEnable(builder, z);
    }

    public static void applyAiASDEnable(Builder builder, boolean z) {
        IMPL.applyAiASDEnable(builder, z);
    }

    public static void applyAiMoonEffectEnable(Builder builder, boolean z) {
        IMPL.applyAiMoonEffectEnable(builder, z);
    }

    public static void applyAiScenePeriod(Builder builder, int i) {
        IMPL.applyAiScenePeriod(builder, i);
    }

    public static void applyAiShutterEnable(Builder builder, boolean z) {
        IMPL.applyAiShutterEnable(builder, z);
    }

    public static void applyAiShutterExistMotion(Builder builder, boolean z) {
        IMPL.applyAiShutterExistMotion(builder, z);
    }

    public static void applyAlgoUpEnabled(Builder builder, boolean z) {
        IMPL.applyAlgoUpEnabled(builder, z);
    }

    public static void applyAmbilightAeTarget(Builder builder, int i) {
        IMPL.applyAmbilightAeTarget(builder, i);
    }

    public static void applyAmbilightMode(Builder builder, int i) {
        IMPL.applyAmbilightMode(builder, i);
    }

    public static void applyAsdDirtyEnable(Builder builder, boolean z) {
        IMPL.applyAsdDirtyEnable(builder, z);
    }

    public static void applyAutoZoomMode(Builder builder, int i) {
        IMPL.applyAutoZoomMode(builder, i);
    }

    public static void applyAutoZoomScaleOffset(Builder builder, float f) {
        IMPL.applyAutoZoomScaleOffset(builder, f);
    }

    public static void applyBackSoftLight(Builder builder, byte b) {
        IMPL.applyBackSoftLight(builder, b);
    }

    public static void applyBackwardCaptureHint(Builder builder, byte b) {
        IMPL.applyBackwardCaptureHint(builder, b);
    }

    public static void applyBeautyParameter(Builder builder, HashSet hashSet, BeautyValues beautyValues) {
        IMPL.applyBeautyParameter(builder, hashSet, beautyValues);
    }

    public static void applyBurstFps(Builder builder, int i) {
        IMPL.applyBurstFps(builder, i);
    }

    public static void applyBurstHint(Builder builder, int i) {
        IMPL.applyBurstHint(builder, i);
    }

    public static void applyCShotFeatureCapture(Builder builder, boolean z) {
        IMPL.applyCShotFeatureCapture(builder, z);
    }

    public static void applyCameraAi30Enable(Builder builder, boolean z) {
        IMPL.applyCameraAi30Enable(builder, z);
    }

    public static void applyCinematicPhoto(Builder builder, byte b) {
        IMPL.applyCinematicPhoto(builder, b);
    }

    public static void applyCinematicVideo(Builder builder, byte b) {
        IMPL.applyCinematicVideo(builder, b);
    }

    public static void applyColorEnhanceEnable(Builder builder, boolean z) {
        IMPL.applyColorEnhanceEnable(builder, z);
    }

    public static void applyContrast(Builder builder, int i) {
        IMPL.applyContrast(builder, i);
    }

    public static void applyCropFeature(Builder builder, int[] iArr) {
        IMPL.applyCropFeature(builder, iArr);
    }

    public static void applyCustomAWB(Builder builder, int i) {
        IMPL.applyCustomWB(builder, i);
    }

    public static void applyCustomWaterMark(Builder builder, String str) {
        IMPL.applyCustomWaterMark(builder, str);
    }

    public static void applyDepurpleEnable(Builder builder, boolean z) {
        IMPL.applyDepurpleEnable(builder, z);
    }

    public static void applyDeviceOrientation(Builder builder, int i) {
        IMPL.applyDeviceOrientation(builder, i);
    }

    public static void applyDualBokehEnable(Builder builder, boolean z) {
        IMPL.applyDualBokeh(builder, z);
    }

    public static void applyExposureMeteringMode(Builder builder, int i) {
        IMPL.applyExposureMeteringMode(builder, i);
    }

    public static void applyExposureTime(Builder builder, long j) {
        IMPL.applyExposureTime(builder, j);
    }

    public static void applyEyeLight(Builder builder, int i, int i2) {
        IMPL.applyEyeLight(builder, i, i2);
    }

    public static void applyFNumber(Builder builder, String str) {
        IMPL.applyFNumber(builder, str);
    }

    public static void applyFaceAgeAnalyzeEnable(Builder builder, boolean z) {
        IMPL.applyFaceAnalyzeAge(builder, z);
    }

    public static void applyFaceDetection(Builder builder, boolean z) {
        IMPL.applyFaceDetection(builder, z);
    }

    public static void applyFaceRectangles(Builder builder, Rect[] rectArr) {
        IMPL.applyFaceRectangles(builder, rectArr);
    }

    public static void applyFaceScoreEnable(Builder builder, boolean z) {
        IMPL.applyFaceScore(builder, z);
    }

    public static void applyFaceWaterMark(Builder builder, String str) {
        IMPL.applyFaceWaterMark(builder, str);
    }

    public static void applyFeatureMode(Builder builder, int i) {
        IMPL.applyFeatureMode(builder, i);
    }

    public static void applyFlashCurrent(Builder builder, int i) {
        IMPL.applyFlashCurrent(builder, i);
    }

    public static void applyFlashMode(Builder builder, int i) {
        IMPL.applyFlashMode(builder, i);
    }

    public static void applyFlawDetectEnable(Builder builder, boolean z) {
        IMPL.applyFlawDetectEnable(builder, z);
    }

    public static void applyFrontMirror(Builder builder, boolean z) {
        IMPL.applyFrontMirror(builder, z);
    }

    public static void applyHDR(Builder builder, boolean z) {
        IMPL.applyHDR(builder, z);
    }

    public static void applyHDR10Video(Builder builder, int i) {
        IMPL.applyHDRVideoMode(builder, i);
    }

    public static void applyHDRCheckerEnable(Builder builder, boolean z) {
        IMPL.applyHDRCheckerEnable(builder, z);
    }

    public static void applyHDRCheckerStatus(Builder builder, int i) {
        IMPL.applyHDRCheckerStatus(builder, i);
    }

    public static void applyHDRMode(Builder builder, int i) {
        IMPL.applyHDRMode(builder, i);
    }

    public static void applyHFRDeflicker(Builder builder, boolean z) {
        IMPL.applyHFRDeflicker(builder, z);
    }

    public static void applyHHT(Builder builder, boolean z) {
        IMPL.applyHHT(builder, z);
    }

    public static void applyHdrBokeh(Builder builder, boolean z) {
        IMPL.applyHdrBokeh(builder, z);
    }

    public static void applyHdrBracketMode(Builder builder, byte b) {
        IMPL.applyHdrBracketMode(builder, b);
    }

    public static void applyHdrParameter(Builder builder, Integer num, Integer num2) {
        IMPL.applyHdrParameter(builder, num, num2);
    }

    public static void applyHighFpsVideoRecordingMode(Builder builder, boolean z) {
        IMPL.applyHighFpsVideoRecordingMode(builder, z);
    }

    public static void applyHighQualityPreferred(Builder builder, boolean z) {
        IMPL.applyHighQualityPreferred(builder, z);
    }

    public static void applyHighQualityReprocess(Builder builder, boolean z) {
        IMPL.applyHighQualityReprocess(builder, z);
    }

    public static void applyHistogramStats(Builder builder, byte b) {
        IMPL.applyHistogramStats(builder, b);
    }

    public static void applyISO(Builder builder, int i) {
        IMPL.applyISO(builder, i);
    }

    public static void applyIsHfrPreview(Builder builder, boolean z) {
        IMPL.applyIsHfrPreview(builder, z);
    }

    public static void applyIspFrameCount(Builder builder, int i) {
        IMPL.applyIspFrameCount(builder, i);
    }

    public static void applyIspFrameIndex(Builder builder, int i) {
        IMPL.applyIspFrameIndex(builder, i);
    }

    public static void applyIspMetaEnable(Builder builder, boolean z) {
        IMPL.applyIspMetaEnable(builder, z);
    }

    public static void applyIspMetaType(Builder builder, byte b) {
        IMPL.applyIspMetaType(builder, b);
    }

    public static void applyIspPackedRawEnable(Builder builder, int i) {
        IMPL.applyIspPackedRawEnable(builder, i);
    }

    public static void applyIspPackedRawSupport(Builder builder, int i) {
        IMPL.applyIspPackedRawSupport(builder, i);
    }

    public static void applyIspTuningHint(Builder builder, int i) {
        IMPL.applyIspTuningHint(builder, i);
    }

    public static void applyIspTuningIndex(Builder builder, long j) {
        IMPL.applyIspTuningIndex(builder, j);
    }

    public static void applyLLS(Builder builder, int i) {
        IMPL.applyLLS(builder, i);
    }

    public static void applyLensDirtyDetect(Builder builder, boolean z) {
        IMPL.applyLensDirtyDetect(builder, z);
    }

    public static void applyMacroMode(Builder builder, boolean z) {
        IMPL.applyMacroMode(builder, z);
    }

    public static void applyMfnrEnable(Builder builder, boolean z) {
        IMPL.applyMfnr(builder, z);
    }

    public static void applyMfnrFrameNum(Builder builder, int i) {
        IMPL.applyMfnrFrameNum(builder, i);
    }

    public static void applyMiHDRSR(Builder builder, boolean z) {
        IMPL.applyMiHDRSR(builder, z);
    }

    public static void applyMtkProcessRaw(Builder builder, int i) {
        IMPL.applyMtkProcessRaw(builder, i);
    }

    public static void applyMultiFrameCount(Builder builder, int i) {
        IMPL.applyMultiFrameCount(builder, i);
    }

    public static void applyMultiFrameIndex(Builder builder, int i) {
        IMPL.applyMultiFrameIndex(builder, i);
    }

    public static void applyMultiFrameInputNum(Builder builder, int i) {
        IMPL.applyMultiFrameInputNum(builder, i);
    }

    public static void applyNoiseReduction(Builder builder, boolean z) {
        IMPL.applyNoiseReduction(builder, z);
    }

    public static void applyNormalWideLDC(Builder builder, boolean z) {
        IMPL.applyNormalWideLDC(builder, z);
    }

    public static void applyNotificationTrigger(Builder builder, boolean z) {
        IMPL.applyNotificationTrigger(builder, z);
    }

    public static void applyOnTripodModeStatus(Builder builder, ASDScene[] aSDSceneArr) {
        IMPL.applyOnTripodModeStatus(builder, aSDSceneArr);
    }

    public static void applyPanoramaP2SEnabled(Builder builder, boolean z) {
        IMPL.applyPanoramaP2SEnabled(builder, z);
    }

    public static void applyParallelProcessEnable(Builder builder, boolean z) {
        IMPL.applyParallelProcessEnable(builder, z);
    }

    public static void applyParallelProcessPath(Builder builder, String str) {
        IMPL.applyParallelProcessPath(builder, str);
    }

    public static void applyParallelSnapshot(Builder builder, boolean z) {
        IMPL.applyParallelSnapshot(builder, z);
    }

    public static void applyPortraitLighting(Builder builder, int i) {
        IMPL.applyPortraitLighting(builder, i);
    }

    public static void applyPostProcessCropRegion(Builder builder, Rect rect) {
        IMPL.applyPostProcessCropRegion(builder, rect);
    }

    public static void applyPqFeature(Builder builder, boolean z) {
        IMPL.applyPqFeature(builder, z);
    }

    public static void applyQuickPreview(Builder builder, boolean z) {
        IMPL.applyQuickPreview(builder, z);
    }

    public static void applyRawReprocessHint(Builder builder, boolean z) {
        IMPL.applyRawReprocessHint(builder, z);
    }

    public static void applyRemosaicEnabled(Builder builder, boolean z) {
        IMPL.applyRemosaicEnabled(builder, z);
    }

    public static void applyRemosaicHint(Builder builder, boolean z) {
        IMPL.applyRemosaicHint(builder, z);
    }

    public static void applySATUltraWideLDC(Builder builder, boolean z) {
        IMPL.applySATUltraWideLDC(builder, z);
    }

    public static void applySatFallback(Builder builder, boolean z) {
        IMPL.applySatFallback(builder, z);
    }

    public static void applySatFallbackDisable(Builder builder, boolean z) {
        IMPL.applySatFallbackDisable(builder, z);
    }

    public static void applySatIsZooming(Builder builder, boolean z) {
        IMPL.applySatIsZooming(builder, z);
    }

    public static void applySaturation(Builder builder, int i) {
        IMPL.applySaturation(builder, i);
    }

    public static void applyScreenLightHint(Builder builder, byte b) {
        IMPL.applyScreenLightHint(builder, b);
    }

    public static void applySharpness(Builder builder, int i) {
        IMPL.applySharpness(builder, i);
    }

    public static void applyShrinkMemoryMode(Builder builder, int i) {
        IMPL.applyShrinkMemoryMode(builder, i);
    }

    public static void applySingleBokehEnable(Builder builder, boolean z) {
        IMPL.applySingleBokeh(builder, z);
    }

    public static void applySlowMotionVideoRecordingMode(Builder builder, int[] iArr) {
        IMPL.applySlowMotionVideoRecordingMode(builder, iArr);
    }

    public static void applySmoothTransition(Builder builder, boolean z) {
        IMPL.applySmoothTransition(builder, z);
    }

    public static void applySnapshotTorch(Builder builder, boolean z) {
        IMPL.applySnapshotTorch(builder, z);
    }

    public static void applySpecshotMode(Builder builder, int i) {
        IMPL.applySpecshotMode(builder, i);
    }

    public static void applyStFastZoomIn(Builder builder, boolean z) {
        IMPL.applyStFastZoomIn(builder, z);
    }

    public static void applySuperNightRawEnabled(Builder builder, boolean z) {
        IMPL.applySuperNightRawEnabled(builder, z);
    }

    public static void applySuperNightScene(Builder builder, boolean z) {
        IMPL.applySuperNightScene(builder, z);
    }

    public static void applySuperResolution(Builder builder, boolean z) {
        IMPL.applySuperResolution(builder, z);
    }

    public static void applySwMfnrEnable(Builder builder, boolean z) {
        IMPL.applySwMfnr(builder, z);
    }

    public static void applyTargetZoom(Builder builder, float f) {
        IMPL.applyTargetZoom(builder, f);
    }

    public static void applyThermalLevel(Builder builder, Integer num) {
        IMPL.applyThermalLevel(builder, num.intValue());
    }

    public static void applyTimeWaterMark(Builder builder, String str) {
        IMPL.applyTimeWaterMark(builder, str);
    }

    public static void applyTofLaserDist(Builder builder, float f) {
        IMPL.applyTofLaserDist(builder, f);
    }

    public static void applyTuningMode(Builder builder, byte b) {
        IMPL.applyTuningMode(builder, b);
    }

    public static void applyUltraPixelPortrait(Builder builder, boolean z) {
        IMPL.applyUltraPixelPortrait(builder, z);
    }

    public static void applyUltraWideLDC(Builder builder, boolean z) {
        IMPL.applyUltraWideLDC(builder, z);
    }

    public static void applyVideoBokehBackLevel(Builder builder, int i) {
        IMPL.applyVideoBokehBackLevel(builder, i);
    }

    public static void applyVideoBokehColorRetentionMode(Builder builder, int i, boolean z) {
        IMPL.applyVideoBokehColorRetentionMode(builder, i, z);
    }

    public static void applyVideoBokehFrontLevel(Builder builder, float f) {
        IMPL.applyVideoBokehFrontLevel(builder, f);
    }

    public static void applyVideoFilterColorRetentionBack(Builder builder, boolean z) {
        IMPL.applyVideoFilterColorRetentionBack(builder, z);
    }

    public static void applyVideoFilterColorRetentionFront(Builder builder, boolean z) {
        IMPL.applyVideoFilterColorRetentionFront(builder, z);
    }

    public static void applyVideoFilterId(Builder builder, int i) {
        IMPL.applyVideoFilterId(builder, i);
    }

    public static void applyVideoHdrMode(Builder builder, boolean z) {
        IMPL.applyVideoHdrMode(builder, z);
    }

    public static void applyVideoHdrMode(Builder builder, int[] iArr) {
        IMPL.applyVideoHdrMode(builder, iArr);
    }

    public static void applyVideoLogEnable(Builder builder, byte b) {
        IMPL.applyVideoLogEnable(builder, b);
    }

    public static void applyVideoStreamState(Builder builder, boolean z) {
        IMPL.applyVideoStreamState(builder, z);
    }

    public static void applyWaterMarkAppliedList(Builder builder, String str) {
        IMPL.applyWaterMarkAppliedList(builder, str);
    }

    public static void applyZsd(Builder builder, boolean z) {
        IMPL.applyZsd(builder, z);
    }

    public static void applyZsl(Builder builder, boolean z) {
        IMPL.applyZsl(builder, z);
    }

    public static void copyAiSceneFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
        IMPL.copyAiSceneFromCaptureResultToRequest(captureResult, builder);
    }

    public static void copyFpcDataFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
        IMPL.copyFpcDataFromCaptureResultToRequest(captureResult, builder);
    }

    public static VendorTag getDefaultSteamConfigurationsTag() {
        return IMPL.getDefaultSteamConfigurationsTag();
    }
}
