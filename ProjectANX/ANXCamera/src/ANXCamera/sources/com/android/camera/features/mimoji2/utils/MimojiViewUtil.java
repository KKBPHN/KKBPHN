package com.android.camera.features.mimoji2.utils;

import android.view.View;
import com.android.camera.log.Log;

public class MimojiViewUtil {
    public static final String TAG = MimojiViewUtil.class.getClass().getSimpleName();
    public static boolean isDebug = false;

    public static boolean getViewIsVisible(View view) {
        boolean z = false;
        if (view == null) {
            return false;
        }
        boolean z2 = view.getVisibility() == 0;
        boolean z3 = view.getAlpha() == 1.0f;
        if (isDebug) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji boolean getViewIsVisible[view] ");
            sb.append(view.getId());
            sb.append(" : ");
            sb.append(z2);
            sb.append("  ");
            sb.append(z3);
            Log.d(str, sb.toString());
        }
        if (z2 && z3) {
            z = true;
        }
        return z;
    }

    public static boolean setViewVisible(View view, boolean z) {
        int i = 0;
        if (view == null) {
            return false;
        }
        if (isDebug) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("mimoji void setViewVisible[view, isVisible]");
            sb.append(view.getId());
            sb.append(view.getId());
            sb.append(" : ");
            sb.append(z);
            Log.d(str, sb.toString());
        }
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
        view.setAlpha(z ? 1.0f : 0.0f);
        return true;
    }
}
