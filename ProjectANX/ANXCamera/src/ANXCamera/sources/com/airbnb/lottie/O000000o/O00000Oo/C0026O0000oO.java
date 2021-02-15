package com.airbnb.lottie.O000000o.O00000Oo;

import android.graphics.PointF;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import java.util.Collections;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O0000oO reason: case insensitive filesystem */
public class C0026O0000oO extends O0000O0o {
    private final PointF O00o00o0 = new PointF();
    private final O0000O0o O00o0O0;
    private final O0000O0o O00oo000;

    public C0026O0000oO(O0000O0o o0000O0o, O0000O0o o0000O0o2) {
        super(Collections.emptyList());
        this.O00o0O0 = o0000O0o;
        this.O00oo000 = o0000O0o2;
        setProgress(getProgress());
    }

    /* access modifiers changed from: 0000 */
    public PointF O000000o(C0054O000000o o000000o, float f) {
        return this.O00o00o0;
    }

    public PointF getValue() {
        return O000000o((C0054O000000o) null, 0.0f);
    }

    public void setProgress(float f) {
        this.O00o0O0.setProgress(f);
        this.O00oo000.setProgress(f);
        this.O00o00o0.set(((Float) this.O00o0O0.getValue()).floatValue(), ((Float) this.O00oo000.getValue()).floatValue());
        for (int i = 0; i < this.listeners.size(); i++) {
            ((O00000Oo) this.listeners.get(i)).O00000oO();
        }
    }
}
