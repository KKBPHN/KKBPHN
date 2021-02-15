package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0099O0000OoO;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.xiaomi.stat.d;

public class O00000Oo {
    private static O000000o O0O0O = O000000o.of(d.ak, "sc", "sw", "t");
    private static O000000o O0O0O0o = O000000o.of(SupportedConfigFactory.CLOSE_BY_HHT);

    private O00000Oo() {
    }

    public static C0099O0000OoO O000000o(com.airbnb.lottie.parser.moshi.O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        o00000Oo.beginObject();
        C0099O0000OoO o0000OoO = null;
        while (o00000Oo.hasNext()) {
            if (o00000Oo.O000000o(O0O0O0o) != 0) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                o0000OoO = O0000Oo(o00000Oo, o0000o0O);
            }
        }
        o00000Oo.endObject();
        return o0000OoO == null ? new C0099O0000OoO(null, null, null, null) : o0000OoO;
    }

    private static C0099O0000OoO O0000Oo(com.airbnb.lottie.parser.moshi.O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        o00000Oo.beginObject();
        com.airbnb.lottie.model.O000000o.O000000o o000000o = null;
        com.airbnb.lottie.model.O000000o.O000000o o000000o2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(O0O0O);
            if (O000000o2 == 0) {
                o000000o = O00000o.O00000o0(o00000Oo, o0000o0O);
            } else if (O000000o2 == 1) {
                o000000o2 = O00000o.O00000o0(o00000Oo, o0000o0O);
            } else if (O000000o2 == 2) {
                o00000Oo2 = O00000o.O00000oO(o00000Oo, o0000o0O);
            } else if (O000000o2 != 3) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                o00000Oo3 = O00000o.O00000oO(o00000Oo, o0000o0O);
            }
        }
        o00000Oo.endObject();
        return new C0099O0000OoO(o000000o, o000000o2, o00000Oo2, o00000Oo3);
    }
}
