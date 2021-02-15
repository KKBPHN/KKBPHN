package com.android.camera.fragment.settings;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.android.camera.ModeEditorActivity;
import com.android.camera.MoreModeActivity;
import com.android.camera.R;
import com.android.camera.customization.ActivityCustomSound;
import com.android.camera.customization.CustomTintActivity;
import com.android.camera.customization.ShutterSound;
import com.android.camera.data.DataRepository;
import com.android.camera.log.Log;
import com.android.camera.statistic.MistatsConstants.CUSTOMIZE_CAMERA;
import com.android.camera.statistic.MistatsConstants.Setting;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.ui.ValuePreference;

public class CustomizationFragment extends BasePreferenceFragment {
    public static final String KEY_CUSTOM_MODE = "pref_custom_feature_layout";
    public static final String KEY_CUSTOM_SHUTTER_SOUND = "custom_shutter_sound_key";
    public static final String KEY_CUSTOM_TINT = "custom_tint_key";
    public static final String KEY_MORE_MODE = "pref_custom_more_mode";
    public static final String TAG = "Customization";
    private ValuePreference mCustomShutterSound;
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
        this.mCustomShutterSound = (ValuePreference) findPreference(KEY_CUSTOM_SHUTTER_SOUND);
        ValuePreference valuePreference = this.mCustomShutterSound;
        if (valuePreference != null) {
            valuePreference.setOnPreferenceClickListener(this);
            this.mCustomShutterSound.setValue(ShutterSound.readSoundName(getContext()));
            this.mCustomShutterSound.setDefaultValue(ShutterSound.readSoundName(getContext()));
        }
        ValuePreference valuePreference2 = (ValuePreference) findPreference(KEY_CUSTOM_TINT);
        if (valuePreference2 != null) {
            valuePreference2.setOnPreferenceClickListener(this);
        }
        boolean OOOOoo = C0122O00000o.instance().OOOOoo();
        String str = KEY_CUSTOM_MODE;
        if (OOOOoo || !DataRepository.dataItemGlobal().isNormalIntent()) {
            removePreference(this.mPreferenceGroup, str);
        } else {
            Preference findPreference = findPreference(str);
            if (findPreference != null) {
                findPreference.setOnPreferenceClickListener(this);
            }
        }
        boolean OOOOoo2 = C0122O00000o.instance().OOOOoo();
        String str2 = KEY_MORE_MODE;
        if (OOOOoo2 || !DataRepository.dataItemGlobal().isNormalIntent()) {
            removePreference(this.mPreferenceGroup, str2);
            return;
        }
        Preference findPreference2 = findPreference(str2);
        if (findPreference2 != null) {
            findPreference2.setOnPreferenceClickListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public int getPreferenceXml() {
        return R.xml.camera_preferences_customization;
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
                actionBar.setTitle(R.string.pref_customization_title);
            }
        }
        initializeActivity();
    }

    public boolean onPreferenceClick(Preference preference) {
        String str;
        String str2;
        String str3;
        String key = preference.getKey();
        if (TextUtils.isEmpty(key)) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("onPreferenceClick: key=");
        sb.append(key);
        Log.u(TAG, sb.toString());
        char c = 65535;
        switch (key.hashCode()) {
            case -1531632123:
                if (key.equals(KEY_CUSTOM_MODE)) {
                    c = 0;
                    break;
                }
                break;
            case -1278534757:
                if (key.equals(KEY_MORE_MODE)) {
                    c = 1;
                    break;
                }
                break;
            case -1172234199:
                if (key.equals(KEY_CUSTOM_TINT)) {
                    c = 3;
                    break;
                }
                break;
            case 1650712591:
                if (key.equals(KEY_CUSTOM_SHUTTER_SOUND)) {
                    c = 2;
                    break;
                }
                break;
        }
        String str4 = "from_where";
        String str5 = "StartActivityWhenLocked";
        if (c == 0) {
            Intent intent = new Intent(getActivity(), ModeEditorActivity.class);
            intent.putExtra(str4, this.mFromWhere);
            if (getActivity().getIntent().getBooleanExtra(str5, false)) {
                intent.putExtra(str5, true);
            }
            startActivity(intent);
            str = CUSTOMIZE_CAMERA.PREF_KEY_CUSTOM_MODE_SETTING;
        } else if (c != 1) {
            if (c != 2) {
                if (c == 3) {
                    Intent intent2 = new Intent(getActivity(), CustomTintActivity.class);
                    intent2.putExtra(str4, this.mFromWhere);
                    if (getActivity().getIntent().getBooleanExtra(str5, false)) {
                        intent2.putExtra(str5, true);
                    }
                    startActivity(intent2);
                    str2 = "attr_edit_tint";
                    str3 = Setting.CUSTOM_TINT_OUTTER;
                }
                return false;
            }
            Intent intent3 = new Intent(getActivity(), ActivityCustomSound.class);
            intent3.putExtra(str4, this.mFromWhere);
            if (getActivity().getIntent().getBooleanExtra(str5, false)) {
                intent3.putExtra(str5, true);
            }
            startActivity(intent3);
            str2 = "attr_edit_sound";
            str3 = Setting.CUSTOM_SOUND_OUTTER;
            MistatsWrapper.settingClickEvent(str2, str3);
            MistatsWrapper.customizeCameraSettingClick(str2);
            return true;
        } else {
            Intent intent4 = new Intent(getActivity(), MoreModeActivity.class);
            intent4.putExtra(str4, this.mFromWhere);
            intent4.putExtra(":miui:starting_window_label", getResources().getString(R.string.pref_more_mode_style_title));
            if (getActivity().getIntent().getBooleanExtra(str5, false)) {
                intent4.putExtra(str5, true);
            }
            startActivity(intent4);
            str = "attr_more_mode";
        }
        MistatsWrapper.customizeCameraSettingClick(str);
        return false;
    }

    public void onRestart() {
        initializeActivity();
    }

    /* access modifiers changed from: protected */
    public void updateConflictPreference(Preference preference) {
    }
}
