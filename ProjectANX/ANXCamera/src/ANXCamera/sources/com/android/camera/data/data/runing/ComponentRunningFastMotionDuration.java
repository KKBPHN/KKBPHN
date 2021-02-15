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

public class ComponentRunningFastMotionDuration extends ComponentData {
    private static final String TAG = "ComponentRunningFastMotionDuration";
    private boolean mDisabled;
    private ComponentDataItem[] mFullItems;

    public ComponentRunningFastMotionDuration(DataItemRunning dataItemRunning) {
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
        String str = "0";
        arrayList.add(new ComponentDataItem(-1, -1, str, str));
        String str2 = "10";
        arrayList.add(new ComponentDataItem(-1, -1, str2, str2));
        String str3 = "20";
        arrayList.add(new ComponentDataItem(-1, -1, str3, str3));
        String str4 = "30";
        arrayList.add(new ComponentDataItem(-1, -1, str4, str4));
        String str5 = "40";
        arrayList.add(new ComponentDataItem(-1, -1, str5, str5));
        String str6 = FastMotionConstant.FAST_MOTION_DURATION_50;
        arrayList.add(new ComponentDataItem(-1, -1, str6, str6));
        String str7 = "60";
        arrayList.add(new ComponentDataItem(-1, -1, str7, str7));
        String str8 = FastMotionConstant.FAST_MOTION_DURATION_80;
        arrayList.add(new ComponentDataItem(-1, -1, str8, str8));
        String str9 = FastMotionConstant.FAST_MOTION_DURATION_100;
        arrayList.add(new ComponentDataItem(-1, -1, str9, str9));
        String str10 = "120";
        arrayList.add(new ComponentDataItem(-1, -1, str10, str10));
        String str11 = FastMotionConstant.FAST_MOTION_DURATION_160;
        arrayList.add(new ComponentDataItem(-1, -1, str11, str11));
        String str12 = "200";
        arrayList.add(new ComponentDataItem(-1, -1, str12, str12));
        String str13 = "240";
        arrayList.add(new ComponentDataItem(-1, -1, str13, str13));
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
        return "0";
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_fastmotion_duration;
    }

    public List getItems() {
        return this.mItems;
    }

    public String getKey(int i) {
        return CameraSettings.KEY_NEW_VIDEO_TIME_LAPSE_DURATION;
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
