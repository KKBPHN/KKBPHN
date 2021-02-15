package miuix.animation.utils;

import android.animation.TimeInterpolator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import java.util.Arrays;
import miuix.view.animation.BounceEaseInInterpolator;
import miuix.view.animation.BounceEaseInOutInterpolator;
import miuix.view.animation.BounceEaseOutInterpolator;
import miuix.view.animation.CubicEaseInInterpolator;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.ExponentialEaseInInterpolator;
import miuix.view.animation.ExponentialEaseInOutInterpolator;
import miuix.view.animation.ExponentialEaseOutInterpolator;
import miuix.view.animation.QuadraticEaseInInterpolator;
import miuix.view.animation.QuadraticEaseInOutInterpolator;
import miuix.view.animation.QuadraticEaseOutInterpolator;
import miuix.view.animation.QuarticEaseInInterpolator;
import miuix.view.animation.QuarticEaseInOutInterpolator;
import miuix.view.animation.QuinticEaseInInterpolator;
import miuix.view.animation.QuinticEaseInOutInterpolator;
import miuix.view.animation.QuinticEaseOutInterpolator;
import miuix.view.animation.SineEaseInInterpolator;
import miuix.view.animation.SineEaseInOutInterpolator;
import miuix.view.animation.SineEaseOutInterpolator;

public class EaseManager {
    public static final long DEFAULT_DURATION = 300;

    public class EaseStyle {
        public float[] factors;
        public boolean stopAtTarget;
        public final int style;

        public EaseStyle(int i) {
            this.style = i;
        }

        public EaseStyle setFactors(float... fArr) {
            this.factors = fArr;
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("EaseStyle{style=");
            sb.append(this.style);
            sb.append(", factors=");
            sb.append(Arrays.toString(this.factors));
            sb.append('}');
            return sb.toString();
        }
    }

    public interface EaseStyleDef {
        public static final int ACCELERATE = -3;
        public static final int ACCELERATE_DECELERATE = 21;
        public static final int ACCELERATE_INTERPOLATOR = 22;
        public static final int BOUNCE = 23;
        public static final int BOUNCE_EASE_IN = 24;
        public static final int BOUNCE_EASE_INOUT = 26;
        public static final int BOUNCE_EASE_OUT = 25;
        public static final int CUBIC_IN = 5;
        public static final int CUBIC_INOUT = 7;
        public static final int CUBIC_OUT = 6;
        public static final int DECELERATE = 20;
        public static final int DURATION = -1;
        public static final int EXPO_IN = 17;
        public static final int EXPO_INOUT = 19;
        public static final int EXPO_OUT = 18;
        public static final int FRICTION = -4;
        public static final int LINEAR = 1;
        public static final int QUAD_IN = 2;
        public static final int QUAD_INOUT = 4;
        public static final int QUAD_OUT = 3;
        public static final int QUART_IN = 8;
        public static final int QUART_INOUT = 10;
        public static final int QUART_OUT = 9;
        public static final int QUINT_IN = 11;
        public static final int QUINT_INOUT = 13;
        public static final int QUINT_OUT = 12;
        public static final int REBOUND = -6;
        public static final int SIN_IN = 14;
        public static final int SIN_INOUT = 16;
        public static final int SIN_OUT = 15;
        public static final int SPRING = 0;
        public static final int SPRING_PHY = -2;
        public static final int STOP = -5;
    }

    public class InterpolateEaseStyle extends EaseStyle {
        public long duration = 300;

        public InterpolateEaseStyle(int i) {
            super(i);
        }

        public InterpolateEaseStyle setDuration(long j) {
            this.duration = j;
            return this;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("InterpolateEaseStyle{style=");
            sb.append(this.style);
            sb.append(", duration=");
            sb.append(this.duration);
            sb.append(", factors=");
            sb.append(Arrays.toString(this.factors));
            sb.append('}');
            return sb.toString();
        }
    }

    public class SpringInterpolator implements TimeInterpolator {
        private float c;
        private float c1 = this.initial;
        private float c2;
        private float damping = 0.95f;
        private float initial = -1.0f;
        private float k;
        private float m = 1.0f;
        private float r;
        private float response = 0.6f;
        private float w;

        public SpringInterpolator() {
            updateParameters();
        }

        private void updateParameters() {
            double pow = Math.pow(6.283185307179586d / ((double) this.response), 2.0d);
            float f = this.m;
            this.k = (float) (pow * ((double) f));
            this.c = (float) (((((double) this.damping) * 12.566370614359172d) * ((double) f)) / ((double) this.response));
            float f2 = f * 4.0f * this.k;
            float f3 = this.c;
            float sqrt = (float) Math.sqrt((double) (f2 - (f3 * f3)));
            float f4 = this.m;
            this.w = sqrt / (f4 * 2.0f);
            this.r = -((this.c / 2.0f) * f4);
            this.c2 = (0.0f - (this.r * this.initial)) / this.w;
        }

        public float getDamping() {
            return this.damping;
        }

        public float getInterpolation(float f) {
            return (float) ((Math.pow(2.718281828459045d, (double) (this.r * f)) * ((((double) this.c1) * Math.cos((double) (this.w * f))) + (((double) this.c2) * Math.sin((double) (this.w * f))))) + 1.0d);
        }

        public float getResponse() {
            return this.response;
        }

        public SpringInterpolator setDamping(float f) {
            this.damping = f;
            updateParameters();
            return this;
        }

        public SpringInterpolator setResponse(float f) {
            this.response = f;
            updateParameters();
            return this;
        }
    }

    public static TimeInterpolator getInterpolator(int i, float... fArr) {
        return getInterpolator(getInterpolatorStyle(i, fArr));
    }

    public static TimeInterpolator getInterpolator(InterpolateEaseStyle interpolateEaseStyle) {
        if (interpolateEaseStyle != null) {
            switch (interpolateEaseStyle.style) {
                case -1:
                case 1:
                    return new LinearInterpolator();
                case 0:
                    return new SpringInterpolator().setDamping(interpolateEaseStyle.factors[0]).setResponse(interpolateEaseStyle.factors[1]);
                case 2:
                    return new QuadraticEaseInInterpolator();
                case 3:
                    return new QuadraticEaseOutInterpolator();
                case 4:
                    return new QuadraticEaseInOutInterpolator();
                case 5:
                    return new CubicEaseInInterpolator();
                case 6:
                    return new CubicEaseOutInterpolator();
                case 7:
                    return new CubicEaseInOutInterpolator();
                case 8:
                    return new QuarticEaseInInterpolator();
                case 9:
                    return new QuadraticEaseOutInterpolator();
                case 10:
                    return new QuarticEaseInOutInterpolator();
                case 11:
                    return new QuinticEaseInInterpolator();
                case 12:
                    return new QuinticEaseOutInterpolator();
                case 13:
                    return new QuinticEaseInOutInterpolator();
                case 14:
                    return new SineEaseInInterpolator();
                case 15:
                    return new SineEaseOutInterpolator();
                case 16:
                    return new SineEaseInOutInterpolator();
                case 17:
                    return new ExponentialEaseInInterpolator();
                case 18:
                    return new ExponentialEaseOutInterpolator();
                case 19:
                    return new ExponentialEaseInOutInterpolator();
                case 20:
                    return new DecelerateInterpolator();
                case 21:
                    return new AccelerateDecelerateInterpolator();
                case 22:
                    return new AccelerateInterpolator();
                case 23:
                    return new BounceInterpolator();
                case 24:
                    return new BounceEaseInInterpolator();
                case 25:
                    return new BounceEaseOutInterpolator();
                case 26:
                    return new BounceEaseInOutInterpolator();
            }
        }
        return null;
    }

    private static InterpolateEaseStyle getInterpolatorStyle(int i, float... fArr) {
        InterpolateEaseStyle interpolateEaseStyle = new InterpolateEaseStyle(i);
        interpolateEaseStyle.setFactors(fArr);
        return interpolateEaseStyle;
    }

    public static EaseStyle getStyle(int i, float... fArr) {
        if (i >= -1) {
            InterpolateEaseStyle interpolatorStyle = getInterpolatorStyle(i, fArr.length > 1 ? Arrays.copyOfRange(fArr, 1, fArr.length) : new float[0]);
            if (fArr.length > 0) {
                interpolatorStyle.setDuration((long) ((int) fArr[0]));
            }
            return interpolatorStyle;
        }
        EaseStyle easeStyle = new EaseStyle(i);
        easeStyle.setFactors(fArr);
        return easeStyle;
    }

    public static boolean isPhysicsStyle(int i) {
        return i < -1;
    }
}
