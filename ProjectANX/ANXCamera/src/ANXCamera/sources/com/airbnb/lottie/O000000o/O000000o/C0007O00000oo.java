package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0029O0000oOo;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.C0103O00000oo;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.content.O0000o00;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O00000oo reason: case insensitive filesystem */
public class C0007O00000oo implements O0000O0o, O0000o, O00000Oo, C0103O00000oo {
    private final C0083O000OoO0 O000OoO0;
    @Nullable
    private List O00Oo0;
    private Paint O00Oo00;
    private RectF O00Oo00o;
    @Nullable
    private C0029O0000oOo O00Oo0OO;
    private final List contents;
    private final boolean hidden;
    private final Matrix matrix;
    private final String name;
    private final Path path;
    private final RectF rect;

    public C0007O00000oo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O0000o00 o0000o00) {
        this(o000OoO0, o00000o0, o0000o00.getName(), o0000o00.isHidden(), O000000o(o000OoO0, o00000o0, o0000o00.getItems()), O00000Oo(o0000o00.getItems()));
    }

    C0007O00000oo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, String str, boolean z, List list, @Nullable C0100O0000Ooo o0000Ooo) {
        this.O00Oo00 = new O000000o();
        this.O00Oo00o = new RectF();
        this.matrix = new Matrix();
        this.path = new Path();
        this.rect = new RectF();
        this.name = str;
        this.O000OoO0 = o000OoO0;
        this.hidden = z;
        this.contents = list;
        if (o0000Ooo != null) {
            this.O00Oo0OO = o0000Ooo.O00000o();
            this.O00Oo0OO.O000000o(o00000o0);
            this.O00Oo0OO.O000000o((O00000Oo) this);
        }
        ArrayList arrayList = new ArrayList();
        for (int size = list.size() - 1; size >= 0; size--) {
            C0006O00000oO o00000oO = (C0006O00000oO) list.get(size);
            if (o00000oO instanceof C0009O0000Ooo) {
                arrayList.add((C0009O0000Ooo) o00000oO);
            }
        }
        for (int size2 = arrayList.size() - 1; size2 >= 0; size2--) {
            ((C0009O0000Ooo) arrayList.get(size2)).O000000o(list.listIterator(list.size()));
        }
    }

    private static List O000000o(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, List list) {
        ArrayList arrayList = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            C0006O00000oO O000000o2 = ((com.airbnb.lottie.model.content.O00000Oo) list.get(i)).O000000o(o000OoO0, o00000o0);
            if (O000000o2 != null) {
                arrayList.add(O000000o2);
            }
        }
        return arrayList;
    }

    @Nullable
    static C0100O0000Ooo O00000Oo(List list) {
        for (int i = 0; i < list.size(); i++) {
            com.airbnb.lottie.model.content.O00000Oo o00000Oo = (com.airbnb.lottie.model.content.O00000Oo) list.get(i);
            if (o00000Oo instanceof C0100O0000Ooo) {
                return (C0100O0000Ooo) o00000Oo;
            }
        }
        return null;
    }

    private boolean Oo0oOo0() {
        int i = 0;
        for (int i2 = 0; i2 < this.contents.size(); i2++) {
            if (this.contents.get(i2) instanceof O0000O0o) {
                i++;
                if (i >= 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public void O000000o(Canvas canvas, Matrix matrix2, int i) {
        if (!this.hidden) {
            this.matrix.set(matrix2);
            C0029O0000oOo o0000oOo = this.O00Oo0OO;
            if (o0000oOo != null) {
                this.matrix.preConcat(o0000oOo.getMatrix());
                i = (int) ((((((float) (this.O00Oo0OO.getOpacity() == null ? 100 : ((Integer) this.O00Oo0OO.getOpacity().getValue()).intValue())) / 100.0f) * ((float) i)) / 255.0f) * 255.0f);
            }
            boolean z = this.O000OoO0.O000O0oo() && Oo0oOo0() && i != 255;
            if (z) {
                this.O00Oo00o.set(0.0f, 0.0f, 0.0f, 0.0f);
                O000000o(this.O00Oo00o, this.matrix, true);
                this.O00Oo00.setAlpha(i);
                O0000OOo.O000000o(canvas, this.O00Oo00o, this.O00Oo00);
            }
            if (z) {
                i = 255;
            }
            for (int size = this.contents.size() - 1; size >= 0; size--) {
                Object obj = this.contents.get(size);
                if (obj instanceof O0000O0o) {
                    ((O0000O0o) obj).O000000o(canvas, this.matrix, i);
                }
            }
            if (z) {
                canvas.restore();
            }
        }
    }

    public void O000000o(RectF rectF, Matrix matrix2, boolean z) {
        this.matrix.set(matrix2);
        C0029O0000oOo o0000oOo = this.O00Oo0OO;
        if (o0000oOo != null) {
            this.matrix.preConcat(o0000oOo.getMatrix());
        }
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        for (int size = this.contents.size() - 1; size >= 0; size--) {
            C0006O00000oO o00000oO = (C0006O00000oO) this.contents.get(size);
            if (o00000oO instanceof O0000O0o) {
                ((O0000O0o) o00000oO).O000000o(this.rect, this.matrix, z);
                rectF.union(this.rect);
            }
        }
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        if (o00000oO.O00000o(getName(), i)) {
            if (!"__container".equals(getName())) {
                o00000oO2 = o00000oO2.O000O0Oo(getName());
                if (o00000oO.O00000Oo(getName(), i)) {
                    list.add(o00000oO2.O000000o(this));
                }
            }
            if (o00000oO.O00000oO(getName(), i)) {
                int O00000o02 = i + o00000oO.O00000o0(getName(), i);
                for (int i2 = 0; i2 < this.contents.size(); i2++) {
                    C0006O00000oO o00000oO3 = (C0006O00000oO) this.contents.get(i2);
                    if (o00000oO3 instanceof C0103O00000oo) {
                        ((C0103O00000oo) o00000oO3).O000000o(o00000oO, O00000o02, list, o00000oO2);
                    }
                }
            }
        }
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        C0029O0000oOo o0000oOo = this.O00Oo0OO;
        if (o0000oOo != null) {
            o0000oOo.O00000Oo(obj, o0000Oo);
        }
    }

    public void O000000o(List list, List list2) {
        ArrayList arrayList = new ArrayList(list.size() + this.contents.size());
        arrayList.addAll(list);
        for (int size = this.contents.size() - 1; size >= 0; size--) {
            C0006O00000oO o00000oO = (C0006O00000oO) this.contents.get(size);
            o00000oO.O000000o(arrayList, this.contents.subList(0, size));
            arrayList.add(o00000oO);
        }
    }

    public void O00000oO() {
        this.O000OoO0.invalidateSelf();
    }

    /* access modifiers changed from: 0000 */
    public List O00OOOo() {
        if (this.O00Oo0 == null) {
            this.O00Oo0 = new ArrayList();
            for (int i = 0; i < this.contents.size(); i++) {
                C0006O00000oO o00000oO = (C0006O00000oO) this.contents.get(i);
                if (o00000oO instanceof O0000o) {
                    this.O00Oo0.add((O0000o) o00000oO);
                }
            }
        }
        return this.O00Oo0;
    }

    /* access modifiers changed from: 0000 */
    public Matrix O00OOo0() {
        C0029O0000oOo o0000oOo = this.O00Oo0OO;
        if (o0000oOo != null) {
            return o0000oOo.getMatrix();
        }
        this.matrix.reset();
        return this.matrix;
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        this.matrix.reset();
        C0029O0000oOo o0000oOo = this.O00Oo0OO;
        if (o0000oOo != null) {
            this.matrix.set(o0000oOo.getMatrix());
        }
        this.path.reset();
        if (this.hidden) {
            return this.path;
        }
        for (int size = this.contents.size() - 1; size >= 0; size--) {
            C0006O00000oO o00000oO = (C0006O00000oO) this.contents.get(size);
            if (o00000oO instanceof O0000o) {
                this.path.addPath(((O0000o) o00000oO).getPath(), this.matrix);
            }
        }
        return this.path;
    }
}
