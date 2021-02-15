package com.android.camera.data.data.runing;

import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentRunningTiltValue extends ComponentData {
    public static final String TILT_CIRCLE = "circle";
    public static final String TILT_PARALLEL = "parallel";

    public ComponentRunningTiltValue(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private List initItems() {
        ArrayList arrayList = new ArrayList();
        ComponentDataItem componentDataItem = new ComponentDataItem(R.drawable.ic_config_tilt_circle, R.drawable.ic_config_tilt_circle, R.drawable.ic_config_tilt_circle, R.string.pref_camera_tilt_shift_entry_circle, R.string.accessibility_tilt_shift_circle_button, TILT_CIRCLE);
        arrayList.add(componentDataItem);
        ComponentDataItem componentDataItem2 = new ComponentDataItem(R.drawable.ic_config_tilt_parallel, R.drawable.ic_config_tilt_parallel, R.drawable.ic_config_tilt_parallel, R.string.pref_camera_tilt_shift_entry_parallel, R.string.accessibility_tilt_shift_parallel_button, TILT_PARALLEL);
        arrayList.add(componentDataItem2);
        return Collections.unmodifiableList(arrayList);
    }

    public String getDefaultValue(int i) {
        return TILT_CIRCLE;
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_tilt_shift_title;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_camera_tilt_shift_key";
    }

    public boolean isSwitchOn(int i) {
        return this.mParentDataItem.getBoolean("pref_camera_tilt_shift_mode", false);
    }

    public void toSwitch(int i, boolean z) {
        this.mParentDataItem.putBoolean("pref_camera_tilt_shift_mode", z);
    }
}
