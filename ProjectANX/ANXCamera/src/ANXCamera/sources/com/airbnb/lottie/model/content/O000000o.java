package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.O0000OOo;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.model.layer.O00000o0;

public class O000000o implements O00000Oo {
    private final boolean O00oO0Oo;
    private final boolean hidden;
    private final String name;
    private final O0000o00 position;
    private final C0098O00000oo size;

    public O000000o(String str, O0000o00 o0000o00, C0098O00000oo o00000oo, boolean z, boolean z2) {
        this.name = str;
        this.position = o0000o00;
        this.size = o00000oo;
        this.O00oO0Oo = z;
        this.hidden = z2;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new O0000OOo(o000OoO0, o00000o0, this);
    }

    public String getName() {
        return this.name;
    }

    public O0000o00 getPosition() {
        return this.position;
    }

    public C0098O00000oo getSize() {
        return this.size;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public boolean isReversed() {
        return this.O00oO0Oo;
    }
}
