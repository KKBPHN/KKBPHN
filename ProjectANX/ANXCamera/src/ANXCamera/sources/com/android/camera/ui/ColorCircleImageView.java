package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import com.android.camera.constant.ColorConstant;
import com.android.camera.customization.TintColor;
import miui.view.animation.CubicEaseOutInterpolator;

public class ColorCircleImageView extends AppCompatImageView {
    private static final int[] BACKGROUND_COLOR = {654311423, ColorConstant.COLOR_COMMON_SELECTED};
    /* access modifiers changed from: private */
    public static final int[] ICON_COLOR = {-1, ViewCompat.MEASURED_STATE_MASK};
    private ValueAnimator mAnimator;
    /* access modifiers changed from: private */
    public ArgbEvaluator mArgbEvaluator;
    private int mBackgroundColor;
    /* access modifiers changed from: private */
    public Paint mBackgroundPaint;
    private boolean mCurrentStatus;
    /* access modifiers changed from: private */
    public Paint mIconPaint;

    public ColorCircleImageView(Context context) {
        super(context);
        init();
    }

    public ColorCircleImageView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ColorCircleImageView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    /* access modifiers changed from: private */
    public int getBackgroundColor(int i) {
        return i == 0 ? BACKGROUND_COLOR[0] : TintColor.tintColor();
    }

    private void init() {
        ViewCompat.setLayerType(this, 2, null);
        this.mIconPaint = new Paint();
        this.mIconPaint.setAntiAlias(true);
        this.mIconPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        this.mIconPaint.setColor(ICON_COLOR[0]);
        this.mBackgroundPaint = new Paint();
        this.mBackgroundPaint.setAntiAlias(true);
        this.mBackgroundPaint.setStyle(Style.FILL);
        this.mBackgroundPaint.setColor(getBackgroundColor(0));
        this.mArgbEvaluator = new ArgbEvaluator();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawCircle((float) (getWidth() / 2), (float) (getHeight() / 2), (float) Math.min(getWidth() / 2, getHeight() / 2), this.mBackgroundPaint);
        int saveLayer = canvas.saveLayer(0.0f, 0.0f, (float) getWidth(), (float) getHeight(), null, 31);
        super.onDraw(canvas);
        canvas.drawPaint(this.mIconPaint);
        canvas.restoreToCount(saveLayer);
    }

    public void setSwitchOn(boolean z) {
        this.mCurrentStatus = !z;
        toggle(false);
    }

    public void toggle(boolean z) {
        boolean z2 = this.mCurrentStatus;
        final int i = !z2;
        this.mCurrentStatus = !z2;
        if (!z) {
            this.mIconPaint.setColor(ICON_COLOR[i]);
            this.mBackgroundPaint.setColor(getBackgroundColor(i));
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mAnimator.setDuration(200);
        this.mAnimator.setInterpolator(new CubicEaseOutInterpolator() {
            public float getInterpolation(float f) {
                float interpolation = super.getInterpolation(f);
                ColorCircleImageView.this.mIconPaint.setColor(((Integer) ColorCircleImageView.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(ColorCircleImageView.ICON_COLOR[1 - i]), Integer.valueOf(ColorCircleImageView.ICON_COLOR[i]))).intValue());
                ColorCircleImageView.this.mBackgroundPaint.setColor(((Integer) ColorCircleImageView.this.mArgbEvaluator.evaluate(interpolation, Integer.valueOf(ColorCircleImageView.this.getBackgroundColor(1 - i)), Integer.valueOf(ColorCircleImageView.this.getBackgroundColor(i)))).intValue());
                ColorCircleImageView.this.invalidate();
                return interpolation;
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                ColorCircleImageView.this.mIconPaint.setColor(ColorCircleImageView.ICON_COLOR[i]);
                ColorCircleImageView.this.mBackgroundPaint.setColor(ColorCircleImageView.this.getBackgroundColor(i));
                ColorCircleImageView.this.invalidate();
            }
        });
        this.mAnimator.start();
    }
}
