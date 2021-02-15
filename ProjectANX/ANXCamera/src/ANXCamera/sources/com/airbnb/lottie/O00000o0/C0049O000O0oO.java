package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.content.O0000o00;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import java.util.ArrayList;

/* renamed from: com.airbnb.lottie.O00000o0.O000O0oO reason: case insensitive filesystem */
class C0049O000O0oO {
    private static O000000o NAMES = O000000o.of("nm", "hd", "it");

    private C0049O000O0oO() {
    }

    static O0000o00 O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        ArrayList arrayList = new ArrayList();
        String str = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 1) {
                z = o00000Oo.nextBoolean();
            } else if (O000000o2 != 2) {
                o00000Oo.skipValue();
            } else {
                o00000Oo.beginArray();
                while (o00000Oo.hasNext()) {
                    com.airbnb.lottie.model.content.O00000Oo O000000o3 = O0000O0o.O000000o(o00000Oo, o0000o0O);
                    if (O000000o3 != null) {
                        arrayList.add(O000000o3);
                    }
                }
                o00000Oo.endArray();
            }
        }
        return new O0000o00(str, arrayList, z);
    }
}
