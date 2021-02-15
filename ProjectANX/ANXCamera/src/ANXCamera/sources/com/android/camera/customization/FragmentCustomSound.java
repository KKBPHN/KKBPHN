package com.android.camera.customization;

import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.camera.R;
import com.android.camera.fragment.settings.BasePreferenceFragment;
import com.android.camera.fragment.settings.CustomizationFragment;
import com.android.camera.log.Log;

public class FragmentCustomSound extends BasePreferenceFragment {
    public static final String TAG = "FragmentCustomSound";
    private PreferenceCustomSound mCustomSound;
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
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preferences_custom_sound;
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
                actionBar.setTitle(R.string.pref_custom_shutter_sound_title);
            }
        }
        initializeActivity();
        this.mCustomSound = (PreferenceCustomSound) this.mPreferenceGroup.findPreference(CustomizationFragment.KEY_CUSTOM_SHUTTER_SOUND);
    }

    public void onDestroy() {
        super.onDestroy();
        PreferenceCustomSound preferenceCustomSound = this.mCustomSound;
        if (preferenceCustomSound != null) {
            preferenceCustomSound.onDestroy();
        }
    }

    public void onPause() {
        super.onPause();
    }

    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    public void onRestart() {
    }

    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }

    public void updatePreferences(PreferenceGroup preferenceGroup, SharedPreferences sharedPreferences) {
        super.updatePreferences(preferenceGroup, sharedPreferences);
    }
}
