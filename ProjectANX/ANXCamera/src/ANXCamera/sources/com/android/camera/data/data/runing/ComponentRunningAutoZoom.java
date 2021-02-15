package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.ComponentData;
import java.util.List;

public class ComponentRunningAutoZoom extends ComponentData {
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";
    private int mCameraId;

    public ComponentRunningAutoZoom(DataItemRunning dataItemRunning) {
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
        return "pref_camera_auto_zoom";
    }

    public int getResText() {
        return R.string.autozoom_hint;
    }

    public boolean isNormalIntent() {
        return DataRepository.dataItemGlobal().isNormalIntent();
    }

    public boolean isSwitchOn(int i) {
        if (!C0122O00000o.instance().OO0oOOo() || this.mCameraId != 0 || i != 162 || !isNormalIntent()) {
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
