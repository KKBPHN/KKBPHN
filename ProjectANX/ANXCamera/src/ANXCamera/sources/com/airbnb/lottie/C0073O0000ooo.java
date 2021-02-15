package com.airbnb.lottie;

import java.util.concurrent.Callable;
import java.util.zip.ZipInputStream;

/* renamed from: com.airbnb.lottie.O0000ooo reason: case insensitive filesystem */
class C0073O0000ooo implements Callable {
    final /* synthetic */ ZipInputStream O000oo0;
    final /* synthetic */ String val$cacheKey;

    C0073O0000ooo(ZipInputStream zipInputStream, String str) {
        this.O000oo0 = zipInputStream;
        this.val$cacheKey = str;
    }

    public C0086O000Ooo call() {
        return C0096O00oOooo.O00000Oo(this.O000oo0, this.val$cacheKey);
    }
}
