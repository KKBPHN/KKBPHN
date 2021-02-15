package com.airbnb.lottie.parser.moshi;

class O0000O0o {
    private O0000o00 O0OO0o0;

    O0000O0o() {
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(O0000o00 o0000o00) {
        O0000o00 o0000o002 = null;
        while (true) {
            O0000o00 o0000o003 = o0000o002;
            o0000o002 = o0000o00;
            O0000o00 o0000o004 = o0000o003;
            if (o0000o002 != null) {
                o0000o002.parent = o0000o004;
                o0000o00 = o0000o002.left;
            } else {
                this.O0OO0o0 = o0000o004;
                return;
            }
        }
    }

    public O0000o00 next() {
        O0000o00 o0000o00 = this.O0OO0o0;
        if (o0000o00 == null) {
            return null;
        }
        O0000o00 o0000o002 = o0000o00.parent;
        o0000o00.parent = null;
        O0000o00 o0000o003 = o0000o00.right;
        while (true) {
            O0000o00 o0000o004 = o0000o002;
            o0000o002 = o0000o003;
            O0000o00 o0000o005 = o0000o004;
            if (o0000o002 != null) {
                o0000o002.parent = o0000o005;
                o0000o003 = o0000o002.left;
            } else {
                this.O0OO0o0 = o0000o005;
                return o0000o00;
            }
        }
    }
}
