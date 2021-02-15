package com.android.camera.visibilityutils.calculator;

import com.android.camera.visibilityutils.scroll_utils.ItemsPositionGetter;
import com.android.camera.visibilityutils.scroll_utils.ScrollDirectionDetector;
import com.android.camera.visibilityutils.scroll_utils.ScrollDirectionDetector.OnDetectScrollListener;

public abstract class BaseItemsVisibilityCalculator implements ListItemsVisibilityCalculator, OnDetectScrollListener {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "BaseItemsVisibilityCalculator";
    private final ScrollDirectionDetector mScrollDirectionDetector = new ScrollDirectionDetector(this);

    private String scrollStateStr(int i) {
        if (i == 0) {
            return "SCROLL_STATE_IDLE";
        }
        if (i == 1) {
            return "SCROLL_STATE_TOUCH_SCROLL";
        }
        if (i == 2) {
            return "SCROLL_STATE_FLING";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("wrong data, scrollState ");
        sb.append(i);
        throw new RuntimeException(sb.toString());
    }

    public void onScroll(ItemsPositionGetter itemsPositionGetter, int i, int i2, int i3) {
        this.mScrollDirectionDetector.onDetectedListScroll(itemsPositionGetter, i);
        if (i3 == 0) {
            return;
        }
        if (i3 == 1 || i3 == 2) {
            onStateTouchScroll(itemsPositionGetter);
        }
    }

    public abstract void onStateFling(ItemsPositionGetter itemsPositionGetter);

    public abstract void onStateTouchScroll(ItemsPositionGetter itemsPositionGetter);
}
