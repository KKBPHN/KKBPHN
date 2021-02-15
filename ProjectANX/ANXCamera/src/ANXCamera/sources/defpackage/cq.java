package defpackage;

/* renamed from: cq reason: default package */
public final class cq {
    final eh a;
    public final Object b;
    final eh c;
    public final dd d;

    public cq() {
    }

    public cq(eh ehVar, Object obj, eh ehVar2, dd ddVar) {
        this();
        if (ehVar == null) {
            throw new IllegalArgumentException("Null containingTypeDefaultInstance");
        } else if (ddVar.b == fy.MESSAGE && ehVar2 == null) {
            throw new IllegalArgumentException("Null messageDefaultInstance");
        } else {
            this.a = ehVar;
            this.b = obj;
            this.c = ehVar2;
            this.d = ddVar;
        }
    }

    public Object O00000Oo(Object obj) {
        if (this.d.c() != fz.ENUM) {
            return obj;
        }
        ((Integer) obj).intValue();
        throw null;
    }

    public Object O00000oO(Object obj) {
        return O00000Oo(obj);
    }

    public Object O0000O0o(Object obj) {
        return this.d.c() == fz.ENUM ? Integer.valueOf(((dg) obj).a()) : obj;
    }

    public fy a() {
        return this.d.b;
    }
}
