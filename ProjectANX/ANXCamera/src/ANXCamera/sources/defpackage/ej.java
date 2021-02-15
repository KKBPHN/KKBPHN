package defpackage;

import com.android.camera.aiwatermark.util.WatermarkConstant;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

/* renamed from: ej reason: default package */
final class ej {
    public ej() {
    }

    public ej(byte[] bArr) {
        this();
    }

    public static int O000000o(Entry entry) {
        return ((dd) entry.getKey()).a;
    }

    public static Object O000000o(cs csVar, eh ehVar, int i) {
        return csVar.O000000o(ehVar, i);
    }

    public static void O000000o(eh ehVar, StringBuilder sb, int i) {
        String str;
        Object O000000o2;
        String a;
        Object obj;
        HashMap hashMap = new HashMap();
        HashMap hashMap2 = new HashMap();
        TreeSet<String> treeSet = new TreeSet<>();
        Method[] declaredMethods = ehVar.getClass().getDeclaredMethods();
        int length = declaredMethods.length;
        int i2 = 0;
        while (true) {
            str = "get";
            if (i2 >= length) {
                break;
            }
            Method method = declaredMethods[i2];
            hashMap2.put(method.getName(), method);
            if (method.getParameterTypes().length == 0) {
                hashMap.put(method.getName(), method);
                if (method.getName().startsWith(str)) {
                    treeSet.add(method.getName());
                }
            }
            i2++;
        }
        for (String str2 : treeSet) {
            String substring = str2.startsWith(str) ? str2.substring(3) : str2;
            String str3 = "List";
            if (substring.endsWith(str3) && !substring.endsWith("OrBuilderList") && !substring.equals(str3)) {
                String valueOf = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf2 = String.valueOf(substring.substring(1, substring.length() - 4));
                String str4 = valueOf2.length() == 0 ? new String(valueOf) : valueOf.concat(valueOf2);
                Method method2 = (Method) hashMap.get(str2);
                if (method2 != null && method2.getReturnType().equals(List.class)) {
                    O000000o(sb, i, a(str4), de.O000000o(method2, (Object) ehVar, new Object[0]));
                }
            }
            String str5 = "Map";
            if (substring.endsWith(str5) && !substring.equals(str5)) {
                String valueOf3 = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf4 = String.valueOf(substring.substring(1, substring.length() - 3));
                String str6 = valueOf4.length() == 0 ? new String(valueOf3) : valueOf3.concat(valueOf4);
                Method method3 = (Method) hashMap.get(str2);
                if (method3 != null && method3.getReturnType().equals(Map.class) && !method3.isAnnotationPresent(Deprecated.class) && Modifier.isPublic(method3.getModifiers())) {
                    a = a(str6);
                    O000000o2 = de.O000000o(method3, (Object) ehVar, new Object[0]);
                    O000000o(sb, i, a, O000000o2);
                }
            }
            String valueOf5 = String.valueOf(substring);
            String str7 = "set";
            if (((Method) hashMap2.get(valueOf5.length() == 0 ? new String(str7) : str7.concat(valueOf5))) != null) {
                if (substring.endsWith("Bytes")) {
                    String valueOf6 = String.valueOf(substring.substring(0, substring.length() - 5));
                    if (hashMap.containsKey(valueOf6.length() == 0 ? new String(str) : str.concat(valueOf6))) {
                    }
                }
                String valueOf7 = String.valueOf(substring.substring(0, 1).toLowerCase());
                String valueOf8 = String.valueOf(substring.substring(1));
                String str8 = valueOf8.length() == 0 ? new String(valueOf7) : valueOf7.concat(valueOf8);
                String valueOf9 = String.valueOf(substring);
                Method method4 = (Method) hashMap.get(valueOf9.length() == 0 ? new String(str) : str.concat(valueOf9));
                String valueOf10 = String.valueOf(substring);
                String str9 = "has";
                Method method5 = (Method) hashMap.get(valueOf10.length() == 0 ? new String(str9) : str9.concat(valueOf10));
                if (method4 != null) {
                    O000000o2 = de.O000000o(method4, (Object) ehVar, new Object[0]);
                    if (method5 != null) {
                        if (!((Boolean) de.O000000o(method5, (Object) ehVar, new Object[0])).booleanValue()) {
                        }
                    } else if (O000000o2 instanceof Boolean) {
                        if (!((Boolean) O000000o2).booleanValue()) {
                        }
                    } else if (O000000o2 instanceof Integer) {
                        if (((Integer) O000000o2).intValue() == 0) {
                        }
                    } else if (O000000o2 instanceof Float) {
                        if (((Float) O000000o2).floatValue() == 0.0f) {
                        }
                    } else if (!(O000000o2 instanceof Double)) {
                        if (O000000o2 instanceof String) {
                            obj = "";
                        } else if (O000000o2 instanceof ck) {
                            obj = ck.b;
                        } else if (O000000o2 instanceof eh) {
                            if (O000000o2 == ((eh) O000000o2).i()) {
                            }
                        } else if ((O000000o2 instanceof Enum) && ((Enum) O000000o2).ordinal() == 0) {
                        }
                        if (O000000o2.equals(obj)) {
                        }
                    } else if (((Double) O000000o2).doubleValue() == 0.0d) {
                    }
                    a = a(str8);
                    O000000o(sb, i, a, O000000o2);
                }
            }
        }
        if (ehVar instanceof dc) {
            Iterator d = ((dc) ehVar).d.d();
            while (d.hasNext()) {
                Entry entry = (Entry) d.next();
                int i3 = ((dd) entry.getKey()).a;
                StringBuilder sb2 = new StringBuilder(13);
                sb2.append("[");
                sb2.append(i3);
                sb2.append("]");
                O000000o(sb, i, sb2.toString(), entry.getValue());
            }
        }
        fi fiVar = ((de) ehVar).h;
        if (fiVar != null) {
            for (int i4 = 0; i4 < fiVar.b; i4++) {
                O000000o(sb, i, String.valueOf(ga.b(fiVar.c[i4])), fiVar.d[i4]);
            }
        }
    }

    public static void O000000o(gb gbVar, Entry entry) {
        dd ddVar = (dd) entry.getKey();
        fy fyVar = fy.DOUBLE;
        switch (ddVar.b.ordinal()) {
            case 0:
                gbVar.O000000o(ddVar.a, ((Double) entry.getValue()).doubleValue());
                return;
            case 1:
                gbVar.O000000o(ddVar.a, ((Float) entry.getValue()).floatValue());
                return;
            case 2:
                gbVar.a(ddVar.a, ((Long) entry.getValue()).longValue());
                return;
            case 3:
                gbVar.O000000o(ddVar.a, ((Long) entry.getValue()).longValue());
                return;
            case 4:
                gbVar.O000000o(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 5:
                gbVar.O00000Oo(ddVar.a, ((Long) entry.getValue()).longValue());
                return;
            case 6:
                gbVar.O00000Oo(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 7:
                gbVar.O000000o(ddVar.a, ((Boolean) entry.getValue()).booleanValue());
                return;
            case 8:
                gbVar.a(ddVar.a, (String) entry.getValue());
                return;
            case 9:
                gbVar.O000000o(ddVar.a, entry.getValue(), ep.a.O00000Oo(entry.getValue().getClass()));
                return;
            case 10:
                gbVar.O00000Oo(ddVar.a, entry.getValue(), ep.a.O00000Oo(entry.getValue().getClass()));
                return;
            case 11:
                gbVar.O000000o(ddVar.a, (ck) entry.getValue());
                return;
            case 12:
                gbVar.O00000o0(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 13:
                gbVar.O000000o(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 14:
                gbVar.a(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 15:
                gbVar.O00000o(ddVar.a, ((Long) entry.getValue()).longValue());
                return;
            case 16:
                gbVar.O00000o(ddVar.a, ((Integer) entry.getValue()).intValue());
                return;
            case 17:
                gbVar.O00000o0(ddVar.a, ((Long) entry.getValue()).longValue());
                return;
            default:
                return;
        }
    }

    static final void O000000o(StringBuilder sb, int i, String str, Object obj) {
        if (obj instanceof List) {
            for (Object O000000o2 : (List) obj) {
                O000000o(sb, i, str, O000000o2);
            }
        } else if (obj instanceof Map) {
            for (Entry O000000o3 : ((Map) obj).entrySet()) {
                O000000o(sb, i, str, O000000o3);
            }
        } else {
            sb.append(10);
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                sb.append(' ');
            }
            sb.append(str);
            String str2 = ": \"";
            if (obj instanceof String) {
                sb.append(str2);
                sb.append(ff.O000000o(ck.a((String) obj)));
                sb.append('\"');
            } else if (obj instanceof ck) {
                sb.append(str2);
                sb.append(ff.O000000o((ck) obj));
                sb.append('\"');
            } else {
                String str3 = "}";
                String str4 = "\n";
                String str5 = " {";
                if (obj instanceof de) {
                    sb.append(str5);
                    O000000o((eh) (de) obj, sb, i + 2);
                    sb.append(str4);
                    while (i2 < i) {
                        sb.append(' ');
                        i2++;
                    }
                    sb.append(str3);
                } else if (obj instanceof Entry) {
                    sb.append(str5);
                    Entry entry = (Entry) obj;
                    int i4 = i + 2;
                    O000000o(sb, i4, WatermarkConstant.ITEM_KEY, entry.getKey());
                    O000000o(sb, i4, "value", entry.getValue());
                    sb.append(str4);
                    while (i2 < i) {
                        sb.append(' ');
                        i2++;
                    }
                    sb.append(str3);
                } else {
                    sb.append(": ");
                    sb.append(obj.toString());
                }
            }
        }
    }

    public static boolean O000000o(eh ehVar) {
        return ehVar instanceof dc;
    }

    public static cu O00000Oo(Object obj) {
        return ((dc) obj).d;
    }

    public static cu O0000O0o(Object obj) {
        return ((dc) obj).d();
    }

    private static final String a(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (Character.isUpperCase(charAt)) {
                sb.append("_");
            }
            sb.append(Character.toLowerCase(charAt));
        }
        return sb.toString();
    }

    public void O00000oO(Object obj) {
        O00000Oo(obj).b();
    }
}
