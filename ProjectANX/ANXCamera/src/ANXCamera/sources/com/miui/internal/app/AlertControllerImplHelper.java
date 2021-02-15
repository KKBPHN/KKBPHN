package com.miui.internal.app;

import android.view.View;
import android.view.Window;
import android.widget.TextView;
import miui.animation.Folme;
import miui.animation.ITouchStyle.TouchType;
import miui.animation.base.AnimConfig;
import miui.view.EditActionMode;

public class AlertControllerImplHelper {
    public static void changeTitlePadding(TextView textView) {
        textView.setPadding(textView.getPaddingLeft(), textView.getPaddingTop(), textView.getPaddingRight(), 0);
    }

    public static int getLandScapeGravity() {
        return 17;
    }

    public static void initTouchAnim(Window window) {
        if (window.findViewById(EditActionMode.BUTTON1) != null) {
            View findViewById = window.findViewById(EditActionMode.BUTTON1);
            Folme.useAt(findViewById).touch().setScale(1.0f, TouchType.DOWN).handleTouchOf(findViewById, new AnimConfig[0]);
        }
        if (window.findViewById(EditActionMode.BUTTON2) != null) {
            View findViewById2 = window.findViewById(EditActionMode.BUTTON2);
            Folme.useAt(findViewById2).touch().setScale(1.0f, TouchType.DOWN).handleTouchOf(findViewById2, new AnimConfig[0]);
        }
        if (window.findViewById(16908315) != null) {
            View findViewById3 = window.findViewById(16908315);
            Folme.useAt(findViewById3).touch().setScale(1.0f, TouchType.DOWN).handleTouchOf(findViewById3, new AnimConfig[0]);
        }
    }
}
