package defpackage;

import java.util.Map.Entry;

/* renamed from: ez reason: default package */
final class ez implements Entry, Comparable {
    public final Comparable a;
    public Object b;
    final /* synthetic */ fc c;

    public ez(fc fcVar, Comparable comparable, Object obj) {
        this.c = fcVar;
        this.a = comparable;
        this.b = obj;
    }

    private static final boolean O000000o(Object obj, Object obj2) {
        boolean z;
        if (obj != null) {
            z = obj.equals(obj2);
        } else if (obj2 == null) {
            return true;
        } else {
            z = false;
        }
        return z;
    }

    public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return this.a.compareTo(((ez) obj).a);
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Entry) {
            Entry entry = (Entry) obj;
            return O000000o(this.a, entry.getKey()) && O000000o(this.b, entry.getValue());
        }
    }

    public final /* bridge */ /* synthetic */ Object getKey() {
        return this.a;
    }

    public final Object getValue() {
        return this.b;
    }

    public final int hashCode() {
        Comparable comparable = this.a;
        int i = 0;
        int hashCode = comparable != null ? comparable.hashCode() : 0;
        Object obj = this.b;
        if (obj != null) {
            i = obj.hashCode();
        }
        return hashCode ^ i;
    }

    public final Object setValue(Object obj) {
        this.c.c();
        Object obj2 = this.b;
        this.b = obj;
        return obj2;
    }

    public final String toString() {
        String valueOf = String.valueOf(this.a);
        String valueOf2 = String.valueOf(this.b);
        StringBuilder sb = new StringBuilder(String.valueOf(valueOf).length() + 1 + String.valueOf(valueOf2).length());
        sb.append(valueOf);
        sb.append("=");
        sb.append(valueOf2);
        return sb.toString();
    }
}
