package com.airbnb.lottie.model;

import androidx.annotation.CheckResult;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* renamed from: com.airbnb.lottie.model.O00000oO reason: case insensitive filesystem */
public class C0102O00000oO {
    @Nullable
    private C0103O00000oo O00oO00o;
    private final List keys;

    private C0102O00000oO(C0102O00000oO o00000oO) {
        this.keys = new ArrayList(o00000oO.keys);
        this.O00oO00o = o00000oO.O00oO00o;
    }

    public C0102O00000oO(String... strArr) {
        this.keys = Arrays.asList(strArr);
    }

    private boolean O000OoO0(String str) {
        return "__container".equals(str);
    }

    private boolean Oo0oo0O() {
        List list = this.keys;
        return ((String) list.get(list.size() - 1)).equals("**");
    }

    @RestrictTo({Scope.LIBRARY})
    public C0102O00000oO O000000o(C0103O00000oo o00000oo) {
        C0102O00000oO o00000oO = new C0102O00000oO(this);
        o00000oO.O00oO00o = o00000oo;
        return o00000oO;
    }

    @RestrictTo({Scope.LIBRARY})
    public boolean O00000Oo(String str, int i) {
        boolean z = false;
        if (i >= this.keys.size()) {
            return false;
        }
        boolean z2 = i == this.keys.size() - 1;
        String str2 = (String) this.keys.get(i);
        if (!str2.equals("**")) {
            boolean z3 = str2.equals(str) || str2.equals("*");
            if ((z2 || (i == this.keys.size() - 2 && Oo0oo0O())) && z3) {
                z = true;
            }
            return z;
        }
        boolean z4 = !z2 && ((String) this.keys.get(i + 1)).equals(str);
        if (z4) {
            if (i == this.keys.size() - 2 || (i == this.keys.size() - 3 && Oo0oo0O())) {
                z = true;
            }
            return z;
        } else if (z2) {
            return true;
        } else {
            int i2 = i + 1;
            if (i2 < this.keys.size() - 1) {
                return false;
            }
            return ((String) this.keys.get(i2)).equals(str);
        }
    }

    @RestrictTo({Scope.LIBRARY})
    public boolean O00000o(String str, int i) {
        if (O000OoO0(str)) {
            return true;
        }
        if (i >= this.keys.size()) {
            return false;
        }
        return ((String) this.keys.get(i)).equals(str) || ((String) this.keys.get(i)).equals("**") || ((String) this.keys.get(i)).equals("*");
    }

    @RestrictTo({Scope.LIBRARY})
    public int O00000o0(String str, int i) {
        if (O000OoO0(str)) {
            return 0;
        }
        if (!((String) this.keys.get(i)).equals("**")) {
            return 1;
        }
        return (i != this.keys.size() - 1 && ((String) this.keys.get(i + 1)).equals(str)) ? 2 : 0;
    }

    @RestrictTo({Scope.LIBRARY})
    public boolean O00000oO(String str, int i) {
        boolean z = true;
        if ("__container".equals(str)) {
            return true;
        }
        if (i >= this.keys.size() - 1 && !((String) this.keys.get(i)).equals("**")) {
            z = false;
        }
        return z;
    }

    @CheckResult
    @RestrictTo({Scope.LIBRARY})
    public C0102O00000oO O000O0Oo(String str) {
        C0102O00000oO o00000oO = new C0102O00000oO(this);
        o00000oO.keys.add(str);
        return o00000oO;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY})
    public C0103O00000oo O00OoO0() {
        return this.O00oO00o;
    }

    public String O00OoO0o() {
        return this.keys.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KeyPath{keys=");
        sb.append(this.keys);
        sb.append(",resolved=");
        sb.append(this.O00oO00o != null);
        sb.append('}');
        return sb.toString();
    }
}
