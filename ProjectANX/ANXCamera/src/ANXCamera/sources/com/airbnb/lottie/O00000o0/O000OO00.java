package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.content.C0109O0000o0o;
import com.airbnb.lottie.model.content.ShapeStroke$LineCapType;
import com.airbnb.lottie.model.content.ShapeStroke$LineJoinType;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;

class O000OO00 {
    private static O000000o NAMES = O000000o.of("nm", SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, "w", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, "lc", "lj", "ml", "hd", SupportedConfigFactory.CLOSE_BY_BURST_SHOOT);
    private static final O000000o O0O0Oo0 = O000000o.of("n", "v");

    private O000OO00() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:35:0x008d  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x009e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static C0109O0000o0o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        char c;
        int i;
        O00000Oo o00000Oo2 = o00000Oo;
        ArrayList arrayList = new ArrayList();
        float f = 0.0f;
        String str = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        com.airbnb.lottie.model.O000000o.O000000o o000000o = null;
        O00000o o00000o = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo4 = null;
        ShapeStroke$LineCapType shapeStroke$LineCapType = null;
        ShapeStroke$LineJoinType shapeStroke$LineJoinType = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            int i2 = 1;
            switch (o00000Oo2.O000000o(NAMES)) {
                case 0:
                    C0064O0000o0O o0000o0O2 = o0000o0O;
                    str = o00000Oo.nextString();
                    break;
                case 1:
                    C0064O0000o0O o0000o0O3 = o0000o0O;
                    o000000o = O00000o.O00000o0(o00000Oo, o0000o0O);
                    break;
                case 2:
                    C0064O0000o0O o0000o0O4 = o0000o0O;
                    o00000Oo4 = O00000o.O00000oO(o00000Oo, o0000o0O);
                    break;
                case 3:
                    C0064O0000o0O o0000o0O5 = o0000o0O;
                    o00000o = O00000o.O00000oo(o00000Oo, o0000o0O);
                    break;
                case 4:
                    C0064O0000o0O o0000o0O6 = o0000o0O;
                    shapeStroke$LineCapType = ShapeStroke$LineCapType.values()[o00000Oo.nextInt() - 1];
                    break;
                case 5:
                    C0064O0000o0O o0000o0O7 = o0000o0O;
                    shapeStroke$LineJoinType = ShapeStroke$LineJoinType.values()[o00000Oo.nextInt() - 1];
                    break;
                case 6:
                    C0064O0000o0O o0000o0O8 = o0000o0O;
                    f = (float) o00000Oo.nextDouble();
                    break;
                case 7:
                    C0064O0000o0O o0000o0O9 = o0000o0O;
                    z = o00000Oo.nextBoolean();
                    break;
                case 8:
                    o00000Oo.beginArray();
                    while (o00000Oo.hasNext()) {
                        o00000Oo.beginObject();
                        String str2 = null;
                        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo5 = null;
                        while (o00000Oo.hasNext()) {
                            int O000000o2 = o00000Oo2.O000000o(O0O0Oo0);
                            if (O000000o2 == 0) {
                                str2 = o00000Oo.nextString();
                            } else if (O000000o2 != i2) {
                                o00000Oo.O00o0O0();
                                o00000Oo.skipValue();
                            } else {
                                o00000Oo5 = O00000o.O00000oO(o00000Oo, o0000o0O);
                            }
                        }
                        o00000Oo.endObject();
                        int hashCode = str2.hashCode();
                        if (hashCode != 100) {
                            if (hashCode != 103) {
                                if (hashCode == 111 && str2.equals(SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT)) {
                                    c = 0;
                                    if (c == 0) {
                                        i = 1;
                                        if (c == 1 || c == 2) {
                                            o0000o0O.O0000oO0(true);
                                            arrayList.add(o00000Oo5);
                                        } else {
                                            C0064O0000o0O o0000o0O10 = o0000o0O;
                                        }
                                    } else {
                                        C0064O0000o0O o0000o0O11 = o0000o0O;
                                        i = 1;
                                        o00000Oo3 = o00000Oo5;
                                    }
                                    i2 = i;
                                }
                            } else if (str2.equals(SupportedConfigFactory.CLOSE_BY_HDR)) {
                                c = 2;
                                if (c == 0) {
                                }
                                i2 = i;
                            }
                        } else if (str2.equals(SupportedConfigFactory.CLOSE_BY_BURST_SHOOT)) {
                            c = 1;
                            if (c == 0) {
                            }
                            i2 = i;
                        }
                        c = 65535;
                        if (c == 0) {
                        }
                        i2 = i;
                    }
                    C0064O0000o0O o0000o0O12 = o0000o0O;
                    int i3 = i2;
                    o00000Oo.endArray();
                    if (arrayList.size() != i3) {
                        break;
                    } else {
                        arrayList.add(arrayList.get(0));
                        break;
                    }
                    break;
                default:
                    C0064O0000o0O o0000o0O13 = o0000o0O;
                    o00000Oo.skipValue();
                    break;
            }
        }
        C0109O0000o0o o0000o0o = new C0109O0000o0o(str, o00000Oo3, arrayList, o000000o, o00000o, o00000Oo4, shapeStroke$LineCapType, shapeStroke$LineJoinType, f, z);
        return o0000o0o;
    }
}
