package com.android.camera.fragment.settings;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.app.KeyguardManager;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.Preference.OnPreferenceChangeListener;
import androidx.preference.Preference.OnPreferenceClickListener;
import androidx.preference.PreferenceGroup;
import com.android.camera.CameraApplicationDelegate;
import com.android.camera.CameraSettings;
import com.android.camera.SettingUiState;
import com.android.camera.ThermalHelper;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.android.camera.preferences.CameraSettingPreferences;
import com.android.camera.statistic.MistatsWrapper;
import com.android.camera.statistic.SettingRecord;
import com.android.camera.ui.PreviewListPreference;
import miuix.preference.PreferenceFragment;

public abstract class BasePreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener, OnPreferenceClickListener {
    public static final String FROM_WHERE = "from_where";
    private static final String TAG = "BasePreferenceFragment";
    protected int mFromWhere;
    protected CameraSettingPreferences mPreferences;

    /* access modifiers changed from: protected */
    public void dealPreferenceUiState(PreferenceGroup preferenceGroup, String str, SettingUiState settingUiState) {
        Preference findPreference = preferenceGroup.findPreference(str);
        if (findPreference != null) {
            if (settingUiState.isRomove) {
                if (preferenceGroup.removePreference(findPreference)) {
                    return;
                }
            } else if (settingUiState.isMutexEnable) {
                findPreference.setEnabled(false);
                return;
            } else {
                findPreference.setEnabled(true);
                return;
            }
        }
        int preferenceCount = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                dealPreferenceUiState((PreferenceGroup) preference, str, settingUiState);
            }
        }
    }

    public abstract int getPreferenceXml();

    /* access modifiers changed from: protected */
    public boolean isFromSecureKeyguard() {
        if (startFromKeyGuard()) {
            return ((KeyguardManager) getActivity().getSystemService("keyguard")).isKeyguardSecure();
        }
        return false;
    }

    public abstract void onBackPressed();

    public void onCreatePreferences(Bundle bundle, String str) {
        MistatsWrapper.initialize(CameraApplicationDelegate.getAndroidContext());
        Util.updateDeviceConfig(getContext());
        this.mPreferences = CameraSettingPreferences.instance();
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getActivity().getApplication());
        }
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        onSettingChanged(1);
        Editor edit = this.mPreferences.edit();
        String key = preference.getKey();
        if (!TextUtils.isEmpty(key) && obj != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("onPreferenceChange: key=");
            sb.append(key);
            sb.append(", newValue=");
            sb.append(obj);
            Log.u(str, sb.toString());
        }
        if (obj instanceof String) {
            edit.putString(key, (String) obj);
        } else if (obj instanceof Boolean) {
            edit.putBoolean(key, ((Boolean) obj).booleanValue());
        } else if (obj instanceof Integer) {
            edit.putInt(key, ((Integer) obj).intValue());
        } else if (obj instanceof Long) {
            edit.putLong(key, ((Long) obj).longValue());
        } else if (obj instanceof Float) {
            edit.putFloat(key, ((Float) obj).floatValue());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unhandled new value with type=");
            sb2.append(obj.getClass().getName());
            throw new IllegalStateException(sb2.toString());
        }
        edit.apply();
        MistatsWrapper.settingClickEvent(SettingRecord.getMistatString(key), obj);
        if (Util.isAccessible() && (preference instanceof PreviewListPreference)) {
            PreviewListPreference previewListPreference = (PreviewListPreference) preference;
            previewListPreference.setSummary((CharSequence) previewListPreference.getEntryByIndex(previewListPreference.findIndexOfValue(String.valueOf(obj))));
        }
        updateConflictPreference(preference);
        return true;
    }

    public abstract void onRestart();

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getActivity().getApplication());
        }
    }

    /* access modifiers changed from: protected */
    public void onSettingChanged(int i) {
        CameraSettings.sCameraChangeManager.request(i);
    }

    /* access modifiers changed from: protected */
    public void registerListener(PreferenceGroup preferenceGroup, OnPreferenceChangeListener onPreferenceChangeListener) {
        int preferenceCount = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if (preference instanceof PreferenceGroup) {
                registerListener((PreferenceGroup) preference, onPreferenceChangeListener);
            } else {
                preference.setOnPreferenceChangeListener(onPreferenceChangeListener);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void removeFromGroup(Preference preference, String str) {
        if (preference != null && (preference instanceof PreferenceGroup)) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            if (preferenceGroup.getPreferenceCount() > 0) {
                removePreference(preferenceGroup, str);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean removePreference(PreferenceGroup preferenceGroup, String str) {
        Preference findPreference = preferenceGroup.findPreference(str);
        if (findPreference != null && preferenceGroup.removePreference(findPreference)) {
            return true;
        }
        int preferenceCount = preferenceGroup.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            Preference preference = preferenceGroup.getPreference(i);
            if ((preference instanceof PreferenceGroup) && removePreference((PreferenceGroup) preference, str)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean startFromKeyGuard() {
        return getActivity().getIntent().getBooleanExtra("StartActivityWhenLocked", false);
    }

    public abstract void updateConflictPreference(Preference preference);

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
