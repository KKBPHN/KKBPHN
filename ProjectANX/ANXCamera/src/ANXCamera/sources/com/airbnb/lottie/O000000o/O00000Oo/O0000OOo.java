package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.O00000o.O00000Oo;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

public class O0000OOo extends C0023O0000Ooo {
    public O0000OOo(List list) {
        super(list);
    }

    /* access modifiers changed from: 0000 */
    public Integer O000000o(C0054O000000o o000000o, float f) {
        return Integer.valueOf(O00000Oo(o000000o, f));
    }

    public int O00000Oo(C0054O000000o o000000o, float f) {
        Object obj = o000000o.startValue;
        if (obj == null || o000000o.endValue == null) {
            throw new IllegalStateException("Missing values for keyframe.");
        }
        int intValue = ((Integer) obj).intValue();
        int intValue2 = ((Integer) o000000o.endValue).intValue();
        O0000Oo o0000Oo = this.O00o00;
        if (o0000Oo != null) {
            Integer num = (Integer) o0000Oo.O00000Oo(o000000o.O000oO0O, o000000o.O000oO0o.floatValue(), Integer.valueOf(intValue), Integer.valueOf(intValue2), f, O00OOoO(), getProgress());
            if (num != null) {
                return num.intValue();
            }
        }
        return O00000Oo.O000000o(O0000O0o.clamp(f, 0.0f, 1.0f), intValue, intValue2);
    }

    public int getIntValue() {
        return O00000Oo(O000000o(), O00OOo());
    }
}
