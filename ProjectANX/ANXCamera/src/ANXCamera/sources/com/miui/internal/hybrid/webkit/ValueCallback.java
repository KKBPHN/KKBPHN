package com.miui.internal.hybrid.webkit;

public class ValueCallback implements miui.hybrid.ValueCallback {
    private android.webkit.ValueCallback mValueCallback;

    public ValueCallback(android.webkit.ValueCallback valueCallback) {
        this.mValueCallback = valueCallback;
    }

    public void onReceiveValue(Object obj) {
        this.mValueCallback.onReceiveValue(obj);
    }
}
