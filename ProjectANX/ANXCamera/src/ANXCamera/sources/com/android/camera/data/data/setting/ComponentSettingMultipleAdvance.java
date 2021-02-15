package com.android.camera.data.data.setting;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.data.data.ComponentMultiple;
import com.android.camera.data.data.DataItemBase;
import com.android.camera.data.data.TypeItem;
import com.android.camera2.CameraCapabilities;

public class ComponentSettingMultipleAdvance extends ComponentMultiple {
    public ComponentSettingMultipleAdvance(DataItemBase dataItemBase) {
        super(dataItemBase);
    }

    public int getDisplayTitleString() {
        return R.string.camera_settings_category_development;
    }

    public void initTypeElements(Context context, int i, CameraCapabilities cameraCapabilities, int i2) {
        TypeItem typeItem;
        TypeItem entryArrayRes;
        int i3;
        String string = context.getString(getDisplayTitleString());
        if (C0124O00000oO.OOoo0o0()) {
            insert(new TypeItem((int) R.string.pref_fingerprint_capture_title, string, CameraSettings.KEY_FINGERPRINT_CAPTURE, (Object) Boolean.TRUE));
        }
        if (i == 162) {
            typeItem = new TypeItem((int) R.string.pref_camera_volumekey_function_title, string, CameraSettings.KEY_VOLUME_VIDEO_FUNCTION, (Object) context.getString(R.string.pref_camera_volumekey_function_entry_shutter));
            entryArrayRes = typeItem.setEntryArrayRes(R.array.pref_video_volumekey_function_entries);
            i3 = R.array.pref_video_volumekey_function_entryvalues;
        } else if (i == 174 || i == 183) {
            typeItem = new TypeItem((int) R.string.pref_camera_volumekey_function_title, string, CameraSettings.KEY_VOLUME_LIVE_FUNCTION, (Object) context.getString(R.string.pref_camera_volumekey_function_entry_zoom));
            entryArrayRes = typeItem.setEntryArrayRes(R.array.pref_live_volumekey_function_entries);
            i3 = R.array.pref_live_volumekey_function_entryvalues;
        } else {
            typeItem = new TypeItem((int) R.string.pref_camera_volumekey_function_title, string, CameraSettings.KEY_VOLUME_CAMERA_FUNCTION, (Object) context.getString(R.string.pref_camera_volumekey_function_entry_timer));
            entryArrayRes = typeItem.setEntryArrayRes(R.array.pref_camera_volumekey_function_entries);
            i3 = R.array.pref_camera_volumekey_function_entryvalues;
        }
        entryArrayRes.setValueArrayRes(i3);
        insert(typeItem);
    }
}
