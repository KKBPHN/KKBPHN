package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.model.O000000o.C0097O00000oO;
import com.airbnb.lottie.model.O000000o.O0000Oo0;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;

public class O000000o {
    private static com.airbnb.lottie.parser.moshi.O000000o NAMES = com.airbnb.lottie.parser.moshi.O000000o.of(SupportedConfigFactory.CLOSE_BY_FILTER, "x", "y");

    private O000000o() {
    }

    public static C0097O00000oO O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        ArrayList arrayList = new ArrayList();
        if (o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY) {
            o00000Oo.beginArray();
            while (o00000Oo.hasNext()) {
                arrayList.add(C0047O0000ooo.O000000o(o00000Oo, o0000o0O));
            }
            o00000Oo.endArray();
            C0040O0000oO.O00000o0(arrayList);
        } else {
            arrayList.add(new C0054O000000o(O0000o.O00000Oo(o00000Oo, O0000OOo.O00o0O0O())));
        }
        return new C0097O00000oO(arrayList);
    }

    static O0000o00 O00000Oo(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        o00000Oo.beginObject();
        C0097O00000oO o00000oO = null;
        boolean z = false;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        while (o00000Oo.peek() != JsonReader$Token.END_OBJECT) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 != 0) {
                if (O000000o2 != 1) {
                    if (O000000o2 != 2) {
                        o00000Oo.O00o0O0();
                        o00000Oo.skipValue();
                    } else if (o00000Oo.peek() != JsonReader$Token.STRING) {
                        o00000Oo3 = O00000o.O00000oO(o00000Oo, o0000o0O);
                    }
                } else if (o00000Oo.peek() != JsonReader$Token.STRING) {
                    o00000Oo2 = O00000o.O00000oO(o00000Oo, o0000o0O);
                }
                o00000Oo.skipValue();
                z = true;
            } else {
                o00000oO = O000000o(o00000Oo, o0000o0O);
            }
        }
        o00000Oo.endObject();
        if (z) {
            o0000o0O.O0000oo("Lottie doesn't support expressions.");
        }
        return o00000oO != null ? o00000oO : new O0000Oo0(o00000Oo2, o00000Oo3);
    }
}
