package com.android.camera.visibilityutils.calculator;

import com.android.camera.visibilityutils.scroll_utils.ItemsPositionGetter;

public interface ListItemsVisibilityCalculator {
    void onScroll(ItemsPositionGetter itemsPositionGetter, int i, int i2, int i3);

    void onScrollStateIdle(ItemsPositionGetter itemsPositionGetter, int i, int i2);
}
