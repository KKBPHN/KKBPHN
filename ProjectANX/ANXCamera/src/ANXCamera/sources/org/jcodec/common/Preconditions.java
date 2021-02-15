package org.jcodec.common;

public final class Preconditions {
    private Preconditions() {
    }

    public static Object checkNotNull(Object obj) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException();
    }

    public static Object checkNotNull(Object obj, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(String.valueOf(obj2));
    }

    public static Object checkNotNull(Object obj, String str, char c) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Character.valueOf(c)));
    }

    public static Object checkNotNull(Object obj, String str, char c, char c2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Character.valueOf(c), Character.valueOf(c2)));
    }

    public static Object checkNotNull(Object obj, String str, char c, int i) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Character.valueOf(c), Integer.valueOf(i)));
    }

    public static Object checkNotNull(Object obj, String str, char c, long j) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Character.valueOf(c), Long.valueOf(j)));
    }

    public static Object checkNotNull(Object obj, String str, char c, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Character.valueOf(c), obj2));
    }

    public static Object checkNotNull(Object obj, String str, int i) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Integer.valueOf(i)));
    }

    public static Object checkNotNull(Object obj, String str, int i, char c) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Integer.valueOf(i), Character.valueOf(c)));
    }

    public static Object checkNotNull(Object obj, String str, int i, int i2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Integer.valueOf(i), Integer.valueOf(i2)));
    }

    public static Object checkNotNull(Object obj, String str, int i, long j) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Integer.valueOf(i), Long.valueOf(j)));
    }

    public static Object checkNotNull(Object obj, String str, int i, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Integer.valueOf(i), obj2));
    }

    public static Object checkNotNull(Object obj, String str, long j) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Long.valueOf(j)));
    }

    public static Object checkNotNull(Object obj, String str, long j, char c) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Long.valueOf(j), Character.valueOf(c)));
    }

    public static Object checkNotNull(Object obj, String str, long j, int i) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Long.valueOf(j), Integer.valueOf(i)));
    }

    public static Object checkNotNull(Object obj, String str, long j, long j2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Long.valueOf(j), Long.valueOf(j2)));
    }

    public static Object checkNotNull(Object obj, String str, long j, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, Long.valueOf(j), obj2));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, char c) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, Character.valueOf(c)));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, int i) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, Integer.valueOf(i)));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, long j) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, Long.valueOf(j)));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, Object obj3) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, obj3));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, Object obj3, Object obj4) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, obj3, obj4));
    }

    public static Object checkNotNull(Object obj, String str, Object obj2, Object obj3, Object obj4, Object obj5) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, obj2, obj3, obj4, obj5));
    }

    public static Object checkNotNull(Object obj, String str, Object... objArr) {
        if (obj != null) {
            return obj;
        }
        throw new NullPointerException(format(str, objArr));
    }

    public static void checkState(boolean z) {
        if (!z) {
            throw new IllegalStateException();
        }
    }

    public static void checkState(boolean z, Object obj) {
        if (!z) {
            throw new IllegalStateException(String.valueOf(obj));
        }
    }

    public static void checkState(boolean z, String str, char c) {
        if (!z) {
            throw new IllegalStateException(format(str, Character.valueOf(c)));
        }
    }

    public static void checkState(boolean z, String str, char c, char c2) {
        if (!z) {
            throw new IllegalStateException(format(str, Character.valueOf(c), Character.valueOf(c2)));
        }
    }

    public static void checkState(boolean z, String str, char c, int i) {
        if (!z) {
            throw new IllegalStateException(format(str, Character.valueOf(c), Integer.valueOf(i)));
        }
    }

    public static void checkState(boolean z, String str, char c, long j) {
        if (!z) {
            throw new IllegalStateException(format(str, Character.valueOf(c), Long.valueOf(j)));
        }
    }

    public static void checkState(boolean z, String str, char c, Object obj) {
        if (!z) {
            throw new IllegalStateException(format(str, Character.valueOf(c), obj));
        }
    }

    public static void checkState(boolean z, String str, int i) {
        if (!z) {
            throw new IllegalStateException(format(str, Integer.valueOf(i)));
        }
    }

    public static void checkState(boolean z, String str, int i, char c) {
        if (!z) {
            throw new IllegalStateException(format(str, Integer.valueOf(i), Character.valueOf(c)));
        }
    }

    public static void checkState(boolean z, String str, int i, int i2) {
        if (!z) {
            throw new IllegalStateException(format(str, Integer.valueOf(i), Integer.valueOf(i2)));
        }
    }

    public static void checkState(boolean z, String str, int i, long j) {
        if (!z) {
            throw new IllegalStateException(format(str, Integer.valueOf(i), Long.valueOf(j)));
        }
    }

    public static void checkState(boolean z, String str, int i, Object obj) {
        if (!z) {
            throw new IllegalStateException(format(str, Integer.valueOf(i), obj));
        }
    }

    public static void checkState(boolean z, String str, long j) {
        if (!z) {
            throw new IllegalStateException(format(str, Long.valueOf(j)));
        }
    }

    public static void checkState(boolean z, String str, long j, char c) {
        if (!z) {
            throw new IllegalStateException(format(str, Long.valueOf(j), Character.valueOf(c)));
        }
    }

    public static void checkState(boolean z, String str, long j, int i) {
        if (!z) {
            throw new IllegalStateException(format(str, Long.valueOf(j), Integer.valueOf(i)));
        }
    }

    public static void checkState(boolean z, String str, long j, long j2) {
        if (!z) {
            throw new IllegalStateException(format(str, Long.valueOf(j), Long.valueOf(j2)));
        }
    }

    public static void checkState(boolean z, String str, long j, Object obj) {
        if (!z) {
            throw new IllegalStateException(format(str, Long.valueOf(j), obj));
        }
    }

    public static void checkState(boolean z, String str, Object obj) {
        if (!z) {
            throw new IllegalStateException(format(str, obj));
        }
    }

    public static void checkState(boolean z, String str, Object obj, char c) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, Character.valueOf(c)));
        }
    }

    public static void checkState(boolean z, String str, Object obj, int i) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, Integer.valueOf(i)));
        }
    }

    public static void checkState(boolean z, String str, Object obj, long j) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, Long.valueOf(j)));
        }
    }

    public static void checkState(boolean z, String str, Object obj, Object obj2) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, obj2));
        }
    }

    public static void checkState(boolean z, String str, Object obj, Object obj2, Object obj3) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, obj2, obj3));
        }
    }

    public static void checkState(boolean z, String str, Object obj, Object obj2, Object obj3, Object obj4) {
        if (!z) {
            throw new IllegalStateException(format(str, obj, obj2, obj3, obj4));
        }
    }

    public static void checkState(boolean z, String str, Object... objArr) {
        if (!z) {
            throw new IllegalStateException(format(str, objArr));
        }
    }

    static String format(String str, Object... objArr) {
        if (objArr != null) {
            String valueOf = String.valueOf(str);
            StringBuilder sb = new StringBuilder(valueOf.length() + (objArr.length * 16));
            int i = 0;
            int i2 = 0;
            while (i < objArr.length) {
                int indexOf = valueOf.indexOf("%s", i2);
                if (indexOf == -1) {
                    break;
                }
                sb.append(valueOf, i2, indexOf);
                int i3 = i + 1;
                sb.append(objArr[i]);
                int i4 = i3;
                i2 = indexOf + 2;
                i = i4;
            }
            sb.append(valueOf, i2, valueOf.length());
            if (i < objArr.length) {
                sb.append(" [");
                int i5 = i + 1;
                sb.append(objArr[i]);
                while (i5 < objArr.length) {
                    sb.append(", ");
                    int i6 = i5 + 1;
                    sb.append(objArr[i5]);
                    i5 = i6;
                }
                sb.append(']');
            }
            return sb.toString();
        }
        throw new NullPointerException();
    }
}
