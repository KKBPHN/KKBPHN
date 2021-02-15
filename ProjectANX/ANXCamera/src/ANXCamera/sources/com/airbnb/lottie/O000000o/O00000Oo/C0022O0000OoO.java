package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O0000OoO reason: case insensitive filesystem */
public class C0022O0000OoO extends C0023O0000Ooo {
    public C0022O0000OoO(List list) {
        super(list);
    }

    /* access modifiers changed from: 0000 */
    public Integer O000000o(C0054O000000o o000000o, float f) {
        return Integer.valueOf(O00000Oo(o000000o, f));
    }

    /* access modifiers changed from: 0000 */
    public int O00000Oo(C0054O000000o o000000o, float f) {
        if (o000000o.startValue == null || o000000o.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        O0000Oo o0000Oo = this.O00o00;
        if (o0000Oo != null) {
            Integer num = (Integer) o0000Oo.O00000Oo(o000000o.O000oO0O, o000000o.O000oO0o.floatValue(), o000000o.startValue, o000000o.endValue, f, O00OOoO(), getProgress());
            if (num != null) {
                return num.intValue();
            }
        }
        return O0000O0o.O000000o(o000000o.O00o0OO(), o000000o.O00o0O(), f);
    }

    public int getIntValue() {
        return O00000Oo(O000000o(), O00OOo());
    }
}
