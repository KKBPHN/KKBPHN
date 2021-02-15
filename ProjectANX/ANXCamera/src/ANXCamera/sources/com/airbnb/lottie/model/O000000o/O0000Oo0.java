package com.airbnb.lottie.model.O000000o;

import com.airbnb.lottie.O000000o.O00000Oo.C0026O0000oO;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import java.util.List;

public class O0000Oo0 implements O0000o00 {
    private final O00000Oo O00oO0O;
    private final O00000Oo O00ooO00;

    public O0000Oo0(O00000Oo o00000Oo, O00000Oo o00000Oo2) {
        this.O00oO0O = o00000Oo;
        this.O00ooO00 = o00000Oo2;
    }

    public O0000O0o O00000o() {
        return new C0026O0000oO(this.O00oO0O.O00000o(), this.O00ooO00.O00000o());
    }

    public List getKeyframes() {
        throw new UnsupportedOperationException("Cannot call getKeyframes on AnimatableSplitDimensionPathValue.");
    }

    public boolean isStatic() {
        return this.O00oO0O.isStatic() && this.O00ooO00.isStatic();
    }
}
