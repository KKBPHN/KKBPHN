package com.android.camera2.compat;

import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.CaptureResult;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.CameraCharacteristicsVendorTags;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;

@TargetApi(21)
class MiCameraCompatMtkImpl extends MiCameraCompatBaseImpl {
    MiCameraCompatMtkImpl() {
    }

    public void applyAiShutterEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.XIAOMI_AISHUTTER_FEATURE_ENABLED, Boolean.valueOf(z));
    }

    public void applyAiShutterExistMotion(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.XIAOMI_AISHUTTER_EXIST_MOTION, z ? CaptureRequestVendorTags.XIAOMI_AISHUTTER_EXIST_MOTION_ON : CaptureRequestVendorTags.XIAOMI_AISHUTTER_EXIST_MOTION_OFF);
    }

    public void applyAmbilightAeTarget(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.AMBILIGHT_AE_TARGET, Integer.valueOf(i));
    }

    public void applyAmbilightMode(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.AMBILIGHT_MODE, Integer.valueOf(i));
    }

    public void applyCShotFeatureCapture(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CSHOT_FEATURE_CAPTURE, Integer.valueOf(z ? 1 : 0));
    }

    public void applyContrast(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.CONTRAST_LEVEL, Integer.valueOf(i + 1));
    }

    public void applyCropFeature(Builder builder, int[] iArr) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MTK_MULTI_CAM_CONFIG_SCALER_CROP_REGION, iArr);
    }

    public void applyCustomWB(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.USE_CUSTOM_WB, Integer.valueOf(i));
    }

    public void applyExposureMeteringMode(Builder builder, int i) {
        VendorTag vendorTag;
        Byte valueOf;
        if (i == 1) {
            vendorTag = CaptureRequestVendorTags.MTK_EXPOSURE_METERING_MODE;
            valueOf = Byte.valueOf(0);
        } else if (i == 2) {
            VendorTagHelper.setValue(builder, CaptureRequestVendorTags.MTK_EXPOSURE_METERING_MODE, Byte.valueOf(1));
            return;
        } else if (i == 0) {
            vendorTag = CaptureRequestVendorTags.MTK_EXPOSURE_METERING_MODE;
            valueOf = Byte.valueOf(2);
        } else {
            return;
        }
        VendorTagHelper.setValue(builder, vendorTag, valueOf);
    }

    public void applyExposureTime(Builder builder, long j) {
        Integer num;
        Key key;
        if (j > 0) {
            builder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, Long.valueOf(j));
            key = CaptureRequest.CONTROL_AE_MODE;
            num = Integer.valueOf(0);
        } else {
            builder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, Long.valueOf(0));
            key = CaptureRequest.CONTROL_AE_MODE;
            num = (Integer) builder.get(key);
        }
        builder.set(key, num);
    }

    public void applyFaceDetection(Builder builder, boolean z) {
        super.applyFaceDetection(builder, z);
        Key key = CaptureRequest.CONTROL_SCENE_MODE;
        builder.set(key, z ? Integer.valueOf(1) : (Integer) builder.get(key));
    }

    public void applyFaceRectangles(Builder builder, Rect[] rectArr) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MI_STATISTICS_FACE_RECTANGLES, rectArr);
    }

    public void applyFeatureMode(Builder builder, int i) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MTK_MULTI_CAM_FEATURE_MODE, Integer.valueOf(i));
    }

    public void applyFrontMirror(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.FRONT_MIRROR, Boolean.valueOf(z));
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.SANPSHOT_FLIP_MODE, Integer.valueOf(z ? 1 : 0));
    }

    public void applyHDRVideoMode(Builder builder, int i) {
    }

    public void applyHdrBracketMode(Builder builder, byte b) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_BRACKET_MODE, Byte.valueOf(b));
    }

    public void applyHighFpsVideoRecordingMode(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.HFPSVR_MODE, Integer.valueOf(z ? 1 : 0));
    }

    public void applyHighQualityReprocess(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HIGH_QUALITY_REPROCESS, Integer.valueOf(z ? 1 : 0));
    }

    public void applyHistogramStats(Builder builder, byte b) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.HISTOGRAM_STATS_ENABLED, Byte.valueOf(b));
    }

    public void applyISO(Builder builder, int i) {
        Integer valueOf = Integer.valueOf(0);
        if (i > 0) {
            builder.set(CaptureRequest.SENSOR_SENSITIVITY, Integer.valueOf(i));
            builder.set(CaptureRequest.CONTROL_AE_MODE, valueOf);
            return;
        }
        builder.set(CaptureRequest.SENSOR_SENSITIVITY, valueOf);
        Key key = CaptureRequest.CONTROL_AE_MODE;
        builder.set(key, (Integer) builder.get(key));
    }

    public void applyIspFrameCount(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyIspFrameCount: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_COUNT, Integer.valueOf(i));
    }

    public void applyIspFrameIndex(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyIspFrameIndex: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_INDEX, Integer.valueOf(i));
    }

    public void applyIspMetaEnable(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_ISP_META_ENABLE, Byte.valueOf(z ? (byte) 2 : 0));
    }

    public void applyIspMetaType(Builder builder, byte b) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_ISP_META_REQUEST, Byte.valueOf(b));
    }

    public void applyIspPackedRawEnable(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyIspPackedRawEnable: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_PACKED_RAW_ENABLE, Integer.valueOf(i));
    }

    public void applyIspPackedRawSupport(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyIspPackedRawSupport: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_PACKED_RAW_SUPPORT, Integer.valueOf(i));
    }

    public void applyIspTuningHint(Builder builder, int i) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_TUNING, Integer.valueOf(i));
    }

    public void applyIspTuningIndex(Builder builder, long j) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyIspTuningIndex: 0x");
        sb.append(Long.toHexString(j));
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_TUNING_INDEX, Long.valueOf(j));
    }

    public void applyMtkProcessRaw(Builder builder, int i) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_MTK_PROCESS_RAW_ENABLE, Integer.valueOf(i));
    }

    public void applyNoiseReduction(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_SINGLE_YUV_NR, Integer.valueOf(z ? 1 : 0));
    }

    public void applyNotificationTrigger(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_NOTIFICATION_TRIGGER, Integer.valueOf(z ? 1 : 0));
    }

    public void applyPanoramaP2SEnabled(Builder builder, boolean z) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.MI_PANORAMA_P2S_ENABLED, Boolean.valueOf(z));
    }

    public void applyPostProcessCropRegion(Builder builder, Rect rect) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.POST_PROCESS_CROP_REGION, rect);
    }

    public void applyPqFeature(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY, z ? CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY_ON : CaptureRequestVendorTags.MTK_CONFIGURE_SETTING_PROPRIETARY_OFF);
    }

    public void applyQuickPreview(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_QUICK_PREVIEW, z ? CaptureRequestVendorTags.CONTROL_QUICK_PREVIEW_ON : CaptureRequestVendorTags.CONTROL_QUICK_PREVIEW_OFF);
    }

    public void applyRawReprocessHint(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.HINT_FOR_RAW_REPROCESS, Boolean.valueOf(z));
    }

    public void applyRemosaicHint(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_REMOSAIC_HINT, z ? CaptureRequestVendorTags.CONTROL_REMOSAIC_HINT_ON : CaptureRequestVendorTags.CONTROL_REMOSAIC_HINT_OFF);
    }

    public void applySaturation(Builder builder, int i) {
        int i2 = 0;
        switch (i) {
            case 1:
                i2 = 2;
                break;
            case 2:
                i2 = 4;
                break;
            case 3:
                i2 = 5;
                break;
            case 4:
                i2 = 6;
                break;
            case 5:
                i2 = 8;
                break;
            case 6:
                i2 = 10;
                break;
        }
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SATURATION, Integer.valueOf(i2));
    }

    public void applySharpness(Builder builder, int i) {
        int i2 = 0;
        switch (i) {
            case 1:
                i2 = 1;
                break;
            case 2:
                i2 = 2;
                break;
            case 3:
                i2 = 3;
                break;
            case 4:
                i2 = 4;
                break;
            case 5:
                i2 = 5;
                break;
            case 6:
                i2 = 6;
                break;
        }
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SHARPNESS_CONTROL, Integer.valueOf(i2));
    }

    public void applySlowMotionVideoRecordingMode(Builder builder, int[] iArr) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.SMVR_MODE, iArr);
    }

    public void applySpecshotMode(Builder builder, int i) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_ENABLE_SPECSHOT_MODE, Integer.valueOf(i));
    }

    public void applySuperNightRawEnabled(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.SUPERNIGHT_RAW_ENABLED, Boolean.valueOf(z));
    }

    public void applyVideoHdrMode(Builder builder, int[] iArr) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MTK_HDR_KEY_DETECTION_MODE, iArr);
    }

    public void applyVideoStreamState(Builder builder, boolean z) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.RECORDING_END_STREAM, Byte.valueOf(z ^ true ? (byte) 1 : 0));
    }

    public void applyZsd(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.ZSL_CAPTURE_MODE, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void copyAiSceneFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
        Integer num = (Integer) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.AI_SCENE_DETECTED);
        if (num != null) {
            VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_AI_SCENE_MODE, num);
        }
    }

    public void copyFpcDataFromCaptureResultToRequest(CaptureResult captureResult, Builder builder) {
        byte[] bArr = (byte[]) VendorTagHelper.getValueSafely(captureResult, CaptureResultVendorTags.DISTORTION_FPC_DATA);
        if (bArr != null && bArr.length / 8 == 23) {
            VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.CONTROL_DISTORTION_FPC_DATA, bArr);
        }
    }

    public VendorTag getDefaultSteamConfigurationsTag() {
        return CameraCharacteristicsVendorTags.SCALER_AVAILABLE_STREAM_CONFIGURATIONS;
    }
}
