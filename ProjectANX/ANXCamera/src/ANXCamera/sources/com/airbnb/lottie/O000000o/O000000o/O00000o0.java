package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0022O0000OoO;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O000000o.O00000Oo.O0000Oo0;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.O000000o.O00000o;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import java.util.ArrayList;
import java.util.List;

public abstract class O00000o0 implements O00000Oo, O0000o00, O0000O0o {
    private final C0083O000OoO0 O000OoO0;
    private final float[] O00O0o;
    private final Path O00O0o0O = new Path();
    private final List O00O0o0o = new ArrayList();
    private final O0000O0o O00O0oO0;
    private final O0000O0o O00O0oOO;
    private final List O00O0oOo;
    @Nullable
    private O0000O0o O00O0oo;
    @Nullable
    private final O0000O0o O00O0oo0;
    protected final com.airbnb.lottie.model.layer.O00000o0 layer;
    final Paint paint = new O000000o(1);
    private final Path path = new Path();
    private final PathMeasure pm = new PathMeasure();
    private final RectF rect = new RectF();

    O00000o0(C0083O000OoO0 o000OoO0, com.airbnb.lottie.model.layer.O00000o0 o00000o0, Cap cap, Join join, float f, O00000o o00000o, com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo, List list, com.airbnb.lottie.model.O000000o.O00000Oo o00000Oo2) {
        this.O000OoO0 = o000OoO0;
        this.layer = o00000o0;
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeCap(cap);
        this.paint.setStrokeJoin(join);
        this.paint.setStrokeMiter(f);
        this.O00O0oOO = o00000o.O00000o();
        this.O00O0oO0 = o00000Oo.O00000o();
        this.O00O0oo0 = o00000Oo2 == null ? null : o00000Oo2.O00000o();
        this.O00O0oOo = new ArrayList(list.size());
        this.O00O0o = new float[list.size()];
        for (int i = 0; i < list.size(); i++) {
            this.O00O0oOo.add(((com.airbnb.lottie.model.O000000o.O00000Oo) list.get(i)).O00000o());
        }
        o00000o0.O000000o(this.O00O0oOO);
        o00000o0.O000000o(this.O00O0oO0);
        for (int i2 = 0; i2 < this.O00O0oOo.size(); i2++) {
            o00000o0.O000000o((O0000O0o) this.O00O0oOo.get(i2));
        }
        O0000O0o o0000O0o = this.O00O0oo0;
        if (o0000O0o != null) {
            o00000o0.O000000o(o0000O0o);
        }
        this.O00O0oOO.O00000Oo(this);
        this.O00O0oO0.O00000Oo(this);
        for (int i3 = 0; i3 < list.size(); i3++) {
            ((O0000O0o) this.O00O0oOo.get(i3)).O00000Oo(this);
        }
        O0000O0o o0000O0o2 = this.O00O0oo0;
        if (o0000O0o2 != null) {
            o0000O0o2.O00000Oo(this);
        }
    }

    private void O000000o(Canvas canvas, O00000Oo o00000Oo, Matrix matrix) {
        String str = "StrokeContent#applyTrimPath";
        C0053O00000oO.beginSection(str);
        if (o00000Oo.O00O0o0 == null) {
            C0053O00000oO.O0000oOo(str);
            return;
        }
        this.path.reset();
        for (int size = o00000Oo.O00O0o00.size() - 1; size >= 0; size--) {
            this.path.addPath(((O0000o) o00000Oo.O00O0o00.get(size)).getPath(), matrix);
        }
        this.pm.setPath(this.path, false);
        float length = this.pm.getLength();
        while (this.pm.nextContour()) {
            length += this.pm.getLength();
        }
        float floatValue = (((Float) o00000Oo.O00O0o0.getOffset().getValue()).floatValue() * length) / 360.0f;
        float floatValue2 = ((((Float) o00000Oo.O00O0o0.getStart().getValue()).floatValue() * length) / 100.0f) + floatValue;
        float floatValue3 = ((((Float) o00000Oo.O00O0o0.getEnd().getValue()).floatValue() * length) / 100.0f) + floatValue;
        float f = 0.0f;
        for (int size2 = o00000Oo.O00O0o00.size() - 1; size2 >= 0; size2--) {
            this.O00O0o0O.set(((O0000o) o00000Oo.O00O0o00.get(size2)).getPath());
            this.O00O0o0O.transform(matrix);
            this.pm.setPath(this.O00O0o0O, false);
            float length2 = this.pm.getLength();
            float f2 = 1.0f;
            if (floatValue3 > length) {
                float f3 = floatValue3 - length;
                if (f3 < f + length2 && f < f3) {
                    O0000OOo.O000000o(this.O00O0o0O, floatValue2 > length ? (floatValue2 - length) / length2 : 0.0f, Math.min(f3 / length2, 1.0f), 0.0f);
                    canvas.drawPath(this.O00O0o0O, this.paint);
                    f += length2;
                }
            }
            float f4 = f + length2;
            if (f4 >= floatValue2 && f <= floatValue3) {
                if (f4 > floatValue3 || floatValue2 >= f) {
                    float f5 = floatValue2 < f ? 0.0f : (floatValue2 - f) / length2;
                    if (floatValue3 <= f4) {
                        f2 = (floatValue3 - f) / length2;
                    }
                    O0000OOo.O000000o(this.O00O0o0O, f5, f2, 0.0f);
                }
                canvas.drawPath(this.O00O0o0O, this.paint);
            }
            f += length2;
        }
        C0053O00000oO.O0000oOo(str);
    }

    private void O00000o0(Matrix matrix) {
        String str = "StrokeContent#applyDashPattern";
        C0053O00000oO.beginSection(str);
        if (this.O00O0oOo.isEmpty()) {
            C0053O00000oO.O0000oOo(str);
            return;
        }
        float O000000o2 = O0000OOo.O000000o(matrix);
        for (int i = 0; i < this.O00O0oOo.size(); i++) {
            this.O00O0o[i] = ((Float) ((O0000O0o) this.O00O0oOo.get(i)).getValue()).floatValue();
            if (i % 2 == 0) {
                float[] fArr = this.O00O0o;
                if (fArr[i] < 1.0f) {
                    fArr[i] = 1.0f;
                }
            } else {
                float[] fArr2 = this.O00O0o;
                if (fArr2[i] < 0.1f) {
                    fArr2[i] = 0.1f;
                }
            }
            float[] fArr3 = this.O00O0o;
            fArr3[i] = fArr3[i] * O000000o2;
        }
        O0000O0o o0000O0o = this.O00O0oo0;
        this.paint.setPathEffect(new DashPathEffect(this.O00O0o, o0000O0o == null ? 0.0f : O000000o2 * ((Float) o0000O0o.getValue()).floatValue()));
        C0053O00000oO.O0000oOo(str);
    }

    public void O000000o(Canvas canvas, Matrix matrix, int i) {
        String str = "StrokeContent#draw";
        C0053O00000oO.beginSection(str);
        if (O0000OOo.O00000Oo(matrix)) {
            C0053O00000oO.O0000oOo(str);
            return;
        }
        this.paint.setAlpha(com.airbnb.lottie.O00000o.O0000O0o.clamp((int) ((((((float) i) / 255.0f) * ((float) ((C0022O0000OoO) this.O00O0oOO).getIntValue())) / 100.0f) * 255.0f), 0, 255));
        this.paint.setStrokeWidth(((O0000Oo0) this.O00O0oO0).getFloatValue() * O0000OOo.O000000o(matrix));
        if (this.paint.getStrokeWidth() <= 0.0f) {
            C0053O00000oO.O0000oOo(str);
            return;
        }
        O00000o0(matrix);
        O0000O0o o0000O0o = this.O00O0oo;
        if (o0000O0o != null) {
            this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
        }
        for (int i2 = 0; i2 < this.O00O0o0o.size(); i2++) {
            O00000Oo o00000Oo = (O00000Oo) this.O00O0o0o.get(i2);
            if (o00000Oo.O00O0o0 != null) {
                O000000o(canvas, o00000Oo, matrix);
            } else {
                String str2 = "StrokeContent#buildPath";
                C0053O00000oO.beginSection(str2);
                this.path.reset();
                for (int size = o00000Oo.O00O0o00.size() - 1; size >= 0; size--) {
                    this.path.addPath(((O0000o) o00000Oo.O00O0o00.get(size)).getPath(), matrix);
                }
                C0053O00000oO.O0000oOo(str2);
                String str3 = "StrokeContent#drawPath";
                C0053O00000oO.beginSection(str3);
                canvas.drawPath(this.path, this.paint);
                C0053O00000oO.O0000oOo(str3);
            }
        }
        C0053O00000oO.O0000oOo(str);
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        String str = "StrokeContent#getBounds";
        C0053O00000oO.beginSection(str);
        this.path.reset();
        for (int i = 0; i < this.O00O0o0o.size(); i++) {
            O00000Oo o00000Oo = (O00000Oo) this.O00O0o0o.get(i);
            for (int i2 = 0; i2 < o00000Oo.O00O0o00.size(); i2++) {
                this.path.addPath(((O0000o) o00000Oo.O00O0o00.get(i2)).getPath(), matrix);
            }
        }
        this.path.computeBounds(this.rect, false);
        float floatValue = ((O0000Oo0) this.O00O0oO0).getFloatValue();
        RectF rectF2 = this.rect;
        float f = floatValue / 2.0f;
        rectF2.set(rectF2.left - f, rectF2.top - f, rectF2.right + f, rectF2.bottom + f);
        rectF.set(this.rect);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
        C0053O00000oO.O0000oOo(str);
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    @CallSuper
    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        if (obj == C0087O000Ooo0.OO000oO) {
            o0000O0o = this.O00O0oOO;
        } else if (obj == C0087O000Ooo0.STROKE_WIDTH) {
            o0000O0o = this.O00O0oO0;
        } else if (obj != C0087O000Ooo0.OO00ooO) {
            return;
        } else {
            if (o0000Oo == null) {
                this.O00O0oo = null;
                return;
            }
            this.O00O0oo = new C0031O0000oo0(o0000Oo);
            this.O00O0oo.O00000Oo(this);
            this.layer.O000000o(this.O00O0oo);
            return;
        }
        o0000O0o.O000000o(o0000Oo);
    }

    public void O000000o(List list, List list2) {
        C0018O0000ooO o0000ooO = null;
        for (int size = list.size() - 1; size >= 0; size--) {
            C0006O00000oO o00000oO = (C0006O00000oO) list.get(size);
            if (o00000oO instanceof C0018O0000ooO) {
                C0018O0000ooO o0000ooO2 = (C0018O0000ooO) o00000oO;
                if (o0000ooO2.getType() == ShapeTrimPath$Type.INDIVIDUALLY) {
                    o0000ooO = o0000ooO2;
                }
            }
        }
        if (o0000ooO != null) {
            o0000ooO.O000000o(this);
        }
        O00000Oo o00000Oo = null;
        for (int size2 = list2.size() - 1; size2 >= 0; size2--) {
            C0006O00000oO o00000oO2 = (C0006O00000oO) list2.get(size2);
            if (o00000oO2 instanceof C0018O0000ooO) {
                C0018O0000ooO o0000ooO3 = (C0018O0000ooO) o00000oO2;
                if (o0000ooO3.getType() == ShapeTrimPath$Type.INDIVIDUALLY) {
                    if (o00000Oo != null) {
                        this.O00O0o0o.add(o00000Oo);
                    }
                    o00000Oo = new O00000Oo(o0000ooO3);
                    o0000ooO3.O000000o(this);
                }
            }
            if (o00000oO2 instanceof O0000o) {
                if (o00000Oo == null) {
                    o00000Oo = new O00000Oo(o0000ooO);
                }
                o00000Oo.O00O0o00.add((O0000o) o00000oO2);
            }
        }
        if (o00000Oo != null) {
            this.O00O0o0o.add(o00000Oo);
        }
    }

    public void O00000oO() {
        this.O000OoO0.invalidateSelf();
    }
}
