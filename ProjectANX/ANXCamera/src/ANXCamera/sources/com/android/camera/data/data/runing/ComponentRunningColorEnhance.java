package com.android.camera.data.data.runing;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.DataItemBase;
import com.android.camera2.CameraCapabilities;
import java.util.List;

public class ComponentRunningColorEnhance extends ComponentData {
    private boolean mIsEnabled = false;
    private boolean mRecordValue = false;

    public ComponentRunningColorEnhance(DataItemBase dataItemBase) {
        super(dataItemBase);
    }

    private boolean getValue(int i, int i2, CameraCapabilities cameraCapabilities) {
        if (cameraCapabilities == null) {
            return false;
        }
        if (i != 163 && i != 165) {
            return false;
        }
        if (!(i2 == 0) || !cameraCapabilities.isSupportedColorEnhance() || !supportColorEnhance()) {
            return false;
        }
        return this.mRecordValue;
    }

    @NonNull
    public String getDefaultValue(int i) {
        return String.valueOf(false);
    }

    public int getDisplayTitleString() {
        return R.string.pro_color_mode;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_color_enhance";
    }

    public int getResIcon(boolean z) {
        return z ? R.drawable.ic_color_enhance_on : R.drawable.ic_color_enhance_off;
    }

    public boolean isEnabled(int i) {
        if (i == 163 || i == 165) {
            return this.mIsEnabled;
        }
        return false;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        this.mIsEnabled = getValue(i, i2, cameraCapabilities);
    }

    public void reset(boolean z) {
        this.mIsEnabled = z;
        this.mRecordValue = z;
    }

    public void setEnabled(boolean z, int i) {
        this.mIsEnabled = z;
        if (i == 1) {
            this.mRecordValue = this.mIsEnabled;
        }
    }

    public boolean supportColorEnhance() {
        return C0122O00000o.instance().supportColorEnhance();
    }
}
