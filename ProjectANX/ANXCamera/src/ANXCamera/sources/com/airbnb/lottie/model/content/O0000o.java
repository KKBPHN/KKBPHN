package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0018O0000ooO;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000o implements O00000Oo {
    private final O00000Oo end;
    private final boolean hidden;
    private final String name;
    private final O00000Oo offset;
    private final O00000Oo start;
    private final ShapeTrimPath$Type type;

    public O0000o(String str, ShapeTrimPath$Type shapeTrimPath$Type, O00000Oo o00000Oo, O00000Oo o00000Oo2, O00000Oo o00000Oo3, boolean z) {
        this.name = str;
        this.type = shapeTrimPath$Type;
        this.start = o00000Oo;
        this.end = o00000Oo2;
        this.offset = o00000Oo3;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0018O0000ooO(o00000o0, this);
    }

    public O00000Oo getEnd() {
        return this.end;
    }

    public String getName() {
        return this.name;
    }

    public O00000Oo getOffset() {
        return this.offset;
    }

    public O00000Oo getStart() {
        return this.start;
    }

    public ShapeTrimPath$Type getType() {
        return this.type;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trim Path: {start: ");
        sb.append(this.start);
        sb.append(", end: ");
        sb.append(this.end);
        sb.append(", offset: ");
        sb.append(this.offset);
        sb.append("}");
        return sb.toString();
    }
}
