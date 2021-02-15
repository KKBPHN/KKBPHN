package com.airbnb.lottie;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.airbnb.lottie.O000o0OO reason: case insensitive filesystem */
public class C0094O000o0OO {
    private final Map O00O0Oo0;
    @Nullable
    private final LottieAnimationView O00O0OoO;
    private boolean O00O0Ooo;
    @Nullable
    private final C0083O000OoO0 drawable;

    @VisibleForTesting
    C0094O000o0OO() {
        this.O00O0Oo0 = new HashMap();
        this.O00O0Ooo = true;
        this.O00O0OoO = null;
        this.drawable = null;
    }

    public C0094O000o0OO(LottieAnimationView lottieAnimationView) {
        this.O00O0Oo0 = new HashMap();
        this.O00O0Ooo = true;
        this.O00O0OoO = lottieAnimationView;
        this.drawable = null;
    }

    public C0094O000o0OO(C0083O000OoO0 o000OoO0) {
        this.O00O0Oo0 = new HashMap();
        this.O00O0Ooo = true;
        this.drawable = o000OoO0;
        this.O00O0OoO = null;
    }

    private String getText(String str) {
        return str;
    }

    private void invalidate() {
        LottieAnimationView lottieAnimationView = this.O00O0OoO;
        if (lottieAnimationView != null) {
            lottieAnimationView.invalidate();
        }
        C0083O000OoO0 o000OoO0 = this.drawable;
        if (o000OoO0 != null) {
            o000OoO0.invalidateSelf();
        }
    }

    public void O00000oO(String str, String str2) {
        this.O00O0Oo0.put(str, str2);
        invalidate();
    }

    public void O0000oO(boolean z) {
        this.O00O0Ooo = z;
    }

    public void O00OO0o() {
        this.O00O0Oo0.clear();
        invalidate();
    }

    public final String O00oOooO(String str) {
        if (this.O00O0Ooo && this.O00O0Oo0.containsKey(str)) {
            return (String) this.O00O0Oo0.get(str);
        }
        getText(str);
        if (this.O00O0Ooo) {
            this.O00O0Oo0.put(str, str);
        }
        return str;
    }

    public void O00oOooo(String str) {
        this.O00O0Oo0.remove(str);
        invalidate();
    }
}
