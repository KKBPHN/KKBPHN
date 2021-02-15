package com.airbnb.lottie.model.content;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0016O0000oo;
import com.airbnb.lottie.model.O000000o.O000000o;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.content.O0000o0o reason: case insensitive filesystem */
public class C0109O0000o0o implements O00000Oo {
    private final float O00oOO0;
    private final ShapeStroke$LineJoinType O00oOO00;
    private final List O00oOO0O;
    private final ShapeStroke$LineCapType O00oOO0o;
    private final O000000o color;
    private final boolean hidden;
    private final String name;
    @Nullable
    private final O00000Oo offset;
    private final O00000o opacity;
    private final O00000Oo width;

    public C0109O0000o0o(String str, @Nullable O00000Oo o00000Oo, List list, O000000o o000000o, O00000o o00000o, O00000Oo o00000Oo2, ShapeStroke$LineCapType shapeStroke$LineCapType, ShapeStroke$LineJoinType shapeStroke$LineJoinType, float f, boolean z) {
        this.name = str;
        this.offset = o00000Oo;
        this.O00oOO0O = list;
        this.color = o000000o;
        this.opacity = o00000o;
        this.width = o00000Oo2;
        this.O00oOO0o = shapeStroke$LineCapType;
        this.O00oOO00 = shapeStroke$LineJoinType;
        this.O00oOO0 = f;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new C0016O0000oo(o000OoO0, o00000o0, this);
    }

    public ShapeStroke$LineCapType O00OoOo() {
        return this.O00oOO0o;
    }

    public O00000Oo O00OoOoO() {
        return this.offset;
    }

    public ShapeStroke$LineJoinType O00OoOoo() {
        return this.O00oOO00;
    }

    public float O00Ooo0() {
        return this.O00oOO0;
    }

    public List O00Ooo00() {
        return this.O00oOO0O;
    }

    public O000000o getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }

    public O00000o getOpacity() {
        return this.opacity;
    }

    public O00000Oo getWidth() {
        return this.width;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
