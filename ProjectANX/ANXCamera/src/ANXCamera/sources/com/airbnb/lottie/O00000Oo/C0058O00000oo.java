package com.airbnb.lottie.O00000oO;

import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

/* renamed from: com.airbnb.lottie.O00000oO.O00000oo reason: case insensitive filesystem */
abstract class C0058O00000oo extends O0000Oo {
    private final Object endValue;
    private final Interpolator interpolator;
    private final Object startValue;

    C0058O00000oo(Object obj, Object obj2) {
        this(obj, obj2, new LinearInterpolator());
    }

    C0058O00000oo(Object obj, Object obj2, Interpolator interpolator2) {
        this.startValue = obj;
        this.endValue = obj2;
        this.interpolator = interpolator2;
    }

    public Object O000000o(C0055O00000Oo o00000Oo) {
        return O000000o(this.startValue, this.endValue, this.interpolator.getInterpolation(o00000Oo.O00o0Oo()));
    }

    public abstract Object O000000o(Object obj, Object obj2, float f);
}
