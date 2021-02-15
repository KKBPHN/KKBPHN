package com.miui.internal.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.miui.internal.R;
import miui.util.AttributeResolver;

public class ArrowPopupWindowHelper {
    public static void addShadow(View view, ViewOutlineProvider viewOutlineProvider) {
        view.setOutlineProvider(viewOutlineProvider);
        view.setElevation((float) getPopupElevation(view.getContext()));
    }

    private static int getPopupElevation(Context context) {
        return AttributeResolver.resolveDimensionPixelSize(context, R.attr.arrowPopupWindowElevation);
    }
}
