package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.content.C0104O00000oO;
import com.airbnb.lottie.model.content.GradientType;
import com.airbnb.lottie.model.layer.O00000o0;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000OoO reason: case insensitive filesystem */
public class C0008O0000OoO extends O00000o0 {
    private static final int O00OOoo = 32;
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
    private final String name;
    private final GradientType type;

    public C0008O0000OoO(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, C0104O00000oO o00000oO) {
        super(o000OoO0, o00000o0, o00000oO.O00OoOo().Oo0o0oO(), o00000oO.O00OoOoo().Oo0o0oo(), o00000oO.O00Ooo0(), o00000oO.getOpacity(), o00000oO.getWidth(), o00000oO.O00Ooo00(), o00000oO.O00OoOoO());
        this.name = o00000oO.getName();
        this.type = o00000oO.getGradientType();
        this.hidden = o00000oO.isHidden();
        this.O00OO0o = (int) (o000OoO0.O000O0OO().getDuration() / 32.0f);
        this.O00OOOo = o00000oO.O00OoOO0().O00000o();
        this.O00OOOo.O00000Oo(this);
        o00000o0.O000000o(this.O00OOOo);
        this.O00OOo0 = o00000oO.getStartPoint().O00000o();
        this.O00OOo0.O00000Oo(this);
        o00000o0.O000000o(this.O00OOo0);
        this.O00OOo = o00000oO.getEndPoint().O00000o();
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
        RadialGradient radialGradient2 = new RadialGradient(f, f2, (float) Math.hypot((double) (pointF2.x - f), (double) (pointF2.y - f2)), O00000Oo2, positions, TileMode.CLAMP);
        this.O00O0ooo.put(Oo0oOO0, radialGradient2);
        return radialGradient2;
    }

    public void O000000o(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            O000000o(this.O00OO0O, matrix, false);
            Shader Oo0oOOO = this.type == GradientType.LINEAR ? Oo0oOOO() : Oo0oOOo();
            Oo0oOOO.setLocalMatrix(matrix);
            this.paint.setShader(Oo0oOOO);
            super.O000000o(canvas, matrix, i);
        }
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        super.O000000o(obj, o0000Oo);
        if (obj != C0087O000Ooo0.OO00ooo) {
            return;
        }
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
        this.layer.O000000o(this.O00OOoO);
    }

    public String getName() {
        return this.name;
    }
}
