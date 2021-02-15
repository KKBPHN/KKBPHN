package com.android.camera.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;

public class FastmotionIndicatorView extends View {
    private static final float HEIGHT_COMPENSATION = 1.1f;
    private static final float WIDTH_COMPENSATION = 1.1f;
    private String mDuration;
    protected TextPaint mDurationTextPaint;
    protected int mDurationTextSize;
    private boolean mNeedExtendSpeedWidth;
    private boolean mNeedExtendTotalWidth;
    private String mNumber;
    protected Paint mPaint;
    private String mSpeed;
    protected TextPaint mSpeedTextPaint;
    protected int mSpeedTextSize;
    private boolean mTextBold;
    protected int mTextColor;
    protected int mlineColor;
    protected int mlineGap;
    protected int mlineHeight;
    protected int mlineWidth;

    public FastmotionIndicatorView(Context context) {
        super(context);
        String str = "";
        this.mSpeed = str;
        this.mNumber = str;
        this.mDuration = str;
        init(context, null);
    }

    public FastmotionIndicatorView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        String str = "";
        this.mSpeed = str;
        this.mNumber = str;
        this.mDuration = str;
        init(context, attributeSet);
    }

    public FastmotionIndicatorView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        String str = "";
        this.mSpeed = str;
        this.mNumber = str;
        this.mDuration = str;
        init(context, attributeSet);
    }

    public FastmotionIndicatorView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        String str = "";
        this.mSpeed = str;
        this.mNumber = str;
        this.mDuration = str;
        init(context, attributeSet);
    }

    public void init(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.FastmotionIndicatorView);
            this.mSpeedTextSize = obtainStyledAttributes.getDimensionPixelSize(5, 0);
            this.mDurationTextSize = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            this.mTextBold = obtainStyledAttributes.getBoolean(4, true);
            this.mlineGap = obtainStyledAttributes.getDimensionPixelSize(1, 0);
            this.mlineHeight = obtainStyledAttributes.getDimensionPixelSize(2, 0);
            this.mlineWidth = obtainStyledAttributes.getDimensionPixelSize(3, 0);
            obtainStyledAttributes.recycle();
        }
        this.mTextColor = context.getResources().getColor(R.color.zoom_popup_color_new_default);
        this.mlineColor = context.getResources().getColor(R.color.mode_edit_sub_title_color);
        this.mSpeedTextPaint = new TextPaint(1);
        this.mSpeedTextPaint.setAntiAlias(true);
        this.mSpeedTextPaint.setTextSize((float) this.mSpeedTextSize);
        this.mSpeedTextPaint.setColor(this.mTextColor);
        this.mSpeedTextPaint.setTypeface(Util.getMiuiTypeface());
        this.mSpeedTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        if (this.mTextBold) {
            this.mSpeedTextPaint.setTypeface(Typeface.defaultFromStyle(1));
        }
        this.mDurationTextPaint = new TextPaint(1);
        this.mDurationTextPaint.setAntiAlias(true);
        this.mDurationTextPaint.setTextSize((float) this.mDurationTextSize);
        this.mDurationTextPaint.setColor(this.mTextColor);
        this.mDurationTextPaint.setTypeface(Util.getMiuiTypeface());
        this.mDurationTextPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        if (this.mTextBold) {
            this.mDurationTextPaint.setTypeface(Typeface.defaultFromStyle(1));
        }
        this.mDurationTextPaint.setFontFeatureSettings("tnum");
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setColor(this.mlineColor);
        this.mPaint.setStyle(Style.FILL);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        String str;
        int width;
        if (!TextUtils.isEmpty(this.mSpeed) && !TextUtils.isEmpty(this.mDuration)) {
            Rect rect = new Rect();
            TextPaint textPaint = this.mSpeedTextPaint;
            String str2 = this.mSpeed;
            textPaint.getTextBounds(str2, 0, str2.length(), rect);
            Rect rect2 = new Rect();
            TextPaint textPaint2 = this.mDurationTextPaint;
            String str3 = this.mDuration;
            textPaint2.getTextBounds(str3, 0, str3.length(), rect2);
            int round = Math.round(((float) Math.max(Math.max(rect.height(), rect2.height()), this.mlineHeight)) * 1.1f);
            FontMetricsInt fontMetricsInt = this.mSpeedTextPaint.getFontMetricsInt();
            FontMetricsInt fontMetricsInt2 = this.mDurationTextPaint.getFontMetricsInt();
            canvas.drawText(this.mSpeed, 0.0f, (float) (((round - fontMetricsInt.bottom) - fontMetricsInt.top) / 2), this.mSpeedTextPaint);
            canvas.drawRoundRect((float) ((this.mNeedExtendSpeedWidth ? rect.width() + 10 : rect.width()) + this.mlineGap), (float) ((round - this.mlineHeight) / 2), (float) ((this.mNeedExtendSpeedWidth ? rect.width() + 10 : rect.width()) + this.mlineGap + this.mlineWidth), (float) ((this.mlineHeight + round) / 2), 2.0f, 2.0f, this.mPaint);
            if (TextUtils.isEmpty(this.mNumber)) {
                str = this.mDuration;
                boolean z = this.mNeedExtendSpeedWidth;
                int width2 = rect.width();
                if (z) {
                    width2 += 10;
                }
                width = width2 + (this.mlineGap * 2) + this.mlineWidth;
            } else {
                Rect rect3 = new Rect();
                TextPaint textPaint3 = this.mSpeedTextPaint;
                String str4 = this.mNumber;
                textPaint3.getTextBounds(str4, 0, str4.length(), rect3);
                canvas.drawText(this.mNumber, (float) ((this.mNeedExtendSpeedWidth ? rect.width() + 10 : rect.width()) + (this.mlineGap * 2) + this.mlineWidth), (float) (((round - fontMetricsInt.bottom) - fontMetricsInt.top) / 2), this.mSpeedTextPaint);
                str = this.mDuration;
                boolean z2 = this.mNeedExtendSpeedWidth;
                int width3 = rect.width();
                if (z2) {
                    width3 += 10;
                }
                width = width3 + rect3.width() + (this.mlineGap * 2) + this.mlineWidth + 4;
            }
            canvas.drawText(str, (float) width, (float) (((round - fontMetricsInt2.bottom) - fontMetricsInt2.top) / 2), this.mDurationTextPaint);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        if (!TextUtils.isEmpty(this.mSpeed) && !TextUtils.isEmpty(this.mDuration)) {
            Rect rect = new Rect();
            TextPaint textPaint = this.mSpeedTextPaint;
            String str = this.mSpeed;
            textPaint.getTextBounds(str, 0, str.length(), rect);
            Rect rect2 = new Rect();
            TextPaint textPaint2 = this.mDurationTextPaint;
            String str2 = this.mDuration;
            textPaint2.getTextBounds(str2, 0, str2.length(), rect2);
            int round = Math.round(((float) Math.max(Math.max(rect.height(), rect2.height()), this.mlineHeight)) * 1.1f);
            if (TextUtils.isEmpty(this.mNumber)) {
                boolean z = this.mNeedExtendSpeedWidth;
                i3 = rect.width();
                if (z) {
                    i3 += 10;
                }
            } else {
                Rect rect3 = new Rect();
                TextPaint textPaint3 = this.mSpeedTextPaint;
                String str3 = this.mNumber;
                textPaint3.getTextBounds(str3, 0, str3.length(), rect3);
                boolean z2 = this.mNeedExtendSpeedWidth;
                int width = rect.width();
                if (z2) {
                    width += 10;
                }
                i3 = width + rect3.width();
            }
            int width2 = i3 + rect2.width() + this.mlineWidth + (this.mlineGap * 2);
            if (this.mNeedExtendTotalWidth) {
                width2 = Math.round(((float) width2) * 1.1f);
            }
            setMeasuredDimension(width2, round);
        }
    }

    public void showFastmotion(String str, String str2, String str3, boolean z, boolean z2) {
        this.mNeedExtendSpeedWidth = z;
        this.mNeedExtendTotalWidth = z2;
        this.mSpeed = str;
        this.mNumber = str2;
        this.mDuration = str3;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(str2);
        sb.append(str3);
        setContentDescription(sb.toString());
        requestLayout();
    }
}
