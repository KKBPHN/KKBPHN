package com.airbnb.lottie;

/* renamed from: com.airbnb.lottie.O000OooO reason: case insensitive filesystem */
class C0088O000OooO implements Runnable {
    final /* synthetic */ O000o000 this$0;

    C0088O000OooO(O000o000 o000o000) {
        this.this$0 = o000o000;
    }

    public void run() {
        if (this.this$0.result != null) {
            C0086O000Ooo O000000o2 = this.this$0.result;
            Object value = O000000o2.getValue();
            O000o000 o000o000 = this.this$0;
            if (value != null) {
                o000o000.O0000OOo(O000000o2.getValue());
            } else {
                o000o000.O0000o0(O000000o2.getException());
            }
        }
    }
}
