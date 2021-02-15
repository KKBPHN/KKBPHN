package com.airbnb.lottie.O00000o0;

import android.graphics.Color;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O00000Oo;

/* renamed from: com.airbnb.lottie.O00000o0.O00000oo reason: case insensitive filesystem */
public class C0035O00000oo implements O000OO {
    public static final C0035O00000oo INSTANCE = new C0035O00000oo();

    private C0035O00000oo() {
    }

    public Integer O000000o(O00000Oo o00000Oo, float f) {
        boolean z = o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY;
        if (z) {
            o00000Oo.beginArray();
        }
        double nextDouble = o00000Oo.nextDouble();
        double nextDouble2 = o00000Oo.nextDouble();
        double nextDouble3 = o00000Oo.nextDouble();
        double nextDouble4 = o00000Oo.nextDouble();
        if (z) {
            o00000Oo.endArray();
        }
        if (nextDouble <= 1.0d && nextDouble2 <= 1.0d && nextDouble3 <= 1.0d && nextDouble4 <= 1.0d) {
            nextDouble *= 255.0d;
            nextDouble2 *= 255.0d;
            nextDouble3 *= 255.0d;
            nextDouble4 *= 255.0d;
        }
        return Integer.valueOf(Color.argb((int) nextDouble4, (int) nextDouble, (int) nextDouble2, (int) nextDouble3));
    }
}
