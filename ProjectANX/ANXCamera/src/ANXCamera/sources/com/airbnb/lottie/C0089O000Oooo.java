package com.airbnb.lottie;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/* renamed from: com.airbnb.lottie.O000Oooo reason: case insensitive filesystem */
class C0089O000Oooo extends FutureTask {
    final /* synthetic */ O000o000 this$0;

    C0089O000Oooo(O000o000 o000o000, Callable callable) {
        this.this$0 = o000o000;
        super(callable);
    }

    /* access modifiers changed from: protected */
    public void done() {
        if (!isCancelled()) {
            try {
                this.this$0.O000000o((C0086O000Ooo) get());
            } catch (InterruptedException | ExecutionException e) {
                this.this$0.O000000o(new C0086O000Ooo(e));
            }
        }
    }
}
