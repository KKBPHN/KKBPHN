package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.model.content.O0000OOo;
import com.airbnb.lottie.model.content.PolystarShape$Type;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.xiaomi.stat.d;

class O000O00o {
    private static final O000000o NAMES = O000000o.of("nm", "sy", "pt", "p", "r", "or", d.l, "ir", "is", "hd");

    private O000O00o() {
    }

    static O0000OOo O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z = false;
        String str = null;
        PolystarShape$Type polystarShape$Type = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        O0000o00 o0000o00 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo4 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo5 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo6 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo7 = null;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo.O000000o(NAMES)) {
                case 0:
                    str = o00000Oo.nextString();
                    break;
                case 1:
                    polystarShape$Type = PolystarShape$Type.O000OOo(o00000Oo.nextInt());
                    break;
                case 2:
                    o00000Oo2 = O00000o.O000000o(o00000Oo, o0000o0O, false);
                    break;
                case 3:
                    o0000o00 = O000000o.O00000Oo(o00000Oo, o0000o0O);
                    break;
                case 4:
                    o00000Oo3 = O00000o.O000000o(o00000Oo, o0000o0O, false);
                    break;
                case 5:
                    o00000Oo5 = O00000o.O00000oO(o00000Oo, o0000o0O);
                    break;
                case 6:
                    o00000Oo7 = O00000o.O000000o(o00000Oo, o0000o0O, false);
                    break;
                case 7:
                    o00000Oo4 = O00000o.O00000oO(o00000Oo, o0000o0O);
                    break;
                case 8:
                    o00000Oo6 = O00000o.O000000o(o00000Oo, o0000o0O, false);
                    break;
                case 9:
                    z = o00000Oo.nextBoolean();
                    break;
                default:
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    break;
            }
        }
        O0000OOo o0000OOo = new O0000OOo(str, polystarShape$Type, o00000Oo2, o0000o00, o00000Oo3, o00000Oo4, o00000Oo5, o00000Oo6, o00000Oo7, z);
        return o0000OOo;
    }
}
