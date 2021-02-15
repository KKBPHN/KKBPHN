package com.airbnb.lottie;

import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.O00000o.O0000OOo;

/* renamed from: com.airbnb.lottie.O00000oo reason: case insensitive filesystem */
class C0061O00000oo implements C0082O000OoO {
    C0061O00000oo() {
    }

    /* renamed from: O0000OOo */
    public void O000000o(Throwable th) {
        if (O0000OOo.isNetworkException(th)) {
            O00000o.O00000Oo("Unable to load composition.", th);
            return;
        }
        throw new IllegalStateException("Unable to parse composition", th);
    }
}
