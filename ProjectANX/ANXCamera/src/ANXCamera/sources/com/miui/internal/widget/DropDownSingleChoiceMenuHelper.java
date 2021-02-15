package com.miui.internal.widget;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import com.miui.internal.R;

public class DropDownSingleChoiceMenuHelper {
    public static View getView(Context context, int i, int i2, View view) {
        int dimensionPixelSize;
        Resources resources;
        int i3;
        Resources resources2;
        int i4;
        view.getLayoutParams();
        int paddingStart = view.getPaddingStart();
        view.getPaddingTop();
        int paddingEnd = view.getPaddingEnd();
        view.getPaddingBottom();
        if (i != 1) {
            if (i2 == 0) {
                resources2 = context.getResources();
                i4 = R.dimen.drop_down_menu_padding_large;
                dimensionPixelSize = resources2.getDimensionPixelSize(i4);
                resources = context.getResources();
                i3 = R.dimen.drop_down_menu_padding_small;
                view.setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, resources.getDimensionPixelSize(i3));
                return view;
            } else if (i2 == i - 1) {
                dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.drop_down_menu_padding_small);
                resources = context.getResources();
                i3 = R.dimen.drop_down_menu_padding_large;
                view.setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, resources.getDimensionPixelSize(i3));
                return view;
            }
        }
        resources2 = context.getResources();
        i4 = R.dimen.drop_down_menu_padding_small;
        dimensionPixelSize = resources2.getDimensionPixelSize(i4);
        resources = context.getResources();
        i3 = R.dimen.drop_down_menu_padding_small;
        view.setPaddingRelative(paddingStart, dimensionPixelSize, paddingEnd, resources.getDimensionPixelSize(i3));
        return view;
    }
}
