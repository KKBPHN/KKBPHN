package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.model.content.O0000Oo0;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;

class O000O0OO {
    private static O000000o NAMES = O000000o.of("nm", "p", "s", "r", "hd");

    private O000O0OO() {
    }

    static O0000Oo0 O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        String str = null;
        O0000o00 o0000o00 = null;
        C0098O00000oo o00000oo = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                o0000o00 = O000000o.O00000Oo(o00000Oo, o0000o0O);
            } else if (O000000o2 == 2) {
                o00000oo = O00000o.O0000O0o(o00000Oo, o0000o0O);
            } else if (O000000o2 == 3) {
                o00000Oo2 = O00000o.O00000oO(o00000Oo, o0000o0O);
            } else if (O000000o2 != 4) {
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextBoolean();
            }
        }
        O0000Oo0 o0000Oo0 = new O0000Oo0(str, o0000o00, o00000oo, o00000Oo2, z);
        return o0000Oo0;
    }
}
