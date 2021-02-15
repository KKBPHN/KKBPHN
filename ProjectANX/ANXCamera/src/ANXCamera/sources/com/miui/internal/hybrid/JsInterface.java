package com.miui.internal.hybrid;

import android.util.Log;
import android.webkit.JavascriptInterface;

public class JsInterface {
    public static final String INTERFACE_NAME = "MiuiJsBridge";
    private HybridManager mManager;

    public JsInterface(HybridManager hybridManager) {
        this.mManager = hybridManager;
    }

    @JavascriptInterface
    public String config(String str) {
        String config = this.mManager.config(str);
        String str2 = HybridManager.TAG;
        if (Log.isLoggable(str2, 3)) {
            StringBuilder sb = new StringBuilder();
            sb.append("config response is ");
            sb.append(config);
            Log.d(str2, sb.toString());
        }
        return config;
    }

    @JavascriptInterface
    public String invoke(String str, String str2, String str3, String str4) {
        String invoke = this.mManager.invoke(str, str2, str3, str4);
        String str5 = HybridManager.TAG;
        if (Log.isLoggable(str5, 3)) {
            StringBuilder sb = new StringBuilder();
            sb.append("blocking response is ");
            sb.append(invoke);
            Log.d(str5, sb.toString());
        }
        return invoke;
    }

    @JavascriptInterface
    public String lookup(String str, String str2) {
        String lookup = this.mManager.lookup(str, str2);
        String str3 = HybridManager.TAG;
        if (Log.isLoggable(str3, 3)) {
            StringBuilder sb = new StringBuilder();
            sb.append("lookup response is ");
            sb.append(lookup);
            Log.d(str3, sb.toString());
        }
        return lookup;
    }
}
