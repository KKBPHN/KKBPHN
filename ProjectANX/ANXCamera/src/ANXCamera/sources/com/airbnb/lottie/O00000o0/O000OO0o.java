package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.content.O0000o;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

class O000OO0o {
    private static O000000o NAMES = O000000o.of("s", "e", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "nm", "m", "hd");

    private O000OO0o() {
    }

    static O0000o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z = false;
        String str = null;
        ShapeTrimPath$Type shapeTrimPath$Type = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo4 = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                o00000Oo2 = O00000o.O000000o(o00000Oo, o0000o0O, false);
            } else if (O000000o2 == 1) {
                o00000Oo3 = O00000o.O000000o(o00000Oo, o0000o0O, false);
            } else if (O000000o2 == 2) {
                o00000Oo4 = O00000o.O000000o(o00000Oo, o0000o0O, false);
            } else if (O000000o2 == 3) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 4) {
                shapeTrimPath$Type = ShapeTrimPath$Type.O000OOo0(o00000Oo.nextInt());
            } else if (O000000o2 != 5) {
                o00000Oo.skipValue();
            } else {
                z = o00000Oo.nextBoolean();
            }
        }
        O0000o o0000o = new O0000o(str, shapeTrimPath$Type, o00000Oo2, o00000Oo3, o00000Oo4, z);
        return o0000o;
    }
}
