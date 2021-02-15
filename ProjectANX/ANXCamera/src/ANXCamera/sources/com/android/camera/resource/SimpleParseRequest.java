package com.android.camera.resource;

public abstract class SimpleParseRequest extends BaseObservableRequest {
    public abstract void processParse(BaseResourceCacheable baseResourceCacheable);

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, BaseResourceCacheable baseResourceCacheable) {
        try {
            processParse(baseResourceCacheable);
            responseListener.onResponse(baseResourceCacheable, false);
        } catch (Exception e) {
            e.printStackTrace();
            responseListener.onResponse(null, true);
        }
    }
}
