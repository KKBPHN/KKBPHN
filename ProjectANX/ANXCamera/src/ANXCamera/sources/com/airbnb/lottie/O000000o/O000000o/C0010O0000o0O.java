package com.airbnb.lottie.O000000o.O000000o;

import android.annotation.TargetApi;
import android.graphics.Path;
import android.graphics.Path.Op;
import android.os.Build.VERSION;
import com.airbnb.lottie.model.content.O0000O0o;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@TargetApi(19)
/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000o0O reason: case insensitive filesystem */
public class C0010O0000o0O implements O0000o, C0009O0000Ooo {
    private final List O00Oo0 = new ArrayList();
    private final Path O00OoO0 = new Path();
    private final O0000O0o O00OoO0O;
    private final Path O00OoO0o = new Path();
    private final String name;
    private final Path path = new Path();

    public C0010O0000o0O(O0000O0o o0000O0o) {
        if (VERSION.SDK_INT >= 19) {
            this.name = o0000O0o.getName();
            this.O00OoO0O = o0000O0o;
            return;
        }
        throw new IllegalStateException("Merge paths are not supported pre-KitKat.");
    }

    @TargetApi(19)
    private void O000000o(Op op) {
        this.O00OoO0o.reset();
        this.O00OoO0.reset();
        for (int size = this.O00Oo0.size() - 1; size >= 1; size--) {
            O0000o o0000o = (O0000o) this.O00Oo0.get(size);
            if (o0000o instanceof C0007O00000oo) {
                C0007O00000oo o00000oo = (C0007O00000oo) o0000o;
                List O00OOOo = o00000oo.O00OOOo();
                for (int size2 = O00OOOo.size() - 1; size2 >= 0; size2--) {
                    Path path2 = ((O0000o) O00OOOo.get(size2)).getPath();
                    path2.transform(o00000oo.O00OOo0());
                    this.O00OoO0o.addPath(path2);
                }
            } else {
                this.O00OoO0o.addPath(o0000o.getPath());
            }
        }
        O0000o o0000o2 = (O0000o) this.O00Oo0.get(0);
        if (o0000o2 instanceof C0007O00000oo) {
            C0007O00000oo o00000oo2 = (C0007O00000oo) o0000o2;
            List O00OOOo2 = o00000oo2.O00OOOo();
            for (int i = 0; i < O00OOOo2.size(); i++) {
                Path path3 = ((O0000o) O00OOOo2.get(i)).getPath();
                path3.transform(o00000oo2.O00OOo0());
                this.O00OoO0.addPath(path3);
            }
        } else {
            this.O00OoO0.set(o0000o2.getPath());
        }
        this.path.op(this.O00OoO0, this.O00OoO0o, op);
    }

    private void Oo0oOo() {
        for (int i = 0; i < this.O00Oo0.size(); i++) {
            this.path.addPath(((O0000o) this.O00Oo0.get(i)).getPath());
        }
    }

    public void O000000o(List list, List list2) {
        for (int i = 0; i < this.O00Oo0.size(); i++) {
            ((O0000o) this.O00Oo0.get(i)).O000000o(list, list2);
        }
    }

    public void O000000o(ListIterator listIterator) {
        while (listIterator.hasPrevious() && listIterator.previous() != this) {
        }
        while (listIterator.hasPrevious()) {
            C0006O00000oO o00000oO = (C0006O00000oO) listIterator.previous();
            if (o00000oO instanceof O0000o) {
                this.O00Oo0.add((O0000o) o00000oO);
                listIterator.remove();
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Path getPath() {
        Op op;
        this.path.reset();
        if (this.O00OoO0O.isHidden()) {
            return this.path;
        }
        int i = O0000o0.O00Oo[this.O00OoO0O.getMode().ordinal()];
        if (i != 1) {
            if (i == 2) {
                op = Op.UNION;
            } else if (i == 3) {
                op = Op.REVERSE_DIFFERENCE;
            } else if (i == 4) {
                op = Op.INTERSECT;
            } else if (i == 5) {
                op = Op.XOR;
            }
            O000000o(op);
        } else {
            Oo0oOo();
        }
        return this.path;
    }
}
