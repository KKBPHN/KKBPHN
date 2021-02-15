package com.miui.internal.variable.v23;

import android.view.View;
import android.view.Window;

public class Android_View_Window_class extends com.miui.internal.variable.v19.Android_View_Window_class {
    private static final int FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS = Integer.MIN_VALUE;
    private static final int SYSTEM_UI_FLAG_LIGHT_STATUS_BAR = 8192;

    public boolean setTranslucentStatus(Window window, int i) {
        if (i == 0) {
            window.clearFlags(Integer.MIN_VALUE);
        } else {
            window.addFlags(Integer.MIN_VALUE);
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(i == 1 ? 8192 : decorView.getSystemUiVisibility() & -8193);
        }
        return super.setTranslucentStatus(window, i);
    }
}
