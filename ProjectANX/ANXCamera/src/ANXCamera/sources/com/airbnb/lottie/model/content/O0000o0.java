package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0017O0000oo0;
import com.airbnb.lottie.model.O000000o.O0000OOo;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000o0 implements O00000Oo {
    private final O0000OOo O00oOOoO;
    private final boolean hidden;
    private final int index;
    private final String name;

    public O0000o0(String str, int i, O0000OOo o0000OOo, boolean z) {
        this.name = str;
        this.index = i;
        this.O00oOOoO = o0000OOo;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0017O0000oo0(o000OoO0, o00000o0, this);
    }

    public O0000OOo O00OoooO() {
        return this.O00oOOoO;
    }

    public String getName() {
        return this.name;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShapePath{name=");
        sb.append(this.name);
        sb.append(", index=");
        sb.append(this.index);
        sb.append('}');
        return sb.toString();
    }
}
