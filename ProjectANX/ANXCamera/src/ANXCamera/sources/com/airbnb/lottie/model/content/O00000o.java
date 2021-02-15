package com.airbnb.lottie.model.content;

import android.graphics.Path.FillType;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.O0000Oo;
import com.airbnb.lottie.model.O000000o.C0098O00000oo;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O000000o.O00000o0;

public class O00000o implements O00000Oo {
    private final O00000o0 O00o00Oo;
    @Nullable
    private final O00000Oo O00oO;
    private final GradientType O00oO0oO;
    @Nullable
    private final O00000Oo O00oO0oo;
    private final C0098O00000oo endPoint;
    private final FillType fillType;
    private final boolean hidden;
    private final String name;
    private final com.airbnb.lottie.model.O000000o.O00000o opacity;
    private final C0098O00000oo startPoint;

    public O00000o(String str, GradientType gradientType, FillType fillType2, O00000o0 o00000o0, com.airbnb.lottie.model.O000000o.O00000o o00000o, C0098O00000oo o00000oo, C0098O00000oo o00000oo2, O00000Oo o00000Oo, O00000Oo o00000Oo2, boolean z) {
        this.O00oO0oO = gradientType;
        this.fillType = fillType2;
        this.O00o00Oo = o00000o0;
        this.opacity = o00000o;
        this.startPoint = o00000oo;
        this.endPoint = o00000oo2;
        this.name = str;
        this.O00oO0oo = o00000Oo;
        this.O00oO = o00000Oo2;
        this.hidden = z;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, com.airbnb.lottie.model.layer.O00000o0 o00000o0) {
        return new O0000Oo(o000OoO0, o00000o0, this);
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public O00000Oo O00OoOO() {
        return this.O00oO;
    }

    public O00000o0 O00OoOO0() {
        return this.O00o00Oo;
    }

    /* access modifiers changed from: 0000 */
    @Nullable
    public O00000Oo O00OoOo0() {
        return this.O00oO0oo;
    }

    public C0098O00000oo getEndPoint() {
        return this.endPoint;
    }

    public FillType getFillType() {
        return this.fillType;
    }

    public GradientType getGradientType() {
        return this.O00oO0oO;
    }

    public String getName() {
        return this.name;
    }

    public com.airbnb.lottie.model.O000000o.O00000o getOpacity() {
        return this.opacity;
    }

    public C0098O00000oo getStartPoint() {
        return this.startPoint;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
