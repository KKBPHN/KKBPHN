package com.airbnb.lottie.O00000oO;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;

public class O0000Oo {
    @Nullable
    private O0000O0o animation;
    private final C0055O00000Oo frameInfo = new C0055O00000Oo();
    @Nullable
    protected Object value = null;

    public O0000Oo() {
    }

    public O0000Oo(@Nullable Object obj) {
        this.value = obj;
    }

    @Nullable
    public Object O000000o(C0055O00000Oo o00000Oo) {
        return this.value;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY})
    public final Object O00000Oo(float f, float f2, Object obj, Object obj2, float f3, float f4, float f5) {
        return O000000o(this.frameInfo.O000000o(f, f2, obj, obj2, f3, f4, f5));
    }

    @RestrictTo({Scope.LIBRARY})
    public final void O00000o0(@Nullable O0000O0o o0000O0o) {
        this.animation = o0000O0o;
    }

    public final void setValue(@Nullable Object obj) {
        this.value = obj;
        O0000O0o o0000O0o = this.animation;
        if (o0000O0o != null) {
            o0000O0o.O00OOoo();
        }
    }
}
