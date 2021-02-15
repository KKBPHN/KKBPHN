package com.airbnb.lottie.O00000o0;

import android.graphics.Color;
import android.graphics.Rect;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.model.O000000o.C0099O0000OoO;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.O000000o.O0000Oo;
import com.airbnb.lottie.model.layer.Layer$LayerType;
import com.airbnb.lottie.model.layer.Layer$MatteType;
import com.airbnb.lottie.model.layer.O0000O0o;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.xiaomi.stat.d;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

/* renamed from: com.airbnb.lottie.O00000o0.O0000oOO reason: case insensitive filesystem */
public class C0042O0000oOO {
    private static final O000000o NAMES = O000000o.of("nm", "ind", "refId", "ty", "parent", "sw", "sh", "sc", "ks", "tt", "masksProperties", "shapes", "t", "ef", d.Y, d.n, "w", SupportedConfigFactory.CLOSE_BY_VIDEO, OnNativeInvokeListener.ARG_IP, "op", "tm", "cl", "hd");
    private static final O000000o O0O0o0O = O000000o.of(SupportedConfigFactory.CLOSE_BY_BURST_SHOOT, SupportedConfigFactory.CLOSE_BY_HHT);
    private static final O000000o O0O0o0o = O000000o.of("nm");

    private C0042O0000oOO() {
    }

    public static O0000O0o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O) {
        float f;
        ArrayList arrayList;
        float f2;
        O00000Oo o00000Oo2 = o00000Oo;
        C0064O0000o0O o0000o0O2 = o0000o0O;
        Layer$MatteType layer$MatteType = Layer$MatteType.NONE;
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        o00000Oo.beginObject();
        Float valueOf = Float.valueOf(1.0f);
        Float valueOf2 = Float.valueOf(0.0f);
        Layer$MatteType layer$MatteType2 = layer$MatteType;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        boolean z = false;
        Layer$LayerType layer$LayerType = null;
        String str = null;
        C0100O0000Ooo o0000Ooo = null;
        O0000Oo o0000Oo = null;
        C0099O0000OoO o0000OoO = null;
        com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo3 = null;
        float f3 = 0.0f;
        float f4 = 0.0f;
        long j = -1;
        float f5 = 1.0f;
        float f6 = 0.0f;
        long j2 = 0;
        Object obj = null;
        String str2 = "UNSET";
        while (o00000Oo.hasNext()) {
            switch (o00000Oo2.O000000o(NAMES)) {
                case 0:
                    str2 = o00000Oo.nextString();
                    continue;
                case 1:
                    j2 = (long) o00000Oo.nextInt();
                    continue;
                case 2:
                    str = o00000Oo.nextString();
                    continue;
                case 3:
                    int nextInt = o00000Oo.nextInt();
                    if (nextInt >= Layer$LayerType.UNKNOWN.ordinal()) {
                        layer$LayerType = Layer$LayerType.UNKNOWN;
                        break;
                    } else {
                        layer$LayerType = Layer$LayerType.values()[nextInt];
                        continue;
                    }
                case 4:
                    j = (long) o00000Oo.nextInt();
                    continue;
                case 5:
                    i = (int) (((float) o00000Oo.nextInt()) * O0000OOo.O00o0O0O());
                    continue;
                case 6:
                    i2 = (int) (((float) o00000Oo.nextInt()) * O0000OOo.O00o0O0O());
                    continue;
                case 7:
                    i3 = Color.parseColor(o00000Oo.nextString());
                    continue;
                case 8:
                    o0000Ooo = O00000o0.O000000o(o00000Oo, o0000o0O);
                    continue;
                case 9:
                    layer$MatteType2 = Layer$MatteType.values()[o00000Oo.nextInt()];
                    o0000o0O2.O0000Ooo(1);
                    continue;
                case 10:
                    o00000Oo.beginArray();
                    while (o00000Oo.hasNext()) {
                        arrayList2.add(C0044O0000oo.O000000o(o00000Oo, o0000o0O));
                    }
                    o0000o0O2.O0000Ooo(arrayList2.size());
                    break;
                case 11:
                    o00000Oo.beginArray();
                    while (o00000Oo.hasNext()) {
                        com.airbnb.lottie.model.content.O00000Oo O000000o2 = O0000O0o.O000000o(o00000Oo, o0000o0O);
                        if (O000000o2 != null) {
                            arrayList3.add(O000000o2);
                        }
                    }
                    break;
                case 12:
                    o00000Oo.beginObject();
                    while (o00000Oo.hasNext()) {
                        int O000000o3 = o00000Oo2.O000000o(O0O0o0O);
                        if (O000000o3 == 0) {
                            o0000Oo = O00000o.O00000o(o00000Oo, o0000o0O);
                        } else if (O000000o3 != 1) {
                            o00000Oo.O00o0O0();
                            o00000Oo.skipValue();
                        } else {
                            o00000Oo.beginArray();
                            if (o00000Oo.hasNext()) {
                                o0000OoO = O00000Oo.O000000o(o00000Oo, o0000o0O);
                            }
                            while (o00000Oo.hasNext()) {
                                o00000Oo.skipValue();
                            }
                            o00000Oo.endArray();
                        }
                    }
                    o00000Oo.endObject();
                    continue;
                case 13:
                    o00000Oo.beginArray();
                    ArrayList arrayList4 = new ArrayList();
                    while (o00000Oo.hasNext()) {
                        o00000Oo.beginObject();
                        while (o00000Oo.hasNext()) {
                            if (o00000Oo2.O000000o(O0O0o0o) != 0) {
                                o00000Oo.O00o0O0();
                                o00000Oo.skipValue();
                            } else {
                                arrayList4.add(o00000Oo.nextString());
                            }
                        }
                        o00000Oo.endObject();
                    }
                    o00000Oo.endArray();
                    StringBuilder sb = new StringBuilder();
                    sb.append("Lottie doesn't support layer effects. If you are using them for  fills, strokes, trim paths etc. then try adding them directly as contents  in your shape. Found: ");
                    sb.append(arrayList4);
                    o0000o0O2.O0000oo(sb.toString());
                    continue;
                case 14:
                    f5 = (float) o00000Oo.nextDouble();
                    continue;
                case 15:
                    f4 = (float) o00000Oo.nextDouble();
                    continue;
                case 16:
                    i4 = (int) (((float) o00000Oo.nextInt()) * O0000OOo.O00o0O0O());
                    continue;
                case 17:
                    i5 = (int) (((float) o00000Oo.nextInt()) * O0000OOo.O00o0O0O());
                    continue;
                case 18:
                    f3 = (float) o00000Oo.nextDouble();
                    continue;
                case 19:
                    f6 = (float) o00000Oo.nextDouble();
                    continue;
                case 20:
                    o00000Oo3 = O00000o.O000000o(o00000Oo2, o0000o0O2, false);
                    continue;
                case 21:
                    obj = o00000Oo.nextString();
                    continue;
                case 22:
                    z = o00000Oo.nextBoolean();
                    continue;
                default:
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    continue;
            }
            o00000Oo.endArray();
        }
        o00000Oo.endObject();
        float f7 = f3 / f5;
        float f8 = f6 / f5;
        ArrayList arrayList5 = new ArrayList();
        if (f7 > 0.0f) {
            C0054O000000o o000000o = r0;
            f = f5;
            arrayList = arrayList5;
            C0054O000000o o000000o2 = new C0054O000000o(o0000o0O, valueOf2, valueOf2, null, 0.0f, Float.valueOf(f7));
            arrayList.add(o000000o);
            f2 = 0.0f;
        } else {
            f = f5;
            arrayList = arrayList5;
            f2 = 0.0f;
        }
        if (f8 <= f2) {
            f8 = o0000o0O.O00O0o0O();
        }
        C0064O0000o0O o0000o0O3 = o0000o0O;
        C0054O000000o o000000o3 = new C0054O000000o(o0000o0O3, valueOf, valueOf, null, f7, Float.valueOf(f8));
        arrayList.add(o000000o3);
        C0054O000000o o000000o4 = new C0054O000000o(o0000o0O3, valueOf2, valueOf2, null, f8, Float.valueOf(Float.MAX_VALUE));
        arrayList.add(o000000o4);
        if (str2.endsWith(".ai") || "ai".equals(obj)) {
            o0000o0O2.O0000oo("Convert your Illustrator layers to shape layers.");
        }
        O0000O0o o0000O0o = new O0000O0o(arrayList3, o0000o0O, str2, j2, layer$LayerType, j, str, arrayList2, o0000Ooo, i, i2, i3, f, f4, i4, i5, o0000Oo, o0000OoO, arrayList, layer$MatteType2, o00000Oo3, z);
        return o0000O0o;
    }

    public static O0000O0o O00000oO(C0064O0000o0O o0000o0O) {
        C0064O0000o0O o0000o0O2 = o0000o0O;
        Rect bounds = o0000o0O.getBounds();
        List emptyList = Collections.emptyList();
        Layer$LayerType layer$LayerType = Layer$LayerType.PRE_COMP;
        List emptyList2 = Collections.emptyList();
        C0100O0000Ooo o0000Ooo = r4;
        C0100O0000Ooo o0000Ooo2 = new C0100O0000Ooo();
        O0000O0o o0000O0o = new O0000O0o(emptyList, o0000o0O2, "__container", -1, layer$LayerType, -1, null, emptyList2, o0000Ooo, 0, 0, 0, 0.0f, 0.0f, bounds.width(), bounds.height(), null, null, Collections.emptyList(), Layer$MatteType.NONE, null, false);
        return o0000O0o;
    }
}
