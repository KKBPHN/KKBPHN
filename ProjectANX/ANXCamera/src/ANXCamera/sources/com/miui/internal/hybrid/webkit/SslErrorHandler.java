package com.miui.internal.hybrid.webkit;

public class SslErrorHandler extends miui.hybrid.SslErrorHandler {
    private android.webkit.SslErrorHandler mSslErrorHandler;

    public SslErrorHandler(android.webkit.SslErrorHandler sslErrorHandler) {
        this.mSslErrorHandler = sslErrorHandler;
    }

    public void cancel() {
        this.mSslErrorHandler.cancel();
    }

    public void proceed() {
        this.mSslErrorHandler.proceed();
    }
}
