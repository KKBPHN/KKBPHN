package com.airbnb.lottie;

import android.content.Context;
import java.util.concurrent.Callable;

/* renamed from: com.airbnb.lottie.O0000oO reason: case insensitive filesystem */
class C0066O0000oO implements Callable {
    final /* synthetic */ String O000oOOo;
    final /* synthetic */ Context val$appContext;
    final /* synthetic */ String val$cacheKey;

    C0066O0000oO(Context context, String str, String str2) {
        this.val$appContext = context;
        this.O000oOOo = str;
        this.val$cacheKey = str2;
    }

    public C0086O000Ooo call() {
        return C0096O00oOooo.O00000Oo(this.val$appContext, this.O000oOOo, this.val$cacheKey);
    }
}
