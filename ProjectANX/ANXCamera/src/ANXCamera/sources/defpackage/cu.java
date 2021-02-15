package defpackage;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map.Entry;

/* renamed from: cu reason: default package */
public final class cu {
    public static final cu c = new cu(null);
    final fc a = fc.a(16);
    public boolean b;
    private boolean d;

    private cu() {
    }

    private cu(byte[] bArr) {
        b();
        b();
    }

    static int O000000o(fy fyVar, int i, Object obj) {
        int a2 = cn.a(i);
        if (fyVar == fy.GROUP) {
            dj.O000000o((eh) obj);
            a2 += a2;
        }
        return a2 + O000000o(fyVar, obj);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0033, code lost:
        return 8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0039, code lost:
        r2 = defpackage.cn.a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003b, code lost:
        return 4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0031, code lost:
        r2 = defpackage.cn.a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int O000000o(fy fyVar, Object obj) {
        fy fyVar2 = fy.DOUBLE;
        fz fzVar = fz.INT;
        switch (fyVar.ordinal()) {
            case 0:
                ((Double) obj).doubleValue();
                break;
            case 1:
                ((Float) obj).floatValue();
                break;
            case 2:
                return cn.a(((Long) obj).longValue());
            case 3:
                return cn.a(((Long) obj).longValue());
            case 4:
                return cn.b(((Integer) obj).intValue());
            case 5:
            case 15:
                ((Long) obj).longValue();
                break;
            case 6:
            case 14:
                ((Integer) obj).intValue();
                break;
            case 7:
                ((Boolean) obj).booleanValue();
                boolean z = cn.a;
                return 1;
            case 8:
                return obj instanceof ck ? cn.O000000o((ck) obj) : cn.a((String) obj);
            case 9:
                return cn.O00000o0((eh) obj);
            case 10:
                return obj instanceof dp ? cn.O000000o((dq) (dp) obj) : cn.O000000o((eh) obj);
            case 11:
                return obj instanceof ck ? cn.O000000o((ck) obj) : cn.b((byte[]) obj);
            case 12:
                return cn.c(((Integer) obj).intValue());
            case 13:
                return obj instanceof dg ? cn.b(((dg) obj).a()) : cn.b(((Integer) obj).intValue());
            case 16:
                return cn.d(((Integer) obj).intValue());
            case 17:
                return cn.b(((Long) obj).longValue());
            default:
                throw new RuntimeException("There is no way to get here, but the compiler thinks otherwise.");
        }
    }

    static void O000000o(cn cnVar, int i, Object obj) {
        if (fy.GROUP == null) {
            eh ehVar = (eh) obj;
            dj.O000000o(ehVar);
            cnVar.O00000o(i, 3);
            cnVar.O00000Oo(ehVar);
            cnVar.O00000o(i, 4);
            return;
        }
        throw null;
    }

    public static int O00000Oo(dd ddVar, Object obj) {
        return O000000o(ddVar.b(), ddVar.a(), obj);
    }

    private static final void O00000Oo(fy fyVar, Object obj) {
        boolean z;
        dj.O00000oO(obj);
        fy fyVar2 = fy.DOUBLE;
        fz fzVar = fz.INT;
        switch (fyVar.s.ordinal()) {
            case 0:
                z = obj instanceof Integer;
                break;
            case 1:
                z = obj instanceof Long;
                break;
            case 2:
                z = obj instanceof Float;
                break;
            case 3:
                z = obj instanceof Double;
                break;
            case 4:
                z = obj instanceof Boolean;
                break;
            case 5:
                z = obj instanceof String;
                break;
            case 6:
                if ((obj instanceof ck) || (obj instanceof byte[])) {
                    return;
                }
            case 7:
                if ((obj instanceof Integer) || (obj instanceof dg)) {
                    return;
                }
            case 8:
                if ((obj instanceof eh) || (obj instanceof dp)) {
                    return;
                }
        }
        if (z) {
            return;
        }
        throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
    }

    private static boolean O00000o0(Entry entry) {
        if (((dd) entry.getKey()).c() == fz.MESSAGE) {
            Object value = entry.getValue();
            if (value instanceof eh) {
                if (!((eh) value).c()) {
                    return false;
                }
            } else if (value instanceof dp) {
                return true;
            } else {
                throw new IllegalArgumentException("Wrong object type used with protocol message reflection.");
            }
        }
        return true;
    }

    private static Object O00000oO(Object obj) {
        if (obj instanceof em) {
            return ((em) obj).d();
        }
        if (!(obj instanceof byte[])) {
            return obj;
        }
        byte[] bArr = (byte[]) obj;
        int length = bArr.length;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, 0, bArr2, 0, length);
        return bArr2;
    }

    public final Object O000000o(dd ddVar) {
        Object obj = this.a.get(ddVar);
        if (!(obj instanceof dp)) {
            return obj;
        }
        dp dpVar = (dp) obj;
        throw null;
    }

    public final void O000000o(dd ddVar, Object obj) {
        O00000Oo(ddVar.b(), obj);
        if (obj instanceof dp) {
            this.d = true;
        }
        this.a.put(ddVar, obj);
    }

    public final void O000000o(Entry entry) {
        Object obj;
        fc fcVar;
        dd ddVar = (dd) entry.getKey();
        Object value = entry.getValue();
        if (!(value instanceof dp)) {
            if (ddVar.c() == fz.MESSAGE) {
                Object O000000o2 = O000000o(ddVar);
                if (O000000o2 != null) {
                    if (!(O000000o2 instanceof em)) {
                        obj = dd.O000000o(((eh) O000000o2).b(), (eh) value).h();
                        fcVar = this.a;
                        fcVar.put(ddVar, obj);
                        return;
                    }
                    em emVar = (em) O000000o2;
                    em emVar2 = (em) value;
                    dd.d();
                    throw null;
                }
            }
            fcVar = this.a;
            obj = O00000oO(value);
            fcVar.put(ddVar, obj);
            return;
        }
        dp dpVar = (dp) value;
        throw null;
    }

    public final int O00000Oo(Entry entry) {
        int O000000o2;
        int a2;
        dd ddVar = (dd) entry.getKey();
        Object value = entry.getValue();
        if (ddVar.c() != fz.MESSAGE) {
            return O00000Oo(ddVar, value);
        }
        if (value instanceof dp) {
            int a3 = ((dd) entry.getKey()).a();
            dp dpVar = (dp) value;
            int a4 = cn.a(1);
            O000000o2 = a4 + a4 + cn.O000000o(2, a3);
            a2 = cn.O000000o(3, (dq) dpVar);
        } else {
            int a5 = ((dd) entry.getKey()).a();
            eh ehVar = (eh) value;
            int a6 = cn.a(1);
            O000000o2 = a6 + a6 + cn.O000000o(2, a5);
            a2 = cn.a(3) + cn.O000000o(ehVar);
        }
        return O000000o2 + a2;
    }

    /* access modifiers changed from: 0000 */
    public final boolean a() {
        return this.a.isEmpty();
    }

    public final void b() {
        if (!this.b) {
            fc fcVar = this.a;
            if (!fcVar.c) {
                for (int i = 0; i < fcVar.a(); i++) {
                    dd ddVar = (dd) fcVar.b(i).getKey();
                }
                for (Entry key : fcVar.b()) {
                    dd ddVar2 = (dd) key.getKey();
                }
            }
            if (!fcVar.c) {
                fcVar.b = fcVar.b.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(fcVar.b);
                fcVar.d = !fcVar.d.isEmpty() ? Collections.unmodifiableMap(fcVar.d) : Collections.emptyMap();
                fcVar.c = true;
            }
            this.b = true;
        }
    }

    /* renamed from: c */
    public final cu clone() {
        cu cuVar = new cu();
        for (int i = 0; i < this.a.a(); i++) {
            Entry b2 = this.a.b(i);
            cuVar.O000000o((dd) b2.getKey(), b2.getValue());
        }
        for (Entry entry : this.a.b()) {
            cuVar.O000000o((dd) entry.getKey(), entry.getValue());
        }
        cuVar.d = this.d;
        return cuVar;
    }

    public final Iterator d() {
        return this.d ? new Cdo(this.a.entrySet().iterator()) : this.a.entrySet().iterator();
    }

    public final boolean e() {
        for (int i = 0; i < this.a.a(); i++) {
            if (!O00000o0(this.a.b(i))) {
                return false;
            }
        }
        for (Entry O00000o02 : this.a.b()) {
            if (!O00000o0(O00000o02)) {
                return false;
            }
        }
        return true;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof cu)) {
            return false;
        }
        return this.a.equals(((cu) obj).a);
    }

    public final int hashCode() {
        return this.a.hashCode();
    }
}
