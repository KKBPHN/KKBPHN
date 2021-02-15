package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import java.util.List;

public class ComponentRunningSubtitle extends ComponentData {
    public static final String VALUE_OFF = "OFF";
    public static final String VALUE_ON = "ON";
    private boolean mIsNormalIntent;

    public ComponentRunningSubtitle(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    public void clearArrayMap() {
        String str = "OFF";
        setComponentValue(162, str);
        setComponentValue(214, str);
    }

    @NonNull
    public String getDefaultValue(int i) {
        return Boolean.toString(false);
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return null;
    }

    public String getKey(int i) {
        if (i == 162 || i == 169) {
            return "pref_video_subtitle_key";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("pref_video_subtitle_key_");
        sb.append(Integer.toHexString(i));
        return sb.toString();
    }

    public int getResText() {
        return R.string.pref_video_subtitle;
    }

    public boolean isSwitchOn(int i) {
        if (!C0122O00000o.instance().OOOOOoo()) {
            return false;
        }
        if ((i != 162 && i != 214) || !this.mIsNormalIntent) {
            return false;
        }
        return "ON".equals(getComponentValue(i));
    }

    public void reInit(int i, boolean z) {
        this.mIsNormalIntent = z;
    }

    public void reInitIntentType(boolean z) {
        this.mIsNormalIntent = z;
    }

    public void setEnabled(int i, boolean z) {
        if (i == 162 || i == 204 || i == 214) {
            setComponentValue(i, z ? "ON" : "OFF");
        }
    }
}
