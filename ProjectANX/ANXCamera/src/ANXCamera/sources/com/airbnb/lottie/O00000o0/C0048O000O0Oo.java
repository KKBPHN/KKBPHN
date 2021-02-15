package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.content.O0000Oo;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

/* renamed from: com.airbnb.lottie.O00000o0.O000O0Oo reason: case insensitive filesystem */
class C0048O000O0Oo {
    private static O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "tr", "hd");

    private C0048O000O0Oo() {
    }

    static O0000Oo O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z = false;
        String str = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        C0100O0000Ooo o0000Ooo = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                o00000Oo2 = O00000o.O000000o(o00000Oo, o0000o0O, false);
            } else if (O000000o2 == 2) {
                o00000Oo3 = O00000o.O000000o(o00000Oo, o0000o0O, false);
            } else if (O000000o2 == 3) {
                o0000Ooo = O00000o0.O000000o(o00000Oo, o0000o0O);
            } else if (O000000o2 != 4) {
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextBoolean();
            }
        }
        O0000Oo o0000Oo = new O0000Oo(str, o00000Oo2, o00000Oo3, o0000Ooo, z);
        return o0000Oo;
    }
}
