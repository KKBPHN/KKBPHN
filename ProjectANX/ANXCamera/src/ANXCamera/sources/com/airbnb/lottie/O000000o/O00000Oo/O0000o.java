package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.C0059O0000OoO;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

public class O0000o extends C0023O0000Ooo {
    private final C0059O0000OoO O00o00oo = new C0059O0000OoO();

    public O0000o(List list) {
        super(list);
    }

    public C0059O0000OoO O000000o(C0054O000000o o000000o, float f) {
        Object obj = o000000o.startValue;
        if (obj != null) {
            Object obj2 = o000000o.endValue;
            if (obj2 != null) {
                C0059O0000OoO o0000OoO = (C0059O0000OoO) obj;
                C0059O0000OoO o0000OoO2 = (C0059O0000OoO) obj2;
                O0000Oo o0000Oo = this.O00o00;
                if (o0000Oo != null) {
                    C0059O0000OoO o0000OoO3 = (C0059O0000OoO) o0000Oo.O00000Oo(o000000o.O000oO0O, o000000o.O000oO0o.floatValue(), o0000OoO, o0000OoO2, f, O00OOoO(), getProgress());
                    if (o0000OoO3 != null) {
                        return o0000OoO3;
                    }
                }
                this.O00o00oo.set(O0000O0o.lerp(o0000OoO.getScaleX(), o0000OoO2.getScaleX(), f), O0000O0o.lerp(o0000OoO.getScaleY(), o0000OoO2.getScaleY(), f));
                return this.O00o00oo;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
