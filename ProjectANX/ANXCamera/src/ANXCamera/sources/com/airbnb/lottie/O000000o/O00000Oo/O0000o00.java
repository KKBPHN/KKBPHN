package com.airbnb.lottie.O000000o.O00000Oo;

import com.airbnb.lottie.model.content.C0105O00000oo;
import java.util.ArrayList;
import java.util.List;

public class O0000o00 {
    private final List O00o0O;
    private final List O00o0O0o;
    private final List O00o0OO0;

    public O0000o00(List list) {
        this.O00o0OO0 = list;
        this.O00o0O0o = new ArrayList(list.size());
        this.O00o0O = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            this.O00o0O0o.add(((C0105O00000oo) list.get(i)).O00Ooo0o().O00000o());
            this.O00o0O.add(((C0105O00000oo) list.get(i)).getOpacity().O00000o());
        }
    }

    public List O00Oo0() {
        return this.O00o0OO0;
    }

    public List O00Oo00o() {
        return this.O00o0O0o;
    }

    public List O00Oo0OO() {
        return this.O00o0O;
    }
}
