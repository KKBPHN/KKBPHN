package com.miui.internal.variable.v16;

import android.view.Window;
import com.miui.internal.variable.VariableExceptionHandler;
import miui.reflect.Method;

public class Android_View_Window_class extends com.miui.internal.variable.Android_View_Window_class {
    private static final int EXTRA_FLAG_STATUS_BAR_DARK_MODE = 16;
    private static final int EXTRA_FLAG_STATUS_BAR_TRANSPARENT = 1;
    private static final int EXTRA_FLAG_STATUS_BAR_TRANSPARENT_MASK = 17;

    public boolean setTranslucentStatus(Window window, int i) {
        VariableExceptionHandler instance;
        String str;
        Method method = com.miui.internal.variable.Android_View_Window_class.setExtraFlags;
        boolean z = false;
        if (method == null) {
            return false;
        }
        if (i == 0) {
            try {
                method.invoke(null, window, Integer.valueOf(0), Integer.valueOf(17));
            } catch (Exception e) {
                e = e;
                instance = VariableExceptionHandler.getInstance();
                str = "clearExtraFlags failed";
            }
        } else {
            try {
                com.miui.internal.variable.Android_View_Window_class.setExtraFlags.invoke(null, window, Integer.valueOf(i == 1 ? 17 : 1), Integer.valueOf(17));
            } catch (Exception e2) {
                e = e2;
                instance = VariableExceptionHandler.getInstance();
                str = "addExtraFlags failed";
                instance.onThrow(str, e);
                return z;
            }
        }
        z = true;
        return z;
    }
}
