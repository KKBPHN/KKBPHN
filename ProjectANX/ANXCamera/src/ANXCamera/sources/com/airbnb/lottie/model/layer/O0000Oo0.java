package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o.C0007O00000oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.O0000o00;
import java.util.Collections;
import java.util.List;

public class O0000Oo0 extends O00000o0 {
    private final C0007O00000oo O00Ooo0o;

    O0000Oo0(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o) {
        super(o000OoO0, o0000O0o);
        this.O00Ooo0o = new C0007O00000oo(o000OoO0, this, new O0000o00("__container", o0000O0o.O00Oo(), false));
        this.O00Ooo0o.O000000o(Collections.emptyList(), Collections.emptyList());
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        super.O000000o(rectF, matrix, z);
        this.O00Ooo0o.O000000o(rectF, this.O00oo0OO, z);
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(@NonNull Canvas canvas, Matrix matrix, int i) {
        this.O00Ooo0o.O000000o(canvas, matrix, i);
    }

    /* access modifiers changed from: protected */
    public void O00000Oo(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        this.O00Ooo0o.O000000o(o00000oO, i, list, o00000oO2);
    }
}
