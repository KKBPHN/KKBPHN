package miui.hybrid;

import android.content.Context;
import com.miui.internal.hybrid.provider.AbsCookieSyncManager;
import com.miui.internal.hybrid.provider.WebViewFactory;
import miui.util.AppConstants;

public final class CookieSyncManager {
    private static CookieSyncManager sRef;

    private CookieSyncManager() {
    }

    public static CookieSyncManager createInstance(Context context) {
        getCookieSyncManager().createInstance(context);
        return getInstance();
    }

    private static AbsCookieSyncManager getCookieSyncManager() {
        return WebViewFactory.getProvider(AppConstants.getCurrentApplication()).getCookieSyncManager();
    }

    public static CookieSyncManager getInstance() {
        getCookieSyncManager().getInstance();
        if (sRef == null) {
            sRef = new CookieSyncManager();
        }
        return sRef;
    }

    public static void sync() {
        getCookieSyncManager().sync();
    }
}
