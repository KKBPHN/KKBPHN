package com.airbnb.lottie.O00000o0;

import android.graphics.PointF;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O00000Oo;

/* renamed from: com.airbnb.lottie.O00000o0.O00oOooo reason: case insensitive filesystem */
public class C0052O00oOooo implements O000OO {
    public static final C0052O00oOooo INSTANCE = new C0052O00oOooo();

    private C0052O00oOooo() {
    }

    public PointF O000000o(O00000Oo o00000Oo, float f) {
        JsonReader$Token peek = o00000Oo.peek();
        if (peek == JsonReader$Token.BEGIN_ARRAY) {
            return O0000o.O00000Oo(o00000Oo, f);
        }
        if (peek == JsonReader$Token.BEGIN_OBJECT) {
            return O0000o.O00000Oo(o00000Oo, f);
        }
        if (peek == JsonReader$Token.NUMBER) {
            PointF pointF = new PointF(((float) o00000Oo.nextDouble()) * f, ((float) o00000Oo.nextDouble()) * f);
            while (o00000Oo.hasNext()) {
                o00000Oo.skipValue();
            }
            return pointF;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot convert json to point. Next token is ");
        sb.append(peek);
        throw new IllegalArgumentException(sb.toString());
    }
}
