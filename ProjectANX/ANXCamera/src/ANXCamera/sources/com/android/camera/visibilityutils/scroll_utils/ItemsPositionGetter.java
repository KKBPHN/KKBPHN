package com.android.camera.visibilityutils.scroll_utils;

import android.view.View;

public interface ItemsPositionGetter {
    View getChildAt(int i);

    int getChildCount();

    int getFirstVisiblePosition();

    int getLastVisiblePosition();

    int indexOfChild(View view);
}
