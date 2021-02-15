package com.android.camera.constant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AlertConstant {
    public static final int ALERT_CLEAR = 1;
    public static final int ALERT_IMAGE_TYPE_FLASH_BACK_SOFT_LIGHT = 5;
    public static final int ALERT_IMAGE_TYPE_FLASH_ON = 1;
    public static final int ALERT_IMAGE_TYPE_FLASH_TORCH = 2;
    public static final int ALERT_IMAGE_TYPE_HDR_LIVE = 4;
    public static final int ALERT_IMAGE_TYPE_HDR_ON = 3;
    public static final int ALERT_IMAGE_TYPE_NULL = -1;
    public static final int ALERT_NOT_CLEAR = 2;
    public static final int FLASH_TIP_TYPE = 2;
    public static final int HDR_TIP_TYPE = 1;
    public static final int RECORD_STATE_PAUSE = 3;
    public static final int RECORD_STATE_RESUME = 4;
    public static final int RECORD_STATE_START = 1;
    public static final int RECORD_STATE_STOP = 2;
    public static final int RECORD_STATE_UNKNOWN = 0;
    public static final int UNKNOWN_TIP_TYPE = -1;
    public static final int VALUE_CHANGE_TO_BEAUTY = 2;
    public static final int VALUE_CHANGE_TO_BOKEH = 3;
    public static final int VALUE_CHANGE_TO_DEPTH = 4;
    public static final int VALUE_CHANGE_TO_MANUALLY_ET = 6;
    public static final int VALUE_CHANGE_TO_MANUALLY_EXPOSURE = 8;
    public static final int VALUE_CHANGE_TO_MANUALLY_FOCUS = 9;
    public static final int VALUE_CHANGE_TO_MANUALLY_ISO = 7;
    public static final int VALUE_CHANGE_TO_MANUALLY_WB = 5;
    public static final int VALUE_CHANGE_TO_RESET = 0;
    public static final int VALUE_CHANGE_TO_ZOOM = 1;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AlertCLearType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AlertImageType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RecordState {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ValueChangeType {
    }
}
