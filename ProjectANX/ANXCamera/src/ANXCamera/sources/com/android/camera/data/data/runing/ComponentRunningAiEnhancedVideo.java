package com.android.camera.data.data.runing;

import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import java.util.List;

public class ComponentRunningAiEnhancedVideo extends ComponentData {
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";
    private int mCameraId;

    public ComponentRunningAiEnhancedVideo(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    @NonNull
    public String getDefaultValue(int i) {
        return "OFF";
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return null;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_AI_ENHANCED_VIDEO;
    }

    public int getResText() {
        return R.string.pref_camera_video_ai_scene_title;
    }

    public boolean isNormalIntent() {
        return DataRepository.dataItemGlobal().isNormalIntent();
    }

    public boolean isSwitchOn(int i) {
        if (this.mCameraId != 0 || i != 162 || !isNormalIntent()) {
            return false;
        }
        return "ON".equals(getComponentValue(i));
    }

    public void reInit(int i) {
        this.mCameraId = i;
    }

    public void setEnabled(int i, boolean z) {
        setComponentValue(i, z ? "ON" : "OFF");
    }
}
