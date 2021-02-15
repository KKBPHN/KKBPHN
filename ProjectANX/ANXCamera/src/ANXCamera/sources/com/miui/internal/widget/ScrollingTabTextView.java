package com.miui.internal.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;
import com.miui.internal.R;
import miui.util.ViewUtils;

public class ScrollingTabTextView extends TextView {
    private ValueAnimator mAnimator;
    /* access modifiers changed from: private */
    public int mClipPosition;
    private boolean mLeftToRight;
    private int mNormalColor;
    private ColorStateList mOriginColor;
    private int mSelectedColor;

    public ScrollingTabTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setupColors();
    }

    private void setupColors() {
        this.mOriginColor = getTextColors();
        this.mNormalColor = this.mOriginColor.getColorForState(TextView.ENABLED_STATE_SET, getResources().getColor(R.color.action_bar_tab_text_color_normal_light));
        this.mSelectedColor = this.mOriginColor.getColorForState(TextView.ENABLED_SELECTED_STATE_SET, getResources().getColor(R.color.action_bar_tab_text_color_selected_light));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null || !valueAnimator.isRunning()) {
            super.onDraw(canvas);
            return;
        }
        int i2 = ((!this.mLeftToRight || isSelected()) && (this.mLeftToRight || !isSelected())) ? this.mSelectedColor : this.mNormalColor;
        setTextColor(i2);
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int i3 = this.mClipPosition;
        int height = getHeight();
        if (isLayoutRtl) {
            i = getScrollX() + 0;
            i3 += getScrollX();
        } else {
            i = 0;
        }
        canvas.save();
        canvas.clipRect(i, 0, i3, height);
        super.onDraw(canvas);
        canvas.restore();
        int i4 = this.mNormalColor;
        if (i2 == i4) {
            i2 = this.mSelectedColor;
        } else if (i2 == this.mSelectedColor) {
            i2 = i4;
        }
        setTextColor(i2);
        int i5 = this.mClipPosition;
        int width = getWidth();
        if (isLayoutRtl) {
            i5 += getScrollX();
            width += getScrollX();
        }
        canvas.save();
        canvas.clipRect(i5, 0, width, height);
        super.onDraw(canvas);
        canvas.restore();
        setTextColor(this.mOriginColor);
    }

    public void setTextColor(ColorStateList colorStateList) {
        super.setTextColor(colorStateList);
        setupColors();
    }

    public void startScrollAnimation(boolean z) {
        int i;
        int i2;
        ValueAnimator valueAnimator = this.mAnimator;
        if (valueAnimator == null) {
            this.mAnimator = new ValueAnimator();
        } else {
            valueAnimator.cancel();
        }
        this.mLeftToRight = z;
        if (this.mLeftToRight) {
            i2 = getWidth();
            i = 0;
        } else {
            i = getWidth();
            i2 = 0;
        }
        this.mAnimator.setIntValues(new int[]{i, i2});
        this.mAnimator.setDuration(200);
        this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScrollingTabTextView.this.mClipPosition = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                ScrollingTabTextView.this.invalidate();
            }
        });
        this.mAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                ScrollingTabTextView scrollingTabTextView = ScrollingTabTextView.this;
                scrollingTabTextView.mClipPosition = scrollingTabTextView.getWidth();
            }
        });
        this.mAnimator.start();
    }
}
