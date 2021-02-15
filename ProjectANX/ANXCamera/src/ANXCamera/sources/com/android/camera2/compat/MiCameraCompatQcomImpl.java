package com.android.camera2.compat;

import android.annotation.TargetApi;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Builder;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;

@TargetApi(21)
class MiCameraCompatQcomImpl extends MiCameraCompatBaseImpl {
    MiCameraCompatQcomImpl() {
    }

    public void applyAmbilightAeTarget(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.AMBILIGHT_AE_TARGET, Integer.valueOf(i));
    }

    public void applyAmbilightMode(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.AMBILIGHT_MODE, Integer.valueOf(i));
    }

    public void applyContrast(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.CONTRAST_LEVEL, Integer.valueOf(i + 1));
    }

    public void applyCustomWB(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.USE_CUSTOM_WB, Integer.valueOf(i));
    }

    public void applyExposureMeteringMode(Builder builder, int i) {
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.EXPOSURE_METERING, Integer.valueOf(i));
    }

    public void applyExposureTime(Builder builder, long j) {
        Long l = (Long) VendorTagHelper.getValue(builder, CaptureRequestVendorTags.ISO_EXP);
        if ((l == null || l.longValue() == 0) && j > 0) {
            VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SELECT_PRIORITY, Integer.valueOf(1));
            VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ISO_EXP, Long.valueOf(j));
            builder.set(CaptureRequest.SENSOR_SENSITIVITY, null);
        }
        super.applyExposureTime(builder, j);
    }

    public void applyHDRVideoMode(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("HDR10VideoMode: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR10_VIDEO, Byte.valueOf((byte) i));
    }

    public void applyHdrBracketMode(Builder builder, byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyHdrBracketMode: ");
        sb.append(b);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.HDR_BRACKET_MODE, Byte.valueOf(b));
    }

    public void applyHighQualityPreferred(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.HIGHQUALITY_PREFERRED, Byte.valueOf(z ? (byte) 1 : 0));
    }

    public void applyISO(Builder builder, int i) {
        VendorTag vendorTag;
        long j;
        Object valueOf;
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SELECT_PRIORITY, Integer.valueOf(0));
        if (i != 0) {
            if (i == 100) {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 2;
            } else if (i == 200) {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 3;
            } else if (i == 400) {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 4;
            } else if (i == 800) {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 5;
            } else if (i == 1600) {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 6;
            } else if (i != 3200) {
                StringBuilder sb = new StringBuilder();
                sb.append("applyISO(): set manual absolute iso value to ");
                sb.append(i);
                Log.d("MiCameraCompat", sb.toString());
                VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ISO_EXP, Long.valueOf(8));
                vendorTag = CaptureRequestVendorTags.USE_ISO_VALUE;
                valueOf = Integer.valueOf(i);
            } else {
                vendorTag = CaptureRequestVendorTags.ISO_EXP;
                j = 7;
            }
            valueOf = Long.valueOf(j);
        } else {
            vendorTag = CaptureRequestVendorTags.ISO_EXP;
            j = 0;
            valueOf = Long.valueOf(j);
        }
        VendorTagHelper.setValue(builder, vendorTag, valueOf);
    }

    public void applyLLS(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyLLS: value = ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.IS_LLS_NEEDED, Integer.valueOf(i));
    }

    public void applyMfnrFrameNum(Builder builder, int i) {
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.MFNR_FRAME_NUM, Integer.valueOf(i));
    }

    public void applyMultiFrameCount(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyMultiFrameCount: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_FOR_MULTIFRAME_FRAME_COUNT, Integer.valueOf(i));
    }

    public void applyMultiFrameIndex(Builder builder, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyMultiFrameIndex: ");
        sb.append(i);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueQuietly(builder, CaptureRequestVendorTags.CONTROL_CAPTURE_FOR_MULTIFRAME_FRAME_INDEX, Integer.valueOf(i));
    }

    public void applyParallelSnapshot(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.IS_PARALLEL_SNAPSHOT, Boolean.valueOf(z));
    }

    public void applySatFallback(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySatFallback: ");
        sb.append(z);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SAT_FALLBACK_ENABLE, Boolean.valueOf(z));
    }

    public void applySatFallbackDisable(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySatFallbackDisable: ");
        sb.append(z ? 1 : 0);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.SAT_FALLBACK_DISABLE, Byte.valueOf(z ? (byte) 1 : 0));
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

    public void applySmoothTransition(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applySmoothTransition: ");
        sb.append(z);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.ST_ENABLED, Boolean.valueOf(z));
    }

    public void applyTuningMode(Builder builder, byte b) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyTuningMode: ");
        sb.append(b);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.MI_TUNING_MODE, Byte.valueOf(b));
    }

    public void applyVideoBokehColorRetentionMode(Builder builder, int i, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("applyVideoBokehColorRetentionMode: ");
        sb.append(i);
        sb.append(" isFront:");
        sb.append(z);
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, z ? CaptureRequestVendorTags.VIDEO_BOKEH_COLOR_RETENTION_FRONT_MODE : CaptureRequestVendorTags.VIDEO_BOKEH_COLOR_RETENTION_BACK_MODE, Integer.valueOf(i));
    }

    public void applyVideoHdrMode(Builder builder, boolean z) {
        VendorTagHelper.setValueSafely(builder, CaptureRequestVendorTags.QCOM_VIDEO_HDR_ENABLED, Integer.valueOf(z ? 1 : 0));
    }

    public void applyVideoStreamState(Builder builder, boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("recordingEndOfStream: ");
        sb.append(z ? "0x0" : "0x1");
        Log.d("MiCameraCompat", sb.toString());
        VendorTagHelper.setValue(builder, CaptureRequestVendorTags.RECORDING_END_STREAM, Byte.valueOf(z ^ true ? (byte) 1 : 0));
    }
}
