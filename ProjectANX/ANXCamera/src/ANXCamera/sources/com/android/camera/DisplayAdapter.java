package com.android.camera;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.NonNull;
import com.android.camera.log.Log;

public class DisplayAdapter implements IDisplayRect {
    private static final String TAG = "DisplayAdapter";
    private final Context mContext;
    private IDisplayRect mDisplayRect;
    private final DisplayParameter mParameter;

    class DisplayParameter {
        boolean isNotchDevice;
        int statusBarHeight;
        int windowHeight;
        int windowWidth;

        DisplayParameter() {
        }

        @NonNull
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("windowWidth:");
            sb.append(this.windowWidth);
            sb.append(",windowHeight:");
            sb.append(this.windowHeight);
            sb.append(",isNotchDevice:");
            sb.append(this.isNotchDevice);
            sb.append(",statusBarHeight:");
            sb.append(this.statusBarHeight);
            return sb.toString();
        }
    }

    public DisplayAdapter(Context context, DisplayParameter displayParameter) {
        IDisplayRect displayPreviewRect;
        this.mContext = context;
        this.mParameter = displayParameter;
        DisplayParameter displayParameter2 = this.mParameter;
        if (CameraSettings.isAspectRatio123(displayParameter2.windowWidth, displayParameter2.windowHeight)) {
            Log.d(TAG, "init Display123PreviewRect");
            displayPreviewRect = new Display123PreviewRect(context, this.mParameter);
        } else {
            DisplayParameter displayParameter3 = this.mParameter;
            if (CameraSettings.isAspectRatio456(displayParameter3.windowWidth, displayParameter3.windowHeight)) {
                Log.d(TAG, "init Display456PreviewRect");
                displayPreviewRect = new Display456PreviewRect(context, this.mParameter);
            } else {
                Log.d(TAG, "init DisplayPreviewRect");
                displayPreviewRect = new DisplayPreviewRect(context, this.mParameter);
            }
        }
        this.mDisplayRect = displayPreviewRect;
    }

    public int getBottomBarHeight() {
        return this.mDisplayRect.getBottomBarHeight();
    }

    public int getBottomHeight() {
        return this.mDisplayRect.getBottomHeight();
    }

    public int getBottomMargin() {
        return this.mDisplayRect.getBottomMargin();
    }

    public int getCenterDisplayHeight() {
        return this.mDisplayRect.getCenterDisplayHeight();
    }

    public String getDisplayRatio() {
        return this.mDisplayRect.getDisplayRatio();
    }

    public Rect getDisplayRect(int i) {
        return this.mDisplayRect.getDisplayRect(i);
    }

    public int getDragDistanceFix() {
        return this.mDisplayRect.getDragDistanceFix();
    }

    public int getDragLayoutTopMargin() {
        return this.mDisplayRect.getDragLayoutTopMargin();
    }

    public int getEndMargin() {
        return this.mDisplayRect.getEndMargin();
    }

    public Rect getMaxViewFinderRect() {
        return this.mDisplayRect.getMaxViewFinderRect();
    }

    public int[] getMoreModePrefVideo(boolean z) {
        return this.mDisplayRect.getMoreModePrefVideo(z);
    }

    public int getMoreModeTabCol(int i, boolean z) {
        return this.mDisplayRect.getMoreModeTabCol(i, z);
    }

    public int getMoreModeTabMarginVer(int i, boolean z) {
        return this.mDisplayRect.getMoreModeTabMarginVer(i, z);
    }

    public int getMoreModeTabRow(int i, boolean z) {
        return this.mDisplayRect.getMoreModeTabRow(i, z);
    }

    public int getSquareBottomCoverHeight() {
        return this.mDisplayRect.getSquareBottomCoverHeight();
    }

    public int getStartMargin() {
        return this.mDisplayRect.getStartMargin();
    }

    public int getTipsMarginTop() {
        return this.mDisplayRect.getTipsMarginTop();
    }

    public int getTopBarHeight() {
        return this.mDisplayRect.getTopBarHeight();
    }

    public int getTopBarWidth() {
        return this.mDisplayRect.getTopBarWidth();
    }

    public int getTopCoverHeight() {
        return this.mDisplayRect.getTopCoverHeight();
    }

    public int getTopMargin() {
        return this.mDisplayRect.getTopMargin();
    }

    public boolean needAlphaAnimation4PopMore() {
        return this.mDisplayRect.needAlphaAnimation4PopMore();
    }
}
