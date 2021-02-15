package com.miui.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.miui.internal.R;
import miui.animation.physics.DynamicAnimation;
import miui.animation.physics.DynamicAnimation.OnAnimationEndListener;
import miui.animation.physics.DynamicAnimation.OnAnimationUpdateListener;
import miui.animation.physics.SpringAnimation;
import miui.animation.property.FloatProperty;
import miui.smooth.SmoothContainerDrawable;
import miui.util.HapticFeedbackUtil;
import miui.util.ViewUtils;
import miui.view.MiuiHapticFeedbackConstants;

public class SlidingButtonHelper {
    private static final int FULL_ALPHA = 255;
    /* access modifiers changed from: private */
    public boolean mAnimChecked;
    private boolean mAnimCheckedTemp = false;
    private int mCornerRadius;
    private float mExtraAlpha = 1.0f;
    private int mHeight;
    private OnAnimationUpdateListener mInvalidateUpdateListener = new OnAnimationUpdateListener() {
        public void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2) {
            SlidingButtonHelper.this.mView.invalidate();
        }
    };
    private boolean mIsSliderEdgeReached = false;
    private int mLastX;
    private int mMarginVertical;
    private SpringAnimation mMarkedAlphaHideAnim;
    private SpringAnimation mMarkedAlphaShowAnim;
    private Drawable mMaskCheckedSlideBar;
    private float mMaskCheckedSlideBarAlpha = 1.0f;
    private FloatProperty mMaskCheckedSlideBarAlphaProperty = new FloatProperty("MaskCheckedSlideBarAlpha") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getMaskCheckedSlideBarAlpha();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setMaskCheckedSlideBarAlpha(f);
        }
    };
    private float mMaskCheckedSlideBarAlphaTemp = -1.0f;
    private Drawable mMaskUnCheckedPressedSlideBar;
    private float mMaskUnCheckedPressedSlideBarAlpha = 0.0f;
    private FloatProperty mMaskUnCheckedPressedSlideBarAlphaProperty = new FloatProperty("MaskUnCheckedSlideBarAlpha") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getMaskUnCheckedPressedSlideBarAlpha();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setMaskUnCheckedPressedSlideBarAlpha(f);
        }
    };
    private Drawable mMaskUnCheckedSlideBar;
    private OnCheckedChangeListener mOnPerformCheckedChangeListener;
    private int mOriginalTouchPointX;
    private boolean mParamCached = false;
    private StateListDrawable mSlideBar;
    private int mSliderHeight;
    private SpringAnimation mSliderMoveAnim;
    private boolean mSliderMoved;
    private Drawable mSliderOff;
    /* access modifiers changed from: private */
    public int mSliderOffset;
    private FloatProperty mSliderOffsetProperty = new FloatProperty("SliderOffset") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getSliderOffset();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setSliderOffset((int) f);
        }
    };
    private int mSliderOffsetTemp = -1;
    private Drawable mSliderOn;
    private int mSliderOnAlpha;
    private int mSliderOnAlphaTemp = -1;
    /* access modifiers changed from: private */
    public int mSliderPositionEnd;
    private int mSliderPositionStart;
    private SpringAnimation mSliderPressedAnim;
    private float mSliderScale = 1.0f;
    private FloatProperty mSliderScaleFloatProperty = new FloatProperty("SliderScale") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getSliderScale();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setSliderScale(f);
        }
    };
    private Drawable mSliderShadow;
    private float mSliderShadowAlpha = 0.0f;
    private FloatProperty mSliderShadowAlphaProperty = new FloatProperty("SliderShadowAlpha") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getSliderShadowAlpha();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setSliderShadowAlpha(f);
        }
    };
    private SpringAnimation mSliderShadowHideAnim;
    private SpringAnimation mSliderShadowShowAnim;
    private Drawable mSliderStroke;
    private SpringAnimation mSliderUnPressedAnim;
    private int mSliderWidth;
    private SpringAnimation mStokeAlphaHideAnim;
    private SpringAnimation mStokeAlphaShowAnim;
    private float mStrokeAlpha = 0.1f;
    private FloatProperty mStrokeAlphaProperty = new FloatProperty("StrokeAlpha") {
        public float getValue(SlidingButtonHelper slidingButtonHelper) {
            return SlidingButtonHelper.this.getStrokeAlpha();
        }

        public void setValue(SlidingButtonHelper slidingButtonHelper, float f) {
            SlidingButtonHelper.this.setStrokeAlpha(f);
        }
    };
    private int mTapThreshold;
    private Rect mTmpRect = new Rect();
    private boolean mTracking;
    private SpringAnimation mUnMarkedPressedAlphaHideAnim;
    private SpringAnimation mUnMarkedPressedAlphaShowAnim;
    /* access modifiers changed from: private */
    public CompoundButton mView;
    private int mWidth;

    public SlidingButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
        this.mAnimChecked = this.mView.isChecked();
        if (!compoundButton.isChecked()) {
            this.mMaskCheckedSlideBarAlpha = 0.0f;
        }
        initDrawable();
        initAnim();
    }

    private void animateToState(boolean z) {
        if (z != this.mView.isChecked()) {
            this.mView.setChecked(z);
            startCheckedChangeAnimInternal(z);
            notifyCheckedChangeListener();
        }
        animateToState(z, z ? this.mSliderPositionEnd : this.mSliderPositionStart, new Runnable() {
            public void run() {
                SlidingButtonHelper slidingButtonHelper = SlidingButtonHelper.this;
                slidingButtonHelper.mAnimChecked = slidingButtonHelper.mSliderOffset >= SlidingButtonHelper.this.mSliderPositionEnd;
            }
        });
    }

    private void animateToggle() {
        animateToState(!this.mView.isChecked());
        if (HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH)) {
            this.mView.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH);
        }
    }

    private Drawable createMaskDrawable(Drawable drawable) {
        SmoothContainerDrawable smoothContainerDrawable = new SmoothContainerDrawable();
        smoothContainerDrawable.setCornerRadius((float) this.mCornerRadius);
        smoothContainerDrawable.setChildDrawable(drawable);
        int i = this.mMarginVertical;
        smoothContainerDrawable.setBounds(new Rect(0, i, this.mWidth, this.mHeight - i));
        return smoothContainerDrawable;
    }

    private StateListDrawable createMaskedSlideBar() {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.setBounds(0, 0, this.mWidth, this.mHeight);
        stateListDrawable.setCallback(this.mView);
        return stateListDrawable;
    }

    private void initAnim() {
        this.mSliderPressedAnim = new SpringAnimation(this, this.mSliderScaleFloatProperty, 1.61f);
        this.mSliderPressedAnim.getSpring().setStiffness(986.96f);
        this.mSliderPressedAnim.getSpring().setDampingRatio(0.6f);
        this.mSliderPressedAnim.setMinimumVisibleChange(0.002f);
        this.mSliderPressedAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mSliderUnPressedAnim = new SpringAnimation(this, this.mSliderScaleFloatProperty, 1.0f);
        this.mSliderUnPressedAnim.getSpring().setStiffness(986.96f);
        this.mSliderUnPressedAnim.getSpring().setDampingRatio(0.6f);
        this.mSliderUnPressedAnim.setMinimumVisibleChange(0.002f);
        this.mSliderUnPressedAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mSliderShadowShowAnim = new SpringAnimation(this, this.mSliderShadowAlphaProperty, 1.0f);
        this.mSliderShadowShowAnim.getSpring().setStiffness(986.96f);
        this.mSliderShadowShowAnim.getSpring().setDampingRatio(0.99f);
        this.mSliderShadowShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mSliderShadowShowAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mSliderShadowHideAnim = new SpringAnimation(this, this.mSliderShadowAlphaProperty, 0.0f);
        this.mSliderShadowHideAnim.getSpring().setStiffness(986.96f);
        this.mSliderShadowHideAnim.getSpring().setDampingRatio(0.99f);
        this.mSliderShadowHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mSliderShadowHideAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mStokeAlphaShowAnim = new SpringAnimation(this, this.mStrokeAlphaProperty, 0.15f);
        this.mStokeAlphaShowAnim.getSpring().setStiffness(986.96f);
        this.mStokeAlphaShowAnim.getSpring().setDampingRatio(0.99f);
        this.mStokeAlphaShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mStokeAlphaShowAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mStokeAlphaHideAnim = new SpringAnimation(this, this.mStrokeAlphaProperty, 0.1f);
        this.mStokeAlphaHideAnim.getSpring().setStiffness(986.96f);
        this.mStokeAlphaHideAnim.getSpring().setDampingRatio(0.99f);
        this.mStokeAlphaHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mStokeAlphaHideAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mMarkedAlphaShowAnim = new SpringAnimation(this, this.mMaskCheckedSlideBarAlphaProperty, 1.0f);
        this.mMarkedAlphaShowAnim.getSpring().setStiffness(438.64f);
        this.mMarkedAlphaShowAnim.getSpring().setDampingRatio(0.99f);
        this.mMarkedAlphaShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mMarkedAlphaShowAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mMarkedAlphaHideAnim = new SpringAnimation(this, this.mMaskCheckedSlideBarAlphaProperty, 0.0f);
        this.mMarkedAlphaHideAnim.getSpring().setStiffness(986.96f);
        this.mMarkedAlphaHideAnim.getSpring().setDampingRatio(0.99f);
        this.mMarkedAlphaHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mMarkedAlphaHideAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mUnMarkedPressedAlphaShowAnim = new SpringAnimation(this, this.mMaskUnCheckedPressedSlideBarAlphaProperty, 0.05f);
        this.mUnMarkedPressedAlphaShowAnim.getSpring().setStiffness(986.96f);
        this.mUnMarkedPressedAlphaShowAnim.getSpring().setDampingRatio(0.99f);
        this.mUnMarkedPressedAlphaShowAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnMarkedPressedAlphaShowAnim.addUpdateListener(this.mInvalidateUpdateListener);
        this.mUnMarkedPressedAlphaHideAnim = new SpringAnimation(this, this.mMaskUnCheckedPressedSlideBarAlphaProperty, 0.0f);
        this.mUnMarkedPressedAlphaHideAnim.getSpring().setStiffness(986.96f);
        this.mUnMarkedPressedAlphaHideAnim.getSpring().setDampingRatio(0.99f);
        this.mUnMarkedPressedAlphaHideAnim.setMinimumVisibleChange(0.00390625f);
        this.mUnMarkedPressedAlphaHideAnim.addUpdateListener(this.mInvalidateUpdateListener);
    }

    private void initDrawable() {
        this.mSliderShadow = this.mView.getResources().getDrawable(R.drawable.sliding_btn_slider_shadow);
        this.mSliderStroke = this.mView.getResources().getDrawable(R.drawable.sliding_btn_slider_stroke_light);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0019, code lost:
        if (r3 > r0) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void moveSlider(int i) {
        if (ViewUtils.isLayoutRtl(this.mView)) {
            i = -i;
        }
        this.mSliderOffset += i;
        int i2 = this.mSliderOffset;
        int i3 = this.mSliderPositionStart;
        if (i2 >= i3) {
            i3 = this.mSliderPositionEnd;
        }
        this.mSliderOffset = i3;
        int i4 = this.mSliderOffset;
        boolean z = i4 == this.mSliderPositionStart || i4 == this.mSliderPositionEnd;
        if (z && !this.mIsSliderEdgeReached && HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH)) {
            this.mView.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH);
        }
        this.mIsSliderEdgeReached = z;
        setSliderOffset(this.mSliderOffset);
    }

    private void onTouchEventDown(int i, int i2, Rect rect) {
        if (rect.contains(i, i2)) {
            boolean z = true;
            this.mTracking = true;
            this.mView.setPressed(true);
            onPressedInner();
            int i3 = this.mSliderOffset;
            if (i3 > this.mSliderPositionStart && i3 < this.mSliderPositionEnd) {
                z = false;
            }
            this.mIsSliderEdgeReached = z;
        } else {
            this.mTracking = false;
        }
        this.mLastX = i;
        this.mOriginalTouchPointX = i;
        this.mSliderMoved = false;
    }

    private void onTouchEventMove(int i) {
        if (this.mTracking) {
            moveSlider(i - this.mLastX);
            this.mLastX = i;
            if (Math.abs(i - this.mOriginalTouchPointX) >= this.mTapThreshold) {
                this.mSliderMoved = true;
                this.mView.getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    private void onTouchEventUp(int i, int i2, Rect rect) {
        if (!this.mTracking || !this.mSliderMoved) {
            animateToggle();
            return;
        }
        this.mAnimChecked = this.mSliderOffset >= this.mSliderPositionEnd / 2;
        animateToState(this.mAnimChecked);
        if (rect.contains(i, i2) && HapticFeedbackUtil.isSupportLinearMotorVibrate(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH)) {
            this.mView.performHapticFeedback(MiuiHapticFeedbackConstants.FLAG_MIUI_HAPTIC_SWITCH);
        }
    }

    private void popSavedParams() {
        if (this.mParamCached) {
            this.mSliderOffset = this.mSliderOffsetTemp;
            this.mSliderOnAlpha = this.mSliderOnAlphaTemp;
            this.mMaskCheckedSlideBarAlpha = this.mMaskCheckedSlideBarAlphaTemp;
            this.mAnimChecked = this.mAnimCheckedTemp;
            this.mParamCached = false;
            this.mSliderOffsetTemp = -1;
            this.mSliderOnAlphaTemp = -1;
            this.mMaskCheckedSlideBarAlphaTemp = -1.0f;
        }
    }

    private void saveCurrentParams() {
        this.mSliderOffsetTemp = this.mSliderOffset;
        this.mSliderOnAlphaTemp = this.mSliderOnAlpha;
        this.mMaskCheckedSlideBarAlphaTemp = this.mMaskCheckedSlideBarAlpha;
        this.mAnimCheckedTemp = this.mAnimChecked;
        this.mParamCached = true;
    }

    private void startCheckedChangeAnimInternal(boolean z) {
        SpringAnimation springAnimation = this.mSliderMoveAnim;
        if (springAnimation == null || !springAnimation.isRunning()) {
            this.mSliderOffset = this.mAnimChecked ? this.mSliderPositionEnd : this.mSliderPositionStart;
            this.mSliderOnAlpha = this.mAnimChecked ? 255 : 0;
        }
        popSavedParams();
        setCheckedInner(z);
    }

    private void updateCheckedAnim(boolean z) {
        if (this.mMarkedAlphaHideAnim.isRunning()) {
            this.mMarkedAlphaHideAnim.cancel();
        }
        if (!this.mMarkedAlphaShowAnim.isRunning() && !z) {
            this.mMaskCheckedSlideBarAlpha = 1.0f;
        }
    }

    private void updateUnCheckedAnim(boolean z) {
        if (this.mMarkedAlphaShowAnim.isRunning()) {
            this.mMarkedAlphaShowAnim.cancel();
        }
        if (!this.mMarkedAlphaHideAnim.isRunning() && z) {
            this.mMaskCheckedSlideBarAlpha = 0.0f;
        }
    }

    public void addState(StateListDrawable stateListDrawable, int[] iArr, Drawable drawable, int[] iArr2, Drawable drawable2, int i) {
    }

    public void animateToState(boolean z, int i, final Runnable runnable) {
        SpringAnimation springAnimation;
        SpringAnimation springAnimation2 = this.mSliderMoveAnim;
        if (springAnimation2 != null && springAnimation2.isRunning()) {
            this.mSliderMoveAnim.cancel();
        }
        if (z == this.mView.isChecked()) {
            this.mSliderMoveAnim = new SpringAnimation(this, this.mSliderOffsetProperty, (float) i);
            this.mSliderMoveAnim.getSpring().setStiffness(986.96f);
            this.mSliderMoveAnim.getSpring().setDampingRatio(0.7f);
            this.mSliderMoveAnim.addUpdateListener(this.mInvalidateUpdateListener);
            this.mSliderMoveAnim.addEndListener(new OnAnimationEndListener() {
                public void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
                    runnable.run();
                }
            });
            this.mSliderMoveAnim.start();
            if (z) {
                if (!this.mMarkedAlphaShowAnim.isRunning()) {
                    this.mMarkedAlphaShowAnim.start();
                }
                if (this.mMarkedAlphaHideAnim.isRunning()) {
                    springAnimation = this.mMarkedAlphaHideAnim;
                }
            }
            if (!this.mMarkedAlphaHideAnim.isRunning()) {
                this.mMarkedAlphaHideAnim.start();
            }
            if (this.mMarkedAlphaShowAnim.isRunning()) {
                springAnimation = this.mMarkedAlphaShowAnim;
            }
            springAnimation.cancel();
        }
    }

    public float getAlpha() {
        return this.mExtraAlpha;
    }

    public float getMaskCheckedSlideBarAlpha() {
        return this.mMaskCheckedSlideBarAlpha;
    }

    public float getMaskUnCheckedPressedSlideBarAlpha() {
        return this.mMaskUnCheckedPressedSlideBarAlpha;
    }

    public int getMeasuredHeight() {
        return this.mHeight;
    }

    public int getMeasuredWidth() {
        return this.mWidth;
    }

    public float getSliderOffset() {
        return (float) this.mSliderOffset;
    }

    public int getSliderOnAlpha() {
        return this.mSliderOnAlpha;
    }

    public float getSliderScale() {
        return this.mSliderScale;
    }

    public float getSliderShadowAlpha() {
        return this.mSliderShadowAlpha;
    }

    public float getStrokeAlpha() {
        return this.mStrokeAlpha;
    }

    public void initMaskedSlideBar(Drawable drawable, Drawable drawable2, Drawable drawable3) {
        this.mMaskCheckedSlideBar = drawable;
        this.mMaskUnCheckedSlideBar = drawable2;
        this.mMaskUnCheckedPressedSlideBar = drawable3;
    }

    public void initResource(Context context, TypedArray typedArray) {
        this.mCornerRadius = this.mView.getResources().getDimensionPixelSize(R.dimen.sliding_button_frame_corner_radius);
        this.mMarginVertical = this.mView.getResources().getDimensionPixelSize(R.dimen.sliding_button_frame_vertical_padding);
        this.mView.setDrawingCacheEnabled(false);
        this.mTapThreshold = ViewConfiguration.get(context).getScaledTouchSlop() / 2;
        this.mSliderOn = typedArray.getDrawable(R.styleable.SlidingButton_sliderOn);
        this.mSliderOff = typedArray.getDrawable(R.styleable.SlidingButton_sliderOff);
        this.mView.setBackground(typedArray.getDrawable(R.styleable.SlidingButton_android_background));
        int dimensionPixelSize = this.mView.getResources().getDimensionPixelSize(R.dimen.sliding_button_height);
        this.mWidth = this.mView.getResources().getDimensionPixelSize(R.dimen.sliding_button_width);
        this.mHeight = (this.mMarginVertical * 2) + dimensionPixelSize;
        this.mSliderWidth = Math.min(this.mWidth, this.mSliderOn.getIntrinsicWidth());
        this.mSliderHeight = Math.min(this.mHeight, this.mSliderOn.getIntrinsicHeight());
        this.mSliderPositionStart = 0;
        this.mSliderPositionEnd = this.mWidth - this.mSliderWidth;
        this.mSliderOffset = this.mSliderPositionStart;
        TypedValue typedValue = new TypedValue();
        typedArray.getValue(R.styleable.SlidingButton_barOff, typedValue);
        TypedValue typedValue2 = new TypedValue();
        typedArray.getValue(R.styleable.SlidingButton_barOn, typedValue2);
        Drawable drawable = typedArray.getDrawable(R.styleable.SlidingButton_barOff);
        Drawable drawable2 = typedArray.getDrawable(R.styleable.SlidingButton_barOn);
        if (typedValue.type == typedValue2.type && typedValue.data == typedValue2.data && typedValue.resourceId == typedValue2.resourceId) {
            drawable2 = drawable;
        }
        if (!(drawable2 == null || drawable == null)) {
            initMaskedSlideBar(createMaskDrawable(drawable2), createMaskDrawable(drawable), createMaskDrawable(drawable2));
            this.mSlideBar = createMaskedSlideBar();
        }
        setSliderDrawState();
        if (this.mView.isChecked()) {
            setSliderOffset(this.mSliderPositionEnd);
        }
    }

    public void jumpDrawablesToCurrentState() {
        StateListDrawable stateListDrawable = this.mSlideBar;
        if (stateListDrawable != null) {
            stateListDrawable.jumpToCurrentState();
        }
    }

    public void notifyCheckedChangeListener() {
        if (this.mOnPerformCheckedChangeListener != null) {
            this.mOnPerformCheckedChangeListener.onCheckedChanged(this.mView, this.mView.isChecked());
        }
    }

    public void onDraw(Canvas canvas) {
        Drawable drawable;
        int i = (int) (((float) (this.mView.isEnabled() ? 255 : 127)) * this.mExtraAlpha);
        float f = ((float) i) / 255.0f;
        onDrawSlideBar(canvas, f, this.mSlideBar);
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this.mView);
        int i2 = isLayoutRtl ? (this.mWidth - this.mSliderOffset) - this.mSliderWidth : this.mSliderOffset;
        int i3 = isLayoutRtl ? this.mWidth - this.mSliderOffset : this.mSliderWidth + this.mSliderOffset;
        int i4 = this.mHeight;
        int i5 = this.mSliderHeight;
        int i6 = (i4 - i5) / 2;
        int i7 = i6 + i5;
        int i8 = (i3 + i2) / 2;
        int i9 = (i7 + i6) / 2;
        onDrawSliderShadow(canvas, i8, i9);
        scaleCanvasStart(canvas, i8, i9);
        if (this.mAnimChecked) {
            this.mSliderOn.setAlpha(i);
            this.mSliderOn.setBounds(i2, i6, i3, i7);
            drawable = this.mSliderOn;
        } else {
            this.mSliderOff.setAlpha(i);
            this.mSliderOff.setBounds(i2, i6, i3, i7);
            drawable = this.mSliderOff;
        }
        drawable.draw(canvas);
        onDrawSliderStroke(canvas, f, i2, i6, i3, i7);
        scaleCanvasEnd(canvas);
    }

    public void onDrawSlideBar(Canvas canvas, float f, Drawable drawable) {
        int i = (int) (f * 255.0f);
        if (i > 0) {
            this.mMaskUnCheckedSlideBar.setAlpha(i);
            this.mMaskUnCheckedSlideBar.draw(canvas);
        }
        int i2 = (int) (this.mMaskUnCheckedPressedSlideBarAlpha * 255.0f * f);
        if (i2 > 0) {
            this.mMaskUnCheckedPressedSlideBar.setAlpha(i2);
            this.mMaskUnCheckedPressedSlideBar.draw(canvas);
        }
        int i3 = (int) (this.mMaskCheckedSlideBarAlpha * 255.0f * f);
        if (i3 > 0) {
            this.mMaskCheckedSlideBar.setAlpha(i3);
            this.mMaskCheckedSlideBar.draw(canvas);
        }
    }

    public void onDrawSliderShadow(Canvas canvas, int i, int i2) {
        int i3;
        int i4;
        int i5 = (int) (this.mSliderShadowAlpha * 255.0f);
        if (i5 != 0) {
            Drawable drawable = this.mSliderShadow;
            if (drawable instanceof BitmapDrawable) {
                i4 = ((BitmapDrawable) drawable).getBitmap().getWidth();
                i3 = ((BitmapDrawable) this.mSliderShadow).getBitmap().getHeight();
            } else {
                i4 = drawable.getIntrinsicWidth();
                i3 = this.mSliderShadow.getIntrinsicHeight();
            }
            int i6 = i4 / 2;
            int i7 = i3 / 2;
            this.mSliderShadow.setBounds(i - i6, i2 - i7, i + i6, i2 + i7);
            this.mSliderShadow.setAlpha(i5);
            this.mSliderShadow.draw(canvas);
        }
    }

    public void onDrawSliderStroke(Canvas canvas, float f, int i, int i2, int i3, int i4) {
        this.mSliderStroke.setAlpha((int) (this.mStrokeAlpha * 255.0f * f));
        this.mSliderStroke.setBounds(i, i2, i3, i4);
        this.mSliderStroke.draw(canvas);
    }

    public void onPressedInner() {
        if (this.mSliderUnPressedAnim.isRunning()) {
            this.mSliderUnPressedAnim.cancel();
        }
        if (!this.mSliderPressedAnim.isRunning()) {
            this.mSliderPressedAnim.start();
        }
        if (!this.mSliderShadowShowAnim.isRunning()) {
            this.mSliderShadowShowAnim.start();
        }
        if (!this.mView.isChecked()) {
            if (this.mUnMarkedPressedAlphaHideAnim.isRunning()) {
                this.mUnMarkedPressedAlphaHideAnim.cancel();
            }
            if (!this.mUnMarkedPressedAlphaShowAnim.isRunning()) {
                this.mUnMarkedPressedAlphaShowAnim.start();
            }
            if (!this.mStokeAlphaShowAnim.isRunning()) {
                this.mStokeAlphaShowAnim.start();
            }
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        Rect rect = this.mTmpRect;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this.mView);
        rect.set(isLayoutRtl ? (this.mWidth - this.mSliderOffset) - this.mSliderWidth : this.mSliderOffset, 0, isLayoutRtl ? this.mWidth - this.mSliderOffset : this.mSliderOffset + this.mSliderWidth, this.mHeight);
        if (action != 0) {
            if (action == 1) {
                onUnPressedInner();
                onTouchEventUp(x, y, rect);
            } else if (action == 2) {
                onTouchEventMove(x);
                return;
            } else if (action == 3) {
                onUnPressedInner();
            } else {
                return;
            }
            this.mTracking = false;
            this.mSliderMoved = false;
            this.mView.setPressed(false);
            return;
        }
        onTouchEventDown(x, y, rect);
    }

    public void onUnPressedInner() {
        if (this.mSliderPressedAnim.isRunning()) {
            this.mSliderPressedAnim.cancel();
        }
        if (!this.mSliderUnPressedAnim.isRunning()) {
            this.mSliderUnPressedAnim.start();
        }
        if (this.mSliderShadowShowAnim.isRunning()) {
            this.mSliderShadowShowAnim.cancel();
        }
        if (!this.mSliderShadowHideAnim.isRunning()) {
            this.mSliderShadowHideAnim.start();
        }
        if (this.mStokeAlphaShowAnim.isRunning()) {
            this.mStokeAlphaShowAnim.cancel();
        }
        if (!this.mView.isChecked()) {
            if (this.mUnMarkedPressedAlphaShowAnim.isRunning()) {
                this.mUnMarkedPressedAlphaShowAnim.cancel();
            }
            if (!this.mUnMarkedPressedAlphaHideAnim.isRunning()) {
                this.mUnMarkedPressedAlphaHideAnim.start();
            }
            if (!this.mStokeAlphaHideAnim.isRunning()) {
                this.mStokeAlphaHideAnim.start();
            }
        }
    }

    public void scaleCanvasEnd(Canvas canvas) {
        canvas.restore();
    }

    public void scaleCanvasStart(Canvas canvas, int i, int i2) {
        canvas.save();
        float f = this.mSliderScale;
        canvas.scale(f, f, (float) i, (float) i2);
    }

    public void setAlpha(float f) {
        this.mExtraAlpha = f;
    }

    public void setChecked(boolean z) {
        saveCurrentParams();
        this.mAnimChecked = z;
        this.mSliderOffset = z ? this.mSliderPositionEnd : this.mSliderPositionStart;
        this.mSliderOnAlpha = z ? 255 : 0;
        this.mMaskCheckedSlideBarAlpha = z ? 1.0f : 0.0f;
        SpringAnimation springAnimation = this.mSliderMoveAnim;
        if (springAnimation != null && springAnimation.isRunning()) {
            this.mSliderMoveAnim.cancel();
        }
        if (this.mMarkedAlphaHideAnim.isRunning()) {
            this.mMarkedAlphaHideAnim.cancel();
        }
        if (this.mMarkedAlphaShowAnim.isRunning()) {
            this.mMarkedAlphaShowAnim.cancel();
        }
        this.mView.invalidate();
    }

    public void setCheckedInner(boolean z) {
        if (this.mAnimChecked) {
            updateCheckedAnim(z);
        } else {
            updateUnCheckedAnim(z);
        }
    }

    public void setMaskCheckedSlideBarAlpha(float f) {
        this.mMaskCheckedSlideBarAlpha = f;
    }

    public void setMaskUnCheckedPressedSlideBarAlpha(float f) {
        this.mMaskUnCheckedPressedSlideBarAlpha = f;
    }

    public void setOnPerformCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.mOnPerformCheckedChangeListener = onCheckedChangeListener;
    }

    public void setParentClipChildren() {
        ViewParent parent = this.mView.getParent();
        if (parent != null && (parent instanceof ViewGroup)) {
            ((ViewGroup) parent).setClipChildren(false);
        }
    }

    public void setSliderDrawState() {
        Drawable drawable = this.mSliderOn;
        if (drawable != null) {
            drawable.setState(this.mView.getDrawableState());
            this.mSlideBar.setState(this.mView.getDrawableState());
        }
    }

    public void setSliderOffset(int i) {
        this.mSliderOffset = i;
        this.mView.invalidate();
    }

    public void setSliderOnAlpha(int i) {
        this.mSliderOnAlpha = i;
        this.mView.invalidate();
    }

    public void setSliderScale(float f) {
        this.mSliderScale = f;
    }

    public void setSliderShadowAlpha(float f) {
        this.mSliderShadowAlpha = f;
    }

    public void setStrokeAlpha(float f) {
        this.mStrokeAlpha = f;
    }

    public boolean verifyDrawable(Drawable drawable) {
        return drawable == this.mSlideBar;
    }
}
