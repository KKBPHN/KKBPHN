package com.android.camera.statistic;

import android.content.Context;
import android.os.SystemClock;
import android.os.statistics.E2EScenario;
import android.os.statistics.E2EScenarioPayload;
import android.os.statistics.E2EScenarioPerfTracer;
import android.os.statistics.E2EScenarioSettings;
import android.provider.MiuiSettings.System;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.log.Log;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class ScenarioTrackUtil {
    /* access modifiers changed from: private */
    public static final String TAG = "ScenarioTrackUtil";
    public static final CameraEventScenario sCaptureTimeScenario = new CameraEventScenario("CaptureTime");
    public static final CameraEventScenario sLaunchTimeScenario = new CameraEventScenario("CameraLaunchTime");
    public static final CameraEventScenario sShotToGalleryTimeScenario = new CameraEventScenario("ShotToGallery");
    public static final CameraEventScenario sShotToViewTimeScenario = new CameraEventScenario("ShotToView");
    public static final CameraEventScenario sStartVideoRecordTimeScenario = new CameraEventScenario("StartVideoRecordTime");
    public static final CameraEventScenario sStopVideoRecordTimeScenario = new CameraEventScenario("StopVideoRecordTime");
    private static final CameraEventScenario sSwitchCameraTimeScenario = new CameraEventScenario("SwitchCameraTime");
    public static final CameraEventScenario sSwitchModeTimeScenario = new CameraEventScenario("SwitchModeTime");
    private static final E2EScenarioSettings scenarioSettings = new E2EScenarioSettings();

    public class CameraEventScenario {
        private static final String CAMERA_PACKAGE = "com.android.camera";
        private static final String CATEGORY_PERFORMANCE = "Performance";
        E2EScenario e2eScenario;
        public volatile boolean isTrackStarted = false;
        String mEventName;

        CameraEventScenario(String str) {
            this.e2eScenario = initE2EScenario(str);
            this.mEventName = str;
        }

        private E2EScenario initE2EScenario(String str) {
            try {
                return new E2EScenario("com.android.camera", CATEGORY_PERFORMANCE, str);
            } catch (Exception e) {
                String access$000 = ScenarioTrackUtil.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(" initializer failed: ");
                sb.append(e.getMessage());
                Log.w(access$000, sb.toString());
                return null;
            }
        }

        public String toString() {
            return this.mEventName;
        }
    }

    class MapUtil {
        public static final String BACK = "back";
        public static final String BEAUTY = "M_beauty_";
        public static final String CAPTURE = "M_capture_";
        public static final String FAST_MOTION = "M_fastMotion_";
        public static final String FRONT = "front";
        public static final String FUN = "M_funTinyVideo_";
        public static final String FUN_AR = "M_funArMimoji_";
        public static final String FUN_AR2 = "M_funArMimoji2_";
        public static final String ID_CARD = "M_idCard_";
        public static final String LIVE = "M_liveDouyin_";
        public static final String LIVE_VV = "M_liveVlog_";
        public static final String MANUAL = "M_manual_";
        public static final String MI_LIVE = "M_miLive_";
        public static final String NEW_SLOW_MOTION = "M_newSlowMotion_";
        public static final String PANORAMA = "M_panorama_";
        public static final String PIXEL = "M_48mPixel_";
        public static final String PORTRAIT = "M_portrait_";
        public static final String PROVIDEO = "M_proVideo_";
        public static final String RECORD_VIDEO = "M_recordVideo_";
        public static final String SLOW_MOTION = "M_slowMotion_";
        public static final String SQUARE = "M_square_";
        public static final String STANDALONE_MACRO = "M_standaloneMacro_";
        public static final String SUPER_MOON = "M_superMoon_";
        public static final String SUPER_NIGHT = "M_superNight_";
        public static final String SUPER_NIGHT_VIDEO = "M_superNightVideo_";
        public static final String UNSPECIFIED = "M_unspecified_";
        public static final String VALUE_TARGET_MODE = "target_mode";
        public static final String VIDEO_HFR = "M_videoHfr_";
        public static final String WIDE_SELFIE = "M_wideSelfie_";
        private static SparseArray sCameraModeIdToName = new SparseArray();

        static {
            sCameraModeIdToName.put(161, "M_funTinyVideo_");
            sCameraModeIdToName.put(174, "M_liveDouyin_");
            sCameraModeIdToName.put(183, "M_miLive_");
            sCameraModeIdToName.put(177, "M_funArMimoji_");
            sCameraModeIdToName.put(184, "M_funArMimoji2_");
            sCameraModeIdToName.put(163, "M_capture_");
            sCameraModeIdToName.put(165, "M_square_");
            sCameraModeIdToName.put(167, "M_manual_");
            sCameraModeIdToName.put(171, "M_portrait_");
            sCameraModeIdToName.put(188, "M_superMoon_");
            sCameraModeIdToName.put(166, "M_panorama_");
            sCameraModeIdToName.put(176, "M_wideSelfie_");
            sCameraModeIdToName.put(172, "M_newSlowMotion_");
            sCameraModeIdToName.put(162, "M_recordVideo_");
            sCameraModeIdToName.put(169, "M_fastMotion_");
            sCameraModeIdToName.put(173, "M_superNight_");
            sCameraModeIdToName.put(214, "M_superNightVideo_");
            sCameraModeIdToName.put(175, "M_48mPixel_");
            sCameraModeIdToName.put(180, "M_proVideo_");
        }

        MapUtil() {
        }

        public static String cameraIdToName(boolean z) {
            return z ? "front" : "back";
        }

        public static String modeIdToName(int i) {
            String str = (String) sCameraModeIdToName.get(i);
            return str == null ? "M_unspecified_" : str;
        }
    }

    static {
        scenarioSettings.setStatisticsMode(7);
        scenarioSettings.setHistoryLimitPerDay(200);
    }

    private static void abortScenario(@NonNull CameraEventScenario cameraEventScenario) {
        abortScenario(cameraEventScenario, "");
    }

    private static void abortScenario(@NonNull CameraEventScenario cameraEventScenario, @Nullable String str) {
        if (cameraEventScenario.e2eScenario == null) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("track ");
            sb.append(cameraEventScenario.toString());
            sb.append(" event start cancel due to scenario is null!");
            Log.w(str2, sb.toString());
            return;
        }
        if (cameraEventScenario.isTrackStarted) {
            E2EScenarioPerfTracer.abortScenario(cameraEventScenario.e2eScenario, str);
        }
    }

    private static void beginScenario(@NonNull CameraEventScenario cameraEventScenario) {
        beginScenario(cameraEventScenario, "", null);
    }

    private static void beginScenario(CameraEventScenario cameraEventScenario, E2EScenarioPayload e2EScenarioPayload) {
        beginScenario(cameraEventScenario, "", e2EScenarioPayload);
    }

    private static void beginScenario(@NonNull CameraEventScenario cameraEventScenario, @Nullable String str, @Nullable E2EScenarioPayload e2EScenarioPayload) {
        String str2 = "track ";
        if (cameraEventScenario.e2eScenario == null) {
            String str3 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(cameraEventScenario.toString());
            sb.append(" event start cancel due to scenario is null!");
            Log.w(str3, sb.toString());
            return;
        }
        if (cameraEventScenario.isTrackStarted) {
            E2EScenario e2EScenario = cameraEventScenario.e2eScenario;
            String str4 = "";
            if (!str4.equals(str)) {
                str4 = str;
            }
            E2EScenarioPerfTracer.abortScenario(e2EScenario, str4);
        }
        try {
            E2EScenarioPerfTracer.asyncBeginScenario(cameraEventScenario.e2eScenario, scenarioSettings, str, e2EScenarioPayload);
            cameraEventScenario.isTrackStarted = true;
        } catch (Exception e) {
            String str5 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(cameraEventScenario.toString());
            sb2.append(" event start failed: ");
            sb2.append(e.getMessage());
            Log.w(str5, sb2.toString());
        }
    }

    private static void finishScenario(CameraEventScenario cameraEventScenario, E2EScenarioPayload e2EScenarioPayload) {
        finishScenario(cameraEventScenario, "", e2EScenarioPayload);
    }

    private static void finishScenario(@NonNull CameraEventScenario cameraEventScenario, @Nullable String str, @Nullable E2EScenarioPayload e2EScenarioPayload) {
        String str2;
        StringBuilder sb;
        String str3;
        String str4 = "track ";
        if (cameraEventScenario.e2eScenario == null) {
            str2 = TAG;
            sb = new StringBuilder();
            sb.append(str4);
            sb.append(cameraEventScenario.toString());
            str3 = " event end cancel, due to scenario is null!";
        } else if (!cameraEventScenario.isTrackStarted) {
            str2 = TAG;
            sb = new StringBuilder();
            sb.append(str4);
            sb.append(cameraEventScenario.toString());
            str3 = " event end cancel, due to scenario has not started!";
        } else {
            if (e2EScenarioPayload != null) {
                try {
                    E2EScenarioPerfTracer.finishScenario(cameraEventScenario.e2eScenario, str, e2EScenarioPayload);
                } catch (Exception e) {
                    String str5 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str4);
                    sb2.append(cameraEventScenario.toString());
                    sb2.append(" event end failed: ");
                    sb2.append(e.getMessage());
                    Log.w(str5, sb2.toString());
                }
            } else {
                E2EScenarioPerfTracer.finishScenario(cameraEventScenario.e2eScenario);
            }
            cameraEventScenario.isTrackStarted = false;
            return;
        }
        sb.append(str3);
        Log.w(str2, sb.toString());
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0040  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void trackAppLunchTimeEnd(@Nullable Map map, Context context) {
        JSONObject jSONObject;
        String str = "time";
        String string = System.getString(context.getContentResolver(), "camera_boost");
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        if (string != null) {
            boolean z = false;
            try {
                jSONObject = new JSONObject(string);
                try {
                    if (SystemClock.uptimeMillis() - Long.parseLong(jSONObject.optString(str)) < 1000) {
                        z = true;
                    }
                    jSONObject.remove(str);
                } catch (Exception e) {
                    e = e;
                    Log.w(TAG, "Exception", (Throwable) e);
                    if (z) {
                    }
                    e2EScenarioPayload.putAll(map);
                    finishScenario(sLaunchTimeScenario, e2EScenarioPayload);
                }
            } catch (Exception e2) {
                e = e2;
                jSONObject = null;
                Log.w(TAG, "Exception", (Throwable) e);
                if (z) {
                }
                e2EScenarioPayload.putAll(map);
                finishScenario(sLaunchTimeScenario, e2EScenarioPayload);
            }
            if (z) {
                Iterator keys = jSONObject.keys();
                while (keys.hasNext()) {
                    String str2 = (String) keys.next();
                    e2EScenarioPayload.put(str2, jSONObject.opt(str2));
                }
            }
        }
        e2EScenarioPayload.putAll(map);
        finishScenario(sLaunchTimeScenario, e2EScenarioPayload);
    }

    public static void trackAppLunchTimeStart(@NonNull boolean z) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.put("LaunchMode", z ? "COLD" : "HOT");
        beginScenario(sLaunchTimeScenario, e2EScenarioPayload);
    }

    public static void trackCaptureTimeEnd() {
        finishScenario(sCaptureTimeScenario, null);
    }

    public static void trackCaptureTimeStart(@NonNull boolean z, @NonNull int i) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"CameraID", MapUtil.cameraIdToName(z), "Module", MapUtil.modeIdToName(i)});
        beginScenario(sCaptureTimeScenario, e2EScenarioPayload);
    }

    public static void trackScenarioAbort(@NonNull CameraEventScenario cameraEventScenario) {
        abortScenario(cameraEventScenario);
    }

    public static void trackScenarioAbort(@NonNull CameraEventScenario cameraEventScenario, @Nullable String str) {
        abortScenario(cameraEventScenario, str);
    }

    public static void trackShotToGalleryEnd(@NonNull boolean z, @NonNull long j) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"quickShotAnimation", Boolean.valueOf(z)});
        finishScenario(sShotToGalleryTimeScenario, String.valueOf(j), e2EScenarioPayload);
    }

    public static void trackShotToGalleryStart(@NonNull boolean z, @NonNull int i, @NonNull long j) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"CameraID", MapUtil.cameraIdToName(z), "Module", MapUtil.modeIdToName(i)});
        beginScenario(sShotToGalleryTimeScenario, String.valueOf(j), e2EScenarioPayload);
    }

    public static void trackShotToViewEnd(@NonNull boolean z, @NonNull long j) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"quickShotAnimation", Boolean.valueOf(z)});
        finishScenario(sShotToViewTimeScenario, String.valueOf(j), e2EScenarioPayload);
    }

    public static void trackShotToViewStart(@NonNull boolean z, @NonNull int i, @NonNull long j) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"CameraID", MapUtil.cameraIdToName(z), "Module", MapUtil.modeIdToName(i)});
        beginScenario(sShotToViewTimeScenario, String.valueOf(j), e2EScenarioPayload);
    }

    public static void trackStartVideoRecordEnd() {
        finishScenario(sStartVideoRecordTimeScenario, null);
    }

    public static void trackStartVideoRecordStart(@NonNull String str, @NonNull boolean z) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"mode", str, "cameraId", MapUtil.cameraIdToName(z)});
        beginScenario(sStartVideoRecordTimeScenario, e2EScenarioPayload);
    }

    public static void trackStopVideoRecordEnd() {
        finishScenario(sStopVideoRecordTimeScenario, null);
    }

    public static void trackStopVideoRecordStart(@NonNull String str, @NonNull boolean z) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"mode", str, "cameraId", MapUtil.cameraIdToName(z)});
        beginScenario(sStopVideoRecordTimeScenario, e2EScenarioPayload);
    }

    public static void trackSwitchCameraEnd() {
        finishScenario(sSwitchCameraTimeScenario, null);
    }

    public static void trackSwitchCameraStart(@NonNull boolean z, @NonNull boolean z2, @NonNull int i) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"from", MapUtil.cameraIdToName(z), "to", MapUtil.cameraIdToName(z2), "inMode", MapUtil.modeIdToName(i)});
        beginScenario(sSwitchCameraTimeScenario, e2EScenarioPayload);
    }

    public static void trackSwitchModeEnd() {
        finishScenario(sSwitchModeTimeScenario, null);
    }

    public static void trackSwitchModeStart(@NonNull int i, @NonNull int i2, @NonNull boolean z) {
        E2EScenarioPayload e2EScenarioPayload = new E2EScenarioPayload();
        e2EScenarioPayload.putValues(new Object[]{"from", MapUtil.modeIdToName(i), "to", MapUtil.modeIdToName(i2), "cameraId", MapUtil.cameraIdToName(z)});
        beginScenario(sSwitchModeTimeScenario, e2EScenarioPayload);
    }
}
