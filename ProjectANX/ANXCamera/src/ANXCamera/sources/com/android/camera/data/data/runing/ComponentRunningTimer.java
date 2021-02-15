package com.android.camera.data.data.runing;

import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.List;

public class ComponentRunningTimer extends ComponentData {
    public static final String TIMER_0 = "0";
    public static final String TIMER_10 = "10";
    public static final String TIMER_3 = "3";
    public static final String TIMER_5 = "5";

    public ComponentRunningTimer(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private List initItems() {
        ArrayList arrayList = new ArrayList();
        ComponentDataItem componentDataItem = new ComponentDataItem(R.drawable.ic_vector_config_timer, R.drawable.ic_vector_config_timer, R.string.pref_camera_delay_capture_title, R.string.accessibility_delay_capture_close_button, "0", false, 654311423);
        arrayList.add(componentDataItem);
        ComponentDataItem componentDataItem2 = new ComponentDataItem(-1, -1, R.string.pref_camera_delay_capture_entry_3s, R.string.accessibility_delay_capture_3s_button, "3", true, 0);
        arrayList.add(componentDataItem2);
        ComponentDataItem componentDataItem3 = new ComponentDataItem(-1, -1, R.string.pref_camera_delay_capture_entry_5s, R.string.accessibility_delay_capture_5s_button, "5", true, 0);
        arrayList.add(componentDataItem3);
        ComponentDataItem componentDataItem4 = new ComponentDataItem(-1, -1, R.string.pref_camera_delay_capture_entry_10s, R.string.accessibility_delay_capture_10s_button, "10", true, 0);
        arrayList.add(componentDataItem4);
        return arrayList;
    }

    public String getDefaultValue(int i) {
        return "0";
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_delay_capture_title;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_delay_capture_mode";
    }

    public String getNextValue() {
        String componentValue = getComponentValue(160);
        String str = "0";
        String str2 = "3";
        if (str.equals(componentValue)) {
            return str2;
        }
        boolean equals = str2.equals(componentValue);
        String str3 = "5";
        if (equals) {
            return str3;
        }
        boolean equals2 = str3.equals(componentValue);
        return str;
    }

    public int getTimer() {
        return Integer.valueOf(getComponentValue(160)).intValue();
    }

    public boolean isSwitchOn() {
        return !getComponentValue(160).equals("0");
    }

    public void switchOff() {
        setComponentValue(160, "0");
    }
}
