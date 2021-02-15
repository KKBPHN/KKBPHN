package com.android.camera.statistic;

import android.content.Context;
import android.content.res.Resources;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.customization.ShutterSound;
import com.android.camera.customization.TintColor;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.settings.CameraPreferenceFragment;
import com.android.camera.statistic.MistatsConstants.CaptureAttr;
import com.android.camera.statistic.MistatsConstants.Manual;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.android.camera.statistic.MistatsConstants.VideoAttr;
import java.util.HashMap;

public class SettingRecord {
    private static String TAG = "SettingRecord";
    private Context mContext;
    private boolean mInRecording = false;

    public SettingRecord(Context context) {
        this.mContext = context;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String getMistatString(String str) {
        char c;
        switch (str.hashCode()) {
            case -2144607600:
                if (str.equals(CameraSettings.KEY_ULTRA_WIDE_LDC)) {
                    c = 11;
                    break;
                }
            case -2108353415:
                if (str.equals(CameraSettings.KEY_NORMAL_WIDE_LDC)) {
                    c = 20;
                    break;
                }
            case -1920715416:
                if (str.equals(CameraSettings.KEY_KARAOKE_REDUCTION)) {
                    c = 31;
                    break;
                }
            case -1844275638:
                if (str.equals(CameraSettings.KEY_INTELLIGENT_NOISE_REDUCTION)) {
                    c = 30;
                    break;
                }
            case -1717659284:
                if (str.equals(CameraPreferenceFragment.PREF_KEY_PRIVACY)) {
                    c = '%';
                    break;
                }
            case -1662809593:
                if (str.equals(CameraSettings.KEY_FRONT_DENOISE)) {
                    c = 29;
                    break;
                }
            case -1620641004:
                if (str.equals(CameraSettings.KEY_SCAN_QRCODE)) {
                    c = 13;
                    break;
                }
            case -1427654064:
                if (str.equals(CameraSettings.KEY_VIDEO_DYNAMIC_FRAME_RATE)) {
                    c = 27;
                    break;
                }
            case -1408552375:
                if (str.equals(CameraSettings.KEY_LENS_DIRTY_TIP)) {
                    c = 18;
                    break;
                }
            case -1334394994:
                if (str.equals(CameraSettings.KEY_CAMERA_SOUND)) {
                    c = 1;
                    break;
                }
            case -1326017004:
                if (str.equals(CameraSettings.KEY_EAR_PHONE_RADIO)) {
                    c = ' ';
                    break;
                }
            case -1153050370:
                if (str.equals(CameraSettings.KEY_MOVIE_SOLID)) {
                    c = 22;
                    break;
                }
            case -1051988173:
                if (str.equals(CameraSettings.KEY_HDR10PLUS_VIDEO_ENCODER)) {
                    c = 26;
                    break;
                }
            case -802558857:
                if (str.equals(CameraSettings.KEY_CAMERA_AUDIO_MAP)) {
                    c = '!';
                    break;
                }
            case -636369951:
                if (str.equals(CameraSettings.KEY_REFERENCE_LINE)) {
                    c = 10;
                    break;
                }
            case -435486694:
                if (str.equals("pref_camera_focus_shoot_key")) {
                    c = 12;
                    break;
                }
            case -316718012:
                if (str.equals(CameraSettings.KEY_CAMERA_NEAR_RANGE)) {
                    c = '(';
                    break;
                }
            case -305641358:
                if (str.equals(CameraPreferenceFragment.PREF_KEY_RESTORE)) {
                    c = '&';
                    break;
                }
            case -44500048:
                if (str.equals(CameraSettings.KEY_VOLUME_CAMERA_FUNCTION)) {
                    c = '#';
                    break;
                }
            case -33912691:
                if (str.equals(CameraSettings.KEY_ANTIBANDING)) {
                    c = '$';
                    break;
                }
            case 4130057:
                if (str.equals(CameraSettings.KEY_PICTURE_FLAW_TIP)) {
                    c = 21;
                    break;
                }
            case 17536442:
                if (str.equals(CameraSettings.KEY_FRONT_MIRROR)) {
                    c = 4;
                    break;
                }
            case 212575376:
                if (str.equals(CameraSettings.KEY_CAMERA_PRO_HISTOGRAM)) {
                    c = '\"';
                    break;
                }
            case 386125541:
                if (str.equals(CameraSettings.KEY_HEIC_FORMAT)) {
                    c = 17;
                    break;
                }
            case 554750382:
                if (str.equals("pref_time_watermark_key")) {
                    c = 7;
                    break;
                }
            case 580528798:
                if (str.equals(CameraSettings.KEY_WIND_DENOISE)) {
                    c = 28;
                    break;
                }
            case 585576333:
                if (str.equals(CameraSettings.KEY_HDR10_VIDEO_ENCODER)) {
                    c = 25;
                    break;
                }
            case 852574760:
                if (str.equals(CameraSettings.KEY_CAMERA_SNAP)) {
                    c = 3;
                    break;
                }
            case 966436379:
                if (str.equals(CameraSettings.KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL)) {
                    c = 24;
                    break;
                }
            case 982390275:
                if (str.equals(CameraSettings.KEY_AUTO_HIBERNATION)) {
                    c = 5;
                    break;
                }
            case 1069539048:
                if (str.equals("pref_watermark_key")) {
                    c = 6;
                    break;
                }
            case 1167378432:
                if (str.equals(CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH)) {
                    c = 19;
                    break;
                }
            case 1324596611:
                if (str.equals(CameraSettings.KEY_LONG_PRESS_SHUTTER_FEATURE)) {
                    c = 14;
                    break;
                }
            case 1516583362:
                if (str.equals(CameraSettings.KEY_CAMERA_HIGH_QUALITY_PREFERRED)) {
                    c = 15;
                    break;
                }
            case 1540922525:
                if (str.equals(CameraSettings.KEY_CAMERA_MANUALLY_DESCRIPTION_TIP)) {
                    c = '\'';
                    break;
                }
            case 1613717468:
                if (str.equals(CameraSettings.KEY_VIDEO_ENCODER)) {
                    c = 23;
                    break;
                }
            case 1739638146:
                if (str.equals("pref_dualcamera_watermark_key")) {
                    c = 8;
                    break;
                }
            case 1752299636:
                if (str.equals("user_define_watermark_key")) {
                    c = 9;
                    break;
                }
            case 1761265663:
                if (str.equals(CameraSettings.KEY_RETAIN_CAMERA_MODE)) {
                    c = 2;
                    break;
                }
            case 1934228025:
                if (str.equals(CameraSettings.KEY_JPEG_QUALITY)) {
                    c = 16;
                    break;
                }
            case 2069752292:
                if (str.equals(CameraSettings.KEY_RECORD_LOCATION)) {
                    c = 0;
                    break;
                }
            default:
                c = 65535;
                break;
        }
        switch (c) {
            case 0:
                return Setting.PARAM_SAVE_LOCATION;
            case 1:
                return Setting.PARAM_CAMERA_SOUND;
            case 2:
                return Setting.PARAM_RETAIN_CAMERA_MODE;
            case 3:
                return Setting.PARAM_CAMERA_SNAP;
            case 4:
                return Setting.PARAM_FRONT_MIRROR;
            case 5:
                return "attr_auto_hibernation";
            case 6:
                return Setting.PARAM_WATERMARK;
            case 7:
                return Setting.PARAM_TIME_WATERMARK;
            case 8:
                return Setting.PARAM_DEVICE_WATERMARK;
            case 9:
                return Setting.PARAM_USERDEFINE_WATERMARK;
            case 10:
                return "attr_reference_line";
            case 11:
                return Setting.PARAM_ULTRA_WIDE_LDC;
            case 12:
                return Setting.PARAM_FOCUS_SHOOT;
            case 13:
                return Setting.PARAM_SCAN_QRCODE;
            case 14:
                return Setting.PARAM_LONG_PRESS_SHUTTER_FEATURE;
            case 15:
                return Setting.PARAM_HIGH_QUALITY_PREFERRED;
            case 16:
                return Setting.PARAM_JPEG_QUALITY;
            case 17:
                return Setting.PARAM_HEIC_FORMAT;
            case 18:
                return Setting.PARAM_LENS_DIRTY_SWITCH;
            case 19:
                return Setting.PARAM_LYING_TIP_SWITCH;
            case 20:
                return Setting.PARAM_NORMAL_WIDE_LDC;
            case 21:
                return Setting.PARAM_FLAW_TIP;
            case 22:
                return 180 == DataRepository.dataItemGlobal().getCurrentMode() ? Setting.PARAM_PRO_MODE_MOVIE_SOLID : Setting.PARAM_MOVIE_SOLID;
            case 23:
                return Setting.PARAM_VIDEO_ENCODER;
            case 24:
                return Setting.PARAM_VIDEO_TIME_LAPSE_FRAME_INTERVAL;
            case 25:
                return Setting.PARAM_VIDEO_HDR10_VIDEO_ENCODER;
            case 26:
                return Setting.PARAM_VIDEO_HDR10PLUS_VIDEO_ENCODER;
            case 27:
                return Setting.PARAM_VIDEO_DYNAMIC_FRAME_RATE;
            case 28:
                return Setting.PARAM_VIDEO_WIND_DENOISE;
            case 29:
                return Setting.PARAM_VIDEO_FRONT_DENOISE;
            case 30:
                return VideoAttr.PARAM_AI_NOISE_REDUCTION;
            case 31:
                return VideoAttr.PARAM_KARAOKE;
            case ' ':
                return VideoAttr.PARAM_HEADSET;
            case '!':
                return Manual.PARAM_AUDIO_MAP;
            case '\"':
                return Manual.PARAM_HISTOGRAM;
            case '#':
                return Setting.PARAM_VOLUME_CAMERA_FUNCTION;
            case '$':
                return Setting.PARAM_ANTIBANDING;
            case '%':
                return Setting.PREF_KEY_PRIVACY;
            case '&':
                return Setting.PREF_KEY_RESTORE;
            case '\'':
                return Setting.PARAM_DESCRIPTION;
            case '(':
                return CaptureAttr.PARAM_NEAR_RANGE_MODE;
            default:
                return null;
        }
    }

    private void uploadAdvanceSetting() {
        HashMap hashMap = new HashMap();
        Resources resources = this.mContext.getResources();
        hashMap.put(Setting.PARAM_SUB_MODULE, Setting.VALUE_SETTING_ADVANCE);
        hashMap.put(Setting.PARAM_VOLUME_CAMERA_FUNCTION, DataRepository.dataItemGlobal().getString(CameraSettings.KEY_VOLUME_CAMERA_FUNCTION, resources.getString(R.string.pref_camera_volumekey_function_default)));
        hashMap.put(Setting.PARAM_ANTIBANDING, CameraSettings.getAntiBanding());
        MistatsWrapper.settingSchedualEvent(hashMap);
    }

    private void uploadCaptureSetting() {
        HashMap hashMap = new HashMap();
        hashMap.put(Setting.PARAM_SUB_MODULE, Setting.VALUE_SETTING_CAPTURE);
        boolean isTimeWaterMarkOpen = CameraSettings.isTimeWaterMarkOpen();
        hashMap.put(Setting.PARAM_TIME_WATERMARK, Boolean.valueOf(CameraSettings.isTimeWaterMarkOpen()));
        boolean isDualCameraWaterMarkOpen = CameraSettings.isDualCameraWaterMarkOpen();
        hashMap.put(Setting.PARAM_DEVICE_WATERMARK, Boolean.valueOf(isDualCameraWaterMarkOpen));
        boolean isCustomWatermarkOpen = CameraSettings.isCustomWatermarkOpen();
        hashMap.put(Setting.PARAM_USERDEFINE_WATERMARK, Boolean.valueOf(isCustomWatermarkOpen));
        boolean z = (CameraSettings.isSupportedDualCameraWaterMark() && isDualCameraWaterMarkOpen) || isTimeWaterMarkOpen || isCustomWatermarkOpen;
        hashMap.put(Setting.PARAM_WATERMARK, Boolean.valueOf(z));
        hashMap.put("attr_reference_line", Boolean.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_REFERENCE_LINE, false)));
        hashMap.put(Setting.PARAM_ULTRA_WIDE_LDC, Boolean.valueOf(CameraSettings.isUltraWideLDCEnabled()));
        hashMap.put(Setting.PARAM_NORMAL_WIDE_LDC, Boolean.valueOf(CameraSettings.isNormalWideLDCEnabled()));
        hashMap.put(Setting.PARAM_FOCUS_SHOOT, Boolean.valueOf(DataRepository.dataItemGlobal().isGlobalSwitchOn("pref_camera_focus_shoot_key")));
        hashMap.put(Setting.PARAM_SCAN_QRCODE, Boolean.valueOf(DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_SCAN_QRCODE, Boolean.valueOf(this.mContext.getResources().getString(R.string.pref_scan_qrcode_default)).booleanValue())));
        hashMap.put(Setting.PARAM_LONG_PRESS_SHUTTER_FEATURE, DataRepository.dataItemGlobal().getString(CameraSettings.KEY_LONG_PRESS_SHUTTER_FEATURE, this.mContext.getResources().getString(R.string.pref_camera_long_press_shutter_feature_default)));
        hashMap.put(Setting.PARAM_JPEG_QUALITY, DataRepository.dataItemConfig().getString(CameraSettings.KEY_JPEG_QUALITY, this.mContext.getResources().getString(R.string.pref_camera_jpegquality_default)));
        hashMap.put(Setting.PARAM_HEIC_FORMAT, Boolean.valueOf(CameraSettings.isHeicImageFormatSelected()));
        hashMap.put(Setting.PARAM_LYING_TIP_SWITCH, Boolean.valueOf(CameraSettings.isCameraLyingHintOn()));
        MistatsWrapper.settingSchedualEvent(hashMap);
    }

    private void uploadRecordSettingGlobal() {
        HashMap hashMap = new HashMap();
        hashMap.put(Setting.PARAM_SUB_MODULE, Setting.VALUE_SETTING_GOLBAL);
        hashMap.put(Setting.PARAM_SAVE_LOCATION, Boolean.valueOf(CameraSettings.isRecordLocation()));
        hashMap.put(Setting.PARAM_CAMERA_SOUND, Boolean.valueOf(CameraSettings.isCameraSoundOpen()));
        hashMap.put(Setting.PARAM_RETAIN_CAMERA_MODE, Boolean.valueOf(CameraSettings.retainCameraMode()));
        hashMap.put(Setting.PARAM_CAMERA_SNAP, DataRepository.dataItemGlobal().getString(CameraSettings.KEY_CAMERA_SNAP, null));
        hashMap.put(Setting.PARAM_FRONT_MIRROR, Boolean.valueOf(CameraSettings.isRecordLocation()));
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_CAMERA_SOUND, true);
        StringBuilder sb = new StringBuilder();
        sb.append("day_");
        sb.append(z);
        sb.append(ShutterSound.read());
        hashMap.put("attr_edit_sound", sb.toString());
        hashMap.put("attr_edit_tint", Integer.valueOf(TintColor.readColorId()));
        hashMap.put("attr_more_mode", Integer.valueOf(CameraSettings.getMoreModeStyle()));
        MistatsWrapper.settingSchedualEvent(hashMap);
    }

    private void uploadVideoSetting() {
        HashMap hashMap = new HashMap();
        Resources resources = this.mContext.getResources();
        hashMap.put(Setting.PARAM_SUB_MODULE, Setting.VALUE_SETTING_VIDEO_RECORD);
        hashMap.put(Setting.PARAM_MOVIE_SOLID, Boolean.valueOf(CameraSettings.isMovieSolidOn()));
        hashMap.put(Setting.PARAM_VIDEO_ENCODER, Integer.valueOf(CameraSettings.getVideoEncoder()));
        hashMap.put(Setting.PARAM_VIDEO_TIME_LAPSE_FRAME_INTERVAL, DataRepository.dataItemGlobal().getString(CameraSettings.KEY_VIDEO_TIME_LAPSE_FRAME_INTERVAL, resources.getString(R.string.pref_video_time_lapse_frame_interval_default)));
        MistatsWrapper.settingSchedualEvent(hashMap);
    }

    public void endRecord() {
        if (this.mInRecording) {
            uploadRecordSettingGlobal();
            uploadCaptureSetting();
            uploadVideoSetting();
            uploadAdvanceSetting();
            this.mInRecording = false;
        }
    }

    public void startRecord() {
        this.mInRecording = true;
    }
}
