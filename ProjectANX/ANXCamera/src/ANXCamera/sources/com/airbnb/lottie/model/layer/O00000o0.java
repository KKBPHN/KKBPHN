package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build.VERSION;
import androidx.annotation.CallSuper;
import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O000000o.O0000O0o;
import com.airbnb.lottie.O000000o.O00000Oo.C0029O0000oOo;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000Oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000o00;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.C0103O00000oo;
import com.airbnb.lottie.model.content.C0105O00000oo;
import com.airbnb.lottie.model.content.Mask$MaskMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class O00000o0 implements O0000O0o, O00000Oo, C0103O00000oo {
    private static final int CLIP_SAVE_FLAG = 2;
    private static final int CLIP_TO_LAYER_SAVE_FLAG = 16;
    private static final int MATRIX_SAVE_FLAG = 1;
    private static final int O00ooO0 = 19;
    final C0083O000OoO0 O000OoO0;
    final C0029O0000oOo O00Ooo0O;
    private final Paint O00oOoOO = new O000000o(1);
    private final Paint O00oOoo = new O000000o(1, Mode.DST_OUT);
    private final Paint O00oOoo0 = new O000000o(1, Mode.DST_IN);
    private final List O00oo = new ArrayList();
    private final RectF O00oo0 = new RectF();
    private final Paint O00oo00 = new O000000o(1);
    private final Paint O00oo00O = new O000000o(Mode.CLEAR);
    private final RectF O00oo00o = new RectF();
    private final String O00oo0O;
    private final RectF O00oo0O0 = new RectF();
    final Matrix O00oo0OO = new Matrix();
    final O0000O0o O00oo0Oo;
    @Nullable
    private O00000o0 O00oo0o;
    @Nullable
    private O00000o0 O00oo0o0;
    private List O00oo0oo;
    @Nullable
    private O0000o00 mask;
    private final Matrix matrix = new Matrix();
    private final Path path = new Path();
    private final RectF rect = new RectF();
    private boolean visible = true;

    O00000o0(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o) {
        Paint paint;
        PorterDuffXfermode porterDuffXfermode;
        this.O000OoO0 = o000OoO0;
        this.O00oo0Oo = o0000O0o;
        StringBuilder sb = new StringBuilder();
        sb.append(o0000O0o.getName());
        sb.append("#draw");
        this.O00oo0O = sb.toString();
        if (o0000O0o.O00o00() == Layer$MatteType.INVERT) {
            paint = this.O00oo00;
            porterDuffXfermode = new PorterDuffXfermode(Mode.DST_OUT);
        } else {
            paint = this.O00oo00;
            porterDuffXfermode = new PorterDuffXfermode(Mode.DST_IN);
        }
        paint.setXfermode(porterDuffXfermode);
        this.O00Ooo0O = o0000O0o.getTransform().O00000o();
        this.O00Ooo0O.O000000o((O00000Oo) this);
        if (o0000O0o.O00Oo0() != null && !o0000O0o.O00Oo0().isEmpty()) {
            this.mask = new O0000o00(o0000O0o.O00Oo0());
            for (com.airbnb.lottie.O000000o.O00000Oo.O0000O0o O00000Oo2 : this.mask.O00Oo00o()) {
                O00000Oo2.O00000Oo(this);
            }
            for (com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2 : this.mask.O00Oo0OO()) {
                O000000o(o0000O0o2);
                o0000O0o2.O00000Oo(this);
            }
        }
        Oo0ooO0();
    }

    @Nullable
    static O00000o0 O000000o(O0000O0o o0000O0o, C0083O000OoO0 o000OoO0, C0064O0000o0O o0000o0O) {
        switch (O00000Oo.O00oOo0o[o0000O0o.getLayerType().ordinal()]) {
            case 1:
                return new O0000Oo0(o000OoO0, o0000O0o);
            case 2:
                return new C0111O00000oO(o000OoO0, o0000O0o, o0000o0O.O0000ooo(o0000O0o.O00o00OO()), o0000o0O);
            case 3:
                return new O0000Oo(o000OoO0, o0000O0o);
            case 4:
                return new C0112O00000oo(o000OoO0, o0000O0o);
            case 5:
                return new O0000OOo(o000OoO0, o0000O0o);
            case 6:
                return new O0000o0(o000OoO0, o0000O0o);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown layer type ");
                sb.append(o0000O0o.getLayerType());
                O00000o.warning(sb.toString());
                return null;
        }
    }

    private void O000000o(Canvas canvas, Matrix matrix2) {
        String str = "Layer#saveLayer";
        C0053O00000oO.beginSection(str);
        O0000OOo.O000000o(canvas, this.rect, this.O00oOoo0, 19);
        if (VERSION.SDK_INT < 28) {
            canvas.drawColor(0);
        }
        C0053O00000oO.O0000oOo(str);
        for (int i = 0; i < this.mask.O00Oo0().size(); i++) {
            C0105O00000oo o00000oo = (C0105O00000oo) this.mask.O00Oo0().get(i);
            com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o = (com.airbnb.lottie.O000000o.O00000Oo.O0000O0o) this.mask.O00Oo00o().get(i);
            com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2 = (com.airbnb.lottie.O000000o.O00000Oo.O0000O0o) this.mask.O00Oo0OO().get(i);
            int i2 = O00000Oo.O00oOoO[o00000oo.O00Ooo0O().ordinal()];
            if (i2 != 1) {
                if (i2 == 2) {
                    if (i == 0) {
                        this.O00oOoOO.setColor(ViewCompat.MEASURED_STATE_MASK);
                        this.O00oOoOO.setAlpha(255);
                        canvas.drawRect(this.rect, this.O00oOoOO);
                    }
                    Canvas canvas2 = canvas;
                    Matrix matrix3 = matrix2;
                    if (o00000oo.O00OooO0()) {
                        O00000oO(canvas2, matrix3, o00000oo, o0000O0o, o0000O0o2);
                    } else {
                        O00000oo(canvas2, matrix3, o00000oo, o0000O0o, o0000O0o2);
                    }
                } else if (i2 == 3) {
                    Canvas canvas3 = canvas;
                    Matrix matrix4 = matrix2;
                    if (o00000oo.O00OooO0()) {
                        O00000o(canvas3, matrix4, o00000oo, o0000O0o, o0000O0o2);
                    } else {
                        O00000Oo(canvas3, matrix4, o00000oo, o0000O0o, o0000O0o2);
                    }
                } else if (i2 == 4) {
                    Canvas canvas4 = canvas;
                    Matrix matrix5 = matrix2;
                    if (o00000oo.O00OooO0()) {
                        O00000o0(canvas4, matrix5, o00000oo, o0000O0o, o0000O0o2);
                    } else {
                        O000000o(canvas4, matrix5, o00000oo, o0000O0o, o0000O0o2);
                    }
                }
            } else if (Oo0oo0o()) {
                this.O00oOoOO.setAlpha(255);
                canvas.drawRect(this.rect, this.O00oOoOO);
            }
        }
        String str2 = "Layer#restoreLayer";
        C0053O00000oO.beginSection(str2);
        canvas.restore();
        C0053O00000oO.O0000oOo(str2);
    }

    private void O000000o(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        this.O00oOoOO.setAlpha((int) (((float) ((Integer) o0000O0o2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.O00oOoOO);
    }

    private void O000000o(RectF rectF, Matrix matrix2) {
        this.O00oo00o.set(0.0f, 0.0f, 0.0f, 0.0f);
        if (O00o000()) {
            int size = this.mask.O00Oo0().size();
            int i = 0;
            while (i < size) {
                C0105O00000oo o00000oo = (C0105O00000oo) this.mask.O00Oo0().get(i);
                this.path.set((Path) ((com.airbnb.lottie.O000000o.O00000Oo.O0000O0o) this.mask.O00Oo00o().get(i)).getValue());
                this.path.transform(matrix2);
                int i2 = O00000Oo.O00oOoO[o00000oo.O00Ooo0O().ordinal()];
                if (i2 != 1 && i2 != 2) {
                    if ((i2 != 3 && i2 != 4) || !o00000oo.O00OooO0()) {
                        this.path.computeBounds(this.O00oo0O0, false);
                        RectF rectF2 = this.O00oo00o;
                        if (i == 0) {
                            rectF2.set(this.O00oo0O0);
                        } else {
                            rectF2.set(Math.min(rectF2.left, this.O00oo0O0.left), Math.min(this.O00oo00o.top, this.O00oo0O0.top), Math.max(this.O00oo00o.right, this.O00oo0O0.right), Math.max(this.O00oo00o.bottom, this.O00oo0O0.bottom));
                        }
                        i++;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            if (!rectF.intersect(this.O00oo00o)) {
                rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    private void O00000Oo(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        O0000OOo.O000000o(canvas, this.rect, this.O00oOoo0);
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        this.O00oOoOO.setAlpha((int) (((float) ((Integer) o0000O0o2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.O00oOoOO);
        canvas.restore();
    }

    private void O00000Oo(RectF rectF, Matrix matrix2) {
        if (O00o000O() && this.O00oo0Oo.O00o00() != Layer$MatteType.INVERT) {
            this.O00oo0.set(0.0f, 0.0f, 0.0f, 0.0f);
            this.O00oo0o0.O000000o(this.O00oo0, matrix2, true);
            if (!rectF.intersect(this.O00oo0)) {
                rectF.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    private void O00000o(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        O0000OOo.O000000o(canvas, this.rect, this.O00oOoo0);
        canvas.drawRect(this.rect, this.O00oOoOO);
        this.O00oOoo.setAlpha((int) (((float) ((Integer) o0000O0o2.getValue()).intValue()) * 2.55f));
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        canvas.drawPath(this.path, this.O00oOoo);
        canvas.restore();
    }

    private void O00000o0(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        O0000OOo.O000000o(canvas, this.rect, this.O00oOoOO);
        canvas.drawRect(this.rect, this.O00oOoOO);
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        this.O00oOoOO.setAlpha((int) (((float) ((Integer) o0000O0o2.getValue()).intValue()) * 2.55f));
        canvas.drawPath(this.path, this.O00oOoo);
        canvas.restore();
    }

    private void O00000oO(Canvas canvas) {
        String str = "Layer#clearLayer";
        C0053O00000oO.beginSection(str);
        RectF rectF = this.rect;
        canvas.drawRect(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f, this.O00oo00O);
        C0053O00000oO.O0000oOo(str);
    }

    private void O00000oO(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        O0000OOo.O000000o(canvas, this.rect, this.O00oOoo);
        canvas.drawRect(this.rect, this.O00oOoOO);
        this.O00oOoo.setAlpha((int) (((float) ((Integer) o0000O0o2.getValue()).intValue()) * 2.55f));
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        canvas.drawPath(this.path, this.O00oOoo);
        canvas.restore();
    }

    private void O00000oo(Canvas canvas, Matrix matrix2, C0105O00000oo o00000oo, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o, com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o2) {
        this.path.set((Path) o0000O0o.getValue());
        this.path.transform(matrix2);
        canvas.drawPath(this.path, this.O00oOoo);
    }

    private void O0000o0O(float f) {
        this.O000OoO0.O000O0OO().O00oOoOo().O000000o(this.O00oo0Oo.getName(), f);
    }

    private void Oo0oo() {
        if (this.O00oo0oo == null) {
            if (this.O00oo0o == null) {
                this.O00oo0oo = Collections.emptyList();
                return;
            }
            this.O00oo0oo = new ArrayList();
            for (O00000o0 o00000o0 = this.O00oo0o; o00000o0 != null; o00000o0 = o00000o0.O00oo0o) {
                this.O00oo0oo.add(o00000o0);
            }
        }
    }

    private boolean Oo0oo0o() {
        if (this.mask.O00Oo00o().isEmpty()) {
            return false;
        }
        for (int i = 0; i < this.mask.O00Oo0().size(); i++) {
            if (((C0105O00000oo) this.mask.O00Oo0().get(i)).O00Ooo0O() != Mask$MaskMode.MASK_MODE_NONE) {
                return false;
            }
        }
        return true;
    }

    private void Oo0ooO0() {
        boolean z = true;
        if (!this.O00oo0Oo.O00o000o().isEmpty()) {
            O0000Oo0 o0000Oo0 = new O0000Oo0(this.O00oo0Oo.O00o000o());
            o0000Oo0.O00Oo00();
            o0000Oo0.O00000Oo(new O000000o(this, o0000Oo0));
            if (((Float) o0000Oo0.getValue()).floatValue() != 1.0f) {
                z = false;
            }
            setVisible(z);
            O000000o(o0000Oo0);
            return;
        }
        setVisible(true);
    }

    private void invalidateSelf() {
        this.O000OoO0.invalidateSelf();
    }

    /* access modifiers changed from: private */
    public void setVisible(boolean z) {
        if (z != this.visible) {
            this.visible = z;
            invalidateSelf();
        }
    }

    public void O000000o(Canvas canvas, Matrix matrix2, int i) {
        C0053O00000oO.beginSection(this.O00oo0O);
        if (!this.visible || this.O00oo0Oo.isHidden()) {
            C0053O00000oO.O0000oOo(this.O00oo0O);
            return;
        }
        Oo0oo();
        String str = "Layer#parentMatrix";
        C0053O00000oO.beginSection(str);
        this.matrix.reset();
        this.matrix.set(matrix2);
        for (int size = this.O00oo0oo.size() - 1; size >= 0; size--) {
            this.matrix.preConcat(((O00000o0) this.O00oo0oo.get(size)).O00Ooo0O.getMatrix());
        }
        C0053O00000oO.O0000oOo(str);
        int intValue = (int) ((((((float) i) / 255.0f) * ((float) (this.O00Ooo0O.getOpacity() == null ? 100 : ((Integer) this.O00Ooo0O.getOpacity().getValue()).intValue()))) / 100.0f) * 255.0f);
        String str2 = "Layer#drawLayer";
        if (O00o000O() || O00o000()) {
            String str3 = "Layer#computeBounds";
            C0053O00000oO.beginSection(str3);
            O000000o(this.rect, this.matrix, false);
            O00000Oo(this.rect, matrix2);
            this.matrix.preConcat(this.O00Ooo0O.getMatrix());
            O000000o(this.rect, this.matrix);
            if (!this.rect.intersect(0.0f, 0.0f, (float) canvas.getWidth(), (float) canvas.getHeight())) {
                this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
            }
            C0053O00000oO.O0000oOo(str3);
            if (!this.rect.isEmpty()) {
                String str4 = "Layer#saveLayer";
                C0053O00000oO.beginSection(str4);
                this.O00oOoOO.setAlpha(255);
                O0000OOo.O000000o(canvas, this.rect, this.O00oOoOO);
                C0053O00000oO.O0000oOo(str4);
                O00000oO(canvas);
                C0053O00000oO.beginSection(str2);
                O00000Oo(canvas, this.matrix, intValue);
                C0053O00000oO.O0000oOo(str2);
                if (O00o000()) {
                    O000000o(canvas, this.matrix);
                }
                String str5 = "Layer#restoreLayer";
                if (O00o000O()) {
                    String str6 = "Layer#drawMatte";
                    C0053O00000oO.beginSection(str6);
                    C0053O00000oO.beginSection(str4);
                    O0000OOo.O000000o(canvas, this.rect, this.O00oo00, 19);
                    C0053O00000oO.O0000oOo(str4);
                    O00000oO(canvas);
                    this.O00oo0o0.O000000o(canvas, matrix2, intValue);
                    C0053O00000oO.beginSection(str5);
                    canvas.restore();
                    C0053O00000oO.O0000oOo(str5);
                    C0053O00000oO.O0000oOo(str6);
                }
                C0053O00000oO.beginSection(str5);
                canvas.restore();
                C0053O00000oO.O0000oOo(str5);
            }
            O0000o0O(C0053O00000oO.O0000oOo(this.O00oo0O));
            return;
        }
        this.matrix.preConcat(this.O00Ooo0O.getMatrix());
        C0053O00000oO.beginSection(str2);
        O00000Oo(canvas, this.matrix, intValue);
        C0053O00000oO.O0000oOo(str2);
        O0000o0O(C0053O00000oO.O0000oOo(this.O00oo0O));
    }

    @CallSuper
    public void O000000o(RectF rectF, Matrix matrix2, boolean z) {
        this.rect.set(0.0f, 0.0f, 0.0f, 0.0f);
        Oo0oo();
        this.O00oo0OO.set(matrix2);
        if (z) {
            List list = this.O00oo0oo;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    this.O00oo0OO.preConcat(((O00000o0) this.O00oo0oo.get(size)).O00Ooo0O.getMatrix());
                }
            } else {
                O00000o0 o00000o0 = this.O00oo0o;
                if (o00000o0 != null) {
                    this.O00oo0OO.preConcat(o00000o0.O00Ooo0O.getMatrix());
                }
            }
        }
        this.O00oo0OO.preConcat(this.O00Ooo0O.getMatrix());
    }

    public void O000000o(@Nullable com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o) {
        if (o0000O0o != null) {
            this.O00oo.add(o0000O0o);
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
                O00000Oo(o00000oO, i + o00000oO.O00000o0(getName(), i), list, o00000oO2);
            }
        }
    }

    @CallSuper
    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        this.O00Ooo0O.O00000Oo(obj, o0000Oo);
    }

    public void O000000o(List list, List list2) {
    }

    public abstract void O00000Oo(Canvas canvas, Matrix matrix2, int i);

    public void O00000Oo(com.airbnb.lottie.O000000o.O00000Oo.O0000O0o o0000O0o) {
        this.O00oo.remove(o0000O0o);
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(@Nullable O00000o0 o00000o0) {
        this.O00oo0o0 = o00000o0;
    }

    /* access modifiers changed from: 0000 */
    public void O00000o0(@Nullable O00000o0 o00000o0) {
        this.O00oo0o = o00000o0;
    }

    public void O00000oO() {
        invalidateSelf();
    }

    /* access modifiers changed from: 0000 */
    public boolean O00o000() {
        O0000o00 o0000o00 = this.mask;
        return o0000o00 != null && !o0000o00.O00Oo00o().isEmpty();
    }

    /* access modifiers changed from: 0000 */
    public O0000O0o O00o0000() {
        return this.O00oo0Oo;
    }

    /* access modifiers changed from: 0000 */
    public boolean O00o000O() {
        return this.O00oo0o0 != null;
    }

    public String getName() {
        return this.O00oo0Oo.getName();
    }

    /* access modifiers changed from: 0000 */
    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        this.O00Ooo0O.setProgress(f);
        if (this.mask != null) {
            for (int i = 0; i < this.mask.O00Oo00o().size(); i++) {
                ((com.airbnb.lottie.O000000o.O00000Oo.O0000O0o) this.mask.O00Oo00o().get(i)).setProgress(f);
            }
        }
        if (this.O00oo0Oo.O00o0() != 0.0f) {
            f /= this.O00oo0Oo.O00o0();
        }
        O00000o0 o00000o0 = this.O00oo0o0;
        if (o00000o0 != null) {
            this.O00oo0o0.setProgress(o00000o0.O00oo0Oo.O00o0() * f);
        }
        for (int i2 = 0; i2 < this.O00oo.size(); i2++) {
            ((com.airbnb.lottie.O000000o.O00000Oo.O0000O0o) this.O00oo.get(i2)).setProgress(f);
        }
    }
}
