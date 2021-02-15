package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.layer.O00000oO reason: case insensitive filesystem */
public class C0111O00000oO extends O00000o0 {
    private Paint O00ooO = new Paint();
    @Nullable
    private O0000O0o O00ooO0O;
    private final RectF O00ooO0o = new RectF();
    @Nullable
    private Boolean O00ooOO0;
    @Nullable
    private Boolean O00ooOo0;
    private final List layers = new ArrayList();
    private final RectF rect = new RectF();

    public C0111O00000oO(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o, List list, C0064O0000o0O o0000o0O) {
        int i;
        super(o000OoO0, o0000O0o);
        O00000Oo O00o00oo = o0000O0o.O00o00oo();
        if (O00o00oo != null) {
            this.O00ooO0O = O00o00oo.O00000o();
            O000000o(this.O00ooO0O);
            this.O00ooO0O.O00000Oo(this);
        } else {
            this.O00ooO0O = null;
        }
        LongSparseArray longSparseArray = new LongSparseArray(o0000o0O.getLayers().size());
        int size = list.size() - 1;
        O00000o0 o00000o0 = null;
        while (true) {
            if (size < 0) {
                break;
            }
            O0000O0o o0000O0o2 = (O0000O0o) list.get(size);
            O00000o0 O000000o2 = O00000o0.O000000o(o0000O0o2, o000OoO0, o0000o0O);
            if (O000000o2 != null) {
                longSparseArray.put(O000000o2.O00o0000().getId(), O000000o2);
                if (o00000o0 != null) {
                    o00000o0.O00000Oo(O000000o2);
                    o00000o0 = null;
                } else {
                    this.layers.add(0, O000000o2);
                    int i2 = O00000o.O00oooO[o0000O0o2.O00o00().ordinal()];
                    if (i2 == 1 || i2 == 2) {
                        o00000o0 = O000000o2;
                    }
                }
            }
            size--;
        }
        for (i = 0; i < longSparseArray.size(); i++) {
            O00000o0 o00000o02 = (O00000o0) longSparseArray.get(longSparseArray.keyAt(i));
            if (o00000o02 != null) {
                O00000o0 o00000o03 = (O00000o0) longSparseArray.get(o00000o02.O00o0000().getParentId());
                if (o00000o03 != null) {
                    o00000o02.O00000o0(o00000o03);
                }
            }
        }
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        super.O000000o(rectF, matrix, z);
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
            ((O00000o0) this.layers.get(size)).O000000o(this.rect, this.O00oo0OO, true);
            rectF.union(this.rect);
        }
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        super.O000000o(obj, o0000Oo);
        if (obj != C0087O000Ooo0.OO00oo) {
            return;
        }
        if (o0000Oo == null) {
            this.O00ooO0O = null;
            return;
        }
        this.O00ooO0O = new C0031O0000oo0(o0000Oo);
        O000000o(this.O00ooO0O);
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(Canvas canvas, Matrix matrix, int i) {
        String str = "CompositionLayer#draw";
        C0053O00000oO.beginSection(str);
        this.O00ooO0o.set(0.0f, 0.0f, (float) this.O00oo0Oo.O00o00O(), (float) this.O00oo0Oo.O00o00O0());
        matrix.mapRect(this.O00ooO0o);
        boolean z = this.O000OoO0.O000O0oo() && this.layers.size() > 1 && i != 255;
        if (z) {
            this.O00ooO.setAlpha(i);
            O0000OOo.O000000o(canvas, this.O00ooO0o, this.O00ooO);
        } else {
            canvas.save();
        }
        if (z) {
            i = 255;
        }
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            if (!this.O00ooO0o.isEmpty() ? canvas.clipRect(this.O00ooO0o) : true) {
                ((O00000o0) this.layers.get(size)).O000000o(canvas, matrix, i);
            }
        }
        canvas.restore();
        C0053O00000oO.O0000oOo(str);
    }

    /* access modifiers changed from: protected */
    public void O00000Oo(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        for (int i2 = 0; i2 < this.layers.size(); i2++) {
            ((O00000o0) this.layers.get(i2)).O000000o(o00000oO, i, list, o00000oO2);
        }
    }

    public boolean O000O0o() {
        if (this.O00ooOO0 == null) {
            int size = this.layers.size() - 1;
            while (size >= 0) {
                O00000o0 o00000o0 = (O00000o0) this.layers.get(size);
                if (!(o00000o0 instanceof O0000Oo0)) {
                    if ((o00000o0 instanceof C0111O00000oO) && ((C0111O00000oO) o00000o0).O000O0o()) {
                    }
                    size--;
                } else if (!o00000o0.O00o000()) {
                    size--;
                }
                this.O00ooOO0 = Boolean.valueOf(true);
                return true;
            }
            this.O00ooOO0 = Boolean.valueOf(false);
        }
        return this.O00ooOO0.booleanValue();
    }

    public boolean O000O0oO() {
        if (this.O00ooOo0 == null) {
            if (!O00o000O()) {
                int size = this.layers.size() - 1;
                while (size >= 0) {
                    if (!((O00000o0) this.layers.get(size)).O00o000O()) {
                        size--;
                    }
                }
                this.O00ooOo0 = Boolean.valueOf(false);
            }
            this.O00ooOo0 = Boolean.valueOf(true);
            return true;
        }
        return this.O00ooOo0.booleanValue();
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        super.setProgress(f);
        if (this.O00ooO0O != null) {
            f = ((((Float) this.O00ooO0O.getValue()).floatValue() * this.O00oo0Oo.O000O0OO().getFrameRate()) - this.O00oo0Oo.O000O0OO().O00O0oOO()) / (this.O000OoO0.O000O0OO().O00O0o0() + 0.01f);
        }
        if (this.O00oo0Oo.O00o0() != 0.0f) {
            f /= this.O00oo0Oo.O00o0();
        }
        if (this.O00ooO0O == null) {
            f -= this.O00oo0Oo.O00o00o();
        }
        for (int size = this.layers.size() - 1; size >= 0; size--) {
            ((O00000o0) this.layers.get(size)).setProgress(f);
        }
    }
}
