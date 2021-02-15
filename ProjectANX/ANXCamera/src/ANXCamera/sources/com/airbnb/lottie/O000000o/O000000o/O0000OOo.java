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
import com.airbnb.lottie.model.content.O000000o;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.List;

public class O0000OOo implements O0000o, O00000Oo, O0000o00 {
    private static final float O00Oo0oo = 0.55228f;
    private final C0083O000OoO0 O000OoO0;
    private final O0000O0o O00Oo0Oo;
    private final O000000o O00Oo0o;
    private final O0000O0o O00Oo0o0;
    private O00000o O00Oo0oO = new O00000o();
    private boolean O00OooOO;
    private final String name;
    private final Path path = new Path();

    public O0000OOo(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O000000o o000000o) {
        this.name = o000000o.getName();
        this.O000OoO0 = o000OoO0;
        this.O00Oo0Oo = o000000o.getSize().O00000o();
        this.O00Oo0o0 = o000000o.getPosition().O00000o();
        this.O00Oo0o = o000000o;
        o00000o0.O000000o(this.O00Oo0Oo);
        o00000o0.O000000o(this.O00Oo0o0);
        this.O00Oo0Oo.O00000Oo(this);
        this.O00Oo0o0.O00000Oo(this);
    }

    private void invalidate() {
        this.O00OooOO = false;
        this.O000OoO0.invalidateSelf();
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        if (obj == C0087O000Ooo0.OO00O0) {
            o0000O0o = this.O00Oo0Oo;
        } else if (obj == C0087O000Ooo0.POSITION) {
            o0000O0o = this.O00Oo0o0;
        } else {
            return;
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
        if (!this.O00Oo0o.isHidden()) {
            PointF pointF = (PointF) this.O00Oo0Oo.getValue();
            float f = pointF.x / 2.0f;
            float f2 = pointF.y / 2.0f;
            float f3 = f * O00Oo0oo;
            float f4 = O00Oo0oo * f2;
            this.path.reset();
            if (this.O00Oo0o.isReversed()) {
                float f5 = -f2;
                this.path.moveTo(0.0f, f5);
                float f6 = 0.0f - f3;
                float f7 = -f;
                float f8 = 0.0f - f4;
                this.path.cubicTo(f6, f5, f7, f8, f7, 0.0f);
                float f9 = f4 + 0.0f;
                float f10 = f5;
                this.path.cubicTo(f7, f9, f6, f2, 0.0f, f2);
                float f11 = f3 + 0.0f;
                this.path.cubicTo(f11, f2, f, f9, f, 0.0f);
                this.path.cubicTo(f, f8, f11, f10, 0.0f, f10);
            } else {
                float f12 = -f2;
                this.path.moveTo(0.0f, f12);
                float f13 = f3 + 0.0f;
                float f14 = 0.0f - f4;
                this.path.cubicTo(f13, f12, f, f14, f, 0.0f);
                float f15 = f4 + 0.0f;
                this.path.cubicTo(f, f15, f13, f2, 0.0f, f2);
                float f16 = 0.0f - f3;
                float f17 = -f;
                this.path.cubicTo(f16, f2, f17, f15, f17, 0.0f);
                float f18 = f12;
                this.path.cubicTo(f17, f14, f16, f18, 0.0f, f18);
            }
            PointF pointF2 = (PointF) this.O00Oo0o0.getValue();
            this.path.offset(pointF2.x, pointF2.y);
            this.path.close();
            this.O00Oo0oO.O000000o(this.path);
        }
        this.O00OooOO = true;
        return this.path;
    }
}
