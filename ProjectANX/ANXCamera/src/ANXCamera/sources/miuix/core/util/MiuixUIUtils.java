package miuix.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class MiuixUIUtils {
    private static final String HIDE_GESTURE_LINE = "hide_gesture_line";
    private static final String TAG = "MiuixUtils";
    private static final String USE_GESTURE_VERSION_THREE = "use_gesture_version_three";

    public static boolean checkDeviceHasNavigationBar(Context context) {
        String str = SystemProperties.get("qemu.hw.mainkeys");
        if ("1".equals(str)) {
            return false;
        }
        if ("0".equals(str)) {
            return true;
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("config_showNavigationBar", "bool", "android");
        if (identifier > 0) {
            return resources.getBoolean(identifier);
        }
        return false;
    }

    private static boolean checkMultiWindow(Activity activity) {
        if (VERSION.SDK_INT >= 24) {
            return activity.isInMultiWindowMode();
        }
        return false;
    }

    public static int getNavigationBarHeight(Context context) {
        if (isInMultiWindowMode(context)) {
            return 0;
        }
        int realNavigationBarHeight = (isShowNavigationHandle(context) || !isNavigationBarFullScreen(context)) ? getRealNavigationBarHeight(context) : 0;
        if (realNavigationBarHeight < 0) {
            realNavigationBarHeight = 0;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getNavigationBarHeight = ");
        sb.append(realNavigationBarHeight);
        Log.i(TAG, sb.toString());
        return realNavigationBarHeight;
    }

    public static int getRealNavigationBarHeight(Context context) {
        int i = 0;
        if (!checkDeviceHasNavigationBar(context)) {
            return 0;
        }
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier > 0) {
            i = resources.getDimensionPixelSize(identifier);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("getNavigationBarHeightFromProp = ");
        sb.append(i);
        Log.i(TAG, sb.toString());
        return i;
    }

    public static boolean isEnableGestureLine(Context context) {
        return Global.getInt(context.getContentResolver(), HIDE_GESTURE_LINE, 0) == 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0017, code lost:
        if ((r1 instanceof android.app.Activity) != false) goto L_0x0004;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isInMultiWindowMode(Context context) {
        if (!(context instanceof Activity)) {
            if (context instanceof ContextThemeWrapper) {
                context = ((ContextThemeWrapper) context).getBaseContext();
            }
            return false;
        }
        return checkMultiWindow((Activity) context);
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == 2;
    }

    public static boolean isNavigationBarFullScreen(Context context) {
        return Global.getInt(context.getContentResolver(), "force_fsg_nav_bar", 0) != 0;
    }

    public static boolean isShowNavigationHandle(Context context) {
        return isEnableGestureLine(context) && isNavigationBarFullScreen(context) && isSupportGestureLine(context);
    }

    public static boolean isSupportGestureLine(Context context) {
        return Global.getInt(context.getContentResolver(), USE_GESTURE_VERSION_THREE, 0) != 0;
    }
}
