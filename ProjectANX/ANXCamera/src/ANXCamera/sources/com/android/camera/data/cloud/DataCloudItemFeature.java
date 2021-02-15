package com.android.camera.data.cloud;

import androidx.collection.SimpleArrayMap;
import com.android.camera.resource.BaseResourceCacheable;
import java.util.Iterator;
import org.json.JSONObject;

public class DataCloudItemFeature extends DataCloudItemBase implements BaseResourceCacheable {
    public static final String CACHE_INFO = "cache_info";
    private static final String SUPPORT_TM_MUSIC = "tm";
    private static final String VERSION = "v";

    public DataCloudItemFeature() {
        setReady(false);
    }

    public long getCacheExpireTime() {
        return 5400000;
    }

    public String getVersion() {
        return getCloudString(VERSION, "");
    }

    public void parseJson(JSONObject jSONObject) {
        Iterator keys = jSONObject.keys();
        SimpleArrayMap values = getValues();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            values.put(str, jSONObject.opt(str));
        }
        setReady(true);
    }

    public String provideKey() {
        return null;
    }

    public boolean supportTMMusic() {
        return getCloudBoolean(SUPPORT_TM_MUSIC, true);
    }
}
