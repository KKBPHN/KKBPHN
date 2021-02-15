package miuix.overscroller.widget;

import android.content.Context;
import android.util.Log;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

public class OverScroller {
    private static final int DEFAULT_DURATION = 250;
    public static final int FLING_MODE = 1;
    private static boolean SCROLL_BOOST_SS_ENABLE;
    public static final int SCROLL_MODE = 0;
    private final boolean mFlywheel;
    private Interpolator mInterpolator;
    private int mMode;
    private final SplineOverScroller mScrollerX;
    private final SplineOverScroller mScrollerY;

    class SplineOverScroller {
        private static final int BALLISTIC = 2;
        private static final int CUBIC = 1;
        private static float DECELERATION_RATE = ((float) (Math.log(0.78d) / Math.log(0.9d)));
        private static final float END_TENSION = 1.0f;
        private static final float GRAVITY = 2000.0f;
        private static final float INFLEXION = 0.35f;
        private static final int NB_SAMPLES = 100;
        private static final float P1 = 0.175f;
        private static final float P2 = 0.35000002f;
        private static final int SPLINE = 0;
        private static final float[] SPLINE_POSITION = new float[101];
        private static final float[] SPLINE_TIME = new float[101];
        private static final float START_TENSION = 0.5f;
        static final int STATE_BALLISTIC = 2;
        static final int STATE_CUBIC = 1;
        static final int STATE_SPLINE = 0;
        private Context mContext;
        /* access modifiers changed from: private */
        public float mCurrVelocity;
        /* access modifiers changed from: private */
        public int mCurrentPosition;
        private float mDeceleration;
        /* access modifiers changed from: private */
        public int mDuration;
        /* access modifiers changed from: private */
        public int mFinal;
        /* access modifiers changed from: private */
        public boolean mFinished;
        private float mFlingFriction = ViewConfiguration.getScrollFriction();
        private int mOver;
        private float mPhysicalCoeff;
        private int mSplineDistance;
        private int mSplineDuration;
        /* access modifiers changed from: private */
        public int mStart;
        /* access modifiers changed from: private */
        public long mStartTime;
        /* access modifiers changed from: private */
        public int mState = 0;
        private int mVelocity;

        static {
            float f;
            float f2;
            float f3;
            float f4;
            float f5;
            float f6;
            float f7;
            float f8;
            float f9;
            float f10;
            float f11 = 0.0f;
            float f12 = 0.0f;
            for (int i = 0; i < 100; i++) {
                float f13 = ((float) i) / 100.0f;
                float f14 = 1.0f;
                while (true) {
                    f = 2.0f;
                    f2 = ((f14 - f11) / 2.0f) + f11;
                    f3 = 3.0f;
                    f4 = 1.0f - f2;
                    f5 = f2 * 3.0f * f4;
                    f6 = f2 * f2 * f2;
                    float f15 = (((f4 * P1) + (f2 * P2)) * f5) + f6;
                    float f16 = f15;
                    if (((double) Math.abs(f15 - f13)) < 1.0E-5d) {
                        break;
                    } else if (f16 > f13) {
                        f14 = f2;
                    } else {
                        f11 = f2;
                    }
                }
                SPLINE_POSITION[i] = (f5 * ((f4 * 0.5f) + f2)) + f6;
                float f17 = 1.0f;
                while (true) {
                    f7 = ((f17 - f12) / f) + f12;
                    f8 = 1.0f - f7;
                    f9 = f7 * f3 * f8;
                    f10 = f7 * f7 * f7;
                    float f18 = (((f8 * 0.5f) + f7) * f9) + f10;
                    if (((double) Math.abs(f18 - f13)) < 1.0E-5d) {
                        break;
                    }
                    if (f18 > f13) {
                        f17 = f7;
                    } else {
                        f12 = f7;
                    }
                    f = 2.0f;
                    f3 = 3.0f;
                }
                SPLINE_TIME[i] = (f9 * ((f8 * P1) + (f7 * P2))) + f10;
            }
            float[] fArr = SPLINE_POSITION;
            SPLINE_TIME[100] = 1.0f;
            fArr[100] = 1.0f;
        }

        SplineOverScroller(Context context) {
            this.mContext = context;
            this.mFinished = true;
            this.mPhysicalCoeff = context.getResources().getDisplayMetrics().density * 160.0f * 386.0878f * 0.84f;
        }

        private void adjustDuration(int i, int i2, int i3) {
            float abs = Math.abs(((float) (i3 - i)) / ((float) (i2 - i)));
            int i4 = (int) (abs * 100.0f);
            if (i4 < 100) {
                float f = ((float) i4) / 100.0f;
                int i5 = i4 + 1;
                float f2 = ((float) i5) / 100.0f;
                float[] fArr = SPLINE_TIME;
                float f3 = fArr[i4];
                this.mDuration = (int) (((float) this.mDuration) * (f3 + (((abs - f) / (f2 - f)) * (fArr[i5] - f3))));
            }
        }

        private void fitOnBounceCurve(int i, int i2, int i3) {
            float f = (float) (-i3);
            float f2 = this.mDeceleration;
            float f3 = (float) i3;
            float sqrt = (float) Math.sqrt((((double) ((((f3 * f3) / 2.0f) / Math.abs(f2)) + ((float) Math.abs(i2 - i)))) * 2.0d) / ((double) Math.abs(this.mDeceleration)));
            this.mStartTime -= (long) ((int) ((sqrt - (f / f2)) * 1000.0f));
            this.mStart = i2;
            this.mCurrentPosition = i2;
            this.mVelocity = (int) ((-this.mDeceleration) * sqrt);
        }

        private static float getDeceleration(int i) {
            if (i > 0) {
                return -2000.0f;
            }
            return GRAVITY;
        }

        private double getSplineDeceleration(int i) {
            return Math.log((double) ((((float) Math.abs(i)) * INFLEXION) / (this.mFlingFriction * this.mPhysicalCoeff)));
        }

        private double getSplineFlingDistance(int i) {
            double splineDeceleration = getSplineDeceleration(i);
            float f = DECELERATION_RATE;
            return ((double) (this.mFlingFriction * this.mPhysicalCoeff)) * Math.exp((((double) f) / (((double) f) - 1.0d)) * splineDeceleration);
        }

        private int getSplineFlingDuration(int i) {
            return (int) (Math.exp(getSplineDeceleration(i) / (((double) DECELERATION_RATE) - 1.0d)) * 1000.0d);
        }

        private void onEdgeReached() {
            int i = this.mVelocity;
            float f = ((float) i) * ((float) i);
            float abs = f / (Math.abs(this.mDeceleration) * 2.0f);
            float signum = Math.signum((float) this.mVelocity);
            int i2 = this.mOver;
            if (abs > ((float) i2)) {
                this.mDeceleration = ((-signum) * f) / (((float) i2) * 2.0f);
                abs = (float) i2;
            }
            this.mOver = (int) abs;
            this.mState = 2;
            int i3 = this.mStart;
            if (this.mVelocity <= 0) {
                abs = -abs;
            }
            this.mFinal = i3 + ((int) abs);
            this.mDuration = -((int) ((((float) this.mVelocity) * 1000.0f) / this.mDeceleration));
        }

        private void startAfterEdge(int i, int i2, int i3, int i4) {
            boolean z = true;
            if (i <= i2 || i >= i3) {
                boolean z2 = i > i3;
                int i5 = z2 ? i3 : i2;
                int i6 = i - i5;
                if (i6 * i4 < 0) {
                    z = false;
                }
                if (z) {
                    startBounceAfterEdge(i, i5, i4);
                } else if (getSplineFlingDistance(i4) > ((double) Math.abs(i6))) {
                    fling(i, i4, z2 ? i2 : i, z2 ? i : i3, this.mOver);
                } else {
                    startSpringback(i, i5, i4);
                }
                return;
            }
            Log.e("OverScroller", "startAfterEdge called from a valid position");
            this.mFinished = true;
        }

        private void startBounceAfterEdge(int i, int i2, int i3) {
            this.mDeceleration = getDeceleration(i3 == 0 ? i - i2 : i3);
            fitOnBounceCurve(i, i2, i3);
            onEdgeReached();
        }

        private void startSpringback(int i, int i2, int i3) {
            this.mFinished = false;
            this.mState = 1;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mFinal = i2;
            int i4 = i - i2;
            this.mDeceleration = getDeceleration(i4);
            this.mVelocity = -i4;
            this.mOver = Math.abs(i4);
            this.mDuration = (int) (Math.sqrt((((double) i4) * -2.0d) / ((double) this.mDeceleration)) * 1000.0d);
        }

        /* access modifiers changed from: 0000 */
        public boolean continueWhenFinished() {
            int i = this.mState;
            if (i != 0) {
                if (i == 1) {
                    return false;
                }
                if (i == 2) {
                    this.mStartTime += (long) this.mDuration;
                    startSpringback(this.mFinal, this.mStart, 0);
                }
            } else if (this.mDuration >= this.mSplineDuration) {
                return false;
            } else {
                int i2 = this.mFinal;
                this.mStart = i2;
                this.mCurrentPosition = i2;
                this.mVelocity = (int) this.mCurrVelocity;
                this.mDeceleration = getDeceleration(this.mVelocity);
                this.mStartTime += (long) this.mDuration;
                onEdgeReached();
            }
            update();
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void extendDuration(int i) {
            this.mDuration = ((int) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) + i;
            this.mFinished = false;
        }

        /* access modifiers changed from: 0000 */
        public void finish() {
            this.mCurrentPosition = this.mFinal;
            this.mFinished = true;
        }

        /* access modifiers changed from: 0000 */
        public void fling(int i, int i2, int i3, int i4, int i5) {
            this.mOver = i5;
            this.mFinished = false;
            this.mVelocity = i2;
            float f = (float) i2;
            this.mCurrVelocity = f;
            this.mSplineDuration = 0;
            this.mDuration = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStart = i;
            this.mCurrentPosition = i;
            if (i > i4 || i < i3) {
                startAfterEdge(i, i3, i4, i2);
                return;
            }
            this.mState = 0;
            double d = 0.0d;
            if (i2 != 0) {
                int splineFlingDuration = getSplineFlingDuration(i2);
                this.mSplineDuration = splineFlingDuration;
                this.mDuration = splineFlingDuration;
                d = getSplineFlingDistance(i2);
            }
            this.mSplineDistance = (int) (d * ((double) Math.signum(f)));
            this.mFinal = i + this.mSplineDistance;
            int i6 = this.mFinal;
            if (i6 < i3) {
                adjustDuration(this.mStart, i6, i3);
                this.mFinal = i3;
            }
            int i7 = this.mFinal;
            if (i7 > i4) {
                adjustDuration(this.mStart, i7, i4);
                this.mFinal = i4;
            }
        }

        /* access modifiers changed from: 0000 */
        public final float getCurrVelocity() {
            return this.mCurrVelocity;
        }

        /* access modifiers changed from: 0000 */
        public final int getCurrentPosition() {
            return this.mCurrentPosition;
        }

        /* access modifiers changed from: 0000 */
        public final int getDuration() {
            return this.mDuration;
        }

        /* access modifiers changed from: 0000 */
        public final int getFinal() {
            return this.mFinal;
        }

        /* access modifiers changed from: 0000 */
        public final int getStart() {
            return this.mStart;
        }

        /* access modifiers changed from: 0000 */
        public final long getStartTime() {
            return this.mStartTime;
        }

        /* access modifiers changed from: 0000 */
        public final int getState() {
            return this.mState;
        }

        /* access modifiers changed from: 0000 */
        public final boolean isFinished() {
            return this.mFinished;
        }

        /* access modifiers changed from: 0000 */
        public void notifyEdgeReached(int i, int i2, int i3) {
            if (this.mState == 0) {
                this.mOver = i3;
                this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
                startAfterEdge(i, i2, i2, (int) this.mCurrVelocity);
            }
        }

        /* access modifiers changed from: 0000 */
        public final void setCurrVelocity(float f) {
            this.mCurrVelocity = f;
        }

        /* access modifiers changed from: 0000 */
        public final void setCurrentPosition(int i) {
            this.mCurrentPosition = i;
        }

        /* access modifiers changed from: 0000 */
        public final void setDuration(int i) {
            this.mDuration = i;
        }

        /* access modifiers changed from: 0000 */
        public final void setFinal(int i) {
            this.mFinal = i;
        }

        /* access modifiers changed from: 0000 */
        public void setFinalPosition(int i) {
            this.mFinal = i;
            this.mFinished = false;
        }

        /* access modifiers changed from: 0000 */
        public final void setFinished(boolean z) {
            this.mFinished = z;
        }

        /* access modifiers changed from: 0000 */
        public void setFriction(float f) {
            this.mFlingFriction = f;
        }

        /* access modifiers changed from: 0000 */
        public final void setStart(int i) {
            this.mStart = i;
        }

        /* access modifiers changed from: 0000 */
        public final void setStartTime(long j) {
            this.mStartTime = j;
        }

        /* access modifiers changed from: 0000 */
        public final void setState(int i) {
            this.mState = i;
        }

        /* access modifiers changed from: 0000 */
        public boolean springback(int i, int i2, int i3) {
            this.mFinished = true;
            this.mFinal = i;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mVelocity = 0;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = 0;
            if (i < i2) {
                startSpringback(i, i2, 0);
            } else if (i > i3) {
                startSpringback(i, i3, 0);
            }
            return !this.mFinished;
        }

        /* access modifiers changed from: 0000 */
        public void startScroll(int i, int i2, int i3) {
            this.mFinished = false;
            this.mStart = i;
            this.mCurrentPosition = i;
            this.mFinal = i + i2;
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mDuration = i3;
            this.mDeceleration = 0.0f;
            this.mVelocity = 0;
        }

        /* access modifiers changed from: 0000 */
        public boolean update() {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
            boolean z = false;
            if (currentAnimationTimeMillis == 0) {
                if (this.mDuration > 0) {
                    z = true;
                }
                return z;
            }
            int i = this.mDuration;
            if (currentAnimationTimeMillis > ((long) i)) {
                return false;
            }
            double d = 0.0d;
            int i2 = this.mState;
            if (i2 == 0) {
                float f = ((float) currentAnimationTimeMillis) / ((float) this.mSplineDuration);
                int i3 = (int) (f * 100.0f);
                float f2 = 1.0f;
                float f3 = 0.0f;
                if (i3 < 100) {
                    float f4 = ((float) i3) / 100.0f;
                    int i4 = i3 + 1;
                    float f5 = ((float) i4) / 100.0f;
                    float[] fArr = SPLINE_POSITION;
                    float f6 = fArr[i3];
                    f3 = (fArr[i4] - f6) / (f5 - f4);
                    f2 = f6 + ((f - f4) * f3);
                }
                int i5 = this.mSplineDistance;
                double d2 = (double) (f2 * ((float) i5));
                this.mCurrVelocity = ((f3 * ((float) i5)) / ((float) this.mSplineDuration)) * 1000.0f;
                d = d2;
            } else if (i2 == 1) {
                float f7 = ((float) currentAnimationTimeMillis) / ((float) i);
                float f8 = f7 * f7;
                float signum = Math.signum((float) this.mVelocity);
                int i6 = this.mOver;
                d = (double) (((float) i6) * signum * ((3.0f * f8) - ((2.0f * f7) * f8)));
                this.mCurrVelocity = signum * ((float) i6) * 6.0f * ((-f7) + f8);
            } else if (i2 == 2) {
                float f9 = ((float) currentAnimationTimeMillis) / 1000.0f;
                int i7 = this.mVelocity;
                float f10 = (float) i7;
                float f11 = this.mDeceleration;
                this.mCurrVelocity = f10 + (f11 * f9);
                d = (double) ((((float) i7) * f9) + (((f11 * f9) * f9) / 2.0f));
            }
            this.mCurrentPosition = this.mStart + ((int) Math.round(d));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void updateScroll(float f) {
            int i = this.mStart;
            this.mCurrentPosition = i + Math.round(f * ((float) (this.mFinal - i)));
        }
    }

    class ViscousFluidInterpolator implements Interpolator {
        private static final float VISCOUS_FLUID_NORMALIZE = (1.0f / viscousFluid(1.0f));
        private static final float VISCOUS_FLUID_OFFSET = (1.0f - (VISCOUS_FLUID_NORMALIZE * viscousFluid(1.0f)));
        private static final float VISCOUS_FLUID_SCALE = 8.0f;

        ViscousFluidInterpolator() {
        }

        private static float viscousFluid(float f) {
            float f2 = f * VISCOUS_FLUID_SCALE;
            return f2 < 1.0f ? f2 - (1.0f - ((float) Math.exp((double) (-f2)))) : ((1.0f - ((float) Math.exp((double) (1.0f - f2)))) * 0.63212055f) + 0.36787945f;
        }

        public float getInterpolation(float f) {
            float viscousFluid = VISCOUS_FLUID_NORMALIZE * viscousFluid(f);
            return viscousFluid > 0.0f ? viscousFluid + VISCOUS_FLUID_OFFSET : viscousFluid;
        }
    }

    public OverScroller(Context context) {
        this(context, null);
    }

    public OverScroller(Context context, Interpolator interpolator) {
        this(context, interpolator, true);
    }

    @Deprecated
    public OverScroller(Context context, Interpolator interpolator, float f, float f2) {
        this(context, interpolator, true);
    }

    @Deprecated
    public OverScroller(Context context, Interpolator interpolator, float f, float f2, boolean z) {
        this(context, interpolator, z);
    }

    public OverScroller(Context context, Interpolator interpolator, boolean z) {
        if (interpolator == null) {
            interpolator = new ViscousFluidInterpolator();
        }
        this.mInterpolator = interpolator;
        this.mFlywheel = z;
        this.mScrollerX = new DynamicScroller(context);
        this.mScrollerY = new DynamicScroller(context);
    }

    public void abortAnimation() {
        this.mScrollerX.finish();
        this.mScrollerY.finish();
    }

    public boolean computeScrollOffset() {
        if (isFinished()) {
            return false;
        }
        int i = this.mMode;
        if (i == 0) {
            long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis() - this.mScrollerX.mStartTime;
            int access$500 = this.mScrollerX.mDuration;
            if (currentAnimationTimeMillis < ((long) access$500)) {
                float interpolation = this.mInterpolator.getInterpolation(((float) currentAnimationTimeMillis) / ((float) access$500));
                this.mScrollerX.updateScroll(interpolation);
                this.mScrollerY.updateScroll(interpolation);
            } else {
                abortAnimation();
            }
        } else if (i == 1) {
            if (!this.mScrollerX.mFinished && !this.mScrollerX.update() && !this.mScrollerX.continueWhenFinished()) {
                this.mScrollerX.finish();
            }
            if (!this.mScrollerY.mFinished && !this.mScrollerY.update() && !this.mScrollerY.continueWhenFinished()) {
                this.mScrollerY.finish();
            }
        }
        return true;
    }

    @Deprecated
    public void extendDuration(int i) {
        this.mScrollerX.extendDuration(i);
        this.mScrollerY.extendDuration(i);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        fling(i, i2, i3, i4, i5, i6, i7, i8, 0, 0);
    }

    public void fling(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        int i11;
        int i12;
        int i13;
        int i14;
        if (!this.mFlywheel || isFinished()) {
            i14 = i3;
        } else {
            float access$200 = this.mScrollerX.mCurrVelocity;
            float access$2002 = this.mScrollerY.mCurrVelocity;
            i14 = i3;
            float f = (float) i14;
            if (Math.signum(f) == Math.signum(access$200)) {
                i13 = i4;
                float f2 = (float) i13;
                if (Math.signum(f2) == Math.signum(access$2002)) {
                    i12 = (int) (f2 + access$2002);
                    i11 = (int) (f + access$200);
                    this.mMode = 1;
                    this.mScrollerX.fling(i, i11, i5, i6, i9);
                    this.mScrollerY.fling(i2, i12, i7, i8, i10);
                }
                i12 = i13;
                i11 = i14;
                this.mMode = 1;
                this.mScrollerX.fling(i, i11, i5, i6, i9);
                this.mScrollerY.fling(i2, i12, i7, i8, i10);
            }
        }
        i13 = i4;
        i12 = i13;
        i11 = i14;
        this.mMode = 1;
        this.mScrollerX.fling(i, i11, i5, i6, i9);
        this.mScrollerY.fling(i2, i12, i7, i8, i10);
    }

    public final void forceFinished(boolean z) {
        SplineOverScroller splineOverScroller = this.mScrollerX;
        this.mScrollerY.mFinished = z;
        splineOverScroller.mFinished = z;
    }

    public float getCurrVelocity() {
        return (float) Math.hypot((double) this.mScrollerX.mCurrVelocity, (double) this.mScrollerY.mCurrVelocity);
    }

    public float getCurrVelocityX() {
        return this.mScrollerX.mCurrVelocity;
    }

    public float getCurrVelocityY() {
        return this.mScrollerY.mCurrVelocity;
    }

    public final int getCurrX() {
        return this.mScrollerX.mCurrentPosition;
    }

    public final int getCurrY() {
        return this.mScrollerY.mCurrentPosition;
    }

    @Deprecated
    public final int getDuration() {
        return Math.max(this.mScrollerX.mDuration, this.mScrollerY.mDuration);
    }

    public final int getFinalX() {
        return this.mScrollerX.mFinal;
    }

    public final int getFinalY() {
        return this.mScrollerY.mFinal;
    }

    public int getMode() {
        return this.mMode;
    }

    public final int getStartX() {
        return this.mScrollerX.mStart;
    }

    public final int getStartY() {
        return this.mScrollerY.mStart;
    }

    public final boolean isFinished() {
        return this.mScrollerX.mFinished && this.mScrollerY.mFinished;
    }

    public boolean isOverScrolled() {
        return (!this.mScrollerX.mFinished && this.mScrollerX.mState != 0) || (!this.mScrollerY.mFinished && this.mScrollerY.mState != 0);
    }

    public boolean isScrollingInDirection(float f, float f2) {
        return !isFinished() && Math.signum(f) == Math.signum((float) (this.mScrollerX.mFinal - this.mScrollerX.mStart)) && Math.signum(f2) == Math.signum((float) (this.mScrollerY.mFinal - this.mScrollerY.mStart));
    }

    public void notifyHorizontalEdgeReached(int i, int i2, int i3) {
        this.mScrollerX.notifyEdgeReached(i, i2, i3);
    }

    public void notifyVerticalEdgeReached(int i, int i2, int i3) {
        this.mScrollerY.notifyEdgeReached(i, i2, i3);
    }

    @Deprecated
    public void setFinalX(int i) {
        this.mScrollerX.setFinalPosition(i);
    }

    @Deprecated
    public void setFinalY(int i) {
        this.mScrollerY.setFinalPosition(i);
    }

    public final void setFriction(float f) {
        this.mScrollerX.setFriction(f);
        this.mScrollerY.setFriction(f);
    }

    /* access modifiers changed from: 0000 */
    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null) {
            interpolator = new ViscousFluidInterpolator();
        }
        this.mInterpolator = interpolator;
    }

    public boolean springBack(int i, int i2, int i3, int i4, int i5, int i6) {
        this.mMode = 1;
        return this.mScrollerX.springback(i, i3, i4) || this.mScrollerY.springback(i2, i5, i6);
    }

    public void startScroll(int i, int i2, int i3, int i4) {
        startScroll(i, i2, i3, i4, 250);
    }

    public void startScroll(int i, int i2, int i3, int i4, int i5) {
        this.mMode = 0;
        this.mScrollerX.startScroll(i, i3, i5);
        this.mScrollerY.startScroll(i2, i4, i5);
    }

    public int timePassed() {
        return (int) (AnimationUtils.currentAnimationTimeMillis() - Math.min(this.mScrollerX.mStartTime, this.mScrollerY.mStartTime));
    }
}
