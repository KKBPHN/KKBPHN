package com.android.camera.statistic;

public interface MistatsConstants {
    public static final String TAG = "MistatsConstants";
    public static final long UPLOAD_POLICY_INTERVAL = 300000;

    public interface AIWatermark {
        public static final String AI_WATERMARK = "ai_watermark";
        public static final String AI_WATERMARK_AI = "ai_watermark_ai";
        public static final String AI_WATERMARK_CATEGORY = "ai_watermark_category";
        public static final String AI_WATERMARK_CLOSE = "ai_watermark_close";
        public static final String AI_WATERMARK_KEY = "ai_watermark_key";
        public static final String AI_WATERMARK_LIST_HIDE = "ai_watermark_list_hide";
        public static final String AI_WATERMARK_LIST_SHOW = "ai_watermark_list_show";
        public static final String AI_WATERMARK_MANUAL = "ai_watermark_manual";
        public static final String AI_WATERMARK_MOVE = "ai_watermark_move";
        public static final String AI_WATERMARK_OPEN = "ai_watermark_open";
        public static final String AI_WATERMARK_ORIENTATION = "ai_watermark_orientation";
        public static final String AI_WATERMARK_SELECT = "ai_watermark_select";
        public static final String AI_WATERMARK_TYPE = "ai_watermark_type";
    }

    public interface AlgoAttr {
        public static final String PARAM_AI_SCENE = "attr_aiScene";
        public static final String PARAM_BOKEN = "attr_BOKEH";
        public static final String PARAM_HDR = "attr_HDR";
        public static final String VALUE_HDR_OUT_BUTTON = "hdr_out_button";
        public static final String VAULE_AI_CC = "aiCC";
        public static final String VAULE_AI_SCENE = "aiScene";
        public static final String VAULE_START_AI_DETECT_BUTTON = "start_ai_detect_googleLens";
    }

    public interface Ambilight {
        public static final String KEY_AMBILIGHT = "key_ambilight";
        public static final String PARAM_AMBILIGHT_ABOUT_BUTTON_CLICK = "attr_ambilight_about_button_click";
        public static final String PARAM_AMBILIGHT_GENERATE_VIDEO = "attr_ambilight_generate_video";
        public static final String PARAM_AMBILIGHT_SCENE_MODE = "attr_ambilight_scene_mode";
    }

    public interface AutoHibernation {
        public static final String PARAM_AUTO_HIBERNATION = "attr_auto_hibernation";
        public static final String PARAM_AUTO_HIBERNATION_COUNT = "attr_auto_hibernation_count";
    }

    public interface AutoZoom {
        public static final String AUTOZOOM_LOST_10_MORE = "lost_more_10";
        public static final String AUTO_ZOOM_STATE = "attr_autozoom_state";
        public static final String KEY_AUTO_ZOOM = "key_auto_zoom";
        public static final String PARAM_SELECT_OBJECT_STATE = "attr_select_object_state";
        public static final String PARAM_TRACKING_LOST_OBJECT = "attr_tracking_lost_object";
        public static final String VALUE_AUTOZOOM_NOT_ULTRA = "not_ultra";
        public static final String VALUE_AUTOZOOM_ULTRA = "use_ultra";
        public static final String VALUE_BEFORE_RECORDING = "before_record";
        public static final String VALUE_IN_RECORDING = "in_recording";
    }

    public interface BaseEvent {
        public static final String ACCEPT = "accept";
        public static final String ADD = "add";
        public static final String AUTO = "auto";
        public static final String AUTO_OFF = "auto-off";
        public static final String AUTO_ON = "auto-on";
        public static final String AWB = "attr_awb";
        public static final String BACK = "back";
        public static final String CALCULATE = "calculate";
        public static final String CAMERA_CALLER = "camera_caller";
        public static final String CAPTURE = "capture";
        public static final String CLICK = "click";
        public static final String COST_TIME = "attr_cost_time";
        public static final String COUNT = "attr_count";
        public static final String CREATE = "create";
        public static final String DELETE = "delete";
        public static final String DESTORY = "destory";
        public static final String EDIT = "edit";
        public static final String EVENT_NAME = "attr_event_name";
        public static final String EXIT = "exit";
        public static final String FEATURE_NAME = "attr_feature_name";
        public static final String FRONT = "front";
        public static final String GESTURE = "gesture";
        public static final String HIGH = "high";
        public static final String ISO = "attr_iso";
        public static final String KEY_CAPTURE = "key_capture";
        public static final String KEY_VIDEO = "key_video";
        public static final String LIFE_STATE = "attr_life_state";
        public static final String LOW = "low";
        public static final String MODE = "attr_mode";
        public static final String MODULE_NAME = "attr_module_name";
        public static final String NONE = "none";
        public static final String NORMAL = "normal";
        public static final String OPERATE_STATE = "attr_operate_state";
        public static final String OTHERS = "others";
        public static final String PARAM_LYING_DIRECT = "attr_lying_direct";
        public static final String PARAM_PHOTO_RATIO_MOVIE = "attr_picture_ration_movie";
        public static final String PARAM_PICTURE_RATIO = "attr_picture_ration";
        public static final String PARAM_VIDEO_RATIO_MOVIE = "attr_video_ratio_movie";
        public static final String PAUSE = "pause";
        public static final String PHOTO = "photo";
        public static final String PICTURE_RESOLUTION = "attr_picture_resolution";
        public static final String PREVIEW = "preview";
        public static final String QUALITY = "attr_quality";
        public static final String QUALITY_1080P = "1080p";
        public static final String QUALITY_1080P_60FPS = "1080p-60fps";
        public static final String QUALITY_2160P = "4k";
        public static final String QUALITY_480P = "480p";
        public static final String QUALITY_4K_60FPS = "4k-60fps";
        public static final String QUALITY_720P = "720p";
        public static final String QUALITY_8K_30FPS = "8k-30fps";
        public static final String QUERY = "query";
        public static final String REJECT = "reject";
        public static final String RESET = "reset";
        public static final String RESUME = "resume";
        public static final String SAVE = "save";
        public static final String SCHEDULE = "schedule";
        public static final String SENSOR_ID = "attr_sensor_id";
        public static final String SLIDE = "slide";
        public static final String SLIDER = "slider";
        public static final String START = "start";
        public static final String STOP = "stop";
        public static final String SWITCH_STATE = "attr_switch_state";
        public static final String TO = "attr_to";
        public static final String TRIGGER_MODE = "attr_trigger_mode";
        public static final String UNSPECIFIED = "unspecified";
        public static final String VALUE = "attr_value";
        public static final String VALUE_AFTER = "after";
        public static final String VALUE_BEFOR = "before";
        public static final String VALUE_CANCEL = "cancel";
        public static final String VALUE_CLOSE = "close";
        public static final String VALUE_CONFIRM = "confirm";
        public static final String VALUE_DOING = "doing";
        public static final String VALUE_ERROR_MSG = "attr_error_msg";
        public static final String VALUE_FAILED = "failed";
        public static final String VALUE_FALSE = "false";
        public static final String VALUE_NOT_NULL = "not_null";
        public static final String VALUE_NULL = "null";
        public static final String VALUE_OFF = "off";
        public static final String VALUE_ON = "on";
        public static final String VALUE_OPEN = "open";
        public static final String VALUE_SUCCESS = "success";
        public static final String VALUE_TRUE = "true";
        public static final String VIDEO = "video";
        public static final String VOLUME = "volume";
    }

    public interface BeautyAttr {
        public static final String BEAUTY_BLUSHER = "blusher";
        public static final String BEAUTY_CHIN = "chin";
        public static final String BEAUTY_ENLARGE_EYE = "enlarge_eye";
        public static final String BEAUTY_EYEBROW_DYE = "eyebrow_dye";
        public static final String BEAUTY_JELLY_LIPS = "jelly_lips";
        public static final String BEAUTY_LEVEL = "beauty_level";
        public static final String BEAUTY_LIPS = "lips";
        public static final String BEAUTY_NECK = "neck";
        public static final String BEAUTY_NOSE = "nose";
        public static final String BEAUTY_PUPIL_LINE = "pupil_line";
        public static final String BEAUTY_RISORIUS = "risorius";
        public static final String BEAUTY_SKIN_COLOR = "skin_color";
        public static final String BEAUTY_SKIN_SMOOTH = "skin_smooth";
        public static final String BEAUTY_SLIM_FACE = "slim_face";
        public static final String BEAUTY_SLIM_NOSE = "slim_nose";
        public static final String BEAUTY_SMILE = "smile";
        public static final String BEAUTY_TYPE_FACE = "beauty_face";
        public static final String EYE_LIGHT = "eye_sparkle";
        public static final String HAIRLINE = "hairline";
        public static final String KEY_BEAUTY = "key_beauty";
        public static final String KEY_BEAUTY_CLICK = "key_beauty_click";
        public static final String KEY_BEAUTY_FACE = "key_beauty_face";
        public static final String KEY_BEAUTY_OLD = "key_beauty_old";
        public static final String PARAM_BEAUTY_BLUSHER = "attr_blusher";
        public static final String PARAM_BEAUTY_CHIN = "attr_chin";
        public static final String PARAM_BEAUTY_ENLARGE_EYE = "attr_enlarge_eye";
        public static final String PARAM_BEAUTY_EYEBROW_DYE = "attr_eyebrow_dye";
        public static final String PARAM_BEAUTY_JELLY_LIPS = "attr_jelly_lips";
        public static final String PARAM_BEAUTY_LEVEL = "attr_beauty_level";
        public static final String PARAM_BEAUTY_LIPS = "attr_lips";
        public static final String PARAM_BEAUTY_MAKEUP_SWITCH = "attr_beauty_makeup_switch";
        public static final String PARAM_BEAUTY_NECK = "attr_neck";
        public static final String PARAM_BEAUTY_NOSE = "attr_nose";
        public static final String PARAM_BEAUTY_PUPIL_LINE = "attr_pupil_line";
        public static final String PARAM_BEAUTY_RISORIUS = "attr_risorius";
        public static final String PARAM_BEAUTY_SKIN_COLOR = "attr_skin_color";
        public static final String PARAM_BEAUTY_SKIN_SMOOTH = "attr_skin_smooth";
        public static final String PARAM_BEAUTY_SLIM_FACE = "attr_slim_face";
        public static final String PARAM_BEAUTY_SLIM_NOSE = "attr_slim_nose";
        public static final String PARAM_BEAUTY_SMILE = "attr_smile";
        public static final String PARAM_BEAUTY_SWITCH = "attr_beauty_switch";
        public static final String PARAM_BEAUTY_TYPE = "attr_beauty_type";
        public static final String PARAM_EYE_LIGHT = "attr_eye_sparkle";
        public static final String PARAM_HAIRLINE = "attr_hairline";
        public static final String VALUE_BEAUTY_BOTTOM_TAB = "beauty_bottom_tab";
        public static final String VALUE_BEAUTY_FACE_CLEAR = "beauty_face_clear";
        public static final String VALUE_BEAUTY_FACE_RESET = "beauty_face_reset";
        public static final String VALUE_BEAUTY_SHOW_BOTTOM_BUTTON = "beauty_show_bottom_button";
        public static final String VALUE_BEAUTY_TOP_BUTTON = "beauty_top_button";
        public static final String VALUE_BEAUTY_V1_BOTTOM_TAB = "beauty_v1_bottom_tab";
        public static final String VALUE_BOKEH_BOTTOM_TAB = "bokeh_bottom_tab";
        public static final String VALUE_MAKEUP_BOTTOM_TAB = "makeup_bottom_tab";
        public static final String VALUE_MAKEUP_CLEAR = "makeup_clear";
        public static final String VALUE_MAKEUP_RESET = "makeup_reset";
    }

    public interface BeautyBodySlimAttr {
        public static final String BEAUTY_HEAD_SLIM = "head_slim";
        public static final String BEAUTY_LEG_SLIM = "leg_slim";
        public static final String BEAUTY_PORT = "attr_port";
        public static final String BEAUTY_SHOULDER_SLIM = "shoulder_slim";
        public static final String BEAUTY_TYPE_BODY_SLIM = "body_slim";
        public static final String BEAUTY_WHOLE_BODY_SLIM = "whole_body_slim";
        public static final String BUTT_SLIM = "butt_slim";
        public static final String EIGHTTY = "80+";
        public static final String FIFTY = "50+";
        public static final String FOURTY = "40+";
        public static final String KEY_BODY_SLIM = "key_body_slim";
        public static final String NINETY = "90+";
        public static final String ONE = "1+";
        public static final String PARAM_BEAUTY_BODY_SLIM = "attr_body_slim";
        public static final String PARAM_BEAUTY_HEAD_SLIM = "attr_head_slim";
        public static final String PARAM_BEAUTY_LEG_SLIM = "attr_leg_slim";
        public static final String PARAM_BEAUTY_SHOULDER_SLIM = "attr_shoulder_slim";
        public static final String PARAM_BEAUTY_WHOLE_BODY_SLIM = "attr_whole_body_slim";
        public static final String PARAM_BUTT_SLIM = "attr_butt_slim";
        public static final String SEVENTY = "70+";
        public static final String SIXTY = "60+";
        public static final String TEN = "10+";
        public static final String THIRDTY = "30+";
        public static final String TWENTY = "20+";
        public static final String ZERO = "0+";
    }

    public interface CUSTOMIZE_CAMERA {
        public static final String KEY_SETTING_CLICK = "key_customize_camera";
        public static final String PARAM_CUSTOM_CAMERA = "attr_custom_camera";
        public static final String PREF_KEY_CUSTOM_MODE_ICON = "attr_edit_mode_icon";
        public static final String PREF_KEY_CUSTOM_MODE_SETTING = "attr_edit_mode_setting";
        public static final String PREF_KEY_CUSTOM_SOUND = "attr_edit_sound";
        public static final String PREF_KEY_CUSTOM_TINT = "attr_edit_tint";
        public static final String PREF_KEY_MORE_MODE = "attr_more_mode";
    }

    public interface CaptureAttr {
        public static final String PARAM_ASD_DETECT_TIP = "attr_asd_detect_tip";
        public static final String PARAM_ASD_FLAW_TIP = "attr_asd_flaw_tip";
        public static final String PARAM_NEAR_RANGE_MODE = "attr_near_range_mode";
        public static final String PARAM_TIMER = "attr_timer";
        public static final String PARAM_TIMER_CHANGED = "attr_timer_changed";
        public static final String VALUE_ASD_BACKLIT_TIP = "asd_backlit_tip";
        public static final String VALUE_ASD_DIRTY_TIP = "asd_dirty_tip";
        public static final String VALUE_ASD_DOCUMENT_BLUR_TIP = "asd_document_blur_tip";
        public static final String VALUE_ASD_FLAW_BLINK_MORE = "asd_flaw_blink_more";
        public static final String VALUE_ASD_FLAW_BLINK_ONE = "asd_flaw_blink_one";
        public static final String VALUE_ASD_FLAW_COVER = "asd_flaw_cover";
        public static final String VALUE_ASD_MACRO_TIP = "asd_macro_tip";
        public static final String VALUE_ASD_PORTRAIT_TIP = "asd_portrait_tip";
        public static final String VALUE_ASD_SUPER_NIGHT_TIP = "asd_super_night_tip";
        public static final String VALUE_CLOSE_NEAR_RANGE_MODE = "close_near_range_mode";
    }

    public interface CaptureSence {
        public static final String AUDIO_CAPTURE = "audio_capture";
        public static final String BURST = "burst";
        public static final String EXPOSURE_VIEW = "exposure_view";
        public static final String FINGERPRINT = "fingerprint";
        public static final String FOCUS_SHOOT = "focus_shoot";
        public static final String HAND_GESTURE = "hand_gesture";
        public static final String KEYCODE_CAMERA = "keycode_camera";
        public static final String KEYCODE_DPAD = "keycode_dpad";
        public static final String KEY_BURST_SHOT_TIMES = "key_burst_shot_times";
        public static final String KEY_SNAP_CAMERA = "key_snap_camera";
        public static final String OBJECT_TRACK = "object_track";
        public static final String PARAM_BURST_COUNT = "attr_burst_count";
        public static final String PARAM_PALM_SHUTTER = "attr_palm_shutter";
        public static final String PARAM_TIMER_BURST = "attr_timer_burst";
        public static final String SHUTTER_BUTTON = "shutter_button";
        public static final String SNAP = "snap";
        public static final String SPEECH_SHUTTER = "speech_shutter";
        public static final String VOLUME = "volume";
    }

    public interface CloneAttr {
        public static final String ATTR_CLONE_MODE = "attr_clone_capture_mode";
        public static final String ATTR_FREEZE_FRAME_CAPTURE_HINT = "attr_freeze_frame_capture_hint";
        public static final String ATTR_FREEZE_FRAME_SUBJECT_COUNT = "attr_freeze_frame_subject_count";
        public static final String ATTR_FREEZE_FRAME_VIDEO_DURATION = "attr_freeze_frame_video_duration";
        public static final String ATTR_PHOTO_CAPTURE_HINT = "attr_clone_photo_capture_hint";
        public static final String ATTR_PHOTO_SUBJECT_COUNT = "attr_photo_subject_count";
        public static final String ATTR_VIDEO_CAPTURE_HINT = "attr_clone_video_capture_hint";
        public static final String ATTR_VIDEO_DURATION = "attr_video_duration";
        public static final String KEY_CLONE = "key_clone";
        public static final String VALUE_ADJUST_CLICK = "value_adjust_click";
        public static final String VALUE_BACK_CLICK = "value_clone_click_back";
        public static final String VALUE_CANCEL_CLICK = "value_clone_click_cancel";
        public static final String VALUE_CANCEL_EDIT_CLICK = "value_cancel_edit_click";
        public static final String VALUE_CLONE_ICON_CLICK = "value_clone_enter_clone_click";
        public static final String VALUE_DRAG_SUBJECT_CLICK = "value_drag_subject_click";
        public static final String VALUE_FREEZE_FRAME_GUIDE_CLICK = "value_clone_click_freeze_frame_guide";
        public static final String VALUE_GIVEUP_CLICK = "value_clone_click_giveup";
        public static final String VALUE_PHOTO_GUIDE_CLICK = "value_clone_click_photo_guide";
        public static final String VALUE_RESET_EDIT_CLICK = "value_reset_edit_click";
        public static final String VALUE_SAVE_EDIT_CLICK = "value_save_edit_click";
        public static final String VALUE_SHARE_CLICK = "value_clone_click_share";
        public static final String VALUE_START_FREEZE_FRAME_CLICK = "value_clone_click_start_freeze_frame";
        public static final String VALUE_START_PHOTO_CLICK = "value_clone_click_start_photo";
        public static final String VALUE_START_VIDEO_CLICK = "value_clone_click_start_video";
        public static final String VALUE_STOP_CAPTURE_CLICK = "value_clone_click_stop_capture";
        public static final String VALUE_VIDEO_GUIDE_CLICK = "value_clone_click_video_guide";
    }

    public interface CostTime {
        public static final String KEY_CAMERA_PERPORMANCE = "key_camera_performance";
        public static final String START_APP_COST = "start_app_cost";
        public static final String TAKE_PICTURE_COST = "take_picture_cost";
    }

    public interface DualVideoAttr {
        public static final String ATTR_LAYOUT_TYPE = "attr_compose_type";
        public static final String ATTR_VIDEO_DURATION = "attr_video_duration";
        public static final String KEY_DUAL_VIDEO = "key_front_back";
        public static final String VALUE_PREVIEW_EQUAL = "value_preview_equal";
        public static final String VALUE_PREVIEW_MINI = "value_preview_mini";
    }

    public interface EditMode {
        public static final String KEY_CAMERA_MODE_EDIT = "key_camera_mode_edit";
        public static final String KEY_COMMON_MODES_AFTER_EDIT = "key_common_modes_after_edit";
        public static final String KEY_MORE_MODES_AFTER_EDIT = "key_more_modes_after_edit";
        public static final String PARAM_COMMON_MODE_COUNT_AFTER_EDIT = "attr_common_mode_count_after_edit";
        public static final String PARAM_COMMON_MODE_FULL = "attr_common_mode_full";
        public static final String PARAM_MORE_MODE_COUNT_AFTER_EDIT = "attr_more_mode_count_after_edit";
        public static final String VALUE_EDIT_MODE_CLICK_CONFIRM = "value_edit_mode_click_confirm";
        public static final String VALUE_EDIT_MODE_CLICK_EXIT = "value_edit_mode_click_exit";
        public static final String VALUE_EDIT_MODE_CLICK_EXIT_CANCEL = "value_edit_mode_click_exit_cancel";
        public static final String VALUE_EDIT_MODE_CLICK_EXIT_CONFIRM = "value_edit_mode_click_exit_confirm";
    }

    public interface Error {
        public static final String CAMERA_BROADCAST_KILL_SERVICE = "camera_broadcast_kill_service";
        public static final String CAMERA_ERROR_DIALOG_SHOW = "camera_error_dialog_show";
        public static final String CAMERA_HARDWARE_ERROR = "camera_hardware_error";
        public static final String KEY_CAMERA_EXCEPTION = "key_camera_exception";
    }

    public interface Fastmotion {
        public static final String PARAM_MANUAL_ADJUST = "param_manual_adjust";
        public static final String PARAM_SPEED_DURATION = "param_speed_duration";
    }

    public interface FeatureAttr {
        public static final String KEY_FEATURE = "key_feature";
        public static final String KEY_FEATURE_INSTALL_CANCEL_CONFIRM = "attr_feature_install_cancel_conform";
        public static final String KEY_FEATURE_INSTALL_CANCEL_SHOW = "attr_feature_install_cancel_show";
        public static final String KEY_FEATURE_INSTALL_CELLULAR_CONFIRM = "attr_feature_install_cellular_confirm";
        public static final String KEY_FEATURE_INSTALL_CELLULAR_SHOW = "attr_feature_install_cellular_show";
        public static final String KEY_FEATURE_INSTALL_ERROR = "attr_feature_install_error";
        public static final String KEY_FEATURE_INSTALL_FROM = "attr_feature_install_from";
        public static final String KEY_FEATURE_INSTALL_RESULT = "attr_feature_install_result";
        public static final String KEY_FEATURE_INSTALL_WIFI = "attr_feature_install_use_wifi";
        public static final String KEY_FEATURE_NAME_VERSION = "attr_feature_name_version";
    }

    public interface FeatureName {
        public static final String KEY_COMMON = "key_common";
        public static final String KEY_COMMON_TIPS = "key_common_tips";
        public static final String VALUE_8k_VIDEO_FPS = "8K_video_fps";
        public static final String VALUE_AI_AUDIO = "attr_ai_audio";
        public static final String VALUE_AI_AUDIO_ZOOM = "attr_ai_audio_zoom";
        public static final String VALUE_AI_DETECT_CHANGED = "ai_detect_changed";
        public static final String VALUE_CTA_AGREE = "attr_cta_agree";
        public static final String VALUE_CTA_DISAGREE = "attr_cta_disagree";
        public static final String VALUE_DOCUMENT_MODE = "value_document_mode";
        public static final String VALUE_EDIT_MODE = "edit_mode";
        public static final String VALUE_EV_ADJUSTED = "ev_adjusted";
        public static final String VALUE_GENDER_AGE = "gender_age";
        public static final String VALUE_GOTO_GALLERY = "goto_gallery";
        public static final String VALUE_GOTO_ID_CARD = "goto_id_card";
        public static final String VALUE_GOTO_SETTINGS = "goto_settings";
        public static final String VALUE_GOTO_TIMER_BURST_MENU = "goto_timer_burst_menu";
        public static final String VALUE_GRADIENTER = "gradienter";
        public static final String VALUE_HHT = "HHT";
        public static final String VALUE_MAGIC_MIRROR = "magic_mirror";
        public static final String VALUE_MENU_MORE = "menu_more";
        public static final String VALUE_METER_ICON_CLICK = "meter_icon_click";
        public static final String VALUE_PHOTO_ASSISTANCE_TIP_CLICK = "photo_assistance_click";
        public static final String VALUE_QRCODE_DETECTED = "qrcode_detected";
        public static final String VALUE_REFERENCE_LINE = "reference_line";
        public static final String VALUE_SUBTITLE = "attr_video_subtitle";
        public static final String VALUE_SUBTITLE_NETWORK_INTERRUPTION = "subtitle_network_interruption";
        public static final String VALUE_SUBTITLE_RECORDING = "attr_subtitle_recording";
        public static final String VALUE_SUBTITLE_START_NO_NETWORK = "subtitle_start_no_network";
        public static final String VALUE_SUBTITLE_START_RECIRDING = "subtitle_start_recording";
        public static final String VALUE_SUPER_EIS_PRO = "super_eis_pro";
        public static final String VALUE_SUPER_MOON_CAPTURE = "super_moon_capture";
        public static final String VALUE_T2T_TIMES = "value_t2t_times";
        public static final String VALUE_TILTSHIFT = "tiltshift";
        public static final String VALUE_TOPMENU_GRADIENTER = "topmenu_gradienter";
        public static final String VALUE_TRIGGER_SUBTITLE = "trigger_subtitle";
    }

    public interface FilmAttr {
        public static final String ATTR_TIME_FREEZE_CAPTURE_HINT = "attr_time_freeze_capture_hint";
        public static final String KEY_FILM = "key_film";
        public static final String KEY_FILM_DOLLY_ZOOM = "key_film_dolly_zoom";
        public static final String KEY_FILM_DREAM = "key_film_dream";
        public static final String KEY_TIME_FREEZE = "key_film_time_freeze";
        public static final String PARAM_DOLLY_ZOOM_EXCEPTION_ZOOM_VALUE = "attr_operate_exception_zoom_value";
        public static final String PARAM_DOLLY_ZOOM_NORMAL_ZOOM_VALUE = "attr_operate_normal_zoom_value";
        public static final String PARAM_FILM_CLICK_TEMPLATE_PREVIEW = "attr_film_click_template_preview";
        public static final String PARAM_FILM_TEMPLATE_NAME = "attr_film_template_name";
        public static final String PARAM_FILM_USEGUIDE_CLICK = "attr_film_useguide_click";
        public static final String PARAM_TIME_FREEZE_BEFORE_RECORDING = "attr_time_freeze_before_recording";
        public static final String VALUE_DOLLY_ZOOM_CLICK_EXIT_CONFIRM = "value_dolly_zoom_exit_confirm";
        public static final String VALUE_DOLLY_ZOOM_CLICK_EXIT_PREVIEW = "value_dolly_zoom_exit_preview";
        public static final String VALUE_DOLLY_ZOOM_CLICK_PLAY_CANCEL = "value_dolly_zoom_click_play_cancel";
        public static final String VALUE_DOLLY_ZOOM_CLICK_PLAY_SAVE = "value_dolly_zoom_click_play_save";
        public static final String VALUE_DOLLY_ZOOM_CLICK_PLAY_SHARE = "value_dolly_zoom_click_play_share";
        public static final String VALUE_DOLLY_ZOOM_CLICK_START_RECORD = "value_dolly_zoom_start_record";
        public static final String VALUE_DOLLY_ZOOM_STATE_FRAMER_OUT = "value_dolly_zoom_state_frame_out";
        public static final String VALUE_DOLLY_ZOOM_STATE_TARGET_LOST = "value_dolly_zoom_state_target_lost";
        public static final String VALUE_FILM_DREAM_CLICK_EXIT_CONFIRM = "value_film_dream_exit_confirm";
        public static final String VALUE_FILM_DREAM_CLICK_EXIT_PREVIEW = "value_film_dream_exit_preview";
        public static final String VALUE_FILM_DREAM_CLICK_PLAY_SAVE = "value_film_dream_click_play_save";
        public static final String VALUE_FILM_DREAM_CLICK_PLAY_SHARE = "value_film_dream_click_play_share";
        public static final String VALUE_FILM_DREAM_CLICK_PLAY_SHARE_CANCEL = "value_film_dream_click_play_share_cancel";
        public static final String VALUE_FILM_DREAM_CLICK_START_RECORD = "value_film_dream_start_record";
        public static final String VALUE_FILM_DREAM_CLICK_STOP_RECORD = "value_film_dream_stop_record";
        public static final String VALUE_TIME_FREEZE_CLICK_EXIT_CONFIRM = "value_time_freeze_exit_confirm";
        public static final String VALUE_TIME_FREEZE_CLICK_EXIT_PREVIEW = "value_time_freeze_exit_preview";
        public static final String VALUE_TIME_FREEZE_CLICK_FREEZE = "value_time_freeze_click_freeze";
        public static final String VALUE_TIME_FREEZE_CLICK_PLAY_SAVE = "value_time_freeze_click_play_save";
        public static final String VALUE_TIME_FREEZE_CLICK_PLAY_SHARE = "value_time_freeze_click_play_share";
        public static final String VALUE_TIME_FREEZE_CLICK_PLAY_SHARE_CANCEL = "value_time_freeze_click_play_share_cancel";
        public static final String VALUE_TIME_FREEZE_CLICK_RESET = "value_time_freeze_click_reset";
        public static final String VALUE_TIME_FREEZE_CLICK_START_RECORD = "value_time_freeze_start_record";
        public static final String VALUE_TIME_FREEZE_CLICK_STOP_RECORD = "value_time_freeze_stop_record";
    }

    public interface FilterAttr {
        public static final String A10_NIGHT = "A10_NIGHT";
        public static final String A11_SNOW = "A11_SNOW";
        public static final String A12_SEA = "A12_SEA";
        public static final String A13_AUTUMN = "A13_AUTUMN";
        public static final String A14_CANDLELIGHT = "A14_CANDLELIGHT";
        public static final String A15_CAR = "A15_CAR";
        public static final String A16_GRASS = "A16_GRASS";
        public static final String A17_MAPLE_LEAVES = "A17_MAPLE_LEAVES";
        public static final String A18_SUCCULENT = "A18_SUCCULENT";
        public static final String A19_BUILDING = "A19_BUILDING";
        public static final String A1_DOC = "A1_DOC";
        public static final String A20_CITY = "A20_CITY";
        public static final String A21_CLOUD = "A21_CLOUD";
        public static final String A22_OVERCAST = "A22_OVERCAST";
        public static final String A23_BACKLIGHT = "A23_BACKLIGHT";
        public static final String A24_SILHOUETTE = "A24_SILHOUETTE";
        public static final String A25_HUMAN = "A25_HUMAN";
        public static final String A26_JEWELRY = "A26_JEWELRY";
        public static final String A27_BUDDHA = "A27_BUDDHA";
        public static final String A28_COW = "A28_COW";
        public static final String A29_CURRY = "A29_CURRY";
        public static final String A2_FLOWER = "A2_FLOWER";
        public static final String A30_MOTORBIKE = "A30_MOTORBIKE";
        public static final String A31_TEMPLE = "A31_TEMPLE";
        public static final String A32_BEACH = "A32_BEACH";
        public static final String A33_DRIVING = "A33_DRIVING";
        public static final String A3_FOOD = "A3_FOOD";
        public static final String A4_PPT = "A4_PPT";
        public static final String A5_SKY = "A5_SKY";
        public static final String A6_SUNRISE_SUNSET = "A6_SUNRISE_SUNSET";
        public static final String A7_CAT = "A7_CAT";
        public static final String A8_DOG = "A8_DOG";
        public static final String A9_GREEN_PLANTS = "A9_GREEN_PLANTS";
        public static final String BI_MEMORY = "bi_memory";
        public static final String BI_MONO = "bi_mono";
        public static final String BI_M_DUSK = "bi_m_dusk";
        public static final String BI_M_LILT = "bi_m_lilt";
        public static final String BI_M_SEPIA = "bi_m_sepia";
        public static final String BI_M_TEA = "bi_m_tea";
        public static final String BI_M_WHITEANDBLACK = "bi_m_whiteandblack";
        public static final String BI_PINK = "bi_pink";
        public static final String BI_PORTRAIT = "bi_portrait";
        public static final String BI_RETRO = "bi_retro";
        public static final String BI_ROMANTIC = "bi_remantic";
        public static final String BI_STRONG = "bi_strong";
        public static final String BI_SUNNY = "bi_sunny";
        public static final String BI_SWEET = "bi_sweet";
        public static final String BI_WARM = "bi_warm";
        public static final String BI_YOUNG = "bi_young";
        public static final String B_FAIRYTALE = "b_fairytale";
        public static final String B_JAPANESE = "b_japanese";
        public static final String B_MAZE = "b_maze";
        public static final String B_MINT = "b_mint";
        public static final String B_MOOD = "b_mood";
        public static final String B_MOVIE = "b_movie";
        public static final String B_M_LILT = "b_m_lilt";
        public static final String B_M_SEPIA = "b_m_sepia";
        public static final String B_M_TEA = "b_m_tea";
        public static final String B_M_WHITEANDBLACK = "b_m_whiteandblack";
        public static final String B_NATURE = "b_nature";
        public static final String B_PINK = "b_pink";
        public static final String B_RIDDLE = "b_riddle";
        public static final String B_ROMANCE = "b_romance";
        public static final String B_STORY = "b_story";
        public static final String B_WHITEANDBLACK = "b_whiteAndBlack";
        public static final String KEY_FILTER_CHANGED = "key_filter_changed";
        public static final String ML_BLUE = "ml_blue";
        public static final String ML_CONTRAST = "ml_contrast";
        public static final String ML_DEEPBLACK = "ml_deepblack";
        public static final String ML_FAIR = "ml_fair";
        public static final String ML_HONGKONG = "ml_hongkong";
        public static final String ML_MOUSSE = "ml_mousse";
        public static final String ML_SOLAR = "ml_solar";
        public static final String ML_YEARS = "ml_years";
        public static final String N_BERRY = "n_berry";
        public static final String N_BLACKGOLD = "n_blackgold";
        public static final String N_BLACKICE = "n_blackice";
        public static final String N_CLASSIC = "n_classic";
        public static final String N_COOKIE = "n_cookie";
        public static final String N_DELICACY = "n_delicacy";
        public static final String N_FADE = "n_fade";
        public static final String N_FILM = "n_film";
        public static final String N_FIRST = "n_first";
        public static final String N_KOIZORA = "n_koizora";
        public static final String N_LATTE = "n_latte";
        public static final String N_LIGHT = "n_light";
        public static final String N_LIVELY = "n_lively";
        public static final String N_ORANGE = "n_orange";
        public static final String N_QUIET = "n_quiet";
        public static final String N_SIBOPENK = "n_sibopenk";
        public static final String N_SODA = "n_soda";
        public static final String N_TRAVAEL = "n_travel";
        public static final String N_WARM = "n_warm";
        public static final String N_WHITEANDBLACK = "n_whiteAndBlack";
        public static final String PARAM_FILTER = "attr_filter";
        public static final String S_BYGONE = "s_bygone";
        public static final String S_FILM = "s_film";
        public static final String S_FOREST = "s_forest";
        public static final String S_POLAROID = "s_polaroid";
        public static final String S_WHITEANDBLACK = "s_whiteAndBlack";
        public static final String S_YEARS = "s_years";
        public static final String VALUE_FILTER_BOTTOM_TAB = "filter_bottom_tab";
    }

    public interface FlashAttr {
        public static final String FLASH_VALUE_SCREEN_LIGHT_AUTO = "screen-light-auto";
        public static final String FLASH_VALUE_SCREEN_LIGHT_ON = "screen-light-on";
        public static final String PARAM_FLASH_MODE = "attr_flash_mode";
        public static final String VALUE_FLASH_OUT_BUTTON = "flash_out_button";
    }

    public interface GoogleLens {
        public static final String GOOGLE_LENS_PICKER = "google_lens_picker";
        public static final String GOOGLE_LENS_TOUCH_AND_HOLD = "google_lens_touch_and_hold";
        public static final String KEY_GOOGLE_LENS = "key_google_lens";
        public static final String KEY_GOOGLE_LENS_OOBE_CONTINUE = "key_google_lens_oobe_continue";
        public static final String PARAM_OOBE_CONTINUE_CLICK = "attr_Google_OOBE_continue_button_click";
        public static final String PARAM_PICK_WHICH = "attr_longPress_preview_dialog_pick";
        public static final String VALUE_GOOGLE_LENS = "google_lens";
        public static final String VALUE_LOCK_AEAF = "lock_AE/AF";
    }

    public interface Live {
        public static final String KEY_LIVE_BEAUTY = "key_live_beauty";
        public static final String KEY_LIVE_VIDEO_COMPLETE = "key_live_video_complete";
        public static final String KEY_LIVE_VIDEO_SEGMENT = "key_live_video_segment";
        public static final String LIVE_CLICK_PAUSE = "live_pause";
        public static final String LIVE_CLICK_RESUME = "live_resume";
        public static final String LIVE_CLICK_START = "live_start";
        public static final String LIVE_STICKER_MORE = "key_see_more";
        public static final String PARAM_LIVE_BEAUTY_ON = "attr_beauty_on";
        public static final String PARAM_LIVE_BEAUTY_PORT = "attr_live_beauty_port";
        public static final String PARAM_LIVE_BEAUTY_SEGMENT_ON = "attr_live_beauty_segment_on";
        public static final String PARAM_LIVE_BEAUTY_TYPE = "attr_live_beautyType";
        public static final String PARAM_LIVE_ENLARGE_EYE_RATIO = "attr_enlarge_eye_ratio";
        public static final String PARAM_LIVE_FILTER_NAME = "attr_filter_name";
        public static final String PARAM_LIVE_FILTER_ON = "attr_filter_on";
        public static final String PARAM_LIVE_FILTER_SEGMENT_ON = "attr_filter_segment_on";
        public static final String PARAM_LIVE_MUSIC_NAME = "attr_live_music_name";
        public static final String PARAM_LIVE_MUSIC_ON = "attr_live_music_on";
        public static final String PARAM_LIVE_RECORD_SEGMENTS = "attr_record_segments";
        public static final String PARAM_LIVE_RECORD_TIME = "attr_record_time";
        public static final String PARAM_LIVE_SHRINK_FACE_RATIO = "attr_shrink_face_ratio";
        public static final String PARAM_LIVE_SMOOTH_RATIO = "attr_smooth_ratio";
        public static final String PARAM_LIVE_SPEED_LEVEL = "attr_live_speed_level";
        public static final String PARAM_LIVE_STICKER_DOWNLOAD = "attr_live_download";
        public static final String PARAM_LIVE_STICKER_NAME = "attr_live_sticker_name";
        public static final String PARAM_LIVE_STICKER_ON = "attr_sticker_on";
        public static final String PARAM_LIVE_STICKER_SEGMENT_ON = "attr_sticker_segment_on";
        public static final String VALUE_LIVE_CLICK_PLAY_EXIT = "exit";
        public static final String VALUE_LIVE_CLICK_PLAY_EXIT_CONFIRM = "live_exit";
        public static final String VALUE_LIVE_CLICK_PLAY_SAVE = "live_save";
        public static final String VALUE_LIVE_CLICK_PLAY_SHARE = "live_share";
        public static final String VALUE_LIVE_CLICK_PLAY_SHARE_SHEET = "live_preview_shareTo_";
        public static final String VALUE_LIVE_CLICK_REVERSE = "live_reverse";
        public static final String VALUE_LIVE_CLICK_REVERSE_CONFIRM = "live_reverse_confirm";
        public static final String VALUE_LIVE_CLICK_SWITCH = "live_swith_camera";
        public static final String VALUE_LIVE_ENLARGE_EYE_RATIO = "live_enlarge_eye_ratio";
        public static final String VALUE_LIVE_MUSIC_ICON_CLICK = "feature_live_music_click";
        public static final String VALUE_LIVE_SHRINK_FACE_RATIO = "live_shrink_face_ratio";
        public static final String VALUE_LIVE_SMOOTH_RATIO = "live_smooth_ratio";
        public static final String VALUE_LIVE_SPEED = "live_speed";
        public static final String VALUE_LIVE_STICKER = "live_sticker";
        public static final String VALUE_LIVE_STICKER_APP = "live_app";
        public static final String VALUE_LIVE_STICKER_MARKET = "market";
        public static final String VALUE_LIVE_STICKER_OFF = "live_sticker_off";
    }

    public interface LiveShotAttr {
        public static final String FUCNAME_LIVESHOT_MODE = "key_liveshot_mode";
        public static final String PARAM_LIVESHOT = "attr_liveshot";
        public static final String VALUE_TOPMENU_LIVESHOT_CLICK = "liveshot_topmenu_click";
    }

    public interface MacroAttr {
        public static final String FUCNAME_MACRO_MODE = "key_macro_mode";
        public static final String PARAM_SLOW_MOTION_MACRO = "attr_slow_motion_macro";
        public static final String PARAM_SWITCH_MACRO = "attr_switch_macro";
    }

    public interface Manual {
        public static final String AE_AF_AWB_LOCKED = "3A_Locked";
        public static final String AUTOEXPOSURE = "auto_exposure";
        public static final String AUXILIARY = "auxiliary";
        public static final String AUXILIAYR_CLOSE = "auxiliary_close";
        public static final String AVERAGE_PHOTOMETRY = "average_photometry";
        public static final String AWB = "awb";
        public static final String CENTER_PHOTOMETRY = "center_photometry";
        public static final String CENTER_WEIGHT = "center_weight";
        public static final String ET = "exposureTime";
        public static final String EV = "exposureValue";
        public static final String EXPOSURE_FEEDBACK = "exposure_feedback";
        public static final String FOCUS_POSITION = "focus_position";
        public static final String GRADIENT = "gradient";
        public static final String HISTOGRAM = "histogram";
        public static final String ISO = "iso";
        public static final String LENS = "lens";
        public static final String LOG = "log";
        public static final String MANUAL_EDIT_TAB_HIDE = "manual_edit_tab_hide";
        public static final String MANUAL_EDIT_TAB_SHOW = "manual_edit_tab_show";
        public static final String MANUAL_FOCUS_PEAK = "manual_focus_peak";
        public static final String METERING_FOCUS_SPLIT = "metering_focus_split";
        public static final String PARAMETER_DESCRIPTION = "parameter_description";
        public static final String PARAM_3A_LOCKED = "attr_3A_Locked";
        public static final String PARAM_AUDIO_MAP = "attr_audio_map";
        public static final String PARAM_AUTOEXPOSURE = "attr_auto_exposure";
        public static final String PARAM_AWB = "attr_awb";
        public static final String PARAM_ET = "attr_et";
        public static final String PARAM_EV = "attr_ev";
        public static final String PARAM_EXPOSURE_FEEDBACk = "attr_exposure_feedback";
        public static final String PARAM_FLASH = "attr_flash";
        public static final String PARAM_FOCUS_PEAK = "attr_focus_peak";
        public static final String PARAM_FOCUS_POSITION = "attr_focus_position";
        public static final String PARAM_GRADIENT = "attr_gradient";
        public static final String PARAM_HISTOGRAM = "attr_histogram";
        public static final String PARAM_ISO = "attr_iso";
        public static final String PARAM_LENS = "attr_lens";
        public static final String PARAM_LOG = "attr_log";
        public static final String PARAM_PALM_SHUTTER = "attr_palm_shutter";
        public static final String PARAM_PRO_MODE_ADJUST_VOLUME_CONTROL_CLICK = "attr_pro_mode_adjust_volume_control_click";
        public static final String PARAM_PRO_MODE_ADJUST_VOLUME_CONTROL_VALUE = "attr_pro_mode_adjust_volume_control_value";
        public static final String PARAM_RAW = "attr_raw";
        public static final String PARAM_REFERENCE_LINE = "attr_reference_line";
        public static final String PARAM_SUPERME_PIXEL_VALUE = "attr_supreme_pixel_value";
        public static final String PARAM_SUPER_RESOLUTION = "attr_super_resolution";
        public static final String PARAM_TIMER = "attr_timer";
        public static final String RAW = "raw";
        public static final String REFERENCE_LINE = "reference_line";
        public static final String RESET_PARAMS_CLICK = "reset_params_click";
        public static final String RESET_PARAMS_SHOW = "reset_params_show";
        public static final String SUPERME_PIXEL = "supreme_pixel";
        public static final String SUPER_RESOLUTION = "super_resolution";
        public static final String VALUE_AUDIO_MAP_VIDEO = "attr_audio_map_video";
        public static final String VALUE_HISTOGRAM_VIDEO = "attr_histogram_video";
        public static final String VALUE_PREFERENCE_DISPLAY = "attr_preference_display_click";
        public static final String VALUE_SUPERME_PIXEL_108M_ON = "108M_ON";
        public static final String VALUE_SUPERME_PIXEL_32M_ON = "32M_ON";
        public static final String VALUE_SUPERME_PIXEL_48M_ON = "48M_ON";
        public static final String VALUE_SUPERME_PIXEL_64M_ON = "64M_ON";
        public static final String VIDEO_8K = "video_8k";
    }

    public interface MiLive {
        public static final String KEY_MI_LIVE_VIDEO_SEGMENT = "key_mi_live_video_segment";
        public static final String PARAM_MI_LIVE_BEAUTY_BODY_SLIM = "attr_mi_live_body_slim";
        public static final String PARAM_MI_LIVE_BEAUTY_HEAD_SLIM = "attr_mi_live_head_slim";
        public static final String PARAM_MI_LIVE_BEAUTY_LEG_SLIM = "attr_mi_live_leg_slim";
        public static final String PARAM_MI_LIVE_BEAUTY_ON = "attr_mi_live_beauty_on";
        public static final String PARAM_MI_LIVE_BEAUTY_SHOULDER_SLIM = "attr_mi_live_shoulder_slim";
        public static final String PARAM_MI_LIVE_BEAUTY_WHOLE_BODY_SLIM = "attr_mi_live_whole_body_slim";
        public static final String PARAM_MI_LIVE_CAMERA_FACING = "attr_mi_live_facing";
        public static final String PARAM_MI_LIVE_CAMERA_QUALITY = "attr_mi_live_quality";
        public static final String PARAM_MI_LIVE_ENLARGE_EYE_RATIO = "attr_mi_live_enlarge_eye_ratio";
        public static final String PARAM_MI_LIVE_FILTER_NAME = "attr_mi_live_filter_name";
        public static final String PARAM_MI_LIVE_KALEIDOSCOPE_NAME = "attr_mi_live_kaleidoscope_name";
        public static final String PARAM_MI_LIVE_MUSIC_NAME = "attr_mi_live_music_name";
        public static final String PARAM_MI_LIVE_SEGMENT_COUNT = "attr_mi_live_segment_count";
        public static final String PARAM_MI_LIVE_SHRINK_FACE_RATIO = "attr_mi_live_shrink_face_ratio";
        public static final String PARAM_MI_LIVE_SMOOTH_RATIO = "attr_mi_live_smooth_ratio";
        public static final String PARAM_MI_LIVE_SPEED = "attr_mi_live_speed";
        public static final String VALUE_MI_LIVE_CLICK_BEAUTY = "mi_live_click_beauty";
        public static final String VALUE_MI_LIVE_CLICK_BEAUTY_RESET = "mi_live_click_beauty_reset";
        public static final String VALUE_MI_LIVE_CLICK_CANCEL = "mi_live_click_cancel";
        public static final String VALUE_MI_LIVE_CLICK_DONE = "mi_live_click_done";
        public static final String VALUE_MI_LIVE_CLICK_ENLARGE_EYE = "mi_live_click_enlarge_eye";
        public static final String VALUE_MI_LIVE_CLICK_FILTER = "mi_live_click_filter";
        public static final String VALUE_MI_LIVE_CLICK_KALEIDOSCOPE = "mi_live_click_kaleidoscope";
        public static final String VALUE_MI_LIVE_CLICK_KALEIDOSCOPE_CAPTURE = "mi_live_click_kaleidoscope_capture";
        public static final String VALUE_MI_LIVE_CLICK_MUSIC = "mi_live_click_music";
        public static final String VALUE_MI_LIVE_CLICK_PAUSE = "mi_live_pause_recording";
        public static final String VALUE_MI_LIVE_CLICK_SHARE = "mi_live_click_share";
        public static final String VALUE_MI_LIVE_CLICK_SHRINK_FACE = "mi_live_click_shrink_face";
        public static final String VALUE_MI_LIVE_CLICK_SMOOTH = "mi_live_click_smooth";
        public static final String VALUE_MI_LIVE_CLICK_SPEED = "mi_live_click_speed";
        public static final String VALUE_MI_LIVE_CLICK_START = "mi_live_start_recording";
        public static final String VALUE_MI_LIVE_CLICK_SWITCH = "mi_live_switch_camera";
    }

    public interface Mimoji {
        public static final String CARTOON = "mimoji_cartoon";
        public static final String CATEGORY_BEAR = "bear";
        public static final String CATEGORY_CUSTOM = "custom";
        public static final String CATEGORY_PERSON = "person";
        public static final String CATEGORY_PIG = "pig";
        public static final String CATEGORY_RABBIT = "rabbit";
        public static final String CATEGORY_ROYAN = "royan";
        public static final String GIF = "mimoji_click_gif";
        public static final String GIF_REMOVE_BG = "mimoji_gif_remove_background";
        public static final String GIF_REMOVE_REVERSE = "mimoji_gif_reverse";
        public static final String GIF_SAVE = "mimoji_gif_save";
        public static final String GIF_SHARE = "mimoji_gif_share";
        public static final String GIF_SPEED = "mimoji_gif_speed";
        public static final String HUMAN = "mimoji_persion";
        public static final String KEY_MIMOJI_CLICK = "key_mimoji_click";
        public static final String MIMOJI_CATEGORY = "attr_mimoji_category";
        public static final String MIMOJI_CHANGE_BG = "mimoji_change_background";
        public static final String MIMOJI_CHANGE_TIMBRE = "mimoji_change_timbre";
        public static final String MIMOJI_CLICK_ADD = "mimoji_click_add";
        public static final String MIMOJI_CLICK_CREATE_BACK = "mimoji_click_create_back";
        public static final String MIMOJI_CLICK_CREATE_CAPTURE = "mimoji_click_create_capture";
        public static final String MIMOJI_CLICK_CREATE_SOFT_BACK = "mimoji_click_create_soft_back";
        public static final String MIMOJI_CLICK_CREATE_SWITCH = "mimoji_click_create_switch";
        public static final String MIMOJI_CLICK_DELETE = "mimoji_click_delete";
        public static final String MIMOJI_CLICK_EDIT = "mimoji_click_edit";
        public static final String MIMOJI_CLICK_EDIT_CANCEL = "edit_cancel";
        public static final String MIMOJI_CLICK_EDIT_RESET = "edit_reset";
        public static final String MIMOJI_CLICK_EDIT_SAVE = "key_edit_save";
        public static final String MIMOJI_CLICK_EDIT_SOFT_BACK = "edit_soft_back";
        public static final String MIMOJI_CLICK_NULL = "mimoji_click_null";
        public static final String MIMOJI_CLICK_PREVIEW_MID_BACK = "mimoji_click_preview_mid_back";
        public static final String MIMOJI_CLICK_PREVIEW_MID_EDIT = "key_preview_mid_edit";
        public static final String MIMOJI_CLICK_PREVIEW_MID_RECAPTURE = "preview_mid_recapture";
        public static final String MIMOJI_CLICK_PREVIEW_MID_SAVE = "preview_mid_save";
        public static final String MIMOJI_CLICK_PREVIEW_MID_SOFT_BACK = "preview_mid_soft_back";
        public static final String MIMOJI_CONFIG_EAR = "attr_ear";
        public static final String MIMOJI_CONFIG_EARING = "attr_earing";
        public static final String MIMOJI_CONFIG_EYEBROW_SHAPE = "attr_eyebrow_shape";
        public static final String MIMOJI_CONFIG_EYEGLASS = "attr_eyeglass";
        public static final String MIMOJI_CONFIG_EYELASH = "attr_eyelash";
        public static final String MIMOJI_CONFIG_EYE_SHAPE = "attr_eye_shape";
        public static final String MIMOJI_CONFIG_FEATURE_FACE = "attr_feature_face";
        public static final String MIMOJI_CONFIG_FRECKLE = "attr_freckle";
        public static final String MIMOJI_CONFIG_HARISTYLE = "attr_hairstyle";
        public static final String MIMOJI_CONFIG_HEADWEAR = "attr_headwear";
        public static final String MIMOJI_CONFIG_MOUTH_SHAPE = "attr_mouth_shape";
        public static final String MIMOJI_CONFIG_MUSTACHE = "attr_mustache";
        public static final String MIMOJI_CONFIG_NOSE = "attr_nose";
        public static final String MIMOJI_HISTORY_EMOJI_COUNT = "attr_mimoji_emoji_history_count";
        public static final String MIMOJI_SAVE_CARTOON = "attr_mimoji_cartoon";
        public static final String MIMOJI_SAVE_CHANGE_BG = "attr_mimoji_change_background";
        public static final String MIMOJI_SAVE_CHANGE_TIMBRE = "attr_mimoji_change_timbre";
        public static final String MIMOJI_SAVE_EXTRA_SCENE = "attr_mimoji_extra_scene";
        public static final String MIMOJI_SAVE_GIF = "attr_mimoji_gif";
        public static final String MIMOJI_STICKER_PACK = "mimoji_sticker_pack";
        public static final String MIMOJI_TRIGGER = "attr_mimoji_extra_scene_trigger";
        public static final String MIMOJI_TYPE = "attr_mimoji_type";
        public static final String PARAM_MIMOJI_EDIT_COUNT = "attr_mimoji_edit_count";
        public static final String PREVIEW_MID = "preview_mid";
    }

    public interface ModuleName {
        public static final String AI_WATERMARK = "M_ai_watermark_";
        public static final String AMBILIGHT = "M_ambilight_";
        public static final String BEAUTY = "M_beauty_";
        public static final String CAPTURE = "M_capture_";
        public static final String CLONE = "M_clone_";
        public static final String DOC = "M_Doc_";
        public static final String DUAL_VIDEO = "M_dual_video_";
        public static final String FAST_MOTION = "M_fastMotion_";
        public static final String FILM = "M_film_";
        public static final String FILM_DOLLY_ZOOM = "M_film_dolly_zoom_";
        public static final String FILM_EXPOSUREDELAY = "M_film_exposuredelay_";
        public static final String FILM_PARALLELDREAM = "M_film_paralleldream_";
        public static final String FILM_SLOWSHUTTER = "M_film_slow_shutter_";
        public static final String FILM_TIME_FREEZE = "M_film_time_freeze_";
        public static final String FUN = "M_funTinyVideo_";
        public static final String FUN_AR = "M_funArMimoji_";
        public static final String FUN_AR2 = "M_funArMimoji2_";
        public static final String ID_CARD = "M_idCard_";
        public static final String ID_PHOTO = "M_idphoto";
        public static final String LIVE = "M_liveDouyin_";
        public static final String LIVE_VV = "M_liveVlog_";
        public static final String MANUAL = "M_manual_";
        public static final String MI_LIVE = "M_miLive_";
        public static final String MULTI_CAMERA_DUAL_VIDEO = "M_multi_camera_dual_video_";
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
    }

    public interface MoonAndNightAttr {
        public static final String KEY_MOON_MODE = "key_moon_mode";
        public static final String PARAM_NIGHT_MOON_MODE = "attr_night_moon_mode";
        public static final String PARAM_SUPER_NIGHT = "attr_supernight_in_M_capture_";
        public static final String VAULE_MOON_MODE = "in_moon_mode";
        public static final String VAULE_NIGHT_MODE = "in_night_mode";
    }

    public interface MoreMode {
        public static final String KEY_CAMERA_MORE_MODE = "key_camera_more_mode";
        public static final String PARAM_ENTER_MORE_MODE_TYPE = "attr_enter_more_mode_type";
        public static final String PARAM_SWITCH_TAB_STYLE = "attr_switch_tab_style";
        public static final String VALUE_ENTER_MORE_MODE_BY_POP = "value_enter_more_mode_by_pop";
        public static final String VALUE_ENTER_MORE_MODE_BY_TAB = "value_enter_more_mode_by_tab";
        public static final String VALUE_ENTER_MORE_MODE_BY_TAB_NEW = "value_enter_more_mode_by_tab_new";
        public static final String VALUE_ENTER_MORE_MODE_TAB_NEW = "vale_enter_more_mode_tab_new";
        public static final String VALUE_ENTER_MORE_MODE_TAB_OLD = "vale_enter_more_mode_tab_old";
    }

    public interface MultiCameraAttr extends DualVideoAttr {
        public static final String ATTR_DEVICE_TYPE = "attr_device_type";
        public static final String ATTR_RECORD_CAPTURE = "attr_record_capture";
        public static final String ATTR_RECORD_PAUSED = "attr_record_paused";
        public static final String ATTR_RECORD_RESUME = "attr_record_resume";
        public static final String ATTR_RECORD_TYPE = "attr_record_type";
        public static final String KEY_MULTI_CAMERA_DUAL_VIDEO = "key_multi_camera_dual_video";
        public static final String VALUE_BACK_SELECT = "value_back_select";
        public static final String VALUE_CANCEL_SELECT = "value_cancel_select";
        public static final String VALUE_CONFIRM_SELECT = "value_confirm_select";
        public static final String VALUE_IDLE_SWITCH = "value_idle_switch";
        public static final String VALUE_PAUSE_SWITCH = "value_pause_switch";
        public static final String VALUE_RECORD_MERGED = "value_record_merged";
        public static final String VALUE_RECORD_STANDALONE = "value_record_standalone";
        public static final String VALUE_RESELECT = "value_reselect";
        public static final String VALUE_USER_GUIDE = "value_user_guide";
    }

    public interface NonUI {
        public static final String KEY_ENTER_FAULT = "key_enter_fault";
        public static final String KEY_POCKET_MODE_KEYGUARD_EXIT = "key_pocket_mode_keyguard_exit";
        public static final String KEY_POCKET_MODE_SENSOR_DELAY = "key_pocket_mode_sensor_delay";
        public static final String POCKET_MODE_KEYGUARD_EXIT_DISMISS = "keyguard_exit_dismiss";
        public static final String POCKET_MODE_KEYGUARD_EXIT_TIMEOUT = "keyguard_exit_timeout";
        public static final String POCKET_MODE_KEYGUARD_EXIT_UNLOCK = "keyguard_exit_unlock";
        public static final String POCKET_MODE_NONUI_ENTER_SNAP = "NonUI_snap";
        public static final String POCKET_MODE_NONUI_ENTER_VOLUME = "NonUI_volume";
        public static final String POCKET_MODE_PSENSOR_ENTER_KEYGUARD = "Psensor_keyguard";
        public static final String POCKET_MODE_PSENSOR_ENTER_SNAP = "Psensor_snap";
        public static final String POCKET_MODE_PSENSOR_ENTER_VOLUME = "Psensor_volume";
        public static final String VALUE_POCKET_MODE_ENTER = "pocket_mode_enter";
    }

    public interface Other {
        public static final String KEY_CTA_CLICK_DIALOG_AGREE = "key_cta_dialog_agree";
        public static final String KEY_CTA_CLICK_DIALOG_DISAGREE = "key_cta_dialog_disagree";
        public static final String KEY_LONG_PRESS_DIALOG = "key_lp_dialog";
        public static final String KEY_LONG_PRESS_DIALOG_BURST = "key_lp_dialog_burst";
        public static final String KEY_LONG_PRESS_DIALOG_RECORD = "key_lp_dialog_record";
    }

    public interface Panorama {
        public static final String PANORAMA_DIRECTION = "panorama_direction";
        public static final String PANORAMA_DIRECTION_B2T = "B2T";
        public static final String PANORAMA_DIRECTION_L2R = "L2R";
        public static final String PANORAMA_DIRECTION_R2L = "R2L";
        public static final String PANORAMA_DIRECTION_T2B = "T2B";
        public static final String PANORAMA_TOGGLE_HORIZONTAL = "panorama_toggle_horizontal";
        public static final String PANORAMA_TOGGLE_VERTICAL = "panorama_toggle_vertical";
        public static final String PANORAMA_TOGGLE_V_H = "panorama_toggle_v_h";
        public static final String PARAM_PANORAMA_DIRECTION = "attr_panorama_direction";
        public static final String PARAM_STOP_CAPTURE_MODE = "attr_stop_capture_mode";
        public static final String STOP_CAPTURE_MODE_COMPLETE = "stop_capture_complete";
        public static final String STOP_CAPTURE_MODE_FILL = "stop_capture_fill_preview";
        public static final String STOP_CAPTURE_MODE_HORIZONTAL_OUT = "stop_capture_horizontal_out";
        public static final String STOP_CAPTURE_MODE_ON_HOME_OR_BACK = "stop_capture_on_home_or_back";
        public static final String STOP_CAPTURE_MODE_ON_SHUTTER_BUTTON = "stop_capture_on_shutter_button";
        public static final String STOP_CAPTURE_MODE_OUT_OF_RANGE = "stop_capture_out_of_range";
        public static final String STOP_CAPTURE_MODE_PRESS_BACK = "stop_capture_press_back";
        public static final String STOP_CAPTURE_MODE_PRESS_STOP = "stop_capture_press_stop";
        public static final String STOP_CAPTURE_MODE_REVERSE = "stop_capture_reverse";
        public static final String STOP_CAPTURE_MODE_ROTATE_OUT = "stop_capture_rotate_out";
        public static final String STOP_CAPTURE_MODE_THERMAL = "stop_capture_thermal";
        public static final String STOP_CAPTURE_MODE_UN_EXPECT = "stop_capture_un_expect";
        public static final String STOP_CAPTURE_MODE_VERTICAL_OUT = "stop_capture_vertical_out";
    }

    public interface PictureQuality {
        public static final String HIGH = "high";
        public static final String HIGHER = "higher";
        public static final String HIGHEST = "highest";
        public static final String LOW = "low";
        public static final String LOWER = "lower";
        public static final String LOWEST = "lowest";
        public static final String NORMAL = "normal";
        public static final String PARAM_ULTRA_PIXEL = "attr_ultra_pixel";
        public static final String VALUE_ULTRA_PIXEL_108MP = "108MP";
        public static final String VALUE_ULTRA_PIXEL_32MP = "32MP";
        public static final String VALUE_ULTRA_PIXEL_48MP = "48MP";
        public static final String VALUE_ULTRA_PIXEL_64MP = "64MP";
    }

    public interface PortraitAttr {
        public static final String PARAM_BOKEH_RATIO = "attr_bokeh_ratio";
        public static final String PARAM_PORTRAIT_LIGHTING = "attr_portrait_lighting";
        public static final String PARAM_ULTRAPIXEL_PORTRAIT = "attr_ultrapixel_portrait";
        public static final String PARAM_ULTRA_WIDE_BOKEH = "attr_whole_body";
        public static final String PORTRAIT_LIGHTING = "portrait_lighting";
        public static final String ULTRAPIXEL_PORTRAIT = "ultrapixel_portrait";
        public static final String VALUE_BOKEH_ADJUST_ENTRY = "bokeh_adjust_entry";
        public static final String VALUE_LIGHTING_OUT_BUTTON = "lighting_out_button";
        public static final String VALUE_ULTRA_WIDE_BOKEH = "whole_body";
    }

    public interface ProColor {
        public static final String CAPTURE_STATE = "attr_pro_color_state";
        public static final String KEY_PRO_COLOR = "pro_color";
        public static final String VALUE_CLOSE = "value_pro_color_close";
        public static final String VALUE_OFF = "pro_color_off";
        public static final String VALUE_ON = "pro_color_on";
        public static final String VALUE_OPEN = "value_pro_color_open";
    }

    public interface ResourceAttr {
        public static final String KEY_RESOURCE = "key_resource";
        public static final String KEY_RESOURCE_DOWNLOAD_RESULT = "attr_resource_download_result";
        public static final String KEY_RESOURCE_ID = "attr_resource_id";
    }

    public interface SensorAttr {
        public static final String PARAM_MACRO_MODE = "attr_macro_mode";
        public static final String PARAM_SAT_ZOOM = "attr_sat_device";
        public static final String PARAM_STANDALONE_MACRO_MODE = "attr_standlone_macroMode";
        public static final String VALUE_SENSOR_TYPE_REAR_MACRO = "_RearMacro";
        public static final String VALUE_SENSOR_TYPE_REAR_TELE = "_RearTele2x";
        public static final String VALUE_SENSOR_TYPE_REAR_TELE4x = "_RearTele4x";
        public static final String VALUE_SENSOR_TYPE_REAR_ULTRA = "_RearUltra";
        public static final String VALUE_SENSOR_TYPE_REAR_WIDE = "_RearWide";
    }

    public interface Setting {
        public static final String CUSTOM_SOUND_OUTTER = "edit_sound_outter_click";
        public static final String CUSTOM_TINT_OUTTER = "edit_tint_outter_click";
        public static final String KEY_SETTING = "key_settings";
        public static final String PARAM_AICC = "attr_aicc";
        public static final String PARAM_ANTIBANDING = "attr_antibanding";
        public static final String PARAM_AUTO_HIBERNATION = "attr_auto_hibernation";
        public static final String PARAM_CAMERA_SNAP = "attr_snap_enable";
        public static final String PARAM_CAMERA_SOUND = "attr_camera_sound";
        public static final String PARAM_DESCRIPTION = "attr_param_description";
        public static final String PARAM_DEVICE_WATERMARK = "attr_watermark_device";
        public static final String PARAM_DOCUMENT_MODE = "attr_document_mode";
        public static final String PARAM_FLAW_TIP = "attr_flaw_tip";
        public static final String PARAM_FOCUS_SHOOT = "attr_focus_shoot";
        public static final String PARAM_FRONT_MIRROR = "attr_front_mirror";
        public static final String PARAM_GRADIENTER = "attr_gradiente";
        public static final String PARAM_HEIC = "attr_heic";
        public static final String PARAM_HEIC_FORMAT = "attr_heic_format";
        public static final String PARAM_HIGH_QUALITY_PREFERRED = "attr_high_quality_preferred";
        public static final String PARAM_JPEG_QUALITY = "attr_jpeg_quality";
        public static final String PARAM_LENS_DIRTY_SWITCH = "attr_lens_dirty_switch";
        public static final String PARAM_LONG_PRESS_SHUTTER_FEATURE = "attr_long_press_shutter";
        public static final String PARAM_LYING_TIP_SWITCH = "attr_lying_tip_switch";
        public static final String PARAM_MOVIE_SOLID = "attr_movie_solid";
        public static final String PARAM_NORMAL_WIDE_LDC = "attr_normal_wide_ldc";
        public static final String PARAM_PRO_MODE_MOVIE_SOLID = "attr_pro_mode_movie_solid";
        public static final String PARAM_REFERENCE_LINE = "attr_reference_line";
        public static final String PARAM_RETAIN_CAMERA_MODE = "attr_retain_camera_mode";
        public static final String PARAM_SAVE_LOCATION = "attr_save_location";
        public static final String PARAM_SCAN_QRCODE = "attr_scan_qrcode";
        public static final String PARAM_SUB_MODULE = "attr_sub_module";
        public static final String PARAM_TILTSHIFT = "attr_tiltshift";
        public static final String PARAM_TIME_WATERMARK = "attr_watermark_time";
        public static final String PARAM_ULTRA_WIDE_LDC = "attr_ultra_wide_ldc";
        public static final String PARAM_USERDEFINE_WATERMARK = "attr_watermark_custom";
        public static final String PARAM_VIDEO_DYNAMIC_FRAME_RATE = "attr_video_dynamic_frame_rate";
        public static final String PARAM_VIDEO_ENCODER = "attr_video_encoder";
        public static final String PARAM_VIDEO_FRONT_DENOISE = "attr_video_front_denoise";
        public static final String PARAM_VIDEO_HDR10PLUS_VIDEO_ENCODER = "attr_video_hdr10+";
        public static final String PARAM_VIDEO_HDR10_VIDEO_ENCODER = "attr_video_hdr10";
        public static final String PARAM_VIDEO_TIME_LAPSE_FRAME_INTERVAL = "attr_video_time_lapse_frame_interval";
        public static final String PARAM_VIDEO_WIND_DENOISE = "attr_video_wind_denoise";
        public static final String PARAM_VOLUME_CAMERA_FUNCTION = "attr_volume_camera_fuction";
        public static final String PARAM_WATERMARK = "attr_watermark";
        public static final String PREF_KEY_CUSTOM_CAMERA = "attr_custom_camera";
        public static final String PREF_KEY_CUSTOM_SOUND = "attr_edit_sound";
        public static final String PREF_KEY_CUSTOM_TINT = "attr_edit_tint";
        public static final String PREF_KEY_MORE_MODE = "attr_more_mode";
        public static final String PREF_KEY_POPUP_CAMERA = "attr_popup_camera";
        public static final String PREF_KEY_PRIVACY = "attr_privacy";
        public static final String PREF_KEY_RESTORE = "attr_restore";
        public static final String SHORTCUT_DOCS_MODE = "attr_shortcut_docs_mode";
        public static final String SHORTCUT_PRO_MODE = "attr_shortcut_pro_mode";
        public static final String SHORTCUT_SELFIE_MODE = "attr_shortcut_selfie_mode";
        public static final String SHORTCUT_VIDEO_MODE = "attr_shortcut_video_mode";
        public static final String VALUE_SETTING_ADVANCE = "settings_advance";
        public static final String VALUE_SETTING_CAPTURE = "settings_capture";
        public static final String VALUE_SETTING_GOLBAL = "settings_global";
        public static final String VALUE_SETTING_VIDEO_RECORD = "settings_video_record";
    }

    public interface SlowMotion {
        public static final String FPS_120 = "fps_120";
        public static final String FPS_240 = "fps_240";
        public static final String FPS_960 = "fps_960";
    }

    public interface SpeechShutterAttr {
        public static final String KEY_SPEECH_SHUTTER = "key_speech_shutter";
        public static final String PARAM_SPEECH_SHUTTER_STATUS = "attr_speech_shutter_status";
        public static final String PARAM_SPEECH_SHUTTER_VOCON_PHRASE = "attr_speech_shutter_vocon_phrase";
    }

    public interface SuperMoon {
        public static final String KEY_SUPER_MOON = "key_super_moon";
        public static final String PARAM_SUPER_MOON_EFFECT_CLICK = "attr_super_moon_click_effect";
        public static final String PARAM_SUPER_MOON_EFFECT_SELECT = "attr_super_moon_effect_select";
        public static final String PARAM_SUPER_MOON_HAS_EFFECT = "attr_super_moon_has_effect";
        public static final String PARAM_SUPER_MOON_SILHOUETTE_KEY = "attr_super_moon_silhouette_key";
        public static final String PARAM_SUPER_MOON_TEXT_KEY = "attr_super_moon_text_key";
    }

    public interface TimerBurst {
        public static final String KEY_TIMER_BURST_TAKEN = "key_timer_burst_taken";
        public static final String PARAM_INTERVAL_TIMER = "param_interval_timer";
        public static final String PARAM_TAKEN_COUNT = "param_taken_count";
        public static final String PARAM_TOTAL_COUNT = "param_total_count";
    }

    public interface VLogAttr {
        public static final String KEY_VLOG = "key_vlog";
        public static final String KEY_VV_CLICK_EXIT_2 = "attr_key_vv_exit_2";
        public static final String KEY_VV_CLICK_EXIT_CONFIRM_2 = "attr_key_vv_exit_confirm_2";
        public static final String PARAM_VV_CLICK_TEMPLATE_PREVIEW = "attr_vv_click_template_preview";
        public static final String PARAM_VV_TEMPLATE_NAME_CLICK = "attr_vv_template_name_click";
        public static final String PARAM_VV_TEMPLATE_NAME_FINISH = "attr_vv_template_name_finish";
        public static final String PARAM_VV_TEMPLATE_NAME_SAVE = "attr_vv_template_name_save";
        public static final String PARAM_VV_TEMPLATE_NAME_START = "attr_vv_template_name_start";
        public static final String VALUE_VV_CLICK_PLAY_ALL = "value_vv_click_play_all";
        public static final String VALUE_VV_CLICK_PLAY_ALL_PAUSE = "value_vv_click_play_all_pause";
        public static final String VALUE_VV_CLICK_PLAY_ALL_RESUME = "value_vv_click_play_all_resume";
        public static final String VALUE_VV_CLICK_PLAY_SAVE = "value_vv_click_play_save";
        public static final String VALUE_VV_CLICK_PLAY_SEGMENT = "value_vv_click_play_segment";
        public static final String VALUE_VV_CLICK_PLAY_SEGMENT_EXIT = "value_vv_click_play_segement_exit";
        public static final String VALUE_VV_CLICK_PLAY_SHARE = "value_vv_click_play_share";
        public static final String VALUE_VV_CLICK_REVERSE = "value_vv_reverse";
        public static final String VALUE_VV_CLICK_REVERSE_CONFIRM = "value_vv_reverse_confirm";
        public static final String VALUE_VV_CLICK_START_SEGMENT = "value_vv_start_segment";
        public static final String VALUE_VV_CLICK_WORKSPACE_CONTINUE = "value_vv_click_workspace_continue";
        public static final String VALUE_VV_CLICK_WORKSPACE_DELETE = "value_vv_click_workspace_delete";
        public static final String VALUE_VV_CLICK_WORKSPACE_DELETE_CONFIRM = "value_vv_click_workspace_delete_confirm";
        public static final String VALUE_VV_CLICK_WORKSPACE_EDIT = "value_vv_click_workspace_edit";
        public static final String VALUE_VV_CLICK_WORKSPACE_ICON = "value_vv_click_workspace_into";
        public static final String VALUE_VV_CLICK_WORKSPACE_PLAY = "value_vv_click_workspace_play";
        public static final String VALUE_VV_CLICK_WORKSPACE_SELECT_ALL = "value_vv_click_workspace_select_all";
        public static final String VALUE_VV_ICON_CLICK = "value_vv_icon_click";
    }

    public interface VideoAttr {
        public static final String KEY_LONG_PRESS_RECORD = "key_long_press_record";
        public static final String KEY_NEW_SLOW_MOTION = "key_slow_motion_mode";
        public static final String KEY_VIDEO_960 = "key_video_960";
        public static final String KEY_VIDEO_COMMON_CLICK = "key_video_common_click";
        public static final String KEY_VIDEO_QUICK = "key_video_quick";
        public static final String NEW_SLOW_MOTION_SWITCH_FPS = "new_slow_motion_switch_fps";
        public static final String PARAM_AI_ENHANCED_VIDEO = "attr_video_ai";
        public static final String PARAM_AI_NOISE_REDUCTION = "attr_pro_mode_ai_noise_reduction";
        public static final String PARAM_AI_NOISE_REDUCTION_NR = "attr_pro_mode_ai_noise_reduction_nr";
        public static final String PARAM_AI_NOISE_REDUCTION_OS = "attr_pro_mode_ai_noise_reduction_os";
        public static final String PARAM_AI_NOISE_REDUCTION_VIDEO = "attr_pro_mode_ai_noise_reduction_video";
        public static final String PARAM_BEAUTY = "attr_beauty";
        public static final String PARAM_BLUETOOTH_EARPHONE_VIDEO = "attr_pro_mode_bluetooth_earphone_video";
        public static final String PARAM_BLUETOOTH_SCO = "attr_bluetooth_sco";
        public static final String PARAM_BOKEH = "attr_bokeh";
        public static final String PARAM_BOKEH_MODE = "attr_bokeh_mode";
        public static final String PARAM_CINEMATIC_RATIO = "attr_cinematic";
        public static final String PARAM_FILTER = "attr_filter";
        public static final String PARAM_HEADSET = "attr_pro_mode_headset";
        public static final String PARAM_KARAOKE = "attr_pro_mode_karaoke";
        public static final String PARAM_KARAOKE_VIDEO = "attr_pro_mode_karaoke_video";
        public static final String PARAM_SOUND_SETTING = "attr_sound_setting_click";
        public static final String PARAM_SUPER_EIS = "attr_super_eis";
        public static final String PARAM_VIDEO_FPS = "attr_video_fps";
        public static final String PARAM_VIDEO_HDR = "attr_video_hdr";
        public static final String PARAM_VIDEO_MODE = "attr_video_mode";
        public static final String PARAM_VIDEO_QUALITY = "attr_video_quality";
        public static final String PARAM_VIDEO_SMOOTH_ZOOM = "attr_video_smooth_zoom";
        public static final String PARAM_VIDEO_SNAPSHOT_COUNT = "attr_video_snapshot_count";
        public static final String PARAM_VIDEO_TIME = "attr_video_time";
        public static final String PARAM_VIDEO_TIME_LAPSE_DURATION = "attr_video_time_lapse_duration";
        public static final String PARAM_VIDEO_TIME_LAPSE_INTERVAL = "attr_video_time_lapse_interval";
        public static final String VALUE_FPS960_PROCESS_FAILED = "fps_960_process_failed";
        public static final String VALUE_FPS960_TOO_SHORT = "fps_960_too_short";
        public static final String VALUE_SPEED_SLOW = "video_slow";
        public static final String VALUE_SUPER_NIGHT_VIDEO = "video_super_night";
        public static final String VALUE_VIDEO_PAUSE_RECORDING = "video_pause_recording";
        public static final String VALUE_VIDEO_RESUME_RECORDING = "video_resume_recording";
    }

    public interface Zoom {
        public static final String KEY_ZOOM = "key_zoom";
        public static final String PARAM_SAT_ZOOM_RATIO = "attr_SAT_ratio";
        public static final String PARAM_ZOOM_ADJUSTED_MODE = "attr_zoom_adjusted_mode";
        public static final String PARAM_ZOOM_IN_RECORDING = "attr_in_recording";
        public static final String PARAM_ZOOM_RATIO = "attr_zoom_ratio";
        public static final String TRIGGER_MODE = "attr_trigger_mode";
        public static final String VALUE_SHOW_ZOOM_BAR_BY_SCROLL = "show_zoom_bar_by_scroll";
    }

    public interface ZoomMapAttr {
        public static final String ATTR_MOVE_WINDOW = "attr_zoom_map_move_window";
        public static final String KEY_ZOOM_MAP = "key_zoom_map";
        public static final String MOVE_DIRECTION_LEFT_BOTTOM = "LEFT_BOTTOM";
        public static final String MOVE_DIRECTION_LEFT_TOP = "LEFT_TOP";
        public static final String MOVE_DIRECTION_RIGHT_BOTTOM = "RIGHT_BOTTOM";
        public static final String MOVE_DIRECTION_RIGHT_TOP = "RIGHT_TOP";
        public static final String VALUE_REMOVE_WINDOW = "value_zoom_map_remove_window";
        public static final String VALUE_SHOW_WINDOW = "value_zoom_map_show_window";
    }
}
