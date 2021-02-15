package com.android.camera2.vendortag;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.graphics.Rect;
import android.hardware.camera2.CaptureRequest.Key;
import com.android.camera.log.Log;
import com.android.camera2.vendortag.struct.MarshalQueryableASDScene;
import com.android.camera2.vendortag.struct.MarshalQueryableDxoAsdScene.ASDScene;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class CaptureRequestVendorTags {
    public static final VendorTag AI_AIIE_PREVIEWENABLED = create(C0603O00oo000.INSTANCE, Boolean.class);
    public static final VendorTag AI_MOON_EFFECT_ENABLED = create(C0530O00o00OO.INSTANCE, Boolean.class);
    public static final VendorTag AI_SCENE = create(C0604O00oo00O.INSTANCE, Boolean.class);
    public static final VendorTag AI_SCENE_APPLY = create(C0526O00o000O.INSTANCE, Integer.class);
    public static final VendorTag AI_SCENE_PERIOD = create(O00O0o00.INSTANCE, Integer.class);
    public static final VendorTag ALGO_UP_ENABLED = create(C0576O00oOO0.INSTANCE, Boolean.class);
    public static final VendorTag AMBILIGHT_AE_TARGET = create(C0581O00oOOO0.INSTANCE, Integer.class);
    public static final VendorTag AMBILIGHT_MODE = create(C0594O00oOoOO.INSTANCE, Integer.class);
    public static final VendorTag ASD_DIRTY_ENABLE = create(C0596O00oOoo.INSTANCE, Byte.class);
    public static final VendorTag ASD_ENABLE = create(C0597O00oOoo0.INSTANCE, Boolean.class);
    public static final VendorTag AUTOZOOM_APPLY_IN_PREVIEW = create(O00oO000.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_CENTER_OFFSET = create(C0509O00OoO0o.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_FORCE_LOCK = create(C0504O00Oo0o.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_MINIMUM_SCALING = create(O00Ooo00.INSTANCE, Float.class);
    public static final VendorTag AUTOZOOM_MODE = create(C0593O00oOoO.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_SCALE_OFFSET = create(O00O00o.INSTANCE, Float.class);
    public static final VendorTag AUTOZOOM_SELECT = create(C0564O00oO00.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_START = create(C0495O00O0oOo.INSTANCE, float[].class);
    public static final VendorTag AUTOZOOM_STOP = create(oooOoO.INSTANCE, Integer.class);
    public static final VendorTag AUTOZOOM_UNSELECT = create(C0520O00OooOO.INSTANCE, Integer.class);
    public static final VendorTag BACKWARD_CAPTURE_HINT = create(C0617O00ooO0o.INSTANCE, Byte.class);
    public static final VendorTag BACK_SOFT_LIGHT = create(C0612O00oo0oo.INSTANCE, Byte.class);
    public static final VendorTag BEAUTY_BLUSHER = create(O00O0OO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_BODY_SLIM = create(C0554O00o0oO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_CHIN = create(C0507O00OoO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_ENLARGE_EYE = create(C0525O00Ooooo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_EYEBROW_DYE = create(C0529O00o00O0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_HAIRLINE = create(C0494O00O0oOO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_HEAD_SLIM = create(C0625O00ooOoo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_JELLY_LIPS = create(O00OO0O.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_LEG_SLIM = create(C0493O00O0oO0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_LEVEL = create(C0589O00oOo0.INSTANCE, String.class);
    public static final VendorTag BEAUTY_LIPS = create(C0574O00oO0oo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_NECK = create(C0502O00OOoo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_NOSE = create(C0500O00OO0o.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_PUPIL_LINE = create(C0528O00o00O.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_RISORIUS = create(C0624O00ooOoO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SHOULDER_SLIM = create(C0522O00Oooo.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SKIN_COLOR = create(C0555O00o0oO0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SKIN_SMOOTH = create(C0524O00OoooO.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SLIM_FACE = create(O00o00.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SLIM_NOSE = create(O00O00o0.INSTANCE, Integer.class);
    public static final VendorTag BEAUTY_SMILE = create(C0516O00Ooo0O.INSTANCE, Integer.class);
    public static final VendorTag BOKEH_F_NUMBER = create(C0627O00ooo0.INSTANCE, String.class);
    public static final VendorTag BURST_CAPTURE_HINT = create(C0606O00oo0O.INSTANCE, Integer.class);
    public static final VendorTag BURST_SHOOT_FPS = create(O00o000.INSTANCE, Integer.class);
    public static final VendorTag BUTT_SLIM = create(C0510O00OoOO.INSTANCE, Integer.class);
    public static final VendorTag CAMERA_AI_30 = create(C0549O00o0o.INSTANCE, Byte.class);
    public static final VendorTag CINEMATIC_PHOTO_ENABLED = create(C0620O00ooOOO.INSTANCE, Byte.class);
    public static final VendorTag CINEMATIC_VIDEO_ENABLED = create(C0523O00Oooo0.INSTANCE, Byte.class);
    public static final VendorTag COLOR_ENHANCE_ENABLED = create(C0503O00Oo0Oo.INSTANCE, Byte.class);
    public static final VendorTag CONTRAST_LEVEL = create(C0543O00o0OOO.INSTANCE, Integer.class);
    public static VendorTag CONTROL_AI_SCENE_MODE = create(C0534O00o00oO.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_FOR_MULTIFRAME_FRAME_COUNT = create(C0600O00oo.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_FOR_MULTIFRAME_FRAME_INDEX = create(C0619O00ooOO0.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_HIGH_QUALITY_REPROCESS = create(O00o0.INSTANCE, Integer.class);
    public static final int CONTROL_CAPTURE_HIGH_QUALITY_YUV_OFF = 0;
    public static final int CONTROL_CAPTURE_HIGH_QUALITY_YUV_ON = 1;
    public static final VendorTag CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_COUNT = create(C0588O00oOo.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_INDEX = create(C0610O00oo0o.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_HINT_FOR_ISP_FRAME_TUNING_INDEX = create(C0609O00oo0Oo.INSTANCE, Long.class);
    public static final VendorTag CONTROL_CAPTURE_HINT_FOR_ISP_TUNING = create(C0587O00oOOoo.INSTANCE, Integer.class);
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_AINR = 2;
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_HDR = 5005;
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_LLHDR = 5007;
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_MFNR = 1;
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_MFSR = 5006;
    public static final int CONTROL_CAPTURE_HINT_FOR_ISP_TUNING_SUPER_RAW = 5008;
    public static final VendorTag CONTROL_CAPTURE_ISP_META_ENABLE = create(C0579O00oOO0o.INSTANCE, Byte.class);
    public static final VendorTag CONTROL_CAPTURE_ISP_META_REQUEST = create(C0557O00o0oOo.INSTANCE, Byte.class);
    public static final byte CONTROL_CAPTURE_ISP_TUNING_DATA_BUFFER = 2;
    public static final byte CONTROL_CAPTURE_ISP_TUNING_DATA_IN_METADATA = 1;
    public static final byte CONTROL_CAPTURE_ISP_TUNING_DATA_NONE = 0;
    public static final byte CONTROL_CAPTURE_ISP_TUNING_REQ_RAW = 1;
    public static final byte CONTROL_CAPTURE_ISP_TUNING_REQ_YUV = 2;
    public static final VendorTag CONTROL_CAPTURE_MTK_PROCESS_RAW_ENABLE = create(C0553O00o0o0o.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_PACKED_RAW_ENABLE = create(C0615O00ooO00.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_PACKED_RAW_SUPPORT = create(O00OOOo.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_CAPTURE_SINGLE_YUV_NR = create(C0590O00oOo00.INSTANCE, Integer.class);
    public static final int CONTROL_CAPTURE_SINGLE_YUV_NR_OFF = 0;
    public static final int CONTROL_CAPTURE_SINGLE_YUV_NR_ON = 1;
    public static final VendorTag CONTROL_CSHOT_FEATURE_CAPTURE = create(C0491O00O0o0O.INSTANCE, Integer.class);
    public static final int CONTROL_CSHOT_FEATURE_CAPTURE_OFF = 0;
    public static final int CONTROL_CSHOT_FEATURE_CAPTURE_ON = 1;
    public static final VendorTag CONTROL_DISTORTION_FPC_DATA = create(C0613O00ooO.INSTANCE, byte[].class);
    public static final VendorTag CONTROL_ENABLE_REMOSAIC = create(C0578O00oOO0O.INSTANCE, Boolean.class);
    public static final VendorTag CONTROL_ENABLE_SPECSHOT_MODE = create(C0586O00oOOoO.INSTANCE, Integer.class);
    public static final VendorTag CONTROL_NOTIFICATION_TRIGGER = create(C0561O00o0ooo.INSTANCE, Integer.class);
    public static final int CONTROL_NOTIFICATION_TRIGGER_OFF = 0;
    public static final int CONTROL_NOTIFICATION_TRIGGER_ON = 1;
    public static final VendorTag CONTROL_QUICK_PREVIEW = create(C0577O00oOO00.INSTANCE, int[].class);
    public static final int[] CONTROL_QUICK_PREVIEW_OFF = {0};
    public static final int[] CONTROL_QUICK_PREVIEW_ON = {1};
    public static final VendorTag CONTROL_REMOSAIC_HINT = create(C0483O000ooo0.INSTANCE, int[].class);
    public static final int[] CONTROL_REMOSAIC_HINT_OFF = {0};
    public static final int[] CONTROL_REMOSAIC_HINT_ON = {1};
    public static final Key CONTROL_SAT_FUSION_IMAGE_TYPE = requestKey("xiaomi.capturefusion.imageType", Byte.class);
    public static final VendorTag CONTROL_SAT_FUSION_SHOT = create(C0548O00o0Ooo.INSTANCE, Byte.class);
    public static final byte CONTROL_SAT_FUSION_SHOT_OFF = 0;
    public static final byte CONTROL_SAT_FUSION_SHOT_ON = 1;
    public static final VendorTag CUSTOM_WATERMARK_TEXT = create(C0572O00oO0o0.INSTANCE, String.class);
    public static final VendorTag DEBUG_INFO_AS_WATERMARK = create(C0566O00oO00o.INSTANCE, Boolean.class);
    public static final VendorTag DEFLICKER_ENABLED = create(C0540O00o0O0o.INSTANCE, Boolean.class);
    public static final VendorTag DEPURPLE = create(C0570O00oO0Oo.INSTANCE, Byte.class);
    public static final VendorTag DEVICE_ORIENTATION = create(C0518O00OooO.INSTANCE, Integer.class);
    public static final VendorTag DUAL_BOKEH_ENABLE = create(C0560O00o0ooO.INSTANCE, Boolean.class);
    public static final Key DXO_ASD_SCENE = requestKey("xiaomi.ai.asd.sceneDetectedExt", ASDScene.class);
    public static final VendorTag DYNAMIC_FPS_CONFIG = create(C0536O00o0O.INSTANCE, float[].class);
    public static final VendorTag EXPOSURE_METERING = create(C0565O00oO00O.INSTANCE, Integer.class);
    public static final VendorTag EYE_LIGHT_STRENGTH = create(C0508O00OoO0.INSTANCE, Integer.class);
    public static final VendorTag EYE_LIGHT_TYPE = create(C0486O00O00oO.INSTANCE, Integer.class);
    public static final VendorTag FACE_AGE_ANALYZE_ENABLED = create(C0563O00oO0.INSTANCE, Boolean.class);
    public static final VendorTag FACE_SCORE_ENABLED = create(O00O0o0.INSTANCE, Boolean.class);
    public static final VendorTag FLASH_CURRENT = create(C0558O00o0oo.INSTANCE, Integer.class);
    public static final VendorTag FLASH_MODE = create(C0583O00oOOOo.INSTANCE, Integer.class);
    public static final VendorTag FLAW_DETECT_ENABLE = create(O00OOo.INSTANCE, Boolean.class);
    public static final VendorTag FRONT_MIRROR = create(C0498O00O0ooO.INSTANCE, Boolean.class);
    public static final VendorTag HDR10_VIDEO = create(C0515O00Ooo0.INSTANCE, Byte.class);
    public static final VendorTag HDR_BOKEH_ENABLED = create(C0511O00OoOo.INSTANCE, Boolean.class);
    public static final VendorTag HDR_BRACKET_MODE = create(C0519O00OooO0.INSTANCE, Byte.class);
    public static final VendorTag HDR_CHECKER_ADRC = create(O00O000o.INSTANCE, Integer.class);
    public static final VendorTag HDR_CHECKER_ENABLE = create(C0537O00o0O0.INSTANCE, Boolean.class);
    public static final VendorTag HDR_CHECKER_SCENETYPE = create(C0608O00oo0OO.INSTANCE, Integer.class);
    public static final VendorTag HDR_CHECKER_STATUS = create(C0487O00O00oo.INSTANCE, Integer.class);
    public static final VendorTag HDR_ENABLED = create(C0591O00oOo0O.INSTANCE, Boolean.class);
    public static final VendorTag HDR_MODE = create(O00Oo00.INSTANCE, Integer.class);
    public static final VendorTag HFPSVR_MODE = create(C0501O00OOoO.INSTANCE, Integer.class);
    public static final VendorTag HHT_ENABLED = create(C0605O00oo00o.INSTANCE, Boolean.class);
    public static VendorTag HIGHQUALITY_PREFERRED = create(C0490O00O0Ooo.INSTANCE, Byte.class);
    public static final VendorTag HINT_FOR_RAW_REPROCESS = create(C0569O00oO0OO.INSTANCE, Boolean.class);
    public static final VendorTag HISTOGRAM_STATS_ENABLED = create(C0489O00O0OoO.INSTANCE, Byte.class);
    public static final VendorTag ISO_EXP = create(C0544O00o0OOo.INSTANCE, Long.class);
    public static final VendorTag IS_HFR_PREVIEW = create(C0551O00o0o00.INSTANCE, Byte.class);
    public static final VendorTag IS_LLS_NEEDED = create(O00O0OOo.INSTANCE, Integer.class);
    public static final VendorTag IS_PARALLEL_SNAPSHOT = create(O00o.INSTANCE, Boolean.class);
    public static final VendorTag LENS_DIRTY_DETECT = create(C0547O00o0OoO.INSTANCE, Boolean.class);
    public static final VendorTag MACRO_MODE = create(C0541O00o0OO.INSTANCE, Byte.class);
    public static final VendorTag MFNR_ENABLED = create(C0496O00O0oo.INSTANCE, Boolean.class);
    public static final VendorTag MFNR_FRAME_NUM = create(C0531O00o00Oo.INSTANCE, Integer.class);
    public static final VendorTag MI_HDR_SR_ENABLED = create(C0571O00oO0o.INSTANCE, Boolean.class);
    public static final VendorTag MI_PANORAMA_P2S_ENABLED = create(C0505O00Oo0oO.INSTANCE, Boolean.class);
    public static final VendorTag MI_STATISTICS_FACE_RECTANGLES = create(C0499O00O0ooo.INSTANCE, Rect[].class);
    public static final VendorTag MI_TUNING_MODE = create(C0535O00o00oo.INSTANCE, Byte.class);
    public static final VendorTag MTK_CONFIGURE_SETTING_PROPRIETARY = create(C0533O00o00o0.INSTANCE, int[].class);
    public static final int[] MTK_CONFIGURE_SETTING_PROPRIETARY_OFF = {0};
    public static final int[] MTK_CONFIGURE_SETTING_PROPRIETARY_ON = {1};
    public static final VendorTag MTK_EXPOSURE_METERING_MODE = create(O00Oo.INSTANCE, Byte.class);
    public static final byte MTK_EXPOSURE_METERING_MODE_AVERAGE = 2;
    public static final byte MTK_EXPOSURE_METERING_MODE_CENTER_WEIGHT = 0;
    public static final byte MTK_EXPOSURE_METERING_MODE_SOPT = 1;
    public static final int[] MTK_HDR_FEATURE_HDR_MODE_OFF = {0};
    public static final int[] MTK_HDR_FEATURE_HDR_MODE_VIDEO_ON = {3};
    public static final VendorTag MTK_HDR_KEY_DETECTION_MODE = create(C0550O00o0o0.INSTANCE, int[].class);
    public static final VendorTag MTK_MULTI_CAM_CONFIG_SCALER_CROP_REGION = create(C0484O000oooO.INSTANCE, int[].class);
    public static final VendorTag MTK_MULTI_CAM_FEATURE_MODE = create(C0602O00oo00.INSTANCE, Integer.class);
    public static final int MTK_MULTI_CAM_FEATURE_MODE_DENOISE = 2;
    public static final int MTK_MULTI_CAM_FEATURE_MODE_VSDOF = 1;
    public static final int MTK_MULTI_CAM_FEATURE_MODE_ZOOM = 0;
    public static final VendorTag MTK_STREAMING_FEATURE_PIP_DEVICES = create(C0506O00Oo0oo.INSTANCE, int[].class);
    public static final VendorTag MULTIFRAME_INPUTNUM = create(C0628O00ooo00.INSTANCE, Integer.class);
    public static final VendorTag NORMAL_WIDE_LENS_DISTORTION_CORRECTION_LEVEL = create(C0575O00oOO.INSTANCE, Byte.class);
    public static final VendorTag ON_TRIPOD_MODE = create(C0618O00ooOO.INSTANCE, MarshalQueryableASDScene.ASDScene[].class);
    public static final VendorTag PARALLEL_ENABLED = create(C0592O00oOo0o.INSTANCE, Boolean.class);
    public static final VendorTag PARALLEL_PATH = create(O00Oo0.INSTANCE, byte[].class);
    public static final VendorTag PORTRAIT_LIGHTING = create(O00Oo00o.INSTANCE, Integer.class);
    public static final VendorTag POST_PROCESS_CROP_REGION = create(C0538O00o0O00.INSTANCE, Rect.class);
    public static final VendorTag PRO_VIDEO_LOG_ENABLED = create(C0485O000oooo.INSTANCE, Byte.class);
    public static final VendorTag QCOM_VIDEO_HDR_ENABLED = create(C0601O00oo0.INSTANCE, Integer.class);
    public static final VendorTag RECORDING_END_STREAM = create(C0512O00OoOo0.INSTANCE, Byte.class);
    public static final VendorTag SANPSHOT_FLIP_MODE = create(O00OoOoO.INSTANCE, Integer.class);
    public static final VendorTag SATURATION = create(C0611O00oo0o0.INSTANCE, Integer.class);
    public static final VendorTag SAT_FALLBACK_DISABLE = create(C0552O00o0o0O.INSTANCE, Byte.class);
    public static final VendorTag SAT_FALLBACK_ENABLE = create(C0616O00ooO0O.INSTANCE, Boolean.class);
    public static final VendorTag SAT_IS_ZOOMING = create(O00OOo0.INSTANCE, Boolean.class);
    public static final VendorTag SAT_ULTRA_WIDE_LENS_DISTORTION_CORRECTION_ENABLE = create(C0497O00O0oo0.INSTANCE, Byte.class);
    public static final VendorTag SCREEN_LIGHT_HINT = create(C0517O00Ooo0o.INSTANCE, Byte.class);
    public static final VendorTag SELECT_PRIORITY = create(C0559O00o0oo0.INSTANCE, Integer.class);
    public static final VendorTag SHARPNESS_CONTROL = create(C0614O00ooO0.INSTANCE, Integer.class);
    public static final VendorTag SHRINK_MEMORY_MODE = create(C0568O00oO0O0.INSTANCE, Integer.class);
    public static final int SHRINK_MEMORY_MODE_ALL = 2;
    public static final int SHRINK_MEMORY_MODE_INACTIVE = 1;
    public static final int SHRINK_MEMORY_MODE_NONE = 0;
    public static final VendorTag SINGLE_CAMERA_BOKEH = create(C0567O00oO0O.INSTANCE, Boolean.class);
    public static final VendorTag SMVR_MODE = create(O00O0o.INSTANCE, int[].class);
    public static final VendorTag SNAP_SHOT_TORCH = create(C0539O00o0O0O.INSTANCE, Boolean.class);
    public static final VendorTag ST_ENABLED = create(C0482O000ooo.INSTANCE, Boolean.class);
    public static final VendorTag ST_FAST_ZOOM_IN = create(O00Oo0o0.INSTANCE, Boolean.class);
    public static final VendorTag SUPERNIGHT_RAW_ENABLED = create(C0492O00O0o0o.INSTANCE, Boolean.class);
    public static final VendorTag SUPER_NIGHT_SCENE_ENABLED = create(O00O00Oo.INSTANCE, Boolean.class);
    public static final VendorTag SUPER_RESOLUTION_ENABLED = create(C0527O00o000o.INSTANCE, Boolean.class);
    public static final VendorTag SW_MFNR_ENABLED = create(O00O0O0o.INSTANCE, Boolean.class);
    private static final String TAG = "CaptureRequestVendorTags";
    public static final VendorTag TARGET_ZOOM = create(C0585O00oOOo0.INSTANCE, Float.class);
    public static final VendorTag THERMAL_LEVEL = create(C0532O00o00o.INSTANCE, Integer.class);
    public static final VendorTag TOF_LASERDIST = create(O00o0000.INSTANCE, Byte.class);
    public static VendorTag ULTRA_PIXEL_PORTRAIT_ENABLED = create(C0514O00Ooo.INSTANCE, Boolean.class);
    public static final VendorTag ULTRA_WIDE_LENS_DISTORTION_CORRECTION_LEVEL = create(C0562O00oO.INSTANCE, Byte.class);
    public static final VendorTag USE_CUSTOM_WB = create(C0513O00OoOoo.INSTANCE, Integer.class);
    public static final VendorTag USE_ISO_VALUE = create(C0521O00OooOo.INSTANCE, Integer.class);
    public static final int VALUE_HFPSVR_MODE_OFF = 0;
    public static final int VALUE_HFPSVR_MODE_ON = 1;
    public static final int VALUE_SANPSHOT_FLIP_MODE_OFF = 0;
    public static final int VALUE_SANPSHOT_FLIP_MODE_ON = 1;
    public static final int VALUE_SELECT_PRIORITY_EXP_TIME_PRIORITY = 1;
    public static final int VALUE_SELECT_PRIORITY_ISO_PRIORITY = 0;
    public static final int[] VALUE_SMVR_MODE_120FPS = {120, 4};
    public static final int[] VALUE_SMVR_MODE_240FPS = {240, 8};
    public static final int VALUE_VIDEO_RECORD_CONTROL_PREPARE = 0;
    public static final int VALUE_VIDEO_RECORD_CONTROL_START = 1;
    public static final int VALUE_VIDEO_RECORD_CONTROL_STOP = 2;
    public static final byte VALUE_ZSL_CAPTURE_MODE_OFF = 0;
    public static final byte VALUE_ZSL_CAPTURE_MODE_ON = 1;
    public static final VendorTag VIDEO_BOKEH_BACK_LEVEL = create(C0607O00oo0O0.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_BOKEH_COLOR_RETENTION_BACK_MODE = create(O00Oo0OO.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_BOKEH_COLOR_RETENTION_FRONT_MODE = create(O00OoO0O.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_BOKEH_FRONT_LEVEL = create(C0546O00o0Oo0.INSTANCE, Float.class);
    public static final VendorTag VIDEO_FILTER_COLOR_RETENTION_BACK = create(C0545O00o0Oo.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_FILTER_COLOR_RETENTION_FRONT = create(C0582O00oOOOO.INSTANCE, Boolean.class);
    public static final VendorTag VIDEO_FILTER_ID = create(O00O0Oo0.INSTANCE, Integer.class);
    public static final VendorTag VIDEO_RECORD_CONTROL = create(C0584O00oOOo.INSTANCE, Integer.class);
    public static final VendorTag WATERMARK_APPLIEDTYPE = create(C0623O00ooOo0.INSTANCE, String.class);
    public static final VendorTag WATERMARK_AVAILABLETYPE = create(C0542O00o0OO0.INSTANCE, String.class);
    public static final VendorTag WATERMARK_FACE = create(C0622O00ooOo.INSTANCE, String.class);
    public static final VendorTag WATERMARK_TIME = create(C0573O00oO0oO.INSTANCE, String.class);
    public static final VendorTag WHOLE_BODY_SLIM = create(O00OoOO0.INSTANCE, Integer.class);
    public static final VendorTag XIAOMI_AISHUTTER_EXIST_MOTION = create(C0556O00o0oOO.INSTANCE, int[].class);
    public static final int[] XIAOMI_AISHUTTER_EXIST_MOTION_OFF = {0};
    public static final int[] XIAOMI_AISHUTTER_EXIST_MOTION_ON = {1};
    public static final VendorTag XIAOMI_AISHUTTER_FEATURE_ENABLED = create(C0621O00ooOOo.INSTANCE, Boolean.class);
    public static final byte XIAOMI_TUNING_MODE_EXPOSUREDELAY = 1;
    public static final VendorTag ZSL_CAPTURE_MODE = create(C0580O00oOOO.INSTANCE, Byte.class);
    private static Constructor requestConstructor;

    static /* synthetic */ String O00oooo() {
        return "com.vidhance.autozoom.applyinpreview";
    }

    static /* synthetic */ String O00oooo0() {
        return "com.vidhance.autozoom.mode";
    }

    static /* synthetic */ String O00ooooo() {
        return "xiaomi.imageQuality.isHighQualityPreferred";
    }

    static /* synthetic */ String O0O000o() {
        return "xiaomi.algoup.enabled";
    }

    static /* synthetic */ String O0O00OO() {
        return "xiaomi.hdr.hdrChecker.enabled";
    }

    static /* synthetic */ String O0O00Oo() {
        return "xiaomi.hdr.hdrChecker.status";
    }

    static /* synthetic */ String O0O00o() {
        return "xiaomi.hdr.hdrChecker.adrc";
    }

    static /* synthetic */ String O0O00o0() {
        return "xiaomi.hdr.hdrChecker.sceneType";
    }

    static /* synthetic */ String O0O00oO() {
        return "xiaomi.hdr.enabled";
    }

    static /* synthetic */ String O0O0O() {
        return "xiaomi.parallel.path";
    }

    static /* synthetic */ String O0O0O0o() {
        return "xiaomi.hdr.hdrMode";
    }

    static /* synthetic */ String O0O0OO() {
        return "xiaomi.parallel.enabled";
    }

    static /* synthetic */ String O0O0OO0() {
        return "com.vidhance.autozoom.minimumscaling";
    }

    static /* synthetic */ String O0O0OOO() {
        return "xiaomi.hht.enabled";
    }

    static /* synthetic */ String O0O0OOo() {
        return "xiaomi.node.hfr.deflicker.enabled";
    }

    static /* synthetic */ String O0O0Oo0() {
        return "xiaomi.superportrait.enabled";
    }

    static /* synthetic */ String O0O0OoO() {
        return "xiaomi.superResolution.enabled";
    }

    static /* synthetic */ String O0O0Ooo() {
        return "xiaomi.mfnr.enabled";
    }

    static /* synthetic */ String O0O0o() {
        return "com.vidhance.autozoom.stop";
    }

    static /* synthetic */ String O0O0o0() {
        return "xiaomi.swmf.enabled";
    }

    static /* synthetic */ String O0O0o00() {
        return "xiaomi.mfnr.frameNum";
    }

    static /* synthetic */ String O0O0o0O() {
        return "xiaomi.bokeh.enabled";
    }

    static /* synthetic */ String O0O0o0o() {
        return "xiaomi.bokehrear.enabled";
    }

    static /* synthetic */ String O0O0oO() {
        return "xiaomi.videoBokehParam.back";
    }

    static /* synthetic */ String O0O0oO0() {
        return "xiaomi.bokeh.fNumberApplied";
    }

    static /* synthetic */ String O0O0oOO() {
        return "xiaomi.videoBokehParam.front";
    }

    static /* synthetic */ String O0O0oOo() {
        return "xiaomi.videofilter.filterApplied";
    }

    static /* synthetic */ String O0O0oo() {
        return "xiaomi.colorRetention.frontEnable";
    }

    static /* synthetic */ String O0O0oo0() {
        return "xiaomi.colorRetention.enable";
    }

    static /* synthetic */ String O0O0ooO() {
        return "xiaomi.colorRetention.value";
    }

    static /* synthetic */ String O0O0ooo() {
        return "xiaomi.colorRetention.frontValue";
    }

    static /* synthetic */ String O0OO00O() {
        return "com.vidhance.autozoom.start_region";
    }

    static /* synthetic */ String O0OO00o() {
        return "xiaomi.smoothTransition.fastZoomIn";
    }

    static /* synthetic */ String O0OO0O() {
        return "xiaomi.smoothTransition.disablefallback";
    }

    static /* synthetic */ String O0OO0Oo() {
        return "xiaomi.portrait.lighting";
    }

    static /* synthetic */ String O0OO0o0() {
        return "xiaomi.ai.segment.enabled";
    }

    static /* synthetic */ String O0OO0oO() {
        return "xiaomi.faceGenderAndAge.enabled";
    }

    static /* synthetic */ String O0OO0oo() {
        return "xiaomi.faceScore.enabled";
    }

    static /* synthetic */ String O0OOO0() {
        return "com.vidhance.autozoom.select";
    }

    static /* synthetic */ String O0OOO00() {
        return "xiaomi.device.orientation";
    }

    static /* synthetic */ String O0OOO0O() {
        return "xiaomi.beauty.slimFaceRatio";
    }

    static /* synthetic */ String O0OOO0o() {
        return "xiaomi.beauty.skinSmoothRatio";
    }

    static /* synthetic */ String O0OOOO() {
        return "xiaomi.beauty.noseRatio";
    }

    static /* synthetic */ String O0OOOOO() {
        return "xiaomi.beauty.risoriusRatio";
    }

    static /* synthetic */ String O0OOOOo() {
        return "xiaomi.beauty.lipsRatio";
    }

    static /* synthetic */ String O0OOOo() {
        return "xiaomi.beauty.neckRatio";
    }

    static /* synthetic */ String O0OOOo0() {
        return "xiaomi.beauty.chinRatio";
    }

    static /* synthetic */ String O0OOOoO() {
        return "xiaomi.beauty.smileRatio";
    }

    static /* synthetic */ String O0OOOoo() {
        return "xiaomi.beauty.slimNoseRatio";
    }

    static /* synthetic */ String O0OOo() {
        return "xiaomi.watermark.typeApplied";
    }

    static /* synthetic */ String O0OOo00() {
        return "com.vidhance.autozoom.unselect";
    }

    static /* synthetic */ String O0OOo0O() {
        return "xiaomi.beauty.hairlineRatio";
    }

    static /* synthetic */ String O0OOo0o() {
        return "xiaomi.watermark.availableType";
    }

    static /* synthetic */ String O0OOoO() {
        return "xiaomi.beauty.skinColorRatio";
    }

    static /* synthetic */ String O0OOoO0() {
        return "xiaomi.watermark.time";
    }

    static /* synthetic */ String O0OOoOO() {
        return "xiaomi.watermark.face";
    }

    static /* synthetic */ String O0OOoOo() {
        return "xiaomi.snapshotTorch.enabled";
    }

    static /* synthetic */ String O0OOoo() {
        return "xiaomi.beauty.beautyLevelApplied";
    }

    static /* synthetic */ String O0OOoo0() {
        return "xiaomi.flip.enabled";
    }

    static /* synthetic */ String O0OOooO() {
        return "xiaomi.burst.captureHint";
    }

    static /* synthetic */ String O0OOooo() {
        return "xiaomi.burst.shootFPS";
    }

    static /* synthetic */ String O0Oo() {
        return "xiaomi.beauty.oneKeySlimRatio";
    }

    static /* synthetic */ String O0Oo0() {
        return "xiaomi.beauty.blushRatio";
    }

    static /* synthetic */ String O0Oo00() {
        return "com.vidhance.autozoom.force_lock";
    }

    static /* synthetic */ String O0Oo000() {
        return "xiaomi.beauty.eyeBrowDyeRatio";
    }

    static /* synthetic */ String O0Oo00O() {
        return "xiaomi.beauty.pupilLineRatio";
    }

    static /* synthetic */ String O0Oo00o() {
        return "xiaomi.beauty.lipGlossRatio";
    }

    static /* synthetic */ String O0Oo0O() {
        return "xiaomi.supernight.enabled";
    }

    static /* synthetic */ String O0Oo0O0() {
        return "xiaomi.beauty.eyeLightType";
    }

    static /* synthetic */ String O0Oo0OO() {
        return "xiaomi.mimovie.enabled";
    }

    static /* synthetic */ String O0Oo0Oo() {
        return "xiaomi.beauty.headSlimRatio";
    }

    static /* synthetic */ String O0Oo0o() {
        return "xiaomi.beauty.bodySlimRatio";
    }

    static /* synthetic */ String O0Oo0o0() {
        return "xiaomi.ai.add.enabled";
    }

    static /* synthetic */ String O0Oo0oO() {
        return "xiaomi.beauty.shoulderSlimRatio";
    }

    static /* synthetic */ String O0Oo0oo() {
        return "com.vidhance.autozoom.center_offset";
    }

    static /* synthetic */ String O0OoO() {
        return "xiaomi.softlightMode.enabled";
    }

    static /* synthetic */ String O0OoO0() {
        return "xiaomi.distortion.distortionLevelApplied";
    }

    static /* synthetic */ String O0OoO00() {
        return "xiaomi.beauty.buttPlumpSlimRatio";
    }

    static /* synthetic */ String O0OoO0O() {
        return "xiaomi.distortion.ultraWideDistortionLevel";
    }

    static /* synthetic */ String O0OoOO() {
        return "xiaomi.smoothTransition.fallback";
    }

    static /* synthetic */ String O0OoOO0() {
        return "xiaomi.snapshot.backwardfetchframe.enabled";
    }

    static /* synthetic */ String O0OoOOO() {
        return "org.codeaurora.qcamera3.iso_exp_priority.select_priority";
    }

    static /* synthetic */ String O0OoOOo() {
        return "com.vidhance.autozoom.scale_offset";
    }

    static /* synthetic */ String O0OoOo() {
        return "xiaomi.smoothTransition.enabled";
    }

    static /* synthetic */ String O0OoOo0() {
        return "xiaomi.ai.asd.previewenabled";
    }

    static /* synthetic */ String O0OoOoO() {
        return "xiaomi.ai.asd.sceneApplied";
    }

    static /* synthetic */ String O0OoOoo() {
        return "xiaomi.ai.asd.period";
    }

    static /* synthetic */ String O0Ooo() {
        return "xiaomi.depurple.enabled";
    }

    static /* synthetic */ String O0Ooo0() {
        return "xiaomi.hfrPreview.isHFRPreview";
    }

    static /* synthetic */ String O0Ooo00() {
        return "org.codeaurora.qcamera3.contrast.level";
    }

    static /* synthetic */ String O0Ooo0O() {
        return "org.codeaurora.qcamera3.ae_bracket.mode";
    }

    static /* synthetic */ String O0Ooo0o() {
        return "xiaomi.multiframe.inputNum";
    }

    static /* synthetic */ String O0OooO() {
        return "xiaomi.watermark.custom";
    }

    static /* synthetic */ String O0OooO0() {
        return "xiaomi.MacroMode.enabled";
    }

    static /* synthetic */ String O0OooOO() {
        return "xiaomi.satIsZooming.satIsZooming";
    }

    static /* synthetic */ String O0OooOo() {
        return "xiaomi.burst.curReqIndex";
    }

    static /* synthetic */ String O0Oooo() {
        return "xiaomi.beauty.eyeLightStrength";
    }

    static /* synthetic */ String O0Oooo0() {
        return "xiaomi.burst.totalReqNum";
    }

    static /* synthetic */ String O0OoooO() {
        return "com.mediatek.streamingfeature.hfpsMode";
    }

    static /* synthetic */ String O0Ooooo() {
        return "com.mediatek.smvrfeature.smvrMode";
    }

    static /* synthetic */ String O0o() {
        return "xiaomi.asd.enabled";
    }

    static /* synthetic */ String O0o0() {
        return "com.mediatek.configure.setting.proprietaryRequest";
    }

    static /* synthetic */ String O0o00() {
        return "xiaomi.remosaic.enabled";
    }

    static /* synthetic */ String O0o000() {
        return "com.mediatek.control.capture.flipmode";
    }

    static /* synthetic */ String O0o0000() {
        return "com.mediatek.control.capture.zsl.mode";
    }

    static /* synthetic */ String O0o000O() {
        return "com.mediatek.control.capture.remosaicenable";
    }

    static /* synthetic */ String O0o000o() {
        return "com.mediatek.control.capture.hintForIspTuning";
    }

    static /* synthetic */ String O0o00O() {
        return "xiaomi.specshot.mode.enabled";
    }

    static /* synthetic */ String O0o00O0() {
        return "com.mediatek.control.capture.processRaw.enable";
    }

    static /* synthetic */ String O0o00OO() {
        return "xiaomi.distortion.distortioFpcData";
    }

    static /* synthetic */ String O0o00Oo() {
        return "com.mediatek.control.capture.hintForRawReprocess";
    }

    static /* synthetic */ String O0o00o() {
        return "com.mediatek.streamingfeature.pipDevices";
    }

    static /* synthetic */ String O0o00o0() {
        return "xiaomi.superResolution.cropRegionMtk";
    }

    static /* synthetic */ String O0o00oO() {
        return "com.mediatek.3afeature.aeMeteringMode";
    }

    static /* synthetic */ String O0o00oo() {
        return "com.mediatek.configure.setting.initrequest";
    }

    static /* synthetic */ String O0o0O() {
        return "com.mediatek.multicamfeature.multiCamFeatureMode";
    }

    static /* synthetic */ String O0o0O0() {
        return "com.mediatek.hdrfeature.hdrMode";
    }

    static /* synthetic */ String O0o0O00() {
        return "com.mediatek.multicamfeature.multiCamConfigScalerCropRegion";
    }

    static /* synthetic */ String O0o0O0O() {
        return "xiaomi.aishutter.enabled";
    }

    static /* synthetic */ String O0o0O0o() {
        return "xiaomi.aishutter.existmotion";
    }

    static /* synthetic */ String O0o0OO() {
        return "com.mediatek.control.capture.singleYuvNr";
    }

    static /* synthetic */ String O0o0OO0() {
        return "com.mediatek.control.capture.early.notification.trigger";
    }

    static /* synthetic */ String O0o0OOO() {
        return "com.mediatek.control.capture.highQualityYuv";
    }

    static /* synthetic */ String O0o0OOo() {
        return "com.mediatek.control.capture.ispMetaEnable";
    }

    static /* synthetic */ String O0o0Oo() {
        return "com.mediatek.control.capture.packedRaw.enable";
    }

    static /* synthetic */ String O0o0Oo0() {
        return "com.mediatek.control.capture.ispTuningRequest";
    }

    static /* synthetic */ String O0o0OoO() {
        return "com.mediatek.control.capture.packedRaw.support";
    }

    static /* synthetic */ String O0o0Ooo() {
        return "com.mediatek.control.capture.hintForIspFrameTuningIndex";
    }

    static /* synthetic */ String O0o0o0() {
        return "com.mediatek.control.capture.hintForIspFrameCount";
    }

    static /* synthetic */ String O0o0o00() {
        return "com.mediatek.control.capture.hintForIspFrameIndex";
    }

    static /* synthetic */ String O0o0o0O() {
        return "xiaomi.ai.asd.sceneDetected";
    }

    static /* synthetic */ String O0o0o0o() {
        return "xiaomi.ai.misd.StateScene";
    }

    static /* synthetic */ String O0o0oO() {
        return "xiaomi.ai.asd.dirtyEnable";
    }

    static /* synthetic */ String O0o0oO0() {
        return "xiaomi.ai.flaw.enabled";
    }

    static /* synthetic */ String O0o0oOO() {
        return "xiaomi.pro.video.log.enabled";
    }

    static /* synthetic */ String O0o0oOo() {
        return "xiaomi.thermal.thermalLevel";
    }

    static /* synthetic */ String O0o0oo() {
        return "xiaomi.pro.video.movie.enabled";
    }

    static /* synthetic */ String O0o0ooO() {
        return "xiaomi.pro.video.histogram.stats.enabled";
    }

    static /* synthetic */ String O0oO() {
        return "xiaomi.supernight.raw.enabled";
    }

    static /* synthetic */ String O0oO0() {
        return "xiaomi.flash.mode";
    }

    static /* synthetic */ String O0oO00() {
        return "xiaomi.bokeh.hdrEnabled";
    }

    static /* synthetic */ String O0oO000() {
        return "xiaomi.colorenhancement.enabled";
    }

    static /* synthetic */ String O0oO00O() {
        return "xiaomi.softlightMode.current";
    }

    static /* synthetic */ String O0oO00o() {
        return "xiaomi.watermark.debug";
    }

    static /* synthetic */ String O0oO0O() {
        return "xiaomi.panorama.p2s.enabled";
    }

    static /* synthetic */ String O0oO0O0() {
        return "xiaomi.statistics.faceRectangles";
    }

    static /* synthetic */ String O0oO0OO() {
        return "xiaomi.capturefusion.isFusionOn";
    }

    static /* synthetic */ String O0oO0Oo() {
        return "xiaomi.ai.misd.miaitof";
    }

    static /* synthetic */ String O0oO0o() {
        return "xiaomi.super.night.target";
    }

    static /* synthetic */ String O0oO0o0() {
        return "xiaomi.super.night.mode";
    }

    static /* synthetic */ String O0oO0oO() {
        return "xiaomi.sat.targetzoom";
    }

    static /* synthetic */ String O0oO0oo() {
        return "xiaomi.hdr.sr.enabled";
    }

    static /* synthetic */ String O0oOO() {
        return "org.codeaurora.qcamera3.iso_exp_priority.use_iso_value";
    }

    static /* synthetic */ String O0oOO0() {
        return "com.qti.stats_control.is_lls_needed";
    }

    static /* synthetic */ String O0oOO00() {
        return "xiaomi.movie.shot.mode";
    }

    static /* synthetic */ String O0oOO0O() {
        return "org.codeaurora.qcamera3.sessionParameters.dynamicFPSConfig";
    }

    static /* synthetic */ String O0oOO0o() {
        return "org.codeaurora.qcamera3.iso_exp_priority.use_iso_exp_priority";
    }

    static /* synthetic */ String O0oOOO0() {
        return C0124O00000oO.isMTKPlatform() ? "xiaomi.camera.awb.cct" : "com.qti.stats.awbwrapper.AWBCCT";
    }

    static /* synthetic */ String O0oOOOo() {
        return "org.codeaurora.qcamera3.saturation.use_saturation";
    }

    static /* synthetic */ String O0oOOo() {
        return "org.quic.camera.recording.endOfStream";
    }

    static /* synthetic */ String O0oOOo0() {
        return "org.codeaurora.qcamera3.sharpness.strength";
    }

    static /* synthetic */ String O0oOOoO() {
        return "org.quic.camera2.streamconfigs.HDRVideoMode";
    }

    static /* synthetic */ String O0oOOoo() {
        return "xiaomi.ai.asd.enabled";
    }

    static /* synthetic */ String O0oOo00() {
        return "xiaomi.ai.asd.AiMoonEffectEnabled";
    }

    static /* synthetic */ String O0oo0o() {
        return "com.mediatek.cshotfeature.capture";
    }

    static /* synthetic */ String OO0oO() {
        return "xiaomi.beauty.enlargeEyeRatio";
    }

    static /* synthetic */ String Oo0OOo() {
        return "org.codeaurora.qcamera3.exposure_metering.exposure_metering_mode";
    }

    static /* synthetic */ String OoO0o() {
        return "xiaomi.snapshot.front.ScreenLighting.enabled";
    }

    static /* synthetic */ String OooOO() {
        return "xiaomi.distortion.ultraWideDistortionEnable";
    }

    private static VendorTag create(final Supplier supplier, final Class cls) {
        return new VendorTag() {
            /* access modifiers changed from: protected */
            public Key create() {
                return CaptureRequestVendorTags.requestKey(getName(), cls);
            }

            public String getName() {
                return (String) supplier.get();
            }
        };
    }

    static /* synthetic */ String o00O00O0() {
        return "xiaomi.snapshot.isParallelSnapshot";
    }

    static /* synthetic */ String o00o00O() {
        return "xiaomi.beauty.legSlimRatio";
    }

    static /* synthetic */ String o0Oo0o0() {
        return "org.codeaurora.qcamera3.sessionParameters.EnableMFHDR";
    }

    static /* synthetic */ String oOOoOO() {
        return "xiaomi.memory.shrinkMode";
    }

    static /* synthetic */ String ooooooo() {
        return "xiaomi.video.recordControl";
    }

    public static void preload() {
        Log.d(TAG, "preloading...");
    }

    static Key requestKey(String str, Class cls) {
        try {
            if (requestConstructor == null) {
                requestConstructor = Key.class.getConstructor(new Class[]{String.class, cls.getClass()});
                requestConstructor.setAccessible(true);
            }
            return (Key) requestConstructor.newInstance(new Object[]{str, cls});
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot find/call Key constructor: ");
            sb.append(e.getMessage());
            Log.d(TAG, sb.toString());
            return null;
        }
    }
}
