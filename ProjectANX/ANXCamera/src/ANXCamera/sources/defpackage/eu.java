package defpackage;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.RandomAccess;

/* renamed from: eu reason: default package */
final class eu {
    public static final fh a = a(false);
    public static final fh b = a(true);
    public static final fh c = new fh(null);
    private static final Class d;

    static {
        Class cls;
        try {
            cls = Class.forName("com.google.protobuf.GeneratedMessage");
        } catch (Throwable unused) {
            cls = null;
        }
        d = cls;
    }

    static int O000000o(int i, List list) {
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        int a2 = cn.a(i) * size;
        if (list instanceof ds) {
            ds dsVar = (ds) list;
            while (i2 < size) {
                Object c2 = dsVar.c(i2);
                a2 += c2 instanceof ck ? cn.O000000o((ck) c2) : cn.a((String) c2);
                i2++;
            }
        } else {
            while (i2 < size) {
                Object obj = list.get(i2);
                a2 += obj instanceof ck ? cn.O000000o((ck) obj) : cn.a((String) obj);
                i2++;
            }
        }
        return a2;
    }

    static int O000000o(int i, List list, es esVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int a2 = cn.a(i) * size;
        for (int i2 = 0; i2 < size; i2++) {
            Object obj = list.get(i2);
            a2 += obj instanceof dq ? cn.O000000o((dq) obj) : cn.O000000o((eh) obj, esVar);
        }
        return a2;
    }

    static Object O000000o(int i, int i2, Object obj, fh fhVar) {
        if (obj == null) {
            obj = fi.a();
        }
        ((fi) obj).O000000o(ga.a(i, 0), (Object) Long.valueOf((long) i2));
        return obj;
    }

    static Object O000000o(int i, List list, dh dhVar, Object obj, fh fhVar) {
        if (dhVar != null) {
            if (list instanceof RandomAccess) {
                int size = list.size();
                Object obj2 = obj;
                int i2 = 0;
                for (int i3 = 0; i3 < size; i3++) {
                    int intValue = ((Integer) list.get(i3)).intValue();
                    if (!dhVar.a(intValue)) {
                        obj2 = O000000o(i, intValue, obj2, fhVar);
                    } else {
                        if (i3 != i2) {
                            list.set(i2, Integer.valueOf(intValue));
                        }
                        i2++;
                    }
                }
                if (i2 == size) {
                    obj = obj2;
                } else {
                    list.subList(i2, size).clear();
                    return obj2;
                }
            } else {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    int intValue2 = ((Integer) it.next()).intValue();
                    if (!dhVar.a(intValue2)) {
                        obj = O000000o(i, intValue2, obj, fhVar);
                        it.remove();
                    }
                }
            }
        }
        return obj;
    }

    public static void O000000o(int i, List list, gb gbVar) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (list instanceof ds) {
                ds dsVar = (ds) list;
                while (i2 < list.size()) {
                    Object c2 = dsVar.c(i2);
                    if (c2 instanceof String) {
                        ((co) gbVar).a.O00000Oo(i, (String) c2);
                    } else {
                        ((co) gbVar).a.O00000Oo(i, (ck) c2);
                    }
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O00000Oo(i, (String) list.get(i2));
                i2++;
            }
        }
    }

    public static void O000000o(int i, List list, gb gbVar, es esVar) {
        if (list != null && !list.isEmpty()) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                ((co) gbVar).O00000Oo(i, list.get(i2), esVar);
            }
        }
    }

    public static void O000000o(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Double) list.get(i3)).doubleValue();
                    i4 += 8;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O00000oO(Double.doubleToRawLongBits(((Double) list.get(i2)).doubleValue()));
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O000000o(i, ((Double) list.get(i2)).doubleValue());
                i2++;
            }
        }
    }

    static void O000000o(ej ejVar, Object obj, Object obj2) {
        cu O00000Oo2 = ej.O00000Oo(obj2);
        if (!O00000Oo2.a()) {
            cu O0000O0o = ej.O0000O0o(obj);
            for (int i = 0; i < O00000Oo2.a.a(); i++) {
                O0000O0o.O000000o(O00000Oo2.a.b(i));
            }
            for (Entry O000000o2 : O00000Oo2.a.b()) {
                O0000O0o.O000000o(O000000o2);
            }
        }
    }

    static void O000000o(ff ffVar, Object obj, Object obj2, long j) {
        fr.O000000o(obj, j, ff.O00000Oo(fr.O00000oo(obj, j), fr.O00000oo(obj2, j)));
    }

    static void O000000o(fh fhVar, Object obj, Object obj2) {
        fhVar.O00000Oo(obj, fh.O00000o0(fhVar.O00000Oo(obj), fhVar.O00000Oo(obj2)));
    }

    static boolean O000000o(Object obj, Object obj2) {
        boolean z = true;
        if (obj != obj2) {
            if (obj == null) {
                z = false;
            } else if (obj.equals(obj2)) {
                return z;
            } else {
                return false;
            }
        }
        return z;
    }

    static int O00000Oo(int i, Object obj, es esVar) {
        if (obj instanceof dq) {
            return cn.O000000o(i, (dq) obj);
        }
        return cn.a(i) + cn.O000000o((eh) obj, esVar);
    }

    static int O00000Oo(int i, List list) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int a2 = size * cn.a(i);
        for (int i2 = 0; i2 < list.size(); i2++) {
            a2 += cn.O000000o((ck) list.get(i2));
        }
        return a2;
    }

    static int O00000Oo(int i, List list, es esVar) {
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        int i2 = 0;
        for (int i3 = 0; i3 < size; i3++) {
            i2 += cn.O000000o(i, (eh) list.get(i3), esVar);
        }
        return i2;
    }

    public static void O00000Oo(int i, List list, gb gbVar) {
        if (list != null && !list.isEmpty()) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                ((co) gbVar).a.O00000Oo(i, (ck) list.get(i2));
            }
        }
    }

    public static void O00000Oo(int i, List list, gb gbVar, es esVar) {
        if (list != null && !list.isEmpty()) {
            for (int i2 = 0; i2 < list.size(); i2++) {
                ((co) gbVar).O000000o(i, list.get(i2), esVar);
            }
        }
    }

    public static void O00000Oo(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Float) list.get(i3)).floatValue();
                    i4 += 4;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O000O0o0(Float.floatToRawIntBits(((Float) list.get(i2)).floatValue()));
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O000000o(i, ((Float) list.get(i2)).floatValue());
                i2++;
            }
        }
    }

    public static void O00000Oo(Class cls) {
        if (!de.class.isAssignableFrom(cls)) {
            Class cls2 = d;
            if (cls2 != null && !cls2.isAssignableFrom(cls)) {
                throw new IllegalArgumentException("Message classes must extend GeneratedMessage or GeneratedMessageLite");
            }
        }
    }

    static int O00000o(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return O00000oO(list) + (size * cn.a(i));
        }
        return 0;
    }

    static int O00000o(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof dw) {
            dw dwVar = (dw) list;
            i = 0;
            while (i2 < size) {
                i += cn.b(dwVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.b(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    public static void O00000o(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.a(((Long) list.get(i4)).longValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.d(((Long) list.get(i2)).longValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O00000o0(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
        }
    }

    static int O00000o0(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return size * cn.O000O0o(i);
        }
        return 0;
    }

    public static void O00000o0(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.a(((Long) list.get(i4)).longValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.d(((Long) list.get(i2)).longValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O00000o0(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
        }
    }

    static int O00000oO(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return size * cn.O000O0oo(i);
        }
        return 0;
    }

    static int O00000oO(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof df) {
            df dfVar = (df) list;
            i = 0;
            while (i2 < size) {
                i += cn.b(dfVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.b(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void O00000oO(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.b(((Long) list.get(i4)).longValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.d(cn.c(((Long) list.get(i2)).longValue()));
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.a(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
        }
    }

    static int O00000oo(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return size * cn.O000OO00(i);
        }
        return 0;
    }

    static int O00000oo(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof df) {
            df dfVar = (df) list;
            i = 0;
            while (i2 < size) {
                i += cn.b(dfVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.b(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void O00000oo(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Long) list.get(i3)).longValue();
                    i4 += 8;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O00000oO(((Long) list.get(i2)).longValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O00000oO(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
        }
    }

    static int O0000O0o(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return O00000oo(list) + (size * cn.a(i));
        }
        return 0;
    }

    static int O0000O0o(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof df) {
            df dfVar = (df) list;
            i = 0;
            while (i2 < size) {
                i += cn.c(dfVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.c(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void O0000O0o(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Long) list.get(i3)).longValue();
                    i4 += 8;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O00000oO(((Long) list.get(i2)).longValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O00000oO(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
        }
    }

    static int O0000OOo(int i, List list) {
        if (list.size() != 0) {
            return a(list) + (list.size() * cn.a(i));
        }
        return 0;
    }

    static int O0000OOo(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof df) {
            df dfVar = (df) list;
            i = 0;
            while (i2 < size) {
                i += cn.d(dfVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.d(((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
        return i;
    }

    public static void O0000OOo(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.b(((Integer) list.get(i4)).intValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.O000O0Oo(((Integer) list.get(i2)).intValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O0000O0o(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    static int O0000Oo(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return O00000o(list) + (size * cn.a(i));
        }
        return 0;
    }

    static int O0000Oo(List list) {
        return list.size() * 8;
    }

    public static void O0000Oo(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.d(((Integer) list.get(i4)).intValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.O00oOoOo(cn.O000O0OO(((Integer) list.get(i2)).intValue()));
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.a(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    static int O0000Oo0(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return O0000OOo(list) + (size * cn.a(i));
        }
        return 0;
    }

    static int O0000Oo0(List list) {
        return list.size() * 4;
    }

    public static void O0000Oo0(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.c(((Integer) list.get(i4)).intValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.O00oOoOo(((Integer) list.get(i2)).intValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O0000OOo(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    static int O0000OoO(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return O0000O0o(list) + (size * cn.a(i));
        }
        return 0;
    }

    static int O0000OoO(List list) {
        return list.size();
    }

    public static void O0000OoO(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Integer) list.get(i3)).intValue();
                    i4 += 4;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O000O0o0(((Integer) list.get(i2)).intValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O0000Oo0(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    static int O0000Ooo(int i, List list) {
        int size = list.size();
        if (size != 0) {
            return b(list) + (size * cn.a(i));
        }
        return 0;
    }

    public static void O0000Ooo(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Integer) list.get(i3)).intValue();
                    i4 += 4;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O000O0o0(((Integer) list.get(i2)).intValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O0000Oo0(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    public static void O0000o0(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                int i4 = 0;
                while (i3 < list.size()) {
                    ((Boolean) list.get(i3)).booleanValue();
                    i4++;
                    i3++;
                    boolean z2 = cn.a;
                }
                coVar.a.O00oOoOo(i4);
                while (i2 < list.size()) {
                    coVar.a.O000000o(((Boolean) list.get(i2)).booleanValue() ? (byte) 1 : 0);
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O000000o(i, ((Boolean) list.get(i2)).booleanValue());
                i2++;
            }
        }
    }

    public static void O0000o00(int i, List list, gb gbVar, boolean z) {
        if (list != null && !list.isEmpty()) {
            int i2 = 0;
            if (z) {
                co coVar = (co) gbVar;
                coVar.a.O00000o(i, 2);
                int i3 = 0;
                for (int i4 = 0; i4 < list.size(); i4++) {
                    i3 += cn.b(((Integer) list.get(i4)).intValue());
                }
                coVar.a.O00oOoOo(i3);
                while (i2 < list.size()) {
                    coVar.a.O000O0Oo(((Integer) list.get(i2)).intValue());
                    i2++;
                }
                return;
            }
            while (i2 < list.size()) {
                ((co) gbVar).a.O0000O0o(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
        }
    }

    static int a(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof dw) {
            dw dwVar = (dw) list;
            i = 0;
            while (i2 < size) {
                i += cn.a(dwVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.a(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }

    private static fh a(boolean z) {
        Class cls;
        try {
            cls = Class.forName("com.google.protobuf.UnknownFieldSetSchema");
        } catch (Throwable unused) {
            cls = null;
        }
        if (cls != null) {
            try {
                return (fh) cls.getConstructor(new Class[]{Boolean.TYPE}).newInstance(new Object[]{Boolean.valueOf(z)});
            } catch (Throwable unused2) {
            }
        }
        return null;
    }

    static int b(List list) {
        int i;
        int size = list.size();
        int i2 = 0;
        if (size == 0) {
            return 0;
        }
        if (list instanceof dw) {
            dw dwVar = (dw) list;
            i = 0;
            while (i2 < size) {
                i += cn.a(dwVar.b(i2));
                i2++;
            }
        } else {
            int i3 = 0;
            while (i2 < size) {
                i3 = i + cn.a(((Long) list.get(i2)).longValue());
                i2++;
            }
        }
        return i;
    }
}
