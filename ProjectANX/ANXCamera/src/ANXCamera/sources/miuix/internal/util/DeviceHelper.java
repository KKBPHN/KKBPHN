package miuix.internal.util;

import android.content.Context;
import android.provider.Settings.Global;
import miuix.core.util.SystemProperties;

public class DeviceHelper {
    public static final boolean IS_DEBUGGABLE;

    static {
        boolean z = false;
        if (SystemProperties.getInt("ro.debuggable", 0) == 1) {
            z = true;
        }
        IS_DEBUGGABLE = z;
    }

    public static boolean isFeatureWholeAnim() {
        return true;
    }

    public static boolean isHideGestureLine(Context context) {
        return Global.getInt(context.getContentResolver(), "hide_gesture_line", 0) != 0;
    }

    public static boolean isOled() {
        return false;
    }

    public static boolean isTablet() {
        return false;
    }
}
