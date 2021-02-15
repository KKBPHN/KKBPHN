package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0007O00000oo;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.Arrays;
import java.util.List;

public class O0000o00 implements O00000Oo {
    private final boolean hidden;
    private final List items;
    private final String name;

    public O0000o00(String str, List list, boolean z) {
        this.name = str;
        this.items = list;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0007O00000oo(o000OoO0, o00000o0, this);
    }

    public List getItems() {
        return this.items;
    }

    public String getName() {
        return this.name;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShapeGroup{name='");
        sb.append(this.name);
        sb.append("' Shapes: ");
        sb.append(Arrays.toString(this.items.toArray()));
        sb.append('}');
        return sb.toString();
    }
}
