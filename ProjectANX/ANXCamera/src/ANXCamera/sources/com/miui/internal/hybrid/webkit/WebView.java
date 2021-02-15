package com.miui.internal.hybrid.webkit;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import com.miui.internal.hybrid.provider.AbsWebChromeClient;
import com.miui.internal.hybrid.provider.AbsWebView;
import com.miui.internal.hybrid.provider.AbsWebViewClient;
import miui.hybrid.HybridBackForwardList;
import miui.hybrid.HybridSettings;
import miui.hybrid.HybridView;

public class WebView extends AbsWebView {
    protected android.webkit.WebView mWebView = new android.webkit.WebView(this.mContext);

    public WebView(Context context, HybridView hybridView) {
        super(context, hybridView);
    }

    public void addJavascriptInterface(Object obj, String str) {
        this.mWebView.addJavascriptInterface(obj, str);
    }

    public boolean canGoBack() {
        return this.mWebView.canGoBack();
    }

    public boolean canGoForward() {
        return this.mWebView.canGoForward();
    }

    public void clearCache(boolean z) {
        this.mWebView.clearCache(z);
    }

    public HybridBackForwardList copyBackForwardList() {
        return new WebBackForwardList(this.mWebView.copyBackForwardList());
    }

    public void destroy() {
        this.mWebView.destroy();
    }

    public void draw(Canvas canvas) {
        this.mWebView.draw(canvas);
    }

    public View getBaseWebView() {
        return this.mWebView;
    }

    public int getContentHeight() {
        return this.mWebView.getContentHeight();
    }

    public Context getContext() {
        return this.mWebView.getContext();
    }

    public View getRootView() {
        return this.mWebView.getRootView();
    }

    public float getScale() {
        return this.mWebView.getScale();
    }

    public HybridSettings getSettings() {
        return new WebSettings(this.mWebView.getSettings());
    }

    public String getTitle() {
        return this.mWebView.getTitle();
    }

    public String getUrl() {
        return this.mWebView.getUrl();
    }

    public void goBack() {
        this.mWebView.goBack();
    }

    public void loadUrl(String str) {
        this.mWebView.loadUrl(str);
    }

    public void reload() {
        this.mWebView.reload();
    }

    public void setVisibility(int i) {
        this.mWebView.setVisibility(i);
    }

    public void setWebChromeClient(AbsWebChromeClient absWebChromeClient) {
        this.mWebView.setWebChromeClient((WebChromeClient) absWebChromeClient.getWebChromeClient());
    }

    public void setWebViewClient(AbsWebViewClient absWebViewClient) {
        this.mWebView.setWebViewClient((WebViewClient) absWebViewClient.getWebViewClient());
    }
}
