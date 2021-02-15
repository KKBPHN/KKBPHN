package com.airbnb.lottie.O00000o0;

import android.graphics.Rect;
import androidx.collection.LongSparseArray;
import androidx.collection.SparseArrayCompat;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00O0Oo;
import com.airbnb.lottie.model.O00000o;
import com.airbnb.lottie.model.O00000o0;
import com.airbnb.lottie.model.O0000OOo;
import com.airbnb.lottie.model.layer.Layer$LayerType;
import com.airbnb.lottie.model.layer.O0000O0o;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

/* renamed from: com.airbnb.lottie.O00000o0.O0000oOo reason: case insensitive filesystem */
public class C0043O0000oOo {
    private static final O000000o NAMES = O000000o.of("w", SupportedConfigFactory.CLOSE_BY_VIDEO, OnNativeInvokeListener.ARG_IP, "op", "fr", "v", "layers", "assets", "fonts", "chars", "markers");
    static O000000o O0O0o = O000000o.of("id", "layers", "w", SupportedConfigFactory.CLOSE_BY_VIDEO, "p", "u");
    private static final O000000o O0O0oO = O000000o.of("cm", "tm", "dr");
    private static final O000000o O0O0oO0 = O000000o.of("list");

    private static void O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, SparseArrayCompat sparseArrayCompat) {
        o00000Oo.beginArray();
        while (o00000Oo.hasNext()) {
            O00000o O000000o2 = O0000Oo.O000000o(o00000Oo, o0000o0O);
            sparseArrayCompat.put(O000000o2.hashCode(), O000000o2);
        }
        o00000Oo.endArray();
    }

    private static void O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, List list) {
        o00000Oo.beginArray();
        while (o00000Oo.hasNext()) {
            String str = null;
            o00000Oo.beginObject();
            float f = 0.0f;
            float f2 = 0.0f;
            while (o00000Oo.hasNext()) {
                int O000000o2 = o00000Oo.O000000o(O0O0oO);
                if (O000000o2 == 0) {
                    str = o00000Oo.nextString();
                } else if (O000000o2 == 1) {
                    f = (float) o00000Oo.nextDouble();
                } else if (O000000o2 != 2) {
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                } else {
                    f2 = (float) o00000Oo.nextDouble();
                }
            }
            o00000Oo.endObject();
            list.add(new O0000OOo(str, f, f2));
        }
        o00000Oo.endArray();
    }

    private static void O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, List list, LongSparseArray longSparseArray) {
        o00000Oo.beginArray();
        int i = 0;
        while (o00000Oo.hasNext()) {
            O0000O0o O000000o2 = C0042O0000oOO.O000000o(o00000Oo, o0000o0O);
            if (O000000o2.getLayerType() == Layer$LayerType.IMAGE) {
                i++;
            }
            list.add(O000000o2);
            longSparseArray.put(O000000o2.getId(), O000000o2);
            if (i > 4) {
                StringBuilder sb = new StringBuilder();
                sb.append("You have ");
                sb.append(i);
                sb.append(" images. Lottie should primarily be used with shapes. If you are using Adobe Illustrator, convert the Illustrator layers to shape layers.");
                com.airbnb.lottie.O00000o.O00000o.warning(sb.toString());
            }
        }
        o00000Oo.endArray();
    }

    private static void O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, Map map, Map map2) {
        o00000Oo.beginArray();
        while (o00000Oo.hasNext()) {
            ArrayList arrayList = new ArrayList();
            LongSparseArray longSparseArray = new LongSparseArray();
            o00000Oo.beginObject();
            int i = 0;
            int i2 = 0;
            String str = null;
            String str2 = null;
            String str3 = null;
            while (o00000Oo.hasNext()) {
                int O000000o2 = o00000Oo.O000000o(O0O0o);
                if (O000000o2 == 0) {
                    str = o00000Oo.nextString();
                } else if (O000000o2 == 1) {
                    o00000Oo.beginArray();
                    while (o00000Oo.hasNext()) {
                        O0000O0o O000000o3 = C0042O0000oOO.O000000o(o00000Oo, o0000o0O);
                        longSparseArray.put(O000000o3.getId(), O000000o3);
                        arrayList.add(O000000o3);
                    }
                    o00000Oo.endArray();
                } else if (O000000o2 == 2) {
                    i = o00000Oo.nextInt();
                } else if (O000000o2 == 3) {
                    i2 = o00000Oo.nextInt();
                } else if (O000000o2 == 4) {
                    str2 = o00000Oo.nextString();
                } else if (O000000o2 != 5) {
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                } else {
                    str3 = o00000Oo.nextString();
                }
            }
            o00000Oo.endObject();
            if (str2 != null) {
                O00O0Oo o00O0Oo = new O00O0Oo(i, i2, str, str2, str3);
                map2.put(o00O0Oo.getId(), o00O0Oo);
            } else {
                map.put(str, arrayList);
            }
        }
        o00000Oo.endArray();
    }

    private static void O000000o(O00000Oo o00000Oo, Map map) {
        o00000Oo.beginObject();
        while (o00000Oo.hasNext()) {
            if (o00000Oo.O000000o(O0O0oO0) != 0) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                o00000Oo.beginArray();
                while (o00000Oo.hasNext()) {
                    O00000o0 O00000Oo2 = C0036O0000OoO.O00000Oo(o00000Oo);
                    map.put(O00000Oo2.getName(), O00000Oo2);
                }
                o00000Oo.endArray();
            }
        }
        o00000Oo.endObject();
    }

    public static C0064O0000o0O O00000Oo(O00000Oo o00000Oo) {
        ArrayList arrayList;
        HashMap hashMap;
        O00000Oo o00000Oo2 = o00000Oo;
        float O00o0O0O = com.airbnb.lottie.O00000o.O0000OOo.O00o0O0O();
        LongSparseArray longSparseArray = new LongSparseArray();
        ArrayList arrayList2 = new ArrayList();
        HashMap hashMap2 = new HashMap();
        HashMap hashMap3 = new HashMap();
        HashMap hashMap4 = new HashMap();
        ArrayList arrayList3 = new ArrayList();
        SparseArrayCompat sparseArrayCompat = new SparseArrayCompat();
        C0064O0000o0O o0000o0O = new C0064O0000o0O();
        o00000Oo.beginObject();
        float f = 0.0f;
        float f2 = 0.0f;
        float f3 = 0.0f;
        int i = 0;
        int i2 = 0;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo2.O000000o(NAMES)) {
                case 0:
                    HashMap hashMap5 = hashMap4;
                    ArrayList arrayList4 = arrayList3;
                    i = o00000Oo.nextInt();
                    continue;
                case 1:
                    HashMap hashMap6 = hashMap4;
                    ArrayList arrayList5 = arrayList3;
                    i2 = o00000Oo.nextInt();
                    continue;
                case 2:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    f = (float) o00000Oo.nextDouble();
                    break;
                case 3:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    f2 = ((float) o00000Oo.nextDouble()) - 0.01f;
                    break;
                case 4:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    f3 = (float) o00000Oo.nextDouble();
                    break;
                case 5:
                    String[] split = o00000Oo.nextString().split("\\.");
                    if (!com.airbnb.lottie.O00000o.O0000OOo.O000000o(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 4, 4, 0)) {
                        o0000o0O.O0000oo("Lottie only supports bodymovin >= 4.4.0");
                        break;
                    }
                    break;
                case 6:
                    O000000o(o00000Oo2, o0000o0O, (List) arrayList2, longSparseArray);
                    break;
                case 7:
                    O000000o(o00000Oo2, o0000o0O, (Map) hashMap2, (Map) hashMap3);
                    break;
                case 8:
                    O000000o(o00000Oo2, hashMap4);
                    break;
                case 9:
                    O000000o(o00000Oo2, o0000o0O, sparseArrayCompat);
                    break;
                case 10:
                    O000000o(o00000Oo2, o0000o0O, (List) arrayList3);
                    break;
                default:
                    hashMap = hashMap4;
                    arrayList = arrayList3;
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    break;
            }
            hashMap = hashMap4;
            arrayList = arrayList3;
            hashMap4 = hashMap;
            arrayList3 = arrayList;
            o00000Oo2 = o00000Oo;
        }
        HashMap hashMap7 = hashMap4;
        ArrayList arrayList6 = arrayList3;
        o0000o0O.O000000o(new Rect(0, 0, (int) (((float) i) * O00o0O0O), (int) (((float) i2) * O00o0O0O)), f, f2, f3, arrayList2, longSparseArray, hashMap2, hashMap3, sparseArrayCompat, hashMap4, arrayList3);
        return o0000o0O;
    }
}
