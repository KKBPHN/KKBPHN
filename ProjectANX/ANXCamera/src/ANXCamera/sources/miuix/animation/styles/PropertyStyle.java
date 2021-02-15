package miuix.animation.styles;

import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.TimeInterpolator;
import android.animation.TypeEvaluator;
import java.util.Arrays;
import miuix.animation.Folme;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.physics.AccelerateOperator;
import miuix.animation.physics.EquilibriumChecker;
import miuix.animation.physics.FrictionOperator;
import miuix.animation.physics.PhysicsOperator;
import miuix.animation.physics.SpringOperator;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.EaseManager.EaseStyle;
import miuix.animation.utils.EaseManager.InterpolateEaseStyle;
import miuix.animation.utils.LogUtils;

public class PropertyStyle {
    private static final long LONGEST_DURATION = 10000;
    private AnimConfigLink mConfig;
    private long mDuration = -1;
    EquilibriumChecker mEquilibrium;
    private int mFrameCount;
    int[] mIntValues = new int[0];
    private TimeInterpolator mInterpolator;
    private boolean mIsRunning;
    private PhysicsOperator mPhyOperator;
    FloatProperty mProperty;
    IAnimTarget mTarget;
    Object mToTag;
    private long mTotalTime;
    boolean mUseIntValue;
    private double[] mVV = {0.0d, 0.0d};
    float[] mValues = new float[0];

    public PropertyStyle(Object obj, FloatProperty floatProperty) {
        this.mToTag = obj;
        this.mProperty = floatProperty;
        this.mUseIntValue = this.mProperty instanceof IIntValueProperty;
    }

    private void applyDelayedAnimConfig(float f, double d) {
        double[] dArr = this.mVV;
        double d2 = dArr[0];
        double d3 = dArr[1];
        double processCurrentValue = processCurrentValue(dArr[0]);
        float f2 = f;
        doPhysicsCalculation(this.mVV, f, d);
        EaseStyle easeEffectFromValue = this.mConfig.getEaseEffectFromValue(this.mProperty, d2, this.mVV[0]);
        if (easeEffectFromValue != null) {
            setEase(easeEffectFromValue);
        }
        this.mVV[0] = toAnimValue(processCurrentValue);
        this.mVV[1] = d3;
    }

    private PhysicsOperator createPhyOperator(EaseStyle easeStyle) {
        float[] factors = getFactors(easeStyle);
        int i = easeStyle.style;
        if (i == -4) {
            return new FrictionOperator(factors[0]);
        }
        if (i == -3) {
            return new AccelerateOperator(factors[0]);
        }
        if (i != -2) {
            return null;
        }
        return new SpringOperator(factors[0], factors[1]);
    }

    private void doFinishProcess() {
        if (!(this.mPhyOperator instanceof SpringOperator)) {
            setFinishVV();
            return;
        }
        double targetDoubleValue = getTargetDoubleValue();
        double diff = getDiff(targetDoubleValue, this.mVV[0]);
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("targetValue = ");
            sb.append(targetDoubleValue);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("value = ");
            sb2.append(this.mVV[0]);
            StringBuilder sb3 = new StringBuilder();
            sb3.append("diff = ");
            sb3.append(diff);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("frameCount = ");
            sb4.append(this.mFrameCount);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("totalTime = ");
            sb5.append(this.mTotalTime);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("predict duration = ");
            sb6.append(this.mDuration);
            LogUtils.debug("doFinishProcess", this.mProperty, sb.toString(), sb2.toString(), sb3.toString(), sb4.toString(), sb5.toString(), sb6.toString());
        }
        setFinishVV();
    }

    private void doPhysicsCalculation(double[] dArr, float f, double d) {
        double processTargetValue = processTargetValue(d);
        double processCurrentValue = processCurrentValue(dArr[0]);
        dArr[1] = this.mPhyOperator.updateVelocity(dArr[1], f, processTargetValue, processCurrentValue);
        dArr[0] = toAnimValue(processCurrentValue + (dArr[1] * ((double) f)));
    }

    private double getDiff(double d, double d2) {
        return processTargetValue(d) - processCurrentValue(d2);
    }

    private float[] getFactors(EaseStyle easeStyle) {
        if (easeStyle.factors.length == 0) {
            int i = easeStyle.style;
            if (i == -4) {
                return new float[]{0.4761905f};
            } else if (i == -2) {
                return this.mTarget.getVelocity(this.mProperty) > 0.0d ? new float[]{0.65f, 0.35f} : new float[]{1.0f, 0.35f};
            }
        }
        return easeStyle.factors;
    }

    private void setAnimValue(double d) {
        if (this.mUseIntValue) {
            this.mTarget.setIntValue((IIntValueProperty) this.mProperty, (int) d);
            return;
        }
        IAnimTarget iAnimTarget = this.mTarget;
        FloatProperty floatProperty = this.mProperty;
        iAnimTarget.setValue(floatProperty, iAnimTarget.shouldUseIntValue(floatProperty) ? (float) ((int) d) : (float) d);
    }

    private void setEase(EaseStyle easeStyle) {
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(toString());
            sb.append(".setEase");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("ease = ");
            sb3.append(easeStyle);
            LogUtils.debug(sb2, this.mProperty.getName(), sb3.toString());
        }
        if (EaseManager.isPhysicsStyle(easeStyle.style)) {
            this.mPhyOperator = createPhyOperator(easeStyle);
            this.mDuration = (long) (Folme.getTimeRatio() * 10000.0f);
            if (this.mEquilibrium == null) {
                this.mEquilibrium = new EquilibriumChecker(this.mTarget, this.mProperty);
            }
        } else if (easeStyle instanceof InterpolateEaseStyle) {
            InterpolateEaseStyle interpolateEaseStyle = (InterpolateEaseStyle) easeStyle;
            this.mInterpolator = EaseManager.getInterpolator(interpolateEaseStyle);
            this.mDuration = interpolateEaseStyle.duration;
        }
    }

    private void setFinishVV() {
        this.mTarget.setVelocity(this.mProperty, 0.0d);
        setAnimValue(this.mPhyOperator instanceof SpringOperator ? getTargetDoubleValue() : this.mVV[0]);
    }

    private void setupAnim() {
        this.mTotalTime = 0;
        EquilibriumChecker equilibriumChecker = this.mEquilibrium;
        if (equilibriumChecker != null) {
            equilibriumChecker.setTargetValue(getTargetDoubleValue());
        }
    }

    private void updateInterpolatorAnim() {
        this.mIsRunning = this.mTotalTime < this.mDuration;
        float regulateProgress = regulateProgress(!this.mIsRunning ? 1.0f : this.mInterpolator.getInterpolation(((float) this.mTotalTime) / ((float) this.mDuration)));
        if (this.mProperty instanceof IIntValueProperty) {
            this.mTarget.setIntValue((IIntValueProperty) this.mProperty, (int) toAnimValue((double) ((Integer) getEvaluator().evaluate(regulateProgress, Integer.valueOf(this.mIntValues[0]), Integer.valueOf(this.mIntValues[1]))).intValue()));
        } else {
            this.mTarget.setValue(this.mProperty, (float) toAnimValue((double) ((Float) getEvaluator().evaluate(regulateProgress, Float.valueOf(this.mValues[0]), Float.valueOf(this.mValues[1]))).floatValue()));
        }
    }

    private void updatePhysicsAnim(long j) {
        updateVV();
        float f = ((float) j) / 1000.0f;
        double targetDoubleValue = getTargetDoubleValue();
        doPhysicsCalculation(this.mVV, f, targetDoubleValue);
        double[] dArr = this.mVV;
        this.mIsRunning = isAnimRunning(dArr[0], dArr[1]);
        if (this.mIsRunning) {
            this.mTarget.setVelocity(this.mProperty, this.mVV[1]);
            setAnimValue(this.mVV[0]);
            applyDelayedAnimConfig(f, targetDoubleValue);
            return;
        }
        doFinishProcess();
    }

    private void updateVV() {
        if (this.mPhyOperator != null) {
            double intValue = this.mUseIntValue ? (double) this.mTarget.getIntValue((IIntValueProperty) this.mProperty) : (double) this.mTarget.getValue(this.mProperty);
            boolean z = this.mUseIntValue || this.mTarget.shouldUseIntValue(this.mProperty);
            if (!z || Math.abs(this.mVV[0] - intValue) > 1.0d) {
                this.mVV[0] = intValue;
            }
            this.mVV[1] = this.mTarget.getVelocity(this.mProperty);
        }
    }

    public void cancel() {
        if (this.mIsRunning) {
            this.mIsRunning = false;
            onEnd();
        }
    }

    public void clear() {
        this.mIsRunning = false;
        Arrays.fill(this.mVV, 0.0d);
        this.mConfig = null;
        this.mValues = null;
        this.mIntValues = null;
        this.mInterpolator = null;
        this.mPhyOperator = null;
        this.mEquilibrium = null;
        this.mTotalTime = 0;
    }

    /* access modifiers changed from: protected */
    public void doSetConfig(AnimConfigLink animConfigLink) {
    }

    public void end() {
        if (this.mProperty instanceof IIntValueProperty) {
            int targetIntValue = getTargetIntValue();
            if (targetIntValue != Integer.MAX_VALUE) {
                this.mTarget.setIntValue((IIntValueProperty) this.mProperty, targetIntValue);
            }
        } else {
            float targetValue = getTargetValue();
            if (targetValue != Float.MAX_VALUE) {
                this.mTarget.setValue(this.mProperty, targetValue);
            }
        }
        cancel();
    }

    public int getCurrentIntValue() {
        FloatProperty floatProperty = this.mProperty;
        if (floatProperty instanceof IIntValueProperty) {
            return this.mTarget.getIntValue((IIntValueProperty) floatProperty);
        }
        return Integer.MAX_VALUE;
    }

    public float getCurrentValue() {
        return this.mTarget.getValue(this.mProperty);
    }

    /* access modifiers changed from: protected */
    public TypeEvaluator getEvaluator() {
        return this.mProperty instanceof IIntValueProperty ? new IntEvaluator() : new FloatEvaluator();
    }

    public long getRunningTime() {
        return this.mTotalTime;
    }

    public IAnimTarget getTarget() {
        return this.mTarget;
    }

    /* access modifiers changed from: 0000 */
    public double getTargetDoubleValue() {
        return this.mUseIntValue ? (double) getTargetIntValue() : (double) getTargetValue();
    }

    public int getTargetIntValue() {
        int[] iArr = this.mIntValues;
        if (iArr == null || iArr.length == 0) {
            return Integer.MAX_VALUE;
        }
        return iArr.length > 1 ? iArr[1] : iArr[0];
    }

    public float getTargetValue() {
        float[] fArr = this.mValues;
        if (fArr == null || fArr.length == 0) {
            return Float.MAX_VALUE;
        }
        return fArr.length > 1 ? fArr[1] : fArr[0];
    }

    /* access modifiers changed from: protected */
    public boolean isAnimRunning(double d, double d2) {
        boolean z = !this.mEquilibrium.isAtEquilibrium(d, d2);
        if (!z) {
            return z;
        }
        long j = this.mDuration;
        if (j <= 0 || this.mTotalTime <= j) {
            return z;
        }
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("animation for ");
            sb.append(this.mProperty.getName());
            sb.append(" stopped for running time too long, frame count = ");
            sb.append(this.mFrameCount);
            sb.append(", totalTime = ");
            sb.append(this.mTotalTime);
            sb.append(", predict duration = ");
            sb.append(this.mDuration);
            LogUtils.debug(sb.toString(), new Object[0]);
        }
        return false;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    /* access modifiers changed from: protected */
    public void onEnd() {
    }

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    /* access modifiers changed from: protected */
    public void onUpdate() {
    }

    /* access modifiers changed from: protected */
    public double processCurrentValue(double d) {
        return d;
    }

    /* access modifiers changed from: protected */
    public double processTargetValue(double d) {
        return d;
    }

    /* access modifiers changed from: protected */
    public float regulateProgress(float f) {
        return f;
    }

    public void resetRunningTime() {
        this.mTotalTime = 0;
    }

    public final void setConfig(AnimConfigLink animConfigLink) {
        this.mConfig = animConfigLink;
        setEase(this.mConfig.getEase(this.mProperty));
        doSetConfig(animConfigLink);
    }

    public void setIntValues(int... iArr) {
        if (iArr.length == 1) {
            this.mIntValues = new int[]{getCurrentIntValue(), iArr[0]};
        } else {
            this.mIntValues = iArr;
        }
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("PropertyStyle.setIntValues, values = ");
            sb.append(Arrays.toString(iArr));
            LogUtils.debug(sb.toString(), new Object[0]);
        }
        setupAnim();
    }

    public void setTarget(IAnimTarget iAnimTarget) {
        this.mTarget = iAnimTarget;
    }

    public void setValues(float... fArr) {
        if (fArr.length == 1) {
            this.mValues = new float[]{getCurrentValue(), fArr[0]};
        } else {
            this.mValues = fArr;
        }
        if (LogUtils.isLogEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("PropertyStyle.setValues, values = ");
            sb.append(Arrays.toString(fArr));
            LogUtils.debug(sb.toString(), new Object[0]);
        }
        setupAnim();
    }

    public void start() {
        if (this.mIsRunning) {
            return;
        }
        if (this.mInterpolator != null || this.mPhyOperator != null) {
            this.mIsRunning = true;
            onStart();
            setupAnim();
        }
    }

    /* access modifiers changed from: protected */
    public double toAnimValue(double d) {
        return d;
    }

    public void update(long j) {
        if (this.mIsRunning) {
            this.mTotalTime += j;
            this.mFrameCount++;
            if (this.mPhyOperator != null) {
                updatePhysicsAnim(j);
            } else if (this.mInterpolator != null) {
                updateInterpolatorAnim();
            }
            onUpdate();
            if (!this.mIsRunning) {
                onEnd();
            }
        }
    }
}
