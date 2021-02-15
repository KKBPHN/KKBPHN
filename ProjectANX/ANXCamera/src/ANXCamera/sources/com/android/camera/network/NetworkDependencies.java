package com.android.camera.network;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import com.android.camera.network.net.HttpManager;
import com.android.camera.network.util.NetworkUtils;
import java.io.File;

public class NetworkDependencies {
    public static void depend(Application application) {
        HttpManager.getInstance().initRequestQueue(application);
        NetworkUtils.bind(application);
    }

    public static File getRequestCache(Context context) {
        return context.getExternalCacheDir();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16);
    }

    public static boolean isConnectedWIFI(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        return networkCapabilities != null && networkCapabilities.hasCapability(12) && networkCapabilities.hasCapability(16) && networkCapabilities.hasTransport(1);
    }
}
