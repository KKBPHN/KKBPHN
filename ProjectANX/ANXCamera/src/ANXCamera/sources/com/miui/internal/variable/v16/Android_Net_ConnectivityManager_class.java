package com.miui.internal.variable.v16;

import android.net.ConnectivityManager;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Method;

public class Android_Net_ConnectivityManager_class extends com.miui.internal.variable.Android_Net_ConnectivityManager_class {
    private static final Method isNetworkSupported = Method.of(ConnectivityManager.class, "isNetworkSupported", "(I)Z");

    public boolean isNetworkSupported(ConnectivityManager connectivityManager, int i) {
        boolean z = false;
        try {
            z = isNetworkSupported.invokeBoolean(null, connectivityManager, Integer.valueOf(i));
            return z;
        } catch (RuntimeException e) {
            VariableExceptionHandler.getInstance().onThrow("android.os.Process.getTotalMemory", e);
            return z;
        }
    }
}
