package com.android.camera.resource;

public interface ResponseListener {
    void onResponse(Object obj, boolean z);

    void onResponseError(int i, String str, Object obj);

    void onResponseProgress(long j, long j2);
}
