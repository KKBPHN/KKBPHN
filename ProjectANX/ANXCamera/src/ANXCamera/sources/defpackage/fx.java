package defpackage;

import tv.danmaku.ijk.media.player.IjkMediaMeta;

/* renamed from: fx reason: default package */
final class fx {
    public static final ft a;

    static {
        ft fwVar = (fr.b && fr.a && !bx.a()) ? new fw() : new fu();
        a = fwVar;
    }

    public static int O000000o(int i, int i2, int i3) {
        if (i > -12 || i2 > -65 || i3 > -65) {
            return -1;
        }
        return (i ^ (i2 << 8)) ^ (i3 << 16);
    }

    static int O000000o(CharSequence charSequence) {
        int length = charSequence.length();
        int i = 0;
        int i2 = 0;
        while (i2 < length && charSequence.charAt(i2) < 128) {
            i2++;
        }
        int i3 = length;
        while (true) {
            if (i2 >= length) {
                break;
            }
            char charAt = charSequence.charAt(i2);
            if (charAt < 2048) {
                i3 += (127 - charAt) >>> 31;
                i2++;
            } else {
                int length2 = charSequence.length();
                while (i2 < length2) {
                    char charAt2 = charSequence.charAt(i2);
                    if (charAt2 < 2048) {
                        i += (127 - charAt2) >>> 31;
                    } else {
                        i += 2;
                        if (charAt2 >= 55296 && charAt2 <= 57343) {
                            if (Character.codePointAt(charSequence, i2) >= 65536) {
                                i2++;
                            } else {
                                throw new fv(i2, length2);
                            }
                        }
                    }
                    i2++;
                }
                i3 += i;
            }
        }
        if (i3 >= length) {
            return i3;
        }
        StringBuilder sb = new StringBuilder(54);
        sb.append("UTF-8 length does not fit in int: ");
        sb.append(((long) i3) + IjkMediaMeta.AV_CH_WIDE_RIGHT);
        throw new IllegalArgumentException(sb.toString());
    }

    static int O000000o(CharSequence charSequence, byte[] bArr, int i, int i2) {
        return a.O000000o(charSequence, bArr, i, i2);
    }

    public static boolean O000000o(byte[] bArr, int i, int i2) {
        return a.O000000o(bArr, i, i2);
    }

    public static int O00000o(byte[] bArr, int i, int i2) {
        byte b = bArr[i - 1];
        int i3 = i2 - i;
        if (i3 == 0) {
            return a(b);
        }
        if (i3 == 1) {
            return a(b, bArr[i]);
        }
        if (i3 == 2) {
            return O000000o((int) b, (int) bArr[i], (int) bArr[i + 1]);
        }
        throw new AssertionError();
    }

    public static int a(int i) {
        if (i > -12) {
            return -1;
        }
        return i;
    }

    public static int a(int i, int i2) {
        if (i > -12 || i2 > -65) {
            return -1;
        }
        return i ^ (i2 << 8);
    }
}
