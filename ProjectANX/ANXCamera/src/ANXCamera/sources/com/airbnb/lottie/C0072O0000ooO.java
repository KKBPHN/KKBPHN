package com.airbnb.lottie;

import com.airbnb.lottie.parser.moshi.O00000Oo;
import java.util.concurrent.Callable;

/* renamed from: com.airbnb.lottie.O0000ooO reason: case insensitive filesystem */
class C0072O0000ooO implements Callable {
    final /* synthetic */ String val$cacheKey;
    final /* synthetic */ O00000Oo val$reader;

    C0072O0000ooO(O00000Oo o00000Oo, String str) {
        this.val$reader = o00000Oo;
        this.val$cacheKey = str;
    }

    public C0086O000Ooo call() {
        return C0096O00oOooo.O00000Oo(this.val$reader, this.val$cacheKey);
    }
}
