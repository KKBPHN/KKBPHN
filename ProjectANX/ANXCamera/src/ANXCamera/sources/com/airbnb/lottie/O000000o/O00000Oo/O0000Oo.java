package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.model.content.O00000o0;
import java.util.List;

public class O0000Oo extends C0023O0000Ooo {
    private final O00000o0 O00o00Oo;

    public O0000Oo(List list) {
        super(list);
        int i = 0;
        O00000o0 o00000o0 = (O00000o0) ((C0054O000000o) list.get(0)).startValue;
        if (o00000o0 != null) {
            i = o00000o0.getSize();
        }
        this.O00o00Oo = new O00000o0(new float[i], new int[i]);
    }

    /* access modifiers changed from: 0000 */
    public O00000o0 O000000o(C0054O000000o o000000o, float f) {
        this.O00o00Oo.O000000o((O00000o0) o000000o.startValue, (O00000o0) o000000o.endValue, f);
        return this.O00o00Oo;
    }
}
