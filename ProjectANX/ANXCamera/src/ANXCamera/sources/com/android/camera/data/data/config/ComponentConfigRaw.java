package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.util.SparseBooleanArray;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ComponentConfigRaw extends ComponentData {
    private SparseBooleanArray mIsClosed;

    public ComponentConfigRaw(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    @NonNull
    public String getDefaultValue(int i) {
        return null;
    }

    public int getDisplayTitleString() {
        return 0;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_camera_raw_key";
    }

    public boolean isClosed(int i) {
        SparseBooleanArray sparseBooleanArray = this.mIsClosed;
        if (sparseBooleanArray == null) {
            return false;
        }
        return sparseBooleanArray.get(i);
    }

    public boolean isSwitchOn(int i) {
        if (!isEmpty() && !isClosed(i)) {
            return this.mParentDataItem.getBoolean(getKey(i), false);
        }
        return false;
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        ArrayList arrayList = new ArrayList();
        if (i == 167 && cameraCapabilities.isSupportRaw() && C0122O00000o.instance().O0oo0O()) {
            arrayList.add(new ComponentDataItem((int) R.drawable.ic_raw_off, (int) R.drawable.ic_raw_on, -1, "on"));
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }

    public void setClosed(boolean z, int i) {
        if (this.mIsClosed == null) {
            this.mIsClosed = new SparseBooleanArray();
        }
        this.mIsClosed.put(i, z);
    }

    public void setRaw(int i, boolean z) {
        if (!isEmpty()) {
            setClosed(false, i);
            this.mParentDataItem.editor().putBoolean(getKey(i), z).apply();
        }
    }
}
