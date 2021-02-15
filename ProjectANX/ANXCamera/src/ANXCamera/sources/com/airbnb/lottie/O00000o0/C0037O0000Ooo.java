package com.airbnb.lottie.O00000o0;

import android.graphics.Color;
import androidx.annotation.IntRange;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.model.content.O00000o0;
import com.airbnb.lottie.parser.moshi.JsonReader$Token;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.O00000o0.O0000Ooo reason: case insensitive filesystem */
public class C0037O0000Ooo implements O000OO {
    private int O0O0OOO;

    public C0037O0000Ooo(int i) {
        this.O0O0OOO = i;
    }

    @IntRange(from = 0, to = 255)
    private int O000000o(double d, double[] dArr, double[] dArr2) {
        double d2;
        double[] dArr3 = dArr;
        double[] dArr4 = dArr2;
        int i = 1;
        while (true) {
            if (i >= dArr3.length) {
                d2 = dArr4[dArr4.length - 1];
                break;
            }
            int i2 = i - 1;
            double d3 = dArr3[i2];
            double d4 = dArr3[i];
            if (dArr3[i] >= d) {
                d2 = O0000O0o.O000000o(dArr4[i2], dArr4[i], (d - d3) / (d4 - d3));
                break;
            }
            i++;
        }
        return (int) (d2 * 255.0d);
    }

    private void O000000o(O00000o0 o00000o0, List list) {
        int i = this.O0O0OOO * 4;
        if (list.size() > i) {
            int size = (list.size() - i) / 2;
            double[] dArr = new double[size];
            double[] dArr2 = new double[size];
            int i2 = 0;
            while (i < list.size()) {
                if (i % 2 == 0) {
                    dArr[i2] = (double) ((Float) list.get(i)).floatValue();
                } else {
                    dArr2[i2] = (double) ((Float) list.get(i)).floatValue();
                    i2++;
                }
                i++;
            }
            for (int i3 = 0; i3 < o00000o0.getSize(); i3++) {
                int i4 = o00000o0.getColors()[i3];
                o00000o0.getColors()[i3] = Color.argb(O000000o((double) o00000o0.getPositions()[i3], dArr, dArr2), Color.red(i4), Color.green(i4), Color.blue(i4));
            }
        }
    }

    public O00000o0 O000000o(O00000Oo o00000Oo, float f) {
        ArrayList arrayList = new ArrayList();
        boolean z = o00000Oo.peek() == JsonReader$Token.BEGIN_ARRAY;
        if (z) {
            o00000Oo.beginArray();
        }
        while (o00000Oo.hasNext()) {
            arrayList.add(Float.valueOf((float) o00000Oo.nextDouble()));
        }
        if (z) {
            o00000Oo.endArray();
        }
        if (this.O0O0OOO == -1) {
            this.O0O0OOO = arrayList.size() / 4;
        }
        int i = this.O0O0OOO;
        float[] fArr = new float[i];
        int[] iArr = new int[i];
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < this.O0O0OOO * 4; i4++) {
            int i5 = i4 / 4;
            double floatValue = (double) ((Float) arrayList.get(i4)).floatValue();
            int i6 = i4 % 4;
            if (i6 == 0) {
                fArr[i5] = (float) floatValue;
            } else if (i6 == 1) {
                i2 = (int) (floatValue * 255.0d);
            } else if (i6 == 2) {
                i3 = (int) (floatValue * 255.0d);
            } else if (i6 == 3) {
                iArr[i5] = Color.argb(255, i2, i3, (int) (floatValue * 255.0d));
            }
        }
        O00000o0 o00000o0 = new O00000o0(fArr, iArr);
        O000000o(o00000o0, (List) arrayList);
        return o00000o0;
    }
}
