package com.android.camera.fragment.settings;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.log.Log;

public class PhotoAssistanceTipsFragment extends BasePreferenceFragment {
    public static final String TAG = "PhotoAssistanceTipsFragment";
    protected PreferenceScreen mPreferenceGroup;

    private void filterByFeature() {
        if (!C0122O00000o.instance().OOOO0oO()) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_PICTURE_FLAW_TIP);
        }
        boolean OOO0o00 = C0122O00000o.instance().OOO0o00();
        String str = CameraSettings.KEY_LENS_DIRTY_TIP;
        if (!OOO0o00) {
            removePreference(this.mPreferenceGroup, str);
        } else {
            CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mPreferenceGroup.findPreference(str);
            boolean O0oo00 = C0122O00000o.instance().O0oo00();
            if (checkBoxPreference != null) {
                checkBoxPreference.setChecked(O0oo00);
            }
        }
        if (!C0122O00000o.instance().OOo0o0()) {
            removePreference(this.mPreferenceGroup, CameraSettings.KEY_CAMERA_LYING_TIP_SWITCH);
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
            Log.e(TAG, "fail to init PreferenceGroup");
            getActivity().finish();
        }
        filterByFeature();
        registerListener(this.mPreferenceGroup, this);
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preferences_photo_assistance_tips;
    }

    public void onBackPressed() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false)) {
            getActivity().setShowWhenLocked(true);
        }
        if (getActivity().getIntent().getCharSequenceExtra(":miui:starting_window_label") != null) {
            ActionBar actionBar = getActivity().getActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.photo_assistance_tips_title);
            }
        }
        initializeActivity();
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return super.onPreferenceChange(preference, obj);
    }

    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    public void onRestart() {
        updatePreferences(this.mPreferenceGroup, this.mPreferences);
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }

    public void updatePreferences(PreferenceGroup preferenceGroup, SharedPreferences sharedPreferences) {
        if (preferenceGroup != null) {
            int preferenceCount = preferenceGroup.getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                Preference preference = preferenceGroup.getPreference(i);
                if (preference instanceof CheckBoxPreference) {
                    CheckBoxPreference checkBoxPreference = (CheckBoxPreference) preference;
                    checkBoxPreference.setChecked(sharedPreferences.getBoolean(checkBoxPreference.getKey(), checkBoxPreference.isChecked()));
                    preference.setPersistent(false);
                } else if (preference instanceof PreferenceGroup) {
                    updatePreferences((PreferenceGroup) preference, sharedPreferences);
                } else {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("no need update preference for ");
                    sb.append(preference.getKey());
                    Log.v(str, sb.toString());
                }
            }
        }
    }
}
