package com.android.camera;

import android.content.Context;
import android.graphics.Rect;
import com.android.camera.log.Log;

public class Display456PreviewRect implements IDisplayRect {
    private final Context mContext;
    private final DisplayParameter mDisplayParameter;

    public Display456PreviewRect(Context context, DisplayParameter displayParameter) {
        this.mContext = context;
        this.mDisplayParameter = displayParameter;
    }

    public int getBottomBarHeight() {
        return Math.round(((float) this.mDisplayParameter.windowHeight) * 0.205f);
    }

    public int getBottomHeight() {
        return getBottomMargin() + getBottomBarHeight();
    }

    public int getBottomMargin() {
        return getTopBarHeight();
    }

    public int getCenterDisplayHeight() {
        return Math.round(((float) this.mDisplayParameter.windowHeight) * 0.592f);
    }

    public String getDisplayRatio() {
        return Display.DISPLAY_RATIO_456;
    }

    public Rect getDisplayRect(int i) {
        int i2;
        int i3;
        if (i == 3) {
            i2 = this.mDisplayParameter.windowHeight;
            i3 = 0;
        } else if (i != 4) {
            i3 = Display.getTopMargin() + Display.getTopBarHeight();
            i2 = (int) (((float) this.mDisplayParameter.windowWidth) * (i == 0 ? 1.3333334f : 1.7777778f));
        } else {
            i2 = this.mDisplayParameter.windowWidth;
            i3 = getTopCoverHeight();
        }
        Rect rect = new Rect(0, i3, this.mDisplayParameter.windowWidth, i2 + i3);
        StringBuilder sb = new StringBuilder();
        sb.append("getDisplayRect:");
        sb.append(rect);
        Log.d("IDisplayRect", sb.toString());
        return rect;
    }

    public int getDragDistanceFix() {
        return getBottomHeight();
    }

    public int getDragLayoutTopMargin() {
        return getTopMargin() + getTopBarHeight() + getDisplayRect(1).height();
    }

    public int getEndMargin() {
        return 0;
    }

    public Rect getMaxViewFinderRect() {
        return getDisplayRect(1);
    }

    public int[] getMoreModePrefVideo(boolean z) {
        int[] iArr = {R.raw.more_mode_style_tab_light_456, R.raw.more_mode_style_popup_light_456};
        if (z) {
            // fill-array-data instruction
            iArr[0] = 2131689515;
            iArr[1] = 2131689509;
            return iArr;
        }
        // fill-array-data instruction
        iArr[0] = 2131689512;
        iArr[1] = 2131689506;
        return iArr;
    }

    public int getMoreModeTabCol(int i, boolean z) {
        return z ? 2 : 3;
    }

    public int getMoreModeTabMarginVer(int i, boolean z) {
        if (z) {
            return this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_margin_new);
        }
        int height = getDisplayRect(i).height();
        int moreModeTabRow = getMoreModeTabRow(i, false);
        return (height - (this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_width) * moreModeTabRow)) / (moreModeTabRow + 1);
    }

    public int getMoreModeTabRow(int i, boolean z) {
        return (i == 0 || i == 4) ? (z ? 1 : 0) + true : z + true;
    }

    public int getSquareBottomCoverHeight() {
        DisplayParameter displayParameter = this.mDisplayParameter;
        float bottomHeight = (((((float) displayParameter.windowHeight) - (((float) displayParameter.windowWidth) / 0.75f)) - ((float) getBottomHeight())) - ((float) getTopMargin())) - ((float) getTopBarHeight());
        int i = this.mDisplayParameter.windowWidth;
        return (int) (bottomHeight + (((((float) i) / 0.75f) - ((float) i)) * 0.5f));
    }

    public int getStartMargin() {
        return 0;
    }

    public int getTipsMarginTop() {
        return getDisplayRect(0).top + this.mContext.getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_top);
    }

    public int getTopBarHeight() {
        return Math.round(((float) (((this.mDisplayParameter.windowHeight - getTopMargin()) - getCenterDisplayHeight()) - getBottomBarHeight())) * 0.5f);
    }

    public int getTopBarWidth() {
        return this.mDisplayParameter.windowWidth - (this.mContext.getResources().getDimensionPixelSize(R.dimen.top_bar_margin) * 2);
    }

    public int getTopCoverHeight() {
        return ((this.mDisplayParameter.windowHeight - getBottomHeight()) - this.mDisplayParameter.windowWidth) - getSquareBottomCoverHeight();
    }

    public int getTopMargin() {
        DisplayParameter displayParameter = this.mDisplayParameter;
        if (displayParameter.isNotchDevice) {
            return displayParameter.statusBarHeight;
        }
        return 0;
    }

    public boolean needAlphaAnimation4PopMore() {
        return false;
    }
}
