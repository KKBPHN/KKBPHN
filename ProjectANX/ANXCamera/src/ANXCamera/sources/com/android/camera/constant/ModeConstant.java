package com.android.camera.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ModeConstant {
    public static final int FROM_SETTING_SYSTEM_APP = 0;
    public static final int MODE_AI_WATERMARK = 205;
    public static final int MODE_CAPTURE = 163;
    public static final int MODE_CLONE = 185;
    public static final int MODE_DOC = 186;
    public static final int MODE_DUAL_VIDEO = 204;
    public static final int MODE_DUMMY_CLONE = 210;
    public static final int MODE_DUMMY_FILM = 211;
    public static final int MODE_DUMMY_VV = 209;
    public static final int MODE_EDIT = 255;
    public static final int MODE_FAST_MOTION = 169;
    public static final int MODE_FILM_DOLLY_ZOOM = 189;
    public static final int MODE_FILM_EXPOSUREDELAY = 208;
    public static final int MODE_FILM_PARALLELDREAM = 212;
    public static final int MODE_FILM_SLOWSHUTTER = 207;
    public static final int MODE_FILM_TIME_FREEZE = 213;
    public static final int MODE_FUN = 161;
    public static final int MODE_FUN2 = 181;
    public static final String MODE_FUN2_ALIAS = "com.android.camera.features.fun2.Fun2Module";
    public static final int MODE_FUN_AR = 177;
    public static final int MODE_FUN_AR2 = 184;
    public static final int MODE_ID_CARD = 182;
    public static final int MODE_LIVE = 174;
    public static final int MODE_LIVE_VV = 179;
    public static final int MODE_MANUAL = 167;
    public static final int MODE_MI_LIVE = 183;
    public static final int MODE_MORE = 254;
    public static final int MODE_NEW_SLOW_MOTION = 172;
    public static final int MODE_PANORAMA = 166;
    public static final int MODE_PIXEL = 175;
    public static final int MODE_PORTRAIT = 171;
    public static final int MODE_PRO_AMBILIGHT = 187;
    public static final int MODE_PRO_PHOTO = 167;
    public static final int MODE_PRO_VIDEO = 180;
    public static final int MODE_RECORD_VIDEO = 162;
    public static final int MODE_SLOW_MOTION = 168;
    public static final int MODE_SQUARE = 165;
    public static final int MODE_STANDALONE_MACRO = 178;
    public static final int MODE_SUPER_MOON = 188;
    public static final int MODE_SUPER_NIGHT = 173;
    public static final int MODE_SUPER_NIGHT_VIDEO = 214;
    public static final int MODE_UNSPECIFIED = 160;
    public static final int MODE_VIDEO_HFR = 170;
    public static final int MODE_WIDE_SELFIE = 176;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ConstantMode {
    }
}
