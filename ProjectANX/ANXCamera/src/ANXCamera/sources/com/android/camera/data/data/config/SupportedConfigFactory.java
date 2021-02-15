package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera2.CameraCapabilities;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class SupportedConfigFactory {
    public static final int AI_108 = 171;
    public static final int AI_AUDIO = 168;
    public static final int AI_DETECT = 242;
    public static final int AI_ENHANCED_VIDEO = 175;
    public static final int AI_SCENE = 201;
    public static final int AI_WATERMARK = 223;
    public static final int AUTO_ZOOM = 253;
    public static final int BACK = 217;
    public static final int BACKLIGHT = 249;
    public static final int BEAUTY = 239;
    public static final int BOKEH = 200;
    public static final int CINEMATIC_ASPECT_RATIO = 251;
    public static final String CLOSE_BY_AI = "e";
    public static final String CLOSE_BY_BOKEH = "f";
    public static final String CLOSE_BY_BURST_SHOOT = "d";
    public static final String CLOSE_BY_DOCUMENT_MODE = "p";
    public static final String CLOSE_BY_FILTER = "k";
    public static final String CLOSE_BY_GROUP = "b";
    public static final String CLOSE_BY_HDR = "g";
    public static final String CLOSE_BY_HHT = "a";
    public static final String CLOSE_BY_MACRO_MODE = "m";
    public static final String CLOSE_BY_MANUAL_MODE = "mm";
    public static final String CLOSE_BY_RATIO = "l";
    public static final String CLOSE_BY_RAW = "n";
    public static final String CLOSE_BY_SUPER_RESOLUTION = "c";
    public static final String CLOSE_BY_ULTRA_PIXEL = "j";
    public static final String CLOSE_BY_ULTRA_PIXEL_PORTRAIT = "o";
    public static final String CLOSE_BY_ULTRA_WIDE = "i";
    public static final String CLOSE_BY_VIDEO = "h";
    public static final int COLOR_ENHANCE = 227;
    public static final int DOCUMENT = 221;
    public static final int DOLLY_ZOOM_USE_GUIDE = 179;
    public static final int DUAL_VIDEO = 222;
    public static final int DUAL_WATER_MARK = 240;
    public static final int EXPOSURE_FEEDBACK = 258;
    public static final int EYE_LIGHT = 254;
    public static final int FAST = 233;
    public static final int FILTER = 196;
    public static final int FLASH = 193;
    public static final int FLASH_BLANK = 177;
    public static final int FOCUS_PEAK = 199;
    public static final int GENDER_AGE = 238;
    public static final int GRADIENTER = 229;
    public static final int GROUP = 235;
    public static final int HAND_GESTURE = 252;
    public static final int HDR = 194;
    public static final int HHT = 230;
    public static final int ID_CARD = 166;
    public static final int INVALID = 176;
    public static final int LIGHTING = 203;
    public static final int LIVE_MUSIC_SELECT = 245;
    public static final int LIVE_SHOT = 206;
    public static final int LIVE_VIDEO_QUALITY = 187;
    public static final int MACRO_MODE = 255;
    public static final int MAGIC_FOCUS = 231;
    public static final int MAGIC_MIRROR = 236;
    public static final int MASTER_FILTER = 263;
    public static final int METER = 214;
    public static final int MIMOJI_EDIT = 161;
    public static final int MIMOJI_GIF = 162;
    public static final int MOON = 246;
    public static final int MORE = 197;
    public static final int MULTI_CAM_RESELECT = 513;
    public static final int[] MUTEX_MENU_CONFIGS = {236, 235, 228, 230, 241, 234, 195, 238, 203, 206, 209};
    public static final int[] MUTEX_VIDEO_FEATURES = {233, 212, 218, 220, 253, 255, 216, 165};
    public static final int NEAR_RANGE_MODE = 167;
    public static final int NEW_SLOW_MOTION = 204;
    public static final int NIGHT = 247;
    public static final int PANO_SWITCH_ORIENTATION = 169;
    public static final int PORTRAIT = 195;
    public static final int RATIO = 210;
    public static final int RAW = 237;
    public static final int REFERENCE_LINE = 219;
    public static final int REMOTE_CAMERA = 512;
    public static final int RGB_HISTOGRAM = 261;
    public static final int SCENE = 234;
    public static final int SETTING = 225;
    public static final int SHINE = 212;
    public static final int SILHOUETTE = 248;
    public static final int SLOW_QUALITY = 213;
    public static final int SPEECH_SHUTTER = 262;
    public static final int SUBTITLE = 220;
    public static final int SUPER_EIS = 218;
    public static final int SUPER_EIS_PRO = 165;
    public static final int SUPER_RESOLUTION = 241;
    public static final int TILT = 228;
    public static final int TIMER = 226;
    public static final int TIMER_BURST = 170;
    public static final int ULTRA_PIXEL = 209;
    public static final int ULTRA_PIXEL_PORTRAIT = 215;
    public static final int ULTRA_WIDE = 205;
    public static final int ULTRA_WIDE_BOKEH = 207;
    public static final int USE_GUIDE = 164;
    public static final int VIDEO_8K = 256;
    public static final int VIDEO_BOKEH = 243;
    public static final int VIDEO_BOKEH_WITH_COLOR_RETENTION = 244;
    public static final int VIDEO_LOG = 260;
    public static final int VIDEO_QUALITY = 208;
    public static final int VIDEO_SUB_FPS = 174;
    public static final int VIDEO_SUB_QUALITY = 173;
    public static final int VV = 216;
    public static final int WORKSPACE = 172;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ClosableElement {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CloseElementTrigger {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConfigItem {
    }

    private static TopConfigItem createConfigItem(int i) {
        return new TopConfigItem(i);
    }

    private static TopConfigItem createConfigItem(int i, int i2) {
        return new TopConfigItem(i, i2);
    }

    public static String getConfigKey(int i) {
        if (i == 227) {
            return "pref_color_enhance";
        }
        if (i == 228) {
            return "pref_camera_tilt_shift_mode";
        }
        if (i == 252) {
            return "pref_hand_gesture";
        }
        if (i == 253) {
            return "pref_camera_auto_zoom";
        }
        switch (i) {
            case 195:
                return "pref_camera_portrait_mode_key";
            case 199:
                return "pref_camera_peak_key";
            case 201:
                return "pref_camera_ai_scene_mode_key";
            case 206:
                return "pref_live_shot_enabled";
            case 209:
                return "pref_ultra_pixel";
            case 221:
                return "pref_document_mode_key";
            case 223:
                return "pref_watermark_key";
            case 230:
                return "pref_camera_hand_night_key";
            case 241:
                return "pref_camera_super_resolution_key";
            case 258:
                return "pref_camera_exposure_feedback";
            case 262:
                return "pref_speech_shutter";
            default:
                switch (i) {
                    case 234:
                        return "pref_camera_scenemode_setting_key";
                    case 235:
                        return "pref_camera_groupshot_mode_key";
                    case 236:
                        return "pref_camera_magic_mirror_key";
                    case 237:
                        return "pref_camera_raw_key";
                    case 238:
                        return "pref_camera_show_gender_age_key";
                    default:
                        StringBuilder sb = new StringBuilder();
                        sb.append("unknown config item: ");
                        sb.append(Integer.toHexString(i));
                        throw new RuntimeException(sb.toString());
                }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:100:0x0171, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:111:0x0194, code lost:
        if (r4.supportMacroMode(r1, r0) != false) goto L_0x0196;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:122:0x01b9, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:129:0x01d6, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0205, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:173:0x028d, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:200:0x02ef, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:215:0x0324, code lost:
        if (r19 != false) goto L_0x045e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:242:0x039a, code lost:
        if (O00000Oo.O00000oO.O000000o.C0122O00000o.instance().OOO00o0() != false) goto L_0x0196;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:261:0x03eb, code lost:
        if (r5.OOoOOoo() != false) goto L_0x03ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:294:0x045c, code lost:
        if (r19 != false) goto L_0x045e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0110, code lost:
        if (r5.OOoOOoo() != false) goto L_0x03ed;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0120, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:93:0x0156, code lost:
        if (O00000Oo.O00000oO.O000000o.C0124O00000oO.Oo00O0O() != false) goto L_0x0122;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final SupportedConfigs getSupportedExtraConfigs(int i, int i2, CameraCapabilities cameraCapabilities, boolean z) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = i;
        int i8 = i2;
        SupportedConfigs supportedConfigs = new SupportedConfigs();
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        C0122O00000o instance = C0122O00000o.instance();
        if (i7 != 161) {
            if (i7 != 162) {
                if (i7 != 166) {
                    if (i7 != 167) {
                        if (i7 != 169) {
                            if (i7 != 180) {
                                if (i7 == 214) {
                                    if (dataItemConfig.supportRatio()) {
                                        supportedConfigs.add(210);
                                    }
                                    supportedConfigs.add(225);
                                    if (instance.isCinematicPhotoSupported()) {
                                        supportedConfigs.add(createConfigItem(251));
                                    }
                                    supportedConfigs.add(219);
                                    if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                                        supportedConfigs.add(229);
                                    }
                                    if (instance.OOOOOoo()) {
                                    }
                                } else if (i7 != 183) {
                                    if (i7 == 184) {
                                        if (dataItemConfig.supportRatio()) {
                                            supportedConfigs.add(210);
                                        }
                                        supportedConfigs.add(225);
                                        if (C0124O00000oO.OOOOOo() && DataRepository.dataItemLive().getMimojiStatusManager2().isInMimojiPhoto()) {
                                            supportedConfigs.add(262);
                                        }
                                        supportedConfigs.add(219);
                                        if (i8 == 0) {
                                        }
                                    } else if (i7 != 204) {
                                        if (i7 != 205) {
                                            switch (i7) {
                                                case 171:
                                                    if (dataItemConfig.supportRatio()) {
                                                        supportedConfigs.add(210);
                                                    }
                                                    supportedConfigs.add(226);
                                                    supportedConfigs.add(225);
                                                    if (instance.isCinematicPhotoSupported()) {
                                                        supportedConfigs.add(createConfigItem(251));
                                                    }
                                                    if (dataItemRunning.supportHandGesture()) {
                                                        supportedConfigs.add(252);
                                                    }
                                                    if (C0124O00000oO.OOOOOo()) {
                                                        supportedConfigs.add(262);
                                                    }
                                                    supportedConfigs.add(219);
                                                    if (i8 == 0) {
                                                        break;
                                                    }
                                                    break;
                                                case 172:
                                                    if (dataItemConfig.getComponentConfigSlowMotion().getItems().size() > 1) {
                                                        supportedConfigs.add(213);
                                                        supportedConfigs.add(204);
                                                    } else if (dataItemConfig.getComponentConfigSlowMotionQuality().getItems().size() > 1) {
                                                        supportedConfigs.add(213);
                                                    }
                                                    supportedConfigs.add(225);
                                                    supportedConfigs.add(219);
                                                    if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                                                        supportedConfigs.add(229);
                                                    }
                                                    if (instance.OOO0oo() && i8 == 0) {
                                                        supportedConfigs.add(createConfigItem(255));
                                                        break;
                                                    }
                                                case 173:
                                                    if (dataItemConfig.supportRatio()) {
                                                        supportedConfigs.add(210);
                                                    }
                                                    supportedConfigs.add(225);
                                                    if (instance.isCinematicPhotoSupported()) {
                                                        supportedConfigs.add(createConfigItem(251));
                                                    }
                                                    if (C0124O00000oO.OOOOOo()) {
                                                        supportedConfigs.add(262);
                                                    }
                                                    supportedConfigs.add(219);
                                                    if (i8 == 0) {
                                                        break;
                                                    }
                                                    break;
                                                case 174:
                                                case 176:
                                                    break;
                                                case 175:
                                                    supportedConfigs.add(226).add(225);
                                                    if (C0124O00000oO.OOOOOo()) {
                                                        supportedConfigs.add(262);
                                                    }
                                                    supportedConfigs.add(219);
                                                    if (i8 == 0) {
                                                        break;
                                                    }
                                                    break;
                                                case 177:
                                                    if (dataItemConfig.supportRatio()) {
                                                        supportedConfigs.add(210);
                                                    }
                                                    supportedConfigs.add(225);
                                                    if (C0124O00000oO.OOOOOo()) {
                                                        supportedConfigs.add(262);
                                                    }
                                                    supportedConfigs.add(219);
                                                    if (i8 == 0) {
                                                        break;
                                                    }
                                                    break;
                                                default:
                                                    switch (i7) {
                                                        case 186:
                                                            if (dataItemConfig.supportRatio()) {
                                                                supportedConfigs.add(210);
                                                            }
                                                            supportedConfigs.add(225);
                                                            supportedConfigs.add(219);
                                                            if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                                                                supportedConfigs.add(229);
                                                            }
                                                            if (z) {
                                                                break;
                                                            }
                                                            break;
                                                        case 187:
                                                            supportedConfigs.add(226);
                                                            supportedConfigs.add(225);
                                                            if (C0124O00000oO.OOOOOo()) {
                                                                supportedConfigs.add(262);
                                                            }
                                                            supportedConfigs.add(219);
                                                            if (i8 == 0) {
                                                                break;
                                                            }
                                                            break;
                                                        case 188:
                                                            if (dataItemConfig.supportRatio()) {
                                                                supportedConfigs.add(210);
                                                            }
                                                            supportedConfigs.add(226);
                                                            supportedConfigs.add(225);
                                                            if (C0124O00000oO.OOOOOo()) {
                                                                supportedConfigs.add(262);
                                                            }
                                                            supportedConfigs.add(219);
                                                            if (i8 == 0) {
                                                                break;
                                                            }
                                                            break;
                                                        default:
                                                            switch (i7) {
                                                                case 209:
                                                                    break;
                                                                case 210:
                                                                    supportedConfigs.add(225);
                                                                    if (C0124O00000oO.OOOOOo()) {
                                                                        supportedConfigs.add(262);
                                                                        break;
                                                                    }
                                                                    break;
                                                                case 211:
                                                                    supportedConfigs.add(225);
                                                                    supportedConfigs.add(219);
                                                                    if (i8 == 0) {
                                                                        break;
                                                                    }
                                                                    break;
                                                                default:
                                                                    if (z) {
                                                                        if (dataItemConfig.supportRatio()) {
                                                                            supportedConfigs.add(210);
                                                                        }
                                                                        supportedConfigs.add(226);
                                                                        supportedConfigs.add(225);
                                                                        if (instance.isCinematicPhotoSupported()) {
                                                                            supportedConfigs.add(createConfigItem(251));
                                                                        }
                                                                        if (dataItemRunning.supportHandGesture()) {
                                                                            supportedConfigs.add(252);
                                                                        }
                                                                        if (C0124O00000oO.OOOOOo()) {
                                                                            supportedConfigs.add(262);
                                                                        }
                                                                        supportedConfigs.add(219);
                                                                        if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                                                                            supportedConfigs.add(229);
                                                                        }
                                                                        if (dataItemRunning.supportSuperMacroMode()) {
                                                                            if (instance.OOO0o0o()) {
                                                                                supportedConfigs.add(206);
                                                                            }
                                                                        } else if (dataItemRunning.supportMacroMode(i8, i7)) {
                                                                            supportedConfigs.add(255);
                                                                        }
                                                                        if (i8 == 0) {
                                                                            if (C0124O00000oO.Oo0()) {
                                                                                supportedConfigs.add(228);
                                                                            }
                                                                            if (C0124O00000oO.Oo0o0o()) {
                                                                                supportedConfigs.add(234);
                                                                            }
                                                                        }
                                                                        if (CameraSettings.checkLensAvailability(CameraAppImpl.getAndroidContext()) && i8 == 0) {
                                                                            supportedConfigs.add(242);
                                                                        }
                                                                        if (instance.supportColorEnhance() && cameraCapabilities.isSupportedColorEnhance() && i8 == 0) {
                                                                            supportedConfigs.add(227);
                                                                        }
                                                                        break;
                                                                    } else {
                                                                        if (dataItemConfig.supportRatio() && !DataRepository.dataItemGlobal().isIntentIDPhoto()) {
                                                                            supportedConfigs.add(210);
                                                                        }
                                                                        supportedConfigs.add(226);
                                                                        supportedConfigs.add(225);
                                                                        break;
                                                                    }
                                                            }
                                                    }
                                            }
                                        } else {
                                            supportedConfigs.add(226);
                                            supportedConfigs.add(225);
                                            supportedConfigs.add(219);
                                            if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                                                supportedConfigs.add(229);
                                            }
                                            if (dataItemRunning.supportHandGesture()) {
                                                supportedConfigs.add(252);
                                            }
                                            if (C0124O00000oO.OOOOOo()) {
                                                supportedConfigs.add(262);
                                            }
                                            if (i8 == 0 && C0124O00000oO.Oo0()) {
                                                i4 = 228;
                                            }
                                        }
                                    }
                                    supportedConfigs.add(229);
                                } else {
                                    i3 = 187;
                                }
                                return supportedConfigs;
                            }
                            if (dataItemConfig.getComponentConfigVideoQuality().isTwoComponent()) {
                                supportedConfigs.add(173);
                                i6 = 174;
                            } else {
                                i6 = 208;
                            }
                            supportedConfigs.add(i6);
                            supportedConfigs.add(225);
                            if (cameraCapabilities.isSupportedVideoLogFormat()) {
                                supportedConfigs.add(260);
                            }
                            if (cameraCapabilities.isSupportedVideoMiMovie()) {
                                supportedConfigs.add(251);
                            }
                            supportedConfigs.add(219);
                            if (C0124O00000oO.Oo00O0O()) {
                                supportedConfigs.add(229);
                            }
                            supportedConfigs.add(199);
                            i4 = 258;
                            supportedConfigs.add(i4);
                            return supportedConfigs;
                        }
                        supportedConfigs.add(208);
                        supportedConfigs.add(225);
                        if (cameraCapabilities.isSupportedVideoMiMovie()) {
                            supportedConfigs.add(251);
                        }
                        supportedConfigs.add(219);
                        if (i8 == 0 && C0124O00000oO.Oo00O0O()) {
                            supportedConfigs.add(229);
                        }
                        if (i8 == 0) {
                            if (dataItemRunning.supportMacroMode(i8, i7)) {
                            }
                        }
                        return supportedConfigs;
                        supportedConfigs.add(255);
                        return supportedConfigs;
                    }
                    if (dataItemConfig.supportRatio()) {
                        supportedConfigs.add(210);
                    }
                    supportedConfigs.add(226);
                    supportedConfigs.add(225);
                    if (C0122O00000o.instance().O0oo0O() && cameraCapabilities.isSupportRaw()) {
                        supportedConfigs.add(237);
                    }
                    if (C0124O00000oO.OOOOOo()) {
                        supportedConfigs.add(262);
                    }
                    supportedConfigs.add(219);
                    if (C0124O00000oO.Oo00O0O()) {
                        supportedConfigs.add(229);
                    }
                    if (C0124O00000oO.Oo00oO0()) {
                        supportedConfigs.add(199);
                    }
                    supportedConfigs.add(258);
                    i4 = 170;
                    supportedConfigs.add(i4);
                    return supportedConfigs;
                }
                supportedConfigs.add(225);
                supportedConfigs.add(219);
                return supportedConfigs;
            }
            if (dataItemConfig.getComponentConfigVideoQuality().isTwoComponent()) {
                supportedConfigs.add(173);
                i5 = 174;
            } else {
                i5 = 208;
            }
            supportedConfigs.add(i5);
            supportedConfigs.add(225);
            if (i8 == 0) {
                if (cameraCapabilities.isSupportedVideoMiMovie()) {
                    supportedConfigs.add(251);
                }
                supportedConfigs.add(219);
                if (C0124O00000oO.Oo00O0O()) {
                    supportedConfigs.add(229);
                }
                if (dataItemRunning.supportMacroMode(i8, i7)) {
                    supportedConfigs.add(255);
                }
                if (z && instance.OO0oOOo()) {
                    supportedConfigs.add(253);
                }
                if (z && cameraCapabilities.supportAiEnhancedVideo()) {
                    supportedConfigs.add(175);
                }
            } else if (i8 == 1) {
                if (cameraCapabilities.isSupportedVideoMiMovie()) {
                    supportedConfigs.add(251);
                }
                supportedConfigs.add(219);
            }
            if (instance.OOOOOoo()) {
            }
            return supportedConfigs;
            i4 = 220;
            supportedConfigs.add(i4);
            return supportedConfigs;
        }
        i3 = 208;
        supportedConfigs.add(i3);
        supportedConfigs.add(225);
        supportedConfigs.add(219);
        return supportedConfigs;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:106:0x01c3, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:114:0x01e5, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:137:0x025f, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:155:0x02b6, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x011d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:162:0x02d6, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:225:0x03e2, code lost:
        if (r19 == false) goto L_0x03e4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:233:0x03ff, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x011d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0077, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:66:0x010c, code lost:
        r6.add(r0);
        r0 = createConfigItem(251);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:68:0x011b, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x011d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:69:0x011d, code lost:
        r0 = r4.getComponentRunningShine().getTopConfigItem();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x0139, code lost:
        r6.add(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:78:0x014d, code lost:
        r0 = createConfigItem(245, 17);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x0157, code lost:
        r6.clear();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:85:0x0172, code lost:
        if (r4.supportTopShineEntry() != false) goto L_0x0079;
     */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c0  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00ea  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static final SupportedConfigs getSupportedTopConfigs(int i, int i2, boolean z) {
        TopConfigItem topConfigItem;
        TopConfigItem topConfigItem2;
        int topConfigItem3;
        int i3;
        int i4;
        TopConfigItem topConfigItem4;
        TopConfigItem topConfigItem5;
        int i5;
        int i6 = i;
        int i7 = i2;
        CameraCapabilities capabilitiesByBogusCameraId = Camera2DataContainer.getInstance().getCapabilitiesByBogusCameraId(i7, i6);
        if (capabilitiesByBogusCameraId == null) {
            return null;
        }
        DataItemConfig dataItemConfig = DataRepository.dataItemConfig();
        DataItemRunning dataItemRunning = DataRepository.dataItemRunning();
        C0122O00000o instance = C0122O00000o.instance();
        ArrayList arrayList = new ArrayList();
        if (dataItemConfig.supportFlash()) {
            arrayList.add(createConfigItem(193));
        }
        if (i6 != 161) {
            if (i6 == 162) {
                if (i7 == 0) {
                    if (C0122O00000o.instance().OO0oO0O()) {
                        arrayList.add(createConfigItem(168));
                    }
                    if (z && instance.OOOoO0o() && dataItemConfig.supportHdr()) {
                        arrayList.add(createConfigItem(194));
                    }
                    if (z && instance.OOOOo00()) {
                        arrayList.add(createConfigItem(instance.OOOOo0O() ? 165 : 218));
                    }
                    if (!z) {
                        if (capabilitiesByBogusCameraId.isSupportVideoFilter() && capabilitiesByBogusCameraId.isSupportVideoMasterFilter()) {
                            arrayList.add(createConfigItem(263));
                        }
                        if (dataItemRunning.supportTopShineEntry()) {
                            arrayList.add(createConfigItem(212, 17));
                        }
                    } else {
                        if (!instance.OOOoO0o() && dataItemConfig.supportHdr()) {
                            arrayList.add(createConfigItem(194));
                        }
                        if (capabilitiesByBogusCameraId.isSupportVideoFilter() && capabilitiesByBogusCameraId.isSupportVideoMasterFilter()) {
                            arrayList.add(createConfigItem(263));
                        }
                        if (dataItemRunning.supportTopShineEntry()) {
                            topConfigItem3 = 212;
                        }
                        topConfigItem = createConfigItem(197);
                        arrayList.add(topConfigItem);
                        return TopViewPositionArray.fillNotUseViewPosition(arrayList);
                    }
                } else {
                    if (z && dataItemConfig.supportHdr() && capabilitiesByBogusCameraId.isSupportVideoHdr()) {
                        arrayList.add(createConfigItem(194));
                    }
                    if (capabilitiesByBogusCameraId.isSupportVideoFilter() && capabilitiesByBogusCameraId.isSupportVideoMasterFilter()) {
                        arrayList.add(createConfigItem(263));
                    }
                    if (dataItemRunning.supportTopShineEntry()) {
                        arrayList.add(createConfigItem(dataItemRunning.getComponentRunningShine().getTopConfigItem(), 17));
                    }
                    if (z && i6 == 162 && instance.OOoOo0o()) {
                        arrayList.add(createConfigItem(243));
                    }
                }
                i3 = 225;
            } else if (i6 != 166) {
                if (i6 == 167) {
                    arrayList.add(createConfigItem(214));
                    if (z && dataItemRunning.supportUltraPixel()) {
                        arrayList.add(createConfigItem(209));
                    }
                } else if (i6 == 169) {
                    if (C0122O00000o.instance().OOO00Oo() && i7 == 0) {
                        arrayList.add(createConfigItem(214));
                    }
                    if (capabilitiesByBogusCameraId.isSupportVideoFilter() && capabilitiesByBogusCameraId.isSupportVideoMasterFilter()) {
                        arrayList.add(createConfigItem(263, 17));
                    }
                } else if (i6 != 254) {
                    if (i6 != 179) {
                        if (i6 == 180) {
                            if (C0122O00000o.instance().OO0oO0O()) {
                                arrayList.add(createConfigItem(168));
                            }
                            arrayList.add(createConfigItem(214));
                            ComponentManuallyFocus manuallyFocus = dataItemConfig.getManuallyFocus();
                            if (C0124O00000oO.Oo00oO0() && !manuallyFocus.getComponentValue(i6).equals(manuallyFocus.getDefaultValue(i6))) {
                                capabilitiesByBogusCameraId.isAFRegionSupported();
                            }
                            if (capabilitiesByBogusCameraId.isSupportVideoFilter() && capabilitiesByBogusCameraId.isSupportVideoMasterFilter()) {
                                arrayList.add(createConfigItem(263));
                            }
                        } else if (i6 == 204) {
                            boolean OOO000o = C0122O00000o.instance().OOO000o();
                            boolean ismDrawSelectWindow = CameraSettings.getDualVideoConfig().ismDrawSelectWindow();
                            if (OOO000o) {
                                if (!ismDrawSelectWindow || CameraSettings.isDualVideoRecording()) {
                                    i3 = 513;
                                } else {
                                    topConfigItem2 = createConfigItem(164);
                                    arrayList.add(topConfigItem2);
                                }
                            }
                            topConfigItem = createConfigItem(197);
                            arrayList.add(topConfigItem);
                            return TopViewPositionArray.fillNotUseViewPosition(arrayList);
                        } else if (i6 != 205) {
                            switch (i6) {
                                case 171:
                                    if (instance.isSupportUltraWide() && CameraSettings.isSupportedOpticalZoom() && i7 == 0 && z && instance.OOOOooO()) {
                                        arrayList.add(createConfigItem(207));
                                    }
                                    if (dataItemConfig.supportAi()) {
                                        arrayList.add(createConfigItem(201, 17));
                                    }
                                    break;
                                case 172:
                                case 173:
                                case 177:
                                    break;
                                case 174:
                                    if (z && i7 != 0 && instance.OOoOo0o()) {
                                        arrayList.add(createConfigItem(243));
                                        break;
                                    }
                                case 175:
                                    if (C0122O00000o.instance().OOOoooO()) {
                                        arrayList.add(createConfigItem(171));
                                    }
                                    break;
                                case 176:
                                    break;
                                default:
                                    switch (i6) {
                                        case 182:
                                            break;
                                        case 183:
                                            break;
                                        case 184:
                                            if (C0122O00000o.instance().OOo0O0()) {
                                                topConfigItem2 = createConfigItem(162, 17);
                                            }
                                            break;
                                        case 185:
                                            topConfigItem4 = createConfigItem(164);
                                            break;
                                        case 186:
                                            if (C0122O00000o.instance().OOO0OoO()) {
                                                topConfigItem2 = createConfigItem(166);
                                            }
                                            break;
                                        case 187:
                                            arrayList.clear();
                                            break;
                                        case 188:
                                            break;
                                        case 189:
                                            topConfigItem5 = createConfigItem(179);
                                            break;
                                        default:
                                            switch (i6) {
                                                case 207:
                                                case 213:
                                                    topConfigItem5 = createConfigItem(164);
                                                    break;
                                                case 208:
                                                case 212:
                                                    topConfigItem4 = createConfigItem(251, 17);
                                                    break;
                                                case 209:
                                                    arrayList.clear();
                                                    if (instance.OOO00o()) {
                                                        topConfigItem3 = 172;
                                                        break;
                                                    }
                                                    break;
                                                case 210:
                                                case 211:
                                                    break;
                                                case 214:
                                                    break;
                                                default:
                                                    if (z) {
                                                        if (dataItemConfig.supportHdr()) {
                                                            arrayList.add(createConfigItem(194));
                                                        }
                                                        if (dataItemConfig.supportAi()) {
                                                            arrayList.add(createConfigItem(201));
                                                        }
                                                        if (!dataItemRunning.supportSuperMacroMode()) {
                                                            if (instance.OOO0o0o()) {
                                                                i5 = 206;
                                                            }
                                                            if (dataItemRunning.supportTopShineEntry()) {
                                                                arrayList.add(createConfigItem(dataItemRunning.getComponentRunningShine().getTopConfigItem()));
                                                            }
                                                            if (dataItemRunning.supportUltraPixelPortrait() && capabilitiesByBogusCameraId.isSupportedSuperPortrait()) {
                                                                arrayList.add(createConfigItem(215));
                                                            }
                                                            if (dataItemConfig.supportBokeh()) {
                                                                i4 = 200;
                                                                break;
                                                            }
                                                        } else {
                                                            i5 = 255;
                                                        }
                                                        arrayList.add(createConfigItem(i5));
                                                        if (dataItemRunning.supportTopShineEntry()) {
                                                        }
                                                        arrayList.add(createConfigItem(215));
                                                        if (dataItemConfig.supportBokeh()) {
                                                        }
                                                        break;
                                                    } else {
                                                        break;
                                                    }
                                                    break;
                                            }
                                    }
                            }
                        } else {
                            if (dataItemConfig.supportHdr()) {
                                arrayList.add(createConfigItem(194));
                            }
                            if (dataItemConfig.supportAi()) {
                                arrayList.add(createConfigItem(201));
                            }
                        }
                    }
                    topConfigItem = createConfigItem(217);
                    arrayList.add(topConfigItem);
                    return TopViewPositionArray.fillNotUseViewPosition(arrayList);
                } else {
                    if (arrayList.isEmpty()) {
                        arrayList.add(new TopConfigItem(176));
                    } else {
                        arrayList.set(0, new TopConfigItem(176));
                    }
                    return TopViewPositionArray.fillNotUseViewPosition(arrayList);
                }
                i4 = dataItemRunning.getComponentRunningShine().getTopConfigItem();
                topConfigItem2 = createConfigItem(i4);
                arrayList.add(topConfigItem2);
                topConfigItem = createConfigItem(197);
                arrayList.add(topConfigItem);
                return TopViewPositionArray.fillNotUseViewPosition(arrayList);
            } else {
                arrayList.clear();
                if (instance.OOOO0Oo()) {
                    arrayList.add(createConfigItem(177));
                    topConfigItem2 = createConfigItem(169);
                    arrayList.add(topConfigItem2);
                }
                topConfigItem = createConfigItem(197);
                arrayList.add(topConfigItem);
                return TopViewPositionArray.fillNotUseViewPosition(arrayList);
            }
            topConfigItem = createConfigItem(i3);
            arrayList.add(topConfigItem);
            return TopViewPositionArray.fillNotUseViewPosition(arrayList);
        } else if (z && i7 != 0 && instance.OOoOo0o()) {
            arrayList.add(createConfigItem(243));
        }
        topConfigItem2 = createConfigItem(topConfigItem3, 17);
        arrayList.add(topConfigItem2);
        topConfigItem = createConfigItem(197);
        arrayList.add(topConfigItem);
        return TopViewPositionArray.fillNotUseViewPosition(arrayList);
    }

    public static boolean isMutexConfig(int i) {
        for (int i2 : MUTEX_MENU_CONFIGS) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }
}
