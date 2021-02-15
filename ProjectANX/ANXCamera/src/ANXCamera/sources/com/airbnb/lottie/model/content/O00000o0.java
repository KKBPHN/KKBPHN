package com.airbnb.lottie.model.content;

import com.airbnb.lottie.O00000o.O00000Oo;
import com.airbnb.lottie.O00000o.O0000O0o;

public class O00000o0 {
    private final int[] O00oO0o;
    private final float[] O00oO0o0;

    public O00000o0(float[] fArr, int[] iArr) {
        this.O00oO0o0 = fArr;
        this.O00oO0o = iArr;
    }

    public void O000000o(O00000o0 o00000o0, O00000o0 o00000o02, float f) {
        if (o00000o0.O00oO0o.length == o00000o02.O00oO0o.length) {
            for (int i = 0; i < o00000o0.O00oO0o.length; i++) {
                this.O00oO0o0[i] = O0000O0o.lerp(o00000o0.O00oO0o0[i], o00000o02.O00oO0o0[i], f);
                this.O00oO0o[i] = O00000Oo.O000000o(f, o00000o0.O00oO0o[i], o00000o02.O00oO0o[i]);
            }
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Cannot interpolate between gradients. Lengths vary (");
        sb.append(o00000o0.O00oO0o.length);
        sb.append(" vs ");
        sb.append(o00000o02.O00oO0o.length);
        sb.append(")");
        throw new IllegalArgumentException(sb.toString());
    }

    public int[] getColors() {
        return this.O00oO0o;
    }

    public float[] getPositions() {
        return this.O00oO0o0;
    }

    public int getSize() {
        return this.O00oO0o.length;
    }
}
