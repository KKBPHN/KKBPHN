package defpackage;

/* renamed from: cr reason: default package */
final class cr {
    private final Object a;
    private final int b;

    public cr(Object obj, int i) {
        this.a = obj;
        this.b = i;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof cr) {
            cr crVar = (cr) obj;
            if (this.a == crVar.a && this.b == crVar.b) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return (System.identityHashCode(this.a) * 65535) + this.b;
    }
}
