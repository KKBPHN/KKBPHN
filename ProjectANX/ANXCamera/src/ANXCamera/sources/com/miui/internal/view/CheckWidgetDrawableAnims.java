package com.miui.internal.view;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import miui.animation.physics.DynamicAnimation;
import miui.animation.physics.DynamicAnimation.OnAnimationUpdateListener;
import miui.animation.physics.SpringAnimation;
import miui.animation.property.FloatProperty;

public class CheckWidgetDrawableAnims {
    private static final int FULL_ALPHA = 255;
    private static final float HIGH_STIFFNESS = 986.96f;
    private int mBackgroundDisableAlpha;
    private int mBackgroundNormalAlpha;
    private CheckWidgetCircleDrawable mBlackDrawable;
    private CheckWidgetCircleDrawable mBlueDrawable;
    private FloatProperty mCircleAlphaFloatProperty;
    private CheckWidgetCircleDrawable mGrayDrawable;
    private boolean mIsSingleSelection;
    /* access modifiers changed from: private */
    public CheckBoxAnimatedStateListDrawable mParent;
    /* access modifiers changed from: private */
    public SpringAnimation mParentCheckedUnPressScaleAnim;
    private FloatProperty mParentContentAlphaFloatProperty;
    private OnAnimationUpdateListener mParentInvalidListener = new OnAnimationUpdateListener() {
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            CheckWidgetDrawableAnims.this.mParent.invalidateSelf();
        }
    };
    private SpringAnimation mParentPressAnim;
    private FloatProperty mParentScaleFloatProperty;
    private OnAnimationUpdateListener mParentScaleInvalidListener = new OnAnimationUpdateListener() {
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            CheckWidgetDrawableAnims.this.mParent.setScale(CheckWidgetDrawableAnims.this.getScale());
            CheckWidgetDrawableAnims.this.mParent.invalidateSelf();
        }
    };
    private SpringAnimation mParentUnCheckedUnPressScaleAnim;
    /* access modifiers changed from: private */
    public SpringAnimation mParentUnPressAlphaAnim;
    private SpringAnimation mPressedBlackAnim;
    private SpringAnimation mPressedScaleAnim;
    private float mScale = 1.0f;
    private SpringAnimation mUnPressedBlackAnim;
    private SpringAnimation mUnPressedBlueHideAnim;
    private SpringAnimation mUnPressedBlueShowAnim;
    private FloatProperty scaleFloatProperty;
    private SpringAnimation unPressedScaleAnim;

    public CheckWidgetDrawableAnims(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, boolean z, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9 = i4;
        int i10 = i5;
        String str = "Scale";
        this.mParentScaleFloatProperty = new FloatProperty(str) {
            public float getValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable) {
                return CheckWidgetDrawableAnims.this.mParent.getScale();
            }

            public void setValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, float f) {
                CheckWidgetDrawableAnims.this.mParent.setScale(f);
            }
        };
        this.mParentContentAlphaFloatProperty = new FloatProperty("ContentAlpha") {
            public float getValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable) {
                return checkBoxAnimatedStateListDrawable.getContentAlpha();
            }

            public void setValue(CheckBoxAnimatedStateListDrawable checkBoxAnimatedStateListDrawable, float f) {
                float f2 = 1.0f;
                if (f <= 1.0f) {
                    f2 = f;
                }
                if (f2 < 0.0f) {
                    f2 = 0.0f;
                }
                checkBoxAnimatedStateListDrawable.setContentAlpha(f2);
            }
        };
        this.scaleFloatProperty = new FloatProperty(str) {
            public float getValue(CheckWidgetDrawableAnims checkWidgetDrawableAnims) {
                return CheckWidgetDrawableAnims.this.getScale();
            }

            public void setValue(CheckWidgetDrawableAnims checkWidgetDrawableAnims, float f) {
                CheckWidgetDrawableAnims.this.setScale(f);
            }
        };
        this.mCircleAlphaFloatProperty = new FloatProperty("Alpha") {
            public float getValue(CheckWidgetCircleDrawable checkWidgetCircleDrawable) {
                return (float) (checkWidgetCircleDrawable.getAlpha() / 255);
            }

            public void setValue(CheckWidgetCircleDrawable checkWidgetCircleDrawable, float f) {
                float f2 = 1.0f;
                if (f <= 1.0f) {
                    f2 = f;
                }
                if (f2 < 0.0f) {
                    f2 = 0.0f;
                }
                checkWidgetCircleDrawable.setAlpha((int) (f2 * 255.0f));
            }
        };
        this.mIsSingleSelection = false;
        this.mBackgroundNormalAlpha = i9;
        this.mBackgroundDisableAlpha = i10;
        this.mIsSingleSelection = z;
        CheckWidgetCircleDrawable checkWidgetCircleDrawable = new CheckWidgetCircleDrawable(i, i4, i5, i6, i7, i8);
        this.mGrayDrawable = checkWidgetCircleDrawable;
        this.mGrayDrawable.setAlpha(this.mBackgroundNormalAlpha);
        this.mBlackDrawable = new CheckWidgetCircleDrawable(i2, i9, i10);
        this.mBlackDrawable.setAlpha(0);
        this.mBlueDrawable = new CheckWidgetCircleDrawable(i3, i9, i10);
        this.mBlueDrawable.setAlpha(255);
        this.mParent = checkBoxAnimatedStateListDrawable;
        initAnim();
    }

    private void initAnim() {
        float f;
        SpringAnimation springAnimation;
        this.mPressedScaleAnim = new SpringAnimation(this, this.scaleFloatProperty, 0.6f);
        this.mPressedScaleAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mPressedScaleAnim.getSpring().setDampingRatio(0.99f);
        this.mPressedScaleAnim.getSpring().setFinalPosition(0.6f);
        this.mPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.mPressedScaleAnim.addUpdateListener(this.mParentScaleInvalidListener);
        this.unPressedScaleAnim = new SpringAnimation(this, this.scaleFloatProperty, 1.0f);
        this.unPressedScaleAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.unPressedScaleAnim.getSpring().setDampingRatio(0.6f);
        this.unPressedScaleAnim.setMinimumVisibleChange(0.002f);
        this.unPressedScaleAnim.addUpdateListener(new OnAnimationUpdateListener() {
            public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
                CheckWidgetDrawableAnims.this.mParent.invalidateSelf();
            }
        });
        this.mParentPressAnim = new SpringAnimation(this.mParent, this.mParentContentAlphaFloatProperty, 0.5f);
        this.mParentPressAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mParentPressAnim.getSpring().setDampingRatio(0.99f);
        this.mParentPressAnim.setMinimumVisibleChange(0.00390625f);
        this.mParentPressAnim.addUpdateListener(this.mParentInvalidListener);
        this.mPressedBlackAnim = new SpringAnimation(this.mBlackDrawable, this.mCircleAlphaFloatProperty, 0.1f);
        this.mPressedBlackAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mPressedBlackAnim.getSpring().setDampingRatio(0.99f);
        this.mPressedBlackAnim.setMinimumVisibleChange(0.00390625f);
        this.mPressedBlackAnim.addUpdateListener(this.mParentInvalidListener);
        this.mUnPressedBlackAnim = new SpringAnimation(this.mBlackDrawable, this.mCircleAlphaFloatProperty, 0.0f);
        this.mUnPressedBlackAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mUnPressedBlackAnim.getSpring().setDampingRatio(0.99f);
        this.mUnPressedBlackAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlackAnim.addUpdateListener(this.mParentInvalidListener);
        this.mUnPressedBlueShowAnim = new SpringAnimation(this.mBlueDrawable, this.mCircleAlphaFloatProperty, 1.0f);
        this.mUnPressedBlueShowAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mUnPressedBlueShowAnim.getSpring().setDampingRatio(0.7f);
        this.mUnPressedBlueShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlueShowAnim.addUpdateListener(this.mParentInvalidListener);
        this.mParentUnPressAlphaAnim = new SpringAnimation(this.mParent, this.mParentContentAlphaFloatProperty, 1.0f);
        this.mParentUnPressAlphaAnim.getSpring().setStiffness(438.64f);
        this.mParentUnPressAlphaAnim.getSpring().setDampingRatio(0.6f);
        this.mParentUnPressAlphaAnim.setMinimumVisibleChange(0.00390625f);
        this.mParentUnPressAlphaAnim.addUpdateListener(this.mParentInvalidListener);
        this.mUnPressedBlueHideAnim = new SpringAnimation(this.mBlueDrawable, this.mCircleAlphaFloatProperty, 0.0f);
        this.mUnPressedBlueHideAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mUnPressedBlueHideAnim.getSpring().setDampingRatio(0.99f);
        this.mUnPressedBlueHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnPressedBlueHideAnim.addUpdateListener(this.mParentInvalidListener);
        this.mParentCheckedUnPressScaleAnim = new SpringAnimation(this.mParent, this.mParentScaleFloatProperty, 1.0f);
        this.mParentCheckedUnPressScaleAnim.getSpring().setStiffness(438.64f);
        this.mParentCheckedUnPressScaleAnim.getSpring().setDampingRatio(0.6f);
        this.mParentCheckedUnPressScaleAnim.setMinimumVisibleChange(0.002f);
        this.mParentCheckedUnPressScaleAnim.addUpdateListener(this.mParentInvalidListener);
        if (this.mIsSingleSelection) {
            springAnimation = this.mParentCheckedUnPressScaleAnim;
            f = 5.0f;
        } else {
            springAnimation = this.mParentCheckedUnPressScaleAnim;
            f = 10.0f;
        }
        springAnimation.setStartVelocity(f);
        this.mParentUnCheckedUnPressScaleAnim = new SpringAnimation(this.mParent, this.mParentScaleFloatProperty, 0.3f);
        this.mParentUnCheckedUnPressScaleAnim.getSpring().setStiffness(HIGH_STIFFNESS);
        this.mParentUnCheckedUnPressScaleAnim.getSpring().setDampingRatio(0.99f);
        this.mParentUnCheckedUnPressScaleAnim.setMinimumVisibleChange(0.002f);
        this.mParentUnCheckedUnPressScaleAnim.addUpdateListener(this.mParentScaleInvalidListener);
    }

    public void draw(Canvas canvas) {
        this.mGrayDrawable.draw(canvas);
        this.mBlackDrawable.draw(canvas);
        this.mBlueDrawable.draw(canvas);
    }

    public float getScale() {
        return this.mScale;
    }

    public void setBounds(int i, int i2, int i3, int i4) {
        this.mGrayDrawable.setBounds(i, i2, i3, i4);
        this.mBlackDrawable.setBounds(i, i2, i3, i4);
        this.mBlueDrawable.setBounds(i, i2, i3, i4);
    }

    public void setBounds(Rect rect) {
        this.mGrayDrawable.setBounds(rect);
        this.mBlackDrawable.setBounds(rect);
        this.mBlueDrawable.setBounds(rect);
    }

    public void setScale(float f) {
        this.mGrayDrawable.setScale(f);
        this.mBlackDrawable.setScale(f);
        this.mBlueDrawable.setScale(f);
        this.mScale = f;
    }

    /* access modifiers changed from: protected */
    public void startPressedAnim(boolean z, boolean z2) {
        if (z2 && Thread.currentThread() == Looper.getMainLooper().getThread()) {
            if (!this.mPressedScaleAnim.isRunning()) {
                this.mPressedScaleAnim.start();
            }
            if (!this.mParentPressAnim.isRunning()) {
                this.mParentPressAnim.start();
            }
            if (!z && !this.mPressedBlackAnim.isRunning()) {
                this.mPressedBlackAnim.start();
            }
            if (this.mUnPressedBlackAnim.isRunning()) {
                this.mUnPressedBlackAnim.cancel();
            }
            if (this.unPressedScaleAnim.isRunning()) {
                this.unPressedScaleAnim.cancel();
            }
            if (this.mParentUnPressAlphaAnim.isRunning()) {
                this.mParentUnPressAlphaAnim.cancel();
            }
            if (this.mParentCheckedUnPressScaleAnim.isRunning()) {
                this.mParentCheckedUnPressScaleAnim.cancel();
            }
            if (this.mParentUnCheckedUnPressScaleAnim.isRunning()) {
                this.mParentUnCheckedUnPressScaleAnim.cancel();
            }
            if (this.mUnPressedBlueHideAnim.isRunning()) {
                this.mUnPressedBlueHideAnim.cancel();
            }
            if (this.mUnPressedBlueShowAnim.isRunning()) {
                this.mUnPressedBlueShowAnim.cancel();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void startUnPressedAnim(boolean z, boolean z2) {
        CheckWidgetCircleDrawable checkWidgetCircleDrawable;
        SpringAnimation springAnimation;
        float f;
        SpringAnimation springAnimation2;
        if (!z2 || Thread.currentThread() != Looper.getMainLooper().getThread()) {
            if (z) {
                checkWidgetCircleDrawable = this.mBlueDrawable;
                springAnimation = this.mUnPressedBlueShowAnim;
            } else {
                checkWidgetCircleDrawable = this.mBlueDrawable;
                springAnimation = this.mUnPressedBlueHideAnim;
            }
            checkWidgetCircleDrawable.setAlpha((int) (springAnimation.getSpring().getFinalPosition() * 255.0f));
            return;
        }
        if (this.mPressedScaleAnim.isRunning()) {
            this.mPressedScaleAnim.cancel();
        }
        if (this.mParentPressAnim.isRunning()) {
            this.mParentPressAnim.cancel();
        }
        if (this.mPressedBlackAnim.isRunning()) {
            this.mPressedBlackAnim.cancel();
        }
        if (!this.mUnPressedBlackAnim.isRunning()) {
            this.mUnPressedBlackAnim.start();
        }
        if (z) {
            if (this.mUnPressedBlueHideAnim.isRunning()) {
                this.mUnPressedBlueHideAnim.cancel();
            }
            if (!this.mUnPressedBlueShowAnim.isRunning()) {
                this.mUnPressedBlueShowAnim.start();
            }
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    if (!CheckWidgetDrawableAnims.this.mParentUnPressAlphaAnim.isRunning()) {
                        CheckWidgetDrawableAnims.this.mParentUnPressAlphaAnim.start();
                    }
                    if (!CheckWidgetDrawableAnims.this.mParentCheckedUnPressScaleAnim.isRunning()) {
                        CheckWidgetDrawableAnims.this.mParentCheckedUnPressScaleAnim.start();
                    }
                }
            }, 50);
            if (this.mIsSingleSelection) {
                springAnimation2 = this.unPressedScaleAnim;
                f = 10.0f;
            } else {
                springAnimation2 = this.unPressedScaleAnim;
                f = 5.0f;
            }
            springAnimation2.setStartVelocity(f);
        } else {
            if (this.mUnPressedBlueShowAnim.isRunning()) {
                this.mUnPressedBlueShowAnim.cancel();
            }
            if (!this.mUnPressedBlueHideAnim.isRunning()) {
                this.mUnPressedBlueHideAnim.start();
            }
            if (!this.mParentUnCheckedUnPressScaleAnim.isRunning()) {
                this.mParentUnCheckedUnPressScaleAnim.start();
            }
        }
        this.unPressedScaleAnim.start();
    }

    /* access modifiers changed from: protected */
    public void verifyChecked(boolean z, boolean z2) {
        CheckWidgetCircleDrawable checkWidgetCircleDrawable;
        int i;
        if (z2) {
            if (z) {
                this.mBlueDrawable.setAlpha(255);
                this.mBlackDrawable.setAlpha(25);
            } else {
                this.mBlueDrawable.setAlpha(0);
                this.mBlackDrawable.setAlpha(0);
            }
            checkWidgetCircleDrawable = this.mGrayDrawable;
            i = this.mBackgroundNormalAlpha;
        } else {
            this.mBlueDrawable.setAlpha(0);
            this.mBlackDrawable.setAlpha(0);
            checkWidgetCircleDrawable = this.mGrayDrawable;
            i = this.mBackgroundDisableAlpha;
        }
        checkWidgetCircleDrawable.setAlpha(i);
    }
}
