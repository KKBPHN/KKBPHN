package com.android.camera2.vendortag;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CameraCharacteristics.Key;
import android.hardware.camera2.params.StreamConfiguration;
import android.util.Size;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.struct.SatFusionCalibrationData;
import com.android.camera2.vendortag.struct.SlowMotionVideoConfiguration;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CameraCharacteristicsVendorTags {
    public static final VendorTag ADAPTIVE_SNAPSHOT_SIZE_IN_SAT_MODE_SUPPORTED = create(C0459O000o0o0.INSTANCE, Boolean.class);
    public static final VendorTag AI_SCENE_AVAILABLE_MODES = create(C0438O000O0oo.INSTANCE, int[].class);
    public static final VendorTag ANCHOR_FRAME_MASK = create(O000Oo0.INSTANCE, Integer.class);
    public static final VendorTag ANDROID_JPEG_MAX_SIZE = create(C0465O000oO0o.INSTANCE, Integer.TYPE);
    public static final VendorTag BEAUTY_MAKEUP = create(C0468O000oOOO.INSTANCE, Boolean.class);
    public static final VendorTag BEAUTY_VERSION = create(C0424O0000OoO.INSTANCE, Byte.class);
    public static final VendorTag CAMERA_ROLE_ID = create(C0480O000ooOO.INSTANCE, Integer.class);
    public static final VendorTag CAMERA_ROLE_IDS = create(C0462O000oO0.INSTANCE, int[].class);
    public static final VendorTag CAM_CALIBRATION_DATA = create(C0469O000oOOo.INSTANCE, byte[].class);
    public static final VendorTag COLOR_ENHANCE = create(C0430O0000oOO.INSTANCE, Boolean.class);
    public static final VendorTag CONTROL_CAPTURE_ISP_TUNING_DATA_SIZE_FOR_RAW = create(C0443O000Oo0o.INSTANCE, int[].class);
    public static final VendorTag CONTROL_CAPTURE_ISP_TUNING_DATA_SIZE_FOR_YUV = create(C0599O00oOooo.INSTANCE, int[].class);
    public static final VendorTag CUSTOM_HFR_FPS_TABLE = create(C0448O000Ooo.INSTANCE, int[].class);
    public static VendorTag DYNAMIC_60FPS_SUPPORTED = create(O000OOo0.INSTANCE, Byte.class);
    public static VendorTag EIS_4K_60FPS_SUPPORTED = create(C0428O0000oO.INSTANCE, Byte.class);
    public static final VendorTag EIS_PREVIEW_SUPPORTED = create(C0431O0000oOo.INSTANCE, Byte.class);
    public static VendorTag EIS_QUALITY_SUPPORTED = create(C0426O0000o0O.INSTANCE, Integer[].class);
    public static VendorTag ENABLE_ZERO_DEGREE_ORIENTATION_IMAGE = create(O0000o0.INSTANCE, Integer.class);
    public static final VendorTag END_OF_STREAM_TYPE = create(C0460O000o0oo.INSTANCE, Integer.class);
    public static final VendorTag EXTRA_HIGH_SPEED_VIDEO_CONFIGURATIONS = create(C0478O000ooO.INSTANCE, int[].class);
    public static final VendorTag EXTRA_HIGH_SPEED_VIDEO_NUMBER = create(O0000o.INSTANCE, Integer.class);
    public static final VendorTag FAKE_SAT_JPEG_SIZE = create(C0451O000Oooo.INSTANCE, Size[].class);
    public static final VendorTag FAKE_SAT_YUV_SIZE = create(C0458O000o0o.INSTANCE, Size[].class);
    public static final VendorTag FOVC_SUPPORTED = create(O0000Oo.INSTANCE, Byte.class);
    public static final VendorTag HDR_KEY_AVAILABLE_HDR_MODES_VIDEO = create(C0427O0000o0o.INSTANCE, int[].class);
    public static VendorTag HIGHQUALITY_PREFERRED_SUPPORTED = create(O000O0o.INSTANCE, Integer.class);
    public static final VendorTag IS_QCFA_SENSOR = create(O000o000.INSTANCE, Byte.class);
    public static final VendorTag LOG_FORMAT = create(C0453O000o00o.INSTANCE, Boolean.class);
    public static final VendorTag MACRO_HDR_MUTEX = create(O000OOOo.INSTANCE, Byte.class);
    public static final VendorTag MACRO_ZOOM_FEATURE = create(C0455O000o0O0.INSTANCE, Byte.class);
    public static final VendorTag MFNR_BOKEH_SUPPORTED = create(O000000o.INSTANCE, Byte.class);
    public static final VendorTag MI_ALGO_ASD_VERSION = create(O000OO00.INSTANCE, Float.class);
    public static final VendorTag MI_DUAL_BOKEH_VENDOR = create(C0470O000oOo.INSTANCE, Integer.class);
    public static final VendorTag P2_KEY_SUPPORT_MODES = create(C0477O000oo0o.INSTANCE, int[].class);
    public static final VendorTag PARALLEL_CAMERA_DEVICE = create(C0423O00000oo.INSTANCE, Byte.class);
    public static final VendorTag PORTRAIT_LIGHTING_VERSION = create(C0433O0000oo0.INSTANCE, Integer.class);
    public static final int PORTRAIT_LIGHTING_VERSION_1 = 1;
    public static final int PORTRAIT_LIGHTING_VERSION_2 = 2;
    public static VendorTag PORTRAIT_MASTER_ID = create(C0450O000OooO.INSTANCE, Integer.class);
    public static VendorTag PORTRAIT_OPTIMAL_MASTER_SIZE = create(C0464O000oO0O.INSTANCE, int[].class);
    public static VendorTag PORTRAIT_OPTIMAL_PICTURE_SIZE = create(C0461O000oO.INSTANCE, int[].class);
    public static VendorTag PORTRAIT_OPTIMAL_SLAVE__SIZE = create(C0437O000O0oO.INSTANCE, int[].class);
    public static VendorTag PORTRAIT_SLAVE_ID = create(C0474O000oo.INSTANCE, Integer.class);
    public static final VendorTag QCFA_ACTIVE_ARRAY_SIZE = create(C0447O000OoOo.INSTANCE, Rect.class);
    public static final VendorTag QCFA_STREAM_CONFIGURATIONS = create(C0471O000oOo0.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag SAT_FUSION_SHOT_CALIBRATION_INFO = create(C0488O00O0Oo.INSTANCE, SatFusionCalibrationData[].class);
    public static final VendorTag SCALER_AVAILABLE_LIMIT_STREAM_CONFIGURATIONS = create(C0466O000oOO.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag SCALER_AVAILABLE_MIN_DIGITAL_ZOOM = create(C0439O000OOoO.INSTANCE, Float.class);
    public static final VendorTag SCALER_AVAILABLE_SR_STREAM_CONFIGURATIONS = create(O0000OOo.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag SCALER_AVAILABLE_STREAM_CONFIGURATIONS = create(C0475O000oo0.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag SCREEN_LIGHT_BRIGHTNESS = create(C0445O000OoO0.INSTANCE, Integer.class);
    public static final VendorTag SENSOR_DEPURPLE_DISABLE = create(C0449O000Ooo0.INSTANCE, Byte.class);
    public static final VendorTag SLOW_MOTION_VIDEO_CONFIGURATIONS = create(C0473O000oOoo.INSTANCE, SlowMotionVideoConfiguration[].class);
    public static VendorTag SPECIAL_VIDEOSIZE = create(O000O0o0.INSTANCE, Integer[].class);
    public static final VendorTag SPECSHOT_MODE = create(O00000o.INSTANCE, Boolean.class);
    public static final VendorTag SUPER_NIGHT_LIMIT_STREAM = create(C0467O000oOO0.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag SUPER_PORTRAIT_SUPPORTED = create(O000OOo.INSTANCE, Boolean.class);
    public static final VendorTag SUPPORTED_HDR_TYPE = create(C0441O000Oo00.INSTANCE, Integer.class);
    public static final VendorTag SUPPORTED_VIDEO_SAT_QUALITY_RANGE = create(O0000Oo0.INSTANCE, Integer.class);
    public static final VendorTag SUPPORT_AI_ENHANCED_VIDEO = create(C0454O000o0O.INSTANCE, Byte.class);
    public static final VendorTag SUPPORT_ANCHOR_FRAME = create(O0000O0o.INSTANCE, Boolean.class);
    public static final VendorTag SUPPORT_FLASH_HDR = create(C0481O000ooOo.INSTANCE, Boolean.class);
    public static final VendorTag SUPPORT_MOON_AUTO_FOCUS = create(O000o.INSTANCE, Boolean.class);
    public static final VendorTag SUPPORT_NEAR_RANGE_MODE = create(O0000o00.INSTANCE, Boolean.class);
    public static VendorTag SUPPORT_PHYSIC_CAMERA_ID = create(C0436O000O0Oo.INSTANCE, Boolean.class);
    public static final VendorTag SUPPORT_PORTRAIT_LIGHTING_ARRAY = create(O000o0.INSTANCE, Integer[].class);
    public static final VendorTag SUPPORT_SAT_FUSION_SHOT = create(O000O0OO.INSTANCE, Byte.class);
    public static final VendorTag SUPPORT_SAT_PIP = create(C0457O000o0Oo.INSTANCE, Byte.class);
    public static final VendorTag SUPPORT_VIDEO_HDR10 = create(C0463O000oO00.INSTANCE, Integer.class);
    public static VendorTag SUPPORT_ZOOM_RATIO_TAG = create(C0440O000OOoo.INSTANCE, Boolean.class);
    private static final String TAG = "CameraCharacteristicsVendorTags";
    public static final VendorTag TELE_OIS_SUPPORTED = create(O00000o0.INSTANCE, Byte.class);
    public static final VendorTag TRIPARTITE_LIGHT = create(C0429O0000oO0.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_BEAUTY = create(C0456O000o0OO.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_BEAUTY_FORCE_EIS = create(C0444O000OoO.INSTANCE, Boolean.class);
    public static VendorTag VIDEO_BEAUTY_SCREENSHOT_SUPPORTED = create(C0422O00000oO.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_BOKEH_ADJUEST = create(C0425O0000Ooo.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_BOKEH_FRONT_ADJUEST = create(O000OO0o.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_COLOR_BOKEH_VERSION = create(C0432O0000oo.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_COLOR_RENTENTION_BACK = create(C0442O000Oo0O.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_COLOR_RENTENTION_FRONT = create(O000O00o.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_EXPOSURE_DELAY_SUPPORTED = create(C0435O0000ooo.INSTANCE, int[].class);
    public static final VendorTag VIDEO_FILTER = create(C0446O000OoOO.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_MASTER_FILTER = create(C0598O00oOooO.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_MIMOVIE = create(C0476O000oo0O.INSTANCE, Boolean.class);
    public static final VendorTag XIAOMI_AISHUTTER_SUPPORTED = create(C0479O000ooO0.INSTANCE, Integer.class);
    public static final VendorTag XIAOMI_AI_COLOR_CORRECTION_VERSION = create(C0472O000oOoO.INSTANCE, Byte.class);
    public static final VendorTag XIAOMI_BOKEH_DEPTH_BUFFER_Size = create(O000o00.INSTANCE, int[].class);
    public static final VendorTag XIAOMI_PRECAPTUREAF_SUPPORTED = create(C0434O0000ooO.INSTANCE, Integer.class);
    public static final VendorTag XIAOMI_SCALER_HEIC_STREAM_CONFIGURATIONS = create(O00000Oo.INSTANCE, StreamConfiguration[].class);
    public static final VendorTag XIAOMI_SENSOR_INFO_EXPOSURE_RANGE = create(C0452O000o00O.INSTANCE, long[].class);
    public static final VendorTag XIAOMI_SENSOR_INFO_SENSITIVITY_RANGE = create(O000OO.INSTANCE, int[].class);
    public static final VendorTag XIAOMI_YUV_FORMAT = create(C0595O00oOoOo.INSTANCE, Integer.class);
    private static Constructor characteristicsConstructor;

    static /* synthetic */ String O00oooo() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.qcfa.supported" : "org.codeaurora.qcamera3.quadra_cfa.is_qcfa_sensor";
    }

    static /* synthetic */ String O00oooo0() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.ai.asd.availableSceneMode" : "xiaomi.ai.asd.availableSceneMode";
    }

    static /* synthetic */ String O00ooooo() {
        return "com.xiaomi.camera.supportedfeatures.beautyMakeup";
    }

    static /* synthetic */ String O0O000o() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.scaler.availableStreamConfigurations" : "xiaomi.scaler.availableStreamConfigurations";
    }

    static /* synthetic */ String O0O00OO() {
        return "com.xiaomi.scaler.availableMinDigitalZoom";
    }

    static /* synthetic */ String O0O00Oo() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.scaler.availableLimitStreamConfigurations" : "xiaomi.scaler.availableLimitStreamConfigurations";
    }

    static /* synthetic */ String O0O00o() {
        return "org.codeaurora.qcamera3.quadra_cfa.availableStreamConfigurations";
    }

    static /* synthetic */ String O0O00o0() {
        return "org.codeaurora.qcamera3.quadra_cfa.activeArraySize";
    }

    static /* synthetic */ String O0O00oO() {
        return C0124O00000oO.isMTKPlatform() ? "com.mediatek.streamingfeature.availableHfpsMaxResolutions" : "org.quic.camera2.customhfrfps.info.CustomHFRFpsTable";
    }

    static /* synthetic */ String O0O0O() {
        return "com.xiaomi.camera.algoup.dualCalibrationData";
    }

    static /* synthetic */ String O0O0O0o() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.scaler.availableSuperResolutionStreamConfigurations" : "xiaomi.scaler.availableSuperResolutionStreamConfigurations";
    }

    static /* synthetic */ String O0O0OO() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.capabilities.videoStabilization.previewSupported" : "xiaomi.capabilities.videoStabilization.previewSupported";
    }

    static /* synthetic */ String O0O0OO0() {
        return "com.xiaomi.camera.supportedfeatures.videofilter";
    }

    static /* synthetic */ String O0O0OOO() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.flash.screenLight.brightness" : "xiaomi.flash.screenLight.brightness";
    }

    static /* synthetic */ String O0O0OOo() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.capabilities.mfnr_bokeh_supported" : "xiaomi.capabilities.mfnr_bokeh_supported";
    }

    static /* synthetic */ String O0O0Oo0() {
        return "xiaomi.capabilities.macro_zoom_feature";
    }

    static /* synthetic */ String O0O0OoO() {
        return "com.xiaomi.camera.supportedfeatures.fovcEnable";
    }

    static /* synthetic */ String O0O0Ooo() {
        return "com.xiaomi.camera.supportedfeatures.beautyVersion";
    }

    static /* synthetic */ String O0O0o() {
        return "com.xiaomi.camera.supportedfeatures.superVideoFilterVersion";
    }

    static /* synthetic */ String O0O0o0() {
        return "org.codeaurora.qcamera3.additional_hfr_video_sizes.valid_number";
    }

    static /* synthetic */ String O0O0o00() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.capabilities.satAdaptiveSnapshotSizeSupported" : "xiaomi.capabilities.satAdaptiveSnapshotSizeSupported";
    }

    static /* synthetic */ String O0O0o0O() {
        return "xiaomi.capabilities.videoStabilization.60fpsSupported";
    }

    static /* synthetic */ String O0O0o0o() {
        return C0124O00000oO.isMTKPlatform() ? "com.xiaomi.capabilities.videoStabilization.quality" : "xiaomi.capabilities.videoStabilization.quality";
    }

    static /* synthetic */ String O0O0oO() {
        return "xiaomi.capabilities.videoStabilization.60fpsDynamicSupported";
    }

    static /* synthetic */ String O0O0oO0() {
        return "com.xiaomi.gpu.enableGPURotation";
    }

    static /* synthetic */ String O0O0oOO() {
        return "com.mediatek.smvrfeature.availableSmvrModes";
    }

    static /* synthetic */ String O0O0oOo() {
        return "com.mediatek.control.capture.ispMetaSizeForYuv";
    }

    static /* synthetic */ String O0O0oo() {
        return "com.mediatek.hdrfeature.availableHdrModesVideo";
    }

    static /* synthetic */ String O0O0oo0() {
        return "com.xiaomi.camera.supportedfeatures.bokehDepthBufferSize";
    }

    static /* synthetic */ String O0O0ooO() {
        return "com.xiaomi.aishutter.supported";
    }

    static /* synthetic */ String O0O0ooo() {
        return "com.xiaomi.precaptureaf.supported";
    }

    static /* synthetic */ String O0OO00O() {
        return "com.xiaomi.camera.supportedfeatures.videoBokeh";
    }

    static /* synthetic */ String O0OO00o() {
        return "com.mediatek.control.capture.ispMetaSizeForRaw";
    }

    static /* synthetic */ String O0OO0O() {
        return "xiaomi.ai.misd.MiAlgoAsdVersion";
    }

    static /* synthetic */ String O0OO0Oo() {
        return "com.xiaomi.camera.supportedfeatures.TeleOisSupported";
    }

    static /* synthetic */ String O0OO0o0() {
        return "com.xiaomi.cameraid.role.cameraId";
    }

    static /* synthetic */ String O0OO0oO() {
        return "com.xiaomi.cameraid.role.cameraIds";
    }

    static /* synthetic */ String O0OO0oo() {
        return "com.xiaomi.camera.supportedfeatures.superportraitSupported";
    }

    static /* synthetic */ String O0OOO0() {
        return "com.xiaomi.camera.supportedfeatures.videoBokehFront";
    }

    static /* synthetic */ String O0OOO00() {
        return "com.xiaomi.camera.supportedfeatures.videomimovie";
    }

    static /* synthetic */ String O0OOO0O() {
        return "com.xiaomi.camera.supportedfeatures.isMacroMutexWithHdr";
    }

    static /* synthetic */ String O0OOO0o() {
        return "xiaomi.scaler.availableHeicStreamConfigurations";
    }

    static /* synthetic */ String O0OOOO() {
        return "com.xiaomi.camera.supportedfeatures.AIEnhancementVersion";
    }

    static /* synthetic */ String O0OOOOO() {
        return "xiaomi.sensor.info.sensitivityRange";
    }

    static /* synthetic */ String O0OOOOo() {
        return "com.xiaomi.camera.supportedfeatures.colorenhancement";
    }

    static /* synthetic */ String O0OOOo() {
        return "xiaomi.ai.supportedMoonAutoFocus";
    }

    static /* synthetic */ String O0OOOo0() {
        return "com.xiaomi.camera.supportedfeatures.parallelCameraDevice";
    }

    static /* synthetic */ String O0OOOoO() {
        return "com.xiaomi.camera.supportedfeatures.specshot";
    }

    static /* synthetic */ String O0OOOoo() {
        return "xiaomi.hdr.supportedFlashHdr";
    }

    static /* synthetic */ String O0OOo() {
        return "com.xiaomi.camera.supportedfeatures.satPip";
    }

    static /* synthetic */ String O0OOo00() {
        return "com.xiaomi.camera.supportedfeatures.videoColorRetentionFront";
    }

    static /* synthetic */ String O0OOo0O() {
        return "com.xiaomi.camera.supportedfeatures.bokehVendorID";
    }

    static /* synthetic */ String O0OOo0o() {
        return "xiaomi.capturefusion.supportCPFusion";
    }

    static /* synthetic */ String O0OOoO() {
        return "com.xiaomi.camera.supportedfeatures.3rdLightWeightSupported";
    }

    static /* synthetic */ String O0OOoO0() {
        return "com.xiaomi.camera.dualcal.info.dataInfo";
    }

    static /* synthetic */ String O0OOoOO() {
        return "com.xiaomi.fakesat.FakeSatYuvSize";
    }

    static /* synthetic */ String O0OOoOo() {
        return "com.xiaomi.fakesat.FakeSatJpegSize";
    }

    static /* synthetic */ String O0OOoo() {
        return "com.xiaomi.camera.supportedfeatures.videologformat";
    }

    static /* synthetic */ String O0OOoo0() {
        return "xiaomi.videohdrmode.value";
    }

    static /* synthetic */ String O0OOooO() {
        return "xiaomi.videosize.CustomSizes";
    }

    static /* synthetic */ String O0OOooo() {
        return "com.xiaomi.camera.supportedfeatures.bokehRelightVerion";
    }

    static /* synthetic */ String O0Oo() {
        return "xiaomi.camera.bokehinfo.slaveOptimalSize";
    }

    static /* synthetic */ String O0Oo0() {
        return "com.xiaomi.camera.supportedfeatures.videobeautyeis";
    }

    static /* synthetic */ String O0Oo00() {
        return "com.xiaomi.camera.supportedfeatures.videoColorRetentionBack";
    }

    static /* synthetic */ String O0Oo000() {
        return "com.xiaomi.camera.supportedfeatures.bokehRelightModes";
    }

    static /* synthetic */ String O0Oo00O() {
        return "xiaomi.smoothTransition.nearRangeMode";
    }

    static /* synthetic */ String O0Oo00o() {
        return "xiaomi.capabilities.quick_view_support";
    }

    static /* synthetic */ String O0Oo0O() {
        return "xiaomi.capabilities.isZoomRatioSupported";
    }

    static /* synthetic */ String O0Oo0O0() {
        return "android.jpeg.maxSize";
    }

    static /* synthetic */ String O0Oo0OO() {
        return "xiaomi.capabilities.isPhyicalMultiCameraSupported";
    }

    static /* synthetic */ String O0Oo0Oo() {
        return "com.xiaomi.camera.supportedfeatures.exposuredelayfps";
    }

    static /* synthetic */ String O0Oo0o() {
        return "xiaomi.camera.bokehinfo.masterCameraId";
    }

    static /* synthetic */ String O0Oo0o0() {
        return "xiaomi.sensorDepurple.disable";
    }

    static /* synthetic */ String O0Oo0oO() {
        return "xiaomi.camera.bokehinfo.slaveCameraId";
    }

    static /* synthetic */ String O0Oo0oo() {
        return "com.xiaomi.camera.supportedfeatures.colorBokehVersion";
    }

    static /* synthetic */ String O0OoO() {
        return "xiaomi.scaler.supernight.availableLimitStreamConfigurations";
    }

    static /* synthetic */ String O0OoO0() {
        return "com.xiaomi.camera.supportedfeatures.supportedAlgoEngineHdr";
    }

    static /* synthetic */ String O0OoO00() {
        return "xiaomi.camera.bokehinfo.optimalPictureSize";
    }

    static /* synthetic */ String O0OoO0O() {
        return "xiaomi.sensor.info.exposureTimeRange";
    }

    static /* synthetic */ String O0OoOO() {
        return "com.mediatek.control.capture.early.notification.support";
    }

    static /* synthetic */ String O0OoOO0() {
        return "xiaomi.capabilities.videoStabilization.endOfStreamType";
    }

    static /* synthetic */ String O0OoOOO() {
        return "xiaomi.capabilities.quick_view_mask";
    }

    static /* synthetic */ String O0OoOOo() {
        return "com.xiaomi.camera.supportedfeatures.videoBeauty";
    }

    static /* synthetic */ String O0OoOo() {
        return "xiaomi.imageQuality.available";
    }

    static /* synthetic */ String O0Oooo() {
        return "com.xiaomi.camera.supportedfeatures.videobeautyscreenshot";
    }

    static /* synthetic */ String OO0oO() {
        return "xiaomi.yuv.format";
    }

    static /* synthetic */ String OoO0o() {
        return "com.xiaomi.videosat.supportedRange";
    }

    static /* synthetic */ String OooOO() {
        return "com.xiaomi.camera.supportedfeatures.AIEnhancementVideoSupportVersion";
    }

    /* access modifiers changed from: private */
    public static Key characteristicsKey(String str, Class cls) {
        try {
            if (characteristicsConstructor == null) {
                characteristicsConstructor = Key.class.getConstructor(new Class[]{String.class, cls.getClass()});
                characteristicsConstructor.setAccessible(true);
            }
            return (Key) characteristicsConstructor.newInstance(new Object[]{str, cls});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find/call Key constructor: ");
            sb.append(e.getMessage());
            Log.d(TAG, sb.toString());
            return null;
        }
    }

    private static VendorTag create(final Supplier supplier, final Class cls) {
        return new VendorTag() {
            /* access modifiers changed from: protected */
            public Key create() {
                return CameraCharacteristicsVendorTags.characteristicsKey(getName(), cls);
            }

            public String getName() {
                return (String) supplier.get();
            }
        };
    }

    static /* synthetic */ String o00o00O() {
        return "xiaomi.camera.bokehinfo.masterOptimalSize";
    }

    static /* synthetic */ String ooooooo() {
        return "org.codeaurora.qcamera3.additional_hfr_video_sizes.hfr_video_size";
    }

    public static void preload() {
        Log.d(TAG, "preloading...");
    }
}
