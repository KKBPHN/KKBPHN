package com.android.camera;

import android.graphics.Rect;

public interface IDisplayRect {
    int getBottomBarHeight();

    int getBottomHeight();

    int getBottomMargin();

    int getCenterDisplayHeight();

    String getDisplayRatio();

    Rect getDisplayRect(int i);

    int getDragDistanceFix();

    int getDragLayoutTopMargin();

    int getEndMargin();

    Rect getMaxViewFinderRect();

    int[] getMoreModePrefVideo(boolean z);

    int getMoreModeTabCol(int i, boolean z);

    int getMoreModeTabMarginVer(int i, boolean z);

    int getMoreModeTabRow(int i, boolean z);

    int getSquareBottomCoverHeight();

    int getStartMargin();

    int getTipsMarginTop();

    int getTopBarHeight();

    int getTopBarWidth();

    int getTopCoverHeight();

    int getTopMargin();

    boolean needAlphaAnimation4PopMore();
}
