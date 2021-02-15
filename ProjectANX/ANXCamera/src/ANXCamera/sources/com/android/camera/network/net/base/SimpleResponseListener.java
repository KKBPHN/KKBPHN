package com.android.camera.network.net.base;

public abstract class SimpleResponseListener implements ResponseListener {
    public abstract void onResponse(Object obj);

    public final void onResponse(Object... objArr) {
        Object obj = (objArr == null || objArr.length <= 0) ? null : objArr[0];
        onResponse(obj);
    }

    public void onResponseError(ErrorCode errorCode, String str, Object obj) {
    }
}
