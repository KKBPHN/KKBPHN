package com.android.camera.fragment.manually.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.data.ComponentData;
import com.android.camera.data.data.ComponentDataItem;
import com.android.camera.fragment.manually.ManuallyListener;
import com.android.camera.log.Log;
import java.util.ArrayList;
import java.util.List;

public class ExtraHorizontalListAdapter extends AbstractZoomSliderAdapter {
    private static final String TAG = "ExtraHorizontalListAdapter";
    private static final int[] sFastmotionDurationInfinityImages = {R.drawable.ic_fastmotion_duration_infinity_back, R.drawable.ic_fastmotion_duration_infinity_front};
    private ComponentData mComponentData;
    private Context mContext;
    private int mCurrentMode;
    private List mItems = new ArrayList(this.mComponentData.getItems());
    private ManuallyListener mManuallyListener;

    public ExtraHorizontalListAdapter(Context context, ComponentData componentData, int i, ManuallyListener manuallyListener) {
        this.mContext = context;
        this.mComponentData = componentData;
        this.mCurrentMode = i;
        this.mManuallyListener = manuallyListener;
        List list = this.mItems;
        boolean z = false;
        if (list != null && !list.isEmpty() && (this.mComponentData.getDisplayTitleString() == R.string.pref_camera_iso_title_abbr || this.mComponentData.getDisplayTitleString() == R.string.pref_manual_exposure_title_abbr)) {
            this.mItems.remove(0);
        }
        if (this.mItems.size() < 7) {
            z = true;
        }
        this.mNeedIllegalLine = z;
        initStyle(context);
        this.mCurrentValue = componentData.getComponentValue(this.mCurrentMode);
    }

    private void changeValue(int i) {
        List list = this.mItems;
        if (list == null || list.isEmpty()) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Error change value, items is ");
            sb.append(list);
            Log.d(str, sb.toString());
            return;
        }
        String str2 = ((ComponentDataItem) list.get(Util.clamp(i, 0, list.size() - 1))).mValue;
        if (str2 != null && !str2.equals(this.mCurrentValue)) {
            this.mComponentData.setComponentValue(this.mCurrentMode, str2);
            ManuallyListener manuallyListener = this.mManuallyListener;
            if (manuallyListener != null) {
                manuallyListener.onManuallyDataChanged(this.mComponentData, this.mCurrentValue, str2, false, this.mCurrentMode);
            }
            this.mCurrentValue = str2;
        }
    }

    private void drawIllegalLine(int i, Canvas canvas, float f) {
        if (needDrawIllegalLine(i)) {
            float f2 = (this.mIsRSL ? -this.mItemGap : this.mItemGap) / 3.0f;
            float f3 = f + f2;
            int i2 = this.mLineWidth;
            float f4 = f3 - ((float) (i2 / 2));
            float f5 = this.mCurrentLineIllegalHalfHeight;
            canvas.drawRoundRect(f4, -f5, f3 + ((float) (i2 / 2)), f5, 1.0f, 1.0f, this.mIllegalLinePaint);
            float f6 = f3 + f2;
            int i3 = this.mLineWidth;
            float f7 = f6 - ((float) (i3 / 2));
            float f8 = this.mCurrentLineIllegalHalfHeight;
            canvas.drawRoundRect(f7, -f8, f6 + ((float) (i3 / 2)), f8, 1.0f, 1.0f, this.mIllegalLinePaint);
        }
    }

    private void drawImage(float f, int i, boolean z, Canvas canvas, int i2) {
        float f2;
        float f3;
        float f4;
        if (i == 0) {
            for (int drawable : sFastmotionDurationInfinityImages) {
                Drawable drawable2 = this.mContext.getResources().getDrawable(drawable);
                canvas.save();
                int round = Math.round(f - ((float) (drawable2.getIntrinsicWidth() / 2)));
                float f5 = (float) (-this.mLineTextGap);
                if (i2 == 2) {
                    drawable2.setBounds(round, Math.round(((f5 - this.mCurrentLineSelectHalfHeight) - ((float) drawable2.getIntrinsicHeight())) + ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_duration_infinity_icon_gap))), Math.round(((float) (drawable2.getIntrinsicWidth() / 2)) + f), Math.round((((float) (-this.mLineTextGap)) - this.mCurrentLineSelectHalfHeight) + ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_duration_infinity_icon_gap))));
                    f4 = this.mDegree;
                    f3 = (float) (-this.mLineTextGap);
                    f2 = this.mCurrentLineSelectHalfHeight;
                } else {
                    drawable2.setBounds(round, Math.round(((f5 - this.mLineSelectHalfHeight) - ((float) drawable2.getIntrinsicHeight())) + ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_duration_infinity_icon_gap))), Math.round(((float) (drawable2.getIntrinsicWidth() / 2)) + f), Math.round((((float) (-this.mLineTextGap)) - this.mLineSelectHalfHeight) + ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_duration_infinity_icon_gap))));
                    f4 = this.mDegree;
                    f3 = (float) (-this.mLineTextGap);
                    f2 = this.mLineSelectHalfHeight;
                }
                canvas.rotate(f4, f, (float) Math.round(((f3 - f2) - ((float) (drawable2.getIntrinsicHeight() / 2))) + ((float) this.mContext.getResources().getDimensionPixelSize(R.dimen.fastmotion_duration_infinity_icon_gap))));
                drawable2.draw(canvas);
                canvas.restore();
            }
        }
    }

    private void drawText(float f, int i, Canvas canvas, int i2) {
        float f2;
        float f3;
        int width;
        int width2;
        List list = this.mItems;
        if (list != null && list.size() > 0) {
            ComponentDataItem componentDataItem = (ComponentDataItem) this.mItems.get(i);
            String string = TextUtils.isEmpty(componentDataItem.mDisplayNameStr) ? this.mContext.getResources().getString(componentDataItem.mDisplayNameRes) : componentDataItem.mDisplayNameStr;
            Rect rect = new Rect();
            this.mTextPaint.getTextBounds(string, 0, string.length(), rect);
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
            canvas.drawText(string, f, (f2 - f3) - ((float) rect.bottom), this.mTextPaint);
            canvas.restore();
        }
    }

    private boolean isStopPoint(int i) {
        boolean z;
        boolean z2;
        int displayTitleString = this.mComponentData.getDisplayTitleString();
        boolean z3 = true;
        if (i == 0 || i == getCount() - 1) {
            return true;
        }
        switch (displayTitleString) {
            case R.string.pref_camera_fastmotion_duration /*2131756283*/:
            case R.string.pref_camera_fastmotion_speed /*2131756286*/:
                if (i % 3 != 0) {
                    z = false;
                }
                return z;
            case R.string.pref_camera_iso_title_abbr /*2131756363*/:
            case R.string.pref_manual_exposure_title_abbr /*2131756598*/:
                float count = (float) (getCount() / 3);
                if (count < 3.0f) {
                    count = (float) (getCount() / 2);
                }
                if (count >= 3.0f) {
                    if (i % Math.round(count) != 0 || (getCount() - 1) - i < 3) {
                        z2 = false;
                    }
                    return z2;
                }
                break;
            case R.string.pref_camera_manually_exposure_value_abbr /*2131756400*/:
                int count2 = getCount();
                int count3 = getCount();
                if (count2 > 14) {
                    if (i % Math.round((float) (count3 / 4)) != 0) {
                        z3 = false;
                    }
                    return z3;
                }
                if (i % Math.round((float) (count3 / 2)) != 0) {
                    z3 = false;
                }
                return z3;
        }
        return false;
    }

    private boolean needDrawIllegalLine(int i) {
        return this.mNeedIllegalLine && i < getCount() - 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0017, code lost:
        if (r15.getDisplayTitleString() == com.android.camera.R.string.pref_camera_fastmotion_duration) goto L_0x001c;
     */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0070  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
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
            if (i == 0) {
                ComponentData componentData = this.mComponentData;
                if (componentData != null) {
                }
            }
            drawText(0.0f, i, canvas, i2);
        }
        ComponentData componentData2 = this.mComponentData;
        if (componentData2 != null && componentData2.getDisplayTitleString() == R.string.pref_camera_fastmotion_duration) {
            drawImage(0.0f, i, z, canvas, i2);
        }
        canvas.save();
        if (isStopPoint(i)) {
            int i3 = this.mLineStopPointWidth;
            f2 = 0.0f - ((float) (i3 / 2));
            f3 = this.mCurrentLineHalfHeight;
            f4 = -f3;
            f5 = 0.0f + ((float) (i3 / 2));
            f6 = 1.0f;
            f7 = 1.0f;
            paint = this.mStopPointPaint;
        } else {
            if (i != -1) {
                int i4 = this.mLineWidth;
                f2 = 0.0f - ((float) (i4 / 2));
                f3 = this.mCurrentLineHalfHeight;
                f4 = -f3;
                f5 = 0.0f + ((float) (i4 / 2));
                f6 = 1.0f;
                f7 = 1.0f;
                paint = this.mNormalPaint;
            }
            if (z) {
                int i5 = this.mLineStopPointWidth;
                float f8 = (float) ((-i5) / 2);
                float f9 = this.mCurrentLineSelectHalfHeight;
                canvas.drawRoundRect(f8, -f9, f8 + ((float) i5), f9, 2.0f, 2.0f, this.mSelectPaint);
            }
            canvas.restore();
        }
        canvas.drawRoundRect(f2, f4, f5, f3, f6, f7, paint);
        drawIllegalLine(i, canvas, 0.0f);
        if (z) {
        }
        canvas.restore();
    }

    public Align getAlign(int i) {
        return null;
    }

    public int getCount() {
        return this.mItems.size();
    }

    /* access modifiers changed from: protected */
    public void initStyle(Context context) {
        super.initStyle(context);
        this.mLineTextGap = context.getResources().getDimensionPixelSize(R.dimen.manually_item_line_text_gap);
        this.mTextPaint.setTextAlign(Align.CENTER);
        this.mLineSelectMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_selected_moving_height)) / 2.0f;
        this.mLineMovingHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_moving_height)) / 2.0f;
        this.mLineHalfHeight = ((float) context.getResources().getDimensionPixelSize(R.dimen.manually_line_height)) / 2.0f;
        this.mSelectPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));
    }

    public boolean isEnable() {
        return false;
    }

    public boolean isTextPoint(int i) {
        if (!this.mNeedIllegalLine) {
            return isStopPoint(i);
        }
        boolean z = true;
        if (i == 0 || i == getCount() - 1) {
            return true;
        }
        float count = (float) (getCount() / 3);
        if (count < 2.0f) {
            return false;
        }
        if (i % Math.round(count) != 0) {
            z = false;
        }
        return z;
    }

    public String mapPositionToValue(float f) {
        return ((ComponentDataItem) this.mItems.get(Util.clamp(Math.round(f), 0, getCount() - 1))).mValue;
    }

    public float mapValueToPosition(String str) {
        int count = getCount();
        for (int i = 0; i < count; i++) {
            if (str.equals(((ComponentDataItem) this.mItems.get(i)).mValue)) {
                return (float) i;
            }
        }
        return -1.0f;
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
        changeValue(i);
    }

    public void setEnable(boolean z) {
    }
}
