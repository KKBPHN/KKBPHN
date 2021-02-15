package com.airbnb.lottie.O000000o.O00000Oo;

import androidx.annotation.FloatRange;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.O00000oO.C0054O000000o;
import com.airbnb.lottie.O00000oO.O0000Oo;
import java.util.ArrayList;
import java.util.List;

public abstract class O0000O0o {
    @Nullable
    protected O0000Oo O00o00;
    private boolean O00o000O = false;
    private final O00000o O00o000o;
    private float O00o00O = -1.0f;
    @Nullable
    private Object O00o00O0 = null;
    private float O00o00OO = -1.0f;
    final List listeners = new ArrayList(1);
    private float progress = 0.0f;

    O0000O0o(List list) {
        this.O00o000o = O0000Ooo(list);
    }

    @FloatRange(from = 0.0d, to = 1.0d)
    private float O00000o0() {
        if (this.O00o00O == -1.0f) {
            this.O00o00O = this.O00o000o.O00000o0();
        }
        return this.O00o00O;
    }

    private static O00000o O0000Ooo(List list) {
        return list.isEmpty() ? new O00000o0() : list.size() == 1 ? new C0021O00000oo(list) : new C0020O00000oO(list);
    }

    /* access modifiers changed from: protected */
    public C0054O000000o O000000o() {
        String str = "BaseKeyframeAnimation#getCurrentKeyframe";
        C0053O00000oO.beginSection(str);
        C0054O000000o O000000o2 = this.O00o000o.O000000o();
        C0053O00000oO.O0000oOo(str);
        return O000000o2;
    }

    public abstract Object O000000o(C0054O000000o o000000o, float f);

    public void O000000o(@Nullable O0000Oo o0000Oo) {
        O0000Oo o0000Oo2 = this.O00o00;
        if (o0000Oo2 != null) {
            o0000Oo2.O00000o0(null);
        }
        this.O00o00 = o0000Oo;
        if (o0000Oo != null) {
            o0000Oo.O00000o0(this);
        }
    }

    public void O00000Oo(O00000Oo o00000Oo) {
        this.listeners.add(o00000Oo);
    }

    /* access modifiers changed from: 0000 */
    @FloatRange(from = 0.0d, to = 1.0d)
    public float O0000Oo0() {
        if (this.O00o00OO == -1.0f) {
            this.O00o00OO = this.O00o000o.O0000Oo0();
        }
        return this.O00o00OO;
    }

    /* access modifiers changed from: protected */
    public float O00OOo() {
        C0054O000000o O000000o2 = O000000o();
        if (O000000o2.isStatic()) {
            return 0.0f;
        }
        return O000000o2.interpolator.getInterpolation(O00OOoO());
    }

    /* access modifiers changed from: 0000 */
    public float O00OOoO() {
        if (this.O00o000O) {
            return 0.0f;
        }
        C0054O000000o O000000o2 = O000000o();
        if (O000000o2.isStatic()) {
            return 0.0f;
        }
        return (this.progress - O000000o2.O00o00o()) / (O000000o2.O0000Oo0() - O000000o2.O00o00o());
    }

    public void O00OOoo() {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((O00000Oo) this.listeners.get(i)).O00000oO();
        }
    }

    public void O00Oo00() {
        this.O00o000O = true;
    }

    public float getProgress() {
        return this.progress;
    }

    public Object getValue() {
        float O00OOo = O00OOo();
        if (this.O00o00 == null && this.O00o000o.O000000o(O00OOo)) {
            return this.O00o00O0;
        }
        Object O000000o2 = O000000o(O000000o(), O00OOo);
        this.O00o00O0 = O000000o2;
        return O000000o2;
    }

    public void setProgress(@FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (!this.O00o000o.isEmpty()) {
            if (f < O00000o0()) {
                f = O00000o0();
            } else if (f > O0000Oo0()) {
                f = O0000Oo0();
            }
            if (f != this.progress) {
                this.progress = f;
                if (this.O00o000o.O00000Oo(f)) {
                    O00OOoo();
                }
            }
        }
    }
}
