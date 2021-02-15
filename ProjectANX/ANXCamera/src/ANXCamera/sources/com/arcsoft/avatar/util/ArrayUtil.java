package com.arcsoft.avatar.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ArrayUtil {
    private static final String a = "ArrayUtil";
    private static final double b = 1.0E-9d;

    private ArrayUtil() {
    }

    public static String array2String(Object[] objArr) {
        return Arrays.toString(objArr);
    }

    public static int getIndex(Object[] objArr, Object obj) {
        int i = -1;
        if (objArr == null) {
            return -1;
        }
        int length = objArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            } else if (isEqual(obj, objArr[i2])) {
                i = i2;
                break;
            } else {
                i2++;
            }
        }
        return i;
    }

    public static int[] getIndices(Object[] objArr, Object obj) {
        if (!hasElementInArray(objArr, obj)) {
            return new int[0];
        }
        int length = objArr.length;
        int[] iArr = new int[length];
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (isEqual(objArr[i2], obj)) {
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

    public static boolean hasElementInArray(int[] iArr, int i) {
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

    public static boolean hasElementInArray(Object[] objArr, Object obj) {
        if (objArr == null || objArr.length <= 0) {
            return false;
        }
        for (Object isEqual : objArr) {
            if (isEqual(isEqual, obj)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEqual(Object obj, Object obj2) {
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
            return obj instanceof String ? ((String) obj).equals((String) obj2) : obj.equals(obj2);
        } else {
            double abs2 = Math.abs(((Double) obj).doubleValue() - ((Double) obj2).doubleValue());
            if (abs2 <= -1.0E-9d || abs2 >= b) {
                z = false;
            }
            return z;
        }
    }

    public static Object[] mergeAll(Object[] objArr, Object[]... objArr2) {
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

    public static Object[] removeArrayElementsByValue(Object[] objArr, Object obj, Class cls) {
        if (!hasElementInArray(objArr, obj)) {
            return objArr;
        }
        int length = objArr.length;
        if (length == 1) {
            if (objArr[0] == obj) {
                objArr = (Object[]) Array.newInstance(cls, 0);
            }
            return objArr;
        }
        int[] indices = getIndices(objArr, obj);
        if (indices == null || indices.length <= 0) {
            return objArr;
        }
        Object[] objArr2 = (Object[]) Array.newInstance(cls, length - indices.length);
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            if (!hasElementInArray(indices, i2)) {
                objArr2[i] = objArr[i2];
                i++;
            }
        }
        return objArr2;
    }

    public static Object[] removeRedundantElement(Object[] objArr, Class cls) {
        HashSet hashSet = new HashSet();
        for (Object add : objArr) {
            hashSet.add(add);
        }
        return hashSet.toArray((Object[]) Array.newInstance(cls, hashSet.size()));
    }

    public static void reverse(Object[] objArr) {
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

    public static int[] selectSort(int[] iArr) {
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

    public static void sort(List list) {
        Collections.sort(list);
    }

    public static void sort(Comparable[] comparableArr) {
        Arrays.sort(comparableArr);
    }
}
