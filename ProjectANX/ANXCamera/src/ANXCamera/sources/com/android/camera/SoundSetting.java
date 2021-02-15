package com.android.camera;

import android.content.Context;
import android.media.AudioManager;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;

public class SoundSetting {
    private static final String AUDIO_CAMERA_ENHANCE = "misound_audio_camera_enhance";
    private static final String AUDIO_CAMERA_ENHANCE_SUPPORT = "audio_camera_enhance_support";
    private static final String AUDIO_CAMERA_ENHANCE_TRUE = "audio_camera_enhance_support=true";
    private static final String AUDIO_CAMERA_GAIN = "misound_audio_camera_gain";
    private static final String AUDIO_CAMERA_GAIN_SUPPORT = "audio_camera_gain_support";
    private static final String AUDIO_CAMERA_GAIN_TRUE = "audio_camera_gain_support=true";
    private static final String AUDIO_CAMERA_KARAOKE_TRUE = "audio_karaoke_support=true";
    private static final String AUDIO_CAMERA_NS = "misound_audio_camera_ns";
    private static final String AUDIO_CAMERA_NS_SUPPORT = "audio_camera_ns_support";
    private static final String AUDIO_CAMERA_NS_TRUE = "audio_camera_ns_support=true";
    private static final String AUDIO_KARAOKE_ENABLE = "audio_karaoke_enable";
    private static final String AUDIO_KARAOKE_KTVMODE = "audio_karaoke_ktvmode";
    private static final String AUDIO_KARAOKE_SUPPORT = "audio_karaoke_support";
    private static final String AUDIO_KARAOKE_VOLUME = "audio_karaoke_volume";
    private static String TAG = "SoundSetting";
    private static final int VOLUME_MAX_APP = 15;
    private static final int VOLUME_MIN_APP = 0;

    public static void closeKaraokeEquipment(Context context, int i) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (isSupportKaraoke(context, i)) {
            Log.d(TAG, "openKaraokeEquipment:->disabled");
            audioManager.setParameters("audio_karaoke_ktvmode=disable");
        }
    }

    public static void closeKaraokeState(Context context, int i) {
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_KARAOKE_REDUCTION, false);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("KARAOKE_ISON  Karaoke->:");
        sb.append(z);
        Log.d(str, sb.toString());
        if (isSupportKaraoke(context, i)) {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (z) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" AUDIO_KARAOKE_SUPPORT Karaoke Support->:");
                sb2.append(isSupportKaraoke(context, i));
                Log.d(str2, sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("audio_karaoke_enable=");
                sb3.append(0);
                audioManager.setParameters(sb3.toString());
            }
        }
    }

    public static boolean isStartKaraoke(Context context, int i) {
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_KARAOKE_REDUCTION, false);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("KARAOKE_ISON   isStartKaraoke:->");
        sb.append(z);
        Log.d(str, sb.toString());
        if (!isSupportKaraoke(context, i) || !z) {
            return false;
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" AUDIO_KARAOKE_SUPPORT Karaoke Support   isStartKaraoke->:");
        sb2.append(isSupportKaraoke(context, i));
        Log.d(str2, sb2.toString());
        return true;
    }

    public static boolean isSupportEnhance(Context context, int i) {
        String parameters = ((AudioManager) context.getSystemService("audio")).getParameters(AUDIO_CAMERA_ENHANCE_SUPPORT);
        if (AUDIO_CAMERA_ENHANCE_TRUE.equals(parameters)) {
            return i == 162 || i == 180 || i == 204;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append(" AUDIO_KARAOKE_SUPPORT Karaoke Support->:");
        sb.append(parameters);
        Log.d(str, sb.toString());
        return false;
    }

    public static boolean isSupportGain(Context context) {
        return AUDIO_CAMERA_GAIN_TRUE.equals(((AudioManager) context.getSystemService("audio")).getParameters(AUDIO_CAMERA_GAIN_SUPPORT));
    }

    public static boolean isSupportKaraoke(Context context, int i) {
        if (!AUDIO_CAMERA_KARAOKE_TRUE.equals(((AudioManager) context.getSystemService("audio")).getParameters(AUDIO_KARAOKE_SUPPORT))) {
            return false;
        }
        return i == 162 || i == 180 || i == 204;
    }

    public static boolean isSupportNs(Context context, int i) {
        if (!AUDIO_CAMERA_NS_TRUE.equals(((AudioManager) context.getSystemService("audio")).getParameters(AUDIO_CAMERA_NS_SUPPORT))) {
            return false;
        }
        return i == 162 || i == 180 || i == 204;
    }

    public static void openKaraokeEquipment(Context context, int i) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (isSupportKaraoke(context, i)) {
            Log.d(TAG, "openKaraokeEquipment ->:enable");
            audioManager.setParameters("audio_karaoke_ktvmode=enable");
        }
    }

    public static void openKaraokeState(Context context, int i) {
        setMicVolParam(context, 15);
        boolean z = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_KARAOKE_REDUCTION, false);
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("KARAOKE_ISON  Karaoke ->:");
        sb.append(z);
        Log.d(str, sb.toString());
        if (isSupportKaraoke(context, i)) {
            AudioManager audioManager = (AudioManager) context.getSystemService("audio");
            if (z) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" AUDIO_KARAOKE_SUPPORT Karaoke Support->:");
                sb2.append(isSupportKaraoke(context, i));
                Log.d(str2, sb2.toString());
                StringBuilder sb3 = new StringBuilder();
                sb3.append("audio_karaoke_enable=");
                sb3.append(1);
                audioManager.setParameters(sb3.toString());
            }
        }
    }

    public static void setGainState(Context context, String str) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (isSupportGain(context)) {
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("AUDIO_CAMERA_GAIN GainValue10.14->:");
            sb.append(str);
            Log.d(str2, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("misound_audio_camera_gain=");
            sb2.append(str);
            audioManager.setParameters(sb2.toString());
        }
    }

    public static void setMicVolParam(Context context, int i) {
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (i > 15) {
            i = 15;
        }
        if (i < 0) {
            i = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("audio_karaoke_volume=");
        sb.append(i);
        audioManager.setParameters(sb.toString());
    }

    public static void setNoiseReductionState(Context context, int i, boolean z) {
        String str;
        boolean z2 = DataRepository.dataItemGlobal().getBoolean(CameraSettings.KEY_INTELLIGENT_NOISE_REDUCTION, false);
        AudioManager audioManager = (AudioManager) context.getSystemService("audio");
        if (z) {
            String str2 = "true;";
            String str3 = "false;";
            if (isSupportEnhance(context, i)) {
                StringBuilder sb = new StringBuilder();
                sb.append("misound_audio_camera_enhance=");
                sb.append(z2 ? str2 : str3);
                audioManager.setParameters(sb.toString());
            }
            if (isSupportNs(context, i)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("misound_audio_camera_ns=");
                if (!z2) {
                    str2 = str3;
                }
                sb2.append(str2);
                str = sb2.toString();
            } else {
                return;
            }
        } else {
            if (isSupportEnhance(context, i)) {
                audioManager.setParameters("misound_audio_camera_enhance=false;");
            }
            if (isSupportNs(context, i)) {
                str = "misound_audio_camera_ns=false;";
            } else {
                return;
            }
        }
        audioManager.setParameters(str);
    }
}
