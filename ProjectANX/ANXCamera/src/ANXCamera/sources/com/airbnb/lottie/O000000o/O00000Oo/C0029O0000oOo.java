package com.airbnb.lottie.O000000o.O00000Oo;

import android.graphics.Matrix;
import android.graphics.PointF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.C0059O0000OoO;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.O000000o.C0100O0000Ooo;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.Collections;

/* renamed from: com.airbnb.lottie.O000000o.O00000Oo.O0000oOo reason: case insensitive filesystem */
public class C0029O0000oOo {
    private final Matrix O00o0OO;
    private final Matrix O00o0OOO;
    private final Matrix O00o0OOo;
    @NonNull
    private O0000O0o O00o0Oo;
    private final float[] O00o0Oo0;
    @Nullable
    private O0000Oo0 O00o0OoO;
    @Nullable
    private O0000Oo0 O00o0Ooo;
    @Nullable
    private O0000O0o O00o0o0;
    @Nullable
    private O0000O0o O00o0o00;
    private final Matrix matrix = new Matrix();
    @NonNull
    private O0000O0o opacity;
    @NonNull
    private O0000O0o position;
    @NonNull
    private O0000O0o rotation;
    @NonNull
    private O0000O0o scale;

    public C0029O0000oOo(C0100O0000Ooo o0000Ooo) {
        this.O00o0Oo = o0000Ooo.O00OoO0O() == null ? null : o0000Ooo.O00OoO0O().O00000o();
        this.position = o0000Ooo.getPosition() == null ? null : o0000Ooo.getPosition().O00000o();
        this.scale = o0000Ooo.getScale() == null ? null : o0000Ooo.getScale().O00000o();
        this.rotation = o0000Ooo.getRotation() == null ? null : o0000Ooo.getRotation().O00000o();
        this.O00o0OoO = o0000Ooo.O00Ooo() == null ? null : (O0000Oo0) o0000Ooo.O00Ooo().O00000o();
        if (this.O00o0OoO != null) {
            this.O00o0OO = new Matrix();
            this.O00o0OOO = new Matrix();
            this.O00o0OOo = new Matrix();
            this.O00o0Oo0 = new float[9];
        } else {
            this.O00o0OO = null;
            this.O00o0OOO = null;
            this.O00o0OOo = null;
            this.O00o0Oo0 = null;
        }
        this.O00o0Ooo = o0000Ooo.O00OoO() == null ? null : (O0000Oo0) o0000Ooo.O00OoO().O00000o();
        if (o0000Ooo.getOpacity() != null) {
            this.opacity = o0000Ooo.getOpacity().O00000o();
        }
        if (o0000Ooo.O00Oo0o0() != null) {
            this.O00o0o00 = o0000Ooo.O00Oo0o0().O00000o();
        } else {
            this.O00o0o00 = null;
        }
        if (o0000Ooo.O00Oo0Oo() != null) {
            this.O00o0o0 = o0000Ooo.O00Oo0Oo().O00000o();
        } else {
            this.O00o0o0 = null;
        }
    }

    private void Oo0oo00() {
        for (int i = 0; i < 9; i++) {
            this.O00o0Oo0[i] = 0.0f;
        }
    }

    public void O000000o(O00000Oo o00000Oo) {
        O0000O0o o0000O0o = this.opacity;
        if (o0000O0o != null) {
            o0000O0o.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o2 = this.O00o0o00;
        if (o0000O0o2 != null) {
            o0000O0o2.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o3 = this.O00o0o0;
        if (o0000O0o3 != null) {
            o0000O0o3.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o4 = this.O00o0Oo;
        if (o0000O0o4 != null) {
            o0000O0o4.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o5 = this.position;
        if (o0000O0o5 != null) {
            o0000O0o5.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o6 = this.scale;
        if (o0000O0o6 != null) {
            o0000O0o6.O00000Oo(o00000Oo);
        }
        O0000O0o o0000O0o7 = this.rotation;
        if (o0000O0o7 != null) {
            o0000O0o7.O00000Oo(o00000Oo);
        }
        O0000Oo0 o0000Oo0 = this.O00o0OoO;
        if (o0000Oo0 != null) {
            o0000Oo0.O00000Oo(o00000Oo);
        }
        O0000Oo0 o0000Oo02 = this.O00o0Ooo;
        if (o0000Oo02 != null) {
            o0000Oo02.O00000Oo(o00000Oo);
        }
    }

    public void O000000o(O00000o0 o00000o0) {
        o00000o0.O000000o(this.opacity);
        o00000o0.O000000o(this.O00o0o00);
        o00000o0.O000000o(this.O00o0o0);
        o00000o0.O000000o(this.O00o0Oo);
        o00000o0.O000000o(this.position);
        o00000o0.O000000o(this.scale);
        o00000o0.O000000o(this.rotation);
        o00000o0.O000000o(this.O00o0OoO);
        o00000o0.O000000o(this.O00o0Ooo);
    }

    public boolean O00000Oo(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000Oo0 o0000Oo0;
        O0000O0o o0000O0o;
        O0000O0o o0000O0o2;
        if (obj == C0087O000Ooo0.OO000oo) {
            o0000O0o2 = this.O00o0Oo;
            if (o0000O0o2 == null) {
                this.O00o0Oo = new C0031O0000oo0(o0000Oo, new PointF());
                return true;
            }
        } else if (obj == C0087O000Ooo0.OO00OO) {
            o0000O0o2 = this.position;
            if (o0000O0o2 == null) {
                this.position = new C0031O0000oo0(o0000Oo, new PointF());
                return true;
            }
        } else if (obj == C0087O000Ooo0.OO00OOO) {
            o0000O0o2 = this.scale;
            if (o0000O0o2 == null) {
                this.scale = new C0031O0000oo0(o0000Oo, new C0059O0000OoO());
                return true;
            }
        } else if (obj == C0087O000Ooo0.OO00OOo) {
            o0000O0o2 = this.rotation;
            if (o0000O0o2 == null) {
                this.rotation = new C0031O0000oo0(o0000Oo, Float.valueOf(0.0f));
                return true;
            }
        } else {
            if (obj == C0087O000Ooo0.OO000o) {
                o0000O0o2 = this.opacity;
                if (o0000O0o2 == null) {
                    this.opacity = new C0031O0000oo0(o0000Oo, Integer.valueOf(100));
                }
            } else {
                if (obj == C0087O000Ooo0.OO00oOo) {
                    o0000O0o = this.O00o0o00;
                    if (o0000O0o != null) {
                        if (o0000O0o == null) {
                            this.O00o0o00 = new C0031O0000oo0(o0000Oo, Integer.valueOf(100));
                        }
                        o0000O0o.O000000o(o0000Oo);
                    }
                }
                if (obj == C0087O000Ooo0.OO00oo0) {
                    o0000O0o = this.O00o0o0;
                    if (o0000O0o != null) {
                        if (o0000O0o == null) {
                            this.O00o0o0 = new C0031O0000oo0(o0000Oo, Integer.valueOf(100));
                        }
                        o0000O0o.O000000o(o0000Oo);
                    }
                }
                if (obj == C0087O000Ooo0.OO00Oo0) {
                    O0000Oo0 o0000Oo02 = this.O00o0OoO;
                    if (o0000Oo02 != null) {
                        if (o0000Oo02 == null) {
                            this.O00o0OoO = new O0000Oo0(Collections.singletonList(new C0054O000000o(Float.valueOf(0.0f))));
                        }
                        o0000Oo0 = this.O00o0OoO;
                        o0000Oo0.O000000o(o0000Oo);
                    }
                }
                if (obj == C0087O000Ooo0.OO0ooO) {
                    O0000Oo0 o0000Oo03 = this.O00o0Ooo;
                    if (o0000Oo03 != null) {
                        if (o0000Oo03 == null) {
                            this.O00o0Ooo = new O0000Oo0(Collections.singletonList(new C0054O000000o(Float.valueOf(0.0f))));
                        }
                        o0000Oo0 = this.O00o0Ooo;
                        o0000Oo0.O000000o(o0000Oo);
                    }
                }
                return false;
            }
            return true;
        }
        o0000O0o2.O000000o(o0000Oo);
        return true;
    }

    public Matrix O0000Ooo(float f) {
        O0000O0o o0000O0o = this.position;
        PointF pointF = null;
        PointF pointF2 = o0000O0o == null ? null : (PointF) o0000O0o.getValue();
        O0000O0o o0000O0o2 = this.scale;
        C0059O0000OoO o0000OoO = o0000O0o2 == null ? null : (C0059O0000OoO) o0000O0o2.getValue();
        this.matrix.reset();
        if (pointF2 != null) {
            this.matrix.preTranslate(pointF2.x * f, pointF2.y * f);
        }
        if (o0000OoO != null) {
            double d = (double) f;
            this.matrix.preScale((float) Math.pow((double) o0000OoO.getScaleX(), d), (float) Math.pow((double) o0000OoO.getScaleY(), d));
        }
        O0000O0o o0000O0o3 = this.rotation;
        if (o0000O0o3 != null) {
            float floatValue = ((Float) o0000O0o3.getValue()).floatValue();
            O0000O0o o0000O0o4 = this.O00o0Oo;
            if (o0000O0o4 != null) {
                pointF = (PointF) o0000O0o4.getValue();
            }
            Matrix matrix2 = this.matrix;
            float f2 = floatValue * f;
            float f3 = 0.0f;
            float f4 = pointF == null ? 0.0f : pointF.x;
            if (pointF != null) {
                f3 = pointF.y;
            }
            matrix2.preRotate(f2, f4, f3);
        }
        return this.matrix;
    }

    @Nullable
    public O0000O0o O00Oo0Oo() {
        return this.O00o0o0;
    }

    @Nullable
    public O0000O0o O00Oo0o0() {
        return this.O00o0o00;
    }

    public Matrix getMatrix() {
        this.matrix.reset();
        O0000O0o o0000O0o = this.position;
        if (o0000O0o != null) {
            PointF pointF = (PointF) o0000O0o.getValue();
            if (!(pointF.x == 0.0f && pointF.y == 0.0f)) {
                this.matrix.preTranslate(pointF.x, pointF.y);
            }
        }
        O0000O0o o0000O0o2 = this.rotation;
        if (o0000O0o2 != null) {
            float floatValue = o0000O0o2 instanceof C0031O0000oo0 ? ((Float) o0000O0o2.getValue()).floatValue() : ((O0000Oo0) o0000O0o2).getFloatValue();
            if (floatValue != 0.0f) {
                this.matrix.preRotate(floatValue);
            }
        }
        if (this.O00o0OoO != null) {
            O0000Oo0 o0000Oo0 = this.O00o0Ooo;
            float cos = o0000Oo0 == null ? 0.0f : (float) Math.cos(Math.toRadians((double) ((-o0000Oo0.getFloatValue()) + 90.0f)));
            O0000Oo0 o0000Oo02 = this.O00o0Ooo;
            float sin = o0000Oo02 == null ? 1.0f : (float) Math.sin(Math.toRadians((double) ((-o0000Oo02.getFloatValue()) + 90.0f)));
            float tan = (float) Math.tan(Math.toRadians((double) this.O00o0OoO.getFloatValue()));
            Oo0oo00();
            float[] fArr = this.O00o0Oo0;
            fArr[0] = cos;
            fArr[1] = sin;
            float f = -sin;
            fArr[3] = f;
            fArr[4] = cos;
            fArr[8] = 1.0f;
            this.O00o0OO.setValues(fArr);
            Oo0oo00();
            float[] fArr2 = this.O00o0Oo0;
            fArr2[0] = 1.0f;
            fArr2[3] = tan;
            fArr2[4] = 1.0f;
            fArr2[8] = 1.0f;
            this.O00o0OOO.setValues(fArr2);
            Oo0oo00();
            float[] fArr3 = this.O00o0Oo0;
            fArr3[0] = cos;
            fArr3[1] = f;
            fArr3[3] = sin;
            fArr3[4] = cos;
            fArr3[8] = 1.0f;
            this.O00o0OOo.setValues(fArr3);
            this.O00o0OOO.preConcat(this.O00o0OO);
            this.O00o0OOo.preConcat(this.O00o0OOO);
            this.matrix.preConcat(this.O00o0OOo);
        }
        O0000O0o o0000O0o3 = this.scale;
        if (o0000O0o3 != null) {
            C0059O0000OoO o0000OoO = (C0059O0000OoO) o0000O0o3.getValue();
            if (!(o0000OoO.getScaleX() == 1.0f && o0000OoO.getScaleY() == 1.0f)) {
                this.matrix.preScale(o0000OoO.getScaleX(), o0000OoO.getScaleY());
            }
        }
        O0000O0o o0000O0o4 = this.O00o0Oo;
        if (o0000O0o4 != null) {
            PointF pointF2 = (PointF) o0000O0o4.getValue();
            if (!(pointF2.x == 0.0f && pointF2.y == 0.0f)) {
                this.matrix.preTranslate(-pointF2.x, -pointF2.y);
            }
        }
        return this.matrix;
    }

    @Nullable
    public O0000O0o getOpacity() {
        return this.opacity;
    }

    public void setProgress(float f) {
        O0000O0o o0000O0o = this.opacity;
        if (o0000O0o != null) {
            o0000O0o.setProgress(f);
        }
        O0000O0o o0000O0o2 = this.O00o0o00;
        if (o0000O0o2 != null) {
            o0000O0o2.setProgress(f);
        }
        O0000O0o o0000O0o3 = this.O00o0o0;
        if (o0000O0o3 != null) {
            o0000O0o3.setProgress(f);
        }
        O0000O0o o0000O0o4 = this.O00o0Oo;
        if (o0000O0o4 != null) {
            o0000O0o4.setProgress(f);
        }
        O0000O0o o0000O0o5 = this.position;
        if (o0000O0o5 != null) {
            o0000O0o5.setProgress(f);
        }
        O0000O0o o0000O0o6 = this.scale;
        if (o0000O0o6 != null) {
            o0000O0o6.setProgress(f);
        }
        O0000O0o o0000O0o7 = this.rotation;
        if (o0000O0o7 != null) {
            o0000O0o7.setProgress(f);
        }
        O0000Oo0 o0000Oo0 = this.O00o0OoO;
        if (o0000Oo0 != null) {
            o0000Oo0.setProgress(f);
        }
        O0000Oo0 o0000Oo02 = this.O00o0Ooo;
        if (o0000Oo02 != null) {
            o0000Oo02.setProgress(f);
        }
    }
}
