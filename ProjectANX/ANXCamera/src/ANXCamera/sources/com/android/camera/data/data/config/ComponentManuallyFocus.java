package com.android.camera.data.data.config;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.constant.FastMotionConstant;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import java.util.ArrayList;
import java.util.List;

public class ComponentManuallyFocus extends ComponentData {
    private boolean mIsFixedFocus;

    public ComponentManuallyFocus(DataItemConfig dataItemConfig) {
        super(dataItemConfig);
    }

    private List initItems() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ComponentDataItem(-1, -1, (int) R.string.pref_camera_focusmode_entry_auto_abbr, FastMotionConstant.FAST_MOTION_SPEED_30X));
        arrayList.add(new ComponentDataItem(-1, -1, (int) R.string.pref_camera_focusmode_entry_auto_abbr, "manual"));
        return arrayList;
    }

    public boolean disableUpdate() {
        return this.mIsFixedFocus;
    }

    public int getContentDescriptionString() {
        return R.string.parameter_focus_title;
    }

    public String getDefaultValue(int i) {
        return FastMotionConstant.FAST_MOTION_SPEED_30X;
    }

    @StringRes
    public int getDefaultValueDisplayString(int i) {
        return R.string.pref_camera_focusmode_entry_auto_abbr;
    }

    public int getDisplayTitleString() {
        return R.string.pref_qc_focus_position_title_abbr;
    }

    public List getItems() {
        if (this.mItems == null) {
            this.mItems = initItems();
        }
        return this.mItems;
    }

    public String getKey(int i) {
        return i != 167 ? i != 169 ? CameraSettings.KEY_QC_PRO_VIDEO_FOCUS_POSITION : CameraSettings.KEY_QC_FASTMOTION_PRO_FOCUS_POSITION : CameraSettings.KEY_QC_FOCUS_POSITION;
    }

    @StringRes
    public int getValueDisplayString(int i) {
        if (Integer.valueOf(getComponentValue(i)).intValue() == 1000) {
            return R.string.pref_camera_focusmode_entry_auto_abbr;
        }
        return -1;
    }

    @DrawableRes
    public int getValueSelectedDrawable(int i) {
        int intValue = Integer.valueOf(getComponentValue(i)).intValue();
        if (intValue == 1000) {
            return -1;
        }
        double d = (double) intValue;
        return d >= 600.0d ? R.drawable.ic_focusmode_flag_near : d >= 200.0d ? R.drawable.ic_focusmode_flag_normal : R.drawable.ic_focusmode_flag_far;
    }

    public int getValueSelectedShadowDrawable(int i) {
        int intValue = Integer.valueOf(getComponentValue(i)).intValue();
        if (intValue == 1000) {
            return -1;
        }
        double d = (double) intValue;
        return d >= 600.0d ? R.drawable.ic_focusmode_flag_near_shadow : d >= 200.0d ? R.drawable.ic_focusmode_flag_normal_shadow : R.drawable.ic_focusmode_flag_far_shadow;
    }

    public void setFixedFocusLens(boolean z) {
        this.mIsFixedFocus = z;
    }
}
