package com.airbnb.lottie.O00000o0;

import android.graphics.PointF;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.C0059O0000OoO;
import com.airbnb.lottie.model.O000000o.C0097O00000oO;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.O000000o.O0000O0o;
import com.airbnb.lottie.model.O000000o.O0000Oo0;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.xiaomi.stat.d;
import java.util.List;

public class O00000o0 {
    private static O000000o NAMES = O000000o.of(SupportedConfigFactory.CLOSE_BY_HHT, "p", "s", "rz", "r", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "so", "eo", d.af, "sa");
    private static O000000o O0O0OO0 = O000000o.of(SupportedConfigFactory.CLOSE_BY_FILTER);

    private O00000o0() {
    }

    public static C0100O0000Ooo O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        boolean z;
        O00000Oo o00000Oo2 = o00000Oo;
        C0064O0000o0O o0000o0O2 = o0000o0O;
        boolean z2 = false;
        boolean z3 = o00000Oo.peek() == JsonReader$Token.BEGIN_OBJECT;
        if (z3) {
            o00000Oo.beginObject();
        }
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        C0097O00000oO o00000oO = null;
        O0000o00 o0000o00 = null;
        O0000O0o o0000O0o = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo4 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo5 = null;
        O00000o o00000o = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo6 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo7 = null;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo2.O000000o(NAMES)) {
                case 0:
                    boolean z4 = z2;
                    o00000Oo.beginObject();
                    while (o00000Oo.hasNext()) {
                        if (o00000Oo2.O000000o(O0O0OO0) != 0) {
                            o00000Oo.O00o0O0();
                            o00000Oo.skipValue();
                        } else {
                            o00000oO = O000000o.O000000o(o00000Oo, o0000o0O);
                        }
                    }
                    o00000Oo.endObject();
                    z2 = z4;
                    continue;
                case 1:
                    boolean z5 = z2;
                    o0000o00 = O000000o.O00000Oo(o00000Oo, o0000o0O);
                    continue;
                case 2:
                    boolean z6 = z2;
                    o0000O0o = O00000o.O0000OOo(o00000Oo, o0000o0O);
                    continue;
                case 3:
                    o0000o0O2.O0000oo("Lottie doesn't support 3D layers.");
                    break;
                case 4:
                    break;
                case 5:
                    o00000o = O00000o.O00000oo(o00000Oo, o0000o0O);
                    continue;
                case 6:
                    o00000Oo6 = O00000o.O000000o(o00000Oo2, o0000o0O2, z2);
                    continue;
                case 7:
                    o00000Oo7 = O00000o.O000000o(o00000Oo2, o0000o0O2, z2);
                    continue;
                case 8:
                    o00000Oo4 = O00000o.O000000o(o00000Oo2, o0000o0O2, z2);
                    continue;
                case 9:
                    o00000Oo5 = O00000o.O000000o(o00000Oo2, o0000o0O2, z2);
                    continue;
                default:
                    boolean z7 = z2;
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    continue;
            }
            com.airbnb.lottie.model.O000000o.O00000Oo O000000o2 = O00000o.O000000o(o00000Oo2, o0000o0O2, z2);
            if (O000000o2.getKeyframes().isEmpty()) {
                C0054O000000o o000000o = r1;
                List keyframes = O000000o2.getKeyframes();
                C0054O000000o o000000o2 = new C0054O000000o(o0000o0O, Float.valueOf(0.0f), Float.valueOf(0.0f), null, 0.0f, Float.valueOf(o0000o0O.O00O0o0O()));
                keyframes.add(o000000o);
            } else if (((C0054O000000o) O000000o2.getKeyframes().get(0)).startValue == null) {
                List keyframes2 = O000000o2.getKeyframes();
                C0054O000000o o000000o3 = new C0054O000000o(o0000o0O, Float.valueOf(0.0f), Float.valueOf(0.0f), null, 0.0f, Float.valueOf(o0000o0O.O00O0o0O()));
                z = false;
                keyframes2.set(0, o000000o3);
                z2 = z;
                o00000Oo3 = O000000o2;
            }
            z = false;
            z2 = z;
            o00000Oo3 = O000000o2;
        }
        if (z3) {
            o00000Oo.endObject();
        }
        C0097O00000oO o00000oO2 = O000000o(o00000oO) ? null : o00000oO;
        O0000o00 o0000o002 = O000000o(o0000o00) ? null : o0000o00;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo8 = O000000o(o00000Oo3) ? null : o00000Oo3;
        if (O000000o(o0000O0o)) {
            o0000O0o = null;
        }
        C0100O0000Ooo o0000Ooo = new C0100O0000Ooo(o00000oO2, o0000o002, o0000O0o, o00000Oo8, o00000o, o00000Oo6, o00000Oo7, O00000o0(o00000Oo4) ? null : o00000Oo4, O00000Oo(o00000Oo5) ? null : o00000Oo5);
        return o0000Ooo;
    }

    private static boolean O000000o(com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo) {
        return o00000Oo == null || (o00000Oo.isStatic() && ((Float) ((C0054O000000o) o00000Oo.getKeyframes().get(0)).startValue).floatValue() == 0.0f);
    }

    private static boolean O000000o(C0097O00000oO o00000oO) {
        return o00000oO == null || (o00000oO.isStatic() && ((PointF) ((C0054O000000o) o00000oO.getKeyframes().get(0)).startValue).equals(0.0f, 0.0f));
    }

    private static boolean O000000o(O0000O0o o0000O0o) {
        return o0000O0o == null || (o0000O0o.isStatic() && ((C0059O0000OoO) ((C0054O000000o) o0000O0o.getKeyframes().get(0)).startValue).equals(1.0f, 1.0f));
    }

    private static boolean O000000o(O0000o00 o0000o00) {
        return o0000o00 == null || (!(o0000o00 instanceof O0000Oo0) && o0000o00.isStatic() && ((PointF) ((C0054O000000o) o0000o00.getKeyframes().get(0)).startValue).equals(0.0f, 0.0f));
    }

    private static boolean O00000Oo(com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo) {
        return o00000Oo == null || (o00000Oo.isStatic() && ((Float) ((C0054O000000o) o00000Oo.getKeyframes().get(0)).startValue).floatValue() == 0.0f);
    }

    private static boolean O00000o0(com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo) {
        return o00000Oo == null || (o00000Oo.isStatic() && ((Float) ((C0054O000000o) o00000Oo.getKeyframes().get(0)).startValue).floatValue() == 0.0f);
    }
}
