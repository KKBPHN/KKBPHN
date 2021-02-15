package defpackage;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/* renamed from: ff reason: default package */
final class ff {
    public static int O000000o(Object obj, Object obj2) {
        ec ecVar = (ec) obj;
        eb ebVar = (eb) obj2;
        if (!ecVar.isEmpty()) {
            Iterator it = ecVar.entrySet().iterator();
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                entry.getKey();
                entry.getValue();
                throw null;
            }
        }
        return 0;
    }

    static String O000000o(ck ckVar) {
        String str;
        fe feVar = new fe(ckVar);
        StringBuilder sb = new StringBuilder(feVar.a());
        for (int i = 0; i < feVar.a(); i++) {
            int a = feVar.a.a(i);
            if (a == 34) {
                str = "\\\"";
            } else if (a == 39) {
                str = "\\'";
            } else if (a != 92) {
                switch (a) {
                    case 7:
                        str = "\\a";
                        break;
                    case 8:
                        str = "\\b";
                        break;
                    case 9:
                        str = "\\t";
                        break;
                    case 10:
                        str = "\\n";
                        break;
                    case 11:
                        str = "\\v";
                        break;
                    case 12:
                        str = "\\f";
                        break;
                    case 13:
                        str = "\\r";
                        break;
                    default:
                        if (a < 32 || a > 126) {
                            sb.append('\\');
                            sb.append((char) (((a >>> 6) & 3) + 48));
                            sb.append((char) (((a >>> 3) & 7) + 48));
                            a = (a & 7) + 48;
                        }
                        sb.append((char) a);
                        continue;
                }
            } else {
                str = "\\\\";
            }
            sb.append(str);
        }
        return sb.toString();
    }

    public static ea O00000Oo(Object obj) {
        eb ebVar = (eb) obj;
        throw null;
    }

    public static Object O00000Oo(Object obj, Object obj2) {
        ec ecVar = (ec) obj;
        ec ecVar2 = (ec) obj2;
        if (!ecVar2.isEmpty()) {
            if (!ecVar.a) {
                ecVar = ecVar.a();
            }
            ecVar.c();
            if (!ecVar2.isEmpty()) {
                ecVar.putAll(ecVar2);
            }
        }
        return ecVar;
    }

    public static boolean O00000o(Object obj) {
        return !((ec) obj).a;
    }

    public static void O00000o0(Object obj) {
        ((ec) obj).b();
    }

    public static Map O00000oO(Object obj) {
        return (ec) obj;
    }

    public static Map O0000O0o(Object obj) {
        return (ec) obj;
    }

    public static Object a() {
        return ec.b.a();
    }
}
