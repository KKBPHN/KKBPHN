package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Path;
import android.graphics.Path.FillType;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.model.content.O0000o0;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000oo0 reason: case insensitive filesystem */
public class C0017O0000oo0 implements O0000o, O00000Oo {
    private final C0083O000OoO0 O000OoO0;
    private O00000o O00Oo0oO = new O00000o();
    private final O0000O0o O00OooO0;
    private boolean O00OooOO;
    private final boolean hidden;
    private final String name;
    private final Path path = new Path();

    public C0017O0000oo0(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, O0000o0 o0000o0) {
        this.name = o0000o0.getName();
        this.hidden = o0000o0.isHidden();
        this.O000OoO0 = o000OoO0;
        this.O00OooO0 = o0000o0.O00OoooO().O00000o();
        o00000o0.O000000o(this.O00OooO0);
        this.O00OooO0.O00000Oo(this);
    }

    private void invalidate() {
        this.O00OooOO = false;
        this.O000OoO0.invalidateSelf();
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
            this.path.set((Path) this.O00OooO0.getValue());
            this.path.setFillType(FillType.EVEN_ODD);
            this.O00Oo0oO.O000000o(this.path);
        }
        this.O00OooOO = true;
        return this.path;
    }
}
