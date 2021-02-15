package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.O0000Oo0;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000oOO reason: case insensitive filesystem */
public class C0014O0000oOO implements O00000Oo, O0000o00, O0000o {
    private final C0083O000OoO0 O000OoO0;
    private final O0000O0o O00Oo0Oo;
    private final O0000O0o O00Oo0o0;
    private O00000o O00Oo0oO = new O00000o();
    private final O0000O0o O00Ooo00;
    private boolean O00OooOO;
    private final boolean hidden;
    private final String name;
    private final Path path = new Path();
    private final RectF rect = new RectF();

    public C0014O0000oOO(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O0000Oo0 o0000Oo0) {
        this.name = o0000Oo0.getName();
        this.hidden = o0000Oo0.isHidden();
        this.O000OoO0 = o000OoO0;
        this.O00Oo0o0 = o0000Oo0.getPosition().O00000o();
        this.O00Oo0Oo = o0000Oo0.getSize().O00000o();
        this.O00Ooo00 = o0000Oo0.getCornerRadius().O00000o();
        o00000o0.O000000o(this.O00Oo0o0);
        o00000o0.O000000o(this.O00Oo0Oo);
        o00000o0.O000000o(this.O00Ooo00);
        this.O00Oo0o0.O00000Oo(this);
        this.O00Oo0Oo.O00000Oo(this);
        this.O00Ooo00.O00000Oo(this);
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
        if (obj == C0087O000Ooo0.OO00O0o) {
            o0000O0o = this.O00Oo0Oo;
        } else if (obj == C0087O000Ooo0.POSITION) {
            o0000O0o = this.O00Oo0o0;
        } else if (obj == C0087O000Ooo0.OO00O) {
            o0000O0o = this.O00Ooo00;
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
        if (!this.hidden) {
            PointF pointF = (PointF) this.O00Oo0Oo.getValue();
            float f = pointF.x / 2.0f;
            float f2 = pointF.y / 2.0f;
            O0000O0o o0000O0o = this.O00Ooo00;
            float floatValue = o0000O0o == null ? 0.0f : ((com.airbnb.lottie.O000000o.O00000Oo.O0000Oo0) o0000O0o).getFloatValue();
            float min = Math.min(f, f2);
            if (floatValue > min) {
                floatValue = min;
            }
            PointF pointF2 = (PointF) this.O00Oo0o0.getValue();
            this.path.moveTo(pointF2.x + f, (pointF2.y - f2) + floatValue);
            this.path.lineTo(pointF2.x + f, (pointF2.y + f2) - floatValue);
            int i = (floatValue > 0.0f ? 1 : (floatValue == 0.0f ? 0 : -1));
            if (i > 0) {
                RectF rectF = this.rect;
                float f3 = pointF2.x;
                float f4 = floatValue * 2.0f;
                float f5 = (f3 + f) - f4;
                float f6 = pointF2.y;
                rectF.set(f5, (f6 + f2) - f4, f3 + f, f6 + f2);
                this.path.arcTo(this.rect, 0.0f, 90.0f, false);
            }
            this.path.lineTo((pointF2.x - f) + floatValue, pointF2.y + f2);
            if (i > 0) {
                RectF rectF2 = this.rect;
                float f7 = pointF2.x;
                float f8 = f7 - f;
                float f9 = pointF2.y;
                float f10 = floatValue * 2.0f;
                rectF2.set(f8, (f9 + f2) - f10, (f7 - f) + f10, f9 + f2);
                this.path.arcTo(this.rect, 90.0f, 90.0f, false);
            }
            this.path.lineTo(pointF2.x - f, (pointF2.y - f2) + floatValue);
            if (i > 0) {
                RectF rectF3 = this.rect;
                float f11 = pointF2.x;
                float f12 = f11 - f;
                float f13 = pointF2.y;
                float f14 = floatValue * 2.0f;
                rectF3.set(f12, f13 - f2, (f11 - f) + f14, (f13 - f2) + f14);
                this.path.arcTo(this.rect, 180.0f, 90.0f, false);
            }
            this.path.lineTo((pointF2.x + f) - floatValue, pointF2.y - f2);
            if (i > 0) {
                RectF rectF4 = this.rect;
                float f15 = pointF2.x;
                float f16 = floatValue * 2.0f;
                float f17 = (f15 + f) - f16;
                float f18 = pointF2.y;
                rectF4.set(f17, f18 - f2, f15 + f, (f18 - f2) + f16);
                this.path.arcTo(this.rect, 270.0f, 90.0f, false);
            }
            this.path.close();
            this.O00Oo0oO.O000000o(this.path);
        }
        this.O00OooOO = true;
        return this.path;
    }
}
