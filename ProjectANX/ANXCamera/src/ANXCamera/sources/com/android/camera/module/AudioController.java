package com.android.camera.module;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import com.android.camera.log.Log;

public class AudioController {
    private static final String TAG = "AudioController";
    private AudioManager mAudioManager;
    private Context mContext;
    private int mOldControlStream = -1;

    public AudioController(Context context) {
        this.mContext = context.getApplicationContext();
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
    }

    public void requestMusicSteam(Activity activity) {
        silenceAudio();
        this.mOldControlStream = activity.getVolumeControlStream();
        activity.setVolumeControlStream(3);
    }

    public void restoreAudio() {
        Log.d(TAG, "restoreAudio: ");
        this.mAudioManager.abandonAudioFocus(null);
    }

    public void restoreMusicSteam(Activity activity) {
        restoreAudio();
        int i = this.mOldControlStream;
        if (i != -1) {
            activity.setVolumeControlStream(i);
        }
    }

    public void silenceAudio() {
        Log.d(TAG, "silenceAudio: ");
        this.mAudioManager.requestAudioFocus(null, 3, 2);
    }

    public void stopAudio() {
        Log.d(TAG, "stopAudio: ");
        this.mAudioManager.requestAudioFocus(null, 3, 1);
    }
}
