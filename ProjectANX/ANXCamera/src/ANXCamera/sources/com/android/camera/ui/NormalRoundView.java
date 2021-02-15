package com.android.camera.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.android.camera.R;

public class NormalRoundView extends FrameLayout {
    private GradientDrawable mBgDrawable = ((GradientDrawable) getContext().getDrawable(R.drawable.bg_mode_item));
    private boolean mIsFill;
    private float mRadius;
    private int mStrokeColor;
    private int mStrokeWidth;

    public NormalRoundView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.NormalRoundView);
        this.mRadius = obtainStyledAttributes.getDimension(0, 0.0f);
        this.mStrokeWidth = (int) obtainStyledAttributes.getDimension(2, 0.0f);
        this.mStrokeColor = obtainStyledAttributes.getColor(1, 0);
        obtainStyledAttributes.recycle();
        GradientDrawable gradientDrawable = this.mBgDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setStroke(this.mStrokeWidth, this.mStrokeColor);
            this.mBgDrawable.setCornerRadius(this.mRadius);
            setBackground(this.mBgDrawable);
        }
    }

    public float getCornerRadius() {
        return this.mRadius;
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        GradientDrawable gradientDrawable = this.mBgDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setSize(i, i2);
        }
    }

    public void setCornerRadius(float f) {
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.mRadius = f;
        GradientDrawable gradientDrawable = this.mBgDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setCornerRadius(this.mRadius);
            invalidateDrawable(this.mBgDrawable);
            invalidate();
        }
    }

    public void setFill(boolean z) {
        this.mIsFill = z;
        GradientDrawable gradientDrawable = this.mBgDrawable;
        if (gradientDrawable != null) {
            gradientDrawable.setColor(this.mIsFill ? getResources().getColor(R.color.mode_icon_bg) : 0);
            if (this.mIsFill) {
                this.mBgDrawable.setStroke(0, 0);
            } else {
                this.mBgDrawable.setStroke(this.mStrokeWidth, this.mStrokeColor);
            }
            invalidateDrawable(this.mBgDrawable);
            invalidate();
        }
    }
}
