package com.airbnb.lottie.O00000o0;

import android.graphics.PointF;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.model.content.C0106O0000OoO;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class O000O0o0 implements O000OO {
    public static final O000O0o0 INSTANCE = new O000O0o0();
    private static final O000000o NAMES = O000000o.of(SupportedConfigFactory.CLOSE_BY_SUPER_RESOLUTION, "v", SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE, SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT);

    private O000O0o0() {
    }

    public C0106O0000OoO O000000o(O00000Oo o00000Oo, float f) {
        if (o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY) {
            o00000Oo.beginArray();
        }
        o00000Oo.beginObject();
        List list = null;
        List list2 = null;
        List list3 = null;
        boolean z = false;
        while (o00000Oo.hasNext()) {
            int O000000o2 = o00000Oo.O000000o(NAMES);
            if (O000000o2 == 0) {
                z = o00000Oo.nextBoolean();
            } else if (O000000o2 == 1) {
                list = O0000o.O00000o0(o00000Oo, f);
            } else if (O000000o2 == 2) {
                list2 = O0000o.O00000o0(o00000Oo, f);
            } else if (O000000o2 != 3) {
                o00000Oo.O00o0O0();
                o00000Oo.skipValue();
            } else {
                list3 = O0000o.O00000o0(o00000Oo, f);
            }
        }
        o00000Oo.endObject();
        if (o00000Oo.peek() == JsonReader$Token.END_ARRAY) {
            o00000Oo.endArray();
        }
        if (list == null || list2 == null || list3 == null) {
            throw new IllegalArgumentException("Shape data was missing information.");
        } else if (list.isEmpty()) {
            return new C0106O0000OoO(new PointF(), false, Collections.emptyList());
        } else {
            int size = list.size();
            PointF pointF = (PointF) list.get(0);
            ArrayList arrayList = new ArrayList(size);
            for (int i = 1; i < size; i++) {
                PointF pointF2 = (PointF) list.get(i);
                int i2 = i - 1;
                arrayList.add(new com.airbnb.lottie.model.O000000o(O0000O0o.O000000o((PointF) list.get(i2), (PointF) list3.get(i2)), O0000O0o.O000000o(pointF2, (PointF) list2.get(i)), pointF2));
            }
            if (z) {
                PointF pointF3 = (PointF) list.get(0);
                int i3 = size - 1;
                arrayList.add(new com.airbnb.lottie.model.O000000o(O0000O0o.O000000o((PointF) list.get(i3), (PointF) list3.get(i3)), O0000O0o.O000000o(pointF3, (PointF) list2.get(0)), pointF3));
            }
            return new C0106O0000OoO(pointF, z, arrayList);
        }
    }
}
