package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import com.android.camera.data.data.ComponentData;
import com.android.camera2.CameraCapabilities;
import java.util.List;

public class ComponentConfigLiveShot extends ComponentData {
    private static final String TAG = "ComponentConfigLiveShot";
    private boolean mIsClosed;
    private boolean mSupported;

    public ComponentConfigLiveShot(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    public String getDefaultValue(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append("#getDefaultValue() not supported");
        throw new UnsupportedOperationException(sb.toString());
    }

    public int getDisplayTitleString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append("#getDisplayTitleString() not supported");
        throw new UnsupportedOperationException(sb.toString());
    }

    public List getItems() {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append("#getItems() not supported");
        throw new UnsupportedOperationException(sb.toString());
    }

    public String getKey(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append("#getKey() not supported");
        throw new UnsupportedOperationException(sb.toString());
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public boolean isSwitchOn(int i) {
        if (this.mSupported && !isClosed()) {
            return this.mParentDataItem.getBoolean("pref_live_shot_enabled", false);
        }
        return false;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        this.mSupported = false;
        if (C0122O00000o.instance().OOO0o0o()) {
            if (i == 163 || i == 165) {
                this.mSupported = true;
            } else {
                this.mSupported = false;
            }
        }
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }

    public void setLiveShotOn(boolean z) {
        setClosed(false);
        this.mParentDataItem.editor().putBoolean("pref_live_shot_enabled", z).apply();
    }
}
