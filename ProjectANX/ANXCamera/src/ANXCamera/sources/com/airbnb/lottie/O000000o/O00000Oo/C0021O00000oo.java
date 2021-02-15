package com.airbnb.lottie.O000000o.O00000Oo;

import androidx.annotation.NonNull;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O00000oo reason: case insensitive filesystem */
final class C0021O00000oo implements O00000o {
    @NonNull
    private final C0054O000000o O00o000;
    private float O00o0000 = -1.0f;

    C0021O00000oo(List list) {
        this.O00o000 = (C0054O000000o) list.get(0);
    }

    public C0054O000000o O000000o() {
        return this.O00o000;
    }

    public boolean O000000o(float f) {
        if (this.O00o0000 == f) {
            return true;
        }
        this.O00o0000 = f;
        return false;
    }

    public boolean O00000Oo(float f) {
        return !this.O00o000.isStatic();
    }

    public float O00000o0() {
        return this.O00o000.O00o00o();
    }

    public float O0000Oo0() {
        return this.O00o000.O0000Oo0();
    }

    public boolean isEmpty() {
        return false;
    }
}
