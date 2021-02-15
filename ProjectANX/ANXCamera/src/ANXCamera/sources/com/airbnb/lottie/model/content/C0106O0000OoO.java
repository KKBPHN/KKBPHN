package com.airbnb.lottie.model.content;

import android.graphics.PointF;
import androidx.annotation.FloatRange;
import com.airbnb.lottie.O00000o.O00000o;
import com.airbnb.lottie.O00000o.O0000O0o;
import com.airbnb.lottie.model.O000000o;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.content.O0000OoO reason: case insensitive filesystem */
public class C0106O0000OoO {
    private PointF O00oOOo;
    private final List O00oOOo0;
    private boolean closed;

    public C0106O0000OoO() {
        this.O00oOOo0 = new ArrayList();
    }

    public C0106O0000OoO(PointF pointF, boolean z, List list) {
        this.O00oOOo = pointF;
        this.closed = z;
        this.O00oOOo0 = new ArrayList(list);
    }

    private void O0000O0o(float f, float f2) {
        if (this.O00oOOo == null) {
            this.O00oOOo = new PointF();
        }
        this.O00oOOo.set(f, f2);
    }

    public void O000000o(C0106O0000OoO o0000OoO, C0106O0000OoO o0000OoO2, @FloatRange(from = 0.0d, to = 1.0d) float f) {
        if (this.O00oOOo == null) {
            this.O00oOOo = new PointF();
        }
        boolean z = o0000OoO.isClosed() || o0000OoO2.isClosed();
        this.closed = z;
        if (o0000OoO.O00Oooo().size() != o0000OoO2.O00Oooo().size()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Curves must have the same number of control points. Shape 1: ");
            sb.append(o0000OoO.O00Oooo().size());
            sb.append("\tShape 2: ");
            sb.append(o0000OoO2.O00Oooo().size());
            O00000o.warning(sb.toString());
        }
        int min = Math.min(o0000OoO.O00Oooo().size(), o0000OoO2.O00Oooo().size());
        if (this.O00oOOo0.size() < min) {
            for (int size = this.O00oOOo0.size(); size < min; size++) {
                this.O00oOOo0.add(new O000000o());
            }
        } else if (this.O00oOOo0.size() > min) {
            for (int size2 = this.O00oOOo0.size() - 1; size2 >= min; size2--) {
                List list = this.O00oOOo0;
                list.remove(list.size() - 1);
            }
        }
        PointF O00Ooooo = o0000OoO.O00Ooooo();
        PointF O00Ooooo2 = o0000OoO2.O00Ooooo();
        O0000O0o(O0000O0o.lerp(O00Ooooo.x, O00Ooooo2.x, f), O0000O0o.lerp(O00Ooooo.y, O00Ooooo2.y, f));
        for (int size3 = this.O00oOOo0.size() - 1; size3 >= 0; size3--) {
            O000000o o000000o = (O000000o) o0000OoO.O00Oooo().get(size3);
            O000000o o000000o2 = (O000000o) o0000OoO2.O00Oooo().get(size3);
            PointF O00Oo0o = o000000o.O00Oo0o();
            PointF O00Oo0oO = o000000o.O00Oo0oO();
            PointF O00OooOO = o000000o.O00OooOO();
            PointF O00Oo0o2 = o000000o2.O00Oo0o();
            PointF O00Oo0oO2 = o000000o2.O00Oo0oO();
            PointF O00OooOO2 = o000000o2.O00OooOO();
            ((O000000o) this.O00oOOo0.get(size3)).O00000o0(O0000O0o.lerp(O00Oo0o.x, O00Oo0o2.x, f), O0000O0o.lerp(O00Oo0o.y, O00Oo0o2.y, f));
            ((O000000o) this.O00oOOo0.get(size3)).O00000o(O0000O0o.lerp(O00Oo0oO.x, O00Oo0oO2.x, f), O0000O0o.lerp(O00Oo0oO.y, O00Oo0oO2.y, f));
            ((O000000o) this.O00oOOo0.get(size3)).O00000oO(O0000O0o.lerp(O00OooOO.x, O00OooOO2.x, f), O0000O0o.lerp(O00OooOO.y, O00OooOO2.y, f));
        }
    }

    public List O00Oooo() {
        return this.O00oOOo0;
    }

    public PointF O00Ooooo() {
        return this.O00oOOo;
    }

    public boolean isClosed() {
        return this.closed;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ShapeData{numCurves=");
        sb.append(this.O00oOOo0.size());
        sb.append("closed=");
        sb.append(this.closed);
        sb.append('}');
        return sb.toString();
    }
}
