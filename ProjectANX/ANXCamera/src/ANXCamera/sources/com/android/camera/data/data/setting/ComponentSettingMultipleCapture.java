package com.android.camera.data.data.setting;

import android.content.Context;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentMultiple;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.data.TypeItem;
import com.android.camera2.CameraCapabilities;

public class ComponentSettingMultipleCapture extends ComponentMultiple {
    public ComponentSettingMultipleCapture(DataItemBase dataItemBase) {
        super(dataItemBase);
    }

    public int getDisplayTitleString() {
        return R.string.camera_settings_category_development;
    }

    public void initTypeElements(Context context, int i, CameraCapabilities cameraCapabilities, int i2) {
        TypeItem typeItem = new TypeItem((int) R.string.pref_camera_jpegquality_title, context.getString(getDisplayTitleString()), CameraSettings.KEY_JPEG_QUALITY, (Object) context.getString(R.string.pref_camera_jpegquality_default));
        typeItem.setEntryArrayRes(R.array.pref_camera_jpegquality_entries).setValueArrayRes(R.array.pref_camera_jpegquality_entryvalues);
        insert(typeItem);
    }
}
