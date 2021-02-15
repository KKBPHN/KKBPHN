package com.miui.internal.widget;

import android.graphics.drawable.LayerDrawable;
import android.widget.ProgressBar;

public class ProgressBarDelegateHelper {
    public static void setOutLayerParams(LayerDrawable layerDrawable, LayerDrawable layerDrawable2, int i) {
        layerDrawable.setLayerWidth(i, layerDrawable2.getLayerWidth(i));
        layerDrawable.setLayerHeight(i, layerDrawable2.getLayerHeight(i));
        layerDrawable.setLayerInsetLeft(i, layerDrawable2.getLayerInsetLeft(i));
        layerDrawable.setLayerInsetRight(i, layerDrawable2.getLayerInsetRight(i));
        layerDrawable.setLayerInsetTop(i, layerDrawable2.getLayerInsetTop(i));
        layerDrawable.setLayerInsetBottom(i, layerDrawable2.getLayerInsetBottom(i));
        layerDrawable.setLayerInsetStart(i, layerDrawable2.getLayerInsetStart(i));
        layerDrawable.setLayerInsetEnd(i, layerDrawable2.getLayerInsetEnd(i));
    }

    public static void setProgress(ProgressBar progressBar) {
        int progress = progressBar.getProgress();
        progressBar.setProgress(0);
        progressBar.setProgress(progress);
    }
}
