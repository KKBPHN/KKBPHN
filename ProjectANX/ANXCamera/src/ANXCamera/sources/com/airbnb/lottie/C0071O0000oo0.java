package com.airbnb.lottie;

import java.util.concurrent.Callable;
import org.json.JSONObject;

/* renamed from: com.airbnb.lottie.O0000oo0 reason: case insensitive filesystem */
class C0071O0000oo0 implements Callable {
    final /* synthetic */ JSONObject O000oOoo;
    final /* synthetic */ String val$cacheKey;

    C0071O0000oo0(JSONObject jSONObject, String str) {
        this.O000oOoo = jSONObject;
        this.val$cacheKey = str;
    }

    public C0086O000Ooo call() {
        return C0096O00oOooo.O00000Oo(this.O000oOoo, this.val$cacheKey);
    }
}
