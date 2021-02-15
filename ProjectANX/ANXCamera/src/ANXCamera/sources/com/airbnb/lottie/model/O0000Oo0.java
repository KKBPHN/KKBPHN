package com.airbnb.lottie.model;

import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.util.Pair;

@RestrictTo({Scope.LIBRARY})
public class O0000Oo0 {
    @Nullable
    Object first;
    @Nullable
    Object second;

    private static boolean O00000o(Object obj, Object obj2) {
        return obj == obj2 || (obj != null && obj.equals(obj2));
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof Pair)) {
            return false;
        }
        Pair pair = (Pair) obj;
        if (O00000o(pair.first, this.first) && O00000o(pair.second, this.second)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        Object obj = this.first;
        int i = 0;
        int hashCode = obj == null ? 0 : obj.hashCode();
        Object obj2 = this.second;
        if (obj2 != null) {
            i = obj2.hashCode();
        }
        return hashCode ^ i;
    }

    public void set(Object obj, Object obj2) {
        this.first = obj;
        this.second = obj2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
