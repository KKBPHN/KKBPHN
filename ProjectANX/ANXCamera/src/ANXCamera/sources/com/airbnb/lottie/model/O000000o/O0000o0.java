package com.airbnb.lottie.model.O000000o;

import com.airbnb.lottie.O00000oO.C0054O000000o;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

abstract class O0000o0 implements O0000o00 {
    final List O00Oooo;

    O0000o0(Object obj) {
        this(Collections.singletonList(new C0054O000000o(obj)));
    }

    O0000o0(List list) {
        this.O00Oooo = list;
    }

    public List getKeyframes() {
        return this.O00Oooo;
    }

    public boolean isStatic() {
        return this.O00Oooo.isEmpty() || (this.O00Oooo.size() == 1 && ((C0054O000000o) this.O00Oooo.get(0)).isStatic());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (!this.O00Oooo.isEmpty()) {
            sb.append("values=");
            sb.append(Arrays.toString(this.O00Oooo.toArray()));
        }
        return sb.toString();
    }
}
