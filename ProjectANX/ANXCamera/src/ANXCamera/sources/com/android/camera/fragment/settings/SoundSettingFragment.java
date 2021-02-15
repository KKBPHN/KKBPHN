package com.android.camera.fragment.settings;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.log.Log;
import com.android.camera.preferences.EffectComparisonPreference;

public class SoundSettingFragment extends BasePreferenceFragment implements TextWatcher {
    public static final String TAG = "NoiseReductionFragment";
    private EffectComparisonPreference mEffectPreference;
    private boolean mLocked = false;
    protected PreferenceScreen mPreferenceGroup;

    private void initializeActivity() {
        this.mPreferenceGroup = getPreferenceScreen();
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        if (preferenceScreen != null) {
            preferenceScreen.removeAll();
        }
        addPreferencesFromResource(getPreferenceXml());
        this.mPreferenceGroup = getPreferenceScreen();
        if (this.mPreferenceGroup == null) {
            Log.e(TAG, "fail to init PreferenceGroup");
            getActivity().finish();
        }
        registerListener();
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    private void registerListener() {
        registerListener(this.mPreferenceGroup, this);
        Preference findPreference = this.mPreferenceGroup.findPreference(CameraSettings.KEY_INTELLIGENT_NOISE_REDUCTION);
        if (findPreference != null) {
            findPreference.setOnPreferenceClickListener(this);
        }
        Preference findPreference2 = this.mPreferenceGroup.findPreference(CameraSettings.KEY_KARAOKE_REDUCTION);
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(this);
        }
        PreferenceScreen preferenceScreen = this.mPreferenceGroup;
        String str = CameraSettings.KEY_EAR_PHONE_RADIO;
        Preference findPreference3 = preferenceScreen.findPreference(str);
        if (findPreference3 != null) {
            findPreference3.setOnPreferenceClickListener(this);
        }
        if (CameraSettings.getCameraBluetoothHeadsetNeedRemove()) {
            removePreference(this.mPreferenceGroup, str);
        }
    }

    public void afterTextChanged(Editable editable) {
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preference_noise_reduction;
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
                actionBar.setTitle(R.string.pref_n_s_a);
            }
        }
        initializeActivity();
        this.mEffectPreference = (EffectComparisonPreference) this.mPreferenceGroup.findPreference(CameraSettings.KEY_CAMERA_EFFECT_COMPARISON);
    }

    public void onPause() {
        super.onPause();
        EffectComparisonPreference effectComparisonPreference = this.mEffectPreference;
        if (effectComparisonPreference != null) {
            effectComparisonPreference.onPause();
        }
    }

    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    public void onRestart() {
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    public void onResume() {
        super.onResume();
        EffectComparisonPreference effectComparisonPreference = this.mEffectPreference;
        if (effectComparisonPreference != null) {
            effectComparisonPreference.onResume();
        }
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }

    public void updatePreferences(PreferenceGroup preferenceGroup, SharedPreferences sharedPreferences) {
        super.updatePreferences(preferenceGroup, sharedPreferences);
    }
}
