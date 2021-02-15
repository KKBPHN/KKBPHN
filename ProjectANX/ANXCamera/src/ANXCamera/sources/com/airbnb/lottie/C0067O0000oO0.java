package com.airbnb.lottie;

import android.content.Context;
import com.airbnb.lottie.network.O00000Oo;
import java.util.concurrent.Callable;

/* renamed from: com.airbnb.lottie.O0000oO0 reason: case insensitive filesystem */
class C0067O0000oO0 implements Callable {
    final /* synthetic */ String O000oOOO;
    final /* synthetic */ Context val$context;

    C0067O0000oO0(Context context, String str) {
        this.val$context = context;
        this.O000oOOO = str;
    }

    public C0086O000Ooo call() {
        return O00000Oo.O00000oo(this.val$context, this.O000oOOO);
    }
}
