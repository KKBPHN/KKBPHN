package defpackage;

/* renamed from: dd reason: default package */
final class dd implements Comparable {
    final int a;
    final fy b;

    public dd(int i, fy fyVar) {
        this.a = i;
        this.b = fyVar;
    }

    public static final eg O000000o(eg egVar, eh ehVar) {
        da daVar = (da) egVar;
        daVar.O000000o((de) ehVar);
        return daVar;
    }

    public static final em d() {
        throw new UnsupportedOperationException();
    }

    public final int a() {
        return this.a;
    }

    public final fy b() {
        return this.b;
    }

    public final fz c() {
        return this.b.s;
    }

    public final /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return this.a - ((dd) obj).a;
    }
}
