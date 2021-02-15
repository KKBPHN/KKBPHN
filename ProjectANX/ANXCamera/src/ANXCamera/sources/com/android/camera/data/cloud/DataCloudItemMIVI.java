package com.android.camera.data.cloud;

import com.android.camera.resource.BaseResourceCacheable;

public class DataCloudItemMIVI implements BaseResourceCacheable {
    private static final long SOFT_EXPIRE_TIME = 43200000;
    private String mData;

    public long getCacheExpireTime() {
        return SOFT_EXPIRE_TIME;
    }

    public String getData() {
        return this.mData;
    }

    public void setData(String str) {
        this.mData = str;
    }
}
