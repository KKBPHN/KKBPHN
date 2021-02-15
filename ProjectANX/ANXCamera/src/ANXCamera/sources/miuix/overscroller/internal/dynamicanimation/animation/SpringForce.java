package miuix.overscroller.internal.dynamicanimation.animation;

public final class SpringForce implements Force {
    public static final float DAMPING_RATIO_HIGH_BOUNCY = 0.2f;
    public static final float DAMPING_RATIO_LOW_BOUNCY = 0.75f;
    public static final float DAMPING_RATIO_MEDIUM_BOUNCY = 0.5f;
    public static final float DAMPING_RATIO_NO_BOUNCY = 1.0f;
    private static final float MOVE_STOP_PHYSICAL_DISTANCE = 0.6f;
    public static final float STIFFNESS_HIGH = 10000.0f;
    public static final float STIFFNESS_LOW = 200.0f;
    public static final float STIFFNESS_MEDIUM = 1500.0f;
    public static final float STIFFNESS_VERY_LOW = 50.0f;
    private static final double UNSET = Double.MAX_VALUE;
    private static final double VELOCITY_THRESHOLD_MULTIPLIER = 62.5d;
    private double mDampedFreq;
    double mDampingRatio = 0.5d;
    private double mFinalPosition = UNSET;
    private double mGammaMinus;
    private double mGammaPlus;
    private boolean mInitialized = false;
    private final MassState mMassState = new MassState();
    double mNaturalFreq = Math.sqrt(1500.0d);
    double mTimeRatio = 1000.0d;
    private double mValueThreshold;
    private double mVelocityThreshold;

    public SpringForce() {
    }

    public SpringForce(float f) {
        this.mFinalPosition = (double) f;
    }

    private void init() {
        if (!this.mInitialized) {
            if (this.mFinalPosition != UNSET) {
                double d = this.mDampingRatio;
                if (d > 1.0d) {
                    double d2 = -d;
                    double d3 = this.mNaturalFreq;
                    this.mGammaPlus = (d2 * d3) + (d3 * Math.sqrt((d * d) - 1.0d));
                    double d4 = this.mDampingRatio;
                    double d5 = -d4;
                    double d6 = this.mNaturalFreq;
                    this.mGammaMinus = (d5 * d6) - (d6 * Math.sqrt((d4 * d4) - 1.0d));
                } else if (d >= 0.0d && d < 1.0d) {
                    this.mDampedFreq = this.mNaturalFreq * Math.sqrt(1.0d - (d * d));
                }
                this.mInitialized = true;
                return;
            }
            throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
        }
    }

    public float getAcceleration(float f, float f2) {
        float finalPosition = f - getFinalPosition();
        double d = this.mNaturalFreq;
        return (float) (((-(d * d)) * ((double) finalPosition)) - (((d * 2.0d) * this.mDampingRatio) * ((double) f2)));
    }

    public float getDampingRatio() {
        return (float) this.mDampingRatio;
    }

    public float getFinalPosition() {
        return (float) this.mFinalPosition;
    }

    public float getStiffness() {
        double d = this.mNaturalFreq;
        return (float) (d * d);
    }

    public boolean isAtEquilibrium(float f, float f2) {
        return ((double) Math.abs(f2)) < this.mVelocityThreshold && ((double) Math.abs(f - getFinalPosition())) < this.mValueThreshold;
    }

    public SpringForce setDampingRatio(float f) {
        if (f >= 0.0f) {
            this.mDampingRatio = (double) f;
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public SpringForce setFinalPosition(float f) {
        this.mFinalPosition = (double) f;
        return this;
    }

    public SpringForce setStiffness(float f) {
        if (f > 0.0f) {
            this.mNaturalFreq = Math.sqrt((double) f);
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    public SpringForce setTimeRatio(double d) {
        this.mTimeRatio = d;
        return this;
    }

    /* access modifiers changed from: 0000 */
    public void setValueThreshold(double d) {
        this.mValueThreshold = Math.abs(d);
        this.mVelocityThreshold = this.mValueThreshold * VELOCITY_THRESHOLD_MULTIPLIER;
    }

    /* access modifiers changed from: 0000 */
    public MassState updateValues(double d, double d2, long j) {
        double d3;
        double d4;
        init();
        double d5 = ((double) j) / this.mTimeRatio;
        double d6 = d - this.mFinalPosition;
        double d7 = this.mDampingRatio;
        if (d7 > 1.0d) {
            double d8 = this.mGammaMinus;
            double d9 = (d8 * d6) - d2;
            double d10 = this.mGammaPlus;
            double d11 = d6 - (d9 / (d8 - d10));
            double d12 = ((d6 * d8) - d2) / (d8 - d10);
            d4 = (Math.pow(2.718281828459045d, d8 * d5) * d11) + (Math.pow(2.718281828459045d, this.mGammaPlus * d5) * d12);
            double d13 = this.mGammaMinus;
            double pow = d11 * d13 * Math.pow(2.718281828459045d, d13 * d5);
            double d14 = this.mGammaPlus;
            d3 = pow + (d12 * d14 * Math.pow(2.718281828459045d, d14 * d5));
        } else if (d7 == 1.0d) {
            double d15 = this.mNaturalFreq;
            double d16 = d2 + (d15 * d6);
            double d17 = d6 + (d16 * d5);
            d4 = Math.pow(2.718281828459045d, (-d15) * d5) * d17;
            double pow2 = d17 * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * d5);
            double d18 = this.mNaturalFreq;
            d3 = (d16 * Math.pow(2.718281828459045d, (-d18) * d5)) + (pow2 * (-d18));
        } else {
            double d19 = 1.0d / this.mDampedFreq;
            double d20 = this.mNaturalFreq;
            double d21 = d19 * ((d7 * d20 * d6) + d2);
            double pow3 = Math.pow(2.718281828459045d, (-d7) * d20 * d5) * ((Math.cos(this.mDampedFreq * d5) * d6) + (Math.sin(this.mDampedFreq * d5) * d21));
            double d22 = this.mNaturalFreq;
            double d23 = (-d22) * pow3;
            double d24 = this.mDampingRatio;
            double d25 = d23 * d24;
            double pow4 = Math.pow(2.718281828459045d, (-d24) * d22 * d5);
            double d26 = this.mDampedFreq;
            double d27 = pow3;
            double sin = (-d26) * d6 * Math.sin(d26 * d5);
            double d28 = this.mDampedFreq;
            d3 = d25 + (pow4 * (sin + (d21 * d28 * Math.cos(d28 * d5))));
            d4 = d27;
        }
        double d29 = 0.0d;
        if (Math.abs(d4) < 0.6000000238418579d) {
            d3 = 0.0d;
        } else {
            d29 = d4;
        }
        MassState massState = this.mMassState;
        massState.mValue = (float) (d29 + this.mFinalPosition);
        massState.mVelocity = (float) d3;
        return massState;
    }
}
