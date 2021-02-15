package com.airbnb.lottie.model.content;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0015O0000oOo;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000Oo implements O00000Oo {
    private final O00000Oo O00Ooo0;
    private final C0100O0000Ooo O00Ooo0O;
    private final boolean hidden;
    private final String name;
    private final O00000Oo offset;

    public O0000Oo(String str, O00000Oo o00000Oo, O00000Oo o00000Oo2, C0100O0000Ooo o0000Ooo, boolean z) {
        this.name = str;
        this.O00Ooo0 = o00000Oo;
        this.offset = o00000Oo2;
        this.O00Ooo0O = o0000Ooo;
        this.hidden = z;
    }

    @Nullable
    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0015O0000oOo(o000OoO0, o00000o0, this);
    }

    public O00000Oo getCopies() {
        return this.O00Ooo0;
    }

    public String getName() {
        return this.name;
    }

    public O00000Oo getOffset() {
        return this.offset;
    }

    public C0100O0000Ooo getTransform() {
        return this.O00Ooo0O;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
