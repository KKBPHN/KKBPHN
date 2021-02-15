package com.android.camera.resource.conf;

import com.android.camera.CameraAppImpl;
import com.android.camera.data.cloud.DataCloudItemFeature;
import com.android.camera.resource.SimpleLocalJsonCacheRequest;
import java.io.File;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfCacheRequest extends SimpleLocalJsonCacheRequest {
    /* access modifiers changed from: protected */
    public boolean isCacheValid(DataCloudItemFeature dataCloudItemFeature) {
        File cacheFile = getCacheFile(DataCloudItemFeature.CACHE_INFO, CameraAppImpl.getAndroidContext());
        if (!cacheFile.exists()) {
            return false;
        }
        return System.currentTimeMillis() - cacheFile.lastModified() <= dataCloudItemFeature.getCacheExpireTime();
    }

    /* access modifiers changed from: protected */
    public void processRestore(DataCloudItemFeature dataCloudItemFeature) {
        try {
            dataCloudItemFeature.parseJson(new JSONObject(getCacheJsonString(DataCloudItemFeature.CACHE_INFO, CameraAppImpl.getAndroidContext())));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
