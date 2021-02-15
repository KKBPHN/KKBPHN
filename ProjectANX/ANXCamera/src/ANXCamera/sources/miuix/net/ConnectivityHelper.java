package miuix.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import androidx.annotation.RequiresPermission;
import miuix.core.util.ConnectivityManagerWrapper;
import miuix.core.util.SoftReferenceSingleton;

public class ConnectivityHelper {
    private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
        /* access modifiers changed from: protected */
        public ConnectivityHelper createInstance(Object obj) {
            return new ConnectivityHelper((Context) obj);
        }
    };
    private static final String TAG = "ConnectivityHelper";
    private ConnectivityManager mConnectivityManager;
    private String mMacAddress;
    private WifiManager mWifiManager;

    private ConnectivityHelper(Context context) {
        this.mConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    public static ConnectivityHelper getInstance(Context context) {
        return (ConnectivityHelper) INSTANCE.get(context);
    }

    @RequiresPermission("android.permission.ACCESS_WIFI_STATE")
    public String getMacAddress(Context context) {
        String str = this.mMacAddress;
        if (str != null) {
            return str;
        }
        if (this.mWifiManager == null) {
            this.mWifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        }
        WifiInfo connectionInfo = this.mWifiManager.getConnectionInfo();
        if (connectionInfo != null) {
            this.mMacAddress = connectionInfo.getMacAddress();
        }
        return this.mMacAddress;
    }

    public ConnectivityManager getManager() {
        return this.mConnectivityManager;
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public boolean isNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public boolean isUnmeteredNetworkConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && !this.mConnectivityManager.isActiveNetworkMetered();
    }

    @RequiresPermission("android.permission.ACCESS_NETWORK_STATE")
    public boolean isWifiConnected() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected() && activeNetworkInfo.getType() == 1;
    }

    public boolean isWifiOnly() {
        return !ConnectivityManagerWrapper.getInstance().isNetworkSupported(this.mConnectivityManager, 0);
    }
}
