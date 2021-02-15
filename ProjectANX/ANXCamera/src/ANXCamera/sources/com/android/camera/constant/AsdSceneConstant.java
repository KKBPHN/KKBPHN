package com.android.camera.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AsdSceneConstant {
    public static final int SCENE_FLASH = 0;
    public static final int SCENE_FLASH_FRONT = 9;
    public static final int SCENE_HDR = 1;
    public static final int SCENE_MOTION = 3;
    public static final int SCENE_NIGHT = 2;
    public static final int SCENE_NONE = -1;
    public static final int SCENE_RTB_DEPTH_EFFECT_SUCCESS = 7;
    public static final int SCENE_RTB_LOW_LIGHT = 6;
    public static final int SCENE_RTB_TOO_CLOSE = 4;
    public static final int SCENE_RTB_TOO_FAR = 5;
    public static final int SCENE_TELE_NIGHT = 8;

    @Retention(RetentionPolicy.SOURCE)
    public @interface SceneResult {
    }
}
