package com.airbnb.lottie.model.content;

import android.graphics.Path.FillType;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0006O00000oO;
import com.airbnb.lottie.O000000o.O000000o.O0000Oo0;
import com.airbnb.lottie.model.O000000o.O000000o;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.layer.O00000o0;

/* renamed from: com.airbnb.lottie.model.content.O0000Ooo reason: case insensitive filesystem */
public class C0107O0000Ooo implements O00000Oo {
    @Nullable
    private final O000000o color;
    private final boolean fillEnabled;
    private final FillType fillType;
    private final boolean hidden;
    private final String name;
    @Nullable
    private final O00000o opacity;

    public C0107O0000Ooo(String str, boolean z, FillType fillType2, @Nullable O000000o o000000o, @Nullable O00000o o00000o, boolean z2) {
        this.name = str;
        this.fillEnabled = z;
        this.fillType = fillType2;
        this.color = o000000o;
        this.opacity = o00000o;
        this.hidden = z2;
    }

    public C0006O00000oO O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0) {
        return new O0000Oo0(o000OoO0, o00000o0, this);
    }

    @Nullable
    public O000000o getColor() {
        return this.color;
    }

    public FillType getFillType() {
        return this.fillType;
    }

    public String getName() {
        return this.name;
    }

    @Nullable
    public O00000o getOpacity() {
        return this.opacity;
    }

    public boolean isHidden() {
        return this.hidden;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShapeFill{color=, fillEnabled=");
        sb.append(this.fillEnabled);
        sb.append('}');
        return sb.toString();
    }
}
