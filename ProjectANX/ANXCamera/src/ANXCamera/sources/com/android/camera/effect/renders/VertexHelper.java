package com.android.camera.effect.renders;

public class VertexHelper {
    private static final int ALL_POINT = 366;
    private static final double INTER = 0.017453292519943295d;
    private static final int NUM_FLOAT = 732;
    private static final int NUM_ROUND_POINT = 360;
    private static final String TAG = "VertexHelper";

    public static float[] genRoundRectVex(int i, int i2, int i3) {
        int i4;
        double d;
        int i5;
        int i6;
        int i7 = i;
        int i8 = i2;
        int i9 = i3;
        float[] fArr = new float[NUM_FLOAT];
        float f = (float) i7;
        fArr[0] = f / 2.0f;
        float f2 = (float) i8;
        fArr[1] = f2 / 2.0f;
        int i10 = i7 - i9;
        int i11 = i8 - i9;
        int i12 = 0;
        int i13 = 2;
        while (true) {
            i4 = 90;
            d = INTER;
            if (i12 > 90) {
                break;
            }
            double d2 = ((double) i12) * INTER;
            int i14 = i13 + 1;
            double d3 = (double) i9;
            float f3 = f2;
            fArr[i13] = (float) ((Math.cos(d2) * d3) + ((double) i10));
            i13 = i14 + 1;
            fArr[i14] = (float) ((d3 * Math.sin(d2)) + ((double) i11));
            i12++;
            f2 = f3;
        }
        float f4 = f2;
        while (true) {
            i5 = 180;
            if (i4 > 180) {
                break;
            }
            double d4 = ((double) i4) * d;
            int i15 = i13 + 1;
            double d5 = (double) i9;
            fArr[i13] = (float) ((Math.cos(d4) * d5) + d5);
            i13 = i15 + 1;
            fArr[i15] = (float) ((d5 * Math.sin(d4)) + ((double) i11));
            i4++;
            d = INTER;
        }
        while (true) {
            if (i5 > 270) {
                break;
            }
            double d6 = ((double) i5) * INTER;
            int i16 = i13 + 1;
            double d7 = (double) i9;
            fArr[i13] = (float) ((Math.cos(d6) * d7) + d7);
            i13 = i16 + 1;
            fArr[i16] = (float) ((Math.sin(d6) * d7) + d7);
            i5++;
        }
        for (i6 = 270; i6 <= 360; i6++) {
            double d8 = ((double) i6) * INTER;
            int i17 = i13 + 1;
            double d9 = (double) i9;
            fArr[i13] = (float) ((Math.cos(d8) * d9) + ((double) i10));
            i13 = i17 + 1;
            fArr[i17] = (float) ((Math.sin(d8) * d9) + d9);
        }
        fArr[730] = fArr[2];
        fArr[731] = fArr[3];
        for (int i18 = 0; i18 < NUM_FLOAT; i18 += 2) {
            fArr[i18] = fArr[i18] / f;
            int i19 = i18 + 1;
            fArr[i19] = fArr[i19] / f4;
        }
        return fArr;
    }
}
