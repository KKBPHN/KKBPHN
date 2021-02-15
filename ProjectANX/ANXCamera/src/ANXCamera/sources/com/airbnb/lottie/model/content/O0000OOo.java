package com.airbnb.lottie.model.content;

import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0012O0000oO;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O0000o00;
import com.airbnb.lottie.model.layer.O00000o0;

public class O0000OOo implements O00000Oo {
    private final O00000Oo O00oOOOO;
    private final O00000Oo O00oOOOo;
    private final boolean hidden;
    private final O00000Oo innerRadius;
    private final String name;
    private final O00000Oo outerRadius;
    private final O00000Oo points;
    private final O0000o00 position;
    private final O00000Oo rotation;
    private final PolystarShape$Type type;

    public O0000OOo(String str, PolystarShape$Type polystarShape$Type, O00000Oo o00000Oo, O0000o00 o0000o00, O00000Oo o00000Oo2, O00000Oo o00000Oo3, O00000Oo o00000Oo4, O00000Oo o00000Oo5, O00000Oo o00000Oo6, boolean z) {
        this.name = str;
        this.type = polystarShape$Type;
        this.points = o00000Oo;
        this.position = o0000o00;
        this.rotation = o00000Oo2;
        this.innerRadius = o00000Oo3;
        this.outerRadius = o00000Oo4;
        this.O00oOOOO = o00000Oo5;
        this.O00oOOOo = o00000Oo6;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0012O0000oO(o000OoO0, o00000o0, this);
    }

    public O00000Oo O00OooO() {
        return this.O00oOOOO;
    }

    public O00000Oo O00OooOo() {
        return this.outerRadius;
    }

    public O00000Oo O00Oooo0() {
        return this.O00oOOOo;
    }

    public O00000Oo getInnerRadius() {
        return this.innerRadius;
    }

    public String getName() {
        return this.name;
    }

    public O00000Oo getPoints() {
        return this.points;
    }

    public O0000o00 getPosition() {
        return this.position;
    }

    public O00000Oo getRotation() {
        return this.rotation;
    }

    public PolystarShape$Type getType() {
        return this.type;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
