package com.android.camera.fragment.manually.adapter;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import com.android.camera.CameraSettings;
import com.android.camera.Util;
import com.android.camera.fragment.manually.ZoomValueListener;
import java.util.Arrays;
import java.util.List;

public class ExtraSlideFNumberAdapter extends AbstractZoomSliderAdapter {
    private static final int ENTRY_COUNT_TOTAL = F_NUMBERS.length;
    public static final String[] F_NUMBERS = {"1.0", "1.1", "1.2", "1.4", "1.6", "1.8", "2", "2.2", "2.5", "2.8", "3.2", "3.5", "4", "4.5", "5.0", "5.6", "6.3", "7.1", "8", "9", "10", "11", "13", "14", "16"};
    private String mCurrentValue;
    private C0122O00000o mDataItemFeature;
    private boolean mEnable;
    private ZoomValueListener mManuallyListener;
    private List mValidFNumbers = Arrays.asList(F_NUMBERS);

    public ExtraSlideFNumberAdapter(Context context, String str, ZoomValueListener zoomValueListener) {
        this.mManuallyListener = zoomValueListener;
        this.mCurrentValue = str;
        this.mDataItemFeature = C0122O00000o.instance();
        initStyle(context);
    }

    private boolean isFlagPosition(int i) {
        return F_NUMBERS[i].equals(this.mDataItemFeature.O00oOoOo(CameraSettings.isFrontCamera())) || i == 0 || i == F_NUMBERS.length - 1;
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
        if (z) {
            int i3 = this.mLineStopPointWidth;
            f2 = (float) ((-i3) / 2);
            f3 = this.mCurrentLineSelectHalfHeight;
            f4 = -f3;
            f5 = (float) (i3 / 2);
            f6 = 2.0f;
            f7 = 2.0f;
            paint = this.mSelectPaint;
        } else if (isFlagPosition(i)) {
            int i4 = this.mLineStopPointWidth;
            f2 = (float) ((-i4) / 2);
            f3 = this.mCurrentLineHalfHeight;
            f4 = -f3;
            f5 = (float) (i4 / 2);
            f6 = 1.0f;
            f7 = 1.0f;
            paint = this.mStopPointPaint;
        } else {
            int i5 = this.mLineWidth;
            f2 = (float) ((-i5) / 2);
            f3 = this.mCurrentLineHalfHeight;
            f4 = -f3;
            f5 = (float) (i5 / 2);
            f6 = 1.0f;
            f7 = 1.0f;
            paint = this.mNormalPaint;
        }
        canvas.drawRoundRect(f2, f4, f5, f3, f6, f7, paint);
    }

    public Align getAlign(int i) {
        return Align.CENTER;
    }

    public int getCount() {
        return ENTRY_COUNT_TOTAL;
    }

    public boolean isEnable() {
        return false;
    }

    public String mapPositionToValue(float f) {
        return F_NUMBERS[Util.clamp(Math.round(f), 0, getCount() - 1)];
    }

    public float mapValueToPosition(String str) {
        return (float) this.mValidFNumbers.indexOf(str);
    }

    public float measureWidth(int i) {
        return (float) (isFlagPosition(i) ? this.mLineStopPointWidth : this.mLineWidth);
    }

    public void onChangeValue(String str) {
    }

    public void onPositionSelect(View view, int i, float f) {
        if (this.mEnable) {
            if (i == -1) {
                if (f != -1.0f) {
                    i = Math.round(f * ((float) (getCount() - 1)));
                } else {
                    return;
                }
            }
            String mapPositionToValue = mapPositionToValue((float) i);
            if (!mapPositionToValue.equals(this.mCurrentValue)) {
                ZoomValueListener zoomValueListener = this.mManuallyListener;
                if (zoomValueListener != null) {
                    zoomValueListener.onManuallyDataChanged(mapPositionToValue);
                    this.mManuallyListener.onZoomItemSlideOn(i, false);
                }
                this.mCurrentValue = mapPositionToValue;
            }
        }
    }

    public void setEnable(boolean z) {
        this.mEnable = z;
    }
}
