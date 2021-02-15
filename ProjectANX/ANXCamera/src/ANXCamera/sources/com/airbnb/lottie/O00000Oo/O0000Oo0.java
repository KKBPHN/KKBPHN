package com.airbnb.lottie.O00000oO;

import android.graphics.PointF;
import androidx.annotation.NonNull;
import com.airbnb.lottie.O00000o.O0000O0o;

public class O0000Oo0 extends O0000Oo {
    private final PointF O00o00o0 = new PointF();

    public O0000Oo0() {
    }

    public O0000Oo0(@NonNull PointF pointF) {
        super(pointF);
    }

    public final PointF O000000o(C0055O00000Oo o00000Oo) {
        this.O00o00o0.set(O0000O0o.lerp(((PointF) o00000Oo.getStartValue()).x, ((PointF) o00000Oo.getEndValue()).x, o00000Oo.O00o0OOo()), O0000O0o.lerp(((PointF) o00000Oo.getStartValue()).y, ((PointF) o00000Oo.getEndValue()).y, o00000Oo.O00o0OOo()));
        PointF O00000o02 = O00000o0(o00000Oo);
        this.O00o00o0.offset(O00000o02.x, O00000o02.y);
        return this.O00o00o0;
    }

    public PointF O00000o0(C0055O00000Oo o00000Oo) {
        Object obj = this.value;
        if (obj != null) {
            return (PointF) obj;
        }
        throw new IllegalArgumentException("You must provide a static value in the constructor , call setValue, or override getValue.");
    }
}
