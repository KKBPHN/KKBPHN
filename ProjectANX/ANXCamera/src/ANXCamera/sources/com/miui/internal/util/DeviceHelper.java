package com.miui.internal.util;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.text.TextUtils;
import miui.os.SystemProperties;

public class DeviceHelper {
    public static final boolean FEATURE_WHOLE_ANIM = SystemProperties.getBoolean("ro.sys.ft_whole_anim", true);
    public static final boolean HAS_CUST_PARTITION = SystemProperties.getBoolean("ro.miui.has_cust_partition", false);
    public static final boolean IS_ALPHA_BUILD;
    public static final boolean IS_DEBUGGABLE = (SystemProperties.getInt("ro.debuggable", 0) == 1);
    public static final boolean IS_DEVELOPMENT_VERSION;
    public static final boolean IS_INTERNATIONAL_BUILD;
    public static final boolean IS_MIFOUR;
    public static final boolean IS_OLED;
    public static final boolean IS_PCMODE_ENABLED = (SystemProperties.getInt("persist.sys.miui.pcmode", 0) > 0);
    public static final boolean IS_STABLE_VERSION;
    public static final boolean IS_TABLET = isTablet();
    private static final String REGULAR_EXPRESSION_FOR_DEVELOPMENT = "\\d+.\\d+.\\d+(-internal)?";

    static {
        boolean z = true;
        boolean z2 = "cancro".equals(Build.DEVICE) && Build.MODEL.startsWith("MI 4");
        IS_MIFOUR = z2;
        String str = "";
        String str2 = "ro.product.mod_device";
        IS_ALPHA_BUILD = SystemProperties.get(str2, str).endsWith("_alpha");
        IS_INTERNATIONAL_BUILD = SystemProperties.get(str2, str).endsWith("_global");
        boolean z3 = !TextUtils.isEmpty(VERSION.INCREMENTAL) && VERSION.INCREMENTAL.matches(REGULAR_EXPRESSION_FOR_DEVELOPMENT);
        IS_DEVELOPMENT_VERSION = z3;
        boolean z4 = "user".equals(Build.TYPE) && !IS_DEVELOPMENT_VERSION;
        IS_STABLE_VERSION = z4;
        String str3 = "oled";
        if (!str3.equals(SystemProperties.get("ro.vendor.display.type")) && !str3.equals(SystemProperties.get("ro.display.type"))) {
            z = false;
        }
        IS_OLED = z;
    }

    public static String getDebugEnable() {
        return SystemProperties.get("persist.sys.miui.sdk.dbg", "");
    }

    public static String getRegion() {
        return SystemProperties.get("ro.miui.region", "CN");
    }

    public static boolean isDeviceProvisioned(Context context) {
        return Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
    }

    public static boolean isHideGestureLine(Context context) {
        return Global.getInt(context.getContentResolver(), "hide_gesture_line", 0) != 0;
    }

    private static boolean isTablet() {
        return SystemProperties.get("ro.build.characteristics", "default").contains("tablet");
    }
}
