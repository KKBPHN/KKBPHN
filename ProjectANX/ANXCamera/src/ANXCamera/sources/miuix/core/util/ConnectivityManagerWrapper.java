package miuix.core.util;

import android.net.ConnectivityManager;
import miuix.reflect.Method;

public class ConnectivityManagerWrapper {
    private static ConnectivityManagerWrapper sInstance;
    private Method mIsNetworkSupport;

    private ConnectivityManagerWrapper() {
        try {
            this.mIsNetworkSupport = Method.of(ConnectivityManager.class, "isNetworkSupported", "(I)Z");
        } catch (Exception unused) {
            this.mIsNetworkSupport = null;
        }
    }

    public static ConnectivityManagerWrapper getInstance() {
        if (sInstance == null) {
            sInstance = new ConnectivityManagerWrapper();
        }
        return sInstance;
    }

    public boolean isNetworkSupported(ConnectivityManager connectivityManager, int i) {
        try {
            return this.mIsNetworkSupport.invokeBoolean(null, connectivityManager, Integer.valueOf(i));
        } catch (RuntimeException unused) {
            return false;
        }
    }
}
