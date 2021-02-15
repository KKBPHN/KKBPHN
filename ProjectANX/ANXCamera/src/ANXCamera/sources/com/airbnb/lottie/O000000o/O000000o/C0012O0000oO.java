package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Path;
import android.graphics.PointF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.O0000OOo;
import com.airbnb.lottie.model.content.PolystarShape$Type;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000oO reason: case insensitive filesystem */
public class C0012O0000oO implements O0000o, O00000Oo, O0000o00 {
    private static final float O00OoOoO = 0.47829f;
    private static final float O00OoOoo = 0.25f;
    private final C0083O000OoO0 O000OoO0;
    private final O0000O0o O00Oo0o0;
    private O00000o O00Oo0oO = new O00000o();
    private final O0000O0o O00OoO;
    private final O0000O0o O00OoOO;
    @Nullable
    private final O0000O0o O00OoOO0;
    private final O0000O0o O00OoOo;
    @Nullable
    private final O0000O0o O00OoOo0;
    private boolean O00OooOO;
    private final boolean hidden;
    private final String name;
    private final Path path = new Path();
    private final O0000O0o rotationAnimation;
    private final PolystarShape$Type type;

    public C0012O0000oO(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O0000OOo o0000OOo) {
        O0000O0o o0000O0o;
        this.O000OoO0 = o000OoO0;
        this.name = o0000OOo.getName();
        this.type = o0000OOo.getType();
        this.hidden = o0000OOo.isHidden();
        this.O00OoO = o0000OOo.getPoints().O00000o();
        this.O00Oo0o0 = o0000OOo.getPosition().O00000o();
        this.rotationAnimation = o0000OOo.getRotation().O00000o();
        this.O00OoOO = o0000OOo.O00OooOo().O00000o();
        this.O00OoOo = o0000OOo.O00Oooo0().O00000o();
        if (this.type == PolystarShape$Type.STAR) {
            this.O00OoOO0 = o0000OOo.getInnerRadius().O00000o();
            o0000O0o = o0000OOo.O00OooO().O00000o();
        } else {
            o0000O0o = null;
            this.O00OoOO0 = null;
        }
        this.O00OoOo0 = o0000O0o;
        o00000o0.O000000o(this.O00OoO);
        o00000o0.O000000o(this.O00Oo0o0);
        o00000o0.O000000o(this.rotationAnimation);
        o00000o0.O000000o(this.O00OoOO);
        o00000o0.O000000o(this.O00OoOo);
        if (this.type == PolystarShape$Type.STAR) {
            o00000o0.O000000o(this.O00OoOO0);
            o00000o0.O000000o(this.O00OoOo0);
        }
        this.O00OoO.O00000Oo(this);
        this.O00Oo0o0.O00000Oo(this);
        this.rotationAnimation.O00000Oo(this);
        this.O00OoOO.O00000Oo(this);
        this.O00OoOo.O00000Oo(this);
        if (this.type == PolystarShape$Type.STAR) {
            this.O00OoOO0.O00000Oo(this);
            this.O00OoOo0.O00000Oo(this);
        }
    }

    private void Oo0oOoO() {
        double d;
        double d2;
        double d3;
        int i;
        int floor = (int) Math.floor((double) ((Float) this.O00OoO.getValue()).floatValue());
        O0000O0o o0000O0o = this.rotationAnimation;
        double radians = Math.toRadians((o0000O0o == null ? 0.0d : (double) ((Float) o0000O0o.getValue()).floatValue()) - 90.0d);
        double d4 = (double) floor;
        float f = (float) (6.283185307179586d / d4);
        float floatValue = ((Float) this.O00OoOo.getValue()).floatValue() / 100.0f;
        float floatValue2 = ((Float) this.O00OoOO.getValue()).floatValue();
        double d5 = (double) floatValue2;
        float cos = (float) (Math.cos(radians) * d5);
        float sin = (float) (Math.sin(radians) * d5);
        this.path.moveTo(cos, sin);
        double d6 = (double) f;
        double d7 = radians + d6;
        double ceil = Math.ceil(d4);
        int i2 = 0;
        while (((double) i2) < ceil) {
            float cos2 = (float) (Math.cos(d7) * d5);
            double d8 = ceil;
            float sin2 = (float) (d5 * Math.sin(d7));
            if (floatValue != 0.0f) {
                d3 = d5;
                i = i2;
                d2 = d7;
                double atan2 = (double) ((float) (Math.atan2((double) sin, (double) cos) - 1.5707963267948966d));
                d = d6;
                double atan22 = (double) ((float) (Math.atan2((double) sin2, (double) cos2) - 1.5707963267948966d));
                float f2 = floatValue2 * floatValue * O00OoOoo;
                this.path.cubicTo(cos - (((float) Math.cos(atan2)) * f2), sin - (((float) Math.sin(atan2)) * f2), cos2 + (((float) Math.cos(atan22)) * f2), sin2 + (f2 * ((float) Math.sin(atan22))), cos2, sin2);
            } else {
                d2 = d7;
                d3 = d5;
                d = d6;
                i = i2;
                this.path.lineTo(cos2, sin2);
            }
            d7 = d2 + d;
            i2 = i + 1;
            sin = sin2;
            cos = cos2;
            ceil = d8;
            d5 = d3;
            d6 = d;
        }
        PointF pointF = (PointF) this.O00Oo0o0.getValue();
        this.path.offset(pointF.x, pointF.y);
        this.path.close();
    }

    private void Oo0oOoo() {
        double d;
        int i;
        float f;
        float f2;
        float f3;
        double d2;
        float f4;
        float f5;
        float f6;
        float f7;
        float f8;
        float f9;
        float f10;
        float f11;
        float f12;
        float floatValue = ((Float) this.O00OoO.getValue()).floatValue();
        O0000O0o o0000O0o = this.rotationAnimation;
        double radians = Math.toRadians((o0000O0o == null ? 0.0d : (double) ((Float) o0000O0o.getValue()).floatValue()) - 90.0d);
        double d3 = (double) floatValue;
        float f13 = (float) (6.283185307179586d / d3);
        float f14 = f13 / 2.0f;
        float f15 = floatValue - ((float) ((int) floatValue));
        int i2 = (f15 > 0.0f ? 1 : (f15 == 0.0f ? 0 : -1));
        if (i2 != 0) {
            radians += (double) ((1.0f - f15) * f14);
        }
        float floatValue2 = ((Float) this.O00OoOO.getValue()).floatValue();
        float floatValue3 = ((Float) this.O00OoOO0.getValue()).floatValue();
        O0000O0o o0000O0o2 = this.O00OoOo0;
        float floatValue4 = o0000O0o2 != null ? ((Float) o0000O0o2.getValue()).floatValue() / 100.0f : 0.0f;
        O0000O0o o0000O0o3 = this.O00OoOo;
        float floatValue5 = o0000O0o3 != null ? ((Float) o0000O0o3.getValue()).floatValue() / 100.0f : 0.0f;
        if (i2 != 0) {
            f = ((floatValue2 - floatValue3) * f15) + floatValue3;
            i = i2;
            double d4 = (double) f;
            d = d3;
            f3 = (float) (d4 * Math.cos(radians));
            f2 = (float) (d4 * Math.sin(radians));
            this.path.moveTo(f3, f2);
            d2 = radians + ((double) ((f13 * f15) / 2.0f));
        } else {
            d = d3;
            i = i2;
            double d5 = (double) floatValue2;
            float cos = (float) (Math.cos(radians) * d5);
            float sin = (float) (d5 * Math.sin(radians));
            this.path.moveTo(cos, sin);
            d2 = radians + ((double) f14);
            f3 = cos;
            f2 = sin;
            f = 0.0f;
        }
        double ceil = Math.ceil(d) * 2.0d;
        boolean z = false;
        double d6 = d2;
        float f16 = f14;
        int i3 = 0;
        while (true) {
            double d7 = (double) i3;
            if (d7 < ceil) {
                float f17 = z ? floatValue2 : floatValue3;
                int i4 = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i4 == 0 || d7 != ceil - 2.0d) {
                    f4 = f16;
                } else {
                    f4 = f16;
                    f16 = (f13 * f15) / 2.0f;
                }
                if (i4 == 0 || d7 != ceil - 1.0d) {
                    f6 = f13;
                    f5 = floatValue3;
                    f8 = f17;
                    f7 = floatValue2;
                } else {
                    f6 = f13;
                    f7 = floatValue2;
                    f5 = floatValue3;
                    f8 = f;
                }
                double d8 = (double) f8;
                float f18 = f16;
                float cos2 = (float) (d8 * Math.cos(d6));
                float sin2 = (float) (d8 * Math.sin(d6));
                if (floatValue4 == 0.0f && floatValue5 == 0.0f) {
                    this.path.lineTo(cos2, sin2);
                    f12 = sin2;
                    f9 = floatValue4;
                    f10 = floatValue5;
                    f11 = f;
                } else {
                    f9 = floatValue4;
                    f10 = floatValue5;
                    double atan2 = (double) ((float) (Math.atan2((double) f2, (double) f3) - 1.5707963267948966d));
                    float cos3 = (float) Math.cos(atan2);
                    float sin3 = (float) Math.sin(atan2);
                    f11 = f;
                    f12 = sin2;
                    float f19 = f3;
                    double atan22 = (double) ((float) (Math.atan2((double) sin2, (double) cos2) - 1.5707963267948966d));
                    float cos4 = (float) Math.cos(atan22);
                    float sin4 = (float) Math.sin(atan22);
                    float f20 = z ? f9 : f10;
                    float f21 = (z ? f5 : f7) * f20 * O00OoOoO;
                    float f22 = cos3 * f21;
                    float f23 = f21 * sin3;
                    float f24 = (z ? f7 : f5) * (z ? f10 : f9) * O00OoOoO;
                    float f25 = cos4 * f24;
                    float f26 = f24 * sin4;
                    if (i != 0) {
                        if (i3 == 0) {
                            f22 *= f15;
                            f23 *= f15;
                        } else if (d7 == ceil - 1.0d) {
                            f25 *= f15;
                            f26 *= f15;
                        }
                    }
                    this.path.cubicTo(f19 - f22, f2 - f23, cos2 + f25, f12 + f26, cos2, f12);
                }
                d6 += (double) f18;
                z = !z;
                i3++;
                f3 = cos2;
                f = f11;
                floatValue2 = f7;
                f13 = f6;
                f16 = f4;
                floatValue3 = f5;
                floatValue4 = f9;
                floatValue5 = f10;
                f2 = f12;
            } else {
                PointF pointF = (PointF) this.O00Oo0o0.getValue();
                this.path.offset(pointF.x, pointF.y);
                this.path.close();
                return;
            }
        }
    }

    private void invalidate() {
        this.O00OooOO = false;
        this.O000OoO0.invalidateSelf();
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001e, code lost:
        if (r0 != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0020, code lost:
        r0.O000000o(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0031, code lost:
        if (r0 != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:26:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        O0000O0o o0000O0o2;
        if (obj == C0087O000Ooo0.OO00o00) {
            o0000O0o = this.O00OoO;
        } else if (obj == C0087O000Ooo0.OO00o0O) {
            o0000O0o = this.rotationAnimation;
        } else if (obj == C0087O000Ooo0.POSITION) {
            o0000O0o = this.O00Oo0o0;
        } else {
            if (obj == C0087O000Ooo0.OO00o0o) {
                o0000O0o2 = this.O00OoOO0;
            }
            if (obj == C0087O000Ooo0.OO00oO0) {
                o0000O0o = this.O00OoOO;
            } else {
                if (obj == C0087O000Ooo0.OO00oO) {
                    o0000O0o2 = this.O00OoOo0;
                }
                if (obj == C0087O000Ooo0.OO00oOO) {
                    o0000O0o = this.O00OoOo;
                } else {
                    return;
                }
            }
        }
        o0000O0o.O000000o(o0000Oo);
    }

    public void O000000o(List list, List list2) {
        for (int i = 0; i < list.size(); i++) {
            C0006O00000oO o00000oO = (C0006O00000oO) list.get(i);
            if (o00000oO instanceof C0018O0000ooO) {
                C0018O0000ooO o0000ooO = (C0018O0000ooO) o00000oO;
                if (o0000ooO.getType() == ShapeTrimPath$Type.SIMULTANEOUSLY) {
                    this.O00Oo0oO.O000000o(o0000ooO);
                    o0000ooO.O000000o(this);
                }
            }
        }
    }

    public void O00000oO() {
        invalidate();
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        if (this.O00OooOO) {
            return this.path;
        }
        this.path.reset();
        if (!this.hidden) {
            int i = C0013O0000oO0.O00Ooo[this.type.ordinal()];
            if (i == 1) {
                Oo0oOoo();
            } else if (i == 2) {
                Oo0oOoO();
            }
            this.path.close();
            this.O00Oo0oO.O000000o(this.path);
        }
        this.O00OooOO = true;
        return this.path;
    }
}
