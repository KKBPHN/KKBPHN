package miuix.view;

import android.util.Log;
import androidx.annotation.Keep;
import miui.util.HapticFeedbackUtil;
import miui.view.MiuiHapticFeedbackConstants;

@Keep
public class PlatformConstants {
    public static final int VERSION;

    static {
        int i;
        String str = "HapticCompat";
        try {
            i = (Class.forName("miui.util.HapticFeedbackUtil").getMethod("isSupportLinearMotorVibrate", new Class[]{Integer.TYPE}) == null || Class.forName("miui.view.MiuiHapticFeedbackConstants").getDeclaredField("FLAG_MIUI_HAPTIC_VERSION") == null) ? 0 : 4;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            Log.w(str, "MIUI Haptic Implementation not found.", e);
            i = -1;
        } catch (NoSuchFieldException e2) {
            Log.w(str, "error when getting FLAG_MIUI_HAPTIC_VERSION.", e2);
            i = checkVersion();
        }
        VERSION = i;
        Log.i(str, String.format("Platform version: %d.", new Object[]{Integer.valueOf(VERSION)}));
    }

    static int checkVersion() {
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435470)) {
            return 4;
        }
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(268435469)) {
            return 3;
        }
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_TRIGGER_DRAWER)) {
            return 2;
        }
        return HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_POPUP_LIGHT) ? 1 : 0;
    }
}
