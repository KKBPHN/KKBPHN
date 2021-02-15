package com.airbnb.lottie.O000000o.O00000Oo;

import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O0000o0O reason: case insensitive filesystem */
public class C0024O0000o0O extends C0023O0000Ooo {
    private O0000o0 O00o00o;
    private final PointF O00o00o0 = new PointF();
    private PathMeasure O00o00oO = new PathMeasure();
    private final float[] pos = new float[2];

    public C0024O0000o0O(List list) {
        super(list);
    }

    public PointF O000000o(C0054O000000o o000000o, float f) {
        O0000o0 o0000o0 = (O0000o0) o000000o;
        Path path = o0000o0.getPath();
        if (path == null) {
            return (PointF) o000000o.startValue;
        }
        O0000Oo o0000Oo = this.O00o00;
        if (o0000Oo != null) {
            PointF pointF = (PointF) o0000Oo.O00000Oo(o0000o0.O000oO0O, o0000o0.O000oO0o.floatValue(), o0000o0.startValue, o0000o0.endValue, O00OOoO(), f, getProgress());
            if (pointF != null) {
                return pointF;
            }
        }
        if (this.O00o00o != o0000o0) {
            this.O00o00oO.setPath(path, false);
            this.O00o00o = o0000o0;
        }
        PathMeasure pathMeasure = this.O00o00oO;
        pathMeasure.getPosTan(f * pathMeasure.getLength(), this.pos, null);
        PointF pointF2 = this.O00o00o0;
        float[] fArr = this.pos;
        pointF2.set(fArr[0], fArr[1]);
        return this.O00o00o0;
    }
}
