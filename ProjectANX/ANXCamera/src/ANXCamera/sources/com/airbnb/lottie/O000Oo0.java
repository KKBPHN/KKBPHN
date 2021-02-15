package com.airbnb.lottie;

import android.graphics.ColorFilter;
import androidx.annotation.Nullable;

class O000Oo0 {
    final String O00O000o;
    @Nullable
    final String O00O00Oo;
    @Nullable
    final ColorFilter colorFilter;

    O000Oo0(@Nullable String str, @Nullable String str2, @Nullable ColorFilter colorFilter2) {
        this.O00O000o = str;
        this.O00O00Oo = str2;
        this.colorFilter = colorFilter2;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof O000Oo0)) {
            return false;
        }
        O000Oo0 o000Oo0 = (O000Oo0) obj;
        if (!(hashCode() == o000Oo0.hashCode() && this.colorFilter == o000Oo0.colorFilter)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        String str = this.O00O000o;
        int hashCode = str != null ? 527 * str.hashCode() : 17;
        String str2 = this.O00O00Oo;
        return str2 != null ? hashCode * 31 * str2.hashCode() : hashCode;
    }
}
