package com.arcsoft.camera.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class O00000o {
    private static final String a = "O00000o";
    private static final double b = 1.0E-9d;

    private O00000o() {
    }

    public static void O000000o(Comparable[] comparableArr) {
        Arrays.sort(comparableArr);
    }

    public static boolean O000000o(Object obj, Object obj2) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5 = true;
        if (obj == null && obj2 == null) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj instanceof Byte) {
            if (((Byte) obj).byteValue() != ((Byte) obj2).byteValue()) {
                z5 = false;
            }
            return z5;
        } else if (obj instanceof Short) {
            if (((Short) obj).shortValue() != ((Short) obj2).shortValue()) {
                z5 = false;
            }
            return z5;
        } else if (obj instanceof Integer) {
            if (((Integer) obj).intValue() != ((Integer) obj2).intValue()) {
                z4 = false;
            }
            return z4;
        } else if (obj instanceof Long) {
            if (((Long) obj).longValue() != ((Long) obj2).longValue()) {
                z3 = false;
            }
            return z3;
        } else if (obj instanceof Float) {
            double abs = (double) Math.abs(((Float) obj).floatValue() - ((Float) obj2).floatValue());
            if (abs <= -1.0E-9d || abs >= b) {
                z2 = false;
            }
            return z2;
        } else if (!(obj instanceof Double)) {
            return obj instanceof String ? ((String) obj).equals((String) obj2) : obj instanceof C0705O00000oo ? ((C0705O00000oo) obj).O00000oO((C0705O00000oo) obj2) : obj.equals(obj2);
        } else {
            double abs2 = Math.abs(((Double) obj).doubleValue() - ((Double) obj2).doubleValue());
            if (abs2 <= -1.0E-9d || abs2 >= b) {
                z = false;
            }
            return z;
        }
    }

    public static boolean O000000o(int[] iArr, int i) {
        if (iArr == null || iArr.length <= 0) {
            return false;
        }
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public static boolean O000000o(Object[] objArr, Object obj) {
        if (objArr == null || objArr.length <= 0) {
            return false;
        }
        for (Object O000000o2 : objArr) {
            if (O000000o(O000000o2, obj)) {
                return true;
            }
        }
        return false;
    }

    public static int[] O000000o(int[] iArr) {
        if (iArr != null && iArr.length > 1) {
            int length = iArr.length;
            int i = 0;
            while (i < length - 1) {
                int i2 = i + 1;
                int i3 = i;
                for (int i4 = i2; i4 < length; i4++) {
                    if (iArr[i3] > iArr[i4]) {
                        i3 = i4;
                    }
                }
                if (i3 != i) {
                    int i5 = iArr[i];
                    iArr[i] = iArr[i3];
                    iArr[i3] = i5;
                }
                i = i2;
            }
        }
        return iArr;
    }

    public static Object[] O000000o(Object[] objArr, Class cls) {
        HashSet hashSet = new HashSet();
        for (Object add : objArr) {
            hashSet.add(add);
        }
        return hashSet.toArray((Object[]) Array.newInstance(cls, hashSet.size()));
    }

    public static Object[] O000000o(Object[] objArr, Object obj, Class cls) {
        if (!O000000o(objArr, obj)) {
            return objArr;
        }
        int length = objArr.length;
        if (length == 1) {
            if (objArr[0] == obj) {
                objArr = (Object[]) Array.newInstance(cls, 0);
            }
            return objArr;
        }
        int[] O00000o02 = O00000o0(objArr, obj);
        if (O00000o02 == null || O00000o02.length <= 0) {
            return objArr;
        }
        Object[] objArr2 = (Object[]) Array.newInstance(cls, length - O00000o02.length);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!O000000o(O00000o02, i2)) {
                objArr2[i] = objArr[i2];
                i++;
            }
        }
        return objArr2;
    }

    public static Object[] O000000o(Object[] objArr, Object[]... objArr2) {
        int length = objArr.length;
        for (Object[] length2 : objArr2) {
            length += length2.length;
        }
        Object[] copyOf = Arrays.copyOf(objArr, length);
        int length3 = objArr.length;
        int i = length3;
        for (Object[] objArr3 : objArr2) {
            System.arraycopy(objArr3, 0, copyOf, i, objArr3.length);
            i += objArr3.length;
        }
        return copyOf;
    }

    public static int O00000Oo(Object[] objArr, Object obj) {
        int i = -1;
        if (objArr == null) {
            return -1;
        }
        int length = objArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            } else if (O000000o(obj, objArr[i2])) {
                i = i2;
                break;
            } else {
                i2++;
            }
        }
        return i;
    }

    public static void O00000Oo(Object[] objArr) {
        if (objArr != null && objArr.length != 0 && objArr.length != 1) {
            int length = objArr.length;
            int i = length / 2;
            for (int i2 = 0; i2 < i; i2++) {
                Object obj = objArr[i2];
                int i3 = (length - i2) - 1;
                objArr[i2] = objArr[i3];
                objArr[i3] = obj;
            }
        }
    }

    public static String O00000o0(Object[] objArr) {
        return Arrays.toString(objArr);
    }

    public static int[] O00000o0(Object[] objArr, Object obj) {
        if (!O000000o(objArr, obj)) {
            return new int[0];
        }
        int length = objArr.length;
        int[] iArr = new int[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (O000000o(objArr[i2], obj)) {
                iArr[i] = i2;
                i++;
            }
        }
        if (i == 0) {
            return new int[0];
        }
        int[] iArr2 = new int[i];
        System.arraycopy(iArr, 0, iArr2, 0, i);
        return iArr2;
    }

    public static void a(List list) {
        Collections.sort(list);
    }
}
