package com.android.camera.module.loader;

import com.android.camera.CameraSettings;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.data.provider.DataProvider.ProviderEditor;
import com.android.camera.module.BaseModule;
import com.android.camera2.CameraCapabilities;
import io.reactivex.functions.Action;
import java.lang.ref.WeakReference;

public class ActionUpdateLensDirtyDetect implements Action {
    private boolean mIsOnCreate;
    private WeakReference mModuleWeakReference;

    public ActionUpdateLensDirtyDetect(BaseModule baseModule, boolean z) {
        this.mModuleWeakReference = new WeakReference(baseModule);
        this.mIsOnCreate = z;
    }

    public void run() {
        BaseModule baseModule = (BaseModule) this.mModuleWeakReference.get();
        if (baseModule != null) {
            if (this.mIsOnCreate) {
                CameraSettings.setLensDirtyDetectEnable(false);
            } else {
                CameraCapabilities cameraCapabilities = baseModule.getCameraCapabilities();
                if (cameraCapabilities == null || cameraCapabilities.getMiAlgoASDVersion() < 2.0f) {
                    CameraSettings.addLensDirtyDetectedTimes();
                } else {
                    DataItemConfig dataNormalItemConfig = DataRepository.dataNormalItemConfig();
                    ProviderEditor editor = dataNormalItemConfig.editor();
                    editor.putBoolean(CameraSettings.KEY_LENS_DIRTY_DETECT_ENABLED, false);
                    String str = CameraSettings.KEY_LENS_DIRTY_DETECT_TIMES;
                    if (dataNormalItemConfig.contains(str)) {
                        dataNormalItemConfig.remove(str);
                    }
                    String str2 = CameraSettings.KEY_LENS_DIRTY_DETECT_DATE;
                    if (dataNormalItemConfig.contains(str2)) {
                        dataNormalItemConfig.remove(str2);
                    }
                    editor.apply();
                }
            }
            baseModule.updatePreferenceTrampoline(61);
            baseModule.updateLensDirtyDetect(true);
        }
    }
}
