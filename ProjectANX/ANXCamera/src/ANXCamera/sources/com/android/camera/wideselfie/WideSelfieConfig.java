package com.android.camera.wideselfie;

import android.content.Context;
import android.content.res.Resources;
import com.android.camera.R;
import com.android.camera.log.Log;
import java.util.concurrent.atomic.AtomicReference;

public class WideSelfieConfig {
    private static final AtomicReference INSTANCE = new AtomicReference();
    public static final String TAG = "WideSelfieConstants";
    public static final int THUMB_BORDER = 1;
    private static final float UI_RATE = 1.3333334f;
    private int mStillPreviewHeight;
    private int mStillPreviewWidth;
    private int mThumbBgHeight;
    private int mThumbBgHeightVertical;
    private int mThumbBgTopMargin;
    private int mThumbBgTopMarginVertical;
    private int mThumbBgWidth;
    private int mThumbBgWidthVertical;
    private int mThumbViewHeight;
    private int mThumbViewHeightVertical;
    private int mThumbViewTopMargin;
    private int mThumbViewTopMarginVertical;
    private int mThumbViewWidth;
    private int mThumbViewWidthVertical;

    private WideSelfieConfig(Context context) {
        init(context);
    }

    public static WideSelfieConfig getInstance(Context context) {
        WideSelfieConfig wideSelfieConfig;
        do {
            WideSelfieConfig wideSelfieConfig2 = (WideSelfieConfig) INSTANCE.get();
            if (wideSelfieConfig2 != null) {
                return wideSelfieConfig2;
            }
            wideSelfieConfig = new WideSelfieConfig(context);
        } while (!INSTANCE.compareAndSet(null, wideSelfieConfig));
        return wideSelfieConfig;
    }

    public int getStillPreviewHeight() {
        return this.mStillPreviewHeight;
    }

    public int getStillPreviewWidth() {
        return this.mStillPreviewWidth;
    }

    public int getThumbBgHeight() {
        return this.mThumbBgHeight;
    }

    public int getThumbBgHeightVertical() {
        return this.mThumbBgHeightVertical;
    }

    public int getThumbBgTopMargin() {
        return this.mThumbBgTopMargin;
    }

    public int getThumbBgTopMarginVertical() {
        return this.mThumbBgTopMarginVertical;
    }

    public int getThumbBgWidth() {
        return this.mThumbBgWidth;
    }

    public int getThumbBgWidthVertical() {
        return this.mThumbBgWidthVertical;
    }

    public int getThumbViewHeight() {
        return this.mThumbViewHeight;
    }

    public int getThumbViewHeightVertical() {
        return this.mThumbViewHeightVertical;
    }

    public int getThumbViewTopMargin() {
        return this.mThumbViewTopMargin;
    }

    public int getThumbViewTopMarginVertical() {
        return this.mThumbViewTopMarginVertical;
    }

    public int getThumbViewWidth() {
        return this.mThumbViewWidth;
    }

    public int getThumbViewWidthVertical() {
        return this.mThumbViewWidthVertical;
    }

    public void init(Context context) {
        Resources resources = context.getResources();
        this.mStillPreviewWidth = resources.getDimensionPixelOffset(R.dimen.wide_selfie_still_preview_width);
        this.mStillPreviewHeight = (int) (((float) this.mStillPreviewWidth) * UI_RATE);
        StringBuilder sb = new StringBuilder();
        sb.append("mStillPreviewWidth ");
        sb.append(this.mStillPreviewWidth);
        sb.append(", mStillPreviewHeight = ");
        sb.append(this.mStillPreviewHeight);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        this.mThumbBgWidth = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_width) + 2;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("mThumbBgWidth ");
        sb3.append(this.mThumbBgWidth);
        sb3.append(", mStillPreviewWidth = ");
        sb3.append(this.mStillPreviewWidth);
        Log.d(str, sb3.toString());
        this.mThumbBgHeight = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_height) + 2;
        this.mThumbBgTopMargin = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_top_margin) - 1;
        this.mThumbBgWidthVertical = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_width_vertical) + 2;
        this.mThumbBgHeightVertical = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_height_vertical) + 2;
        this.mThumbBgTopMarginVertical = resources.getDimensionPixelSize(R.dimen.wide_selfie_progress_thumbnail_background_top_margin_vertical) - 1;
        int i = this.mThumbBgWidth;
        int i2 = this.mStillPreviewWidth;
        this.mThumbViewWidth = i + i2;
        this.mThumbViewHeight = this.mThumbBgHeight + i2;
        this.mThumbViewTopMargin = (this.mThumbBgTopMargin + 1) - (i2 / 2);
        int i3 = this.mThumbBgWidthVertical;
        int i4 = this.mStillPreviewHeight;
        this.mThumbViewWidthVertical = i3 + i4;
        this.mThumbViewHeightVertical = this.mThumbBgHeightVertical + i4;
        this.mThumbViewTopMarginVertical = (this.mThumbBgTopMarginVertical + 1) - (i4 / 2);
    }
}
