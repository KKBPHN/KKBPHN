package com.airbnb.lottie.O000000o.O00000Oo;

import android.graphics.PointF;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O0000o0o reason: case insensitive filesystem */
public class C0025O0000o0o extends C0023O0000Ooo {
    private final PointF O00o00o0 = new PointF();

    public C0025O0000o0o(List list) {
        super(list);
    }

    public PointF O000000o(C0054O000000o o000000o, float f) {
        Object obj = o000000o.startValue;
        if (obj != null) {
            Object obj2 = o000000o.endValue;
            if (obj2 != null) {
                PointF pointF = (PointF) obj;
                PointF pointF2 = (PointF) obj2;
                O0000Oo o0000Oo = this.O00o00;
                if (o0000Oo != null) {
                    PointF pointF3 = (PointF) o0000Oo.O00000Oo(o000000o.O000oO0O, o000000o.O000oO0o.floatValue(), pointF, pointF2, f, O00OOoO(), getProgress());
                    if (pointF3 != null) {
                        return pointF3;
                    }
                }
                PointF pointF4 = this.O00o00o0;
                float f2 = pointF.x;
                float f3 = f2 + ((pointF2.x - f2) * f);
                float f4 = pointF.y;
                pointF4.set(f3, f4 + (f * (pointF2.y - f4)));
                return this.O00o00o0;
            }
        }
        throw new IllegalStateException("Missing values for keyframe.");
    }
}
