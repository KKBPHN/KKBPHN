package com.miui.internal.widget;

import android.view.View;
import miui.animation.Folme;
import miui.animation.ITouchStyle.TouchType;
import miui.animation.base.AnimConfig;

public class Android_Preference_Preference_class_helper {
    public static void setPreferenceTouchAnim(View view) {
        Folme.useAt(view).touch().setScale(1.0f, TouchType.DOWN).handleTouchOf(view, new AnimConfig[0]);
    }
}
