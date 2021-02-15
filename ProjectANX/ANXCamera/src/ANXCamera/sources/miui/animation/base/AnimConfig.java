package miui.animation.base;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import miui.animation.listener.TransitionListener;
import miui.animation.property.FloatProperty;
import miui.animation.property.ValueProperty;
import miui.animation.utils.EaseManager;
import miui.animation.utils.EaseManager.EaseStyle;

public class AnimConfig {
    public static final int TINT_ALPHA = 0;
    public static final int TINT_CANVAS = 1;
    public static final int TINT_OPAQUE = 1;
    public static final int TINT_USE_LAYER = 0;
    public long delay;
    public EaseStyle ease = EaseManager.getStyle(0, 350.0f, 0.9f, 0.86f);
    public double effectFromValue;
    public long flags;
    public float fromSpeed = Float.MAX_VALUE;
    public HashSet listeners = new HashSet();
    public long minDuration;
    public FloatProperty[] relatedProperty;
    public Object tag;
    public int tintMode;

    public AnimConfig() {
    }

    public AnimConfig(AnimConfig animConfig) {
        if (animConfig != null) {
            this.delay = animConfig.delay;
            this.ease = animConfig.ease;
            this.relatedProperty = animConfig.relatedProperty;
            this.listeners.addAll(animConfig.listeners);
            this.tag = animConfig.tag;
            this.flags = animConfig.flags;
            this.fromSpeed = animConfig.fromSpeed;
            this.minDuration = animConfig.minDuration;
            this.tintMode = animConfig.tintMode;
            this.effectFromValue = animConfig.effectFromValue;
        }
    }

    public AnimConfig(FloatProperty floatProperty) {
        setProperty(floatProperty);
    }

    public AnimConfig(String... strArr) {
        this.relatedProperty = new ValueProperty[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            this.relatedProperty[i] = new ValueProperty(strArr[i]);
        }
    }

    public AnimConfig(FloatProperty... floatPropertyArr) {
        this.relatedProperty = floatPropertyArr;
    }

    public static AnimConfig forProperty(String... strArr) {
        return new AnimConfig(strArr);
    }

    public static AnimConfig forProperty(FloatProperty... floatPropertyArr) {
        return new AnimConfig(floatPropertyArr);
    }

    public AnimConfig addListeners(TransitionListener... transitionListenerArr) {
        Collections.addAll(this.listeners, transitionListenerArr);
        return this;
    }

    public AnimConfig removeListeners(TransitionListener... transitionListenerArr) {
        if (transitionListenerArr.length == 0) {
            this.listeners.clear();
        } else {
            this.listeners.removeAll(Arrays.asList(transitionListenerArr));
        }
        return this;
    }

    public AnimConfig setDelay(long j) {
        this.delay = j;
        return this;
    }

    public AnimConfig setEase(int i, float... fArr) {
        this.ease = EaseManager.getStyle(i, fArr);
        return this;
    }

    public AnimConfig setEase(EaseStyle easeStyle) {
        this.ease = easeStyle;
        return this;
    }

    public AnimConfig setEffectFromValue(double d) {
        this.effectFromValue = d;
        return this;
    }

    public AnimConfig setFromSpeed(float f) {
        this.fromSpeed = f;
        return this;
    }

    public AnimConfig setMinDuration(long j) {
        this.minDuration = j;
        return this;
    }

    public final void setProperty(FloatProperty floatProperty) {
        this.relatedProperty = new FloatProperty[]{floatProperty};
    }

    public AnimConfig setTag(Object obj) {
        this.tag = obj;
        return this;
    }

    public AnimConfig setTintMethod(int i) {
        return this;
    }

    public AnimConfig setTintMode(int i) {
        this.tintMode = i;
        return this;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AnimConfig{, delay=");
        sb.append(this.delay);
        sb.append(", minDuration = ");
        sb.append(this.minDuration);
        sb.append(", fromSpeed = ");
        sb.append(this.fromSpeed);
        sb.append(", ease=");
        sb.append(this.ease);
        sb.append(", relatedProperty=");
        sb.append(Arrays.toString(this.relatedProperty));
        sb.append(", tag = ");
        sb.append(this.tag);
        sb.append(", listeners = ");
        sb.append(Arrays.toString(this.listeners.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
