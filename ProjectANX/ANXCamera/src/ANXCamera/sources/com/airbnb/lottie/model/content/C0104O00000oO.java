package com.airbnb.lottie.model.content;

import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.C0008O0000OoO;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.O000000o.O00000o0;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.content.O00000oO reason: case insensitive filesystem */
public class C0104O00000oO implements O00000Oo {
    private final O00000o0 O00o00Oo;
    private final GradientType O00oO0oO;
    private final float O00oOO0;
    private final ShapeStroke$LineJoinType O00oOO00;
    private final List O00oOO0O;
    private final ShapeStroke$LineCapType O00oOO0o;
    @Nullable
    private final O00000Oo O00oOo;
    private final C0098O00000oo endPoint;
    private final boolean hidden;
    private final String name;
    private final O00000o opacity;
    private final C0098O00000oo startPoint;
    private final O00000Oo width;

    public C0104O00000oO(String str, GradientType gradientType, O00000o0 o00000o0, O00000o o00000o, C0098O00000oo o00000oo, C0098O00000oo o00000oo2, O00000Oo o00000Oo, ShapeStroke$LineCapType shapeStroke$LineCapType, ShapeStroke$LineJoinType shapeStroke$LineJoinType, float f, List list, @Nullable O00000Oo o00000Oo2, boolean z) {
        this.name = str;
        this.O00oO0oO = gradientType;
        this.O00o00Oo = o00000o0;
        this.opacity = o00000o;
        this.startPoint = o00000oo;
        this.endPoint = o00000oo2;
        this.width = o00000Oo;
        this.O00oOO0o = shapeStroke$LineCapType;
        this.O00oOO00 = shapeStroke$LineJoinType;
        this.O00oOO0 = f;
        this.O00oOO0O = list;
        this.O00oOo = o00000Oo2;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, com.airbnb.lottie.model.layer.O00000o0 o00000o0) {
        return new C0008O0000OoO(o000OoO0, o00000o0, this);
    }

    public O00000o0 O00OoOO0() {
        return this.O00o00Oo;
    }

    public ShapeStroke$LineCapType O00OoOo() {
        return this.O00oOO0o;
    }

    @Nullable
    public O00000Oo O00OoOoO() {
        return this.O00oOo;
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

    public C0098O00000oo getEndPoint() {
        return this.endPoint;
    }

    public GradientType getGradientType() {
        return this.O00oO0oO;
    }

    public String getName() {
        return this.name;
    }

    public O00000o getOpacity() {
        return this.opacity;
    }

    public C0098O00000oo getStartPoint() {
        return this.startPoint;
    }

    public O00000Oo getWidth() {
        return this.width;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
