package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import androidx.annotation.NonNull;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentConfigAi extends ComponentData {
    private boolean mIsClosed;

    public ComponentConfigAi(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    public void clearClosed() {
        this.mIsClosed = false;
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
        return "pref_camera_ai_scene_mode_key";
    }

    public boolean isAiSceneOn(int i) {
        if (!isEmpty() && !isClosed()) {
            return this.mParentDataItem.getBoolean(getKey(i), C0122O00000o.instance().O0ooO00());
        }
        return false;
    }

    public boolean isClosed() {
        return this.mIsClosed;
    }

    public List reInit(int i, int i2, CameraCapabilities cameraCapabilities, int i3) {
        ComponentDataItem componentDataItem;
        ArrayList arrayList = new ArrayList();
        if ((i == 163 || i == 165 || (i == 171 ? C0122O00000o.instance().OOoO00() : i == 205)) && (!cameraCapabilities.isSupportLightTripartite() || i3 == 0)) {
            String str = "on";
            if (i2 == 0) {
                if (C0122O00000o.instance().OO0oO0o()) {
                    componentDataItem = new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str);
                }
            } else if (C0122O00000o.instance().OOo0()) {
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_new_ai_scene_off, (int) R.drawable.ic_new_ai_scene_on, (int) R.string.accessibility_ai_scene_on, str);
            }
            arrayList.add(componentDataItem);
        }
        this.mItems = Collections.unmodifiableList(arrayList);
        return this.mItems;
    }

    public void setAiScene(int i, boolean z) {
        if (!isEmpty()) {
            setClosed(false);
            this.mParentDataItem.editor().putBoolean(getKey(i), z).apply();
        }
    }

    public void setClosed(boolean z) {
        this.mIsClosed = z;
    }
}
