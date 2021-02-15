package com.airbnb.lottie.O00000oO;

import androidx.annotation.NonNull;

public class O0000O0o extends O0000Oo {
    public O0000O0o() {
    }

    public O0000O0o(@NonNull Float f) {
        super(f);
    }

    public Float O000000o(C0055O00000Oo o00000Oo) {
        return Float.valueOf(com.airbnb.lottie.O00000o.O0000O0o.lerp(((Float) o00000Oo.getStartValue()).floatValue(), ((Float) o00000Oo.getEndValue()).floatValue(), o00000Oo.O00o0OOo()) + O00000o0(o00000Oo).floatValue());
    }

    public Float O00000o0(C0055O00000Oo o00000Oo) {
        Object obj = this.value;
        if (obj != null) {
            return (Float) obj;
        }
        throw new IllegalArgumentException("You must provide a static value in the constructor , call setValue, or override getValue.");
    }
}
