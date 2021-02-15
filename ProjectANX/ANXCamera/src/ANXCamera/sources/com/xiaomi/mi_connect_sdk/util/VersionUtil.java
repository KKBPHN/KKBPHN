package com.xiaomi.mi_connect_sdk.util;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.xiaomi.mi_connect_sdk.Version;

public class VersionUtil {
    public static String getSdkVersion() {
        StringBuilder sb = new StringBuilder();
        sb.append(Version.getMiconnectSpecVersionCode());
        String str = ".";
        sb.append(str);
        sb.append(Version.getSdkVersionCode());
        sb.append(str);
        sb.append(Version.getAidlVersionCode());
        sb.append(str);
        sb.append(Version.getLittleVersionCode());
        return sb.toString();
    }

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
