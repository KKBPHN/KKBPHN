package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.content.O00000o;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.ArrayList;
import java.util.List;

public class O0000Oo implements O0000O0o, O00000Oo, O0000o00 {
    private static final int O00OOoo = 32;
    private final C0083O000OoO0 O000OoO0;
    private final List O00O0o00 = new ArrayList();
    private final O0000O0o O00O0oOO;
    @Nullable
    private O0000O0o O00O0oo;
    private final LongSparseArray O00O0ooO = new LongSparseArray();
    private final LongSparseArray O00O0ooo = new LongSparseArray();
    private final RectF O00OO0O = new RectF();
    private final int O00OO0o;
    private final O0000O0o O00OOOo;
    private final O0000O0o O00OOo;
    private final O0000O0o O00OOo0;
    @Nullable
    private C0031O0000oo0 O00OOoO;
    private final boolean hidden;
    private final O00000o0 layer;
    @NonNull
    private final String name;
    private final Paint paint = new O000000o(1);
    private final Path path = new Path();
    private final GradientType type;

    public O0000Oo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O00000o o00000o) {
        this.layer = o00000o0;
        this.name = o00000o.getName();
        this.hidden = o00000o.isHidden();
        this.O000OoO0 = o000OoO0;
        this.type = o00000o.getGradientType();
        this.path.setFillType(o00000o.getFillType());
        this.O00OO0o = (int) (o000OoO0.O000O0OO().getDuration() / 32.0f);
        this.O00OOOo = o00000o.O00OoOO0().O00000o();
        this.O00OOOo.O00000Oo(this);
        o00000o0.O000000o(this.O00OOOo);
        this.O00O0oOO = o00000o.getOpacity().O00000o();
        this.O00O0oOO.O00000Oo(this);
        o00000o0.O000000o(this.O00O0oOO);
        this.O00OOo0 = o00000o.getStartPoint().O00000o();
        this.O00OOo0.O00000Oo(this);
        o00000o0.O000000o(this.O00OOo0);
        this.O00OOo = o00000o.getEndPoint().O00000o();
        this.O00OOo.O00000Oo(this);
        o00000o0.O000000o(this.O00OOo);
    }

    private int[] O00000Oo(int[] iArr) {
        C0031O0000oo0 o0000oo0 = this.O00OOoO;
        if (o0000oo0 != null) {
            Integer[] numArr = (Integer[]) o0000oo0.getValue();
            int i = 0;
            if (iArr.length == numArr.length) {
                while (i < iArr.length) {
                    iArr[i] = numArr[i].intValue();
                    i++;
                }
            } else {
                iArr = new int[numArr.length];
                while (i < numArr.length) {
                    iArr[i] = numArr[i].intValue();
                    i++;
                }
            }
        }
        return iArr;
    }

    private int Oo0oOO0() {
        int round = Math.round(this.O00OOo0.getProgress() * ((float) this.O00OO0o));
        int round2 = Math.round(this.O00OOo.getProgress() * ((float) this.O00OO0o));
        int round3 = Math.round(this.O00OOOo.getProgress() * ((float) this.O00OO0o));
        int i = round != 0 ? 527 * round : 17;
        if (round2 != 0) {
            i = i * 31 * round2;
        }
        return round3 != 0 ? i * 31 * round3 : i;
    }

    private LinearGradient Oo0oOOO() {
        long Oo0oOO0 = (long) Oo0oOO0();
        LinearGradient linearGradient = (LinearGradient) this.O00O0ooO.get(Oo0oOO0);
        if (linearGradient != null) {
            return linearGradient;
        }
        PointF pointF = (PointF) this.O00OOo0.getValue();
        PointF pointF2 = (PointF) this.O00OOo.getValue();
        com.airbnb.lottie.model.content.O00000o0 o00000o0 = (com.airbnb.lottie.model.content.O00000o0) this.O00OOOo.getValue();
        LinearGradient linearGradient2 = new LinearGradient(pointF.x, pointF.y, pointF2.x, pointF2.y, O00000Oo(o00000o0.getColors()), o00000o0.getPositions(), TileMode.CLAMP);
        this.O00O0ooO.put(Oo0oOO0, linearGradient2);
        return linearGradient2;
    }

    private RadialGradient Oo0oOOo() {
        long Oo0oOO0 = (long) Oo0oOO0();
        RadialGradient radialGradient = (RadialGradient) this.O00O0ooo.get(Oo0oOO0);
        if (radialGradient != null) {
            return radialGradient;
        }
        PointF pointF = (PointF) this.O00OOo0.getValue();
        PointF pointF2 = (PointF) this.O00OOo.getValue();
        com.airbnb.lottie.model.content.O00000o0 o00000o0 = (com.airbnb.lottie.model.content.O00000o0) this.O00OOOo.getValue();
        int[] O00000Oo2 = O00000Oo(o00000o0.getColors());
        float[] positions = o00000o0.getPositions();
        float f = pointF.x;
        float f2 = pointF.y;
        float hypot = (float) Math.hypot((double) (pointF2.x - f), (double) (pointF2.y - f2));
        if (hypot <= 0.0f) {
            hypot = 0.001f;
        }
        RadialGradient radialGradient2 = new RadialGradient(f, f2, hypot, O00000Oo2, positions, TileMode.CLAMP);
        this.O00O0ooo.put(Oo0oOO0, radialGradient2);
        return radialGradient2;
    }

    public void O000000o(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            String str = "GradientFillContent#draw";
            C0053O00000oO.beginSection(str);
            this.path.reset();
            for (int i2 = 0; i2 < this.O00O0o00.size(); i2++) {
                this.path.addPath(((O0000o) this.O00O0o00.get(i2)).getPath(), matrix);
            }
            this.path.computeBounds(this.O00OO0O, false);
            Shader Oo0oOOO = this.type == GradientType.LINEAR ? Oo0oOOO() : Oo0oOOo();
            Oo0oOOO.setLocalMatrix(matrix);
            this.paint.setShader(Oo0oOOO);
            O0000O0o o0000O0o = this.O00O0oo;
            if (o0000O0o != null) {
                this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
            }
            this.paint.setAlpha(com.airbnb.lottie.O00000o.O0000O0o.clamp((int) ((((((float) i) / 255.0f) * ((float) ((Integer) this.O00O0oOO.getValue()).intValue())) / 100.0f) * 255.0f), 0, 255));
            canvas.drawPath(this.path, this.paint);
            C0053O00000oO.O0000oOo(str);
        }
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        this.path.reset();
        for (int i = 0; i < this.O00O0o00.size(); i++) {
            this.path.addPath(((O0000o) this.O00O0o00.get(i)).getPath(), matrix);
        }
        this.path.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    public void O000000o(Object obj, @Nullable com.airbnb.lottie.O00000oO.O0000Oo o0000Oo) {
        O00000o0 o00000o0;
        O0000O0o o0000O0o;
        if (obj == C0087O000Ooo0.OO000oO) {
            this.O00O0oOO.O000000o(o0000Oo);
            return;
        }
        if (obj == C0087O000Ooo0.OO00ooO) {
            if (o0000Oo == null) {
                this.O00O0oo = null;
                return;
            }
            this.O00O0oo = new C0031O0000oo0(o0000Oo);
            this.O00O0oo.O00000Oo(this);
            o00000o0 = this.layer;
            o0000O0o = this.O00O0oo;
        } else if (obj != C0087O000Ooo0.OO00ooo) {
            return;
        } else {
            if (o0000Oo == null) {
                C0031O0000oo0 o0000oo0 = this.O00OOoO;
                if (o0000oo0 != null) {
                    this.layer.O00000Oo((O0000O0o) o0000oo0);
                }
                this.O00OOoO = null;
                return;
            }
            this.O00OOoO = new C0031O0000oo0(o0000Oo);
            this.O00OOoO.O00000Oo(this);
            o00000o0 = this.layer;
            o0000O0o = this.O00OOoO;
        }
        o00000o0.O000000o(o0000O0o);
    }

    public void O000000o(List list, List list2) {
        for (int i = 0; i < list2.size(); i++) {
            C0006O00000oO o00000oO = (C0006O00000oO) list2.get(i);
            if (o00000oO instanceof O0000o) {
                this.O00O0o00.add((O0000o) o00000oO);
            }
        }
    }

    public void O00000oO() {
        this.O000OoO0.invalidateSelf();
    }

    public String getName() {
        return this.name;
    }
}
