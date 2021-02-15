package com.miui.internal.hybrid;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import miui.R;

public class WebContainerView extends FrameLayout {
    private boolean mIsPulling = false;
    private float mLastX;
    private float mLastY;
    private boolean mPullable;
    private int mTouchSlop;
    private View mWebView;

    public WebContainerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.HybridViewStyle, 0, 0);
        this.mPullable = obtainStyledAttributes.getBoolean(R.styleable.HybridViewStyle_hybridPullable, true);
        obtainStyledAttributes.recycle();
        this.mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    private void springBack() {
        if (getTranslationY() != 0.0f) {
            ViewPropertyAnimator animate = animate();
            animate.translationY(0.0f);
            animate.setDuration((long) getResources().getInteger(17694721));
            animate.start();
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.mWebView != null && this.mPullable) {
            int actionMasked = motionEvent.getActionMasked();
            float rawX = motionEvent.getRawX();
            float rawY = motionEvent.getRawY();
            if (actionMasked == 3 || actionMasked == 1) {
                this.mIsPulling = false;
            } else {
                if (actionMasked == 0) {
                    this.mLastX = rawX;
                    this.mLastY = rawY;
                } else if (actionMasked == 2) {
                    if (this.mIsPulling) {
                        return true;
                    }
                    float f = this.mLastY - rawY;
                    float abs = Math.abs(this.mLastX - rawX);
                    float abs2 = Math.abs(f);
                    this.mLastX = rawX;
                    this.mLastY = rawY;
                    if (this.mWebView.getScrollY() == 0 && f < 0.0f && abs2 > abs && abs2 > ((float) this.mTouchSlop)) {
                        this.mIsPulling = true;
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        if (r5 != 3) goto L_0x0036;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.mPullable && this.mIsPulling) {
            float rawY = motionEvent.getRawY();
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    if (action == 2) {
                        setTranslationY(getTranslationY() + ((rawY - this.mLastY) * 0.5f));
                    }
                }
                this.mIsPulling = false;
                springBack();
            }
            this.mLastY = rawY;
        }
        return false;
    }

    public void setWebView(View view) {
        if (view != null) {
            View view2 = this.mWebView;
            if (view2 != view) {
                if (view2 != null) {
                    removeView(view2);
                }
                this.mWebView = view;
                addView(view, 0, new LayoutParams(-1, -1));
            }
        }
    }
}
