package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.android.camera.constant.ColorConstant;
import com.android.camera.customization.TintColor;
import miui.view.animation.CubicEaseOutInterpolator;

public class ColorCircleBackgroundView extends View {
    private static final int[] BACKGROUND_COLOR = {654311423, ColorConstant.COLOR_COMMON_SELECTED};
    private ValueAnimator mAnimator;
    /* access modifiers changed from: private */
    public ArgbEvaluator mArgbEvaluator;
    private int mBackgroundColor;
    /* access modifiers changed from: private */
    public Paint mBackgroundPaint;
    private boolean mCurrentStatus;

    public ColorCircleBackgroundView(Context context) {
        super(context);
        init();
    }

    public ColorCircleBackgroundView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ColorCircleBackgroundView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: private */
    public int getBackgroundColor(int i) {
        return i == 0 ? BACKGROUND_COLOR[0] : TintColor.tintColor();
    }

    private void init() {
        ViewCompat.setLayerType(this, 2, null);
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mBackgroundPaint.setColor(getBackgroundColor(0));
        this.mArgbEvaluator = new ArgbEvaluator();
    }

    private void toggle(boolean z) {
        final boolean z2 = this.mCurrentStatus;
        if (!z) {
            this.mBackgroundPaint.setColor(getBackgroundColor(z2));
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(1200);
        final int color = this.mBackgroundPaint.getColor();
        final int backgroundColor = getBackgroundColor(z2 ? 1 : 0);
        this.mAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                ColorCircleBackgroundView.this.mBackgroundPaint.setColor(((Integer) ColorCircleBackgroundView.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(color), Integer.valueOf(backgroundColor))).intValue());
                ColorCircleBackgroundView.this.invalidate();
                return interpolation;
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                ColorCircleBackgroundView.this.mBackgroundPaint.setColor(ColorCircleBackgroundView.this.getBackgroundColor(z2));
                ColorCircleBackgroundView.this.invalidate();
            }
        });
        this.mAnimator.start();
    }

    public boolean isSwitchOn() {
        return this.mCurrentStatus;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) Math.min(getWidth() / 2, getHeight() / 2), this.mBackgroundPaint);
    }

    public void setSwitchOn(boolean z, boolean z2) {
        if (this.mCurrentStatus != z) {
            this.mCurrentStatus = z;
            toggle(z2);
        }
    }
}
