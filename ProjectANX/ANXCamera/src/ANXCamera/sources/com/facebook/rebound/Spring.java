package com.facebook.rebound;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class Spring {
    private static int ID = 0;
    private static final double MAX_DELTA_TIME_SEC = 0.064d;
    private static final double SOLVER_TIMESTEP_SEC = 0.001d;
    private final PhysicsState mCurrentState = new PhysicsState();
    private double mDisplacementFromRestThreshold = 0.005d;
    private double mEndValue;
    private final String mId;
    private CopyOnWriteArraySet mListeners = new CopyOnWriteArraySet();
    private boolean mOvershootClampingEnabled;
    private final PhysicsState mPreviousState = new PhysicsState();
    private double mRestSpeedThreshold = 0.005d;
    private SpringConfig mSpringConfig;
    private final BaseSpringSystem mSpringSystem;
    private double mStartValue;
    private final PhysicsState mTempState = new PhysicsState();
    private double mTimeAccumulator = 0.0d;
    private boolean mWasAtRest = true;

    class PhysicsState {
        double position;
        double velocity;

        private PhysicsState() {
        }
    }

    Spring(BaseSpringSystem baseSpringSystem) {
        if (baseSpringSystem != null) {
            this.mSpringSystem = baseSpringSystem;
            StringBuilder sb = new StringBuilder();
            sb.append("spring:");
            int i = ID;
            ID = i + 1;
            sb.append(i);
            this.mId = sb.toString();
            setSpringConfig(SpringConfig.defaultConfig);
            return;
        }
        throw new IllegalArgumentException("Spring cannot be created outside of a BaseSpringSystem");
    }

    private double getDisplacementDistanceForState(PhysicsState physicsState) {
        return Math.abs(this.mEndValue - physicsState.position);
    }

    private void interpolate(double d) {
        PhysicsState physicsState = this.mCurrentState;
        double d2 = physicsState.position * d;
        PhysicsState physicsState2 = this.mPreviousState;
        double d3 = 1.0d - d;
        physicsState.position = d2 + (physicsState2.position * d3);
        physicsState.velocity = (physicsState.velocity * d) + (physicsState2.velocity * d3);
    }

    public Spring addListener(SpringListener springListener) {
        if (springListener != null) {
            this.mListeners.add(springListener);
            return this;
        }
        throw new IllegalArgumentException("newListener is required");
    }

    /* access modifiers changed from: 0000 */
    public void advance(double d) {
        double d2;
        boolean z;
        boolean isAtRest = isAtRest();
        if (!isAtRest || !this.mWasAtRest) {
            double d3 = MAX_DELTA_TIME_SEC;
            if (d <= MAX_DELTA_TIME_SEC) {
                d3 = d;
            }
            this.mTimeAccumulator += d3;
            SpringConfig springConfig = this.mSpringConfig;
            double d4 = springConfig.tension;
            double d5 = springConfig.friction;
            PhysicsState physicsState = this.mCurrentState;
            double d6 = physicsState.position;
            double d7 = physicsState.velocity;
            PhysicsState physicsState2 = this.mTempState;
            double d8 = physicsState2.position;
            double d9 = physicsState2.velocity;
            boolean z2 = isAtRest;
            while (true) {
                d2 = this.mTimeAccumulator;
                if (d2 < SOLVER_TIMESTEP_SEC) {
                    break;
                }
                this.mTimeAccumulator = d2 - SOLVER_TIMESTEP_SEC;
                if (this.mTimeAccumulator < SOLVER_TIMESTEP_SEC) {
                    PhysicsState physicsState3 = this.mPreviousState;
                    physicsState3.position = d6;
                    physicsState3.velocity = d7;
                }
                double d10 = this.mEndValue;
                double d11 = ((d10 - d8) * d4) - (d5 * d7);
                double d12 = d7 + (d11 * SOLVER_TIMESTEP_SEC * 0.5d);
                double d13 = ((d10 - (((d7 * SOLVER_TIMESTEP_SEC) * 0.5d) + d6)) * d4) - (d5 * d12);
                double d14 = d7 + (d13 * SOLVER_TIMESTEP_SEC * 0.5d);
                double d15 = ((d10 - (d6 + ((d12 * SOLVER_TIMESTEP_SEC) * 0.5d))) * d4) - (d5 * d14);
                double d16 = d6 + (d14 * SOLVER_TIMESTEP_SEC);
                double d17 = d7 + (d15 * SOLVER_TIMESTEP_SEC);
                d6 += (d7 + ((d12 + d14) * 2.0d) + d17) * 0.16666666666666666d * SOLVER_TIMESTEP_SEC;
                d7 += (d11 + ((d13 + d15) * 2.0d) + (((d10 - d16) * d4) - (d5 * d17))) * 0.16666666666666666d * SOLVER_TIMESTEP_SEC;
                d8 = d16;
                d9 = d17;
            }
            PhysicsState physicsState4 = this.mTempState;
            physicsState4.position = d8;
            physicsState4.velocity = d9;
            PhysicsState physicsState5 = this.mCurrentState;
            physicsState5.position = d6;
            physicsState5.velocity = d7;
            if (d2 > 0.0d) {
                interpolate(d2 / SOLVER_TIMESTEP_SEC);
            }
            boolean z3 = true;
            if (isAtRest() || (this.mOvershootClampingEnabled && isOvershooting())) {
                if (d4 > 0.0d) {
                    double d18 = this.mEndValue;
                    this.mStartValue = d18;
                    this.mCurrentState.position = d18;
                } else {
                    this.mEndValue = this.mCurrentState.position;
                    this.mStartValue = this.mEndValue;
                }
                setVelocity(0.0d);
                z2 = true;
            }
            if (this.mWasAtRest) {
                this.mWasAtRest = false;
                z = true;
            } else {
                z = false;
            }
            if (z2) {
                this.mWasAtRest = true;
            } else {
                z3 = false;
            }
            Iterator it = this.mListeners.iterator();
            while (it.hasNext()) {
                SpringListener springListener = (SpringListener) it.next();
                if (z) {
                    springListener.onSpringActivate(this);
                }
                springListener.onSpringUpdate(this);
                if (z3) {
                    springListener.onSpringAtRest(this);
                }
            }
        }
    }

    public boolean currentValueIsApproximately(double d) {
        return Math.abs(getCurrentValue() - d) <= getRestDisplacementThreshold();
    }

    public void destroy() {
        this.mListeners.clear();
        this.mSpringSystem.deregisterSpring(this);
    }

    public double getCurrentDisplacementDistance() {
        return getDisplacementDistanceForState(this.mCurrentState);
    }

    public double getCurrentValue() {
        return this.mCurrentState.position;
    }

    public double getEndValue() {
        return this.mEndValue;
    }

    public String getId() {
        return this.mId;
    }

    public double getRestDisplacementThreshold() {
        return this.mDisplacementFromRestThreshold;
    }

    public double getRestSpeedThreshold() {
        return this.mRestSpeedThreshold;
    }

    public SpringConfig getSpringConfig() {
        return this.mSpringConfig;
    }

    public double getStartValue() {
        return this.mStartValue;
    }

    public double getVelocity() {
        return this.mCurrentState.velocity;
    }

    public boolean isAtRest() {
        return Math.abs(this.mCurrentState.velocity) <= this.mRestSpeedThreshold && (getDisplacementDistanceForState(this.mCurrentState) <= this.mDisplacementFromRestThreshold || this.mSpringConfig.tension == 0.0d);
    }

    public boolean isOvershootClampingEnabled() {
        return this.mOvershootClampingEnabled;
    }

    public boolean isOvershooting() {
        return this.mSpringConfig.tension > 0.0d && ((this.mStartValue < this.mEndValue && getCurrentValue() > this.mEndValue) || (this.mStartValue > this.mEndValue && getCurrentValue() < this.mEndValue));
    }

    public Spring removeAllListeners() {
        this.mListeners.clear();
        return this;
    }

    public Spring removeListener(SpringListener springListener) {
        if (springListener != null) {
            this.mListeners.remove(springListener);
            return this;
        }
        throw new IllegalArgumentException("listenerToRemove is required");
    }

    public Spring setAtRest() {
        PhysicsState physicsState = this.mCurrentState;
        double d = physicsState.position;
        this.mEndValue = d;
        this.mTempState.position = d;
        physicsState.velocity = 0.0d;
        return this;
    }

    public Spring setCurrentValue(double d) {
        return setCurrentValue(d, true);
    }

    public Spring setCurrentValue(double d, boolean z) {
        this.mStartValue = d;
        this.mCurrentState.position = d;
        this.mSpringSystem.activateSpring(getId());
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((SpringListener) it.next()).onSpringUpdate(this);
        }
        if (z) {
            setAtRest();
        }
        return this;
    }

    public Spring setEndValue(double d) {
        if (this.mEndValue == d && isAtRest()) {
            return this;
        }
        this.mStartValue = getCurrentValue();
        this.mEndValue = d;
        this.mSpringSystem.activateSpring(getId());
        Iterator it = this.mListeners.iterator();
        while (it.hasNext()) {
            ((SpringListener) it.next()).onSpringEndStateChange(this);
        }
        return this;
    }

    public Spring setOvershootClampingEnabled(boolean z) {
        this.mOvershootClampingEnabled = z;
        return this;
    }

    public Spring setRestDisplacementThreshold(double d) {
        this.mDisplacementFromRestThreshold = d;
        return this;
    }

    public Spring setRestSpeedThreshold(double d) {
        this.mRestSpeedThreshold = d;
        return this;
    }

    public Spring setSpringConfig(SpringConfig springConfig) {
        if (springConfig != null) {
            this.mSpringConfig = springConfig;
            return this;
        }
        throw new IllegalArgumentException("springConfig is required");
    }

    public Spring setVelocity(double d) {
        PhysicsState physicsState = this.mCurrentState;
        if (d == physicsState.velocity) {
            return this;
        }
        physicsState.velocity = d;
        this.mSpringSystem.activateSpring(getId());
        return this;
    }

    public boolean systemShouldAdvance() {
        return !isAtRest() || !wasAtRest();
    }

    public boolean wasAtRest() {
        return this.mWasAtRest;
    }
}
