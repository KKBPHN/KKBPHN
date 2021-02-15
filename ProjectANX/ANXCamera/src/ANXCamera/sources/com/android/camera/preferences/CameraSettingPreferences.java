package com.android.camera.preferences;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import java.util.Map;
import java.util.Set;

public class CameraSettingPreferences implements SharedPreferences {
    public static final String FILE_NAME_GLOBAL = "camera_settings_global";
    public static final String FILE_NAME_PREFIX_SIMPLE_MODE_LOCAL = "camera_settings_simple_mode_local_";
    public static final String FILE_NAME_SIMPLE_MODE_GLOBAL = "camera_settings_simple_mode_global";
    private static CameraSettingPreferences sPreferences = new CameraSettingPreferences();

    class MyEditor implements Editor {
        private ProviderEditor mEditorConfig = DataRepository.dataItemConfig().editor();
        private ProviderEditor mEditorGlobal = DataRepository.dataItemGlobal().editor();
        private ProviderEditor mEditorRunning = DataRepository.dataItemRunning().editor();

        MyEditor() {
        }

        public void apply() {
            this.mEditorGlobal.apply();
            this.mEditorConfig.apply();
        }

        public Editor clear() {
            this.mEditorGlobal.clear();
            this.mEditorConfig.clear();
            this.mEditorRunning.clear();
            return this;
        }

        public boolean commit() {
            return this.mEditorGlobal.commit() && this.mEditorConfig.commit();
        }

        public Editor putBoolean(String str, boolean z) {
            ProviderEditor providerEditor = CameraSettings.isCameraSpecific(str) ? this.mEditorConfig : CameraSettings.isTransient(str) ? this.mEditorRunning : this.mEditorGlobal;
            providerEditor.putBoolean(str, z);
            return this;
        }

        public Editor putFloat(String str, float f) {
            ProviderEditor providerEditor = CameraSettings.isCameraSpecific(str) ? this.mEditorConfig : CameraSettings.isTransient(str) ? this.mEditorRunning : this.mEditorGlobal;
            providerEditor.putFloat(str, f);
            return this;
        }

        public Editor putInt(String str, int i) {
            ProviderEditor providerEditor = CameraSettings.isCameraSpecific(str) ? this.mEditorConfig : CameraSettings.isTransient(str) ? this.mEditorRunning : this.mEditorGlobal;
            providerEditor.putInt(str, i);
            return this;
        }

        public Editor putLong(String str, long j) {
            ProviderEditor providerEditor = CameraSettings.isCameraSpecific(str) ? this.mEditorConfig : CameraSettings.isTransient(str) ? this.mEditorRunning : this.mEditorGlobal;
            providerEditor.putLong(str, j);
            return this;
        }

        public Editor putString(String str, String str2) {
            ProviderEditor providerEditor = CameraSettings.isCameraSpecific(str) ? this.mEditorConfig : CameraSettings.isTransient(str) ? this.mEditorRunning : this.mEditorGlobal;
            providerEditor.putString(str, str2);
            return this;
        }

        public Editor putStringSet(String str, Set set) {
            throw new UnsupportedOperationException();
        }

        public Editor remove(String str) {
            this.mEditorGlobal.remove(str);
            this.mEditorConfig.remove(str);
            this.mEditorRunning.remove(str);
            return this;
        }
    }

    private CameraSettingPreferences() {
    }

    public static synchronized CameraSettingPreferences instance() {
        CameraSettingPreferences cameraSettingPreferences;
        synchronized (CameraSettingPreferences.class) {
            cameraSettingPreferences = sPreferences;
        }
        return cameraSettingPreferences;
    }

    public boolean contains(String str) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.contains(str);
    }

    public Editor edit() {
        return new MyEditor();
    }

    public Map getAll() {
        return null;
    }

    public boolean getBoolean(String str, boolean z) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.getBoolean(str, z);
    }

    public float getFloat(String str, float f) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.getFloat(str, f);
    }

    public int getInt(String str, int i) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.getInt(str, i);
    }

    public long getLong(String str, long j) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.getLong(str, j);
    }

    public String getString(String str, String str2) {
        DataItemBase dataItemGlobal = CameraSettings.isCameraSpecific(str) ? DataRepository.dataItemConfig() : CameraSettings.isTransient(str) ? DataRepository.dataItemRunning() : DataRepository.dataItemGlobal();
        return dataItemGlobal.getString(str, str2);
    }

    public Set getStringSet(String str, Set set) {
        throw new UnsupportedOperationException();
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
    }
}
