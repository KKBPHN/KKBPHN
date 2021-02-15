package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.O000000o.O00000o0;
import com.airbnb.lottie.model.content.C0104O00000oO;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.ShapeStroke$LineCapType;
import com.airbnb.lottie.model.content.ShapeStroke$LineJoinType;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;

class O0000o0 {
    private static O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_HDR, SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "t", "s", "e", "w", "lc", "lj", "ml", "hd", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);
    private static final O000000o O0O0OOo = O000000o.of("p", SupportedConfigFactory.CLOSE_BY_FILTER);
    private static final O000000o O0O0Oo0 = O000000o.of("n", "v");

    private O0000o0() {
    }

    static C0104O00000oO O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        O00000Oo o00000Oo2 = o00000Oo;
        C0064O0000o0O o0000o0O2 = o0000o0O;
        ArrayList arrayList = new ArrayList();
        float f = 0.0f;
        String str = null;
        GradientType gradientType = null;
        O00000o0 o00000o0 = null;
        O00000o o00000o = null;
        C0098O00000oo o00000oo = null;
        C0098O00000oo o00000oo2 = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        ShapeStroke$LineCapType shapeStroke$LineCapType = null;
        ShapeStroke$LineJoinType shapeStroke$LineJoinType = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo4 = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo2.O000000o(NAMES)) {
                case 0:
                    str = o00000Oo.nextString();
                    break;
                case 1:
                    int i = -1;
                    o00000Oo.beginObject();
                    while (o00000Oo.hasNext()) {
                        int O000000o2 = o00000Oo2.O000000o(O0O0OOo);
                        O00000o0 o00000o02 = o00000o0;
                        if (O000000o2 == 0) {
                            i = o00000Oo.nextInt();
                        } else if (O000000o2 != 1) {
                            o00000Oo.O00o0O0();
                            o00000Oo.skipValue();
                        } else {
                            o00000o0 = O00000o.O000000o(o00000Oo2, o0000o0O2, i);
                        }
                        o00000o0 = o00000o02;
                    }
                    O00000o0 o00000o03 = o00000o0;
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
                    o00000Oo3 = O00000o.O00000oO(o00000Oo, o0000o0O);
                    break;
                case 7:
                    shapeStroke$LineCapType = ShapeStroke$LineCapType.values()[o00000Oo.nextInt() - 1];
                    break;
                case 8:
                    shapeStroke$LineJoinType = ShapeStroke$LineJoinType.values()[o00000Oo.nextInt() - 1];
                    break;
                case 9:
                    f = (float) o00000Oo.nextDouble();
                    break;
                case 10:
                    z = o00000Oo.nextBoolean();
                    break;
                case 11:
                    o00000Oo.beginArray();
                    while (o00000Oo.hasNext()) {
                        o00000Oo.beginObject();
                        String str2 = null;
                        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo5 = null;
                        while (o00000Oo.hasNext()) {
                            int O000000o3 = o00000Oo2.O000000o(O0O0Oo0);
                            com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo6 = o00000Oo4;
                            if (O000000o3 != 0) {
                                if (O000000o3 != 1) {
                                    o00000Oo.O00o0O0();
                                    o00000Oo.skipValue();
                                } else {
                                    o00000Oo5 = O00000o.O00000oO(o00000Oo, o0000o0O);
                                }
                                o00000Oo4 = o00000Oo6;
                            } else {
                                str2 = o00000Oo.nextString();
                            }
                        }
                        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo7 = o00000Oo4;
                        o00000Oo.endObject();
                        if (str2.equals(SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT)) {
                            o00000Oo4 = o00000Oo5;
                        } else {
                            if (str2.equals(SupportedConfigFactory.CLOSE_BY_BURST_SHOOT) || str2.equals(SupportedConfigFactory.CLOSE_BY_HDR)) {
                                o0000o0O2.O0000oO0(true);
                                arrayList.add(o00000Oo5);
                            }
                            o00000Oo4 = o00000Oo7;
                        }
                    }
                    com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo8 = o00000Oo4;
                    o00000Oo.endArray();
                    if (arrayList.size() == 1) {
                        arrayList.add(arrayList.get(0));
                    }
                    o00000Oo4 = o00000Oo8;
                    break;
                default:
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    break;
            }
        }
        C0104O00000oO o00000oO = new C0104O00000oO(str, gradientType, o00000o0, o00000o, o00000oo, o00000oo2, o00000Oo3, shapeStroke$LineCapType, shapeStroke$LineJoinType, f, arrayList, o00000Oo4, z);
        return o00000oO;
    }
}
