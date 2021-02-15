package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.model.O00000o0;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;

/* renamed from: com.airbnb.lottie.O00000o0.O0000OoO reason: case insensitive filesystem */
class C0036O0000OoO {
    private static final O000000o NAMES = O000000o.of("fFamily", "fName", "fStyle", "ascent");

    private C0036O0000OoO() {
    }

    static O00000o0 O00000Oo(O00000Oo o00000Oo) {
        o00000Oo.beginObject();
        String str = null;
        String str2 = null;
        float f = 0.0f;
        String str3 = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                str3 = o00000Oo.nextString();
            } else if (O000000o2 == 2) {
                str2 = o00000Oo.nextString();
            } else if (O000000o2 != 3) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                f = (float) o00000Oo.nextDouble();
            }
        }
        o00000Oo.endObject();
        return new O00000o0(str, str3, str2, f);
    }
}
