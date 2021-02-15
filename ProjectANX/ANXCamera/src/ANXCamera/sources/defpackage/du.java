package defpackage;

/* renamed from: du reason: default package */
final class du extends dv {
    static di O00000Oo(Object obj, long j) {
        return (di) fr.O00000oo(obj, j);
    }

    public final void O000000o(Object obj, long j) {
        O00000Oo(obj, j).b();
    }

    public final void O000000o(Object obj, Object obj2, long j) {
        di O00000Oo2 = O00000Oo(obj, j);
        di O00000Oo3 = O00000Oo(obj2, j);
        int size = O00000Oo2.size();
        int size2 = O00000Oo3.size();
        if (size > 0 && size2 > 0) {
            if (!O00000Oo2.a()) {
                O00000Oo2 = O00000Oo2.a(size2 + size);
            }
            O00000Oo2.addAll(O00000Oo3);
        }
        if (size <= 0) {
            O00000Oo2 = O00000Oo3;
        }
        fr.O000000o(obj, j, (Object) O00000Oo2);
    }
}
