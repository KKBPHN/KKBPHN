package com.miui.internal.view;

import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.internal.R;
import com.miui.internal.view.SeekBarGradientDrawable.SeekBarGradientState;
import miui.animation.physics.DynamicAnimation;
import miui.animation.physics.DynamicAnimation.OnAnimationUpdateListener;
import miui.animation.physics.SpringAnimation;
import miui.animation.property.FloatProperty;

public class SeekBaThumbShapeDrawable extends SeekBarGradientDrawable {
    private static final int FULL_ALPHA = 255;
    private static final String TAG = "SeekBaThumbShape";
    private static Drawable mShadowDrawable;
    private OnAnimationUpdateListener mInvalidateUpdateListener = new OnAnimationUpdateListener() {
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            SeekBaThumbShapeDrawable.this.invalidateSelf();
        }
    };
    private SpringAnimation mPressedScaleAnim;
    private SpringAnimation mPressedShadowShowAnim;
    private float mScale = 1.0f;
    private FloatProperty mScaleFloatProperty = new FloatProperty("Scale") {
        public float getValue(SeekBaThumbShapeDrawable seekBaThumbShapeDrawable) {
            return seekBaThumbShapeDrawable.getScale();
        }

        public void setValue(SeekBaThumbShapeDrawable seekBaThumbShapeDrawable, float f) {
            seekBaThumbShapeDrawable.setScale(f);
        }
    };
    private float mShadowAlpha = 0.0f;
    private FloatProperty mShadowAlphaProperty = new FloatProperty("ShadowAlpha") {
        public float getValue(SeekBaThumbShapeDrawable seekBaThumbShapeDrawable) {
            return seekBaThumbShapeDrawable.getShadowAlpha();
        }

        public void setValue(SeekBaThumbShapeDrawable seekBaThumbShapeDrawable, float f) {
            float f2 = 1.0f;
            if (f <= 1.0f) {
                f2 = f;
            }
            if (f2 < 0.0f) {
                f2 = 0.0f;
            }
            seekBaThumbShapeDrawable.setShadowAlpha(f2);
        }
    };
    private SpringAnimation mUnPressedScaleAnim;
    private SpringAnimation mUnPressedShadowHideAnim;

    public class SeekBaThumbShapeDrawableState extends SeekBarGradientState {
        protected SeekBaThumbShapeDrawableState() {
        }

        /* access modifiers changed from: protected */
        public Drawable newSeekBarGradientDrawable(Resources resources, Theme theme, SeekBarGradientState seekBarGradientState) {
            return new SeekBaThumbShapeDrawable(resources, theme, seekBarGradientState);
        }
    }

    public SeekBaThumbShapeDrawable() {
        initAnim();
    }

    public SeekBaThumbShapeDrawable(Resources resources, Theme theme, SeekBarGradientState seekBarGradientState) {
        super(resources, theme, seekBarGradientState);
        initAnim();
        if (resources != null && mShadowDrawable == null) {
            mShadowDrawable = resources.getDrawable(R.drawable.sliding_btn_slider_shadow);
        }
    }

    private void drawShadow(Canvas canvas) {
        Rect bounds = getBounds();
        Drawable drawable = mShadowDrawable;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicWidth2 = (intrinsicWidth - getIntrinsicWidth()) / 2;
            int intrinsicHeight = (mShadowDrawable.getIntrinsicHeight() - getIntrinsicHeight()) / 2;
            mShadowDrawable.setBounds(bounds.left - intrinsicWidth2, bounds.top - intrinsicHeight, bounds.right + intrinsicWidth2, bounds.bottom + intrinsicHeight);
            mShadowDrawable.setAlpha((int) (this.mShadowAlpha * 255.0f));
            mShadowDrawable.draw(canvas);
        }
    }

    private void initAnim() {
        this.mPressedScaleAnim = new SpringAnimation(this, this.mScaleFloatProperty, 3.19f);
        this.mPressedScaleAnim.getSpring().setStiffness(986.96f);
        this.mPressedScaleAnim.getSpring().setDampingRatio(0.7f);
        this.mPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.mPressedScaleAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mUnPressedScaleAnim = new SpringAnimation(this, this.mScaleFloatProperty, 1.0f);
        this.mUnPressedScaleAnim.getSpring().setStiffness(986.96f);
        this.mUnPressedScaleAnim.getSpring().setDampingRatio(0.8f);
        this.mUnPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.mUnPressedScaleAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mPressedShadowShowAnim = new SpringAnimation(this, this.mShadowAlphaProperty, 1.0f);
        this.mPressedShadowShowAnim.getSpring().setStiffness(986.96f);
        this.mPressedShadowShowAnim.getSpring().setDampingRatio(0.99f);
        this.mPressedShadowShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mPressedShadowShowAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mUnPressedShadowHideAnim = new SpringAnimation(this, this.mShadowAlphaProperty, 0.0f);
        this.mUnPressedShadowHideAnim.getSpring().setStiffness(986.96f);
        this.mUnPressedShadowHideAnim.getSpring().setDampingRatio(0.99f);
        this.mUnPressedShadowHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedShadowHideAnim.addUpdateListener(this.mInvalidateUpdateListener);
    }

    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        int i = (bounds.right + bounds.left) / 2;
        int i2 = (bounds.top + bounds.bottom) / 2;
        drawShadow(canvas);
        canvas.save();
        float f = this.mScale;
        canvas.scale(f, f, (float) i, (float) i2);
        super.draw(canvas);
        canvas.restore();
    }

    public float getScale() {
        return this.mScale;
    }

    public float getShadowAlpha() {
        return this.mShadowAlpha;
    }

    /* access modifiers changed from: protected */
    public SeekBarGradientState newSeekBarGradientState() {
        return new SeekBaThumbShapeDrawableState();
    }

    public void setScale(float f) {
        this.mScale = f;
    }

    public void setShadowAlpha(float f) {
        this.mShadowAlpha = f;
    }

    /* access modifiers changed from: protected */
    public void startPressedAnim() {
        if (this.mUnPressedScaleAnim.isRunning()) {
            this.mUnPressedScaleAnim.cancel();
        }
        if (!this.mPressedScaleAnim.isRunning()) {
            this.mPressedScaleAnim.start();
        }
        if (this.mUnPressedShadowHideAnim.isRunning()) {
            this.mUnPressedShadowHideAnim.cancel();
        }
        if (!this.mPressedShadowShowAnim.isRunning()) {
            this.mPressedShadowShowAnim.start();
        }
    }

    /* access modifiers changed from: protected */
    public void startUnPressedAnim() {
        if (this.mPressedScaleAnim.isRunning()) {
            this.mPressedScaleAnim.cancel();
        }
        if (!this.mUnPressedScaleAnim.isRunning()) {
            this.mUnPressedScaleAnim.start();
        }
        if (this.mPressedShadowShowAnim.isRunning()) {
            this.mPressedShadowShowAnim.cancel();
        }
        if (!this.mUnPressedShadowHideAnim.isRunning()) {
            this.mUnPressedShadowHideAnim.start();
        }
    }
}
