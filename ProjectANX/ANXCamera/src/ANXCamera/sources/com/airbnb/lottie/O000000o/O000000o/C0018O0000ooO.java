package com.airbnb.lottie.O000000o.O000000o;

import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.model.content.O0000o;
import com.airbnb.lottie.model.content.ShapeTrimPath$Type;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.airbnb.lottie.O000000o.O000000o.O0000ooO reason: case insensitive filesystem */
public class C0018O0000ooO implements C0006O00000oO, O00000Oo {
    private final O0000O0o O00OooO;
    private final O0000O0o O00OooOo;
    private final O0000O0o O00Oooo0;
    private final boolean hidden;
    private final List listeners = new ArrayList();
    private final String name;
    private final ShapeTrimPath$Type type;

    public C0018O0000ooO(O00000o0 o00000o0, O0000o o0000o) {
        this.name = o0000o.getName();
        this.hidden = o0000o.isHidden();
        this.type = o0000o.getType();
        this.O00OooO = o0000o.getStart().O00000o();
        this.O00OooOo = o0000o.getEnd().O00000o();
        this.O00Oooo0 = o0000o.getOffset().O00000o();
        o00000o0.O000000o(this.O00OooO);
        o00000o0.O000000o(this.O00OooOo);
        o00000o0.O000000o(this.O00Oooo0);
        this.O00OooO.O00000Oo(this);
        this.O00OooOo.O00000Oo(this);
        this.O00Oooo0.O00000Oo(this);
    }

    /* access modifiers changed from: 0000 */
    public void O000000o(O00000Oo o00000Oo) {
        this.listeners.add(o00000Oo);
    }

    public void O000000o(List list, List list2) {
    }

    public void O00000oO() {
        for (int i = 0; i < this.listeners.size(); i++) {
            ((O00000Oo) this.listeners.get(i)).O00000oO();
        }
    }

    public O0000O0o getEnd() {
        return this.O00OooOo;
    }

    public String getName() {
        return this.name;
    }

    public O0000O0o getOffset() {
        return this.O00Oooo0;
    }

    public O0000O0o getStart() {
        return this.O00OooO;
    }

    /* access modifiers changed from: 0000 */
    public ShapeTrimPath$Type getType() {
        return this.type;
    }

    public boolean isHidden() {
        return this.hidden;
    }
}
