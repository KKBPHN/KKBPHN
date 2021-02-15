package com.android.camera.statistic;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.beauty.BeautyValues;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera.statistic.MistatsConstants.CUSTOMIZE_CAMERA;
import com.android.camera.statistic.MistatsConstants.FeatureName;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.MiStatParams;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import miui.os.Build;

public class MistatsWrapper {
    private static final String APP_ID = "2882303761517373386";
    private static final String APP_KEY = "5641737344386";
    private static final String CHANNEL = SystemProperties.get("ro.product.mod_device", Build.DEVICE);
    private static final String TAG = "MistatsWrapper";
    private static boolean sDumpStatEvent;
    private static boolean sInitialized;
    private static boolean sIsAnonymous;
    private static boolean sIsCounterEventEnabled;
    private static boolean sIsEnabled;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ModuleName {
    }

    public class PictureTakenParameter {
        public String aiSceneName;
        public int ambilightMode;
        public BeautyValues beautyValues;
        public boolean burst;
        public boolean isASDBacklitTip;
        public boolean isASDPortraitTip;
        public boolean isEnteringMoon;
        public boolean isNearRangeMode;
        public boolean isSelectMoonMode;
        public boolean isSuperNightInCaptureMode;
        public boolean location;
        public int takenNum;
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TriggerMode {
    }

    public static void commonKeyTriggerEvent(String str, Object obj, String str2) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str3 = "attr_trigger_mode";
            if (TextUtils.isEmpty(str2)) {
                str2 = "click";
            }
            miStatParams.putString(str3, str2);
            hashMap.put(str3, str2);
            String statsModuleKey = getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode());
            boolean isIntentIDPhoto = DataRepository.dataItemGlobal().isIntentIDPhoto();
            String str4 = BaseEvent.MODULE_NAME;
            if (isIntentIDPhoto) {
                statsModuleKey = com.android.camera.statistic.MistatsConstants.ModuleName.ID_PHOTO;
            } else {
                miStatParams.putString(str4, statsModuleKey);
            }
            hashMap.put(str4, statsModuleKey);
            String str5 = BaseEvent.FEATURE_NAME;
            miStatParams.putString(str5, str);
            hashMap.put(str5, str);
            if (obj != null) {
                String str6 = obj instanceof Boolean ? ((Boolean) obj).booleanValue() ? "on" : "off" : String.valueOf(obj);
                String str7 = BaseEvent.VALUE;
                miStatParams.putString(str7, str6);
                hashMap.put(str7, str6);
            }
            String str8 = "front";
            String str9 = "back";
            String str10 = CameraSettings.isFrontCamera() ? str8 : str9;
            String str11 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str11, str10);
            if (!CameraSettings.isFrontCamera()) {
                str8 = str9;
            }
            hashMap.put(str11, str8);
            String str12 = FeatureName.KEY_COMMON;
            MiStat.trackEvent(str12, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str12, (Map) hashMap);
            }
        }
    }

    public static void customizeCameraSettingClick(String str) {
        if (str != null && sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            miStatParams.putString("attr_trigger_mode", "click");
            String str2 = BaseEvent.FEATURE_NAME;
            miStatParams.putString(str2, str);
            MiStat.trackEvent(CUSTOMIZE_CAMERA.KEY_SETTING_CLICK, miStatParams);
            if (sDumpStatEvent) {
                HashMap hashMap = new HashMap();
                hashMap.put(str2, str);
                dumpEvent(Setting.KEY_SETTING, (Map) hashMap);
            }
        }
    }

    private static void dumpEvent(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        sb.append("mapKey:");
        sb.append(str);
        sb.append("  ");
        sb.append("mapValue:");
        sb.append(str2);
        Log.d(TAG, sb.toString());
    }

    private static void dumpEvent(String str, Map map) {
        StringBuilder sb = new StringBuilder();
        Set<String> keySet = map.keySet();
        sb.append("functionKey:");
        sb.append(str);
        for (String str2 : keySet) {
            sb.append("\n");
            sb.append("mapKey:");
            sb.append(str2);
            sb.append("  ");
            String valueOf = String.valueOf(map.get(str2));
            sb.append("mapValue:");
            sb.append(valueOf);
        }
        Log.d(TAG, sb.toString());
    }

    public static void featureTriggerEvent(String str, Object obj, String str2) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str3 = "attr_trigger_mode";
            if (TextUtils.isEmpty(str2)) {
                str2 = "click";
            }
            miStatParams.putString(str3, str2);
            hashMap.put(str3, str2);
            String str4 = obj instanceof Boolean ? ((Boolean) obj).booleanValue() ? "on" : "off" : String.valueOf(obj);
            String statsModuleKey = getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode());
            String str5 = BaseEvent.MODULE_NAME;
            miStatParams.putString(str5, statsModuleKey);
            String str6 = BaseEvent.VALUE;
            miStatParams.putString(str6, str4);
            String str7 = "front";
            String str8 = "back";
            String str9 = CameraSettings.isFrontCamera() ? str7 : str8;
            String str10 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str10, str9);
            if (DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                statsModuleKey = com.android.camera.statistic.MistatsConstants.ModuleName.ID_PHOTO;
            }
            hashMap.put(str5, statsModuleKey);
            hashMap.put(str6, str4);
            if (!CameraSettings.isFrontCamera()) {
                str7 = str8;
            }
            hashMap.put(str10, str7);
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:60:?, code lost:
        return com.android.camera.statistic.MistatsConstants.ModuleName.CLONE;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getStatsModuleKey(int i) {
        if (i == 204) {
            return C0122O00000o.instance().OOO000o() ? com.android.camera.statistic.MistatsConstants.ModuleName.MULTI_CAMERA_DUAL_VIDEO : com.android.camera.statistic.MistatsConstants.ModuleName.DUAL_VIDEO;
        }
        if (i == 205) {
            return com.android.camera.statistic.MistatsConstants.ModuleName.AI_WATERMARK;
        }
        switch (i) {
            case 160:
                return "M_unspecified_";
            case 161:
                return "M_funTinyVideo_";
            case 162:
                return "M_recordVideo_";
            case 163:
                return "M_capture_";
            default:
                switch (i) {
                    case 165:
                        return "M_square_";
                    case 166:
                        return "M_panorama_";
                    case 167:
                        return "M_manual_";
                    case 168:
                        return "M_slowMotion_";
                    case 169:
                        return "M_fastMotion_";
                    case 170:
                        return "M_videoHfr_";
                    case 171:
                        return "M_portrait_";
                    case 172:
                        return "M_newSlowMotion_";
                    case 173:
                        return "M_superNight_";
                    case 174:
                        return "M_liveDouyin_";
                    case 175:
                        return "M_48mPixel_";
                    case 176:
                        return "M_wideSelfie_";
                    case 177:
                        return "M_funArMimoji_";
                    case 178:
                        return "M_standaloneMacro_";
                    case 179:
                        break;
                    case 180:
                        return "M_proVideo_";
                    default:
                        switch (i) {
                            case 182:
                                return "M_idCard_";
                            case 183:
                                return "M_miLive_";
                            case 184:
                                return "M_funArMimoji2_";
                            case 185:
                                break;
                            case 186:
                                return com.android.camera.statistic.MistatsConstants.ModuleName.DOC;
                            case 187:
                                return com.android.camera.statistic.MistatsConstants.ModuleName.AMBILIGHT;
                            case 188:
                                return "M_superMoon_";
                            case 189:
                                return com.android.camera.statistic.MistatsConstants.ModuleName.FILM_DOLLY_ZOOM;
                            default:
                                switch (i) {
                                    case 207:
                                        return com.android.camera.statistic.MistatsConstants.ModuleName.FILM_SLOWSHUTTER;
                                    case 208:
                                        return com.android.camera.statistic.MistatsConstants.ModuleName.FILM_EXPOSUREDELAY;
                                    case 209:
                                        break;
                                    case 210:
                                        break;
                                    case 211:
                                        return com.android.camera.statistic.MistatsConstants.ModuleName.FILM;
                                    case 212:
                                        return com.android.camera.statistic.MistatsConstants.ModuleName.FILM_PARALLELDREAM;
                                    case 213:
                                        return com.android.camera.statistic.MistatsConstants.ModuleName.FILM_TIME_FREEZE;
                                    case 214:
                                        return "M_superNightVideo_";
                                    default:
                                        return null;
                                }
                        }
                }
                return "M_liveVlog_";
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0065, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0067, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized void initialize(Context context) {
        synchronized (MistatsWrapper.class) {
            if (!sInitialized) {
                if (Util.isUserUnlocked(context)) {
                    sInitialized = true;
                    boolean z = false;
                    sDumpStatEvent = SystemProperties.getBoolean("camera.debug.dump_stat_event", false);
                    Resources resources = context.getResources();
                    sIsEnabled = resources.getBoolean(R.bool.pref_camera_statistic_default);
                    if (sIsEnabled) {
                        sIsCounterEventEnabled = resources.getBoolean(R.bool.pref_camera_statistic_counter_event_default);
                        sIsAnonymous = Build.IS_INTERNATIONAL_BUILD;
                        MiStat.initialize(context, APP_ID, APP_KEY, false, CHANNEL);
                        if (!sIsAnonymous) {
                            z = true;
                        }
                        MiStat.setExceptionCatcherEnabled(z);
                        if (sDumpStatEvent) {
                            MiStat.setDebugModeEnabled(true);
                        }
                        MiStat.setUploadInterval(90000);
                        MiStat.setUseSystemUploadingService(true, true);
                        if (Build.IS_INTERNATIONAL_BUILD) {
                            MiStat.setInternationalRegion(true, Build.getRegion());
                        }
                    }
                }
            }
        }
    }

    public static boolean isCounterEventDisabled() {
        return !sIsCounterEventEnabled;
    }

    public static void keyTriggerEvent(String str, String str2, Object obj) {
        keyTriggerEvent(str, str2, obj, "none");
    }

    public static void keyTriggerEvent(String str, String str2, Object obj, String str3) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str4 = "attr_trigger_mode";
            if (TextUtils.isEmpty(str3)) {
                str3 = "click";
            }
            miStatParams.putString(str4, str3);
            hashMap.put(str4, str3);
            String str5 = obj instanceof Boolean ? ((Boolean) obj).booleanValue() ? "on" : "off" : String.valueOf(obj);
            String statsModuleKey = getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode());
            String str6 = BaseEvent.MODULE_NAME;
            miStatParams.putString(str6, statsModuleKey);
            miStatParams.putString(str2, str5);
            String str7 = "front";
            String str8 = "back";
            String str9 = CameraSettings.isFrontCamera() ? str7 : str8;
            String str10 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str10, str9);
            if (DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                statsModuleKey = com.android.camera.statistic.MistatsConstants.ModuleName.ID_PHOTO;
            }
            hashMap.put(str6, statsModuleKey);
            hashMap.put(str2, str5);
            if (!CameraSettings.isFrontCamera()) {
                str7 = str8;
            }
            hashMap.put(str10, str7);
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void mistatEvent(String str, Map map) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String statsModuleKey = getStatsModuleKey(DataRepository.dataItemGlobal().getCurrentMode());
            String str2 = BaseEvent.MODULE_NAME;
            miStatParams.putString(str2, statsModuleKey);
            String str3 = CameraSettings.isFrontCamera() ? "front" : "back";
            String str4 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str4, str3);
            if (DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                statsModuleKey = com.android.camera.statistic.MistatsConstants.ModuleName.ID_PHOTO;
            }
            hashMap.put(str2, statsModuleKey);
            hashMap.put(str4, str3);
            for (String str5 : map.keySet()) {
                String valueOf = String.valueOf(map.get(str5));
                miStatParams.putString(str5, valueOf);
                hashMap.put(str5, valueOf);
            }
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void mistatEventSimple(String str, Map map) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            for (String str2 : map.keySet()) {
                miStatParams.putString(str2, String.valueOf(map.get(str2)));
            }
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, map);
            }
        }
    }

    public static void modeMistatsEvent(int i, Map map) {
        moduleMistatsEvent(getStatsModuleKey(i), map);
    }

    public static void moduleCaptureEvent(String str, Map map) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str2 = BaseEvent.PHOTO;
            String str3 = BaseEvent.MODE;
            miStatParams.putString(str3, str2);
            String str4 = "front";
            String str5 = "back";
            String str6 = CameraSettings.isFrontCamera() ? str4 : str5;
            String str7 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str7, str6);
            hashMap.put(str3, str2);
            if (!CameraSettings.isFrontCamera()) {
                str4 = str5;
            }
            hashMap.put(str7, str4);
            for (String str8 : map.keySet()) {
                String valueOf = String.valueOf(map.get(str8));
                miStatParams.putString(str8, valueOf);
                hashMap.put(str8, valueOf);
            }
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void moduleMistatsEvent(String str, Map map) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str2 = "front";
            String str3 = "back";
            String str4 = CameraSettings.isFrontCamera() ? str2 : str3;
            String str5 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str5, str4);
            if (!CameraSettings.isFrontCamera()) {
                str2 = str3;
            }
            hashMap.put(str5, str2);
            for (String str6 : map.keySet()) {
                String valueOf = String.valueOf(map.get(str6));
                miStatParams.putString(str6, valueOf);
                hashMap.put(str6, valueOf);
            }
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void moduleRecordEvent(String str, Map map) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str2 = "video";
            String str3 = BaseEvent.MODE;
            miStatParams.putString(str3, str2);
            String str4 = "front";
            String str5 = "back";
            String str6 = CameraSettings.isFrontCamera() ? str4 : str5;
            String str7 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str7, str6);
            hashMap.put(str3, str2);
            if (!CameraSettings.isFrontCamera()) {
                str4 = str5;
            }
            hashMap.put(str7, str4);
            for (String str8 : map.keySet()) {
                String valueOf = String.valueOf(map.get(str8));
                miStatParams.putString(str8, valueOf);
                hashMap.put(str8, valueOf);
            }
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void moduleUIClickEvent(int i, String str, Object obj) {
        moduleUIClickEvent(getStatsModuleKey(i), str, obj);
    }

    public static void moduleUIClickEvent(String str, String str2, Object obj) {
        if (sIsEnabled) {
            MiStatParams miStatParams = new MiStatParams();
            HashMap hashMap = new HashMap();
            String str3 = obj instanceof Boolean ? ((Boolean) obj).booleanValue() ? "on" : "off" : String.valueOf(obj);
            String str4 = "click";
            String str5 = "attr_trigger_mode";
            miStatParams.putString(str5, str4);
            String str6 = BaseEvent.FEATURE_NAME;
            miStatParams.putString(str6, str2);
            String str7 = BaseEvent.VALUE;
            miStatParams.putString(str7, str3);
            String str8 = "front";
            String str9 = "back";
            String str10 = CameraSettings.isFrontCamera() ? str8 : str9;
            String str11 = BaseEvent.SENSOR_ID;
            miStatParams.putString(str11, str10);
            hashMap.put(str5, str4);
            hashMap.put(str6, str2);
            hashMap.put(str7, str3);
            if (!CameraSettings.isFrontCamera()) {
                str8 = str9;
            }
            hashMap.put(str11, str8);
            MiStat.trackEvent(str, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str, (Map) hashMap);
            }
        }
    }

    public static void recordPageEnd(String str) {
        if (sIsEnabled) {
            MiStat.trackPageEnd(str, new MiStatParams());
        }
    }

    public static void recordPageStart(String str) {
        if (sIsEnabled) {
            MiStat.trackPageStart(str);
        }
    }

    public static void recordPropertyEvent(String str, String str2) {
        if (sIsEnabled) {
            MiStat.setUserProperty(str, str2);
        }
    }

    public static void settingClickEvent(String str, Object obj) {
        if (str != null) {
            String str2 = obj instanceof Boolean ? ((Boolean) obj).booleanValue() ? "on" : "off" : String.valueOf(obj);
            char c = 65535;
            if (str.hashCode() == 1694617033 && str.equals(Setting.PARAM_VIDEO_TIME_LAPSE_FRAME_INTERVAL)) {
                c = 0;
            }
            if (c == 0) {
                try {
                    str2 = CameraStatUtils.timeLapseIntervalToName(Integer.parseInt(str2));
                } catch (NumberFormatException unused) {
                    String str3 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("invalid interval ");
                    sb.append(str2);
                    Log.e(str3, sb.toString());
                }
            }
            if (sIsEnabled) {
                MiStatParams miStatParams = new MiStatParams();
                miStatParams.putString("attr_trigger_mode", "click");
                String str4 = BaseEvent.FEATURE_NAME;
                miStatParams.putString(str4, str);
                String str5 = BaseEvent.VALUE;
                if (str2 != null) {
                    miStatParams.putString(str5, str2);
                    miStatParams.putString(str, str2);
                }
                String str6 = Setting.KEY_SETTING;
                MiStat.trackEvent(str6, miStatParams);
                if (sDumpStatEvent) {
                    HashMap hashMap = new HashMap();
                    hashMap.put(str4, str);
                    hashMap.put(str5, str2);
                    hashMap.put(str, str2);
                    dumpEvent(str6, (Map) hashMap);
                }
            }
        }
    }

    public static void settingSchedualEvent(Map map) {
        if (sIsEnabled) {
            HashMap hashMap = new HashMap();
            MiStatParams miStatParams = new MiStatParams();
            String str = BaseEvent.SCHEDULE;
            String str2 = "attr_trigger_mode";
            miStatParams.putString(str2, str);
            hashMap.put(str2, str);
            for (String str3 : map.keySet()) {
                String valueOf = String.valueOf(map.get(str3));
                miStatParams.putString(str3, valueOf);
                hashMap.put(str3, valueOf);
            }
            String str4 = Setting.KEY_SETTING;
            MiStat.trackEvent(str4, miStatParams);
            if (sDumpStatEvent) {
                dumpEvent(str4, (Map) hashMap);
            }
        }
    }
}
