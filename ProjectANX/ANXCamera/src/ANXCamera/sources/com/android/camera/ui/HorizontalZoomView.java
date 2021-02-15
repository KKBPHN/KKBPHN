package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Spline;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.manually.adapter.AbstractZoomSliderAdapter;
import com.android.camera.log.Log;
import com.android.camera.ui.BaseHorizontalZoomView.HorizontalDrawAdapter;
import com.android.camera.ui.BaseHorizontalZoomView.OnPositionSelectListener;
import com.android.camera.ui.BaseHorizontalZoomView.onTouchUpStateListener;
import com.xiaomi.camera.util.SystemProperties;
import miui.view.animation.CubicEaseOutInterpolator;

public class HorizontalZoomView extends BaseHorizontalZoomView {
    private static final int DEFAULT_TIME = 200;
    private static final int DIS_USED = -100;
    public static final int NONE = -1;
    private static final String TAG = "HorizontalZoomView";
    private static final int TOUCH_SCROLL_THRESHOLD = 10;
    private float mCenterYBottomMargin;
    /* access modifiers changed from: private */
    public int mCurrentAnimateFrom;
    /* access modifiers changed from: private */
    public float mCurrentInterpolation;
    protected HorizontalDrawAdapter mDrawAdapter;
    protected float mDrawEndX;
    protected float mDrawStartX;
    private float mInitPositionRatio;
    private int mInitSelectIndex;
    /* access modifiers changed from: private */
    public boolean mIsAdsorb;
    private boolean mIsCurrentAnimateFromDown;
    private boolean mIsEffectInProcess;
    private boolean mIsRLT;
    protected float mItemGap;
    protected float mItemHalfWidth;
    protected OnPositionSelectListener mOnPositionZoomSelectListener;
    /* access modifiers changed from: private */
    public Spline mPositiveSpline;
    private ValueAnimator mScrollAnimator;
    protected float mSelectPointX;
    private Spline mSpline;
    protected float mTotalWidth;
    private float mTouchStartTime;
    private float mTouchStartX;
    private float mTouchStartY;
    private int mTouchState;
    protected onTouchUpStateListener mTouchUpStateListener;
    private float mTouchX;
    private ValueAnimator mValueAnimator;

    public HorizontalZoomView(Context context) {
        this(context, null, -1);
    }

    public HorizontalZoomView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public HorizontalZoomView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDrawStartX = -1.0f;
        this.mDrawEndX = -1.0f;
        this.mInitSelectIndex = -100;
        this.mInitPositionRatio = -100.0f;
        this.mTouchState = 0;
        this.mCurrentAnimateFrom = 0;
        this.mIsAdsorb = true;
        this.mCurrentInterpolation = 1.0f;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.HorizontalView);
        this.mCenterYBottomMargin = obtainStyledAttributes.getDimension(0, 100.0f);
        obtainStyledAttributes.recycle();
        init(context);
    }

    private float indexToPointX(int i) {
        return (((float) i) * this.mItemGap) + ((float) getPaddingLeft()) + this.mItemHalfWidth;
    }

    private boolean isValidEvent(float f) {
        return f >= (((float) getHeight()) - (this.mCenterYBottomMargin * 2.0f)) / 2.0f;
    }

    private float mapIndexToValue(int i) {
        return Float.parseFloat((String) ((AbstractZoomSliderAdapter) this.mDrawAdapter).mapPositionToValue((float) i));
    }

    private float mapPositionToValue(float f) {
        float paddingLeft = (f - this.mItemHalfWidth) - ((float) getPaddingLeft());
        float f2 = this.mTotalWidth;
        float f3 = this.mItemHalfWidth;
        float clamp = Util.clamp(paddingLeft / ((f2 - f3) - f3), 0.0f, 1.0f);
        if (this.mIsRLT) {
            clamp = 1.0f - clamp;
        }
        return Float.parseFloat((String) ((AbstractZoomSliderAdapter) this.mDrawAdapter).mapPositionToValue(clamp * ((float) (this.mDrawAdapter.getCount() - 1))));
    }

    private int pointXToIndex(float f) {
        return Util.clamp(Math.round((((f - this.mItemHalfWidth) - ((float) getPaddingLeft())) / (this.mTotalWidth - (this.mItemHalfWidth * 2.0f))) * ((float) (this.mDrawAdapter.getCount() - 1))), 0, this.mDrawAdapter.getCount() - 1);
    }

    private float positionRatioToPointX(float f) {
        float f2 = this.mTotalWidth;
        float f3 = this.mItemHalfWidth;
        int pointXToIndex = pointXToIndex(((f2 - (f3 * 2.0f)) * f) + f3 + ((float) getPaddingLeft()));
        if (this.mDrawAdapter.isSingleValueLine(pointXToIndex)) {
            if (this.mIsRLT) {
                pointXToIndex = (this.mDrawAdapter.getCount() - 1) - pointXToIndex;
            }
            return indexToPointX(pointXToIndex);
        }
        if (this.mIsRLT) {
            f = 1.0f - f;
        }
        float f4 = this.mTotalWidth;
        float f5 = this.mItemHalfWidth;
        return ((float) getPaddingLeft()) + (f * (f4 - (2.0f * f5))) + f5;
    }

    private void selectByIndex(int i) {
        OnPositionSelectListener onPositionSelectListener = this.mOnPositionZoomSelectListener;
        if (onPositionSelectListener != null) {
            onPositionSelectListener.onPositionSelect(this, i, -1.0f);
        }
    }

    private void selectByPointX(float f) {
        float paddingLeft = (f - this.mItemHalfWidth) - ((float) getPaddingLeft());
        float f2 = this.mTotalWidth;
        float f3 = this.mItemHalfWidth;
        float clamp = Util.clamp(paddingLeft / ((f2 - f3) - f3), 0.0f, 1.0f);
        if (this.mIsRLT) {
            clamp = 1.0f - clamp;
        }
        OnPositionSelectListener onPositionSelectListener = this.mOnPositionZoomSelectListener;
        if (onPositionSelectListener != null) {
            onPositionSelectListener.onPositionSelect(this, -1, clamp);
        }
    }

    private void setSelectPointXToEffectiveRang(float f) {
        float f2 = this.mDrawStartX;
        float f3 = this.mItemHalfWidth;
        this.mSelectPointX = Util.clamp(f, f2 + f3, this.mDrawEndX - f3);
    }

    private void startScaleAnimator(final boolean z) {
        final int i = this.mTouchState;
        if (i == 2 && (i != this.mCurrentAnimateFrom || (this.mCurrentInterpolation != (z ? 1.0f : 0.0f) && this.mIsCurrentAnimateFromDown != z))) {
            ValueAnimator valueAnimator = this.mScrollAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mScrollAnimator.cancel();
            }
            this.mCurrentAnimateFrom = i;
            this.mIsCurrentAnimateFromDown = z;
            float[] fArr = new float[2];
            if (z) {
                // fill-array-data instruction
                fArr[0] = 0;
                fArr[1] = 1065353216;
            } else {
                // fill-array-data instruction
                fArr[0] = 1065353216;
                fArr[1] = 0;
            }
            this.mScrollAnimator = ValueAnimator.ofFloat(fArr);
            this.mScrollAnimator.setDuration(z ? 200 : 400).setInterpolator(new CubicEaseOutInterpolator());
            this.mScrollAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    HorizontalZoomView.this.mCurrentInterpolation = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    HorizontalZoomView.this.mIsAdsorb = !z;
                    HorizontalZoomView.this.invalidate();
                }
            });
            this.mScrollAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationCancel(Animator animator) {
                    HorizontalZoomView.this.mCurrentAnimateFrom = i;
                    HorizontalZoomView.this.mCurrentInterpolation = z ? 1.0f : 0.0f;
                    HorizontalZoomView.this.mIsAdsorb = !z;
                    HorizontalZoomView.this.invalidate();
                }
            });
            this.mScrollAnimator.start();
        }
    }

    private void startScrollAnimator(final float f, int i) {
        if (this.mSelectPointX == -1.0f && this.mInitSelectIndex == -1) {
            this.mSelectPointX = f;
            this.mIsAdsorb = true;
            if (this.mIsEffectInProcess) {
                selectByPointX(f);
            }
            invalidate();
            return;
        }
        ValueAnimator valueAnimator = this.mScrollAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mScrollAnimator.cancel();
        }
        this.mScrollAnimator = ValueAnimator.ofFloat(new float[]{this.mSelectPointX, f});
        this.mScrollAnimator.setDuration((long) i).setInterpolator(this.mIsEffectInProcess ? new LinearInterpolator() : new CubicEaseOutInterpolator());
        this.mScrollAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                HorizontalZoomView.this.mSelectPointX = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                HorizontalZoomView.this.mIsAdsorb = false;
                HorizontalZoomView.this.invalidate();
            }
        });
        this.mScrollAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                HorizontalZoomView horizontalZoomView = HorizontalZoomView.this;
                horizontalZoomView.mSelectPointX = f;
                horizontalZoomView.mIsAdsorb = true;
                HorizontalZoomView.this.invalidate();
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                HorizontalZoomView horizontalZoomView = HorizontalZoomView.this;
                horizontalZoomView.mSelectPointX = f;
                horizontalZoomView.mIsAdsorb = true;
                HorizontalZoomView.this.invalidate();
            }
        });
        this.mScrollAnimator.start();
    }

    private void startScrollAnimatorByIndex(int i) {
        int i2;
        boolean z;
        float f;
        float f2;
        if (!this.mIsEffectInProcess) {
            selectByIndex(i);
            i2 = 200;
        } else {
            float mapPositionToValue = mapPositionToValue(this.mSelectPointX);
            float mapIndexToValue = mapIndexToValue(i);
            float interpolate = this.mSpline.interpolate(mapPositionToValue);
            float interpolate2 = this.mSpline.interpolate(mapIndexToValue);
            int abs = Math.abs(Math.round(interpolate2 - interpolate));
            int i3 = SystemProperties.getInt("animator_duration", 0);
            if (i3 != 0) {
                f2 = mapPositionToValue;
                i2 = i3;
                z = false;
                f = mapIndexToValue;
            } else {
                z = true;
                f = interpolate2;
                i2 = abs;
                f2 = interpolate;
            }
            startValueAnimator(i2, f2, f, mapIndexToValue, z);
        }
        if (this.mIsRLT) {
            i = (this.mDrawAdapter.getCount() - 1) - i;
        }
        startScrollAnimator(indexToPointX(i), i2);
    }

    private boolean startScrollIfNeeded(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        float f = (float) x;
        float f2 = this.mTouchStartX;
        if (f >= f2 - 10.0f && f <= f2 + 10.0f) {
            float f3 = (float) y;
            float f4 = this.mTouchStartY;
            if (f3 >= f4 - 10.0f && f3 <= f4 + 10.0f) {
                return false;
            }
        }
        this.mTouchState = 2;
        startScaleAnimator(true);
        return true;
    }

    private void startValueAnimator(int i, float f, float f2, final float f3, final boolean z) {
        ValueAnimator valueAnimator = this.mValueAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mValueAnimator.cancel();
        }
        this.mValueAnimator = ValueAnimator.ofFloat(new float[]{f, f2});
        this.mValueAnimator.setDuration((long) i).setInterpolator(new LinearInterpolator());
        this.mValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                if (z) {
                    floatValue = HorizontalZoomView.this.mPositiveSpline.interpolate(floatValue);
                }
                String valueOf = String.valueOf(floatValue);
                OnPositionSelectListener onPositionSelectListener = HorizontalZoomView.this.mOnPositionZoomSelectListener;
                if (onPositionSelectListener != null) {
                    onPositionSelectListener.onChangeValue(valueOf);
                }
            }
        });
        this.mValueAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationCancel(Animator animator) {
                super.onAnimationCancel(animator);
                OnPositionSelectListener onPositionSelectListener = HorizontalZoomView.this.mOnPositionZoomSelectListener;
                if (onPositionSelectListener != null) {
                    onPositionSelectListener.onChangeValue(String.valueOf(f3));
                }
                onTouchUpStateListener ontouchupstatelistener = HorizontalZoomView.this.mTouchUpStateListener;
                if (ontouchupstatelistener != null) {
                    ontouchupstatelistener.onTouchUpState();
                    HorizontalZoomView.this.mTouchUpStateListener.onScrollEnd();
                }
            }

            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                OnPositionSelectListener onPositionSelectListener = HorizontalZoomView.this.mOnPositionZoomSelectListener;
                if (onPositionSelectListener != null) {
                    onPositionSelectListener.onChangeValue(String.valueOf(f3));
                }
                onTouchUpStateListener ontouchupstatelistener = HorizontalZoomView.this.mTouchUpStateListener;
                if (ontouchupstatelistener != null) {
                    ontouchupstatelistener.onTouchUpState();
                    HorizontalZoomView.this.mTouchUpStateListener.onScrollEnd();
                }
            }

            public void onAnimationStart(Animator animator) {
                onTouchUpStateListener ontouchupstatelistener = HorizontalZoomView.this.mTouchUpStateListener;
                if (ontouchupstatelistener != null) {
                    ontouchupstatelistener.onScrollStart(f3);
                }
            }
        });
        this.mValueAnimator.start();
    }

    private void toUpdateView(float f, boolean z) {
        int i;
        boolean z2;
        float f2;
        float f3;
        int pointXToIndex = pointXToIndex(f);
        if (this.mIsRLT) {
            pointXToIndex = (this.mDrawAdapter.getCount() - 1) - pointXToIndex;
        }
        boolean isSingleValueLine = this.mDrawAdapter.isSingleValueLine(pointXToIndex);
        if (z) {
            if (isSingleValueLine) {
                selectByIndex(pointXToIndex);
                this.mSelectPointX = indexToPointX(pointXToIndex(f));
            } else {
                selectByPointX(f);
            }
            startScaleAnimator(false);
            onTouchUpStateListener ontouchupstatelistener = this.mTouchUpStateListener;
            if (ontouchupstatelistener != null && this.mIsEffectInProcess) {
                ontouchupstatelistener.onTouchUpState();
                return;
            }
            return;
        }
        if (!this.mIsEffectInProcess) {
            if (isSingleValueLine) {
                selectByIndex(pointXToIndex);
            } else {
                selectByPointX(f);
            }
            i = 200;
        } else {
            float mapPositionToValue = mapPositionToValue(this.mSelectPointX);
            float mapIndexToValue = isSingleValueLine ? mapIndexToValue(pointXToIndex) : mapPositionToValue(f);
            float interpolate = this.mSpline.interpolate(mapPositionToValue);
            float interpolate2 = this.mSpline.interpolate(mapIndexToValue);
            int abs = Math.abs(Math.round(interpolate2 - interpolate));
            int i2 = SystemProperties.getInt("animator_duration", 0);
            if (i2 != 0) {
                f3 = mapPositionToValue;
                z2 = false;
                i = i2;
                f2 = mapIndexToValue;
            } else {
                f3 = interpolate;
                f2 = interpolate2;
                z2 = true;
                i = abs;
            }
            startValueAnimator(i, f3, f2, mapIndexToValue, z2);
        }
        startScrollAnimator(indexToPointX(pointXToIndex(f)), i);
    }

    public HorizontalDrawAdapter getDrawAdapter() {
        return this.mDrawAdapter;
    }

    /* access modifiers changed from: protected */
    public float getItemWidth(int i) {
        return this.mDrawAdapter.measureWidth(i);
    }

    /* access modifiers changed from: protected */
    public void init(Context context) {
        this.mIsRLT = Util.isLayoutRTL(getContext());
    }

    public boolean isIdle() {
        return this.mTouchState == 0;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        int i;
        boolean z;
        Canvas canvas2 = canvas;
        if (this.mSelectPointX != -1.0f && this.mInitSelectIndex == -1) {
            this.mInitSelectIndex = -100;
        }
        float height = ((float) getHeight()) - this.mCenterYBottomMargin;
        HorizontalDrawAdapter horizontalDrawAdapter = this.mDrawAdapter;
        if (horizontalDrawAdapter != null) {
            if (this.mItemGap == 0.0f) {
                this.mItemHalfWidth = horizontalDrawAdapter.measureWidth(0) / 2.0f;
                float f = this.mTotalWidth;
                float f2 = this.mItemHalfWidth;
                this.mItemGap = ((f - f2) - f2) / ((float) (this.mDrawAdapter.getCount() - 1));
            }
            this.mDrawAdapter.setItemGap(this.mItemGap);
            boolean z2 = this.mIsRLT;
            int count = z2 ? this.mDrawAdapter.getCount() - 1 : 0;
            int count2 = z2 ? 0 : this.mDrawAdapter.getCount() - 1;
            int i2 = z2 ? -1 : 1;
            float f3 = this.mDrawStartX + this.mItemHalfWidth;
            if (this.mInitSelectIndex == -100 || this.mDrawAdapter == null) {
                float f4 = this.mInitPositionRatio;
                if (f4 != -100.0f) {
                    this.mSelectPointX = positionRatioToPointX(f4);
                    this.mInitPositionRatio = -100.0f;
                }
                setSelectPointXToEffectiveRang(this.mSelectPointX);
                if (this.mDrawAdapter != null) {
                    boolean z3 = true;
                    int i3 = 0;
                    float f5 = f3;
                    int i4 = -1;
                    while (i3 < this.mDrawAdapter.getCount()) {
                        int i5 = (i3 * i2) + count;
                        float itemWidth = getItemWidth(i5);
                        float f6 = this.mSelectPointX;
                        float f7 = f5 - f6;
                        if (z3) {
                            float f8 = this.mDrawStartX;
                            float f9 = this.mItemHalfWidth;
                            if (f6 <= f8 + f9) {
                                i = count;
                            } else if (f6 >= this.mDrawEndX - f9) {
                                i = count2;
                            } else if (Math.abs(f7) <= itemWidth / 2.0f || (this.mIsAdsorb && Math.abs(f7) <= this.mItemGap / 2.0f)) {
                                i = i5;
                            }
                            z = false;
                            canvas.save();
                            canvas2.translate(f5, height);
                            HorizontalDrawAdapter horizontalDrawAdapter2 = this.mDrawAdapter;
                            boolean z4 = z && i == i5;
                            horizontalDrawAdapter2.draw(i5, canvas, z4, this.mCurrentAnimateFrom, this.mCurrentInterpolation);
                            canvas.restore();
                            f5 += this.mItemGap;
                            i3++;
                            z3 = z;
                            i4 = i;
                        }
                        i = i4;
                        z = z3;
                        canvas.save();
                        canvas2.translate(f5, height);
                        HorizontalDrawAdapter horizontalDrawAdapter22 = this.mDrawAdapter;
                        if (z) {
                        }
                        horizontalDrawAdapter22.draw(i5, canvas, z4, this.mCurrentAnimateFrom, this.mCurrentInterpolation);
                        canvas.restore();
                        f5 += this.mItemGap;
                        i3++;
                        z3 = z;
                        i4 = i;
                    }
                    if (z3) {
                        canvas.save();
                        canvas2.translate(this.mSelectPointX, height);
                        this.mDrawAdapter.draw(-1, canvas, z3, this.mCurrentAnimateFrom, this.mCurrentInterpolation);
                        canvas.restore();
                    }
                }
                return;
            }
            float f10 = f3;
            for (int i6 = 0; i6 < this.mDrawAdapter.getCount(); i6++) {
                int i7 = count + (i6 * i2);
                canvas.save();
                canvas2.translate(f10, height);
                this.mDrawAdapter.draw(i7, canvas, this.mInitSelectIndex == i7, this.mCurrentAnimateFrom, this.mCurrentInterpolation);
                canvas.restore();
                f10 += this.mItemGap;
            }
            int i8 = this.mInitSelectIndex;
            if (i8 != -1) {
                if (this.mIsRLT) {
                    i8 = (this.mDrawAdapter.getCount() - 1) - this.mInitSelectIndex;
                }
                this.mSelectPointX = indexToPointX(i8);
                this.mInitSelectIndex = -100;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (MeasureSpec.getMode(i) != 1073741824) {
            super.onMeasure(i, i2);
            return;
        }
        int size = MeasureSpec.getSize(i);
        setMeasuredDimension(size, MeasureSpec.getSize(i2));
        this.mDrawStartX = (float) getPaddingLeft();
        this.mDrawEndX = (float) (size - getPaddingRight());
        this.mTotalWidth = (float) ((size - getPaddingLeft()) - getPaddingRight());
        this.mItemGap = 0.0f;
        this.mIsAdsorb = true;
        HorizontalDrawAdapter horizontalDrawAdapter = this.mDrawAdapter;
        if (horizontalDrawAdapter != null) {
            horizontalDrawAdapter.updateSelectColor();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0015, code lost:
        if (r0 != 3) goto L_0x00b2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!super.onTouchEvent(motionEvent)) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (this.mTouchState == 1) {
                        startScrollIfNeeded(motionEvent);
                    }
                    if (this.mTouchState == 2) {
                        float x = (this.mSelectPointX == -1.0f && this.mInitSelectIndex == -1) ? motionEvent.getX() : this.mSelectPointX + (motionEvent.getX() - this.mTouchX);
                        this.mSelectPointX = x;
                        selectByPointX(this.mSelectPointX);
                        this.mIsAdsorb = false;
                        invalidate();
                    }
                }
            }
            if (((float) SystemClock.elapsedRealtime()) - this.mTouchStartTime <= ((float) ViewConfiguration.getLongPressTimeout()) || this.mTouchState == 2) {
                if (this.mTouchState == 2) {
                    this.mSelectPointX += motionEvent.getX() - this.mTouchX;
                    toUpdateView(this.mSelectPointX, true);
                } else {
                    toUpdateView(motionEvent.getX(), false);
                }
                onTouchUpStateListener ontouchupstatelistener = this.mTouchUpStateListener;
                if (ontouchupstatelistener != null && !this.mIsEffectInProcess) {
                    ontouchupstatelistener.onTouchUpState();
                }
                this.mTouchState = 0;
            } else {
                this.mTouchState = 0;
                return true;
            }
        } else {
            this.mTouchStartTime = (float) SystemClock.elapsedRealtime();
            this.mTouchStartX = motionEvent.getX();
            this.mTouchStartY = motionEvent.getY();
            if (!isValidEvent(this.mTouchStartY)) {
                return false;
            }
            if (this.mTouchState != 2) {
                this.mTouchState = 1;
            }
        }
        this.mTouchX = motionEvent.getX();
        return true;
    }

    public void setDrawAdapter(HorizontalDrawAdapter horizontalDrawAdapter, int i, boolean z) {
        this.mDrawAdapter = horizontalDrawAdapter;
        this.mIsEffectInProcess = z;
        if (this.mIsEffectInProcess) {
            float[] fArr = DataRepository.dataItemGlobal().getCurrentMode() == 163 ? HybridZoomingSystem.SAT_ZOOM_RATIO_X : HybridZoomingSystem.VIDEO_SAT_ZOOM_RATIO_X;
            float[] fArr2 = DataRepository.dataItemGlobal().getCurrentMode() == 163 ? HybridZoomingSystem.SAT_ZOOM_RATIO_Y : HybridZoomingSystem.VIDEO_SAT_ZOOM_RATIO_Y;
            this.mSpline = Spline.createMonotoneCubicSpline(fArr, fArr2);
            this.mPositiveSpline = Spline.createMonotoneCubicSpline(fArr2, fArr);
        }
        setRotate(i);
        this.mItemGap = 0.0f;
        invalidate();
    }

    public void setListener(OnPositionSelectListener onPositionSelectListener, onTouchUpStateListener ontouchupstatelistener) {
        this.mOnPositionZoomSelectListener = onPositionSelectListener;
        this.mTouchUpStateListener = ontouchupstatelistener;
    }

    public void setRotate(int i) {
        HorizontalDrawAdapter horizontalDrawAdapter = this.mDrawAdapter;
        if (horizontalDrawAdapter != null) {
            horizontalDrawAdapter.setRotate(i);
            this.mIsAdsorb = true;
            invalidate();
        }
    }

    public void setSelection(float f) {
        float count = f / ((float) (this.mDrawAdapter.getCount() - 1));
        this.mDrawAdapter.setCurrentValue(null);
        if (this.mItemGap == 0.0f) {
            this.mInitPositionRatio = count;
        } else {
            this.mSelectPointX = positionRatioToPointX(count);
        }
        this.mInitSelectIndex = -100;
        this.mIsAdsorb = true;
        StringBuilder sb = new StringBuilder();
        sb.append("setSelection   ");
        sb.append(count);
        Log.d(TAG, sb.toString());
        invalidate();
    }

    public void setSelection(int i, boolean z) {
        HorizontalDrawAdapter horizontalDrawAdapter = this.mDrawAdapter;
        if (horizontalDrawAdapter != null) {
            if (i >= horizontalDrawAdapter.getCount()) {
                i = this.mDrawAdapter.getCount() - 1;
            }
            this.mDrawAdapter.setCurrentValue(null);
            if (z) {
                this.mInitSelectIndex = i;
                if (this.mInitSelectIndex == -1) {
                    this.mSelectPointX = -1.0f;
                }
                this.mIsAdsorb = true;
                invalidate();
                return;
            }
            startScrollAnimatorByIndex(i);
        }
    }

    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            ValueAnimator valueAnimator = this.mValueAnimator;
            if (valueAnimator != null && valueAnimator.isRunning()) {
                this.mValueAnimator.cancel();
            }
            ValueAnimator valueAnimator2 = this.mScrollAnimator;
            if (valueAnimator2 != null && valueAnimator2.isRunning()) {
                this.mScrollAnimator.cancel();
            }
            this.mTouchState = 0;
        }
    }
}
