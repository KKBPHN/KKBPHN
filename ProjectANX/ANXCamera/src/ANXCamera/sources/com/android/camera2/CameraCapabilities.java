package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.annotation.TargetApi;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CaptureRequest.Key;
import android.hardware.camera2.params.StreamConfiguration;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.util.Range;
import android.util.Rational;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseArray;
import com.android.camera.BokehVendor;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.compat.MiCameraCompat;
import com.android.camera2.vendortag.CameraCharacteristicsVendorTags;
import com.android.camera2.vendortag.CaptureRequestVendorTags;
import com.android.camera2.vendortag.CaptureResultVendorTags;
import com.android.camera2.vendortag.VendorTag;
import com.android.camera2.vendortag.VendorTagHelper;
import com.android.camera2.vendortag.struct.SatFusionCalibrationData;
import com.android.camera2.vendortag.struct.SlowMotionVideoConfiguration;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@TargetApi(21)
public class CameraCapabilities {
    public static final int CAMERA_ILLEGALSTATE_EXCEPTION = 256;
    private static final boolean DEBUG = false;
    private static final float DEFAULT_VIEW_ANGLE = 51.5f;
    public static final int END_OF_STREAM_TYPE_PREVIEW = 1;
    public static final int END_OF_STREAM_TYPE_VIDEO = 0;
    public static final int HAL_PIXEL_FORMAT_BLOB = 33;
    public static final int HAL_PIXEL_FORMAT_IMPLEMENTATION_DEFINED = 34;
    public static final int HAL_PIXEL_FORMAT_YCbCr_420_888 = 35;
    public static final int SESSION_OPERATION_MODE_AI_VIDEO_ENHANCED = 32809;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_DUAL_BOKEH = 36864;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_HD = 36868;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_MANUAL = 36872;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_MANUAL_G7 = 36870;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_MANUAL_ULTRA_PIXEL = 36871;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_NORMAL = 36869;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_QCFA = 36865;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_SAT = 36866;
    public static final int SESSION_OPERATION_MODE_ALGO_UP_SINGLE_BOKEH = 36867;
    public static final int SESSION_OPERATION_MODE_AMBILIGHT = 37632;
    public static final int SESSION_OPERATION_MODE_AUTO_ZOOM = 33012;
    public static final int SESSION_OPERATION_MODE_CLONE_MODE = 33016;
    public static final int SESSION_OPERATION_MODE_DOLLY_ZOOM = 32782;
    public static final int SESSION_OPERATION_MODE_FOVC = 61456;
    public static final int SESSION_OPERATION_MODE_FRONT_PORTRAIT = 33009;
    public static final int SESSION_OPERATION_MODE_HDR10 = 32804;
    public static final int SESSION_OPERATION_MODE_HFR_120 = 32888;
    public static final int SESSION_OPERATION_MODE_HIGH_SPEED = 1;
    public static final int SESSION_OPERATION_MODE_HSR_60 = 32828;
    public static final int SESSION_OPERATION_MODE_MANUAL = 32771;
    public static final int SESSION_OPERATION_MODE_MCTF = 32816;
    public static final int SESSION_OPERATION_MODE_MIMOJI = 32779;
    public static final int SESSION_OPERATION_MODE_MIUI_BACK = 32769;
    public static final int SESSION_OPERATION_MODE_MIUI_FRONT = 32773;
    public static final int SESSION_OPERATION_MODE_MI_LIVE = 32780;
    public static final int SESSION_OPERATION_MODE_NORMAL = 0;
    public static final int SESSION_OPERATION_MODE_NORMAL_ULTRA_PIXEL_PHOTOGRAPHY = 33011;
    public static final int SESSION_OPERATION_MODE_PANORMA = 32776;
    public static final int SESSION_OPERATION_MODE_PORTRAIT = 32770;
    public static final int SESSION_OPERATION_MODE_PROFESSIONAL_ULTRA_PIXEL_PHOTOGRAPHY = 33013;
    public static final int SESSION_OPERATION_MODE_QCFA = 32775;
    public static final int SESSION_OPERATION_MODE_SUPER_NIGHT = 32778;
    public static final int SESSION_OPERATION_MODE_VIDEO = 32772;
    public static final int SESSION_OPERATION_MODE_VIDEO_BEAUTY = 32777;
    public static final int SESSION_OPERATION_MODE_VIDEO_BEAUTY_WITH_PREVIEW_EIS = 32793;
    public static final int SESSION_OPERATION_MODE_VIDEO_EIS_8K = 32797;
    public static final int SESSION_OPERATION_MODE_VIDEO_HDR = 61442;
    public static final int SESSION_OPERATION_MODE_VIDEO_SUPEREIS = 32781;
    public static final int SESSION_OPERATION_MODE_VIDEO_SUPEREISPRO = 32783;
    public static final int SESSION_OPERATION_MODE_VIDEO_SUPER_NIGHT = 32817;
    public static final int SESSION_OPERATION_MODE_VV = 32780;
    private static final List STREAM_CONFIGURATIONS_VENDOR_KEYS = new ArrayList(3) {
        {
            add(CameraCharacteristicsVendorTags.QCFA_STREAM_CONFIGURATIONS);
            add(CameraCharacteristicsVendorTags.SCALER_AVAILABLE_LIMIT_STREAM_CONFIGURATIONS);
            add(CameraCharacteristicsVendorTags.SCALER_AVAILABLE_SR_STREAM_CONFIGURATIONS);
        }
    };
    private static final String TAG = "CameraCapabilities";
    public static final int TUNING_BUFFER_FORMAT_RAW = 2;
    public static final int TUNING_BUFFER_FORMAT_YUV = 1;
    public static final int ULTRA_PIXEL_FRONT_32M_INDEX = 0;
    public static final int ULTRA_PIXEL_REAR_108M_INDEX = 3;
    public static final int ULTRA_PIXEL_REAR_48M_INDEX = 1;
    public static final int ULTRA_PIXEL_REAR_64M_INDEX = 2;
    public static final int XIAOMI_YUV_FORMAT_INVALID = -1;
    public static final int XIAOMI_YUV_FORMAT_NV12 = 1;
    public static final int XIAOMI_YUV_FORMAT_NV21 = 2;
    private final int mCameraId;
    private final HashSet mCaptureRequestVendorKeys;
    private final CameraCharacteristics mCharacteristics;
    private int mOperatingMode;
    private final HashSet mSessionKeys;
    private SparseArray mStreamConfigurationMap;

    @Retention(RetentionPolicy.SOURCE)
    public @interface OperatingMode {
    }

    public CameraCapabilities(CameraCharacteristics cameraCharacteristics, int i) {
        HashSet hashSet;
        if (cameraCharacteristics != null) {
            this.mCharacteristics = cameraCharacteristics;
            this.mCameraId = i;
            if (((Integer) this.mCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue() == 2) {
                this.mCaptureRequestVendorKeys = new HashSet();
                hashSet = new HashSet();
            } else {
                ArrayList<Key> allVendorKeys = this.mCharacteristics.getNativeCopy().getAllVendorKeys(Key.class);
                if (allVendorKeys != null) {
                    this.mCaptureRequestVendorKeys = new HashSet(allVendorKeys.size());
                    for (Key name : allVendorKeys) {
                        this.mCaptureRequestVendorKeys.add(name.getName());
                    }
                    List<Key> availableSessionKeys = CompatibilityUtils.getAvailableSessionKeys(this.mCharacteristics);
                    if (availableSessionKeys != null) {
                        this.mSessionKeys = new HashSet(availableSessionKeys.size());
                        if (availableSessionKeys != null && !availableSessionKeys.isEmpty()) {
                            for (Key name2 : availableSessionKeys) {
                                this.mSessionKeys.add(name2.getName());
                            }
                            return;
                        }
                        return;
                    }
                    hashSet = new HashSet();
                } else {
                    throw new IllegalArgumentException("Null vendor tag! Need to check it!");
                }
            }
            this.mSessionKeys = hashSet;
            return;
        }
        throw new IllegalArgumentException("Null CameraCharacteristics");
    }

    private void addStreamConfigurationToList(List list, VendorTag vendorTag) {
        if (vendorTag == null) {
            Log.w(TAG, "addStreamConfigurationToList: but the key is null!");
            return;
        }
        StreamConfiguration[] streamConfigurationArr = (StreamConfiguration[]) VendorTagHelper.getValueSafely(this.mCharacteristics, vendorTag);
        String str = "addStreamConfigurationToList: ";
        if (streamConfigurationArr != null) {
            list.addAll(Arrays.asList(streamConfigurationArr));
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(vendorTag.getName());
            sb.append(": size = ");
            sb.append(streamConfigurationArr.length);
            Log.d(str2, sb.toString());
        } else {
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(vendorTag.getName());
            sb2.append("'s configurations is null!");
            Log.w(str3, sb2.toString());
        }
    }

    private static boolean contains(int[] iArr, int i) {
        if (iArr == null) {
            return false;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    private List convertToPictureSize(Size[] sizeArr) {
        ArrayList arrayList = new ArrayList();
        if (sizeArr != null) {
            for (Size size : sizeArr) {
                arrayList.add(new CameraSize(size.getWidth(), size.getHeight()));
            }
        }
        return arrayList;
    }

    private static Size[] convertToSizes(int[] iArr) {
        Size[] sizeArr = null;
        if (!(iArr == null || iArr.length == 0)) {
            int length = iArr.length;
            if (length % 2 != 0) {
                Log.d(TAG, "length must be odd");
                return null;
            }
            sizeArr = new Size[(length / 2)];
            for (int i = 0; i < length - 1; i += 2) {
                sizeArr[i / 2] = new Size(iArr[i], iArr[i + 1]);
            }
        }
        return sizeArr;
    }

    private static int getAnchorFrameMaskBit(int i, int i2) {
        if (i == 0) {
            switch (i2) {
                case 1:
                    return 256;
                case 2:
                    return 512;
                case 3:
                    return 2048;
                case 4:
                    return 4096;
                case 5:
                    return 8192;
                case 6:
                    return 16384;
                case 7:
                    return 1024;
            }
        } else if (i2 == 100) {
            return 1;
        } else {
            if (i2 == 101) {
                return 2;
            }
        }
        return 0;
    }

    private List getExtraHighSpeedVideoConfiguration() {
        if (!isSupportExtraHighSpeedVideoConfiguration()) {
            return null;
        }
        Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.EXTRA_HIGH_SPEED_VIDEO_NUMBER);
        if (num == null || num.intValue() <= 0) {
            return null;
        }
        int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.EXTRA_HIGH_SPEED_VIDEO_CONFIGURATIONS);
        if (iArr != null) {
            return MiHighSpeedVideoConfiguration.unmarshal(iArr, num.intValue());
        }
        return null;
    }

    private int[] getOptimalMasterBokehSizes() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_MASTER_SIZE.getName())) {
            return null;
        }
        return (int[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_MASTER_SIZE);
    }

    private int[] getOptimalPictureBokehSizes() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_PICTURE_SIZE.getName())) {
            return null;
        }
        return (int[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_PICTURE_SIZE);
    }

    private int[] getOptimalSlaveBokehSizes() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_SLAVE__SIZE.getName())) {
            return null;
        }
        return (int[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_SLAVE__SIZE);
    }

    private SlowMotionVideoConfiguration[] getSlowMotionVideoConfiguration() {
        if (isSupportSlowMotionVideoConfiguration()) {
            return (SlowMotionVideoConfiguration[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SLOW_MOTION_VIDEO_CONFIGURATIONS);
        }
        return null;
    }

    private StreamConfigurationMap getStreamConfigurationMap(int i) {
        if (this.mStreamConfigurationMap == null) {
            this.mStreamConfigurationMap = new SparseArray(5);
        }
        if (this.mStreamConfigurationMap.get(i) == null) {
            List streamConfigurations = getStreamConfigurations(i);
            if (streamConfigurations.size() == 0) {
                this.mStreamConfigurationMap.put(i, (StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP));
            } else {
                this.mStreamConfigurationMap.put(i, CompatibilityUtils.createStreamConfigMap(streamConfigurations, this.mCharacteristics));
            }
        }
        return (StreamConfigurationMap) this.mStreamConfigurationMap.get(i);
    }

    private List getStreamConfigurations(int i) {
        ArrayList arrayList = new ArrayList();
        boolean isTagDefined = isTagDefined(CameraCharacteristicsVendorTags.SCALER_AVAILABLE_LIMIT_STREAM_CONFIGURATIONS.getName());
        boolean isTagDefined2 = isTagDefined(CameraCharacteristicsVendorTags.SCALER_AVAILABLE_SR_STREAM_CONFIGURATIONS.getName());
        boolean isTagDefined3 = isTagDefined(CameraCharacteristicsVendorTags.QCFA_STREAM_CONFIGURATIONS.getName());
        if (isTagDefined2 && isUltraPixelPhotographyMode(i)) {
            addStreamConfigurationToList(arrayList, CameraCharacteristicsVendorTags.SCALER_AVAILABLE_SR_STREAM_CONFIGURATIONS);
            return arrayList;
        } else if (!isSupportedQcfa() || !isTagDefined3 || (!isQcfaMode() && ((isTagDefined || isSupportedAndroidScalerStream()) && !isUltraPixelPhotographyMode(i) && !C0122O00000o.instance().OOoO0oO()))) {
            if (isTagDefined) {
                addStreamConfigurationToList(arrayList, CameraCharacteristicsVendorTags.SCALER_AVAILABLE_LIMIT_STREAM_CONFIGURATIONS);
            }
            if (arrayList.size() == 0) {
                addStreamConfigurationToList(arrayList, MiCameraCompat.getDefaultSteamConfigurationsTag());
            }
            return arrayList;
        } else {
            if (i != 36867) {
                addStreamConfigurationToList(arrayList, CameraCharacteristicsVendorTags.QCFA_STREAM_CONFIGURATIONS);
            }
            addStreamConfigurationToList(arrayList, isTagDefined ? CameraCharacteristicsVendorTags.SCALER_AVAILABLE_LIMIT_STREAM_CONFIGURATIONS : CameraCharacteristicsVendorTags.SCALER_AVAILABLE_STREAM_CONFIGURATIONS);
            return arrayList;
        }
    }

    private boolean isSupportedAndroidScalerStream() {
        return (C0124O00000oO.O0o00O0 || C0124O00000oO.O0o00o) && getFacing() == 1;
    }

    private boolean isUltraPixelPhotographyMode(int i) {
        return i == 33011 || i == 36868 || i == 33013 || i == 36871;
    }

    private void outputSizeDebugLog(Size[] sizeArr) {
        if (android.util.Log.isLoggable("OUTPUT_SIZE", 2) && Util.isDebugOsBuild()) {
            Log.d(TAG, "==================support output size list:=======================");
            Log.d(TAG, Arrays.toString(sizeArr));
        }
    }

    public int get8KMaxFpsSupported() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SPECIAL_VIDEOSIZE.getName())) {
            Log.w(TAG, "get8KMaxFpsSupported SPECIAL_VIDEOSIZE is not defined");
            return 30;
        }
        Integer[] numArr = (Integer[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SPECIAL_VIDEOSIZE);
        if (numArr == null) {
            Log.w(TAG, "get8KMaxFpsSupported.support is null");
            return 30;
        } else if (numArr.length % 4 != 0) {
            Log.e(TAG, "get8KMaxFpsSupported.support.length % 4 != 0");
            return 30;
        } else {
            int i = 0;
            while (i < numArr.length) {
                if (numArr[i].intValue() == 7680 && numArr[i + 1].intValue() == 4320) {
                    return numArr[i + 2].intValue();
                }
                i += 4;
            }
            return 30;
        }
    }

    public Rect getActiveArraySize() {
        Object obj = (!isSupportedQcfa() || !isTagDefined(CameraCharacteristicsVendorTags.QCFA_ACTIVE_ARRAY_SIZE.getName())) ? this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE) : VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.QCFA_ACTIVE_ARRAY_SIZE);
        return (Rect) obj;
    }

    public int getAiColorCorrectionVersion() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_AI_COLOR_CORRECTION_VERSION.getName()) ? (Byte) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_AI_COLOR_CORRECTION_VERSION) : null;
        if (b == null) {
            return 0;
        }
        return b.byteValue();
    }

    public int getAiVideoColorCorrectionVersion() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_AI_ENHANCED_VIDEO.getName()) ? (Byte) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_AI_ENHANCED_VIDEO) : null;
        if (b == null) {
            return 0;
        }
        return b.byteValue();
    }

    public int getAnchorFrameMask() {
        int i = 0;
        if (!isSupportAnchorFrame() || !isTagDefined(CameraCharacteristicsVendorTags.ANCHOR_FRAME_MASK.getName())) {
            return 0;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.ANCHOR_FRAME_MASK);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public int getBeautyVersion() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.BEAUTY_VERSION.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.BEAUTY_VERSION) : null;
        if (b != null) {
            return b.byteValue();
        }
        return -1;
    }

    public CameraSize getBokeBufferSize() {
        if (isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_BOKEH_DEPTH_BUFFER_Size.getName())) {
            int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_BOKEH_DEPTH_BUFFER_Size);
            if (iArr != null && iArr.length >= 2 && iArr[0] > 0 && iArr[1] > 0) {
                return new CameraSize(iArr[0], iArr[1]);
            }
        }
        return null;
    }

    public byte[] getCameraCalibrationData() {
        if (isTagDefined(CameraCharacteristicsVendorTags.CAM_CALIBRATION_DATA.getName())) {
            return (byte[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.CAM_CALIBRATION_DATA);
        }
        return null;
    }

    public CameraCharacteristics getCameraCharacteristics() {
        return this.mCharacteristics;
    }

    public int getCameraId() {
        return this.mCameraId;
    }

    public int getCameraRoleId() {
        Integer num = isTagDefined(CameraCharacteristicsVendorTags.CAMERA_ROLE_ID.getName()) ? (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.CAMERA_ROLE_ID) : null;
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    public int[] getCameraRoleIds() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.CAMERA_ROLE_IDS.getName())) {
            return null;
        }
        int[] iArr = (int[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.CAMERA_ROLE_IDS);
        if (iArr == null || iArr.length <= 0) {
            return null;
        }
        return iArr;
    }

    public HashSet getCaptureRequestVendorKeys() {
        return this.mCaptureRequestVendorKeys;
    }

    public int getEndOfStreamType() {
        int i = 0;
        if (!isTagDefined(CameraCharacteristicsVendorTags.END_OF_STREAM_TYPE.getName())) {
            return 0;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.END_OF_STREAM_TYPE);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public Range getExposureCompensationRange() {
        return (Range) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_RANGE);
    }

    public Rational getExposureCompensationRational() {
        return (Rational) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
    }

    public float getExposureCompensationStep() {
        Rational rational = (Rational) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_COMPENSATION_STEP);
        if (rational != null) {
            return rational.floatValue();
        }
        return 1.0f;
    }

    public Range getExposureTimeRange() {
        Range range = (Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_EXPOSURE_TIME_RANGE);
        if (range == null) {
            range = new Range(Long.valueOf(0), Long.valueOf(0));
        }
        if (!isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_SENSOR_INFO_EXPOSURE_RANGE.getName())) {
            return range;
        }
        long[] jArr = (long[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_SENSOR_INFO_EXPOSURE_RANGE);
        return (jArr == null || jArr.length != 2) ? range : new Range(Long.valueOf(Math.min(jArr[0], ((Long) range.getLower()).longValue())), Long.valueOf(Math.max(jArr[1], ((Long) range.getUpper()).longValue())));
    }

    public int getFacing() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.LENS_FACING);
        if (num != null) {
            return num.intValue();
        }
        return 1;
    }

    public Range getIsoRange() {
        Range range = (Range) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_SENSITIVITY_RANGE);
        Integer valueOf = Integer.valueOf(0);
        if (range == null) {
            range = new Range(valueOf, valueOf);
        }
        if (!isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_SENSOR_INFO_SENSITIVITY_RANGE.getName())) {
            return range;
        }
        int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_SENSOR_INFO_SENSITIVITY_RANGE);
        return (iArr == null || iArr.length != 2) ? range : new Range(Integer.valueOf(Math.min(iArr[0], ((Integer) range.getLower()).intValue())), Integer.valueOf(Math.max(iArr[1], ((Integer) range.getUpper()).intValue())));
    }

    public int getMaxExposureCompensation() {
        Range exposureCompensationRange = getExposureCompensationRange();
        if (exposureCompensationRange == null) {
            return 0;
        }
        return ((Integer) exposureCompensationRange.getUpper()).intValue();
    }

    public int getMaxFaceCount() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_MAX_FACE_COUNT);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public int getMaxIso() {
        Range isoRange = getIsoRange();
        if (isoRange == null) {
            return 0;
        }
        return ((Integer) isoRange.getUpper()).intValue();
    }

    public int getMaxJpegSize() {
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.ANDROID_JPEG_MAX_SIZE);
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }

    public float getMaxZoomRatio() {
        Float f = (Float) this.mCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM);
        if (f != null) {
            return f.floatValue();
        }
        return 1.0f;
    }

    public float getMiAlgoASDVersion() {
        if (this.mCaptureRequestVendorKeys == null || !isTagDefined(CameraCharacteristicsVendorTags.MI_ALGO_ASD_VERSION.getName())) {
            return 0.0f;
        }
        Float f = (Float) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.MI_ALGO_ASD_VERSION);
        if (f == null) {
            return 0.0f;
        }
        return f.floatValue();
    }

    public int getMinIso() {
        Range isoRange = getIsoRange();
        if (isoRange == null) {
            return 0;
        }
        return ((Integer) isoRange.getLower()).intValue();
    }

    public float getMinZoomRatio(float f) {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SCALER_AVAILABLE_MIN_DIGITAL_ZOOM.getName())) {
            return f;
        }
        Float f2 = (Float) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SCALER_AVAILABLE_MIN_DIGITAL_ZOOM);
        return f2 != null ? f2.floatValue() : f;
    }

    public float getMinimumFocusDistance() {
        Float f = (Float) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        if (f != null) {
            return f.floatValue();
        }
        return 0.0f;
    }

    public int getOperatingMode() {
        return this.mOperatingMode;
    }

    public int getOptimalMasterBokehId() {
        int i = -1;
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_MASTER_ID.getName())) {
            return -1;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_MASTER_ID);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0035, code lost:
        if (r8.equals(com.android.camera.data.data.config.ComponentConfigRatio.RATIO_FULL_19X9) != false) goto L_0x0061;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Size getOptimalMasterBokehSize(String str) {
        String str2;
        String str3;
        Size[] convertToSizes = convertToSizes(getOptimalMasterBokehSizes());
        if (convertToSizes != null) {
            char c = 3;
            if (convertToSizes.length >= 3) {
                switch (str.hashCode()) {
                    case -2109552250:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18_7_5X9)) {
                            c = 5;
                            break;
                        }
                    case 53743:
                        if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                            c = 0;
                            break;
                        }
                    case 1515430:
                        if (str.equals(ComponentConfigRatio.RATIO_16X9)) {
                            c = 1;
                            break;
                        }
                    case 1517352:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                            c = 2;
                            break;
                        }
                    case 1518313:
                        break;
                    case 1539455:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_20X9)) {
                            c = 6;
                            break;
                        }
                    case 1456894192:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                            c = 4;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        return convertToSizes[0];
                    case 1:
                        return convertToSizes[1];
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return convertToSizes[2];
                    default:
                        str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("not supported master size in portrait, ratio ");
                        sb.append(str);
                        str3 = sb.toString();
                        break;
                }
                Log.d(str2, str3);
                return null;
            }
        }
        str2 = TAG;
        str3 = "could not get master optimal size";
        Log.d(str2, str3);
        return null;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0035, code lost:
        if (r8.equals(com.android.camera.data.data.config.ComponentConfigRatio.RATIO_FULL_19X9) != false) goto L_0x0061;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Size getOptimalPictureBokehSize(String str) {
        String str2;
        String str3;
        Size[] convertToSizes = convertToSizes(getOptimalPictureBokehSizes());
        if (convertToSizes != null) {
            char c = 3;
            if (convertToSizes.length >= 3) {
                switch (str.hashCode()) {
                    case -2109552250:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18_7_5X9)) {
                            c = 5;
                            break;
                        }
                    case 53743:
                        if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                            c = 0;
                            break;
                        }
                    case 1515430:
                        if (str.equals(ComponentConfigRatio.RATIO_16X9)) {
                            c = 1;
                            break;
                        }
                    case 1517352:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                            c = 2;
                            break;
                        }
                    case 1518313:
                        break;
                    case 1539455:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_20X9)) {
                            c = 6;
                            break;
                        }
                    case 1456894192:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                            c = 4;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        return convertToSizes[0];
                    case 1:
                        return convertToSizes[1];
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return convertToSizes[2];
                    default:
                        str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("not supported picture size in portrait, ratio ");
                        sb.append(str);
                        str3 = sb.toString();
                        break;
                }
                Log.d(str2, str3);
                return null;
            }
        }
        str2 = TAG;
        str3 = "could not get optimal picture size";
        Log.d(str2, str3);
        return null;
    }

    public int getOptimalSlaveBokehId() {
        int i = -1;
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_SLAVE_ID.getName())) {
            return -1;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_SLAVE_ID);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0035, code lost:
        if (r8.equals(com.android.camera.data.data.config.ComponentConfigRatio.RATIO_FULL_19X9) != false) goto L_0x0061;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Size getOptimalSlaveBokehSize(String str) {
        String str2;
        String str3;
        Size[] convertToSizes = convertToSizes(getOptimalSlaveBokehSizes());
        if (convertToSizes != null) {
            char c = 3;
            if (convertToSizes.length >= 3) {
                switch (str.hashCode()) {
                    case -2109552250:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18_7_5X9)) {
                            c = 5;
                            break;
                        }
                    case 53743:
                        if (str.equals(ComponentConfigRatio.RATIO_4X3)) {
                            c = 0;
                            break;
                        }
                    case 1515430:
                        if (str.equals(ComponentConfigRatio.RATIO_16X9)) {
                            c = 1;
                            break;
                        }
                    case 1517352:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_18X9)) {
                            c = 2;
                            break;
                        }
                    case 1518313:
                        break;
                    case 1539455:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_20X9)) {
                            c = 6;
                            break;
                        }
                    case 1456894192:
                        if (str.equals(ComponentConfigRatio.RATIO_FULL_195X9)) {
                            c = 4;
                            break;
                        }
                    default:
                        c = 65535;
                        break;
                }
                switch (c) {
                    case 0:
                        return convertToSizes[0];
                    case 1:
                        return convertToSizes[1];
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        return convertToSizes[2];
                    default:
                        str2 = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("not supported slave size in portrait, ratio ");
                        sb.append(str);
                        str3 = sb.toString();
                        break;
                }
                Log.d(str2, str3);
                return null;
            }
        }
        str2 = TAG;
        str3 = "could not get slave optimal size";
        Log.d(str2, str3);
        return null;
    }

    public Set getPhysicalCameraIds() {
        return CompatibilityUtils.getPhysicalCameraIds(this.mCharacteristics);
    }

    public int[] getPortraitLightingArray() {
        int[] intArray = CameraAppImpl.getAndroidContext().getResources().getIntArray(R.array.portrait_lighting_array_default);
        if (getPortraitLightingVersion() < 2 || !isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_PORTRAIT_LIGHTING_ARRAY.getName())) {
            return intArray;
        }
        Integer[] numArr = (Integer[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_PORTRAIT_LIGHTING_ARRAY);
        if (numArr == null) {
            return intArray;
        }
        int[] iArr = new int[numArr.length];
        for (int i = 0; i < iArr.length; i++) {
            iArr[i] = numArr[i].intValue();
        }
        return iArr;
    }

    public int getPortraitLightingVersion() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_LIGHTING_VERSION.getName())) {
            return 1;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.PORTRAIT_LIGHTING_VERSION);
        if (num == null) {
            return 1;
        }
        return num.intValue();
    }

    public SatFusionCalibrationData[] getSatFusionCalibrationDataArray() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SAT_FUSION_SHOT_CALIBRATION_INFO.getName())) {
            Log.d(TAG, "getSatFusionCalibrationInfoArray: tag undefined");
            return null;
        }
        SatFusionCalibrationData[] satFusionCalibrationDataArr = (SatFusionCalibrationData[]) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SAT_FUSION_SHOT_CALIBRATION_INFO);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSatFusionCalibrationDataArray: ");
        sb.append(satFusionCalibrationDataArr);
        Log.d(str, sb.toString());
        return satFusionCalibrationDataArr;
    }

    public int getScreenLightBrightness() {
        Integer num = isTagDefined(CameraCharacteristicsVendorTags.SCREEN_LIGHT_BRIGHTNESS.getName()) ? (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SCREEN_LIGHT_BRIGHTNESS) : null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Screen light brightness: ");
        sb.append(num);
        Log.d(str, sb.toString());
        if (num != null) {
            return num.intValue();
        }
        return -1;
    }

    public int getSensorOrientation() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public List getSuperNightLimitSize() {
        ArrayList<StreamConfiguration> arrayList = new ArrayList<>();
        ArrayList arrayList2 = new ArrayList();
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPER_NIGHT_LIMIT_STREAM.getName())) {
            return null;
        }
        addStreamConfigurationToList(arrayList, CameraCharacteristicsVendorTags.SUPER_NIGHT_LIMIT_STREAM);
        for (StreamConfiguration streamConfiguration : arrayList) {
            CameraSize cameraSize = new CameraSize(streamConfiguration.getWidth(), streamConfiguration.getHeight());
            if (!arrayList2.contains(cameraSize)) {
                arrayList2.add(cameraSize);
            }
        }
        return arrayList2;
    }

    public List getSupportFakeSatJpegSizes() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.FAKE_SAT_JPEG_SIZE.getName())) {
            Log.d(TAG, "getSupportFakeSatJpegSizes: tag undefined");
            return null;
        }
        Size[] sizeArr = (Size[]) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.FAKE_SAT_JPEG_SIZE);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSupportFakeSatJpegSizes: ");
        sb.append(Arrays.toString(sizeArr));
        Log.d(str, sb.toString());
        return convertToPictureSize(sizeArr);
    }

    public List getSupportFakeSatYuvSizes() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.FAKE_SAT_YUV_SIZE.getName())) {
            Log.d(TAG, "getSupportFakeSatYuvSizes: tag undefined");
            return null;
        }
        Size[] sizeArr = (Size[]) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.FAKE_SAT_YUV_SIZE);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getSupportFakeSatYuvSizes: ");
        sb.append(Arrays.toString(sizeArr));
        Log.d(str, sb.toString());
        return convertToPictureSize(sizeArr);
    }

    public int[] getSupportedAWBModes() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AWB_AVAILABLE_MODES);
    }

    public int[] getSupportedAntiBandingModes() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_ANTIBANDING_MODES);
    }

    public int[] getSupportedColorEffects() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_EFFECTS);
    }

    public List getSupportedCustomFpsRange() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.CUSTOM_HFR_FPS_TABLE.getName())) {
            return null;
        }
        int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.CUSTOM_HFR_FPS_TABLE);
        if (iArr != null) {
            return MiCustomFpsRange.unmarshal(iArr);
        }
        return null;
    }

    public int[] getSupportedFlashModes() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_MODES);
    }

    public int[] getSupportedFocusModes() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AF_AVAILABLE_MODES);
    }

    public Range[] getSupportedFpsRange() {
        return (Range[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
    }

    public List getSupportedHFRVideoFPSList(Size size) {
        ArrayList arrayList = new ArrayList();
        Range[] supportedHighSpeedVideoFPSRange = getSupportedHighSpeedVideoFPSRange(size);
        int length = supportedHighSpeedVideoFPSRange.length;
        for (int i = 0; i < length; i++) {
            Range range = supportedHighSpeedVideoFPSRange[i];
            if (((Integer) range.getUpper()).equals(range.getLower()) && !arrayList.contains(range.getUpper())) {
                arrayList.add((Integer) range.getUpper());
            }
        }
        return arrayList;
    }

    public int getSupportedHardwareLevel() {
        return ((Integer) this.mCharacteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)).intValue();
    }

    public int getSupportedHdrType() {
        int i = -1;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORTED_HDR_TYPE.getName())) {
            return -1;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORTED_HDR_TYPE);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public List getSupportedHeicOutputStreamSizes() {
        StreamConfiguration[] streamConfigurationArr = (StreamConfiguration[]) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_SCALER_HEIC_STREAM_CONFIGURATIONS);
        ArrayList arrayList = new ArrayList();
        if (streamConfigurationArr == null) {
            return arrayList;
        }
        Arrays.stream(streamConfigurationArr).forEach(new O000000o(arrayList));
        return arrayList;
    }

    public Range[] getSupportedHighSpeedVideoFPSRange(Size size) {
        Range[] highSpeedVideoFpsRangesFor = ((StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getHighSpeedVideoFpsRangesFor(size);
        ArrayList arrayList = new ArrayList();
        if (C0124O00000oO.isMTKPlatform()) {
            if (!isSupportSlowMotionVideoConfiguration()) {
                return highSpeedVideoFpsRangesFor;
            }
            SlowMotionVideoConfiguration[] slowMotionVideoConfiguration = getSlowMotionVideoConfiguration();
            if (slowMotionVideoConfiguration == null) {
                return highSpeedVideoFpsRangesFor;
            }
            int length = slowMotionVideoConfiguration.length;
            for (int i = 0; i < length; i++) {
                SlowMotionVideoConfiguration slowMotionVideoConfiguration2 = slowMotionVideoConfiguration[i];
                if (size != null && size.getWidth() == slowMotionVideoConfiguration2.width && size.getHeight() == slowMotionVideoConfiguration2.height) {
                    arrayList.add(new Range(Integer.valueOf(slowMotionVideoConfiguration2.maxFps), Integer.valueOf(slowMotionVideoConfiguration2.maxFps)));
                }
            }
        } else if (!isSupportExtraHighSpeedVideoConfiguration()) {
            return highSpeedVideoFpsRangesFor;
        } else {
            List<MiHighSpeedVideoConfiguration> extraHighSpeedVideoConfiguration = getExtraHighSpeedVideoConfiguration();
            if (extraHighSpeedVideoConfiguration == null) {
                return highSpeedVideoFpsRangesFor;
            }
            for (MiHighSpeedVideoConfiguration miHighSpeedVideoConfiguration : extraHighSpeedVideoConfiguration) {
                if (size != null && size.equals(miHighSpeedVideoConfiguration.getSize())) {
                    arrayList.add(miHighSpeedVideoConfiguration.getFpsRange());
                }
            }
        }
        if (highSpeedVideoFpsRangesFor != null) {
            Collections.addAll(arrayList, highSpeedVideoFpsRangesFor);
        }
        return (Range[]) arrayList.toArray(new Range[arrayList.size()]);
    }

    public Size[] getSupportedHighSpeedVideoSize() {
        Size[] highSpeedVideoSizes = ((StreamConfigurationMap) this.mCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)).getHighSpeedVideoSizes();
        ArrayList arrayList = new ArrayList();
        if (C0124O00000oO.isMTKPlatform()) {
            if (!isSupportSlowMotionVideoConfiguration()) {
                return highSpeedVideoSizes;
            }
            SlowMotionVideoConfiguration[] slowMotionVideoConfiguration = getSlowMotionVideoConfiguration();
            if (slowMotionVideoConfiguration == null) {
                return highSpeedVideoSizes;
            }
            for (SlowMotionVideoConfiguration slowMotionVideoConfiguration2 : slowMotionVideoConfiguration) {
                Size size = new Size(slowMotionVideoConfiguration2.width, slowMotionVideoConfiguration2.height);
                if (!arrayList.contains(size)) {
                    arrayList.add(size);
                }
            }
        } else if (!isSupportExtraHighSpeedVideoConfiguration()) {
            return highSpeedVideoSizes;
        } else {
            List<MiHighSpeedVideoConfiguration> extraHighSpeedVideoConfiguration = getExtraHighSpeedVideoConfiguration();
            if (extraHighSpeedVideoConfiguration == null) {
                return highSpeedVideoSizes;
            }
            for (MiHighSpeedVideoConfiguration size2 : extraHighSpeedVideoConfiguration) {
                Size size3 = size2.getSize();
                if (!arrayList.contains(size3)) {
                    arrayList.add(size3);
                }
            }
        }
        if (highSpeedVideoSizes != null) {
            for (Size size4 : highSpeedVideoSizes) {
                if (!arrayList.contains(size4)) {
                    arrayList.add(size4);
                }
            }
        }
        return (Size[]) arrayList.toArray(new Size[0]);
    }

    public List getSupportedOutputSizeWithAssignedMode(int i) {
        return getSupportedOutputSizeWithAssignedMode(i, this.mOperatingMode);
    }

    public List getSupportedOutputSizeWithAssignedMode(int i, int i2) {
        StreamConfigurationMap streamConfigurationMap = getStreamConfigurationMap(i2);
        if (streamConfigurationMap == null) {
            return new ArrayList(0);
        }
        if (!C0122O00000o.instance().OOo0OOo()) {
            return convertToPictureSize(streamConfigurationMap.getOutputSizes(i));
        }
        ArrayList arrayList = new ArrayList();
        arrayList.addAll(convertToPictureSize(streamConfigurationMap.getHighResolutionOutputSizes(i)));
        arrayList.addAll(convertToPictureSize(streamConfigurationMap.getOutputSizes(i)));
        return arrayList;
    }

    public List getSupportedOutputSizeWithAssignedMode(Class cls) {
        return getSupportedOutputSizeWithTargetMode(cls, this.mOperatingMode);
    }

    public List getSupportedOutputSizeWithTargetMode(Class cls, int i) {
        StreamConfigurationMap streamConfigurationMap = getStreamConfigurationMap(i);
        if (streamConfigurationMap == null) {
            return new ArrayList(0);
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("get output sizes class:");
        sb.append(cls.getSimpleName());
        Log.c(str, sb.toString());
        Size[] outputSizes = streamConfigurationMap.getOutputSizes(cls);
        outputSizeDebugLog(outputSizes);
        return convertToPictureSize(outputSizes);
    }

    public List getSupportedOutputStreamSizes(int i) {
        ArrayList arrayList = new ArrayList();
        for (VendorTag vendorTag : STREAM_CONFIGURATIONS_VENDOR_KEYS) {
            if (isTagDefined(vendorTag.getName())) {
                StreamConfiguration[] streamConfigurationArr = (StreamConfiguration[]) VendorTagHelper.getValue(this.mCharacteristics, vendorTag);
                if (streamConfigurationArr != null) {
                    if (streamConfigurationArr.length > 0) {
                        int length = streamConfigurationArr.length;
                        for (int i2 = 0; i2 < length; i2++) {
                            StreamConfiguration streamConfiguration = streamConfigurationArr[i2];
                            if (streamConfiguration.getFormat() == i && streamConfiguration.isOutput()) {
                                arrayList.add(new CameraSize(streamConfiguration.getSize()));
                            }
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    public int[] getSupportedSceneModes() {
        return (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_SCENE_MODES);
    }

    public List getSupportedThumbnailSizes() {
        return convertToPictureSize((Size[]) this.mCharacteristics.get(CameraCharacteristics.JPEG_AVAILABLE_THUMBNAIL_SIZES));
    }

    public Range getSupportedVideoExposureDelay() {
        if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_EXPOSURE_DELAY_SUPPORTED.getName())) {
            int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_EXPOSURE_DELAY_SUPPORTED);
            if (iArr != null && iArr.length == 2) {
                return new Range(Integer.valueOf(iArr[0]), Integer.valueOf(iArr[1]));
            }
        }
        return new Range(Integer.valueOf(30), Integer.valueOf(30));
    }

    public CameraSize getTuningBufferSize(int i) {
        VendorTag vendorTag = i == 1 ? CameraCharacteristicsVendorTags.CONTROL_CAPTURE_ISP_TUNING_DATA_SIZE_FOR_YUV : CameraCharacteristicsVendorTags.CONTROL_CAPTURE_ISP_TUNING_DATA_SIZE_FOR_RAW;
        if (isTagDefined(vendorTag.getName())) {
            int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, vendorTag);
            if (iArr != null && iArr.length >= 2 && iArr[0] > 0 && iArr[1] > 0) {
                return new CameraSize(iArr[0], iArr[1]);
            }
        }
        return null;
    }

    public int getVideoSatSupportedQualities() {
        int i = 0;
        if (!supportVideoSatQualityTag()) {
            return 0;
        }
        Integer num = (Integer) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORTED_VIDEO_SAT_QUALITY_RANGE);
        if (num != null) {
            i = num.intValue();
        }
        return i;
    }

    public float getViewAngle(boolean z) {
        float[] fArr = (float[]) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS);
        String str = "vertical";
        String str2 = "horizontal";
        if (fArr != null && fArr.length > 0) {
            float f = fArr[0];
            SizeF sizeF = (SizeF) this.mCharacteristics.get(CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE);
            if (sizeF != null) {
                float height = z ? sizeF.getHeight() : sizeF.getWidth();
                if (height > 0.0f) {
                    float degrees = (float) (Math.toDegrees(Math.atan((((double) height) * 0.5d) / ((double) f))) * 2.0d);
                    String str3 = TAG;
                    Locale locale = Locale.US;
                    Object[] objArr = new Object[4];
                    if (!z) {
                        str = str2;
                    }
                    objArr[0] = str;
                    objArr[1] = Float.valueOf(degrees);
                    objArr[2] = sizeF;
                    objArr[3] = Float.valueOf(f);
                    Log.d(str3, String.format(locale, "%s view angle: %.2f, size = %s, focalLength = %.4f", objArr));
                    return degrees;
                }
            }
        }
        String str4 = TAG;
        Locale locale2 = Locale.US;
        Object[] objArr2 = new Object[1];
        if (!z) {
            str = str2;
        }
        objArr2[0] = str;
        Log.e(str4, String.format(locale2, "failed to get %s view angle", objArr2));
        return DEFAULT_VIEW_ANGLE;
    }

    public int getXiaomiYuvFormat() {
        Integer num = isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_YUV_FORMAT.getName()) ? (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_YUV_FORMAT) : null;
        if (num == null) {
            return -1;
        }
        return num.intValue();
    }

    public boolean hasStandaloneHeicStreamConfigurations() {
        return isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_SCALER_HEIC_STREAM_CONFIGURATIONS.getName());
    }

    public boolean is4K60FpsEISSupported() {
        if (isTagDefined(CameraCharacteristicsVendorTags.EIS_4K_60FPS_SUPPORTED.getName())) {
            Byte b = (Byte) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.EIS_4K_60FPS_SUPPORTED);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("is4K60FpsEISSupported: ");
            sb.append(b);
            Log.d(str, sb.toString());
            boolean z = true;
            if (b == null || b.byteValue() != 1) {
                z = false;
            }
            return z;
        }
        Log.d(TAG, "is4K60FpsEISSupported: false");
        return false;
    }

    public boolean is60fpsDynamicSupported() {
        if (isTagDefined(CameraCharacteristicsVendorTags.DYNAMIC_60FPS_SUPPORTED.getName())) {
            Byte b = (Byte) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.DYNAMIC_60FPS_SUPPORTED);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("is60fpsDynamicSupported: ");
            sb.append(b);
            Log.d(str, sb.toString());
            boolean z = true;
            if (b == null || b.byteValue() != 1) {
                z = false;
            }
            return z;
        }
        Log.d(TAG, "is60fpsDynamicSupported: false");
        return false;
    }

    @TargetApi(23)
    public boolean isAELockSupported() {
        Boolean bool = (Boolean) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AE_LOCK_AVAILABLE);
        return bool != null && bool.booleanValue();
    }

    public boolean isAERegionSupported() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.CONTROL_MAX_REGIONS_AE);
        return num != null && num.intValue() > 0;
    }

    public boolean isAFRegionSupported() {
        Float f = (Float) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_MINIMUM_FOCUS_DISTANCE);
        return f != null && f.floatValue() > 0.0f;
    }

    public boolean isASDSceneSupported() {
        return isTagDefined(CaptureRequestVendorTags.AI_SCENE_APPLY.getName());
    }

    @TargetApi(23)
    public boolean isAWBLockSupported() {
        Boolean bool = (Boolean) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AWB_LOCK_AVAILABLE);
        return bool != null && bool.booleanValue();
    }

    public boolean isAdaptiveSnapshotSizeInSatModeSupported() {
        String str = "isAdaptiveSnapshotSizeInSatModeSupported(): false";
        boolean z = false;
        if (this.mCameraId != Camera2DataContainer.getInstance().getSATCameraId()) {
            Log.d(TAG, str);
            return false;
        } else if (!isTagDefined(CameraCharacteristicsVendorTags.ADAPTIVE_SNAPSHOT_SIZE_IN_SAT_MODE_SUPPORTED.getName())) {
            Log.d(TAG, str);
            return false;
        } else {
            Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.ADAPTIVE_SNAPSHOT_SIZE_IN_SAT_MODE_SUPPORTED);
            if (bool != null && bool.booleanValue()) {
                z = true;
            }
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isAdaptiveSnapshotSizeInSatModeSupported(): ");
            sb.append(z);
            Log.d(str2, sb.toString());
            return z;
        }
    }

    public boolean isAiMoonEffectEnableSupported() {
        return isTagDefined(CaptureRequestVendorTags.AI_MOON_EFFECT_ENABLED.getName());
    }

    public boolean isAnchorFrameType(int i, int i2) {
        return (getAnchorFrameMask() & getAnchorFrameMaskBit(i, i2)) != 0;
    }

    public boolean isAutoFocusSupported() {
        return Util.isSupported(1, getSupportedFocusModes());
    }

    public boolean isBackSoftLightSupported() {
        return isTagDefined(CaptureRequestVendorTags.BACK_SOFT_LIGHT.getName());
    }

    public boolean isBackwardCaptureSupported() {
        return isTagDefined(CaptureRequestVendorTags.BACKWARD_CAPTURE_HINT.getName());
    }

    public boolean isCinematicPhotoSupported() {
        return isTagDefined(CaptureRequestVendorTags.CINEMATIC_PHOTO_ENABLED.getName());
    }

    public boolean isCinematicVideoSupported() {
        return isTagDefined(CaptureRequestVendorTags.CINEMATIC_VIDEO_ENABLED.getName());
    }

    public boolean isCurrentQualitySupportEis(int i, int i2) {
        if (!isTagDefined(CameraCharacteristicsVendorTags.EIS_QUALITY_SUPPORTED.getName())) {
            Log.w(TAG, "isCurrentQualitySupportEis EIS_QUALITY_SUPPORTED is not defined");
            return false;
        }
        Integer[] numArr = (Integer[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.EIS_QUALITY_SUPPORTED);
        if (numArr == null) {
            Log.w(TAG, "isCurrentQualitySupportEis.support is null");
            return false;
        } else if (numArr.length % 2 != 0) {
            Log.e(TAG, "isCurrentQualitySupportEis.support.length % 2 != 0");
            return false;
        } else {
            int i3 = 0;
            while (i3 < numArr.length) {
                if (numArr[i3].intValue() == i && numArr[i3 + 1].intValue() == i2) {
                    return true;
                }
                i3 += 2;
            }
            return false;
        }
    }

    public boolean isDebugInfoAsWatermarkSupported() {
        return isTagDefined(CaptureRequestVendorTags.DEBUG_INFO_AS_WATERMARK.getName());
    }

    public boolean isDynamicFpsConfigSupported() {
        return isTagDefined(CaptureRequestVendorTags.DYNAMIC_FPS_CONFIG.getName());
    }

    public boolean isEISPreviewSupported() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.EIS_PREVIEW_SUPPORTED.getName())) {
            return false;
        }
        Byte b = (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.EIS_PREVIEW_SUPPORTED);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isEISPreviewSupported: ");
        sb.append(b);
        Log.d(str, sb.toString());
        boolean z = true;
        if (b == null || b.byteValue() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isFaceDetectionSupported() {
        int[] iArr = (int[]) this.mCharacteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
        if (iArr != null) {
            for (int i : iArr) {
                if (i == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isFixedFocus() {
        return getMinimumFocusDistance() > 0.0f;
    }

    public boolean isFlashSupported() {
        Boolean bool = (Boolean) this.mCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        return bool != null && bool.booleanValue();
    }

    public boolean isFovcSupported() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.FOVC_SUPPORTED.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.FOVC_SUPPORTED) : null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isFovcSupported: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b != null && b.byteValue() == 1;
    }

    public boolean isHeicSupported() {
        return CompatibilityUtils.isHeicSupported(this.mCharacteristics);
    }

    public boolean isHistogramStatsSupported() {
        return isTagDefined(CaptureRequestVendorTags.HISTOGRAM_STATS_ENABLED.getName());
    }

    public boolean isLLSSupported() {
        return isTagDefined(CaptureResultVendorTags.IS_LLS_NEEDED.getName());
    }

    public boolean isMFNRBokehSupported() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.MFNR_BOKEH_SUPPORTED.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.MFNR_BOKEH_SUPPORTED) : null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isMFNRBokehSupported: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b != null && b.byteValue() == 1;
    }

    public boolean isMacroHdrMutex() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.MACRO_HDR_MUTEX.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.MACRO_HDR_MUTEX) : null;
        return b != null && b.byteValue() == 1;
    }

    public boolean isMfnrMacroZoomSupported() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.MACRO_ZOOM_FEATURE.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.MACRO_ZOOM_FEATURE) : null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isMfnrMacroZoomSupported: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b != null && b.byteValue() == 1;
    }

    public boolean isMotionDetectionSupported() {
        return isTagDefined(CaptureResultVendorTags.HDR_MOTION_DETECTED.getName());
    }

    public boolean isMtkPipDevicesSupported() {
        return isTagDefined(CaptureRequestVendorTags.MTK_STREAMING_FEATURE_PIP_DEVICES.getName());
    }

    public boolean isPartialMetadataSupported() {
        Integer num = (Integer) this.mCharacteristics.get(CameraCharacteristics.REQUEST_PARTIAL_RESULT_COUNT);
        return num != null && num.intValue() > 1;
    }

    public boolean isPreCaptureSupportAF() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_PRECAPTUREAF_SUPPORTED.getName())) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_PRECAPTUREAF_SUPPORTED);
        return num != null && num.intValue() == 1;
    }

    public boolean isQcfaMode() {
        int i = this.mOperatingMode;
        return i == 32775 || i == 36865 || C0122O00000o.instance().O000O0Oo(CameraSettings.isFrontCamera());
    }

    public boolean isReal8K() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SPECIAL_VIDEOSIZE.getName())) {
            Log.w(TAG, "isReal8K SPECIAL_VIDEOSIZE is not defined");
            return false;
        }
        Integer[] numArr = (Integer[]) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SPECIAL_VIDEOSIZE);
        if (numArr == null) {
            Log.w(TAG, "isReal8K.support is null");
            return false;
        } else if (numArr.length % 4 != 0) {
            Log.e(TAG, "isReal8K.support.length % 4 != 0");
            return false;
        } else {
            int i = 0;
            while (i < numArr.length) {
                if (numArr[i].intValue() == 7680 && numArr[i + 1].intValue() == 4320) {
                    int i2 = i + 3;
                    if (i2 < numArr.length && numArr[i2].intValue() == 1) {
                        z = true;
                    }
                    return z;
                }
                i += 4;
            }
            return false;
        }
    }

    public boolean isRemosaicDetecedSupported() {
        return isTagDefined(CaptureResultVendorTags.REMOSAIC_DETECTED.getName());
    }

    public boolean isSatFusionShotSupported() {
        String str;
        String sb;
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_SAT_FUSION_SHOT.getName())) {
            str = TAG;
            sb = "isSatFusionShotSupported: false, because tag undefined";
        } else {
            Byte b = (Byte) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_SAT_FUSION_SHOT);
            if (b != null && b.byteValue() > 0) {
                z = true;
            }
            str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("isSatFusionShotSupported: ");
            sb2.append(z);
            sb = sb2.toString();
        }
        Log.d(str, sb);
        return z;
    }

    public boolean isSatPipSupported() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_SAT_PIP.getName())) {
            return false;
        }
        Byte b = (Byte) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_SAT_PIP);
        boolean z = true;
        if (b == null || b.intValue() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isScreenLightHintSupported() {
        return isTagDefined(CaptureRequestVendorTags.SCREEN_LIGHT_HINT.getName());
    }

    public boolean isSensorDepurpleDisable() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.SENSOR_DEPURPLE_DISABLE.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SENSOR_DEPURPLE_DISABLE) : null;
        return b != null && b.byteValue() == 1;
    }

    public boolean isSensorHdrSupported() {
        return isTagDefined(CaptureResultVendorTags.SENSOR_HDR_ENABLE.getName());
    }

    public boolean isSessionKeyDefined(String str) {
        HashSet hashSet = this.mSessionKeys;
        return hashSet != null && hashSet.contains(str);
    }

    public boolean isSpecshotModeSupported() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SPECSHOT_MODE.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SPECSHOT_MODE);
        return bool != null && bool.booleanValue();
    }

    public boolean isSuperNightExifTagDefined() {
        return isTagDefined(CaptureResultVendorTags.SUPER_NIGHT_EXIF.getName());
    }

    public boolean isSupportAIIE() {
        return getAiColorCorrectionVersion() >= 1;
    }

    public boolean isSupportAiShutter() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.XIAOMI_AISHUTTER_SUPPORTED.getName())) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.XIAOMI_AISHUTTER_SUPPORTED);
        return num != null && num.intValue() == 1;
    }

    public boolean isSupportAmbilightAutoAeTag() {
        return isTagDefined(CaptureRequestVendorTags.AMBILIGHT_MODE.getName());
    }

    public boolean isSupportAnchorFrame() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_ANCHOR_FRAME.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_ANCHOR_FRAME);
        if (bool != null) {
            z = bool.booleanValue();
        }
        return z;
    }

    public boolean isSupportAutoHdr() {
        return isTagDefined(CaptureRequestVendorTags.HDR_CHECKER_ENABLE.getName());
    }

    public boolean isSupportBeauty() {
        return isTagDefined(CaptureRequestVendorTags.BEAUTY_LEVEL.getName());
    }

    public boolean isSupportBeautyMakeup() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.BEAUTY_MAKEUP.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.BEAUTY_MAKEUP);
        return bool != null && bool.booleanValue();
    }

    public boolean isSupportBeautyType(String str) {
        if (BeautyConstant.BEAUTY_TYPE_VENDOR_TAG_MAP.containsKey(str)) {
            return isTagDefined(((VendorTag) BeautyConstant.BEAUTY_TYPE_VENDOR_TAG_MAP.get(str)).getName());
        }
        return false;
    }

    public boolean isSupportBokehAdjust() {
        return isTagDefined(CaptureRequestVendorTags.BOKEH_F_NUMBER.getName());
    }

    public boolean isSupportBurstFps() {
        return isTagDefined(CaptureRequestVendorTags.BURST_SHOOT_FPS.getName());
    }

    public boolean isSupportBurstHint() {
        return isTagDefined(CaptureRequestVendorTags.BURST_CAPTURE_HINT.getName());
    }

    public boolean isSupportCameraAi30() {
        return isTagDefined(CaptureRequestVendorTags.CAMERA_AI_30.getName());
    }

    public boolean isSupportColorRetentionBackRequestTag() {
        return isTagDefined(CaptureRequestVendorTags.VIDEO_FILTER_COLOR_RETENTION_BACK.getName());
    }

    public boolean isSupportColorRetentionFrontRequestTag() {
        return isTagDefined(CaptureRequestVendorTags.VIDEO_FILTER_COLOR_RETENTION_FRONT.getName());
    }

    public boolean isSupportContrast() {
        return isTagDefined(CaptureRequestVendorTags.CONTRAST_LEVEL.getName());
    }

    public boolean isSupportCustomFlashCurrent() {
        return isTagDefined(CaptureRequestVendorTags.FLASH_CURRENT.getName());
    }

    public boolean isSupportCustomWatermark() {
        return isTagDefined(CaptureRequestVendorTags.CUSTOM_WATERMARK_TEXT.getName());
    }

    public boolean isSupportDepurple() {
        return isTagDefined(CaptureRequestVendorTags.DEPURPLE.getName());
    }

    public boolean isSupportDeviceOrientation() {
        return isTagDefined(CaptureRequestVendorTags.DEVICE_ORIENTATION.getName());
    }

    public boolean isSupportDualBokeh() {
        return isTagDefined(CaptureRequestVendorTags.DUAL_BOKEH_ENABLE.getName());
    }

    public boolean isSupportExtraHighSpeedVideoConfiguration() {
        return isTagDefined(CameraCharacteristicsVendorTags.EXTRA_HIGH_SPEED_VIDEO_CONFIGURATIONS.getName()) && isTagDefined(CameraCharacteristicsVendorTags.EXTRA_HIGH_SPEED_VIDEO_NUMBER.getName());
    }

    public boolean isSupportEyeLight() {
        return isTagDefined(CaptureRequestVendorTags.EYE_LIGHT_TYPE.getName());
    }

    public boolean isSupportFaceAgeAnalyze() {
        return isTagDefined(CaptureRequestVendorTags.FACE_AGE_ANALYZE_ENABLED.getName());
    }

    public boolean isSupportFaceScore() {
        return isTagDefined(CaptureRequestVendorTags.FACE_SCORE_ENABLED.getName());
    }

    public boolean isSupportFastZoomIn() {
        return isTagDefined(CaptureResultVendorTags.FAST_ZOOM_RESULT.getName());
    }

    public boolean isSupportFlashHdr() {
        Boolean valueOf = Boolean.valueOf(false);
        if (isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_FLASH_HDR.getName())) {
            valueOf = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_FLASH_HDR);
        }
        return valueOf != null && valueOf.booleanValue();
    }

    public boolean isSupportFrontMirror() {
        return isTagDefined(CaptureRequestVendorTags.FRONT_MIRROR.getName());
    }

    public boolean isSupportHFRDeflicker() {
        return isTagDefined(CaptureRequestVendorTags.DEFLICKER_ENABLED.getName());
    }

    public boolean isSupportHHT() {
        return isTagDefined(CaptureRequestVendorTags.HHT_ENABLED.getName());
    }

    public boolean isSupportHdr() {
        return isTagDefined(CaptureRequestVendorTags.HDR_ENABLED.getName());
    }

    public boolean isSupportHdrBokeh() {
        return isTagDefined(CaptureRequestVendorTags.HDR_BOKEH_ENABLED.getName());
    }

    public boolean isSupportHdrCheckerStatus() {
        return isTagDefined(CaptureRequestVendorTags.HDR_CHECKER_STATUS.getName());
    }

    public boolean isSupportHdrMode() {
        return isTagDefined(CaptureRequestVendorTags.HDR_MODE.getName());
    }

    public boolean isSupportHighQualityPreferred() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.HIGHQUALITY_PREFERRED_SUPPORTED.getName())) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.HIGHQUALITY_PREFERRED_SUPPORTED);
        if (num == null || num.intValue() != 1) {
            return false;
        }
        return isTagDefined(CaptureRequestVendorTags.HIGHQUALITY_PREFERRED.getName());
    }

    public boolean isSupportHistogram() {
        return isTagDefined(CaptureResultVendorTags.HISTOGRAM_STATS.getName());
    }

    public boolean isSupportLensDirtyDetect() {
        return isTagDefined(CaptureRequestVendorTags.LENS_DIRTY_DETECT.getName());
    }

    public boolean isSupportLightTripartite() {
        Boolean valueOf = Boolean.valueOf(false);
        if (isTagDefined(CameraCharacteristicsVendorTags.TRIPARTITE_LIGHT.getName())) {
            valueOf = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.TRIPARTITE_LIGHT);
        }
        return valueOf != null && valueOf.booleanValue();
    }

    public boolean isSupportMacroMode() {
        return isTagDefined(CaptureRequestVendorTags.MACRO_MODE.getName());
    }

    public boolean isSupportMegviiDualBokeh() {
        return supportDualBokehVendor() == BokehVendor.MEGVII_DUAL_BOKEH.ordinal();
    }

    public boolean isSupportMfnr() {
        return isTagDefined(CaptureRequestVendorTags.MFNR_ENABLED.getName());
    }

    public boolean isSupportMiBokeh() {
        return isTagDefined(CaptureRequestVendorTags.SINGLE_CAMERA_BOKEH.getName());
    }

    public boolean isSupportMiDualBokeh() {
        return supportDualBokehVendor() == BokehVendor.MI_DUAL_BOKEH.ordinal();
    }

    public boolean isSupportNormalWideLDC() {
        if (isTagDefined(CaptureRequestVendorTags.NORMAL_WIDE_LENS_DISTORTION_CORRECTION_LEVEL.getName())) {
            Log.d(TAG, "isSupportNormalWideLDC: true");
            return true;
        }
        Log.d(TAG, "isSupportNormalWideLDC: false");
        return false;
    }

    public boolean isSupportOIS() {
        int[] iArr = (int[]) this.mCharacteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_OPTICAL_STABILIZATION);
        if (iArr == null || iArr.length == 0) {
            return false;
        }
        return (iArr.length == 1 && iArr[0] == 0) ? false : true;
    }

    public boolean isSupportOptimalBokehSize() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.PORTRAIT_OPTIMAL_MASTER_SIZE.getName())) {
            return false;
        }
        int[] optimalMasterBokehSizes = getOptimalMasterBokehSizes();
        if (optimalMasterBokehSizes != null && optimalMasterBokehSizes.length > 0) {
            z = true;
        }
        return z;
    }

    public boolean isSupportP2done() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.P2_KEY_SUPPORT_MODES.getName())) {
            return false;
        }
        int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.P2_KEY_SUPPORT_MODES);
        if (iArr != null && iArr.length > 0) {
            for (int i : iArr) {
                if (i == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSupportParallel() {
        return isTagDefined(CaptureRequestVendorTags.PARALLEL_ENABLED.getName());
    }

    public boolean isSupportParallelCameraDevice() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.PARALLEL_CAMERA_DEVICE.getName())) {
            return false;
        }
        Byte b = (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.PARALLEL_CAMERA_DEVICE);
        boolean z = true;
        if (b == null || b.byteValue() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isSupportPortraitLighting() {
        return isTagDefined(CaptureRequestVendorTags.PORTRAIT_LIGHTING.getName());
    }

    public boolean isSupportQcomVideoHdr() {
        return isTagDefined(CaptureRequestVendorTags.QCOM_VIDEO_HDR_ENABLED.getName());
    }

    public boolean isSupportRaw() {
        return contains((int[]) this.mCharacteristics.get(CameraCharacteristics.REQUEST_AVAILABLE_CAPABILITIES), 3);
    }

    public boolean isSupportSlowMotionVideoConfiguration() {
        return isTagDefined(CameraCharacteristicsVendorTags.SLOW_MOTION_VIDEO_CONFIGURATIONS.getName());
    }

    public boolean isSupportSnapShotTorch() {
        return isTagDefined(CaptureRequestVendorTags.SNAP_SHOT_TORCH.getName());
    }

    public boolean isSupportSuperNight() {
        return isTagDefined(CaptureRequestVendorTags.SUPER_NIGHT_SCENE_ENABLED.getName());
    }

    public boolean isSupportSuperResolution() {
        return isTagDefined(CaptureRequestVendorTags.SUPER_RESOLUTION_ENABLED.getName());
    }

    public boolean isSupportSwMfnr() {
        return isTagDefined(CaptureRequestVendorTags.SW_MFNR_ENABLED.getName());
    }

    public boolean isSupportTargetZoom() {
        return isTagDefined(CaptureRequestVendorTags.TARGET_ZOOM.getName());
    }

    public boolean isSupportUltraWideLDC() {
        if (isTagDefined(CaptureRequestVendorTags.ULTRA_WIDE_LENS_DISTORTION_CORRECTION_LEVEL.getName())) {
            Log.d(TAG, "isSupportUltraWideLDC: true");
            return true;
        }
        Log.d(TAG, "isSupportUltraWideLDC: false");
        return false;
    }

    public boolean isSupportVideoBeauty() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_BEAUTY.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_BEAUTY);
        return bool != null && bool.booleanValue();
    }

    public boolean isSupportVideoBeautyScreenshot() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_BEAUTY_SCREENSHOT_SUPPORTED.getName())) {
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_BEAUTY_SCREENSHOT_SUPPORTED);
        boolean z = true;
        if (num == null || num.intValue() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isSupportVideoBokehAdjust() {
        boolean z = true;
        if (CameraSettings.isFrontCamera()) {
            if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_BOKEH_FRONT_ADJUEST.getName())) {
                Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_BOKEH_FRONT_ADJUEST);
                if (bool == null || !bool.booleanValue()) {
                    z = false;
                }
                return z;
            }
        } else if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_BOKEH_ADJUEST.getName())) {
            Boolean bool2 = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_BOKEH_ADJUEST);
            if (bool2 == null || !bool2.booleanValue()) {
                z = false;
            }
            return z;
        }
        return false;
    }

    public boolean isSupportVideoBokehColorRetention(boolean z) {
        boolean z2;
        if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_COLOR_BOKEH_VERSION.getName())) {
            Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_COLOR_BOKEH_VERSION);
            if (num != null && num.intValue() > 0) {
                z2 = true;
                return !z2 && isSupportVideoBokehColorRetentionTag(z);
            }
        }
        z2 = false;
        if (!z2) {
        }
    }

    public boolean isSupportVideoBokehColorRetentionTag(boolean z) {
        return isTagDefined((z ? CaptureRequestVendorTags.VIDEO_BOKEH_COLOR_RETENTION_FRONT_MODE : CaptureRequestVendorTags.VIDEO_BOKEH_COLOR_RETENTION_BACK_MODE).getName());
    }

    public boolean isSupportVideoBokehRequestTag(boolean z) {
        return isTagDefined((z ? CaptureRequestVendorTags.VIDEO_BOKEH_FRONT_LEVEL : CaptureRequestVendorTags.VIDEO_BOKEH_BACK_LEVEL).getName());
    }

    public boolean isSupportVideoFilter() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_FILTER.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_FILTER);
        return bool != null && bool.booleanValue();
    }

    public boolean isSupportVideoFilterColorRetentionBack() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_COLOR_RENTENTION_BACK.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_COLOR_RENTENTION_BACK);
        return bool != null && bool.booleanValue();
    }

    public boolean isSupportVideoFilterColorRetentionFront() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_COLOR_RENTENTION_FRONT.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_COLOR_RENTENTION_FRONT);
        return bool != null && bool.booleanValue();
    }

    public boolean isSupportVideoFilterRequestTag() {
        return isTagDefined(CaptureRequestVendorTags.VIDEO_FILTER_ID.getName());
    }

    public boolean isSupportVideoHdr() {
        if (isSupportQcomVideoHdr()) {
            return true;
        }
        if (!isTagDefined(CameraCharacteristicsVendorTags.HDR_KEY_AVAILABLE_HDR_MODES_VIDEO.getName()) || !isTagDefined(CaptureRequestVendorTags.MTK_HDR_KEY_DETECTION_MODE.getName())) {
            return false;
        }
        int[] iArr = (int[]) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.HDR_KEY_AVAILABLE_HDR_MODES_VIDEO);
        return iArr != null && iArr.length > 1;
    }

    public boolean isSupportVideoMasterFilter() {
        if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_MASTER_FILTER.getName())) {
            Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_MASTER_FILTER);
            if (num != null) {
                boolean z = true;
                if (num.intValue() != 1) {
                    z = false;
                }
                return z;
            }
        }
        return false;
    }

    public boolean isSupportWatermark() {
        return isTagDefined(CaptureRequestVendorTags.WATERMARK_APPLIEDTYPE.getName());
    }

    public boolean isSupportZeroDegreeOrientationImage() {
        if (isTagDefined(CameraCharacteristicsVendorTags.ENABLE_ZERO_DEGREE_ORIENTATION_IMAGE.getName())) {
            Integer num = (Integer) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.ENABLE_ZERO_DEGREE_ORIENTATION_IMAGE);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("isSupportZeroDegreeOrientationImage: ");
            sb.append(num);
            Log.d(str, sb.toString());
            boolean z = true;
            if (num == null || num.intValue() != 1) {
                z = false;
            }
            return z;
        }
        Log.d(TAG, "isSupportZeroDegreeOrientationImage: false");
        return false;
    }

    public boolean isSupportedColorEnhance() {
        Boolean valueOf = Boolean.valueOf(false);
        if (isTagDefined(CameraCharacteristicsVendorTags.COLOR_ENHANCE.getName())) {
            valueOf = (Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.COLOR_ENHANCE);
        }
        return valueOf != null && valueOf.booleanValue();
    }

    public boolean isSupportedQcfa() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.IS_QCFA_SENSOR.getName())) {
            return false;
        }
        Byte b = (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.IS_QCFA_SENSOR);
        boolean z = true;
        if (b == null || b.byteValue() != 1) {
            z = false;
        }
        return z;
    }

    public boolean isSupportedSuperPortrait() {
        if (isTagDefined(CameraCharacteristicsVendorTags.SUPER_PORTRAIT_SUPPORTED.getName())) {
            return ((Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPER_PORTRAIT_SUPPORTED)).booleanValue();
        }
        return false;
    }

    public boolean isSupportedVideoLogFormat() {
        if (isTagDefined(CameraCharacteristicsVendorTags.LOG_FORMAT.getName())) {
            return ((Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.LOG_FORMAT)).booleanValue();
        }
        return false;
    }

    public boolean isSupportedVideoMiMovie() {
        if (isTagDefined(CameraCharacteristicsVendorTags.VIDEO_MIMOVIE.getName())) {
            return ((Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_MIMOVIE)).booleanValue();
        }
        return false;
    }

    public boolean isTagDefined(String str) {
        HashSet hashSet = this.mCaptureRequestVendorKeys;
        return hashSet != null && hashSet.contains(str);
    }

    public boolean isTeleMacro(int i) {
        boolean z = false;
        if (!C0122O00000o.instance().OOoOOo() || !CameraSettings.isMacroModeEnabled(i)) {
            return false;
        }
        if (getCameraId() == Camera2DataContainer.getInstance().getAuxCameraId()) {
            z = true;
        }
        return z;
    }

    public boolean isTeleOISSupported() {
        Byte b = isTagDefined(CameraCharacteristicsVendorTags.TELE_OIS_SUPPORTED.getName()) ? (Byte) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.TELE_OIS_SUPPORTED) : null;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isTeleOISSupported: ");
        sb.append(b);
        Log.d(str, sb.toString());
        return b != null && b.byteValue() == 1;
    }

    public boolean isUltraPixelPhotographySupported(Size size) {
        if (size == null) {
            return false;
        }
        for (CameraSize cameraSize : getSupportedOutputStreamSizes(33)) {
            if (size.getWidth() == cameraSize.getWidth() && size.getHeight() == cameraSize.getHeight()) {
                return true;
            }
        }
        return false;
    }

    public boolean isUltraPixelPortraitTagDefined() {
        return isTagDefined(CaptureRequestVendorTags.ULTRA_PIXEL_PORTRAIT_ENABLED.getName());
    }

    public boolean isVideoBeautyForceEis() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.VIDEO_BEAUTY_FORCE_EIS.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.VIDEO_BEAUTY_FORCE_EIS);
        if (bool != null && bool.booleanValue()) {
            z = true;
        }
        return z;
    }

    public boolean isVideoHDR10PlusSupported() {
        String str;
        String sb;
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_VIDEO_HDR10.getName())) {
            str = TAG;
            sb = "isVideoHDR10Supported: false, because tag undefined";
        } else {
            Integer num = (Integer) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_VIDEO_HDR10);
            if (num != null && num.intValue() == 2) {
                z = true;
            }
            str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("isVideoHDR10+Supported: ");
            sb2.append(z);
            sb = sb2.toString();
        }
        Log.d(str, sb);
        return z;
    }

    public boolean isVideoHDR10Supported() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_VIDEO_HDR10.getName())) {
            Log.d(TAG, "isVideoHDR10Supported: false, because tag undefined");
            return false;
        }
        Integer num = (Integer) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_VIDEO_HDR10);
        boolean z = true;
        if (num == null || num.intValue() != 1) {
            z = false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("isVideoHDR10Supported: ");
        sb.append(z);
        Log.d(str, sb.toString());
        return z;
    }

    public boolean isVideoStabilizationSupported() {
        int[] iArr = (int[]) this.mCharacteristics.get(CameraCharacteristics.CONTROL_AVAILABLE_VIDEO_STABILIZATION_MODES);
        return iArr != null && iArr.length > 1;
    }

    public boolean isZoomRatioSupported() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_ZOOM_RATIO_TAG.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueSafely(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_ZOOM_RATIO_TAG);
        if (bool != null) {
            z = bool.booleanValue();
        }
        return z;
    }

    public boolean isZoomSupported() {
        return getMaxZoomRatio() > 1.0f;
    }

    public void setOperatingMode(int i) {
        this.mOperatingMode = i;
    }

    public boolean supportAiEnhancedVideo() {
        return getAiVideoColorCorrectionVersion() >= 1;
    }

    public int supportDualBokehVendor() {
        if (isTagDefined(CameraCharacteristicsVendorTags.MI_DUAL_BOKEH_VENDOR.getName())) {
            Integer num = (Integer) VendorTagHelper.getValue(this.mCharacteristics, CameraCharacteristicsVendorTags.MI_DUAL_BOKEH_VENDOR);
            if (num != null) {
                return num.intValue();
            }
        }
        return BokehVendor.ARCSOFT_DUAL_BOKEH.ordinal();
    }

    public boolean supportFakeSat() {
        boolean z = true;
        boolean z2 = isTagDefined(CameraCharacteristicsVendorTags.FAKE_SAT_YUV_SIZE.getName()) && isTagDefined(CameraCharacteristicsVendorTags.FAKE_SAT_JPEG_SIZE.getName());
        if (!z2) {
            return false;
        }
        List supportFakeSatYuvSizes = getSupportFakeSatYuvSizes();
        List supportFakeSatJpegSizes = getSupportFakeSatJpegSizes();
        if (supportFakeSatYuvSizes == null || supportFakeSatYuvSizes.size() <= 0 || supportFakeSatJpegSizes == null || supportFakeSatJpegSizes.size() <= 0) {
            z = false;
        }
        return z;
    }

    public boolean supportMoonAutoFocus() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_MOON_AUTO_FOCUS.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_MOON_AUTO_FOCUS);
        if (bool == null) {
            Log.e(TAG, "supportMoonAutoFocus tag value:null");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("supportMoonAutoFocus:");
        sb.append(bool.booleanValue());
        Log.e(str, sb.toString());
        return bool.booleanValue();
    }

    public boolean supportNearRangeMode() {
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_NEAR_RANGE_MODE.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_NEAR_RANGE_MODE);
        boolean booleanValue = bool != null ? bool.booleanValue() : false;
        if (isTagDefined(CaptureRequestVendorTags.SAT_FALLBACK_DISABLE.getName()) && isTagDefined(CaptureResultVendorTags.SAT_FALLBACKROLE.getName())) {
            return booleanValue;
        }
        return false;
    }

    public boolean supportPhysicCameraId() {
        boolean z = false;
        if (!isTagDefined(CameraCharacteristicsVendorTags.SUPPORT_PHYSIC_CAMERA_ID.getName())) {
            return false;
        }
        Boolean bool = (Boolean) VendorTagHelper.getValueQuietly(this.mCharacteristics, CameraCharacteristicsVendorTags.SUPPORT_PHYSIC_CAMERA_ID);
        if (bool != null && bool.booleanValue()) {
            z = true;
        }
        return z;
    }

    public boolean supportSATUltraWideLDCEnable() {
        return isTagDefined(CaptureRequestVendorTags.SAT_ULTRA_WIDE_LENS_DISTORTION_CORRECTION_ENABLE.getName());
    }

    public boolean supportVideoSatQualityTag() {
        return isTagDefined(CameraCharacteristicsVendorTags.SUPPORTED_VIDEO_SAT_QUALITY_RANGE.getName());
    }
}
