package com.airbnb.lottie.O00000o0;

import android.graphics.Path.FillType;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.content.C0107O0000Ooo;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

class O000O0o {
    private static final O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "fillEnabled", "r", "hd");

    private O000O0o() {
    }

    static C0107O0000Ooo O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z = false;
        boolean z2 = false;
        int i = 1;
        String str = null;
        com.airbnb.lottie.model.O000000o.O000000o o000000o = null;
        O00000o o00000o = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                o000000o = O00000o.O00000o0(o00000Oo, o0000o0O);
            } else if (O000000o2 == 2) {
                o00000o = O00000o.O00000oo(o00000Oo, o0000o0O);
            } else if (O000000o2 == 3) {
                z = o00000Oo.nextBoolean();
            } else if (O000000o2 == 4) {
                i = o00000Oo.nextInt();
            } else if (O000000o2 != 5) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                z2 = o00000Oo.nextBoolean();
            }
        }
        C0107O0000Ooo o0000Ooo = new C0107O0000Ooo(str, z, i == 1 ? FillType.WINDING : FillType.EVEN_ODD, o000000o, o00000o, z2);
        return o0000Ooo;
    }
}
