package com.airbnb.lottie.parser.moshi;

/* renamed from: com.airbnb.lottie.parser.moshi.O00000oo reason: case insensitive filesystem */
final class C0117O00000oo {
    private int O0OO0Oo;
    private int O0Oo0o0;
    private int size;
    private O0000o00 stack;

    C0117O00000oo() {
    }

    /* access modifiers changed from: 0000 */
    public void O000000o(O0000o00 o0000o00) {
        o0000o00.right = null;
        o0000o00.parent = null;
        o0000o00.left = null;
        o0000o00.height = 1;
        int i = this.O0Oo0o0;
        if (i > 0) {
            int i2 = this.size;
            if ((i2 & 1) == 0) {
                this.size = i2 + 1;
                this.O0Oo0o0 = i - 1;
                this.O0OO0Oo++;
            }
        }
        o0000o00.parent = this.stack;
        this.stack = o0000o00;
        this.size++;
        int i3 = this.O0Oo0o0;
        if (i3 > 0) {
            int i4 = this.size;
            if ((i4 & 1) == 0) {
                this.size = i4 + 1;
                this.O0Oo0o0 = i3 - 1;
                this.O0OO0Oo++;
            }
        }
        int i5 = 4;
        while (true) {
            int i6 = i5 - 1;
            if ((this.size & i6) == i6) {
                int i7 = this.O0OO0Oo;
                if (i7 == 0) {
                    O0000o00 o0000o002 = this.stack;
                    O0000o00 o0000o003 = o0000o002.parent;
                    O0000o00 o0000o004 = o0000o003.parent;
                    o0000o003.parent = o0000o004.parent;
                    this.stack = o0000o003;
                    o0000o003.left = o0000o004;
                    o0000o003.right = o0000o002;
                    o0000o003.height = o0000o002.height + 1;
                    o0000o004.parent = o0000o003;
                    o0000o002.parent = o0000o003;
                } else {
                    if (i7 == 1) {
                        O0000o00 o0000o005 = this.stack;
                        O0000o00 o0000o006 = o0000o005.parent;
                        this.stack = o0000o006;
                        o0000o006.right = o0000o005;
                        o0000o006.height = o0000o005.height + 1;
                        o0000o005.parent = o0000o006;
                    } else if (i7 != 2) {
                    }
                    this.O0OO0Oo = 0;
                }
                i5 *= 2;
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public O0000o00 O00oo000() {
        O0000o00 o0000o00 = this.stack;
        if (o0000o00.parent == null) {
            return o0000o00;
        }
        throw new IllegalStateException();
    }

    /* access modifiers changed from: 0000 */
    public void reset(int i) {
        this.O0Oo0o0 = ((Integer.highestOneBit(i) * 2) - 1) - i;
        this.size = 0;
        this.O0OO0Oo = 0;
        this.stack = null;
    }
}
