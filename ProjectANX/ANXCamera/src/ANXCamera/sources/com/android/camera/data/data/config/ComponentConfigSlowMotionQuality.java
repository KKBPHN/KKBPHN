package com.android.camera.data.data.config;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComponentConfigSlowMotionQuality extends ComponentData {
    public static final String QUALITY_1080P = "6";
    public static final String QUALITY_720P = "5";
    public static final String SIZE_FPS_1080_120 = "1920x1080:120";
    private static final String TAG = "ComponentConfigSlowMotionQuality";

    public ComponentConfigSlowMotionQuality(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    /* access modifiers changed from: protected */
    public boolean checkValueValid(int i, String str) {
        for (ComponentDataItem componentDataItem : getItems()) {
            if (TextUtils.equals(str, componentDataItem.mValue) && !componentDataItem.mIsDisabled) {
                return true;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("checkValueValid: invalid value: ");
        sb.append(str);
        Log.d(TAG, sb.toString());
        return false;
    }

    public boolean disableUpdate() {
        return this.mItems == null || this.mItems.size() == 1;
    }

    public String getComponentValue(int i) {
        String defaultValue = getDefaultValue(i);
        String string = this.mParentDataItem.getString(getKey(i), defaultValue);
        if (string == null || string.equals(defaultValue) || checkValueValid(i, string)) {
            return string;
        }
        String simpleName = ComponentConfigSlowMotionQuality.class.getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append("Items do not have this ");
        sb.append(string);
        sb.append(",so return defaultValue = ");
        sb.append(defaultValue);
        Log.e(simpleName, sb.toString());
        return defaultValue;
    }

    public String getDefaultValue(int i) {
        return "5";
    }

    public int getDisplayTitleString() {
        return R.string.pref_video_quality_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return "pref_video_new_slow_motion_key";
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        ComponentDataItem componentDataItem;
        ArrayList arrayList = new ArrayList();
        if (i == 172) {
            boolean contains = CameraSettings.getSupportedHfrSettings(cameraCapabilities).contains(SIZE_FPS_1080_120);
            String str = "6";
            String str2 = "5";
            if ((!C0122O00000o.instance().OOOo0o0() || i2 != 0) && (!C0122O00000o.instance().OOOo0OO() || i2 != 1)) {
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_config_slow_720p, (int) R.drawable.ic_config_slow_720p, (int) R.string.pref_new_slow_motion_video_quality_entry_720p, str2));
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_slow_1080p, (int) R.drawable.ic_config_slow_1080p, (int) R.string.pref_new_slow_motion_video_quality_entry_1080p, str);
            } else if (contains) {
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_config_slow_720p_120fps, (int) R.drawable.ic_config_slow_720p_120fps, (int) R.string.pref_new_slow_motion_video_quality_entry_720p, str2));
                componentDataItem = new ComponentDataItem((int) R.drawable.ic_config_slow_1080p_120fps, (int) R.drawable.ic_config_slow_1080p_120fps, (int) R.string.pref_new_slow_motion_video_quality_entry_1080p, str);
            } else {
                arrayList.add(new ComponentDataItem((int) R.drawable.ic_config_slow_720p_120fps, (int) R.drawable.ic_config_slow_720p_120fps, (int) R.string.pref_new_slow_motion_video_quality_entry_720p, str2));
                if (C0122O00000o.instance().OOOo0o0()) {
                    setComponentValue(i, str2);
                }
            }
            arrayList.add(componentDataItem);
        }
        this.mItems = Collections.unmodifiableList(arrayList);
    }
}
