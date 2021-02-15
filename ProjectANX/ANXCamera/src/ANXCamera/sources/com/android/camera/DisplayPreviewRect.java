package com.android.camera;

import android.content.Context;
import android.graphics.Rect;
import com.android.camera.log.Log;

public class DisplayPreviewRect implements IDisplayRect {
    private final Context mContext;
    private final DisplayParameter mDisplayParameter;

    public DisplayPreviewRect(Context context, DisplayParameter displayParameter) {
        this.mContext = context;
        this.mDisplayParameter = displayParameter;
    }

    public int getBottomBarHeight() {
        return getCenterDisplayHeight() - Math.round(((float) (this.mDisplayParameter.windowWidth * 4)) / 3.0f);
    }

    public int getBottomHeight() {
        return getBottomMargin() + getBottomBarHeight();
    }

    public int getBottomMargin() {
        return getTopBarHeight();
    }

    public int getCenterDisplayHeight() {
        return Math.round(((float) (this.mDisplayParameter.windowWidth * 16)) / 9.0f);
    }

    public String getDisplayRatio() {
        return Display.DISPLAY_RATIO_169;
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
        return getTopMargin() + getTopBarHeight() + getDisplayRect(0).height();
    }

    public int getEndMargin() {
        return 0;
    }

    public Rect getMaxViewFinderRect() {
        return getDisplayRect(0);
    }

    public int[] getMoreModePrefVideo(boolean z) {
        int[] iArr = {R.raw.more_mode_style_tab_light, R.raw.more_mode_style_popup_light};
        if (z) {
            // fill-array-data instruction
            iArr[0] = 2131689513;
            iArr[1] = 2131689507;
            return iArr;
        }
        // fill-array-data instruction
        iArr[0] = 2131689510;
        iArr[1] = 2131689504;
        return iArr;
    }

    public int getMoreModeTabCol(int i, boolean z) {
        return z ? 2 : 3;
    }

    public int getMoreModeTabMarginVer(int i, boolean z) {
        return z ? this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_margin_new) : this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_height_normal) - this.mContext.getResources().getDimensionPixelSize(R.dimen.mode_item_width);
    }

    public int getMoreModeTabRow(int i, boolean z) {
        return 4;
    }

    public int getSquareBottomCoverHeight() {
        int i = this.mDisplayParameter.windowWidth;
        return (int) (((((float) i) / 0.75f) - ((float) i)) * 0.5f);
    }

    public int getStartMargin() {
        return 0;
    }

    public int getTipsMarginTop() {
        return getDisplayRect(0).top + this.mContext.getResources().getDimensionPixelSize(R.dimen.video_ultra_tip_margin_top);
    }

    public int getTopBarHeight() {
        return Math.round(((float) ((this.mDisplayParameter.windowHeight - getTopMargin()) - getCenterDisplayHeight())) * 0.5f);
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
        return true;
    }
}
