package com.airbnb.lottie.O00000o;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.FloatRange;
import com.airbnb.lottie.O000000o.O000000o.O0000o00;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.O000000o;
import com.airbnb.lottie.model.content.C0106O0000OoO;
import java.util.List;

public class O0000O0o {
    private static PointF O0OO0oo = new PointF();

    public static double O000000o(double d, double d2, @FloatRange(from = 0.0d, to = 1.0d) double d3) {
        return d + (d3 * (d2 - d));
    }

    public static int O000000o(int i, int i2, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        return (int) (((float) i) + (f * ((float) (i2 - i))));
    }

    public static PointF O000000o(PointF pointF, PointF pointF2) {
        return new PointF(pointF.x + pointF2.x, pointF.y + pointF2.y);
    }

    public static void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2, O0000o00 o0000o00) {
        if (o00000oO.O00000Oo(o0000o00.getName(), i)) {
            list.add(o00000oO2.O000O0Oo(o0000o00.getName()).O000000o(o0000o00));
        }
    }

    public static void O000000o(C0106O0000OoO o0000OoO, Path path) {
        path.reset();
        PointF O00Ooooo = o0000OoO.O00Ooooo();
        path.moveTo(O00Ooooo.x, O00Ooooo.y);
        O0OO0oo.set(O00Ooooo.x, O00Ooooo.y);
        for (int i = 0; i < o0000OoO.O00Oooo().size(); i++) {
            O000000o o000000o = (O000000o) o0000OoO.O00Oooo().get(i);
            PointF O00Oo0o = o000000o.O00Oo0o();
            PointF O00Oo0oO = o000000o.O00Oo0oO();
            PointF O00OooOO = o000000o.O00OooOO();
            if (!O00Oo0o.equals(O0OO0oo) || !O00Oo0oO.equals(O00OooOO)) {
                path.cubicTo(O00Oo0o.x, O00Oo0o.y, O00Oo0oO.x, O00Oo0oO.y, O00OooOO.x, O00OooOO.y);
            } else {
                path.lineTo(O00OooOO.x, O00OooOO.y);
            }
            O0OO0oo.set(O00OooOO.x, O00OooOO.y);
        }
        if (o0000OoO.isClosed()) {
            path.close();
        }
    }

    public static boolean O000000o(float f, float f2, float f3) {
        return f >= f2 && f <= f3;
    }

    static int O00000oo(float f, float f2) {
        return floorMod((int) f, (int) f2);
    }

    public static float clamp(float f, float f2, float f3) {
        return Math.max(f2, Math.min(f3, f));
    }

    public static int clamp(int i, int i2, int i3) {
        return Math.max(i2, Math.min(i3, i));
    }

    private static int floorDiv(int i, int i2) {
        int i3 = i / i2;
        return (((i ^ i2) >= 0) || i % i2 == 0) ? i3 : i3 - 1;
    }

    private static int floorMod(int i, int i2) {
        return i - (i2 * floorDiv(i, i2));
    }

    public static float lerp(float f, float f2, @FloatRange(from = 0.0d, to = 1.0d) float f3) {
        return f + (f3 * (f2 - f));
    }
}
