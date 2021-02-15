package com.android.camera.ui;

import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import androidx.core.view.ViewCompat;
import com.android.camera.Display;
import miui.view.animation.CubicEaseOutInterpolator;

public class ShapeBackGroundView extends FrameLayout {
    public static final int SHAPE_HINT_ALPHA = 153;
    /* access modifiers changed from: private */
    public int mBlackMaskHeight;
    private Paint mBlackMaskPaint;
    private int mBlackOriginHeight;
    /* access modifiers changed from: private */
    public int mCurrentAlpha;
    /* access modifiers changed from: private */
    public int mCurrentHeight;
    /* access modifiers changed from: private */
    public int mCurrentRadius;
    private int mCurrentWidth;
    public int mGravity;
    /* access modifiers changed from: private */
    public Paint mPaint;
    private boolean mTopFloating;
    private int mTopHorizontalOffset;
    private int mTotalHeight;
    private int mTotalWidth;
    private ValueAnimator mValueAnimator;

    public ShapeBackGroundView(Context context) {
        this(context, null, -1, -1);
    }

    public ShapeBackGroundView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1, -1);
        init(context);
    }

    public ShapeBackGroundView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, -1);
        init(context);
    }

    public ShapeBackGroundView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mGravity = 48;
        init(context);
    }

    private void cancelAnimation() {
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mValueAnimator.cancel();
            this.mValueAnimator = null;
        }
    }

    private void init(Context context) {
        this.mBlackMaskHeight = 0;
        setWillNotDraw(false);
        this.mTotalWidth = Display.getWindowWidth();
        this.mPaint = new Paint(1);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBlackMaskPaint = new Paint(1);
        this.mBlackMaskPaint.setStyle(Style.FILL);
        this.mBlackMaskPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
    }

    public boolean animationRunning() {
        ValueAnimator valueAnimator = this.mValueAnimator;
        return valueAnimator != null && valueAnimator.isRunning();
    }

    public int getBlackOriginHeight() {
        return this.mBlackOriginHeight;
    }

    public int getCurrentHeight() {
        return this.mCurrentHeight;
    }

    public int getCurrentMaskHeight() {
        return this.mBlackMaskHeight;
    }

    public void initHeight(int i) {
        this.mCurrentHeight = i;
        this.mTotalHeight = i;
    }

    public boolean maskVisible() {
        return this.mBlackMaskHeight != 0;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i = this.mGravity;
        if (i != 3) {
            if (i == 48) {
                int i2 = this.mTopFloating ? this.mCurrentRadius : -this.mCurrentRadius;
                int i3 = this.mTopFloating ? this.mCurrentHeight - (this.mCurrentRadius / 2) : this.mCurrentHeight;
                int i4 = this.mTopHorizontalOffset;
                float f = (float) i4;
                float f2 = (float) i2;
                float f3 = (float) (this.mTotalWidth - i4);
                float f4 = (float) i3;
                int i5 = this.mCurrentRadius;
                canvas.drawRoundRect(f, f2, f3, f4, (float) i5, (float) i5, this.mPaint);
                int i6 = this.mTopFloating ? this.mBlackMaskHeight - (this.mCurrentRadius / 2) : this.mBlackMaskHeight;
                int i7 = this.mCurrentRadius;
                canvas.drawRoundRect(0.0f, (float) (-i7), (float) this.mTotalWidth, (float) i6, (float) i7, (float) i7, this.mBlackMaskPaint);
            } else if (i == 80) {
                int i8 = this.mTotalHeight;
                float f5 = (float) (i8 - this.mBlackMaskHeight);
                float f6 = (float) this.mTotalWidth;
                float f7 = (float) i8;
                int i9 = this.mCurrentRadius;
                canvas.drawRoundRect(0.0f, f5, f6, f7, (float) i9, (float) i9, this.mBlackMaskPaint);
            }
        }
        super.onDraw(canvas);
    }

    public void setBackgroundAlpha(int i) {
        this.mPaint.setAlpha(i);
    }

    public void setBackgroundAlphaAndRadius(int i, int i2, int i3, int i4) {
        if (i != 255) {
            this.mPaint.setAlpha(i);
        }
        this.mCurrentRadius = i2;
        this.mCurrentHeight = i3;
        this.mBlackMaskHeight = i4;
        invalidate();
    }

    public void setBlackOriginHeight(int i) {
        this.mBlackOriginHeight = i;
    }

    public void setCurrentHeight(int i) {
        this.mCurrentHeight = i;
    }

    public void setCurrentRadius(int i) {
        this.mCurrentRadius = i;
    }

    public void setGravity(int i) {
        this.mGravity = i;
    }

    public void setMaskSpecificHeight(final int i, boolean z) {
        cancelAnimation();
        final int i2 = this.mBlackMaskHeight;
        if (i2 != i) {
            if (!z) {
                this.mBlackMaskHeight = i;
                invalidate();
                return;
            }
            this.mValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mValueAnimator.setDuration(300);
            this.mValueAnimator.setInterpolator(new CubicEaseOutInterpolator());
            this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    ShapeBackGroundView shapeBackGroundView = ShapeBackGroundView.this;
                    int i = i2;
                    shapeBackGroundView.mBlackMaskHeight = (int) (((float) i) + (((float) (i - i)) * floatValue));
                    ShapeBackGroundView.this.invalidate();
                }
            });
            this.mValueAnimator.start();
        }
    }

    public void setTopFloating(boolean z, int i) {
        this.mTopFloating = z;
        this.mTopHorizontalOffset = i;
    }

    public void startBackGroundAnimator(int i, int i2, int i3, int i4, int i5, int i6, int i7, AnimatorListener animatorListener) {
        AnimatorListener animatorListener2 = animatorListener;
        cancelAnimation();
        this.mValueAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mValueAnimator.setDuration((long) i7);
        this.mValueAnimator.setInterpolator(new CubicEaseOutInterpolator());
        if (animatorListener2 != null) {
            this.mValueAnimator.addListener(animatorListener2);
        }
        ValueAnimator valueAnimator = this.mValueAnimator;
        final int i8 = i3;
        final int i9 = i4;
        final int i10 = i5;
        final int i11 = i6;
        final int i12 = i2;
        final int i13 = i;
        AnonymousClass1 r0 = new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ShapeBackGroundView shapeBackGroundView;
                int i;
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ShapeBackGroundView shapeBackGroundView2 = ShapeBackGroundView.this;
                int i2 = i8;
                shapeBackGroundView2.mCurrentHeight = (int) (((float) i2) + (((float) (i9 - i2)) * floatValue));
                ShapeBackGroundView shapeBackGroundView3 = ShapeBackGroundView.this;
                int i3 = i10;
                shapeBackGroundView3.mCurrentRadius = (int) (((float) i3) + (((float) (i11 - i3)) * floatValue));
                int i4 = i12;
                if (i4 == 255) {
                    shapeBackGroundView = ShapeBackGroundView.this;
                    i = shapeBackGroundView.mCurrentHeight;
                } else {
                    ShapeBackGroundView shapeBackGroundView4 = ShapeBackGroundView.this;
                    int i5 = i13;
                    shapeBackGroundView4.mCurrentAlpha = (int) (((float) i5) + (((float) (i4 - i5)) * floatValue));
                    ShapeBackGroundView.this.mPaint.setAlpha(ShapeBackGroundView.this.mCurrentAlpha);
                    shapeBackGroundView = ShapeBackGroundView.this;
                    i = 0;
                }
                shapeBackGroundView.mBlackMaskHeight = i;
                ShapeBackGroundView.this.invalidate();
            }
        };
        valueAnimator.addUpdateListener(r0);
        this.mValueAnimator.start();
    }
}
