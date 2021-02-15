package miuix.core.util.variable;

import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;
import java.lang.reflect.Method;
import miuix.reflect.Reflects;

public class WindowWrapper {
    private static final int EXTRA_FLAG_STATUS_BAR_DARK_MODE = 16;
    private static final int EXTRA_FLAG_STATUS_BAR_TRANSPARENT = 1;
    private static final int EXTRA_FLAG_STATUS_BAR_TRANSPARENT_MASK = 17;
    private static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
    private static final int FLAG_TRANSLUCENT_STATUS = 67108864;
    private static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;
    protected static Method setExtraFlags;

    static {
        try {
            setExtraFlags = Reflects.getMethod(Window.class, "setExtraFlags", Integer.TYPE, Integer.TYPE);
        } catch (Exception unused) {
            setExtraFlags = null;
        }
    }

    public static boolean setTranslucentStatus(Window window, int i) {
        boolean z = false;
        if (setExtraFlags == null) {
            return false;
        }
        if (VERSION.SDK_INT >= 23) {
            if (i == 0) {
                window.clearFlags(Integer.MIN_VALUE);
            } else {
                window.addFlags(Integer.MIN_VALUE);
                View decorView = window.getDecorView();
                decorView.setSystemUiVisibility(i == 1 ? 8192 : decorView.getSystemUiVisibility() & -8193);
            }
        }
        if (VERSION.SDK_INT >= 19) {
            if (i == 0) {
                window.clearFlags(FLAG_TRANSLUCENT_STATUS);
            } else {
                window.setFlags(FLAG_TRANSLUCENT_STATUS, FLAG_TRANSLUCENT_STATUS);
            }
        }
        if (i == 0) {
            try {
                setExtraFlags.invoke(window, new Object[]{Integer.valueOf(0), Integer.valueOf(17)});
            } catch (Exception unused) {
            }
        } else {
            setExtraFlags.invoke(window, new Object[]{Integer.valueOf(i == 1 ? 17 : 1), Integer.valueOf(17)});
        }
        z = true;
        return z;
    }
}
