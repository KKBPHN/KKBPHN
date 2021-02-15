package com.android.camera;

import android.content.Context;
import android.graphics.Rect;
import com.android.camera.log.Log;

public class Display123PreviewRect implements IDisplayRect {
    private final Context mContext;
    private final DisplayParameter mDisplayParameter;

    public Display123PreviewRect(Context context, DisplayParameter displayParameter) {
        this.mContext = context;
        this.mDisplayParameter = displayParameter;
    }

    public int getBottomBarHeight() {
        int max;
        int min;
        if (Display.getScreenOrientation() != 0) {
            DisplayParameter displayParameter = this.mDisplayParameter;
            max = Math.min(displayParameter.windowHeight, displayParameter.windowWidth);
            min = Math.min(getDisplayRect(1).height(), getDisplayRect(1).width());
        } else {
            DisplayParameter displayParameter2 = this.mDisplayParameter;
            max = Math.max(displayParameter2.windowHeight, displayParameter2.windowWidth);
            DisplayParameter displayParameter3 = this.mDisplayParameter;
            min = Math.min(displayParameter3.windowHeight, displayParameter3.windowWidth);
        }
        return (max - min) / 2;
    }

    public int getBottomHeight() {
        return getBottomMargin() + getBottomBarHeight();
    }

    public int getBottomMargin() {
        return 0;
    }

    public int getCenterDisplayHeight() {
        return Math.round((float) ((this.mDisplayParameter.windowWidth * 4) / 3));
    }

    public String getDisplayRatio() {
        return Display.DISPLAY_RATIO_123;
    }

    public Rect getDisplayRect(int i) {
        int i2;
        int i3;
        int i4;
        if (i != 0) {
            if (i == 1) {
                DisplayParameter displayParameter = this.mDisplayParameter;
                int i5 = displayParameter.windowHeight;
                int i6 = (i5 * 9) / 16;
                i2 = (displayParameter.windowWidth - i6) / 2;
                int i7 = i6;
                i3 = i5;
                i4 = i7;
            } else if (!(i == 3 || i == 4)) {
                i2 = 0;
                i4 = 0;
                i3 = 0;
            }
            Rect rect = new Rect(i2, 0, i4 + i2, i3 + 0);
            StringBuilder sb = new StringBuilder();
            sb.append("getDisplayRect:");
            sb.append(rect);
            sb.append(",uiStyle:");
            sb.append(i);
            Log.d("IDisplayRect", sb.toString());
            return rect;
        }
        DisplayParameter displayParameter2 = this.mDisplayParameter;
        i4 = displayParameter2.windowWidth;
        i3 = displayParameter2.windowHeight;
        i2 = 0;
        Rect rect2 = new Rect(i2, 0, i4 + i2, i3 + 0);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("getDisplayRect:");
        sb2.append(rect2);
        sb2.append(",uiStyle:");
        sb2.append(i);
        Log.d("IDisplayRect", sb2.toString());
        return rect2;
    }

    public int getDragDistanceFix() {
        return getBottomBarHeight();
    }

    public int getDragLayoutTopMargin() {
        return (Display.getScreenOrientation() != 0 ? this.mDisplayParameter.windowWidth : this.mDisplayParameter.windowHeight) - getBottomBarHeight();
    }

    public int getEndMargin() {
        return getStartMargin();
    }

    public Rect getMaxViewFinderRect() {
        Rect rect = new Rect();
        rect.set(getStartMargin(), getTopBarHeight(), getStartMargin() + getDisplayRect(1).width(), this.mDisplayParameter.windowHeight - getBottomBarHeight());
        return rect;
    }

    public int[] getMoreModePrefVideo(boolean z) {
        int[] iArr = {R.raw.more_mode_style_tab_light_123, R.raw.more_mode_style_popup_light_123};
        if (z) {
            // fill-array-data instruction
            iArr[0] = 2131689514;
            iArr[1] = 2131689508;
            return iArr;
        }
        // fill-array-data instruction
        iArr[0] = 2131689511;
        iArr[1] = 2131689505;
        return iArr;
    }

    public int getMoreModeTabCol(int i, boolean z) {
        return z ? 3 : 4;
    }

    public int getMoreModeTabMarginVer(int i, boolean z) {
        if (z) {
            return this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_margin_new);
        }
        DisplayParameter displayParameter = this.mDisplayParameter;
        int min = Math.min(displayParameter.windowWidth, displayParameter.windowHeight);
        int moreModeTabRow = getMoreModeTabRow(i, false);
        return (min - (this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_width) * moreModeTabRow)) / (moreModeTabRow + 1);
    }

    public int getMoreModeTabRow(int i, boolean z) {
        return 4;
    }

    public int getSquareBottomCoverHeight() {
        DisplayParameter displayParameter = this.mDisplayParameter;
        return ((displayParameter.windowHeight - displayParameter.windowWidth) / 2) - getBottomHeight();
    }

    public int getStartMargin() {
        return (this.mDisplayParameter.windowWidth - getDisplayRect(1).width()) / 2;
    }

    public int getTipsMarginTop() {
        return getTopMargin() + getTopBarHeight() + this.mContext.getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_top);
    }

    public int getTopBarHeight() {
        DisplayParameter displayParameter = this.mDisplayParameter;
        int max = Math.max(displayParameter.windowHeight, displayParameter.windowWidth);
        DisplayParameter displayParameter2 = this.mDisplayParameter;
        return (max - Math.min(displayParameter2.windowHeight, displayParameter2.windowWidth)) / 2;
    }

    public int getTopBarWidth() {
        return ((this.mDisplayParameter.windowWidth - getStartMargin()) - getEndMargin()) - (this.mContext.getResources().getDimensionPixelSize(R.dimen.top_bar_margin) * 2);
    }

    public int getTopCoverHeight() {
        DisplayParameter displayParameter = this.mDisplayParameter;
        return (displayParameter.windowHeight - displayParameter.windowWidth) / 2;
    }

    public int getTopMargin() {
        return 0;
    }

    public boolean needAlphaAnimation4PopMore() {
        return true;
    }
}
