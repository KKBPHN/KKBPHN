package com.android.camera.fragment.manually.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.data.ComponentData;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.statistic.CameraStatUtils;

public class ExtraFocusAdapter extends AbstractZoomSliderAdapter {
    private static final int MAX_POSITION = 1000;
    private static final int[] mImageIds = {R.drawable.ic_focusmode_flag_near_normal, R.drawable.ic_focusmode_flag_far_normal};
    private ComponentData mComponentData;
    private Context mContext;
    private int mCurrentMode;
    private ManuallyListener mManuallyListener;
    private int mTrackedFocusPosition;

    public ExtraFocusAdapter(Context context, ComponentData componentData, int i, ManuallyListener manuallyListener) {
        this.mContext = context;
        this.mComponentData = componentData;
        this.mCurrentMode = i;
        this.mManuallyListener = manuallyListener;
        this.mCurrentValue = componentData.getComponentValue(this.mCurrentMode);
        try {
            this.mTrackedFocusPosition = Integer.parseInt(this.mCurrentValue);
        } catch (NumberFormatException unused) {
            this.mTrackedFocusPosition = 1000;
        }
        initStyle(context);
    }

    private void drawImage(float f, int i, Canvas canvas, int i2) {
        float f2;
        float f3;
        float f4;
        int mapIndexToImagePosition = mapIndexToImagePosition(i);
        if (mapIndexToImagePosition != -1) {
            Drawable drawable = this.mContext.getResources().getDrawable(mImageIds[mapIndexToImagePosition]);
            if (drawable != null) {
                canvas.save();
                if (i2 == 2) {
                    drawable.setBounds(Math.round(f - ((float) (drawable.getIntrinsicWidth() / 2))), Math.round((((float) (-this.mLineTextGap)) - this.mCurrentLineSelectHalfHeight) - ((float) drawable.getIntrinsicHeight())), Math.round(((float) (drawable.getIntrinsicWidth() / 2)) + f), Math.round(((float) (-this.mLineTextGap)) - this.mCurrentLineSelectHalfHeight));
                    f2 = this.mDegree;
                    f4 = (float) (-this.mLineTextGap);
                    f3 = this.mCurrentLineSelectHalfHeight;
                } else {
                    drawable.setBounds(Math.round(f - ((float) (drawable.getIntrinsicWidth() / 2))), Math.round((((float) (-this.mLineTextGap)) - this.mLineSelectHalfHeight) - ((float) drawable.getIntrinsicHeight())), Math.round(((float) (drawable.getIntrinsicWidth() / 2)) + f), Math.round(((float) (-this.mLineTextGap)) - this.mLineSelectHalfHeight));
                    f2 = this.mDegree;
                    f4 = (float) (-this.mLineTextGap);
                    f3 = this.mLineSelectHalfHeight;
                }
                canvas.rotate(f2, f, (float) Math.round((f4 - f3) - ((float) (drawable.getIntrinsicHeight() / 2))));
                drawable.draw(canvas);
                canvas.restore();
            }
        }
    }

    private int mapIndexToImagePosition(int i) {
        if (i == 0) {
            return 0;
        }
        return i == getCount() - 1 ? 1 : -1;
    }

    public void draw(int i, Canvas canvas, boolean z, int i2, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        float f7;
        Paint paint;
        super.draw(i, canvas, z, i2, f);
        if (isImagePoint(i)) {
            drawImage(0.0f, i, canvas, i2);
            f2 = (float) ((-this.mLineStopPointWidth) / 2);
        } else {
            f2 = (-measureWidth(i)) / 2.0f;
        }
        float f8 = f2;
        if (z) {
            f3 = this.mCurrentLineSelectHalfHeight;
            f4 = -f3;
            f5 = f8 + ((float) this.mLineStopPointWidth);
            f6 = 2.0f;
            f7 = 2.0f;
            paint = this.mSelectPaint;
        } else {
            boolean isStopPoint = isStopPoint(i);
            f3 = this.mCurrentLineHalfHeight;
            f4 = -f3;
            if (isStopPoint) {
                f5 = f8 + ((float) this.mLineStopPointWidth);
                f6 = 1.0f;
                f7 = 1.0f;
                paint = this.mStopPointPaint;
            } else {
                f5 = f8 + ((float) this.mLineWidth);
                f6 = 1.0f;
                f7 = 1.0f;
                paint = this.mNormalPaint;
            }
        }
        canvas.drawRoundRect(f8, f4, f5, f3, f6, f7, paint);
    }

    public Align getAlign(int i) {
        return Align.LEFT;
    }

    public int getCount() {
        return 40;
    }

    /* access modifiers changed from: protected */
    public void initStyle(Context context) {
        super.initStyle(context);
        this.mLineSelectMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_selected_moving_height)) / 2.0f;
        this.mLineMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_moving_height)) / 2.0f;
        this.mLineHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_height)) / 2.0f;
        this.mLineTextGap = context.getResources().getDimensionPixelSize(R.dimen.manually_item_line_text_gap);
    }

    public boolean isEnable() {
        return false;
    }

    public boolean isImagePoint(int i) {
        return i == 0 || i == getCount() - 1;
    }

    public boolean isSingleValueLine(int i) {
        return false;
    }

    public boolean isStopPoint(int i) {
        return i == 0 || i == getCount() - 1 || i == 15 || i == 31;
    }

    public Integer mapPositionToValue(float f) {
        return Integer.valueOf(Util.clamp((Math.round(1000.0f - (f * 1000.0f)) / 10) * 10, 0, 990));
    }

    public float mapValueToPosition(Integer num) {
        return (float) Math.round(((float) Integer.valueOf(1000 - num.intValue()).intValue()) / (1000.0f / ((float) (getCount() - 1))));
    }

    public float measureWidth(int i) {
        return (float) (isStopPoint(i) ? this.mLineStopPointWidth : this.mLineWidth);
    }

    public void onChangeValue(String str) {
    }

    public void onPositionSelect(View view, int i, float f) {
        int intValue = mapPositionToValue(f == -1.0f ? ((float) i) / ((float) (getCount() - 1)) : Util.clamp(f, 0.0f, 1.0f)).intValue();
        if (this.mTrackedFocusPosition != intValue) {
            this.mTrackedFocusPosition = intValue;
            CameraStatUtils.trackFocusPositionChanged(intValue, this.mCurrentMode);
        }
        String valueOf = String.valueOf(intValue);
        if (!valueOf.equals(this.mCurrentValue)) {
            this.mComponentData.setComponentValue(this.mCurrentMode, valueOf);
            ManuallyListener manuallyListener = this.mManuallyListener;
            if (manuallyListener != null) {
                manuallyListener.onManuallyDataChanged(this.mComponentData, this.mCurrentValue, valueOf, false, this.mCurrentMode);
            }
            this.mCurrentValue = valueOf;
        }
    }

    public void setEnable(boolean z) {
    }
}
