package com.airbnb.lottie.O00000o0;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import androidx.annotation.Nullable;
import androidx.collection.SparseArrayCompat;
import androidx.core.view.animation.PathInterpolatorCompat;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.parser.moshi.O000000o;
import com.airbnb.lottie.parser.moshi.O00000Oo;
import com.android.camera.data.data.config.SupportedConfigFactory;
import java.lang.ref.WeakReference;

/* renamed from: com.airbnb.lottie.O00000o0.O0000oO0 reason: case insensitive filesystem */
class C0041O0000oO0 {
    private static final Interpolator LINEAR_INTERPOLATOR = new LinearInterpolator();
    static O000000o NAMES = O000000o.of("t", "s", "e", SupportedConfigFactory.CLOSE_BY_ULTRA_PIXEL_PORTRAIT, SupportedConfigFactory.CLOSE_BY_ULTRA_WIDE, SupportedConfigFactory.CLOSE_BY_VIDEO, "to", "ti");
    private static SparseArrayCompat O0O0o0 = null;
    private static final float O0O0o00 = 100.0f;

    C0041O0000oO0() {
    }

    private static C0054O000000o O000000o(C0064O0000o0O o0000o0O, O00000Oo o00000Oo, float f, O000OO o000oo) {
        Interpolator interpolator;
        Object obj;
        O00000Oo o00000Oo2 = o00000Oo;
        float f2 = f;
        O000OO o000oo2 = o000oo;
        o00000Oo.beginObject();
        Interpolator interpolator2 = null;
        PointF pointF = null;
        PointF pointF2 = null;
        Object obj2 = null;
        Object obj3 = null;
        PointF pointF3 = null;
        PointF pointF4 = null;
        float f3 = 0.0f;
        while (true) {
            boolean z = false;
            while (true) {
                if (o00000Oo.hasNext()) {
                    switch (o00000Oo2.O000000o(NAMES)) {
                        case 0:
                            f3 = (float) o00000Oo.nextDouble();
                            break;
                        case 1:
                            obj3 = o000oo2.O000000o(o00000Oo2, f2);
                            break;
                        case 2:
                            obj2 = o000oo2.O000000o(o00000Oo2, f2);
                            break;
                        case 3:
                            pointF = O0000o.O00000Oo(o00000Oo, f);
                            break;
                        case 4:
                            pointF2 = O0000o.O00000Oo(o00000Oo, f);
                            break;
                        case 5:
                            if (o00000Oo.nextInt() == 1) {
                                z = true;
                                break;
                            }
                        case 6:
                            pointF3 = O0000o.O00000Oo(o00000Oo, f);
                            break;
                        case 7:
                            pointF4 = O0000o.O00000Oo(o00000Oo, f);
                            break;
                        default:
                            o00000Oo.skipValue();
                            break;
                    }
                } else {
                    o00000Oo.endObject();
                    if (z) {
                        interpolator = LINEAR_INTERPOLATOR;
                        obj = obj3;
                    } else {
                        if (pointF == null || pointF2 == null) {
                            interpolator = LINEAR_INTERPOLATOR;
                        } else {
                            float f4 = -f2;
                            pointF.x = O0000O0o.clamp(pointF.x, f4, f2);
                            pointF.y = O0000O0o.clamp(pointF.y, -100.0f, (float) O0O0o00);
                            pointF2.x = O0000O0o.clamp(pointF2.x, f4, f2);
                            pointF2.y = O0000O0o.clamp(pointF2.y, -100.0f, (float) O0O0o00);
                            int O000000o2 = O0000OOo.O000000o(pointF.x, pointF.y, pointF2.x, pointF2.y);
                            WeakReference interpolator3 = getInterpolator(O000000o2);
                            if (interpolator3 != null) {
                                interpolator2 = (Interpolator) interpolator3.get();
                            }
                            if (interpolator3 == null || interpolator2 == null) {
                                interpolator2 = PathInterpolatorCompat.create(pointF.x / f2, pointF.y / f2, pointF2.x / f2, pointF2.y / f2);
                                try {
                                    O000000o(O000000o2, new WeakReference(interpolator2));
                                } catch (ArrayIndexOutOfBoundsException unused) {
                                }
                            }
                            interpolator = interpolator2;
                        }
                        obj = obj2;
                    }
                    C0054O000000o o000000o = new C0054O000000o(o0000o0O, obj3, obj, interpolator, f3, null);
                    o000000o.O0OOOo0 = pointF3;
                    o000000o.O0OOOo = pointF4;
                    return o000000o;
                }
            }
        }
    }

    private static C0054O000000o O000000o(O00000Oo o00000Oo, float f, O000OO o000oo) {
        return new C0054O000000o(o000oo.O000000o(o00000Oo, f));
    }

    static C0054O000000o O000000o(O00000Oo o00000Oo, C0064O0000o0O o0000o0O, float f, O000OO o000oo, boolean z) {
        return z ? O000000o(o0000o0O, o00000Oo, f, o000oo) : O000000o(o00000Oo, f, o000oo);
    }

    private static void O000000o(int i, WeakReference weakReference) {
        synchronized (C0041O0000oO0.class) {
            O0O0o0.put(i, weakReference);
        }
    }

    private static SparseArrayCompat Oo0oooO() {
        if (O0O0o0 == null) {
            O0O0o0 = new SparseArrayCompat();
        }
        return O0O0o0;
    }

    @Nullable
    private static WeakReference getInterpolator(int i) {
        WeakReference weakReference;
        synchronized (C0041O0000oO0.class) {
            weakReference = (WeakReference) Oo0oooO().get(i);
        }
        return weakReference;
    }
}
