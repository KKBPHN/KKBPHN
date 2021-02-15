package com.android.camera.fragment.manually.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.View;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.constant.FastMotionConstant;
import com.android.camera.data.data.config.ComponentManuallyWB;
import com.android.camera.fragment.manually.ManuallyListener;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ExtraCustomWBListAdapter extends AbstractZoomSliderAdapter {
    private static final int FRONT_PART_COUNT = 20;
    private static final int GAP_K_LONG_VALUE = 500;
    private static final int GAP_K_SHOT_VALUE = 200;
    private static final int LATTER_PART_COUNT = 4;
    private static final int MAX_K_VALUE = 8000;
    private static final int MIDDLE_K_VALUE = 6000;
    private static final int MIN_K_VALUE = 2000;
    private static final int TOTAL_COUNT = 25;
    private static int[] WB_VALUES = new int[25];
    private static final String[] mLables = {FastMotionConstant.FAST_MOTION_SPEED_60X, "3600", "5200", "8000"};
    public static List wbValues = ((List) Arrays.stream(WB_VALUES).boxed().collect(Collectors.toList()));
    private int mCurrentIndex = -1;
    private int mCurrentMode;
    private boolean mEnableGradient;
    private ManuallyListener mManuallyListener;
    private ComponentManuallyWB mManuallyWB;

    static {
        for (int i = 0; i < 25; i++) {
            if (i < 20) {
                WB_VALUES[i] = (i * 200) + 2000;
            } else {
                WB_VALUES[i] = ((i - 20) * 500) + 6000;
            }
        }
    }

    public ExtraCustomWBListAdapter(Context context, ComponentManuallyWB componentManuallyWB, int i, boolean z, ManuallyListener manuallyListener) {
        this.mManuallyWB = componentManuallyWB;
        this.mCurrentMode = i;
        this.mEnableGradient = z;
        this.mManuallyListener = manuallyListener;
        initStyle(context);
        this.mLineSelectMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_selected_moving_height)) / 2.0f;
        this.mLineMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_moving_height)) / 2.0f;
        this.mLineHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_height)) / 2.0f;
        this.mLineTextGap = context.getResources().getDimensionPixelSize(R.dimen.manually_item_line_text_gap);
        this.mTextPaint.setTextAlign(Align.CENTER);
    }

    private void changeValue(int i) {
        int intValue = mapPositionToValue((float) i).intValue();
        int customWB = this.mManuallyWB.getCustomWB(this.mCurrentMode);
        if (intValue != customWB) {
            this.mManuallyWB.setCustomWB(this.mCurrentMode, intValue);
            ManuallyListener manuallyListener = this.mManuallyListener;
            if (manuallyListener != null) {
                manuallyListener.onManuallyDataChanged(this.mManuallyWB, String.valueOf(customWB), String.valueOf(intValue), true, this.mCurrentMode);
            }
        }
    }

    private void drawText(float f, int i, Canvas canvas, int i2) {
        float f2;
        float f3;
        int width;
        int width2;
        String str = mLables[mapIndexToTextPosition(i)];
        Rect rect = new Rect();
        this.mTextPaint.getTextBounds(str, 0, str.length(), rect);
        canvas.save();
        if (i2 == 2) {
            canvas.rotate(this.mDegree, f, (((float) (-this.mLineTextGap)) - this.mCurrentLineSelectHalfHeight) - ((float) (rect.height() / 2)));
            float f4 = this.mDegree;
            if (f4 == 90.0f) {
                width2 = ((-rect.width()) / 2) + (rect.height() / 2);
            } else {
                if (f4 == 270.0f) {
                    width2 = (rect.width() / 2) - (rect.height() / 2);
                }
                f2 = (float) (-this.mLineTextGap);
                f3 = this.mCurrentLineSelectHalfHeight;
            }
            canvas.translate((float) width2, 0.0f);
            f2 = (float) (-this.mLineTextGap);
            f3 = this.mCurrentLineSelectHalfHeight;
        } else {
            canvas.rotate(this.mDegree, f, (((float) (-this.mLineTextGap)) - this.mLineSelectHalfHeight) - ((float) (rect.height() / 2)));
            float f5 = this.mDegree;
            if (f5 == 90.0f) {
                width = ((-rect.width()) / 2) + (rect.height() / 2);
            } else {
                if (f5 == 270.0f) {
                    width = (rect.width() / 2) - (rect.height() / 2);
                }
                f2 = (float) (-this.mLineTextGap);
                f3 = this.mLineSelectHalfHeight;
            }
            canvas.translate((float) width, 0.0f);
            f2 = (float) (-this.mLineTextGap);
            f3 = this.mLineSelectHalfHeight;
        }
        canvas.drawText(str, f, (f2 - f3) - ((float) rect.bottom), this.mTextPaint);
        canvas.restore();
    }

    private int mapIndexToTextPosition(int i) {
        if (i == 0) {
            return 0;
        }
        if (i == 8) {
            return 1;
        }
        if (i == 16) {
            return 2;
        }
        return i == 24 ? 3 : -1;
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
        if (isTextPoint(i)) {
            drawText(0.0f, i, canvas, i2);
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
        return null;
    }

    public int getCount() {
        return 25;
    }

    public boolean isEnable() {
        return false;
    }

    public boolean isStopPoint(int i) {
        return i % 8 == 0 || i == 20;
    }

    public boolean isTextPoint(int i) {
        return i % 8 == 0;
    }

    public Integer mapPositionToValue(float f) {
        return (Integer) wbValues.get(Util.clamp(Math.round(f), 0, getCount() - 1));
    }

    public float mapValueToPosition(Integer num) {
        return (float) wbValues.indexOf(num);
    }

    public float measureWidth(int i) {
        return (float) (isStopPoint(i) ? this.mLineStopPointWidth : this.mLineWidth);
    }

    public void onChangeValue(String str) {
    }

    public void onPositionSelect(View view, int i, float f) {
        if (i == -1) {
            if (f != -1.0f) {
                i = Math.round(f * ((float) (getCount() - 1)));
            } else {
                return;
            }
        }
        if (this.mCurrentIndex != i) {
            this.mCurrentIndex = i;
            changeValue(i);
        }
    }

    public void setEnable(boolean z) {
    }
}
