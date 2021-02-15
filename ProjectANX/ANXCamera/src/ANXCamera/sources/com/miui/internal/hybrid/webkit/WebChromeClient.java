package com.miui.internal.hybrid.webkit;

import android.webkit.GeolocationPermissions.Callback;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import com.miui.internal.hybrid.provider.AbsWebChromeClient;
import miui.hybrid.GeolocationPermissions;
import miui.hybrid.HybridChromeClient;
import miui.hybrid.HybridView;

public class WebChromeClient extends AbsWebChromeClient {

    class InternalWebChromeClient extends android.webkit.WebChromeClient {
        InternalWebChromeClient() {
        }

        public void onGeolocationPermissionsShowPrompt(String str, Callback callback) {
            WebChromeClient.this.onGeolocationPermissionsShowPrompt(str, new GeolocationPermissionsCallback(callback));
        }

        public boolean onJsAlert(WebView webView, String str, String str2, JsResult jsResult) {
            JsResult jsResult2 = new JsResult(jsResult);
            WebChromeClient webChromeClient = WebChromeClient.this;
            return webChromeClient.onJsAlert(webChromeClient.mHybridView, str, str2, jsResult2);
        }

        public boolean onJsConfirm(WebView webView, String str, String str2, JsResult jsResult) {
            JsResult jsResult2 = new JsResult(jsResult);
            WebChromeClient webChromeClient = WebChromeClient.this;
            return webChromeClient.onJsConfirm(webChromeClient.mHybridView, str, str2, jsResult2);
        }

        public void onProgressChanged(WebView webView, int i) {
            WebChromeClient webChromeClient = WebChromeClient.this;
            webChromeClient.onProgressChanged(webChromeClient.mHybridView, i);
        }

        public void onReceivedTitle(WebView webView, String str) {
            WebChromeClient webChromeClient = WebChromeClient.this;
            webChromeClient.onReceivedTitle(webChromeClient.mHybridView, str);
        }

        public void openFileChooser(ValueCallback valueCallback, String str, String str2) {
            WebChromeClient.this.openFileChooser(new ValueCallback(valueCallback), str, str2);
        }
    }

    public WebChromeClient(HybridChromeClient hybridChromeClient, HybridView hybridView) {
        super(hybridChromeClient, hybridView);
    }

    public Object getWebChromeClient() {
        return new InternalWebChromeClient();
    }

    public void onGeolocationPermissionsShowPrompt(String str, GeolocationPermissions.Callback callback) {
        this.mHybridChromeClient.onGeolocationPermissionsShowPrompt(str, callback);
    }

    public boolean onJsAlert(HybridView hybridView, String str, String str2, miui.hybrid.JsResult jsResult) {
        return this.mHybridChromeClient.onJsAlert(hybridView, str, str2, jsResult);
    }

    public boolean onJsConfirm(HybridView hybridView, String str, String str2, miui.hybrid.JsResult jsResult) {
        return this.mHybridChromeClient.onJsConfirm(hybridView, str, str2, jsResult);
    }

    public void onProgressChanged(HybridView hybridView, int i) {
        this.mHybridChromeClient.onProgressChanged(hybridView, i);
    }

    public void onReceivedTitle(HybridView hybridView, String str) {
        this.mHybridChromeClient.onReceivedTitle(hybridView, str);
    }

    public void openFileChooser(miui.hybrid.ValueCallback valueCallback, String str, String str2) {
        this.mHybridChromeClient.openFileChooser(valueCallback, str, str2);
    }
}
