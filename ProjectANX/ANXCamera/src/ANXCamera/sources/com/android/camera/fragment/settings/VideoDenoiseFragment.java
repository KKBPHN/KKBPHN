package com.android.camera.fragment.settings;

import android.app.ActionBar;
import android.media.AudioManager;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.camera.AudioManagerAudioDeviceCallback;
import com.android.camera.AudioManagerAudioDeviceCallback.OnAudioDeviceChangeListener;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.SettingUiState;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class VideoDenoiseFragment extends BasePreferenceFragment {
    public static final String TAG = "VideoDenoiseFragment";
    private OnAudioDeviceChangeListener mAudioDeviceChangeListener = new C0316O00000oo(this);
    private AudioManager mAudioManager;
    private AudioManagerAudioDeviceCallback mAudioManagerAudioDeviceCallback;
    private boolean mLocked = false;
    protected PreferenceScreen mPreferenceGroup;

    private void handleUIState() {
        if (this.mPreferenceGroup != null) {
            SettingUiState settingUiState = new SettingUiState();
            if (Util.isWiredHeadsetOn()) {
                settingUiState.isMutexEnable = true;
            }
            dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_WIND_DENOISE, settingUiState);
            dealPreferenceUiState(this.mPreferenceGroup, CameraSettings.KEY_FRONT_DENOISE, settingUiState);
        }
    }

    private void initializeActivity() {
        this.mPreferenceGroup = getPreferenceScreen();
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        addPreferencesFromResource(getPreferenceXml());
        this.mPreferenceGroup = getPreferenceScreen();
        if (this.mPreferenceGroup == null) {
            Log.e("VideoDenoiseFragment", "fail to init PreferenceGroup");
            getActivity().finish();
        }
        registerListener();
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    private void registerListener() {
        registerListener(this.mPreferenceGroup, this);
        Preference findPreference = this.mPreferenceGroup.findPreference(CameraSettings.KEY_FRONT_DENOISE);
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
        Preference findPreference2 = this.mPreferenceGroup.findPreference(CameraSettings.KEY_WIND_DENOISE);
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(this);
        }
    }

    public /* synthetic */ void O00000oO(boolean z) {
        if (z) {
            handleUIState();
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preferences_video_denoise;
    }

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mLocked = getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false);
        if (this.mLocked) {
            getActivity().setShowWhenLocked(true);
        }
        this.mFromWhere = getActivity().getIntent().getIntExtra("from_where", 0);
        if (getActivity().getIntent().getCharSequenceExtra(":miui:starting_window_label") != null) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.pref_audio_denoise_title);
            }
        }
        initializeActivity();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onPause() {
        super.onPause();
        AudioManager audioManager = this.mAudioManager;
        if (audioManager != null) {
            AudioManagerAudioDeviceCallback audioManagerAudioDeviceCallback = this.mAudioManagerAudioDeviceCallback;
            if (audioManagerAudioDeviceCallback != null) {
                audioManager.unregisterAudioDeviceCallback(audioManagerAudioDeviceCallback);
                this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(null);
            }
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        super.onPreferenceChange(preference, obj);
        return true;
    }

    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    public void onRestart() {
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    public void onResume() {
        super.onResume();
        this.mAudioManager = (AudioManager) getActivity().getSystemService("audio");
        this.mAudioManagerAudioDeviceCallback = new AudioManagerAudioDeviceCallback();
        this.mAudioManager.registerAudioDeviceCallback(this.mAudioManagerAudioDeviceCallback, null);
        this.mAudioManagerAudioDeviceCallback.setOnAudioDeviceChangeListener(this.mAudioDeviceChangeListener);
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }
}
