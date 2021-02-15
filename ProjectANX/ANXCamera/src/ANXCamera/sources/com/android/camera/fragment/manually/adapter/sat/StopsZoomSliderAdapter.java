package com.android.camera.fragment.manually.adapter.sat;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.view.View;
import com.android.camera.ActivityBase;
import com.android.camera.CameraSettings;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.Util;
import com.android.camera.fragment.manually.ZoomValueListener;
import com.android.camera.fragment.manually.adapter.AbstractZoomSliderAdapter;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import com.android.camera.module.ModuleManager;
import java.util.ArrayList;
import java.util.List;

public class StopsZoomSliderAdapter extends AbstractZoomSliderAdapter {
    private static final String TAG = "StopsZoomSliderAdapter";
    private int mCurrentIndex = -1;
    protected final int mCurrentMode;
    private boolean mEnable;
    private List mRulerLines = new ArrayList();
    private List mZoomIndexs = new ArrayList();
    protected final float mZoomRatioMax;
    protected final float mZoomRatioMin;

    public StopsZoomSliderAdapter(Context context, boolean z, int i, ZoomValueListener zoomValueListener) {
        this.mCurrentMode = i;
        this.mZoomValueListener = zoomValueListener;
        this.mCurrentValue = String.valueOf(CameraSettings.getRetainZoom(this.mCurrentMode));
        BaseModule baseModule = (BaseModule) ((ActivityBase) context).getCurrentModule();
        this.mZoomRatioMin = z ? 1.0f : baseModule.getMinZoomRatio();
        this.mZoomRatioMax = baseModule.getMaxZoomRatio();
        StringBuilder sb = new StringBuilder();
        sb.append("ZOOM RATIO RANGE [");
        sb.append(this.mZoomRatioMin);
        sb.append(", ");
        sb.append(this.mZoomRatioMax);
        sb.append("]");
        Log.d(TAG, sb.toString());
        initStyle(context);
        int i2 = 0;
        boolean z2 = i == 188;
        boolean isVideoCategory = ModuleManager.isVideoCategory(i);
        List O000000o2 = C0122O00000o.instance().O000000o(z2, isVideoCategory, HybridZoomingSystem.ZOOM_INDEXS_DEFAULT);
        List O00000Oo2 = C0122O00000o.instance().O00000Oo(z2, isVideoCategory, HybridZoomingSystem.ZOOM_RULER_DEFAULT);
        while (i2 < O000000o2.size()) {
            float floatValue = ((Float) O000000o2.get(i2)).floatValue();
            if (floatValue >= this.mZoomRatioMin && floatValue <= this.mZoomRatioMax) {
                this.mZoomIndexs.add(Float.valueOf(floatValue));
                if (i2 >= 1 && i2 <= O00000Oo2.size()) {
                    int i3 = i2 - 1;
                    if (this.mZoomIndexs.contains(O000000o2.get(i3))) {
                        this.mRulerLines.add((Integer) O00000Oo2.get(i3));
                    }
                }
            }
            i2++;
        }
        if (this.mZoomIndexs.size() != this.mRulerLines.size() + 1) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("init zoom fail mZoomIndexs = ");
            sb2.append(this.mZoomIndexs);
            sb2.append(",mRulerLines = ");
            sb2.append(this.mRulerLines);
            throw new IllegalStateException(sb2.toString());
        } else if (!this.mZoomIndexs.contains(Float.valueOf(this.mZoomRatioMax))) {
            this.mZoomIndexs.add(Float.valueOf(this.mZoomRatioMax));
            this.mRulerLines.add(Integer.valueOf(10));
        }
    }

    public void draw(int i, Canvas canvas, boolean z, int i2, float f) {
        float f2;
        float f3;
        float f4;
        float f5;
        float f6;
        Paint paint;
        super.draw(i, canvas, z, i2, f);
        float f7 = (-measureWidth(i)) / 2.0f;
        if (z) {
            f2 = this.mCurrentLineSelectHalfHeight;
            f3 = -f2;
            f4 = f7 + ((float) this.mLineStopPointWidth);
            f5 = 2.0f;
            f6 = 2.0f;
            paint = this.mSelectPaint;
        } else {
            boolean isStopPoint = isStopPoint(i);
            f2 = this.mCurrentLineHalfHeight;
            f3 = -f2;
            if (isStopPoint) {
                f4 = f7 + ((float) this.mLineStopPointWidth);
                f5 = 1.0f;
                f6 = 1.0f;
                paint = this.mStopPointPaint;
            } else {
                f4 = f7 + ((float) this.mLineWidth);
                f5 = 1.0f;
                f6 = 1.0f;
                paint = this.mNormalPaint;
            }
        }
        canvas.drawRoundRect(f7, f3, f4, f2, f5, f6, paint);
    }

    public Align getAlign(int i) {
        return null;
    }

    public int getCount() {
        int i = 1;
        for (Integer intValue : this.mRulerLines) {
            i += intValue.intValue();
        }
        return i;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public boolean isFirstStopPoint(int i) {
        return i == 0;
    }

    public boolean isLastStopPoint(int i) {
        return i == getCount() - 1;
    }

    public boolean isSingleValueLine(int i) {
        int i2 = 0;
        int i3 = 0;
        while (i2 < this.mZoomIndexs.size() - 1) {
            i3 += ((Integer) this.mRulerLines.get(i2)).intValue();
            if (i <= i3 && ((float) ((int) (((((Float) this.mZoomIndexs.get(i2 + 1)).floatValue() * 10.0f) - (((Float) this.mZoomIndexs.get(i2)).floatValue() * 10.0f)) / ((float) ((Integer) this.mRulerLines.get(i2)).intValue())))) == 1.0f) {
                return true;
            }
            i2++;
        }
        return false;
    }

    public boolean isStopPoint(int i) {
        if (isFirstStopPoint(i) || isLastStopPoint(i)) {
            return true;
        }
        int i2 = 0;
        for (Integer intValue : this.mRulerLines) {
            i2 += intValue.intValue();
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public String mapPositionToValue(float f) {
        int i = 0;
        float f2 = this.mZoomRatioMin;
        int i2 = 0;
        while (true) {
            if (i >= this.mZoomIndexs.size() - 1) {
                break;
            }
            i2 += ((Integer) this.mRulerLines.get(i)).intValue();
            float f3 = (float) i2;
            if (f <= f3) {
                f2 += ((f - f3) + ((float) ((Integer) this.mRulerLines.get(i)).intValue())) * ((((Float) this.mZoomIndexs.get(i + 1)).floatValue() - ((Float) this.mZoomIndexs.get(i)).floatValue()) / ((float) ((Integer) this.mRulerLines.get(i)).intValue()));
                break;
            }
            i++;
            f2 = ((Float) this.mZoomIndexs.get(i)).floatValue();
        }
        return String.valueOf(f2);
    }

    public float mapValueToPosition(String str) {
        float parseFloat = Float.parseFloat(str);
        int i = 0;
        int i2 = 0;
        while (true) {
            if (i >= this.mZoomIndexs.size() - 1) {
                break;
            }
            if (parseFloat >= ((Float) this.mZoomIndexs.get(i)).floatValue()) {
                int i3 = i + 1;
                if (parseFloat < ((Float) this.mZoomIndexs.get(i3)).floatValue()) {
                    i2 = (int) (((float) i2) + (((parseFloat - ((Float) this.mZoomIndexs.get(i)).floatValue()) * ((float) ((Integer) this.mRulerLines.get(i)).intValue())) / (((Float) this.mZoomIndexs.get(i3)).floatValue() - ((Float) this.mZoomIndexs.get(i)).floatValue())));
                    break;
                }
            }
            i2 += ((Integer) this.mRulerLines.get(i)).intValue();
            i++;
        }
        return (float) i2;
    }

    public float measureWidth(int i) {
        return (float) (isStopPoint(i) ? this.mLineStopPointWidth : this.mLineWidth);
    }

    public void onChangeValue(String str) {
        if (!str.equals(this.mCurrentValue)) {
            ZoomValueListener zoomValueListener = this.mZoomValueListener;
            if (zoomValueListener != null) {
                zoomValueListener.onManuallyDataChanged(str);
                this.mCurrentValue = str;
                this.mCurrentIndex = Math.round(mapValueToPosition(this.mCurrentValue));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0044, code lost:
        if (java.lang.Math.abs(((float) r5.mCurrentIndex) - r6) > 0.95f) goto L_0x004d;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x004b, code lost:
        if (r5.mCurrentIndex != r7) goto L_0x004d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onPositionSelect(View view, int i, float f) {
        float f2;
        if (this.mEnable) {
            float f3 = -1.0f;
            boolean z = true;
            if (f != -1.0f) {
                f = Util.clamp(f, 0.0f, 1.0f);
                f2 = ((float) (getCount() - 1)) * f;
            } else if (i != -1) {
                f2 = (float) i;
            } else {
                return;
            }
            String mapPositionToValue = mapPositionToValue(f2);
            if (!mapPositionToValue.equals(this.mCurrentValue)) {
                if (i == -1) {
                    f3 = ((float) (getCount() - 1)) * f;
                }
                z = false;
                if (this.mZoomValueListener != null && z) {
                    this.mCurrentIndex = i == -1 ? Math.round(f3) : i;
                    this.mZoomValueListener.onZoomItemSlideOn(i, isStopPoint(i));
                }
                ZoomValueListener zoomValueListener = this.mZoomValueListener;
                if (zoomValueListener != null) {
                    zoomValueListener.onManuallyDataChanged(mapPositionToValue);
                }
                this.mCurrentValue = mapPositionToValue;
            }
        }
    }

    public void setCurrentValue(String str) {
        this.mCurrentValue = String.valueOf(CameraSettings.getRetainZoom(this.mCurrentMode));
        this.mCurrentIndex = Math.round(mapValueToPosition(this.mCurrentValue));
    }

    public void setEnable(boolean z) {
        this.mEnable = z;
    }
}
