package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.runing.DataItemRunning;
import java.util.List;

public class ComponentRunningMacroMode extends ComponentData {
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";
    public List mItems;

    public ComponentRunningMacroMode(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private String getMode(int i, int i2) {
        if (165 == i || 186 == i) {
            i = 163;
        }
        if (169 == i) {
            i = 162;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append("_");
        sb.append(i2);
        return sb.toString();
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
        return "pref_camera_macro_scene_mode_key";
    }

    public int getResText() {
        return C0122O00000o.instance().OO0o0o() ? R.string.super_macro_mode : R.string.macro_mode;
    }

    public boolean isSwitchOn(int i) {
        return "ON".equals(getComponentValue(i));
    }

    public void reInit(int i, boolean z) {
    }

    public void setSwitchOff(int i) {
        setComponentValue(i, "OFF");
    }

    public void setSwitchOn(int i) {
        setComponentValue(i, "ON");
    }
}
