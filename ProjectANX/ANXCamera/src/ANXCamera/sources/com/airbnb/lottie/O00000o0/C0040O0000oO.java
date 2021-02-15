package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O000000o.O00000Oo.O0000o0;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.O00000o0.O0000oO reason: case insensitive filesystem */
class C0040O0000oO {
    static O000000o NAMES = O000000o.of(SupportedConfigFactory.CLOSE_BY_FILTER);

    private C0040O0000oO() {
    }

    static List O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, float f, O000OO o000oo) {
        ArrayList arrayList = new ArrayList();
        if (o00000Oo.peek() == JsonReader$Token.STRING) {
            o0000o0O.O0000oo("Lottie doesn't support expressions.");
            return arrayList;
        }
        o00000Oo.beginObject();
        while (o00000Oo.hasNext()) {
            if (o00000Oo.O000000o(NAMES) != 0) {
                o00000Oo.skipValue();
            } else if (o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY) {
                o00000Oo.beginArray();
                if (o00000Oo.peek() == JsonReader$Token.NUMBER) {
                    arrayList.add(C0041O0000oO0.O000000o(o00000Oo, o0000o0O, f, o000oo, false));
                } else {
                    while (o00000Oo.hasNext()) {
                        arrayList.add(C0041O0000oO0.O000000o(o00000Oo, o0000o0O, f, o000oo, true));
                    }
                }
                o00000Oo.endArray();
            } else {
                arrayList.add(C0041O0000oO0.O000000o(o00000Oo, o0000o0O, f, o000oo, false));
            }
        }
        o00000Oo.endObject();
        O00000o0(arrayList);
        return arrayList;
    }

    public static void O00000o0(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        while (true) {
            i = size - 1;
            if (i2 >= i) {
                break;
            }
            C0054O000000o o000000o = (C0054O000000o) list.get(i2);
            i2++;
            C0054O000000o o000000o2 = (C0054O000000o) list.get(i2);
            o000000o.O000oO0o = Float.valueOf(o000000o2.O000oO0O);
            if (o000000o.endValue == null) {
                Object obj = o000000o2.startValue;
                if (obj != null) {
                    o000000o.endValue = obj;
                    if (o000000o instanceof O0000o0) {
                        ((O0000o0) o000000o).O00o0OOO();
                    }
                }
            }
        }
        C0054O000000o o000000o3 = (C0054O000000o) list.get(i);
        if ((o000000o3.startValue == null || o000000o3.endValue == null) && list.size() > 1) {
            list.remove(o000000o3);
        }
    }
}
