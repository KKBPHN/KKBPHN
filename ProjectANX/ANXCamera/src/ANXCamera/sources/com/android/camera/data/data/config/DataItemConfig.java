package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.text.TextUtils;
import com.android.camera.data.data.DataItemBase;
import com.android.camera2.CameraCapabilities;

public class DataItemConfig extends DataItemBase {
    public static final String DATA_CONFIG_AI_SCENE = "pref_camera_ai_scene_mode_key";
    public static final String DATA_CONFIG_BEAUTY_CAPTURE = "pref_beautify_level_key_capture";
    public static final String DATA_CONFIG_BEAUTY_VIDEO = "pref_beautify_level_key_video";
    public static final String DATA_CONFIG_FOCUS_MODE = "pref_camera_focus_mode_key";
    public static final String DATA_CONFIG_FOCUS_SWITCHING = "pref_qc_focus_mode_switching_key";
    public static final String DATA_CONFIG_GIF = "key_config_gif";
    public static final String DATA_CONFIG_GRADIENTER = "pref_camera_gradienter_key";
    public static final String DATA_CONFIG_LIVE_SHOT = "pref_live_shot_enabled";
    public static final String DATA_CONFIG_MACRO_SCENE = "pref_camera_macro_scene_mode_key";
    public static final String DATA_CONFIG_NEW_SLOW_MOTION_KEY = "key_new_slow_motion";
    public static final String DATA_CONFIG_RATIO = "pref_camera_picturesize_key";
    public static final String DATA_CONFIG_RATIO_CINEMATIC = "is_cinematic";
    public static final String DATA_CONFIG_RATIO_SQUARE = "is_square";
    public static final String DATA_CONFIG_RAW = "pref_camera_raw_key";
    public static final String DATA_CONFIG_SLOW_MOTION_QUALITY = "pref_video_new_slow_motion_key";
    public static final String DATA_CONFIG_STICKER_PATH = "pref_sticker_path_key";
    public static final String DATA_CONFIG_VIDEO_BOKEH = "pref_video_bokeh_key";
    public static final String DATA_CONFIG_VIDEO_QUALITY = "pref_video_quality_key";
    public static final String KEY = "camera_settings_simple_mode_local_";
    private int mCameraId;
    private ComponentConfigBokeh mComponentBokeh = new ComponentConfigBokeh(this);
    private ComponentConfigAi mComponentConfigAi;
    private ComponentConfigAuxiliary mComponentConfigAuxiliary;
    private ComponentConfigBeauty mComponentConfigBeauty;
    private ComponentConfigGradienter mComponentConfigGradienter;
    private ComponentConfigLiveShot mComponentConfigLiveShot;
    private ComponentConfigMeter mComponentConfigMeter;
    private ComponentConfigRatio mComponentConfigRatio;
    private ComponentConfigRaw mComponentConfigRaw;
    private ComponentConfigSlowMotionQuality mComponentConfigSlowMotionQuality;
    private ComponentConfigUltraWide mComponentConfigUltraWide;
    private ComponentConfigVideoQuality mComponentConfigVideoQuality;
    private ComponentConfigFlash mComponentFlash = new ComponentConfigFlash(this);
    private ComponentConfigHdr mComponentHdr = new ComponentConfigHdr(this);
    private ComponentManuallyET mComponentManuallyET;
    private ComponentManuallyEV mComponentManuallyEV;
    private ComponentManuallyISO mComponentManuallyISO;
    private ComponentManuallyWB mComponentManuallyWB;
    private int mIntentType;
    private ComponentManuallyDualLens mManuallyDualLens;
    private ComponentManuallyFocus mManuallyFocus;
    private ComponentConfigSlowMotion mSlowMotion;

    public DataItemConfig(int i, int i2) {
        this.mCameraId = i;
        this.mIntentType = i2;
        this.mComponentConfigBeauty = new ComponentConfigBeauty(this, i);
        this.mSlowMotion = new ComponentConfigSlowMotion(this, i);
        this.mComponentConfigRatio = new ComponentConfigRatio(this);
        this.mComponentConfigAi = new ComponentConfigAi(this);
        this.mComponentConfigRaw = new ComponentConfigRaw(this);
        this.mComponentConfigSlowMotionQuality = new ComponentConfigSlowMotionQuality(this);
        this.mComponentConfigMeter = new ComponentConfigMeter(this);
        this.mComponentConfigVideoQuality = new ComponentConfigVideoQuality(this);
        this.mComponentManuallyWB = new ComponentManuallyWB(this);
        this.mComponentManuallyET = new ComponentManuallyET(this);
        this.mComponentManuallyISO = new ComponentManuallyISO(this);
        this.mComponentManuallyEV = new ComponentManuallyEV(this);
        this.mManuallyDualLens = new ComponentManuallyDualLens(this);
        this.mComponentConfigAuxiliary = new ComponentConfigAuxiliary(this);
        this.mComponentConfigGradienter = new ComponentConfigGradienter(this, i);
        this.mComponentConfigLiveShot = new ComponentConfigLiveShot(this);
    }

    public static int provideLocalId(int i, int i2) {
        return i2 == 0 ? i : i + 100;
    }

    public ComponentConfigBokeh getComponentBokeh() {
        return this.mComponentBokeh;
    }

    public ComponentConfigAi getComponentConfigAi() {
        return this.mComponentConfigAi;
    }

    public ComponentConfigAuxiliary getComponentConfigAuxiliary() {
        if (this.mComponentConfigAuxiliary == null) {
            this.mComponentConfigAuxiliary = new ComponentConfigAuxiliary(this);
        }
        return this.mComponentConfigAuxiliary;
    }

    public ComponentConfigBeauty getComponentConfigBeauty() {
        return this.mComponentConfigBeauty;
    }

    public ComponentConfigGradienter getComponentConfigGradienter() {
        return this.mComponentConfigGradienter;
    }

    public ComponentConfigLiveShot getComponentConfigLiveShot() {
        return this.mComponentConfigLiveShot;
    }

    public ComponentConfigMeter getComponentConfigMeter() {
        return this.mComponentConfigMeter;
    }

    public ComponentConfigRatio getComponentConfigRatio() {
        return this.mComponentConfigRatio;
    }

    public ComponentConfigRaw getComponentConfigRaw() {
        return this.mComponentConfigRaw;
    }

    public ComponentConfigSlowMotion getComponentConfigSlowMotion() {
        return this.mSlowMotion;
    }

    public ComponentConfigSlowMotionQuality getComponentConfigSlowMotionQuality() {
        return this.mComponentConfigSlowMotionQuality;
    }

    public ComponentConfigUltraWide getComponentConfigUltraWide() {
        if (this.mComponentConfigUltraWide == null) {
            this.mComponentConfigUltraWide = new ComponentConfigUltraWide(this);
        }
        return this.mComponentConfigUltraWide;
    }

    public ComponentConfigVideoQuality getComponentConfigVideoQuality() {
        return this.mComponentConfigVideoQuality;
    }

    public ComponentConfigVideoSubFPS getComponentConfigVideoSubFps() {
        return this.mComponentConfigVideoQuality.getComponentConfigVideoSubFps();
    }

    public ComponentConfigVideoSubQuality getComponentConfigVideoSubQuality() {
        return this.mComponentConfigVideoQuality.getComponentConfigVideoSubQuality();
    }

    public ComponentConfigFlash getComponentFlash() {
        return this.mComponentFlash;
    }

    public ComponentConfigHdr getComponentHdr() {
        return this.mComponentHdr;
    }

    public ComponentManuallyEV getComponentManuallyEV() {
        if (this.mComponentManuallyEV == null) {
            this.mComponentManuallyEV = new ComponentManuallyEV(this);
        }
        return this.mComponentManuallyEV;
    }

    public ComponentManuallyDualLens getManuallyDualLens() {
        if (this.mManuallyDualLens == null) {
            this.mManuallyDualLens = new ComponentManuallyDualLens(this);
        }
        return this.mManuallyDualLens;
    }

    public ComponentManuallyFocus getManuallyFocus() {
        if (this.mManuallyFocus == null) {
            this.mManuallyFocus = new ComponentManuallyFocus(this);
        }
        return this.mManuallyFocus;
    }

    public ComponentManuallyET getmComponentManuallyET() {
        if (this.mComponentManuallyET == null) {
            this.mComponentManuallyET = new ComponentManuallyET(this);
        }
        return this.mComponentManuallyET;
    }

    public ComponentManuallyISO getmComponentManuallyISO() {
        if (this.mComponentManuallyISO == null) {
            this.mComponentManuallyISO = new ComponentManuallyISO(this);
        }
        return this.mComponentManuallyISO;
    }

    public ComponentManuallyWB getmComponentManuallyWB() {
        if (this.mComponentManuallyWB == null) {
            this.mComponentManuallyWB = new ComponentManuallyWB(this);
        }
        return this.mComponentManuallyWB;
    }

    public boolean isSwitchOn(String str) {
        return getBoolean(str, false);
    }

    public boolean isTransient() {
        return false;
    }

    public String provideKey() {
        StringBuilder sb = new StringBuilder();
        sb.append("camera_settings_simple_mode_local_");
        sb.append(provideLocalId(this.mCameraId, this.mIntentType));
        return sb.toString();
    }

    public boolean reConfigBokehIfHdrChanged(int i, String str) {
        if (!TextUtils.isEmpty(str) && !"off".equals(str)) {
            if ("on".equals(this.mComponentBokeh.getComponentValue(i))) {
                this.mComponentBokeh.toggle(i);
                return true;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x004f, code lost:
        if (r8 != 3) goto L_0x007d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0056, code lost:
        if (r1.equals(r0) != false) goto L_0x007d;
     */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean reConfigFlashIfHdrChanged(int i, String str) {
        char c;
        String persistValue = getComponentFlash().getPersistValue(i);
        switch (str.hashCode()) {
            case -1039745817:
                if (str.equals("normal")) {
                    c = 2;
                    break;
                }
            case 3551:
                if (str.equals("on")) {
                    c = 1;
                    break;
                }
            case 109935:
                if (str.equals("off")) {
                    c = 4;
                    break;
                }
            case 3005871:
                if (str.equals("auto")) {
                    c = 0;
                    break;
                }
            case 3322092:
                if (str.equals("live")) {
                    c = 3;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        String str2 = "0";
        if (c != 0) {
            if (c != 1) {
                if (c != 2) {
                }
            }
        } else if ("1".equals(persistValue) || "2".equals(persistValue) || "5".equals(persistValue)) {
            if (C0124O00000oO.Oo00OOo()) {
                str2 = "3";
            }
            if (str2 != null || persistValue.equals(str2)) {
                return false;
            }
            getComponentFlash().setComponentValue(i, str2);
            return !getComponentFlash().isEmpty();
        } else if (ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON.equals(persistValue)) {
            str2 = ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO;
            if (str2 != null) {
            }
            return false;
        }
        str2 = null;
        if (str2 != null) {
        }
        return false;
    }

    public boolean reConfigHdrIfBokehChanged(int i, String str) {
        if ("on".equals(str)) {
            String str2 = "off";
            if (!str2.equals(this.mComponentHdr.getComponentValue(i))) {
                this.mComponentHdr.setComponentValue(i, str2);
                return true;
            }
        }
        return false;
    }

    public boolean reConfigHhrIfFlashChanged(int i, String str, String str2) {
        String persistValue = getComponentHdr().getPersistValue(i);
        String str3 = "0";
        boolean equals = str3.equals(str);
        String str4 = ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_AUTO;
        String str5 = "3";
        boolean z = equals && (str5.equals(str2) || str4.equals(str2));
        String str6 = "off";
        if (!z || (!"normal".equals(persistValue) && !"live".equals(persistValue) && !"on".equals(persistValue))) {
            boolean equals2 = str3.equals(str);
            String str7 = ComponentConfigFlash.FLASH_VALUE_SCREEN_LIGHT_ON;
            String str8 = "5";
            String str9 = "2";
            String str10 = "1";
            boolean z2 = (equals2 || str5.equals(str) || str4.equals(str)) && (str10.equals(str2) || str9.equals(str2) || str8.equals(str2) || str7.equals(str2));
            if (!z2) {
                boolean z3 = (str10.equals(str) || str9.equals(str) || str8.equals(str) || str7.equals(str)) && (str3.equals(str2) || str5.equals(str2) || str4.equals(str2));
                str6 = z3 ? getComponentHdr().getDefaultValue(i) : null;
            }
        } else if (getComponentHdr().isSupportAutoHdr()) {
            str6 = "auto";
        }
        if (str6 == null || persistValue.equals(str6)) {
            return false;
        }
        getComponentHdr().setComponentValue(i, str6);
        return !getComponentHdr().isEmpty();
    }

    public void reInitComponent(int i, int i2, CameraCapabilities cameraCapabilities) {
        getComponentConfigUltraWide().reInit(i, i2, cameraCapabilities);
        this.mComponentFlash.reInit(i, i2, cameraCapabilities, getComponentConfigUltraWide());
        this.mComponentHdr.reInit(i, i2, cameraCapabilities, this.mIntentType);
        this.mComponentBokeh.reInit(i, i2);
        this.mComponentConfigAi.reInit(i, i2, cameraCapabilities, this.mIntentType);
        this.mComponentConfigRaw.reInit(i, i2, cameraCapabilities);
        this.mComponentConfigRatio.reInit(i, i2);
        this.mComponentConfigSlowMotionQuality.reInit(i, i2, cameraCapabilities);
        this.mSlowMotion.reInit(i, i2, this.mComponentConfigSlowMotionQuality, cameraCapabilities);
        this.mComponentConfigMeter.reInit(i, i2, cameraCapabilities);
        this.mComponentConfigVideoQuality.reInit(i, i2, cameraCapabilities, this.mIntentType);
        this.mComponentManuallyWB.reInit(i, i2, cameraCapabilities);
        this.mComponentManuallyET.reInit(i, cameraCapabilities);
        this.mComponentManuallyISO.reInit(i, cameraCapabilities);
        this.mComponentManuallyEV.reInit(i, cameraCapabilities);
        this.mManuallyDualLens.reInit(i);
        this.mComponentConfigAuxiliary.reInit(i, cameraCapabilities);
        this.mComponentConfigGradienter.reInit(i, i2);
        this.mComponentConfigLiveShot.reInit(i, i2, cameraCapabilities);
    }

    public void resetAll() {
        editor().clear().apply();
        getComponentFlash().clearClosed();
        getComponentHdr().clearClosed();
        getComponentConfigBeauty().clearClosed();
    }

    public boolean supportAi() {
        return !this.mComponentConfigAi.isEmpty();
    }

    public boolean supportBokeh() {
        return !this.mComponentBokeh.isEmpty();
    }

    public boolean supportFlash() {
        return !this.mComponentFlash.isEmpty();
    }

    public boolean supportHdr() {
        return !this.mComponentHdr.isEmpty();
    }

    public boolean supportRatio() {
        return this.mComponentConfigRatio.supportRatioSwitch();
    }

    public void switchOff(String str) {
        putBoolean(str, false);
    }

    public void switchOn(String str) {
        putBoolean(str, true);
    }
}
