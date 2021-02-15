package com.android.camera.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Range;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FolmeUtils.IPhyAnimatorListener;
import com.android.camera.animation.folme.FolmeAlphaToOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.mode.MoreModeHelper;
import com.android.camera.log.Log;
import com.android.camera.module.ModuleManager;
import io.reactivex.Completable;
import java.util.ArrayList;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

public class DragLayout extends FrameLayout {
    private static final int STATE_DRAGGING = 1;
    private static final int STATE_EXPEND = 4;
    private static final int STATE_IDLE = 0;
    private static final int STATE_SETTLING = 2;
    private static final int STATE_SHRINK = 3;
    public static final String TAG = "DragLayout";
    private static boolean debug;
    private static DragAnimationConfig sAnimationConfig;
    /* access modifiers changed from: private */
    public Bar mBar;
    private boolean mCatchDrag;
    private FrameLayout mChildren;
    /* access modifiers changed from: private */
    public boolean mCurDirectionIsUp;
    /* access modifiers changed from: private */
    public final Dependency mDependency;
    private boolean mDragEnable;
    private Interpolator mDragInterpolator;
    /* access modifiers changed from: private */
    public float mOffset;
    /* access modifiers changed from: private */
    public List mOnDragListeners;
    private int mScreenOrientation;
    private boolean mSkipDragUpDown;
    /* access modifiers changed from: private */
    public boolean mStartDirectionIsUp;
    private float mStartX;
    private float mStartY;
    /* access modifiers changed from: private */
    public int mState;
    private IPhyAnimatorListener mValueAnimatorListener;
    private VelocityTracker mVelocityTracker;

    public class Bar extends View {
        private static final int FLAT_DURATION = 600;
        private static final int FOLD_DURATION = 200;
        private static final int UI_STATE_FLAT = 0;
        private static final int UI_STATE_FOLD_DOWN = -1;
        private static final int UI_STATE_FOLD_UP = 1;
        /* access modifiers changed from: private */
        public boolean isUpAnimation;
        /* access modifiers changed from: private */
        public float mAlpha;
        private ValueAnimator mAnimator;
        public int mBgColor;
        /* access modifiers changed from: private */
        @UiState
        public int mCurUiState = 0;
        /* access modifiers changed from: private */
        public float mDegree;
        /* access modifiers changed from: private */
        public float mExtendDimen;
        private Interpolator mFlatInterpolator;
        private Interpolator mFoldInterpolator;
        private float mHeight;
        private GradientDrawable mLeftDrawable;
        /* access modifiers changed from: private */
        public float mOffset;
        private float mRadius;
        private GradientDrawable mRightDrawable;
        /* access modifiers changed from: private */
        @UiState
        public int mTargetUiState = 0;
        /* access modifiers changed from: private */
        public float mWidth;
        /* access modifiers changed from: private */
        public float mWidthMax;
        private float mWidthMin;

        public @interface UiState {
        }

        public Bar(Context context) {
            super(context);
            this.mWidthMax = context.getResources().getDimension(R.dimen.mode_select_popup_bar_width_max);
            this.mWidthMin = context.getResources().getDimension(R.dimen.mode_select_popup_bar_width_min);
            float f = this.mWidthMax;
            this.mExtendDimen = f - this.mWidthMin;
            this.mWidth = f;
            this.mHeight = context.getResources().getDimension(R.dimen.mode_select_popup_bar_height);
            this.mBgColor = context.getResources().getColor(R.color.mode_select_popup_bar_color);
            this.mAlpha = ((float) Color.alpha(this.mBgColor)) / 255.0f;
            this.mBgColor = Color.argb(255, Color.red(this.mBgColor), Color.green(this.mBgColor), Color.blue(this.mBgColor));
            setAlpha(this.mAlpha);
            this.mRadius = this.mHeight / 2.0f;
            this.mLeftDrawable = new GradientDrawable();
            this.mLeftDrawable.setColor(this.mBgColor);
            this.mLeftDrawable.setSize(((int) this.mWidth) / 2, (int) this.mHeight);
            this.mLeftDrawable.setBounds(0, 0, ((int) this.mWidth) / 2, (int) this.mHeight);
            GradientDrawable gradientDrawable = this.mLeftDrawable;
            float f2 = this.mRadius;
            gradientDrawable.setCornerRadii(new float[]{f2, f2, f2, f2, f2, f2, f2, f2});
            this.mRightDrawable = new GradientDrawable();
            this.mRightDrawable.setColor(this.mBgColor);
            this.mRightDrawable.setSize(((int) this.mWidth) / 2, (int) this.mHeight);
            this.mRightDrawable.setBounds(0, 0, ((int) this.mWidth) / 2, (int) this.mHeight);
            GradientDrawable gradientDrawable2 = this.mRightDrawable;
            float f3 = this.mRadius;
            gradientDrawable2.setCornerRadii(new float[]{f3, f3, f3, f3, f3, f3, f3, f3});
            this.mFoldInterpolator = new CubicEaseOutInterpolator();
            this.mFlatInterpolator = new PathInterpolator(0.4f, 0.0f, 0.2f, 1.0f);
            this.mAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            this.mAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float f;
                    Bar bar;
                    float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                    Bar.this.mDegree = Math.abs(20.0f * floatValue);
                    Bar bar2 = Bar.this;
                    bar2.mWidth = bar2.mWidthMax - Math.abs(Bar.this.mExtendDimen * floatValue);
                    if (Bar.this.isUpAnimation) {
                        bar = Bar.this;
                        f = -4.0f;
                    } else {
                        bar = Bar.this;
                        f = 8.0f;
                    }
                    bar.mOffset = Math.abs(floatValue) * f;
                    Bar.this.invalidate();
                }
            });
            this.mAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animator) {
                    super.onAnimationEnd(animator);
                    Bar bar = Bar.this;
                    bar.mCurUiState = bar.mTargetUiState;
                }

                public void onAnimationStart(Animator animator) {
                    super.onAnimationStart(animator);
                }
            });
        }

        /* access modifiers changed from: private */
        @UiThread
        public boolean start(@UiState int i) {
            int i2 = this.mTargetUiState;
            if (i2 == i) {
                Log.w(DragLayout.TAG, String.format("start bar animation with invalid state {%d} , and cur target state {%d}", new Object[]{Integer.valueOf(i), Integer.valueOf(this.mTargetUiState)}));
                return false;
            }
            boolean z = i2 == 1 || i == 1;
            this.isUpAnimation = z;
            float f = 0.0f;
            float f2 = this.mCurUiState == 0 ? 0.0f : 1.0f;
            this.mTargetUiState = i;
            if (this.mAnimator.isRunning()) {
                f2 = ((Float) this.mAnimator.getAnimatedValue()).floatValue();
                String str = DragLayout.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("cancel running animation, cur process ");
                sb.append(f2);
                Log.d(str, sb.toString());
                this.mAnimator.cancel();
            }
            if (i == 0) {
                f2 = -f2;
            } else {
                f = 1.0f;
            }
            this.mAnimator.setFloatValues(new float[]{f2, f});
            this.mAnimator.setDuration((long) ((int) (Math.abs(f - f2) * (i == 0 ? 600.0f : 200.0f))));
            this.mAnimator.setInterpolator(i == 0 ? this.mFlatInterpolator : this.mFoldInterpolator);
            Log.d(DragLayout.TAG, String.format("start animation with states {%d} {%d} AND duration {%s} , values {%s:%s} , isUp {%s}", new Object[]{Integer.valueOf(this.mCurUiState), Integer.valueOf(this.mTargetUiState), Long.valueOf(this.mAnimator.getDuration()), Float.valueOf(f2), Float.valueOf(f), Boolean.valueOf(this.isUpAnimation)}));
            this.mAnimator.start();
            return true;
        }

        /* access modifiers changed from: protected */
        public void onDraw(Canvas canvas) {
            canvas.save();
            float f = this.mWidthMax;
            canvas.translate(f / 2.0f, (f * 0.3f) + this.mOffset);
            canvas.save();
            canvas.rotate(this.isUpAnimation ? 180.0f - this.mDegree : this.mDegree - 180.0f);
            this.mLeftDrawable.setSize(((int) this.mWidth) / 2, (int) this.mHeight);
            this.mLeftDrawable.setBounds(0, 0, ((int) this.mWidth) / 2, (int) this.mHeight);
            canvas.translate(-this.mRadius, ((float) (-this.mLeftDrawable.getBounds().height())) / 2.0f);
            this.mLeftDrawable.draw(canvas);
            canvas.restore();
            canvas.save();
            canvas.rotate(this.isUpAnimation ? this.mDegree : -this.mDegree);
            this.mRightDrawable.setSize(((int) this.mWidth) / 2, (int) this.mHeight);
            this.mRightDrawable.setBounds(0, 0, ((int) this.mWidth) / 2, (int) this.mHeight);
            canvas.translate(-this.mRadius, ((float) (-this.mLeftDrawable.getBounds().height())) / 2.0f);
            this.mRightDrawable.draw(canvas);
            canvas.restore();
            canvas.restore();
        }

        /* access modifiers changed from: protected */
        public void onMeasure(int i, int i2) {
            float f = this.mWidthMax;
            setMeasuredDimension((int) f, (int) (((double) f) * 0.6d));
        }
    }

    class Dependency {
        private List mCatchDragList;
        private float mTotalTranY;

        private Dependency() {
            this.mTotalTranY = 0.0f;
            this.mCatchDragList = new ArrayList(3);
        }

        public boolean catchDrag(int i, int i2) {
            this.mCatchDragList.clear();
            boolean z = false;
            for (OnDragListener catchDrag : DragLayout.this.mOnDragListeners) {
                boolean catchDrag2 = catchDrag.catchDrag(i, i2);
                this.mCatchDragList.add(Boolean.valueOf(catchDrag2));
                z |= catchDrag2;
            }
            return z;
        }

        public void onDragDone(boolean z) {
            DragLayout dragLayout = DragLayout.this;
            StringBuilder sb = new StringBuilder();
            sb.append("onDone dragUp : ");
            sb.append(z);
            dragLayout.LogV(sb.toString());
            if (!z) {
                DragLayout.this.getDragChildren().setTranslationY(0.0f);
            }
            this.mTotalTranY = DragLayout.this.getDragChildren().getTranslationY();
            for (OnDragListener onDragDone : DragLayout.this.mOnDragListeners) {
                onDragDone.onDragDone(z);
            }
            if (DragLayout.this.mBar != null) {
                DragLayout.this.mBar.start(0);
            }
        }

        public void onDragProgress(int i, boolean z) {
            DragLayout dragLayout = DragLayout.this;
            StringBuilder sb = new StringBuilder();
            sb.append("onDragProgress ");
            sb.append(i);
            sb.append(", dragUp ");
            sb.append(z);
            dragLayout.LogD(sb.toString());
            float f = this.mTotalTranY + ((float) i);
            DragLayout.this.getDragChildren().setTranslationY(f);
            for (OnDragListener onDragProgress : DragLayout.this.mOnDragListeners) {
                onDragProgress.onDragProgress((int) f, z);
            }
        }

        public void onDragStart(boolean z) {
            DragLayout dragLayout = DragLayout.this;
            StringBuilder sb = new StringBuilder();
            sb.append("onStart dragUp : ");
            sb.append(z);
            dragLayout.LogV(sb.toString());
            for (OnDragListener onDragStart : DragLayout.this.mOnDragListeners) {
                onDragStart.onDragStart(z);
            }
        }

        public boolean onInterceptDrag() {
            boolean z = false;
            for (int i = 0; i < DragLayout.this.mOnDragListeners.size(); i++) {
                if (((Boolean) this.mCatchDragList.get(i)).booleanValue()) {
                    z |= ((OnDragListener) DragLayout.this.mOnDragListeners.get(i)).onInterceptDrag();
                }
            }
            return z;
        }

        public boolean showPress(int i, int i2) {
            boolean z = false;
            for (int i3 = 0; i3 < DragLayout.this.mOnDragListeners.size(); i3++) {
                z |= ((OnDragListener) DragLayout.this.mOnDragListeners.get(i3)).showDragAnimation(i, i2);
            }
            return z;
        }
    }

    public class DragAnimationConfig {
        private float mAlphaThreshold;
        private Range mBgAlphaRange;
        private Range mCornerRadiusRange;
        private Range mDisappearAlphaRange;
        private float mDisappearDistance;
        private Range mDisappearRange;
        private Range mDisplayAlphaRange;
        private float mDisplayDistance;
        private Range mDisplayRange;
        private float mDragThreshold = 12.0f;
        private int mDuration;
        private float mSpringDistance;
        private float mTotalDragDistance;

        public DragAnimationConfig(Context context) {
            this.mDuration = context.getResources().getInteger(R.integer.drag_animation_duration);
            this.mTotalDragDistance = context.getResources().getDimension(R.dimen.more_mode_drag_distance);
            this.mDragThreshold = context.getResources().getDimension(R.dimen.drag_start_threshold);
            Float valueOf = Float.valueOf(0.0f);
            this.mDisappearRange = new Range(valueOf, Float.valueOf(context.getResources().getDimension(R.dimen.more_mode_disappear_point)));
            this.mDisappearDistance = ((Float) this.mDisappearRange.getUpper()).floatValue() - ((Float) this.mDisappearRange.getLower()).floatValue();
            this.mDisplayRange = new Range(Float.valueOf(context.getResources().getDimension(R.dimen.more_mode_disappear_point)), Float.valueOf(context.getResources().getDimension(R.dimen.more_mode_display_point)));
            this.mDisplayDistance = ((Float) this.mDisplayRange.getUpper()).floatValue() - ((Float) this.mDisplayRange.getLower()).floatValue();
            this.mCornerRadiusRange = new Range(valueOf, Float.valueOf(context.getResources().getDimension(R.dimen.more_mode_corner_radius)));
            Float valueOf2 = Float.valueOf(1.0f);
            this.mDisappearAlphaRange = new Range(valueOf, valueOf2);
            this.mDisplayAlphaRange = new Range(Float.valueOf(0.1f), valueOf2);
            this.mBgAlphaRange = new Range(valueOf, valueOf2);
            this.mSpringDistance = this.mTotalDragDistance / 2.0f;
            this.mAlphaThreshold = context.getResources().getDimension(R.dimen.more_mode_disappear_point);
        }

        public void calDragLayoutHeight(Context context, int i) {
            float f;
            if (context != null) {
                int dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.mode_item_width);
                int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.mode_item_height) - dimensionPixelSize;
                int dimensionPixelOffset = context.getResources().getDimensionPixelOffset(R.dimen.mode_more_top_margin);
                int row4PopupStyle = MoreModeHelper.getRow4PopupStyle(i);
                if (Display.fitDisplayFull(1.3333333f)) {
                    int dimensionPixelOffset2 = (Display.getScreenOrientation() == 0 ? !ModuleManager.isSquareModule() : DataRepository.dataItemRunning().getUiStyle() != 1) ? context.getResources().getDimensionPixelOffset(R.dimen.mode_list_bottom_padding_popup) : context.getResources().getDimensionPixelOffset(R.dimen.mode_list_bottom_padding_popup_11) + Display.getBottomBarHeight();
                    f = (float) (((((dimensionPixelSize * row4PopupStyle) + (dimensionPixelSize2 * (row4PopupStyle - 1))) + (dimensionPixelOffset * 2)) + dimensionPixelOffset2) - Display.getDragDistanceFix());
                } else {
                    f = (float) (((Display.getNavigationBarHeight(context) + ((dimensionPixelSize + dimensionPixelSize2) * row4PopupStyle)) + dimensionPixelOffset) - Display.getDragDistanceFix());
                }
                this.mTotalDragDistance = f;
            }
        }

        public Range getBgAlphaRange() {
            return this.mBgAlphaRange;
        }

        public Range getCornerRadiusRange() {
            return this.mCornerRadiusRange;
        }

        public Range getDisappearAlphaRange() {
            return this.mDisappearAlphaRange;
        }

        public float getDisappearDistance() {
            return this.mDisappearDistance;
        }

        public Range getDisappearRange() {
            return this.mDisappearRange;
        }

        public Range getDisplayAlphaRange() {
            return this.mDisplayAlphaRange;
        }

        public float getDisplayDistance() {
            return this.mDisplayDistance;
        }

        public Range getDisplayRange() {
            return this.mDisplayRange;
        }

        public float getDragThreshold() {
            return this.mDragThreshold;
        }

        public int getDuration() {
            return this.mDuration;
        }

        public float getMaxDragDistance() {
            return this.mSpringDistance + this.mTotalDragDistance;
        }

        public float getSpringDistance() {
            return this.mSpringDistance;
        }

        public float getTotalDragDistance() {
            return this.mTotalDragDistance;
        }
    }

    public interface OnDragListener {
        boolean catchDrag(int i, int i2) {
            return false;
        }

        void onDragDone(boolean z);

        void onDragProgress(int i, boolean z);

        void onDragStart(boolean z);

        boolean onInterceptDrag() {
            return false;
        }

        boolean showDragAnimation(int i, int i2) {
            return false;
        }
    }

    public DragLayout(@NonNull Context context) {
        this(context, null);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DragLayout(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDragEnable = false;
        this.mSkipDragUpDown = false;
        this.mCatchDrag = false;
        this.mState = 0;
        this.mScreenOrientation = 0;
        this.mOnDragListeners = new ArrayList();
        if (sAnimationConfig == null) {
            sAnimationConfig = new DragAnimationConfig(context);
        }
        this.mDependency = new Dependency();
        this.mDragInterpolator = new LinearInterpolator();
        this.mValueAnimatorListener = new IPhyAnimatorListener() {
            public void onCancel() {
            }

            public void onEnd() {
                if (DragLayout.this.mState == 2) {
                    DragLayout dragLayout = DragLayout.this;
                    dragLayout.setState(dragLayout.mCurDirectionIsUp ? 4 : 3);
                    DragLayout.this.mDependency.onDragDone(DragLayout.this.mCurDirectionIsUp);
                }
            }

            public void onStart() {
                if (DragLayout.this.mState == 4 || DragLayout.this.mState == 3) {
                    DragLayout dragLayout = DragLayout.this;
                    dragLayout.mStartDirectionIsUp = dragLayout.mState == 3;
                    DragLayout dragLayout2 = DragLayout.this;
                    dragLayout2.mCurDirectionIsUp = dragLayout2.mStartDirectionIsUp;
                    DragLayout.this.mDependency.onDragStart(DragLayout.this.mStartDirectionIsUp);
                    DragLayout.this.setState(2);
                }
            }

            public void onUpdate(float f) {
                if (DragLayout.this.mState == 2) {
                    DragLayout.this.mOffset = f;
                    DragLayout.this.mDependency.onDragProgress((int) DragLayout.this.mOffset, DragLayout.this.mCurDirectionIsUp);
                }
            }
        };
    }

    /* access modifiers changed from: private */
    public void LogD(String str) {
        Log.d(TAG, str);
    }

    /* access modifiers changed from: private */
    public void LogV(String str) {
        if (debug) {
            Log.v(TAG, str);
        }
    }

    private void ensureView() {
        if (this.mBar == null) {
            this.mBar = new Bar(getContext());
            LayoutParams layoutParams = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.mode_select_popup_bar_width_max), (int) (((float) getResources().getDimensionPixelSize(R.dimen.mode_select_popup_bar_width_max)) * 0.6f));
            layoutParams.gravity = 1;
            layoutParams.topMargin = getResources().getDimensionPixelSize(R.dimen.mode_select_popup_bar_margin_top);
            getDragChildren().addView(this.mBar, 1, layoutParams);
            this.mBar.setVisibility(this.mDragEnable ? 0 : 8);
        }
        if (this.mState == 0) {
            setState(3);
        }
    }

    public static DragAnimationConfig getAnimationConfig() {
        return sAnimationConfig;
    }

    private boolean isInvalidMoveEvent(boolean z, MotionEvent motionEvent) {
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        float rawY = motionEvent.getRawY() - this.mStartY;
        float rawX = motionEvent.getRawX() - this.mStartX;
        int i = this.mScreenOrientation;
        boolean z6 = false;
        if (i != 1) {
            if (i != 2) {
                if (z) {
                    if (rawY > 0.0f) {
                        z6 = true;
                    }
                    return z6;
                }
                if (rawY < 0.0f) {
                    z6 = true;
                }
                return z6;
            } else if (z) {
                if (rawX > 0.0f) {
                    z5 = true;
                }
                return z5;
            } else {
                if (rawX < 0.0f) {
                    z4 = true;
                }
                return z4;
            }
        } else if (z) {
            if (rawX < 0.0f) {
                z3 = true;
            }
            return z3;
        } else {
            if (rawX > 0.0f) {
                z2 = true;
            }
            return z2;
        }
    }

    private boolean isLeftLandscape() {
        return this.mScreenOrientation == 1;
    }

    private boolean isPortrait() {
        return this.mScreenOrientation == 0;
    }

    private boolean isRightLandscape() {
        return this.mScreenOrientation == 2;
    }

    private boolean onDrag(MotionEvent motionEvent) {
        StringBuilder sb = new StringBuilder();
        sb.append("onDrag skip ?");
        sb.append(this.mSkipDragUpDown);
        sb.append(", mState ");
        sb.append(this.mState);
        LogV(sb.toString());
        int i = this.mState;
        boolean z = false;
        if (i == 3 || i == 4) {
            if (this.mState == 3) {
                z = true;
            }
            this.mStartDirectionIsUp = z;
            boolean z2 = this.mStartDirectionIsUp;
            this.mCurDirectionIsUp = z2;
            this.mDependency.onDragStart(z2);
            setState(1);
            this.mOffset = 0.0f;
            this.mStartY = motionEvent.getRawY();
            this.mStartX = motionEvent.getRawX();
            return true;
        } else if (i != 1) {
            return false;
        } else {
            if (isInvalidMoveEvent(this.mStartDirectionIsUp, motionEvent)) {
                return true;
            }
            float rawY = motionEvent.getRawY() - this.mStartY;
            float rawX = motionEvent.getRawX() - this.mStartX;
            if (isPortrait()) {
                rawX = rawY;
            }
            if (Math.abs(this.mOffset) < getAnimationConfig().getTotalDragDistance()) {
                float interpolation = this.mDragInterpolator.getInterpolation(Math.max(Math.min(Math.abs(rawX), getAnimationConfig().getTotalDragDistance()), 0.0f));
                if (!isLeftLandscape() ? rawX < 0.0f : rawX > 0.0f) {
                    interpolation = -interpolation;
                }
                float f = this.mOffset;
                if (interpolation != f) {
                    if (interpolation < f) {
                        z = true;
                    }
                    this.mCurDirectionIsUp = z;
                }
                this.mOffset = interpolation;
                this.mDependency.onDragProgress((int) this.mOffset, this.mCurDirectionIsUp);
            }
            return true;
        }
    }

    /* access modifiers changed from: private */
    public void setState(int i) {
        if (this.mState != i) {
            StringBuilder sb = new StringBuilder();
            sb.append("setState ");
            sb.append(i);
            LogD(sb.toString());
            this.mState = i;
        }
    }

    public /* synthetic */ boolean O000000o(View view, MotionEvent motionEvent) {
        return this.mDragEnable;
    }

    public void addOnDragListener(OnDragListener onDragListener) {
        StringBuilder sb = new StringBuilder();
        sb.append("addOnDragListener ");
        sb.append(onDragListener);
        LogV(sb.toString());
        if (!this.mOnDragListeners.contains(onDragListener)) {
            this.mOnDragListeners.add(onDragListener);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:69:0x0108, code lost:
        if (r1 < 0.0f) goto L_0x010a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:70:0x010a, code lost:
        r8 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x0117, code lost:
        if (r1 > 0) goto L_0x010a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0121, code lost:
        if (r0 < 0.0f) goto L_0x010a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        boolean z;
        StringBuilder sb = new StringBuilder();
        sb.append("dispatchTouchEvent ");
        sb.append(motionEvent.getActionMasked());
        LogV(sb.toString());
        if (!this.mDragEnable) {
            return super.dispatchTouchEvent(motionEvent);
        }
        boolean z2 = true;
        if (this.mState == 2) {
            return true;
        }
        int actionMasked = motionEvent.getActionMasked();
        int i = 4;
        if (actionMasked != 0) {
            float f3 = 0.0f;
            if (actionMasked == 1) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                if (velocityTracker != null) {
                    velocityTracker.addMovement(motionEvent);
                }
                Bar bar = this.mBar;
                if (bar != null) {
                    bar.start(0);
                }
                if (this.mState == 1) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("onUp ");
                    sb2.append(this.mStartDirectionIsUp);
                    sb2.append(" ");
                    sb2.append(this.mCurDirectionIsUp);
                    sb2.append(", mOffset = ");
                    sb2.append(this.mOffset);
                    LogV(sb2.toString());
                    if (Math.abs(this.mOffset) < getAnimationConfig().getTotalDragDistance()) {
                        boolean z3 = this.mStartDirectionIsUp;
                        boolean z4 = this.mCurDirectionIsUp;
                        if (z3 == z4) {
                            f2 = this.mOffset;
                            f = z4 ? -getAnimationConfig().getTotalDragDistance() : getAnimationConfig().getTotalDragDistance();
                        } else {
                            f2 = this.mOffset;
                            f = 0.0f;
                        }
                        VelocityTracker velocityTracker2 = this.mVelocityTracker;
                        if (velocityTracker2 != null) {
                            velocityTracker2.computeCurrentVelocity(1000);
                            f3 = this.mVelocityTracker.getYVelocity();
                        }
                        FolmeUtils.popup(this, f2, f, f3, this.mValueAnimatorListener);
                        setState(2);
                    } else {
                        this.mDependency.onDragDone(this.mCurDirectionIsUp);
                        if (!this.mCurDirectionIsUp) {
                            i = 3;
                        }
                        setState(i);
                    }
                    if (!this.mCatchDrag) {
                        return true;
                    }
                }
            } else if (actionMasked == 2) {
                VelocityTracker velocityTracker3 = this.mVelocityTracker;
                if (velocityTracker3 != null) {
                    velocityTracker3.addMovement(motionEvent);
                }
                if (this.mSkipDragUpDown) {
                    return super.dispatchTouchEvent(motionEvent);
                }
                if (!this.mCatchDrag || !this.mDependency.onInterceptDrag()) {
                    float rawX = motionEvent.getRawX() - this.mStartX;
                    float rawY = motionEvent.getRawY() - this.mStartY;
                    int i2 = this.mState;
                    if ((i2 == 3 || i2 == 4) && Math.abs(rawX) < getAnimationConfig().getDragThreshold() && Math.abs(rawY) < getAnimationConfig().getDragThreshold()) {
                        return true;
                    }
                    if (this.mState == 3) {
                        z = !isLeftLandscape() ? !isRightLandscape() ? Math.abs(rawX) > Math.abs(rawY) || rawY > 0.0f : Math.abs(rawY) > Math.abs(rawX) || rawX > 0.0f : Math.abs(rawY) > Math.abs(rawX) || rawX < 0.0f;
                        if (z) {
                            this.mSkipDragUpDown = true;
                            LogD("skip drag up.");
                            Bar bar2 = this.mBar;
                            if (bar2 != null) {
                                bar2.start(0);
                            }
                            return super.dispatchTouchEvent(motionEvent);
                        }
                    } else {
                        z = false;
                    }
                    if (this.mState == 4) {
                        String str = "skip drag down.";
                        if (!isPortrait()) {
                            int i3 = (rawX > 0.0f ? 1 : (rawX == 0.0f ? 0 : -1));
                            if (i3 != 0) {
                                if (isLeftLandscape()) {
                                }
                                if (isRightLandscape()) {
                                }
                                if (z) {
                                    this.mSkipDragUpDown = true;
                                }
                            }
                        } else if (rawY != 0.0f) {
                        }
                        LogD(str);
                    }
                    Bar bar3 = this.mBar;
                    if (bar3 != null) {
                        int i4 = this.mState;
                        if (i4 == 3) {
                            bar3.start(1);
                        } else if (i4 == 4) {
                            bar3.start(-1);
                        }
                    }
                    if (!onDrag(motionEvent) && !super.dispatchTouchEvent(motionEvent)) {
                        z2 = false;
                    }
                    return z2;
                }
                LogV("skip drag caz dependency intercept.");
                this.mSkipDragUpDown = true;
                return super.dispatchTouchEvent(motionEvent);
            }
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("onDown ");
            sb3.append(motionEvent.getActionMasked());
            LogV(sb3.toString());
            VelocityTracker velocityTracker4 = this.mVelocityTracker;
            if (velocityTracker4 != null) {
                velocityTracker4.clear();
            } else {
                this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(motionEvent);
            this.mSkipDragUpDown = false;
            this.mStartX = motionEvent.getRawX();
            this.mStartY = motionEvent.getRawY();
            this.mCatchDrag = this.mDependency.catchDrag((int) this.mStartX, (int) this.mStartY);
            if (this.mCatchDrag) {
                LogV("dependency wanna catch drag event.");
            }
            if (Util.isInViewRegion(getDragChildren(), (int) this.mStartX, (int) this.mStartY) && this.mDependency.showPress((int) this.mStartX, (int) this.mStartY)) {
                Bar bar4 = this.mBar;
                if (bar4 != null) {
                    int i5 = this.mState;
                    if (i5 == 3) {
                        bar4.start(1);
                    } else if (i5 == 4) {
                        bar4.start(-1);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    public FrameLayout getDragChildren() {
        if (this.mChildren == null) {
            this.mChildren = (FrameLayout) findViewById(R.id.drag_layout_children);
            this.mChildren.setOnTouchListener(new O00000Oo(this));
        }
        return this.mChildren;
    }

    public boolean isExpanded() {
        return this.mState == 4;
    }

    public boolean isShrink() {
        int i = this.mState;
        return i == 3 || i == 0;
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        ensureView();
        super.onMeasure(i, i2);
    }

    public void removeOnDragListener(OnDragListener onDragListener) {
        StringBuilder sb = new StringBuilder();
        sb.append("removeOnDragListener ");
        sb.append(onDragListener);
        LogV(sb.toString());
        this.mOnDragListeners.remove(onDragListener);
    }

    public void reset() {
        int i = this.mState;
        if (i != 0 && i != 3) {
            this.mDependency.onDragStart(false);
            this.mDependency.onDragProgress(0, false);
            this.mDependency.onDragDone(false);
            setState(0);
        }
    }

    public void setDragEnable(boolean z) {
        boolean z2 = !CameraSettings.isPopupMoreStyle() ? false : z;
        if (!DataRepository.dataItemGlobal().isNormalIntent()) {
            z2 = false;
        }
        if (!DataRepository.dataItemGlobal().getComponentModuleList().isCommonMode(ModuleManager.getActiveModuleIndex())) {
            z2 = false;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("setDragEnable : ");
        sb.append(z);
        sb.append(", force : ");
        sb.append(z2);
        LogD(sb.toString());
        if (this.mDragEnable != z2) {
            this.mDragEnable = z2;
            Bar bar = this.mBar;
            if (bar == null) {
                return;
            }
            if (this.mDragEnable) {
                Completable.create(new FolmeAlphaToOnSubscribe(bar, 0.0f, bar.mAlpha)).subscribe();
            } else {
                bar.setVisibility(8);
            }
        }
    }

    public void setScreenOrientation(int i) {
        if (this.mScreenOrientation != i) {
            this.mScreenOrientation = i;
            if (this.mState == 4) {
                getDragChildren().setTranslationY(-getAnimationConfig().getTotalDragDistance());
            }
        }
    }

    public boolean shrink(boolean z) {
        int i = this.mState;
        if (i == 4 || i == 2) {
            FolmeUtils.popup(null, 0.0f, getAnimationConfig().getTotalDragDistance(), 0.0f, this.mValueAnimatorListener);
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("shrink fail, state error. now state :");
        sb.append(this.mState);
        LogD(sb.toString());
        return false;
    }
}
