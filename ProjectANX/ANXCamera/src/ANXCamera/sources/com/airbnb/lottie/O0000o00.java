package com.airbnb.lottie;

final class O0000o00 implements C0082O000OoO, O00000Oo {
    private boolean cancelled;
    private final O000o00 listener;

    private O0000o00(O000o00 o000o00) {
        this.cancelled = false;
        this.listener = o000o00;
    }

    /* renamed from: O00000o */
    public void O000000o(C0064O0000o0O o0000o0O) {
        if (!this.cancelled) {
            this.listener.O000000o(o0000o0O);
        }
    }

    public void cancel() {
        this.cancelled = true;
    }
}
