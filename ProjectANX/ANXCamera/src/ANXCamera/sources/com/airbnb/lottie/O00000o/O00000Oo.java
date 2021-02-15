package com.airbnb.lottie.O00000o;

public class O00000Oo {
    public static int O000000o(float f, int i, int i2) {
        if (i == i2) {
            return i;
        }
        float f2 = ((float) ((i >> 24) & 255)) / 255.0f;
        float f3 = ((float) ((i >> 8) & 255)) / 255.0f;
        float f4 = ((float) (i & 255)) / 255.0f;
        float f5 = ((float) ((i2 >> 24) & 255)) / 255.0f;
        float f6 = ((float) ((i2 >> 16) & 255)) / 255.0f;
        float f7 = ((float) ((i2 >> 8) & 255)) / 255.0f;
        float f8 = ((float) (i2 & 255)) / 255.0f;
        float O0000o0o = O0000o0o(((float) ((i >> 16) & 255)) / 255.0f);
        float O0000o0o2 = O0000o0o(f3);
        float O0000o0o3 = O0000o0o(f4);
        float O0000o0o4 = O0000o0o(f6);
        float O0000o0o5 = O0000o0o2 + ((O0000o0o(f7) - O0000o0o2) * f);
        float O0000o0o6 = O0000o0o3 + (f * (O0000o0o(f8) - O0000o0o3));
        float f9 = (f2 + ((f5 - f2) * f)) * 255.0f;
        return (Math.round(O0000o(O0000o0o + ((O0000o0o4 - O0000o0o) * f)) * 255.0f) << 16) | (Math.round(f9) << 24) | (Math.round(O0000o(O0000o0o5) * 255.0f) << 8) | Math.round(O0000o(O0000o0o6) * 255.0f);
    }

    private static float O0000o(float f) {
        return f <= 0.0031308f ? f * 12.92f : (float) ((Math.pow((double) f, 0.4166666567325592d) * 1.0549999475479126d) - 0.054999999701976776d);
    }

    private static float O0000o0o(float f) {
        return f <= 0.04045f ? f / 12.92f : (float) Math.pow((double) ((f + 0.055f) / 1.055f), 2.4000000953674316d);
    }
}
