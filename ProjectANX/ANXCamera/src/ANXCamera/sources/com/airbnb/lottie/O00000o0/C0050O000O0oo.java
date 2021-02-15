package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.O0000OOo;
import com.airbnb.lottie.model.content.O0000o0;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;

/* renamed from: com.airbnb.lottie.O00000o0.O000O0oo reason: case insensitive filesystem */
class C0050O000O0oo {
    static O000000o NAMES = O000000o.of("nm", "ind", "ks", "hd");

    private C0050O000O0oo() {
    }

    static O0000o0 O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        int i = 0;
        String str = null;
        boolean z = false;
        O0000OOo o0000OOo = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                i = o00000Oo.nextInt();
            } else if (O000000o2 == 2) {
                o0000OOo = O00000o.O0000Oo0(o00000Oo, o0000o0O);
            } else if (O000000o2 != 3) {
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextBoolean();
            }
        }
        return new O0000o0(str, i, o0000OOo, z);
    }
}
