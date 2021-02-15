package com.android.camera.resource.conf;

import android.provider.MiuiSettings.SettingsCloudData;
import com.android.camera.CameraAppImpl;
import com.android.camera.data.cloud.DataCloudItemMIVI;
import com.android.camera.resource.SimpleParseRequest;

public class ConfMIVIRequest extends SimpleParseRequest {
    public static final String CLOUD_DATA_KEY = "miviInfo";
    public static final String CLOUD_DATA_MODULE_NAME = "camera_v4";

    public static final String getCloudDataString() {
        return SettingsCloudData.getCloudDataString(CameraAppImpl.getAndroidContext().getContentResolver(), CLOUD_DATA_MODULE_NAME, "miviInfo", null);
    }

    /* access modifiers changed from: protected */
    public void processParse(DataCloudItemMIVI dataCloudItemMIVI) {
        dataCloudItemMIVI.setData(getCloudDataString());
    }
}
