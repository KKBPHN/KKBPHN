package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0014O0000oOO;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000Oo0 implements O00000Oo {
    private final O00000Oo cornerRadius;
    private final boolean hidden;
    private final String name;
    private final O0000o00 position;
    private final C0098O00000oo size;

    public O0000Oo0(String str, O0000o00 o0000o00, C0098O00000oo o00000oo, O00000Oo o00000Oo, boolean z) {
        this.name = str;
        this.position = o0000o00;
        this.size = o00000oo;
        this.cornerRadius = o00000Oo;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0014O0000oOO(o000OoO0, o00000o0, this);
    }

    public O00000Oo getCornerRadius() {
        return this.cornerRadius;
    }

    public String getName() {
        return this.name;
    }

    public O0000o00 getPosition() {
        return this.position;
    }

    public C0098O00000oo getSize() {
        return this.size;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RectangleShape{position=");
        sb.append(this.position);
        sb.append(", size=");
        sb.append(this.size);
        sb.append('}');
        return sb.toString();
    }
}
