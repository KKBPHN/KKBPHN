package com.miui.internal.webkit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import com.miui.internal.hybrid.provider.AbsWebView;
import com.miui.internal.util.ContextHelper;
import com.miui.internal.util.UrlResolverHelper;
import miui.util.UrlResolver;

public class WebViewClientDelegate {
    private static final int FEATURE_ALL = -1;
    public static final int FEATURE_AUTO_LOGIN = 2;
    public static final int FEATURE_DEEP_LINK = 1;
    private DeviceAccountLogin mAccountLogin;
    private LoginState mLoginState;
    private boolean mSupportAutoLogin;
    private boolean mSupportDeepLink;

    enum LoginState {
        LOGIN_START,
        LOGIN_INPROGRESS,
        LOGIN_FINISHED
    }

    public WebViewClientDelegate() {
        this(-1);
    }

    public WebViewClientDelegate(int i) {
        this(i, -1);
    }

    public WebViewClientDelegate(int i, int i2) {
        this.mLoginState = LoginState.LOGIN_FINISHED;
        int i3 = (i & i2) | ((~i2) & -1);
        boolean z = false;
        this.mSupportDeepLink = (i3 & 1) != 0;
        if ((i3 & 2) != 0) {
            z = true;
        }
        this.mSupportAutoLogin = z;
    }

    public void onPageFinished(AbsWebView absWebView, String str) {
        if (this.mSupportAutoLogin && this.mLoginState == LoginState.LOGIN_INPROGRESS) {
            this.mLoginState = LoginState.LOGIN_FINISHED;
            this.mAccountLogin.onLoginPageFinished();
        }
    }

    public void onPageStarted(AbsWebView absWebView, String str, Bitmap bitmap) {
        if (this.mSupportAutoLogin && this.mLoginState == LoginState.LOGIN_START) {
            this.mLoginState = LoginState.LOGIN_INPROGRESS;
        }
    }

    public void onReceivedLoginRequest(AbsWebView absWebView, String str, String str2, String str3) {
        if (this.mSupportAutoLogin) {
            Activity activityContextFromView = ContextHelper.getActivityContextFromView(absWebView.getRootView());
            if (activityContextFromView != null) {
                if (this.mAccountLogin == null) {
                    this.mAccountLogin = new DefaultDeviceAccountLogin(activityContextFromView, absWebView);
                }
                if (!absWebView.canGoForward()) {
                    this.mLoginState = LoginState.LOGIN_START;
                    absWebView.setVisibility(4);
                    this.mAccountLogin.login(str, str2, str3);
                } else if (absWebView.canGoBack()) {
                    absWebView.goBack();
                } else {
                    activityContextFromView.finish();
                }
            }
        }
    }

    public boolean shouldOverrideUrlLoading(AbsWebView absWebView, String str) {
        if (!this.mSupportDeepLink || !UrlResolverHelper.isMiUrl(str)) {
            return false;
        }
        Context context = absWebView.getContext();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addCategory("android.intent.category.BROWSABLE");
        ResolveInfo checkMiuiIntent = UrlResolver.checkMiuiIntent(context, packageManager, intent);
        if (checkMiuiIntent == null) {
            return false;
        }
        if (checkMiuiIntent.activityInfo != null) {
            context.startActivity(intent);
        }
        return true;
    }
}
