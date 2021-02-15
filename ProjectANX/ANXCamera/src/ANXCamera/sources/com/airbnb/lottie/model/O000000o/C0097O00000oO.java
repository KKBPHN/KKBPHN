package com.airbnb.lottie.model.O000000o;

import android.graphics.PointF;
import com.airbnb.lottie.O000000o.O00000Oo.C0024O0000o0O;
import com.airbnb.lottie.O000000o.O00000Oo.C0025O0000o0o;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import java.util.Collections;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.O000000o.O00000oO reason: case insensitive filesystem */
public class C0097O00000oO implements O0000o00 {
    private final List O00Oooo;

    public C0097O00000oO() {
        this.O00Oooo = Collections.singletonList(new C0054O000000o(new PointF(0.0f, 0.0f)));
    }

    public C0097O00000oO(List list) {
        this.O00Oooo = list;
    }

    public O0000O0o O00000o() {
        return ((C0054O000000o) this.O00Oooo.get(0)).isStatic() ? new C0025O0000o0o(this.O00Oooo) : new C0024O0000o0O(this.O00Oooo);
    }

    public List getKeyframes() {
        return this.O00Oooo;
    }

    public boolean isStatic() {
        return this.O00Oooo.size() == 1 && ((C0054O000000o) this.O00Oooo.get(0)).isStatic();
    }
}
