package com.airbnb.lottie.O00000oO;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0064O0000o0O;

/* renamed from: com.airbnb.lottie.O00000oO.O000000o reason: case insensitive filesystem */
public class C0054O000000o {
    private static final float O0OOOoO = -3987645.8f;
    private static final int O0OOOoo = 784923401;
    @Nullable
    private final C0064O0000o0O O00000oo;
    public final float O000oO0O;
    @Nullable
    public Float O000oO0o;
    private float O0OOO0O;
    private float O0OOO0o;
    private int O0OOOO;
    private float O0OOOOO;
    private float O0OOOOo;
    public PointF O0OOOo;
    public PointF O0OOOo0;
    private int OO0oO;
    @Nullable
    public Object endValue;
    @Nullable
    public final Interpolator interpolator;
    @Nullable
    public final Object startValue;

    public C0054O000000o(C0064O0000o0O o0000o0O, @Nullable Object obj, @Nullable Object obj2, @Nullable Interpolator interpolator2, float f, @Nullable Float f2) {
        this.O0OOO0O = O0OOOoO;
        this.O0OOO0o = O0OOOoO;
        this.OO0oO = O0OOOoo;
        this.O0OOOO = O0OOOoo;
        this.O0OOOOO = Float.MIN_VALUE;
        this.O0OOOOo = Float.MIN_VALUE;
        this.O0OOOo0 = null;
        this.O0OOOo = null;
        this.O00000oo = o0000o0O;
        this.startValue = obj;
        this.endValue = obj2;
        this.interpolator = interpolator2;
        this.O000oO0O = f;
        this.O000oO0o = f2;
    }

    public C0054O000000o(Object obj) {
        this.O0OOO0O = O0OOOoO;
        this.O0OOO0o = O0OOOoO;
        this.OO0oO = O0OOOoo;
        this.O0OOOO = O0OOOoo;
        this.O0OOOOO = Float.MIN_VALUE;
        this.O0OOOOo = Float.MIN_VALUE;
        this.O0OOOo0 = null;
        this.O0OOOo = null;
        this.O00000oo = null;
        this.startValue = obj;
        this.endValue = obj;
        this.interpolator = null;
        this.O000oO0O = Float.MIN_VALUE;
        this.O000oO0o = Float.valueOf(Float.MAX_VALUE);
    }

    public float O0000Oo0() {
        if (this.O00000oo == null) {
            return 1.0f;
        }
        if (this.O0OOOOo == Float.MIN_VALUE) {
            if (this.O000oO0o == null) {
                this.O0OOOOo = 1.0f;
            } else {
                this.O0OOOOo = O00o00o() + ((this.O000oO0o.floatValue() - this.O000oO0O) / this.O00000oo.O00O0o0());
            }
        }
        return this.O0OOOOo;
    }

    public boolean O0000o00(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        return f >= O00o00o() && f < O0000Oo0();
    }

    public float O00o00o() {
        C0064O0000o0O o0000o0O = this.O00000oo;
        if (o0000o0O == null) {
            return 0.0f;
        }
        if (this.O0OOOOO == Float.MIN_VALUE) {
            this.O0OOOOO = (this.O000oO0O - o0000o0O.O00O0oOO()) / this.O00000oo.O00O0o0();
        }
        return this.O0OOOOO;
    }

    public int O00o0O() {
        if (this.O0OOOO == O0OOOoo) {
            this.O0OOOO = ((Integer) this.endValue).intValue();
        }
        return this.O0OOOO;
    }

    public float O00o0O0o() {
        if (this.O0OOO0o == O0OOOoO) {
            this.O0OOO0o = ((Float) this.endValue).floatValue();
        }
        return this.O0OOO0o;
    }

    public int O00o0OO() {
        if (this.OO0oO == O0OOOoo) {
            this.OO0oO = ((Integer) this.startValue).intValue();
        }
        return this.OO0oO;
    }

    public float O00o0OO0() {
        if (this.O0OOO0O == O0OOOoO) {
            this.O0OOO0O = ((Float) this.startValue).floatValue();
        }
        return this.O0OOO0O;
    }

    public boolean isStatic() {
        return this.interpolator == null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Keyframe{startValue=");
        sb.append(this.startValue);
        sb.append(", endValue=");
        sb.append(this.endValue);
        sb.append(", startFrame=");
        sb.append(this.O000oO0O);
        sb.append(", endFrame=");
        sb.append(this.O000oO0o);
        sb.append(", interpolator=");
        sb.append(this.interpolator);
        sb.append('}');
        return sb.toString();
    }
}
