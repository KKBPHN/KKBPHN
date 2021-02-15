package com.android.camera.visibilityutils.calculator;

import android.view.View;
import com.android.camera.visibilityutils.calculator.SingleListViewItemActiveCalculator.Callback;
import com.android.camera.visibilityutils.items.ListItem;

public class DefaultSingleItemCalculatorCallback implements Callback {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "DefaultSingleItemCalculatorCallback";

    public void activateNewCurrentItem(ListItem listItem, View view, int i) {
        listItem.setActive(view, i);
    }

    public void deactivateCurrentItem(ListItem listItem, View view, int i) {
        listItem.deactivate(view, i);
    }
}
