package com.miui.internal.hybrid.provider;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.util.Log;
import com.miui.internal.hybrid.HybridManager;
import com.miui.internal.hybrid.webkit.WebkitFactoryProvider;

public class WebViewFactory {
    private static final String META_DATA_KEY = "com.miui.sdk.hybrid.webview";
    private static WebViewFactoryProvider sProviderInstance;
    private static final Object sProviderLock = new Object();

    public static WebViewFactoryProvider getProvider(Context context) {
        synchronized (sProviderLock) {
            if (sProviderInstance != null) {
                WebViewFactoryProvider webViewFactoryProvider = sProviderInstance;
                return webViewFactoryProvider;
            }
            String str = null;
            try {
                Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
                if (bundle != null) {
                    str = bundle.getString(META_DATA_KEY);
                }
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
            if (str != null) {
                try {
                    sProviderInstance = (WebViewFactoryProvider) Class.forName(str, false, context.getClassLoader()).newInstance();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            if (sProviderInstance == null) {
                sProviderInstance = new WebkitFactoryProvider();
            }
            if (Log.isLoggable(HybridManager.TAG, 3)) {
                String str2 = HybridManager.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("loaded provider:");
                sb.append(sProviderInstance);
                Log.d(str2, sb.toString());
            }
            WebViewFactoryProvider webViewFactoryProvider2 = sProviderInstance;
            return webViewFactoryProvider2;
        }
    }
}
