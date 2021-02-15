package com.airbnb.lottie.O000000o.O00000Oo;

import androidx.annotation.NonNull;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O00000oO reason: case insensitive filesystem */
final class C0020O00000oO implements O00000o {
    private final List O00Oooo;
    private C0054O000000o O00OoooO = null;
    @NonNull
    private C0054O000000o O00Ooooo;
    private float O00o0000 = -1.0f;

    C0020O00000oO(List list) {
        this.O00Oooo = list;
        this.O00Ooooo = O0000o0(0.0f);
    }

    private C0054O000000o O0000o0(float f) {
        List list = this.O00Oooo;
        C0054O000000o o000000o = (C0054O000000o) list.get(list.size() - 1);
        if (f >= o000000o.O00o00o()) {
            return o000000o;
        }
        for (int size = this.O00Oooo.size() - 2; size >= 1; size--) {
            C0054O000000o o000000o2 = (C0054O000000o) this.O00Oooo.get(size);
            if (this.O00Ooooo != o000000o2 && o000000o2.O0000o00(f)) {
                return o000000o2;
            }
        }
        return (C0054O000000o) this.O00Oooo.get(0);
    }

    @NonNull
    public C0054O000000o O000000o() {
        return this.O00Ooooo;
    }

    public boolean O000000o(float f) {
        if (this.O00OoooO == this.O00Ooooo && this.O00o0000 == f) {
            return true;
        }
        this.O00OoooO = this.O00Ooooo;
        this.O00o0000 = f;
        return false;
    }

    public boolean O00000Oo(float f) {
        if (this.O00Ooooo.O0000o00(f)) {
            return !this.O00Ooooo.isStatic();
        }
        this.O00Ooooo = O0000o0(f);
        return true;
    }

    public float O00000o0() {
        return ((C0054O000000o) this.O00Oooo.get(0)).O00o00o();
    }

    public float O0000Oo0() {
        List list = this.O00Oooo;
        return ((C0054O000000o) list.get(list.size() - 1)).O0000Oo0();
    }

    public boolean isEmpty() {
        return false;
    }
}
