package com.miui.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import com.miui.internal.R;

public class ListMenuPresenterHelper {
    public static void calcAndSetPadding(Context context, int i, int i2, View view) {
        int i3;
        int i4;
        Resources resources;
        int paddingStart = view.getPaddingStart();
        view.getPaddingTop();
        int paddingEnd = view.getPaddingEnd();
        view.getPaddingBottom();
        if (i2 == i - 1) {
            i4 = context.getResources().getDimensionPixelSize(R.dimen.list_menu_item_padding_small);
            resources = context.getResources();
            i3 = R.dimen.list_menu_item_padding_large;
        } else {
            i4 = context.getResources().getDimensionPixelSize(R.dimen.list_menu_item_padding_small);
            resources = context.getResources();
            i3 = R.dimen.list_menu_item_padding_small;
        }
        view.setPaddingRelative(paddingStart, i4, paddingEnd, resources.getDimensionPixelSize(i3));
    }
}
