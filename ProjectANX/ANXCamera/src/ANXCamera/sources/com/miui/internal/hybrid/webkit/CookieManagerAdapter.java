package com.miui.internal.hybrid.webkit;

import miui.hybrid.CookieManager;

public class CookieManagerAdapter extends CookieManager {
    private android.webkit.CookieManager mCookieManager;

    public CookieManagerAdapter(android.webkit.CookieManager cookieManager) {
        this.mCookieManager = cookieManager;
    }

    public boolean acceptCookie() {
        return android.webkit.CookieManager.getInstance().acceptCookie();
    }

    /* access modifiers changed from: protected */
    public boolean allowFileSchemeCookiesImpl() {
        return android.webkit.CookieManager.allowFileSchemeCookies();
    }

    public String getCookie(String str) {
        return this.mCookieManager.getCookie(str);
    }

    public boolean hasCookies() {
        return this.mCookieManager.hasCookies();
    }

    public void removeAllCookie() {
        this.mCookieManager.removeAllCookie();
    }

    public void removeExpiredCookie() {
        this.mCookieManager.removeExpiredCookie();
    }

    public void removeSessionCookie() {
        this.mCookieManager.removeSessionCookie();
    }

    public void setAcceptCookie(boolean z) {
        this.mCookieManager.setAcceptCookie(z);
    }

    /* access modifiers changed from: protected */
    public void setAcceptFileSchemeCookiesImpl(boolean z) {
        android.webkit.CookieManager.setAcceptFileSchemeCookies(z);
    }

    public void setCookie(String str, String str2) {
        this.mCookieManager.setCookie(str, str2);
    }
}
