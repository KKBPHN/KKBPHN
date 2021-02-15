package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.O00000oO.C0059O0000OoO;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O00000Oo;

public class O00oOoOo implements O000OO {
    public static final O00oOoOo INSTANCE = new O00oOoOo();

    private O00oOoOo() {
    }

    public C0059O0000OoO O000000o(O00000Oo o00000Oo, float f) {
        boolean z = o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY;
        if (z) {
            o00000Oo.beginArray();
        }
        float nextDouble = (float) o00000Oo.nextDouble();
        float nextDouble2 = (float) o00000Oo.nextDouble();
        while (o00000Oo.hasNext()) {
            o00000Oo.skipValue();
        }
        if (z) {
            o00000Oo.endArray();
        }
        return new C0059O0000OoO((nextDouble / 100.0f) * f, (nextDouble2 / 100.0f) * f);
    }
}
