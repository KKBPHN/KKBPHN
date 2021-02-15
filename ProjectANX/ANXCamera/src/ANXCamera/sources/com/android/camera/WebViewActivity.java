package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import miui.app.ActionBar;
import miui.app.Activity;

public class WebViewActivity extends Activity {
    private WebView mWebView;

    private void initWebPage(String str) {
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.user_notice_title);
            this.mWebView = (WebView) findViewById(R.id.cta_webview);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                    String uri = VERSION.SDK_INT >= 21 ? webResourceRequest.getUrl().toString() : webResourceRequest.toString();
                    if (!uri.startsWith("mailto:")) {
                        return false;
                    }
                    try {
                        WebViewActivity.this.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(uri)));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });
            this.mWebView.loadUrl(str);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_cta_webview);
        if (getIntent().getBooleanExtra("StartActivityWhenLocked", false)) {
            setShowWhenLocked(true);
        }
        initWebPage(getIntent().getStringExtra(CameraIntentManager.EXTRA_CTA_WEBVIEW_LINK));
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (!this.mWebView.canGoBack() || this.mWebView.copyBackForwardList().getSize() < 2) {
                super.onKeyDown(i, keyEvent);
            } else {
                this.mWebView.goBack();
            }
        }
        return true;
    }

    public void onPause() {
        super.onPause();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(false, getApplication());
        }
    }

    public void onResume() {
        super.onResume();
        if (C0124O00000oO.Oo0O0oo()) {
            ThermalHelper.updateDisplayFrameRate(true, getApplication());
        }
    }
}
