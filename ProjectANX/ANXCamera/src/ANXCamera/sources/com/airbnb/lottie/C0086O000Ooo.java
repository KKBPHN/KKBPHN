package com.airbnb.lottie;

import androidx.annotation.Nullable;
import java.util.Arrays;

/* renamed from: com.airbnb.lottie.O000Ooo reason: case insensitive filesystem */
public final class C0086O000Ooo {
    @Nullable
    private final Throwable exception;
    @Nullable
    private final Object value;

    public C0086O000Ooo(Object obj) {
        this.value = obj;
        this.exception = null;
    }

    public C0086O000Ooo(Throwable th) {
        this.exception = th;
        this.value = null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof C0086O000Ooo)) {
            return false;
        }
        C0086O000Ooo o000Ooo = (C0086O000Ooo) obj;
        if (getValue() != null && getValue().equals(o000Ooo.getValue())) {
            return true;
        }
        if (getException() == null || o000Ooo.getException() == null) {
            return false;
        }
        return getException().toString().equals(getException().toString());
    }

    @Nullable
    public Throwable getException() {
        return this.exception;
    }

    @Nullable
    public Object getValue() {
        return this.value;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{getValue(), getException()});
    }
}
