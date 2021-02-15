package miui.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import com.miui.internal.R;
import miui.util.AppConstants;

public class Rotation3DLayout extends FrameLayout {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    private static final int LAYOUT_DIRECTION_LTR = 0;
    private static final int LAYOUT_DIRECTION_RTL = 1;
    private static final int MAX_GRAVITY_DEGREE = 45;
    private static final int MAX_ROTATION_DEGREE_WIDTH = 10;
    private static final int RESET_ANIM_DURATION = 300;
    private static final float[] RESET_BOUNCE_PATTERNS = {1.0f, -0.5f, 0.25f, 0.0f};
    private static final float ROTATION_DEGRREE_PER_PIXEL = computeRotationPerPixel(10.0f);
    private AnimatorSet mAnimator;
    private boolean mEnableTouchRotation;
    private boolean mIsAutoRotationByGravity;
    /* access modifiers changed from: private */
    public boolean mIsDoingAnimation;
    private boolean mIsTouch;
    private long mLastTouchDownEventTime;
    /* access modifiers changed from: private */
    public float mMaxRotationDegree;
    private Sensor mOrientationSensor;
    private SensorEventListener mOrientationSensorEventListener;
    private int mResetAnimDuration;
    private float[] mResetBouncePatterns;
    private float[] mResetBounceValuesX;
    private float[] mResetBounceValuesY;
    private float mRotationDegreePerPixel;
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public float mTargetRotationX;
    /* access modifiers changed from: private */
    public float mTargetRotationY;

    public class LayoutParams extends android.widget.FrameLayout.LayoutParams {
        public int zdistance = 0;

        public LayoutParams(int i, int i2) {
            super(i, i2);
        }

        public LayoutParams(int i, int i2, int i3) {
            super(i, i2, i3);
        }

        public LayoutParams(int i, int i2, int i3, int i4) {
            super(i, i2, i3);
            this.zdistance = i4;
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Rotation3DLayout_Layout);
            this.zdistance = obtainStyledAttributes.getInt(R.styleable.Rotation3DLayout_Layout_layout_zdistance, this.zdistance);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public LayoutParams(android.widget.FrameLayout.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    public Rotation3DLayout(Context context) {
        this(context, null);
    }

    public Rotation3DLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public Rotation3DLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMaxRotationDegree = 10.0f;
        this.mRotationDegreePerPixel = ROTATION_DEGRREE_PER_PIXEL;
        this.mResetAnimDuration = 300;
        float[] fArr = RESET_BOUNCE_PATTERNS;
        this.mResetBouncePatterns = fArr;
        this.mResetBounceValuesX = new float[fArr.length];
        this.mResetBounceValuesY = new float[fArr.length];
        this.mLastTouchDownEventTime = 0;
        this.mEnableTouchRotation = true;
        this.mIsTouch = false;
        this.mIsDoingAnimation = false;
        this.mIsAutoRotationByGravity = false;
        this.mOrientationSensorEventListener = new SensorEventListener() {
            public void onAccuracyChanged(Sensor sensor, int i) {
            }

            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] fArr = sensorEvent.values;
                float f = fArr[1];
                float f2 = fArr[2];
                if (Math.abs(f) < 45.0f) {
                    Rotation3DLayout rotation3DLayout = Rotation3DLayout.this;
                    rotation3DLayout.mTargetRotationX = (rotation3DLayout.mMaxRotationDegree * f) / 45.0f;
                    Rotation3DLayout rotation3DLayout2 = Rotation3DLayout.this;
                    rotation3DLayout2.setRotationX(rotation3DLayout2.mTargetRotationX);
                }
                if (Math.abs(f2) < 45.0f) {
                    Rotation3DLayout rotation3DLayout3 = Rotation3DLayout.this;
                    rotation3DLayout3.mTargetRotationY = ((-rotation3DLayout3.mMaxRotationDegree) * f2) / 45.0f;
                    Rotation3DLayout rotation3DLayout4 = Rotation3DLayout.this;
                    rotation3DLayout4.setRotationY(rotation3DLayout4.mTargetRotationY);
                }
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.Rotation3DLayout, i, 0);
        setMaxRotationDegree(obtainStyledAttributes.getFloat(R.styleable.Rotation3DLayout_maxRotationDegree, this.mMaxRotationDegree));
        this.mIsAutoRotationByGravity = obtainStyledAttributes.getBoolean(R.styleable.Rotation3DLayout_autoGravityRotation, this.mIsAutoRotationByGravity);
        obtainStyledAttributes.recycle();
    }

    private int computeOffset(int i, float f) {
        return (int) (Math.sin(Math.toRadians((double) f)) * ((double) i));
    }

    private static float computeRotationPerPixel(float f) {
        return f / (((float) AppConstants.getCurrentApplication().getResources().getDisplayMetrics().widthPixels) / 2.0f);
    }

    /* access modifiers changed from: private */
    public void setRotationXInternal(float f, boolean z) {
        float abs = Math.abs(f);
        float f2 = this.mMaxRotationDegree;
        if (abs > f2) {
            f = f > 0.0f ? f2 : -f2;
        }
        if (f != getRotationX()) {
            if (z || (!this.mIsTouch && !this.mIsDoingAnimation)) {
                super.setRotationX(f);
                onLayout(false, getLeft(), getTop(), getRight(), getBottom());
            }
        }
    }

    /* access modifiers changed from: private */
    public void setRotationYInternal(float f, boolean z) {
        float abs = Math.abs(f);
        float f2 = this.mMaxRotationDegree;
        if (abs > f2) {
            f = f > 0.0f ? f2 : -f2;
        }
        if (f != getRotationY()) {
            if (z || (!this.mIsTouch && !this.mIsDoingAnimation)) {
                super.setRotationY(f);
                onLayout(false, getLeft(), getTop(), getRight(), getBottom());
            }
        }
    }

    public void enableAutoRotationByGravity(boolean z) {
        this.mIsAutoRotationByGravity = z;
        if (this.mIsAutoRotationByGravity) {
            startGravityDetection();
        } else {
            stopGravityDetection();
        }
    }

    public void enableTouchRotation(boolean z) {
        this.mEnableTouchRotation = z;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        super.finalize();
        stopGravityDetection();
    }

    /* access modifiers changed from: protected */
    public android.widget.FrameLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* access modifiers changed from: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    public android.widget.FrameLayout.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public float getMaxRotationDegree() {
        return this.mMaxRotationDegree;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (!this.mEnableTouchRotation) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x006a  */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0079  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingRight = (i3 - i) - getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = (i4 - i2) - getPaddingBottom();
        for (int i9 = 0; i9 < childCount; i9++) {
            View childAt = getChildAt(i9);
            if (childAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                int i10 = layoutParams.gravity;
                if (i10 == -1) {
                    i10 = DEFAULT_CHILD_GRAVITY;
                }
                int absoluteGravity = Gravity.getAbsoluteGravity(i10, 0);
                int i11 = i10 & 112;
                int i12 = absoluteGravity & 7;
                if (i12 == 1) {
                    i8 = (((paddingRight - paddingLeft) - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                } else if (i12 == 3 || i12 != 5) {
                    i5 = layoutParams.leftMargin + paddingLeft;
                    if (i11 != 16) {
                        i7 = (((paddingBottom - paddingTop) - measuredHeight) / 2) + paddingTop + layoutParams.topMargin;
                    } else if (i11 == 48 || i11 != 80) {
                        i6 = layoutParams.topMargin + paddingTop;
                        int computeOffset = i5 + computeOffset(layoutParams.zdistance, getRotationY());
                        int computeOffset2 = i6 - computeOffset(layoutParams.zdistance, getRotationX());
                        if (!z || computeOffset != childAt.getLeft() || computeOffset2 != childAt.getTop()) {
                            childAt.layout(computeOffset, computeOffset2, measuredWidth + computeOffset, measuredHeight + computeOffset2);
                        }
                    } else {
                        i7 = paddingBottom - measuredHeight;
                    }
                    i6 = i7 - layoutParams.bottomMargin;
                    int computeOffset3 = i5 + computeOffset(layoutParams.zdistance, getRotationY());
                    int computeOffset22 = i6 - computeOffset(layoutParams.zdistance, getRotationX());
                    if (!z) {
                    }
                    childAt.layout(computeOffset3, computeOffset22, measuredWidth + computeOffset3, measuredHeight + computeOffset22);
                } else {
                    i8 = paddingRight - measuredWidth;
                }
                i5 = i8 - layoutParams.rightMargin;
                if (i11 != 16) {
                }
                i6 = i7 - layoutParams.bottomMargin;
                int computeOffset32 = i5 + computeOffset(layoutParams.zdistance, getRotationY());
                int computeOffset222 = i6 - computeOffset(layoutParams.zdistance, getRotationX());
                if (!z) {
                }
                childAt.layout(computeOffset32, computeOffset222, measuredWidth + computeOffset32, measuredHeight + computeOffset222);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0016, code lost:
        if (r0 != 3) goto L_0x008a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mEnableTouchRotation) {
            return super.onTouchEvent(motionEvent);
        }
        int action = motionEvent.getAction();
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    if (SystemClock.elapsedRealtime() - this.mLastTouchDownEventTime > ((long) ViewConfiguration.getTapTimeout())) {
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
            }
            resetRotation();
            this.mIsTouch = false;
            super.onTouchEvent(motionEvent);
            return true;
        }
        if (motionEvent.getAction() == 0) {
            this.mLastTouchDownEventTime = SystemClock.elapsedRealtime();
        }
        float min = Math.min(Math.max(0.0f, motionEvent.getX()), (float) getWidth());
        float min2 = Math.min(Math.max(0.0f, motionEvent.getY()), (float) getHeight());
        int width = getWidth() / 2;
        int height = getHeight() / 2;
        AnimatorSet animatorSet = this.mAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
        setRotationXInternal((((float) height) - min2) * this.mRotationDegreePerPixel, true);
        setRotationYInternal((min - ((float) width)) * this.mRotationDegreePerPixel, true);
        this.mIsTouch = true;
        super.onTouchEvent(motionEvent);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (i != 0) {
            stopGravityDetection();
        } else if (this.mIsAutoRotationByGravity) {
            startGravityDetection();
        }
    }

    /* access modifiers changed from: protected */
    public void resetRotation() {
        if (this.mAnimator == null) {
            this.mAnimator = new AnimatorSet();
            this.mAnimator.addListener(new AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                    Rotation3DLayout.this.mIsDoingAnimation = false;
                }

                public void onAnimationEnd(Animator animator) {
                    Rotation3DLayout.this.mIsDoingAnimation = false;
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                    Rotation3DLayout.this.mIsDoingAnimation = true;
                }
            });
            ValueAnimator valueAnimator = new ValueAnimator();
            valueAnimator.setInterpolator(new DecelerateInterpolator(0.75f));
            valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Rotation3DLayout.this.setRotationXInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
                }
            });
            valueAnimator.setDuration((long) this.mResetAnimDuration);
            ValueAnimator valueAnimator2 = new ValueAnimator();
            valueAnimator2.setInterpolator(new DecelerateInterpolator(0.75f));
            valueAnimator2.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Rotation3DLayout.this.setRotationYInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), true);
                }
            });
            valueAnimator2.setDuration((long) this.mResetAnimDuration);
            this.mAnimator.playTogether(new Animator[]{valueAnimator, valueAnimator2});
        }
        this.mAnimator.cancel();
        float rotationX = getRotationX();
        float rotationY = getRotationY();
        int i = 0;
        while (true) {
            float[] fArr = this.mResetBouncePatterns;
            if (i < fArr.length) {
                this.mResetBounceValuesX[i] = this.mTargetRotationX + (fArr[i] * rotationX);
                this.mResetBounceValuesY[i] = this.mTargetRotationY + (fArr[i] * rotationY);
                i++;
            } else {
                ((ValueAnimator) this.mAnimator.getChildAnimations().get(0)).setFloatValues(this.mResetBounceValuesX);
                ((ValueAnimator) this.mAnimator.getChildAnimations().get(1)).setFloatValues(this.mResetBounceValuesY);
                this.mAnimator.start();
                return;
            }
        }
    }

    public void setMaxRotationDegree(float f) {
        this.mMaxRotationDegree = f;
        this.mRotationDegreePerPixel = computeRotationPerPixel(f);
    }

    public void setResetAnimDuration(int i) {
        this.mResetAnimDuration = i;
    }

    public void setResetBouncePatterns(float[] fArr) {
        if (fArr != null && fArr.length > 1) {
            this.mResetBouncePatterns = fArr;
            this.mResetBounceValuesX = new float[fArr.length];
            this.mResetBounceValuesY = new float[fArr.length];
        }
    }

    public void setRotationX(float f) {
        setRotationXInternal(f, false);
    }

    public void setRotationY(float f) {
        setRotationYInternal(f, false);
    }

    public void startGravityDetection() {
        if (this.mSensorManager == null) {
            this.mSensorManager = (SensorManager) getContext().getSystemService("sensor");
        }
        if (this.mOrientationSensor == null) {
            this.mOrientationSensor = this.mSensorManager.getDefaultSensor(3);
            this.mSensorManager.registerListener(this.mOrientationSensorEventListener, this.mOrientationSensor, 1);
        }
    }

    public void stopGravityDetection() {
        SensorManager sensorManager = this.mSensorManager;
        if (sensorManager != null) {
            Sensor sensor = this.mOrientationSensor;
            if (sensor != null) {
                sensorManager.unregisterListener(this.mOrientationSensorEventListener, sensor);
                this.mOrientationSensor = null;
                this.mSensorManager = null;
                resetRotation();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void stopResetRotation() {
        AnimatorSet animatorSet = this.mAnimator;
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }
}
