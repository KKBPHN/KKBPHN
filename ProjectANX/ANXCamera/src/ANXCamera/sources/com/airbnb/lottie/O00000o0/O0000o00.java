package com.airbnb.lottie.O00000o0;

import android.graphics.Path.FillType;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O00000o0;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.O00000o;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;

class O0000o00 {
    private static final O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_HDR, SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "t", "s", "e", "r", "hd");
    private static final O000000o O0O0OOo = O000000o.of("p", SupportedConfigFactory.CLOSE_BY_FILTER);

    private O0000o00() {
    }

    static O00000o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        FillType fillType = FillType.WINDING;
        String str = null;
        GradientType gradientType = null;
        O00000o0 o00000o0 = null;
        com.airbnb.lottie.model.O000000o.O00000o o00000o = null;
        C0098O00000oo o00000oo = null;
        C0098O00000oo o00000oo2 = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo.O000000o(NAMES)) {
                case 0:
                    str = o00000Oo.nextString();
                    break;
                case 1:
                    int i = -1;
                    o00000Oo.beginObject();
                    while (o00000Oo.hasNext()) {
                        int O000000o2 = o00000Oo.O000000o(O0O0OOo);
                        if (O000000o2 == 0) {
                            i = o00000Oo.nextInt();
                        } else if (O000000o2 != 1) {
                            o00000Oo.O00o0O0();
                            o00000Oo.skipValue();
                        } else {
                            o00000o0 = O00000o.O000000o(o00000Oo, o0000o0O, i);
                        }
                    }
                    o00000Oo.endObject();
                    break;
                case 2:
                    o00000o = O00000o.O00000oo(o00000Oo, o0000o0O);
                    break;
                case 3:
                    gradientType = o00000Oo.nextInt() == 1 ? GradientType.LINEAR : GradientType.RADIAL;
                    break;
                case 4:
                    o00000oo = O00000o.O0000O0o(o00000Oo, o0000o0O);
                    break;
                case 5:
                    o00000oo2 = O00000o.O0000O0o(o00000Oo, o0000o0O);
                    break;
                case 6:
                    fillType = o00000Oo.nextInt() == 1 ? FillType.WINDING : FillType.EVEN_ODD;
                    break;
                case 7:
                    z = o00000Oo.nextBoolean();
                    break;
                default:
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    break;
            }
        }
        O00000o o00000o2 = new O00000o(str, gradientType, fillType, o00000o0, o00000o, o00000oo, o00000oo2, null, null, z);
        return o00000o2;
    }
}
