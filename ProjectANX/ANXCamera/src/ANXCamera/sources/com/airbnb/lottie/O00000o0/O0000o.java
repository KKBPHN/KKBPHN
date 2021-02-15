package com.airbnb.lottie.O00000o0;

import android.graphics.Color;
import android.graphics.PointF;
import androidx.annotation.ColorInt;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import java.util.ArrayList;
import java.util.List;

class O0000o {
    private static final O000000o O0O0Ooo = O000000o.of("x", "y");

    private O0000o() {
    }

    static PointF O00000Oo(O00000Oo o00000Oo, float f) {
        int i = C0039O0000o0o.O0O0OoO[o00000Oo.peek().ordinal()];
        if (i == 1) {
            return O00000oO(o00000Oo, f);
        }
        if (i == 2) {
            return O00000o(o00000Oo, f);
        }
        if (i == 3) {
            return O00000oo(o00000Oo, f);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown point starts with ");
        sb.append(o00000Oo.peek());
        throw new IllegalArgumentException(sb.toString());
    }

    static float O00000o(O00000Oo o00000Oo) {
        JsonReader$Token peek = o00000Oo.peek();
        int i = C0039O0000o0o.O0O0OoO[peek.ordinal()];
        if (i == 1) {
            return (float) o00000Oo.nextDouble();
        }
        if (i == 2) {
            o00000Oo.beginArray();
            float nextDouble = (float) o00000Oo.nextDouble();
            while (o00000Oo.hasNext()) {
                o00000Oo.skipValue();
            }
            o00000Oo.endArray();
            return nextDouble;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unknown value for token of type ");
        sb.append(peek);
        throw new IllegalArgumentException(sb.toString());
    }

    private static PointF O00000o(O00000Oo o00000Oo, float f) {
        o00000Oo.beginArray();
        float nextDouble = (float) o00000Oo.nextDouble();
        float nextDouble2 = (float) o00000Oo.nextDouble();
        while (o00000Oo.peek() != JsonReader$Token.END_ARRAY) {
            o00000Oo.skipValue();
        }
        o00000Oo.endArray();
        return new PointF(nextDouble * f, nextDouble2 * f);
    }

    @ColorInt
    static int O00000o0(O00000Oo o00000Oo) {
        o00000Oo.beginArray();
        int nextDouble = (int) (o00000Oo.nextDouble() * 255.0d);
        int nextDouble2 = (int) (o00000Oo.nextDouble() * 255.0d);
        int nextDouble3 = (int) (o00000Oo.nextDouble() * 255.0d);
        while (o00000Oo.hasNext()) {
            o00000Oo.skipValue();
        }
        o00000Oo.endArray();
        return Color.argb(255, nextDouble, nextDouble2, nextDouble3);
    }

    static List O00000o0(O00000Oo o00000Oo, float f) {
        ArrayList arrayList = new ArrayList();
        o00000Oo.beginArray();
        while (o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY) {
            o00000Oo.beginArray();
            arrayList.add(O00000Oo(o00000Oo, f));
            o00000Oo.endArray();
        }
        o00000Oo.endArray();
        return arrayList;
    }

    private static PointF O00000oO(O00000Oo o00000Oo, float f) {
        float nextDouble = (float) o00000Oo.nextDouble();
        float nextDouble2 = (float) o00000Oo.nextDouble();
        while (o00000Oo.hasNext()) {
            o00000Oo.skipValue();
        }
        return new PointF(nextDouble * f, nextDouble2 * f);
    }

    private static PointF O00000oo(O00000Oo o00000Oo, float f) {
        o00000Oo.beginObject();
        float f2 = 0.0f;
        float f3 = 0.0f;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(O0O0Ooo);
            if (O000000o2 == 0) {
                f2 = O00000o(o00000Oo);
            } else if (O000000o2 != 1) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                f3 = O00000o(o00000Oo);
            }
        }
        o00000Oo.endObject();
        return new PointF(f2 * f, f3 * f);
    }
}
