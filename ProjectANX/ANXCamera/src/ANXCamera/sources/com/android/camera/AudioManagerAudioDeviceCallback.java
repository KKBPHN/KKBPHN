package com.android.camera;

import android.media.AudioDeviceCallback;
import android.media.AudioDeviceInfo;
import com.android.camera.log.Log;

public class AudioManagerAudioDeviceCallback extends AudioDeviceCallback {
    private static final String TAG = "AudioManagerAudioDeviceCallback";
    private OnAudioDeviceChangeListener mListener;

    public interface OnAudioDeviceChangeListener {
        void onAudioDeviceChangeListener(boolean z);
    }

    private void onAudioDevicesAddedOrRemoved(AudioDeviceInfo[] audioDeviceInfoArr, boolean z) {
        if (this.mListener != null) {
            boolean z2 = true;
            boolean z3 = z || !Util.isWiredHeadsetOn();
            int length = audioDeviceInfoArr.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z2 = false;
                    break;
                }
                AudioDeviceInfo audioDeviceInfo = audioDeviceInfoArr[i];
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Audio device added or removed: ");
                sb.append(audioDeviceInfo.getProductName());
                sb.append(" type: ");
                sb.append(audioDeviceInfo.getType());
                sb.append(" isAdd: ");
                sb.append(z);
                sb.append(" canBreakFlag: ");
                sb.append(z3);
                Log.d(str, sb.toString());
                if (Util.isWiredAudioHeadset(audioDeviceInfo) && z3) {
                    break;
                }
                i++;
            }
            String str2 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("onAudioDevicesAddedOrRemoved: ");
            sb2.append(z2);
            Log.d(str2, sb2.toString());
            this.mListener.onAudioDeviceChangeListener(z2);
        }
    }

    public void onAudioDevicesAdded(AudioDeviceInfo[] audioDeviceInfoArr) {
        onAudioDevicesAddedOrRemoved(audioDeviceInfoArr, true);
    }

    public void onAudioDevicesRemoved(AudioDeviceInfo[] audioDeviceInfoArr) {
        onAudioDevicesAddedOrRemoved(audioDeviceInfoArr, false);
    }

    public void setOnAudioDeviceChangeListener(OnAudioDeviceChangeListener onAudioDeviceChangeListener) {
        this.mListener = onAudioDeviceChangeListener;
    }
}
