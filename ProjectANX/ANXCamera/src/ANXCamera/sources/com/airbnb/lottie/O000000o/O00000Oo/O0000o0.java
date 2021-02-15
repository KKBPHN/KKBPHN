package com.airbnb.lottie.O000000o.O00000Oo;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.C0054O000000o;

public class O0000o0 extends C0054O000000o {
    private final C0054O000000o O0OOo00;
    @Nullable
    private Path path;

    public O0000o0(C0064O0000o0O o0000o0O, C0054O000000o o000000o) {
        super(o0000o0O, o000000o.startValue, o000000o.endValue, o000000o.interpolator, o000000o.O000oO0O, o000000o.O000oO0o);
        this.O0OOo00 = o000000o;
        O00o0OOO();
    }

    public void O00o0OOO() {
        boolean z;
        Object obj;
        Object obj2 = this.endValue;
        if (obj2 != null) {
            Object obj3 = this.startValue;
            if (obj3 != null && ((PointF) obj3).equals(((PointF) obj2).x, ((PointF) obj2).y)) {
                z = true;
                obj = this.endValue;
                if (obj != null && !z) {
                    PointF pointF = (PointF) this.startValue;
                    PointF pointF2 = (PointF) obj;
                    C0054O000000o o000000o = this.O0OOo00;
                    this.path = O0000OOo.O000000o(pointF, pointF2, o000000o.O0OOOo0, o000000o.O0OOOo);
                    return;
                }
            }
        }
        z = false;
        obj = this.endValue;
        if (obj != null) {
        }
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public Path getPath() {
        return this.path;
    }
}
