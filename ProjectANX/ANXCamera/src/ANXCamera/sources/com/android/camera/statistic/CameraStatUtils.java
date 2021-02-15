package com.android.camera.statistic;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.exifinterface.media.ExifInterface;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraIntentManager;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.MutexModeManager;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.BeautyConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigFlash;
import com.android.camera.data.data.config.ComponentConfigHdr;
import com.android.camera.data.data.config.ComponentConfigSlowMotion;
import com.android.camera.data.data.config.ComponentConfigVideoQuality;
import com.android.camera.data.data.config.ComponentManuallyDualLens;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.data.runing.ComponentRunningShine.ShineType;
import com.android.camera.data.data.runing.ComponentRunningTimer;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.statistic.MistatsConstants.AIWatermark;
import com.android.camera.statistic.MistatsConstants.AlgoAttr;
import com.android.camera.statistic.MistatsConstants.Ambilight;
import com.android.camera.statistic.MistatsConstants.AutoHibernation;
import com.android.camera.statistic.MistatsConstants.AutoZoom;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.BeautyAttr;
import com.android.camera.statistic.MistatsConstants.BeautyBodySlimAttr;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.CaptureSence;
import com.android.camera.statistic.MistatsConstants.CloneAttr;
import com.android.camera.statistic.MistatsConstants.CostTime;
import com.android.camera.statistic.MistatsConstants.DualVideoAttr;
import com.android.camera.statistic.MistatsConstants.EditMode;
import com.android.camera.statistic.MistatsConstants.Error;
import com.android.camera.statistic.MistatsConstants.FeatureAttr;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.statistic.MistatsConstants.FilterAttr;
import com.android.camera.statistic.MistatsConstants.FlashAttr;
import com.android.camera.statistic.MistatsConstants.GoogleLens;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.statistic.MistatsConstants.MacroAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.statistic.MistatsConstants.ModuleName;
import com.android.camera.statistic.MistatsConstants.MoonAndNightAttr;
import com.android.camera.statistic.MistatsConstants.MoreMode;
import com.android.camera.statistic.MistatsConstants.MultiCameraAttr;
import com.android.camera.statistic.MistatsConstants.NonUI;
import com.android.camera.statistic.MistatsConstants.Other;
import com.android.camera.statistic.MistatsConstants.Panorama;
import com.android.camera.statistic.MistatsConstants.PictureQuality;
import com.android.camera.statistic.MistatsConstants.PortraitAttr;
import com.android.camera.statistic.MistatsConstants.ProColor;
import com.android.camera.statistic.MistatsConstants.ResourceAttr;
import com.android.camera.statistic.MistatsConstants.SensorAttr;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.android.camera.statistic.MistatsConstants.SlowMotion;
import com.android.camera.statistic.MistatsConstants.SpeechShutterAttr;
import com.android.camera.statistic.MistatsConstants.SuperMoon;
import com.android.camera.statistic.MistatsConstants.TimerBurst;
import com.android.camera.statistic.MistatsConstants.VLogAttr;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import com.android.camera.statistic.MistatsConstants.Zoom;
import com.android.camera.statistic.MistatsConstants.ZoomMapAttr;
import com.miui.filtersdk.filter.helper.FilterType;
import com.xiaomi.fenshen.FenShenCam.Message;
import com.xiaomi.fenshen.FenShenCam.Mode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CameraStatUtils {
    private static final String TAG = "CameraStatUtils";
    public static String mLayoutType;
    public static String mZoomPair;
    private static HashMap sBeautyTypeToName = new HashMap();
    private static HashMap sBeautyTypeToValue = new HashMap();
    private static SparseArray sCameraModeIdToName = new SparseArray();
    private static SparseArray sExposureTimeLessThan1sToName = new SparseArray();
    private static SparseArray sFilterTypeToName = new SparseArray();
    private static HashMap sMiLiveBeautyTypeToName = new HashMap();
    private static SparseArray sPictureQualityIndexToName = new SparseArray();
    private static SparseArray sSpeedToName = new SparseArray();
    private static SparseArray sTriggerModeIdToName = new SparseArray();

    /* renamed from: com.android.camera.statistic.CameraStatUtils$1 reason: invalid class name */
    /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message = new int[Message.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(10:0|1|2|3|4|5|6|7|8|10) */
        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|(3:7|8|10)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ALIGN_WARNING.ordinal()] = 1;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.NO_PERSON.ordinal()] = 2;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.PREVIEW_NO_PERSON.ordinal()] = 3;
            try {
                $SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[Message.ALIGN_TOO_LARGE_OR_FAILED.ordinal()] = 4;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    static {
        String str;
        SparseArray sparseArray;
        sCameraModeIdToName.put(161, "M_funTinyVideo_");
        sCameraModeIdToName.put(174, "M_liveDouyin_");
        sCameraModeIdToName.put(183, "M_miLive_");
        sCameraModeIdToName.put(177, "M_funArMimoji_");
        sCameraModeIdToName.put(184, "M_funArMimoji2_");
        sCameraModeIdToName.put(163, "M_capture_");
        sCameraModeIdToName.put(165, "M_square_");
        sCameraModeIdToName.put(167, "M_manual_");
        sCameraModeIdToName.put(171, "M_portrait_");
        sCameraModeIdToName.put(166, "M_panorama_");
        sCameraModeIdToName.put(176, "M_wideSelfie_");
        sCameraModeIdToName.put(172, "M_newSlowMotion_");
        sCameraModeIdToName.put(162, "M_recordVideo_");
        sCameraModeIdToName.put(169, "M_fastMotion_");
        if (C0122O00000o.instance().OOO000o()) {
            sparseArray = sCameraModeIdToName;
            str = ModuleName.MULTI_CAMERA_DUAL_VIDEO;
        } else {
            sparseArray = sCameraModeIdToName;
            str = ModuleName.DUAL_VIDEO;
        }
        sparseArray.put(204, str);
        sCameraModeIdToName.put(173, "M_superNight_");
        sCameraModeIdToName.put(214, "M_superNightVideo_");
        sCameraModeIdToName.put(175, "M_48mPixel_");
        sCameraModeIdToName.put(180, "M_proVideo_");
        sCameraModeIdToName.put(185, ModuleName.CLONE);
        sCameraModeIdToName.put(186, ModuleName.DOC);
        sCameraModeIdToName.put(211, ModuleName.FILM);
        sCameraModeIdToName.put(208, ModuleName.FILM_EXPOSUREDELAY);
        sCameraModeIdToName.put(212, ModuleName.FILM_PARALLELDREAM);
        sCameraModeIdToName.put(207, ModuleName.FILM_SLOWSHUTTER);
        sCameraModeIdToName.put(213, ModuleName.FILM_TIME_FREEZE);
        sCameraModeIdToName.put(189, ModuleName.FILM_DOLLY_ZOOM);
        sTriggerModeIdToName.put(10, CaptureSence.SHUTTER_BUTTON);
        sTriggerModeIdToName.put(20, "volume");
        sTriggerModeIdToName.put(30, CaptureSence.FINGERPRINT);
        sTriggerModeIdToName.put(40, CaptureSence.KEYCODE_CAMERA);
        sTriggerModeIdToName.put(50, CaptureSence.KEYCODE_DPAD);
        sTriggerModeIdToName.put(60, CaptureSence.OBJECT_TRACK);
        sTriggerModeIdToName.put(70, CaptureSence.AUDIO_CAPTURE);
        sTriggerModeIdToName.put(80, CaptureSence.FOCUS_SHOOT);
        sTriggerModeIdToName.put(90, CaptureSence.EXPOSURE_VIEW);
        sTriggerModeIdToName.put(100, "hand_gesture");
        sTriggerModeIdToName.put(110, CaptureSence.SPEECH_SHUTTER);
        sPictureQualityIndexToName.put(0, PictureQuality.LOWEST);
        sPictureQualityIndexToName.put(1, PictureQuality.LOWER);
        sPictureQualityIndexToName.put(2, "low");
        sPictureQualityIndexToName.put(3, "normal");
        sPictureQualityIndexToName.put(4, "high");
        sPictureQualityIndexToName.put(5, PictureQuality.HIGHER);
        sPictureQualityIndexToName.put(6, PictureQuality.HIGHEST);
        sExposureTimeLessThan1sToName.put(0, "auto");
        sExposureTimeLessThan1sToName.put(1000, "1/1000s");
        sExposureTimeLessThan1sToName.put(2000, "1/500s");
        String str2 = "1/250s";
        sExposureTimeLessThan1sToName.put(4000, str2);
        sExposureTimeLessThan1sToName.put(5000, str2);
        sExposureTimeLessThan1sToName.put(8000, "1/125s");
        sExposureTimeLessThan1sToName.put(16667, "1/60s");
        sExposureTimeLessThan1sToName.put(33333, "1/30s");
        sExposureTimeLessThan1sToName.put(66667, "1/15s");
        sExposureTimeLessThan1sToName.put(125000, "1/8s");
        sExposureTimeLessThan1sToName.put(250000, "1/4s");
        sExposureTimeLessThan1sToName.put(500000, "1/2s");
        sExposureTimeLessThan1sToName.put(1000000, "1s");
        sExposureTimeLessThan1sToName.put(2000000, "2s");
        sExposureTimeLessThan1sToName.put(4000000, "4s");
        sExposureTimeLessThan1sToName.put(8000000, "8s");
        sExposureTimeLessThan1sToName.put(16000000, "16s");
        sExposureTimeLessThan1sToName.put(32000000, "32s");
        sBeautyTypeToName.put("pref_beautify_level_key_capture", BeautyAttr.BEAUTY_LEVEL);
        String str3 = "pref_beautify_skin_smooth_ratio_key";
        sBeautyTypeToName.put(str3, BeautyAttr.PARAM_BEAUTY_SKIN_SMOOTH);
        sBeautyTypeToName.put("pref_beautify_skin_color_ratio_key", BeautyAttr.PARAM_BEAUTY_SKIN_COLOR);
        String str4 = "pref_beautify_enlarge_eye_ratio_key";
        sBeautyTypeToName.put(str4, BeautyAttr.PARAM_BEAUTY_ENLARGE_EYE);
        String str5 = "pref_beautify_slim_face_ratio_key";
        sBeautyTypeToName.put(str5, BeautyAttr.PARAM_BEAUTY_SLIM_FACE);
        sBeautyTypeToName.put("pref_beautify_nose_ratio_key", "attr_nose");
        sBeautyTypeToName.put("pref_beautify_risorius_ratio_key", BeautyAttr.PARAM_BEAUTY_RISORIUS);
        sBeautyTypeToName.put("pref_beautify_lips_ratio_key", BeautyAttr.PARAM_BEAUTY_LIPS);
        sBeautyTypeToName.put("pref_beautify_chin_ratio_key", BeautyAttr.PARAM_BEAUTY_CHIN);
        sBeautyTypeToName.put("pref_beautify_neck_ratio_key", BeautyAttr.PARAM_BEAUTY_NECK);
        sBeautyTypeToName.put("pref_beautify_smile_ratio_key", BeautyAttr.PARAM_BEAUTY_SMILE);
        sBeautyTypeToName.put("pref_beautify_slim_nose_ratio_key", BeautyAttr.PARAM_BEAUTY_SLIM_NOSE);
        sBeautyTypeToName.put("pref_beautify_hairline_ratio_key", BeautyAttr.PARAM_HAIRLINE);
        sBeautyTypeToName.put("pref_beautify_eyebrow_dye_ratio_key", BeautyAttr.PARAM_BEAUTY_EYEBROW_DYE);
        sBeautyTypeToName.put("pref_beautify_blusher_ratio_key", BeautyAttr.PARAM_BEAUTY_BLUSHER);
        sBeautyTypeToName.put("pref_beautify_pupil_line_ratio_key", BeautyAttr.PARAM_BEAUTY_PUPIL_LINE);
        sBeautyTypeToName.put("pref_beautify_jelly_lips_ratio_key", BeautyAttr.PARAM_BEAUTY_JELLY_LIPS);
        sBeautyTypeToName.put("pref_eye_light_type_key", BeautyAttr.PARAM_EYE_LIGHT);
        String str6 = "pref_beauty_head_slim_ratio";
        sBeautyTypeToName.put(str6, BeautyBodySlimAttr.PARAM_BEAUTY_HEAD_SLIM);
        String str7 = "pref_beauty_body_slim_ratio";
        sBeautyTypeToName.put(str7, BeautyBodySlimAttr.PARAM_BEAUTY_BODY_SLIM);
        String str8 = "pref_beauty_shoulder_slim_ratio";
        sBeautyTypeToName.put(str8, BeautyBodySlimAttr.PARAM_BEAUTY_SHOULDER_SLIM);
        String str9 = "key_beauty_leg_slim_ratio";
        sBeautyTypeToName.put(str9, BeautyBodySlimAttr.PARAM_BEAUTY_LEG_SLIM);
        String str10 = "pref_beauty_whole_body_slim_ratio";
        sBeautyTypeToName.put(str10, BeautyBodySlimAttr.PARAM_BEAUTY_WHOLE_BODY_SLIM);
        sBeautyTypeToName.put("pref_beauty_butt_slim_ratio", BeautyBodySlimAttr.PARAM_BUTT_SLIM);
        sBeautyTypeToName.put(BeautyConstant.BEAUTY_RESET, BaseEvent.RESET);
        sMiLiveBeautyTypeToName.put(str3, MiLive.PARAM_MI_LIVE_SMOOTH_RATIO);
        sMiLiveBeautyTypeToName.put(str5, MiLive.PARAM_MI_LIVE_SHRINK_FACE_RATIO);
        sMiLiveBeautyTypeToName.put(str4, MiLive.PARAM_MI_LIVE_ENLARGE_EYE_RATIO);
        sMiLiveBeautyTypeToName.put(str10, MiLive.PARAM_MI_LIVE_BEAUTY_WHOLE_BODY_SLIM);
        sMiLiveBeautyTypeToName.put(str9, MiLive.PARAM_MI_LIVE_BEAUTY_LEG_SLIM);
        sMiLiveBeautyTypeToName.put(str6, MiLive.PARAM_MI_LIVE_BEAUTY_HEAD_SLIM);
        sMiLiveBeautyTypeToName.put(str7, MiLive.PARAM_MI_LIVE_BEAUTY_BODY_SLIM);
        sMiLiveBeautyTypeToName.put(str8, MiLive.PARAM_MI_LIVE_BEAUTY_SHOULDER_SLIM);
        sBeautyTypeToValue.put("pref_beautify_level_key_capture", BeautyAttr.BEAUTY_LEVEL);
        sBeautyTypeToValue.put(str3, BeautyAttr.BEAUTY_SKIN_SMOOTH);
        sBeautyTypeToValue.put("pref_beautify_skin_color_ratio_key", BeautyAttr.BEAUTY_SKIN_COLOR);
        sBeautyTypeToValue.put(str4, BeautyAttr.BEAUTY_ENLARGE_EYE);
        sBeautyTypeToValue.put(str5, BeautyAttr.BEAUTY_SLIM_FACE);
        sBeautyTypeToValue.put("pref_beautify_nose_ratio_key", BeautyAttr.BEAUTY_NOSE);
        sBeautyTypeToValue.put("pref_beautify_risorius_ratio_key", BeautyAttr.BEAUTY_RISORIUS);
        sBeautyTypeToValue.put("pref_beautify_lips_ratio_key", BeautyAttr.BEAUTY_LIPS);
        sBeautyTypeToValue.put("pref_beautify_chin_ratio_key", BeautyAttr.BEAUTY_CHIN);
        sBeautyTypeToValue.put("pref_beautify_neck_ratio_key", BeautyAttr.BEAUTY_NECK);
        sBeautyTypeToValue.put("pref_beautify_smile_ratio_key", BeautyAttr.BEAUTY_SMILE);
        sBeautyTypeToValue.put("pref_beautify_slim_nose_ratio_key", BeautyAttr.BEAUTY_SLIM_NOSE);
        sBeautyTypeToValue.put("pref_beautify_hairline_ratio_key", BeautyAttr.HAIRLINE);
        sBeautyTypeToValue.put("pref_beautify_eyebrow_dye_ratio_key", BeautyAttr.BEAUTY_EYEBROW_DYE);
        sBeautyTypeToValue.put("pref_beautify_blusher_ratio_key", BeautyAttr.BEAUTY_BLUSHER);
        sBeautyTypeToValue.put("pref_beautify_pupil_line_ratio_key", BeautyAttr.BEAUTY_PUPIL_LINE);
        sBeautyTypeToValue.put("pref_beautify_jelly_lips_ratio_key", BeautyAttr.BEAUTY_JELLY_LIPS);
        sBeautyTypeToValue.put("pref_eye_light_type_key", BeautyAttr.EYE_LIGHT);
        sBeautyTypeToValue.put(str6, BeautyBodySlimAttr.BEAUTY_HEAD_SLIM);
        sBeautyTypeToValue.put(str7, BeautyBodySlimAttr.BEAUTY_TYPE_BODY_SLIM);
        sBeautyTypeToValue.put(str8, BeautyBodySlimAttr.BEAUTY_SHOULDER_SLIM);
        sBeautyTypeToValue.put(str9, BeautyBodySlimAttr.BEAUTY_LEG_SLIM);
        sBeautyTypeToValue.put(str10, BeautyBodySlimAttr.BEAUTY_WHOLE_BODY_SLIM);
        sBeautyTypeToValue.put("pref_beauty_butt_slim_ratio", BeautyBodySlimAttr.BUTT_SLIM);
        sBeautyTypeToValue.put(BeautyConstant.BEAUTY_RESET, BaseEvent.RESET);
        sFilterTypeToName.put(FilterType.N_FIRST.ordinal(), FilterAttr.N_FIRST);
        sFilterTypeToName.put(FilterType.N_SIBOPENK.ordinal(), FilterAttr.N_SIBOPENK);
        sFilterTypeToName.put(FilterType.N_BLACKGOLD.ordinal(), FilterAttr.N_BLACKGOLD);
        sFilterTypeToName.put(FilterType.N_ORANGE.ordinal(), FilterAttr.N_ORANGE);
        sFilterTypeToName.put(FilterType.N_BLACKICE.ordinal(), FilterAttr.N_BLACKICE);
        sFilterTypeToName.put(FilterType.N_TRAVEL.ordinal(), FilterAttr.N_TRAVAEL);
        sFilterTypeToName.put(FilterType.N_DELICACY.ordinal(), FilterAttr.N_DELICACY);
        sFilterTypeToName.put(FilterType.N_FILM.ordinal(), FilterAttr.N_FILM);
        sFilterTypeToName.put(FilterType.N_KOIZORA.ordinal(), FilterAttr.N_KOIZORA);
        sFilterTypeToName.put(FilterType.N_LIVELY.ordinal(), FilterAttr.N_LIVELY);
        sFilterTypeToName.put(FilterType.N_SODA.ordinal(), FilterAttr.N_SODA);
        sFilterTypeToName.put(FilterType.N_CLASSIC.ordinal(), FilterAttr.N_CLASSIC);
        sFilterTypeToName.put(FilterType.B_FAIRYTALE.ordinal(), FilterAttr.B_FAIRYTALE);
        sFilterTypeToName.put(FilterType.B_JAPANESE.ordinal(), FilterAttr.B_JAPANESE);
        sFilterTypeToName.put(FilterType.B_MINT.ordinal(), FilterAttr.B_MINT);
        sFilterTypeToName.put(FilterType.B_MOOD.ordinal(), FilterAttr.B_MOOD);
        sFilterTypeToName.put(FilterType.B_NATURE.ordinal(), FilterAttr.B_NATURE);
        sFilterTypeToName.put(FilterType.B_PINK.ordinal(), FilterAttr.B_PINK);
        sFilterTypeToName.put(FilterType.B_ROMANCE.ordinal(), FilterAttr.B_ROMANCE);
        sFilterTypeToName.put(FilterType.B_MAZE.ordinal(), FilterAttr.B_MAZE);
        sFilterTypeToName.put(FilterType.B_WHITEANDBLACK.ordinal(), FilterAttr.B_WHITEANDBLACK);
        sFilterTypeToName.put(FilterType.S_FILM.ordinal(), FilterAttr.S_FILM);
        sFilterTypeToName.put(FilterType.S_YEARS.ordinal(), FilterAttr.S_YEARS);
        sFilterTypeToName.put(FilterType.S_POLAROID.ordinal(), FilterAttr.S_POLAROID);
        sFilterTypeToName.put(FilterType.S_FOREST.ordinal(), FilterAttr.S_FOREST);
        sFilterTypeToName.put(FilterType.S_BYGONE.ordinal(), FilterAttr.S_BYGONE);
        sFilterTypeToName.put(FilterType.S_WHITEANDBLACK.ordinal(), FilterAttr.S_WHITEANDBLACK);
        sFilterTypeToName.put(FilterType.N_WHITEANDBLACK.ordinal(), FilterAttr.N_WHITEANDBLACK);
        sFilterTypeToName.put(FilterType.B_RIDDLE.ordinal(), FilterAttr.B_RIDDLE);
        sFilterTypeToName.put(FilterType.B_STORY.ordinal(), FilterAttr.B_STORY);
        sFilterTypeToName.put(FilterType.B_MOVIE.ordinal(), FilterAttr.B_MOVIE);
        sFilterTypeToName.put(FilterType.B_M_TEA.ordinal(), FilterAttr.B_M_TEA);
        sFilterTypeToName.put(FilterType.B_M_LILT.ordinal(), FilterAttr.B_M_LILT);
        sFilterTypeToName.put(FilterType.B_M_SEPIA.ordinal(), FilterAttr.B_M_SEPIA);
        sFilterTypeToName.put(FilterType.B_M_WHITEANDBLACK.ordinal(), FilterAttr.B_M_WHITEANDBLACK);
        sFilterTypeToName.put(FilterType.A_DOC.ordinal(), FilterAttr.A1_DOC);
        sFilterTypeToName.put(FilterType.A_FLOWER.ordinal(), FilterAttr.A2_FLOWER);
        sFilterTypeToName.put(FilterType.A_FOOD.ordinal(), FilterAttr.A3_FOOD);
        sFilterTypeToName.put(FilterType.A_PPT.ordinal(), FilterAttr.A4_PPT);
        sFilterTypeToName.put(FilterType.A_SKY.ordinal(), FilterAttr.A5_SKY);
        sFilterTypeToName.put(FilterType.A_SUNRISE_SUNSET.ordinal(), FilterAttr.A6_SUNRISE_SUNSET);
        sFilterTypeToName.put(FilterType.A_CAT.ordinal(), FilterAttr.A7_CAT);
        sFilterTypeToName.put(FilterType.A_DOG.ordinal(), FilterAttr.A8_DOG);
        sFilterTypeToName.put(FilterType.A_GREEN_PLANTS.ordinal(), FilterAttr.A9_GREEN_PLANTS);
        sFilterTypeToName.put(FilterType.A_NIGHT.ordinal(), FilterAttr.A10_NIGHT);
        sFilterTypeToName.put(FilterType.A_SNOW.ordinal(), FilterAttr.A11_SNOW);
        sFilterTypeToName.put(FilterType.A_SEA.ordinal(), FilterAttr.A12_SEA);
        sFilterTypeToName.put(FilterType.A_AUTUMN.ordinal(), FilterAttr.A13_AUTUMN);
        sFilterTypeToName.put(FilterType.A_CANDLELIGHT.ordinal(), FilterAttr.A14_CANDLELIGHT);
        sFilterTypeToName.put(FilterType.A_CAR.ordinal(), FilterAttr.A15_CAR);
        sFilterTypeToName.put(FilterType.A_GRASS.ordinal(), FilterAttr.A16_GRASS);
        sFilterTypeToName.put(FilterType.A_MAPLE_LEAVES.ordinal(), FilterAttr.A17_MAPLE_LEAVES);
        sFilterTypeToName.put(FilterType.A_SUCCULENT.ordinal(), FilterAttr.A18_SUCCULENT);
        sFilterTypeToName.put(FilterType.A_BUILDING.ordinal(), FilterAttr.A19_BUILDING);
        sFilterTypeToName.put(FilterType.A_CITY.ordinal(), FilterAttr.A20_CITY);
        sFilterTypeToName.put(FilterType.A_CLOUD.ordinal(), FilterAttr.A21_CLOUD);
        sFilterTypeToName.put(FilterType.A_OVERCAST.ordinal(), FilterAttr.A22_OVERCAST);
        sFilterTypeToName.put(FilterType.A_BACKLIGHT.ordinal(), FilterAttr.A23_BACKLIGHT);
        sFilterTypeToName.put(FilterType.A_SILHOUETTE.ordinal(), FilterAttr.A24_SILHOUETTE);
        sFilterTypeToName.put(FilterType.A_HUMAN.ordinal(), FilterAttr.A25_HUMAN);
        sFilterTypeToName.put(FilterType.A_JEWELRY.ordinal(), FilterAttr.A26_JEWELRY);
        sFilterTypeToName.put(FilterType.A_BUDDHA.ordinal(), FilterAttr.A27_BUDDHA);
        sFilterTypeToName.put(FilterType.A_COW.ordinal(), FilterAttr.A28_COW);
        sFilterTypeToName.put(FilterType.A_CURRY.ordinal(), FilterAttr.A29_CURRY);
        sFilterTypeToName.put(FilterType.A_MOTORBIKE.ordinal(), FilterAttr.A30_MOTORBIKE);
        sFilterTypeToName.put(FilterType.A_TEMPLE.ordinal(), FilterAttr.A31_TEMPLE);
        sFilterTypeToName.put(FilterType.A_BEACH.ordinal(), FilterAttr.A32_BEACH);
        sFilterTypeToName.put(FilterType.A_DIVING.ordinal(), FilterAttr.A33_DRIVING);
        sFilterTypeToName.put(FilterType.BI_SUNNY.ordinal(), FilterAttr.BI_SUNNY);
        sFilterTypeToName.put(FilterType.BI_PINK.ordinal(), FilterAttr.BI_PINK);
        sFilterTypeToName.put(FilterType.BI_MEMORY.ordinal(), FilterAttr.BI_MEMORY);
        sFilterTypeToName.put(FilterType.BI_STRONG.ordinal(), FilterAttr.BI_STRONG);
        sFilterTypeToName.put(FilterType.BI_WARM.ordinal(), FilterAttr.BI_WARM);
        sFilterTypeToName.put(FilterType.BI_RETRO.ordinal(), FilterAttr.BI_RETRO);
        sFilterTypeToName.put(FilterType.BI_ROMANTIC.ordinal(), FilterAttr.BI_ROMANTIC);
        sFilterTypeToName.put(FilterType.BI_SWEET.ordinal(), FilterAttr.BI_SWEET);
        sFilterTypeToName.put(FilterType.BI_PORTRAIT.ordinal(), FilterAttr.BI_PORTRAIT);
        sFilterTypeToName.put(FilterType.BI_YOUNG.ordinal(), FilterAttr.BI_YOUNG);
        sFilterTypeToName.put(FilterType.BI_M_DUSK.ordinal(), FilterAttr.BI_M_DUSK);
        sFilterTypeToName.put(FilterType.BI_M_LILT.ordinal(), FilterAttr.BI_M_LILT);
        sFilterTypeToName.put(FilterType.BI_M_TEA.ordinal(), FilterAttr.BI_M_TEA);
        sFilterTypeToName.put(FilterType.BI_M_SEPIA.ordinal(), FilterAttr.BI_M_SEPIA);
        sFilterTypeToName.put(FilterType.BI_M_WHITEANDBLACK.ordinal(), FilterAttr.BI_M_WHITEANDBLACK);
        sFilterTypeToName.put(FilterType.ML_BLUE.ordinal(), FilterAttr.ML_BLUE);
        sFilterTypeToName.put(FilterType.ML_CONTRAST.ordinal(), FilterAttr.ML_CONTRAST);
        sFilterTypeToName.put(FilterType.ML_DEEPBLACK.ordinal(), FilterAttr.ML_DEEPBLACK);
        sFilterTypeToName.put(FilterType.ML_FAIR.ordinal(), FilterAttr.ML_FAIR);
        sFilterTypeToName.put(FilterType.ML_HONGKONG.ordinal(), FilterAttr.ML_HONGKONG);
        sFilterTypeToName.put(FilterType.ML_MOUSSE.ordinal(), FilterAttr.ML_MOUSSE);
        sFilterTypeToName.put(FilterType.ML_SOLAR.ordinal(), FilterAttr.ML_SOLAR);
        sFilterTypeToName.put(FilterType.ML_YEARS.ordinal(), FilterAttr.ML_YEARS);
        sSpeedToName.put(0, "Super slow");
        sSpeedToName.put(1, "Slow");
        sSpeedToName.put(2, "Regular");
        sSpeedToName.put(3, "Fast");
        sSpeedToName.put(4, "Super fast");
    }

    private static String addCameraSuffix(String str) {
        if (str == null) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_");
        sb.append(CameraSettings.isFrontCamera() ? "front" : "back");
        return sb.toString();
    }

    private static void addUltraPixelParameter(Map map) {
        boolean isUltraPixelOn = CameraSettings.isUltraPixelOn();
        boolean isFrontCamera = CameraSettings.isFrontCamera();
        String str = "off";
        String str2 = Manual.PARAM_SUPERME_PIXEL_VALUE;
        if (!isFrontCamera) {
            int OO000oO = C0122O00000o.instance().OO000oO();
            boolean isRear108MPSwitchOn = DataRepository.dataItemRunning().getComponentUltraPixel().isRear108MPSwitchOn();
            if (OO000oO == 1) {
                if (isUltraPixelOn) {
                    str = Manual.VALUE_SUPERME_PIXEL_48M_ON;
                }
            } else if (OO000oO == 2) {
                if (isUltraPixelOn) {
                    str = Manual.VALUE_SUPERME_PIXEL_64M_ON;
                }
            } else if (isRear108MPSwitchOn) {
                map.put(str2, Manual.VALUE_SUPERME_PIXEL_108M_ON);
                return;
            }
        } else if (C0122O00000o.instance().O0ooo00() != 0) {
            return;
        } else {
            if (isUltraPixelOn) {
                str = Manual.VALUE_SUPERME_PIXEL_32M_ON;
            }
        }
        map.put(str2, str);
    }

    private static void addUltraPixelParameter(boolean z, Map map) {
        boolean isUltraPixelOn = CameraSettings.isUltraPixelOn();
        String str = "off";
        String str2 = PictureQuality.PARAM_ULTRA_PIXEL;
        if (!z) {
            int OO000oO = C0122O00000o.instance().OO000oO();
            if (OO000oO == 1) {
                if (isUltraPixelOn) {
                    str = PictureQuality.VALUE_ULTRA_PIXEL_48MP;
                }
            } else if (OO000oO == 2) {
                if (isUltraPixelOn) {
                    str = PictureQuality.VALUE_ULTRA_PIXEL_64MP;
                }
            } else if (OO000oO != 3) {
                return;
            } else {
                if (isUltraPixelOn) {
                    str = PictureQuality.VALUE_ULTRA_PIXEL_108MP;
                }
            }
        } else if (C0122O00000o.instance().O0ooo00() != 0) {
            return;
        } else {
            if (isUltraPixelOn) {
                str = PictureQuality.VALUE_ULTRA_PIXEL_32MP;
            }
        }
        map.put(str2, str);
    }

    private static String antiBandingToName(String str) {
        String str2 = BaseEvent.OTHERS;
        if (str == null) {
            Log.e(TAG, "null antiBanding");
            return str2;
        }
        char c = 65535;
        switch (str.hashCode()) {
            case 48:
                if (str.equals("0")) {
                    c = 0;
                    break;
                }
                break;
            case 49:
                if (str.equals("1")) {
                    c = 1;
                    break;
                }
                break;
            case 50:
                if (str.equals("2")) {
                    c = 2;
                    break;
                }
                break;
            case 51:
                if (str.equals("3")) {
                    c = 3;
                    break;
                }
                break;
        }
        if (c == 0) {
            return "off";
        }
        if (c == 1) {
            return "50hz";
        }
        if (c == 2) {
            return "60hz";
        }
        if (c == 3) {
            return "auto";
        }
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unexpected antiBanding ");
        sb.append(str);
        Log.e(str3, sb.toString());
        return str2;
    }

    private static String autoWhiteBalanceToName(String str) {
        String str2 = BaseEvent.OTHERS;
        if (str == null) {
            Log.e(TAG, "null awb");
            return str2;
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != -1081415738) {
            if (hashCode != 53) {
                if (hashCode != 54) {
                    switch (hashCode) {
                        case 49:
                            if (str.equals("1")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 50:
                            if (str.equals("2")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 51:
                            if (str.equals("3")) {
                                c = 3;
                                break;
                            }
                            break;
                    }
                } else if (str.equals("6")) {
                    c = 5;
                }
            } else if (str.equals("5")) {
                c = 4;
            }
        } else if (str.equals("manual")) {
            c = 0;
        }
        if (c != 0) {
            if (c == 1) {
                str = "auto";
            } else if (c == 2) {
                return "incandescent";
            } else {
                if (c == 3) {
                    return "fluorescent";
                }
                if (c == 4) {
                    return "daylight";
                }
                if (c == 5) {
                    return "cloudy-daylight";
                }
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("unexpected awb ");
                sb.append(str);
                Log.e(str3, sb.toString());
                return str2;
            }
        }
        return str;
    }

    public static String burstShotNumToName(int i) {
        return divideTo10Section(i);
    }

    public static String cameraIdToName(boolean z) {
        return z ? "front" : "back";
    }

    private static String contrastToName(String str) {
        return pictureQualityToName(R.array.pref_camera_contrast_entryvalues, str);
    }

    private static String divideTo10Section(int i) {
        if (i == 0) {
            return BeautyBodySlimAttr.ZERO;
        }
        switch (i > 0 ? (i - 1) / 10 : 0) {
            case 0:
                return BeautyBodySlimAttr.ONE;
            case 1:
                return BeautyBodySlimAttr.TEN;
            case 2:
                return BeautyBodySlimAttr.TWENTY;
            case 3:
                return BeautyBodySlimAttr.THIRDTY;
            case 4:
                return BeautyBodySlimAttr.FOURTY;
            case 5:
                return BeautyBodySlimAttr.FIFTY;
            case 6:
                return BeautyBodySlimAttr.SIXTY;
            case 7:
                return BeautyBodySlimAttr.SEVENTY;
            case 8:
                return BeautyBodySlimAttr.EIGHTTY;
            default:
                return BeautyBodySlimAttr.NINETY;
        }
    }

    private static String exposureTimeToName(String str) {
        if (str != null) {
            try {
                int parseLong = (int) (Long.parseLong(str) / 1000);
                if (parseLong < 1000000) {
                    String str2 = (String) sExposureTimeLessThan1sToName.get(parseLong);
                    if (str2 != null) {
                        return str2;
                    }
                } else {
                    int i = parseLong / 1000000;
                    StringBuilder sb = new StringBuilder();
                    sb.append(i);
                    sb.append("s");
                    return sb.toString();
                }
            } catch (NumberFormatException unused) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("invalid exposure time ");
                sb2.append(str);
                Log.e(str3, sb2.toString());
            }
        }
        String str4 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("unexpected exposure time ");
        sb3.append(str);
        Log.e(str4, sb3.toString());
        return "auto";
    }

    public static String faceBeautyRatioToName(int i) {
        return i == 0 ? "0" : divideTo10Section(i);
    }

    private static String filterIdToName(int i) {
        if (FilterInfo.FILTER_ID_NONE == i) {
            return BaseEvent.RESET;
        }
        int category = FilterInfo.getCategory(i);
        if (category == 1 || category == 2 || category == 3 || category == 8) {
            String str = (String) sFilterTypeToName.get(FilterInfo.getIndex(i));
            if (str != null) {
                return str;
            }
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unexpected filter id: ");
        sb.append(Integer.toHexString(i));
        Log.e(str2, sb.toString());
        return "none";
    }

    private static String flashModeToName(String str) {
        String str2 = BaseEvent.OTHERS;
        if (str == null) {
            Log.e(TAG, "null flash mode");
            return str2;
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != 48626) {
            if (hashCode != 48628) {
                switch (hashCode) {
                    case 48:
                        if (str.equals("0")) {
                            c = 5;
                            break;
                        }
                        break;
                    case 49:
                        if (str.equals("1")) {
                            c = 1;
                            break;
                        }
                        break;
                    case 50:
                        if (str.equals("2")) {
                            c = 4;
                            break;
                        }
                        break;
                    case 51:
                        if (str.equals("3")) {
                            c = 0;
                            break;
                        }
                        break;
                }
            } else if (str.equals(ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO)) {
                c = 2;
            }
        } else if (str.equals(ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON)) {
            c = 3;
        }
        if (c == 0) {
            return "auto";
        }
        if (c == 1) {
            return "on";
        }
        if (c == 2) {
            return FlashAttr.FLASH_VALUE_SCREEN_LIGHT_AUTO;
        }
        if (c == 3) {
            return FlashAttr.FLASH_VALUE_SCREEN_LIGHT_ON;
        }
        if (c == 4) {
            return "torch";
        }
        if (c == 5) {
            return "off";
        }
        String str3 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unexpected flash mode ");
        sb.append(str);
        Log.e(str3, sb.toString());
        return str2;
    }

    private static String focusPositionToName(int i) {
        return 1000 == i ? "auto" : divideTo10Section((1000 - i) / 10);
    }

    public static String getDocumentModeValue(int i) {
        return CameraSettings.isDocumentModeOn(i) ? DataRepository.dataItemRunning().getComponentRunningDocument().getComponentValue(i) : "off";
    }

    private static String getDualZoomName(int i) {
        float retainZoom;
        if (i == 166 || i == 167) {
            String cameraLensType = CameraSettings.getCameraLensType(i);
            if (ComponentManuallyDualLens.LENS_ULTRA.equals(cameraLensType)) {
                retainZoom = HybridZoomingSystem.FLOAT_ZOOM_RATIO_ULTR;
            } else if (ComponentManuallyDualLens.LENS_TELE.equals(cameraLensType)) {
                retainZoom = HybridZoomingSystem.getTeleMinZoomRatio();
            } else if (ComponentManuallyDualLens.LENS_ULTRA_TELE.equals(cameraLensType)) {
                retainZoom = HybridZoomingSystem.getUltraTeleMinZoomRatio();
            } else if (ComponentManuallyDualLens.LENS_WIDE.equals(cameraLensType)) {
                retainZoom = 1.0f;
            }
            return HybridZoomingSystem.toString(retainZoom);
        }
        retainZoom = CameraSettings.getRetainZoom(i);
        return HybridZoomingSystem.toString(retainZoom);
    }

    private static int indexOfString(String[] strArr, String str) {
        if (!(strArr == null || str == null)) {
            for (int i = 0; i < strArr.length; i++) {
                if (str.equals(strArr[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static boolean isMultiCameraDualVideo() {
        return C0122O00000o.instance().OOO000o();
    }

    private static String isoToName(String str) {
        if (str != null) {
            String str2 = "auto";
            if (str2.equalsIgnoreCase(str)) {
                return str2;
            }
            if (str.toUpperCase(Locale.ENGLISH).indexOf(ExifInterface.TAG_RW2_ISO) > -1) {
                str = str.substring(3);
            }
        }
        return str;
    }

    public static String modeIdToName(int i) {
        String str = (String) sCameraModeIdToName.get(i);
        return str == null ? "M_unspecified_" : str;
    }

    private static String pictureQualityToName(int i, String str) {
        String[] stringArray = CameraAppImpl.getAndroidContext().getResources().getStringArray(i);
        if (sPictureQualityIndexToName.size() >= stringArray.length) {
            int indexOfString = indexOfString(stringArray, str);
            if (indexOfString <= -1) {
                return BaseEvent.OTHERS;
            }
            return (String) sPictureQualityIndexToName.get(indexOfString + ((sPictureQualityIndexToName.size() - stringArray.length) / 2));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("picture quality array size is smaller than values size ");
        sb.append(str.length());
        throw new RuntimeException(sb.toString());
    }

    private static long round(long j, int i) {
        if (i <= 0) {
            return j;
        }
        long j2 = (long) i;
        return ((j + ((long) (i / 2))) / j2) * j2;
    }

    private static String saturationToName(String str) {
        return pictureQualityToName(R.array.pref_camera_saturation_entryvalues, str);
    }

    private static String sharpnessToName(String str) {
        return pictureQualityToName(R.array.pref_camera_sharpness_entryvalues, str);
    }

    public static String slowMotionConfigToName(String str) {
        return ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_120.equals(str) ? SlowMotion.FPS_120 : ComponentConfigSlowMotion.DATA_CONFIG_NEW_SLOW_MOTION_240.equals(str) ? SlowMotion.FPS_240 : SlowMotion.FPS_960;
    }

    private static String slowMotionQualityIdToName(String str) {
        String str2 = BaseEvent.OTHERS;
        if (str == null) {
            return str2;
        }
        char c = 65535;
        int hashCode = str.hashCode();
        if (hashCode != 53) {
            if (hashCode == 54 && str.equals("6")) {
                c = 1;
            }
        } else if (str.equals("5")) {
            c = 0;
        }
        return c != 0 ? c != 1 ? str2 : BaseEvent.QUALITY_1080P : BaseEvent.QUALITY_720P;
    }

    private static String speedIdToName(int i) {
        String str = (String) sSpeedToName.get(i);
        return str != null ? str : (String) sSpeedToName.get(2);
    }

    public static void tarckBokenChanged(int i, String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(AlgoAttr.PARAM_BOKEN, str, null);
        }
    }

    public static String timeLapseIntervalToName(int i) {
        if (i < 1000) {
            return String.format(Locale.ENGLISH, "%.2fs", new Object[]{Float.valueOf(((float) i) / 1000.0f)});
        }
        return String.format(Locale.ENGLISH, "%ds", new Object[]{Integer.valueOf(i / 1000)});
    }

    public static void track8KVideo(int i, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, FeatureName.VALUE_8k_VIDEO_FPS);
        hashMap.put(BaseEvent.VALUE, str);
        hashMap.put(BaseEvent.MODULE_NAME, MistatsWrapper.getStatsModuleKey(i));
        hashMap.put(BaseEvent.PARAM_VIDEO_RATIO_MOVIE, CameraSettings.isCinematicAspectRatioEnabled(i) ? "on" : "off");
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackAIAudio(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_AI_AUDIO, str);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackAISceneChanged(int i, int i2, Resources resources) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            TypedArray obtainTypedArray = resources.obtainTypedArray(R.array.ai_scene_names);
            String string = (i2 < 0 || i2 >= obtainTypedArray.length()) ? BaseEvent.UNSPECIFIED : obtainTypedArray.getString(i2);
            obtainTypedArray.recycle();
            MistatsWrapper.commonKeyTriggerEvent(AlgoAttr.VAULE_AI_SCENE, string, null);
        }
    }

    public static void trackAIWatermarkCapture(String str, String str2, String str3, String str4) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(AIWatermark.AI_WATERMARK_TYPE, str);
            hashMap.put(AIWatermark.AI_WATERMARK_KEY, str2);
            hashMap.put(AIWatermark.AI_WATERMARK_MOVE, str3);
            hashMap.put(AIWatermark.AI_WATERMARK_ORIENTATION, str4);
            MistatsWrapper.mistatEvent("ai_watermark", hashMap);
            trackAIWatermarkCategory(str);
        }
    }

    private static void trackAIWatermarkCategory(String str) {
        String str2;
        if (!MistatsWrapper.isCounterEventDisabled()) {
            try {
                int intValue = Integer.valueOf(str).intValue();
                HashMap hashMap = new HashMap();
                String str3 = AIWatermark.AI_WATERMARK_CATEGORY;
                switch (intValue) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        str2 = AIWatermark.AI_WATERMARK_MANUAL;
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        str2 = AIWatermark.AI_WATERMARK_AI;
                        break;
                    default:
                        MistatsWrapper.mistatEvent("ai_watermark", hashMap);
                }
                hashMap.put(str3, str2);
                MistatsWrapper.mistatEvent("ai_watermark", hashMap);
            } catch (NumberFormatException unused) {
                Log.w(TAG, "NumberFormatException when parser type");
            }
        }
    }

    public static void trackAIWatermarkClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent("ai_watermark", hashMap);
        }
    }

    public static void trackAIWatermarkKey(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(AIWatermark.AI_WATERMARK_SELECT, str);
            MistatsWrapper.mistatEvent("ai_watermark", hashMap);
        }
    }

    public static void trackAmbilightCapture(int i, long j, boolean z, int i2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append("value_");
            sb.append(i);
            hashMap.put(Ambilight.PARAM_AMBILIGHT_SCENE_MODE, sb.toString());
            hashMap.put(BaseEvent.COST_TIME, String.valueOf(round(j, 50)));
            hashMap.put("attr_auto_hibernation", z ? "on" : "off");
            hashMap.put(AutoHibernation.PARAM_AUTO_HIBERNATION_COUNT, String.valueOf(i2));
            MistatsWrapper.mistatEvent(Ambilight.KEY_AMBILIGHT, hashMap);
        }
    }

    public static void trackAmbilightClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(Ambilight.KEY_AMBILIGHT, hashMap);
        }
    }

    public static void trackAmbilightGenerateVideo() {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(Ambilight.PARAM_AMBILIGHT_GENERATE_VIDEO, BaseEvent.VALUE_SUCCESS);
            MistatsWrapper.mistatEvent(Ambilight.KEY_AMBILIGHT, hashMap);
        }
    }

    public static void trackAwbChanged(String str, int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i == 167 ? "M_manual_" : "M_proVideo_", Manual.AWB, (Object) autoWhiteBalanceToName(str));
        }
    }

    public static void trackBeautyClick(@ShineType String str, String str2) {
        String str3;
        if (!TextUtils.isEmpty(str2)) {
            HashMap hashMap = new HashMap();
            int currentMode = DataRepository.dataItemGlobal().getCurrentMode();
            hashMap.put(BaseEvent.MODULE_NAME, DataRepository.dataItemGlobal().isIntentIDPhoto() ? ModuleName.ID_PHOTO : modeIdToName(currentMode));
            hashMap.put(BaseEvent.MODE, BaseEvent.PHOTO);
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 1572) {
                if (hashCode != 1574) {
                    switch (hashCode) {
                        case 49:
                            if (str.equals("1")) {
                                c = 0;
                                break;
                            }
                            break;
                        case 50:
                            if (str.equals("2")) {
                                c = 1;
                                break;
                            }
                            break;
                        case 51:
                            if (str.equals("3")) {
                                c = 2;
                                break;
                            }
                            break;
                        case 52:
                            if (str.equals("4")) {
                                c = 3;
                                break;
                            }
                            break;
                        case 53:
                            if (str.equals("5")) {
                                c = 6;
                                break;
                            }
                            break;
                        case 54:
                            if (str.equals("6")) {
                                c = 7;
                                break;
                            }
                            break;
                    }
                } else if (str.equals("17")) {
                    c = 5;
                }
            } else if (str.equals("15")) {
                c = 4;
            }
            String str4 = null;
            switch (c) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    str4 = (String) sBeautyTypeToValue.get(str2);
                    str3 = BeautyAttr.KEY_BEAUTY_FACE;
                    break;
                case 7:
                    str4 = (String) sBeautyTypeToValue.get(str2);
                    str3 = BeautyBodySlimAttr.KEY_BODY_SLIM;
                    break;
                default:
                    str3 = null;
                    break;
            }
            if (!TextUtils.isEmpty(str4)) {
                hashMap.put(BeautyBodySlimAttr.BEAUTY_PORT, str4);
            }
            if (!TextUtils.isEmpty(str3)) {
                MistatsWrapper.mistatEvent(str3, hashMap);
            }
        }
    }

    public static void trackBeautyInfo(int i, String str, BeautyValues beautyValues) {
        String str2;
        String[] strArr;
        String[] strArr2;
        HashMap hashMap = new HashMap();
        if (beautyValues != null && beautyValues.isFaceBeautyOn()) {
            boolean Oo00O = C0124O00000oO.Oo00O();
            String str3 = BeautyAttr.PARAM_BEAUTY_LEVEL;
            if (Oo00O) {
                int i2 = 0;
                for (String str4 : BeautyConstant.BEAUTY_CATEGORY_LEVEL) {
                    String str5 = (String) sBeautyTypeToName.get(str4);
                    if (str5 != null) {
                        hashMap.put(str5, faceBeautyRatioToName(beautyValues.getValueByType(str4)));
                    }
                }
                boolean equals = "front".equals(str);
                str2 = BeautyAttr.KEY_BEAUTY;
                if (equals) {
                    if (beautyValues.isFaceBeautyOn()) {
                        for (String str6 : BeautyConstant.BEAUTY_CATEGORY_FRONT_MAKEUP) {
                            String str7 = (String) sBeautyTypeToName.get(str6);
                            if (str7 != null) {
                                hashMap.put(str7, faceBeautyRatioToName(beautyValues.getValueByType(str6)));
                            }
                        }
                        if (beautyValues != null) {
                            hashMap.put(BeautyAttr.PARAM_BEAUTY_MAKEUP_SWITCH, beautyValues.isBeautyMakeUpOn() ? "on" : "off");
                        }
                    }
                    if (beautyValues.isBeautyMakeUpOn()) {
                        String[] strArr3 = BeautyConstant.BEAUTY_CATEGORY_FRONT_REMODELING;
                        int length = strArr3.length;
                        while (i2 < length) {
                            String str8 = strArr3[i2];
                            String str9 = (String) sBeautyTypeToName.get(str8);
                            if (str9 != null) {
                                hashMap.put(str9, faceBeautyRatioToName(beautyValues.getValueByType(str8)));
                            }
                            i2++;
                        }
                    }
                } else if (beautyValues.isBeautyBodyOn()) {
                    String[] strArr4 = BeautyConstant.BEAUTY_CATEGORY_BACK_FIGURE;
                    int length2 = strArr4.length;
                    while (i2 < length2) {
                        String str10 = strArr4[i2];
                        String str11 = (String) sBeautyTypeToName.get(str10);
                        if (str11 != null) {
                            hashMap.put(str11, faceBeautyRatioToName(beautyValues.getValueByType(str10)));
                        }
                        i2++;
                    }
                    str2 = BeautyBodySlimAttr.KEY_BODY_SLIM;
                }
                if (beautyValues != null) {
                    hashMap.put(str3, String.valueOf(beautyValues.mBeautySkinSmooth));
                }
            } else {
                hashMap.put(BeautyAttr.PARAM_BEAUTY_SLIM_FACE, faceBeautyRatioToName(beautyValues.mBeautySlimFace));
                hashMap.put(BeautyAttr.PARAM_BEAUTY_ENLARGE_EYE, faceBeautyRatioToName(beautyValues.mBeautyEnlargeEye));
                hashMap.put(BeautyAttr.PARAM_BEAUTY_SKIN_COLOR, faceBeautyRatioToName(beautyValues.mBeautySkinColor));
                hashMap.put(BeautyAttr.PARAM_BEAUTY_SKIN_SMOOTH, faceBeautyRatioToName(beautyValues.mBeautySkinSmooth));
                hashMap.put(str3, String.valueOf(beautyValues.mBeautyLevel));
                str2 = BeautyAttr.KEY_BEAUTY_OLD;
            }
            hashMap.put(BaseEvent.COUNT, String.valueOf(i));
            MistatsWrapper.mistatEvent(str2, hashMap);
        }
    }

    public static void trackBeautySwitchChanged(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BeautyAttr.PARAM_BEAUTY_LEVEL, str);
            MistatsWrapper.mistatEvent(BeautyAttr.KEY_BEAUTY_CLICK, hashMap);
        }
    }

    public static void trackBroadcastKillService() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, Error.CAMERA_BROADCAST_KILL_SERVICE);
        MistatsWrapper.mistatEvent(Error.KEY_CAMERA_EXCEPTION, hashMap);
    }

    public static void trackCTADialogAgree() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_CTA_AGREE, BaseEvent.ACCEPT);
        MistatsWrapper.mistatEvent(Other.KEY_CTA_CLICK_DIALOG_AGREE, hashMap);
    }

    public static void trackCTADialogDisagree() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_CTA_DISAGREE, BaseEvent.REJECT);
        MistatsWrapper.mistatEvent(Other.KEY_CTA_CLICK_DIALOG_DISAGREE, hashMap);
    }

    public static void trackCallerControl(Intent intent, String str) {
        if (intent != null) {
            new HashMap();
            try {
                CameraIntentManager.getInstance(intent).isUseFrontCamera();
            } catch (Exception unused) {
            }
            MistatsWrapper.commonKeyTriggerEvent(BaseEvent.CAMERA_CALLER, str, null);
        }
    }

    public static void trackCameraError(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.VALUE_ERROR_MSG, str);
        hashMap.put(BaseEvent.FEATURE_NAME, Error.CAMERA_HARDWARE_ERROR);
        MistatsWrapper.mistatEvent(Error.KEY_CAMERA_EXCEPTION, hashMap);
    }

    public static void trackCameraErrorDialogShow() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, Error.CAMERA_ERROR_DIALOG_SHOW);
        MistatsWrapper.mistatEvent(Error.KEY_CAMERA_EXCEPTION, hashMap);
    }

    public static void trackCapturePortrait(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        if (C0122O00000o.instance().isSupportBokehAdjust()) {
            map.put(PortraitAttr.PARAM_BOKEH_RATIO, CameraSettings.readFNumber());
        }
        map.put(PortraitAttr.PARAM_ULTRA_WIDE_BOKEH, DataRepository.dataItemRunning().isSwitchOn("pref_ultra_wide_bokeh_enabled") ? "on" : "off");
        if (!CameraSettings.isFrontCamera()) {
            map.put(PortraitAttr.PARAM_PORTRAIT_LIGHTING, String.valueOf(CameraSettings.getPortraitLightingPattern()));
        }
        map.put(BaseEvent.MODE, BaseEvent.PHOTO);
        MistatsWrapper.moduleCaptureEvent("M_portrait_", map);
    }

    public static void trackCaptureSuperNight(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        MistatsWrapper.moduleCaptureEvent("M_superNight_", map);
    }

    public static void trackCaptureSuperNightVideo(Map map) {
        if (map == null) {
            map = new HashMap();
        }
        MistatsWrapper.moduleRecordEvent("M_superNight_", map);
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0050  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void trackCloneCaptureHint(Mode mode, Message message) {
        String str;
        HashMap hashMap = new HashMap();
        int i = AnonymousClass1.$SwitchMap$com$xiaomi$fenshen$FenShenCam$Message[message.ordinal()];
        String str2 = i != 1 ? (i == 2 || i == 3) ? "NO_SUBJECT" : i != 4 ? "" : "TOO_MUCH_MOVEMENT" : "RETURN_ORIGINAL_POSITION";
        if (!TextUtils.isEmpty(str2)) {
            if (mode == Mode.PHOTO) {
                str = CloneAttr.ATTR_PHOTO_CAPTURE_HINT;
            } else if (mode == Mode.VIDEO) {
                str = CloneAttr.ATTR_VIDEO_CAPTURE_HINT;
            } else if (mode == Mode.MCOPY) {
                str = CloneAttr.ATTR_FREEZE_FRAME_CAPTURE_HINT;
            } else {
                if (mode == Mode.TIMEFREEZE) {
                    str = FilmAttr.ATTR_TIME_FREEZE_CAPTURE_HINT;
                }
                if (!hashMap.isEmpty()) {
                    MistatsWrapper.mistatEvent(CloneAttr.KEY_CLONE, hashMap);
                }
            }
            hashMap.put(str, str2);
            if (!hashMap.isEmpty()) {
            }
        }
    }

    public static void trackCloneCaptureParams(Mode mode, int i, String str) {
        String valueOf;
        String str2;
        HashMap hashMap = new HashMap();
        Mode mode2 = Mode.PHOTO;
        String str3 = CloneAttr.ATTR_CLONE_MODE;
        if (mode == mode2) {
            hashMap.put(str3, mode.name());
            valueOf = String.valueOf(i);
            str2 = CloneAttr.ATTR_PHOTO_SUBJECT_COUNT;
        } else if (mode == Mode.VIDEO) {
            hashMap.put(str3, mode.name());
            valueOf = String.valueOf(str);
            str2 = "attr_video_duration";
        } else {
            if (mode == Mode.MCOPY) {
                hashMap.put(str3, "FREEZE_FRAME");
                hashMap.put(CloneAttr.ATTR_FREEZE_FRAME_VIDEO_DURATION, String.valueOf(str));
                valueOf = String.valueOf(i);
                str2 = CloneAttr.ATTR_FREEZE_FRAME_SUBJECT_COUNT;
            }
            MistatsWrapper.mistatEvent(CloneAttr.KEY_CLONE, hashMap);
        }
        hashMap.put(str2, valueOf);
        MistatsWrapper.mistatEvent(CloneAttr.KEY_CLONE, hashMap);
    }

    public static void trackCloneClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(CloneAttr.KEY_CLONE, hashMap);
        }
    }

    public static void trackCommonModeFull() {
        HashMap hashMap = new HashMap();
        hashMap.put(EditMode.PARAM_COMMON_MODE_FULL, String.valueOf(1));
        MistatsWrapper.mistatEventSimple(EditMode.KEY_CAMERA_MODE_EDIT, hashMap);
    }

    public static void trackCustomizeCameraSettingClick(String str) {
        new HashMap();
    }

    public static void trackDirectionChanged(int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            String str = i != 3 ? i != 4 ? i != 5 ? i != 6 ? "unknown" : Panorama.PANORAMA_DIRECTION_T2B : Panorama.PANORAMA_DIRECTION_B2T : Panorama.PANORAMA_DIRECTION_R2L : Panorama.PANORAMA_DIRECTION_L2R;
            MistatsWrapper.moduleUIClickEvent("M_panorama_", Panorama.PANORAMA_DIRECTION, (Object) str);
        }
    }

    public static void trackDirectionToggle(boolean z) {
        MistatsWrapper.moduleUIClickEvent("M_panorama_", Panorama.PANORAMA_TOGGLE_V_H, (Object) z ? Panorama.PANORAMA_TOGGLE_VERTICAL : Panorama.PANORAMA_TOGGLE_HORIZONTAL);
    }

    public static void trackDocumentDetectBlurHintShow() {
        HashMap hashMap = new HashMap();
        hashMap.put(CaptureAttr.PARAM_ASD_DETECT_TIP, CaptureAttr.VALUE_ASD_DOCUMENT_BLUR_TIP);
        MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
    }

    public static void trackDocumentModeChanged(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_DOCUMENT_MODE, str, null);
        }
    }

    public static void trackDollyZoomClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM_DOLLY_ZOOM, hashMap);
        }
    }

    public static void trackDollyZoomZoomValue(boolean z, float f) {
        String str;
        String str2;
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            if (z) {
                str2 = String.valueOf(f);
                str = FilmAttr.PARAM_DOLLY_ZOOM_EXCEPTION_ZOOM_VALUE;
            } else {
                str2 = String.valueOf(f);
                str = FilmAttr.PARAM_DOLLY_ZOOM_NORMAL_ZOOM_VALUE;
            }
            hashMap.put(str, str2);
            MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM_DOLLY_ZOOM, hashMap);
        }
    }

    public static void trackDualVideoCommonAttr(String str, String str2) {
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        hashMap.put(str, str2);
        String str3 = "on";
        String str4 = "off";
        hashMap2.put(VideoAttr.PARAM_HEADSET, CameraSettings.getHeadSetState() ? str3 : str4);
        hashMap2.put(VideoAttr.PARAM_BLUETOOTH_EARPHONE_VIDEO, CameraSettings.getHeadSetState() ? str3 : str4);
        hashMap2.put(VideoAttr.PARAM_KARAOKE, CameraSettings.getKaraokeState() ? str3 : str4);
        hashMap2.put(VideoAttr.PARAM_KARAOKE_VIDEO, CameraSettings.getKaraokeState() ? str3 : str4);
        hashMap2.put(VideoAttr.PARAM_AI_NOISE_REDUCTION, CameraSettings.getAiNoiseReductionState() ? str3 : str4);
        if (!CameraSettings.getAiNoiseReductionState()) {
            str3 = str4;
        }
        hashMap2.put(VideoAttr.PARAM_AI_NOISE_REDUCTION_VIDEO, str3);
        MistatsWrapper.moduleMistatsEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap2);
        MistatsWrapper.mistatEvent(isMultiCameraDualVideo() ? MultiCameraAttr.KEY_MULTI_CAMERA_DUAL_VIDEO : DualVideoAttr.KEY_DUAL_VIDEO, hashMap);
    }

    public static void trackDualVideoCommonClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(isMultiCameraDualVideo() ? MultiCameraAttr.KEY_MULTI_CAMERA_DUAL_VIDEO : DualVideoAttr.KEY_DUAL_VIDEO, hashMap);
        }
    }

    public static void trackDualWaterMarkChanged(boolean z) {
        MistatsWrapper.settingClickEvent(Setting.PARAM_DEVICE_WATERMARK, Boolean.valueOf(z));
    }

    public static void trackDualZoomChanged(int i, String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.MODULE_NAME, DataRepository.dataItemGlobal().isIntentIDPhoto() ? ModuleName.ID_PHOTO : modeIdToName(i));
            hashMap.put(Zoom.PARAM_ZOOM_RATIO, str);
            if (HybridZoomingSystem.IS_3_OR_MORE_SAT) {
                hashMap.put(Zoom.PARAM_SAT_ZOOM_RATIO, str);
            }
            MistatsWrapper.mistatEvent(Zoom.KEY_ZOOM, hashMap);
        }
    }

    public static void trackEVChanged(String str, int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i == 167 ? "M_manual_" : "M_proVideo_", "exposureValue", (Object) str);
        }
    }

    public static void trackEnterMoreMode(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(MoreMode.PARAM_ENTER_MORE_MODE_TYPE, str);
            MistatsWrapper.mistatEventSimple(MoreMode.KEY_CAMERA_MORE_MODE, hashMap);
        }
    }

    public static void trackEvAdjusted(float f) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_EV_ADJUSTED, Float.valueOf(f), null);
    }

    public static void trackExposureTimeChanged(String str, int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i == 167 ? "M_manual_" : "M_proVideo_", Manual.ET, (Object) exposureTimeToName(str));
        }
    }

    public static void trackFeatureInstallError(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureAttr.KEY_FEATURE_INSTALL_ERROR, str);
        MistatsWrapper.mistatEvent(FeatureAttr.KEY_FEATURE, hashMap);
    }

    public static void trackFeatureInstallOperation(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, str);
        MistatsWrapper.mistatEvent(FeatureAttr.KEY_FEATURE, hashMap);
    }

    public static void trackFeatureInstallResult(String str, String str2, int i) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        String str3 = "_";
        sb.append(str3);
        sb.append(str2);
        sb.append(str3);
        sb.append(i);
        hashMap.put(FeatureAttr.KEY_FEATURE_INSTALL_RESULT, sb.toString());
        MistatsWrapper.mistatEvent(FeatureAttr.KEY_FEATURE, hashMap);
    }

    public static void trackFeatureInstallStartClick(String str, String str2, int i, boolean z) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_");
        sb.append(str2);
        hashMap.put(FeatureAttr.KEY_FEATURE_NAME_VERSION, sb.toString());
        hashMap.put(FeatureAttr.KEY_FEATURE_INSTALL_FROM, String.valueOf(i));
        hashMap.put(FeatureAttr.KEY_FEATURE_INSTALL_WIFI, String.valueOf(z));
        MistatsWrapper.mistatEvent(FeatureAttr.KEY_FEATURE, hashMap);
    }

    public static void trackFilmDreamClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM_DREAM, hashMap);
        }
    }

    public static void trackFilmStartClick(String str, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(FilmAttr.PARAM_FILM_TEMPLATE_NAME, str);
        hashMap.put(FilmAttr.PARAM_FILM_CLICK_TEMPLATE_PREVIEW, z ? BaseEvent.VALUE_TRUE : BaseEvent.VALUE_FALSE);
        MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM, hashMap);
    }

    public static void trackFilmTemplateThumbnailClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(FilmAttr.PARAM_FILM_TEMPLATE_NAME, str);
        MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM, hashMap);
    }

    public static void trackFilmTimeFreezeClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM_DREAM, hashMap);
        }
    }

    public static void trackFilmTimeFreezeRecord(boolean z) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, FilmAttr.VALUE_TIME_FREEZE_CLICK_START_RECORD);
            hashMap.put(FilmAttr.PARAM_TIME_FREEZE_BEFORE_RECORDING, z ? "on" : "off");
            MistatsWrapper.mistatEvent(FilmAttr.KEY_FILM_DREAM, hashMap);
        }
    }

    public static void trackFilmUseGuideClick() {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.MODULE_NAME, MistatsWrapper.getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode()));
            MistatsWrapper.mistatEvent(FilmAttr.PARAM_FILM_USEGUIDE_CLICK, hashMap);
        }
    }

    public static void trackFilterChanged(int i, boolean z) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.featureTriggerEvent(FilterAttr.KEY_FILTER_CHANGED, filterIdToName(i), z ? "click" : BaseEvent.SLIDE);
        }
    }

    public static void trackFlashChanged(int i, String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(FlashAttr.PARAM_FLASH_MODE, flashModeToName(str), null);
        }
    }

    public static void trackFocusPositionChanged(int i, int i2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i2 == 167 ? "M_manual_" : "M_proVideo_", Manual.FOCUS_POSITION, (Object) focusPositionToName(i));
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:78:0x016c, code lost:
        if (r1 == false) goto L_0x016f;
     */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0067  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0069  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x007d  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a3  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00a6  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b5  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ee  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013b  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0157  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0177  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0179  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x0186  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void trackGeneralInfo(Map map, boolean z, boolean z2, int i, int i2, boolean z3, int i3, BeautyValues beautyValues, MutexModeManager mutexModeManager, String str) {
        String str2;
        ComponentConfigFlash componentFlash;
        ComponentConfigHdr componentHdr;
        int i4 = i;
        boolean z4 = z3;
        String str3 = str;
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        Map hashMap = map == null ? new HashMap() : map;
        hashMap.put(BaseEvent.MODULE_NAME, DataRepository.dataItemGlobal().isIntentIDPhoto() ? ModuleName.ID_PHOTO : modeIdToName(i));
        hashMap.put("attr_trigger_mode", triggerModeToName(i2));
        String str4 = "0";
        if (!z) {
            ComponentRunningTimer componentRunningTimer = dataItemRunning.getComponentRunningTimer();
            if (componentRunningTimer != null) {
                str2 = componentRunningTimer.getComponentValue(i4);
                hashMap.put("attr_reference_line", String.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false)));
                hashMap.put("attr_timer", str2);
                String str5 = "null";
                String str6 = "close";
                String str7 = BaseEvent.VALUE_NOT_NULL;
                String str8 = !z2 ? str7 : CameraSettings.isRecordLocation() ? str5 : str6;
                String str9 = Setting.PARAM_SAVE_LOCATION;
                hashMap.put(str9, str8);
                componentFlash = dataItemConfig.getComponentFlash();
                if (componentFlash != null) {
                    String componentValue = componentFlash.getComponentValue(i4);
                    if (!z || "2".equals(componentValue)) {
                        str4 = componentValue;
                    }
                }
                String str10 = FlashAttr.PARAM_FLASH_MODE;
                if (str3 == null) {
                    str3 = flashModeToName(str4);
                }
                hashMap.put(str10, str3);
                hashMap.put("attr_filter", !z ? "none" : filterIdToName(EffectController.getInstance().getEffectForSaving(false)));
                String str11 = "on";
                String str12 = "off";
                if (beautyValues != null) {
                    String str13 = (z || !beautyValues.isFaceBeautyOn()) ? str12 : str11;
                    hashMap.put(BeautyAttr.PARAM_BEAUTY_SWITCH, str13);
                }
                hashMap.put(BaseEvent.PARAM_PICTURE_RATIO, DataRepository.dataItemConfig().getComponentConfigRatio().getComponentValue(i4));
                addUltraPixelParameter(z4, hashMap);
                hashMap.put(BaseEvent.QUALITY, CameraSettings.getEncodingQuality(z).name().toLowerCase(Locale.ENGLISH));
                if (!z4) {
                    hashMap.put(FeatureName.VALUE_GENDER_AGE, CameraSettings.showGenderAge() ? str11 : str12);
                    hashMap.put(FeatureName.VALUE_MAGIC_MIRROR, CameraSettings.isMagicMirrorOn() ? str11 : str12);
                    if (DataRepository.dataItemRunning().supportHandGesture()) {
                        hashMap.put("attr_palm_shutter", CameraSettings.isHandGestureOpen() ? str11 : str12);
                    }
                    if (DataRepository.dataItemRunning().supportUltraPixelPortrait()) {
                        hashMap.put(PortraitAttr.PARAM_ULTRAPIXEL_PORTRAIT, CameraSettings.isUltraPixelPortraitFrontOn() ? str11 : str12);
                    }
                } else {
                    hashMap.put(Zoom.PARAM_ZOOM_RATIO, getDualZoomName(i));
                    trackSatState(i3, i4, hashMap);
                }
                if (!z && mutexModeManager != null) {
                    boolean isHdr = mutexModeManager.isHdr();
                    componentHdr = dataItemConfig.getComponentHdr();
                    if (componentHdr != null) {
                        if ("auto".equals(componentHdr.getComponentValue(i4))) {
                            str11 = isHdr ? BaseEvent.AUTO_ON : BaseEvent.AUTO_OFF;
                            hashMap.put(AlgoAttr.PARAM_HDR, str11);
                            if (!z2) {
                                str5 = str7;
                            } else if (!CameraSettings.isRecordLocation()) {
                                str5 = str6;
                            }
                            hashMap.put(str9, str5);
                            if (dataItemRunning != null) {
                                hashMap.put(ProColor.CAPTURE_STATE, dataItemRunning.getComponentRunningColorEnhance().isEnabled(i4) ? ProColor.VALUE_ON : ProColor.VALUE_OFF);
                            }
                            MistatsWrapper.mistatEvent(BaseEvent.KEY_CAPTURE, hashMap);
                            return;
                        }
                    }
                }
                str11 = str12;
                hashMap.put(AlgoAttr.PARAM_HDR, str11);
                if (!z2) {
                }
                hashMap.put(str9, str5);
                if (dataItemRunning != null) {
                }
                MistatsWrapper.mistatEvent(BaseEvent.KEY_CAPTURE, hashMap);
                return;
            }
        }
        str2 = str4;
        hashMap.put("attr_reference_line", String.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false)));
        hashMap.put("attr_timer", str2);
        String str52 = "null";
        String str62 = "close";
        String str72 = BaseEvent.VALUE_NOT_NULL;
        if (!z2) {
        }
        String str92 = Setting.PARAM_SAVE_LOCATION;
        hashMap.put(str92, str8);
        componentFlash = dataItemConfig.getComponentFlash();
        if (componentFlash != null) {
        }
        String str102 = FlashAttr.PARAM_FLASH_MODE;
        if (str3 == null) {
        }
        hashMap.put(str102, str3);
        hashMap.put("attr_filter", !z ? "none" : filterIdToName(EffectController.getInstance().getEffectForSaving(false)));
        String str112 = "on";
        String str122 = "off";
        if (beautyValues != null) {
        }
        hashMap.put(BaseEvent.PARAM_PICTURE_RATIO, DataRepository.dataItemConfig().getComponentConfigRatio().getComponentValue(i4));
        addUltraPixelParameter(z4, hashMap);
        hashMap.put(BaseEvent.QUALITY, CameraSettings.getEncodingQuality(z).name().toLowerCase(Locale.ENGLISH));
        if (!z4) {
        }
        boolean isHdr2 = mutexModeManager.isHdr();
        componentHdr = dataItemConfig.getComponentHdr();
        if (componentHdr != null) {
        }
    }

    public static void trackGoogleLensOobeContinue(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(GoogleLens.PARAM_OOBE_CONTINUE_CLICK, z ? BaseEvent.ACCEPT : BaseEvent.REJECT);
        MistatsWrapper.mistatEvent(GoogleLens.KEY_GOOGLE_LENS, hashMap);
    }

    public static void trackGoogleLensPicker() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, GoogleLens.GOOGLE_LENS_PICKER);
        MistatsWrapper.mistatEvent(GoogleLens.KEY_GOOGLE_LENS, hashMap);
    }

    public static void trackGoogleLensPickerValue(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(GoogleLens.PARAM_PICK_WHICH, z ? GoogleLens.VALUE_GOOGLE_LENS : GoogleLens.VALUE_LOCK_AEAF);
        MistatsWrapper.mistatEvent(GoogleLens.KEY_GOOGLE_LENS, hashMap);
    }

    public static void trackGoogleLensTouchAndHold() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, GoogleLens.GOOGLE_LENS_TOUCH_AND_HOLD);
        MistatsWrapper.mistatEvent(GoogleLens.KEY_GOOGLE_LENS, hashMap);
    }

    public static void trackGotoGallery(int i) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_GOTO_GALLERY, null, null);
    }

    public static void trackGotoIDCard() {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_GOTO_ID_CARD, null, null);
    }

    public static void trackGotoSettings(int i) {
        MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_GOTO_SETTINGS, null, null);
    }

    public static void trackHdrChanged(int i, String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(AlgoAttr.PARAM_HDR, str, null);
        }
    }

    public static void trackIdPhoto(Map map) {
        map.put(BaseEvent.SENSOR_ID, CameraSettings.isFrontCamera() ? "front" : "back");
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        ComponentRunningTimer componentRunningTimer = dataItemRunning.getComponentRunningTimer();
        if (componentRunningTimer != null) {
            map.put("attr_timer", componentRunningTimer.getComponentValue(163));
        }
        ComponentConfigFlash componentFlash = dataItemConfig.getComponentFlash();
        map.put(FlashAttr.PARAM_FLASH_MODE, flashModeToName(componentFlash != null ? componentFlash.getComponentValue(163) : "0"));
        map.put("attr_filter", filterIdToName(EffectController.getInstance().getEffectForSaving(false)));
        map.put(Zoom.PARAM_ZOOM_RATIO, HybridZoomingSystem.toString(CameraSettings.getRetainZoom(163)));
        MistatsWrapper.moduleCaptureEvent(ModuleName.ID_PHOTO, map);
    }

    public static void trackInterruptionNetwork() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_SUBTITLE, FeatureName.VALUE_SUBTITLE_NETWORK_INTERRUPTION);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackIntoBluetoothSco() {
        HashMap hashMap = new HashMap();
        hashMap.put(VideoAttr.PARAM_BLUETOOTH_SCO, Integer.valueOf(1));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackIsoChanged(String str, int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i == 167 ? "M_manual_" : "M_proVideo_", Manual.ISO, (Object) isoToName(str));
        }
    }

    public static void trackKaleidoscopeClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent("M_miLive_", hashMap);
        }
    }

    public static void trackKaleidoscopeValue(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(MiLive.PARAM_MI_LIVE_KALEIDOSCOPE_NAME, str);
            MistatsWrapper.mistatEvent("M_miLive_", hashMap);
        }
    }

    public static void trackLensChanged(String str, int i) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent(i == 167 ? "M_manual_" : "M_proVideo_", Manual.LENS, (Object) str);
        }
    }

    public static void trackLightingChanged(int i, String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.moduleUIClickEvent("M_portrait_", PortraitAttr.PORTRAIT_LIGHTING, (Object) str);
        }
    }

    public static void trackLiveBeautyClick(@ShineType String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            String str2 = null;
            char c = 65535;
            int hashCode = str.hashCode();
            if (hashCode != 1567) {
                if (hashCode == 1568 && str.equals("11")) {
                    c = 1;
                }
            } else if (str.equals("10")) {
                c = 0;
            }
            if (c == 0) {
                str2 = "attr_filter";
            } else if (c == 1) {
                str2 = BeautyAttr.BEAUTY_TYPE_FACE;
            }
            if (!TextUtils.isEmpty(str2)) {
                HashMap hashMap = new HashMap();
                hashMap.put(Live.PARAM_LIVE_BEAUTY_TYPE, str2);
                MistatsWrapper.mistatEvent("M_liveDouyin_", hashMap);
            }
        }
    }

    public static void trackLiveBeautyCounter(String str) {
        if (str != null) {
            HashMap hashMap = new HashMap();
            String str2 = null;
            if ("key_live_shrink_face_ratio" == str) {
                str2 = Live.VALUE_LIVE_SHRINK_FACE_RATIO;
            } else if ("key_live_enlarge_eye_ratio" == str) {
                str2 = Live.VALUE_LIVE_ENLARGE_EYE_RATIO;
            } else if ("key_live_smooth_strength" == str) {
                str2 = Live.VALUE_LIVE_SMOOTH_RATIO;
            } else if (BeautyConstant.BEAUTY_RESET == str) {
                str2 = BaseEvent.RESET;
            }
            if (!TextUtils.isEmpty(str2)) {
                hashMap.put(Live.PARAM_LIVE_BEAUTY_PORT, str2);
                MistatsWrapper.mistatEvent(Live.KEY_LIVE_BEAUTY, hashMap);
            }
        }
    }

    public static void trackLiveClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent("M_liveDouyin_", hashMap);
        }
    }

    public static void trackLiveRecordingParams(boolean z, String str, boolean z2, String str2, boolean z3, String str3, String str4, boolean z4, int i, int i2, int i3, int i4, boolean z5) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            String str5 = "on";
            String str6 = "off";
            hashMap.put(Live.PARAM_LIVE_MUSIC_ON, z ? str5 : str6);
            if (z) {
                String str7 = str;
                hashMap.put(Live.PARAM_LIVE_MUSIC_NAME, str);
            }
            hashMap.put(Live.PARAM_LIVE_FILTER_SEGMENT_ON, z2 ? str5 : str6);
            if (z2) {
                String str8 = str2;
                hashMap.put(Live.PARAM_LIVE_FILTER_NAME, str2);
            }
            hashMap.put(Live.PARAM_LIVE_STICKER_SEGMENT_ON, z3 ? str5 : str6);
            if (z3) {
                String str9 = str3;
                hashMap.put(Live.PARAM_LIVE_STICKER_NAME, str3);
            }
            String str10 = str4;
            hashMap.put(Live.PARAM_LIVE_SPEED_LEVEL, str4);
            if (!z4) {
                str5 = str6;
            }
            hashMap.put(Live.PARAM_LIVE_BEAUTY_SEGMENT_ON, str5);
            hashMap.put(Live.PARAM_LIVE_SHRINK_FACE_RATIO, divideTo10Section(i));
            hashMap.put(Live.PARAM_LIVE_ENLARGE_EYE_RATIO, divideTo10Section(i2));
            hashMap.put(Live.PARAM_LIVE_SMOOTH_RATIO, divideTo10Section(i3));
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(i4);
            hashMap.put(BaseEvent.QUALITY, videoQualityToName(sb.toString()));
            MistatsWrapper.mistatEvent(Live.KEY_LIVE_VIDEO_SEGMENT, hashMap);
        }
    }

    public static void trackLiveStickerDownload(String str, boolean z) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(Live.PARAM_LIVE_STICKER_NAME, str);
            hashMap.put(Live.PARAM_LIVE_STICKER_DOWNLOAD, z ? BaseEvent.VALUE_SUCCESS : BaseEvent.VALUE_FAILED);
            MistatsWrapper.mistatEvent("M_liveDouyin_", hashMap);
        }
    }

    public static void trackLiveStickerMore(boolean z) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.TO, z ? Live.VALUE_LIVE_STICKER_MARKET : Live.VALUE_LIVE_STICKER_APP);
            MistatsWrapper.mistatEvent(Live.LIVE_STICKER_MORE, hashMap);
        }
    }

    public static void trackLiveVideoParams(int i, float f, boolean z, boolean z2, boolean z3) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            String str = "on";
            String str2 = "off";
            hashMap.put(Live.PARAM_LIVE_FILTER_ON, z ? str : str2);
            hashMap.put(Live.PARAM_LIVE_STICKER_ON, z2 ? str : str2);
            if (!z3) {
                str = str2;
            }
            hashMap.put(Live.PARAM_LIVE_BEAUTY_ON, str);
            hashMap.put(Live.PARAM_LIVE_RECORD_SEGMENTS, Integer.toString(i));
            hashMap.put(Live.PARAM_LIVE_RECORD_TIME, Integer.toString((int) f));
            MistatsWrapper.mistatEvent(Live.KEY_LIVE_VIDEO_COMPLETE, hashMap);
        }
    }

    public static void trackLongPressDialogSelect(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, str);
        MistatsWrapper.mistatEventSimple(Other.KEY_LONG_PRESS_DIALOG, hashMap);
    }

    public static void trackLongPressRecord() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.COUNT, String.valueOf(1));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_LONG_PRESS_RECORD, hashMap);
    }

    public static void trackLostCount(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(AutoZoom.PARAM_TRACKING_LOST_OBJECT, i < 10 ? String.valueOf(i) : AutoZoom.AUTOZOOM_LOST_10_MORE);
        MistatsWrapper.mistatEvent(AutoZoom.KEY_AUTO_ZOOM, hashMap);
    }

    public static void trackLyingDirectPictureTaken(Map map, int i) {
        if (i != -1) {
            int i2 = i - 1;
            map.put(BaseEvent.PARAM_LYING_DIRECT, i % 2 == 0 ? "none" : String.valueOf((360 - (i2 >= 0 ? i2 % m.cQ : (i2 % m.cQ) + m.cQ)) % m.cQ));
        }
    }

    public static void trackLyingDirectShow(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.PARAM_LYING_DIRECT, String.valueOf(i));
        MistatsWrapper.mistatEvent(FeatureName.KEY_COMMON_TIPS, hashMap);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0055, code lost:
        if (r3 != 169) goto L_0x005e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void trackMacroModeTaken(int i) {
        String str;
        if (DataRepository.dataItemRunning().supportMacroMode(CameraSettings.getBogusCameraId(), i)) {
            boolean isSwitchOn = DataRepository.dataItemRunning().getComponentRunningMacroMode().isSwitchOn(i);
            HashMap hashMap = new HashMap();
            if (isSwitchOn) {
                hashMap.put(Manual.PARAM_FOCUS_POSITION, Util.getZoomRatioText(CameraSettings.getRetainZoom(i)));
                str = "on";
            } else {
                str = "off";
            }
            hashMap.put(C0122O00000o.instance().OOoOO0o() ? SensorAttr.PARAM_STANDALONE_MACRO_MODE : SensorAttr.PARAM_MACRO_MODE, str);
            String str2 = null;
            if (i != 162) {
                if (i == 163 || i == 165) {
                    str2 = BaseEvent.PHOTO;
                    hashMap.put(BaseEvent.MODE, str2);
                    MistatsWrapper.mistatEvent(MacroAttr.FUCNAME_MACRO_MODE, hashMap);
                }
            }
            str2 = "video";
            hashMap.put(BaseEvent.MODE, str2);
            MistatsWrapper.mistatEvent(MacroAttr.FUCNAME_MACRO_MODE, hashMap);
        }
    }

    public static void trackManuallyResetClick() {
        MistatsWrapper.moduleUIClickEvent(167, Manual.RESET_PARAMS_CLICK, (Object) "none");
    }

    public static void trackManuallyResetDialogCancel() {
        MistatsWrapper.moduleUIClickEvent(167, Manual.RESET_PARAMS_CLICK, (Object) "off");
    }

    public static void trackManuallyResetDialogOk() {
        MistatsWrapper.moduleUIClickEvent(167, Manual.RESET_PARAMS_CLICK, (Object) "on");
    }

    public static void trackManuallyResetShow() {
        MistatsWrapper.moduleUIClickEvent(167, Manual.RESET_PARAMS_SHOW, (Object) "none");
    }

    public static void trackMeterClick() {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_METER_ICON_CLICK, Integer.valueOf(1), null);
        }
    }

    public static void trackMiLiveBeautyCounter(String str) {
        String str2;
        if (str != null) {
            char c = 65535;
            switch (str.hashCode()) {
                case -2110473153:
                    if (str.equals("key_live_smooth_strength")) {
                        c = 2;
                        break;
                    }
                    break;
                case 77866287:
                    if (str.equals(BeautyConstant.BEAUTY_RESET)) {
                        c = 3;
                        break;
                    }
                    break;
                case 175697132:
                    if (str.equals("key_live_shrink_face_ratio")) {
                        c = 0;
                        break;
                    }
                    break;
                case 1771202045:
                    if (str.equals("key_live_enlarge_eye_ratio")) {
                        c = 1;
                        break;
                    }
                    break;
            }
            if (c == 0) {
                str2 = MiLive.VALUE_MI_LIVE_CLICK_SHRINK_FACE;
            } else if (c == 1) {
                str2 = MiLive.VALUE_MI_LIVE_CLICK_ENLARGE_EYE;
            } else if (c != 2) {
                if (c == 3) {
                    str2 = MiLive.VALUE_MI_LIVE_CLICK_BEAUTY_RESET;
                }
            } else {
                str2 = MiLive.VALUE_MI_LIVE_CLICK_SMOOTH;
            }
            trackMiLiveClick(str2);
        }
    }

    public static void trackMiLiveClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent("M_miLive_", hashMap);
            HashMap hashMap2 = new HashMap();
            String str2 = "on";
            String str3 = "off";
            hashMap2.put(VideoAttr.PARAM_HEADSET, CameraSettings.getHeadSetState() ? str2 : str3);
            hashMap2.put(VideoAttr.PARAM_BLUETOOTH_EARPHONE_VIDEO, CameraSettings.getHeadSetState() ? str2 : str3);
            hashMap2.put(VideoAttr.PARAM_KARAOKE, CameraSettings.getKaraokeState() ? str2 : str3);
            hashMap2.put(VideoAttr.PARAM_KARAOKE_VIDEO, CameraSettings.getKaraokeState() ? str2 : str3);
            hashMap2.put(VideoAttr.PARAM_AI_NOISE_REDUCTION, CameraSettings.getAiNoiseReductionState() ? str2 : str3);
            if (!CameraSettings.getAiNoiseReductionState()) {
                str2 = str3;
            }
            hashMap2.put(VideoAttr.PARAM_AI_NOISE_REDUCTION_VIDEO, str2);
            MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap2);
        }
    }

    public static void trackMiLiveRecordingParams(int i, String str, int i2, int i3, boolean z, BeautyValues beautyValues, int i4, String str2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(MiLive.PARAM_MI_LIVE_CAMERA_QUALITY, videoQualityToName(String.valueOf(i4)));
            hashMap.put(MiLive.PARAM_MI_LIVE_CAMERA_FACING, z ? "front" : "back");
            hashMap.put(MiLive.PARAM_MI_LIVE_SEGMENT_COUNT, String.valueOf(i));
            String str3 = "none";
            if (TextUtils.isEmpty(str)) {
                str = str3;
            }
            hashMap.put(MiLive.PARAM_MI_LIVE_MUSIC_NAME, str);
            if (i2 != FilterInfo.FILTER_ID_NONE) {
                str3 = filterIdToName(i2);
            }
            hashMap.put(MiLive.PARAM_MI_LIVE_FILTER_NAME, str3);
            hashMap.put(MiLive.PARAM_MI_LIVE_SPEED, speedIdToName(i3));
            hashMap.put(MiLive.PARAM_MI_LIVE_BEAUTY_ON, beautyValues.isFaceBeautyOn() ? "on" : "off");
            String str4 = "pref_beautify_skin_smooth_ratio_key";
            String str5 = (String) sMiLiveBeautyTypeToName.get(str4);
            if (str5 != null) {
                hashMap.put(str5, faceBeautyRatioToName(beautyValues.getValueByType(str4)));
            }
            int i5 = 0;
            if (z) {
                String[] strArr = BeautyConstant.BEAUTY_CATEGORY_MI_LIVE;
                int length = strArr.length;
                while (i5 < length) {
                    String str6 = strArr[i5];
                    String str7 = (String) sMiLiveBeautyTypeToName.get(str6);
                    if (str7 != null) {
                        hashMap.put(str7, faceBeautyRatioToName(beautyValues.getValueByType(str6)));
                    }
                    i5++;
                }
            } else {
                String[] strArr2 = BeautyConstant.BEAUTY_CATEGORY_BACK_FIGURE;
                int length2 = strArr2.length;
                while (i5 < length2) {
                    String str8 = strArr2[i5];
                    String str9 = (String) sMiLiveBeautyTypeToName.get(str8);
                    if (str9 != null) {
                        hashMap.put(str9, faceBeautyRatioToName(beautyValues.getValueByType(str8)));
                    }
                    i5++;
                }
            }
            hashMap.put(MiLive.PARAM_MI_LIVE_KALEIDOSCOPE_NAME, str2);
            MistatsWrapper.mistatEvent(MiLive.KEY_MI_LIVE_VIDEO_SEGMENT, hashMap);
        }
    }

    public static void trackMimoji2CaptureOrRecord(Map map, String str, boolean z, boolean z2, boolean z3) {
        String str2;
        if (!MistatsWrapper.isCounterEventDisabled()) {
            map.put(FlashAttr.PARAM_FLASH_MODE, flashModeToName(str));
            String str3 = BaseEvent.MODE;
            if (z) {
                str2 = BaseEvent.PHOTO;
            } else {
                HashMap hashMap = new HashMap();
                String str4 = "off";
                String str5 = "on";
                hashMap.put(VideoAttr.PARAM_HEADSET, CameraSettings.getHeadSetState() ? str5 : str4);
                hashMap.put(VideoAttr.PARAM_BLUETOOTH_EARPHONE_VIDEO, CameraSettings.getHeadSetState() ? str5 : str4);
                hashMap.put(VideoAttr.PARAM_KARAOKE, CameraSettings.getKaraokeState() ? str5 : str4);
                hashMap.put(VideoAttr.PARAM_KARAOKE_VIDEO, CameraSettings.getKaraokeState() ? str5 : str4);
                hashMap.put(VideoAttr.PARAM_AI_NOISE_REDUCTION, CameraSettings.getAiNoiseReductionState() ? str5 : str4);
                if (CameraSettings.getAiNoiseReductionState()) {
                    str4 = str5;
                }
                hashMap.put(VideoAttr.PARAM_AI_NOISE_REDUCTION_VIDEO, str4);
                MistatsWrapper.moduleMistatsEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
                if (z3) {
                    map.put(VideoAttr.PARAM_BLUETOOTH_SCO, str5);
                }
                str2 = "video";
            }
            map.put(str3, str2);
            MistatsWrapper.moduleMistatsEvent("M_funArMimoji2_", map);
        }
    }

    public static void trackMimoji2Click(String str, String str2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            if (!TextUtils.isEmpty(str)) {
                hashMap.put(Mimoji.MIMOJI_TYPE, str);
            }
            if (!TextUtils.isEmpty(str2)) {
                hashMap.put(BaseEvent.FEATURE_NAME, str2);
            }
            MistatsWrapper.mistatEvent(Mimoji.KEY_MIMOJI_CLICK, hashMap);
        }
    }

    public static void trackMimojiCaptureOrRecord(Map map, String str, boolean z, boolean z2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            map.put(FlashAttr.PARAM_FLASH_MODE, flashModeToName(str));
            map.put(BaseEvent.MODE, z ? BaseEvent.PHOTO : "video");
            MistatsWrapper.moduleMistatsEvent("M_funArMimoji_", map);
        }
    }

    public static void trackMimojiClick(String str, String str2) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.FEATURE_NAME, str);
            hashMap.put(BaseEvent.OPERATE_STATE, str2);
            MistatsWrapper.mistatEvent(Mimoji.KEY_MIMOJI_CLICK, hashMap);
        }
    }

    public static void trackMimojiCount(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(Mimoji.MIMOJI_HISTORY_EMOJI_COUNT, Long.valueOf(str));
            MistatsWrapper.mistatEventSimple("M_funArMimoji2_", hashMap);
        }
    }

    public static void trackMimojiSaveGif(String str, String str2, String str3) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji_gif_text:");
            sb.append(str);
            sb.append(" ; mimoji_gif_type:");
            sb.append(str2);
            sb.append(" ; mimoji_gif_duration:");
            sb.append(str3);
            hashMap.put(Mimoji.MIMOJI_SAVE_GIF, sb.toString());
            MistatsWrapper.mistatEvent(Mimoji.MIMOJI_CLICK_EDIT_SAVE, hashMap);
        }
    }

    public static void trackMimojiSavePara(String str, Map map) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.mistatEvent(str, map);
        }
    }

    public static void trackMimojiTrigger(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(Mimoji.MIMOJI_TRIGGER, str);
            MistatsWrapper.mistatEvent(Mimoji.KEY_MIMOJI_CLICK, hashMap);
        }
    }

    public static void trackModeEditClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEventSimple(FeatureName.VALUE_EDIT_MODE, hashMap);
        }
    }

    public static void trackModeEditInfo() {
        String str;
        int favoriteModeCount = DataRepository.dataItemGlobal().getFavoriteModeCount();
        if (favoriteModeCount > 0) {
            int[] sortModes = DataRepository.dataItemGlobal().getSortModes();
            int[] copyOfRange = Arrays.copyOfRange(sortModes, 0, favoriteModeCount);
            int[] copyOfRange2 = Arrays.copyOfRange(sortModes, favoriteModeCount + 1, sortModes.length);
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("trackModeEdit commonModesCount = ");
            sb.append(favoriteModeCount);
            Log.d(str2, sb.toString());
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("trackModeEdit commonModes = ");
            sb2.append(Arrays.toString(copyOfRange));
            Log.d(str3, sb2.toString());
            String str4 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("trackModeEdit moreModes = ");
            sb3.append(Arrays.toString(copyOfRange2));
            Log.d(str4, sb3.toString());
            HashMap hashMap = new HashMap();
            hashMap.put(EditMode.PARAM_COMMON_MODE_COUNT_AFTER_EDIT, String.valueOf(copyOfRange.length));
            hashMap.put(EditMode.PARAM_MORE_MODE_COUNT_AFTER_EDIT, String.valueOf(copyOfRange2.length));
            MistatsWrapper.mistatEventSimple(EditMode.KEY_CAMERA_MODE_EDIT, hashMap);
            int length = copyOfRange.length;
            int i = 0;
            while (true) {
                str = BaseEvent.MODULE_NAME;
                if (i >= length) {
                    break;
                }
                int i2 = copyOfRange[i];
                HashMap hashMap2 = new HashMap();
                hashMap2.put(str, DataRepository.dataItemGlobal().isIntentIDPhoto() ? ModuleName.ID_PHOTO : modeIdToName(i2));
                MistatsWrapper.mistatEventSimple(EditMode.KEY_COMMON_MODES_AFTER_EDIT, hashMap2);
                i++;
            }
            for (int i3 : copyOfRange2) {
                HashMap hashMap3 = new HashMap();
                hashMap3.put(str, modeIdToName(i3));
                MistatsWrapper.mistatEventSimple(EditMode.KEY_MORE_MODES_AFTER_EDIT, hashMap3);
            }
        }
    }

    public static void trackModeSwitch() {
        MistatsWrapper.commonKeyTriggerEvent("target_mode", modeIdToName(ModuleManager.getActiveModuleIndex()), null);
    }

    public static void trackMoonMode(Map map, boolean z, boolean z2) {
        if (z) {
            if (map == null) {
                map = new HashMap();
            }
            map.put(MoonAndNightAttr.PARAM_NIGHT_MOON_MODE, z2 ? MoonAndNightAttr.VAULE_MOON_MODE : MoonAndNightAttr.VAULE_NIGHT_MODE);
            map.put(Zoom.PARAM_ZOOM_RATIO, String.valueOf(CameraSettings.getRetainZoom(188)));
        }
    }

    public static void trackMultiCameraDualVideo(String str, String str2, int i, int i2, int i3) {
        if (C0122O00000o.instance().OOO000o()) {
            HashMap hashMap = new HashMap();
            if (str != null) {
                hashMap.put("attr_video_duration", str);
            }
            if (str2 != null) {
                hashMap.put(MultiCameraAttr.ATTR_RECORD_TYPE, str2);
            }
            if (i >= 0) {
                hashMap.put(MultiCameraAttr.ATTR_RECORD_PAUSED, String.valueOf(i));
            }
            if (i2 >= 0) {
                hashMap.put(MultiCameraAttr.ATTR_RECORD_RESUME, String.valueOf(i2));
            }
            if (i3 >= 0) {
                hashMap.put(MultiCameraAttr.ATTR_RECORD_CAPTURE, String.valueOf(i3));
            }
            if (!hashMap.isEmpty()) {
                MistatsWrapper.mistatEvent(MultiCameraAttr.KEY_MULTI_CAMERA_DUAL_VIDEO, hashMap);
            }
        }
    }

    public static void trackNewSlowMotionVideoRecorded(String str, int i, int i2, int i3, long j) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.SENSOR_ID, "back");
        hashMap.put(VideoAttr.PARAM_VIDEO_MODE, str);
        hashMap.put(BaseEvent.QUALITY, slowMotionQualityIdToName(String.valueOf(i)));
        hashMap.put(FlashAttr.PARAM_FLASH_MODE, i2 == 2 ? "torch" : "off");
        hashMap.put(VideoAttr.PARAM_VIDEO_FPS, String.valueOf(i3));
        hashMap.put(VideoAttr.PARAM_VIDEO_TIME, String.valueOf(j));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_NEW_SLOW_MOTION, hashMap);
    }

    public static void trackPauseOrResumeVideoRecording(boolean z, boolean z2) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.SENSOR_ID, z ? "front" : "back");
        hashMap.put(BaseEvent.LIFE_STATE, z2 ? VideoAttr.VALUE_VIDEO_RESUME_RECORDING : VideoAttr.VALUE_VIDEO_PAUSE_RECORDING);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackPictureSize(int i, String str) {
        MistatsWrapper.commonKeyTriggerEvent(BaseEvent.PARAM_PICTURE_RATIO, str, null);
    }

    public static void trackPictureTakenInManual(int i, String str, String str2, String str3, String str4, int i2, int i3) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.MODE, BaseEvent.PHOTO);
        hashMap.put(Manual.PARAM_EV, str4);
        hashMap.put("attr_awb", autoWhiteBalanceToName(str));
        hashMap.put(Manual.PARAM_FOCUS_POSITION, focusPositionToName(CameraSettings.getFocusPosition()));
        hashMap.put(Manual.PARAM_ET, exposureTimeToName(str2));
        hashMap.put("attr_iso", isoToName(str3));
        hashMap.put(Manual.PARAM_LENS, CameraSettings.getCameraLensType(i2));
        String str5 = "on";
        String str6 = "off";
        hashMap.put(Manual.PARAM_FOCUS_PEAK, EffectController.getInstance().isNeedDrawPeaking() ? str5 : str6);
        hashMap.put(Manual.PARAM_EXPOSURE_FEEDBACk, EffectController.getInstance().isNeedDrawExposure() ? str5 : str6);
        hashMap.put("attr_reference_line", String.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false)));
        if (!CameraSettings.isGradienterOn()) {
            str5 = str6;
        }
        hashMap.put(Setting.PARAM_GRADIENTER, str5);
        hashMap.put(Zoom.PARAM_ZOOM_RATIO, HybridZoomingSystem.toString(CameraSettings.getRetainZoom(i2)));
        hashMap.put(Manual.PARAM_RAW, String.valueOf(DataRepository.dataItemConfig().getComponentConfigRaw().isSwitchOn(167)));
        hashMap.put(Manual.PARAM_AUTOEXPOSURE, DataRepository.dataItemConfig().getComponentConfigMeter().getTrackValue(167));
        hashMap.put(BaseEvent.COUNT, String.valueOf(i));
        trackSatState(i3, i2, hashMap);
        addUltraPixelParameter(hashMap);
        hashMap.put("attr_filter", filterIdToName(EffectController.getInstance().getEffectForSaving(false)));
        hashMap.put(Manual.PARAM_GRADIENT, String.valueOf(CameraSettings.isGradienterOn()));
        addUltraPixelParameter(false, hashMap);
        MistatsWrapper.moduleCaptureEvent("M_manual_", hashMap);
    }

    public static void trackPictureTakenInPanorama(Map map, Context context, BeautyValues beautyValues, int i) {
        if (map == null) {
            map = new HashMap();
        }
        if (beautyValues != null) {
            map.put(BeautyAttr.PARAM_BEAUTY_LEVEL, Integer.valueOf(beautyValues.mBeautySkinSmooth));
        }
        int panoramaMoveDirection = CameraSettings.getPanoramaMoveDirection(context);
        String str = panoramaMoveDirection != 3 ? panoramaMoveDirection != 4 ? panoramaMoveDirection != 5 ? panoramaMoveDirection != 6 ? "unknown" : Panorama.PANORAMA_DIRECTION_T2B : Panorama.PANORAMA_DIRECTION_B2T : Panorama.PANORAMA_DIRECTION_R2L : Panorama.PANORAMA_DIRECTION_L2R;
        map.put(Panorama.PARAM_PANORAMA_DIRECTION, str);
        map.put(BaseEvent.COUNT, String.valueOf(i));
        map.put(BaseEvent.MODE, BaseEvent.PHOTO);
        MistatsWrapper.mistatEvent("M_panorama_", map);
    }

    public static void trackPictureTakenInWideSelfie(String str, BeautyValues beautyValues) {
        HashMap hashMap = new HashMap();
        hashMap.put(Panorama.PARAM_STOP_CAPTURE_MODE, str);
        if (beautyValues != null) {
            hashMap.put(BeautyAttr.PARAM_BEAUTY_LEVEL, String.valueOf(beautyValues.mBeautySkinSmooth));
        }
        hashMap.put(BaseEvent.MODE, BaseEvent.PHOTO);
        MistatsWrapper.mistatEvent("M_panorama_", hashMap);
    }

    public static void trackPocketModeEnter(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, NonUI.VALUE_POCKET_MODE_ENTER);
        MistatsWrapper.mistatEvent(NonUI.KEY_ENTER_FAULT, hashMap);
    }

    public static void trackPocketModeExit(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, str);
        MistatsWrapper.mistatEvent(NonUI.KEY_POCKET_MODE_KEYGUARD_EXIT, hashMap);
    }

    public static void trackPocketModeSensorDelay() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.COUNT, "1");
        MistatsWrapper.mistatEvent(NonUI.KEY_POCKET_MODE_SENSOR_DELAY, hashMap);
    }

    public static void trackProColorClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(ProColor.KEY_PRO_COLOR, hashMap);
        }
    }

    public static void trackRecordVideoInProMode(String str, String str2, String str3, String str4, int i, int i2, boolean z, boolean z2, int i3) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.MODE, "video");
        hashMap.put(Manual.PARAM_EV, str4);
        hashMap.put("attr_awb", autoWhiteBalanceToName(str));
        hashMap.put(Manual.PARAM_FOCUS_POSITION, focusPositionToName(CameraSettings.getFocusPosition()));
        hashMap.put(Manual.PARAM_ET, exposureTimeToName(str2));
        hashMap.put("attr_iso", isoToName(str3));
        hashMap.put(Manual.PARAM_LENS, CameraSettings.getCameraLensType(i));
        String str5 = "off";
        String str6 = "on";
        hashMap.put(Manual.PARAM_FOCUS_PEAK, EffectController.getInstance().isNeedDrawPeaking() ? str6 : str5);
        hashMap.put(Manual.PARAM_EXPOSURE_FEEDBACk, EffectController.getInstance().isNeedDrawExposure() ? str6 : str5);
        hashMap.put("attr_reference_line", String.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false)));
        hashMap.put(Setting.PARAM_GRADIENTER, CameraSettings.isGradienterOn() ? str6 : str5);
        hashMap.put(Zoom.PARAM_ZOOM_RATIO, HybridZoomingSystem.toString(CameraSettings.getRetainZoom(i)));
        hashMap.put(Manual.PARAM_AUTOEXPOSURE, DataRepository.dataItemConfig().getComponentConfigMeter().getTrackValue(180));
        trackSatState(i2, i, hashMap);
        addUltraPixelParameter(hashMap);
        hashMap.put("attr_filter", filterIdToName(EffectController.getInstance().getEffectForSaving(false)));
        hashMap.put(Manual.PARAM_GRADIENT, String.valueOf(CameraSettings.isGradienterOn()));
        addUltraPixelParameter(false, hashMap);
        hashMap.put(Manual.PARAM_LOG, CameraSettings.isProVideoLogOpen(i) ? str6 : str5);
        hashMap.put(Manual.PARAM_HISTOGRAM, CameraSettings.isProVideoHistogramOpen(i) ? str6 : str5);
        if (z) {
            hashMap.put(VideoAttr.PARAM_BLUETOOTH_SCO, str6);
        }
        hashMap.put("attr_auto_hibernation", z2 ? str6 : str5);
        hashMap.put(AutoHibernation.PARAM_AUTO_HIBERNATION_COUNT, String.valueOf(i3));
        hashMap.put(Manual.PARAM_AUDIO_MAP, CameraSettings.isProVideoAudioMapOpen(i) ? str6 : str5);
        hashMap.put(Manual.VALUE_AUDIO_MAP_VIDEO, CameraSettings.isProVideoAudioMapOpen(i) ? str6 : str5);
        hashMap.put(Manual.VALUE_HISTOGRAM_VIDEO, CameraSettings.isProVideoHistogramOpen(i) ? str6 : str5);
        hashMap.put(VideoAttr.PARAM_HEADSET, CameraSettings.getHeadSetState() ? str6 : str5);
        hashMap.put(VideoAttr.PARAM_BLUETOOTH_EARPHONE_VIDEO, CameraSettings.getHeadSetState() ? str6 : str5);
        hashMap.put(VideoAttr.PARAM_KARAOKE, CameraSettings.getKaraokeState() ? str6 : str5);
        hashMap.put(VideoAttr.PARAM_KARAOKE_VIDEO, CameraSettings.getKaraokeState() ? str6 : str5);
        hashMap.put(VideoAttr.PARAM_AI_NOISE_REDUCTION, CameraSettings.getAiNoiseReductionState() ? str6 : str5);
        if (CameraSettings.getAiNoiseReductionState()) {
            str5 = str6;
        }
        hashMap.put(VideoAttr.PARAM_AI_NOISE_REDUCTION_VIDEO, str5);
        MistatsWrapper.moduleCaptureEvent("M_proVideo_", hashMap);
    }

    public static void trackResourceDownloadResult(String str, int i) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("_");
        sb.append(i);
        hashMap.put(ResourceAttr.KEY_RESOURCE_DOWNLOAD_RESULT, sb.toString());
        MistatsWrapper.mistatEvent(ResourceAttr.KEY_RESOURCE, hashMap);
    }

    public static void trackResourceDownloadStart(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(ResourceAttr.KEY_RESOURCE_ID, String.valueOf(str));
        MistatsWrapper.mistatEvent(ResourceAttr.KEY_RESOURCE, hashMap);
    }

    private static void trackSatState(int i, int i2, Map map) {
        StringBuilder sb;
        String str;
        String sb2;
        boolean z = HybridZoomingSystem.IS_3_OR_MORE_SAT;
        String str2 = SensorAttr.PARAM_SAT_ZOOM;
        if (z) {
            sb2 = getDualZoomName(i2);
        } else {
            if (i == Camera2DataContainer.getInstance().getUltraWideCameraId()) {
                sb = new StringBuilder();
                sb.append(i);
                str = "_RearUltra";
            } else if (i == Camera2DataContainer.getInstance().getStandaloneMacroCameraId()) {
                sb = new StringBuilder();
                sb.append(i);
                str = "_RearMacro";
            } else if (i == Camera2DataContainer.getInstance().getAuxCameraId()) {
                sb = new StringBuilder();
                sb.append(i);
                str = SensorAttr.VALUE_SENSOR_TYPE_REAR_TELE;
            } else if (i == Camera2DataContainer.getInstance().getUltraTeleCameraId()) {
                sb = new StringBuilder();
                sb.append(i);
                str = "_RearTele4x";
            } else if (i == Camera2DataContainer.getInstance().getMainBackCameraId()) {
                sb = new StringBuilder();
                sb.append(i);
                str = "_RearWide";
            } else {
                return;
            }
            sb.append(str);
            sb2 = sb.toString();
        }
        map.put(str2, sb2);
    }

    public static void trackSelectObject(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(AutoZoom.PARAM_SELECT_OBJECT_STATE, z ? AutoZoom.VALUE_IN_RECORDING : AutoZoom.VALUE_BEFORE_RECORDING);
        MistatsWrapper.mistatEvent(AutoZoom.KEY_AUTO_ZOOM, hashMap);
    }

    public static void trackShortcutClick(Intent intent, int i) {
        String str;
        Integer num;
        HashMap hashMap = new HashMap();
        String str2 = VideoAttr.KEY_VIDEO_COMMON_CLICK;
        if (i == 162) {
            num = Integer.valueOf(1);
            str = Setting.SHORTCUT_VIDEO_MODE;
        } else if (i == 163) {
            boolean z = false;
            try {
                z = CameraIntentManager.getInstance(intent).isUseFrontCamera();
            } catch (Exception unused) {
            }
            if (z) {
                num = Integer.valueOf(1);
                str = Setting.SHORTCUT_SELFIE_MODE;
            } else {
                return;
            }
        } else if (i == 167) {
            num = Integer.valueOf(1);
            str = Setting.SHORTCUT_PRO_MODE;
        } else if (i == 186) {
            num = Integer.valueOf(1);
            str = Setting.SHORTCUT_DOCS_MODE;
        } else {
            return;
        }
        hashMap.put(str, num);
        MistatsWrapper.mistatEvent(str2, hashMap);
    }

    public static void trackShowZoomBarByScroll(boolean z) {
        if (z) {
            HashMap hashMap = new HashMap();
            hashMap.put(Zoom.PARAM_ZOOM_ADJUSTED_MODE, Zoom.VALUE_SHOW_ZOOM_BAR_BY_SCROLL);
            MistatsWrapper.mistatEvent(Zoom.KEY_ZOOM, hashMap);
        }
    }

    public static void trackSlowMotionQuality(String str, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put(VideoAttr.PARAM_VIDEO_FPS, str);
        hashMap.put(VideoAttr.PARAM_VIDEO_QUALITY, slowMotionQualityIdToName(str2));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_NEW_SLOW_MOTION, hashMap);
    }

    public static void trackSnapInfo(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.MODULE_NAME, MistatsWrapper.getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode()));
        MistatsWrapper.mistatEventSimple(CaptureSence.KEY_SNAP_CAMERA, hashMap);
    }

    public static void trackSpeechShutterStatus(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(SpeechShutterAttr.PARAM_SPEECH_SHUTTER_STATUS, z ? "on" : "off");
        MistatsWrapper.mistatEvent(SpeechShutterAttr.KEY_SPEECH_SHUTTER, hashMap);
    }

    public static void trackStartAppCost(long j) {
        if (j < 0 || j > FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME) {
            StringBuilder sb = new StringBuilder();
            sb.append("The time cost when start app is illegal: ");
            sb.append(j);
            throw new IllegalArgumentException(sb.toString());
        }
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, CostTime.START_APP_COST);
        hashMap.put(BaseEvent.COST_TIME, String.valueOf(round(j, 50)));
        MistatsWrapper.mistatEvent(CostTime.KEY_CAMERA_PERPORMANCE, hashMap);
    }

    public static void trackSubtitle(boolean z) {
        String str = z ? "on" : "off";
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_SUBTITLE, str);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackSubtitleRecordingStart() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_SUBTITLE, FeatureName.VALUE_SUBTITLE_START_RECIRDING);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackSuperEisPro(int i, String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, "super_eis_pro");
        hashMap.put(BaseEvent.VALUE, str);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackSuperMoonCapture(String str, String str2, String str3) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.FEATURE_NAME, FeatureName.VALUE_SUPER_MOON_CAPTURE);
            hashMap.put(SuperMoon.PARAM_SUPER_MOON_SILHOUETTE_KEY, str);
            hashMap.put(SuperMoon.PARAM_SUPER_MOON_TEXT_KEY, str2);
            hashMap.put(SuperMoon.PARAM_SUPER_MOON_HAS_EFFECT, str3);
            MistatsWrapper.mistatEvent("ai_watermark", hashMap);
        }
    }

    public static void trackSuperMoonClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(SuperMoon.KEY_SUPER_MOON, hashMap);
        }
    }

    public static void trackSuperMoonEffectKey(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            StringBuilder sb = new StringBuilder();
            sb.append("value_");
            sb.append(str);
            hashMap.put(SuperMoon.PARAM_SUPER_MOON_EFFECT_SELECT, sb.toString());
            MistatsWrapper.mistatEvent(SuperMoon.KEY_SUPER_MOON, hashMap);
        }
    }

    public static void trackSuperNightInCaptureMode(Map map, boolean z) {
        if (C0122O00000o.instance().OOOOo()) {
            if (map == null) {
                map = new HashMap();
            }
            map.put(MoonAndNightAttr.PARAM_SUPER_NIGHT, z ? "on" : "off");
        }
    }

    public static void trackSwitchTabStyle(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(MoreMode.PARAM_SWITCH_TAB_STYLE, str);
            MistatsWrapper.mistatEventSimple(MoreMode.KEY_CAMERA_MORE_MODE, hashMap);
        }
    }

    public static void trackTakePictureCost(long j, boolean z, int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.FEATURE_NAME, CostTime.TAKE_PICTURE_COST);
        hashMap.put(BaseEvent.COST_TIME, String.valueOf(round(j, 50)));
        MistatsWrapper.mistatEvent(CostTime.KEY_CAMERA_PERPORMANCE, hashMap);
    }

    public static void trackTiltShiftChanged(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(FeatureName.VALUE_TILTSHIFT, str, null);
        }
    }

    public static void trackTimerBurst(int i, float f, int i2, boolean z, int i3) {
        HashMap hashMap = new HashMap();
        hashMap.put(TimerBurst.PARAM_TOTAL_COUNT, String.valueOf(i));
        hashMap.put(TimerBurst.PARAM_INTERVAL_TIMER, String.valueOf(f));
        hashMap.put(TimerBurst.PARAM_TAKEN_COUNT, String.valueOf(i2));
        hashMap.put("attr_auto_hibernation", z ? "on" : "off");
        hashMap.put(AutoHibernation.PARAM_AUTO_HIBERNATION_COUNT, String.valueOf(i3));
        MistatsWrapper.mistatEvent(TimerBurst.KEY_TIMER_BURST_TAKEN, hashMap);
    }

    public static void trackTimerChanged(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            MistatsWrapper.commonKeyTriggerEvent(CaptureAttr.PARAM_TIMER_CHANGED, str, null);
        }
    }

    public static void trackTriggerSubtitle() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_SUBTITLE, FeatureName.VALUE_TRIGGER_SUBTITLE);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackUserDefineWatermark() {
        MistatsWrapper.settingClickEvent(Setting.PARAM_USERDEFINE_WATERMARK, Integer.valueOf(1));
    }

    public static void trackVV2Exit(boolean z, boolean z2, boolean z3) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        String str = "_";
        sb.append(str);
        sb.append(z2);
        sb.append(str);
        sb.append(z3);
        hashMap.put(VLogAttr.KEY_VV_CLICK_EXIT_2, sb.toString());
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVV2ExitConfirm(boolean z, boolean z2, boolean z3, boolean z4) {
        HashMap hashMap = new HashMap();
        StringBuilder sb = new StringBuilder();
        sb.append(z);
        String str = "_";
        sb.append(str);
        sb.append(z2);
        sb.append(str);
        sb.append(z3);
        sb.append(str);
        sb.append(z4);
        hashMap.put(VLogAttr.KEY_VV_CLICK_EXIT_CONFIRM_2, sb.toString());
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVVClick(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(BaseEvent.OPERATE_STATE, str);
            MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
        }
    }

    public static void trackVVRecordingParams(String str, boolean z) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(VLogAttr.PARAM_VV_TEMPLATE_NAME_FINISH, str);
            MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
        }
    }

    public static void trackVVSave(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(VLogAttr.PARAM_VV_TEMPLATE_NAME_SAVE, str);
            MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
        }
    }

    public static void trackVVStartClick(String str, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(VLogAttr.PARAM_VV_TEMPLATE_NAME_START, str);
        hashMap.put(VLogAttr.PARAM_VV_CLICK_TEMPLATE_PREVIEW, z ? BaseEvent.VALUE_TRUE : BaseEvent.VALUE_FALSE);
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVVTemplateThumbnailClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(VLogAttr.PARAM_VV_TEMPLATE_NAME_CLICK, str);
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVVWorkspaceClick(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, str);
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVVWorkspaceDeleteConfirm(int i) {
        HashMap hashMap = new HashMap();
        hashMap.put(VLogAttr.VALUE_VV_CLICK_WORKSPACE_DELETE_CONFIRM, String.valueOf(i));
        MistatsWrapper.mistatEvent(VLogAttr.KEY_VLOG, hashMap);
    }

    public static void trackVideoCommonClickB(String str, boolean z) {
        String str2 = z ? "on" : "off";
        HashMap hashMap = new HashMap();
        hashMap.put(str, str2);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackVideoModeChanged(String str) {
        if (!MistatsWrapper.isCounterEventDisabled()) {
            HashMap hashMap = new HashMap();
            hashMap.put(VideoAttr.PARAM_VIDEO_MODE, str);
            MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
        }
    }

    public static void trackVideoQuality(String str, boolean z, String str2) {
        HashMap hashMap = new HashMap();
        hashMap.put(VideoAttr.PARAM_VIDEO_QUALITY, videoQualityToName(str2));
        MistatsWrapper.mistatEvent(BaseEvent.KEY_VIDEO, hashMap);
    }

    public static void trackVideoRecorded(boolean z, int i, int i2, boolean z2, boolean z3, boolean z4, String str, int i3, int i4, int i5, int i6, BeautyValues beautyValues, long j, boolean z5, String[] strArr, boolean z6, boolean z7, int i7, boolean z8) {
        int i8 = i2;
        String str2 = str;
        BeautyValues beautyValues2 = beautyValues;
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.SENSOR_ID, z ? "front" : "back");
        hashMap.put(VideoAttr.PARAM_VIDEO_MODE, str2);
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(i3);
        hashMap.put(BaseEvent.QUALITY, videoQualityToName(sb.toString()));
        String str3 = "off";
        hashMap.put(FlashAttr.PARAM_FLASH_MODE, i4 == 2 ? "torch" : str3);
        int i9 = i;
        trackSatState(i, i2, hashMap);
        hashMap.put(VideoAttr.PARAM_VIDEO_FPS, String.valueOf(i5));
        String str4 = "on";
        if (i8 == 162 && !z) {
            String str5 = AutoZoom.AUTO_ZOOM_STATE;
            if (z2) {
                hashMap.put(str5, z4 ? AutoZoom.VALUE_AUTOZOOM_ULTRA : AutoZoom.VALUE_AUTOZOOM_NOT_ULTRA);
            } else {
                hashMap.put(str5, str3);
            }
            hashMap.put(VideoAttr.PARAM_SUPER_EIS, z3 ? str4 : str3);
        }
        if (beautyValues2 != null) {
            hashMap.put(BeautyAttr.PARAM_BEAUTY_LEVEL, String.valueOf(beautyValues2.mBeautySkinSmooth));
        }
        hashMap.put(VideoAttr.PARAM_VIDEO_TIME, String.valueOf(j));
        String str6 = FeatureName.VALUE_SUBTITLE_RECORDING;
        if (z5) {
            hashMap.put(str6, str4);
        } else {
            hashMap.put(str6, str3);
        }
        if (strArr != null) {
            hashMap.put(FeatureName.VALUE_AI_AUDIO, strArr[0]);
            hashMap.put(FeatureName.VALUE_AI_AUDIO_ZOOM, strArr[1]);
        }
        String str7 = "attr_filter";
        String str8 = VideoAttr.PARAM_CINEMATIC_RATIO;
        if (i8 == 180 || i8 == 162) {
            hashMap.put(str8, CameraSettings.isCinematicAspectRatioEnabled(i2) ? str4 : str3);
            hashMap.put(VideoAttr.PARAM_BEAUTY, String.valueOf(CameraSettings.getFaceBeautyRatio("pref_beautify_skin_smooth_ratio_key")));
            hashMap.put(str7, String.valueOf(CameraSettings.isSupportMasterFilter() ? CameraSettings.getVideoMasterFilter() : CameraSettings.getShaderEffect()));
            hashMap.put(VideoAttr.PARAM_BOKEH, String.valueOf(CameraSettings.getVideoBokehRatio()));
            hashMap.put(VideoAttr.PARAM_BOKEH_MODE, String.valueOf(CameraSettings.getVideoBokehColorRetentionMode()));
        }
        if (z6) {
            hashMap.put(VideoAttr.PARAM_BLUETOOTH_SCO, str4);
        }
        hashMap.put("attr_auto_hibernation", z7 ? str4 : str3);
        hashMap.put(AutoHibernation.PARAM_AUTO_HIBERNATION_COUNT, String.valueOf(i7));
        if (i8 == 162) {
            if (CameraSettings.isAiEnhancedVideoEnabled(i2)) {
                hashMap.put(VideoAttr.PARAM_AI_ENHANCED_VIDEO, str4);
            }
            if (z8) {
                hashMap.put(VideoAttr.PARAM_VIDEO_HDR, str4);
            }
        }
        MistatsWrapper.mistatEvent(BaseEvent.KEY_VIDEO, hashMap);
        if (i6 > 0 && CameraSettings.VIDEO_SPEED_FAST.equals(str2)) {
            HashMap hashMap2 = new HashMap();
            hashMap2.put(VideoAttr.PARAM_VIDEO_TIME_LAPSE_INTERVAL, timeLapseIntervalToName(i6));
            if (C0122O00000o.instance().OOO00O0() || C0122O00000o.instance().OOO00Oo()) {
                hashMap2.put(VideoAttr.PARAM_VIDEO_TIME_LAPSE_DURATION, DataRepository.dataItemRunning().getString(CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_DURATION, DataRepository.dataItemRunning().getComponentRunningFastMotionDuration().getDefaultValue(160)));
                hashMap2.put(Zoom.PARAM_SAT_ZOOM_RATIO, String.valueOf(HybridZoomingSystem.toDecimal(CameraSettings.getRetainZoom(i2))));
                hashMap2.put(str7, String.valueOf(CameraSettings.getShaderEffect()));
                hashMap2.put(str8, CameraSettings.isCinematicAspectRatioEnabled(i2) ? str4 : str3);
            }
            MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_QUICK, hashMap2);
        }
        if (i8 == 162) {
            HashMap hashMap3 = new HashMap();
            hashMap3.put(VideoAttr.PARAM_HEADSET, CameraSettings.getHeadSetState() ? str4 : str3);
            hashMap3.put(VideoAttr.PARAM_BLUETOOTH_EARPHONE_VIDEO, CameraSettings.getHeadSetState() ? str4 : str3);
            hashMap3.put(VideoAttr.PARAM_KARAOKE, CameraSettings.getKaraokeState() ? str4 : str3);
            hashMap3.put(VideoAttr.PARAM_KARAOKE_VIDEO, CameraSettings.getKaraokeState() ? str4 : str3);
            hashMap3.put(VideoAttr.PARAM_AI_NOISE_REDUCTION, CameraSettings.getAiNoiseReductionState() ? str4 : str3);
            if (CameraSettings.getAiNoiseReductionState()) {
                str3 = str4;
            }
            hashMap3.put(VideoAttr.PARAM_AI_NOISE_REDUCTION_VIDEO, str3);
            MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap3);
        }
        trackMacroModeTaken(i2);
    }

    public static void trackVideoSmoothZoom(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(VideoAttr.PARAM_VIDEO_SMOOTH_ZOOM, str);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackVideoSnapshot(boolean z) {
        HashMap hashMap = new HashMap();
        DataRepository.dataItemGlobal().getCurrentMode();
        hashMap.put(VideoAttr.PARAM_VIDEO_SNAPSHOT_COUNT, String.valueOf(1));
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackWithoutNetwork() {
        HashMap hashMap = new HashMap();
        hashMap.put(FeatureName.VALUE_SUBTITLE, FeatureName.VALUE_SUBTITLE_START_NO_NETWORK);
        MistatsWrapper.mistatEvent(VideoAttr.KEY_VIDEO_COMMON_CLICK, hashMap);
    }

    public static void trackZoomAdjusted(String str, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(Zoom.PARAM_ZOOM_ADJUSTED_MODE, str);
        hashMap.put(Zoom.PARAM_ZOOM_IN_RECORDING, z ? "on" : "off");
        MistatsWrapper.mistatEvent(Zoom.KEY_ZOOM, hashMap);
    }

    public static void trackZoomMapMoveWindow(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(ZoomMapAttr.ATTR_MOVE_WINDOW, str);
        MistatsWrapper.mistatEvent(ZoomMapAttr.KEY_ZOOM_MAP, hashMap);
    }

    public static void trackZoomMapRemoveWindow() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, ZoomMapAttr.VALUE_REMOVE_WINDOW);
        MistatsWrapper.mistatEvent(ZoomMapAttr.KEY_ZOOM_MAP, hashMap);
    }

    public static void trackZoomMapShow() {
        HashMap hashMap = new HashMap();
        hashMap.put(BaseEvent.OPERATE_STATE, ZoomMapAttr.VALUE_SHOW_WINDOW);
        MistatsWrapper.mistatEvent(ZoomMapAttr.KEY_ZOOM_MAP, hashMap);
    }

    public static String triggerModeToName(int i) {
        return (String) sTriggerModeIdToName.get(i);
    }

    private static String videoQualityToName(String str) {
        if (String.valueOf(8).equals(str)) {
            return BaseEvent.QUALITY_2160P;
        }
        if (String.valueOf(6).equals(str)) {
            return BaseEvent.QUALITY_1080P;
        }
        if (String.valueOf(5).equals(str)) {
            return BaseEvent.QUALITY_720P;
        }
        if (String.valueOf(4).equals(str)) {
            return BaseEvent.QUALITY_480P;
        }
        if (ComponentConfigVideoQuality.QUALITY_4K_60FPS.equals(str)) {
            return BaseEvent.QUALITY_4K_60FPS;
        }
        if (ComponentConfigVideoQuality.QUALITY_1080P_60FPS.equals(str)) {
            return BaseEvent.QUALITY_1080P_60FPS;
        }
        if ("3001".equals(str)) {
            return BaseEvent.QUALITY_8K_30FPS;
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("unexpected video quality: ");
        sb.append(str);
        Log.e(str2, sb.toString());
        return BaseEvent.OTHERS;
    }
}
