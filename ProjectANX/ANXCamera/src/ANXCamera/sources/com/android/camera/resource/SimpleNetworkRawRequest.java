package com.android.camera.resource;

import androidx.annotation.Nullable;

public class SimpleNetworkRawRequest extends SimpleNetworkBaseRequest {
    public SimpleNetworkRawRequest(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public BaseResourceRaw process(String str, @Nullable BaseResourceRaw baseResourceRaw) {
        baseResourceRaw.content = str;
        return baseResourceRaw;
    }
}
