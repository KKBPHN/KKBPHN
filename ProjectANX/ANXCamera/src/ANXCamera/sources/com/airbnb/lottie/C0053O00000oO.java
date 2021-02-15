package com.airbnb.lottie;

import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.os.TraceCompat;

@RestrictTo({Scope.LIBRARY})
/* renamed from: com.airbnb.lottie.O00000oO reason: case insensitive filesystem */
public class C0053O00000oO {
    public static boolean DBG = false;
    private static final int MAX_DEPTH = 20;
    private static long[] O000o0 = null;
    private static boolean O000o00O = false;
    private static String[] O000o00o = null;
    private static int O000o0O = 0;
    private static int O000o0O0 = 0;
    public static final String TAG = "LOTTIE";

    public static void O0000o(boolean z) {
        if (O000o00O != z) {
            O000o00O = z;
            if (O000o00O) {
                O000o00o = new String[20];
                O000o0 = new long[20];
            }
        }
    }

    public static float O0000oOo(String str) {
        int i = O000o0O;
        if (i > 0) {
            O000o0O = i - 1;
            return 0.0f;
        } else if (!O000o00O) {
            return 0.0f;
        } else {
            O000o0O0--;
            int i2 = O000o0O0;
            if (i2 == -1) {
                throw new IllegalStateException("Can't end trace section. There are none.");
            } else if (str.equals(O000o00o[i2])) {
                TraceCompat.endSection();
                return ((float) (System.nanoTime() - O000o0[O000o0O0])) / 1000000.0f;
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("Unbalanced trace call ");
                sb.append(str);
                sb.append(". Expected ");
                sb.append(O000o00o[O000o0O0]);
                sb.append(".");
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public static void beginSection(String str) {
        if (O000o00O) {
            int i = O000o0O0;
            if (i == 20) {
                O000o0O++;
                return;
            }
            O000o00o[i] = str;
            O000o0[i] = System.nanoTime();
            TraceCompat.beginSection(str);
            O000o0O0++;
        }
    }
}
