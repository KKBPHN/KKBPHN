package com.miui.internal.widget;

import android.view.View;
import miui.animation.Folme;
import miui.animation.base.AnimConfig;

public class FloatingActionButtonHelper {
    public static void initFabTouchAnimation(View view) {
        Folme.useAt(view).touch().handleTouchOf(view, new AnimConfig[0]);
    }
}
