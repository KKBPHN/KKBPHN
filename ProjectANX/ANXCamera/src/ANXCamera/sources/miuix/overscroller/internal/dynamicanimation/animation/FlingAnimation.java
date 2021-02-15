package miuix.overscroller.internal.dynamicanimation.animation;

public final class FlingAnimation extends DynamicAnimation {
    private FinalValueListener mFinalValueListener;
    private final DragForce mFlingForce = new DragForce();

    final class DragForce implements Force {
        private static final float DEFAULT_FRICTION = -4.2f;
        private static final int MAX_DELTA_TIME = 16;
        private static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5f;
        private final float MILLISECONDS_PER_SECOND = 1000.0f;
        private double mDragRate;
        /* access modifiers changed from: private */
        public float mFriction = DEFAULT_FRICTION;
        private final MassState mMassState = new MassState();
        /* access modifiers changed from: private */
        public float mVelocityThreshold;

        DragForce() {
        }

        public float getAcceleration(float f, float f2) {
            return f2 * this.mFriction;
        }

        /* access modifiers changed from: 0000 */
        public float getFrictionScalar() {
            return this.mFriction / DEFAULT_FRICTION;
        }

        public boolean isAtEquilibrium(float f, float f2) {
            return Math.abs(f2) < this.mVelocityThreshold;
        }

        /* access modifiers changed from: 0000 */
        public void setFrictionScalar(float f) {
            this.mFriction = f * DEFAULT_FRICTION;
            this.mDragRate = 1.0d - Math.pow(2.718281828459045d, (double) this.mFriction);
        }

        /* access modifiers changed from: 0000 */
        public void setValueThreshold(float f) {
            this.mVelocityThreshold = f * VELOCITY_THRESHOLD_MULTIPLIER;
        }

        /* access modifiers changed from: 0000 */
        public MassState updateValueAndVelocity(float f, float f2, long j) {
            float min = ((float) Math.min(j, 16)) / 1000.0f;
            double pow = Math.pow(1.0d - this.mDragRate, (double) min);
            MassState massState = this.mMassState;
            massState.mVelocity = (float) (((double) f2) * pow);
            float f3 = massState.mVelocity;
            massState.mValue = f + (min * f3);
            if (isAtEquilibrium(massState.mValue, f3)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }
    }

    public interface FinalValueListener {
        void onFinalValueArrived(int i);
    }

    public FlingAnimation(Object obj, FloatPropertyCompat floatPropertyCompat) {
        super(obj, floatPropertyCompat);
        this.mFlingForce.setValueThreshold(getValueThreshold());
    }

    public FlingAnimation(FloatValueHolder floatValueHolder, FinalValueListener finalValueListener) {
        super(floatValueHolder);
        this.mFlingForce.setValueThreshold(getValueThreshold());
        this.mFinalValueListener = finalValueListener;
    }

    private float predictTimeWithVelocity(float f) {
        return (float) ((Math.log((double) (f / this.mVelocity)) * 1000.0d) / ((double) this.mFlingForce.mFriction));
    }

    /* access modifiers changed from: 0000 */
    public float getAcceleration(float f, float f2) {
        return this.mFlingForce.getAcceleration(f, f2);
    }

    public float getFriction() {
        return this.mFlingForce.getFrictionScalar();
    }

    /* access modifiers changed from: 0000 */
    public boolean isAtEquilibrium(float f, float f2) {
        return f >= this.mMaxValue || f <= this.mMinValue || this.mFlingForce.isAtEquilibrium(f, f2);
    }

    public float predictDuration() {
        return predictTimeWithVelocity(Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold);
    }

    public float predictNaturalDest() {
        return (this.mValue - (this.mVelocity / this.mFlingForce.mFriction)) + ((Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold) / this.mFlingForce.mFriction);
    }

    public float predictTimeTo(float f) {
        return predictTimeWithVelocity(((f - this.mValue) + (this.mVelocity / this.mFlingForce.mFriction)) * this.mFlingForce.mFriction);
    }

    public FlingAnimation setFriction(float f) {
        if (f > 0.0f) {
            this.mFlingForce.setFrictionScalar(f);
            return this;
        }
        throw new IllegalArgumentException("Friction must be positive");
    }

    public FlingAnimation setMaxValue(float f) {
        super.setMaxValue(f);
        return this;
    }

    public FlingAnimation setMinValue(float f) {
        super.setMinValue(f);
        return this;
    }

    public FlingAnimation setStartVelocity(float f) {
        super.setStartVelocity(f);
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void setValueThreshold(float f) {
        this.mFlingForce.setValueThreshold(f);
    }

    /* access modifiers changed from: 0000 */
    public boolean updateValueAndVelocity(long j) {
        MassState updateValueAndVelocity = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, j);
        this.mValue = updateValueAndVelocity.mValue;
        this.mVelocity = updateValueAndVelocity.mVelocity;
        float f = this.mValue;
        float f2 = this.mMinValue;
        if (f < f2) {
            this.mValue = f2;
            return true;
        }
        float f3 = this.mMaxValue;
        if (f > f3) {
            this.mValue = f3;
            return true;
        } else if (!isAtEquilibrium(f, this.mVelocity)) {
            return false;
        } else {
            this.mFinalValueListener.onFinalValueArrived((int) this.mValue);
            return true;
        }
    }
}
