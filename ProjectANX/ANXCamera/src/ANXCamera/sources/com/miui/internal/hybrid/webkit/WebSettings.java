package com.miui.internal.hybrid.webkit;

import miui.hybrid.HybridSettings;

public class WebSettings extends HybridSettings {
    private android.webkit.WebSettings mWebSettings;

    public WebSettings(android.webkit.WebSettings webSettings) {
        this.mWebSettings = webSettings;
    }

    public String getUserAgentString() {
        return this.mWebSettings.getUserAgentString();
    }

    public void setAllowFileAccessFromFileURLs(boolean z) {
        this.mWebSettings.setAllowFileAccessFromFileURLs(z);
    }

    public void setAllowUniversalAccessFromFileURLs(boolean z) {
        this.mWebSettings.setAllowUniversalAccessFromFileURLs(z);
    }

    public void setAppCacheEnabled(boolean z) {
        this.mWebSettings.setAppCacheEnabled(z);
    }

    public void setAppCachePath(String str) {
        this.mWebSettings.setAppCachePath(str);
    }

    public void setCacheMode(int i) {
        this.mWebSettings.setCacheMode(i);
    }

    public void setDatabaseEnabled(boolean z) {
        this.mWebSettings.setDatabaseEnabled(z);
    }

    public void setDomStorageEnabled(boolean z) {
        this.mWebSettings.setDomStorageEnabled(z);
    }

    public void setGeolocationDatabasePath(String str) {
        this.mWebSettings.setGeolocationDatabasePath(str);
    }

    public void setGeolocationEnabled(boolean z) {
        this.mWebSettings.setGeolocationEnabled(z);
    }

    public void setJavaScriptCanOpenWindowsAutomatically(boolean z) {
        this.mWebSettings.setJavaScriptCanOpenWindowsAutomatically(z);
    }

    public void setJavaScriptEnabled(boolean z) {
        this.mWebSettings.setJavaScriptEnabled(z);
    }

    public void setLoadWithOverviewMode(boolean z) {
        this.mWebSettings.setLoadWithOverviewMode(z);
    }

    public void setSupportMultipleWindows(boolean z) {
        this.mWebSettings.setSupportMultipleWindows(z);
    }

    public void setTextZoom(int i) {
        this.mWebSettings.setTextZoom(i);
    }

    public void setUseWideViewPort(boolean z) {
        this.mWebSettings.setUseWideViewPort(z);
    }

    public void setUserAgentString(String str) {
        this.mWebSettings.setUserAgentString(str);
    }
}
