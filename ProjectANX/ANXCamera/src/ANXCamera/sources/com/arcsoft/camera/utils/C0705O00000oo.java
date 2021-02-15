package com.arcsoft.camera.utils;

/* renamed from: com.arcsoft.camera.utils.O00000oo reason: case insensitive filesystem */
public class C0705O00000oo implements Comparable {
    private int a;
    private int b;

    public C0705O00000oo() {
    }

    public C0705O00000oo(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    /* renamed from: O000000o */
    public int O00000Oo(C0705O00000oo o00000oo) {
        if (o00000oo == null) {
            return 1;
        }
        int i = this.a;
        int i2 = o00000oo.a;
        return i == i2 ? this.b - o00000oo.b : i - i2;
    }

    public boolean O00000oO(Object obj) {
        if (this == obj) {
            return true;
        }
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if ((obj instanceof C0705O00000oo) && obj != null) {
            C0705O00000oo o00000oo = (C0705O00000oo) obj;
            z = a(o00000oo.a, o00000oo.b);
        }
        return z;
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(this.a);
        sb.append(",");
        sb.append(this.b);
        sb.append("]");
        return new String(sb.toString());
    }

    public boolean a(int i, int i2) {
        return this.a == i && this.b == i2;
    }

    public int b() {
        return (this.a * 31) + this.b;
    }
}
