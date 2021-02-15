package com.android.camera2.compat;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene.ASDScene;
import java.util.HashSet;
import java.util.Map.Entry;

@TargetApi(21)
public class MiCameraCompatBaseImpl {
    protected static final String TAG = "MiCameraCompat";

    public void applyASDEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.ASD_ENABLE, Boolean.valueOf(z));
    }

    public void applyASDScene(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AI_SCENE_APPLY, Integer.valueOf(i));
    }

    public void applyAiAIIEPreviewEnable(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyAiAIIEPreviewEnable:");
        sb.append(z);
        Log.e(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.AI_AIIE_PREVIEWENABLED, Boolean.valueOf(z));
    }

    public void applyAiASDEnable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AI_SCENE, Boolean.valueOf(z));
    }

    public void applyAiMoonEffectEnable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AI_MOON_EFFECT_ENABLED, Boolean.valueOf(z));
    }

    public void applyAiScenePeriod(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AI_SCENE_PERIOD, Integer.valueOf(i));
    }

    public void applyAiShutterEnable(Builder builder, boolean z) {
    }

    public void applyAiShutterExistMotion(Builder builder, boolean z) {
    }

    public void applyAlgoUpEnabled(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ALGO_UP_ENABLED, Boolean.valueOf(z));
    }

    public void applyAmbilightAeTarget(Builder builder, int i) {
    }

    public void applyAmbilightMode(Builder builder, int i) {
    }

    public void applyAsdDirtyEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.ASD_DIRTY_ENABLE, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyAutoZoomMode(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AUTOZOOM_MODE, Integer.valueOf(i));
    }

    public void applyAutoZoomScaleOffset(Builder builder, float f) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.AUTOZOOM_SCALE_OFFSET, Float.valueOf(f));
    }

    public void applyBackSoftLight(Builder builder, byte b) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BACK_SOFT_LIGHT, Byte.valueOf(b));
    }

    public void applyBackwardCaptureHint(Builder builder, byte b) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BACKWARD_CAPTURE_HINT, Byte.valueOf(b));
    }

    public void applyBeautyParameter(Builder builder, HashSet hashSet, BeautyValues beautyValues) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BEAUTY_LEVEL, beautyValues.mBeautyLevel);
        if (C0124O00000oO.Oo00O()) {
            for (Entry entry : BeautyConstant.BEAUTY_TYPE_VENDOR_TAG_MAP.entrySet()) {
                VendorTag vendorTag = (VendorTag) entry.getValue();
                if (hashSet.contains(vendorTag.getName())) {
                    VendorTagHelper.setValue(builder, vendorTag, Integer.valueOf(beautyValues.getValueByType((String) entry.getKey())));
                }
            }
            return;
        }
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BEAUTY_SKIN_COLOR, Integer.valueOf(beautyValues.mBeautySkinColor));
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BEAUTY_SLIM_FACE, Integer.valueOf(beautyValues.mBeautySlimFace));
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BEAUTY_SKIN_SMOOTH, Integer.valueOf(beautyValues.mBeautySkinSmooth));
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BEAUTY_ENLARGE_EYE, Integer.valueOf(beautyValues.mBeautyEnlargeEye));
    }

    public void applyBurstFps(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BURST_SHOOT_FPS, Integer.valueOf(i));
    }

    public void applyBurstHint(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BURST_CAPTURE_HINT, Integer.valueOf(i));
    }

    public void applyCShotFeatureCapture(Builder builder, boolean z) {
    }

    public void applyCameraAi30Enable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.CAMERA_AI_30, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyCinematicPhoto(Builder builder, byte b) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.CINEMATIC_PHOTO_ENABLED, Byte.valueOf(b));
    }

    public void applyCinematicVideo(Builder builder, byte b) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED, Byte.valueOf(b));
    }

    public void applyColorEnhanceEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.COLOR_ENHANCE_ENABLED, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyContrast(Builder builder, int i) {
    }

    public void applyCropFeature(Builder builder, int[] iArr) {
    }

    public void applyCustomWB(Builder builder, int i) {
    }

    public void applyCustomWaterMark(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.CUSTOM_WATERMARK_TEXT, str);
    }

    public void applyDepurpleEnable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.DEPURPLE, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyDeviceOrientation(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.DEVICE_ORIENTATION, Integer.valueOf(i));
    }

    public void applyDualBokeh(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.DUAL_BOKEH_ENABLE, Boolean.valueOf(z));
    }

    public void applyExposureMeteringMode(Builder builder, int i) {
    }

    public void applyExposureTime(Builder builder, long j) {
        Object obj;
        Key key;
        if (j > 0) {
            builder.set(CaptureRequest.CONTROL_MODE, Integer.valueOf(0));
            key = CaptureRequest.SENSOR_EXPOSURE_TIME;
            obj = Long.valueOf(j);
        } else {
            key = CaptureRequest.CONTROL_MODE;
            obj = (Integer) builder.get(key);
        }
        builder.set(key, obj);
    }

    public void applyEyeLight(Builder builder, int i, int i2) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.EYE_LIGHT_TYPE, Integer.valueOf(i));
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.EYE_LIGHT_STRENGTH, Integer.valueOf(i2));
    }

    public void applyFNumber(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.BOKEH_F_NUMBER, str);
    }

    public void applyFaceAnalyzeAge(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.FACE_AGE_ANALYZE_ENABLED, Boolean.valueOf(z));
    }

    public void applyFaceDetection(Builder builder, boolean z) {
        builder.set(CaptureRequest.STATISTICS_FACE_DETECT_MODE, Integer.valueOf(z ? 1 : 0));
    }

    public void applyFaceRectangles(Builder builder, Rect[] rectArr) {
    }

    public void applyFaceScore(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.FACE_SCORE_ENABLED, Boolean.valueOf(z));
    }

    public void applyFaceWaterMark(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.WATERMARK_FACE, str);
    }

    public void applyFeatureMode(Builder builder, int i) {
    }

    public void applyFlashCurrent(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyFlashCurrent: value = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.FLASH_CURRENT, Integer.valueOf(i));
    }

    public void applyFlashMode(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyFlashMode: mode = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.FLASH_MODE, Integer.valueOf(i));
    }

    public void applyFlawDetectEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.FLAW_DETECT_ENABLE, Boolean.valueOf(z));
    }

    public void applyFrontMirror(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.FRONT_MIRROR, Boolean.valueOf(z));
    }

    public void applyHDR(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_ENABLED, Boolean.valueOf(z));
    }

    public void applyHDRCheckerEnable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_CHECKER_ENABLE, Boolean.valueOf(z));
    }

    public void applyHDRCheckerStatus(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_CHECKER_STATUS, Integer.valueOf(i));
    }

    public void applyHDRMode(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyHDRMode:");
        sb.append(i);
        Log.e(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.HDR_MODE, Integer.valueOf(i));
    }

    public void applyHDRVideoMode(Builder builder, int i) {
    }

    public void applyHFRDeflicker(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.DEFLICKER_ENABLED, Boolean.valueOf(z));
    }

    public void applyHHT(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HHT_ENABLED, Boolean.valueOf(z));
    }

    public void applyHdrBokeh(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyHdrBokeh: enabled = ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_BOKEH_ENABLED, Boolean.valueOf(z));
    }

    public void applyHdrBracketMode(Builder builder, byte b) {
    }

    public void applyHdrParameter(Builder builder, Integer num, Integer num2) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.HDR_CHECKER_SCENETYPE, num);
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.HDR_CHECKER_ADRC, num2);
    }

    public void applyHighFpsVideoRecordingMode(Builder builder, boolean z) {
    }

    public void applyHighQualityPreferred(Builder builder, boolean z) {
    }

    public void applyHighQualityReprocess(Builder builder, boolean z) {
    }

    public void applyHistogramStats(Builder builder, byte b) {
    }

    public void applyISO(Builder builder, int i) {
        builder.set(CaptureRequest.SENSOR_SENSITIVITY, Integer.valueOf(i));
    }

    public void applyIsHfrPreview(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.IS_HFR_PREVIEW, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyIspFrameCount(Builder builder, int i) {
    }

    public void applyIspFrameIndex(Builder builder, int i) {
    }

    public void applyIspMetaEnable(Builder builder, boolean z) {
    }

    public void applyIspMetaType(Builder builder, byte b) {
    }

    public void applyIspPackedRawEnable(Builder builder, int i) {
    }

    public void applyIspPackedRawSupport(Builder builder, int i) {
    }

    public void applyIspTuningHint(Builder builder, int i) {
    }

    public void applyIspTuningIndex(Builder builder, long j) {
    }

    public void applyLLS(Builder builder, int i) {
    }

    public void applyLensDirtyDetect(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.LENS_DIRTY_DETECT, Boolean.valueOf(z));
    }

    public void applyMacroMode(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.MACRO_MODE, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyMfnr(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyMfnrEnable: ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.MFNR_ENABLED, Boolean.valueOf(z));
    }

    public void applyMfnrFrameNum(Builder builder, int i) {
    }

    public void applyMiHDRSR(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.MI_HDR_SR_ENABLED, Boolean.valueOf(z));
    }

    public void applyMtkProcessRaw(Builder builder, int i) {
    }

    public void applyMultiFrameCount(Builder builder, int i) {
    }

    public void applyMultiFrameIndex(Builder builder, int i) {
    }

    public void applyMultiFrameInputNum(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.MULTIFRAME_INPUTNUM, Integer.valueOf(i));
    }

    public void applyNoiseReduction(Builder builder, boolean z) {
    }

    public void applyNormalWideLDC(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyNormalWideLDC: ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.NORMAL_WIDE_LENS_DISTORTION_CORRECTION_LEVEL, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyNotificationTrigger(Builder builder, boolean z) {
    }

    public void applyOnTripodModeStatus(Builder builder, ASDScene[] aSDSceneArr) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.ON_TRIPOD_MODE, aSDSceneArr);
    }

    public void applyPanoramaP2SEnabled(Builder builder, boolean z) {
    }

    public void applyParallelProcessEnable(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.PARALLEL_ENABLED, Boolean.valueOf(z));
    }

    public void applyParallelProcessPath(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.PARALLEL_PATH, str.getBytes());
    }

    public void applyParallelSnapshot(Builder builder, boolean z) {
    }

    public void applyPortraitLighting(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.PORTRAIT_LIGHTING, Integer.valueOf(i));
    }

    public void applyPostProcessCropRegion(Builder builder, Rect rect) {
    }

    public void applyPqFeature(Builder builder, boolean z) {
    }

    public void applyQuickPreview(Builder builder, boolean z) {
    }

    public void applyRawReprocessHint(Builder builder, boolean z) {
    }

    public void applyRemosaicEnabled(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_ENABLE_REMOSAIC, Boolean.valueOf(z));
    }

    public void applyRemosaicHint(Builder builder, boolean z) {
    }

    public void applySATUltraWideLDC(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySATUltraWideLDC: ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.SAT_ULTRA_WIDE_LENS_DISTORTION_CORRECTION_ENABLE, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applySatFallback(Builder builder, boolean z) {
    }

    public void applySatFallbackDisable(Builder builder, boolean z) {
    }

    public void applySatIsZooming(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySatIsZooming:");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.SAT_IS_ZOOMING, Boolean.valueOf(z));
    }

    public void applySaturation(Builder builder, int i) {
    }

    public void applyScreenLightHint(Builder builder, byte b) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SCREEN_LIGHT_HINT, Byte.valueOf(b));
    }

    public void applySharpness(Builder builder, int i) {
    }

    public void applyShrinkMemoryMode(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyShrinkMemoryMode: mode = ");
        sb.append(i);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.SHRINK_MEMORY_MODE, Integer.valueOf(i));
    }

    public void applySingleBokeh(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SINGLE_CAMERA_BOKEH, Boolean.valueOf(z));
    }

    public void applySlowMotionVideoRecordingMode(Builder builder, int[] iArr) {
    }

    public void applySmoothTransition(Builder builder, boolean z) {
    }

    public void applySnapshotTorch(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SNAP_SHOT_TORCH, Boolean.valueOf(z));
    }

    public void applySpecshotMode(Builder builder, int i) {
    }

    public void applyStFastZoomIn(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ST_FAST_ZOOM_IN, Boolean.valueOf(z));
    }

    public void applySuperNightRawEnabled(Builder builder, boolean z) {
    }

    public void applySuperNightScene(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SUPER_NIGHT_SCENE_ENABLED, Boolean.valueOf(z));
    }

    public void applySuperResolution(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SUPER_RESOLUTION_ENABLED, Boolean.valueOf(z));
    }

    public void applySwMfnr(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySwMfnrEnable: ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.SW_MFNR_ENABLED, Boolean.valueOf(z));
    }

    public void applyTargetZoom(Builder builder, float f) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.TARGET_ZOOM, Float.valueOf(f));
    }

    public void applyThermalLevel(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.THERMAL_LEVEL, Integer.valueOf(i));
    }

    public void applyTimeWaterMark(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.WATERMARK_TIME, str);
    }

    public void applyTofLaserDist(Builder builder, float f) {
    }

    public void applyTuningMode(Builder builder, byte b) {
    }

    public void applyUltraPixelPortrait(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ULTRA_PIXEL_PORTRAIT_ENABLED, Boolean.valueOf(z));
    }

    public void applyUltraWideLDC(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyUltraWideLDC: ");
        sb.append(z);
        Log.d(TAG, sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ULTRA_WIDE_LENS_DISTORTION_CORRECTION_LEVEL, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyVideoBokehBackLevel(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.VIDEO_BOKEH_BACK_LEVEL, Integer.valueOf(i));
    }

    public void applyVideoBokehColorRetentionMode(Builder builder, int i, boolean z) {
    }

    public void applyVideoBokehFrontLevel(Builder builder, float f) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.VIDEO_BOKEH_FRONT_LEVEL, Float.valueOf(f));
    }

    public void applyVideoFilterColorRetentionBack(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.VIDEO_FILTER_COLOR_RETENTION_BACK, Boolean.valueOf(z));
    }

    public void applyVideoFilterColorRetentionFront(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.VIDEO_FILTER_COLOR_RETENTION_FRONT, Boolean.valueOf(z));
    }

    public void applyVideoFilterId(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.VIDEO_FILTER_ID, Integer.valueOf(i));
    }

    public void applyVideoHdrMode(Builder builder, boolean z) {
    }

    public void applyVideoHdrMode(Builder builder, int[] iArr) {
    }

    public void applyVideoLogEnable(Builder builder, byte b) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.PRO_VIDEO_LOG_ENABLED, Byte.valueOf(b));
    }

    public void applyVideoStreamState(Builder builder, boolean z) {
    }

    public void applyWaterMarkAppliedList(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.WATERMARK_APPLIEDTYPE, str);
    }

    public void applyWaterMarkType(Builder builder, String str) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.WATERMARK_APPLIEDTYPE, str);
    }

    public void applyZsd(Builder builder, boolean z) {
    }

    public void applyZsl(Builder builder, boolean z) {
        CompatibilityUtils.setZsl(builder, z);
    }

    public void copyAiSceneFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
    }

    public void copyFpcDataFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
    }

    public VendorTag getDefaultSteamConfigurationsTag() {
        return null;
    }
}
