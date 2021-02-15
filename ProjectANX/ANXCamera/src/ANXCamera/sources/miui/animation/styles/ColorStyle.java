package miui.animation.styles;

import android.animation.TypeEvaluator;
import miui.animation.property.FloatProperty;
import miui.animation.utils.CommonUtils;

public class ColorStyle extends PropertyStyle {
    private float mProgress;

    ColorStyle(Object obj, FloatProperty floatProperty) {
        super(obj, floatProperty);
    }

    /* access modifiers changed from: protected */
    public TypeEvaluator getEvaluator() {
        return CommonUtils.sArgbEvaluator;
    }

    /* access modifiers changed from: protected */
    public boolean isAnimRunning(double d, double d2) {
        this.mEquilibrium.setTargetValue(1.0d);
        return !this.mEquilibrium.isAtEquilibrium((double) this.mProgress, d2);
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mProgress = 0.0f;
        this.mTarget.setVelocity(this.mProperty, 0.0d);
    }

    /* access modifiers changed from: protected */
    public double processCurrentValue(double d) {
        return (double) this.mProgress;
    }

    /* access modifiers changed from: protected */
    public double processTargetValue(double d) {
        return 1.0d;
    }

    /* access modifiers changed from: protected */
    public float regulateProgress(float f) {
        if (f > 1.0f) {
            return 1.0f;
        }
        if (f < 0.0f) {
            return 0.0f;
        }
        return f;
    }

    /* access modifiers changed from: protected */
    public double toAnimValue(double d) {
        this.mProgress = regulateProgress((float) d);
        return (double) ((Integer) getEvaluator().evaluate(this.mProgress, Integer.valueOf(this.mIntValues[0]), Integer.valueOf(this.mIntValues[1]))).intValue();
    }
}
