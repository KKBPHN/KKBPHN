package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.data.data.ComponentData;
import java.util.List;

public class ComponentRunningSuperEIS extends ComponentData {
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";
    private boolean mIsNormalIntent;

    public ComponentRunningSuperEIS(DataItemRunning dataItemRunning) {
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
        if (i == 162 || i == 169 || i == 204) {
            return "pref_camera_super_eis";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("pref_camera_super_eis_");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }

    public boolean isEnabled(int i) {
        if (!C0122O00000o.instance().OOOOo00() || !this.mIsNormalIntent) {
            return false;
        }
        return "ON".equals(getComponentValue(i));
    }

    public void reInit(int i, boolean z) {
        this.mIsNormalIntent = z;
    }

    public void setEnabled(int i, boolean z) {
        setComponentValue(i, z ? "ON" : "OFF");
    }
}
