package com.android.camera.data.data.runing;

import android.annotation.TargetApi;
import android.text.TextUtils;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.constant.FastMotionConstant;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.log.Log;
import com.android.camera2.CameraCapabilities;
import java.util.ArrayList;
import java.util.List;

public class ComponentRunningFastMotionSpeed extends ComponentData {
    private static final String TAG = "ComponentRunningFastMotionSpeed";
    private boolean mDisabled;
    private ComponentDataItem[] mFullItems;

    public ComponentRunningFastMotionSpeed(DataItemRunning dataItemRunning) {
        super(dataItemRunning);
    }

    private ComponentDataItem[] getFullItems() {
        ComponentDataItem[] componentDataItemArr = this.mFullItems;
        if (componentDataItemArr != null) {
            return componentDataItemArr;
        }
        this.mIsDisplayStringFromResourceId = true;
        this.mIsKeepValueWhenDisabled = true;
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ComponentDataItem(-1, -1, "4X", "120"));
        arrayList.add(new ComponentDataItem(-1, -1, "10X", FastMotionConstant.FAST_MOTION_SPEED_10X));
        arrayList.add(new ComponentDataItem(-1, -1, "15X", "500"));
        arrayList.add(new ComponentDataItem(-1, -1, "30X", FastMotionConstant.FAST_MOTION_SPEED_30X));
        arrayList.add(new ComponentDataItem(-1, -1, "60X", FastMotionConstant.FAST_MOTION_SPEED_60X));
        arrayList.add(new ComponentDataItem(-1, -1, "90X", FastMotionConstant.FAST_MOTION_SPEED_90X));
        arrayList.add(new ComponentDataItem(-1, -1, "120X", FastMotionConstant.FAST_MOTION_SPEED_120X));
        arrayList.add(new ComponentDataItem(-1, -1, "150X", FastMotionConstant.FAST_MOTION_SPEED_150X));
        arrayList.add(new ComponentDataItem(-1, -1, "300X", FastMotionConstant.FAST_MOTION_SPEED_300X));
        arrayList.add(new ComponentDataItem(-1, -1, "450X", FastMotionConstant.FAST_MOTION_SPEED_450X));
        arrayList.add(new ComponentDataItem(-1, -1, "600X", FastMotionConstant.FAST_MOTION_SPEED_600X));
        arrayList.add(new ComponentDataItem(-1, -1, "900X", FastMotionConstant.FAST_MOTION_SPEED_900X));
        arrayList.add(new ComponentDataItem(-1, -1, "1800X", FastMotionConstant.FAST_MOTION_SPEED_1800X));
        this.mFullItems = (ComponentDataItem[]) arrayList.toArray(new ComponentDataItem[arrayList.size()]);
        return this.mFullItems;
    }

    public boolean checkValueValid(int i, String str) {
        if (!TextUtils.isEmpty(str)) {
            for (ComponentDataItem componentDataItem : getFullItems()) {
                if (componentDataItem.mValue.equals(str)) {
                    return true;
                }
            }
        }
        Log.d(TAG, "checkValueValid: invalid value!");
        return false;
    }

    public boolean disableUpdate() {
        return this.mDisabled;
    }

    public String getDefaultValue(int i) {
        return "120";
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_fastmotion_speed;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_FRAME_INTERVAL;
    }

    public boolean isModified() {
        return !getComponentValue(160).equals(getDefaultValue(160));
    }

    @TargetApi(21)
    public List reInit(int i, CameraCapabilities cameraCapabilities) {
        if (this.mItems == null) {
            this.mItems = new ArrayList();
        } else {
            this.mItems.clear();
        }
        if (i == 169) {
            ComponentDataItem[] fullItems = getFullItems();
            for (ComponentDataItem add : fullItems) {
                this.mItems.add(add);
            }
        }
        return this.mItems;
    }

    public void resetComponentValue(int i) {
        super.resetComponentValue(i);
        setComponentValue(i, getDefaultValue(i));
    }

    public void setDisabled(boolean z) {
        this.mDisabled = z;
    }
}
