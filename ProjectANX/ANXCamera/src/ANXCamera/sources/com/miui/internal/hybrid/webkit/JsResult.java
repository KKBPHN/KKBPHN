package com.miui.internal.hybrid.webkit;

public class JsResult extends miui.hybrid.JsResult {
    private android.webkit.JsResult mJsResult;

    public JsResult(android.webkit.JsResult jsResult) {
        this.mJsResult = jsResult;
    }

    public void cancel() {
        this.mJsResult.cancel();
    }

    public void confirm() {
        this.mJsResult.confirm();
    }
}
