package com.xiaomi.stat.d;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.xiaomi.stat.ak;
import java.util.Iterator;

public class l {
    private static final String a = "NetWorkStateUtil";
    private static final int b = 16;
    private static final int c = 17;
    private static final int d = 18;
    private static final int e = 19;
    private static final String f = "2G";
    private static final String g = "3G";
    private static final String h = "4G";
    private static final String i = "WIFI";
    private static final String j = "ETHERNET";
    private static final String k = "UNKNOWN";
    private static final String l = "NOT_CONNECTED";

    class a {
        private a() {
        }

        public static String a(Context context) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            String str = l.l;
            if (connectivityManager == null) {
                return str;
            }
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
                return str;
            }
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(9);
            if (networkInfo == null || !networkInfo.isConnected()) {
                return (networkInfo2 == null || !networkInfo2.isConnected()) ? l.k : l.j;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(l.i);
            sb.append(b(context));
            return sb.toString();
        }

        private static boolean a(int i) {
            return i > 4900 && i < 5900;
        }

        private static String b(Context context) {
            String str = "";
            try {
                int i = VERSION.SDK_INT;
                String str2 = l.f;
                String str3 = "5G";
                String str4 = "wifi";
                if (i >= 22) {
                    int frequency = ((WifiManager) context.getSystemService(str4)).getConnectionInfo().getFrequency();
                    if (!a(frequency)) {
                        if (!b(frequency)) {
                            return str;
                        }
                        return str2;
                    }
                } else {
                    char c = 65535;
                    WifiManager wifiManager = (WifiManager) context.getSystemService(str4);
                    String ssid = wifiManager.getConnectionInfo().getSSID();
                    String substring = ssid.substring(1, ssid.length() - 1);
                    if (ssid != null && ssid.length() > 2) {
                        Iterator it = wifiManager.getScanResults().iterator();
                        while (true) {
                            if (!it.hasNext()) {
                                break;
                            }
                            ScanResult scanResult = (ScanResult) it.next();
                            if (scanResult.SSID.equals(substring)) {
                                if (a(scanResult.frequency)) {
                                    c = 2;
                                } else if (b(scanResult.frequency)) {
                                    c = 1;
                                }
                            }
                        }
                    }
                    if (c != 2) {
                        if (c != 1) {
                            return str;
                        }
                        return str2;
                    }
                }
                return str3;
            } catch (Exception e) {
                k.d(l.a, "getWifiFreeBand error", e);
                return str;
            }
        }

        private static boolean b(int i) {
            return i > 2400 && i < 2500;
        }
    }

    public static int a(Context context) {
        if (context == null) {
            return 0;
        }
        String b2 = b(context);
        if (!TextUtils.isEmpty(b2) && !b2.equals(l)) {
            if (b2.equals(f)) {
                return 1;
            }
            if (b2.equals(g)) {
                return 2;
            }
            if (b2.equals(h)) {
                return 4;
            }
            if (b2.startsWith(i)) {
                return 8;
            }
            if (b2.equals(j)) {
                return 16;
            }
        }
        return 0;
    }

    public static boolean a() {
        Context a2 = ak.a();
        if (a2 != null) {
            try {
                NetworkInfo activeNetworkInfo = ((ConnectivityManager) a2.getSystemService("connectivity")).getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    return activeNetworkInfo.isConnectedOrConnecting();
                }
            } catch (Exception unused) {
                k.b("isNetworkConnected exception");
            }
        }
        return false;
    }

    public static String b(Context context) {
        String str = k;
        try {
            if (e.w(context)) {
                return a.a(context);
            }
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                if (activeNetworkInfo.isConnected()) {
                    if (activeNetworkInfo.getType() == 1) {
                        return i;
                    }
                    if (activeNetworkInfo.getType() == 0) {
                        switch (activeNetworkInfo.getSubtype()) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                            case 16:
                                return f;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                            case 17:
                                return g;
                            case 13:
                            case 18:
                            case 19:
                                return h;
                            default:
                                return str;
                        }
                    }
                    return str;
                }
            }
            return l;
        } catch (Exception e2) {
            k.d(a, "getNetworkState error", e2);
        }
    }
}
