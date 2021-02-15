package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O00000o;
import com.airbnb.lottie.model.content.O0000o00;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.util.ArrayList;

class O0000Oo {
    private static final O000000o NAMES = O000000o.of("ch", "size", "w", "style", "fFamily", PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
    private static final O000000o O0O0OO = O000000o.of("shapes");

    private O0000Oo() {
    }

    static O00000o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        ArrayList arrayList = new ArrayList();
        o00000Oo.beginObject();
        String str = null;
        String str2 = null;
        double d = 0.0d;
        double d2 = 0.0d;
        char c = 0;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                c = o00000Oo.nextString().charAt(0);
            } else if (O000000o2 == 1) {
                d = o00000Oo.nextDouble();
            } else if (O000000o2 == 2) {
                d2 = o00000Oo.nextDouble();
            } else if (O000000o2 == 3) {
                str = o00000Oo.nextString();
            } else if (O000000o2 == 4) {
                str2 = o00000Oo.nextString();
            } else if (O000000o2 != 5) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                o00000Oo.beginObject();
                while (o00000Oo.hasNext()) {
                    if (o00000Oo.O000000o(O0O0OO) != 0) {
                        o00000Oo.O00o0O0();
                        o00000Oo.skipValue();
                    } else {
                        o00000Oo.beginArray();
                        while (o00000Oo.hasNext()) {
                            arrayList.add((O0000o00) O0000O0o.O000000o(o00000Oo, o0000o0O));
                        }
                        o00000Oo.endArray();
                    }
                }
                o00000Oo.endObject();
            }
        }
        o00000Oo.endObject();
        O00000o o00000o = new O00000o(arrayList, c, d, d2, str, str2);
        return o00000o;
    }
}
