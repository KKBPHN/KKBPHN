package com.airbnb.lottie;

import java.io.InputStream;
import java.util.concurrent.Callable;

/* renamed from: com.airbnb.lottie.O0000oOo reason: case insensitive filesystem */
class C0069O0000oOo implements Callable {
    final /* synthetic */ InputStream O000oOoO;
    final /* synthetic */ String val$cacheKey;

    C0069O0000oOo(InputStream inputStream, String str) {
        this.O000oOoO = inputStream;
        this.val$cacheKey = str;
    }

    public C0086O000Ooo call() {
        return C0096O00oOooo.O00000o0(this.O000oOoO, this.val$cacheKey);
    }
}
