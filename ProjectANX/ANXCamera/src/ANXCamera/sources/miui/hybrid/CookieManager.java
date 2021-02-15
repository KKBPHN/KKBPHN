package miui.hybrid;

import com.miui.internal.hybrid.provider.WebViewFactory;
import miui.util.AppConstants;

public abstract class CookieManager {
    public static boolean allowFileSchemeCookies() {
        return getInstance().allowFileSchemeCookiesImpl();
    }

    public static CookieManager getInstance() {
        return WebViewFactory.getProvider(AppConstants.getCurrentApplication()).getCookieManager();
    }

    public static void setAcceptFileSchemeCookies(boolean z) {
        getInstance().setAcceptFileSchemeCookiesImpl(z);
    }

    public boolean acceptCookie() {
        return false;
    }

    /* access modifiers changed from: protected */
    public boolean allowFileSchemeCookiesImpl() {
        return false;
    }

    public String getCookie(String str) {
        return null;
    }

    public boolean hasCookies() {
        return false;
    }

    public void removeAllCookie() {
    }

    public void removeExpiredCookie() {
    }

    public void removeSessionCookie() {
    }

    public void setAcceptCookie(boolean z) {
    }

    /* access modifiers changed from: protected */
    public void setAcceptFileSchemeCookiesImpl(boolean z) {
    }

    public void setCookie(String str, String str2) {
    }
}
