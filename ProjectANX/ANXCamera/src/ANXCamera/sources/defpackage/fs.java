package defpackage;

/* renamed from: fs reason: default package */
final class fs {
    public static void O000000o(byte b, byte b2, byte b3, byte b4, char[] cArr, int i) {
        if (O00000o(b2) || (((b << 28) + (b2 + 112)) >> 30) != 0 || O00000o(b3) || O00000o(b4)) {
            throw dl.f();
        }
        int O00000oO = ((b & 7) << 18) | (O00000oO(b2) << 12) | (O00000oO(b3) << 6) | O00000oO(b4);
        cArr[i] = (char) ((O00000oO >>> 10) + 55232);
        cArr[i + 1] = (char) ((O00000oO & 1023) + 56320);
    }

    public static void O000000o(byte b, byte b2, byte b3, char[] cArr, int i) {
        if (O00000o(b2) || ((b == -32 && b2 < -96) || ((b == -19 && b2 >= -96) || O00000o(b3)))) {
            throw dl.f();
        }
        cArr[i] = (char) (((b & 15) << 12) | (O00000oO(b2) << 6) | O00000oO(b3));
    }

    public static void O000000o(byte b, byte b2, char[] cArr, int i) {
        if (b < -62 || O00000o(b2)) {
            throw dl.f();
        }
        cArr[i] = (char) (((b & 31) << 6) | O00000oO(b2));
    }

    public static void O000000o(byte b, char[] cArr, int i) {
        cArr[i] = (char) b;
    }

    public static boolean O000000o(byte b) {
        return b >= 0;
    }

    public static boolean O00000Oo(byte b) {
        return b < -32;
    }

    private static boolean O00000o(byte b) {
        return b > -65;
    }

    public static boolean O00000o0(byte b) {
        return b < -16;
    }

    private static int O00000oO(byte b) {
        return b & 63;
    }

    public static Object O00000oO(Object obj) {
        return ((de) obj).b(4);
    }
}
