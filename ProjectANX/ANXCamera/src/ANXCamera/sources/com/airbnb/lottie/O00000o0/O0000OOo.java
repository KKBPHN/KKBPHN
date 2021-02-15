package com.airbnb.lottie.O00000o0;

import com.airbnb.lottie.model.DocumentData$Justification;
import com.airbnb.lottie.model.O00000Oo;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.xiaomi.stat.d;

public class O0000OOo implements O000OO {
    public static final O0000OOo INSTANCE = new O0000OOo();
    private static final O000000o NAMES = O000000o.of("t", SupportedConfigFactory.CLOSE_BY_BOKEH, "s", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL, "tr", "lh", "ls", d.ak, "sc", "sw", "of");

    private O0000OOo() {
    }

    public O00000Oo O000000o(com.airbnb.lottie.parser.moshi.O00000Oo o00000Oo, float f) {
        DocumentData$Justification documentData$Justification = DocumentData$Justification.CENTER;
        o00000Oo.beginObject();
        DocumentData$Justification documentData$Justification2 = documentData$Justification;
        String str = null;
        String str2 = null;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        float f2 = 0.0f;
        float f3 = 0.0f;
        float f4 = 0.0f;
        float f5 = 0.0f;
        boolean z = true;
        while (o00000Oo.hasNext()) {
            switch (o00000Oo.O000000o(NAMES)) {
                case 0:
                    str = o00000Oo.nextString();
                    break;
                case 1:
                    str2 = o00000Oo.nextString();
                    break;
                case 2:
                    f2 = (float) o00000Oo.nextDouble();
                    break;
                case 3:
                    int nextInt = o00000Oo.nextInt();
                    if (nextInt <= DocumentData$Justification.CENTER.ordinal() && nextInt >= 0) {
                        documentData$Justification2 = DocumentData$Justification.values()[nextInt];
                        break;
                    } else {
                        documentData$Justification2 = DocumentData$Justification.CENTER;
                        break;
                    }
                case 4:
                    i = o00000Oo.nextInt();
                    break;
                case 5:
                    f3 = (float) o00000Oo.nextDouble();
                    break;
                case 6:
                    f4 = (float) o00000Oo.nextDouble();
                    break;
                case 7:
                    i2 = O0000o.O00000o0(o00000Oo);
                    break;
                case 8:
                    i3 = O0000o.O00000o0(o00000Oo);
                    break;
                case 9:
                    f5 = (float) o00000Oo.nextDouble();
                    break;
                case 10:
                    z = o00000Oo.nextBoolean();
                    break;
                default:
                    o00000Oo.O00o0O0();
                    o00000Oo.skipValue();
                    break;
            }
        }
        com.airbnb.lottie.parser.moshi.O00000Oo o00000Oo2 = o00000Oo;
        o00000Oo.endObject();
        O00000Oo o00000Oo3 = new O00000Oo(str, str2, f2, documentData$Justification2, i, f3, f4, i2, i3, f5, z);
        return o00000Oo3;
    }
}
