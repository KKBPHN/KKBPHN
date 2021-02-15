package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O000000o.O00000Oo.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.content.C0109O0000o0o;
import com.airbnb.lottie.model.layer.O00000o0;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000oo reason: case insensitive filesystem */
public class C0016O0000oo extends O00000o0 {
    @Nullable
    private O0000O0o O00O0oo;
    private final O0000O0o O00OOOo;
    private final boolean hidden;
    private final O00000o0 layer;
    private final String name;

    public C0016O0000oo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, C0109O0000o0o o0000o0o) {
        super(o000OoO0, o00000o0, o0000o0o.O00OoOo().Oo0o0oO(), o0000o0o.O00OoOoo().Oo0o0oo(), o0000o0o.O00Ooo0(), o0000o0o.getOpacity(), o0000o0o.getWidth(), o0000o0o.O00Ooo00(), o0000o0o.O00OoOoO());
        this.layer = o00000o0;
        this.name = o0000o0o.getName();
        this.hidden = o0000o0o.isHidden();
        this.O00OOOo = o0000o0o.getColor().O00000o();
        this.O00OOOo.O00000Oo(this);
        o00000o0.O000000o(this.O00OOOo);
    }

    public void O000000o(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            this.paint.setColor(((O0000OOo) this.O00OOOo).getIntValue());
            O0000O0o o0000O0o = this.O00O0oo;
            if (o0000O0o != null) {
                this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
            }
            super.O000000o(canvas, matrix, i);
        }
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        super.O000000o(obj, o0000Oo);
        if (obj == C0087O000Ooo0.STROKE_COLOR) {
            this.O00OOOo.O000000o(o0000Oo);
        } else if (obj != C0087O000Ooo0.OO00ooO) {
        } else {
            if (o0000Oo == null) {
                this.O00O0oo = null;
                return;
            }
            this.O00O0oo = new C0031O0000oo0(o0000Oo);
            this.O00O0oo.O00000Oo(this);
            this.layer.O000000o(this.O00OOOo);
        }
    }

    public String getName() {
        return this.name;
    }
}
