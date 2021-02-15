package com.android.camera.data.data.setting;

import android.content.Context;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentMultiple;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.data.TypeItem;
import com.android.camera2.CameraCapabilities;

public class ComponentSettingMultipleCommon extends ComponentMultiple {
    public ComponentSettingMultipleCommon(DataItemBase dataItemBase) {
        super(dataItemBase);
    }

    public int getDisplayTitleString() {
        return R.string.pref_camera_settings;
    }

    public void initTypeElements(Context context, int i, CameraCapabilities cameraCapabilities, int i2) {
        String string = context.getString(getDisplayTitleString());
        insert(new TypeItem((int) R.string.pref_camera_recordlocation_title, string, CameraSettings.KEY_RECORD_LOCATION, (Object) Boolean.TRUE), new TypeItem((int) R.string.pref_camera_sound_title, string, CameraSettings.KEY_CAMERA_SOUND, (Object) Boolean.TRUE));
    }
}
