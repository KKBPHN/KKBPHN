package com.android.camera.ui.zoom;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.view.ViewCompat;
import com.android.camera.R;
import miui.view.animation.CubicEaseOutInterpolator;

public class ZoomTextImageView extends View {
    public static final int TYPE_ICON = 2;
    public static final int TYPE_TEXT_CIRCLE = 3;
    public static final int TYPE_TEXT_NORMAL = 0;
    public static final int TYPE_TEXT_SNAP = 1;
    private static final int[] mTextActivatedColorState = {16843518};
    private ValueAnimator mBackColorAnim;
    private int mCircleColor;
    private Paint mCirclePaint;
    private int mCircleSize;
    private int mCurrentBackgroundColor;
    private String mCurrentText;
    private int mCurrentType;
    private float mIconAlpha;
    private Paint mIconCirclePaint;
    private float mIconRadius;
    private Paint mIconShadowPaint;
    private Paint mImagePaint;
    private boolean mIsSelect;
    private int mNormalBackgroundColor;
    private ColorStateList mNormalTextColor;
    private int mNormalTextSize;
    private float mRotation;
    private Paint mTextPaint;

    public ZoomTextImageView(Context context) {
        super(context);
        init(context);
    }

    public ZoomTextImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public ZoomTextImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public ZoomTextImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context);
    }

    private void init(Context context) {
        this.mTextPaint = new Paint(1);
        this.mTextPaint.setFakeBoldText(true);
        this.mImagePaint = new Paint(1);
        this.mCirclePaint = new Paint(1);
        this.mCirclePaint.setStyle(Style.FILL);
        this.mImagePaint.setStyle(Style.STROKE);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R.style.ZoomButtonDigitsTextStyle, new int[]{16842901, 16842904});
        this.mNormalTextSize = obtainStyledAttributes.getDimensionPixelSize(obtainStyledAttributes.getIndex(0), this.mNormalTextSize);
        this.mNormalTextColor = obtainStyledAttributes.getColorStateList(obtainStyledAttributes.getIndex(1));
        this.mNormalBackgroundColor = getResources().getColor(R.color.zoom_button_background_color);
        this.mCircleColor = this.mNormalBackgroundColor;
        this.mCircleSize = (getResources().getDimensionPixelSize(R.dimen.manually_indicator_layout_height) - (getResources().getDimensionPixelSize(R.dimen.manually_indicator_background_margin_top_bottom) * 2)) / 2;
        this.mIconShadowPaint = new Paint(1);
        this.mIconCirclePaint = new Paint(1);
        this.mIconCirclePaint.setStyle(Style.FILL);
        this.mIconShadowPaint.setStyle(Style.STROKE);
        this.mIconShadowPaint.setStrokeWidth((float) context.getResources().getDimensionPixelSize(R.dimen.zoom_ratio_dot_shadow));
        this.mIconRadius = (float) (context.getResources().getDimensionPixelSize(R.dimen.zoom_ratio_dot_background_width) / 2);
        this.mIconShadowPaint.setColor(Integer.MIN_VALUE);
        this.mIconCirclePaint.setColor(-1);
    }

    public float getAlpha() {
        return this.mCurrentType == 2 ? this.mIconAlpha : super.getAlpha();
    }

    public void onDraw(Canvas canvas) {
        float f;
        float f2;
        int i;
        Paint paint;
        int i2 = this.mCurrentType;
        String str = "";
        String str2 = "X";
        if (i2 != 0) {
            if (i2 != 1) {
                if (i2 == 2) {
                    canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), this.mIconRadius + (this.mIconShadowPaint.getStrokeWidth() / 2.0f), this.mIconShadowPaint);
                    canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), this.mIconRadius, this.mIconCirclePaint);
                    return;
                } else if (i2 == 3 && this.mCurrentText != null) {
                    if (this.mIsSelect) {
                        paint = this.mTextPaint;
                        i = ViewCompat.MEASURED_STATE_MASK;
                    } else {
                        paint = this.mTextPaint;
                        i = -1;
                    }
                    paint.setColor(i);
                    this.mCirclePaint.setColor(this.mCurrentBackgroundColor);
                    canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2), this.mCirclePaint);
                    this.mTextPaint.setTextSize((float) this.mNormalTextSize);
                    String replaceAll = this.mCurrentText.replaceAll(str2, str);
                    float measureText = this.mTextPaint.measureText(replaceAll);
                    float ascent = this.mTextPaint.ascent() + this.mTextPaint.descent();
                    float measureText2 = this.mTextPaint.measureText(str2);
                    f = this.mTextPaint.ascent() + this.mTextPaint.descent();
                    canvas.save();
                    canvas.rotate(this.mRotation, (float) (getWidth() / 2), (float) (getHeight() / 2));
                    canvas.translate(0.0f, (float) (getHeight() / 2));
                    canvas.drawText(replaceAll, ((float) (getPaddingLeft() + (((getWidth() - getPaddingLeft()) - getPaddingRight()) / 2))) - ((measureText + measureText2) / 2.0f), ((-ascent) / 2.0f) + 1.0f, this.mTextPaint);
                    f2 = ((float) getPaddingLeft()) + (((((float) ((getWidth() - getPaddingLeft()) - getPaddingRight())) + measureText) - measureText2) / 2.0f);
                } else {
                    return;
                }
            } else if (this.mCurrentText != null) {
                this.mCirclePaint.setColor(this.mCircleColor);
                canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) this.mCircleSize, this.mCirclePaint);
                this.mTextPaint.setColor(this.mNormalTextColor.getColorForState(mTextActivatedColorState, 0));
                this.mTextPaint.setTextSize((float) this.mNormalTextSize);
                float measureText3 = this.mTextPaint.measureText(this.mCurrentText);
                float ascent2 = this.mTextPaint.ascent() + this.mTextPaint.descent();
                canvas.save();
                canvas.rotate(this.mRotation, (float) (getWidth() / 2), (float) (getHeight() / 2));
                canvas.translate(0.0f, (float) (getHeight() / 2));
                canvas.drawText(this.mCurrentText, ((float) (getWidth() / 2)) - (measureText3 / 2.0f), ((-ascent2) / 2.0f) + 1.0f, this.mTextPaint);
                canvas.restore();
            } else {
                return;
            }
        } else if (this.mCurrentText != null) {
            this.mCirclePaint.setColor(this.mCircleColor);
            canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) this.mCircleSize, this.mCirclePaint);
            this.mTextPaint.setColor(this.mNormalTextColor.getColorForState(mTextActivatedColorState, 0));
            this.mTextPaint.setTextSize((float) this.mNormalTextSize);
            String replaceAll2 = this.mCurrentText.replaceAll(str2, str);
            float measureText4 = this.mTextPaint.measureText(replaceAll2);
            float ascent3 = this.mTextPaint.ascent() + this.mTextPaint.descent();
            float measureText5 = this.mTextPaint.measureText(str2);
            f = this.mTextPaint.ascent() + this.mTextPaint.descent();
            canvas.save();
            canvas.rotate(this.mRotation, (float) (getWidth() / 2), (float) (getHeight() / 2));
            canvas.translate(0.0f, (float) (getHeight() / 2));
            canvas.drawText(replaceAll2, ((float) (getWidth() / 2)) - ((measureText4 + measureText5) / 2.0f), ((-ascent3) / 2.0f) + 1.0f, this.mTextPaint);
            f2 = ((((float) getWidth()) + measureText4) - measureText5) / 2.0f;
        } else {
            return;
        }
        canvas.drawText(str2, f2, ((-f) / 2.0f) + 1.0f, this.mTextPaint);
        canvas.restore();
    }

    public void setAlpha(float f) {
        if (this.mCurrentType == 2) {
            this.mIconAlpha = f;
            this.mIconShadowPaint.setAlpha(Math.round(50.0f * f));
            this.mIconCirclePaint.setAlpha(Math.round(f * 255.0f));
            invalidate();
            return;
        }
        super.setAlpha(f);
    }

    public void setBackgroundColor(int i) {
        this.mCurrentBackgroundColor = i;
        invalidate();
    }

    public void setCircleColor(int i) {
        this.mCircleColor = i;
        invalidate();
    }

    public void setIcon() {
        this.mCurrentType = 2;
        invalidate();
    }

    public void setNormalBackgroundColor(int i) {
        this.mNormalBackgroundColor = i;
        setBackgroundColor(i);
    }

    public void setRotation(float f) {
        this.mRotation = f;
        invalidate();
    }

    public void setText(int i, String str) {
        this.mCurrentType = i;
        this.mCurrentText = str;
        invalidate();
    }

    public void toToggle(boolean z, boolean z2) {
        if (this.mIsSelect != z) {
            ValueAnimator valueAnimator = this.mBackColorAnim;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mBackColorAnim.cancel();
                this.mBackColorAnim.end();
            }
            this.mIsSelect = z;
            int i = -1;
            if (z2) {
                this.mBackColorAnim = ObjectAnimator.ofInt(this, "backgroundColor", z ? new int[]{this.mNormalBackgroundColor, -1} : new int[]{-1, this.mNormalBackgroundColor});
                this.mBackColorAnim.setDuration(200);
                this.mBackColorAnim.setEvaluator(new ArgbEvaluator());
                this.mBackColorAnim.setInterpolator(new CubicEaseOutInterpolator());
                this.mBackColorAnim.start();
                return;
            }
            if (!z) {
                i = this.mNormalBackgroundColor;
            }
            this.mCurrentBackgroundColor = i;
            invalidate();
        }
    }
}
