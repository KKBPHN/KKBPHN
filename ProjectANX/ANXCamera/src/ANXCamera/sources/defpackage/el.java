package defpackage;

import java.util.Iterator;
import java.util.Map.Entry;

/* renamed from: el reason: default package */
final class el implements es {
    private final eh a;
    private final fh b;
    private final boolean c;
    private final ej d;

    private el(fh fhVar, ej ejVar, eh ehVar, byte[] bArr) {
        this.b = fhVar;
        this.c = ej.O000000o(ehVar);
        this.d = ejVar;
        this.a = ehVar;
    }

    static el O000000o(fh fhVar, ej ejVar, eh ehVar) {
        return new el(fhVar, ejVar, ehVar, null);
    }

    public final void O000000o(Object obj, gb gbVar) {
        Iterator d2 = ej.O00000Oo(obj).d();
        while (d2.hasNext()) {
            Entry entry = (Entry) d2.next();
            dd ddVar = (dd) entry.getKey();
            if (ddVar.c() == fz.MESSAGE) {
                gbVar.O000000o(ddVar.a(), entry instanceof dn ? ((dp) ((dn) entry).a.getValue()).a() : entry.getValue());
            } else {
                throw new IllegalStateException("Found invalid MessageSet item.");
            }
        }
        ((fi) this.b.O00000Oo(obj)).O000000o(gbVar);
    }

    public final void O000000o(Object obj, byte[] bArr, int i, int i2, by byVar) {
        de deVar = (de) obj;
        fi fiVar = deVar.h;
        if (fiVar == fi.a) {
            fiVar = fi.a();
            deVar.h = fiVar;
        }
        cu d2 = ((dc) obj).d();
        Object obj2 = null;
        while (i < i2) {
            int O000000o2 = eb.O000000o(bArr, i, byVar);
            int i3 = byVar.a;
            if (i3 == ga.a) {
                int i4 = 0;
                Object obj3 = null;
                while (O000000o2 < i2) {
                    O000000o2 = eb.O000000o(bArr, O000000o2, byVar);
                    int i5 = byVar.a;
                    int a2 = ga.a(i5);
                    int b2 = ga.b(i5);
                    if (b2 != 2) {
                        if (b2 == 3) {
                            if (obj2 != null) {
                                cq cqVar = (cq) obj2;
                                O000000o2 = eb.O000000o(ep.a.O00000Oo(cqVar.c.getClass()), bArr, O000000o2, i2, byVar);
                                d2.O000000o(cqVar.d, byVar.c);
                            } else if (a2 == 2) {
                                O000000o2 = eb.O00000oO(bArr, O000000o2, byVar);
                                obj3 = (ck) byVar.c;
                            }
                        }
                    } else if (a2 == 0) {
                        O000000o2 = eb.O000000o(bArr, O000000o2, byVar);
                        i4 = byVar.a;
                        obj2 = ej.O000000o(byVar.d, this.a, i4);
                    }
                    if (i5 == ga.b) {
                        break;
                    }
                    O000000o2 = eb.O000000o(i5, bArr, O000000o2, i2, byVar);
                }
                if (obj3 != null) {
                    fiVar.O000000o(ga.a(i4, 2), obj3);
                }
                i = O000000o2;
            } else if (ga.a(i3) == 2) {
                Object O000000o3 = ej.O000000o(byVar.d, this.a, ga.b(i3));
                if (O000000o3 != null) {
                    cq cqVar2 = (cq) O000000o3;
                    i = eb.O000000o(ep.a.O00000Oo(cqVar2.c.getClass()), bArr, O000000o2, i2, byVar);
                    d2.O000000o(cqVar2.d, byVar.c);
                } else {
                    i = eb.O000000o(i3, bArr, O000000o2, i2, fiVar, byVar);
                }
                obj2 = O000000o3;
            } else {
                i = eb.O000000o(i3, bArr, O000000o2, i2, byVar);
            }
        }
        if (i != i2) {
            throw dl.e();
        }
    }

    public final boolean O000000o(Object obj, Object obj2) {
        if (!this.b.O00000Oo(obj).equals(this.b.O00000Oo(obj2))) {
            return false;
        }
        if (this.c) {
            return ej.O00000Oo(obj).equals(ej.O00000Oo(obj2));
        }
        return true;
    }

    public final int O00000Oo(Object obj) {
        int O00000o02 = ((fi) this.b.O00000Oo(obj)).b();
        if (!this.c) {
            return O00000o02;
        }
        cu O00000Oo2 = ej.O00000Oo(obj);
        int i = 0;
        for (int i2 = 0; i2 < O00000Oo2.a.a(); i2++) {
            i += O00000Oo2.O00000Oo(O00000Oo2.a.b(i2));
        }
        for (Entry O00000Oo3 : O00000Oo2.a.b()) {
            i += O00000Oo2.O00000Oo(O00000Oo3);
        }
        return O00000o02 + i;
    }

    public final void O00000Oo(Object obj, Object obj2) {
        eu.O000000o(this.b, obj, obj2);
        if (this.c) {
            eu.O000000o(this.d, obj, obj2);
        }
    }

    public final void O00000o(Object obj) {
        this.b.O00000oO(obj);
        this.d.O00000oO(obj);
    }

    public final boolean O00000o0(Object obj) {
        return ej.O00000Oo(obj).e();
    }

    public final int O00000oO(Object obj) {
        int hashCode = this.b.O00000Oo(obj).hashCode();
        return this.c ? (hashCode * 53) + ej.O00000Oo(obj).hashCode() : hashCode;
    }

    public final Object a() {
        return this.a.l().g();
    }
}
