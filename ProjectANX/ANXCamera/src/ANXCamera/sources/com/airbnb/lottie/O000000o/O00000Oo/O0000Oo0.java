package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

public class O0000Oo0 extends C0023O0000Ooo {
    public O0000Oo0(List list) {
        super(list);
    }

    /* access modifiers changed from: 0000 */
    public Float O000000o(C0054O000000o o000000o, float f) {
        return Float.valueOf(O00000o0(o000000o, f));
    }

    /* access modifiers changed from: 0000 */
    public float O00000o0(C0054O000000o o000000o, float f) {
        if (o000000o.startValue == null || o000000o.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        O0000Oo o0000Oo = this.O00o00;
        if (o0000Oo != null) {
            Float f2 = (Float) o0000Oo.O00000Oo(o000000o.O000oO0O, o000000o.O000oO0o.floatValue(), o000000o.startValue, o000000o.endValue, f, O00OOoO(), getProgress());
            if (f2 != null) {
                return f2.floatValue();
            }
        }
        return O0000O0o.lerp(o000000o.O00o0OO0(), o000000o.O00o0O0o(), f);
    }

    public float getFloatValue() {
        return O00000o0(O000000o(), O00OOo());
    }
}
