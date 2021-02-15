package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: com.airbnb.lottie.O00000o0.O00000oO reason: case insensitive filesystem */
class C0034O00000oO {
    private static O000000o NAMES = O000000o.of("nm", "p", "s", "hd", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);

    private C0034O00000oO() {
    }

    static com.airbnb.lottie.model.content.O000000o O00000Oo(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, int i) {
        boolean z = i == 3;
        boolean z2 = false;
        String str = null;
        O0000o00 o0000o00 = null;
        C0098O00000oo o00000oo = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                o0000o00 = O000000o.O00000Oo(o00000Oo, o0000o0O);
            } else if (O000000o2 == 2) {
                o00000oo = O00000o.O0000O0o(o00000Oo, o0000o0O);
            } else if (O000000o2 == 3) {
                z2 = o00000Oo.nextBoolean();
            } else if (O000000o2 != 4) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextInt() == 3;
            }
        }
        com.airbnb.lottie.model.content.O000000o o000000o = new com.airbnb.lottie.model.content.O000000o(str, o0000o00, o00000oo, z, z2);
        return o000000o;
    }
}
