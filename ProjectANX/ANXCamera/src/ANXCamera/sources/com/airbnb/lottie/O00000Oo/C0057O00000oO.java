package com.airbnb.lottie.O00000oO;

import android.graphics.PointF;
import android.view.animation.Interpolator;
import com.airbnb.lottie.O00000o.O0000O0o;

/* renamed from: com.airbnb.lottie.O00000oO.O00000oO reason: case insensitive filesystem */
public class C0057O00000oO extends C0058O00000oo {
    private final PointF O00o00o0 = new PointF();

    public C0057O00000oO(PointF pointF, PointF pointF2) {
        super(pointF, pointF2);
    }

    public C0057O00000oO(PointF pointF, PointF pointF2, Interpolator interpolator) {
        super(pointF, pointF2, interpolator);
    }

    /* access modifiers changed from: 0000 */
    public PointF O000000o(PointF pointF, PointF pointF2, float f) {
        this.O00o00o0.set(O0000O0o.lerp(pointF.x, pointF2.x, f), O0000O0o.lerp(pointF.y, pointF2.y, f));
        return this.O00o00o0;
    }
}
