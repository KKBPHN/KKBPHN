package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.C0029O0000oOo;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.O0000Oo;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000oOo reason: case insensitive filesystem */
public class C0015O0000oOo implements O0000O0o, O0000o, C0009O0000Ooo, O00000Oo, O0000o00 {
    private final C0083O000OoO0 O000OoO0;
    private final O0000O0o O00Ooo0;
    private final C0029O0000oOo O00Ooo0O;
    private C0007O00000oo O00Ooo0o;
    private final boolean hidden;
    private final O00000o0 layer;
    private final Matrix matrix = new Matrix();
    private final String name;
    private final O0000O0o offset;
    private final Path path = new Path();

    public C0015O0000oOo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O0000Oo o0000Oo) {
        this.O000OoO0 = o000OoO0;
        this.layer = o00000o0;
        this.name = o0000Oo.getName();
        this.hidden = o0000Oo.isHidden();
        this.O00Ooo0 = o0000Oo.getCopies().O00000o();
        o00000o0.O000000o(this.O00Ooo0);
        this.O00Ooo0.O00000Oo(this);
        this.offset = o0000Oo.getOffset().O00000o();
        o00000o0.O000000o(this.offset);
        this.offset.O00000Oo(this);
        this.O00Ooo0O = o0000Oo.getTransform().O00000o();
        this.O00Ooo0O.O000000o(o00000o0);
        this.O00Ooo0O.O000000o((O00000Oo) this);
    }

    public void O000000o(Canvas canvas, Matrix matrix2, int i) {
        float floatValue = ((Float) this.O00Ooo0.getValue()).floatValue();
        float floatValue2 = ((Float) this.offset.getValue()).floatValue();
        float floatValue3 = ((Float) this.O00Ooo0O.O00Oo0o0().getValue()).floatValue() / 100.0f;
        float floatValue4 = ((Float) this.O00Ooo0O.O00Oo0Oo().getValue()).floatValue() / 100.0f;
        for (int i2 = ((int) floatValue) - 1; i2 >= 0; i2--) {
            this.matrix.set(matrix2);
            float f = (float) i2;
            this.matrix.preConcat(this.O00Ooo0O.O0000Ooo(f + floatValue2));
            this.O00Ooo0o.O000000o(canvas, this.matrix, (int) (((float) i) * com.airbnb.lottie.O00000o.O0000O0o.lerp(floatValue3, floatValue4, f / floatValue)));
        }
    }

    public void O000000o(RectF rectF, Matrix matrix2, boolean z) {
        this.O00Ooo0o.O000000o(rectF, matrix2, z);
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    public void O000000o(Object obj, @Nullable com.airbnb.lottie.O00000oO.O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        if (!this.O00Ooo0O.O00000Oo(obj, o0000Oo)) {
            if (obj == C0087O000Ooo0.OO00OoO) {
                o0000O0o = this.O00Ooo0;
            } else {
                if (obj == C0087O000Ooo0.OO00Ooo) {
                    o0000O0o = this.offset;
                }
            }
            o0000O0o.O000000o(o0000Oo);
        }
    }

    public void O000000o(List list, List list2) {
        this.O00Ooo0o.O000000o(list, list2);
    }

    public void O000000o(ListIterator listIterator) {
        if (this.O00Ooo0o == null) {
            while (listIterator.hasPrevious() && listIterator.previous() != this) {
            }
            ArrayList arrayList = new ArrayList();
            while (listIterator.hasPrevious()) {
                arrayList.add(listIterator.previous());
                listIterator.remove();
            }
            Collections.reverse(arrayList);
            C0007O00000oo o00000oo = new C0007O00000oo(this.O000OoO0, this.layer, "Repeater", this.hidden, arrayList, null);
            this.O00Ooo0o = o00000oo;
        }
    }

    public void O00000oO() {
        this.O000OoO0.invalidateSelf();
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        Path path2 = this.O00Ooo0o.getPath();
        this.path.reset();
        float floatValue = ((Float) this.O00Ooo0.getValue()).floatValue();
        float floatValue2 = ((Float) this.offset.getValue()).floatValue();
        for (int i = ((int) floatValue) - 1; i >= 0; i--) {
            this.matrix.set(this.O00Ooo0O.O0000Ooo(((float) i) + floatValue2));
            this.path.addPath(path2, this.matrix);
        }
        return this.path;
    }
}
