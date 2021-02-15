package com.miui.internal.util;

import android.view.View;
import miui.animation.Folme;
import miui.animation.ITouchStyle.TouchType;
import miui.animation.base.AnimConfig;

public class FolmeAnimHelper {
    private FolmeAnimHelper() {
    }

    public static void addAlphaPressAnim(View view) {
        Folme.useAt(view).touch().setScale(1.0f, new TouchType[0]).setAlpha(0.6f, new TouchType[0]).handleTouchOf(view, new AnimConfig[0]);
    }

    public static void addPressAnim(View view) {
        Folme.useAt(view).touch().setScale(1.0f, new TouchType[0]).handleTouchOf(view, new AnimConfig[0]);
    }

    public static void addPressAnimWithTint(View view) {
        Folme.useAt(view).touch().setScale(1.0f, new TouchType[0]).setTint(0.1f, 0.0f, 0.0f, 0.0f).handleTouchOf(view, new AnimConfig[0]);
    }
}
