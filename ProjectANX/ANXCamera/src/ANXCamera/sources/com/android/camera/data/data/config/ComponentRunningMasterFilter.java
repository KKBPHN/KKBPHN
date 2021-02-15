package com.android.camera.data.data.config;

import androidx.annotation.NonNull;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.effect.EffectController;
import com.android.camera.effect.FilterInfo;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ComponentRunningMasterFilter extends ComponentData {
    private boolean mIsSupportColorRentention;

    public ComponentRunningMasterFilter(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    public boolean IsSupportColorRentention() {
        return this.mIsSupportColorRentention;
    }

    @NonNull
    public String getDefaultValue(int i) {
        int i2 = (i == 162 || i == 169 || i == 180) ? 0 : FilterInfo.FILTER_ID_NONE;
        return String.valueOf(i2);
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_new_coloreffect_title;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_MASTER_SHADER_COLOR_EFFECT;
    }

    public void initItems(int i) {
        if (i == 162 || i == 169 || i == 180) {
            ArrayList filterInfo = EffectController.getInstance().getFilterInfo(9);
            this.mItems = new ArrayList(filterInfo.size());
            Iterator it = filterInfo.iterator();
            while (it.hasNext()) {
                FilterInfo filterInfo2 = (FilterInfo) it.next();
                if (filterInfo2.getTagUniqueFilterId() != 200 || this.mIsSupportColorRentention) {
                    this.mItems.add(new ComponentDataItem(filterInfo2.getIconResId(), filterInfo2.getIconResId(), filterInfo2.getNameResId(), String.valueOf(filterInfo2.getTagUniqueFilterId())));
                }
            }
        }
    }

    public boolean isSwitchOn(int i) {
        return !getComponentValue(i).equals(getDefaultValue(i));
    }

    public void reInit(int i, int i2, CameraCapabilities cameraCapabilities) {
        this.mItems = new ArrayList();
        if (cameraCapabilities.isSupportVideoMasterFilter()) {
            this.mIsSupportColorRentention = i2 == 0 ? cameraCapabilities.isSupportVideoFilterColorRetentionBack() : cameraCapabilities.isSupportVideoFilterColorRetentionFront();
            initItems(i);
        }
    }
}
